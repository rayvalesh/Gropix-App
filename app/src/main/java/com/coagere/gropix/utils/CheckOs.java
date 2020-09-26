package com.coagere.gropix.utils;

import android.content.pm.PackageManager;
import android.os.Build;

/**
 * CheckOs give you functionality to compute Android OS Versions
 * @author Jatin Sahgal
     */
public class CheckOs {
    /**
     * Method help you for checking JellyBean OS Version
     *
     * @return Status of Found
     * @author Jatin Sahgal
     */
    public static boolean checkForJellyBean() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    /**
     * Method help you for checking Nougat OS Version
     *
     * @return Status of Found
     * @author Jatin Sahgal
     */
    public static boolean checkForNogout() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1;
    }

    /**
     * Method help you for checking OREO OS Version
     *
     * @return Status of Found
     * @author Jatin Sahgal
     */
    public static boolean checkForOreo() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
    }

    /**
     * Method help you for checking LOLLIPOP OS Version
     *
     * @return Status of Found
     * @author Jatin Sahgal
     */
    public static boolean checkBuildForLolipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    /**
     * Method help you for checking Marshmallow OS Version
     *
     * @return Status of Found
     * @author Jatin Sahgal
     */
    public static boolean checkBuildMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    /**
     * Method help you for checking KITKAT OS Version
     * @return Status of Found
     * @author Jatin Sahgal
     */
    public static boolean checkBuildKitkat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    /**
     * Method help you for handling Permission reply Result
     * @return Status of Found
     * @author Jatin Sahgal
     */
    public static boolean permissionRequestResult(int[] grantResults) {
        return grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean checkForJellyBeanHighest() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2;
    }

    public static boolean checkForOreoHighest() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1;
    }
}
