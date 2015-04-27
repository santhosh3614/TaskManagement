package com.elixir.taskmanagement;

import com.elixir.taskmanagement.db.Task;

/**
 * Created by santhosh on 27/4/15.
 */
public interface OnStatusChangListener {
    void onStatusChange(Task.Status status,Task task);
}
