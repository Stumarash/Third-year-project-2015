package com.innovative.emergencyapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.innovative.model.IncidentAdapter;


public class ReportView extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_view);

       String[] levels ={"FIre","FIre","FIre","FIre","FIre","FIre","FIre","FIre","FIre","FIre",
                "Medical","Medical","Medical","Medical","Medical","Medical","Medical","Medical","Medical"};
        //populating the list
       ListAdapter listview = new IncidentAdapter(this,
                levels);
            ListView theView = (ListView)findViewById(R.id.list_reports);

        theView.setAdapter(listview);

        theView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent goReport = new Intent(getApplicationContext(),ReportReview.class);
                startActivity(goReport);
            }
        });

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_report_view, menu);
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

        if(id == R.id.profile_pic){

            return true;
        }

        if(id==R.id.profile_view){
            Intent to_next = new Intent(getApplicationContext(), Profile.class);
            startActivity(to_next);
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
            Intent outside = new Intent(getApplicationContext(),LoginScreen.class);
            startActivity(outside);
        }
        return super.onOptionsItemSelected(item);
    }
}
