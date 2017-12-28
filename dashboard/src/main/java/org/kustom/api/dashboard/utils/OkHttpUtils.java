package org.kustom.api.dashboard.utils;

import android.content.Context;

import java.io.File;

import okhttp3.Cache;

public class OkHttpUtils {
    private static Cache sCache = null;

    private OkHttpUtils() {
    }

    public static synchronized Cache getCacheDirectory(Context context) {
        if (sCache == null) {
            File cacheDirectory = new File(context.getCacheDir(), "http");
            int cacheSize = 10 * 1024 * 1024; // 10 MiB
            sCache = new Cache(cacheDirectory, cacheSize);
        }
        return sCache;
    }
}
