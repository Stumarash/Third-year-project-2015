package com.improveresponse.emergencyapp;

//imports
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;

/**
 *  Base activity class handling common menu functionality
 * Created by Dokes on 2015-04-16.
 */
public class BaseActivity extends Activity
{

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_emergency, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //checking for  menu clicks
        if(id == R.id.profile_pic){

            return true;
        }

        if(id==R.id.profile_view){
            Intent to_next = new Intent(getApplicationContext(), Profile.class);
            startActivity(to_next);
            return true;
        }

        if(id==R.id.profile_view_report){
            Intent next = new Intent(getApplicationContext(), ReportView.class);
            startActivity(next);
            return true;
        }

        if(id==R.id.profile_about){
                AlertDialog.Builder build = new AlertDialog.Builder(this);
                build.setTitle(R.string.app_name);
                build.setMessage(R.string.app_slogan);
                build.setPositiveButton(R.string.activity_profile_ok, null);
                AlertDialog start =build.create();
                start.show();

        }

        if(id==R.id.logout_menu){
            SharedPreferences pref = getSharedPreferences("Prefs",MODE_PRIVATE);
            pref.edit().clear().apply();
            Intent outside = new Intent(getApplicationContext(),LoginScreen.class);
            startActivity(outside);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
