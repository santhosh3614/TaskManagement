package com.elixir.taskmanagement;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.elixir.taskmanagement.db.Developer;
import com.elixir.taskmanagement.db.Empolyee;
import com.elixir.taskmanagement.db.Project;
import com.elixir.taskmanagement.db.ProjectManager;
import com.elixir.taskmanagement.db.Task;
import com.elixir.taskmanagement.db.TaskManagmentDb;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by santhosh on 26/4/15.
 */
public class ProTaskInfoAdapter extends BaseAdapter {

    private final ProjectManager projectManager;
    private final List<Empolyee> dev;
    private Context context;
    List<Project> projects;
    private long startTime;
    private long endTime;

    public ProTaskInfoAdapter(Context context, List<Project> allProjects, ProjectManager projectManager, List<Empolyee> dev) {
        this.context=context;
        this.projects=allProjects;
        this.projectManager=projectManager;
        this.dev=dev;
    }

    @Override
    public int getCount() {
        return projects.size();
    }

    @Override
    public Project getItem(int position) {
        return projects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position,View convertView, ViewGroup parent) {
        Holder holder=null;
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.manager_card_item,null);
            convertView.setTag(new Holder(convertView));
        }
        final Project project=projects.get(position);
        holder= (Holder) convertView.getTag();
        holder.proNameTxtView.setText(project.getProjectName());
        holder.statTimeTxtview.setText(new Date(project.getStartTime()).toString());
        holder.endTimeTxtView.setText(new Date(project.getEndTime()).toString());
        String tasks="";
        final List<Task> tasks1 = project.getTasks();
        if(tasks1!=null && tasks1.size()>0){
            for (Task task : tasks1) {
                String proInfo = "Task #"+
                        "Name:" + project.getProjectName() + "\n"
                        + context.getString(R.string.start_time) + new Date(project.getStartTime()).toString() + "\n"
                        + context.getString(R.string.end_time) + new Date(project.getEndTime()).toString()+"\n";
            }
        }else{
            tasks="No available Tasks.";
        }
        holder.addTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTaskDialog(project);
            }
        });
        holder.tasks_txtview.setText(project.getProjectName());
        return convertView;
    }

    private void addTaskDialog(final Project project) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context).setTitle(context.getString(R.string.add_task));
        View view = LayoutInflater.from(context).inflate(R.layout.add_proj_dialog, null);
        builder.setView(view);
        final Spinner spinner = (Spinner) view.findViewById(R.id.mngr_spinner);
        final TextView name = (TextView) view.findViewById(R.id.project_edittxt);
        final EditText startTimeEditText = (EditText) view.findViewById(R.id.starttime_edittxt);
        final EditText endTimeEdittext = (EditText) view.findViewById(R.id.endtime_edittxt);

        final Calendar c = Calendar.getInstance();
        final int year = c.get(Calendar.YEAR);
        final int month = c.get(Calendar.MONTH);
        final int day = c.get(Calendar.DAY_OF_MONTH);

        startTimeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        startTime = new GregorianCalendar(year, monthOfYear, dayOfMonth).getTime().getTime();
                        startTimeEditText.setText(new StringBuilder().append(monthOfYear + 1)
                                .append("-").append(dayOfMonth).append("-").append(year)
                                .append(" "));
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
        endTimeEdittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        endTime = new GregorianCalendar(year, monthOfYear, dayOfMonth).getTime().getTime();
                        endTimeEdittext.setText(new StringBuilder().append(monthOfYear + 1)
                                .append("-").append(dayOfMonth).append("-").append(year)
                                .append(" "));
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
        final ArrayAdapter<Empolyee> adapter = new ArrayAdapter<Empolyee>(context, android.R.layout.simple_spinner_item,dev);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        builder.setPositiveButton(context.getString(R.string.save), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String proName = name.getText().toString();
                if (TextUtils.isEmpty(proName)) {
                    Toast.makeText(context, context.getString(R.string.invalid_task_name), Toast.LENGTH_LONG).show();
                    return;
                }
                if (startTime < 0) {
                    Toast.makeText(context,context.getString(R.string.enter_start_time), Toast.LENGTH_LONG).show();
                    return;
                }
                if (endTime < 0) {
                    Toast.makeText(context, context.getString(R.string.enter_end_time), Toast.LENGTH_LONG).show();
                    return;
                }
                if (endTime < startTime) {
                    Toast.makeText(context, context.getString(R.string.endtime_starttime_mismatch), Toast.LENGTH_LONG).show();
                    return;
                }
                Developer developer = (Developer) spinner.getSelectedItem();
                if (developer == null) {
                    Toast.makeText(context, context.getString(R.string.no_task_available), Toast.LENGTH_LONG).show();
                    return;
                }
                Task task = TaskManagmentDb.getInstance(context).createTask(developer,projectManager, proName, startTime, endTime);
                if (task != null) {
                    Toast.makeText(context, context.getString(R.string.pro_added_succesfully), Toast.LENGTH_LONG).show();
                }
                List<Task> tasks= project.getTasks();
                if(tasks==null){
                    tasks=new ArrayList<Task>();
                }
                tasks.add(task);
                project.setTasks(tasks);
                notifyDataSetChanged();
                startTime = endTime = -1;
                dialog.cancel();
            }
        }).setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startTime = endTime = -1;
                dialog.cancel();
            }
        });
        builder.show();
    }

    static class Holder{
        @InjectView(R.id.pro_name_txtview)
        TextView proNameTxtView;
        @InjectView(R.id.start_txtview)
        TextView statTimeTxtview;
        @InjectView(R.id.end_txtview)
        TextView endTimeTxtView;
        @InjectView(R.id.tasks_txtview)
        TextView tasks_txtview;
        @InjectView(R.id.add_tasks_btn)
        Button addTaskBtn;

        Holder(View view){
            ButterKnife.inject(this,view);
        }
    }


}
