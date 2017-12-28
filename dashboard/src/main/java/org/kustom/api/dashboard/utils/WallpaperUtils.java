package org.kustom.api.dashboard.utils;

import android.Manifest;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import org.kustom.api.dashboard.DashboardSettings;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static android.os.Environment.DIRECTORY_PICTURES;
import static android.support.v4.content.ContextCompat.checkSelfPermission;

public class WallpaperUtils {

    public static void setWallpaper(@NonNull Context context, @NonNull Bitmap bitmap)
            throws IOException {
        WallpaperManager.getInstance(context).setBitmap(bitmap);
    }

    public static File downloadWallpaper(@NonNull Context ctx, @NonNull Bitmap bmp, @NonNull String name)
            throws IOException {
        if (checkSelfPermission(ctx, WRITE_EXTERNAL_STORAGE) != PERMISSION_GRANTED)
            throw new IOException("External storage permission not granted");
        DashboardSettings settings = DashboardSettings.get(ctx);
        File pictures = Environment.getExternalStoragePublicDirectory(DIRECTORY_PICTURES);
        File dir = new File(pictures, settings.wallsDownloadDirectory());
        if (!dir.exists() && !dir.mkdir())
            throw new IOException("Unable to create directory");
        File imageFile = new File(dir, String.format("%s.jpg", name));
        try (OutputStream fOut = new FileOutputStream(imageFile)) {
            bmp.compress(Bitmap.CompressFormat.WEBP, 100, fOut);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException(e);
        }
        return imageFile;
    }
}