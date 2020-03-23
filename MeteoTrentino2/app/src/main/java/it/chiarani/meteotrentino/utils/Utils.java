package it.chiarani.meteotrentino.utils;

public class Utils {
    public static double getCelsiusFromFahrenheit(double data) {
        return data - 273.15;
    }

    public static double getKmhFromMs(double data) {
        return  data * 3.6;
    }
}
