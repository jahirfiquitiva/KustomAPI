package org.kustom.api.dashboard.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.transition.Transition;

import org.kustom.api.dashboard.R;
import org.kustom.api.dashboard.config.KustomConfig;
import org.kustom.api.dashboard.utils.ScreenUtils;
import org.kustom.api.dashboard.utils.WallpaperBitmapLoader;
import org.kustom.api.dashboard.views.AspectRatioImageView;
import org.kustom.api.preset.PresetFile;
import org.kustom.api.preset.PresetInfoLoader;

import java.util.List;

public class DashboardPresetItem
        extends DashboardItem<DashboardPresetItem>
        implements Comparable<DashboardPresetItem> {
    private final PresetFile mPresetFile;
    private final boolean mUseWidgetLayout;

    public DashboardPresetItem(@NonNull PresetFile presetFile, float screenRatio) {
        super(screenRatio);
        mPresetFile = presetFile;
        mUseWidgetLayout = KustomConfig.ENV_KWGT.getExtension().equals(mPresetFile.getExt())
                || KustomConfig.ENV_KOMP.getExtension().equals(mPresetFile.getExt());
    }

    @NonNull
    public PresetFile getPresetFile() {
        return mPresetFile;
    }

    @Override
    public int compareTo(@NonNull DashboardPresetItem o) {
        return mPresetFile.getName().compareTo(o.mPresetFile.getName());
    }

    @Override
    public final int getLayoutRes() {
        return useWidgetLayout()
                ? R.layout.kustom_dashboard_list_item_widget
                : R.layout.kustom_dashboard_list_item_wallpaper;
    }

    @Override
    boolean hasTranslucentInfo() {
        return !useWidgetLayout();
    }

    @Override
    float getImageViewRatio() {
        return useWidgetLayout() ? 1f : super.getImageViewRatio();
    }

    @Override
    float getImageViewPadding(Context context) {
        return useWidgetLayout() ? ScreenUtils.convertDpToPixel(20, context) : 0;
    }

    @Override
    ImageView.ScaleType getImageScaleType() {
        return useWidgetLayout() ? ImageView.ScaleType.FIT_CENTER : super.getImageScaleType();
    }

    private boolean useWidgetLayout() {
        return mUseWidgetLayout;
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
                        holder.onBitmapSet(r, useWidgetLayout());
                    }
                });
        PresetInfoLoader.create(mPresetFile)
                .load(context, info -> {
                    if (info != null) {
                        holder.setTitle(info.getTitle());
                        holder.setAuthor(info.getAuthor());
                    }
                });
        if (useWidgetLayout()) {
            WallpaperBitmapLoader.create()
                    .load(context, bitmap -> {
                        if (bitmap != null)
                            holder.mBackground.setImageBitmap(bitmap);
                    });
        }
    }
}