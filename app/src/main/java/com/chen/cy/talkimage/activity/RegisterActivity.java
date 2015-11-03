package com.chen.cy.talkimage.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.chen.cy.talkimage.R;
import com.chen.cy.talkimage.views.BaseActivity;
import com.chen.cy.talkimage.views.BaseSwipeBackActivity;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.exception.BmobException;
import cn.bmob.sms.listener.RequestSMSCodeListener;
import cn.bmob.sms.listener.VerifySMSCodeListener;

public class RegisterActivity extends BaseSwipeBackActivity implements View.OnClickListener {


    @Bind(R.id.register_toolbar)
    Toolbar registerToolbar;
    @Bind(R.id.register_username_edit)
    EditText registerUsernameEdit;
    @Bind(R.id.register_username_layout)
    TextInputLayout registerUsernameLayout;
    @Bind(R.id.register_sms_edit)
    EditText registerSmsEdit;
    @Bind(R.id.register_sms_layout)
    TextInputLayout registerSmsLayout;
    @Bind(R.id.register_sms)
    Button registerSms;
    @Bind(R.id.register_registerBtn)
    Button registerRegisterBtn;
    @Bind(R.id.register_base_layout)
    RelativeLayout registerBaseLayout;

    private String phone;
    private String code;

    private TimerTask task;
    private Timer timer;
    private int time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        //初始化Bmob短信服务
        BmobSMS.initialize(getApplicationContext(), "23d44345606b136ec883445e7c5ffc60");
        init();
    }

    private void init() {
        initToolbar();
        initView();
        initListener();
    }

    private void initView() {

    }

    private void initToolbar() {
        registerToolbar.setNavigationIcon(R.mipmap.back);
        registerToolbar.setTitle("");
        setSupportActionBar(registerToolbar);
        registerToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initListener() {
        registerUsernameEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 11) {
                    registerSms.setClickable(true);
                    registerSms.setBackgroundResource(R.drawable.login_btn_defalut);
                    registerSms.setOnClickListener(RegisterActivity.this);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        registerSmsEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 6) {
                    registerRegisterBtn.setClickable(true);
                    registerRegisterBtn.setBackgroundResource(R.drawable.login_btn_defalut);
                    registerRegisterBtn.setOnClickListener(RegisterActivity.this);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.register_sms:
                //发送短信
                phone = registerUsernameEdit.getText().toString();
                Toast.makeText(RegisterActivity.this, "短信已发送", Toast.LENGTH_SHORT).show();
                beginTimer();
//                sendSMS(phone);
                break;
            case R.id.register_registerBtn:
                code = registerSmsEdit.getText().toString();
//                if (phone != null && code != null) {
//                    verifySMS(phone, code);
//                }
                Intent intent = new Intent(RegisterActivity.this, RegisterEndActivity.class);
                intent.putExtra("phone", phone);
                startActivity(intent);
                finish();
                break;
        }
    }

    private void sendSMS(String phone) {
        BmobSMS.requestSMSCode(getApplicationContext(), phone, "ImageTalkSMS", new RequestSMSCodeListener() {

            @Override
            public void done(Integer smsId, BmobException ex) {
                // TODO Auto-generated method stub
                if (ex == null) {//验证码发送成功
                    Log.i("bmob", "短信id：" + smsId);//用于查询本次短信发送详情
                }
            }
        });
    }

    private void verifySMS(String phone, String Code) {
        BmobSMS.verifySmsCode(getApplicationContext(), phone, Code, new VerifySMSCodeListener() {

            @Override
            public void done(BmobException ex) {
                // TODO Auto-generated method stub
                if (ex == null) {//短信验证码已验证成功
                    Log.i("bmob", "验证通过");
                    Intent intent = new Intent(RegisterActivity.this, RegisterEndActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Log.i("bmob", "验证失败：code =" + ex.getErrorCode() + ",msg = " + ex.getLocalizedMessage());
                    Toast.makeText(RegisterActivity.this, "验证失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    protected void beginTimer() {
        time = 60;
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {

                runOnUiThread(new Runnable() {      // UI thread
                    @Override
                    public void run() {
                        time--;
                        registerSms.setText(time + "s后可重发");
                        registerSms.setBackgroundResource(R.drawable.login_btn_unfocuse);
                        registerSms.setEnabled(false);
                        if(time < 0){
                            timer.cancel();
                            task.cancel();
                            registerSms.setText("重发");
                            registerSms.setBackgroundResource(R.drawable.login_btn_defalut);
                            registerSms.setEnabled(true);
                        }
                    }
                });
            }
        };
        timer.schedule(task, 0, 1000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
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
