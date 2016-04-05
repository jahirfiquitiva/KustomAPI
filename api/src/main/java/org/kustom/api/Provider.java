package org.kustom.api;

import android.annotation.SuppressLint;
import android.content.ContentProvider;
import android.content.ContentValues;
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
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static android.os.ParcelFileDescriptor.MODE_READ_ONLY;

@SuppressLint("Registered")
public class Provider extends ContentProvider {

    /**
     * Used to match an archive kfile archive
     */
    public final static String ARCHIVE_REGEXP = ".*\\.(k...)(\\.zip)?(/.*)?";

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
        InputStream is = null;
        ZipFile zf = null;
        try {
            File cacheFile = getCacheFile(archivePath, filePath);
            boolean cacheGood = false;
            // We always invalidate the cache, caller won't call this if not needed
            if (!archivePath.matches(ARCHIVE_REGEXP)) {
                is = assets.open(archivePath + "/" + filePath);
                FileUtils.copy(is, cacheFile);
                cacheGood = true;
            } else {
                zf = new ZipFile(getArchiveFile(archivePath));
                Enumeration<? extends ZipEntry> entries = zf.entries();
                while (entries.hasMoreElements()) {
                    ZipEntry ze = entries.nextElement();
                    if (ze.getName().equals(filePath)) {
                        FileUtils.copy(zf.getInputStream(ze), cacheFile);
                        cacheGood = true;
                        break;
                    }
                }
            }
            // If we are here everything should have been copied correctly
            if (cacheGood) return ParcelFileDescriptor.open(cacheFile, MODE_READ_ONLY);
        } catch (IOException e) {
            Logger.e("Unable to open file: " + uri, e);
        } finally {
            if (is != null) try {
                is.close();
            } catch (IOException ignored) {
            }
            if (zf != null) try {
                zf.close();
            } catch (IOException ignored) {
            }
        }
        throw new FileNotFoundException("No file supported by provider at: " + uri);
    }

    private File getArchiveFile(String archivePath) throws IOException {
        File zipCacheFile = getCacheFile(archivePath, "");
        AssetManager assets = getContext().getAssets();
        if (zipCacheFile.length() != assets.openFd(archivePath).getLength()) {
            InputStream is = assets.open(archivePath);
            FileUtils.copy(is, zipCacheFile);
        }
        return zipCacheFile;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public Cursor query(Uri paramUri, String[] paramArrayOfString1, String paramString1,
                        String[] paramArrayOfString2, String paramString2) {
        // Check if Uri is valid
        if (paramUri == null || paramUri.getPathSegments().size() < 2) {
            throw new IllegalArgumentException("Invalid arguments in Uri: " + paramUri);
        }

        // Parse uri
        LinkedList<String> segments = new LinkedList<>();
        segments.addAll(paramUri.getPathSegments());
        final String action = segments.remove(0);
        final String archivePath = getArchivePath(segments);
        final String folderPath = getFilePath(segments);

        // List files
        if (ACTION_LIST.equalsIgnoreCase(action)) {
            List<String> result = listFiles(archivePath, folderPath);
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

    @SuppressWarnings("unused")
    public static Uri buildQuery(String authority, String action, String archivePath, String path) {
        return Uri.parse(String.format("content://%s/%s/%s/%s", authority, action,
                archivePath == null ? "" : archivePath, path));
    }

    @SuppressWarnings("unused")
    public static Uri buildContentUri(String authority, String archivePath, String path) {
        return Uri.parse(String.format("content://%s/%s/%s", authority,
                archivePath == null ? "" : archivePath, path));
    }

    @SuppressWarnings("unused")
    public static FileInfo buildFileInfo(Cursor c) {
        return new FileInfo(c);
    }

    private File getCacheFile(String archivePath, String filePath) {
        String cacheFileName = FileUtils.getHash(String.format("%s/%s", archivePath, filePath));
        return FileUtils.getCacheFile(getContext(), "provider", cacheFileName);
    }

    private List<String> listFiles(String archivePath, String folderPath) {
        Logger.d("List archive://%s, folder://%s", archivePath, folderPath);
        LinkedList<String> result = new LinkedList<>();
        ZipFile zf = null;
        try {
            // List files in archive
            if (archivePath.matches(ARCHIVE_REGEXP)) {
                zf = new ZipFile(getArchiveFile(archivePath));
                Enumeration<? extends ZipEntry> entries = zf.entries();
                while (entries.hasMoreElements()) {
                    ZipEntry ze = entries.nextElement();
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
            // Non compressed
            else {
                if (archivePath.length() > 0) {
                    List<String> entries = Arrays.asList(getContext().getAssets().list(archivePath));
                    if (entries.contains(trimPath(folderPath))) {
                        result.add(trimPath(archivePath + "/" + folderPath));
                    }
                } else {
                    result.addAll(Arrays.asList(getContext().getAssets().list(folderPath)));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (zf != null) try {
                zf.close();
            } catch (IOException ignored) {
            }
        }
        // Done return data
        Logger.d("List returned %d items", result.size());
        return result;
    }

    private String getArchivePath(List<String> segments) {
        String path = "";
        for (int i = 0; i < segments.size(); i++) {
            String segment = segments.get(i);
            if (segment.length() > 0) {
                if (segment.matches(ARCHIVE_REGEXP)) {
                    if (i < segments.size() - 1) path += segment;
                    return trimPath(path);
                } else {
                    path += segment + "/";
                }
            }
        }
        return "";
    }

    private String getFilePath(List<String> segments) {
        String path = "";
        for (int i = 0; i < segments.size(); i++) {
            String segment = segments.get(i);
            if (segment.length() > 0) {
                path += "/" + segment;
                // Start over if we find an archive
                if (segment.matches(ARCHIVE_REGEXP)) {
                    if (i < segments.size() - 1) path = "";
                    else return trimPath(segment);
                }
            }
        }
        return trimPath(path);
    }

    private String trimPath(String path) {
        return path.replaceFirst("^/+", "").replaceFirst("/+$", "").replaceAll("/+", "/");
    }

}