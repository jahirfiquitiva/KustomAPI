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

    PresetFileDataFetcher(Context context, PresetFile file) {
        mContext = context;
        mPresetFile = file;
    }

    @Override
    public void loadData(Priority priority, DataCallback<? super InputStream> callback) {
        try {
            mZipStream = new ZipInputStream(mPresetFile.getStream(mContext));
            ZipEntry ze;
            while ((ze = mZipStream.getNextEntry()) != null) {
                if (ze.getName().equals("preset_thumb_portrait.jpg")) {
                    callback.onDataReady(mZipStream);
                } else if (ze.getName().equals("komponent_thumb.jpg")) {
                    callback.onDataReady(mZipStream);
                }
            }
            throw new IOException("Thumbnail not found");
        } catch (Exception e) {
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
}
