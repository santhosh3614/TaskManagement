package com.elixir.taskmanagement;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.elixir.taskmanagement.db.Empolyee;
import com.elixir.taskmanagement.db.Project;
import com.elixir.taskmanagement.db.ProjectManager;
import com.elixir.taskmanagement.db.TaskManagmentDb;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class ManagersHomeActivity extends Activity {

    private List<Project> allProjects;
    private List<Empolyee> dev;
    private ProTaskInfoAdapter projectInfoAdapter;
    @InjectView(R.id.listview)
    ListView listview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_managers_home);
        ButterKnife.inject(this);
        ProjectManager projectManager=getIntent().getParcelableExtra(LoginActivity.EXTRA_EMP);
        TaskManagmentDb db = TaskManagmentDb.getInstance(this);
        allProjects = db.getAllProjects(projectManager);
        for (Project allProject : allProjects) {
            allProject.setTasks(db.getTasks(allProject));
        }
        dev = db.getAllEmployee(Empolyee.Type.DEVELOPER);
        TextView headerView = new TextView(this);
        headerView.setText(getString(R.string.projects));
        listview.addHeaderView(headerView);
        projectInfoAdapter = new ProTaskInfoAdapter(this, allProjects,projectManager,dev);
        listview.setAdapter(projectInfoAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.managers_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.signout) {
            //TODO signout
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
