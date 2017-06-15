package org.kustom.api.weather.model;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;

public class WeatherHourlyForecast extends WeatherInstant {
    @SerializedName("valid_from")
    private long mValidFrom = 0L;
    @SerializedName("valid_to")
    private long mValidTo = 0L;

    public WeatherHourlyForecast() {
    }

    protected WeatherHourlyForecast(Parcel in) {
        super(in);
        mValidFrom = in.readLong();
        mValidTo = in.readLong();
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

    @SuppressWarnings("unused")
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