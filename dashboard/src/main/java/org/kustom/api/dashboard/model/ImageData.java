package org.kustom.api.dashboard.model;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

@SuppressWarnings("WeakerAccess")
public class ImageData {
    private final String mJsonData;
    private final String mTitle;
    private final String mAuthor;
    private final String mUrl;
    private final String mThumbUrl;

    public ImageData(@NonNull JSONObject image) throws JSONException {
        mJsonData = image.toString(2);
        mTitle = image.getString("name");
        mUrl = image.getString("url");
        mAuthor = image.optString("author");
        String thumb = image.optString("thumbnail");
        mThumbUrl = (TextUtils.isEmpty(thumb)) ? mUrl : thumb;
    }

    public String getJsonData() {
        return mJsonData;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getThumbUrl() {
        return mThumbUrl;
    }
}
