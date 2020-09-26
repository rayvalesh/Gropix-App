package com.tc.utils.utils.customs

import android.content.Context
import android.util.AttributeSet
import android.widget.HorizontalScrollView

class CustomHorizontalView : HorizontalScrollView {

    var mOnScrollChangedListener: OnScrollChangedListener? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        if (mOnScrollChangedListener != null) {
            mOnScrollChangedListener!!.onScrollChanged(l, t, oldl, oldt)
        }
    }

    fun setOnScrollChangedListener(onScrollChangedListener: OnScrollChangedListener) {
        this.mOnScrollChangedListener = onScrollChangedListener
    }

    interface OnScrollChangedListener {
        fun onScrollChanged(left: Int, top: Int, oldLeft: Int, oldTop: Int)
    }

}

