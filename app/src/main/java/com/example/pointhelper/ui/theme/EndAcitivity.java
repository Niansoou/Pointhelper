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
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class EndAcitivity extends Activity{
    protected  static final String TAG="end text";
    private List<Player> mlp=new ArrayList<>();
    private List<Battle> mlb=new ArrayList<>();
    private List<Player> p_lp=new ArrayList<>();
    private  int mpool=0;
    protected  void onCreate(Bundle savedInstanceState)
    {
        boolean mswitch;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.endview);
        mlp=(List<Player>) getIntent().getSerializableExtra("playtonext");
        mlb=(List<Battle>) getIntent().getSerializableExtra("sdplayer");
        Intent intent=getIntent();
        mpool=Integer.parseInt(intent.getStringExtra("pool"));
        mswitch=intent.getBooleanExtra("ifbtfold",false);
        for(int i=0;i<mlb.size();i++)
        {
            for(int j=0;j<mlp.size();j++)
            {
                if(mlb.get(i).getBattleid()==mlp.get(j).getId())
                {
                    Player player=new Player();
                    player.setId(mlp.get(j).getId());
                    player.setName(mlp.get(j).getName());
                    player.setPoints(mlp.get(j).getPoints());
                    p_lp.add(player);
                }
            }
        }
        TextView pool=(TextView) findViewById(R.id.final_potpool);
        ListView lv=(ListView)findViewById(R.id.finallist);
        Button bt=(Button)findViewById(R.id.nextmatch);
        Button home=(Button)findViewById(R.id.gohome);
        pool.setText(mpool+"");
        if(mswitch)
        {
            p_lp.remove(0);
        }
        EndAdapter endAdapter=new EndAdapter(this,p_lp,mpool);
        lv.setAdapter(endAdapter);
      bt.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Log.v(TAG,p_lp.get(0).getPoints()+"");
              for(int i=0;i<p_lp.size();i++)
              {
                  for(int j=0;j<mlp.size();j++)
                  {
                      if(mlp.get(j).getId()==p_lp.get(i).getId())
                      {
                          int ini=p_lp.get(i).getPoints();
                          mlp.get(j).setPoints(ini);
                      }
                  }
              }
              Log.v(TAG,mlp.get(0).getPoints()+"");
              for(int i=0;i<mlp.size()-1;i++)
              {
                  Collections.swap(mlp,i,i+1);
              }
              Intent intent1 =new Intent();
              intent1.putExtra("nlist",(Serializable) mlp);
              setResult(2,intent1);
              finish();
          }
      });
      home.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intent = new Intent(EndAcitivity.this, WellcomeActivity.class);
              intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
              startActivity(intent);
          }
      });
    }
}
