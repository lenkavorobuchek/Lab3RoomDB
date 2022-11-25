package com.example.myroomapplication;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {User.class, Contacts.class, Other.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract ContactsDao contactsDao();
    public abstract OtherDao otherDao();
}