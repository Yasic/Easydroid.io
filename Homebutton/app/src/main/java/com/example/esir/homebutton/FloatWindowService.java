package com.example.esir.homebutton;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
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
import android.os.PowerManager;
import android.provider.Settings;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
//import java.util.logging.Handler;
import java.util.logging.LogRecord;
import android.os.Handler;
/**
 * Created by ESIR on 2015/7/25.
 */
public class FloatWindowService extends Service {

    //定义浮动窗口布局
    public LinearLayout float_window_small;
    private CardView float_window_menu;
    WindowManager.LayoutParams wmParams;
    WindowManager.LayoutParams menuParams;
    int tempx,tempy;
    //创建浮动窗口设置布局参数的对象
    WindowManager mWindowManager;
    WifiManager wifimanager;
    BluetoothAdapter bluetoothadapter;
    AudioManager audiomanager;
    Button floatwindowbutton , home_button , wifi_button , connectivity_button , close_button;
    Button bluetooth_button,ring_button;
    private boolean clickflag;
    public int system_version;
    public int sdk_version;

    Handler handler = new Handler(){
      @Override
    public void handleMessage(Message msg){
          switch (msg.what){
              case 0:
                  //WIFI_STATE_DISABLING
                  wifi_button.setBackground(getApplication().getResources().getDrawable(R.drawable.ic_wifi_grey600_48dp));
                  break;
              case 1:
                  //WIFI_SDATE_DISABLED
                  wifi_button.setBackground(getApplication().getResources().getDrawable(R.drawable.ic_wifi_grey600_48dp));//此方法只适用于sdk16以上版本
                  break;
              case 2:
                  //WIFI_STATE_ENABLING
                  wifi_button.setBackground(getApplication().getResources().getDrawable(R.drawable.ic_wifi_grey600_48dp));
                  break;
              case 3:
                  // WIFI_STATE_ENABLED
                  wifi_button.setBackground(getApplication().getResources().getDrawable(R.drawable.ic_wifi_white_48dp));
                  break;
              case 5:
                  //移动数据打开
                  connectivity_button.setBackground(getApplication().
                          getResources().
                          getDrawable(R.drawable.ic_internet_explorer_white_48dp));
                  break;
              case 6:
                  //移动数据关闭
                  connectivity_button.setBackground(getApplication().
                          getResources().
                          getDrawable(R.drawable.ic_internet_explorer_grey600_48dp));
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
              default:
                  Toast.makeText(getApplication(),"Mistake happened!",Toast.LENGTH_SHORT).show();
                  break;
          }
      }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate(){
        super.onCreate();

        Log.i("system",android.os.Build.MODEL+","+android.os.Build.VERSION.SDK+","+android.os.Build.VERSION.RELEASE);
        String temp[] = Build.VERSION.RELEASE.split("\\.");//.是转义字符，要加上//
        int system = Integer.parseInt(temp[0]);
        system_version = system;
        sdk_version = Build.VERSION.SDK_INT;
        Log.i("sdk" , String.valueOf(sdk_version));//获取int型的版本号

        Notification notification = new Notification(R.drawable.ic_emoticon_devil_black_48dp,
                "easttouch后台运行", System.currentTimeMillis());
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);
        notification.setLatestEventInfo(this, "Easytouch of Yasic", "正在后台运行中", pendingIntent);
        startForeground(1, notification);
    }

    public void setbutton2view(){
        LayoutInflater inflater = LayoutInflater.from(getApplication());
        float_window_menu = (CardView)inflater.inflate(R.layout.float_window_menu , null);
        float_window_small = (LinearLayout) inflater.inflate(R.layout.float_window, null);

        floatwindowbutton = (Button)float_window_small.findViewById(R.id.floatwindowbutton);
        home_button = (Button)float_window_menu.findViewById(R.id.home_button);
        wifi_button = (Button)float_window_menu.findViewById(R.id.wifi_button);
        connectivity_button = (Button)float_window_menu.findViewById(R.id.connectivity_button);
        close_button = (Button)float_window_menu.findViewById(R.id.close_button);
        bluetooth_button = (Button)float_window_menu.findViewById(R.id.bluetooth_button);
        ring_button = (Button)float_window_menu.findViewById(R.id.ring_button);
    }

    @Override
    public int onStartCommand(Intent intent,int flags,int startId){
        wmParams = new WindowManager.LayoutParams();
        mWindowManager = (WindowManager)getApplication().getSystemService(getApplication().WINDOW_SERVICE);
        //访问service提供的服务
        setbutton2view();//设置button绑定view

        wifimanager = (WifiManager)this.getSystemService(Context.WIFI_SERVICE);//获取wifi服务
        refresh_wifistate();//周期更新wifiicon状态

        refresh_connectivity();//周期更新connecticon状态
        refresh_bluetooth();//刷新蓝牙icon状态
        refresh_ring();

        create_float_window();
        create_float_windowmenu();
        float_window_menu.setVisibility(View.GONE);

        floatwindowbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickflag == true) {//不是onTouch事件
                    float_window_small.setVisibility(View.GONE);
                    //mWindowManager.removeView(mFloatLayout);
                    float_window_menu.setVisibility(View.VISIBLE);
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

        home_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("home?", String.valueOf(ishome()));
                /*if(ishome()){
                    float_window_menu.setVisibility(View.GONE);
                    mFloatLayout.setVisibility(View.VISIBLE);
                }
                else{*/
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.putExtra("GOHOME", "GOHOME");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
                float_window_menu.setVisibility(View.GONE);
                float_window_small.setVisibility(View.VISIBLE);
                //}
            }
        });

        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mWindowManager.removeView(float_window_menu);
                //create_float_window();
                float_window_menu.setVisibility(View.GONE);
                float_window_small.setVisibility(View.VISIBLE);
            }
        });

        wifi_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                        Toast.makeText(getApplicationContext(), "Mistake happened!", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        connectivity_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean enabled = false;
                if(system_version < 5){
                    if(getMobileDataState()){

                    }
                    else{
                        enabled = true;
                    }
                    setMobileState(getApplication(),enabled);
                }
                else{
                    Toast.makeText(getApplication(),"当前系统不支持移动网络开关",Toast.LENGTH_LONG).show();
                }
            }
        });

        bluetooth_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bluetoothadapter.isEnabled()){
                    bluetoothadapter.disable();//关闭蓝牙
                }
                else{
                    bluetoothadapter.enable();//打开蓝牙
                }
            }
        });

        return super.onStartCommand(intent,flags,startId);
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

    public void create_float_window(){
        wmParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;//
        //设置图片格式，效果为背景透明
        wmParams.format = PixelFormat.TRANSLUCENT;
        //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        //调整悬浮窗显示的停靠位置为左侧置顶
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;
        // 以屏幕左上角为原点，设置x、y初始值，相对于gravity
        wmParams.x = 0;
        wmParams.y = 0;
        //设置悬浮窗口长宽数据
        //wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        //wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.width = dp2pix(48);
        wmParams.height = dp2pix(48);

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
        menuParams.x = screenWidth / 2 - dp2pix(196) / 2;
        menuParams.y = screenHeight/2 - dp2pix(128)/2;
        menuParams.width = dp2pix(196);
        menuParams.height = dp2pix(128);
        mWindowManager.addView(float_window_menu,menuParams);
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
                ring2icon();
            }
        };
        timer.schedule(timertask,0,300);
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

    public void ring2icon(){
        Message msg = new Message();
        int state = audiomanager.getRingerMode();
        if(state == AudioManager.RINGER_MODE_SILENT){
            msg.what = 9;//静音
        }
        else if(state == AudioManager.RINGER_MODE_NORMAL){
            msg.what = 10;
        }
        handler.sendMessage(msg);
    }

    private int dp2pix(int dp){
       float scale = getResources().getDisplayMetrics().density;
        Log.i("density", String.valueOf(scale));
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
}
