package net.nitratine.priceperunit;

import android.app.Activity;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class Settings {
    private static SharedPreferences.Editor sp_edit;
    private static SharedPreferences sp_set;
    private static SharedPreferences sp_get;

    public static String currencySymbol;
    public static boolean showResultsTile;
    public static int rounding;
    public static boolean rememberData;

    public static void setUp(Activity app) {
        sp_set = app.getSharedPreferences("PricePerUnitSettings", MODE_PRIVATE);
        sp_get = app.getSharedPreferences("PricePerUnitSettings", MODE_PRIVATE);
    }

    public static void pullSettings() {
        currencySymbol = sp_get.getString("currencySymbol", "$");
        showResultsTile = sp_get.getBoolean("showResultsTile", true);
        rounding = sp_get.getInt("rounding", 2);
        rememberData = sp_get.getBoolean("rememberData", true);
    }

    public static void pushSettings() {
        sp_edit = sp_set.edit();
        sp_edit.putString("currencySymbol", currencySymbol);
        sp_edit.putBoolean("showResultsTile", showResultsTile);
        sp_edit.putInt("rounding", rounding);
        sp_edit.putBoolean("rememberData", rememberData);
        sp_edit.apply();
    }

}
