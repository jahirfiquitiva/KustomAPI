package org.kustom.api.dashboard.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.transition.Transition;

import org.kustom.api.preset.PresetFile;
import org.kustom.api.preset.PresetInfo;
import org.kustom.api.preset.PresetInfoLoader;

import java.util.List;

public class PresetItem extends DashboardItem<PresetItem> implements Comparable<PresetItem> {
    private final PresetFile mPresetFile;

    public PresetItem(@NonNull PresetFile presetFile) {
        mPresetFile = presetFile;
    }

    @NonNull
    public PresetFile getPresetFile() {
        return mPresetFile;
    }

    @Override
    public int compareTo(@NonNull PresetItem o) {
        return mPresetFile.getName().compareTo(o.mPresetFile.getName());
    }

    @Override
    public void bindView(ViewHolder holder, List<Object> payloads) {
        super.bindView(holder, payloads);
        Context context = holder.itemView.getContext();
        holder.setTitle(mPresetFile.getName());
        Glide.with(context)
                .asBitmap()
                .load(mPresetFile)
                .into(new BitmapImageViewTarget(holder.mPreview) {
                    @Override
                    public void onResourceReady(Bitmap r, @Nullable Transition<? super Bitmap> t) {
                        super.onResourceReady(r, t);
                        holder.onBitmapSet(r);
                    }
                });
        PresetInfoLoader.create(mPresetFile)
                .load(context, info -> {
                    holder.setTitle(info.getTitle());
                    holder.setAuthor(info.getAuthor());
                });
    }
}
