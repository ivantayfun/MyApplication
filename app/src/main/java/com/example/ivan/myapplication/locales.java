package com.example.ivan.myapplication;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import java.util.Locale;

public class locales extends Application {
    private Locale locale;

    @Override
    public void onCreate() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String lang = preferences.getString("lang", "ru");
        if (lang.equals("default")){
            lang =getResources().getConfiguration().locale.getCountry();

        }

        locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
//"ru-rRU"
        super.onCreate();
        //locale = new Locale("ru");
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,null);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        locale = new Locale("ru");
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration,null);

    }
}
