package org.kustom.api.dashboard.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

public abstract class DashboardTab {
    private final String mTitle;

    DashboardTab(String title) {
        mTitle = title;
    }

    public final String getTitle() {
        return mTitle;
    }

    public abstract View instantiatePage(@NonNull Context context);
}
