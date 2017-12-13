package org.kustom.api.dashboard;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.FrameLayout;

import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.mikepenz.fastadapter.listeners.OnClickListener;

import static org.kustom.api.dashboard.ActivityUtils.openPkgStoreUri;

public class DashboardPage extends FrameLayout implements OnClickListener<PresetItem> {

    public DashboardPage(@NonNull Context context) {
        super(context);
    }

    public DashboardPage(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DashboardPage(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setEntries(String[] entries) {
        RecyclerView list = findViewById(R.id.list);
        FastItemAdapter<PresetItem> adapter = new FastItemAdapter<>();
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int dpWidth = (int) (displayMetrics.widthPixels / displayMetrics.density);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(
                Math.max(2, dpWidth / 180),
                StaggeredGridLayoutManager.VERTICAL
        );
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        list.setLayoutManager(layoutManager);
        list.setAdapter(adapter);
        for (String entry : entries)
            adapter.add(new PresetItem(new PresetFile(entry)).withOnItemClickListener(this));
        list.setVisibility(entries.length > 0 ? View.VISIBLE : View.GONE);
        findViewById(R.id.text).setVisibility(entries.length > 0 ? View.GONE : View.VISIBLE);
        findViewById(R.id.progress).setVisibility(View.GONE);
    }

    @Override
    public boolean onClick(View v, IAdapter<PresetItem> adapter, PresetItem item, int position) {
        final Context context = getContext();
        KustomConfig.Env env = KustomConfig.getEnv(item.getPresetFile().getExt());
        if (env == null) throw new RuntimeException("Invalid env");
        final String pkg = env.getPkg();
        // Standard presets
        if (pkg != null) {
            if (!PackageHelper.packageInstalled(context, pkg)) {
                new AlertDialog.Builder(ThemeHelper.getDialogThemedContext(context))
                        .setTitle(R.string.kustom_not_installed)
                        .setMessage(R.string.kustom_not_installed_desc)
                        .setNegativeButton(android.R.string.cancel, null)
                        .setPositiveButton(R.string.install,
                                (dialog, which) -> openPkgStoreUri(context, pkg))
                        .show();
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
        else {
            new AlertDialog.Builder(ThemeHelper.getDialogThemedContext(context))
                    .setTitle("Komponents")
                    .setMessage(R.string.komponent_open)
                    .setPositiveButton(android.R.string.ok, null)
                    .show();
        }
        return true;
    }

}
