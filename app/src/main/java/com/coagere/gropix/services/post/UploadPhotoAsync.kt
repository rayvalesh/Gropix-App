//package com.coagere.gropix.services.post
//
//import android.os.AsyncTask
//import com.android.volley.Request
//import com.android.volley.toolbox.RequestFuture
//import com.coagere.gropix.utils.RetrofitUtil
//import com.tc.utils.utils.helpers.Utils
//import com.tc.utils.variables.abstracts.OnEventOccurListener
//import com.tc.utils.variables.enums.ResponseTypes
//import com.tc.utils.variables.interfaces.ApiKeys
//import okhttp3.MediaType
//import okhttp3.MediaType.Companion.toMediaTypeOrNull
//import okhttp3.MultipartBody
//import okhttp3.RequestBody
//import org.json.JSONObject
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//import tk.jamun.volley.classes.VolleyJsonObjectRequest
//import tk.jamun.volley.helpers.VolleyErrorExceptions
//import tk.jamun.volley.helpers.VolleyNeeds
//import tk.jamun.volley.helpers.VolleyValues
//import java.io.File
//import java.io.FileInputStream
//import java.io.InputStream
//import java.net.HttpURLConnection
//import java.util.concurrent.ExecutionException
//import java.util.concurrent.TimeUnit
//import java.util.concurrent.TimeoutException
//
//
//class UploadPhotoAsync(
//    private val fileModel: FileModel,
//    private val listener: OnEventOccurListener
//) :
//    AsyncTask<Void?, Void?, Boolean>() {
//    private lateinit var connection: HttpURLConnection
//    private var url: String? = null
//
//    override fun doInBackground(vararg params: Void?): Boolean {
//        val requestFuture = RequestFuture.newFuture<JSONObject>()
//        val volleyStringRequest = VolleyJsonObjectRequest(
//            Request.Method.GET, ApiKeys.URL_POST_UPLOAD_PROFILE_PHOTO, null,
//            requestFuture, requestFuture
//        )
//        VolleyNeeds.get().addCalls(volleyStringRequest)
//        val urlResponse: JSONObject?
//        try {
//            urlResponse =
//                requestFuture[VolleyValues.get().defaultRequestTimeMax.toLong(), TimeUnit.MILLISECONDS]
//        } catch (e: InterruptedException) {
//            return false
//        } catch (e: ExecutionException) {
//            return false
//        } catch (e: TimeoutException) {
//            return false
//        }
//        if (urlResponse != null) {
//            url = urlResponse.getString("url");
//            return uploadFileFrom(url!!, File(fileModel.fileUrl))
//        }
//        return false
//    }
//
//    private fun uploadFileFrom(url: String, file: File): Boolean {
//        val `in`: InputStream = FileInputStream(File(file.path))
//        val buf: ByteArray
//        buf = ByteArray(`in`.available())
//        while (`in`.read(buf) !== -1);
//        val requestBody = RequestBody
//            .create("application/octet-stream".toMediaTypeOrNull(), buf)
//
////        val image = MultipartBody.Part.createFormData(
////            "media",
////            file.name,
////            RequestBody.create(
////                URLConnection.guessContentTypeFromName(file.name).toMediaTypeOrNull(),
////                file
////            )
////        )
//        val call = RetrofitUtil.instance.callRetrofit().uploadBinaryFile(url, requestBody)
//        call.enqueue(object : Callback<Void?> {
//            override fun onResponse(call: Call<Void?>, response: Response<Void?>) {
//                if (response.code() == 200) {
//                    listener.getEventData(url)
//                } else {
//                    listener.onErrorResponse(
//                        ResponseTypes.TYPE_ERROR,
//                        VolleyErrorExceptions.get().VOLLEY_ERROR_STRING_CONNECTION_TIMEOUT
//                    )
//                }
//            }
//
//            override fun onFailure(call: Call<Void?>, t: Throwable) {
//                Utils.log("error " + t.message)
//            }
//        })
//        return false;
//    }
//
//    override fun onPostExecute(result: Boolean?) {
//        super.onPostExecute(result)
//
//    }
//}
