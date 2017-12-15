package org.kustom.api.dashboard;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;

import com.astuetz.PagerSlidingTabStrip;

import org.kustom.api.dashboard.config.KustomConfig;
import org.kustom.api.dashboard.config.KustomEnv;
import org.kustom.api.dashboard.model.DashboardEnvTab;
import org.kustom.api.dashboard.model.DashboardImagesTab;
import org.kustom.api.dashboard.model.DashboardTab;
import org.kustom.api.dashboard.utils.Dialogs;
import org.kustom.api.dashboard.utils.ThemeHelper;
import org.kustom.api.dashboard.views.DashboardPage;

import java.util.ArrayList;

public class DashboardActivity extends Activity {
    private final static String TAG = DashboardActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "Dashboard starting");
        setTheme(ThemeHelper.getThemeResource(this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kustom_dashboard_activity);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.kustom_dashboard_title));
        setActionBar(toolbar);

        // Reload
        new TabLoaderTask().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dashboard, menu);
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
            Dialogs.showInfoDialog(this, getComponentName());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("StaticFieldLeak")
    private class TabLoaderTask extends AsyncTask<Void, Void, DashboardTab[]> {

        @Override
        protected DashboardTab[] doInBackground(Void... voids) {
            ArrayList<DashboardTab> envs = new ArrayList<>();
            for (String ext : KustomConfig.getExtensions()) {
                KustomEnv env = KustomConfig.getEnv(ext);
                if (env != null) {
                    if (env.getFiles(DashboardActivity.this).length > 0) {
                        envs.add(new DashboardEnvTab(env));
                    }
                }
            }
            if (getResources().getBoolean(R.bool.kustom_dashboard_walls)) {
                String url = getString(R.string.kustom_dashboard_walls_url);
                if (!TextUtils.isEmpty(url.trim())) {
                    envs.add(new DashboardImagesTab("WALLS", url));
                }
            }
            return envs.toArray(new DashboardTab[envs.size()]);
        }

        @Override
        protected void onPostExecute(DashboardTab[] tabs) {
            super.onPostExecute(tabs);
            ViewPager pager = findViewById(R.id.pager);
            pager.setAdapter(new KustomEnvPagerAdapter(tabs));
            PagerSlidingTabStrip strip = findViewById(R.id.tabs);
            strip.setViewPager(pager);
            strip.setVisibility(tabs.length == 1 ? View.GONE : View.VISIBLE);
            if (getIntent() != null
                    && tabs.length > 0 && tabs[tabs.length - 1] instanceof DashboardImagesTab
                    && Intent.ACTION_SET_WALLPAPER.equals(getIntent().getAction())) {
                pager.setCurrentItem(tabs.length - 1);
            }
        }
    }

    private class KustomEnvPagerAdapter extends PagerAdapter {
        private final DashboardTab[] mTabs;

        KustomEnvPagerAdapter(DashboardTab[] tabs) {
            mTabs = tabs;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup parent, int pos) {
            View page = mTabs[pos].instantiatePage(DashboardActivity.this);
            parent.addView(page);
            return page;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((DashboardPage) object);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTabs[position].getTitle();
        }

        @Override
        public int getCount() {
            return mTabs.length;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }
    }
}
