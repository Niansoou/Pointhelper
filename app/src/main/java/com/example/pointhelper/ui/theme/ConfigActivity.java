package com.example.pointhelper.ui.theme;
import  android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.pointhelper.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class ConfigActivity extends Activity{
    public Map<Integer,String>name_list=new HashMap<>();
    protected  void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.configview);
        Button btip=(Button) findViewById(R.id.buttonip);
        Button btic=(Button) findViewById(R.id.buttonic);
        Button btnext=(Button)findViewById(R.id.btoplist);
        TextView textView=(TextView) findViewById(R.id.inipoint);
        btip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence num =textView.getText();
                textView.setText(plus(num));
            }
        });
        btic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence num =textView.getText();
                textView.setText(cost(num));
            }
        });
        btnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=getIntent().getExtras();
                MyMap myMap=(MyMap) bundle.get("names");
                name_list=myMap.getMmap();
                //up&down
                Intent intent=new Intent(ConfigActivity.this,ReadyActivity.class);
                MyMap myMap1=new MyMap();
                myMap1.setMmap(name_list);
                Bundle bundle1=new Bundle();
                bundle1.putSerializable("pnames",myMap1);
                intent.putExtras(bundle1);

                //find inipoint
                String ini_point=textView.getText().toString();
                intent.putExtra("inipoint",ini_point);
                startActivity(intent);
            }
        });
    }
    public CharSequence plus(CharSequence cs)
    {
        CharSequence result;
        int num =Integer.parseInt(cs.toString());
        if(num!=9000){
            result=(num+100)+"";
        }else
        {
            result=num+"";
        }
        return result;
    }
    public CharSequence cost(CharSequence cs)
    {
        CharSequence result;
        int num=Integer.parseInt(cs.toString());
        if(num!=1000) {
            result = (num - 100) + "";
        }else
            result=num+"";
        return  result;
    }
}
