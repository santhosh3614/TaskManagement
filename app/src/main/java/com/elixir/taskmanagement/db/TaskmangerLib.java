package com.elixir.taskmanagement.db;

import java.util.List;

/**
 * Created by santhosh on 26/4/15.
 */
public class TaskmangerLib {

    List<Project> getAllProjects(){

        return null;
    }

    List<ProjectManager> getAllManagers(){
        return  null;
    }

    Project addProject(Admin admin,String projectName,long startTime,long endTime){
        return null;
    }

    ProjectManager getProjectManager(String userName, String password){
        return null;
    }

    List<Task> getAllTasks(){
        return null;
    }

    Task addTask(Developer developer,String taskName,long startTime,long endTime){
        return null;
    }

    Developer getDeveloper(String userName,String pwd){
        return null;
    }

    void updateTasksStatus(Task task,Task.Status status){

    }

    Empolyee addEmpolyee(String empnmae,String empadd,String phNo,String emailId,Empolyee.Type type){
        return null;
    }

}
