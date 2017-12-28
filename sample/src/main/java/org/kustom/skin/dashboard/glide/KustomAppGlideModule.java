package org.kustom.skin.dashboard.glide;

import android.content.Context;
import android.util.Log;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

import org.kustom.api.sample.BuildConfig;

@GlideModule
public class KustomAppGlideModule extends AppGlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        builder.setLogLevel(BuildConfig.DEBUG ? Log.VERBOSE : Log.WARN);
    }

}
