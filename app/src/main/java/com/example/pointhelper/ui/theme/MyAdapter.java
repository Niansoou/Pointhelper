package com.example.pointhelper.ui.theme;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.view.*;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pointhelper.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyAdapter extends BaseAdapter {
    private  static  final  String TAG="test";
    public  List<String> listq=new ArrayList<>();
    private List<Player> list;
    private Context mycontext;
    private LayoutInflater layoutInflater;
    int[] ico;
   public MyAdapter(Context context,List<Player>mlist,int[] mico)
   {
       ico=mico;
       list=mlist;
       layoutInflater=LayoutInflater.from(context);
       mycontext=context;
   }
    @Override
    public int getCount() {
        return list.size();
    }
    @Override
    public Object getItem(int position) {
        return list.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       if(convertView==null){
           convertView=layoutInflater.inflate(R.layout.ready_listview,null);
           convertView.setTag(position);
       }
       else{
           convertView.setTag(position);
       }
        ImageView imageView=(ImageView) convertView.findViewById(R.id.imageView_4);
        ImageView imageView1=(ImageView)convertView.findViewById(R.id.front);
        if(position<3) {
            imageView1.setImageResource(ico[position]);
        }else imageView1.setImageResource(R.drawable.pround);
        TextView textView1=(TextView)convertView.findViewById(R.id.re_names);
        TextView textView=(TextView)convertView.findViewById(R.id.point);
        imageView.setImageResource(R.drawable.pic_01);
        textView1.setText(list.get(position).getName());
        textView.setText(list.get(position).getPoints()+"");
       return  convertView;
    }

}
