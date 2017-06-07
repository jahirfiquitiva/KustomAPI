package org.kustom.api.weather.model;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;

public class WeatherForecast extends WeatherCondition {
    @SerializedName("temp_max")
    private float mTempMax = Float.MAX_VALUE;
    @SerializedName("temp_min")
    private float mTempMin = Float.MIN_VALUE;
    @SerializedName("valid_from")
    private long mValidFrom = 0L;
    @SerializedName("valid_to")
    private long mValidTo = 0L;

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

    public long getValidFrom() {
        return mValidFrom;
    }

    public WeatherForecast setValidFrom(long validFrom) {
        mValidFrom = validFrom;
        return this;
    }

    public long getValidTo() {
        return mValidTo;
    }

    public WeatherForecast setValidTo(long validTo) {
        mValidTo = validTo;
        return this;
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
        dest.writeLong(this.mValidFrom);
        dest.writeLong(this.mValidTo);
    }

    protected WeatherForecast(Parcel in) {
        super(in);
        this.mTempMax = in.readFloat();
        this.mTempMin = in.readFloat();
        this.mValidFrom = in.readLong();
        this.mValidTo = in.readLong();
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
