package com.tc.utils.utils.helpers

import android.content.pm.PackageManager
import android.os.Build

/**
 * CheckOs give you functionality to compute Android OS Versions
 */
object CheckOs {
    /**
     * Method help you for checking JellyBean OS Version
     *
     * @return Status of Found
     */
    fun checkForJellyBean(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
    }

    /**
     * Method help you for checking Nougat OS Version
     *
     * @return Status of Found
     */
    fun checkForNogout(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1
    }

    /**
     * Method help you for checking OREO OS Version
     *
     * @return Status of Found
     */
    fun checkForOreo(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
    }

    /**
     * Method help you for checking LOLLIPOP OS Version
     *
     * @return Status of Found
     */
    fun checkBuildForLolipop(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
    }

    /**
     * Method help you for checking Marshmallow OS Version
     *
     * @return Status of Found
     */
    fun checkBuildMarshmallow(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
    }

    /**
     * Method help you for checking KITKAT OS Version
     *
     * @return Status of Found
     */
    fun checkBuildKitkat(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
    }

    /**
     * Method help you for handling Permission reply Result
     *
     * @return Status of Found
     */
    fun permissionRequestResult(grantResults: IntArray): Boolean {
        return grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
    }

    fun checkForJellyBeanHighest(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2
    }

    fun checkForOreoHighest(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1
    }
}
