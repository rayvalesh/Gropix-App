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
    private var appCompatImageViewProgress: AppCompatImageView? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null

//    fun setSwipeRefreshLayout(): SwipeRefreshLayout {
//        swipeRefreshLayout = if (view != null)
//            view.findViewById(R.id.id_swipe_refresh)
//        else
//            activity.findViewById(R.id.id_swipe_refresh)
//        swipeRefreshLayout?.setColorSchemeColors(
//            ContextCompat.getColor(activity, R.color.colorSwipeOne),
//            ContextCompat.getColor(activity, R.color.colorSwipeTwo),
//            ContextCompat.getColor(activity, R.color.colorSwipeThree),
//            ContextCompat.getColor(activity, R.color.colorSwipeFour)
//        )
//        return swipeRefreshLayout as SwipeRefreshLayout
//    }

    fun startSwipeRefreshing() {
        swipeRefreshLayout?.isRefreshing = true
    }

    fun setRecyclerView(id: Int, linearLayoutManager: LinearLayoutManager): RecyclerView {
        return setRecyclerView(
            id,
            bottomOffset = false,
            hasFixedSize = false,
            linearLayoutManager = linearLayoutManager
        )
    }

    @JvmOverloads
    fun setRecyclerView(
        id: Int,
        bottomOffset: Boolean = false,
        hasFixedSize: Boolean = false,
        linearLayoutManager: LinearLayoutManager? = LinearLayoutManager(activity)
    ): RecyclerView {
        val recyclerView: RecyclerView = if (isNotNull(view)) {
            view!!.findViewById(id)
        } else {
            activity.findViewById(id)
        }
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.setHasFixedSize(hasFixedSize)
        return recyclerView
    }

    fun startProgressBar() {
        startProgressBarById(R.id.id_progress_bar)
    }

    fun startProgressBarById(id: Int): AppCompatImageView {
        appCompatImageViewProgress = (if (isNotNull(view)) {
            view!!.findViewById(id)
        } else activity.findViewById(id))
        appCompatImageViewProgress!!.visibility = View.VISIBLE
        appCompatImageViewProgress!!.setImageResource(R.drawable.avd_progress_bar)
        swapAnimation(appCompatImageViewProgress!!)
        return appCompatImageViewProgress as AppCompatImageView
    }

    fun closeSwipeRefresh() {
        swipeRefreshLayout?.isRefreshing = false
        closeProgressBar()
    }

    fun closeProgressBar() {
        if (isNotNull(appCompatImageViewProgress)) {
            appCompatImageViewProgress!!.visibility = View.GONE
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

    fun copyToClipboard(tag: String, content: String) {
        val clipboardManager = activity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText(tag, content)
        clipboardManager.setPrimaryClip(clipData)
        L.getInstance().toast(activity, activity.getString(R.string.string_toast_url_copied), TOAST_SUCCESS)
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

    fun setNestedScrollViewDescendant(): NestedScrollView {
        val nestedScrollView: NestedScrollView = if (isNotNull(view)) {
            view!!.findViewById(R.id.id_scroll_view)
        } else {
            activity.findViewById(R.id.id_scroll_view)
        }
        nestedScrollView.descendantFocusability = ViewGroup.FOCUS_BEFORE_DESCENDANTS
        nestedScrollView.isFocusableInTouchMode = true
        return nestedScrollView
    }

    fun setNestedScrollViewDescendant(viewShadow: View): NestedScrollView {
        val nestedScrollView: NestedScrollView = if (isNull(view)) {
            activity.findViewById(R.id.id_scroll_view)
        } else {
            view!!.findViewById(R.id.id_scroll_view)
        }
        nestedScrollView.descendantFocusability = ViewGroup.FOCUS_BEFORE_DESCENDANTS
        nestedScrollView.isFocusableInTouchMode = true
        nestedScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, _ ->
            if (scrollY > 0) {
                viewShadow.visibility = View.VISIBLE
            } else {
                viewShadow.visibility = View.GONE
            }
        })
        return nestedScrollView
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


    fun hideKeyboard(editText: EditText) {
        val imm = activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(editText.windowToken, 0)
    }


    fun showProgressView(
        button: View? = null,
        progressView: AppCompatImageView? = null,
        hideView: Boolean = false,
        vararg views: View
    ) {
        showProgressDialog(button, progressView, hideView)
        if (isNotNull(views))
            for (view in views) {
                view.visibility = View.GONE
            }

    }

    fun showProgressDialog(button: View? = null, progressView: AppCompatImageView? = null, hideView: Boolean = false) {
        if (isNotNull(progressView)) {
            progressView!!.visibility = View.VISIBLE
            progressView.setImageResource(R.drawable.avd_progress_bar)
            swapAnimation(progressView)
        } else {
            val progress = if (isNull(this.view)) {
                this.activity.findViewById(R.id.id_progress_bar)
            } else {
                this.view!!.findViewById<AppCompatImageView>(R.id.id_progress_bar)
            }
            progress!!.visibility = View.VISIBLE
            progress.setImageResource(R.drawable.avd_progress_bar)
            swapAnimation(progress)
        }
        button?.let {
            if (hideView) {
                it.visibility = View.GONE
            }
            it.isEnabled = false
        }
    }


    fun closeProgressDialog(vararg views: View, progressView: AppCompatImageView? = null, hideView: Boolean? = false) {
        if (isNotNull(progressView)) {
            progressView!!.visibility = View.GONE
        } else {
            if (isNull(this.view)) {
                this.activity.findViewById<AppCompatImageView>(R.id.id_progress_bar).visibility = View.GONE
            } else {
                this.view!!.findViewById<AppCompatImageView>(R.id.id_progress_bar).visibility = View.GONE
            }
        }
        for (view: View in views) {
            view.visibility = if (hideView!!) View.GONE else View.VISIBLE
            view.isEnabled = true
        }
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


        fun swapAnimation(imageView: AppCompatImageView) {
            val d = imageView.drawable
            if (Build.VERSION.SDK_INT >= 21 && d is AnimatedVectorDrawable) {
                d.start()
            } else if (d is AnimatedVectorDrawableCompat) {
                d.start()
            }
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
