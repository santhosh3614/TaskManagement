package com.elixir.taskmanagement;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.elixir.taskmanagement.db.Developer;
import com.elixir.taskmanagement.db.Task;
import com.elixir.taskmanagement.db.TaskManagmentDb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class DevelopersHomeActivity extends ActionBarActivity implements OnStatusChangListener {


    private List<Task> tasks;
    private FragmentStatePagerAdapter frgAdapter;

    public List<Task> getOngoingtasks() {
        return ongoingtasks;
    }

    public List<Task> getPendingTasks() {
        return pendingTasks;
    }

    public List<Task> getCompletedTasks() {
        return completedTasks;
    }

    private List<Task> ongoingtasks = new ArrayList<>();
    private List<Task> pendingTasks = new ArrayList<>();
    private List<Task> completedTasks = new ArrayList<>();
    @InjectView(R.id.pager)
    ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developers_home);
        ButterKnife.inject(this);
        Developer developer = getIntent().getParcelableExtra(LoginActivity.EXTRA_EMP);
        tasks = TaskManagmentDb.getInstance(this).getTasks(developer);
        for (Task task : tasks) {
            if (task.getStatus() == Task.Status.COMPLETE) {
                completedTasks.add(task);
            } else if (task.getStatus() == Task.Status.PENDING) {
                pendingTasks.add(task);
            } else {
                ongoingtasks.add(task);
            }
        }
        frgAdapter=new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                TaskFragment taskFragment = new TaskFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("EXTRA_STATUS", position);
                taskFragment.setArguments(bundle);
                return taskFragment;
            }

            @Override
            public int getCount() {
                return 3;
            }
        };
        viewPager.setAdapter(frgAdapter);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.developers_home, menu);
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

    @Override
    public void onStatusChange(Task.Status status, Task task) {
        if(task.getStatus()== Task.Status.COMPLETE){
            completedTasks.add(task);
        }else if(task.getStatus()== Task.Status.PENDING){
            pendingTasks.add(task);
        }else{
            ongoingtasks.add(task);
        }
        frgAdapter.notifyDataSetChanged();
    }

    public static class TaskFragment extends ListFragment {

        OnStatusChangListener statusChangListener;
        List<Task> tasks;
        Task.Status status;

        public void setOnStatusChangeListener(OnStatusChangListener statusChangListener) {
            this.statusChangListener = statusChangListener;
        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            status = Task.Status.values()[getArguments().getInt("EXTRA_STATUS")];
            if (status == Task.Status.COMPLETE) {
                tasks = ((DevelopersHomeActivity) getActivity()).getCompletedTasks();
            } else if (status == Task.Status.ONGOING) {
                tasks = ((DevelopersHomeActivity) getActivity()).getOngoingtasks();
            } else {
                tasks = ((DevelopersHomeActivity) getActivity()).getPendingTasks();
            }
            getListView().setAdapter(new BaseAdapter() {
                @Override
                public int getCount() {
                    return tasks.size();
                }

                @Override
                public Task getItem(int position) {
                    return tasks.get(position);
                }

                @Override
                public long getItemId(int position) {
                    return 0;
                }

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    if (convertView == null) {
                        convertView = LayoutInflater.from(getActivity()).inflate(R.layout.dev_list_item, null);
                    }
                    final Task task=getItem(position);
                    String proInfo ="Name:" + task.getTaskName() + "\n"
                            + getString(R.string.start_time) + new Date(task.getStartTime()).toString() + "\n"
                            + getString(R.string.end_time) + new Date(task.getEndTime()).toString()+"\n";
                    ((TextView)convertView.findViewById(R.id.textView)).setText(proInfo);
                    Task.Status status1= Task.Status.values()[getArguments().getInt("EXTRA_STATUS")];
                    final List<Task.Status> status=new ArrayList<>(Arrays.asList(Task.Status.values()));
                    status.remove(status1);
                    final ArrayAdapter<Task.Status> adapter = new ArrayAdapter<Task.Status>(getActivity(),
                            android.R.layout.simple_spinner_item,status);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Spinner spinner= (Spinner) convertView.findViewById(R.id.spinner);
                    spinner.setAdapter(adapter);
                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            task.setStatus(status.get(position));
                            statusChangListener.onStatusChange(status.get(position),task);
                            tasks.remove(task);
                            notifyDataSetChanged();
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                    return convertView;
                }
            });

        }
    }


}
