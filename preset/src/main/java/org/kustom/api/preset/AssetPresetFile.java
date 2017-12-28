package org.kustom.api.preset;

import android.content.Context;
import android.support.annotation.NonNull;

import java.io.IOException;
import java.io.InputStream;

public class AssetPresetFile extends PresetFile {
    private final String mFilePath;

    /**
     * Build a preset file according to its relative path in the assets
     *
     * @param path the path in the assets folder, es "widgets/awezome.kwgt"
     */
    public AssetPresetFile(String path) {
        super(extractNameFromPath(path), extractExtFromPath(path));
        mFilePath = path;
    }

    @Override
    public String getPath() {
        return mFilePath;
    }

    @Override
    public InputStream getStream(@NonNull Context context) throws IOException {
        return context.getAssets().open(mFilePath);
    }
}
