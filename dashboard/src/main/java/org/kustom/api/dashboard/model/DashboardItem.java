package org.kustom.api.dashboard.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.CallSuper;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikepenz.fastadapter.IClickable;
import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.items.AbstractItem;

import org.kustom.api.dashboard.DashboardSettings;
import org.kustom.api.dashboard.R;
import org.kustom.api.dashboard.views.AspectRatioImageView;

import java.util.List;

public abstract class DashboardItem<Item extends IItem & IClickable>
        extends AbstractItem<Item, DashboardItem.ViewHolder> {
    private final float mScreenRatio;

    DashboardItem(float screenRatio) {
        mScreenRatio = screenRatio;
    }

    @Override
    public final int getType() {
        return R.id.kustom_dashboard_id_item;
    }

    @Override
    public final ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }

    @CallSuper
    @Override
    public void bindView(ViewHolder holder, List<Object> payloads) {
        super.bindView(holder, payloads);
        int padding = (int) getImageViewPadding(holder.itemView.getContext());
        holder.mInfo.setAlpha(hasTranslucentInfo() ? 0.8f : 1.0f);
        holder.mPreview.setPadding(padding, padding, padding, padding);
        holder.mPreview.setAspectRatio(getImageViewRatio());
        holder.mPreview.setScaleType(getImageScaleType());
    }

    boolean hasTranslucentInfo() {
        return true;
    }

    float getImageViewRatio() {
        return mScreenRatio;
    }

    boolean isLandscape() {
        return mScreenRatio < 1f;
    }

    float getImageViewPadding(Context context) {
        return 0;
    }

    ImageView.ScaleType getImageScaleType() {
        return ImageView.ScaleType.CENTER_CROP;
    }

    @SuppressWarnings("WeakerAccess")
    protected static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView mTitle;
        private final TextView mAuthor;
        private final View mInfo;
        protected final AspectRatioImageView mBackground;
        protected final AspectRatioImageView mPreview;

        public ViewHolder(View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.title);
            mAuthor = itemView.findViewById(R.id.author);
            mPreview = itemView.findViewById(R.id.preview);
            mInfo = itemView.findViewById(R.id.info);
            mBackground = itemView.findViewById(R.id.background);
        }

        final void onBitmapSet(Bitmap bitmap, boolean ignorePalette) {
            Context context = itemView.getContext();
            if (!ignorePalette && DashboardSettings.get(context).dynamicItemsColors()) {
                Palette.from(bitmap).generate(palette -> {
                    Palette.Swatch muted = palette.getMutedSwatch();
                    if (muted != null) {
                        mInfo.setBackgroundColor(muted.getRgb());
                        mTitle.setTextColor(muted.getBodyTextColor());
                        mAuthor.setTextColor(muted.getTitleTextColor());
                    }
                });
            }
        }

        final void setTitle(String text) {
            mTitle.setText(text);
        }

        final void setAuthor(String text) {
            mAuthor.setText(text);
        }
    }
}