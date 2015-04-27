package com.elixir.taskmanagement.db;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by santhoshkumar on 25/4/15.
 */
public class Task implements Parcelable{

    private long taskId, startTime, endTime;
    private String taskName;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    private Status status;


    public Task(long taskId, String taskName, long startTime, long endTime, Status status) {
        this.taskId = taskId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
    }

    public String getTaskName() {
        return taskName;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Task) {
            Task task = (Task) o;
            return task.taskId == taskId;
        }
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return taskName.hashCode();
    }

    @Override
    public String toString() {
        return taskName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeLong(taskId);
        out.writeString(taskName);
        out.writeLong(startTime);
        out.writeLong(endTime);
    }

    private Task(Parcel in) {
        taskId=in.readInt();
        taskName=in.readString();
        startTime=in.readLong();
        endTime=in.readLong();
    }

    public static final Parcelable.Creator<Task> CREATOR
            = new Parcelable.Creator<Task>() {
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        public Task[] newArray(int size) {
            return new Task[size];
        }
    };
    public static enum Status {
        COMPLETE, PENDING, ONGOING
    }

}
