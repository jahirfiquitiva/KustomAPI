package org.kustom.api.preset.glide;

import android.content.Context;
import android.support.annotation.NonNull;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.data.DataFetcher;

import org.kustom.api.preset.PresetFile;

import java.io.InputStream;

public class PresetFileDataFetcher implements DataFetcher<InputStream> {
    private final Context mContext;
    private final PresetFile mPresetFile;
    private InputStream mStream = null;
    private boolean mLandscape = false;

    PresetFileDataFetcher(Context context, PresetFile file) {
        mContext = context;
        mPresetFile = file;
    }

    @Override
    public void loadData(@NonNull Priority p, @NonNull DataCallback<? super InputStream> callback) {
        String thumb;
        if (mPresetFile.isKomponent()) thumb = "komponent_thumb.jpg";
        else if (mLandscape) thumb = "preset_thumb_landscape.jpg";
        else thumb = "preset_thumb_portrait.jpg";
        try {
            mStream = mPresetFile.getStream(mContext, thumb);
            callback.onDataReady(mStream);
        } catch (Exception e) {
            e.printStackTrace();
            callback.onLoadFailed(e);
        }
    }

    @Override
    public void cleanup() {
        if (mStream != null) try {
            mStream.close();
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