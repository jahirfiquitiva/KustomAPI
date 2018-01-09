package org.kustom.api.preset.glide;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bumptech.glide.annotation.GlideOption;
import com.bumptech.glide.load.Option;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.request.RequestOptions;

import org.kustom.api.preset.PresetFile;

import java.io.InputStream;


public class PresetFileModelLoader implements ModelLoader<PresetFile, InputStream> {
    private final Context mContext;

    private static final String KEY_ORIENTATION_LAND = "org.kustom.glide.load.orientation";

    /**
     * A boolean option that, if set to <code>true</code> causes the loader to prefer landscape
     * thumbnails (this applies only if there is actually a landscape thumbnail, not to komps)
     */
    @SuppressWarnings("WeakerAccess")
    public static final Option<Boolean> ORIENTATION_LAND
            = Option.disk(KEY_ORIENTATION_LAND, false, (keyBytes, value, digest) -> {
        if (value) digest.update(keyBytes);
    });

    PresetFileModelLoader(Context context) {
        mContext = context.getApplicationContext();
    }

    @Nullable
    @Override
    public LoadData<InputStream> buildLoadData(@NonNull PresetFile file, int w, int h, @NonNull Options opts) {
        return new LoadData<>(
                new PresetFileKey(file),
                new PresetFileDataFetcher(mContext, file)
                        .setLandscape(opts.get(ORIENTATION_LAND))
        );
    }

    @Override
    public boolean handles(@NonNull PresetFile file) {
        return true;
    }

    @SuppressWarnings("unused")
    @SuppressLint("CheckResult")
    @GlideOption
    public static void landscape(RequestOptions options, boolean value) {
        options.set(ORIENTATION_LAND, value);
    }
}