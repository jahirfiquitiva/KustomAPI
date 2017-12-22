package org.kustom.api.dashboard.glide;

import android.content.Context;
import android.support.annotation.Nullable;

import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.ModelLoader;

import org.kustom.api.dashboard.preset.PresetFile;

import java.io.InputStream;


public class PresetFileModelLoader implements ModelLoader<PresetFile, InputStream> {
    private final Context mContext;

    PresetFileModelLoader(Context context) {
        mContext = context.getApplicationContext();
    }

    @Nullable
    @Override
    public LoadData<InputStream> buildLoadData(PresetFile file, int w, int h, Options opts) {
        return new LoadData<>(new PresetFileKey(file), new PresetFileDataFetcher(mContext, file));
    }

    @Override
    public boolean handles(PresetFile file) {
        return true;
    }
}
