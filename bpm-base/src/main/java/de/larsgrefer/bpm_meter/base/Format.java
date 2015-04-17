package de.larsgrefer.bpm_meter.base;

import java.text.DecimalFormat;
import java.text.ParseException;

public class Format
{
    static DecimalFormat xpmDecimalFormat = new DecimalFormat("0.##");
    static DecimalFormat xdDecimalFormat = new DecimalFormat("0.#####");

    public static String perMinuteFormat(Double dbl){
        return xpmDecimalFormat.format(dbl);
    }

    public static String durationFormat(Double dbl){
        return xdDecimalFormat.format(dbl);
    }

    public static Double fromString(String s){
        DecimalFormat df = new DecimalFormat();
        double d;
        try {
            s = s.replace(",", ".");
            d = df.parse(s).doubleValue();
        } catch (ParseException e1) {
            try {
                s = s.replace(".", ",");
                d = df.parse(s).doubleValue();
            } catch (ParseException e2) {
                throw new RuntimeException("Could not parse input");
            }

        }
        return d;
    }
}
