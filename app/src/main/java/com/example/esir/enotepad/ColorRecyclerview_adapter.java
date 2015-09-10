package com.example.esir.enotepad;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ESIR on 2015/8/21.
 */
public class ColorRecyclerview_adapter extends RecyclerView.Adapter<ColorRecyclerview_adapter.ViewHolder>
        implements View.OnClickListener{
    private List<Integer> ColorNames;
    private List<Integer> list = new ArrayList(){{
        add(R.color.color_0);
        add(R.color.color_1);
        add(R.color.color_2);
        add(R.color.color_3);
        add(R.color.color_4);
        add(R.color.color_5);
        add(R.color.color_6);
        add(R.color.color_7);
        add(R.color.color_8);
        add(R.color.color_9);
        add(R.color.color_10);
        add(R.color.color_11);
        add(R.color.color_12);
        add(R.color.color_13);
        add(R.color.color_14);
        add(R.color.color_15);
        add(R.color.color_16);
        add(R.color.color_17);
        add(R.color.color_18);
    }};

    public Integer getColorName(String i){
        Getcolors getcolors = new Getcolors();
        return getcolors.getColor(Integer.valueOf(i));
    }

    @Override
    public void onClick(View v) {
        if(onRecyclerViewItemClickListener != null){
            onRecyclerViewItemClickListener.onItemClick(v,(String)v.getTag());
        }
    }

    public static interface  OnRecyclerViewItemClickListener {
        void onItemClick(View view,String data);
    }
    private  OnRecyclerViewItemClickListener onRecyclerViewItemClickListener = null;

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.onRecyclerViewItemClickListener = listener;
    }

    public ColorRecyclerview_adapter(Context context,List<Integer> ColorNames){
        this.ColorNames = ColorNames;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup,int viewType){
        View view = View.inflate(viewGroup.getContext(),R.layout.coloritem,null);//创建一个View,用到了自定义的布局
        ViewHolder holder = new ViewHolder(view);//创建一个ViewHolder
        Getcolors getcolors = new Getcolors();
        holder.defaultText.setBackgroundResource(getcolors.getColor(viewType));
        view.setOnClickListener(this);
        //holder.itemView.setBackgroundResource(list.get(viewType));
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        // 绑定数据到ViewHolder上
        final int Colorname = (int)getItem(i);
        viewHolder.itemView.setTag(String.valueOf(i));
        //viewHolder.itemView.setBackgroundResource(list.get(Colorname));
        //Log.i("Colorname", String.valueOf(i));
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;//返回位置
    }

    @Override
    public int getItemCount() {
        return (ColorNames == null)?0:ColorNames.size();
    }

    public Object getItem(int position) {
        return ColorNames.get(position);
    }//获得指定位置的项目

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView defaultText;
        public ViewHolder(View itemView) {//自定义的viewholder
            super(itemView);
            defaultText = (TextView)itemView.findViewById(R.id.defaultText);
        }
    }
}
