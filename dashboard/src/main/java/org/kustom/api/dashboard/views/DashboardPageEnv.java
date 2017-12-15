package org.kustom.api.dashboard.views;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import org.kustom.api.dashboard.config.KustomEnv;
import org.kustom.api.dashboard.utils.Dialogs;
import org.kustom.api.dashboard.config.KustomConfig;
import org.kustom.api.dashboard.model.PresetItem;
import org.kustom.api.dashboard.model.PresetFile;
import org.kustom.api.dashboard.utils.PackageHelper;

import java.util.ArrayList;

public class DashboardPageEnv extends DashboardPage<PresetItem> {

    public DashboardPageEnv(@NonNull Context context) {
        super(context);
    }

    public void setEntries(String[] entries) {
        ArrayList<PresetItem> items = new ArrayList<>();
        for (String entry : entries) items.add(new PresetItem(new PresetFile(entry)));
        setEntries(items);
    }

    @Override
    protected boolean onClick(PresetItem item) {
        final Context context = getContext();
        KustomEnv env = KustomConfig.getEnv(item.getPresetFile().getExt());
        if (env == null) throw new RuntimeException("Invalid env");
        final String pkg = env.getPkg();
        // Standard presets
        if (pkg != null) {
            if (!PackageHelper.packageInstalled(context, pkg)) {
                Dialogs.showAppNotInstalledDialog(context, pkg);
            } else {
                Intent i = new Intent();
                i.setComponent(new ComponentName(pkg, env.getEditorActivity()));
                i.setData(new Uri.Builder()
                        .scheme("kfile")
                        .authority(String.format("%s.kustom.provider", context.getPackageName()))
                        .appendPath(item.getPresetFile().getPath())
                        .build());
                context.startActivity(i);
            }
        }
        // Komponents
        else Dialogs.showOpenKomponentDialog(context);
        return true;
    }
}
