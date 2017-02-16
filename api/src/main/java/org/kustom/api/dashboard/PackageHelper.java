package org.kustom.api.dashboard;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.util.Log;

class PackageHelper {
    private final static String TAG = PackageHelper.class.getSimpleName();

    public static boolean packageInstalled(@NonNull Context context, @NonNull String packageName) {
        try {
            context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_META_DATA);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        } catch (Exception e) {
            Log.e(TAG, "Unable to verify signature", e);
        }
        return false;
    }
}
