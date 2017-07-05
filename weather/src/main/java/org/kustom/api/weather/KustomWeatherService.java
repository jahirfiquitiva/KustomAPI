package org.kustom.api.weather;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import org.kustom.api.weather.model.WeatherRequest;
import org.kustom.api.weather.model.WeatherResponse;

public abstract class KustomWeatherService extends Service {

    public static final String SETTINGS_EXTRA_SERVICE = "org.kustom.weather.extra.SERVICE_NAME";

    public static final String SETTINGS_EXTRA_EXPLICIT = "org.kustom.weather.extra.EXPLICIT";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    protected abstract WeatherResponse fetchWeather(WeatherRequest request);

    private final IKustomWeatherService.Stub mBinder = new IKustomWeatherService.Stub() {
        @Override
        public WeatherResponse fetchWeather(WeatherRequest request) throws RemoteException {
            return KustomWeatherService.this.fetchWeather(request);
        }
    };
}
