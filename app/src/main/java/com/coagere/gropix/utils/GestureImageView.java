package com.coagere.gropix.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;

import androidx.appcompat.widget.AppCompatImageView;

public class GestureImageView extends AppCompatImageView {

    private GestureDetector gestureDetector;
    private OnClick onClick;

    public void onClickListener(OnClick onClick) {
        this.onClick = onClick;
    }

    public GestureImageView(Context context) {
        this(context, null);
    }

    public GestureImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init(context);
    }

    public GestureImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        gestureDetector = new GestureDetector(context, new GestureListener());
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        return gestureDetector.onTouchEvent(e);
    }


    private class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if (onClick != null)
                onClick.onSingleClick();
            return super.onSingleTapConfirmed(e);
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if (onClick != null)
                onClick.onDoubleClick();
            return true;
        }
    }

    public interface OnClick {
        void onSingleClick();

        void onDoubleClick();
    }

}