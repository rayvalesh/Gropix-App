package com.tc.utils.utils.helpers;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;

public class DeviceInfo {
    public String getAndroidVersion() {
        String release = Build.VERSION.RELEASE;
        int sdkVersion = Build.VERSION.SDK_INT;
        return "Android SDK: " + sdkVersion + " (" + release + ")";
    }

    public String getDeviceName() {
        String manufacture = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacture)) {
            return model;
        } else {
            return manufacture + " " + model;
        }
    }

    public String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

}
