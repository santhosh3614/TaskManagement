package com.elixir.taskmanagement;

import android.app.Activity;
import android.content.Intent;
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


public class LoginActivity extends Activity {

    static final String EXTRA_LOGIN_TYPE = "login_type";
    static final String EXTRA_EMP = "emp";
    @InjectView(R.id.title)
    TextView titleTxtView;
    @InjectView(R.id.username_edttxt)
    EditText userNameEditText;
    @InjectView(R.id.pwd_edttxt)
    EditText pwdEditText;
    @InjectView(R.id.signup_btn)
    Button signupBtn;
    @InjectView(R.id.signinbtn)
    Button signnButton;
    private Empolyee.Type loginTypes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);
        Intent intent = getIntent();
        TaskManagmentDb db = TaskManagmentDb.getInstance(this);
        loginTypes = Empolyee.Type.values()[intent.getIntExtra(EXTRA_LOGIN_TYPE,
                Empolyee.Type.ADMIN.ordinal())];
        if (loginTypes == Empolyee.Type.MANAGER) {
            titleTxtView.setText(getString(R.string.manager_login));
            signupBtn.setVisibility(View.GONE);
        } else if (loginTypes == Empolyee.Type.DEVELOPER) {
            titleTxtView.setText(getString(R.string.developers_login));
            signupBtn.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.signup_btn, R.id.signinbtn})
    void onClick(View view) {
        if (view.getId() == R.id.signup_btn) {
            Intent intent = new Intent(this, RegistrationActivity.class);
            intent.putExtra(EXTRA_LOGIN_TYPE, loginTypes);
            startActivity(intent);
        } else if (view.getId() == R.id.signinbtn) {
            String emailID = userNameEditText.getText().toString();
            String password = pwdEditText.getText().toString();
            if (TextUtils.isEmpty(emailID)) {
                Toast.makeText(this, getString(R.string.invalid_emailId), Toast.LENGTH_LONG).show();
                return;
            }
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(this, getString(R.string.invalid_pwd), Toast.LENGTH_LONG).show();
                return;
            }
            Empolyee empolyee = TaskManagmentDb.getInstance(this).getEmployee(emailID, password, loginTypes);
            if (empolyee == null) {
                Toast.makeText(this, getString(R.string.invalid_usrname_pwd), Toast.LENGTH_LONG).show();
                return;
            }
            Intent intent = null;
            if (loginTypes == Empolyee.Type.ADMIN) {
                intent = new Intent(this, AdminHomeActivity.class);
            } else if (loginTypes == Empolyee.Type.DEVELOPER) {
                intent = new Intent(this, DevelopersHomeActivity.class);
            } else {
                intent = new Intent(this, ManagersHomeActivity.class);
            }
            intent.putExtra(EXTRA_EMP, (android.os.Parcelable) empolyee);
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }
}
