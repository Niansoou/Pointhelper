package com.example.pointhelper.ui.theme;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.pointhelper.R;
import java.util.ArrayList;
import java.util.List;
public class EndAdapter extends BaseAdapter {
    private  static  final  String TAG="test";
    private List<Player> list;
    private Context mycontext;
    private LayoutInflater layoutInflater;
    private int finalpool=0;
    private boolean ifc1=true;
    public EndAdapter(Context context,List<Player>mlist,int pool)
    {
        finalpool=pool;
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
            convertView=layoutInflater.inflate(R.layout.end_listview,null);
            convertView.setTag(position);
        }
        else{
            convertView.setTag(position);
        }
        ImageView imageView=(ImageView) convertView.findViewById(R.id.endico);
        TextView textView1=(TextView)convertView.findViewById(R.id.end_names);
        TextView textView=(TextView)convertView.findViewById(R.id.end_point);
        TextView pool=(TextView)convertView.findViewById(R.id.final_potpool);
        Button bt=(Button)convertView.findViewById(R.id.whowin);
        imageView.setImageResource(R.drawable.pic_01);
        textView1.setText(list.get(position).getName());
        textView.setText(list.get(position).getPoints()+"");
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ifc1)
                {
                 textView.setText((list.get(position).getPoints()+finalpool)+"");
                 list.get(position).setPoints(list.get(position).getPoints()+finalpool);
                 ifc1=false;
                 }
            }
        });
        return  convertView;
    }
}
