package com.elixir.taskmanagement;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.elixir.taskmanagement.db.Empolyee;
import com.elixir.taskmanagement.db.TaskManagmentDb;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class RegistrationActivity extends Activity {

    static final String EXTRA_REG_TYPE = "reg_type";
    private Empolyee.Type type;

    @InjectView(R.id.admin_reg_txtview)
    TextView regTxtView;
    @InjectView(R.id.save_edittxt)
    Button saveBtn;
    @InjectView(R.id.name_edittxt)
    EditText nameEdittext;
    @InjectView(R.id.pwd_edittxt)
    EditText pwdEdittext;
    @InjectView(R.id.phno_edittxt)
    EditText phnoEdittext;
    @InjectView(R.id.emailid_edittxt)
    EditText emailIdEditetxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        ButterKnife.inject(this);
        TaskManagmentDb db=TaskManagmentDb.getInstance(this);
        int ordinal = getIntent().getIntExtra(EXTRA_REG_TYPE, Empolyee.Type.ADMIN.ordinal());
        type = Empolyee.Type.values()[ordinal];
        if (type == Empolyee.Type.MANAGER) {
            regTxtView.setText(getString(R.string.manger_reg));
        } else if (type == Empolyee.Type.DEVELOPER) {
            regTxtView.setText(getString(R.string.dev_reg));
        }
    }

    @OnClick(R.id.save_edittxt)
    void onClick(View view) {
        int id = view.getId();
        if(id==R.id.save_edittxt){
            String name=nameEdittext.getText().toString();
            String pwd=pwdEdittext.getText().toString();
            String phno=phnoEdittext.getText().toString();
            String emailId=emailIdEditetxt.getText().toString();
            if(TextUtils.isEmpty(name)){
                Toast.makeText(this,getString(R.string.invalid_name),Toast.LENGTH_LONG).show();
                return;
            }
            if(TextUtils.isEmpty(pwd)){
                Toast.makeText(this,getString(R.string.invalid_pwd),Toast.LENGTH_LONG).show();
                return;
            }
            if(TextUtils.isEmpty(phno)){
                Toast.makeText(this,getString(R.string.invalid_name),Toast.LENGTH_LONG).show();
                return;
            }
            if(TextUtils.isEmpty(emailId)){
                Toast.makeText(this,getString(R.string.invalid_emailId),Toast.LENGTH_LONG).show();
                return;
            }
            Empolyee empolyee=TaskManagmentDb.getInstance(this).createEmployee(name,emailId,pwd,phno,type);
            if(empolyee==null){
                Toast.makeText(this,"some thing get wrong ",Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this,getString(R.string.succesfully_reg),Toast.LENGTH_LONG).show();
                setResult(RESULT_OK);
                finish();
            }
        }
    }
}
