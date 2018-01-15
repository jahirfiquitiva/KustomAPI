package org.kustom.api.preset;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@SuppressWarnings("unused")
public class PresetInfoLoader {
    private final static HashMap<String, PresetInfo> sPresetInfoCache = new HashMap<>();
    private PresetFile mFile;
    private InputStream mInputStream;

    private PresetInfoLoader(@NonNull PresetFile file) {
        mFile = file;
    }

    public static PresetInfoLoader create(@NonNull PresetFile file) {
        return new PresetInfoLoader(file);
    }

    /**
     * Override stream from which data will be loaded
     *
     * @param stream the input stream to load JSON from
     */
    public PresetInfoLoader withStream(@NonNull InputStream stream) {
        mInputStream = stream;
        return this;
    }

    public void load(@NonNull Context context, @NonNull Callback callback) {
        synchronized (sPresetInfoCache) {
            if (sPresetInfoCache.containsKey(mFile.getPath()))
                callback.onInfoLoaded(sPresetInfoCache.get(mFile.getPath()));
            else new LoaderTask(context, callback, mFile, mInputStream).execute();
        }
    }

    public interface Callback {
        void onInfoLoaded(PresetInfo info);
    }

    @SuppressLint("StaticFieldLeak")
    private static class LoaderTask extends AsyncTask<Void, Void, PresetInfo> {
        private final Callback mCallback;
        private final Context mContext;
        private final PresetFile mFile;
        private InputStream mStream;

        LoaderTask(Context context, Callback callback, PresetFile file, InputStream stream) {
            mContext = context.getApplicationContext();
            mCallback = callback;
            mFile = file;
            mStream = stream;
        }

        @Override
        protected PresetInfo doInBackground(Void... params) {
            PresetInfo result = null;
            // From file
            if (mStream == null) {
                try (ZipInputStream zis = new ZipInputStream(mFile.getStream(mContext))) {
                    ZipEntry ze;
                    boolean found = false;
                    while ((ze = zis.getNextEntry()) != null) {
                        if (ze.getName().equalsIgnoreCase("preset.json")
                                || ze.getName().equalsIgnoreCase("komponent.json")) {
                            found = true;
                            break;
                        }
                    }
                    if (found) result = buildFromStream(zis);
                    else throw new IOException("Preset info not found");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            // From stream
            else {
                try {
                    result = buildFromStream(mStream);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        mStream.close();
                    } catch (Exception ignored) {
                    }
                }
            }
            // Done
            return result;
        }

        @Override
        protected void onPostExecute(PresetInfo result) {
            if (result == null) result = new PresetInfo(mFile.getName());
            synchronized (sPresetInfoCache) {
                sPresetInfoCache.put(mFile.getPath(), result);
                mCallback.onInfoLoaded(result);
            }
        }

        private PresetInfo buildFromStream(InputStream is) throws IOException {
            PresetInfo info = null;
            InputStreamReader isr = new InputStreamReader(is, "UTF-8");
            JsonReader reader = new JsonReader(new BufferedReader(isr));
            reader.beginObject();
            String name = reader.nextName();
            if (name.equals("preset_info"))
                info = new Gson().fromJson(reader, PresetInfo.class);
            reader.close();
            return info;
        }
    }
}