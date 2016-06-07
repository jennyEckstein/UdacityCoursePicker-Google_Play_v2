package com.jennyeckstein.udacitycoursepicker;


import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jennyeckstein.udacitycoursepicker.data.CourseContract;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final String LOG_TAG = DetailActivityFragment.class.getSimpleName();
    static final String DETAIL_URI = "URI";
    private static final int DETAIL_LOADER = 1;
    private Uri passedUri;
    Context context;
    String title, image, summary;
    OnDataPass dataPass;
    String subtitle,durationUnit, expected_duration,level,
            required_knowledge,syllabus, faq, key;


    public interface OnDataPass{
        public void onDataPass(String data);
        public void setKey(String data);
    }

    public void passData(String data){
        dataPass.onDataPass(data);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (this.passedUri != null) {
           // Log.v(LOG_TAG, "PASSED uri: " + this.passedUri.toString());
            String key = this.passedUri.getPathSegments().get(1);
            return new CursorLoader(
                    getActivity(), this.passedUri, null, null, new String[]{key}, null);

        }else{
          //  Log.v(LOG_TAG, "creating loader, passedUri is empty");
            return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if(!data.moveToFirst()){
         //   Log.v(LOG_TAG, "NO DATA RETURNED");
            return;
        }
        int keyColumn = data.getColumnIndex(CourseContract.Course.KEY);
        int titleColumn = data.getColumnIndex(CourseContract.Course.TITLE);
        int imageColumn = data.getColumnIndex(CourseContract.Course.IMAGE);
        int summaryColumn = data.getColumnIndex(CourseContract.Course.SUMMARY);
        int subtitleColumn = data.getColumnIndex(CourseContract.Course.SUBTITLE);
        int durationUnitColumn = data.getColumnIndex(CourseContract.Course.EXPECTED_DURATION_UNIT);
        int expected_duration = data.getColumnIndex(CourseContract.Course.EXPECTED_DURATION);
        int level = data.getColumnIndex(CourseContract.Course.LEVEL);
        int required_knowledge = data.getColumnIndex(CourseContract.Course.REQUIRED_KNOWLEDGE);
        int syllabus = data.getColumnIndex(CourseContract.Course.SYLLABUS);
        int faq = data.getColumnIndex(CourseContract.Course.FAQ);
        final int homepage = data.getColumnIndex(CourseContract.Course.HOMEPAGE);



        this.key = data.getString(keyColumn);
        this.title = data.getString(titleColumn);
        this.image = data.getString(imageColumn);
        this.summary = data.getString(summaryColumn);
      //  this.subtitle = data.getString(subtitleColumn);
        this.durationUnit = data.getString(durationUnitColumn);
        this.expected_duration = data.getString(expected_duration);
        this.level = data.getString(level);
        this.required_knowledge = data.getString(required_knowledge);
        this.syllabus = data.getString(syllabus);
        this.faq = data.getString(faq);
        final String homepageUrl = data.getString(homepage);


       // Log.v(LOG_TAG, title + " | " + image + " | " + summary);

         String dur = this.expected_duration + " " + this.durationUnit;
        if("0".equals(dur.trim())){
            dur = "SHORT";
        }

        ViewHolder mViewHolder = (ViewHolder) getView().getTag();
        mViewHolder.subtitleTextView.setText(this.title);
        mViewHolder.durationTextView.setText(dur);
        mViewHolder.levelTextView.setText(this.level.toUpperCase());

        if("".equals(this.required_knowledge.trim())){
            mViewHolder.required_knowledge_header.setVisibility(View.GONE);
        }else {
            mViewHolder.requiredKnowledge.setText(this.required_knowledge);
            mViewHolder.required_knowledge_header.setVisibility(View.VISIBLE);
        }
        if("".equals(this.summary.trim())){
            mViewHolder.summary_header.setVisibility(View.GONE);
        }else {
            mViewHolder.summaryTextView.setText(this.summary);
            mViewHolder.summary_header.setVisibility(View.VISIBLE);
        }
        if("".equals(this.syllabus.trim())) {
            mViewHolder.syllabus_header.setVisibility(View.GONE);
        }else{
            mViewHolder.syllabusTextView.setText(this.syllabus);
            mViewHolder.syllabus_header.setVisibility(View.VISIBLE);
        }
        if("".equals(this.faq.trim())){
            mViewHolder.faq_header.setVisibility(View.GONE);
        }else {
            mViewHolder.faqTextView.setText(this.faq);
            mViewHolder.faq_header.setVisibility(View.VISIBLE);
        }

        mViewHolder.beginCourseButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(homepageUrl));
                startActivity(intent);
            }
        });

        if(mViewHolder.shareButton != null){
            mViewHolder.shareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_TEXT, "Awesome Course #UdacityCoursePicker");
                    shareIntent.setType("text/plain");
                    startActivity(shareIntent);
                }
            });
        }

        CollapsingToolbarLayout collapsingToolbarLayout;
        try{
            collapsingToolbarLayout = ((DetailActivity) getActivity()).getCollapsingToolbarLayout();
        }catch (ClassCastException e){
            collapsingToolbarLayout = ((MainActivity) getActivity()).getCollapsingToolbarLayout();
        }


               if(collapsingToolbarLayout != null){
                 //  Log.v(LOG_TAG, "SUCCESS");
                   collapsingToolbarLayout.setTitle(this.key.toUpperCase());
                   collapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);
                   collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
               }

         //ImageView imageView = (ImageView) getActivity().findViewById(R.id.detail_course_image_appBarLayout);

        if(mViewHolder.courseImageView != null) {
            if(!"".equals(image.trim())){
                Picasso.with(context).load(image).networkPolicy(NetworkPolicy.OFFLINE).into(mViewHolder.courseImageView);
            }else{
                //TODO: replace with some generic "no image"
                Picasso.with(context).load(R.drawable.no_image).into(mViewHolder.courseImageView);
            }

        }

        String like = data.getString(data.getColumnIndex(CourseContract.Course.LIKED_COURSE));
        ((OnDataPass) getActivity()).onDataPass(like);
        ((OnDataPass) getActivity()).setKey(this.key);
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        if (fab != null) {
            if ("1".equals(like)) {
                fab.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.fab_on));
            } else {
                fab.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.fab_off));
            }
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public DetailActivityFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null){
        //    Log.v(LOG_TAG, "Setting passed URI");
            this.passedUri = arguments.getParcelable(DetailActivityFragment.DETAIL_URI);
          //  Log.v(LOG_TAG, "PASSES URI: " + this.passedUri);

            //TODO: fix init loader
            getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        }else{
            //TODO: take care of else case
         //   Log.v(LOG_TAG, "NO ARGUMENTS - NO LOADER");
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        context = (Context)getContext();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // Log.v(LOG_TAG, "CREATED DETAIL FRAGMENT");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        ViewHolder mViewHolder = new ViewHolder(view, 0, getActivity());
        view.setTag(mViewHolder);
        return view;
    }


    public static class ViewHolder{
        public ImageView courseImageView;
        public TextView subtitleTextView;
        public TextView durationTextView;
        public TextView levelTextView;
        public Button beginCourseButton;
        public Button shareButton;
        public TextView requiredKnowledge;
        public TextView summaryTextView;
        public TextView syllabusTextView;
        public TextView faqTextView;

        public TextView faq_header,
                syllabus_header,
                summary_header,
                required_knowledge_header;


        public ViewHolder(View view, int layoutId, Activity activity){
            this.courseImageView = (ImageView) activity.findViewById(R.id.detail_course_image_appBarLayout);
            this.subtitleTextView = (TextView) view.findViewById(R.id.subtitle);
            this.durationTextView = (TextView) view.findViewById(R.id.duration);
            this.levelTextView = (TextView) view.findViewById(R.id.level);
            this.beginCourseButton = (Button) view.findViewById(R.id.begin_course);
            this.shareButton = (Button) view.findViewById(R.id.share_button);
            this.requiredKnowledge = (TextView) view.findViewById(R.id.required_knowledge);
            this.summaryTextView = (TextView) view.findViewById(R.id.summary);
            this.syllabusTextView = (TextView) view.findViewById(R.id.syllabus);
            this.faqTextView = (TextView) view.findViewById(R.id.faq);

            this.faq_header = (TextView) view.findViewById(R.id.faq_header);
            this.syllabus_header = (TextView) view.findViewById(R.id.syllabus_header);
            this.summary_header = (TextView) view.findViewById(R.id.summary_header);
            this.required_knowledge_header = (TextView) view.findViewById(R.id.required_knowledge_header);
        }



    }


}
