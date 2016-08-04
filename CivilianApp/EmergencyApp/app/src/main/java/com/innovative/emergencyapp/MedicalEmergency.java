package com.innovative.emergencyapp;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.innovative.model.DBHelper;
import com.innovative.model.UpdaterService;

/**
 * Medical  emergency class dealing with medical emergencies
 * @author Dokes
 */
public class MedicalEmergency extends Activity {

    //list to change of  medical emergencies
    //instance variables
    private  int count;
    String[] emergencies = new String[20];

    ListAdapter medicalList;
    ListView medicListView;
    SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_emergency);

        //action bar back functionality
        ActionBar actionBar = getActionBar();
        assert actionBar!= null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        SQLiteDatabase db = UpdaterService.dbHelper.getReadableDatabase();
        try {
            Cursor cursor = db.query(DBHelper.TABLE1, DBHelper.DB1_COLUMNS, null, null, null, null, DBHelper.C_Emergency_ID + " ASC");
            try {
                if (cursor.moveToFirst()) {
                    count = 0;
                    while (!cursor.isAfterLast()) {
                        String id = cursor.getString(cursor.getColumnIndex(DBHelper.C_Emergency_ID));
                        String type = cursor.getString(cursor.getColumnIndex(DBHelper.C_Emergency_Name));
                        if(count < 20) {
                            emergencies[count] = id + ":" + type;
                            count++;
                        }
                        FireEmergency.Chemical_fire = type;
                        FireEmergency.Structural_Informal = type;
                        FireEmergency.Structural_Formal = type;
                        FireEmergency.Vehicle_fire = type;
                        FireEmergency.Aircraft_accident = type;
                        FireEmergency.Other_fire_type = type;
                        cursor.moveToNext();
                    }
                }
            } finally {
                cursor.close();
            }
        }finally {
            db.close();
        }
        if(emergencies.length >= 20) {
            medicalList = new ArrayAdapter<>(this, android.R.layout.simple_list_item_activated_1, emergencies);

            medicListView = (ListView) findViewById(R.id.medical_list);
            medicListView.setAdapter(medicalList);

            medicListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent goReport = new Intent(getApplicationContext(), IncidentReport.class);
                    String emergency = emergencies[position];
                    String[] parts = emergency.split(":");
                    goReport.putExtra("Emergency_Type","Medical"); //putting extra information to the intent
                    goReport.putExtra("Emergency_Name", parts[1]);
                    goReport.putExtra("Additional","");
                    startActivity(goReport);
                }
            });
            handleIntent(getIntent());
        }else{

        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow
            for(String str : emergencies){
                if(str.equals(query)){
                    return;
                }else{
                    query = "No result!";
                }
            }
            Intent goReport = new Intent(getApplicationContext(), IncidentReport.class);
            goReport.putExtra("Emergency_Type","Medical");
            goReport.putExtra("Emergency_Name",query);
            goReport.putExtra("Additional","");
            startActivity(goReport);

        } else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            // Handle a suggestions click (because the suggestions all use ACTION_VIEW)
            Uri data = intent.getData();
            String id = data.getLastPathSegment();
            Intent goReport = new Intent(getApplicationContext(), IncidentReport.class);
            goReport.putExtra("Emergency_Type","Medical");
            goReport.putExtra("Emergency_Name",id);
            goReport.putExtra("Additional","");
            startActivity(goReport);
        }
    }


        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_medical_emergency, menu);

            // Get the SearchView and set the searchable configuration
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
             searchView = (SearchView) menu.findItem(R.id.search).getActionView();

            // Assumes current activity is the searchable activity
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            //menu  clicks
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
                return true;
            }

            if(id==R.id.logout_menu){
                SharedPreferences pref = getSharedPreferences("Prefs",MODE_PRIVATE);
                pref.edit().clear().apply();
                Intent outside = new Intent(getApplicationContext(),LoginScreen.class);
                startActivity(outside);
                finish();
                return true;
            }

            return super.onOptionsItemSelected(item);
        }

}