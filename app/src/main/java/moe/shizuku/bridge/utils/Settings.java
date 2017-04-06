package moe.shizuku.bridge.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Rikka on 2017/4/3.
 */

public class Settings {

    private static Settings sInstance;

    public static void init(Context context) {
        sInstance = new Settings(context, "settings");
    }

    private static Settings instance() {
        if (sInstance == null) {
            throw new IllegalStateException("Not initialized, call init in Application onCreate() first.");
        }

        return sInstance;
    }

    private SharedPreferences mPrefs;

    private Settings(Context context, String name) {
        mPrefs = context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }


    public static Settings putBoolean(String key, boolean value) {
        instance().mPrefs.edit()
                .putBoolean(key, value)
                .apply();

        return instance();
    }

    public static boolean getBoolean(String key, boolean def) {
        return instance().mPrefs.getBoolean(key, def);
    }

    public static Settings putInt(String key, int value) {
        instance().mPrefs.edit()
                .putInt(key, value)
                .apply();

        return instance();
    }

    public static int getInt(String key, int defValue) {
        return instance().mPrefs.getInt(key, defValue);
    }

    public static Settings putLong(String key, long value) {
        instance().mPrefs.edit()
                .putLong(key, value)
                .apply();

        return instance();
    }

    public static long getLong(String key, long defValue) {
        return instance().mPrefs.getLong(key, defValue);
    }

    public static Settings putString(String key, String value) {
        instance().mPrefs.edit()
                .putString(key, value)
                .apply();

        return instance();
    }

    public static String getString(String key, String defValue) {
        return instance().mPrefs.getString(key, defValue);
    }

    public static Settings putIntToString(String key, int value) {
        instance().mPrefs.edit()
                .putString(key, Integer.toString(value))
                .apply();

        return instance();
    }

    public static int getIntFromString(String key, int defValue) {
        return Integer.parseInt(instance().mPrefs.getString(key, Integer.toString(defValue)));
    }

    public static Settings putStringSet(String key, Set<String> value) {
        instance().mPrefs.edit()
                .putStringSet(key, value)
                .apply();

        return instance();
    }

    public static Set<String> getStringSet(String key, Set<String> defValue) {
        Set<String> set = new HashSet<>();
        set.addAll(instance().mPrefs.getStringSet(key, defValue));
        return set;
    }
}
