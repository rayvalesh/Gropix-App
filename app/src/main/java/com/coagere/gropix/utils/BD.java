package com.coagere.gropix.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;

import androidx.annotation.NonNull;

import com.coagere.gropix.R;

import tk.jamun.ui.snacks.L;


public class BD extends Drawable {

    private Paint bP;
    private Paint bP1, bP2;
    private Paint tP;
    private Rect tR = new Rect();

    private String mCount = "";
    private boolean mWillDraw;

    public BD(Context context, boolean isOfWhiteColored) {
        float mTextSize = context.getResources().getDimension(R.dimen.badge_text_size);

        bP = new Paint();
        if (isOfWhiteColored) {
            bP.setColor(context.getResources().getColor(R.color.colorWhite));
        } else {
            bP.setColor(context.getResources().getColor(R.color.colorErrorMessage));
        }
        bP.setAntiAlias(true);
        bP.setTextSize(mTextSize);
        bP.setStyle(Paint.Style.FILL);

        bP1 = new Paint();
        if (isOfWhiteColored) {
            bP1.setColor(context.getResources().getColor(R.color.colorWhite));
        } else {
            bP1.setColor(context.getResources().getColor(R.color.colorErrorMessage));
        }
        bP1.setAntiAlias(true);
        bP1.setStyle(Paint.Style.FILL);
        bP1.setTextSize(mTextSize);

        bP2 = new Paint();
        if (isOfWhiteColored) {
            bP2.setColor(context.getResources().getColor(R.color.colorErrorMessage));
        } else {
            bP2.setColor(context.getResources().getColor(R.color.colorWhite));
        }
        bP2.setAntiAlias(true);
        bP2.setTextSize(mTextSize);
        bP2.setStrokeWidth(4);
        bP2.setStyle(Paint.Style.STROKE);

        tP = new Paint();
        if (isOfWhiteColored) {
            tP.setColor(context.getResources().getColor(R.color.colorErrorMessage));
        } else {
            tP.setColor(context.getResources().getColor(R.color.colorWhite));
        }
        tP.setTypeface(Typeface.DEFAULT);
        tP.setTextSize(mTextSize);
        tP.setTypeface(Typeface.DEFAULT_BOLD);
        tP.setAntiAlias(true);
        tP.setTextAlign(Paint.Align.CENTER);
    }

    public static void setBadgeCount(Context context, boolean isOfWhiteColored, int count, int id, LayerDrawable layerDrawable) {
        BD badge = new BD(context, isOfWhiteColored);
        Drawable reuse = layerDrawable.findDrawableByLayerId(id);
        if (reuse instanceof BD) {
            badge = (BD) reuse;
        }
        if (count > 99)
            badge.setCount("99+");
        else
            badge.setCount(String.valueOf(count));
        layerDrawable.mutate();
        layerDrawable.setDrawableByLayerId(id, badge);
    }

    public static void setBadgeCount(Context context, int count, int id, LayerDrawable layerDrawable) {
        BD badge = new BD(context, false);
        Drawable reuse = layerDrawable.findDrawableByLayerId(id);
        if (reuse instanceof BD) {
            badge = (BD) reuse;
        }
        if (count > 99)
            badge.setCount("99+");
        else
            badge.setCount(String.valueOf(count));
        layerDrawable.mutate();
        layerDrawable.setDrawableByLayerId(id, badge);
    }

    public static void setBadgeCount(Context context, String count, int id, LayerDrawable layerDrawable) {
        BD badge = new BD(context, false);
        Drawable reuse = layerDrawable.findDrawableByLayerId(id);
        if (reuse instanceof BD) {
            badge = (BD) reuse;
        }
        badge.setCount(count);
        layerDrawable.mutate();
        layerDrawable.setDrawableByLayerId(id, badge);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        if (!mWillDraw) {
            return;
        }
        Rect bounds = getBounds();
        float width = bounds.right - bounds.left;
        float height = bounds.bottom - bounds.top;
        float radius = ((Math.max(width, height) / 2)) / 2;
        float centerX = (width - radius - 1) + 5;
        float centerY = radius - 5;
        if (mCount.length() <= 2) {
            canvas.drawCircle(centerX, centerY, (int) (radius + 5.5), bP2);
            canvas.drawCircle(centerX, centerY, (int) (radius + 5.5), bP1);
            canvas.drawCircle(centerX, centerY, (int) (radius + 3.5), bP);
        } else {
            canvas.drawCircle(centerX, centerY, (int) (radius + 5.5), bP2);
            canvas.drawCircle(centerX, centerY, (int) (radius + 5.5), bP1);
            canvas.drawCircle(centerX, centerY, (int) (radius + 3.5), bP);
        }
        tP.getTextBounds(mCount, 0, mCount.length(), tR);
        float textHeight = tR.bottom - tR.top;
        float textY = centerY + (textHeight / 2f);
        if (mCount.length() > 2)
            canvas.drawText("99+", centerX, textY, tP);
        else
            canvas.drawText(mCount, centerX, textY, tP);
    }

    public void setCount(String count) {
        mCount = count;
        mWillDraw = !count.equalsIgnoreCase("0");
        invalidateSelf();
    }

    @Override
    public void setAlpha(int alpha) {
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
    }

    @Override
    public int getOpacity() {
        return PixelFormat.UNKNOWN;
    }
}
