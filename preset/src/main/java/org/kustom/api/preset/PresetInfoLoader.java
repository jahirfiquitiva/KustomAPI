package org.kustom.api.preset;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.io.InputStream;
import java.util.HashMap;

@SuppressWarnings("unused")
public class PresetInfoLoader {
    private final static HashMap<String, PresetInfo> sPresetInfoCache = new HashMap<>();
    private PresetFile mFile;

    private PresetInfoLoader(@NonNull PresetFile file) {
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
            PresetInfo result = null;
            String file = mFile.isKomponent() ? "komponent.json" : "preset.json";
            try (InputStream stream = mFile.getStream(mContext, file)) {
                result = new PresetInfo
                        .Builder(stream)
                        .withFallbackTitle(mFile.getName())
                        .build();
            } catch (Exception e) {
                e.printStackTrace();
            }
            // Done
            return result;
        }

        @Override
        protected void onPostExecute(PresetInfo result) {
            if (result == null) result = new PresetInfo
                    .Builder()
                    .withTitle(mFile.getName())
                    .build();
            synchronized (sPresetInfoCache) {
                sPresetInfoCache.put(mFile.getPath(), result);
                mCallback.onInfoLoaded(result);
            }
        }
    }
}