package com.improveresponse.emergencyapp;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;

/**
 * ProfileEdit class dealing with editing of the civilian profile
 * @author Dokes
 */
public class ProfileEdit extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        //button for back functionality
        ActionBar action = getActionBar();
        action.setDisplayHomeAsUpEnabled(true);
    }


}
