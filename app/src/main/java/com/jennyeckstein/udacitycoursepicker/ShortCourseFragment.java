package com.jennyeckstein.udacitycoursepicker;


import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.jennyeckstein.udacitycoursepicker.data.CourseContract;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShortCourseFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = NewCourseFragment.class.getSimpleName();
    private static final int NEW_COURSE_LOADER = 101;
    private View view;
    private CourseAdapter mCourseAdapter;
    SendBackToMainActivity sendBackToMainActivity;

    private LoaderManager.LoaderCallbacks<Cursor> mCallbacks;

    public interface SendBackToMainActivity{
        public void sendCourseKeyToMainActivity(String courseKey);
        public void onFirstLoad(String currentKey);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        sendBackToMainActivity = (SendBackToMainActivity) context;
    }

    public void sendToMain(String courseKey){
        sendBackToMainActivity.sendCourseKeyToMainActivity(courseKey);
    }
    public void firstLoad(String courseKey){
        sendBackToMainActivity.onFirstLoad(courseKey);
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
     //   Log.v(LOG_TAG, "ON CREATE LOADER");
        String selection = CourseContract.Course.TABLE_NAME +
                "." + CourseContract.Course.EXPECTED_DURATION_UNIT + " <= ?";
                //short courses are only those that take days
        String [] selectionArgs = {"days"};
        return new android.support.v4.content.CursorLoader(
                getActivity(),
                CourseContract.Course.CONTENT_URI,
                null, selection, selectionArgs, null);
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        TextView textView = (TextView) getView().findViewById(R.id.no_data);
        if (!data.moveToFirst()){
            textView.setVisibility(View.VISIBLE);
            textView.setText(getResources().getString(R.string.no_short_courses));
            //   Log.v(LOG_TAG, "SETTING LIKED NONE");
        }else{
            textView.setVisibility(View.GONE);
        }
        mCourseAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {
        mCourseAdapter.swapCursor(null);
    }

    public ShortCourseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCallbacks = this;
        android.support.v4.app.LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(NEW_COURSE_LOADER, null, mCallbacks);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mCourseAdapter = new CourseAdapter(getActivity(), null, 0);
        this.view = inflater.inflate(R.layout.fragment_main, container, false);
        ListView listView = (ListView) view.findViewById(R.id.courseView);
        listView.setAdapter(mCourseAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                if (cursor != null) {
                    SharedPreferences sharedPreferences =
                            PreferenceManager.getDefaultSharedPreferences(getActivity());
                    int courseKeyColumn = cursor.getColumnIndex(CourseContract.Course.KEY);
                    String courseKey = cursor.getString(courseKeyColumn);
                    sendToMain(courseKey);
                }

            }
        });
        return view;
    }


}
