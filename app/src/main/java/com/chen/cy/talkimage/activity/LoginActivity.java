package com.chen.cy.talkimage.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chen.cy.talkimage.R;
import com.chen.cy.talkimage.entity.MyUser;
import com.chen.cy.talkimage.views.BaseSwipeBackActivity;
import com.chen.cy.talkimage.views.dialog.MyProgressDialog;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends BaseSwipeBackActivity {

    @Bind(R.id.login_toolbar)
    Toolbar loginToolbar;
    @Bind(R.id.login_username_edit)
    EditText loginUsernameEdit;
    @Bind(R.id.login_username_layout)
    TextInputLayout loginUsernameLayout;
    @Bind(R.id.login_passworld_edit)
    EditText loginPassworldEdit;
    @Bind(R.id.login_passworld_layout)
    TextInputLayout loginPassworldLayout;
    @Bind(R.id.login_forgetPassworld)
    TextView loginForgetPassworld;
    @Bind(R.id.login_loginBtn)
    Button loginLoginBtn;

    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        init();

    }

    private void init() {
        initToolbar();
        initClick();
    }

    private void initClick() {
        loginLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginUser();
            }
        });
    }

    private void LoginUser() {
        dialog = MyProgressDialog.createLoadingDialog(LoginActivity.this,"登录中...");
        dialog.show();
        MyUser user = new MyUser();
        user.setUsername(loginUsernameEdit.getText().toString());
        user.setPassword(loginPassworldEdit.getText().toString());
        user.login(getApplicationContext(), new SaveListener() {
            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                finish();
            }

            @Override
            public void onFailure(int i, String s) {
                dialog.dismiss();
                Toast.makeText(LoginActivity.this, "登录失败"+s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initToolbar() {
        loginToolbar.setNavigationIcon(R.mipmap.back);
        loginToolbar.setTitle("");
        setSupportActionBar(loginToolbar);
        loginToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.login_register) {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
