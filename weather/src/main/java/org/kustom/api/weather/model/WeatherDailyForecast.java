package org.kustom.api.weather.model;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class WeatherDailyForecast extends WeatherCondition {
    @SerializedName("temp_max")
    private float mTempMax = Float.MAX_VALUE;
    @SerializedName("temp_min")
    private float mTempMin = Float.MIN_VALUE;
    @SerializedName("rain_chance")
    private int mRainChance = 0;
    @SerializedName("rain")
    private float mRain = 0;

    public WeatherDailyForecast() {
    }

    protected WeatherDailyForecast(Parcel in) {
        super(in);
        mTempMax = in.readFloat();
        mTempMin = in.readFloat();
        mRainChance = in.readInt();
        mRain = in.readFloat();
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
        dest.writeInt(mRainChance);
        dest.writeFloat(mRain);
    }

    /**
     * @return average temperature in centigrades
     */
    public float getTempAvg() {
        return (mTempMin + mTempMax) / 2;
    }

    /**
     * @return max temperature in centigrades
     */
    public float getTempMax() {
        return mTempMax;
    }

    /**
     * Sets max day temperature
     *
     * @param value the temp in Celsius
     */
    public void setTempMax(float value) {
        mTempMax = value;
    }

    /**
     * @return min temperature in centigrades
     */
    public float getTempMin() {
        return mTempMin;
    }

    /**
     * Sets min day temperature
     *
     * @param value the temp in Celsius
     */
    public void setTempMin(float value) {
        mTempMin = value;
    }

    /**
     * @return rain chance in percentage 0-100
     */
    public int getRainChance() {
        return mRainChance;
    }

    /**
     * Sets current chance of rain
     *
     * @param rainChance the chance of rain in percentage 0-100
     */
    public void setRainChance(int rainChance) {
        mRainChance = Math.max(0, Math.min(100, rainChance));
    }

    /**
     * @return rain precipitations in millimiters
     */
    public float getRain() {
        return mRain;
    }

    /**
     * Sets expected rain precipitations
     *
     * @param rain in millimiters
     */
    public void setRain(float rain) {
        mRain = rain;
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
