package org.kustom.api.dashboard.views;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.mikepenz.fastadapter.listeners.OnClickListener;

import org.kustom.api.dashboard.R;

import java.util.List;

public abstract class DashboardPage<T extends IItem>
        extends FrameLayout
        implements OnClickListener<T> {

    public DashboardPage(@NonNull Context context) {
        super(context);
        inflate(getContext(), R.layout.kustom_dashboard_page, this);
    }

    @Override
    public boolean onClick(View v, IAdapter<T> adapter, T item, int position) {
        return onClick(item);
    }

    protected abstract boolean onClick(T iItem);

    protected void setEntries(List<T> items) {
        if (items.size() == 0) setText("No Results");
        else {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(() -> {
                findViewById(R.id.progress).setVisibility(View.GONE);
                findViewById(R.id.text).setVisibility(View.GONE);
                RecyclerView list = findViewById(R.id.list);
                list.setVisibility(View.VISIBLE);
                FastItemAdapter<T> adapter = new FastItemAdapter<>();
                list.setLayoutManager(getDefaultLayoutManager());
                list.setAdapter(adapter);
                adapter.withOnClickListener(this);
                adapter.add(items);
            });
        }
    }

    protected void setText(String text) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> {
            findViewById(R.id.list).setVisibility(GONE);
            findViewById(R.id.text).setVisibility(VISIBLE);
            findViewById(R.id.progress).setVisibility(GONE);
            ((TextView) findViewById(R.id.text)).setText(text);
        });
    }

    private RecyclerView.LayoutManager getDefaultLayoutManager() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int dpWidth = (int) (displayMetrics.widthPixels / displayMetrics.density);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(
                Math.max(2, dpWidth / 180),
                StaggeredGridLayoutManager.VERTICAL
        );
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        return layoutManager;
    }
}