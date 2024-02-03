package com.example.pointhelper.ui.theme;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.pointhelper.R;

public class SelectActivity extends Activity{
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playernum);
        Button btp =(Button)findViewById(R.id.buttonp);
        Button btc =(Button) findViewById(R.id.buttonc);
        Button btnext =(Button)findViewById(R.id.button4);
        TextView num= (TextView)findViewById(R.id.p_num);
        btp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence num1= num.getText();
                num.setText(plus(num1));
            }
        });
        btc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence num1 = num.getText();
                    num.setText(cost(num1));
            }
        });
        btnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(SelectActivity.this,PlayerlistActivity.class);
                String cs=num.getText().toString();
                intent.putExtra("playernum",cs);
                startActivity(intent);

            }
        });
    }
    public CharSequence plus(CharSequence cs)
    {
        CharSequence result;
        int num =Integer.parseInt(cs.toString());
        if(num!=20){
            result=(num+1)+"";
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
        if(num!=2) {
            result = (num - 1) + "";
        }else
            result=num+"";
        return  result;
    }
}
