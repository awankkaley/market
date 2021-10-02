package com.viaje.market.util;


public class Util {
    public static Double getAmountBuyFromPercentage(Integer buyPercent, Double amount) {
        return (buyPercent.doubleValue() / 100) * amount;
    }

    public static Double getAmountSellFromPercentage(Integer buyPercent, Double amount) {
        double percent = 100.0;
        double sellPercent = percent - buyPercent.doubleValue();
        return (sellPercent / 100) * amount;
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
