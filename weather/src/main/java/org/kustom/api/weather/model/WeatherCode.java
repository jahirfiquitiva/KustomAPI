package org.kustom.api.weather.model;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum WeatherCode implements Parcelable {
    TORNADO(0, WeatherIcon.TORNADO),
    TROPICAL_STORM(1, WeatherIcon.TSTORM),
    HURRICANE(2, WeatherIcon.TSTORM),
    SEVERE_THUNDERSTORMS(3, WeatherIcon.TSTORM),
    THUNDERSTORMS(4, WeatherIcon.TSTORM),
    MIXED_RAIN_SNOW(5, WeatherIcon.SLEET),
    MIXED_RAIN_SLEET(6, WeatherIcon.SLEET),
    MIXED_SNOW_SLEET(7, WeatherIcon.SLEET),
    FREEZING_DRIZZLE(8, WeatherIcon.RAIN),
    DRIZZLE(9, WeatherIcon.RAIN),
    FREEZING_RAIN(10, WeatherIcon.RAIN),
    SHOWERS(11, WeatherIcon.SHOWER),
    HEAVY_SHOWERS(12, WeatherIcon.SHOWER),
    SNOW_FLURRIES(13, WeatherIcon.LSNOW),
    LIGHT_SNOW_SHOWERS(14, WeatherIcon.LSNOW),
    BLOWING_SNOW(15, WeatherIcon.SNOW),
    SNOW(16, WeatherIcon.SNOW),
    HAIL(17, WeatherIcon.HAIL),
    SLEET(18, WeatherIcon.SLEET),
    DUST(19, WeatherIcon.FOG),
    FOGGY(20, WeatherIcon.FOG),
    HAZE(21, WeatherIcon.FOG),
    SMOKY(22, WeatherIcon.FOG),
    BLUSTERY(23, WeatherIcon.WINDY),
    WINDY(24, WeatherIcon.WINDY),
    CLOUDY(26, WeatherIcon.MCLOUDY),
    MOSTLY_CLOUDY(28, WeatherIcon.MCLOUDY),
    PARTLY_CLOUDY(30, WeatherIcon.PCLOUDY),
    CLEAR(32, WeatherIcon.CLEAR),
    FAIR(34, WeatherIcon.PCLOUDY),
    MIXED_RAIN_AND_HAIL(35, WeatherIcon.HAIL),
    ISOLATED_THUNDERSTORMS(37, WeatherIcon.TSHOWER),
    SCATTERED_SHOWERS(40, WeatherIcon.SHOWER),
    HEAVY_SNOW(41, WeatherIcon.SNOW),
    SCATTERED_SNOW_SHOWERS(42, WeatherIcon.LSNOW),
    THUNDERSHOWERS(45, WeatherIcon.TSHOWER),
    SNOW_SHOWERS(46, WeatherIcon.LSNOW),
    ISOLATED_THUNDERSHOWERS(47, WeatherIcon.TSHOWER),
    NOT_AVAILABLE(1000, WeatherIcon.UNKNOWN);

    private static final String TAG = "WeatherCode";

    @SuppressLint("UseSparseArrays")
    private static final Map<Integer, WeatherCode> sLookup = new HashMap<>();

    static {
        for (WeatherCode s : EnumSet.allOf(WeatherCode.class))
            sLookup.put(s.getCode(), s);
    }

    /**
     * Numeric code associated to this code, this is based on yahoo states
     */
    private int mCode;

    /**
     * Short version of this code used for icons
     */
    private WeatherIcon mIcon;

    WeatherCode(int code, WeatherIcon icon) {
        mCode = code;
        mIcon = icon;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeInt(ordinal());
    }

    public WeatherIcon getIcon() {
        return mIcon;
    }

    public int getCode() {
        return mCode;
    }

    /**
     * Build a weather code based on an Yahoo Weather codes (this should be considered the standard
     * baseline by all weather providers)
     *
     * @param code the yahoo weather code
     * @return a valid weather code object
     */
    public static WeatherCode get(int code) {
        // MOSTLY_CLOUDY_NIGHT
        if (code == 27) code = 28;
        // PARTLY_CLOUDY_NIGHT
        if (code == 29) code = 30;
        if (code == 44) code = 30;
        // CLEAR NIGHT
        if (code == 31) code = 32;
        // COLD
        if (code == 25) code = 32;
        // HOT
        if (code == 36) code = 32;
        // FAIR NIGHT
        if (code == 33) code = 34;
        // SCATTERED_THUNDERSTORMS_1
        if (code == 39) code = 37;
        // SCATTERED_THUNDERSTORMS
        if (code == 38) code = 37;
        // Done
        WeatherCode result = sLookup.get(code);
        if (result == null) {
            Log.w(TAG, "Unmapped Weather Condition: " + code);
            return NOT_AVAILABLE;
        }
        return result;
    }

    public static final Creator<WeatherCode> CREATOR = new Creator<WeatherCode>() {
        @Override
        public WeatherCode createFromParcel(final Parcel source) {
            return WeatherCode.values()[source.readInt()];
        }

        @Override
        public WeatherCode[] newArray(final int size) {
            return new WeatherCode[size];
        }
    };
}