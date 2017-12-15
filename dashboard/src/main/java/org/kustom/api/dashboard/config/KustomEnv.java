package org.kustom.api.dashboard.config;

import android.content.Context;
import android.content.res.AssetManager;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

public class KustomEnv {
    private final static String TAG = KustomEnv.class.getSimpleName();

    private final String mExtension;
    private final String mFolder;
    private final String mPkg;
    private final String mEditorActivity;
    private String[] mFiles;

    KustomEnv(String extension, String folder, String pkg, String editorActivity) {
        mExtension = extension;
        mFolder = folder;
        mPkg = pkg;
        mEditorActivity = editorActivity;
    }

    public String getExtension() {
        return mExtension;
    }

    public String getFolder() {
        return mFolder;
    }

    public String getPkg() {
        return mPkg;
    }

    public String getEditorActivity() {
        return mEditorActivity;
    }

    public synchronized String[] getFiles(@NonNull Context context) {
        if (mFiles == null) {
            ArrayList<String> result = new ArrayList<>();
            try {
                AssetManager assets = context.getAssets();
                String[] files = assets.list(getFolder());
                if (files != null) {
                    for (String file : files) {
                        if (file.toLowerCase().endsWith(getExtension())
                                || file.toLowerCase().endsWith(getExtension() + ".zip"))
                            result.add(String.format("%s/%s", getFolder(), file));
                    }
                }
            } catch (IOException e) {
                Log.e(TAG, "Unable to list folder: " + getFolder());
            }
            mFiles = result.toArray(new String[result.size()]);
        }
        return mFiles;
    }
}
