package com.elixir.taskmanagement.db;

/**
 * Created by santhoshkumar on 25/4/15.
 */
public abstract class Empolyee{

    private long id;
    private String name;
    private String phoneNumber;
    private String emailId;
    private Type type;

    Empolyee(){
    }

    Empolyee(long id,String name, String phoneNumber, String emailId,Type type) {
        this.id=id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.emailId = emailId;
        this.type = type;
    }

    public int describeContents() {
        return 0;
    }

    public String getEmailId() {
        return emailId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public long getId() {
        return id;
    }

    protected void setId(long id) {
        this.id = id;
    }

    protected void setName(String name) {
        this.name = name;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    protected void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    protected void setType(Type type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public static enum Type {
        ADMIN, MANAGER, DEVELOPER;
    }

    public abstract String getDesignation();

}
