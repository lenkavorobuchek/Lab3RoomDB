package com.example.myroomapplication;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM user")
    List<User> getAll();

    @RawQuery
    List<User> getAllByColumns(SupportSQLiteQuery dynamicColumnQuery);

    @Query("SELECT * FROM user LIMIT 1 OFFSET :position")
    User getByPosition(int position);

    @Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
            "last_name LIKE :last LIMIT 1")
    User findByName(String first, String last);

    @Insert
    void insert(User user);

    @Insert
    void insertAll(User... users);

    @Update
    void update(User user);

    @Query("DELETE FROM user WHERE user.uid IN (SELECT uid FROM user LIMIT 1 OFFSET :position)")
    void delete(int position);

    @Query("DELETE FROM user")
    void deleteAll();
}
