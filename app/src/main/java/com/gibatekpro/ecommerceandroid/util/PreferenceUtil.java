package com.gibatekpro.ecommerceandroid.util;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * This class is used to save, retrieve and delete state, integers,
 * boolean, strings etc.
 * */

// requires     implementation 'androidx.preference:preference:1.2.0'
public class PreferenceUtil {

    public static void save(Context context, String key, String value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putString(key, value).apply();
    }

    public static void save(Context context, String key, boolean value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putBoolean(key, value).apply();
    }

    public static void save(Context context, String key, long value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putLong(key, value).apply();
    }

    public static void save(Context context, String key, int value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putInt(key, value).apply();
    }

    public static String get(Context context, String key, String defaultValue) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(key, defaultValue);
    }

    public static void remove(Context context, String key) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().remove(key).apply();
    }

    //This method returns the default value - false if the key does not exist
    public static boolean get(Context context, String key, boolean defaultValue) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    public static long get(Context context, String key, long defaultValue) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getLong(key, defaultValue);
    }

    public static int get(Context context, String key, int defaultValue) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getInt(key, defaultValue);
    }

    public static void clear(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().clear().apply();
    }

    private static void clear(Context context, String key) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new BufferedOutputStream(context.openFileOutput(key, Context.MODE_PRIVATE)))) {
            outputStream.writeObject(new Object());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //TODO: Check if this worked: Collection <T> was replaced with Collection<?>
    public static <T> Collection<?> getCachedItems(Context context, String cacheName, Class<T> tClass) {
        try (ObjectInputStream inputStream = new ObjectInputStream(new BufferedInputStream(context.openFileInput(cacheName)))) {
            return (Collection<?>) inputStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public static <T> void cacheItems(Context context, String cacheName, Collection<T> items) {
        if (items == null) {
            return;
        }
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new BufferedOutputStream(context.openFileOutput(cacheName, Context.MODE_PRIVATE)))) {
            outputStream.writeObject(items);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void save(Context context, String key, Set<String> value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putStringSet(key, value).apply();
    }

    public static Set<String> get(Context context, String key) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getStringSet(key, new HashSet<String>());
    }
}
