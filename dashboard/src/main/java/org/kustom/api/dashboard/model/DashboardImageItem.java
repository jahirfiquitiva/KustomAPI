package org.kustom.api.dashboard.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.transition.Transition;

import org.json.JSONException;
import org.json.JSONObject;
import org.kustom.api.dashboard.R;

import java.util.List;

public class DashboardImageItem extends DashboardItem<DashboardImageItem> {
    private final ImageData mImageData;

    public DashboardImageItem(@NonNull JSONObject image, float screenRatio) throws JSONException {
        super(screenRatio);
        mImageData = new ImageData(image);
    }

    public ImageData getImageData() {
        return mImageData;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.kustom_dashboard_list_item_wallpaper;
    }

    @Override
    boolean hasTranslucentInfo() {
        return true;
    }

    @Override
    public void bindView(ViewHolder holder, List<Object> payloads) {
        super.bindView(holder, payloads);
        Context context = holder.itemView.getContext();
        holder.setTitle(mImageData.getTitle());
        holder.setAuthor(mImageData.getAuthor());
        holder.mBackground.setImageBitmap(null);
        holder.mBackground.setVisibility(View.GONE);
        Glide.with(context)
                .asBitmap()
                .load(mImageData.getThumbUrl())
                .into(new BitmapImageViewTarget(holder.mPreview) {
                    @Override
                    public void onResourceReady(Bitmap r, @Nullable Transition<? super Bitmap> t) {
                        super.onResourceReady(r, t);
                        holder.onBitmapSet(r, false);
                    }
                });
    }
}
