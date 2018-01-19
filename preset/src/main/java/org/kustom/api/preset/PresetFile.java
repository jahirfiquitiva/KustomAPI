package org.kustom.api.preset;

import android.content.Context;
import android.support.annotation.NonNull;

import java.io.IOException;
import java.io.InputStream;

@SuppressWarnings("WeakerAccess")
public abstract class PresetFile {
    private final String mName;
    private final String mExt;

    @SuppressWarnings("WeakerAccess")
    public PresetFile(String name, String ext) {
        mName = name;
        mExt = ext;
    }

    public String getName() {
        return mName;
    }

    public String getExt() {
        return mExt;
    }

    public boolean isKomponent() {
        return "komp".equalsIgnoreCase(mExt);
    }

    public abstract String getPath();

    public abstract InputStream getStream(@NonNull Context context, @NonNull String file)
            throws IOException;

    @Override
    public String toString() {
        return String.format("%s.%s", mName, mExt);
    }

    protected static String extractNameFromPath(@NonNull String path) {
        return path.replaceAll(".*\\/", "")
                .replaceAll("\\..*", "");
    }

    protected static String extractExtFromPath(@NonNull String path) {
        return path.replaceAll("\\.zip", "")
                .replaceAll(".*\\.", "");
    }
}