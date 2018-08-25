package com.example.ivan.myapplication;

import android.app.Application;
import android.content.res.Configuration;

import java.util.Locale;

public class SmenaLanguage extends Application {
    String lang1;
    Locale localeg;
    public void main(){

        lang1 = getResources().getConfiguration().locale.getLanguage();

        Configuration conf = getResources().getConfiguration();


        localeg = new Locale("uk","UA");
        lang1 = localeg.getLanguage();


        conf.setLocale(localeg);

        getBaseContext().getResources().updateConfiguration(conf, getResources().getDisplayMetrics());
    }

}
