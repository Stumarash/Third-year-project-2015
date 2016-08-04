//package container for class
package com.innovative.model;

//imports for class
import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import java.util.HashMap;


/**
 * Class providing suggestions to the user
 * @author Dokes
 */
public class SuggestionsProvider extends ContentProvider {

    //instance variables
    public static final Uri SUGGESTIONS_URI = Uri.parse("content://com.innovative.model.suggestionsProvider/select");
    public static final String MULTIPLE_RECORDS_MIME_TYPE1 = "vnd.android.cursor.dir/vnd.innovative.model.suggestions";
    SQLiteQueryBuilder builder = new SQLiteQueryBuilder();

    @Override
    public boolean onCreate() {
        return false;
    }

    /**
     * cursor for the suggestion list
     * @param uri
     * @param projection
     * @param selection
     * @param selectionArgs
     * @param sortOrder
     * @return
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = UpdaterService.dbHelper.getReadableDatabase();//getting a databse as readable
        String query1;
        if(selection == null){
          query1  = uri.getLastPathSegment();
        }else{
            query1 = selection;
        }

        //formatting the information to display on the suggestions
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("_ID",DBHelper.C_Emergency_ID);
        hashMap.put(SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID,DBHelper.C_Emergency_Name + " AS " +
                SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID);
        hashMap.put(SearchManager.SUGGEST_COLUMN_TEXT_1 , DBHelper.C_Emergency_Name + " AS " +
                SearchManager.SUGGEST_COLUMN_TEXT_1);
        builder.setProjectionMap(hashMap);
        builder.setTables(DBHelper.TABLE1);


        Cursor c= builder.query(db,new String[]{"_ID",SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID,
                        SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID,SearchManager.SUGGEST_COLUMN_TEXT_1},
                DBHelper.C_Emergency_Name+" LIKE ?", new String[] {"%" + query1 + "%"}, null, null, null);

        return c;
    }

    @Override
    public String getType(Uri uri) {
        return MULTIPLE_RECORDS_MIME_TYPE1;//returning the records trpes
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
