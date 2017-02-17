package org.kustom.api.dashboard;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

class KustomConfig {

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
                "TBD"
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

        public Env(String extension, String folder, String pkg, String editorActivity) {
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
    }
}
