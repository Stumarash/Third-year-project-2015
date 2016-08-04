package com.inovatives.driverapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class Profile extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.profile_about){
            Intent aboutUs = new Intent(this.getApplicationContext(), About.class);
            startActivity(aboutUs);
            return true;
        }

        if (id == R.id.logout_menu){
            
            Intent logout = new Intent(this.getApplicationContext(), SendVehicleRegistration.class);
            startActivity(logout);
        }

        if (id == R.id.changeVehicle){
            Intent change  = new Intent(this.getApplicationContext(), SendVehicleRegistration.class);
            startActivity(change);
        }



        return super.onOptionsItemSelected(item);
    }
}
