package com.inovatives.driverapp;

    import android.app.ActionBar;
    import android.app.Activity;
    import android.app.ProgressDialog;
    import android.content.Intent;
    import android.content.SharedPreferences;
    import android.os.AsyncTask;
    import android.os.Bundle;
    import android.view.Menu;
    import android.view.MenuItem;
    import android.view.View;
    import android.widget.AdapterView;
    import android.widget.ArrayAdapter;
    import android.widget.AutoCompleteTextView;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.Spinner;
    import android.widget.Toast;

    import com.inovatives.model.UserManager;

public class SendVehicleRegistration extends Activity implements AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {

    private AutoCompleteTextView autoCompleteTextView;
    private  Spinner spinner;
    private static final String[] REG_NUMBWESRS= new String[]{"BB-321-GP","CK-158-GP","GW-002-GP","LK-879-GP","PA-396-GP",
            "PO-023-GP","PW-003-GP","QQ-157-GP","RW-112-GP","SS-809-GP"};

    private ArrayAdapter arrayAdapter;
    private ArrayAdapter<String> adapter;
    private ProgressDialog progressBar;
    private String user;
    private String registration;
    private String stat;
    private String vehicle;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_vehicle_registration);

        progressBar = new ProgressDialog(this);
        progressBar.setIndeterminate(true);
        progressBar.setMessage("Authenticating vehicle...");

        spinner = (Spinner)findViewById(R.id.vehicles_spinner);
        autoCompleteTextView = (AutoCompleteTextView)findViewById(R.id.text_vehicle);

         adapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,REG_NUMBWESRS);
        autoCompleteTextView.setAdapter(adapter);

         arrayAdapter = ArrayAdapter.createFromResource(this,R.array.registration_vehicle_array,android.R.layout.simple_list_item_1);
        spinner.setAdapter(arrayAdapter);

        autoCompleteTextView.setOnItemClickListener(this);
        spinner.setOnItemSelectedListener(this);

        pref = getSharedPreferences("Prefs",MODE_PRIVATE);

        if((pref.getString("stat",null) != null) && (pref.getString("registration",null) != null) &&
                (pref.getString("vehicle",null) != null)){
            registration = pref.getString("registration",null);
            vehicle = pref.getString("vehicle",null);
            stat = "offline";
            new RegistrationTask().execute(new UserManager());

        }
    }

    //proceed to sending the vehicle registration
    public void goEmergency(View view){
        registration = autoCompleteTextView.getText().toString().trim();
        stat = "online";

        for (int i = 0; i < REG_NUMBWESRS.length; i++) {
            if (REG_NUMBWESRS[i].equals(registration)) {
                progressBar.show();
                new RegistrationTask().execute(new UserManager());
                break;
            }
        }

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

         String selection = (String) parent.getItemAtPosition(position);
        int pos = -1;

        for (int i = 0; i < REG_NUMBWESRS.length; i++) {
            if (REG_NUMBWESRS[i].equals(selection.trim())) {
                pos = i;
                break;
            }
        }

        registration = REG_NUMBWESRS[pos];
       // Toast.makeText(getApplicationContext(),REG_NUMBWESRS[position],Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(spinner.getSelectedItemPosition() == position){
            if(position > 0){
                vehicle = arrayAdapter.getItem(position).toString();
            }
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        autoCompleteTextView.setText("");
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
            pref.edit().clear().apply();
            Intent viewEmergency = new Intent(getApplicationContext(), LoginScreen.class);
            startActivity(viewEmergency);

        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * inner class handling the async task
     */
    private  class RegistrationTask extends AsyncTask<UserManager,Long,Boolean> {

        @Override
        protected Boolean doInBackground(UserManager... params) {
            //this is executed in background thread
            return params[0].vehicleRegistration(user,stat,registration,vehicle);
        }

        @Override
        protected void onPreExecute() {

            if((pref.getString("User",null) != null)) {
                user = pref.getString("User", null);
            }

        }

        @Override
        protected void onPostExecute(Boolean s) {
            progressBar.dismiss();
            if(s){
                if(stat.equals("online")) {
                    pref.edit().putString("stat", stat).putString("registration", registration).putString("vehicle", vehicle).apply();
                    Intent viewEmergency = new Intent(getApplicationContext(), EmergencyInformation.class);
                    startActivity(viewEmergency);
                    finish();
                    Toast.makeText(getApplicationContext(), UserManager.responseToUser, Toast.LENGTH_SHORT).show();
                }else{
                    pref.edit().clear().apply();
                    Intent viewEmergency = new Intent(getApplicationContext(), LoginScreen.class);
                    startActivity(viewEmergency);
                    finish();
                }
            }else{
                Toast.makeText(getApplicationContext(), UserManager.responseToUser, Toast.LENGTH_SHORT).show();
            }
        }
    }

}
