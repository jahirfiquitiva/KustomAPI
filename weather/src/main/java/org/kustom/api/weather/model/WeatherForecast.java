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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeFloat(this.mTempMax);
        dest.writeFloat(this.mTempMin);
    }

    protected WeatherForecast(Parcel in) {
        super(in);
        this.mTempMax = in.readFloat();
        this.mTempMin = in.readFloat();
    }

    public static final Creator<WeatherForecast> CREATOR = new Creator<WeatherForecast>() {
        @Override
        public WeatherForecast createFromParcel(Parcel source) {
            return new WeatherForecast(source);
        }

        @Override
        public WeatherForecast[] newArray(int size) {
            return new WeatherForecast[size];
        }
    };
}
