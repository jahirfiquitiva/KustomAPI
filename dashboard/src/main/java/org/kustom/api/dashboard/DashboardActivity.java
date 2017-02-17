package org.kustom.api.dashboard;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;

public class DashboardActivity
        extends AppCompatActivity
        implements TabLayout.OnTabSelectedListener, FastItemAdapter.OnClickListener<PresetItem> {
    private final static String TAG = DashboardActivity.class.getSimpleName();

    private RecyclerView mList;

    private final FastItemAdapter<PresetItem> mAdapter = new FastItemAdapter<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
            new MaterialDialog.Builder(this)
                    .title(R.string.kustom_pack_title)
                    .content(R.string.kustom_pack_description)
                    .negativeText(android.R.string.cancel)
                    .neutralText(R.string.hide_from_launcher)
                    .positiveText(R.string.rate_app)
                    .onNeutral(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog d, @NonNull DialogAction w) {
                            ActivityUtils.hideFromLauncher(DashboardActivity.this, getComponentName());
                            new MaterialDialog.Builder(DashboardActivity.this)
                                    .title(R.string.hide_from_launcher)
                                    .content(R.string.hide_from_launcher_done)
                                    .positiveText(android.R.string.ok)
                                    .show();
                        }
                    })
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog d, @NonNull DialogAction w) {
                            ActivityUtils.openPkgStoreUri(DashboardActivity.this, getPackageName());
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
                new MaterialDialog.Builder(this)
                        .title(R.string.kustom_not_installed)
                        .content(R.string.kustom_not_installed_desc)
                        .negativeText(android.R.string.cancel)
                        .positiveText(R.string.install)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog d, @NonNull DialogAction w) {
                                ActivityUtils.openPkgStoreUri(DashboardActivity.this, pkg);
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
            new MaterialDialog.Builder(this)
                    .title("Komponents")
                    .content(R.string.komponent_open)
                    .negativeText(android.R.string.ok)
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
