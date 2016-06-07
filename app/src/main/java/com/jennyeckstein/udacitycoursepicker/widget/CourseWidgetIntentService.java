package com.jennyeckstein.udacitycoursepicker.widget;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;

import com.jennyeckstein.udacitycoursepicker.MainActivity;
import com.jennyeckstein.udacitycoursepicker.R;
import com.jennyeckstein.udacitycoursepicker.data.CourseContract;

/**
 * Created by Jenny on 6/1/2016.
 */
public class CourseWidgetIntentService extends IntentService {

    private static final String LOG_TAG = CourseAppWidgetProvider.class.getSimpleName();

    public CourseWidgetIntentService() {
        super("CourseWidgetIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
                new ComponentName(
                        this,
                        com.jennyeckstein.udacitycoursepicker.widget.CourseAppWidgetProvider.class
                ));
        Cursor cursor = getContentResolver().query(
                CourseContract.Course.CONTENT_URI,
                new String[]{CourseContract.Course.KEY},
                CourseContract.Course.NEW_RELEASE + " = ?",
                new String[]{"true"},
                null,
                null
        );
        if(cursor == null){
            cursor.close();
            return;
        }
        if(!cursor.moveToFirst()){
            cursor.close();
            return;
        }

        int numberOfNewCourses = cursor.getCount();
        for(int appWidgetId: appWidgetIds){
            int layoutId = R.layout.course_appwidget;
            RemoteViews views = new RemoteViews(getPackageName(), layoutId);

            views.setTextViewText(R.id.new_courses_count, String.valueOf(numberOfNewCourses));

            Intent launchIntent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, launchIntent, 0);
            views.setOnClickPendingIntent(R.id.widget, pendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, views);

        }
    }


}
