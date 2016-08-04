package com.improveresponse.emergencyapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import com.improveresponse.model.UpdaterService;
import com.improveresponse.model.UserManager;

/**
 *  Splash Screen class handling the loading of the application screen
 * @author Dokes
 */
public class SplashScreen extends Activity {

    //instance variables
    private static final String PREFS_NAME= "Prefs";
    private static final String PREFS_USERNAME= "User";
    private static final String PREFS_PASS= "Pass";
    private SharedPreferences pref;
    private static String username, password;
    private ProgressDialog progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);



        pref = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        progressBar= new ProgressDialog(this);
        progressBar.setIndeterminate(true);
        progressBar.setMessage("Signing in...");

        startService(new Intent(this, UpdaterService.class));

        if((pref.getString(PREFS_USERNAME,null) != null) && (pref.getString(PREFS_PASS,null) != null)){
            username = pref.getString(PREFS_USERNAME, null);
            password = pref.getString(PREFS_PASS, null);
            progressBar.show();

            new CheckLogin().execute(new UserManager());

        }else{
            Intent splash = new Intent(getApplicationContext(), LoginScreen.class);
            startActivity(splash);
            finish();
        }
    }

    private  class CheckLogin extends AsyncTask<UserManager,Long,Boolean> {

        @Override
        protected Boolean doInBackground(UserManager... params) {
            //this is executed in background thread
            return params[0].login(username,password);
        }

        @Override
        protected void onPostExecute(Boolean s) {
            progressBar.dismiss();
            if(s){
                Intent outside = new Intent(getApplicationContext(),EmergencyChoice.class);
                outside.putExtra("User",username);
                startActivity(outside);
                finish();

            }else{
                Intent splash = new Intent(getApplicationContext(), LoginScreen.class);
                startActivity(splash);
                finish();
            }
        }
    }

}
