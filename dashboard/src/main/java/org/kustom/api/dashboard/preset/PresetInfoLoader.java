package org.kustom.api.dashboard.preset;

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

public class PresetInfoLoader {
    private final static HashMap<String, PresetInfo> sPresetInfoCache = new HashMap<>();
    private final PresetFile mFile;

    private PresetInfoLoader(PresetFile file) {
        mFile = file;
    }

    public static PresetInfoLoader create(@NonNull PresetFile file) {
        return new PresetInfoLoader(file);
    }

    public void load(@NonNull Context context, @NonNull Callback callback) {
        synchronized (sPresetInfoCache) {
            if (sPresetInfoCache.containsKey(mFile.getPath()))
                callback.onInfoLoaded(sPresetInfoCache.get(mFile.getPath()));
            else new LoaderTask(context, callback, mFile).execute();
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

        LoaderTask(Context context, Callback callback, PresetFile file) {
            mContext = context.getApplicationContext();
            mCallback = callback;
            mFile = file;
        }

        @Override
        protected PresetInfo doInBackground(Void... params) {
            try (ZipInputStream zis = new ZipInputStream(mFile.getStream(mContext))) {
                ZipEntry ze;
                boolean found = false;
                while ((ze = zis.getNextEntry()) != null) {
                    if (ze.getName().equals("preset.json")) {
                        found = true;
                        break;
                    } else if (ze.getName().equals("komponent_thumb.jpg")) {
                        found = true;
                        break;
                    }
                }
                if (found) return buildFromStream(zis);
                else throw new IOException("Preset info not found");
            } catch (Exception e) {
                e.printStackTrace();
                return new PresetInfo(mFile.getName());
            }
        }

        @Override
        protected void onPostExecute(PresetInfo result) {
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
