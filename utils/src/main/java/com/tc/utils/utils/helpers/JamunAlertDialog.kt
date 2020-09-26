package com.tc.utils.utils.helpers

import android.app.Activity

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.tc.utils.R
import com.tc.utils.variables.interfaces.JamunDialogInterface


/**
 * Jamun Alert Dialog provide you Some Custom functions and Utilities for Your Easiness
 */
class JamunAlertDialog
/**
 * Constructor used for required things
 *
 * @param activity your View Context
 */
    (private val activity: Activity) {
    private var alertDialog: AlertDialog? = null
    private var posButton: Button? = null
    private var negButton: Button? = null
    private var negOnClickListener: JamunDialogInterface.OnClickListener? = null
    private var posOnClickListener: JamunDialogInterface.OnClickListener? = null
    private var view: View? = null

    init {
        createAlertDialog()
    }

    /**
     * Method make your Dialog cancelable behavior on Back Key Press
     *
     * @param value True/False for there behavior
     * @return return Class Object
     */
    fun setCancelable(value: Boolean): JamunAlertDialog {
        alertDialog!!.setCancelable(value)
        return this
    }

    /**
     * Method make your Dialog cancelable behavior on Outside Touches
     *
     * @param value True/False for there behavior
     * @return return Class Object
     */
    fun setCancelableOnTouch(value: Boolean): JamunAlertDialog {
        alertDialog!!.setCanceledOnTouchOutside(value)
        return this
    }

    /**
     * Method give Title's to your Dialog
     *
     * @param title Title of your Dialog if you want to Show
     * @return return Class Object
     */
    fun setTitle(title: String): JamunAlertDialog {
        helperDialogTitle(title)
        return this
    }

    private fun helperDialogTitle(title: String) {
        val titleTextView = view!!.findViewById<TextView>(R.id.id_text_message)
        titleTextView.visibility = View.VISIBLE
        titleTextView.text = title
    }

    /**
     * Method give Title's to your Dialog
     *
     * @param title Title of your Dialog if you want to Show
     * @return return Class Object
     */
    fun setTitle(title: Int): JamunAlertDialog {
        helperDialogTitle(activity.getString(title))
        return this
    }

    /**
     * Method give Message to your Dialog
     *
     * @param message Message of your Dialog make this Important
     * @return return Class Object
     */
    fun setMessage(message: String): JamunAlertDialog {
        helperMessage(message)
        return this
    }

    private fun helperMessage(message: String) {
        val messageTextView = view!!.findViewById<TextView>(R.id.id_text_message)
        messageTextView.visibility = View.VISIBLE
        messageTextView.text = message
    }

    /**
     * Method give Message to your Dialog
     *
     * @param message Message of your Dialog make this Important
     * @return return Class Object
     */
    fun setMessage(message: Int): JamunAlertDialog {
        helperMessage(activity.getString(message))
        return this
    }

    /**
     * Method call when need to Show Dialog
     */
    fun show() {
        alertDialog!!.show()
    }

    /**
     * Method call when need to Dismiss Dialog
     */
    fun dismiss() {
        alertDialog!!.dismiss()
    }

    /**
     * Method Listen Positive action button
     *
     * @param name            Need button name
     * @param onClickListener Listener provide you Reply when action call
     * @return Class Object
     */
    fun setPositiveButton(name: String, onClickListener: JamunDialogInterface.OnClickListener): JamunAlertDialog {
        helperPos(name, onClickListener)
        return this
    }

    private fun helperPos(name: String, onClickListener: JamunDialogInterface.OnClickListener) {
        posButton!!.visibility = View.VISIBLE
        posButton!!.text = name
        this.posOnClickListener = onClickListener
    }

    /**
     * Method Listen Positive action button
     *
     * @param name            Need button name
     * @param onClickListener Listener provide you Reply when action call
     * @return Class Object
     */
    fun setPositiveButton(name: Int, onClickListener: JamunDialogInterface.OnClickListener): JamunAlertDialog {
        helperPos(activity.getString(name), onClickListener)
        return this
    }

    /**
     * Method Listen Negative action button
     *
     * @param name            Need button name
     * @param onClickListener Listener provide you Reply when action call
     * @return Class Object
     */
    fun setNegativeButton(name: String, onClickListener: JamunDialogInterface.OnClickListener): JamunAlertDialog {
        helperNeg(name, onClickListener)
        return this
    }

    /**
     * Method Listen Negative action button
     *
     * @param name            Need button name
     * @param onClickListener Listener provide you Reply when action call
     * @return Class Object
     */
    fun setNegativeButton(name: Int, onClickListener: JamunDialogInterface.OnClickListener): JamunAlertDialog {
        helperNeg(activity.getString(name), onClickListener)
        return this
    }

    private fun helperNeg(name: String, onClickListener: JamunDialogInterface.OnClickListener) {
        negButton!!.visibility = View.VISIBLE
        negButton!!.text = name
        this.negOnClickListener = onClickListener
    }

    private fun createAlertDialog() {
        val layoutInflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        view = layoutInflater.inflate(R.layout.library_dialog_view, null)
        alertDialog = AlertDialog.Builder(activity).create()
        posButton = view!!.findViewById(R.id.id_button_pos)
        alertDialog!!.setView(view)
        posButton!!.setOnClickListener { posOnClickListener!!.onClick(this@JamunAlertDialog) }
        negButton = view!!.findViewById(R.id.id_button_neg)
        negButton!!.setOnClickListener { negOnClickListener!!.onClick(this@JamunAlertDialog) }

    }

    /**
     * Utility Method used to make Two calls in One for Giving Cancellable Behavior
     *
     * @return Class Object
     */
    fun setAutoCancelable(): JamunAlertDialog {
        setCancelable(true)
        setCancelableOnTouch(true)
        return this
    }

    /**
     * Utility Method used to make Negative Button Auto Behavior no need of Your Code
     *
     * @return Class Object
     */
    fun setAutoNegativeButton(name: Int): JamunAlertDialog {
        setNegativeButton(name,
            JamunDialogInterface.OnClickListener { jamunAlertDialog -> jamunAlertDialog.dismiss() })
        return this
    }
}
