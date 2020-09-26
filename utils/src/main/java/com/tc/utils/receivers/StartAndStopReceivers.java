package com.tc.utils.receivers;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;


public class StartAndStopReceivers {

    public void enableBroadcastReceiver(Context context, Class<?> cls) {
        ComponentName receiver = new ComponentName(context, cls);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    /**
     * This method disables the Broadcast receiver registered in the AndroidManifest file.
     */
    public void disableBroadcastReceiver(Context context, Class<?> cls) {
        ComponentName receiver = new ComponentName(context, cls);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }
}
