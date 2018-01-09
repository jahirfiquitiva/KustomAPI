package org.kustom.api.preset.glide;

import android.content.Context;
import android.support.annotation.NonNull;

import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;

import org.kustom.api.preset.PresetFile;

import java.io.InputStream;

public class PresetFileModuleFactory implements ModelLoaderFactory<PresetFile, InputStream> {
    private final Context mContext;

    PresetFileModuleFactory(Context context) {
        mContext = context.getApplicationContext();
    }

    @NonNull
    @Override
    public ModelLoader<PresetFile, InputStream> build(@NonNull MultiModelLoaderFactory unused) {
        return new PresetFileModelLoader(mContext);
    }

    @Override
    public void teardown() {
    }
}
