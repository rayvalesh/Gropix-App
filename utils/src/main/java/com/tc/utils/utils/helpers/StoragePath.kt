package com.tc.utils.utils.helpers

import android.content.Context
import android.os.Environment
import java.io.File
import java.io.IOException

object StoragePath {
    private fun setFileFolder(): File {
        val file = File(Environment.getExternalStorageDirectory(), "/Grocery/")
        file.mkdirs()
        return file
    }

    fun createSentImageFile(fileName: String): File {
        return File(setFileFolder(), fileName)
    }
    fun createTempFile(activity: Context): File? {
        var file: File? = null
        try {
            file = File.createTempFile("lib-", ".jpeg", activity.cacheDir)
        } catch (ignored: IOException) {
        }
        return file
    }
}
