package com.coagere.gropix.ui.popups

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import com.coagere.gropix.R
import com.tc.utils.utils.helpers.Utils
import com.tc.utils.variables.abstracts.OnEventOccurListener
import com.tc.utils.variables.enums.ActionType

class Popups(private val context: Context, private val listener: OnEventOccurListener) {
    private var popupWindow: PopupWindow? = null

    fun callHomePopup(viewPoint: View) {
        val view: View = LayoutInflater.from(context).inflate(R.layout.popup_home, null)
        popupWindow = Utils.popupWindowOverflow(view, context, viewPoint)
        view.findViewById<LinearLayout>(R.id.id_parent_button_rate).setOnClickListener {
            listener.getEventData(ActionType.ACTION_RATE)
            popupWindow?.dismiss()
        }
        view.findViewById<LinearLayout>(R.id.id_parent_button_share).setOnClickListener {
            listener.getEventData(ActionType.ACTION_SHARE)
            popupWindow?.dismiss()
        }
        view.findViewById<LinearLayout>(R.id.id_parent_button_privacy_policy).setOnClickListener {
            listener.getEventData(ActionType.ACTION_PRIVACY_POLICY)
            popupWindow?.dismiss()
        }
        view.findViewById<LinearLayout>(R.id.id_parent_button_logout).setOnClickListener {
            listener.getEventData(ActionType.ACTION_LOGOUT)
            popupWindow?.dismiss()
        }
    }

//    fun callClassPopup(viewPoint: View) {
//        val view: View = LayoutInflater.from(context).inflate(R.layout.popup_class, null)
//        popupWindow = Utils.popupWindowOverflow(view, context, viewPoint)
//        view.findViewById<LinearLayout>(R.id.id_parent_button_delete).setOnClickListener {
//            listener.getEventData(ActionType.ACTION_DELETE)
//            popupWindow?.dismiss()
//        }
//        view.findViewById<LinearLayout>(R.id.id_parent_button_edit).setOnClickListener {
//            listener.getEventData(ActionType.ACTION_EDIT)
//            popupWindow?.dismiss()
//        }
//    }

    fun dismiss() {
        popupWindow?.dismiss()
    }
}
