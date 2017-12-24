package org.kustom.api.preset;

import android.support.annotation.NonNull;
import android.text.TextUtils;

/**
 * Preset features
 */
@SuppressWarnings({"WeakerAccess", "UnusedReturnValue", "unused"})
public class PresetFeatures {
    private int mFlags = 0;
    private final boolean mIsMutable;

    /**
     * Feature flags, each one represents a feature being used in the preset
     */
    public final static int FEATURE_LOCATION = 2 << 1;
    public final static int FEATURE_WEATHER = 2 << 2;
    public final static int FEATURE_FORECAST = 2 << 3;
    public final static int FEATURE_GYRO = 2 << 4;
    public final static int FEATURE_ANALOG_CLOCK = 2 << 5;
    public final static int FEATURE_CALENDAR = 2 << 6;
    public final static int FEATURE_MUSIC = 2 << 7;
    public final static int FEATURE_FITNESS = 2 << 8;
    public final static int FEATURE_TRAFFIC = 2 << 9;
    public final static int FEATURE_DOWNLOAD = 2 << 10;
    public final static int FEATURE_SIGNAL = 2 << 11;
    public final static int FEATURE_NOTIFICATIONS = 2 << 12;
    public final static int FEATURE_SHELL = 2 << 13;
    public final static int FEATURE_UNREAD = 2 << 14;
    public final static int FEATURE_CALL = 2 << 15;

    /**
     * Static empty feature flag object
     */
    public final static PresetFeatures FLAG_FEATURE_NONE = new PresetFeatures(false);

    /**
     * Empty Flags Constructor
     */
    public PresetFeatures() {
        this(true);
    }

    /**
     * @param isMutable weather or not this flags are mutable
     */
    private PresetFeatures(boolean isMutable) {
        mIsMutable = isMutable;
    }

    /**
     * Build from string array
     */
    public PresetFeatures(@NonNull String flags) {
        this(true);
        flags = toASCII(flags).trim();
        String[] flagArray = flags.split(" ");
        for (String flag : flagArray) {
            switch (flag) {
                case "LOCATION":
                    add(FEATURE_LOCATION);
                    break;
                case "WEATHER":
                    add(FEATURE_WEATHER);
                    break;
                case "FORECAST":
                    add(FEATURE_FORECAST);
                    break;
                case "GYRO":
                    add(FEATURE_GYRO);
                    break;
                case "ANALOG_CLOCK":
                    add(FEATURE_ANALOG_CLOCK);
                    break;
                case "CALENDAR":
                    add(FEATURE_CALENDAR);
                    break;
                case "MUSIC":
                    add(FEATURE_MUSIC);
                    break;
                case "FITNESS":
                    add(FEATURE_FITNESS);
                    break;
                case "TRAFFIC":
                    add(FEATURE_TRAFFIC);
                    break;
                case "DOWNLOAD":
                    add(FEATURE_DOWNLOAD);
                    break;
                case "SIGNAL":
                    add(FEATURE_SIGNAL);
                    break;
                case "NOTIFICATIONS":
                    add(FEATURE_NOTIFICATIONS);
                    break;
                case "SHELL":
                    add(FEATURE_SHELL);
                    break;
                case "UNREAD":
                    add(FEATURE_UNREAD);
                    break;
                case "CALL":
                    add(FEATURE_CALL);
                    break;
            }
        }
    }


    /**
     * Bitwise check against flag
     *
     * @param flag the flag to check against
     * @return true if found
     */
    public boolean contains(int flag) {
        return mFlags != 0 && flag != 0 && (mFlags & flag) == flag;
    }

    /**
     * Add a flag to current BitMask
     *
     * @param flag the flag to add
     */
    public PresetFeatures add(int flag) {
        if (mIsMutable) mFlags = mFlags | flag;
        else throw new IllegalStateException("Cannot add flags to an immutable instance");
        return this;
    }

    /**
     * Remove a flag from current BitMask
     *
     * @param flag the flag to add
     */
    public void remove(int flag) {
        if (mIsMutable) mFlags &= ~flag;
        else throw new IllegalStateException("Cannot add flags to an immutable instance");
    }

    /**
     * Add all flags contained in a KFeatureFlags object
     *
     * @param flags the update flags to add
     */
    public void add(PresetFeatures flags) {
        add(flags.getFlags());
    }

    /**
     * Clears BitMask
     */
    public void clear() {
        if (mIsMutable) mFlags = 0;
        else throw new IllegalStateException("Cannot clear flags of an immutable instance");
    }

    public int getFlags() {
        return mFlags;
    }

    public String serialize() {
        return toString();
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof PresetFeatures && mFlags == ((PresetFeatures) o).mFlags;
    }

    @Override
    public String toString() {
        if (mFlags == 0) return "";
        StringBuilder sb = new StringBuilder();
        if (contains(FEATURE_LOCATION)) sb.append("LOCATION ");
        if (contains(FEATURE_WEATHER)) sb.append("WEATHER ");
        if (contains(FEATURE_FORECAST)) sb.append("FORECAST ");
        if (contains(FEATURE_GYRO)) sb.append("GYRO ");
        if (contains(FEATURE_ANALOG_CLOCK)) sb.append("ANALOG_CLOCK ");
        if (contains(FEATURE_CALENDAR)) sb.append("CALENDAR ");
        if (contains(FEATURE_MUSIC)) sb.append("MUSIC ");
        if (contains(FEATURE_FITNESS)) sb.append("FITNESS ");
        if (contains(FEATURE_TRAFFIC)) sb.append("TRAFFIC ");
        if (contains(FEATURE_DOWNLOAD)) sb.append("DOWNLOAD ");
        if (contains(FEATURE_SIGNAL)) sb.append("SIGNAL ");
        if (contains(FEATURE_NOTIFICATIONS)) sb.append("NOTIFICATIONS ");
        if (contains(FEATURE_SHELL)) sb.append("SHELL ");
        if (contains(FEATURE_UNREAD)) sb.append("UNREAD ");
        if (contains(FEATURE_CALL)) sb.append("CALL ");
        return sb.toString().trim();
    }

    private String toASCII(String source) {
        if (TextUtils.isEmpty(source)) return source;
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < source.length(); i++) {
            int c = source.charAt(i);
            if (c >= 32 && c <= 122) result.append(Character.toUpperCase(source.charAt(i)));
        }
        return result.toString().trim();
    }
}