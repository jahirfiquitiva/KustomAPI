package org.kustom.api.preset;

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

    /**
     * @return preset version, like 11
     */
    public int getVersion() {
        return mVersion;
    }

    /**
     * @return title of the preset
     */
    public String getTitle() {
        return mTitle;
    }

    /**
     * @return description of the preset
     */
    public String getDescription() {
        return mDescription;
    }

    /**
     * @return author of the preset
     */
    public String getAuthor() {
        return mAuthor;
    }

    /**
     * @return email of the author
     */
    public String getEmail() {
        return mEmail;
    }

    /**
     * @return Kustom internal URI representing file authority
     */
    public String getArchive() {
        return mArchive;
    }

    /**
     * @return width of the preset at the time of saving
     */
    public int getWidth() {
        return mWidth;
    }

    /**
     * @return height of the preset at the time of saving
     */
    public int getHeight() {
        return mHeight;
    }

    /**
     * @return number of the horizontal screens in the preset when saving (if applicable)
     */
    public int getXScreens() {
        return mXScreens;
    }

    /**
     * @return number of the vertical screens in the preset when saving (if applicable)
     */
    public int getYScreens() {
        return mYScreens;
    }

    /**
     * @return preset features
     */
    public PresetFeatures getFeatures() {
        return new PresetFeatures(mFeatures);
    }

    /**
     * @return kustom release in which this preset was saved
     */
    public int getRelease() {
        return mRelease;
    }

    /**
     * @return weather or not this preset has been locked by the author
     */
    public boolean isLocked() {
        return mLocked;
    }

    /**
     * @return internal used preset update flags
     */
    public int getFlags() {
        return mFlags;
    }
}