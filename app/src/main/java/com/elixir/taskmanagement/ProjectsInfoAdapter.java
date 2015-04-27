package com.elixir.taskmanagement;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.elixir.taskmanagement.db.Project;

import java.util.Date;
import java.util.List;

/**
 * Created by santhosh on 26/4/15.
 */
public class ProjectsInfoAdapter extends BaseAdapter {

    private final List<Project> allProjects;
    private final Context context;

    ProjectsInfoAdapter(Context context, List<Project> allProjects) {
        this.context = context;
        this.allProjects = allProjects;
    }

    @Override
    public int getCount() {
        return allProjects.size();
    }

    @Override
    public Project getItem(int position) {
        return allProjects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView = (TextView) convertView;
        if (convertView == null) {
            textView = new TextView(context);
        }
        Project project = getItem(position);
        String proInfo = context.getString(R.string.project_name) + project.getProjectName() + "\n"
                + context.getString(R.string.start_time) + new Date(project.getStartTime()).toString() + "\n"
                + context.getString(R.string.end_time) + new Date(project.getEndTime()).toString();
        textView.setText(proInfo);
        return textView;
    }

}
