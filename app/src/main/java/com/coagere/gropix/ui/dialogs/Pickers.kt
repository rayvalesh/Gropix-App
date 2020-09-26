package com.coagere.gropix.ui.dialogs

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.get
import com.coagere.gropix.R
import com.tc.utils.utils.helpers.HelperTime
import com.tc.utils.utils.helpers.Utils
import com.tc.utils.variables.abstracts.OnEventOccurListener
import java.util.*

class Pickers(private val context: Context, private val listener: OnEventOccurListener) {
    private var alertDialog: AlertDialog? = null

    /**
     * Custom Day Picker Dialog to adjust days
     * use [NumberPicker] for scrolling feature
     */
//    fun callDayPicker() {
//        val view: View = LayoutInflater.from(context).inflate(R.layout.dialog_day_picker, null)
//        alertDialog = Utils.helperDialog(view, context)
//        val dayPicker = view.findViewById<NumberPicker>(R.id.id_picker)
//        val days = arrayOf(
//            "Sunday", "Monday", "Tuesday",
//            "Wednesday", "Thursday", "Friday", "Saturday"
//        )
//        dayPicker.minValue = 0
//        dayPicker.maxValue = 6
//        dayPicker.displayedValues = days
//        dayPicker.value = HelperTime.get().calendar.get(Calendar.DAY_OF_WEEK)
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
//            dayPicker.textSize =
//                context.resources.getDimension(R.dimen.dimen_common_text_picker)
//            dayPicker.textColor = ContextCompat.getColor(context, R.color.colorTextPrimary)
//        }
//        view.findViewById<Button>(R.id.id_button_pos).setOnClickListener {
//            listener.getEventData(days[dayPicker.value])
//            dismissDialog()
//        }
//    }

    /**
     * Custom Time Picker Dialog to adjust time : 24 hour, 60 minute/second
     * format : hh:MM:ss
     * use [NumberPicker] for scrolling feature
     */
//    fun callTimePicker(title: String) {
//        val view: View = LayoutInflater.from(context).inflate(R.layout.dialog_time_picker, null)
//        alertDialog = Utils.helperDialog(view, context)
//        view.findViewById<TextView>(R.id.id_text).text = title
//        val hourPicker = view.findViewById<NumberPicker>(R.id.id_picker_hour)
//        val minutePicker = view.findViewById<NumberPicker>(R.id.id_picker_minute)
//        val secondPicker = view.findViewById<NumberPicker>(R.id.id_picker_second)
//        val hoursList = arrayListOf<String>()
//        val minuteList = arrayListOf<String>()
//        val secondList = arrayListOf<String>()
//        for (i in 0 until 60) {
//            if (i < 24) {
//                hoursList.add(
//                    if (i < 10) {
//                        "0$i"
//                    } else {
//                        "$i"
//                    }
//                )
//            }
//            minuteList.add(
//                if (i < 10) {
//                    "0$i"
//                } else {
//                    "$i"
//                }
//            )
//            secondList.add(
//                if (i < 10) {
//                    "0$i"
//                } else {
//                    "$i"
//                }
//            )
//        }
//        hourPicker.maxValue = 23
//        hourPicker.minValue = 0
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
//            minutePicker.textSize =
//                context.resources.getDimension(R.dimen.dimen_common_text_size_very_very_large)
//            minutePicker.textColor = ContextCompat.getColor(context, R.color.colorTextPrimary)
//            secondPicker.textSize =
//                context.resources.getDimension(R.dimen.dimen_common_text_size_very_very_large)
//            secondPicker.textColor = ContextCompat.getColor(context, R.color.colorTextPrimary)
//            hourPicker.textSize =
//                context.resources.getDimension(R.dimen.dimen_common_text_size_very_very_large)
//            hourPicker.textColor = ContextCompat.getColor(context, R.color.colorTextPrimary)
//        }
//        minutePicker.maxValue = 59
//        minutePicker.minValue = 0
//        secondPicker.maxValue = 59
//        secondPicker.minValue = 0
//        val calendar = Calendar.getInstance()
//        hourPicker.value = calendar.get(Calendar.HOUR_OF_DAY)
//        minutePicker.value = calendar.get(Calendar.MINUTE)
//        secondPicker.value = calendar.get(Calendar.SECOND)
//        hourPicker.displayedValues = hoursList.toTypedArray()
//        minutePicker.displayedValues = minuteList.toTypedArray()
//        secondPicker.displayedValues = secondList.toTypedArray()
//        hourPicker.wrapSelectorWheel = true
//        minutePicker.wrapSelectorWheel = true
//        secondPicker.wrapSelectorWheel = true
//        view.findViewById<Button>(R.id.id_button_pos).setOnClickListener {
//            listener.getEventData("${hoursList[hourPicker.value]}:${minuteList[minutePicker.value]}:${secondList[secondPicker.value]}")
//            dismissDialog()
//        }
//    }

    fun dismissDialog() {
        alertDialog?.dismiss()
    }
}