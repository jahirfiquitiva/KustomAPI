package org.kustom.api.dashboard.preset;

import android.content.Context;
import android.support.annotation.NonNull;

import java.io.IOException;
import java.io.InputStream;

public class PresetFile {
    private final String mFilePath;

    public PresetFile(String filePath) {
        mFilePath = filePath;
    }

    public String getName() {
        return mFilePath.replaceAll(".*\\/", "").replaceAll("\\..*", "");
    }

    public String getPath() {
        return mFilePath;
    }

    public String getExt() {
        return mFilePath.replaceAll("\\.zip", "").replaceAll(".*\\.", "");
    }

    public InputStream getStream(@NonNull Context context) throws IOException {
        return context.getAssets().open(mFilePath);
    }

    @Override
    public String toString() {
        return mFilePath;
    }
}
