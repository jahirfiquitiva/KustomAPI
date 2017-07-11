package org.kustom.api.weather.model;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;

public class WeatherInstant extends WeatherCondition {
    @SerializedName("temp")
    private float mTemp = Float.MIN_VALUE;

    public WeatherInstant() {
    }

    protected WeatherInstant(Parcel in) {
        super(in);
        mTemp = in.readFloat();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeFloat(mTemp);
    }

    @Override
    public String toString() {
        return getCondition() + " " + (int) mTemp + "C";
    }

    /**
     * @return termperature in Celsius
     */
    @Override
    public float getTemperature() {
        return mTemp;
    }

    /**
     * Set temperature in Celsius
     *
     * @param temp current temp in Celsius
     */
    public void setTemperature(float temp) {
        mTemp = temp;
    }

    @SuppressWarnings("unused")
    public static final Creator<WeatherInstant> CREATOR = new Creator<WeatherInstant>() {
        @Override
        public WeatherInstant createFromParcel(Parcel in) {
            return new WeatherInstant(in);
        }

        @Override
        public WeatherInstant[] newArray(int size) {
            return new WeatherInstant[size];
        }
    };
}