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
        value = ["downloadImage", "placeHolder", "radius", "shape"],
        requireAll = false
    )
    @JvmStatic
    fun downloadImage(
        view: View,
        url: String? = null,
        placeHolder: Drawable? = null,
        roundRadius: Int = 12,
        contentType: Int? = 1
    ) {
        if (!url.isNullOrEmpty())
            Glide.with(view.context)
                .load(url)
                .centerCrop()
                .transform(
                    RoundedCornersTransformation(
                        view.context,
                        view,
                        12,
                        0,
                        RoundedCornersTransformation.CornerType.ALL
                    )
                )
                .placeholder(
                    if (placeHolder == null)
                        placeHolder
                    else view.context.getDrawable(R.drawable.placeholder_one)
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


    fun downloadProfilePicture(
        context: Context, url: String? = null, imageView: CircularImageView,
        name: String? = null
    ) {
        if (!url.isNullOrEmpty()) {
            Glide.with(context)
                .load(url)
                .placeholder(
                    if (!name.isNullOrEmpty())
                        setImageText(name)
                    else R.drawable.image_vd_user_default
                )
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(imageView)
        } else {
            if (!name.isNullOrEmpty())
                (imageView).setImageResource(setImageText(name))
        }
    }

    @BindingAdapter(
        value = ["setImage", "setName"],
        requireAll = false
    )
    @JvmStatic
    fun setImage(
        view: View,
        url: String? = null,
        name: String? = null
    ) {
        if (!url.isNullOrEmpty()) {
            Glide.with(view.context)
                .load(url)
                .placeholder(
                    if (!name.isNullOrEmpty())
                        setImageText(name)
                    else R.drawable.image_vd_user_default
                )
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(
                    when (view) {
                        is CircularImageView -> view
                        is AppCompatImageView -> view
                        else -> view as ImageView
                    }
                )
        } else {
            if (!name.isNullOrEmpty())
                (when (view) {
                    is CircularImageView -> view
                    is AppCompatImageView -> view
                    else -> view as ImageView
                }).setImageResource(setImageText(name))
        }
    }

    fun setTextColor(context: Context, textView: TextView, name: String) {
        textView.setTextColor(
            ContextCompat.getColor(
                context,
                when (name.substring(0, 1).toLowerCase()) {
                    "a", "f", "l", "r", "x" -> R.color.colorStyleOneDark
                    "b", "g", "m", "s", "y" -> R.color.colorStyleTwoDark
                    "c", "h", "n", "t", "z" -> R.color.colorStyleThreeDark
                    "d", "i", "o", "u" -> R.color.colorStyleFourDark
                    "e", "j", "p", "v" -> R.color.colorStyleFiveDark
                    else -> R.color.colorStyleSixDark
                }
            )
        )


    }

    private fun setImageText(data: String): Int {
        return when (data.substring(0, 1).toUpperCase()) {
            "G" -> R.drawable.placeholder_g
            "M" -> R.drawable.placeholder_m
            "S" -> R.drawable.placeholder_s
            "Y" -> R.drawable.placeholder_y
            "B" -> R.drawable.placeholder_b
            "H" -> R.drawable.placeholder_h
            "N" -> R.drawable.placeholder_n
            "T" -> R.drawable.placeholder_t
            "Z" -> R.drawable.placeholder_z
            "C" -> R.drawable.placeholder_c
            "I" -> R.drawable.placeholder_i
            "O" -> R.drawable.placeholder_o
            "U" -> R.drawable.placeholder_u
            "D" -> R.drawable.placeholder_d
            "J" -> R.drawable.placeholder_j
            "P" -> R.drawable.placeholder_p
            "V" -> R.drawable.placeholder_v
            "E" -> R.drawable.placeholder_e
            "K" -> R.drawable.placeholder_k
            "Q" -> R.drawable.placeholder_q
            "W" -> R.drawable.placeholder_w
            "F" -> R.drawable.placeholder_f
            "L" -> R.drawable.placeholder_l
            "R" -> R.drawable.placeholder_r
            "X" -> R.drawable.placeholder_x
            else -> R.drawable.placeholder_a
        }
    }

}
