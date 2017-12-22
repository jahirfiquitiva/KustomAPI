package org.kustom.api.dashboard.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.transition.Transition;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ImageItem extends DashboardItem<ImageItem> {
    private final ImageData mImageData;

    public ImageItem(@NonNull JSONObject image) throws JSONException {
        mImageData = new ImageData(image);
    }

    public ImageData getImageData() {
        return mImageData;
    }

    @Override
    public void bindView(ViewHolder holder, List<Object> payloads) {
        super.bindView(holder, payloads);
        Context context = holder.itemView.getContext();
        holder.setTitle(mImageData.getTitle());
        holder.setAuthor(mImageData.getAuthor());
        Glide.with(context)
                .asBitmap()
                .load(mImageData.getThumbUrl())
                .into(new BitmapImageViewTarget(holder.mPreview) {
                    @Override
                    public void onResourceReady(Bitmap r, @Nullable Transition<? super Bitmap> t) {
                        super.onResourceReady(r, t);
                        holder.onBitmapSet(r);
                    }
                });
    }
}
