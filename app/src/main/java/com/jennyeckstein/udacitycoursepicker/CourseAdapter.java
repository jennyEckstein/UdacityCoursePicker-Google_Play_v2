package com.jennyeckstein.udacitycoursepicker;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jennyeckstein.udacitycoursepicker.data.CourseContract;
import com.squareup.picasso.Picasso;

/**
 * Created by Jenny on 5/2/2016.
 */
public class CourseAdapter extends CursorAdapter {

    public static final String LOG_TAG = CourseAdapter.class.getSimpleName();

    private Context context;
    private LayoutInflater inflator;


    public CourseAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        this.context = context;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }



    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        //Log.v(LOG_TAG, "NEW VIEW");
        View view = LayoutInflater.from(context).inflate(R.layout.course_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view, 0);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
       // Log.v(LOG_TAG, "BIND VIEW");
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        String imageLink = cursor.getString(cursor.getColumnIndex(CourseContract.Course.IMAGE));
        String title = cursor.getString(cursor.getColumnIndex(CourseContract.Course.TITLE));
        String duration_unit = cursor.getString(cursor.getColumnIndex(CourseContract.Course.EXPECTED_DURATION_UNIT));
        String expected_duration = cursor.getString(cursor.getColumnIndex(CourseContract.Course.EXPECTED_DURATION));
        String level = cursor.getString(cursor.getColumnIndex(CourseContract.Course.LEVEL));
        String new_release = cursor.getString(cursor.getColumnIndex(CourseContract.Course.NEW_RELEASE));

        //Log.v(LOG_TAG, "IMAGE LINK: "+ title + " |" + imageLink + "|");
        if (!("".equals(imageLink)) && imageLink != null) {
            Picasso.with(context)
                    .load(imageLink)
                    .error(R.drawable.no_image)
                    .resize(300, 200)
                    .into(viewHolder.course_image_view);
        }else{
            Picasso.with(context)
                    .load(R.drawable.no_image)
                    .resize(300, 200)
                    .into(viewHolder.course_image_view);
        }
        viewHolder.course_title_view.setText(title);
        if (!"0".equals(expected_duration)) {
            viewHolder.course_duration_view.setText(expected_duration + " " + duration_unit);
        }
        viewHolder.course_level_view.setText(level);
        //Log.v(LOG_TAG, "New Release: " + new_release + " " + title);
        if("true".equals(new_release)) {
            viewHolder.course_new_release_view.setText("new");
            viewHolder.course_new_release_view.setBackgroundColor(context.getResources().getColor(R.color.soft_yellow));
        }
    }

    public static class ViewHolder{
        public ImageView course_image_view;
        public TextView course_title_view;
        public TextView course_duration_view;
        public TextView course_level_view;
        public TextView course_new_release_view;

        public ViewHolder (View view, int layoutID){
            this.course_image_view = (ImageView)view.findViewById(R.id.course_image);
            this.course_title_view = (TextView)view.findViewById(R.id.course_title);
            this.course_duration_view = (TextView)view.findViewById(R.id.course_duration);
            this.course_level_view = (TextView)view.findViewById(R.id.course_level);
            this.course_new_release_view = (TextView)view.findViewById(R.id.course_new_release);
        }
    }

/*@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (convertView == null){
            view = inflator.inflate(R.layout.course_item, null);

            ImageView courseImage = (ImageView) view.findViewById(R.id.course_image);
            TextView titleView = (TextView) view.findViewById(R.id.course_title);
            TextView durationView = (TextView) view.findViewById(R.id.course_duration);
            TextView newCourseView = (TextView) view.findViewById(R.id.course_new_release);
            TextView courseLevelView = (TextView) view.findViewById(R.id.course_level);

            Picasso.with(context).load(R.drawable.course_test_image).into(courseImage);
            titleView.setText("Analyze Data with Hadoop and MapReduce");
            durationView.setText("3 weeks");
            newCourseView.setText("new");
            courseLevelView.setText("BEGGINER");
        }
        return view;
    }*/
}
