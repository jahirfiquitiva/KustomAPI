package org.kustom.api.dashboard.preset;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings({"FieldCanBeLocal", "unused"})
public class PresetInfo {
    @SerializedName("version")
    private int mVersion;
    @SerializedName("title")
    private String mTitle;
    @SerializedName("description")
    private String mDescription;
    @SerializedName("author")
    private String mAuthor;
    @SerializedName("email")
    private String mEmail;
    @SerializedName("archive")
    private String mArchive;
    @SerializedName("width")
    private int mWidth;
    @SerializedName("height")
    private int mHeight;
    @SerializedName("xscreens")
    private int mXScreens;
    @SerializedName("yscreens")
    private int mYScreens;
    @SerializedName("features")
    private String mFeatures;
    @SerializedName("release")
    private int mRelease;
    @SerializedName("locked")
    private boolean mLocked = false;
    @SerializedName("pflags")
    private int mFlags = 0;

    PresetInfo(String title) {
        mTitle = title;
        mAuthor = "";
    }

    public int getVersion() {
        return mVersion;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getEmail() {
        return mEmail;
    }

    public String getArchive() {
        return mArchive;
    }

    public int getWidth() {
        return mWidth;
    }

    public int getHeight() {
        return mHeight;
    }

    public int getXScreens() {
        return mXScreens;
    }

    public int getYScreens() {
        return mYScreens;
    }

    public String getFeatures() {
        return mFeatures;
    }

    public int getRelease() {
        return mRelease;
    }

    public boolean isLocked() {
        return mLocked;
    }

    public int getFlags() {
        return mFlags;
    }
}
