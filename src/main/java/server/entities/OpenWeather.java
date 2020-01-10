package server.entities;

public class OpenWeather {
    private Weather[] weather;

    public String toString() {
        return weather[0].description;
    }

    public String getMain() {
        return weather[0].main;
    }

    public class Weather {
        private String main;
        private String description;
    }
}
