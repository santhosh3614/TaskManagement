package com.elixir.taskmanagement;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.elixir.taskmanagement.db.Empolyee;
import com.elixir.taskmanagement.db.Project;
import com.elixir.taskmanagement.db.ProjectManager;
import com.elixir.taskmanagement.db.TaskManagmentDb;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class AdminHomeActivity extends Activity {

    private static final int ADD_PROJECTMANAGER_REQUEST_CODE = 143;
    private static final int ADD_DEVELOPER_REQUEST_CODE = 144;
    @InjectView(R.id.listview)
    ListView listView;
    private List<Project> allProjects;
    private ProjectsInfoAdapter projectInfoAdapter;
    private List<Empolyee> mngrs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        ButterKnife.inject(this);
        TaskManagmentDb db = TaskManagmentDb.getInstance(this);
        allProjects = db.getAllProjects(null);
        System.out.println("Pro:" + allProjects);
        mngrs = db.getAllEmployee(Empolyee.Type.MANAGER);
        TextView headerView = new TextView(this);
        headerView.setText(getString(R.string.projects));
        listView.addHeaderView(headerView);
        projectInfoAdapter = new ProjectsInfoAdapter(this, allProjects);
        listView.setAdapter(projectInfoAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add_dev) {
            Intent intent = new Intent(this, RegistrationActivity.class);
            intent.putExtra(RegistrationActivity.EXTRA_REG_TYPE, Empolyee.Type.DEVELOPER.ordinal());
            startActivityForResult(intent, ADD_DEVELOPER_REQUEST_CODE);
            return true;
        } else if (id == R.id.add_pro_mgr) {
            Intent intent = new Intent(this, RegistrationActivity.class);
            intent.putExtra(RegistrationActivity.EXTRA_REG_TYPE, Empolyee.Type.MANAGER.ordinal());
            startActivityForResult(intent, ADD_PROJECTMANAGER_REQUEST_CODE);
        } else if (id == R.id.add_Project) {
            pushAddProjectDialog();
        } else if (id == R.id.signout) {
            //TODO signout
        }
        return super.onOptionsItemSelected(item);
    }

    private long startTime;
    private long endTime;

    private DatePickerDialog.OnDateSetListener enddatePickerListener
            = new DatePickerDialog.OnDateSetListener() {
        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            endTime = new GregorianCalendar(selectedYear, selectedMonth, selectedDay).getTime().getTime();
        }
    };

    private void pushAddProjectDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle(getString(R.string.add_project));
        View view = LayoutInflater.from(this).inflate(R.layout.add_proj_dialog, null);
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
                DatePickerDialog datePickerDialog = new DatePickerDialog(AdminHomeActivity.this, new DatePickerDialog.OnDateSetListener() {
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
                DatePickerDialog datePickerDialog = new DatePickerDialog(AdminHomeActivity.this, new DatePickerDialog.OnDateSetListener() {
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
        final ArrayAdapter<Empolyee> adapter = new ArrayAdapter<Empolyee>(this, android.R.layout.simple_spinner_item, mngrs);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        builder.setPositiveButton(getString(R.string.save), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String proName = name.getText().toString();
                if (TextUtils.isEmpty(proName)) {
                    Toast.makeText(AdminHomeActivity.this, getString(R.string.invalid_project_name), Toast.LENGTH_LONG).show();
                    return;
                }
                if (startTime < 0) {
                    Toast.makeText(AdminHomeActivity.this, getString(R.string.enter_start_time), Toast.LENGTH_LONG).show();
                    return;
                }
                if (endTime < 0) {
                    Toast.makeText(AdminHomeActivity.this, getString(R.string.enter_end_time), Toast.LENGTH_LONG).show();
                    return;
                }
                if (endTime < startTime) {
                    Toast.makeText(AdminHomeActivity.this, getString(R.string.endtime_starttime_mismatch), Toast.LENGTH_LONG).show();
                    return;
                }
                ProjectManager mgr = (ProjectManager) spinner.getSelectedItem();
                if (mgr == null) {
                    Toast.makeText(AdminHomeActivity.this, getString(R.string.no_projectmanager_available), Toast.LENGTH_LONG).show();
                    return;
                }
                Project project = TaskManagmentDb.getInstance(AdminHomeActivity.this).createProject(mgr, proName, startTime, endTime);
                if (project != null) {
                    Toast.makeText(AdminHomeActivity.this, getString(R.string.pro_added_succesfully), Toast.LENGTH_LONG).show();
                }
                allProjects.add(project);
                projectInfoAdapter.notifyDataSetChanged();
                startTime = endTime = -1;
                dialog.cancel();
            }
        }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startTime = endTime = -1;
                dialog.cancel();
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_PROJECTMANAGER_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                mngrs = TaskManagmentDb.getInstance(this).getAllEmployee(Empolyee.Type.MANAGER);
            }
        }
    }

}
