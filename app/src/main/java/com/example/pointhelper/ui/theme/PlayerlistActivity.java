package com.example.pointhelper.ui.theme;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pointhelper.R;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PlayerlistActivity extends Activity{
    public String playernum;
    public int[] li={R.drawable.pic_01};
   public  List<int[]> list=new ArrayList<>();
    public Map<Integer,String> namelist =new HashMap<>();
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.players);
        Intent intent= getIntent();
        playernum=intent.getStringExtra("playernum");
        int pnum=Integer.parseInt(playernum);
        for(int i=0;i<pnum;i++){
            list.add(li);
        }
        ListView playerl=(ListView) findViewById(R.id.plst);
        ListAdapter listAdapter=new ListAdapter();
        playerl.setAdapter(listAdapter);
        Button bnext=(Button) findViewById(R.id.button4);
        bnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MyMap myMap =new MyMap();
                myMap.setMmap(namelist);

                Intent intent1=new Intent(PlayerlistActivity.this,ConfigActivity.class);

                Bundle bundle =new Bundle();
                bundle.putSerializable("names",myMap);
                intent1.putExtras(bundle);
                startActivity(intent1);
            }
        });
    }
    class ListAdapter extends BaseAdapter {
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

            convertView = LayoutInflater.from(getApplication()).inflate(R.layout.listview, null);
            ImageView imageView=(ImageView) convertView.findViewById(R.id.imageView4);
            final  EditText editText =(EditText) convertView.findViewById(R.id.nameedit);
            imageView.setImageResource(list.get(position)[0]);
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                        namelist.put(position,s.toString());
                }
                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            return  convertView;
        }
    }

}
