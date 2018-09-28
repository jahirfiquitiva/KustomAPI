package org.kustom.api.dashboard.utils;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import android.annotation.SuppressLint;
import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import org.kustom.api.preset.PresetInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;

@SuppressWarnings("unused")
public class WallpaperBitmapLoader {
    private final static Object mLock = new Object();
    private static WeakReference<Bitmap> mBitmap = null;

    private WallpaperBitmapLoader() {
    }

    public static WallpaperBitmapLoader create() {
        return new WallpaperBitmapLoader();
    }

    public void load(@NonNull Context context, @NonNull Callback callback) {
        synchronized (mLock) {
            if (mBitmap != null && mBitmap.get() != null)
                callback.onBitmapLoaded(mBitmap.get());
            else new LoaderTask(context, callback).execute();
        }
    }

    public interface Callback {
        void onBitmapLoaded(Bitmap bitmap);
    }

    @SuppressLint("StaticFieldLeak")
    private static class LoaderTask extends AsyncTask<Void, Void, Bitmap> {
        private final Callback mCallback;
        private final Context mContext;

        LoaderTask(Context context, Callback callback) {
            mContext = context.getApplicationContext();
            mCallback = callback;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                WallpaperManager wallpaperManager = WallpaperManager.getInstance(mContext);
                Drawable wallpaperDrawable = wallpaperManager.getDrawable();
                if (wallpaperDrawable != null && wallpaperDrawable instanceof BitmapDrawable)
                    return ((BitmapDrawable) wallpaperDrawable).getBitmap();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                synchronized (mLock) {
                    mBitmap = new WeakReference<>(result);
                    mCallback.onBitmapLoaded(result);
                }
            }
        }

        private PresetInfo buildFromStream(InputStream is) throws IOException {
            PresetInfo info = null;
            InputStreamReader isr = new InputStreamReader(is, "UTF-8");
            JsonReader reader = new JsonReader(new BufferedReader(isr));
            reader.beginObject();
            String name = reader.nextName();
            if (name.equals("preset_info"))
                info = new Gson().fromJson(reader, PresetInfo.class);
            reader.close();
            return info;
        }
    }
}