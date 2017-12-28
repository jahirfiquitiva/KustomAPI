package org.kustom.api.dashboard.config;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@SuppressWarnings({"ConstantConditions", "unused"})
public class KustomConfig {
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

    public static final KustomEnv ENV_KWGT = sEnvs.get("kwgt");
    public static final KustomEnv ENV_KLCK = sEnvs.get("klck");
    public static final KustomEnv ENV_KLWP = sEnvs.get("klwp");
    public static final KustomEnv ENV_KWCH = sEnvs.get("kwch");
    public static final KustomEnv ENV_KOMP = sEnvs.get("komp");

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
