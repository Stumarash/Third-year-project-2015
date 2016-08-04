package com.inovatives.driverapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.inovatives.model.UserManager;


public class LoginScreen extends Activity {

    private EditText txtUserName, txtPassword;
    private ProgressDialog progressBar;
    private static String username, password;
    protected static final String PREFS_NAME= "Prefs";
    private static final String PREFS_USERNAME= "User";
    private static final String PREFS_PASS= "Pass";
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);


        txtUserName = (EditText) findViewById(R.id.username_textbox);
        txtPassword = (EditText) findViewById(R.id.password_textbox);


        pref = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);

        progressBar = new ProgressDialog(this);
        progressBar.setIndeterminate(true);
        progressBar.setMessage("Signing in...");

        if((pref.getString(PREFS_USERNAME,null) != null) && (pref.getString(PREFS_PASS,null) != null)){
            username = pref.getString(PREFS_USERNAME, null);
            password = pref.getString(PREFS_PASS, null);
            progressBar.show();
            if((pref.getString("stat",null) != null) && (pref.getString("registration",null) != null) &&
                    (pref.getString("vehicle",null) != null)) {
                Intent vehicleReg = new Intent(getApplicationContext(),EmergencyInformation.class);
                startActivity(vehicleReg);
                finish();
            }else{
                new LoginTask().execute(new UserManager());
            }

        }

    }



    //proceed to sending the vehicle registration
    public void cmdLogin_Click(View view){

        username= txtUserName.getText().toString().trim();
        password =txtPassword.getText().toString().trim();

        if(!username.isEmpty() && !password.isEmpty()){
            progressBar.show();
            new LoginTask().execute(new UserManager());
        }else{
            Toast.makeText(getApplicationContext(),"Please fill in all fields", Toast.LENGTH_SHORT).show();
        }

    }

    public void makeVisible(View view) {
        if(txtPassword.getTransformationMethod() == HideReturnsTransformationMethod.getInstance()) {
            txtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }else{
            txtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }
    }

    /**
     * inner class handling the async task
     */
    private  class LoginTask extends AsyncTask<UserManager,Long,Boolean> {

        @Override
        protected Boolean doInBackground(UserManager... params) {
            //this is executed in background thread

            return params[0].login(username,password);
        }
        @Override
        protected void onPostExecute(Boolean s) {
            progressBar.dismiss();
            if(s){
                getSharedPreferences(PREFS_NAME,MODE_PRIVATE).edit()
                        .putString(PREFS_USERNAME,username)
                        .putString(PREFS_PASS,password)
                        .apply();
                Intent vehicleReg = new Intent(getApplicationContext(),SendVehicleRegistration.class);
                startActivity(vehicleReg);
                finish();
            }else{
                Toast.makeText(getApplicationContext(),UserManager.responseToUser, Toast.LENGTH_SHORT).show();
            }
        }
    }

}
