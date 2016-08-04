package com.improveresponse.model;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.widget.RemoteViews;

import com.improveresponse.emergencyapp.R;

/**
 * class handling the widget for calling the emergency center
 */
public class CallingWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Cursor c = context.getContentResolver().query(PhoneNumberProvider.CONTENT_URI,null,null,null,null);
        String area = "";
        String num = "";

        try {
            if (c.moveToFirst()) {
                while (!c.isAfterLast()) {
                     area = c.getString(c.getColumnIndex(DBHelper.C_Emergency_Area));
                     num = c.getString(c.getColumnIndex(DBHelper.C_Emergency_Number));
                     c.moveToNext();
                }
                for(int appWidgetId : appWidgetIds){
                    RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.calling_widget);

                    views.setTextViewText(R.id.row_text,num);
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:"+num));
                    views.setOnClickPendingIntent(R.id.calling_button_widget, PendingIntent.getActivity(context,0,intent,0));
                    appWidgetManager.updateAppWidget(appWidgetId,views);
                }
            }
        } finally {
            c.close();
        }
    }
}
