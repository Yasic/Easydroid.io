package com.example.esir.enotepad;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by ESIR on 2015/8/30.
 */
public class ReminderAlarm_Service extends Service{
    //定义浮动窗口布局
    RelativeLayout mFloatLayout;
    WindowManager.LayoutParams wmParams;
    WindowManager mWindowManager;
    Button floatwindowbutton;
    private MediaPlayer alarmMusic;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate(){
        Log.i("测试","onCreate");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent,int flags,int startId){
        Log.i("测试","onStatrtCommand");
        alarmMusic = MediaPlayer.create(ReminderAlarm_Service.this,
                RingtoneManager.getActualDefaultRingtoneUri(getApplicationContext(), RingtoneManager.TYPE_RINGTONE));
        alarmMusic.setLooping(true);
        alarmMusic.start();

        wmParams = new WindowManager.LayoutParams();
        mWindowManager = (WindowManager)getApplication().getSystemService(getApplication().WINDOW_SERVICE);
        //访问service提供的服务
        wmParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;//
        //设置图片格式，效果为背景透明
        wmParams.format = PixelFormat.TRANSLUCENT;
        //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        //调整悬浮窗显示的停靠位置为左侧置顶
        wmParams.gravity = Gravity.BOTTOM;
        // 以屏幕左上角为原点，设置x、y初始值，相对于gravity
        wmParams.x = 0;
        wmParams.y = 0;

        //设置悬浮窗口长宽数据
        wmParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        //wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //wmParams.width = dp2pix(48);
        wmParams.height = dp2pix(210);

        LayoutInflater inflater = LayoutInflater.from(getApplication());
        mFloatLayout = (RelativeLayout) inflater.inflate(R.layout.reminderalarm_floatwindow, null);
        mWindowManager.addView(mFloatLayout, wmParams);
        floatwindowbutton = (Button)mFloatLayout.findViewById(R.id.reminderalarm_okbutton);
        Bundle bundle = intent.getExtras();
        TextView reminderalarm_title = (TextView)mFloatLayout.findViewById(R.id.reminderalarm_title);
        reminderalarm_title.setText(bundle.get("Reminder_Title").toString());
        TextView reminderalarm_description = (TextView)mFloatLayout.findViewById(R.id.reminderalarm_description);
        reminderalarm_description.setText(bundle.get("Reminder_Description").toString());
        deleteReminderDB(bundle);
        floatwindowbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alarmMusic.stop();
                mWindowManager.removeView(mFloatLayout);
                Intent intent = new Intent(ReminderAlarm_Service.this,ReminderAlarm_Service.class);
                stopService(intent);
            }
        });

        return super.onStartCommand(intent,flags,startId);
    }

    public void deleteReminderDB(Bundle bundle){
        ENoteSQLitedbhelperforrenminder eNoteSQLitedbhelperforrenminder = new ENoteSQLitedbhelperforrenminder(getApplicationContext(),"ENOTE",1);
        eNoteSQLitedbhelperforrenminder.getWritableDatabase().delete("REMINDER","TIME = ?",new String[]{bundle.getString("Reminder_Time")});
        eNoteSQLitedbhelperforrenminder.close();
    }

    private int dp2pix(int dp){
        final float scale = getResources().getDisplayMetrics().density;
        int pix = (int) (dp * scale + 0.5f);
        return pix;
    }
}
