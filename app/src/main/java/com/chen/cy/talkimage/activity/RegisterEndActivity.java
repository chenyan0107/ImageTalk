package com.chen.cy.talkimage.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.chen.cy.talkimage.R;
import com.chen.cy.talkimage.entity.MyUser;
import com.chen.cy.talkimage.views.BaseSwipeBackActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.listener.SaveListener;

public class RegisterEndActivity extends BaseSwipeBackActivity {

    @Bind(R.id.register_end_toolbar)
    Toolbar registerEndToolbar;
    @Bind(R.id.register_passworld_edit)
    EditText registerPassworldEdit;
    @Bind(R.id.register_passworld_layout)
    TextInputLayout registerPassworldLayout;
    @Bind(R.id.register_ispassworld_edit)
    EditText registerIspassworldEdit;
    @Bind(R.id.register_ispassworld_layout)
    TextInputLayout registerIspassworldLayout;
    @Bind(R.id.register_registerEndBtn)
    Button registerRegisterEndBtn;

    private String phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_end);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        initToolbar();
        initData();
        initView();
        initClick();
    }

    private void initData() {
        phone = getIntent().getStringExtra("phone");
    }

    private void initToolbar() {
        registerEndToolbar.setNavigationIcon(R.mipmap.back);
        registerEndToolbar.setTitle("");
        setSupportActionBar(registerEndToolbar);
        registerEndToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 初始化界面
     */
    private void initView() {

    }

    /**
     * 初始化按钮与输入框监听事件
     */
    private void initClick() {
        registerPassworldEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() < 8){
                    registerPassworldLayout.setError("密码必须大于8位");
                }
                else {
                    registerPassworldLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        registerIspassworldEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!(registerPassworldEdit.getText().toString().equals(registerIspassworldEdit.getText().toString()))){
                    registerIspassworldLayout.setError("与密码必须一致");
                }
                else{
                    registerIspassworldLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        registerRegisterEndBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean isEqule = (registerPassworldEdit.getText().toString().equals(registerIspassworldEdit.getText().toString()));
                if ((registerPassworldEdit.getText().toString().length() >= 8) && isEqule){
                    signUpUser();
                    Toast.makeText(RegisterEndActivity.this, "电话号码"+phone, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void signUpUser() {
        final MyUser user = new MyUser();
        user.setUsername(phone);
        user.setPassword(registerPassworldEdit.getText().toString());
        user.setMobilePhoneNumber(phone);
        user.setMobilePhoneNumberVerified(true);
        user.setUserSex(false);
        user.setUserIntName(phone);
        user.signUp(this, new SaveListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(RegisterEndActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                user.login(getApplicationContext(), new SaveListener() {
                    @Override
                    public void onSuccess() {
                        Intent intent = new Intent(RegisterEndActivity.this, MainActivity.class);
                        intent.putExtra("phone", phone);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onFailure(int i, String s) {

                    }
                });
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(RegisterEndActivity.this, "注册失败"+s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register_end, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }
}
