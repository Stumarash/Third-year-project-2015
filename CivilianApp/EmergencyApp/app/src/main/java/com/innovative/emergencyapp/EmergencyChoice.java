package com.innovative.emergencyapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.innovative.model.DBHelper;
import com.innovative.model.UpdaterService;

/**
 * Class handling the selection an emergency
 *   @author Dokes
 */
public class EmergencyChoice extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_choice);


        Bundle extras = getIntent().getExtras();
        if(extras != null){
            String value = extras.getString("User");
        }
    }

    //trigger for fire emergency
    public void goFire(View view){
        Intent goF = new Intent(getApplicationContext(), FireEmergency.class);
        startActivity(goF);
    }

    //trigger for medical emergency
    public void goMedical(View view){
        stopService(new Intent(this, UpdaterService.class));
        Intent goM = new Intent(getApplicationContext(), MedicalEmergency.class);
        startActivity(goM);
    }

    //trigger for calling the emergency center
    public void goPhone(View view) {
        stopService(new Intent(this, UpdaterService.class));
        getNumbers();
    }

    // method to get the numbers of the emergency and calling
    public void getNumbers() { //
        SQLiteDatabase db = UpdaterService.dbHelper.getReadableDatabase();
        try {
            Cursor cursor = db.query(DBHelper.TABLE, DBHelper.DB_COLUMNS, null, null, null, null, DBHelper.C_Emergency_Dial_ID + " ASC");
            try {
                if (cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {
                        String area = cursor.getString(cursor.getColumnIndex(DBHelper.C_Emergency_Area));
                        String num = cursor.getString(cursor.getColumnIndex(DBHelper.C_Emergency_Number));
                        if(area.equals("Toll Free")){
                            Intent intent = new Intent(Intent.ACTION_CALL);
                            intent.setData(Uri.parse("tel:"+num));
                            startActivity(intent);
                        }
                        cursor.moveToNext();
                    }
                }
            } finally {
                cursor.close();
            }
        }finally {
            db.close();
        }
    }
}
