package com.example.myroomapplication;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Other implements Parcelable {
    public Other(String studyPlace, String workPlace) {
        this.studyPlace = studyPlace;
        this.workPlace = workPlace;
    }

    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "study_place")
    public String studyPlace;

    @ColumnInfo(name = "work_place")
    public String workPlace;

    protected Other(Parcel in) {
        uid = in.readInt();
        studyPlace = in.readString();
        workPlace = in.readString();
    }

    public static final Creator<Other> CREATOR = new Creator<Other>() {
        @Override
        public Other createFromParcel(Parcel in) {
            return new Other(in);
        }

        @Override
        public Other[] newArray(int size) {
            return new Other[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(uid);
        parcel.writeString(studyPlace);
        parcel.writeString(workPlace);
    }

    boolean hasZeroFields() {
        return studyPlace == null || workPlace == null;
    }


}

