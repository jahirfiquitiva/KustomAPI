package org.kustom.api.weather.model;

import android.os.Parcel;
import android.os.Parcelable;

@SuppressWarnings("unused")
public class WeatherResponse implements Parcelable {
    private WeatherCurrent mCurrent;
    private WeatherForecast[] mForecast;
    private WeatherForecast[] mHourlyForecast;

    public WeatherResponse() {
    }

    protected WeatherResponse(Parcel in) {
        mCurrent = in.readParcelable(WeatherCurrent.class.getClassLoader());
        mForecast = in.createTypedArray(WeatherForecast.CREATOR);
        mHourlyForecast = in.createTypedArray(WeatherForecast.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.mCurrent, flags);
        dest.writeTypedArray(this.mForecast, flags);
        dest.writeTypedArray(this.mHourlyForecast, flags);
    }

    public WeatherCurrent getCurrent() {
        return mCurrent;
    }

    public WeatherResponse setCurrent(WeatherCurrent current) {
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

    public WeatherForecast[] getHourlyForecast() {
        return mHourlyForecast;
    }

    public WeatherResponse setHourlyForecast(WeatherForecast[] hourlyForecast) {
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
