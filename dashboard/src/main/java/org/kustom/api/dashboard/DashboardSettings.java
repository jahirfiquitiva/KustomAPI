package org.kustom.api.dashboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.BoolRes;
import android.support.annotation.StringRes;

@SuppressWarnings("WeakerAccess")
public class DashboardSettings {
    private final static String PREF_COMPACT = "compact";
    private final static String PREF_LAST_PAGE = "last_page";

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

    public int getLastPageIndex() {
        return getPrefs().getInt(PREF_LAST_PAGE, 0);
    }

    public void setLastPageIndex(int index) {
        getPrefs().edit().putInt(PREF_LAST_PAGE, index).apply();
    }

    public boolean dynamicItemsColors() {
        return getBoolean(R.bool.kustom_dashboard_adaptive_item_color);
    }

    public boolean useCompactView() {
        SharedPreferences preferences = getPrefs();
        if (preferences.contains(PREF_COMPACT))
            return preferences.getBoolean(PREF_COMPACT, false);
        return getBoolean(R.bool.kustom_dashboard_compact_view);
    }

    public void setCompactView(boolean value) {
        getPrefs().edit().putBoolean(PREF_COMPACT, value).apply();
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

    private SharedPreferences getPrefs() {
        return mContext.getSharedPreferences("settings", 0);
    }
}
