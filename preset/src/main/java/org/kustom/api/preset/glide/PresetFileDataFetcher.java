package org.kustom.api.preset.glide;

import android.content.Context;
import android.support.annotation.NonNull;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.data.DataFetcher;

import org.kustom.api.preset.PresetFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class PresetFileDataFetcher implements DataFetcher<InputStream> {
    private final Context mContext;
    private final PresetFile mPresetFile;
    private ZipInputStream mZipStream = null;
    private boolean mLandscape = false;

    PresetFileDataFetcher(Context context, PresetFile file) {
        mContext = context;
        mPresetFile = file;
    }

    @Override
    public void loadData(@NonNull Priority priority, @NonNull DataCallback<? super InputStream> callback) {
        String thumb;
        if ("komp".equalsIgnoreCase(mPresetFile.getExt())) thumb = "komponent_thumb.jpg";
        else if (mLandscape) thumb = "preset_thumb_landscape.jpg";
        else thumb = "preset_thumb_portrait.jpg";
        try {
            mZipStream = new ZipInputStream(mPresetFile.getStream(mContext));
            ZipEntry ze;
            boolean found = false;
            while ((ze = mZipStream.getNextEntry()) != null) {
                if (ze.getName().equals(thumb)) {
                    callback.onDataReady(mZipStream);
                    found = true;
                    break;
                }
            }
            if (!found) throw new IOException("Thumbnail not found");
        } catch (Exception e) {
            e.printStackTrace();
            callback.onLoadFailed(e);
        }
    }

    @Override
    public void cleanup() {
        if (mZipStream != null) try {
            mZipStream.close();
        } catch (Exception ignored) {
        }
    }

    @Override
    public void cancel() {
    }

    @NonNull
    @Override
    public Class<InputStream> getDataClass() {
        return InputStream.class;
    }

    @NonNull
    @Override
    public DataSource getDataSource() {
        return DataSource.LOCAL;
    }

    PresetFileDataFetcher setLandscape(boolean landscape) {
        mLandscape = landscape;
        return this;
    }
}