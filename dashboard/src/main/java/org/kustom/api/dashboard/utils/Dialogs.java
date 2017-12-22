package org.kustom.api.dashboard.utils;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import org.kustom.api.dashboard.R;

import static org.kustom.api.dashboard.utils.ActivityUtils.hideFromLauncher;
import static org.kustom.api.dashboard.utils.ActivityUtils.openPkgStoreUri;

@SuppressWarnings("WeakerAccess")
public class Dialogs {

    private Dialogs() {
    }

    public static void showInfoDialog(@NonNull Context context, @NonNull ComponentName component) {
        new Dialog.Builder(context)
                .setTitle(R.string.kustom_pack_title)
                .setContent(R.string.kustom_pack_description)
                .setNegativeText(android.R.string.cancel)
                .setPositiveText(R.string.rate_app)
                .setNeutralText(R.string.hide_from_launcher)
                .setButtonCallback((view, id) -> {
                    if (id == Dialog.BUTTON_POSITIVE)
                        openPkgStoreUri(view.getContext(), context.getPackageName());
                    else if (id == Dialog.BUTTON_NEUTRAL) {
                        hideFromLauncher(view.getContext(), component);
                        new Dialog.Builder(view.getContext())
                                .setTitle(R.string.hide_from_launcher)
                                .setContent(R.string.hide_from_launcher_done)
                                .setPositiveText(android.R.string.ok)
                                .show();
                    }
                })
                .show();
    }

    public static void showAppNotInstalledDialog(@NonNull Context context, @NonNull String pkg) {
        new Dialog.Builder(context)
                .setTitle(R.string.kustom_not_installed)
                .setContent(R.string.kustom_not_installed_desc)
                .setNegativeText(android.R.string.cancel)
                .setPositiveText(R.string.install)
                .setButtonCallback((view, id) -> {
                    if (id == Dialog.BUTTON_POSITIVE)
                        openPkgStoreUri(context, pkg);
                })
                .show();
    }

    public static void showOpenKomponentDialog(@NonNull Context context) {
        new Dialog.Builder(context)
                .setTitle("Komponents")
                .setContent(R.string.komponent_open)
                .setPositiveText(android.R.string.ok)
                .show();
    }

}
