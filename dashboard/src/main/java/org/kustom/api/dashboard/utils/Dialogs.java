package org.kustom.api.dashboard.utils;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.support.annotation.NonNull;

import org.kustom.api.dashboard.R;

import static org.kustom.api.dashboard.utils.ActivityUtils.hideFromLauncher;
import static org.kustom.api.dashboard.utils.ActivityUtils.openPkgStoreUri;

@SuppressWarnings("WeakerAccess")
public class Dialogs {

    private Dialogs() {
    }

    public static void showInfoDialog(@NonNull Context context, @NonNull ComponentName component) {
        Context dialogContext = ThemeHelper.getDialogThemedContext(context);
        new AlertDialog.Builder(dialogContext)
                .setTitle(R.string.kustom_pack_title)
                .setMessage(R.string.kustom_pack_description)
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(R.string.rate_app, (dialog, which)
                        -> openPkgStoreUri(dialogContext, context.getPackageName()))
                .setNeutralButton(R.string.hide_from_launcher, (dialog, which) -> {
                    hideFromLauncher(dialogContext, component);
                    new AlertDialog.Builder(dialogContext)
                            .setTitle(R.string.hide_from_launcher)
                            .setMessage(R.string.hide_from_launcher_done)
                            .setPositiveButton(android.R.string.ok, null)
                            .show();
                })
                .show();
    }

    public static void showAppNotInstalledDialog(@NonNull Context context, @NonNull String pkg) {
        new AlertDialog.Builder(ThemeHelper.getDialogThemedContext(context))
                .setTitle(R.string.kustom_not_installed)
                .setMessage(R.string.kustom_not_installed_desc)
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(R.string.install,
                        (dialog, which) -> openPkgStoreUri(context, pkg))
                .show();
    }

    public static void showOpenKomponentDialog(@NonNull Context context) {
        new AlertDialog.Builder(ThemeHelper.getDialogThemedContext(context))
                .setTitle("Komponents")
                .setMessage(R.string.komponent_open)
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }

}
