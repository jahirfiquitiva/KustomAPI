package org.kustom.api.dashboard.utils;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import org.kustom.api.dashboard.model.ImageData;

import java.io.IOException;

public class WallpaperUtils {

    public static void setWallpaper(@NonNull Context context, @NonNull Bitmap bitmap)
            throws IOException {
        WallpaperManager.getInstance(context).setBitmap(bitmap);
    }

}
