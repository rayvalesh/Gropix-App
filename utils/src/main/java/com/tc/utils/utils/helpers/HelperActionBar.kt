package com.tc.utils.utils.helpers

import android.app.Activity
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.AppBarLayout
import com.tc.utils.utils.utility.isNotNull
import com.tc.utils.R
import tk.jamun.ui.snacks.L
import kotlin.math.abs

object HelperActionBar {
    private var appBarExtraVisibility: Boolean = false

    fun setSupportActionBar(actionBar: ActionBar, activity: Activity, title: Int): ActionBar? {
        return setSupportActionBar(actionBar, activity.getString(title))
    }

    fun setSupportActionBar(
        activity: AppCompatActivity,
        view: View,
        title: String? = null
    ) {
        activity.setSupportActionBar(view.findViewById(R.id.id_app_bar))
        setSupportActionBar(activity.supportActionBar, title)
    }

    fun setSupportActionBar(actionBar: ActionBar?, title: String? = ""): ActionBar? {
        if (isNotNull(actionBar)) {
            actionBar!!.setDisplayHomeAsUpEnabled(true)
            actionBar.title = title
            actionBar.setDisplayShowTitleEnabled(true)
        }
        return actionBar
    }

    fun setSupportActionBar(actionBar: ActionBar?, title: Int): ActionBar? {
        if (isNotNull(actionBar)) {
            actionBar!!.setDisplayHomeAsUpEnabled(true)
            actionBar.setTitle(title)
            actionBar.setDisplayShowTitleEnabled(true)
        }
        return actionBar
    }

    fun setAppBarLayout(
        activity: Activity,
        value: Double,
        scrollingListener: ScrollingListener,
        oldAppBarLayout: AppBarLayout? = null
    ): AppBarLayout {
        val appBarLayout = oldAppBarLayout ?: activity.findViewById(R.id.id_parent_app_bar)
        appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout1: AppBarLayout, offset: Int ->
            val maxScroll = appBarLayout1.totalScrollRange
            val percentage = abs(offset).toFloat() / maxScroll.toFloat()
            if (percentage >= value) {
                if (!appBarExtraVisibility) {
                    appBarExtraVisibility = true
                    scrollingListener.up()
                }
            } else if (appBarExtraVisibility) {
                appBarExtraVisibility = false
                scrollingListener.down()
            }
        })
        return appBarLayout
    }

    interface ScrollingListener {
        fun up()

        fun down()
    }

    fun showActionBar(actionBar: ActionBar?) {
        actionBar?.setDisplayShowTitleEnabled(false)
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }

    fun hideActionBar(actionBar: ActionBar?) {
        actionBar?.setDisplayShowTitleEnabled(false)
        actionBar?.setDisplayHomeAsUpEnabled(false)
    }
}
