package com.example.testproject.database;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.android.volley.VolleyError;
import com.example.testproject.R;
import com.example.testproject.database.db_usecases.CategoryDBUseCases;
import com.example.testproject.database.db_usecases.CourseDBUseCases;
import com.example.testproject.database.db_usecases.QuizUseCases;
import com.example.testproject.database.db_usecases.SectionPatternUseCases;
import com.example.testproject.database.models.Category;
import com.example.testproject.database.models.Course;
import com.example.testproject.database.models.Quiz;
import com.example.testproject.database.models.SectionPattern;
import com.example.testproject.database.operators.DBOperationsHelper;

import java.util.List;

public class DatabaseInitizationService extends Service {

    private static final String TAG = DatabaseInitizationService.class.getSimpleName();
    private LocalBinder mBinder = new LocalBinder();
    private SynchronizationListener mSynchronizationListener;

    private CourseDBUseCases mCourseDBUseCases;
    private CategoryDBUseCases mCategoryDBUseCases;
    private QuizUseCases mQuizUseCases;
    private SectionPatternUseCases mSectionPatternUseCases;

    Context mContext;

    public DatabaseInitizationService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public DatabaseInitizationService(SynchronizationListener listener) {
        mSynchronizationListener = listener;
    }

    public class LocalBinder extends Binder {
        public DatabaseInitizationService getService() {
            return DatabaseInitizationService.this;
        }
    }

    /* Used to build and start foreground service. */
    private void startForegroundService() {
        Log.d(TAG, "Start foreground service.");

        // Create notification builder.
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        // Make notification show big text.
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.setBigContentTitle("Database Syncronization initiated");
        bigTextStyle.bigText("The database of app will be filled by server data shortly.");
        // Set big text style.
        builder.setStyle(bigTextStyle);

        builder.setWhen(System.currentTimeMillis());
        builder.setSmallIcon(R.mipmap.ic_launcher);
        Bitmap largeIconBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_lock_icon);
        builder.setLargeIcon(largeIconBitmap);
        // Make the notification max priority.
        builder.setPriority(Notification.PRIORITY_MAX);
        // Build the notification.
        Notification notification = builder.build();

        // Start foreground service.
        startForeground(1, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForegroundService();
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                callCoursesAPI();
            }
        });
        return START_STICKY;
    }

    private void callCoursesAPI() {

        // *********** API CALL TO COURSES
        SyncApiCallManager.getInstance(mContext).callCourseAPI(new SyncApiCallManager.ApiResponseListener() {
            @Override
            public void onSuccess(String response) {
                saveCoursesLocally(response);
            }

            @Override
            public void onError(VolleyError error) {
                Log.e(TAG, error.getLocalizedMessage());
            }
        });
    }

    private void saveCoursesLocally(String response) {
        // *********** START INSERTING COURSES
        mCourseDBUseCases = new CourseDBUseCases(new DBOperationsHelper() {
            @Override
            public void onListInserted(Long[] result) {
                super.onListInserted(result);
                //Courses added successfully now proceed for Categories Insertion
                mCourseDBUseCases.getAllCoursesUsecase();
            }

            @Override
            public <T> void onItemListSearched(List<T> list) {
                super.onItemListSearched(list);
                for (T item : list) {
                    final Course course = (Course) item;
                    // *********** API CALL TO CATEGORIES
                    callCategoryListAPI(course);
                }
            }
        });
        mCourseDBUseCases.checkIfCoursesExistAndAdd(response);
    }

    private void callCategoryListAPI(final Course course) {
        SyncApiCallManager.getInstance(mContext).callCategoryListAPI(course.getCourseId(), new SyncApiCallManager.ApiResponseListener() {
            @Override
            public void onSuccess(String response) {
                saveCategoriesLocally(response, course);
            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }

    private void saveCategoriesLocally(String response, final Course course) {
        // *********** START INSERTING CATEGORIES RELATED TO THAT COURSE ONE BY ONE
        mCategoryDBUseCases = new CategoryDBUseCases(new DBOperationsHelper() {
            @Override
            public void onItemInserted(Long result) {
                super.onItemInserted(result);
                mCategoryDBUseCases.getCategoryListByCourseIdUsecase(course.getCourseId());
            }

            @Override
            public <T> void onItemListSearched(List<T> list) {
                super.onItemListSearched(list);
                for (T item : list) {
                    Category category = (Category) item;
                    //API CALL TO QUIZES OF CATEGORY
                    callQuizAPI(category);
                }
            }
        });
        List<Category> categoryList = mCategoryDBUseCases.getCategoriesFromString(response);
        mCategoryDBUseCases.checkIfCategoriesExistAndAdd(categoryList, course.getCourseId());
    }

    private void callQuizAPI(final Category category) {
        SyncApiCallManager.getInstance(mContext).callQuizAPI(category, new SyncApiCallManager.ApiResponseListener() {
            @Override
            public void onSuccess(String response) {
                if (response != null && !response.isEmpty()) {
                    saveQuizLocally(response,category);
                }
            }

            @Override
            public void onError(VolleyError error) {
                Log.e(TAG, error.getLocalizedMessage());
            }
        });
    }

    private void saveQuizLocally(String response, Category category) {
        mQuizUseCases = new QuizUseCases(new DBOperationsHelper() {
            @Override
            public void onListInserted(Long[] result) {
                super.onListInserted(result);
            }
        });
        List<Quiz> quizList = mQuizUseCases.getQuizesFromString(response);
        mQuizUseCases.addQuizList(quizList);

        for(Quiz quiz : quizList){
            List<SectionPattern> sectionPatternList = quiz.getSectionPatterns();

            mSectionPatternUseCases = new SectionPatternUseCases(new DBOperationsHelper(){
                @Override
                public <T> void onItemListSearched(List<T> list) {
                    super.onItemListSearched(list);
                }
            });
//            mSectionPatternUseCases.addQuizList();
        }

    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
