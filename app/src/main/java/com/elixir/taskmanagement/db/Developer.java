package com.elixir.taskmanagement.db;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by santhosh on 25/4/15.
 */
public class Developer extends Empolyee implements Parcelable{

    private List<Task> tasks;

    public Developer(long id,String name, String phoneNumber, String emailId,List<Task> tasks) {
        super(id,name, phoneNumber, emailId,Type.DEVELOPER);
        this.tasks = tasks;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    @Override
    public String getDesignation() {
        return Type.DEVELOPER.name();
    }


    public void writeToParcel(Parcel out, int flags) {
        out.writeLong(getId());
        out.writeString(getName());
        out.writeString(getEmailId());
        out.writeString(getPhoneNumber());
        out.writeInt(Type.DEVELOPER.ordinal());

    }

    public static final Parcelable.Creator<Developer> CREATOR
            = new Parcelable.Creator<Developer>() {
        public Developer createFromParcel(Parcel in) {
            return new Developer(in);
        }

        public Developer[] newArray(int size) {
            return new Developer[size];
        }
    };

     private Developer(Parcel in) {
        setId(in.readInt());
        setName(in.readString());
        setEmailId(in.readString());
        setPhoneNumber(in.readString());
        setType(Type.DEVELOPER);
    }
}
