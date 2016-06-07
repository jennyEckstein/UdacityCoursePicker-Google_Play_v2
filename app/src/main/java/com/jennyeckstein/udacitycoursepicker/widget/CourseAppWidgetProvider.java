package com.jennyeckstein.udacitycoursepicker.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.jennyeckstein.udacitycoursepicker.sync.CourseSyncAdapter;

/**
 * Created by Jenny on 6/1/2016.
 */
public class CourseAppWidgetProvider extends AppWidgetProvider {

    private static final String LOG_TAG = CourseAppWidgetProvider.class.getSimpleName();

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        context.startService(new Intent(context, CourseWidgetIntentService.class));
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        context.startService(new Intent(context, CourseWidgetIntentService.class));
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if(CourseSyncAdapter.ACTION_DATA_UPDATED.equals(intent.getAction())){
            context.startService(new Intent(context, CourseWidgetIntentService.class));
        }
    }
}
