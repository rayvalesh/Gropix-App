package com.tc.utils.utils.utility

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.tc.utils.R
import com.tc.utils.utils.helpers.CheckOs


object UtilityCheckPermission {

    const val MY_PERMISSIONS_REQUEST_CAMERA = 121
    const val MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 122
    const val MY_PERMISSIONS_REQUEST_CONTACT = 125
    const val MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 126

    fun checkPermission(context: Context, i: Int): Boolean {
        when (i) {
            MY_PERMISSIONS_REQUEST_CAMERA -> {
                return checkPermission(
                    context, "Camera Permission Required for Capturing Images",
                    Manifest.permission.CAMERA, MY_PERMISSIONS_REQUEST_CAMERA
                )
            }
            MY_PERMISSIONS_REQUEST_CONTACT -> {
                return checkPermission(
                    context,
                    "Contact permission is necessary for Providing Better User Experience.",
                    Manifest.permission.READ_CONTACTS,
                    MY_PERMISSIONS_REQUEST_CONTACT
                )
            }
            MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE -> {
                return checkPermission(
                    context,
                    "External storage permission is necessary without it you Cannot Access Images from Gallery Or Other File Manager",
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE
                )
            }
            MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
                return checkPermission(
                    context,
                    "",
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
                )
            }
            else -> return false
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private fun checkPermission(
        context: Context,
        message: String,
        permission: String,
        code: Int
    ): Boolean {
        if (CheckOs.checkBuildMarshmallow()) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        context as Activity,
                        permission
                    )
                ) {
                    val alertBuilder = AlertDialog.Builder(context, R.style.AlertDialogStyle)
                    alertBuilder.setCancelable(true)
                    alertBuilder.setTitle("Permission Requirement")
                    alertBuilder.setMessage(message)
                    alertBuilder.setPositiveButton(android.R.string.yes) { _, _ ->
                        ActivityCompat.requestPermissions(
                            context,
                            arrayOf(
                                permission,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE
                            ),
                            code
                        )
                    }
                    val alert = alertBuilder.create()
                    alert.show()

                } else {
                    ActivityCompat.requestPermissions(
                        context,
                        arrayOf(
                            permission,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        ),
                        code
                    )
                }
                return false
            } else {
                return true
            }
        } else {
            return true
        }
    }
}

