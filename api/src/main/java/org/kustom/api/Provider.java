package org.kustom.api;

import android.annotation.SuppressLint;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@SuppressLint("Registered")
public class Provider extends ContentProvider {

    /**
     * This filter tells the package manager that we can provide Wallpapers
     */
    public static final String ACTION_PROVIDER_WALLPAPERS = "org.kustom.provider.WALLPAPERS";

    /**
     * This filter tells the package manager that we can provide Komponents
     */
    public static final String ACTION_PROVIDER_KOMPONENTS = "org.kustom.provider.KOMPONENTS";

    /**
     * List action used to get contents of the provider on a specific path
     */
    public static final String ACTION_LIST = "list";

    /**
     * Info action used to get last modification date and file name of a cached item
     */
    public static final String ACTION_INFO = "info";

    /**
     * Shared prefs file used for upgrade
     */
    private static final String SHARED_PREFS = "kustom_provider";
    private static final String PREF_LAST_UPGRADE = "last_upgrade";

    /**
     * Unsupported
     */
    public int delete(Uri paramUri, String paramString, String[] paramArrayOfString) {
        return 0;
    }

    /**
     * Unsupported
     */
    public String getType(Uri paramUri) {
        return null;
    }

    /**
     * Unsupported
     */
    public Uri insert(Uri paramUri, ContentValues paramContentValues) {
        return null;
    }

    /**
     * Unsupported
     */
    public int update(Uri paramUri, ContentValues paramContentValues, String paramString,
                      String[] paramArrayOfString) {
        return 0;
    }

    /**
     * Perform upgrade routines
     */
    @SuppressLint("CommitPrefEdits")
    public boolean onCreate() {
        Logger.i("Provider started");
        // Upgrade Check
        SharedPreferences sp = getContext().getSharedPreferences(SHARED_PREFS, 0);
        try {
            final int lastUpgradedRelease = sp.getInt(PREF_LAST_UPGRADE, 0);
            final int currentRelease = getContext().getPackageManager()
                    .getPackageInfo(getContext().getPackageName(), 0).versionCode;
            if (lastUpgradedRelease != currentRelease) {
                Logger.i("Clearing cache after upgrade");
                FileUtils.clearCache(getContext(), "provider");
                sp.edit().putInt(PREF_LAST_UPGRADE, currentRelease).commit();
            }
        } catch (Exception e) {
            Logger.e("Unable to check for upgrade", e);
        }
        // Done
        return true;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {
        final String archivePath = getArchivePath(uri.getPathSegments());
        final String filePath = getFilePath(uri.getPathSegments());
        Logger.d("Opening archive://%s, file://%s", archivePath, filePath);
        AssetManager assets = getContext().getAssets();
        try {
            InputStream is = assets.open(archivePath);
            File cacheFile = getCacheFile(archivePath, filePath);
            ZipInputStream zis = new ZipInputStream(is);
            boolean archiveFileFound = false;
            ZipEntry ze;
            byte[] buff = new byte[1024];
            while ((ze = zis.getNextEntry()) != null) {
                if (ze.getName().equals(filePath)) {
                    // We always invalidate the cache, caller won't call this if not needed
                    FileUtils.copy(zis, cacheFile);
                    // Done
                    archiveFileFound = true;
                    break;
                }
            }
            zis.close();
            // If we are here everything should have been copied correctly
            if (archiveFileFound) {
                return ParcelFileDescriptor.open(cacheFile, ParcelFileDescriptor.MODE_READ_ONLY);
            }
        } catch (IOException e) {
            Logger.e("Unable to open file: " + uri, e);
        }
        throw new FileNotFoundException("No file supported by provider at: " + uri);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public Cursor query(Uri paramUri, String[] paramArrayOfString1, String paramString1,
                        String[] paramArrayOfString2, String paramString2) {
        // Check if Uri is valid
        if (paramUri == null || paramUri.getPathSegments().size() < 2) {
            throw new IllegalArgumentException("Invalid arguments in Uri: " + paramUri);
        }
        // Parse uri
        LinkedList<String> segments = new LinkedList<String>();
        segments.addAll(paramUri.getPathSegments());
        final String action = segments.remove(0);

        // List files
        if (ACTION_LIST.equalsIgnoreCase(action)) {
            List<String> result = listFiles(getArchivePath(segments), getFilePath(segments));
            MatrixCursor cursor = new MatrixCursor(new String[]{"filename"});
            for (String s : result) {
                cursor.newRow().add(s);
                cursor.moveToNext();
            }
            cursor.moveToFirst();
            return cursor;
        }

        // Info
        else if (ACTION_INFO.equalsIgnoreCase(action)) {
            final String archivePath = getArchivePath(segments);
            final String folderPath = getFilePath(segments);
            Logger.d("Info archive://%s, folder://%s", archivePath, folderPath);
            // Let's check if cacheFile has been created already, if its not, check archive
            File cacheFile = getCacheFile(archivePath, folderPath);
            boolean isValid = cacheFile.exists() && cacheFile.canRead() && cacheFile.length() > 0;
            if (!isValid) {
                cacheFile.delete();
                isValid = listFiles(archivePath, folderPath).size() > 0;
            }
            // Create Cursor (if file isValid but not cached will be reloaded)
            return FileInfo.buildCursor(isValid, getCacheFile(archivePath, folderPath));
        }

        // Unsupported op
        Logger.e("Unsupported operation, uri: " + paramUri);
        return null;
    }

    public static Uri buildQuery(String authority, String action, String archivePath, String path) {
        return Uri.parse(String.format("content://%s/%s/%s/%s", authority, action,
                archivePath == null ? "" : archivePath, path));
    }

    public static Uri buildContentUri(String authority, String archivePath, String path) {
        return Uri.parse(String.format("content://%s/%s/%s", authority,
                archivePath == null ? "" : archivePath, path));
    }

    public static FileInfo buildFileInfo(Cursor c) {
        return new FileInfo(c);
    }

    private File getCacheFile(String archivePath, String filePath) {
        String cacheFileName = FileUtils.getHash(String.format("%s/%s", archivePath, filePath));
        return FileUtils.getCacheFile(getContext(), "provider", cacheFileName);
    }

    private List<String> listFiles(String archivePath, String folderPath) {
        Logger.d("List archive://%s, folder://%s", archivePath, folderPath);
        LinkedList<String> result = new LinkedList<String>();
        try {
            // List files in archive
            if (archivePath.length() > 0) {
                InputStream is = getContext().getAssets().open(archivePath);
                ZipInputStream zis = new ZipInputStream(is);
                ZipEntry ze;
                while ((ze = zis.getNextEntry()) != null) {
                    // Folder Search
                    if (ze.getName().startsWith(folderPath + "/")) {
                        result.add(ze.getName().substring(ze.getName().lastIndexOf("/")));
                    }
                    // File search
                    else if (ze.getName().equals(folderPath)) {
                        result.add(ze.getName());
                    }
                }
            }
            // On SD Card
            else {
                result.addAll(Arrays.asList(getContext().getAssets().list(folderPath)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Done return data
        Logger.d("List returned %d items", result.size());
        return result;
    }

    private String getArchivePath(List<String> segments) {
        String path = "";
        for (String segment : segments) {
            if (segment.length() > 0) {
                path += segment;
                if (segment.endsWith(".zip")) {
                    return trimPath(path);
                } else {
                    path += "/";
                }
            }
        }
        return "";
    }

    private String getFilePath(List<String> segments) {
        String path = "";
        for (String segment : segments) {
            if (segment.length() > 0) {
                path += "/" + segment;
                // Start over if we find an archive
                if (segment.endsWith(".zip")) path = "";
            }
        }
        return trimPath(path);
    }

    private String trimPath(String path) {
        while (path.startsWith("/")) path = path.substring(1);
        return path;
    }

}