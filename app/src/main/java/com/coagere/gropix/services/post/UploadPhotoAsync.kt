import android.app.IntentService
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.AsyncTask
import android.os.Bundle
import android.os.ResultReceiver
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.coagere.gropix.jetpack.entities.FileModel
import com.coagere.gropix.utils.ParseJson
import com.tc.utils.receivers.ServiceReceiver
import com.tc.utils.utils.utility.isNotNull
import com.tc.utils.utils.utility.isNullAndEmpty
import com.tc.utils.variables.interfaces.ApiKeys
import com.tc.utils.variables.interfaces.BroadcastKeys
import com.tc.utils.variables.interfaces.Constants
import com.tc.utils.variables.interfaces.Constants.Companion.RESPONSE_PENDING
import com.tc.utils.variables.interfaces.Constants.Companion.RESPONSE_SUCCESS
import com.tc.utils.variables.interfaces.IntentInterface
import com.tc.utils.variables.interfaces.IntentInterface.INTENT_FOR_RECEIVER
import org.json.JSONException
import org.json.JSONObject
import tk.jamun.ui.snacks.L
import tk.jamun.volley.helpers.VolleyNeeds
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

class UploadPhotoAsync : IntentService(UploadPhotoAsync::class.java.simpleName) {
    private var connection: HttpURLConnection? = null
    private var resultReceiver: ResultReceiver? = null
    private var bundle: Bundle? = null
    private var currentIndex = 0
    private var removedIndex = -1
    var broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            AsyncTask.execute {
                if (isNotNull(intent)) {
                    val comeFrom = intent.getIntExtra(IntentInterface.INTENT_COME_FROM, 0)
                    removedIndex = intent.getIntExtra(IntentInterface.INTENT_FOR_POSITION, 0)
                    if (comeFrom == Constants.TYPE_ACTION_UPLOAD) {
                        val fileModel: FileModel =
                            intent.getParcelableExtra(IntentInterface.INTENT_FOR_MODEL)
                        modelList!!.add(fileModel)
                    } else {
                        if (modelList!!.size > removedIndex) {
                            modelList!![removedIndex]?.actionType = Constants.TYPE_ACTION_UPLOAD
                            if (currentIndex == removedIndex) {
                                connection!!.disconnect()
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onHandleIntent(intent: Intent?) {
        if (isNotNull(intent)) {
            isRunning = true
            LocalBroadcastManager.getInstance(this).registerReceiver(
                broadcastReceiver,
                IntentFilter(BroadcastKeys.BROADCAST_RECEIVER_FOR_FEED_SERVICE)
            )
            resultReceiver = intent!!.getParcelableExtra(INTENT_FOR_RECEIVER)
            bundle = Bundle()
            performOperation()
        }
    }

    private fun performOperation() {
        if (!isNullAndEmpty(modelList)) {
            val modelListClear: ArrayList<FileModel?> = ArrayList<FileModel?>(modelList!!)
            for ((index, fileModel) in modelListClear.withIndex()) {
                if (fileModel?.actionType === Constants.TYPE_ACTION_CANCEL) {
                    if (File(fileModel?.fileUrl).exists()) {
                        postFileByApi(fileModel!!, index)
                    } else {
                        fileModel.progress = 0
                        fileModel.actionType = Constants.TYPE_ACTION_UPLOAD
                        bundle!!.putParcelable(IntentInterface.INTENT_FOR_MODEL, fileModel)
                        resultReceiver!!.send(RESPONSE_PENDING, bundle)
                    }
                }
                try {
                    Thread.sleep(Constants.THREAD_TIME_DELAY)
                } catch (ignored: InterruptedException) {
                }
            }
            modelList!!.removeAll(modelListClear)
            performOperation()
        } else {
            resultReceiver!!.send(RESPONSE_SUCCESS, bundle)
        }
        removedIndex = -1
    }

    private fun postFileByApi(fileModel: FileModel, index: Int) {
        currentIndex = index
        val response = uploadFile(File(fileModel.fileUrl), fileModel, index)
        if (isNotNull(response)) {
            try {
                val jsonObject = JSONObject(response)
                if (ParseJson.instance.dataCheck(jsonObject, "url")) {
                    fileModel.progress = 0
                    fileModel.actionType = 0
                    fileModel.progressData = null
                    fileModel.downloadUrl = jsonObject.getString("url")
                    if (index != removedIndex) {
                        bundle!!.putParcelable(IntentInterface.INTENT_FOR_MODEL, fileModel)
                        bundle!!.putInt(IntentInterface.INTENT_FOR_POSITION, index)
                        resultReceiver!!.send(RESPONSE_PENDING, bundle)
                    }
                    return
                }
            } catch (e: JSONException) {
                L.logE(e.message)
            }
        }
        fileModel.progress = 0
        fileModel.actionType = Constants.TYPE_ACTION_UPLOAD
        bundle!!.putParcelable(IntentInterface.INTENT_FOR_MODEL, fileModel)
        resultReceiver!!.send(RESPONSE_PENDING, bundle)
    }

    private fun uploadFile(sourceFile: File, fileModel: FileModel, index: Int): String? {
        HttpURLConnection.setFollowRedirects(false)
        try {
            connection = URL(ApiKeys.URL_POST_IMAGE_UPLOAD).openConnection() as HttpURLConnection
            connection!!.requestMethod = "POST"
            connection!!.doInput = true
            connection!!.doOutput = true
            connection!!.useCaches = false
            val boundary = "---------------------------boundary"
            val tail = lineEnd + "--" + boundary + "--" + lineEnd
            connection!!.setRequestProperty(
                "Content-Type",
                "multipart/form-data; boundary=$boundary"
            )
            for (modelHeader in VolleyNeeds.get().upHeaders) {
                connection!!.setRequestProperty(modelHeader.key, modelHeader.value)
            }
            val metadataPart = """
                --$boundary${lineEnd}Content-Disposition: form-data; name="metadata"
                
                $lineEnd
                """.trimIndent()
            val fileLength = sourceFile.length() + tail.length
            val tag: String = "img"
            val stringData = """
                $metadataPart--$boundary${lineEnd}Content-Disposition: form-data; name=$tag; filename="${sourceFile.name}"
                Content-Type: application/octet-stream${lineEnd}Content-Transfer-Encoding: binary${lineEnd}Content-length: $fileLength$lineEnd$lineEnd
                """.trimIndent()
            val requestLength = stringData.length + fileLength
            connection!!.setRequestProperty("Content-length", "" + requestLength)
            connection!!.setFixedLengthStreamingMode(requestLength.toInt())
            connection!!.connect()
            val out = DataOutputStream(connection!!.outputStream)
            out.writeBytes(stringData)
            out.flush()
            var total = 0
            var maintainTotal: Long = 6
            var bytesRead: Int
            var delayAfterCount = 0
            val fileInputStream = FileInputStream(sourceFile)
            val bufInput = BufferedInputStream(fileInputStream)
            val buf = ByteArray(sourceFile.length().toInt() / 200)
            if (fileLength <= 0) {
                return null
            }
            while (bufInput.read(buf).also { bytesRead = it } != -1) {
                out.write(buf, 0, bytesRead)
                out.flush()
                total += bytesRead
                if (index != removedIndex) {
                    if (delayAfterCount > 4) {
                        calculativeData(
                            total / 1024.toDouble(),
                            (total - maintainTotal) / 1024.toDouble(),
                            fileModel
                        )
                        fileModel.actionType = (Constants.TYPE_ACTION_CANCEL)
                        bundle!!.putParcelable(IntentInterface.INTENT_FOR_MODEL, fileModel)
                        bundle!!.putInt(IntentInterface.INTENT_FOR_POSITION, index)
                        resultReceiver!!.send(RESPONSE_PENDING, bundle)
                        delayAfterCount = 0
                        maintainTotal = total.toLong()
                    }
                    delayAfterCount += 1
                } else {
                    return null
                }
            }
            out.writeBytes(tail)
            out.flush()
            out.close()
        } catch (e: IOException) {
            return null
        }
        try {
            val inStream = BufferedReader(
                InputStreamReader(
                    connection!!.inputStream
                )
            )
            var str: String?
            if (inStream.readLine().also { str = it } != null) {
                inStream.close()
                return str
            }
        } catch (e: IOException) {
            L.logE("" + e.message)
            return null
        } finally {
            if (connection != null) {
                connection!!.disconnect()
            }
        }
        return null
    }

    private fun calculativeData(progress: Double, speed: Double, fileModel: FileModel) {
        var progress = progress
        var downloadType = " Kbps"
        if (progress >= 1024) {
            progress /= 1024
            downloadType = " Mb"
        }
        fileModel.progress = (progress.toInt())
        fileModel.progressData = (String.format("%.2f", progress) + downloadType)
        //        type = " Kbps";
//        if (speed >= 1024) {
//            speed = speed / 1024;
//            type = " Mb";
//        }
//        fileModel.setSpeed(String.format("%.2f", speed) + type);
    }

    override fun onDestroy() {
        super.onDestroy()
        isRunning = false
        if (connection != null) {
            connection!!.disconnect()
        }
        intent = null
        modelList!!.clear()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver)
    }

    companion object {
        var isRunning = false
        private const val lineEnd = "\r\n"
        private var modelList: ArrayList<FileModel?>? = null
        private var intent: Intent? = null
        fun close(context: Context) {
            if (isNotNull(intent)) {
                context.stopService(intent)
                intent = null
            }
        }

        fun start(
            context: Context,
            fileModelArrayList: ArrayList<FileModel>?,
            resultReceiver: ServiceReceiver?
        ) {
            if (isNullAndEmpty(modelList)) {
                modelList = ArrayList<FileModel?>()
            }
            modelList!!.addAll(fileModelArrayList!!)
            if (!isRunning) {
                intent = Intent(
                    Intent.ACTION_SYNC, null, context,
                    UploadPhotoAsync::class.java
                )
                    .putExtra(INTENT_FOR_RECEIVER, resultReceiver)
                    .putParcelableArrayListExtra(
                        IntentInterface.INTENT_FOR_MODEL_LIST,
                        fileModelArrayList
                    )
                context.startService(intent)
            }
        }
    }
}