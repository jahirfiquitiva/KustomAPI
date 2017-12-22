package org.kustom.api.dashboard;

import android.content.Context;
import android.support.annotation.BoolRes;
import android.support.annotation.StringRes;

@SuppressWarnings("WeakerAccess")
public class DashboardSettings {
    private final Context mContext;

    private DashboardSettings(Context context) {
        mContext = context;
    }

    public static DashboardSettings get(Context context) {
        return new DashboardSettings(context);
    }

    public boolean wallsEnabled() {
        return getBoolean(R.bool.kustom_dashboard_walls);
    }

    public boolean dynamicItemsColors() {
        return getBoolean(R.bool.kustom_dashboard_adaptive_item_color);
    }

    public String wallsUrl() {
        return getString(R.string.kustom_dashboard_walls_url);
    }

    public String dashboardTitle() {
        return getString(R.string.kustom_dashboard_title);
    }

    private boolean getBoolean(@BoolRes int id) {
        return mContext.getResources().getBoolean(id);
    }

    private String getString(@StringRes int id) {
        return mContext.getResources().getString(id);
    }
}
