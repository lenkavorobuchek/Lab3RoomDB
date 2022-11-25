package com.example.myroomapplication;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import java.util.List;

@Dao
public interface OtherDao {
    @Query("SELECT * FROM other")
    List<Other> getAll();

    @RawQuery
    List<Other> getAllByColumns(SupportSQLiteQuery dynamicColumnQuery);

    @Query("SELECT * FROM other LIMIT 1 OFFSET :position")
    Other getByPosition(int position);

    @Insert
    void insert(Other other);

    @Insert
    void insertAll(Other... others);

    @Update
    void update(Other other);

    @Query("DELETE FROM other WHERE other.uid IN (SELECT uid FROM user LIMIT 1 OFFSET :position)")
    void delete(int position);

    @Query("DELETE FROM other")
    void deleteAll();
}
