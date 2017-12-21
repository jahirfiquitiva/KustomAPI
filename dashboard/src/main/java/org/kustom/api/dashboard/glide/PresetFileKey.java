package org.kustom.api.dashboard.glide;

import com.bumptech.glide.load.Key;

import org.kustom.api.dashboard.model.PresetFile;

import java.security.MessageDigest;

public class PresetFileKey implements Key {
    private final String mPath;

    PresetFileKey(PresetFile file) {
        mPath = file.getPath();
    }

    @Override
    public int hashCode() {
        return mPath.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof PresetFileKey && ((PresetFileKey) obj).mPath.equals(mPath);
    }

    @Override
    public String toString() {
        return mPath;
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {
        messageDigest.update(mPath.getBytes(CHARSET));
    }
}
