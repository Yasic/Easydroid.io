package com.example.esir.enotepad;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.Notification;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.software.shell.fab.ActionButton;

import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;


/**
 * Created by ESIR on 2015/5/29.
 */
public class Fragment1 extends Fragment {
    private ListView noteList;
    private View view;
    private  Myadapterfornote adapter;
    private TextView testtext;
    private SQLiteDatabase db;
    private ENoteSQLitedbhelper helper,helperinside;
    public List<Note> Note;
    public String title,note,time,flag;
    public String notecolor;
    private RecyclerView recyclerview;
    private Fragone_recycler_adapter fragone_recycler_adapter;
    private ActionButton FABbutton;
    private String sortflag = null;
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savadInstanceState){
        view = inflater.inflate(R.layout.fragment1,container,false);
        additemlistener();//添加长按短按监听
        init_FABbutton();
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)//fragment开启的activity要由fragment来接收！此处接收noteedit返回数据
    {
        FABbutton.setButtonColor(getResources().getColor(R.color.fab_mdcolor));//颜色变回来
        FABbutton.playShowAnimation();//fab按钮出现动画
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 8080 && resultCode == 80801) {
            Bundle bundle = data.getExtras();
            getintent(bundle);//获取intent值

            if(getIntent_flag(bundle).equals("1")){//为1修改，为0新建
                if(!title.equals("") | !note.equals("")) {//这里判定条件为空而不是null
                    updatadatabase(bundle);//更新数据库
                }
                else{
                    deletedatabase(bundle);//删除数据库
                }
            }
            if(getIntent_flag(bundle).equals("0")){
                insertDB(bundle);
            }
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Bundle bundle = getArguments();
        getintent(bundle);//获取intent的data值
        sortflag = bundle.getString("sortflag");
        Note = null;
        Note = new ArrayList<Note>();
        helper = new ENoteSQLitedbhelper(getActivity(),"ENote", 1);//开启数据库
        dboutput(helper);//数据库数据调出
        helper.close();//此处关闭数据库
        adapter = new Myadapterfornote(getActivity(),Note);//以Note生成adapter
    }

    public void init_FABbutton(){
        FABbutton = (ActionButton)view.findViewById(R.id.plusbuttonfornote);
        FABbutton.setShowAnimation(ActionButton.Animations.JUMP_FROM_DOWN);//设置动画set
        FABbutton.setHideAnimation(ActionButton.Animations.JUMP_TO_DOWN);//设置动画set
        FABbutton.setImageDrawable(getResources().getDrawable(R.drawable.fab_plus_icon));//设置background
        FABbutton.setButtonColor(getResources().getColor(R.color.fab_mdcolor));
        FABbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.i("!", "test");
                FABbutton.setButtonColor(getResources().getColor(R.color.fab_mdcolor_pressed));//fab按钮被点击后变色
                Intent intent = new Intent(getActivity(), Noteedit.class);
                Bundle bundle = new Bundle();
                bundle.putString("title", null);
                bundle.putString("note", null);
                bundle.putString("time", null);
                bundle.putString("flag", "-1");
                bundle.putString("notecolor", "0");
                intent.putExtras(bundle);
                startActivityForResult(intent, 8080);
            }
        });
    }

    public void insertDB(Bundle bundle){
        helperinside = new ENoteSQLitedbhelper(getActivity(),"ENote", 1);//开启数据库
        ContentValues values = new ContentValues();
        values.put("TITLE",getIntent_title(bundle));
        values.put("NOTE", getIntent_note(bundle));
        values.put("TIME", bundle.getString("edittime"));
        values.put("COLOR",getIntent_notecolor(bundle));
        //Log.i("test", title);
        helperinside.getWritableDatabase().insert("NOTETABLE", null, values);//插入新的数据
        renew(helperinside);
        helperinside.close();
    }

    public void deletedatabase(Bundle bundle){
        helperinside = new ENoteSQLitedbhelper(getActivity(), "ENote", 1);//打开数据库
        helperinside.getWritableDatabase().delete("NOTETABLE", "TIME = ?", new String[]{bundle.getString("time")});//删除数据库
        renew(helperinside);//更新gridview
        helperinside.close();//关闭数据库
    }

    public void updatadatabase(Bundle bundle){
        helperinside = new ENoteSQLitedbhelper(getActivity(), "ENote", 1);//打开数据库
        ContentValues cv = new ContentValues();
        cv.put("TITLE",getIntent_title(bundle));
        cv.put("NOTE",getIntent_note(bundle));
        cv.put("TIME", bundle.getString("edittime"));
        cv.put("COLOR",getIntent_notecolor(bundle));
        helperinside.getWritableDatabase().update("NOTETABLE", cv, "TIME = ?", new String[]{bundle.getString("time")});//更新数据库
        renew(helperinside);//更新gridview
        helperinside.close();//关闭数据库
        title = null;
        note = null;
        time = null;
    }

    public void dboutput(ENoteSQLitedbhelper h){//数据库数据遍历输出
        Cursor cursor = null;
        if(sortflag.equals("time")){
            cursor = h.getReadableDatabase().query("NOTETABLE",null,null,null,null,null,"TIME DESC");
        }
        else if(sortflag.equals("notecolor")){
            cursor = h.getReadableDatabase().query("NOTETABLE",null,null,null,null,null,"COLOR");
        }
        while (cursor.moveToNext()) {
            Note.add(new Note(
                    cursor.getString(cursor.getColumnIndex("TITLE")),
                    cursor.getString(cursor.getColumnIndex("NOTE")),
                    cursor.getString(cursor.getColumnIndex("TIME")),
                    cursor.getString(cursor.getColumnIndex("COLOR"))));
        }
    }

    public void renew(ENoteSQLitedbhelper h){
        Note.clear();
        Cursor cursor = h.getReadableDatabase().query("NOTETABLE", null, null, null, null, null, "TIME DESC");//开启游标
        while(cursor.moveToNext()){//遍历全部元素
            Note.add(new Note(
                    cursor.getString(cursor.getColumnIndex("TITLE")),
                    cursor.getString(cursor.getColumnIndex("NOTE")),
                    cursor.getString(cursor.getColumnIndex("TIME")),
                    cursor.getString(cursor.getColumnIndex("COLOR"))));//添加到list
        }
        adapter.notifyDataSetChanged();
        noteList.invalidateViews();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
    }

    public void  additemlistener(){
        noteList = (ListView)view.findViewById(R.id.noteList);//获取gridview实例
        noteList.setAdapter(adapter);
        noteList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()).setMessage("DELETE?")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                helperinside = new ENoteSQLitedbhelper(getActivity(), "ENote", 1);//开启数据库
                                TextView timetext = (TextView) view.findViewById(R.id.notetime_cardview);
                                String s = timetext.getText().toString();
                                helperinside.getWritableDatabase().delete("NOTETABLE", "TIME = ?", new String[]{s});
                                renew(helperinside);
                                helperinside.close();
                            }
                        })
                        .setNegativeButton("cancle", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                builder.create().show();
                return true;//避免同时触发两个监听，长按返回true则短按不会触发
            }
        });
        noteList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //todo
                //实现开启编辑通信
                Intent intent = new Intent(getActivity(), Noteedit.class);
                Bundle bundle = new Bundle();
                TextView postText = (TextView) view.findViewById(R.id.notetitle_cardview);
                String postText_String = postText.getText().toString();
                bundle.putString("title", postText_String);
                postText = (TextView) view.findViewById(R.id.notebody_cardview);
                postText_String = postText.getText().toString();
                bundle.putString("note", postText_String);
                postText = (TextView) view.findViewById(R.id.notetime_cardview);
                postText_String = postText.getText().toString();
                bundle.putString("time", postText_String);
                ENoteSQLitedbhelper color_helper = new ENoteSQLitedbhelper(getActivity(), "ENote", 1);//打开数据库
                Cursor cursor = color_helper.getReadableDatabase().query("NOTETABLE", new String[]{"COLOR"},
                        "TIME=?", new String[]{postText_String}, null, null, null);
                String note_color = "0";
                while(cursor.moveToNext()){
                    note_color = cursor.getString(cursor.getColumnIndex("COLOR"));
                }
                color_helper.close();
                bundle.putString("notecolor",note_color);
                intent.putExtras(bundle);
                startActivityForResult(intent, 8080);
            }
        });
    }

    public void getintent(Bundle bundle){
        title = bundle.getString("title");
        note = bundle.getString("note");
        time = bundle.getString("time");
        flag = bundle.getString("flag");
        notecolor = bundle.getString("notecolor");
    }

    public String getIntent_title(Bundle bundle){
        return bundle.getString("title");
    }

    public String getIntent_note(Bundle bundle){
        return bundle.getString("note");
    }

    public String getIntent_time(Bundle bundle){
        return bundle.getString("time");
    }

    public String getIntent_flag(Bundle bundle){
        return bundle.getString("flag");
    }

    public String getIntent_notecolor(Bundle bundle){return bundle.getString("notecolor");}
}
