package org.kustom.api;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FileUtils {

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static File getCacheFile(Context context, String authority, String filename) {
        final File cacheDir = context.getCacheDir();
        final File folder = new File(cacheDir, authority);
        if (folder.isDirectory() || folder.mkdirs()) {
            final File result = new File(folder, filename);
            if (!result.exists()) try {
                result.createNewFile();
            } catch (IOException e) {
                Logger.e("Unable to create temp file: " + filename);
            }
            return result;
        }
        Logger.e("Unable to create cache folder: " + authority);
        return new File(cacheDir, filename);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void clearCache(Context context, String authority) {
        final File cacheDir = context.getCacheDir();
        final File folder = new File(cacheDir, authority);
        File[] files = folder.listFiles();
        if (files != null) for (File f : files) f.delete();
    }

    public static String getHash(String seed) {
        MessageDigest m;
        try {
            m = MessageDigest.getInstance("MD5");
            m.update(seed.getBytes(), 0, seed.length());
            return new BigInteger(1, m.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            Logger.e("MD5 not available, using native String hashing");
        }
        return Integer.toString(seed.hashCode());
    }


    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void copy(InputStream in, File dst) throws IOException {
        if (!dst.exists()) dst.createNewFile();
        copy(in, new FileOutputStream(dst));
    }

    public static void copy(InputStream in, OutputStream out) throws IOException {
        byte[] buf = new byte[1024 * 4];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }
}