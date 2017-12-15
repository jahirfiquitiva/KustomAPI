package org.kustom.api.dashboard.model;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.mikepenz.fastadapter.items.AbstractItem;

import org.json.JSONException;
import org.json.JSONObject;
import org.kustom.api.dashboard.R;

import java.util.List;

public class ImageItem extends AbstractItem<ImageItem, ImageItem.ViewHolder> {
    private final ImageData mImageData;

    public ImageItem(@NonNull JSONObject image) throws JSONException {
        mImageData = new ImageData(image);
    }

    public ImageData getImageData() {
        return mImageData;
    }

    @Override
    public int getType() {
        return R.id.kustom_dashboard_id_item;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.kustom_dashboard_list_item;
    }

    @Override
    public void bindView(ViewHolder holder, List<Object> payloads) {
        super.bindView(holder, payloads);
        Context context = holder.itemView.getContext();
        holder.mTitle.setText(mImageData.getTitle());
        Glide.with((Activity) context)
                .load(mImageData.getThumbUrl())
                .asBitmap()
                .into(new BitmapImageViewTarget(holder.mPreview) {
                    @Override
                    public void onResourceReady(Bitmap r, GlideAnimation<? super Bitmap> a) {
                        super.onResourceReady(r, a);
                        Palette.from(r).generate(palette -> {
                            Palette.Swatch vibrant = palette.getVibrantSwatch();
                            if (vibrant != null) {
                                holder.mTitle.setBackgroundColor(vibrant.getRgb());
                                holder.mTitle.setTextColor(vibrant.getTitleTextColor());
                            }
                        });
                    }
                });
    }

    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }

    @SuppressWarnings("WeakerAccess")
    protected static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView mTitle;
        private final ImageView mPreview;

        public ViewHolder(View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.title);
            mPreview = itemView.findViewById(R.id.preview);
        }
    }
}
