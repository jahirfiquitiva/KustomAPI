package org.kustom.api.dashboard.config;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class KustomConfig {
    private final static String TAG = KustomConfig.class.getSimpleName();

    private static final Map<String, KustomEnv> sEnvs;

    static {
        HashMap<String, KustomEnv> envs = new HashMap<>();
        envs.put("kwgt", new KustomEnv(
                "kwgt",
                "widgets",
                "org.kustom.widget",
                "org.kustom.widget.picker.WidgetPicker"
        ));
        envs.put("klwp", new KustomEnv(
                "klwp",
                "wallpapers",
                "org.kustom.wallpaper",
                "org.kustom.lib.editor.WpAdvancedEditorActivity"
        ));
        envs.put("klck", new KustomEnv(
                "klck",
                "lockscreens",
                "org.kustom.lockscreen",
                "org.kustom.lib.editor.LockAdvancedEditorActivity"
        ));
        envs.put("kwch", new KustomEnv(
                "kwch",
                "watches",
                "org.kustom.watch",
                "TBD"
        ));
        envs.put("komp", new KustomEnv(
                "komp",
                "komponents",
                null,
                null
        ));
        sEnvs = Collections.unmodifiableMap(envs);
    }

    private KustomConfig() {
    }

    @NonNull
    public static Set<String> getExtensions() {
        return sEnvs.keySet();
    }

    @Nullable
    public static KustomEnv getEnv(@NonNull String extension) {
        return sEnvs.get(extension);
    }

}
