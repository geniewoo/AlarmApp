package com.sungwoo.boostcamp.sungwooalarmapp;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by psw10 on 2017-01-28.
 */

public class WeatherGson {
    @SerializedName("result")
    private Result result;
    @SerializedName("weather")
    private Weather weather;

    public class Result {
        private int code;
        public int getCode() {
            return code;
        }
    }
    public class Weather {
        private List<Minutely> minutely = new ArrayList<>();
        public class Minutely {
            private Station station;
            private Sky sky;
            private Temperature temperature;
            public class Station {
                private String name;
                public String getName() {
                    return name;
                }
            }
            public class Sky {
                private String name;
                public String getName() {
                    return name;
                }
            }
            public class Temperature {
                private String tc;
                public String getTc() {
                    return tc;
                }
            }

            public Station getStation() {
                return station;
            }

            public Sky getSky() {
                return sky;
            }

            public Temperature getTemperature() {
                return temperature;
            }
        }
        public List<Minutely> getMinutely() {
            return minutely;
        }
    }
    public Result getResult() {
        return result;
    }
    public Weather getWeather() {
        return weather;
    }
}
