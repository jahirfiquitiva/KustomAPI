package org.kustom.api.dashboard;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;

import static org.kustom.api.dashboard.ActivityUtils.hideFromLauncher;
import static org.kustom.api.dashboard.ActivityUtils.openPkgStoreUri;

public class DashboardActivity
        extends AppCompatActivity
        implements TabLayout.OnTabSelectedListener, FastItemAdapter.OnClickListener<PresetItem> {
    private final static String TAG = DashboardActivity.class.getSimpleName();

    private RecyclerView mList;

    private final FastItemAdapter<PresetItem> mAdapter = new FastItemAdapter<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "Dashboard starting");
        setTheme(ThemeHelper.getThemeResource(this));
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
        mList.setAdapter(mAdapter);

        // Reload
        ((TabLayout) findViewById(R.id.tabs)).addOnTabSelectedListener(this);
        populatePager();
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        if (tab.getText() != null) {
            String ext = tab.getText().toString().toLowerCase();
            KustomConfig.Env env = KustomConfig.getEnv(ext);
            if (env != null) reload(env.getFiles(this));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        for (int i = 0; i < menu.size(); i++) {
            Drawable icon = menu.getItem(i).getIcon();
            if (icon != null) {
                icon.mutate();
                icon.setColorFilter(
                        ThemeHelper.getThemeColor(this, android.R.attr.textColorPrimary),
                        PorterDuff.Mode.SRC_ATOP
                );
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_info) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.kustom_pack_title)
                    .setMessage(R.string.kustom_pack_description)
                    .setNegativeButton(android.R.string.cancel, null)
                    .setPositiveButton(R.string.rate_app, new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            openPkgStoreUri(DashboardActivity.this, getPackageName());
                        }
                    })
                    .setNeutralButton(R.string.hide_from_launcher, new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            hideFromLauncher(DashboardActivity.this, getComponentName());
                            new AlertDialog.Builder(DashboardActivity.this)
                                    .setTitle(R.string.hide_from_launcher)
                                    .setMessage(R.string.hide_from_launcher_done)
                                    .setPositiveButton(android.R.string.ok, null)
                                    .show();
                        }
                    })
                    .show();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
        // Standard presets
        if (pkg != null) {
            if (!PackageHelper.packageInstalled(this, pkg)) {
                new AlertDialog.Builder(this)
                        .setTitle(R.string.kustom_not_installed)
                        .setMessage(R.string.kustom_not_installed_desc)
                        .setNegativeButton(android.R.string.cancel, null)
                        .setPositiveButton(R.string.install, new OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                openPkgStoreUri(DashboardActivity.this, pkg);
                            }
                        })
                        .show();
            } else {
                Intent i = new Intent();
                i.setComponent(new ComponentName(pkg, env.getEditorActivity()));
                i.setData(new Uri.Builder()
                        .scheme("kfile")
                        .authority(String.format("%s.kustom.provider", getPackageName()))
                        .appendPath(item.getPresetFile().getPath())
                        .build());
                startActivity(i);
            }
        }
        // Komponents
        else {
            new AlertDialog.Builder(this)
                    .setTitle("Komponents")
                    .setMessage(R.string.komponent_open)
                    .setPositiveButton(android.R.string.ok, null)
                    .show();
        }
        return true;
    }

    private void populatePager() {
        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        for (String ext : KustomConfig.getExtensions()) {
            KustomConfig.Env env = KustomConfig.getEnv(ext);
            if (env != null) {
                if (env.getFiles(this).length > 0) {
                    tabs.addTab(tabs.newTab().setText(ext.toUpperCase()));
                }
            }
        }
        tabs.setVisibility(tabs.getTabCount() == 1 ? View.GONE : View.VISIBLE);
    }

    private void reload(@NonNull String[] entries) {
        mAdapter.clear();
        for (String entry : entries)
            mAdapter.add(new PresetItem(new PresetFile(entry)).withOnItemClickListener(this));
        mList.setVisibility(entries.length > 0 ? View.VISIBLE : View.GONE);
        findViewById(R.id.text).setVisibility(entries.length > 0 ? View.GONE : View.VISIBLE);
        findViewById(R.id.progress).setVisibility(View.GONE);
    }
}
