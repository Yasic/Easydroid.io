package com.easydroid.esir.easydroid;

import android.animation.ObjectAnimator;
import android.app.ActivityManager;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.IBinder;
import android.os.Message;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.internal.view.menu.MenuWrapperFactory;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
//import java.util.logging.Handler;
import android.os.Handler;

/**
 * Created by ESIR on 2015/7/25.
 */
public class FloatWindowService extends Service implements View.OnClickListener {

    //定义浮动窗口布局
    public LinearLayout float_window_small;
    private RelativeLayout float_window_menu;
    WindowManager.LayoutParams wmParams;
    WindowManager.LayoutParams menuParams;
    int tempx,tempy;
    //创建浮动窗口设置布局参数的对象
    WindowManager mWindowManager;
    WifiManager wifimanager;
    BluetoothAdapter bluetoothadapter;
    AudioManager audiomanager;
    Button floatwindowbutton , home_button , wifi_button , connectivity_button , airplane_button , camera_button;
    Button bluetooth_button,ring_button,message_button,call_button;
    private boolean clickflag;
    public int system_version;
    public int sdk_version;
    private Camera camera;
    private Message msg = new Message();
    TimerTask timerTask;
    Timer timer;

    Handler handler = new Handler(){
      @Override
    public void handleMessage(Message msg){
          switch (msg.what){
              case 0:
                  //WIFI_STATE_DISABLING
                  //Log.i("wifi","WIFI_STATE_DISABLING");
                  wifi_button.setBackground(getApplication().getResources().getDrawable(R.drawable.ic_wifi_grey600_48dp));
                  break;
              case 1:
                  //WIFI_SDATE_DISABLED
                  //Log.i("wifi","WIFI_SDATE_DISABLED");
                  wifi_button.setBackground(getApplication().getResources().getDrawable(R.drawable.ic_wifi_grey600_48dp));//此方法只适用于sdk16以上版本
                  break;
              case 2:
                  //WIFI_STATE_ENABLING
                  //Log.i("wifi","WIFI_STATE_ENABLING");
                  wifi_button.setBackground(getApplication().getResources().getDrawable(R.drawable.ic_wifi_grey600_48dp));
                  break;
              case 3:
                  // WIFI_STATE_ENABLED
                  //Log.i("wifi","WIFI_STATE_ENABLED");
                  wifi_button.setBackground(getApplication().getResources().getDrawable(R.drawable.ic_wifi_white_48dp));
                  break;
              case 5:
                  //移动数据打开
                  connectivity_button.setBackground(getApplication().
                          getResources().
                          getDrawable(R.drawable.ic_swap_vertical_white_48dp));
                  break;
              case 6:
                  //移动数据关闭
                  connectivity_button.setBackground(getApplication().
                          getResources().
                          getDrawable(R.drawable.ic_swap_vertical_grey600_48dp));
                  break;
              case 7:
                  //蓝牙on
                  bluetooth_button.setBackground(getApplication().
                          getResources().
                          getDrawable(R.drawable.ic_bluetooth_white_48dp));
                  break;
              case 8:
                  //蓝牙off
                  bluetooth_button.setBackground(getApplication().
                          getResources().
                          getDrawable(R.drawable.ic_bluetooth_grey600_48dp));
                  break;
              case 9:
                  ring_button.setBackground(getApplication().getResources().getDrawable(R.drawable.ic_bell_grey600_48dp));
                  break;
              case 10:
                  ring_button.setBackground(getApplication().getResources().getDrawable(R.drawable.ic_bell_white_48dp));
                  break;
              case 11:
                  ring_button.setBackground(getApplication().getResources().getDrawable(R.drawable.ic_vibrate_white_48dp));
                  break;
              case 12:
                  airplane_button.setBackground(getApplication().getResources().getDrawable(R.drawable.ic_airplane_grey600_48dp));
                  break;
              case 13:
                  airplane_button.setBackground(getApplication().getResources().getDrawable(R.drawable.ic_airplane_white_48dp));
                  break;
              case 14:
                  float_window_menu.setVisibility(View.VISIBLE);
                  float_window_small.setVisibility(View.GONE);
                  break;
              case 15:
                  float_window_small.setVisibility(View.VISIBLE);
                  float_window_menu.setVisibility(View.GONE);
              default:
                  //Toast.makeText(getApplication(),"Mistake happened!",Toast.LENGTH_SHORT).show();
                  break;
          }
          //Log.i("msg.!!!!what", String.valueOf(msg.what));
      }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate(){
        super.onCreate();

        //Log.i("system",android.os.Build.MODEL+","+android.os.Build.VERSION.SDK+","+android.os.Build.VERSION.RELEASE);
        String temp[] = Build.VERSION.RELEASE.split("\\.");//.是转义字符，要加上//
        int system = Integer.parseInt(temp[0]);
        system_version = system;
        sdk_version = Build.VERSION.SDK_INT;
        //Log.i("sdk" , String.valueOf(sdk_version));//获取int型的版本号

        /*Notification notification = new Notification(R.drawable.ic_emoticon_devil_black_48dp,
                "easttouch后台运行", System.currentTimeMillis());
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);
        notification.setLatestEventInfo(this, "Easytouch of Yasic", "正在后台运行中", pendingIntent);
        startForeground(1, notification);*/
    }

    public void setbutton2view(){
        LayoutInflater inflater = LayoutInflater.from(getApplication());
        float_window_menu = (RelativeLayout)inflater.inflate(R.layout.float_window_menu , null);
        float_window_small = (LinearLayout) inflater.inflate(R.layout.float_window, null);

        floatwindowbutton = (Button)float_window_small.findViewById(R.id.floatwindowbutton);
        floatwindowbutton.setOnClickListener(this);
        home_button = (Button)float_window_menu.findViewById(R.id.home_button);
        home_button.setOnClickListener(this);
        wifi_button = (Button)float_window_menu.findViewById(R.id.wifi_button);
        wifi_button.setOnClickListener(this);
        connectivity_button = (Button)float_window_menu.findViewById(R.id.connectivity_button);
        connectivity_button.setOnClickListener(this);
        airplane_button = (Button)float_window_menu.findViewById(R.id.airplane_button);
        airplane_button.setOnClickListener(this);
        bluetooth_button = (Button)float_window_menu.findViewById(R.id.bluetooth_button);
        bluetooth_button.setOnClickListener(this);
        ring_button = (Button)float_window_menu.findViewById(R.id.ring_button);
        ring_button.setOnClickListener(this);
        camera_button = (Button)float_window_menu.findViewById(R.id.camera_button);
        camera_button.setOnClickListener(this);
        message_button = (Button)float_window_menu.findViewById(R.id.message_button);
        message_button.setOnClickListener(this);
        call_button = (Button)float_window_menu.findViewById(R.id.call_button);
        call_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.home_button:
                Log.i("fuck","fuck");
                intent = new Intent(Intent.ACTION_MAIN);
                intent.putExtra("GOHOME", "GOHOME");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
                try {
                    float_window_menu_animation(-1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.wifi_button:
                switch (wifimanager.getWifiState()) {
                    case 0:
                        //WIFI_STATE_DISABLING
                        break;
                    case 1:
                        //WIFI_SDATE_DISABLED
                        wifimanager.setWifiEnabled(true);//关闭wifi
                        break;
                    case 2:
                        //WIFI_STATE_ENABLING
                        break;
                    case 3:
                        // WIFI_STATE_ENABLED
                        wifimanager.setWifiEnabled(false);//开启wifi
                        break;
                    default:
                        Toast.makeText(getApplicationContext(), "WIFI Mistake happened!", Toast.LENGTH_SHORT).show();
                        break;
                }
                break;

            case R.id.connectivity_button:
                Boolean enabled = false;
                if(system_version < 5){
                    if(getMobileDataState()){

                    }
                    else{
                        enabled = true;
                    }
                    setMobileState(getApplicationContext(),enabled);
                }
                else{
                    intent = new Intent(Settings.ACTION_SETTINGS);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    try {
                        float_window_menu_animation(-1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                break;

            case R.id.airplane_button:
                ContentResolver cr = getContentResolver();
                if(system_version < 4.2){
                    if(Settings.System.getString(cr,Settings.System.AIRPLANE_MODE_ON).equals("0")){
                        //获取当前飞行模式状态,返回的是String值0,或1.0为关闭飞行,1为开启飞行
                        //如果关闭飞行,则打开飞行
                        Settings.System.putString(cr,Settings.System.AIRPLANE_MODE_ON, "1");
                        intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
                        intent.putExtra("Sponsor", "Sodino");
                        sendBroadcast(intent);
                    }else{
                        //否则关闭飞行
                        Settings.System.putString(cr,Settings.System.AIRPLANE_MODE_ON, "0");
                        intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
                        sendBroadcast(intent);
                    }
                }
                else{
                    intent = new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                try {
                    float_window_menu_animation(-1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.bluetooth_button:
                if(bluetoothadapter.isEnabled()){
                    bluetoothadapter.disable();//关闭蓝牙
                }
                else{
                    bluetoothadapter.enable();//打开蓝牙
                }
                break;

            case R.id.ring_button:
                if(audiomanager.getRingerMode() == AudioManager.RINGER_MODE_SILENT){
                    audiomanager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                }
                else if(audiomanager.getRingerMode() == AudioManager.RINGER_MODE_NORMAL){
                    audiomanager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                }
                else if(audiomanager.getRingerMode() == AudioManager.RINGER_MODE_VIBRATE){
                    /*Intent intent = new Intent(Settings.ACTION_SOUND_SETTINGS);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);*/
                    audiomanager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                }
                break;

            case R.id.camera_button:
                intent=new Intent();
                // 指定开启系统相机的Action
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                try {
                    float_window_menu_animation(-1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.message_button:
                intent = new Intent(Intent.ACTION_MAIN);
                intent.setType("vnd.android-dir/mms-sms");
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                try {
                    float_window_menu_animation(-1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.call_button:
                intent = new Intent(Intent.ACTION_DIAL);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                try {
                    float_window_menu_animation(-1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;

            default:
                break;
        }
    }

    @Override
    public int onStartCommand(Intent intent,int flags, final int startId){
        wmParams = new WindowManager.LayoutParams();
        mWindowManager = (WindowManager)getApplication().getSystemService(getApplication().WINDOW_SERVICE);
        //访问service提供的服务
        setbutton2view();//设置button绑定view
        float_window_menu.setVisibility(View.GONE);
        wifimanager = (WifiManager)this.getSystemService(Context.WIFI_SERVICE);//获取wifi服务
        refresh_wifistate();//周期更新wifiicon状态

        refresh_connectivity();//周期更新connecticon状态
        refresh_bluetooth();//刷新蓝牙icon状态
        refresh_ring();
        refresh_write();

        create_float_window();
        create_float_windowmenu();

        timerTask = new TimerTask() {
            @Override
            public void run() {
                wifi2icon();

                bluetoothadapter = BluetoothAdapter.getDefaultAdapter();
                bluetooth2icon();

                connecty2icon();

                ContentResolver cr = getContentResolver();
                write2icon(cr);

                audiomanager = (AudioManager)getApplication().getSystemService(Context.AUDIO_SERVICE);
                int state = audiomanager.getRingerMode();
                ring2icon(state);
            }
        };

        float_window_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    float_window_menu_animation(-1);
                    //stopinitButtonSetting(timer,timerTask);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        floatwindowbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickflag == true) {//不是onTouch事件
                    try {
                        float_window_menu_animation(1);
                        //timer = initButtonSetting(timer,timerTask);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        floatwindowbutton.setOnTouchListener(new View.OnTouchListener() {
            int lastx, lasty;
            int downx, downy;

            @Override
            public boolean onTouch(View v, final MotionEvent event) {
                // TODO Auto-generated method stub
                int eventaction = event.getAction();
                switch (eventaction) {
                    case MotionEvent.ACTION_DOWN:
                        lastx = (int) event.getRawX();
                        lasty = (int) event.getRawY();
                        downx = lastx;
                        downy = lasty;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (Math.abs((int) (event.getRawX() - downx)) > 8 || Math.abs((int) (event.getRawY() - downy)) > 8) {
                            clickflag = false;
                        } else {
                            clickflag = true;
                        }
                        if (clickflag == false) {
                            //getRawX是触摸位置相对于屏幕的坐标，getX是相对于按钮的坐标
                            wmParams.x = (int) event.getRawX() - floatwindowbutton.getMeasuredWidth() / 2;
                            //减25为状态栏的高度
                            wmParams.y = (int) event.getRawY() - floatwindowbutton.getMeasuredHeight() / 2 - 25;
                            //刷新
                            mWindowManager.updateViewLayout(float_window_small, wmParams);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                }
                return false;  //此处必须返回false，否则OnClickListener获取不到监听
            }
        });

        wifi_button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                try {
                    float_window_menu_animation(-1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return true;
            }
        });

        return super.onStartCommand(intent,flags,startId);
    }

    public Timer initButtonSetting(Timer timer,TimerTask timerTask){
        timer = new Timer(true);
        timer.schedule(timerTask,0,500);
        return timer;
    }

    public void stopinitButtonSetting(Timer timer,TimerTask timerTask){
        timer.cancel();
        timer.purge();
        timerTask.cancel();
    }

    public void setMobileState(Context context,Boolean enabled){
        ConnectivityManager connectivityManager = (ConnectivityManager)this.getSystemService(context.CONNECTIVITY_SERVICE);
        Class<?> conMgrClass = null;
        Field iconMgrField = null;
        Object iconMgr = null;
        Class<?> iconMgrClass = null;
        Method setMobileDataEnabledMethod = null;
        try{
            conMgrClass = Class.forName(connectivityManager.getClass().getName());
            iconMgrField = conMgrClass.getDeclaredField("mService");
            iconMgrField.setAccessible(true);
            iconMgr = iconMgrField.get(connectivityManager);
            iconMgrClass = Class.forName(iconMgr.getClass().getName());
            setMobileDataEnabledMethod = iconMgrClass.getDeclaredMethod("setMobileDataEnabled",Boolean.TYPE);
            setMobileDataEnabledMethod.setAccessible(true);
            setMobileDataEnabledMethod.invoke(iconMgr,enabled);//开关
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void refresh_wifistate(){
        Timer timer = new Timer(true);
        TimerTask timertask = new TimerTask() {
            @Override
            public void run() {
                wifi2icon();//timertask刷新wifiicon
            }
        };
        timer.schedule(timertask,0,300);
    }

    public void refresh_connectivity(){
        Timer timer = new Timer(true);
        TimerTask timertask = new TimerTask() {
            @Override
            public void run() {
                connecty2icon();
            }
        };
        timer.schedule(timertask,0,300);
    }

    public void refresh_bluetooth(){
        Timer timer = new Timer(true);
        bluetoothadapter = BluetoothAdapter.getDefaultAdapter();
        TimerTask timertask = new TimerTask() {
            @Override
            public void run() {
                bluetooth2icon();
            }
        };
        timer.schedule(timertask,0,300);
    }

    public void refresh_ring(){
        audiomanager = (AudioManager)getApplication().getSystemService(Context.AUDIO_SERVICE);
        Timer timer = new Timer(true);
        TimerTask timertask = new TimerTask() {
            @Override
            public void run() {
                int state = audiomanager.getRingerMode();
                ring2icon(state);
            }
        };
        timer.schedule(timertask,0,300);
    }

    public void refresh_write(){
        Timer timer = new Timer(true);
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                ContentResolver cr = getContentResolver();
                write2icon(cr);
            }
        };
        timer.schedule(timerTask,0,300);
    }

    public void refresh_flash(){
        Timer timer = new Timer(true);
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                //flash2icon();
            }
        };
        timer.schedule(timerTask,0,300);
    }

    public boolean getMobileDataState(){
        ConnectivityManager connectivitymangager;
        connectivitymangager = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);//获取移动数据系统服务

        Class cmclass = connectivitymangager.getClass();
        Class[] argclass = null;
        Object[] argobject = null;
        Boolean isopen = false;

        try{
            Method method = cmclass.getMethod("getMobileDataEnabled",argclass);//注意字符串
            isopen = (Boolean)method.invoke(connectivitymangager,argobject);
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        return isopen;
    }

    public void wifi2icon(){
        Message msg = new Message();
        switch (wifimanager.getWifiState()){
            case 0:
                //WIFI_STATE_DISABLING
                //wifi_button.setBackground(this.getResources().getDrawable(R.drawable.ic_wifi_grey600_48dp));
                msg.what = 0;
                break;
            case 1:
                //WIFI_SDATE_DISABLED
                //wifi_button.setBackground(this.getResources().getDrawable(R.drawable.ic_wifi_black_48dp));//此方法只适用于sdk16以上版本
                msg.what = 1;
                break;
            case 2:
                //WIFI_STATE_ENABLING
                //wifi_button.setBackground(this.getResources().getDrawable(R.drawable.ic_wifi_grey600_48dp));
                msg.what = 2;
                break;
            case 3:
                // WIFI_STATE_ENABLED
                //wifi_button.setBackground(this.getResources().getDrawable(R.drawable.ic_wifi_white_48dp));
                //wifi_button.setBackgroundResource(R.drawable.ic_wifi_white_48dp);
                msg.what = 3;
                break;
            default:
                //Toast.makeText(this,"Mistake happened!",Toast.LENGTH_SHORT).show();
                msg.what = 4;
                break;
        }
        //Log.i("msg.what", String.valueOf(msg.what));
        handler.sendMessage(msg);
    }

    public void connecty2icon(){
        Message msg = new Message();
        if(getMobileDataState()){
            msg.what = 5;//特别注明为5时表示移动数据打开
        }
        else{
            msg.what = 6;//移动数据关闭时为6
        }
        handler.sendMessage(msg);
    }

    public void bluetooth2icon(){
        Message msg = new Message();
        if(bluetoothadapter!=null){
            if(bluetoothadapter.isEnabled()){
                msg.what = 7;//蓝牙on
            }
            else {
                msg.what = 8;//；蓝牙off
            }
            handler.sendMessage(msg);
        }
    }

    public void ring2icon(int state){
        Message msg = new Message();
        if(state == AudioManager.RINGER_MODE_SILENT){
            msg.what = 9;//静音
            //Log.i("audiomanager","9");
        }
        else if(state == AudioManager.RINGER_MODE_NORMAL){
            msg.what = 10;//正常
            //Log.i("audiomanager","10");
        }
        else if(state == AudioManager.RINGER_MODE_VIBRATE){
            msg.what = 11;//震动
            //Log.i("audiomanager","11");
        }
        else {
            msg.what = -1;
        }
        handler.sendMessage(msg);
    }

    public void write2icon(ContentResolver cr){
        Message msg = new Message();

        if(Settings.System.getString(cr,Settings.System.AIRPLANE_MODE_ON).equals("0")){
            //获取当前飞行模式状态,返回的是String值0,或1.0为关闭飞行,1为开启飞行
            msg.what = 12;
        }else{
            msg.what = 13;
        }
        handler.sendMessage(msg);
    }

    public void flash2icon(){
        //Message msg = new Message();
        camera = Camera.open();
        Camera.Parameters parameters = camera.getParameters();
        if(parameters == null){
            Log.i("error","parameters");
        }
        List<String> flashModes = parameters.getSupportedFlashModes();
        if(flashModes == null){
            Log.i("error","flashmodes");
        }
        String flashmode = parameters.getFlashMode();
        if (!Camera.Parameters.FLASH_MODE_TORCH.equals(flashmode)) {
            if (flashModes.contains(Camera.Parameters.FLASH_MODE_ON)) {
                msg.what = 14;
                Log.i("flashmode","14");
            }
            else if(flashModes.contains(Camera.Parameters.FLASH_MODE_OFF)){
                Log.i("flashmode","15");
                msg.what = 15;
            }
            else{
                msg.what = 15;
                Log.i("flashmode","15+");
            }
        }
        if (camera!=null){
            camera.stopPreview();
            camera.release();
            camera=null;
        }
        handler.sendMessage(msg);
    }

    private int dp2pix(int dp){
       float scale = getResources().getDisplayMetrics().density;
        int pix = (int) (dp * scale + 0.5f);
        return pix;
    }

    private int pix2dp(int pix){
        float scale = getResources().getDisplayMetrics().density;
        int dp = (int) (pix/scale + 0.5f);
        return dp;
    }

    private boolean ishome(){
        ActivityManager mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> rti = mActivityManager.getRunningTasks(1);
        return getHomes().contains(rti.get(0).topActivity.getPackageName());
    }

    private List<String> getHomes() {
        List<String> names = new ArrayList<String>();
        PackageManager packageManager = this.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo ri : resolveInfo) {
            names.add(ri.activityInfo.packageName);
        }
        return names;
    }

    public void float_window_menu_animation(int i) throws InterruptedException {
        if(i == 1){//出现
            Message msg = new Message();
            msg.what = 14;
            handler.sendMessage(msg);
            ObjectAnimator objectAnimator1;
            objectAnimator1 = ObjectAnimator.ofFloat(float_window_menu,"scaleX",0.0f,1.0f);
            objectAnimator1.setInterpolator(new AccelerateDecelerateInterpolator());
            objectAnimator1.setDuration(300);
            objectAnimator1.start();
            ObjectAnimator objectAnimator2;
            objectAnimator2 = ObjectAnimator.ofFloat(float_window_menu, "scaleY", 0.0f, 1.0f);
            objectAnimator2.setInterpolator(new AccelerateDecelerateInterpolator());
            objectAnimator2.setDuration(300);
            objectAnimator2.start();
        }
        else{
            ObjectAnimator objectAnimator1;
            objectAnimator1 = ObjectAnimator.ofFloat(float_window_menu,"scaleX",1.0f,0.0f);
            objectAnimator1.setInterpolator(new AccelerateDecelerateInterpolator());
            objectAnimator1.setDuration(300);
            objectAnimator1.start();
            ObjectAnimator objectAnimator2;
            objectAnimator2 = ObjectAnimator.ofFloat(float_window_menu, "scaleY", 1.0f, 0.0f);
            objectAnimator2.setInterpolator(new AccelerateDecelerateInterpolator());
            objectAnimator2.setDuration(300);
            objectAnimator2.start();
            Timer timer = new Timer(true);
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    Message msg = new Message();
                    msg.what = 15;
                    handler.sendMessage(msg);
                }
            };
            timer.schedule(timerTask, 300);
        }
    }

    public void create_float_window(){
        wmParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;//
        //设置图片格式，效果为背景透明
        wmParams.format = PixelFormat.TRANSLUCENT;
        //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        //调整悬浮窗显示的停靠位置为左侧置顶
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;
        // 以屏幕左上角为原点，设置x、y初始值，相对于gravity
        int screenWidth = mWindowManager.getDefaultDisplay().getWidth();
        wmParams.x = screenWidth / 2-dp2pix(48) / 2;
        wmParams.y = dp2pix(117)-dp2pix(48) / 2;
        //设置悬浮窗口长宽数据
        //wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        //wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.width = dp2pix(48);
        wmParams.height = dp2pix(48);
        smallfloatbutton_animation();
        mWindowManager.addView(float_window_small, wmParams);
    }

    public void create_float_windowmenu(){
        int screenWidth = mWindowManager.getDefaultDisplay().getWidth();
        int screenHeight = mWindowManager.getDefaultDisplay().getHeight();
        menuParams = new WindowManager.LayoutParams();
        menuParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        menuParams.format = PixelFormat.TRANSLUCENT;
        menuParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        menuParams.gravity = Gravity.LEFT | Gravity.TOP;
        menuParams.x = 0;
        menuParams.y = 0;
        menuParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        menuParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        /*menuParams.x = screenWidth / 2 - dp2pix(196) / 2;
        menuParams.y = screenHeight/2 - dp2pix(196)/2;
        menuParams.width = dp2pix(196);
        menuParams.height = dp2pix(196);*/
        mWindowManager.addView(float_window_menu, menuParams);
    }

    private void smallfloatbutton_animation(){
        ObjectAnimator objectAnimator;
        objectAnimator = ObjectAnimator.ofFloat(floatwindowbutton, "alpha", 0.0f, 1.0f);
        objectAnimator.setDuration(1500);
        objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        objectAnimator.start();
        ObjectAnimator objectAnimator1;
        objectAnimator1 = ObjectAnimator.ofFloat(floatwindowbutton,"scaleX",2.0f,1.0f);
        objectAnimator1.setInterpolator(new AccelerateDecelerateInterpolator());
        objectAnimator1.setDuration(1500);
        objectAnimator1.start();
        ObjectAnimator objectAnimator2;
        objectAnimator2 = ObjectAnimator.ofFloat(floatwindowbutton, "scaleY", 2.0f, 1.0f);
        objectAnimator2.setInterpolator(new AccelerateDecelerateInterpolator());
        objectAnimator2.setDuration(1500);
        objectAnimator2.start();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(float_window_small != null)
        {
            //移除悬浮窗口
            mWindowManager.removeView(float_window_small);
        }
        if(float_window_menu != null) {
            mWindowManager.removeView(float_window_menu);
        }
    }

}
