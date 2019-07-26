package com.example.testproject.database.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.testproject.database.models.Category;
import com.example.testproject.database.models.Course;

import java.util.List;

@Dao
public interface CategoryDAO {


    @Query("SELECT * FROM category")
    public List<Category> getCategoryList();

    @Query("SELECT * FROM category WHERE category_id =:categoryId")
    public Category getCategoryById(String categoryId);


    @Query("SELECT * FROM category WHERE course_id =:courseId")
    public List<Category> getCategoryListByCourseId(String courseId);

    @Insert
    public Long insertCategory(Category category);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public Long[] insertCategories(List<Category> categories);

    @Update
    public int updateCategory(Category category);

    @Delete
    public int deleteCategory(Category category);

    @Query("DELETE FROM category")
    public void nukeTable();
}