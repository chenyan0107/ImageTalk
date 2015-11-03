package com.chen.cy.talkimage.activity.ItemInsert;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chen.cy.talkimage.R;
import com.chen.cy.talkimage.utils.ImageUtils;
import com.chen.cy.talkimage.utils.RecordUtils;
import com.chen.cy.talkimage.views.BaseSwipeBackActivity;
import com.chen.cy.talkimage.views.Fab;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RecordActivity extends BaseSwipeBackActivity {

    @Bind(R.id.record_toolbar)
    Toolbar recordToolbar;
    @Bind(R.id.activity_record_img)
    ImageView activityRecordImg;
    @Bind(R.id.record_dialog_img)
    ImageView recordDialogImg;
    @Bind(R.id.record_dialog_text)
    TextView recordDialogText;
    @Bind(R.id.record_dialoglayout)
    LinearLayout recordDialoglayout;
    @Bind(R.id.record_fab_record)
    Fab recordFABRecord;
    @Bind(R.id.record_fab_stop)
    Fab recordFABStop;
    @Bind(R.id.record_root)
    RelativeLayout recordRoot;
    @Bind(R.id.activity_record_play)
    ImageView activityRecordPlay;

    private Uri imageUri;
    private File recordFile;

    private RecordUtils recordUtils;
    private MediaPlayer mediaPlayer;

    private Timer timer;
    private TimerTask task;
    private int time;

    private boolean isPlay;
    private String recordTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        ButterKnife.bind(this);
        Log.e("record", "imageUri=" + imageUri);
        init();
    }

    private void init() {
        initData();
        initToolbar();
        initView();
        initClick();
    }

    private void initData() {
        imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "italk/" + ImageUtils.IMAGE_FILE_NAME));
        recordFile = new File(Environment.getExternalStorageDirectory().toString(), "italk/rec/"+RecordUtils.RECORD_FILE_NAME);//创建自已项目 文件夹
        recordUtils = new RecordUtils(recordFile, recordDialogImg);
        isPlay = false;
    }

    private void initView() {
        activityRecordImg.setImageDrawable(Drawable.createFromPath(imageUri.getPath()));
    }

    private void initClick() {
        recordFABRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordDialoglayout.setVisibility(View.VISIBLE);
                beginRecord();
                recordFABRecord.hide();
                recordFABStop.show();
            }
        });
        recordFABStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordDialoglayout.setVisibility(View.GONE);
                stopRecord();
                recordFABStop.hide();
                recordFABRecord.show();
            }
        });
    }

    private void stopRecord() {
        long recordTime = recordUtils.stopRecord();
        timer.cancel();
        task.cancel();
        String endTime = FormatData((int) (recordTime / 1000));
        Toast.makeText(getApplicationContext(), "录制时间为" + endTime, Toast.LENGTH_SHORT).show();
        activityRecordPlay.setVisibility(View.VISIBLE);
        activityRecordPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlay) {
                    StopPlayRecord();
                    activityRecordPlay.setImageResource(R.mipmap.item_play);
                    isPlay = false;
                } else {
                    PlayRecord();
                    activityRecordPlay.setImageResource(R.mipmap.item_pause);
                    isPlay = true;
                }
            }
        });
    }

    private void PlayRecord() {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(recordFile.getAbsolutePath());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                activityRecordPlay.setImageResource(R.mipmap.item_play);
                isPlay = false;
            }
        });
    }

    private void StopPlayRecord(){
        mediaPlayer.pause();
    }

    private void beginRecord() {
        recordUtils.startRecord();
        beginTimer();
    }

    private void initToolbar() {
        recordToolbar.setNavigationIcon(R.mipmap.back);
        recordToolbar.setTitle("");
        recordToolbar.setTitleTextAppearance(this, R.style.ToolbarTitleAppearance);
        setSupportActionBar(recordToolbar);
        recordToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_record, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.record_next:
                Intent intent = new Intent(RecordActivity.this,InsertItalkActivity.class);
                intent.putExtra("time",recordDialogText.getText().toString());
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void beginTimer() {
        time = 0;
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {

                runOnUiThread(new Runnable() {      // UI thread
                    @Override
                    public void run() {
                        time++;
                        recordDialogText.setText(FormatData(time));
                    }
                });
            }
        };
        timer.schedule(task, 0, 1000);
    }

    /**
     * 把整数转化成时间格式
     *
     * @param time
     * @return
     */
    private String FormatData(int time) {
        String dataM;
        String dataS;
        int m = 0;
        int s = 0;
        m = time / 60;
        s = time % 60;
        dataM = m + "";
        dataS = s + "";
        if (m < 10) {
            dataM = "0" + m;
        }
        if (s < 10) {
            dataS = "0" + s;
        }
        return dataM + ":" + dataS;
    }
}
