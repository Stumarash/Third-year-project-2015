package com.improveresponse.emergencyapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.improveresponse.model.UserManager;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * class handling the screen to reporting the emergency
 */
public class IncidentReport extends BaseActivity implements LocationListener {

    //instance variables
    private Geocoder geocoder;
    private TextView txtlocation;
    private TextView txtReportHeader;
    public boolean isGPSEnabled = false;// flag for GPS status
    private boolean isNetworkEnabled = false;   // flag for network status
    private boolean canGetLocation = false;     // flag for GPS status
    Location location;                  // location
    private String EmergencyType;
    private String EmergencyListId;
    private String Emergencylocation;
    private String extra_info;
    private String date;
    private String username;
    private String id;
    private ProgressDialog progressBar;
    private SharedPreferences pref;

    // The minimum distance to change Updates in meters
    private static final long LOCATION_REFRESH_TIME = 10; // 10 meter

    // The minimum time between updates in milliseconds
    private static final long LOCATION_REFRESH_DISTANCE = 1; // 1 second

    // Declaring a Location Manager
    protected static LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incident_report);

        //linking components
        txtlocation = (TextView)findViewById(R.id.location_text);
        txtReportHeader = (TextView)findViewById(R.id.medical_report_header_review);
        geocoder = new Geocoder(this);

        pref = getSharedPreferences("Prefs",MODE_PRIVATE);

        Intent intent = getIntent();
        EmergencyType = intent.getStringExtra("Emergency_Type");
        EmergencyListId = intent.getStringExtra("Emergency_id");
        extra_info = intent.getStringExtra("Additional");

        id = intent.getStringExtra("Emergency_Name");
        if(!id.isEmpty()){

            txtReportHeader.setText(id);
        }
    }

    //button to report emergency and to take user to review screen
    public void goReview(View view){
        progressBar= new ProgressDialog(this);
        progressBar.setIndeterminate(true);
        progressBar.setMessage("Reporting...");
        new ReportEmergency().execute(new UserManager());

    }

    /**
     * Function to check what provider is enabled
     */
    private void getProviderType(){

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //checking if the location manager is null
        if(locationManager != null) {

            // getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                showSettingsAlert();
            } else {
                this.canGetLocation = true;
            }
        }
    }

    /**
     * Function to show settings alert dialog On pressing Settings button will
     * launch Settings Options
     * */
    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        // Setting Dialog Title
        alertDialog.setTitle("GPS settings");

        // Setting Dialog Message
        alertDialog
                .setMessage("GPS is not enabled. Do you want to go to settings menu?\n" +
                        "*hint* enable both to get a more accurate location.");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        startActivity(new Intent(getApplicationContext(),EmergencyChoice.class));
                    }
                });

        // Showing Alert Message
        alertDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getProviderType();
        getLocation();
    }

    /**
     * Function to check GPS/wifi enabled
     * @return boolean
     * */
    public boolean getcanGetLocation() {
        return this.canGetLocation;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }
    }

    /**
     *Function getting the location
     */
    public void getLocation(){
        if(getcanGetLocation()) {

            if (isNetworkEnabled) {
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, LOCATION_REFRESH_TIME,
                        LOCATION_REFRESH_DISTANCE, this);
            }

            if (isGPSEnabled) {
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
                        LOCATION_REFRESH_DISTANCE, this);
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
        String text;
        if(location != null){
            text = String.format(
                    "Lat:\t %f\n" +
                            "Long:\t %f\n" +
                            "Alt:\t %f", location.getLatitude(),
                    location.getLongitude(), location.getAltitude());
            txtlocation.setText(text);
        }
        // Perform geocoding for this location
        try {
            List<Address> addresses = geocoder.getFromLocation(
                    location.getLatitude(), location.getLongitude(), 10); //
            for (Address address : addresses) {
                txtlocation.append("\n" + address.getAddressLine(0)); //
            }

            Emergencylocation = addresses.get(0).getAddressLine(0) +" "+ addresses.get(1).getAddressLine(0) +" "+ addresses.get(2).getAddressLine(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void onProviderEnabled(String provider) {
        if(isGPSEnabled) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
                    LOCATION_REFRESH_DISTANCE, this);
        }else if(isNetworkEnabled){
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, LOCATION_REFRESH_TIME,
                    LOCATION_REFRESH_DISTANCE, this);
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        locationManager.removeUpdates(this);

    }

    /**
     * inner class handling the async task
     */
    private  class ReportEmergency extends AsyncTask<UserManager,Long,Boolean> {

        @Override
        protected Boolean doInBackground(UserManager... params) {
            //this is executed in background thread
            return params[0].reportEmergency(EmergencyType,Integer.parseInt(EmergencyListId),Emergencylocation,extra_info,date,username);
        }

        @Override
        protected void onPreExecute() {
            progressBar.show();
            if(pref.getString("User",null) != null){
                username = pref.getString("User",null);
            }

            date = new Date().toLocaleString();

        }

        @Override
        protected void onPostExecute(Boolean s) {
            progressBar.dismiss();
            if(s){
                Intent review = new Intent(getApplicationContext(), ReportReview.class);
                review.putExtra("Type",EmergencyType);
                review.putExtra("Name",id);
                review.putExtra("Time",date);
                review.putExtra("location",Emergencylocation);
                startActivity(review);
            }else{
                Toast.makeText(getApplicationContext(), UserManager.responseToUser, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
