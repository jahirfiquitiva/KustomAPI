package org.kustom.api.dashboard;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;

import com.astuetz.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

import static org.kustom.api.dashboard.ActivityUtils.hideFromLauncher;
import static org.kustom.api.dashboard.ActivityUtils.openPkgStoreUri;

public class DashboardActivity extends Activity {
    private final static String TAG = DashboardActivity.class.getSimpleName();

    private String[] mActiveEnvs = new String[0];

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
            Dialogs.showInfoDialog(this, getComponentName());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("StaticFieldLeak")
    private class TabLoaderTask extends AsyncTask<Void, Void, List<String>> {

        @Override
        protected List<String> doInBackground(Void... voids) {
            ArrayList<String> envs = new ArrayList<>();
            for (String ext : KustomConfig.getExtensions()) {
                KustomConfig.Env env = KustomConfig.getEnv(ext);
                if (env != null) {
                    if (env.getFiles(DashboardActivity.this).length > 0) {
                        envs.add(ext.toUpperCase());
                    }
                }
            }
            return envs;
        }

        @Override
        protected void onPostExecute(List<String> result) {
            super.onPostExecute(result);
            mActiveEnvs = result.toArray(new String[result.size()]);
            ViewPager pager = findViewById(R.id.pager);
            pager.setAdapter(new KustomEnvPagerAdapter(mActiveEnvs));
            PagerSlidingTabStrip tabs = findViewById(R.id.tabs);
            tabs.setViewPager(pager);
            tabs.setVisibility(mActiveEnvs.length == 1 ? View.GONE : View.VISIBLE);
        }
    }

    private class KustomEnvPagerAdapter extends PagerAdapter {
        private final String[] mTitles;

        KustomEnvPagerAdapter(String[] titles) {
            mTitles = titles;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup collection, int position) {
            LayoutInflater inflater = LayoutInflater.from(DashboardActivity.this);
            DashboardPage page = (DashboardPage) inflater
                    .inflate(R.layout.kustom_dashboard_page, collection, false);
            String ext = mActiveEnvs[position].toLowerCase();
            KustomConfig.Env env = KustomConfig.getEnv(ext);
            if (env != null) page.setEntries(env.getFiles(DashboardActivity.this));
            collection.addView(page);
            return page;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((DashboardPage) object);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public int getCount() {
            return mTitles.length;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }
    }
}
