package org.kustom.api.dashboard;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;

class ThemeHelper {

    static int getThemeColor(@NonNull Context context, @AttrRes int attr) {
        TypedArray a = context.getTheme().obtainStyledAttributes(
                getThemeResource(context),
                new int[]{attr}
        );
        int color = a.getColor(0, 0);
        a.recycle();
        return color;
    }

    static int getThemeResource(@NonNull Context context) {
        return context.getResources().getBoolean(R.bool.kustom_dashboard_light_theme)
                ? R.style.KustomDashboardTheme_Light : R.style.KustomDashboardTheme_Dark;
    }
}
