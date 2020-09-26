package com.coagere.gropix.jetpack.bindings

import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BindingConversion
import androidx.databinding.BindingMethod
import androidx.databinding.BindingMethods
import com.tc.utils.variables.interfaces.Constants

object ConversionUtils {
    @JvmStatic
    fun isTrue(value: Int): Boolean {
        return value == Constants.VALUE_YES
    }
}

object BindingConversions {

    @BindingConversion
    @JvmStatic
    fun booleanToVisibility(isNotVisible: Boolean): Int {
        return if (isNotVisible) View.GONE else View.VISIBLE
    }

    @BindingMethods(
        BindingMethod(
            type = ImageView::class,
            attribute = "app:srcCompat",
            method = "setImageDrawable"
        )
    )
    class BindingClass {
    }
}