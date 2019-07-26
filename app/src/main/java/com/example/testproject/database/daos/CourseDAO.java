package com.example.testproject.database.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.testproject.database.models.Course;

import java.util.List;

@Dao
public interface CourseDAO {

    @Query("SELECT * FROM course")
    public List<Course> getCourseList();

    @Query("SELECT * FROM course WHERE course_id =:courseId")
    public Course getCourseById(String courseId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public Long insertCourse(Course course);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public Long[] insertCourses(List<Course> courses);

    @Update
    public int updateCourse(Course course);

    @Delete
    public int deleteCourse(Course course);

    @Query("DELETE FROM course")
    public void nukeTable();


}
