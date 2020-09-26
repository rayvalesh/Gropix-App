package com.tc.utils.utils.helpers

import android.content.Context

import android.graphics.Point
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatImageView
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.tc.utils.R
import com.tc.utils.utils.customs.RoundedCornersTransformation
import com.tc.utils.variables.abstracts.OnEventOccurListener
import com.tc.utils.variables.enums.ActionType
import com.tc.utils.variables.interfaces.ApiKeys


object Utils {
    /**
     * [Toast] function for ease
     */
    fun toast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    /**
     * [Log] function for ease
     *
     * @param key Unique identity key
     * @param message any object for console
     *
     * @author Jatin Sahgal
     */
    fun log(message: Any?, key: String? = "Jamun") {
        Log.d(key, message.toString())
    }
    /**
     * Set visibility of a View
     *
     * @param view Generic implementation for any kind of [View]
     * @param isVisible true for Visible, false for GONE
     * @author Jatin Sahgal
     */
    fun setVisibility(view: View, isVisible: Boolean) {
        view.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    /**
     * Helper function creating or managing default values for [AlertDialog]
     *
     * @param view Root [View] example R.layout.dialog_video_feedback
     * @param context [Context] of View
     * @author Jatin Sahgal
     */
    fun helperDialog(view: View, activity: Context): AlertDialog {
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

    fun popupWindowOverflow(layout: View, activity: Context, viewPoint: View): PopupWindow {
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
        popupWindow.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        popupWindow.showAtLocation(layout, Gravity.NO_GRAVITY, point.x - 80, point.y)
        return popupWindow
    }

    fun startDialogInfo(title: String?, message: String?, context: Context?) {
        val view: View = LayoutInflater.from(context).inflate(R.layout.dialog_info, null)
        val alertDialog = helperDialog(view, context!!)
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

}
