package org.kustom.api.weather;

import org.kustom.api.weather.model.WeatherRequest;
import org.kustom.api.weather.model.WeatherResponse;

interface IKustomWeatherService {
    WeatherResponse fetchWeather(in WeatherRequest request);
}
