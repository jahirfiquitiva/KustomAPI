package org.kustom.api.dashboard.model;

import android.graphics.Bitmap;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikepenz.fastadapter.IClickable;
import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.items.AbstractItem;

import org.kustom.api.dashboard.DashboardSettings;
import org.kustom.api.dashboard.R;

public abstract class DashboardItem<Item extends IItem & IClickable>
        extends AbstractItem<Item, DashboardItem.ViewHolder> {

    @Override
    public final int getType() {
        return R.id.kustom_dashboard_id_item;
    }

    @Override
    public final int getLayoutRes() {
        return R.layout.kustom_dashboard_list_item;
    }

    @Override
    public final ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }

    @SuppressWarnings("WeakerAccess")
    protected static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView mTitle;
        private final TextView mAuthor;
        protected final ImageView mPreview;

        public ViewHolder(View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.title);
            mAuthor = itemView.findViewById(R.id.author);
            mPreview = itemView.findViewById(R.id.preview);
        }

        final void onBitmapSet(Bitmap bitmap) {
            if (DashboardSettings.get(itemView.getContext()).dynamicItemsColors()) {
                Palette.from(bitmap).generate(palette -> {
                    Palette.Swatch vibrant = palette.getVibrantSwatch();
                    if (vibrant != null) {
                        mTitle.setBackgroundColor(vibrant.getRgb());
                        mTitle.setTextColor(vibrant.getBodyTextColor());
                        mAuthor.setBackgroundColor(vibrant.getRgb());
                        mAuthor.setTextColor(vibrant.getTitleTextColor());
                    }
                });
            }
        }

        final void setTitle(String text) {
            mTitle.setText(text);
        }

        final void setAuthor(String text) {
            mAuthor.setText(text);
            mAuthor.setVisibility(TextUtils.isEmpty(text) ? View.GONE : View.VISIBLE);
        }
    }
}
