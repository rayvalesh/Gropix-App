package com.coagere.gropix.jetpack.bindings

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.tc.utils.utils.helpers.HelperTime
import com.tc.utils.variables.interfaces.Constants

object BindingAdapters {

    @BindingAdapter("timeStamp")
    @JvmStatic
    fun timeStamp(view: TextView, timeStamp: String? = null) {
        if (!timeStamp.isNullOrEmpty() && timeStamp != "null") {
            if (timeStamp.length == 19) {
//                view.text = HelperTime.get().getTimeStamp(timeStamp)
            } else
                view.text = HelperTime.get().getWithAmPm(timeStamp)
        } else {
            view.visibility = View.GONE
        }
    }


    @BindingAdapter("dateStamp")
    @JvmStatic
    fun dateStamp(view: TextView, timeStamp: String? = null) {
        if (!timeStamp.isNullOrEmpty() && timeStamp != "null") {
            if (timeStamp.length == 19) {
//                view.text = HelperTime.get().getDate(timeStamp)
            } else
                view.text = HelperTime.get().parseDateFromString(timeStamp)
        }
    }

    @BindingAdapter("dateTimeStamp")
    @JvmStatic
    fun dateTimeStamp(view: TextView, timeStamp: String? = null) {
        if (!timeStamp.isNullOrEmpty()) {
            view.text = HelperTime.get().getDateFormatWithToday(timeStamp)
        } else {
//            view.visibility = View.GONE
        }
    }

    @BindingAdapter("text")
    @JvmStatic
    fun text(view: TextView, any: Any? = null) {
        when (any) {
            is String -> {
                if (any == "null" || any == "")
                    view.visibility = View.GONE
                else
                    view.text = any
            }
            is Int -> {
                view.text = any.toString()
            }
            else -> view.text = ""
        }
    }

    @BindingAdapter("isSelected")
    @JvmStatic
    fun isSelected(view: View, isSelected: Boolean) {
        view.isSelected = isSelected
    }

    @BindingAdapter("visibility")
    @JvmStatic
    fun visibility(view: View, any: Any? = null) {
        when (any) {
            is Boolean -> {
                view.visibility = if (any) View.VISIBLE else View.GONE
            }
            is Int -> {
                view.visibility = if (any == Constants.VALUE_YES) View.VISIBLE else View.GONE
            }
            else -> {
                view.visibility = if (any != null) View.VISIBLE else View.GONE
            }
        }
    }


    @JvmStatic
    @BindingAdapter(
        value = ["item_counts", "item_visibility", "postfix"],
        requireAll = false
    )
    fun setCount(textView: TextView, count: Int, itemVisibility: Int, postfix: String? = null) {
        if (count == 0) {
            when (itemVisibility) {
                2 -> {
                    textView.visibility = View.GONE
                }
                1 -> {
                    textView.visibility = View.INVISIBLE
                }
                else -> {
                    textView.text = "0"
                }
            }
        } else {
            textView.visibility = View.VISIBLE
            textView.text = when {
                count > 1000 * 1000 -> {
                    String.format("%01d", count / 1000) + "M"
                }
                count > 1000 -> {
                    String.format("%01d", count / 1000) + "K"
                }
                else -> {
                    String.format("%01d", count)
                }
            } + if (postfix.isNullOrEmpty()) "" else " $postfix"
        }
    }

}