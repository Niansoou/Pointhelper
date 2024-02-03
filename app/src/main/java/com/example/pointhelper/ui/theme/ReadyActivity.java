package com.example.pointhelper.ui.theme;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.pointhelper.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ReadyActivity extends Activity{
    private  static  final String TAG="test";
    int[] ds={R.drawable.btico_02,R.drawable.ssbico_03,R.drawable.bbico_01};
    List<Player> ready_list=new ArrayList<>();
    Map<Integer,String> mp=new HashMap<>();
    List<Player> listfor_view=new ArrayList<>();
    private int pnum=0;
    public   int inipoint;
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ready_players);
        Bundle bundle=getIntent().getExtras();
        MyMap myMap =(MyMap) bundle.get("pnames");
        mp=myMap.getMmap();

        Intent intent=getIntent();
        String s=intent.getStringExtra("inipoint");

        inipoint=Integer.parseInt(s);
        Random random=new Random();
        boolean[] bool=new boolean[mp.size()];
        for(int i=0;i<mp.size();i++)
        {
            Player player=new Player();
            do{
                pnum=random.nextInt(mp.size());
                player.setId(pnum);
            }while(bool[pnum]);
            bool[pnum]=true;
            player.setName(mp.get(i));
            player.setPoints(inipoint);
            player.setPosition(i);
            ready_list.add(player);
        }
           int[] pid=new int[ready_list.size()];
        for(int i=0;i<ready_list.size();i++)
        {
            for(int j=0;j<ready_list.size();j++)
            {
                if(i==ready_list.get(j).getId())
                    listfor_view.add(ready_list.get(j));
            }
        }
        //list part
        ListView listView=(ListView) findViewById(R.id.rplst);
        MyAdapter myAdapter =new MyAdapter(this,listfor_view,ds);
        listView.setAdapter(myAdapter);
        //to betview
        Button button=(Button) findViewById(R.id.btostart);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(ReadyActivity.this,BattleActivity.class);
                intent1.putExtra("p_message",(Serializable) listfor_view);
                startActivity(intent1);
            }
        });
    }

}
