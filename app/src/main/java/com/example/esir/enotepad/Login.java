package com.example.esir.enotepad;

import android.animation.PropertyValuesHolder;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Interpolator;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.SignUpCallback;

import org.w3c.dom.Text;

import java.sql.Time;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Created by ESIR on 2015/6/3.
 */
public class Login extends Activity {
    private Button loginbutton,localusebutton,denglubutton,zhucebutton;
    private EditText usernameedit;
    private EditText emailedit;
    private EditText passwardedit;
    private EditText confirmpasswordedit;
    private int Loginorrigister;
    private ProgressDialog pd;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        AVOSCloud.initialize(getApplicationContext(), "16se9yws3bbmlnqluefpoone4pyllqsojvu7aayzfowi44su", "nz8gbcvz7zfy1zwqy9zpad2hg2kfoybpi1oeobji48dmzvvi");

        TextView login_appname = (TextView)findViewById(R.id.login_appname);
        TextView login_appmessage = (TextView)findViewById(R.id.login_appmessage);
        Typeface typefacea = Typeface.createFromAsset(getBaseContext().getAssets(), "Fonts/Bookman_Old_Style.TTF");
        Typeface typefaceb = Typeface.createFromAsset(getBaseContext().getAssets(), "Fonts/Blackletter686BT.TTF");
        login_appmessage.setTypeface(typefaceb);
        init();
    }

    public void init(){
        Loginorrigister = 1;//默认为注册
        Timer timer = new Timer(true);
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(Login.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        };
        timer.schedule(timerTask,2000);
        //usernameedit = (EditText)findViewById(R.id.usernameedit);
        //usernameedit.setVisibility(View.GONE);
        //emailedit = (EditText)findViewById(R.id.emailedit);
        //passwardedit = (EditText)findViewById(R.id.passwordedit);
        //confirmpasswordedit = (EditText)findViewById(R.id.confirmpasswordedit);
        //confirmpasswordedit.setVisibility(View.GONE);
        //emaileditaddanimation();
        //passwordeditaddanimation();
        //addbuttonlistener();//添加button监听
    }

    /*public void addbuttonlistener(){
        localusebutton = (Button)findViewById(R.id.localusebutton);
        localusebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        loginbutton = (Button)findViewById(R.id.loginbutton);//下部登陆按钮
        denglubutton = (Button)findViewById(R.id.denglubutton);//上部登陆按钮
        zhucebutton = (Button)findViewById(R.id.zhucebutton);//上部注册按钮
        denglubutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Loginorrigister = 1;//登陆
                denglubutton.setTextColor(Color.rgb(255, 102, 00));
                zhucebutton.setTextColor(Color.rgb(255, 255, 255));
                confirmpasswordedit.setVisibility(View.GONE);
                usernameedit.setVisibility(View.GONE);
                emaileditaddanimation();
                passwordeditaddanimation();
                movefocusanimation2();//登陆按钮变长
            }
        });
        zhucebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Loginorrigister = -1;//注册
                zhucebutton.setTextColor(Color.rgb(255, 102, 00));
                denglubutton.setTextColor(Color.rgb(255, 255, 255));
                confirmpasswordedit.setVisibility(View.VISIBLE);
                usernameedit.setVisibility(View.VISIBLE);
                movefocusanimation1();//登陆按钮变短
                usernameeditaddanimation();
                emaileditaddanimation();
                passwordeditaddanimation();
                confirmpasswordaddanimation();
            }
        });
        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Loginorrigister == -1) {//注册
                    rigisterfunction();
                }
                if (Loginorrigister == 1) {//登陆
                    loginfunction();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext())
                            .setMessage("Something is wrong!!")
                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                }
            }
        });
    }*/

    public void rigisterfunction(){
        final String email = emailedit.getText().toString();
        String password = passwardedit.getText().toString();
        String confirmpassword = confirmpasswordedit.getText().toString();
        Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}");//正则检查邮件地址
        Matcher m = p.matcher(email);
        final boolean b = m.matches();
        if(b){
            if(password.length()>=6){
                if(!password.equals(confirmpassword)){
                    Toast.makeText(this,"两次输入密码不相同",Toast.LENGTH_LONG).show();
                    passwardedit.setText("");//清空输入框
                    confirmpasswordedit.setText("");
                }
                else{
                    pd = ProgressDialog.show(this,null,"Loading...");
                    AVUser user = new AVUser();//开始注册过程
                    user.setUsername(emailedit.getText().toString());//username用邮箱地址存储，从而保证唯一性
                    user.setPassword(passwardedit.getText().toString());//保存密码
                    user.setEmail(emailedit.getText().toString());//保存邮箱地址
                    user.put("clientname",usernameedit.getText().toString());//单独存储用户姓名
                    user.signUpInBackground(new SignUpCallback() {
                    //Todo
                    //还需继续进行注册实验
                        public void done(AVException e) {
                            pd.dismiss();
                            if (e == null) {
                                Toast.makeText(getApplicationContext(),"successful!",Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(Login.this,MainActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("username",usernameedit.getText().toString());
                                bundle.putString("email",emailedit.getText().toString());
                                bundle.putString("password",passwardedit.getText().toString());
                                intent.putExtras(bundle);
                                startActivity(intent);
                                // successfully
                            } else {
                                Log.i("test",e.toString());
                                Toast.makeText(getApplicationContext(),"注册失败，请检查邮箱或网络是否有问题",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
            else{
                Toast.makeText(this,"密码长度不能小于6位",Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(this,"邮箱地址无效",Toast.LENGTH_LONG).show();
            emailedit.setText("");
        }
    }

    public void loginfunction() {
        AVUser.logInInBackground(emailedit.getText().toString(), passwardedit.getText().toString(), new LogInCallback() {
            public void done(AVUser user, AVException e) {
                if (user != null) {
                    Intent intent = new Intent(Login.this,MainActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("username",emailedit.getText().toString());
                    bundle.putString("email", emailedit.getText().toString());
                    final AVQuery<AVUser> query = AVUser.getQuery();
                    query.whereEqualTo("username",emailedit.getText().toString());
                    query.findInBackground(new FindCallback<AVUser>() {
                        @Override
                        public void done(List<AVUser> list, AVException e) {
                            if(e == null){
                            }else{

                            }
                        }
                    });
                    //bundle.putString("password",passwardedit.getText().toString());
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(),"登陆失败。请检查输入内容与网络情况",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void usernameeditaddanimation(){
        PropertyValuesHolder confirmbuttonscalex = PropertyValuesHolder.ofFloat("scaleX", 0.0f, 1.0f);
        PropertyValuesHolder confirmbuttonscaley = PropertyValuesHolder.ofFloat("scaleY", 0.0f, 1.0f);
        ValueAnimator valueanimator = ValueAnimator.ofPropertyValuesHolder(confirmbuttonscalex, confirmbuttonscaley);
        valueanimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatorvaluescalex = (float) animation.getAnimatedValue("scaleX");
                float animatorvaluescaley = (float) animation.getAnimatedValue("scaleY");
                usernameedit.setScaleX(animatorvaluescalex);
                usernameedit.setScaleY(animatorvaluescaley);
            }
        });
        valueanimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueanimator.setDuration(700);
        valueanimator.setTarget(usernameedit);
        valueanimator.start();
    }

    public void confirmpasswordaddanimation(){
        PropertyValuesHolder confirmbuttonscalex = PropertyValuesHolder.ofFloat("scaleX",0.0f,1.0f);
        PropertyValuesHolder confirmbuttonscaley = PropertyValuesHolder.ofFloat("scaleY",0.0f,1.0f);
        ValueAnimator valueanimator = ValueAnimator.ofPropertyValuesHolder(confirmbuttonscalex,confirmbuttonscaley);
        valueanimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatorvaluescalex = (float) animation.getAnimatedValue("scaleX");
                float animatorvaluescaley = (float) animation.getAnimatedValue("scaleY");
                confirmpasswordedit.setScaleX(animatorvaluescalex);
                confirmpasswordedit.setScaleY(animatorvaluescaley);
            }
        });
        valueanimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueanimator.setDuration(700);
        valueanimator.setTarget(confirmpasswordedit);
        valueanimator.start();
    }

    public void emaileditaddanimation(){
        PropertyValuesHolder confirmbuttonscalex = PropertyValuesHolder.ofFloat("scaleX",0.0f,1.0f);
        PropertyValuesHolder confirmbuttonscaley = PropertyValuesHolder.ofFloat("scaleY",0.0f,1.0f);
        ValueAnimator valueanimator = ValueAnimator.ofPropertyValuesHolder(confirmbuttonscalex,confirmbuttonscaley);
        valueanimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatorvaluescalex = (float) animation.getAnimatedValue("scaleX");
                float animatorvaluescaley = (float) animation.getAnimatedValue("scaleY");
                emailedit.setScaleX(animatorvaluescalex);
                emailedit.setScaleY(animatorvaluescaley);
            }
        });
        valueanimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueanimator.setDuration(700);
        valueanimator.setTarget(emailedit);
        valueanimator.start();
    }

    public void movefocusanimation1(){
        PropertyValuesHolder loginbuttonwidth = PropertyValuesHolder.ofInt("Width", 190, 85);
        ValueAnimator valueAnimator = ValueAnimator.ofPropertyValuesHolder(loginbuttonwidth);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){
            @Override
            public void onAnimationUpdate(ValueAnimator animation){
                int animatorvaluewidth = (int) animation.getAnimatedValue("Width");
                denglubutton.setLayoutParams(new LinearLayout.LayoutParams(dip2px(getApplicationContext(),animatorvaluewidth), ViewGroup.LayoutParams.MATCH_PARENT));
            }
        });
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.setDuration(700);
        valueAnimator.setTarget(denglubutton);
        valueAnimator.start();
    }

    public void movefocusanimation2(){
        PropertyValuesHolder loginbuttonwidth = PropertyValuesHolder.ofInt("Width", 85, 190);
        ValueAnimator valueAnimator = ValueAnimator.ofPropertyValuesHolder(loginbuttonwidth);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){
            @Override
            public void onAnimationUpdate(ValueAnimator animation){
                int animatorvaluewidth = (int) animation.getAnimatedValue("Width");
                denglubutton.setLayoutParams(new LinearLayout.LayoutParams(dip2px(getApplicationContext(), animatorvaluewidth), ViewGroup.LayoutParams.MATCH_PARENT));
            }
        });
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.setDuration(700);
        valueAnimator.setTarget(denglubutton);
        valueAnimator.start();
    }

    public void passwordeditaddanimation(){
        PropertyValuesHolder confirmbuttonscalex = PropertyValuesHolder.ofFloat("scaleX",0.0f,1.0f);
        PropertyValuesHolder confirmbuttonscaley = PropertyValuesHolder.ofFloat("scaleY", 0.0f, 1.0f);
        ValueAnimator valueanimator = ValueAnimator.ofPropertyValuesHolder(confirmbuttonscalex,confirmbuttonscaley);
        valueanimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatorvaluescalex = (float) animation.getAnimatedValue("scaleX");
                float animatorvaluescaley = (float) animation.getAnimatedValue("scaleY");
                passwardedit.setScaleX(animatorvaluescalex);
                passwardedit.setScaleY(animatorvaluescaley);
            }
        });
        valueanimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueanimator.setDuration(700);
        valueanimator.setTarget(passwardedit);
        valueanimator.start();
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
