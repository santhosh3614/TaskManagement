package com.elixir.taskmanagement.db;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by santhosh on 25/4/15.
 */
public class Admin extends Empolyee implements Parcelable{
    public Admin(long id, String name, String phoneNumber, String emailId) {
        super(id,name, phoneNumber, emailId, Type.ADMIN);
    }

    @Override
    public String getDesignation() {
        return Type.ADMIN.name();
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeLong(getId());
        out.writeString(getName());
        out.writeString(getEmailId());
        out.writeString(getPhoneNumber());
        out.writeInt(Type.MANAGER.ordinal());
    }

    private Admin(Parcel in) {
        setId(in.readInt());
        setName(in.readString());
        setEmailId(in.readString());
        setPhoneNumber(in.readString());
        setType(Type.DEVELOPER);
    }

    public static final Parcelable.Creator<Admin> CREATOR
            = new Parcelable.Creator<Admin>() {
        public Admin createFromParcel(Parcel in) {
            return new Admin(in);
        }

        public Admin[] newArray(int size) {
            return new Admin[size];
        }
    };

}
