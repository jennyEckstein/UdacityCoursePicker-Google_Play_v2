package com.jennyeckstein.udacitycoursepicker;

import android.content.ContentValues;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jennyeckstein.udacitycoursepicker.data.CourseContract;
import com.jennyeckstein.udacitycoursepicker.sync.CourseSyncAdapter;

public class MainActivity extends AppCompatActivity
        implements MainActivityFragment.SendBackToMainActivity,
                    NewCourseFragment.SendBackToMainActivity,
                    ShortCourseFragment.SendBackToMainActivity,
                    LikedCourseFragment.SendBackToMainActivity,
                    DetailActivity.DetailToMain,
                    DetailActivityFragment.OnDataPass{

    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    private ViewGroup viewGroup;
    private View listView;
    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    boolean mTwoPane;
    public String currentKey;
    String currentVideoLike;
    private ProgressBar spinner;
    DetailActivityFragment detailActivityFragment;
    CollapsingToolbarLayout collapsingToolbarLayout;
    boolean firstLoad = true;

    private static final String DETAIL_FRAGMENT_TAG = "DFTAG";

    @Override
    public void justLikedCourseKey(String key) {
        this.currentKey = key;
      //  Log.v(LOG_TAG, "JUST LIKED KEY " + key);
    }

    public CollapsingToolbarLayout getCollapsingToolbarLayout() {
        return collapsingToolbarLayout;
    }

    @Override
    public void onDataPass(String data) {
        this.currentVideoLike = data;
       // Log.v(LOG_TAG, "LIKE PASSED: " + this.currentVideoLike);
      //  Log.v(LOG_TAG, "STOP SPINNER - data passed");
        spinner.setVisibility(View.GONE);
    }

    @Override
    public void setKey(String data) {
        //Log.v(LOG_TAG, "SET COREECT KEY " + data);
        this.currentKey = data;
    }

    @Override
    public void onFirstLoad(final String data) {
        //  Log.v(LOG_TAG, "STOP SPINNER - first load");
        spinner.setVisibility(View.GONE);
        if(firstLoad) {
            this.currentKey = data;
            this.firstLoad = false;
        }
       // Log.v(LOG_TAG, "FIRST LOAD Key Received by activity " + data);

        if(mTwoPane){
            // Log.v(LOG_TAG, "CREATING DETAIL FRAGMENT ");
            this.detailActivityFragment =
                    new DetailActivityFragment();
            Bundle args = new Bundle();
            if(currentKey != null && !"".equals(currentKey)){
                args.putParcelable(DetailActivityFragment.DETAIL_URI,
                        CourseContract.Course.buildCourseWithId(this.currentKey));
                detailActivityFragment.setArguments(args);

                getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_detail_container,
                                detailActivityFragment,
                                DETAIL_FRAGMENT_TAG)
                        .commit();
            }

        }
    }


    @Override
    public void sendCourseKeyToMainActivity(String courseKey) {
        this.currentKey = courseKey;
       // Log.v(LOG_TAG, "Key Received by activity " + courseKey);

        if(mTwoPane){
         //   Log.v(LOG_TAG, "CREATING DETAIL FRAGMENT ");
            this.detailActivityFragment =
                    new DetailActivityFragment();
            Bundle args = new Bundle();
            if(currentKey == null || "".equals(currentKey)){
                //Log.v(LOG_TAG, "THERE IS NO KEY TO PASS");
            }else {

                args.putParcelable(DetailActivityFragment.DETAIL_URI,
                        CourseContract.Course.buildCourseWithId(currentKey));
                detailActivityFragment.setArguments(args);

                getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_detail_container,
                                detailActivityFragment,
                                DETAIL_FRAGMENT_TAG)
                        .commit();
            }
        }else{
           // Log.v(LOG_TAG, "ONE PANE sending intent");
            Intent intent = new Intent(this, DetailActivity.class).setData(
                    CourseContract.Course.buildCourseWithId(currentKey));
            startActivity(intent);
        }
       // Log.v(LOG_TAG, "WE SHOULD HAVE OUR FRAGMENT ");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Log.v(LOG_TAG, "SAVE INSTANCE");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
       // Log.v(LOG_TAG, "RESTORE INSTANCE");
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.detailActivityFragment = (DetailActivityFragment)
                getFragmentManager().findFragmentByTag(DETAIL_FRAGMENT_TAG);
        //Log.v(LOG_TAG, "ON RESUME");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  Log.v(LOG_TAG, "ON create");
        setContentView(R.layout.activity_main);

        ((MyApplication) getApplication()).startTracking();

        this.spinner = (ProgressBar) findViewById(R.id.progressBar1);

        if(findViewById(R.id.fragment_detail_container) != null){

            this.collapsingToolbarLayout =
                    (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
            mTwoPane = true;


            //
            final FloatingActionButton fab =
                    (FloatingActionButton) findViewById(R.id.fab);
            if (fab != null) {
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                   /* Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();*/
                        ContentValues courseUpdateValues = new ContentValues();

                        if ("0".equals(currentVideoLike)) {
                            currentVideoLike = "1";
                            //fab.setImageDrawable(getResources().getDrawable(R.drawable.fab_on));
                            fab.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.fab_on));
                        } else {
                            currentVideoLike = "0";
                            //fab.setImageDrawable(getResources().getDrawable(R.drawable.fab_off));
                            fab.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.fab_off));
                        }
                        courseUpdateValues.put(CourseContract.Course.LIKED_COURSE, currentVideoLike);
                        //Log.v(LOG_TAG, "LIKED: " + currentVideoLike);
                        String selection =
                                CourseContract.Course.TABLE_NAME +
                                        "." + CourseContract.Course.KEY + " = ?";
                        String[] selectionArgs = {currentKey};

                        getContentResolver().update(
                                CourseContract.Course.buildCourseWithId(currentKey),
                                courseUpdateValues,
                                selection,
                                selectionArgs);
                      //  Log.v(LOG_TAG, "LIKE WAS CLICKED " + currentKey);
                    }
                });
            }

            //

//Log.v(LOG_TAG, "TWO PANE");
            if(savedInstanceState == null){



                this.detailActivityFragment =
                        new DetailActivityFragment();

                Bundle args = new Bundle();
                if(currentKey != null && !("".equals(currentKey))){
                    args.putParcelable(DetailActivityFragment.DETAIL_URI,
                            CourseContract.Course.buildCourseWithId(currentKey));
                    detailActivityFragment.setArguments(args);

                    getFragmentManager().beginTransaction()
                            .replace(R.id.fragment_detail_container,
                                    detailActivityFragment,
                                    DETAIL_FRAGMENT_TAG)
                            .commit();
                }


            }else{
                mTwoPane = false;
            }

        }else{
           // Log.v(LOG_TAG, "ONE PANE");
            mTwoPane = false;
        }


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tabLayout = (TabLayout)findViewById(R.id.tabLayout);
        viewPager = (ViewPager)findViewById(R.id.viewPager);

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragments(
                new MainActivityFragment(), getResources().getString(R.string.beginner_tab));
        viewPagerAdapter.addFragments(
                new NewCourseFragment(), getResources().getString(R.string.new_tab));
        viewPagerAdapter.addFragments(
                new ShortCourseFragment(), getResources().getString(R.string.short_tab));
        viewPagerAdapter.addFragments(
                new LikedCourseFragment(), getResources().getString(R.string.liked_tab));

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setExitTransition(new Explode());
        }

       // TextView no_internet_view = (TextView) findViewById(R.id.no_internet);
       // no_internet_view.setVisibility(View.GONE);

        TextView no_internet_text_view = (TextView)findViewById(R.id.no_internet);

        if(isNetworkAvailable()) {
           // Log.v(LOG_TAG, "YES INTERNET");
            CourseSyncAdapter.syncImmediately(this);
            spinner.setVisibility(View.VISIBLE);
            no_internet_text_view.setVisibility(View.GONE);

        }else{
         //   Log.v(LOG_TAG, "NO INTERNET");
            spinner.setVisibility(View.GONE);
            no_internet_text_view.setVisibility(View.VISIBLE);
        }

   /*     Intent alarmIntent = new Intent(this, CourseService.AlarmReceiver.class);
        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+5000, pendingIntent);*/


        //Now we call it from AlarmReceiver
       // Intent intent = new Intent(this, CourseService.class);
       // this.startService(intent);

    }



    private boolean isNetworkAvailable(){
        try{
            ConnectivityManager connectivityManager =
                    (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo netInfo =
                    connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if(netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED){
                return true;
            }
            netInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if(netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED){
                return true;
            }
            return false;
        }catch (Exception e){
            return false;
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

            return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


            //noinspection SimplifiableIfStatement
            if (id == R.id.action_share) {

            }

        return super.onOptionsItemSelected(item);
    }
}
