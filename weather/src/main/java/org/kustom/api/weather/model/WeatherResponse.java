package org.kustom.api.weather.model;

import android.os.Parcel;
import android.os.Parcelable;

@SuppressWarnings("unused")
public class WeatherResponse implements Parcelable {
    private final String mStationId;
    private final WeatherInstant mCurrent;
    private final WeatherDailyForecast[] mForecast;
    private final WeatherHourlyForecast[] mHourlyForecast;

    protected WeatherResponse(Parcel in) {
        mStationId = in.readString();
        mCurrent = in.readParcelable(WeatherInstant.class.getClassLoader());
        mForecast = in.createTypedArray(WeatherDailyForecast.CREATOR);
        mHourlyForecast = in.createTypedArray(WeatherHourlyForecast.CREATOR);
    }

    private WeatherResponse(Builder builder) {
        mStationId = builder.mStationId;
        mCurrent = builder.mCurrent;
        mForecast = builder.mForecast;
        mHourlyForecast = builder.mHourlyForecast;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mStationId);
        dest.writeParcelable(mCurrent, flags);
        dest.writeTypedArray(mForecast, flags);
        dest.writeTypedArray(mHourlyForecast, flags);
    }

    public String getStationId() {
        return mStationId;
    }

    public WeatherInstant getCurrent() {
        return mCurrent;
    }

    public WeatherDailyForecast[] getForecast() {
        return mForecast;
    }

    public WeatherHourlyForecast[] getHourlyForecast() {
        return mHourlyForecast;
    }

    public static final Creator<WeatherResponse> CREATOR = new Creator<WeatherResponse>() {
        @Override
        public WeatherResponse createFromParcel(Parcel source) {
            return new WeatherResponse(source);
        }

        @Override
        public WeatherResponse[] newArray(int size) {
            return new WeatherResponse[size];
        }
    };

    public static final class Builder {
        private String mStationId = "";
        private WeatherInstant mCurrent;
        private WeatherDailyForecast[] mForecast = new WeatherDailyForecast[0];
        private WeatherHourlyForecast[] mHourlyForecast = new WeatherHourlyForecast[0];

        public Builder(WeatherInstant current) {
            mCurrent = current;
        }

        public Builder withStationId(String stationId) {
            mStationId = stationId;
            return this;
        }

        public Builder withForecast(WeatherDailyForecast[] forecast) {
            mForecast = forecast;
            return this;
        }

        public Builder withHourlyForecast(WeatherHourlyForecast[] hourlyForecast) {
            mHourlyForecast = hourlyForecast;
            return this;
        }

        public WeatherResponse build() {
            return new WeatherResponse(this);
        }
    }
}