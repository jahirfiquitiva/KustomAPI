package org.kustom.api;

import android.util.Log;

public class Logger {
    private final static String TAG = "KustomProvider";

    public static void v(String message, Object... args) {
        Log.v(TAG, String.format(message, args));
    }

    public static void d(String message, Object... args) {
        Log.d(TAG, String.format(message, args));
    }

    public static void i(String message, Object... args) {
        Log.i(TAG, String.format(message, args));
    }

    public static void w(String message) {
        Log.w(TAG, message);
    }

    public static void w(String message, Throwable cause) {
        Log.w(TAG, message, cause);
    }

    public static void e(String message) {
        Log.e(TAG, message);
    }

    public static void e(String message, Throwable cause) {
        Log.e(TAG, message, cause);
    }
}