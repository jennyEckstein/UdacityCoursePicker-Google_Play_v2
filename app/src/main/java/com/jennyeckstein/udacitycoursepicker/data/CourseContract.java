package com.jennyeckstein.udacitycoursepicker.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Jenny on 5/10/2016.
 */
public class CourseContract {

    public static final String CONTENT_AUTHORITY = "com.jennyeckstein.udacitycoursepicker";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_COURSE = "course";
    public static final String PATH_INSTRUCTOR = "instructor";
    public static final String PATH_COURSE_INSTRUCTOR = "course_instructor";
    public static final String PATH_RELATED_COURSES = "related_courses";

    public static final class Related_Courses implements BaseColumns{

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_RELATED_COURSES).build();

        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RELATED_COURSES;
        public static final String CONTENT_iTEM_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RELATED_COURSES;

        public static final String TABLE_NAME = "related_courses";

        public static final String COURSE_ID = "course_id";
        public static final String RELATED_COURSE_ID = "related_course_id";
    }

    public static final class Course_Instructor implements BaseColumns{

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_COURSE_INSTRUCTOR).build();

        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_COURSE_INSTRUCTOR;
        public static final String CONTENT_iTEM_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_COURSE_INSTRUCTOR;

        public static final String TABLE_NAME = "course_instructor";

        public static final String COURSE_ID = "course_id";
        public static final String INSTRUCTOR_ID = "instructor_id";
    }

    public static final class Instructor implements BaseColumns{

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_INSTRUCTOR).build();

        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INSTRUCTOR;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INSTRUCTOR;

        public static final String TABLE_NAME = "instructor";

        public static final String ID = "id";
        public static final String NAME = "name";
        public static final String BIO = "bio";
        public static final String IMAGE = "image";

        public static Uri buildInstructorWithId(int id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class Course implements BaseColumns{

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_COURSE).build();

        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_COURSE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_COURSE;

        public static final String TABLE_NAME = "course";

       // public static final String COURSE_ID = "course_id";
        public static final String SUBTITLE = "subtitle";
        public static final String KEY = "key";
        public static final String IMAGE = "image";
        public static final String EXPECTED_LEARNING = "expected_learning";
        public static final String FEATURED = "featured";
        public static final String PROJECT_NAME = "project_name";

        public static final String TITLE = "title";

        public static final String REQUIRED_KNOWLEDGE = "required_knowledge";
        public static final String SYLLABUS = "syllabus";
        public static final String NEW_RELEASE = "new_release";
        public static final String HOMEPAGE = "homepage";
        public static final String PROJECT_DESCRIPTION = "project_description";
        public static final String FULL_COURSE_AVAILABLE = "full_course_available";
        public static final String FAQ = "faq";
        public static final String BANNER_IMAGE = "banner_image";
        public static final String SHORT_SUMMARY = "short_summary";
        public static final String SLUG = "slug";
        public static final String STARTER = "starter";
        public static final String LEVEL = "level";
        public static final String SUMMARY = "summary";
        public static final String DURATION_IN_HOURS = "duration_in_hours";
        public static final String EXPECTED_DURATION_UNIT = "expected_duration_unit";
        public static final String EXPECTED_DURATION = "expected_duration";
        public static final String LIKED_COURSE = "liked_course";

        public static Uri buildCourseWithId(String key){
            return CONTENT_URI.buildUpon().appendPath(key).build();
        }
    }


}

