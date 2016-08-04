package com.innovative.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * class handling the database updates and creations
 * @author Dokes
 */
public class DBHelper extends SQLiteOpenHelper{
    private static final String DB_NAME = "Targarian_local.db";
    private static final int DB_VERSION = 1; //
    public static final String TABLE = "Emergency_Numbers"; //
    public static final String C_Emergency_Dial_ID = BaseColumns._ID;
    public static final String C_Emergency_Number = "Emergency_Number";
    public static final String C_Emergency_Area = "Emergency_Area";
    public static final String[] DB_COLUMNS = {"_ID","Emergency_Number","Emergency_Area"};
    public static final String TABLE1 = "Emergency_Types"; //
    public static final String C_Emergency_ID = BaseColumns._ID;
    public static final String C_Emergency_Name = "Emergency_Name";
    public static final String[] DB1_COLUMNS = {"_ID","Emergency_Name"};
    private Context context;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table " + TABLE +
               " ("+
                 C_Emergency_Dial_ID + " int primary key, " +
                 C_Emergency_Number + " int, " +
                 C_Emergency_Area + " text);";

        db.execSQL(sql);
        String sql2 = "create table " + TABLE1 +
                " ("+
                C_Emergency_ID + " int primary key, " +
                C_Emergency_Name + " text);";

        db.execSQL(sql2); //
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE); // drops the old database
        db.execSQL("drop table if exists " + TABLE1); // drops the old database
        onCreate(db); // run onCreate to get new database
    }
}
