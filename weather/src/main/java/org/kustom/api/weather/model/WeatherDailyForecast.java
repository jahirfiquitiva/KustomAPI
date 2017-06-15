package org.kustom.api.weather.model;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;

public class WeatherDailyForecast extends WeatherCondition {
    @SerializedName("temp_max")
    private float mTempMax = Float.MAX_VALUE;
    @SerializedName("temp_min")
    private float mTempMin = Float.MIN_VALUE;

    public WeatherDailyForecast() {
    }

    public float getTempMax() {
        return mTempMax;
    }

    public float getTempAvg() {
        return (mTempMin + mTempMax) / 2;
    }

    public void setTempMax(float value) {
        mTempMax = value;
    }

    public float getTempMin() {
        return mTempMin;
    }

    public void setTempMin(float value) {
        mTempMin = value;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeFloat(mTempMax);
        dest.writeFloat(mTempMin);
    }

    protected WeatherDailyForecast(Parcel in) {
        super(in);
        mTempMax = in.readFloat();
        mTempMin = in.readFloat();
    }

    public static final Creator<WeatherDailyForecast> CREATOR = new Creator<WeatherDailyForecast>() {
        @Override
        public WeatherDailyForecast createFromParcel(Parcel source) {
            return new WeatherDailyForecast(source);
        }

        @Override
        public WeatherDailyForecast[] newArray(int size) {
            return new WeatherDailyForecast[size];
        }
    };
}
