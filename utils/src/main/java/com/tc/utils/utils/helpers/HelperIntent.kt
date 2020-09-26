package com.tc.utils.utils.helpers

import android.content.*
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import tk.jamun.ui.snacks.L
import java.io.File

object HelperIntent {
    fun intentForBrowser(activity: Context, url: String) {
        var shareData = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        shareData.setPackage("com.android.chrome")
        if (shareData.resolveActivity(activity.packageManager) != null) {
            activity.startActivity(shareData)
        } else {
            shareData = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            val intentChooser = Intent.createChooser(shareData, "Share to")
            if (shareData.resolveActivity(activity.packageManager) != null) {
                activity.startActivity(intentChooser)
            } else {
                L.getInstance().toast(activity, "Sorry! Browser not Found")
            }
        }
    }

    fun rateUs(activity: Context) {
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        intent.data = Uri.parse("market://search?q=foo")
        val pm = activity.packageManager
        val list = pm.queryIntentActivities(intent, 0)
        if (list.size > 0) {
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse("market://details?id=" + activity.packageName)
            activity.startActivity(i)
        }
    }

    fun exploreApp(context: Context, packageName: String) {
        try {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=$packageName")
                )
            )
        } catch (anfe: ActivityNotFoundException) {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
                )
            )
        }

    }

    fun shareVideoData(
        context: Context,
        file: File,
        description: String,
        subject: String
    ): Intent {
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        val imageUri: Uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            FileProvider.getUriForFile(
                context,
                "com.smilee.fileprovider",
                file
            )
        } else {
            Uri.fromFile(file)
        }
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri)
        shareIntent.type = "video/*"
        shareIntent.putExtra(Intent.EXTRA_TEXT, description)
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        return shareIntent

    }

    fun shareData(context: Context, subject: String, description: String) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, description)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        context.startActivity(shareIntent)
    }


}
