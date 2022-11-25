package com.example.myroomapplication;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import java.util.List;

@Dao
public interface ContactsDao {
    @Query("SELECT * FROM contacts")
    List<Contacts> getAll();

    @RawQuery
    List<Contacts> getAllByColumns(SupportSQLiteQuery dynamicColumnQuery);

    @Query("SELECT * FROM contacts LIMIT 1 OFFSET :position")
    Contacts getByPosition(int position);

    @Insert
    void insert(Contacts contacts);

    @Insert
    void insertAll(Contacts... contacts);

    @Update
    void update(Contacts contacts);

    @Query("DELETE FROM contacts WHERE contacts.uid IN (SELECT uid FROM user LIMIT 1 OFFSET :position)")
    void delete(int position);

    @Query("DELETE FROM contacts")
    void deleteAll();
}
