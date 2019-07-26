package com.example.testproject.database;

import android.app.Notification;
import android.app.PendingIntent;
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
import com.example.testproject.database.models.Course;
import com.example.testproject.database.operators.DBOperationsHelper;

import org.json.JSONArray;

import java.util.List;

public class DatabaseInitizationService extends Service {

    private static final String TAG = DatabaseInitizationService.class.getSimpleName();
    private LocalBinder mBinder = new LocalBinder();
    private SynchronizationListener mSynchronizationListener;

    private CourseDBUseCases mCourseDBUseCases;
    private CategoryDBUseCases mCategoryDBUseCases;

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

    class LocalBinder extends Binder {
        DatabaseInitizationService getService() {
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
                startDBSyncProcess();
            }
        });
        return START_STICKY;
    }

    private void startDBSyncProcess() {

        // *********** START INSERTING COURSES
        SyncApiCallManager.getInstance(mContext).callCourseAPI(new SyncApiCallManager.ApiResponseListener() {
            @Override
            public void onSuccess(String response) {
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
                            // *********** START INSERTING CATEGORIES RELATED TO THAT COURSE ONE BY ONE
                            SyncApiCallManager.getInstance(mContext).callCategoryListAPI(course.getCourseId(), new SyncApiCallManager.ApiResponseListener() {
                                @Override
                                public void onSuccess(String response) {
                                    mCategoryDBUseCases = new CategoryDBUseCases(new DBOperationsHelper() {
                                        @Override
                                        public void onListInserted(Long[] result) {
                                            super.onListInserted(result);
                                        }
                                    });
                                    mCategoryDBUseCases.checkIfCategoriesExistAndAdd(response, course.getCourseId());
                                }

                                @Override
                                public void onError(VolleyError error) {

                                }
                            });
                        }
                    }
                });
                mCourseDBUseCases.checkIfCoursesExistAndAdd(response);
            }

            @Override
            public void onError(VolleyError error) {

            }
        });
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
