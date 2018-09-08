package com.example.ivan.myapplication;

import android.os.Bundle;
//import android.preference.CheckBoxPreference;
//import android.preference.ListPreference;
//import android.preference.Preference;
import android.preference.PreferenceActivity;
//import android.preference.PreferenceCategory;
//import android.preference.PreferenceScreen;

//import com.example.myapplication.R;

public class PrefActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref);

    }
}