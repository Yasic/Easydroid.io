package com.example.esir.enotepad;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import java.security.KeyException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ESIR on 2015/5/30.
 */
public class Noteedit extends Activity{
    private String title;
    private String note;
    private String time,edittime;
    private String notecolor;
    private EditText titleedittext,noteedittext;
    private Button notesavebutton,notebackbutton,notedeletebutton;
    private String flag;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        overridePendingTransition(R.anim.slide_right_in_anim, R.anim.still_nothing_anim);//activity切换动画
        super.onCreate(savedInstanceState);
        setContentView(R.layout.noteedit);
        init();
        initColorList();
        savefunction();
        backandpost();
        deletefunction();

    }

    public void init(){
        notedeletebutton = (Button)findViewById(R.id.notedeletebutton);
        notedeletebutton.setVisibility(View.GONE);//默认隐藏删除按钮
        notecolor = "0";//默认为白色
        flag = "0";//默认新建
        titleedittext = (EditText)findViewById(R.id.edittitle);
        noteedittext = (EditText)findViewById(R.id.editbody);
        getintentdata();//获取intent里的值
        if(title != null | note != null){//不为null则说明是从fragment启动而来，需要修改
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);//关闭输入框弹出
            titleedittext.setText(title);
            noteedittext.setText(note);
            notedeletebutton.setVisibility(View.VISIBLE);//删除键可见
            flag = "1";  //1则修改
        }
    }

    public void initColorList(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        RecyclerView ColorRecyclerView = (RecyclerView)findViewById(R.id.ColorRecyclerView);
        ColorRecyclerView.setLayoutManager(layoutManager);
        //ColorRecyclerView.setItemAnimator(new DefaultItemAnimator());
        List Colorlist = new ArrayList<Integer>();
        Getcolors getcolors = new Getcolors();
        for(int i = 0;i < getcolors.getColorCount();i++){
            Colorlist.add(i);
        }
        final ColorRecyclerview_adapter colorRecyclerview_adapter = new ColorRecyclerview_adapter(this,Colorlist);
        ColorRecyclerView.setAdapter(colorRecyclerview_adapter);
        colorRecyclerview_adapter.setOnItemClickListener(new ColorRecyclerview_adapter.OnRecyclerViewItemClickListener(){
            @Override
            public void onItemClick(View view,String data){
                Getcolors getcolors = new Getcolors();
                changeColor(data);
                notecolor = data;
            }
        });
    }

    public void deletefunction(){//设置删除button功能
        notedeletebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Noteedit.this)
                        .setMessage("DELETE?")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                title = "";//设置为空白而不是null
                                note = "";//设置为空白而不是null，因为直接返回空白时也不可以创建note
                                returnactivity(1);
                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                builder.create().show();
            }
        });
    }

    public void savefunction(){
        notesavebutton = (Button)findViewById(R.id.notesavebutton);
        notesavebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Noteedit.this);
                builder.setMessage("Are you sure to save?");
                builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {//保存后退出
                        if (titleedittext.getText().toString().equals("") & noteedittext.getText().toString().equals("")) {
                            Toast.makeText(getApplicationContext(), "Do you forget write note?", Toast.LENGTH_LONG).show();
                        } else {
                            title = titleedittext.getText().toString();
                            note = noteedittext.getText().toString();
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            edittime = simpleDateFormat.format(new java.util.Date());//get system time
                            returnactivity(1);
                        }
                    }
                });
                builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.setNeutralButton("reset", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {//重置按钮
                        titleedittext.setText("");
                        noteedittext.setText("");
                    }
                });
                builder.create().show();
            }
        });
    }

    public void backandpost(){
        notebackbutton = (Button)findViewById(R.id.notebackbutton);
        notebackbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Noteedit.this);
                builder.setMessage("If you forget save your note,it will be THROWN!");
                builder.setPositiveButton("to leave", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        returnactivity(-1);
                    }
                });
                builder.setNegativeButton("to save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.create().show();
            }
        });
    }

    public void returnactivity(int ifsave){
        Intent intent = new Intent(Noteedit.this,MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("title",title);
        bundle.putString("note",note);
        bundle.putString("time",time);
        bundle.putString("edittime",edittime);
        bundle.putString("notecolor",notecolor);
        if(ifsave == -1){
            bundle.putString("flag","-1");
        }
        else{
            bundle.putString("flag",flag);//1则修改，0则新建
        }
        intent.putExtras(bundle);
        setResult(80801, intent);
        finish();
    }

    public void getintentdata(){
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        title = bundle.getString("title");
        note = bundle.getString("note");
        time = bundle.getString("time");//获取note上的时间
        notecolor = bundle.getString("notecolor");
        changeColor(notecolor);
        edittime = time;
    }

    public void changeColor(String notecolor){
        Getcolors getcolors = new Getcolors();
        Log.i("notecolor",notecolor);
        titleedittext.setBackgroundResource(getcolors.getColor(Integer.valueOf(notecolor)));
        ScrollView edittext_scrollview = (ScrollView)findViewById(R.id.edittext_scrollview);
        edittext_scrollview.setBackgroundResource(getcolors.getColor(Integer.valueOf(notecolor)));
    }

    @Override
    public boolean onKeyDown(int keyCode,KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK){//返回键返回上一个activity
            AlertDialog.Builder builder = new AlertDialog.Builder(Noteedit.this);
            builder.setMessage("If you forget save your note,it will be THROWN!");
            builder.setPositiveButton("to leave", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    returnactivity(-1);}
            });
            builder.setNegativeButton("to save", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            builder.create().show();
        }
        return false;
    }
}
