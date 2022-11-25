package com.example.myroomapplication;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Contacts implements Parcelable {
    public Contacts(String phoneNumber, String email) {
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "phone_number")
    public String phoneNumber;

    public String email;

    protected Contacts(Parcel in) {
        uid = in.readInt();
        phoneNumber = in.readString();
        email = in.readString();
    }

    public static final Creator<Contacts> CREATOR = new Creator<Contacts>() {
        @Override
        public Contacts createFromParcel(Parcel in) {
            return new Contacts(in);
        }

        @Override
        public Contacts[] newArray(int size) {
            return new Contacts[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(uid);
        parcel.writeString(phoneNumber);
        parcel.writeString(email);
    }

    boolean hasZeroFields() {
        return phoneNumber == null || email == null;
    }


}

