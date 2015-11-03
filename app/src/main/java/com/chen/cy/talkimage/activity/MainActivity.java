package com.chen.cy.talkimage.activity;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.chen.cy.talkimage.R;
import com.chen.cy.talkimage.activity.usersetting.UserSettingActivity;
import com.chen.cy.talkimage.baseUtils.L;
import com.chen.cy.talkimage.baseUtils.ScreenUtils;
import com.chen.cy.talkimage.entity.MyUser;
import com.chen.cy.talkimage.frame.FrendsFrame;
import com.chen.cy.talkimage.frame.HomeBaseFragment;
import com.chen.cy.talkimage.frame.MessageFrame;
import com.chen.cy.talkimage.frame.SettingFrame;
import com.chen.cy.talkimage.message.entity.MyPublishEvent;
import com.chen.cy.talkimage.utils.MyImageLoaderUtils;
import com.chen.cy.talkimage.views.BaseActivity;
import com.chen.cy.talkimage.views.Fab;
import com.chen.cy.talkimage.views.RoundImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;
import de.greenrobot.event.EventBus;

/**
 * 主界面
 */
public class MainActivity extends BaseActivity {
    public static final String ACTION_SHOW_LOADING_ITEM = "action_show_loading_item";

    @Bind(R.id.main_toolbar)
    Toolbar mainToolbar;
    @Bind(R.id.id_img)
    RoundImageView idImg;
    @Bind(R.id.id_username)
    TextView idUserName;
    @Bind(R.id.id_link)
    TextView idLink;
    @Bind(R.id.main_framelayout)
    FrameLayout mainFramelayout;
    @Bind(R.id.navigationView)
    NavigationView navigationView;
    @Bind(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @Bind(R.id.floatingActionButton)
    Fab fab;
    //碎片管理器
    private FragmentManager fragmentManager;

    //Fragment页面
    private HomeBaseFragment homeFrame;
    private MessageFrame messageFrame;
    private FrendsFrame frendsFrame;
    private SettingFrame settingFrame;

    private MyUser myUser;

    ImageLoader imageLoader;

    private int ScreenHeight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        initData();
        initToolbar();
        initFrame();
        initDrawer();
        initView();
    }

    private void initData() {
        MyImageLoaderUtils.initImageLoader(this);
        imageLoader = ImageLoader.getInstance();
        myUser = BmobUser.getCurrentUser(MainActivity.this, MyUser.class);
        ScreenHeight = ScreenUtils.getScreenHeight(getApplicationContext());
    }

    private void initView() {
        if (myUser != null) {
            if (myUser.getUserImg() != null) {
                imageLoader.displayImage(myUser.getUserImg().getUrl(), idImg);
            }
            if (myUser.getUserIntName() != null)
                idUserName.setText(myUser.getUserIntName());
            if (myUser.getUserSignature() != null)
                idLink.setText(myUser.getUserSignature());
        } else {
            idImg.setImageResource(R.mipmap.id_img);
            idUserName.setText("请登录");
            idLink.setText("");
        }
    }


    /**
     * 初始化Frame
     */
    private void initDrawer() {
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem menuItem) {
                    //切换相应 Fragment 等操作
                    switch (menuItem.getItemId()) {
                        case R.id.nav_home:
                            setFragmentSelect(0);
                            break;
                        case R.id.nav_messages:
                            setFragmentSelect(1);
                            break;
                        case R.id.nav_friends:
                            setFragmentSelect(2);
                            break;
                        case R.id.nav_setting:
                            setFragmentSelect(3);
                            break;
                    }
                    menuItem.setChecked(true);
                    drawerLayout.closeDrawers();
                    return false;
                }
            });
        }

        idImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myUser != null) {
                    Intent intent = new Intent(MainActivity.this, UserSettingActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    /**
     * 初始化toolbar
     */
    private void initToolbar() {
        mainToolbar.setNavigationIcon(R.mipmap.menu);
        mainToolbar.setTitle("图说");
        //设置Title样式
        mainToolbar.setTitleTextAppearance(this, R.style.ToolbarTitleAppearance);
        mainToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(navigationView);
            }
        });
    }

    private void initFrame() {
        //碎片管理器实例化
        fragmentManager = getSupportFragmentManager();
        //以第一个页面为默认页面
        setFragmentSelect(0);
    }

    /**
     * 设置Fragment的状态
     *
     * @param index 页面在管理器中的位置
     */
    private void setFragmentSelect(int index) {
        //开启fragment事物
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        hidenFragment(fragmentTransaction);
        switch (index) {
            case 0:
                if (homeFrame == null) {
                    homeFrame = new HomeBaseFragment();
                    fragmentTransaction.add(R.id.main_framelayout, homeFrame);
                } else {
                    fragmentTransaction.show(homeFrame);
                }
                break;
            case 1:
                if (messageFrame == null) {
                    messageFrame = new MessageFrame();
                    fragmentTransaction.add(R.id.main_framelayout, messageFrame);
                } else {
                    fragmentTransaction.show(messageFrame);
                }
                break;
            case 2:
                if (frendsFrame == null) {
                    frendsFrame = new FrendsFrame();
                    fragmentTransaction.add(R.id.main_framelayout, frendsFrame);
                } else {
                    fragmentTransaction.show(frendsFrame);
                }
                break;
            case 3:
                if (settingFrame == null) {
                    settingFrame = new SettingFrame();
                    fragmentTransaction.add(R.id.main_framelayout, settingFrame);
                } else {
                    fragmentTransaction.show(settingFrame);
                }
                break;
        }
        fragmentTransaction.commit();
    }

    /**
     * 隐藏已经加载了的Fragment
     *
     * @param fragmentTransaction 操作Fragment的事务
     */
    private void hidenFragment(FragmentTransaction fragmentTransaction) {
        if (homeFrame != null)
            fragmentTransaction.hide(homeFrame);
        if (messageFrame != null)
            fragmentTransaction.hide(messageFrame);
        if (frendsFrame != null)
            fragmentTransaction.hide(frendsFrame);
        if (settingFrame != null)
            fragmentTransaction.hide(settingFrame);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        myUser = BmobUser.getCurrentUser(this, MyUser.class);
        initView();
        super.onResume();
    }

    @Override
    public void onClick(View v) {

    }

    public void showFab(){
        fab.show();
    }

    public void hideFab(){
        fab.hide();
    }

    public void TranslationFab(float x, float y){
        fab.setTranslation(x, y);
    }
    public void TranslationFabOutScreen(){
        float fabY = fab.getY();
        Log.e("ONSCROLL","ScreenHeight="+ScreenHeight+",fabY="+fabY);
        float moveY = ScreenHeight - fabY;
        TranslationFab(0f, moveY);
    }

    public void TranslationFabInScreen(){
        float fabY = fab.getY();
        Log.e("ONSCROLL","height="+ScreenHeight+",fabY="+fabY);
        float moveY = ScreenHeight - fabY;
        TranslationFab(0f, -moveY);
    }

    public Fab getFab(){
        return fab;
    }


    @Override
    public void onAttachFragment(Fragment fragment) {
        L.e("fragment");
        super.onAttachFragment(fragment);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (ACTION_SHOW_LOADING_ITEM.equals(intent.getAction())) {
            Log.e("bmob", "showFeedLoadingItemDelayed()");
            showFeedLoadingItemDelayed(intent.getStringExtra("title"));
        }
    }

    private void showFeedLoadingItemDelayed(String title) {
        Log.e("bmob", "showFeedLoadingItemDelayed()");
        EventBus.getDefault().post(new MyPublishEvent(ACTION_SHOW_LOADING_ITEM, title));
    }
}
