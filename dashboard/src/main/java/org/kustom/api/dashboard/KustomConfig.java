package org.kustom.api.dashboard;

import android.content.Context;
import android.content.res.AssetManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

class KustomConfig {
    private final static String TAG = KustomConfig.class.getSimpleName();

    private static final Map<String, Env> sEnvs;

    static {
        HashMap<String, Env> envs = new HashMap<>();
        envs.put("kwgt", new Env(
                "kwgt",
                "widgets",
                "org.kustom.widget",
                "org.kustom.widget.picker.WidgetPicker"
        ));
        envs.put("klwp", new Env(
                "klwp",
                "wallpapers",
                "org.kustom.wallpaper",
                "org.kustom.lib.editor.WpAdvancedEditorActivity"
        ));
        envs.put("klck", new Env(
                "klck",
                "lockscreens",
                "org.kustom.lockscreen",
                "org.kustom.lib.editor.LockAdvancedEditorActivity"
        ));
        envs.put("kwch", new Env(
                "kwch",
                "watches",
                "org.kustom.watch",
                "TBD"
        ));
        envs.put("komp", new Env(
                "komp",
                "komponents",
                null,
                null
        ));
        sEnvs = Collections.unmodifiableMap(envs);
    }

    @NonNull
    static Set<String> getExtensions() {
        return sEnvs.keySet();
    }

    @Nullable
    static Env getEnv(@NonNull String extension) {
        return sEnvs.get(extension);
    }

    static class Env {
        private final String mExtension;
        private final String mFolder;
        private final String mPkg;
        private final String mEditorActivity;
        private String[] mFiles;

        Env(String extension, String folder, String pkg, String editorActivity) {
            mExtension = extension;
            mFolder = folder;
            mPkg = pkg;
            mEditorActivity = editorActivity;
        }

        String getExtension() {
            return mExtension;
        }

        String getFolder() {
            return mFolder;
        }

        String getPkg() {
            return mPkg;
        }

        String getEditorActivity() {
            return mEditorActivity;
        }

        synchronized String[] getFiles(@NonNull Context context) {
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
}
