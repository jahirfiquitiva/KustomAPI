package org.kustom.api.weather.model;

import android.os.Parcel;
import android.os.Parcelable;

@SuppressWarnings("unused")
public class WeatherRequest implements Parcelable {
    private final double mLatitude;
    private final double mLongitude;
    private final String mCountry;
    private final String mCountryCode;
    private final String mPostalCode;
    private final String mAdminArea;
    private final String mLocality;
    private final String mLang;

    private WeatherRequest(Builder builder) {
        mLatitude = builder.mLatitude;
        mLongitude = builder.mLongitude;
        mCountry = builder.mCountry;
        mCountryCode = builder.mCountryCode;
        mPostalCode = builder.mPostalCode;
        mAdminArea = builder.mAdminArea;
        mLocality = builder.mLocality;
        mLang = builder.mLang;
    }

    protected WeatherRequest(Parcel in) {
        mLatitude = in.readDouble();
        mLongitude = in.readDouble();
        mCountry = in.readString();
        mCountryCode = in.readString();
        mPostalCode = in.readString();
        mAdminArea = in.readString();
        mLocality = in.readString();
        mLang = in.readString();
    }

    /**
     * @return latitude of the point were we want to fetch weather for
     */
    public double getLatitude() {
        return mLatitude;
    }

    /**
     * @return longitude of the point were we want to fetch weather for
     */
    public double getLongitude() {
        return mLongitude;
    }

    /**
     * @return reverse geocoded country of the place we are getting weather from
     */
    public String getCountry() {
        return mCountry;
    }

    /**
     * @return reverse geocoded country code (es US) of the place we are getting weather from
     */
    public String getCountryCode() {
        return mCountryCode;
    }

    /**
     * @return reverse geocoded postal code of the place we are getting weather from
     */
    public String getPostalCode() {
        return mPostalCode;
    }

    /**
     * @return reverse geocoded admin area of the place we are getting weather from
     */
    public String getAdminArea() {
        return mAdminArea;
    }

    /**
     * @return reverse geocoded locality (es New York City) of the place we are getting weather from
     */
    public String getLocality() {
        return mLocality;
    }

    /**
     * @return preferred lang for the request, this will be appended to the request if available
     */
    public String getLang() {
        return mLang;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(mLatitude);
        dest.writeDouble(mLongitude);
        dest.writeString(mCountry);
        dest.writeString(mCountryCode);
        dest.writeString(mPostalCode);
        dest.writeString(mAdminArea);
        dest.writeString(mLocality);
        dest.writeString(mLang);
    }

    public static final Creator<WeatherRequest> CREATOR = new Creator<WeatherRequest>() {
        @Override
        public WeatherRequest createFromParcel(Parcel source) {
            return new WeatherRequest(source);
        }

        @Override
        public WeatherRequest[] newArray(int size) {
            return new WeatherRequest[size];
        }
    };

    public static final class Builder {
        private double mLatitude = 0D;
        private double mLongitude = 0D;
        private String mCountry = "";
        private String mCountryCode = "";
        private String mPostalCode = "";
        private String mAdminArea = "";
        private String mLocality = "";
        private String mLang = "en";

        public Builder() {
        }

        public Builder withLatitude(double latitude) {
            mLatitude = latitude;
            return this;
        }

        public Builder withLongitude(double longitude) {
            mLongitude = longitude;
            return this;
        }

        public Builder withCountry(String country) {
            mCountry = country;
            return this;
        }

        public Builder withCountryCode(String countryCode) {
            mCountryCode = countryCode;
            return this;
        }

        public Builder withPostalCode(String postalCode) {
            mPostalCode = postalCode;
            return this;
        }

        public Builder withAdminArea(String adminArea) {
            mAdminArea = adminArea;
            return this;
        }

        public Builder withLocality(String locality) {
            mLocality = locality;
            return this;
        }

        public Builder withLang(String lang) {
            mLang = lang;
            return this;
        }

        public WeatherRequest build() {
            return new WeatherRequest(this);
        }
    }
}