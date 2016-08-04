package com.innovative.model;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * class handling the providing of phone numbers to the calling widget
 */
public class PhoneNumberProvider extends ContentProvider{

    public static final Uri CONTENT_URI = Uri.parse("content://com.innovative.model.PhoneProvider");
    public static final String MULTIPLE_RECORDS_MIME_TYPE = "vnd.android.cursor.dir/vnd.innovative.model.numbers";
    @Override
    public boolean onCreate() {
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = UpdaterService.dbHelper.getReadableDatabase();
        return db.query(DBHelper.TABLE, DBHelper.DB_COLUMNS, null, null, null, null, DBHelper.C_Emergency_Dial_ID + " ASC");
    }

    @Override
    public String getType(Uri uri) {
        return MULTIPLE_RECORDS_MIME_TYPE;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
