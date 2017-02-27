package org.kustom.api.dashboard;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.stream.StreamModelLoader;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

class ImageLoader implements StreamModelLoader<PresetFile> {
    private static final String TAG = ImageLoader.class.getSimpleName();

    private final Context mContext;

    ImageLoader(@NonNull Context context) {
        mContext = context;
    }

    @Override
    public DataFetcher<InputStream> getResourceFetcher(PresetFile f, int w, int h) {
        return new KustomDashboardImageFetcher(mContext, f);
    }

    private class KustomDashboardImageFetcher implements DataFetcher<InputStream> {
        private final PresetFile mPresetFile;
        private final Context mContext;
        private ZipInputStream mZipStream;

        KustomDashboardImageFetcher(@NonNull Context context, @NonNull PresetFile ref) {
            mPresetFile = ref;
            mContext = context;
        }

        @Override
        public InputStream loadData(Priority priority) throws Exception {
            mZipStream = new ZipInputStream(mPresetFile.getStream(mContext));
            ZipEntry ze;
            while ((ze = mZipStream.getNextEntry()) != null) {
                if (ze.getName().equals("preset_thumb_portrait.jpg"))
                    return mZipStream;
                else if (ze.getName().equals("komponent_thumb.jpg"))
                    return mZipStream;
            }
            throw new IOException("File Not Found: " + mPresetFile.getName());
        }

        @Override
        public void cleanup() {
            if (mZipStream != null) {
                try {
                    mZipStream.close();
                    mZipStream = null;
                } catch (IOException e) {
                    Log.w(TAG, "Could not close zip", e);
                }
            }
        }

        @Override
        public String getId() {
            return mPresetFile.getPath();
        }

        @Override
        public void cancel() {
        }
    }
}
