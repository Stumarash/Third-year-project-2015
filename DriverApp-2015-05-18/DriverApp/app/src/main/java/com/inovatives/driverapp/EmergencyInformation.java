package com.inovatives.driverapp;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.inovatives.model.UserManager;

import org.w3c.dom.Text;

import java.util.concurrent.TimeUnit;


public class EmergencyInformation extends Activity implements TextWatcher {

    private TextView name;
    private TextView additional;
    private TextView reporter;
    private TextView address;
    private boolean running;
    private Updater updater;
    private String splits[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_information);

        name = (TextView)findViewById(R.id.text_emergency_type);
        additional = (TextView)findViewById(R.id.txt_additional_info);

        address = (TextView)findViewById(R.id.text_emergency_address);
        name.addTextChangedListener(this);
        address.addTextChangedListener(this);
        running = true;
        updater = new Updater();
        if(!updater.isAlive()) {
            updater.start();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    /**
     * innerclass handling the updating thread
     */
    private class Updater extends Thread{

        //instance variable
        private Updater() {
            super("Downloading thread");
        }

        @Override
        public void run() {

            while (running){
                try {
                    new EmergencyGet().execute(new UserManager());
                    TimeUnit.SECONDS.sleep(30);
                } catch (InterruptedException e) {
                    running = false;
                }
            }
        }
    }

    public void goToMaps(View view) {
        // Create a Uri from an intent string. Use the result to create an Intent.
        Uri gmmIntentUri = Uri.parse("google.navigation:q="+ address.getText().toString());

        // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);

        // Make the Intent explicit by setting the Google Maps package
        mapIntent.setPackage("com.google.android.apps.maps");

        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_emergency_information, menu);
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
        if (id == R.id.view_profile_title){
            Intent viewProfile = new Intent(this.getApplicationContext(), Profile.class);
            startActivity(viewProfile);
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
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private class EmergencyGet extends AsyncTask<UserManager,Integer,String>{
        @Override
        protected String doInBackground(UserManager... params) {
            return params[0].getEmergency("EmergencyGet");
        }

        @Override
        protected void onPostExecute(String s) {

            String splits[] = s.split(",");
            if (splits.length > 0) {
                for(int i=0; i<splits.length; i++) {
                    name.setText(splits[i]);
                    additional.setText("");
                    address.setText(splits[1]);
                }

            }


        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        running = false;

    }
}
