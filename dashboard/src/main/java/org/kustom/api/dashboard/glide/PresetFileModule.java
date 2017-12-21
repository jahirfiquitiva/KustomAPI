package org.kustom.api.dashboard.glide;

import android.content.Context;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.LibraryGlideModule;

import org.kustom.api.dashboard.model.PresetFile;

import java.io.InputStream;

@GlideModule
public class PresetFileModule extends LibraryGlideModule {
    private final static String TAG = PresetFileModule.class.getSimpleName();

    @Override
    public void registerComponents(Context ctx, Glide glide, Registry registry) {
        Log.i(TAG, "Registering PresetFile module");
        registry.prepend(PresetFile.class, InputStream.class, new PresetFileModuleFactory(ctx));
    }
}
