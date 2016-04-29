package com.interdigital.android.samplemapdataapp;

import android.content.Context;
import android.content.SharedPreferences;

public class Storage {

    public static final String FILE_NAME = "one_transport_prefs";
    public static final String KEY_INSTALLATION_ID = "pref_installation_id";
    public static final String KEY_HOST_NAME = "pref_host_name";
    public static final String KEY_CSE_NAME = "pref_cse_name";
    public static final String KEY_USER_NAME = "pref_user_name";
    public static final String KEY_PASSWORD = "pref_password";

    public static String getInstallationId(Context context) {
        return getString(context, KEY_INSTALLATION_ID);
    }

    public static void putInstallationId(Context context, String installationId) {
        putString(context, KEY_INSTALLATION_ID, installationId);
    }

    public static String getHostName(Context context) {
        return getString(context, KEY_HOST_NAME);
    }

    public static String getCseName(Context context) {
        return getString(context, KEY_CSE_NAME);
    }

    public static String getUserName(Context context) {
        return getString(context, KEY_USER_NAME);
    }

    public static String getPassword(Context context) {
        return getString(context, KEY_PASSWORD);
    }

    private static String getString(Context context, String key) {
        return getSharedPreferences(context).getString(key, null);
    }

    private static void putString(Context context, String key, String value) {
        getSharedPreferences(context).edit().putString(key, value).commit();
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    }
}
