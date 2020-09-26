package com.tc.utils.utils.helpers

import android.content.Context

import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.tc.utils.R
import com.tc.utils.utils.customs.RoundedCornersTransformation
import com.tc.utils.variables.abstracts.OnEventOccurListener
import com.tc.utils.variables.enums.ActionType
import com.tc.utils.variables.interfaces.ApiKeys


class HelperDialogCommon {


    fun dialogHelper(view: View, activity: Context): AlertDialog {
        val alertDialog = AlertDialog.Builder(activity).create()
        alertDialog.setCancelable(true)
        val window = alertDialog.window
        if (window != null) {
            window.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
            window.attributes.windowAnimations = R.style.DialogAnimation
        }
        alertDialog.setCanceledOnTouchOutside(false)
        alertDialog.setView(view)
        alertDialog.show()
        return alertDialog
    }

    fun popupWindowOverflow(layout: View?, activity: Context, viewPoint: View): PopupWindow {
        val location = IntArray(2)
        viewPoint.getLocationOnScreen(location)
        val point = Point()
        point.x = location[0]
        point.y = location[1]
        val popupWindow = PopupWindow(activity)
        popupWindow.contentView = layout
        popupWindow.width = LinearLayout.LayoutParams.WRAP_CONTENT
        popupWindow.height = LinearLayout.LayoutParams.WRAP_CONTENT
        popupWindow.isFocusable = true
//        popupWindow.setBackgroundDrawable(BitmapDrawable())
        popupWindow.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        popupWindow.showAtLocation(layout, Gravity.NO_GRAVITY, point.x - 80, point.y)
        return popupWindow
    }

    fun startDialogInfo(title: String?, message: String?, context: Context?) {
        val view: View = LayoutInflater.from(context).inflate(R.layout.dialog_info, null)
        val alertDialog = HelperDialogCommon().dialogHelper(view, context!!)
        val textView = view.findViewById<TextView>(R.id.id_text_title)
        textView.text = title
        alertDialog.setCancelable(true)
        alertDialog.setCanceledOnTouchOutside(true)
        val textViewDesc = view.findViewById<TextView>(R.id.id_text_description)
        textViewDesc.visibility = View.VISIBLE
        textViewDesc.text = message
        view.findViewById<View>(R.id.id_button_dismiss).setOnClickListener { alertDialog.dismiss() }
        view.findViewById<View>(R.id.id_image_cancel).setOnClickListener { alertDialog.dismiss() }
    }

//    fun startDialogInfo(
//            title: String?,
//            message: String?,
//            videoLink: String,
//            context: Context?,
//            listener: OnEventOccurListener
//    ) {
//        val view: View = LayoutInflater.from(context).inflate(R.layout.dialog_info, null)
//        val alertDialog = HelperDialogCommon().dialogHelper(view, context!!)
//        val textView = view.findViewById<TextView>(R.id.id_text_title)
//        textView.text = title
//        alertDialog.setCancelable(true)
//        alertDialog.setCanceledOnTouchOutside(true)
//        val textViewDesc = view.findViewById<TextView>(R.id.id_text_description)
//        if (message.isNullOrEmpty()) {
//            textViewDesc.visibility = View.GONE
//        } else {
//            textViewDesc.visibility = View.VISIBLE
//            textViewDesc.text = message
//        }
//        view.findViewById<View>(R.id.id_parent_play).visibility = View.VISIBLE
//        Glide.with(context)
//                .load(ApiKeys.URL_VIDEO_THUMBNAILS + videoLink + ApiKeys.URL_VIDEO_THUMBNAILS_LAST)
//                .transform(
//                        RoundedCornersTransformation(
//                                context,
//                                view.findViewById(R.id.id_image),
//                                16,
//                                0,
//                                RoundedCornersTransformation.CornerType.ALL
//                        )
//                )
//                .centerCrop()
//                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
//                .placeholder(R.drawable.placeholder_one)
//                .into(view.findViewById(R.id.id_image))
//        view.findViewById<View>(R.id.id_button_dismiss).setOnClickListener { alertDialog.dismiss() }
//        view.findViewById<View>(R.id.id_image_cancel).setOnClickListener { alertDialog.dismiss() }
//        view.findViewById<View>(R.id.id_parent_play).setOnClickListener {
//            listener.getEventData(ActionType.ACTION_EXPLORE)
//            alertDialog.dismiss()
//        }
//    }
}
