package com.chen.cy.talkimage.activity.usersetting;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bmob.btp.callback.UploadListener;
import com.chen.cy.talkimage.R;
import com.chen.cy.talkimage.entity.MyUser;
import com.chen.cy.talkimage.utils.CheckUtils;
import com.chen.cy.talkimage.utils.ImageUtils;
import com.chen.cy.talkimage.utils.MyBmobUtils;
import com.chen.cy.talkimage.views.BaseSwipeBackActivity;
import com.chen.cy.talkimage.views.dialog.EditDialog;
import com.chen.cy.talkimage.views.dialog.MyProgressDialog;
import com.chen.cy.talkimage.views.dialog.SelectDialog;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UpdateListener;

public class UserSettingActivity extends BaseSwipeBackActivity{
    private ImageUtils imageUtils;

    @Bind(R.id.user_setting_toolbar)
    Toolbar userSettingToolbar;
    @Bind(R.id.user_setting_img_textview)
    TextView userSettingImgTextview;
    @Bind(R.id.user_setting_img_imageview)
    ImageView userSettingImgImageview;
    @Bind(R.id.user_setting_img_moreinfo)
    ImageView userSettingImgMoreinfo;
    @Bind(R.id.user_setting_img)
    RelativeLayout userSettingImg;
    @Bind(R.id.user_setting_name_textview)
    TextView userSettingNameTextview;
    @Bind(R.id.user_setting_name_textview2)
    TextView userSettingNameTextview2;
    @Bind(R.id.user_setting_name_moreinfo)
    ImageView userSettingNameMoreinfo;
    @Bind(R.id.user_setting_name)
    RelativeLayout userSettingName;
    @Bind(R.id.user_setting_id_textview)
    TextView userSettingIdTextview;
    @Bind(R.id.user_setting_id_textview2)
    TextView userSettingIdTextview2;
    @Bind(R.id.user_setting_id)
    RelativeLayout userSettingId;
    @Bind(R.id.user_setting_sex_textview)
    TextView userSettingSexTextview;
    @Bind(R.id.user_setting_sex_textview2)
    TextView userSettingSexTextview2;
    @Bind(R.id.user_setting_sex_moreinfo)
    ImageView userSettingSexMoreinfo;
    @Bind(R.id.user_setting_img_sex)
    RelativeLayout userSettingImgSex;
    @Bind(R.id.user_setting_address_textview)
    TextView userSettingAddressTextview;
    @Bind(R.id.user_setting_address_textview2)
    TextView userSettingAddressTextview2;
    @Bind(R.id.user_setting_address_moreinfo)
    ImageView userSettingAddressMoreinfo;
    @Bind(R.id.user_setting_address)
    RelativeLayout userSettingAddress;
    @Bind(R.id.user_setting_signature_textview)
    TextView userSettingSignatureTextview;
    @Bind(R.id.user_setting_signature_textview2)
    TextView userSettingSignatureTextview2;
    @Bind(R.id.user_setting_signature_moreinfo)
    ImageView userSettingSignatureMoreinfo;
    @Bind(R.id.user_setting_signature)
    RelativeLayout userSettingSignature;
    @Bind(R.id.user_setting_logout_text)
    TextView userSettingLogoutText;
    @Bind(R.id.user_setting_logout)
    RelativeLayout userSettingLogout;

    private Uri imageUri;
    private MyUser myUser;

    private Dialog dialog;
    private MyBmobUtils myBmobUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);
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
        imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "italk/" + imageUtils.IMAGE_FILE_NAME));
        imageUtils = ImageUtils.getInstance();
        imageUtils.setOutput_X(480);
        imageUtils.setOutput_Y(480);
        imageUtils.setCropX(1);
        imageUtils.setCropY(1);
        myUser = BmobUser.getCurrentUser(this, MyUser.class);
        myBmobUtils = MyBmobUtils.getInstance();
    }

    private void initView() {
        if (myUser != null) {
            ImageLoader imagloader = ImageLoader.getInstance();
            if (myUser.getUserImg() != null)
                imagloader.displayImage(myUser.getUserImg().getUrl(), userSettingImgImageview);
            userSettingIdTextview2.setText(myUser.getUsername());
            if (myUser.getUserIntName() != null)
                userSettingNameTextview2.setText(myUser.getUserIntName());
            if (myUser.getUserSex() != null) {
                if (myUser.getUserSex()) {
                    userSettingSexTextview2.setText("男");
                } else {
                    userSettingSexTextview2.setText("女");
                }
            }
            if (myUser.getUserAddress() != null)
                userSettingAddressTextview2.setText(myUser.getUserAddress());
            if (myUser.getUserSignature() != null)
                userSettingSignatureTextview2.setText(myUser.getUserSignature());
        }
    }

    private void initClick() {
        userSettingImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageUtils.chooseImage(UserSettingActivity.this);
            }
        });
        userSettingName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyName();
            }
        });
        userSettingImgSex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifySex();
            }
        });
        userSettingAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyAddress();
            }
        });
        userSettingSignature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifySignature();
            }
        });
        userSettingLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogOut();
            }
        });
    }

    private void modifySignature() {
        final EditDialog signatureDialog;
        signatureDialog = new EditDialog(UserSettingActivity.this, R.style.edit_dialog, "请输入个性签名");
        signatureDialog.setDialogClick(new EditDialog.DialogClick() {
            @Override
            public void yesClick(final String signature) {
                MyUser tempUser = new MyUser();
                tempUser.setUserSignature(signature);
                dialog = MyProgressDialog.createLoadingDialog(UserSettingActivity.this, "修改中...");
                dialog.show();
                myBmobUtils.updateUser(myUser, tempUser, getApplicationContext(), new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        signatureDialog.dismiss();
                        userSettingSignatureTextview2.setText(signature);
                    }

                    @Override
                    public void onFailure(int i, String s) {

                    }
                });
                dialog.dismiss();
            }

            @Override
            public void cancelClick() {
                signatureDialog.dismiss();
            }
        });
        signatureDialog.show();
    }

    private void modifyAddress() {
        final EditDialog addressDialog;
        addressDialog = new EditDialog(UserSettingActivity.this, R.style.edit_dialog, "请输入地址");
        addressDialog.setDialogClick(new EditDialog.DialogClick() {
            @Override
            public void yesClick(final String address) {
                MyUser tempUser = new MyUser();
                tempUser.setUserAddress(address);
                dialog = MyProgressDialog.createLoadingDialog(UserSettingActivity.this, "修改中...");
                dialog.show();
                myBmobUtils.updateUser(myUser, tempUser, getApplicationContext(), new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        addressDialog.dismiss();
                        userSettingAddressTextview2.setText(address);
                    }

                    @Override
                    public void onFailure(int i, String s) {

                    }
                });
                dialog.dismiss();
            }

            @Override
            public void cancelClick() {
                addressDialog.dismiss();
            }
        });
        addressDialog.show();
    }

    private void modifySex() {
        final SelectDialog sexDialog = new SelectDialog(UserSettingActivity.this, R.style.edit_dialog, R.layout.dialog_select);
        sexDialog.setSelectDialogClick(new SelectDialog.SelectDialogClick() {
            @Override
            public void yesClick(int checkedID) {
                MyUser tempUser = new MyUser();
                dialog = MyProgressDialog.createLoadingDialog(UserSettingActivity.this, "修改中...");
                dialog.show();
                if (checkedID == R.id.dialog_select_man) {
                    tempUser.setUserSex(true);
                    myBmobUtils.updateUser(myUser, tempUser, getApplicationContext(), new UpdateListener() {
                        @Override
                        public void onSuccess() {
                            userSettingSexTextview2.setText("男");
                            sexDialog.dismiss();
                        }

                        @Override
                        public void onFailure(int i, String s) {

                        }
                    });
                } else {
                    tempUser.setUserSex(false);
                    myBmobUtils.updateUser(myUser, tempUser, getApplicationContext(), new UpdateListener() {
                        @Override
                        public void onSuccess() {
                            userSettingSexTextview2.setText("女");
                            sexDialog.dismiss();
                        }

                        @Override
                        public void onFailure(int i, String s) {

                        }
                    });
                }
                dialog.dismiss();
            }

            @Override
            public void cancelClick() {
                sexDialog.dismiss();
            }
        });
        sexDialog.show();
    }

    private void modifyName() {
        final EditDialog nameSettingDialog;
        nameSettingDialog = new EditDialog(UserSettingActivity.this, R.style.edit_dialog, "请输入用户名");
        nameSettingDialog.setDialogClick(new EditDialog.DialogClick() {
            @Override
            public void yesClick(final String name) {
                MyUser tempUser = new MyUser();
                tempUser.setUserIntName(name);
                dialog = MyProgressDialog.createLoadingDialog(UserSettingActivity.this, "修改中...");
                dialog.show();
                myBmobUtils.updateUser(myUser, tempUser, getApplicationContext(), new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        userSettingNameTextview2.setText(name);
                        nameSettingDialog.dismiss();
                    }

                    @Override
                    public void onFailure(int i, String s) {

                    }
                });
                dialog.dismiss();
            }

            @Override
            public void cancelClick() {
                nameSettingDialog.dismiss();
            }
        });
        nameSettingDialog.show();
    }

    //用户退出登录
    private void LogOut() {
        BmobUser.logOut(this);  //清除缓存用户对象
        finish();
    }

    private void initToolbar() {
        userSettingToolbar.setNavigationIcon(R.mipmap.back);
        userSettingToolbar.setTitle("");
        setSupportActionBar(userSettingToolbar);
        userSettingToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {

        // 用户没有进行有效的设置操作，返回
        if (resultCode == RESULT_CANCELED) {
            Toast.makeText(getApplication(), "取消", Toast.LENGTH_LONG).show();
            return;
        }

        switch (requestCode) {
            case ImageUtils.CODE_GALLERY_REQUEST:
                imageUtils.cropRawPhoto(intent.getData(), UserSettingActivity.this);
//                cropRawPhoto(intent.getData());
                break;

            case ImageUtils.CODE_CAMERA_REQUEST:
                if (CheckUtils.hasSdcard()) {
                    File tempFile = new File(
                            Environment.getExternalStorageDirectory(),
                            ImageUtils.IMAGE_FILE_NAME);
//                    cropRawPhoto(Uri.fromFile(tempFile));
                    imageUtils.cropRawPhoto(Uri.fromFile(tempFile), UserSettingActivity.this);
                } else {
                    Toast.makeText(getApplication(), "没有SDCard!", Toast.LENGTH_LONG)
                            .show();
                }

                break;

            case ImageUtils.CODE_RESULT_REQUEST:
                if (intent != null) {
                    setImageToHeadView(intent);
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, intent);
    }

    /**
     * 提取保存裁剪之后的图片数据，并设置头像部分的View
     */
    private void setImageToHeadView(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            dialog = MyProgressDialog.createLoadingDialog(UserSettingActivity.this, "上传头像中...");
            dialog.show();
            userSettingImgImageview.setImageDrawable(Drawable.createFromPath(imageUri.getPath()));
//            uploadImg(imageUri.getPath());
            myBmobUtils.uploadImg(imageUri.getPath(), getApplicationContext(), myUser, new UploadListener() {
                @Override
                public void onSuccess(String s, String s1, BmobFile bmobFile) {
                    dialog.dismiss();
                }

                @Override
                public void onProgress(int i) {

                }

                @Override
                public void onError(int i, String s) {
                    dialog.dismiss();
                }
            });
        }
    }
}
