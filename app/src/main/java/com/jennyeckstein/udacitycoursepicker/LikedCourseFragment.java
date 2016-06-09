package com.jennyeckstein.udacitycoursepicker;


import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
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
public class LikedCourseFragment extends Fragment
                implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final String LOG_TAG = LikedCourseFragment.class.getSimpleName();
    private static final int LIKED_COURSE_LOADER = 202;
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
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String selection = CourseContract.Course.TABLE_NAME +
                "." + CourseContract.Course.LIKED_COURSE + " = ?";
        String [] selectionArgs = {"1"};

        return new CursorLoader(getActivity(),
                CourseContract.Course.CONTENT_URI,
                null, selection,selectionArgs, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        TextView textView = (TextView) getView().findViewById(R.id.no_data);
        if (!data.moveToFirst()){
            textView.setVisibility(View.VISIBLE);
            textView.setText(getResources().getString(R.string.no_liked_courses));
         //   Log.v(LOG_TAG, "SETTING LIKED NONE");
        }else{
            textView.setVisibility(View.GONE);
        }
        mCourseAdapter.swapCursor(data);
       // Log.v(LOG_TAG, "SETTING LIKED");
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCourseAdapter.swapCursor(null);
    }

    public LikedCourseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCallbacks = this;
        android.support.v4.app.LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(LIKED_COURSE_LOADER, null, mCallbacks);
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
