package com.coagere.gropix.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.coagere.gropix.R
import com.tc.utils.utils.customs.RoundedCornersTransformation
import com.tc.utils.variables.interfaces.ApiKeys
import tk.jamun.elements.circularimageview.CircularImageView

object DownloadImage {

    fun downloadFile(
        context: Context, url: String?, imageView: ImageView, placeHolder: Int,
        contentType: RoundedCornersTransformation.CornerType = RoundedCornersTransformation.CornerType.ALL
    ) {
        url?.let {
            Glide.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .transform(RoundedCornersTransformation(context, imageView, 16, 0, contentType))
                .placeholder(placeHolder)
                .into(imageView)
        }
    }

    fun downloadImages(
        context: Context,
        url: String?,
        imageView: ImageView,
        placeHolder: Int,
        contentType: RoundedCornersTransformation.CornerType? = RoundedCornersTransformation.CornerType.ALL,
        roundRadius: Int? = 16
    ) {
        url?.let {
            Glide.with(context)
                .load(getUrl(url))
                .transform(
                    RoundedCornersTransformation(
                        context,
                        imageView,
                        roundRadius!!,
                        0,
                        contentType!!
                    )
                )
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .placeholder(placeHolder)
                .into(imageView)
        }
    }

    @BindingAdapter(
        value = ["downloadImage"],
        requireAll = false
    )
    @JvmStatic
    fun downloadImage(
        view: View,
        url: String? = null
    ) {
        if (!url.isNullOrEmpty())
            Glide.with(view.context)
                .load(ApiKeys.URL_DOMAIN + url)
                .centerCrop()
                .transform(
                    RoundedCornersTransformation(
                        view.context,
                        view,
                        radius = 12,
                        0,
                        RoundedCornersTransformation.CornerType.ALL
                    )
                )
                .placeholder(
                    view.context.getDrawable(R.drawable.placeholder_one)
                )
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(
                    if (view is AppCompatImageView) {
                        view
                    } else {
                        view as ImageView
                    }
                )
    }

    fun getUrl(url: String): Any? {
        return url.replace(" ", "%20")
    }

    fun downloadPDFImages(
        context: Context, url: String?, imageView: ImageView, placeHolder: Int
    ) {
        url?.let {
            Glide.with(context)
                .load(
                    getUrl(
                        url.substring(
                            0,
                            url.lastIndexOf(".")
                        ) + ".jpg"
                    )
                )
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .placeholder(placeHolder)
                .into(imageView)
        }
    }

    fun downloadImageByUrl(
        context: Context,
        url: String?,
        imageView: ImageView,
        placeHolder: Int,
        contentType: RoundedCornersTransformation.CornerType? = RoundedCornersTransformation.CornerType.ALL,
        roundRadius: Int? = 16
    ) {
        url?.let {
            Glide.with(context)
                .load(url)
                .transform(
                    RoundedCornersTransformation(
                        context,
                        imageView,
                        roundRadius!!,
                        0,
                        contentType!!
                    )
                )
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .placeholder(placeHolder)
                .into(imageView)
        }
    }


}
