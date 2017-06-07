package org.kustom.api.weather.model;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;

public class WeatherForecast extends WeatherCondition {
    @SerializedName("temp_max")
    private float mTempMax = Float.MAX_VALUE;
    @SerializedName("temp_min")
    private float mTempMin = Float.MIN_VALUE;

    public WeatherForecast() {
    }

    protected WeatherForecast(Parcel in) {
        super(in);
        mTempMax = in.readFloat();
        mTempMin = in.readFloat();
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

    public float getTempMax() {
        return mTempMax;
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

    @SuppressWarnings("unused")
    public static final Creator<WeatherForecast> CREATOR = new Creator<WeatherForecast>() {
        @Override
        public WeatherForecast createFromParcel(Parcel in) {
            return new WeatherForecast(in);
        }

        @Override
        public WeatherForecast[] newArray(int size) {
            return new WeatherForecast[size];
        }
    };
}
