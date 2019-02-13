package com.example.amzz.hm14_sapkota;
// Amrita Sapkota
// L20432797
/**
 * Created by Amzz on 8/12/2017.
 */

import java.util.List;

import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;


public class PrefsActivity extends PreferenceActivity
{
    // Tag for debug messages
    final static String TAG = "PrefsActivity";


    @Override
    public void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }


    @Override
    protected boolean isValidFragment (String fragmentName)
    {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB)
            return true;
        else if (PrefsFragmentSettings.class.getName().equals(fragmentName))
            return true;

        return false;
    }


    @Override
    public void onBuildHeaders (List<Header> target)
    {
        // Use this to load an XML file containing references to multiple fragments (a multi-screen preferences screen)
        // loadHeadersFromResource(R.xml.prefs_headers, target);

        // Use this to load an XML file containing a single preferences screen
        getFragmentManager().beginTransaction().replace(android.R.id.content, new PrefsFragmentSettings()).commit();
    }


}
