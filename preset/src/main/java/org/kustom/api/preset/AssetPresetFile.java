package org.kustom.api.preset;

import android.content.Context;
import android.support.annotation.NonNull;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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
        return String.format("file:///android_asset/%s", mFilePath);
    }

    @Override
    public InputStream getStream(@NonNull Context context, @NonNull String file) throws IOException {
        ZipInputStream zis = new ZipInputStream(context.getAssets().open(mFilePath));
        ZipEntry ze;
        while ((ze = zis.getNextEntry()) != null) {
            if (ze.getName().equals(file)) {
                return zis;
            }
        }
        throw new FileNotFoundException("File not found: " + getPath() + "/" + file);
    }
}
