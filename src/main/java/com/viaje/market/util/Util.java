package com.viaje.market.util;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;

public class Util {
    public static Double getBuyAmountByRation(Integer buyPercent, Double amount) {
        return (buyPercent.doubleValue() / 100) * amount;
    }

    public static Double getSellAmountFromRatio(Integer buyPercent, Double amount) {
        return ((100 - buyPercent.doubleValue()) / 100) * amount;
    }

    public static Double calculatetoBsiAmount(Double price, Double usdAmount, Integer round) {
        return round((usdAmount / price), round);
    }

    public static Double calculateProfitPrice(Double price, Integer profitPercentage, Integer round) {
        return round((price + (price * (profitPercentage / 100.00))), round);
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static String getCurrentTimeSecond() {
        //creating Calendar instance
        Calendar calendar = Calendar.getInstance();
        //Returns current time in millis
        long timeMilli2 = calendar.getTimeInMillis();
        return String.valueOf(timeMilli2 / 1000);
    }

    public static String statusTitle(Integer status) {
        switch (status) {
            case 1:
                return "Created";
            case 2:
                return "Failed";
            case 3:
                return "Finished";
            default:
                return "";
        }
    }
}
