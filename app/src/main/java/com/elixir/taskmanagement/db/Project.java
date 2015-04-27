package com.elixir.taskmanagement.db;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by santhoshkumar on 25/4/15.
 */
public class Project implements Parcelable{

    private String projectName;
    private long proId, startTime, endTime;

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    private List<Task> tasks;

    Project(long proId, String projectName, long startTime, long endTime) {
        this(proId,projectName,startTime,endTime,null);
    }

    public long getProId() {
        return proId;
    }

    Project(long proId, String projectName, long startTime, long endTime, List<Task> tasks) {
        this.proId = proId;
        this.projectName = projectName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.tasks=tasks;
    }


    public String getProjectName() {
        return projectName;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeLong(proId);
        out.writeString(projectName);
        out.writeLong(startTime);
        out.writeLong(endTime);
//        if(tasks==null){
//            out.writeTypedList(new ArrayList<Task>());
//        }
//        out.writeTypedList(tasks);
    }

    private Project(Parcel in) {
        proId=in.readInt();
        projectName=in.readString();
        startTime=in.readLong();
        endTime=in.readLong();
//        in.readTypedList(tasks,Task.CREATOR);
    }

    public static final Parcelable.Creator<Project> CREATOR
            = new Parcelable.Creator<Project>() {
        public Project createFromParcel(Parcel in) {
            return new Project(in);
        }

        public Project[] newArray(int size) {
            return new Project[size];
        }
    };

    public List<Task> getTasks() {
        return tasks;
    }
    @Override
    public boolean equals(Object o) {
        if (o instanceof Project) {
            Project pro = (Project) o;
            return pro.proId == proId;
        }
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return projectName.hashCode();
    }

    @Override
    public String toString() {
        return projectName;
    }


}
