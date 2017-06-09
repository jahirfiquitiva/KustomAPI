package org.kustom.api.weather.model;

import android.os.Parcel;
import android.os.Parcelable;

@SuppressWarnings("unused")
public class WeatherResponse implements Parcelable {
    private WeatherInstant mCurrent;
    private WeatherForecast[] mForecast;
    private WeatherInstant[] mHourlyForecast;

    public WeatherResponse() {
    }

    protected WeatherResponse(Parcel in) {
        mCurrent = in.readParcelable(WeatherInstant.class.getClassLoader());
        mForecast = in.createTypedArray(WeatherForecast.CREATOR);
        mHourlyForecast = in.createTypedArray(WeatherInstant.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(mCurrent, flags);
        dest.writeTypedArray(mForecast, flags);
        dest.writeTypedArray(mHourlyForecast, flags);
    }

    public WeatherInstant getCurrent() {
        return mCurrent;
    }

    public WeatherResponse setCurrent(WeatherInstant current) {
        mCurrent = current;
        return this;
    }

    public WeatherForecast[] getForecast() {
        return mForecast;
    }

    public WeatherResponse setForecast(WeatherForecast[] forecast) {
        mForecast = forecast;
        return this;
    }

    public WeatherInstant[] getHourlyForecast() {
        return mHourlyForecast;
    }

    public WeatherResponse setHourlyForecast(WeatherInstant[] hourlyForecast) {
        mHourlyForecast = hourlyForecast;
        return this;
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
}