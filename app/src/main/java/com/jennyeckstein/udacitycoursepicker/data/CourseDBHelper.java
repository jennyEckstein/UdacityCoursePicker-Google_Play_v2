package com.jennyeckstein.udacitycoursepicker.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Jenny on 5/10/2016.
 */
public class CourseDBHelper extends SQLiteOpenHelper{

    private static final String LOG_TAG = CourseDBHelper.class.getSimpleName();
/*
2 - added new field _id to Course table
3 - added new filed liked
4 - fixed table name
 */
    private static final int DATABASE_VERSION = 4;
    static final String DATABASE_NAME = "course.db";

    public CourseDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_RELATED_COURSES_TABLE = " CREATE TABLE " + CourseContract.Related_Courses.TABLE_NAME + " (" +
                CourseContract.Related_Courses.COURSE_ID + " TEXT, " +
                CourseContract.Related_Courses.RELATED_COURSE_ID + " TEXT," +
                " FOREIGN KEY (" +  CourseContract.Related_Courses.COURSE_ID + ") REFERENCES " +
                CourseContract.Course.TABLE_NAME +  "(" + CourseContract.Course.KEY + "));" ;

     //   Log.v(LOG_TAG, SQL_CREATE_RELATED_COURSES_TABLE);


        final String SQL_CREATE_INSTRUCTOR_TABLE = "CREATE TABLE " + CourseContract.Instructor.TABLE_NAME + " (" +
                CourseContract.Instructor.ID + " INTEGER PRIMARY KEY, " +
                CourseContract.Instructor.NAME + " TEXT NOT NULL, " +
                CourseContract.Instructor.BIO + " TEXT, " +
                CourseContract.Instructor.IMAGE + " TEXT);";

      //  Log.v(LOG_TAG, SQL_CREATE_INSTRUCTOR_TABLE);

        final String SQL_CREATE_COURSE_INSTRUCTOR_TABLE = "CREATE TABLE " + CourseContract.Course_Instructor.TABLE_NAME + " (" +
                CourseContract.Course_Instructor.COURSE_ID + " INTEGER NOT NULL, " +
                CourseContract.Course_Instructor.INSTRUCTOR_ID + " INTEGER NOT NULL, " +
                " FOREIGN KEY (" + CourseContract.Course_Instructor.COURSE_ID + ") REFERENCES " +
                CourseContract.Course_Instructor.TABLE_NAME + " (" + CourseContract.Course.KEY + ")"+
                ");";

     //   Log.v(LOG_TAG, SQL_CREATE_COURSE_INSTRUCTOR_TABLE);


        final String SQL_CREATE_COURSE_TABLE = "CREATE TABLE " + CourseContract.Course.TABLE_NAME + " (" +
                CourseContract.Course._ID + " INTEGER " +
                CourseContract.Course.SUBTITLE + " TEXT, " +
                CourseContract.Course.KEY + " TEXT PRIMARY KEY, " +
                CourseContract.Course.IMAGE + " TEXT, " +
                CourseContract.Course.EXPECTED_LEARNING + " TEXT, " +
                CourseContract.Course.FEATURED + " BOOLEAN, " +
                CourseContract.Course.PROJECT_NAME + " TEXT, " +
                CourseContract.Course.TITLE + " TEXT, " +
                CourseContract.Course.REQUIRED_KNOWLEDGE + " TEXT, " +
                CourseContract.Course.SYLLABUS + " TEXT, " +
                CourseContract.Course.NEW_RELEASE + " TEXT, " +
                CourseContract.Course.HOMEPAGE + " TEXT, " +
                CourseContract.Course.PROJECT_DESCRIPTION + " TEXT, " +
                CourseContract.Course.FULL_COURSE_AVAILABLE + " BOOLEAN, " +
                CourseContract.Course.FAQ + " TEXT, " +
                CourseContract.Course.BANNER_IMAGE + " TEXT, " +
                CourseContract.Course.SHORT_SUMMARY + " TEXT, " +
                CourseContract.Course.SLUG + " TEXT, " +
                CourseContract.Course.STARTER + " BOOLEAN, " +
                CourseContract.Course.LEVEL + " TEXT, " +
                CourseContract.Course.DURATION_IN_HOURS + " INTEGER, " +
                CourseContract.Course.EXPECTED_DURATION + " TEXT, " +
                CourseContract.Course.EXPECTED_DURATION_UNIT + " TEXT, " +
                CourseContract.Course.SUMMARY + " TEXT, " +
                CourseContract.Course.LIKED_COURSE + " TEXT default '0'" +
                ");";

       // Log.v(LOG_TAG, SQL_CREATE_COURSE_TABLE);

        db.execSQL(SQL_CREATE_RELATED_COURSES_TABLE);
        db.execSQL(SQL_CREATE_INSTRUCTOR_TABLE);
        db.execSQL(SQL_CREATE_COURSE_INSTRUCTOR_TABLE);
        db.execSQL(SQL_CREATE_COURSE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + CourseContract.Course.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CourseContract.Course_Instructor.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CourseContract.Instructor.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CourseContract.Related_Courses.TABLE_NAME);
        onCreate(db);
    }
}
