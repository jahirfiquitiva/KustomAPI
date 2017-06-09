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

    private WeatherRequest(Builder builder) {
        mLatitude = builder.mLatitude;
        mLongitude = builder.mLongitude;
        mCountry = builder.mCountry;
        mCountryCode = builder.mCountryCode;
        mPostalCode = builder.mPostalCode;
        mAdminArea = builder.mAdminArea;
        mLocality = builder.mLocality;
    }

    protected WeatherRequest(Parcel in) {
        mLatitude = in.readDouble();
        mLongitude = in.readDouble();
        mCountry = in.readString();
        mCountryCode = in.readString();
        mPostalCode = in.readString();
        mAdminArea = in.readString();
        mLocality = in.readString();
    }

    public double getLatitude() {
        return mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public String getCountry() {
        return mCountry;
    }

    public String getCountryCode() {
        return mCountryCode;
    }

    public String getPostalCode() {
        return mPostalCode;
    }

    public String getAdminArea() {
        return mAdminArea;
    }

    public String getLocality() {
        return mLocality;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.mLatitude);
        dest.writeDouble(this.mLongitude);
        dest.writeString(this.mCountry);
        dest.writeString(this.mCountryCode);
        dest.writeString(this.mPostalCode);
        dest.writeString(this.mAdminArea);
        dest.writeString(this.mLocality);
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

        public Builder() {
        }

        public Builder withLatitude(double val) {
            mLatitude = val;
            return this;
        }

        public Builder withLongitude(double val) {
            mLongitude = val;
            return this;
        }

        public Builder withCountry(String val) {
            mCountry = val;
            return this;
        }

        public Builder withCountryCode(String val) {
            mCountryCode = val;
            return this;
        }

        public Builder withPostalCode(String val) {
            mPostalCode = val;
            return this;
        }

        public Builder withAdminArea(String val) {
            mAdminArea = val;
            return this;
        }

        public Builder withLocality(String val) {
            mLocality = val;
            return this;
        }

        public WeatherRequest build() {
            return new WeatherRequest(this);
        }
    }
}