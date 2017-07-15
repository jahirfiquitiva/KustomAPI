package org.kustom.api.weather.model;

public interface WeatherRainInfo {

    /**
     * @return rain chance in percentage 0-100
     */
    int getRainChance();

    /**
     * @return rain precipitations in millimiters
     */
    float getRain();
}
