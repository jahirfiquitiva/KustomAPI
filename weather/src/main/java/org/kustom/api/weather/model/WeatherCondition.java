package org.kustom.api.weather.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public abstract class WeatherCondition implements Parcelable {
    @SerializedName("condition")
    private String mCondition = "";
    @SerializedName("wind_deg")
    private int mWindDeg = 0;
    @SerializedName("wind_speed")
    private float mWindSpeed = 0f;
    @SerializedName("pressure")
    private float mPressure = 0;
    @SerializedName("humidity")
    private int mHumidity = 0;
    @SerializedName("code")
    private WeatherCode mCode = WeatherCode.NOT_AVAILABLE;

    public WeatherCondition() {
    }

    protected WeatherCondition(Parcel in) {
        mCondition = in.readString();
        mWindDeg = in.readInt();
        mWindSpeed = in.readFloat();
        mPressure = in.readFloat();
        mHumidity = in.readInt();
        mCode = WeatherCode.valueOf(in.readString());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mCondition);
        dest.writeInt(mWindDeg);
        dest.writeFloat(mWindSpeed);
        dest.writeFloat(mPressure);
        dest.writeInt(mHumidity);
        dest.writeString(mCode.toString());
    }

    /**
     * @return current weather condition in readable format
     */
    public String getCondition() {
        return mCondition;
    }

    /**
     * Sets condition in readable format
     *
     * @param condition the condition possibly in request language
     */
    public void setCondition(String condition) {
        mCondition = condition;
    }

    /**
     * A Kustom compatible weather code properly converted
     *
     * @return the code of current weather
     */
    public WeatherCode getCode() {
        return mCode;
    }

    /**
     * Sets Kustom standard weather code
     *
     * @param code the code to be set
     */
    public void setCode(WeatherCode code) {
        mCode = code;
    }

    /**
     * @return wind direction in degrees
     */
    public int getWindDeg() {
        return mWindDeg;
    }

    /**
     * Set wind direction in degrees
     *
     * @param value direction in degrees to north
     */
    public void setWindDeg(int value) {
        mWindDeg = value;
    }

    /**
     * @return wind speed in meters per second
     */
    public float getWindSpeed() {
        return mWindSpeed;
    }

    /**
     * Set wind speed in m/s
     *
     * @param value in meters per second
     */
    public void setWindSpeed(float value) {
        mWindSpeed = value;
    }

    /**
     * @return pressure in millibars
     */
    public float getPressure() {
        return mPressure;
    }

    /**
     * Set pressure in millibars
     *
     * @param pressure in millibars
     */
    public void setPressure(float pressure) {
        mPressure = pressure;
    }

    /**
     * @return humidity in percentage
     */
    public int getHumidity() {
        return mHumidity;
    }

    /**
     * Set humidity in %
     *
     * @param humidity in percentage
     */
    public void setHumidity(int humidity) {
        mHumidity = humidity;
    }

    /**
     * @return termperature in celsius
     */
    public abstract float getTemperature();
}