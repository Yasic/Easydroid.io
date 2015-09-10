package com.example.esir.enotepad;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by ESIR on 2015/7/16.
 */
public class Fragone_recycler_adapter extends RecyclerView.Adapter<Fragone_recycler_adapter.ViewHolder> {//这里注意<>里面是自己定义的ViewHolder
    private List<Note> Note;

    public Fragone_recycler_adapter(Context context,List<Note> Note){//初始化类函数
        this.Note = Note;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup,int viewType){
        View view = View.inflate(viewGroup.getContext(),R.layout.noteitem_cardview,null);//创建一个View,用到了自定义的布局
        ViewHolder holder = new ViewHolder(view);//创建一个ViewHolder
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        // 绑定数据到ViewHolder上
        final Note Note = (Note)getItem(i);
        viewHolder.notetime.setText(Note.time);
        viewHolder.notebody.setText(Note.body);
        viewHolder.notetitle.setText(Note.title);
    }

    @Override
    public long getItemId(int position) {
        return position;//返回位置
    }

    @Override
    public int getItemCount() {
        return (Note==null)?0:Note.size();
    }

    public Object getItem(int position) {
        return Note.get(position);
    }//获得指定位置的项目

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView notetitle;
        public TextView notebody;
        public TextView notetime;;

        public ViewHolder(View itemView) {//自定义的viewholder
            super(itemView);
            notetitle = (TextView)itemView.findViewById(R.id.notetitle_cardview);
            notebody = (TextView)itemView.findViewById(R.id.notebody_cardview);
            notetime = (TextView)itemView.findViewById(R.id.notetime_cardview);
        }
    }
}
