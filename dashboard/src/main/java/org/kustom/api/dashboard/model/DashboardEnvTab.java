package org.kustom.api.dashboard.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import org.kustom.api.dashboard.config.KustomEnv;
import org.kustom.api.dashboard.views.DashboardPageEnv;

public class DashboardEnvTab extends DashboardTab {
    private final KustomEnv mEnv;

    public DashboardEnvTab(@NonNull KustomEnv env) {
        super(env.getExtension().toUpperCase());
        mEnv = env;
    }

    @Override
    public View instantiatePage(@NonNull Context context) {
        DashboardPageEnv page = new DashboardPageEnv(context);
        page.setEntries(mEnv.getFiles(context));
        return page;
    }
}
