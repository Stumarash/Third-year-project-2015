package com.improveresponse.emergencyapp;

import android.app.ActionBar;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.improveresponse.model.DBHelper;
import com.improveresponse.model.UpdaterService;

/**
 * Fire class handling the fire emergency
 *  @author Dokes
 */
public class FireEmergency extends BaseActivity implements View.OnClickListener {

    //instance variable
    //fire types;
    protected static String Chemical_fire;
    protected static String Structural_Informal;
    protected static String Structural_Formal;
    protected static String Vehicle_fire;
    protected static String Aircraft_accident;
    protected static String Other_fire_type;

    //check boxes
    private CheckBox chemical;
    private CheckBox informal;
    private CheckBox formal;
    private CheckBox vehicle;
    private CheckBox aircraft;
    private CheckBox others;
    private CheckBox ambulance;

    //variable
    private  String fire_intensity = "";
    private String emergency_name;
    private String emergency_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fire_emergency);

        //action bar back functionality
        ActionBar actionBar = getActionBar();
        assert actionBar!=null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        //linking the ui components to the background code
        chemical = (CheckBox)findViewById(R.id.checkBox_chem);
        informal= (CheckBox)findViewById(R.id.checkBox_type_struct);
        formal =(CheckBox)findViewById(R.id.checkBox_type_struct_2);
        vehicle =(CheckBox)findViewById(R.id.checkBox_vehicle);
        aircraft =(CheckBox)findViewById(R.id.checkBox_air);
        others = (CheckBox)findViewById(R.id.checkBox_unknown);
        ambulance = (CheckBox)findViewById(R.id.checkBox_ambulance);

        SQLiteDatabase db = UpdaterService.dbHelper.getReadableDatabase();
        try {
            Cursor cursor = db.query(DBHelper.TABLE1, DBHelper.DB1_COLUMNS, null, null, null, null, DBHelper.C_Emergency_ID + " ASC");
            try {
                if (cursor.moveToFirst()) {

                    while (!cursor.isAfterLast()) {
                        String id = cursor.getString(cursor.getColumnIndex(DBHelper.C_Emergency_ID));
                        String type = cursor.getString(cursor.getColumnIndex(DBHelper.C_Emergency_Name));

                        switch(id){
                            case "21":  FireEmergency.Chemical_fire = type;
                                break;
                            case "22":  FireEmergency.Structural_Informal = type;
                                break;
                            case "23": FireEmergency.Structural_Formal = type;
                                break;
                            case "24": FireEmergency.Vehicle_fire = type;
                                break;
                            case "25": FireEmergency.Aircraft_accident = type;
                                break;
                            case "26": FireEmergency.Other_fire_type = type;
                                break;
                            default:
                                break;
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
        //setting the text for the check boxes
        chemical.setText(Chemical_fire);
        informal.setText(Structural_Informal);
        formal.setText(Structural_Formal);
        vehicle.setText(Vehicle_fire);
        aircraft.setText(Aircraft_accident);
        others.setText(Other_fire_type);

        //registering the listener
        chemical.setOnClickListener(this);
        informal.setOnClickListener(this);
        formal.setOnClickListener(this);
        vehicle.setOnClickListener(this);
        aircraft.setOnClickListener(this);
        others.setOnClickListener(this);

    }

    //button taking you to the emergency review screen
    public void goReview(View view){
        if(fire_intensity.isEmpty()){
            Toast.makeText(getApplicationContext(),"Please choose the fire intensity",Toast.LENGTH_SHORT).show();
        }else if(!chemical.isChecked() && !informal.isChecked() && !formal.isChecked() &&
                !vehicle.isChecked() && !aircraft.isChecked() && !others.isChecked()) {
            Toast.makeText(getApplicationContext(),"Please choose the fire type",Toast.LENGTH_SHORT).show();
        }
        else{
            Intent review = new Intent(this, IncidentReport.class);
            review.putExtra("Emergency_Type","Fire");
            review.putExtra("Emergency_Name",emergency_name);
            review.putExtra("Emergency_id",emergency_id);

            if(ambulance.isChecked()){
                review.putExtra("Additional","Ambulance necessary");
            }
            startActivity(review);
        }
    }

    /**
     * Function handling the clicking of minor fire
     * @param view the view from the user interface
     */
    public void cmdMinor_Fire_Click(View view) {
        fire_intensity = "Minor";
    }

    /**
     * Function handling the clicking of the major fire
     * @param view the view from the user interface
     */
    public void cmdMajor_Fire_Click(View view) {
        fire_intensity = "Major";
    }

    @Override
    public void onClick(View v) {
        if(chemical == v){
            formal.setChecked(false);
            informal.setChecked(false);
            vehicle.setChecked(false);
            aircraft.setChecked(false);
            others.setChecked(false);
            emergency_name = Chemical_fire;
            emergency_id = "21";
        }
        if(formal == v){
            chemical.setChecked(false);
            informal.setChecked(false);
            vehicle.setChecked(false);
            aircraft.setChecked(false);
            others.setChecked(false);
            emergency_name= Structural_Formal;
            emergency_id = "22";
        }
        if(informal == v){
            formal.setChecked(false);
            chemical.setChecked(false);
            vehicle.setChecked(false);
            aircraft.setChecked(false);
            others.setChecked(false);
            emergency_name = Structural_Informal;
            emergency_id = "23";
        }
        if(vehicle == v){
            formal.setChecked(false);
            informal.setChecked(false);
            chemical.setChecked(false);
            aircraft.setChecked(false);
            others.setChecked(false);
            emergency_name = Vehicle_fire;
            emergency_id = "24";
        }
        if(aircraft == v){
            formal.setChecked(false);
            informal.setChecked(false);
            vehicle.setChecked(false);
            chemical.setChecked(false);
            others.setChecked(false);
            emergency_name = Aircraft_accident;
            emergency_id = "25";
        }
        if(others == v){
            formal.setChecked(false);
            informal.setChecked(false);
            vehicle.setChecked(false);
            aircraft.setChecked(false);
            chemical.setChecked(false);
            emergency_name= Other_fire_type;
            emergency_id = "26";
        }
    }
}
