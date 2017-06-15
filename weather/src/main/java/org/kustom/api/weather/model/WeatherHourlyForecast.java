package org.kustom.api.weather.model;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class WeatherHourlyForecast extends WeatherInstant {
    @SerializedName("valid_from")
    private long mValidFrom = 0L;
    @SerializedName("valid_to")
    private long mValidTo = 0L;
    @SerializedName("rain_chance")
    private int mRainChance = 0;
    @SerializedName("rain")
    private float mRain = 0;

    public WeatherHourlyForecast() {
    }

    protected WeatherHourlyForecast(Parcel in) {
        super(in);
        mValidFrom = in.readLong();
        mValidTo = in.readLong();
        mRainChance = in.readInt();
        mRain = in.readFloat();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeLong(mValidFrom);
        dest.writeLong(mValidTo);
        dest.writeInt(mRainChance);
        dest.writeFloat(mRain);
    }

    /**
     * @return the UTC time since this data is valid (mandatory for hourly forecasts)
     */
    public long getValidFrom() {
        return mValidFrom;
    }

    /**
     * Sets validity limit of this value (start)
     *
     * @param validFrom the UTC time since this data is valid (mandatory for hourly forecasts)
     */
    public void setValidFrom(long validFrom) {
        mValidFrom = validFrom;
    }

    /**
     * @return the UTC time till this data is valid (mandatory for hourly forecasts)
     */
    public long getValidTo() {
        return mValidTo;
    }

    /**
     * Sets validity limit of this value (end)
     *
     * @param validTo the UTC time till this data is valid (mandatory for hourly forecasts)
     */
    public void setValidTo(long validTo) {
        mValidTo = validTo;
    }

    /**
     * @return rain chance in percentage 0-100
     */
    public int getRainChance() {
        return mRainChance;
    }

    /**
     * Sets current chance of rain
     *
     * @param rainChance the chance of rain in percentage 0-100
     */
    public void setRainChance(int rainChance) {
        mRainChance = Math.max(0, Math.min(100, rainChance));
    }

    /**
     * @return rain precipitations in millimiters
     */
    public float getRain() {
        return mRain;
    }

    /**
     * Sets expected rain precipitations
     *
     * @param rain in millimiters
     */
    public void setRain(float rain) {
        mRain = rain;
    }

    public static final Creator<WeatherHourlyForecast> CREATOR = new Creator<WeatherHourlyForecast>() {
        @Override
        public WeatherHourlyForecast createFromParcel(Parcel in) {
            return new WeatherHourlyForecast(in);
        }

        @Override
        public WeatherHourlyForecast[] newArray(int size) {
            return new WeatherHourlyForecast[size];
        }
    };
}