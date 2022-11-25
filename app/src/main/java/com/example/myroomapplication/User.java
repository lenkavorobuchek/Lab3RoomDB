package com.example.myroomapplication;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User implements Parcelable {
    public User(String firstName, String lastName, String patronymicName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymicName = patronymicName;
    }

    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "first_name")
    public String firstName;

    @ColumnInfo(name = "last_name")
    public String lastName;

    @ColumnInfo(name = "patr_name")
    public String patronymicName;

    protected User(Parcel in) {
        uid = in.readInt();
        firstName = in.readString();
        lastName = in.readString();
        patronymicName = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(uid);
        parcel.writeString(firstName);
        parcel.writeString(lastName);
        parcel.writeString(patronymicName);
    }

    boolean hasZeroFields() {
        return firstName == null || lastName == null || patronymicName == null;
    }


}
