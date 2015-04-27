package com.elixir.taskmanagement.db;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by santhosh on 25/4/15.
 */
public class ProjectManager extends Empolyee implements Parcelable{

    private List<Project> projects;

    public ProjectManager(long id,String name, String phoneNumber, String emailId,List<Project> projects) {
        super(id,name, phoneNumber, emailId,Type.MANAGER);
        this.projects = projects;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void addproject(Project projects) {
        this.projects.add(projects);
    }

    @Override
    public String getDesignation() {
        return Type.MANAGER.name();
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeLong(getId());
        out.writeString(getName());
        out.writeString(getEmailId());
        out.writeString(getPhoneNumber());
        out.writeInt(Type.MANAGER.ordinal());
//        if(projects==null){
//            out.writeTypedList(new ArrayList<Project>());
//        }
//        out.writeTypedList(projects);
    }

    private ProjectManager(Parcel in) {
        setId(in.readInt());
        setName(in.readString());
        setEmailId(in.readString());
        setPhoneNumber(in.readString());
        setType(Type.DEVELOPER);
//        in.readTypedList(projects,Project.CREATOR);
    }

    public static final Parcelable.Creator<ProjectManager> CREATOR
            = new Parcelable.Creator<ProjectManager>() {
        public ProjectManager createFromParcel(Parcel in) {
            return new ProjectManager(in);
        }

        public ProjectManager[] newArray(int size) {
            return new ProjectManager[size];
        }
    };


}
