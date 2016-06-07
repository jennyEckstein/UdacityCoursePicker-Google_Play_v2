package com.jennyeckstein.udacitycoursepicker.data;

import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.test.AndroidTestCase;

/**
 * Created by Jenny on 5/18/2016.
 */
public class TestProvider extends AndroidTestCase {

    /*
    Test if ContentProvider is registered with Android Manifest
     */
    public void testProviderRegistry(){
        PackageManager packageManager = mContext.getPackageManager();
        ComponentName componentName =
                new ComponentName(mContext.getPackageName(), CourseProvider.class.getName());

        try{
            ProviderInfo providerInfo = packageManager.getProviderInfo(componentName, 0);
            assertEquals("Error: MovieProvider registered with authority: " + providerInfo.authority +
                    " instead of authority: " + CourseContract.CONTENT_AUTHORITY, providerInfo.authority, CourseContract.CONTENT_AUTHORITY);
        }catch(PackageManager.NameNotFoundException e){
            assertTrue("Error: CourseProvider not registered at " + mContext.getPackageName(), false);
        }
    }

    /*
     *  test Uri matcher by matching various Uri with returned ITEM or DIR type
     */
    public void testGetType(){
        String type;
        type = mContext.getContentResolver().getType(CourseContract.Course.CONTENT_URI);
        assertEquals("Error: the Course CONTENT_URI should return Course.CONTENT_DIR_TYPE",
                CourseContract.Course.CONTENT_DIR_TYPE, type);

        type = mContext.getContentResolver().getType(CourseContract.Course.buildCourseWithId("ud171"));
        assertEquals("Error: the Course with ID CONTENT_URI should return Course.CONTENT_ITEM_TYPE",
                CourseContract.Course.CONTENT_ITEM_TYPE, type);

        type = mContext.getContentResolver().getType(CourseContract.Instructor.CONTENT_URI);
        assertEquals("Error: the Instructor CONTENT_URI should return Instructor.CONTENT_DIR_TYPE",
                CourseContract.Instructor.CONTENT_DIR_TYPE, type);

        type = mContext.getContentResolver().getType(CourseContract.Instructor.buildInstructorWithId(123));
        assertEquals("Error: the Instructor with Id CONTENT_URI should return Instructor.CONTENT_ITEM_TYPE",
                CourseContract.Instructor.CONTENT_ITEM_TYPE, type);

        type = mContext.getContentResolver().getType(CourseContract.Related_Courses.CONTENT_URI);
        assertEquals("Error: the Related_Courses CONTENT_URI should return Related_Courses.CONTENT_DIR_TYPE",
                CourseContract.Related_Courses.CONTENT_DIR_TYPE, type);

        type = mContext.getContentResolver().getType(CourseContract.Course_Instructor.CONTENT_URI);
        assertEquals("Error: the Course_Instructor CONTENT_URI should return Course_Instructor.CONTENT_DIR_TYPE",
                CourseContract.Course_Instructor.CONTENT_DIR_TYPE, type);
    }
}
