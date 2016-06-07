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

import com.google.android.gms.ads.InterstitialAd;
import com.jennyeckstein.udacitycoursepicker.data.CourseContract;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final String LOG_TAG = MainActivityFragment.class.getSimpleName();
    private static final int COURSE_LOADER = 0;
    private View view;
    private CourseAdapter mCourseAdapter;
    SendBackToMainActivity sendBackToMainActivity;

    private InterstitialAd mInterstitialAd;

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
      //  Log.v(LOG_TAG, "ON CREATE LOADER");
        String selection = CourseContract.Course.TABLE_NAME +
                "." + CourseContract.Course.LEVEL + " = ?";
        String [] selectionArgs = {"beginner"};
        return new android.support.v4.content.CursorLoader(
                getActivity(),
                CourseContract.Course.CONTENT_URI,
                null,
                selection,
                selectionArgs,
                null);
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {
        mCourseAdapter.swapCursor(null);
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {

        if(data.moveToFirst() == true){
            firstLoad(data.getString(data.getColumnIndex(CourseContract.Course.KEY)));
        }else{
            //  Log.v(LOG_TAG, "FIRST LOAD - THERE IS NO KEY TO PASS");
        }
        TextView textView = (TextView) getView().findViewById(R.id.no_data);
        if (!data.moveToFirst()){
            textView.setVisibility(View.VISIBLE);
            textView.setText("No Begginer Level Courses");
            //   Log.v(LOG_TAG, "SETTING LIKED NONE");
        }else{
            textView.setVisibility(View.GONE);
        }
        mCourseAdapter.swapCursor(data);

    }

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCallbacks = this;
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(COURSE_LOADER, null, mCallbacks);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    //    createAdd();
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

              /*  showAdd();
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                    Log.v(LOG_TAG, "LOADED");
                } else {
                    Log.v(LOG_TAG, "NOT LOADED");
                }*/
            }
        });


        return view;
    }

    public View getView(){
        return this.view;
    }

 /*   private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        mInterstitialAd.loadAd(adRequest);
    }
    private void createAdd(){
        mInterstitialAd = new InterstitialAd(getActivity());
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.full_page_add_id));
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
                //loadJoke();
            }
        });
    }
    //SMALL ADD
    private void showAdd(){
        AdView mAdView = (AdView) this.view.findViewById(R.id.adView);
        // Create an ad request. Check logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);
    }*/
}
