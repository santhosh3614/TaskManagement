package com.elixir.taskmanagement;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.elixir.taskmanagement.db.Empolyee;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
    }

    @OnClick({R.id.admin_login_btn, R.id.mngr_login_btn, R.id.dev_login_btn})
    void onLoginClick(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        if (view.getId() == R.id.admin_login_btn) {
            intent.putExtra(LoginActivity.EXTRA_LOGIN_TYPE, Empolyee.Type.ADMIN.ordinal());
        } else if (view.getId() == R.id.mngr_login_btn) {
            intent.putExtra(LoginActivity.EXTRA_LOGIN_TYPE, Empolyee.Type.MANAGER.ordinal());
        } else {
            intent.putExtra(LoginActivity.EXTRA_LOGIN_TYPE, Empolyee.Type.DEVELOPER.ordinal());
        }
        startActivity(intent);
    }

}
