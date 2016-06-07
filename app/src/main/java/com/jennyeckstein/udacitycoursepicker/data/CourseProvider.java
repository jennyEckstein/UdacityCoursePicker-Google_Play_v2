package com.jennyeckstein.udacitycoursepicker.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by Jenny on 5/10/2016.
 */
public class CourseProvider extends ContentProvider {

    private static final String LOG_TAG = CourseProvider.class.getSimpleName();

    private CourseDBHelper courseDBHelper;
    private static final UriMatcher uriMathcher = buildUriMatcher();

    static final int COURSE = 100;
    static final int INSTRUCTOR = 200;
    static final int RELATED_COURSES = 300;
    static final int COURSE_INSTRUCTOR = 400;

    static final int COURSE_WITH_ID = 101;

    static final int INSTRUCTOR_WITH_ID = 201;

    private static final SQLiteQueryBuilder courseInstructorsQueryBuilder;

    static {
        courseInstructorsQueryBuilder = new SQLiteQueryBuilder();
        courseInstructorsQueryBuilder.setTables(
                CourseContract.Instructor.TABLE_NAME + " INNER JOIN " +
                        CourseContract.Course_Instructor.TABLE_NAME +
                        " ON " + CourseContract.Instructor.TABLE_NAME +
                        "." + CourseContract.Instructor.ID +
                        " = " + CourseContract.Course_Instructor.TABLE_NAME +
                        "." + CourseContract.Course_Instructor.INSTRUCTOR_ID
        );
    }

    private static final SQLiteQueryBuilder courseRelatedQueryBuilder;

    static {
        courseRelatedQueryBuilder = new SQLiteQueryBuilder();
        courseRelatedQueryBuilder.setTables(
                CourseContract.Course.TABLE_NAME + " INNER JOIN " +
                CourseContract.Related_Courses.TABLE_NAME +
                " ON " + CourseContract.Course.TABLE_NAME +
                "." + CourseContract.Course.KEY +
                " = " + CourseContract.Related_Courses.TABLE_NAME +
                "." + CourseContract.Related_Courses.RELATED_COURSE_ID
        );
    };

    private static final String courseInstructorSelection =
            CourseContract.Course_Instructor.TABLE_NAME +
                    "." + CourseContract.Course_Instructor.COURSE_ID + " = ?";

    private static final String courseRelatedSelection =
            CourseContract.Related_Courses.TABLE_NAME +
                    "." + CourseContract.Related_Courses.COURSE_ID + " = ?";

    private static final String courseKeyEquals =
            CourseContract.Course.TABLE_NAME +
                    "." + CourseContract.Course.KEY + " = ?";

    private static final String instructorIdEquals =
            CourseContract.Instructor.TABLE_NAME +
                    "." + CourseContract.Instructor.ID;

    static UriMatcher buildUriMatcher(){
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = CourseContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, CourseContract.PATH_COURSE, COURSE);
        matcher.addURI(authority, CourseContract.PATH_INSTRUCTOR, INSTRUCTOR);
        matcher.addURI(authority, CourseContract.PATH_RELATED_COURSES, RELATED_COURSES);
        matcher.addURI(authority, CourseContract.PATH_COURSE_INSTRUCTOR, COURSE_INSTRUCTOR);

        matcher.addURI(authority, CourseContract.PATH_COURSE + "/*", COURSE_WITH_ID);
        matcher.addURI(authority, CourseContract.PATH_INSTRUCTOR + "/#", INSTRUCTOR_WITH_ID);

        return matcher;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {

        final int match = uriMathcher.match(uri);

        switch(match){
            case COURSE:
                return  CourseContract.Course.CONTENT_DIR_TYPE;
            case COURSE_WITH_ID:
                return CourseContract.Course.CONTENT_ITEM_TYPE;

            case INSTRUCTOR:
                return CourseContract.Instructor.CONTENT_DIR_TYPE;
            case INSTRUCTOR_WITH_ID:
                return CourseContract.Instructor.CONTENT_ITEM_TYPE;

            case RELATED_COURSES:
                return CourseContract.Related_Courses.CONTENT_DIR_TYPE;

            case COURSE_INSTRUCTOR:
                return CourseContract.Course_Instructor.CONTENT_DIR_TYPE;

            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }
    }

    @Override
    public boolean onCreate() {
        courseDBHelper = new CourseDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
     //   Log.v(LOG_TAG, "QUERY");
        Cursor cursor;
        switch(uriMathcher.match(uri)){
            case COURSE:
            //    Log.v(LOG_TAG, "COURSE");
                cursor = courseDBHelper.getReadableDatabase().query(
                        CourseContract.Course.TABLE_NAME,
                        null,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        null
                );
            break;

            case COURSE_WITH_ID:
               // Log.v(LOG_TAG, "COURSE WITH ID");
                cursor = courseDBHelper.getReadableDatabase().query(
                        CourseContract.Course.TABLE_NAME,
                        null,
                        courseKeyEquals,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
            break;
            case INSTRUCTOR:
                cursor = courseDBHelper.getReadableDatabase().query(
                        CourseContract.Instructor.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case INSTRUCTOR_WITH_ID:
                cursor = courseDBHelper.getReadableDatabase().query(
                        CourseContract.Instructor.TABLE_NAME,
                        null,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case RELATED_COURSES:
                cursor = getRelatedCourses(uri, projection, sortOrder, selectionArgs);
                break;

            case COURSE_INSTRUCTOR:
                cursor = getInstructorsByCourse(uri, projection, sortOrder, selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = courseDBHelper.getWritableDatabase();
        final int match = uriMathcher.match(uri);
        int rowID = -1;
        switch (match){
            case COURSE_WITH_ID:
                  rowID = db.update(CourseContract.Course.TABLE_NAME, values, selection, selectionArgs);
                break;

        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowID;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        final SQLiteDatabase db = courseDBHelper.getWritableDatabase();
        final int match = uriMathcher.match(uri);

        Uri returnUri = CourseContract.Course.CONTENT_URI;

        switch (match){
            case COURSE:
                long id = db.insert(CourseContract.Course.TABLE_NAME, null, values);
                if(id < 0){
                    throw new android.database.SQLException("Failed to insert into " + uri);
                }else{
                    returnUri = CourseContract.Course.buildCourseWithId(values.getAsString(CourseContract.Course.KEY));
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(returnUri, null);
        return returnUri;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {

        final SQLiteDatabase db = courseDBHelper.getWritableDatabase();
        final int match = uriMathcher.match(uri);

        switch (match){
            case COURSE:
                db.beginTransaction();
                int courseRowsInserted = 0;
                try{
                    for (ContentValues value: values){
                        long result = db.insert(CourseContract.Course.TABLE_NAME, null, value);
                        if (result != -1){
                            courseRowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                }finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return courseRowsInserted;
            default:{
                //throw new UnsupportedOperationException ("Unknown uri: " + uri);
            }
        }

        return super.bulkInsert(uri, values);
    }

    /*
        *   HELPER METHODS
        */
/*
    selectionArgs here must receive Course Key in order to map the Instructor
 */
    private Cursor getInstructorsByCourse(Uri uri, String [] projection, String sortOrder, String [] selectionArgs){

        return courseInstructorsQueryBuilder.query(courseDBHelper.getReadableDatabase(),
                projection, courseInstructorSelection, selectionArgs, null, null, sortOrder);
    }

    /*
    selectionArgs here must receive Course Key in order to map the instructor
     */
    private Cursor getRelatedCourses(Uri uri, String [] projection, String sortOrder, String [] selectionArgs){

        return courseRelatedQueryBuilder.query(courseDBHelper.getReadableDatabase(),
                projection, courseRelatedSelection, selectionArgs, null, null, sortOrder);
    }


}
