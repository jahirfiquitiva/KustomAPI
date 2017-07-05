package org.kustom.api.weather.model;

import android.os.Parcel;
import android.os.Parcelable;

@SuppressWarnings("unused")
public class WeatherResponse implements Parcelable {
    private final String mStationId;
    private final WeatherInstant mCurrent;
    private final WeatherDailyForecast[] mForecast;
    private final WeatherHourlyForecast[] mHourlyForecast;
    private final boolean mIsSuccess;
    private final long mValidUntil;

    protected WeatherResponse(Parcel in) {
        mStationId = in.readString();
        mCurrent = in.readParcelable(WeatherInstant.class.getClassLoader());
        mForecast = in.createTypedArray(WeatherDailyForecast.CREATOR);
        mHourlyForecast = in.createTypedArray(WeatherHourlyForecast.CREATOR);
        mIsSuccess = in.readByte() != 0;
        mValidUntil = in.readLong();
    }

    private WeatherResponse(Builder builder) {
        mStationId = builder.mStationId;
        mCurrent = builder.mCurrent;
        mForecast = builder.mForecast;
        mHourlyForecast = builder.mHourlyForecast;
        mIsSuccess = builder.mIsSuccess;
        mValidUntil = builder.mValidUntil;
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
        dest.writeByte((byte) (mIsSuccess ? 1 : 0));
        dest.writeLong(mValidUntil);
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

    public boolean isSuccess() {
        return mIsSuccess;
    }

    public long getValidUntil() {
        return mValidUntil;
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
        private boolean mIsSuccess = true;
        private long mValidUntil = 0L;

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

        public Builder withSuccess(boolean success) {
            mIsSuccess = success;
            return this;
        }

        public Builder withValidUntil(long validUntil) {
            mValidUntil = validUntil;
            return this;
        }

        public WeatherResponse build() {
            return new WeatherResponse(this);
        }
    }
}