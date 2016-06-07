package com.jennyeckstein.udacitycoursepicker;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ActionProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.jennyeckstein.udacitycoursepicker.data.CourseContract;

public class DetailActivity extends AppCompatActivity
        implements DetailActivityFragment.OnDataPass {

    private static final String LOG_TAG = DetailActivity.class.getSimpleName();
    private ActionProvider mShareActionProvider;
    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayout;
    String currentKey;
    String currentVideoLike;
    DetailToMain mDetailToMain;

    public interface DetailToMain{
        public void justLikedCourseKey(String key);
    }

    public void sendKey(String key){
        this.mDetailToMain.justLikedCourseKey(key);
    }

    @Override
    public void setKey(String data) {
        this.currentKey = data;
    }

    @Override
    public void onDataPass(String data) {
        this.currentVideoLike = data;
       // Log.v(LOG_TAG, "LIKE PASSED: " + this.currentVideoLike);
    }

    public CollapsingToolbarLayout getCollapsingToolbarLayout() {
        return collapsingToolbarLayout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);

        Bundle arguments = new Bundle();


        arguments.putParcelable(DetailActivityFragment.DETAIL_URI, getIntent().getData());
        this.currentKey = getIntent().getData().getPathSegments().get(1);
        DetailActivityFragment fragment = new DetailActivityFragment();
        fragment.setArguments(arguments);
        getFragmentManager().beginTransaction().add(R.id.fragment_detail_container, fragment).commit();

        //setupWindowAnimations();

        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(this.toolbar);



        this.collapsingToolbarLayout =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        //collapsingToolbarLayout.setTitle("Booo");

        //getSupportActionBar().setTitle("YAY");

        //ImageView imageView1 = (ImageView) findViewById(R.id.detail_course_image_appBarLayout);
        //Picasso.with(this).load(R.drawable.course_test_image).into(imageView1);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ContentValues courseUpdateValues = new ContentValues();

                    if("0".equals(currentVideoLike)) {
                        currentVideoLike = "1";
                        fab.setImageDrawable(getResources().getDrawable(R.drawable.fab_on));
                    }else{
                        currentVideoLike = "0";
                        fab.setImageDrawable(getResources().getDrawable(R.drawable.fab_off));
                    }
                    courseUpdateValues.put(CourseContract.Course.LIKED_COURSE, currentVideoLike);
                 //   Log.v(LOG_TAG, "LIKED: " + currentVideoLike);
                    String selection =
                            CourseContract.Course.TABLE_NAME +
                                    "." + CourseContract.Course.KEY + " = ?";
                    String[] selectionArgs = {currentKey};
                    //Log.v(LOG_TAG, "SENDING LIKE");
                    //((DetailToMain) getBaseContext()).justLikedCourseKey(currentKey);

                    getContentResolver().update(
                            CourseContract.Course.buildCourseWithId(currentKey),
                            courseUpdateValues,
                            selection,
                            selectionArgs);

                }
            });
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
         if(id == R.id.action_share){
            //TODO: in the future it should share course title and other useful info
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Awesome Course #UdacityCoursePicker");
            shareIntent.setType("text/plain");
            startActivity(shareIntent);
        }

        return super.onOptionsItemSelected(item);
    }
}
