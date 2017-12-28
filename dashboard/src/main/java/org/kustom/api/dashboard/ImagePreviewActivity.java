package org.kustom.api.dashboard;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.transition.Transition;

import org.json.JSONException;
import org.json.JSONObject;
import org.kustom.api.dashboard.model.ImageData;
import org.kustom.api.dashboard.utils.WallpaperUtils;

import java.io.File;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class ImagePreviewActivity extends Activity {
    private final static String TAG = ImagePreviewActivity.class.getSimpleName();

    private final static int PERMISSION_STORAGE_REQUEST_CODE = 123;

    public static final String EXTRA_IMAGE_DATA = "org.kustom.api.dashboard.EXTRA_IMAGE_DATA";

    private Bitmap mBitmap = null;

    private ImageData mImageData = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "Image preview starting");
        setTheme(R.style.KustomDashboardTheme_Translucent);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kustom_image_preview_activity);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setActionBar(toolbar);
        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getActionBar().setDisplayShowHomeEnabled(true);
        }

        // Load
        if (getIntent() != null && getIntent().hasExtra(EXTRA_IMAGE_DATA)) try {
            JSONObject jsonData = new JSONObject(getIntent().getStringExtra(EXTRA_IMAGE_DATA));
            mImageData = new ImageData(jsonData);
            ((TextView) findViewById(R.id.title)).setText(mImageData.getTitle());
            ((TextView) findViewById(R.id.author)).setText(mImageData.getAuthor());
            Glide.with(this)
                    .asBitmap()
                    .load(mImageData.getUrl())
                    .into(new BitmapImageViewTarget(findViewById(R.id.image)) {
                        @Override
                        public void onResourceReady(Bitmap r, @Nullable Transition<? super Bitmap> t) {
                            mBitmap = r;
                            findViewById(R.id.text).setVisibility(GONE);
                            findViewById(R.id.progress).setVisibility(GONE);
                            super.onResourceReady(r, t);
                        }
                    });
        } catch (JSONException e) {
            e.printStackTrace();
            setText(e.getMessage());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        DashboardSettings settings = DashboardSettings.get(this);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.image_preview, menu);
        for (int i = 0; i < menu.size(); i++) {
            Drawable icon = menu.getItem(i).getIcon();
            if (icon != null) {
                icon.mutate();
                icon.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
            }
        }
        if (!settings.wallsDownloadEnabled())
            menu.removeItem(R.id.menu_save);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        } else if (id == R.id.menu_apply) {
            if (mBitmap != null) new WallpaperApplyTask().execute(mBitmap);
        } else if (id == R.id.menu_save) {
            downloadBitmap();
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("UnnecessaryReturnStatement")
    @Override
    public void onRequestPermissionsResult(int code, @NonNull String perm[], @NonNull int[] result) {
        switch (code) {
            case PERMISSION_STORAGE_REQUEST_CODE: {
                if (result.length > 0 && result[0] == PERMISSION_GRANTED) {
                    downloadBitmap();
                }
                return;
            }
        }
    }

    protected void setText(String text) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> {
            findViewById(R.id.text).setVisibility(VISIBLE);
            findViewById(R.id.progress).setVisibility(GONE);
            ((TextView) findViewById(R.id.text)).setText(text);
        });
    }

    private void downloadBitmap() {
        if (ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE)
                != PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE},
                    PERMISSION_STORAGE_REQUEST_CODE);
        } else if (mBitmap != null && mImageData != null) {
            new WallpaperDownloadTask(mImageData.getTitle()).execute(mBitmap);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class WallpaperDownloadTask extends AsyncTask<Bitmap, Void, Exception> {
        private final String mName;
        private File mDownloadPath;

        WallpaperDownloadTask(String name) {
            mName = name;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            findViewById(R.id.progress).setVisibility(VISIBLE);
        }

        @Override
        protected Exception doInBackground(Bitmap... bitmaps) {
            try {
                mDownloadPath = WallpaperUtils.downloadWallpaper(ImagePreviewActivity.this, bitmaps[0], mName);
                return null;
            } catch (Exception e) {
                return e;
            }
        }

        @Override
        protected void onPostExecute(Exception e) {
            findViewById(R.id.progress).setVisibility(GONE);
            if (e == null) {
                Toast.makeText(ImagePreviewActivity.this,
                        "Wallpaper Saved to: " + mDownloadPath, Toast.LENGTH_LONG).show();
            } else {
                e.printStackTrace();
                Toast.makeText(ImagePreviewActivity.this,
                        e.getMessage(), Toast.LENGTH_LONG).show();
            }
            finish();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class WallpaperApplyTask extends AsyncTask<Bitmap, Void, Exception> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            findViewById(R.id.progress).setVisibility(VISIBLE);
        }

        @Override
        protected Exception doInBackground(Bitmap... bitmaps) {
            try {
                WallpaperUtils.setWallpaper(ImagePreviewActivity.this, bitmaps[0]);
                return null;
            } catch (Exception e) {
                return e;
            }
        }

        @Override
        protected void onPostExecute(Exception e) {
            findViewById(R.id.progress).setVisibility(GONE);
            if (e == null) {
                Toast.makeText(ImagePreviewActivity.this,
                        "Wallpaper Set", Toast.LENGTH_LONG).show();
            } else {
                e.printStackTrace();
                Toast.makeText(ImagePreviewActivity.this,
                        e.getMessage(), Toast.LENGTH_LONG).show();
            }
            finish();
        }
    }
}