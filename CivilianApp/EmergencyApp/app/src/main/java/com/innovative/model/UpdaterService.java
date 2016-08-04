//package container for class
package com.innovative.model;

//imports for class
import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.IBinder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

/**
 * class handling the background service for downloading information and updating the local databases
 * @author Dokes
 */
public class UpdaterService extends Service {

    //instance variables
    private static final int DELAY = 30;//days to update application information
    private boolean running = false;
    private Updater updater;
    public static DBHelper dbHelper;
    private SQLiteDatabase db;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * this method is called when the service is created
     */
    @Override
    public void onCreate() {
        super.onCreate();
        this.updater = new Updater();
        dbHelper = new DBHelper(this);
    }

    /**
     * this method is called when service starts
     * @param intent
     * @param flags
     * @param startId
     * @return START_STICKY, how the service starts
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
         super.onStartCommand(intent, flags, startId);
        this.running = true;
        if(!updater.isAlive()) {
            this.updater.start();
        }
        return START_STICKY;
    }

    /**
     * method called when the service is terminated
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        this.running = false;
        this.updater.interrupt();
        this.updater = null;
    }

    /**
     * innerclass handling the updating thread
     */
    private class Updater extends Thread{

        //instance variable
        private Updater() {
            super("Updater_service");
        }

        @Override
        public void run() {
           UpdaterService updaterService = UpdaterService.this;
            while (updaterService.running){
                try {
                    new GetAllNumbers().execute(new UserManager());//async task handling getting all the numbers

                    new GetAllEmergencies().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                            new UserManager());//async task handling getting the emergency list

                    TimeUnit.DAYS.sleep(DELAY);
                } catch (InterruptedException e) {
                    updaterService.running = false;
                }
            }
        }
    }

    /**
     * inner class getting all the numbers from the database
     */
    private class GetAllNumbers extends AsyncTask<UserManager,Long,JSONArray>{
        @Override
        protected JSONArray doInBackground(UserManager... params) {
            return params[0].getAll("Numbers");
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            if(!(jsonArray == null)) {
                db = dbHelper.getWritableDatabase();//opening the database for writing
                ContentValues values = new ContentValues();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject json;
                    try {
                        json = jsonArray.getJSONObject(i);

                        values.clear();
                        values.put(DBHelper.C_Emergency_Dial_ID, json.getString("Emergency_Dial_ID"));
                        values.put(DBHelper.C_Emergency_Number, json.getString("Emergency_Number"));
                        values.put(DBHelper.C_Emergency_Area, json.getString("Emergency_Area"));
                        db.insertWithOnConflict(DBHelper.TABLE, null, values, SQLiteDatabase.CONFLICT_IGNORE);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                db.close();//closing the database
            }
        }
    }

    /**
     * inner class handling getting all the emergencies from the database
     */
    private class GetAllEmergencies extends AsyncTask<UserManager,Long,JSONArray>{
        @Override
        protected JSONArray doInBackground(UserManager... params) {
            return params[0].getAll("EmergencyList");
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            if(!(jsonArray == null)) {
                db = dbHelper.getWritableDatabase();//opening the database for writing
                ContentValues values = new ContentValues();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject json;
                    try {
                        json = jsonArray.getJSONObject(i);

                        values.clear();
                        values.put(DBHelper.C_Emergency_ID, json.getString("EMERGENCY_LIST_ID"));
                        values.put(DBHelper.C_Emergency_Name, json.getString("EMERGENCY_NAME"));

                        db.insertWithOnConflict(DBHelper.TABLE1, null, values, SQLiteDatabase.CONFLICT_IGNORE);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                db.close();//closing the database
            }
        }
    }
}
