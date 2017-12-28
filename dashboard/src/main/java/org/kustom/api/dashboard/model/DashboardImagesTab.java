package org.kustom.api.dashboard.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.View;

import org.kustom.api.dashboard.views.DashboardPageImages;

public class DashboardImagesTab extends DashboardTab {
    private final String mUrl;

    public DashboardImagesTab(@NonNull String title, @NonNull String url) {
        super(title);
        mUrl = url;
    }

    @Override
    public View instantiatePage(@NonNull Context context) {
        DashboardPageImages page = new DashboardPageImages(context);
        page.setUrl(mUrl);
        return page;
    }
}
