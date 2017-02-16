package org.kustom.api.dashboard;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;

import org.kustom.api.R;

import java.io.IOException;
import java.util.ArrayList;

public class DashboardActivity
        extends AppCompatActivity
        implements TabLayout.OnTabSelectedListener, FastItemAdapter.OnClickListener<PresetItem> {
    private final static String TAG = DashboardActivity.class.getSimpleName();

    private RecyclerView mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(getResources().getBoolean(R.bool.kustom_dashboard_light_theme)
                ? R.style.KustomDashboardTheme_Light : R.style.KustomDashboardTheme_Dark);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kustom_dashboard_activity);

        // Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.kustom_dashboard_title));
        setSupportActionBar(toolbar);

        // List
        mList = (RecyclerView) findViewById(R.id.list);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int dpWidth = (int) (displayMetrics.widthPixels / displayMetrics.density);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(
                Math.max(2, dpWidth / 180),
                StaggeredGridLayoutManager.VERTICAL
        );
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        mList.setLayoutManager(layoutManager);

        // Reload
        ((TabLayout) findViewById(R.id.tabs)).addOnTabSelectedListener(this);
        populatePager();
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        if (tab.getText() != null) {
            String ext = tab.getText().toString().toLowerCase();
            KustomConfig.Env env = KustomConfig.getEnv(ext);
            if (env != null)
                reload(listFolder(env.getFolder(), env.getExtension()));
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
    }

    @Override
    public boolean onClick(View v, IAdapter<PresetItem> adapter, PresetItem item, int position) {
        KustomConfig.Env env = KustomConfig.getEnv(item.getPresetFile().getExt());
        if (env == null) throw new RuntimeException("Invalid env");
        final String pkg = env.getPkg();

        // Asks to install Kustom
        if (pkg != null && !PackageHelper.packageInstalled(this, pkg)) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.kustom_not_installed)
                    .setMessage(R.string.kustom_not_installed_desc)
                    .setPositiveButton(R.string.install, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Uri uri = Uri.parse("market://details?id=" + pkg);
                            Intent i = new Intent(Intent.ACTION_VIEW, uri);
                            try {
                                startActivity(i);
                            } catch (ActivityNotFoundException e) {
                                Toast.makeText(DashboardActivity.this,
                                        e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, null)
                    .show();
        }

        // Open Kustom
        else {
            Intent i = new Intent();
            i.setComponent(new ComponentName(pkg, env.getEditorActivity()));
            i.setData(new Uri.Builder()
                    .scheme("kfile")
                    .authority(String.format("%s.kustom.provider", getPackageName()))
                    .appendPath(item.getPresetFile().getPath())
                    .build());
            startActivity(i);
        }

        return true;
    }

    private void populatePager() {
        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        for (String ext : KustomConfig.getExtensions()) {
            KustomConfig.Env env = KustomConfig.getEnv(ext);
            if (env != null) {
                if (listFolder(env.getFolder(), env.getExtension()).length > 0) {
                    tabs.addTab(tabs.newTab().setText(ext.toUpperCase()));
                }
            }
        }
        tabs.setVisibility(tabs.getTabCount() == 1 ? View.GONE : View.VISIBLE);
    }

    private void reload(@NonNull String[] entries) {
        FastItemAdapter<PresetItem> adapter = new FastItemAdapter<>();
        for (String entry : entries)
            adapter.add(new PresetItem(new PresetFile(entry)).withOnItemClickListener(this));
        mList.setVisibility(adapter.getItemCount() > 0 ? View.VISIBLE : View.GONE);
        findViewById(R.id.text).setVisibility(adapter.getItemCount() > 0 ? View.GONE : View.VISIBLE);
        findViewById(R.id.progress).setVisibility(View.GONE);
        mList.setAdapter(adapter);
    }

    @NonNull
    private String[] listFolder(@NonNull String folder, @NonNull String extension) {
        ArrayList<String> result = new ArrayList<>();
        try {
            AssetManager assets = getAssets();
            String[] files = assets.list(folder);
            if (files != null) {
                for (String file : files) {
                    if (file.toLowerCase().endsWith(extension)
                            || file.toLowerCase().endsWith(extension + ".zip"))
                        result.add(String.format("%s/%s", folder, file));
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "Unable to list folder: " + folder);
        }
        return result.toArray(new String[result.size()]);
    }
}
