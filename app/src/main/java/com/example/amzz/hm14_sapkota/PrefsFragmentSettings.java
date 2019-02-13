package com.example.amzz.hm14_sapkota;
// Amrita Sapkota
// L20432797
/**
 * Created by Amzz on 8/12/2017.
 */

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

public class PrefsFragmentSettings extends PreferenceFragment {
    final static String TAG = "PrefsFragmentSettings";


    public PrefsFragmentSettings() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.prefs_fragment_settings);
    }


    @Override
    public void onResume() {
        super.onResume();

        // Get the value of number from the preferences
        SharedPreferences sprefs = PreferenceManager
                .getDefaultSharedPreferences(Assets.context);
        int number = sprefs.getInt("number", 0);

        // Write the value of number in the summary text so it shows up on the
        // preferences screen
        Preference pref;
        pref = getPreferenceScreen().findPreference("prefs_highscore");
        pref.setSummary("" + number);
    }
}

