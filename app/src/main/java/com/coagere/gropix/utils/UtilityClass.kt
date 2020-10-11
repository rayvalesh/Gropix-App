package com.coagere.gropix.utils

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.AnimatedVectorDrawable
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.ParcelFileDescriptor
import android.provider.OpenableColumns
import android.text.InputFilter
import android.text.TextUtils
import android.util.Patterns
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.coagere.gropix.R
import com.coagere.gropix.jetpack.entities.FileModel
import com.coagere.gropix.ui.activities.CameraActivity
import com.tc.utils.utils.helpers.StoragePath
import com.tc.utils.utils.utility.isNotNull
import com.tc.utils.utils.utility.isNull
import com.tc.utils.variables.abstracts.OnEventOccurListener
import org.jetbrains.annotations.Nullable
import tk.jamun.ui.snacks.L
import tk.jamun.ui.snacks.L.TOAST_SUCCESS
import tk.jamun.ui.snacks.MySnackBar
import tk.jamunx.ui.camera.utils.HelperFileFormat
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.util.ArrayList

class UtilityClass(private val activity: Activity, private val view: View? = null) {
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private var progressBar: ProgressBar? = null


    /**
     * Run progress bar animation
     */
    fun startProgressBar(
        button: View? = null,
        progressBar: ProgressBar? = null,
        hideView: Boolean? = false
    ) {
        if (progressBar == null) {
            this.progressBar = if (view != null)
                view.findViewById(R.id.id_progress_bar) else activity.findViewById(R.id.id_progress_bar)
        } else this.progressBar = progressBar

        this.progressBar?.visibility = View.VISIBLE
        button?.let {
            if (hideView!!) it.visibility = View.GONE else it.alpha = 0.5f
            it.isEnabled = false
        }
    }

    fun closeProgressBar(button: View? = null, hideView: Boolean? = false) {
        progressBar?.visibility = View.GONE
        button?.let {
            if (hideView!!) it.visibility = View.GONE else {
                it.visibility = View.VISIBLE
                it.alpha = 1f
            }
            it.isEnabled = true
        }

    }


    fun checkEmailEditTextEmpty(editText: EditText, minLength: Int = 0, errorTextView: TextView? = null): Boolean {
        val message: String? = editText.text.toString()
        if (TextUtils.isEmpty(message)) {
            setErrorMessage(editText, R.string.string_message_cant_empty, errorTextView)
            return true
        } else if (!Patterns.EMAIL_ADDRESS.matcher(message).matches()) {
            setErrorMessage(editText, R.string.string_message_email_incorrect, errorTextView)
            return true
        } else if (message!!.length < activity.resources.getInteger(R.integer.validation_min_email) || message.length > activity.resources.getInteger(R.integer.validation_max_email)) {
            setErrorMessage(editText, R.string.string_message_email_length, errorTextView)
        } else if (isNotNull(message) && (minLength > 0 && message.length < minLength)) {
            setErrorMessage(editText, "Please enter minimum $minLength Characters.", errorTextView)
            return true
        }
        hideErrorMessage(editText, errorTextView)
        return false
    }

    fun hideErrorMessage(editText: EditText?, errorTextView: TextView?) {
        editText?.setTextColor(ContextCompat.getColor(activity, R.color.colorTextSuccess))
        editText?.isSelected = false
        if (isNotNull(errorTextView))
            errorTextView!!.visibility = View.GONE
    }

    fun setErrorMessage(editText: EditText?, errorMessage: String, errorTextView: TextView?) {
        editText?.requestFocus()
        editText?.isSelected = true
        if (isNotNull(errorTextView)) {
            errorTextView!!.visibility = View.VISIBLE
            errorTextView.text = errorMessage
        } else {
            MySnackBar.getInstance().showSnackBarForMessage(activity, errorMessage)
        }
    }

    fun setErrorMessage(editText: EditText?, errorMessage: Int, errorTextView: TextView?) {
        setErrorMessage(editText, activity.getString(errorMessage), errorTextView)
    }

    fun checkEditTextEmpty(
        editText: EditText?,
        minLength: Int = 0, @Nullable errorTextView: TextView? = null
    ): Boolean {
        val message: String = editText?.text.toString()
        if (TextUtils.isEmpty(message)) {
            setErrorMessage(editText, R.string.string_message_cant_empty, errorTextView)
            return true
        } else if (isNotNull(message) && (minLength > 0 && message.length < minLength)) {
            setErrorMessage(editText, "Please enter minimum $minLength Characters.", errorTextView)
            return true
        }
        hideErrorMessage(editText, errorTextView)
        return false
    }


    fun setEditTextMaxLength(editText: EditText?, length: Int) {
        val filterArray = arrayOfNulls<InputFilter>(1)
        filterArray[0] = InputFilter.LengthFilter(length)
        editText?.filters = filterArray
    }

    fun hideSoftKeyboard() {
        try {
            val inputMethodManager = activity.getSystemService(
                Activity.INPUT_METHOD_SERVICE
            ) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(
                activity.currentFocus!!.windowToken, 0
            )
        } catch (ignored: Exception) {
        }

    }

    fun setAdapter(data: Array<String>): ArrayAdapter<String> {
        val adapter = ArrayAdapter(activity, R.layout.spinner_textviews, data)
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown)
        return adapter
    }


    fun isUnAuthrized(`object`: Any? = null): Boolean {
        return if (`object` is Int)
            `object` == 401
        else false
    }

    private val animSlideLeftRight = AnimationUtils.loadAnimation(
        activity,
        R.anim.left_right
    )
    private val animSlideRightLeft = AnimationUtils.loadAnimation(
        activity,
        R.anim.right_left
    )

    fun leftToRight(imageView: AppCompatImageView) {
        animSlideLeftRight.setAnimationListener(object :
            Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
                rightToLeft(imageView)
            }

            override fun onAnimationStart(animation: Animation?) {
            }

        })
        imageView.startAnimation(animSlideLeftRight);
    }

    private fun rightToLeft(imageView: AppCompatImageView) {
        animSlideRightLeft.setAnimationListener(object :
            Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
                leftToRight(imageView)
            }

            override fun onAnimationStart(animation: Animation?) {
            }

        })
        imageView.startAnimation(animSlideRightLeft);
    }


    companion object {
        fun dpToPx(dp: Int): Int {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp.toFloat(),
                Resources.getSystem().displayMetrics
            ).toInt()
        }
    }

    internal class FileAsync(
        private val context: Context,
        private val uris: Array<Uri>,
        private val listeners: OnEventOccurListener
    ) :
        AsyncTask<Void?, Void?, Void?>() {
        private val fileModels: ArrayList<FileModel> = ArrayList<FileModel>()
        private var errorMessage: String? = null
        private fun getBitmapFromUri(context: Context, uri: Uri): Bitmap? {
            try {
                var parcelFileDescriptor: ParcelFileDescriptor? = null
                parcelFileDescriptor = context.contentResolver.openFileDescriptor(uri, "r")
                val fileDescriptor = parcelFileDescriptor!!.fileDescriptor
                val image = BitmapFactory.decodeFileDescriptor(fileDescriptor)
                parcelFileDescriptor.close()
                return image
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return null
        }

        override fun doInBackground(vararg params: Void?): Void? {
            for (uri in uris) {
                val bitmap = getBitmapFromUri(context, uri)
                bitmap?.let { getFileDetails(it, uri) }
            }
            return null
        }

        private fun getFileDetails(image: Bitmap, uri: Uri) {
            context.contentResolver
                .query(uri, null, null, null, null, null).use { cursor ->
                    if (cursor != null && cursor.moveToFirst()) {
                        val displayName = cursor.getString(
                            cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                        )
                        val file: File =
                            StoragePath.createSentImageFile(displayName)
                        HelperFileFormat.getInstance().saveImageIntoMemo(image, file)
                        getFileModelFromUri(file)
                    }
                }
        }

        private fun getFileModelFromUri(file: File) {
            if (file.length() < CameraActivity.FILE_MAX_SIZE) {
                val fileType = file.name.substring(file.name.lastIndexOf(".") + 1)
                if (HelperFileFormat.getInstance().isImageAvail(fileType)) {
                    val fileModel = FileModel()
                    fileModel.fileName = file.name
                    fileModel.fileUrl = file.absolutePath
                    fileModel.fileSize = file.length()
                    fileModel.fileType = fileType
                    fileModels.add(fileModel)
                } else {
                    errorMessage = context.getString(R.string.string_message_error_incorrect_format)
                }
            } else {
                errorMessage = context.getString(R.string.string_message_error_file_size)
            }
        }

        override fun onPostExecute(aVoid: Void?) {
            super.onPostExecute(aVoid)
            if (fileModels.isEmpty()) {
                listeners.onErrorResponse(1, errorMessage)
            } else {
                listeners.getEventData(fileModels.toTypedArray())
            }
        }

    }

    fun getFileModelFromFile(oldFile: File): FileModel? {
        if (oldFile.exists()) {
            val name = oldFile.name.replace("'".toRegex(), "")
            val index = name.lastIndexOf(".")
            val fileType = name.substring(index + 1).toLowerCase()
            if (oldFile.length() < CameraActivity.FILE_MAX_SIZE) {
                if (HelperFileFormat.getInstance().isImageAvail(fileType) || fileType == "pdf") {
                    val fileModel = FileModel()
                    fileModel.fileName = oldFile.name
                    fileModel.fileUrl = oldFile.absolutePath
                    fileModel.fileSize = oldFile.length()
                    fileModel.fileType = fileType
                    return fileModel
                } else {
                    MySnackBar.getInstance().showSnackBarForMessage(
                        activity,
                        R.string.string_message_error_incorrect_format
                    )
                }
            } else {
                MySnackBar.getInstance().showSnackBarForMessage(
                    activity,
                    R.string.string_message_error_file_size
                )
            }
        } else {
            MySnackBar.getInstance().showSnackBarForMessage(
                activity,
                R.string.string_message_error_incorrect_format
            )
        }
        return null
    }

    fun getFileModelFromUri(uri: Array<Uri>, listeners: OnEventOccurListener) {
        FileAsync(activity, uri, listeners).execute()
    }

    fun handleStatusIconColor() {
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO) {
            val window = activity.window
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                window.statusBarColor = Color.WHITE
            }
        }
    }

}
