package org.kustom.api.preset;


import android.content.Context;
import android.support.annotation.NonNull;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class SDPresetFile extends PresetFile {
    private final File mFile;

    public SDPresetFile(@NonNull File file) {
        super(extractNameFromPath(file.getPath()), extractExtFromPath(file.getPath()));
        mFile = file;
    }

    @Override
    public String getPath() {
        return mFile.toURI().toASCIIString();
    }

    @Override
    public InputStream getStream(@NonNull Context context, @NonNull String file) throws IOException {
        ZipInputStream zis = new ZipInputStream(new BufferedInputStream(new FileInputStream(mFile)));
        ZipEntry ze;
        while ((ze = zis.getNextEntry()) != null) {
            if (ze.getName().equals(file)) {
                return zis;
            }
        }
        throw new FileNotFoundException("File not found: " + getPath() + "/" + file);
    }
}