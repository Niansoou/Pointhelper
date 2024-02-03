package com.example.pointhelper.ui.theme;
import android.animation.IntArrayEvaluator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.LogPrinter;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.pointhelper.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.HashSet;
public class BattleActivity extends Activity{
    private static  final String TAG="bet text";
    private int pnum=1;//关键下标变量,代表当前位置
    public List<Player> lp=new ArrayList<>();//在别的activity继承而来的Player集合
    private  int blind=0;//储存底注·
    private  int match=1;//局数变量
    private  int b_round =1;//轮数变量
    private  int mpotpool=0;//底池变量
    private  int betini=10;//底注初始量
    public  List<Player>list_bet=new ArrayList<>();
    public List<Integer> rember=new ArrayList<>();
    protected void onCreate(Bundle savedInstanceState)
    {
            final boolean[] cancheck={false};
            final boolean[] ifp_allin={false};//这四个都是“开关”变量
            final boolean[] ifbtfold={false};
            final  boolean[] mswitch ={false};
            final boolean[] ifnot = {true};
            super.onCreate(savedInstanceState);
            setContentView(R.layout.betview);
            TextView round = (TextView) findViewById(R.id.round);
            TextView player = (TextView) findViewById(R.id.b_name);
            lp = (List<Player>) getIntent().getSerializableExtra("p_message");
            //recive the playmessage
            List<Battle> roundmac = new ArrayList<>();
            TextView b_match = (TextView) findViewById(R.id.matchnum);
            TextView ttpoint = (TextView) findViewById(R.id.ttpoint);
            TextView potpool = (TextView) findViewById(R.id.potpool);
            TextView betpoint = (TextView) findViewById(R.id.betpoint);
            Button call = (Button) findViewById(R.id.call);
            Button check = (Button) findViewById(R.id.check);
            Button fold = (Button) findViewById(R.id.fold);
            Button b_p = (Button) findViewById(R.id.risep);
            Button b_c = (Button) findViewById(R.id.risec);
            Button allin = (Button) findViewById(R.id.allin);
            betpoint.setText(betini+"");
            b_match.setText(match+"");
            player.setText(lp.get(1).getName());
            ttpoint.setText(lp.get(1).getPoints() + "");
            //将lp中的玩家数据导入roundmac中
            for (int i = 0; i < lp.size(); i++) {
                Battle battle = new Battle();
                battle.setBattleid(lp.get(i).getId());
                battle.setBetnum(0);
                roundmac.add(battle);
            }
            //跟注功能的实现
            call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cancheck[0]=false;
                    if (pnum < roundmac.size()) {//这是讨论玩家轮一轮之前的情况，即pnum<玩家数
                        mswitch[0]=true;
                        int r_round = Integer.parseInt(round.getText().toString());//读取当前页面的文本数据，分别读取了轮数和下注数
                        int betp = Integer.parseInt(betpoint.getText().toString());
                        if (pnum == 1 && r_round == 1&& ifnot[0]) {//这是检测是否是第一轮的小盲注，把他选择的下注数储存为底注
                            blind = betp;
                            betpoint.setText((blind * 2) + "");//在储存之后将下注文本变为两倍，是大盲注的下注金额
                            ifnot[0] =false;
                        }
                        int font = roundmac.get((pnum) % roundmac.size()).getBetnum();//先提前获得玩家的已经下的下注数，为font（前）
                        roundmac.get((pnum) % roundmac.size()).setBetnum(betp);//将当前页面显示的下注数储存到roundmac中（因为我们点击了call按钮），Betnum储存了各个玩家每一轮投至底注的额度
                        for (int i = 0; i < lp.size(); i++) {
                            if (lp.get(i).getId() == roundmac.get((pnum) % roundmac.size()).getBattleid()) {//遍历寻找对应玩家的点数总数，将point-betp-font覆盖原来的
                                //point,betp-font是要下至底注的注与上一次下注的差额。举例子：现在跟注需要20bb，但是你之前已经下了10bb，这次你跟注实际补10bb来保证投入底池的bb都是一样的
                                lp.get(i).setPoints(lp.get(i).getPoints() - (betp - font));
                                mpotpool += (betp - font);
                                potpool.setText(mpotpool + "");//底池增加
                            }
                        }
                        if (ifbetsame(roundmac)) {//ifbetsame是一个外部方法，检测此轮所有玩家投入的金额是否一样
                            if(ifp_allin[0])//这是判断是否有人allin，此时应当进入最后一轮，所以单独拿来讨论
                            {
                                //以下全部理解为进入结算页面即可
                                Intent intent = new Intent(BattleActivity.this, EndAcitivity.class);
                                intent.putExtra("playtonext", (Serializable) lp);
                                intent.putExtra("sdplayer", (Serializable) roundmac);
                                intent.putExtra("pool", mpotpool + "");
                                intent.putExtra("ifbtfold",ifbtfold[0]);
                                startActivityForResult(intent, 1);
                            }
                            if (b_round < 4) {//由于这是全员下注相同的情况，我们此时会进入下一轮，于是round+1
                                b_round++;
                                cancheck[0]=true;
                                round.setText(b_round + "");
                            } else {//进入摊牌页面的结算.
                                Intent intent = new Intent(BattleActivity.this, EndAcitivity.class);
                                intent.putExtra("playtonext", (Serializable) lp);
                                intent.putExtra("sdplayer", (Serializable) roundmac);
                                intent.putExtra("pool", mpotpool + "");
                                intent.putExtra("ifbtfold",ifbtfold[0]);
                                startActivityForResult(intent, 1);
                            }
                            pnum = 1;//新一轮开始，我们让下标回归到1（这是0【bt位】之后）
                            betpoint.setText(blind + "");//重新设置下注额位默认底注
                            //提前将界面显示成标号1玩家的信息，后面类似此类格式都是表示显示某位玩家的信息
                            for (int i = 0; i < lp.size(); i++) {
                                if (lp.get(i).getId() == roundmac.get((pnum) % roundmac.size()).getBattleid()) {
                                    player.setText(lp.get(i).getName());
                                    ttpoint.setText(lp.get(i).getPoints() + "");
                                }
                            }
                            for (int i = 0; i < roundmac.size(); i++) {
                                roundmac.get(i).setBetnum(0);//大家投入底注的bb归0；因为到了新一轮了
                            }
                        } else {
                            if(pnum==roundmac.size()-1&&ifbtfold[0]){//ifbtfold是指bt位是否弃牌。我们通过remove将弃牌玩家移除roundmac，但是bt位为0位。他会影响后面对数组的处理
                                //（bt位删除后bt下顺位成了0位，后面依次减一，那么我们用pnum=1想回到bt下顺位就会全部错位）所以我们这里不是将他删除，而是跳过他，在bt位弃牌后每次都跳过他，不显示。
                                pnum=1;//pnum=1 我们跳过pnum=0的信息，看着就和它不存在了一样
                                roundmac.get(0).setBetnum(betp);//betp是当前显示的下注额度，是上个玩家决定的（因为他可能加注），也可以理解为跟注的额度
                                //这里的思想是，既然我们不能删除它，我们就假装它跟注了，但是不在该玩家点数里减去，而且这局也不去显示它，它就相当于被删除了
                                for (int i = 0; i < lp.size(); i++) {//显示bt下顺位信息
                                    if (lp.get(i).getId() == roundmac.get((pnum) % roundmac.size()).getBattleid()) {
                                        player.setText(lp.get(i).getName());
                                        ttpoint.setText(lp.get(i).getPoints() + "");
                                    }
                                }
                            }else{//显示下一个玩家的信息，先显示后，pnum+1
                                for (int i = 0; i < lp.size(); i++) {
                                    if (lp.get(i).getId() == roundmac.get((pnum + 1) % roundmac.size()).getBattleid()) {
                                        player.setText(lp.get(i).getName());
                                        ttpoint.setText(lp.get(i).getPoints() + "");
                                    }
                                } pnum++;
                            }
                        }
                        //加上bet
                    } else {
                        if (pnum == roundmac.size()) {//这是pnum来到等于数组长度的时刻，当此时，我们再判断以下全员投注是否相同，与上同
                            Log.v(TAG, blind + "");
                            int font = roundmac.get((pnum) % roundmac.size()).getBetnum();
                            int betp = Integer.parseInt(betpoint.getText().toString());
                            roundmac.get((pnum) % roundmac.size()).setBetnum(betp);
                            for (int i = 0; i < lp.size(); i++) {
                                if (lp.get(i).getId() == roundmac.get((pnum) % roundmac.size()).getBattleid()) {
                                    lp.get(i).setPoints(lp.get(i).getPoints() - (betp - font));
                                    mpotpool += (betp - font);
                                    potpool.setText(mpotpool + "");
                                }
                            }
                            if (ifbetsame(roundmac)) {
                                if(ifp_allin[0])
                                {
                                    Intent intent = new Intent(BattleActivity.this, EndAcitivity.class);
                                    intent.putExtra("playtonext", (Serializable) lp);
                                    intent.putExtra("sdplayer", (Serializable) roundmac);
                                    intent.putExtra("pool", mpotpool + "");
                                    intent.putExtra("ifbtfold",ifbtfold[0]);
                                    startActivityForResult(intent, 1);
                                }
                                if (b_round < 4) {
                                    b_round++;
                                    cancheck[0]=true;
                                    round.setText(b_round + "");
                                } else {
                                    Intent intent = new Intent(BattleActivity.this, EndAcitivity.class);
                                    intent.putExtra("playtonext", (Serializable) lp);
                                    intent.putExtra("sdplayer", (Serializable) roundmac);
                                    intent.putExtra("pool", mpotpool + "");
                                    intent.putExtra("ifbtfold",ifbtfold[0]);
                                    startActivityForResult(intent,1);
                                }
                                pnum = 1;
                                betpoint.setText(blind + "");
                                for (int i = 0; i < lp.size(); i++) {
                                    if (lp.get(i).getId() == roundmac.get((pnum) % roundmac.size()).getBattleid()) {
                                        player.setText(lp.get(i).getName());
                                        ttpoint.setText(lp.get(i).getPoints() + "");
                                    }
                                }
                                for (int i = 0; i < roundmac.size(); i++) {
                                    roundmac.get(i).setBetnum(0);
                                }
                            }
                        }
                        pnum = 1;//让pnum回归至1，重新开始顺位，到此时，仍未进入下一轮
                        for (int i = 0; i < lp.size(); i++) {
                            if (lp.get(i).getId() == roundmac.get((pnum) % roundmac.size()).getBattleid()) {
                                player.setText(lp.get(i).getName());
                                ttpoint.setText(lp.get(i).getPoints() + "");
                            }
                        }
                    }
                }
            });
            fold.setOnClickListener(new View.OnClickListener() {//这是弃牌按钮的逻辑设置
                @Override
                public void onClick(View v) {
                    if(roundmac.size()==2){//点击fold后，首先检查集合是否只剩下两个元素，因为bt位不会被删除，所以相当于只剩下一人，此时直接计入结算
                        Intent intent = new Intent(BattleActivity.this, EndAcitivity.class);
                        intent.putExtra("playtonext", (Serializable) lp);
                        intent.putExtra("sdplayer", (Serializable) roundmac);
                        intent.putExtra("pool", mpotpool + "");
                        intent.putExtra("ifbtfold",ifbtfold[0]);
                        startActivityForResult(intent,1);
                    }
                    if (mswitch[0]) {//mswitch在第一轮位false，第一轮结束后位ture
                        //以下和call基本没有差别
                        if (pnum < roundmac.size()) {
                            roundmac.remove(pnum);//将该玩家删除至roundmac
                            if (ifbetsame(roundmac)) {
                                if(ifp_allin[0])
                                {
                                    Intent intent = new Intent(BattleActivity.this, EndAcitivity.class);
                                    intent.putExtra("playtonext", (Serializable) lp);
                                    intent.putExtra("sdplayer", (Serializable) roundmac);
                                    intent.putExtra("pool", mpotpool + "");
                                    intent.putExtra("ifbtfold",ifbtfold[0]);
                                    startActivityForResult(intent, 1);
                                }
                                if (b_round < 4) {
                                    b_round++;
                                    cancheck[0]=true;
                                    round.setText(b_round + "");
                                } else {
                                    Intent intent = new Intent(BattleActivity.this, EndAcitivity.class);
                                    intent.putExtra("playtonext", (Serializable) lp);
                                    intent.putExtra("sdplayer", (Serializable) roundmac);
                                    intent.putExtra("pool", mpotpool + "");
                                    intent.putExtra("ifbtfold",ifbtfold[0]);
                                    startActivityForResult(intent,1);
                                }
                                pnum = 1;
                                betpoint.setText(blind + "");
                                for (int i = 0; i < lp.size(); i++) {
                                    if (lp.get(i).getId() == roundmac.get((pnum) % roundmac.size()).getBattleid()) {
                                        player.setText(lp.get(i).getName());
                                        ttpoint.setText(lp.get(i).getPoints() + "");
                                    }
                                }
                                for (int i = 0; i < roundmac.size(); i++) {
                                    roundmac.get(i).setBetnum(0);
                                }

                            } else {
                                //找到对应id的信息
                                    for (int i = 0; i < lp.size(); i++) {
                                        if (lp.get(i).getId() == roundmac.get((pnum) % roundmac.size()).getBattleid()) {
                                            player.setText(lp.get(i).getName());
                                            ttpoint.setText(lp.get(i).getPoints() + "");
                                        }
                                    }
                            }
                            //加上bet
                        } else {
                            if (pnum == roundmac.size()) {
                                ifbtfold[0]=true;
                                int betp = Integer.parseInt(betpoint.getText().toString());
                                roundmac.get((pnum) % roundmac.size()).setBetnum(betp);
                                if (ifbetsame(roundmac)) {
                                    if(ifp_allin[0])
                                    {
                                        Intent intent = new Intent(BattleActivity.this, EndAcitivity.class);
                                        intent.putExtra("playtonext", (Serializable) lp);
                                        intent.putExtra("sdplayer", (Serializable) roundmac);
                                        intent.putExtra("pool", mpotpool + "");
                                        intent.putExtra("ifbtfold",ifbtfold[0]);
                                        startActivityForResult(intent, 1);
                                    }
                                    if (b_round < 4) {
                                        b_round++;
                                        cancheck[0]=true;
                                        round.setText(b_round + "");
                                    } else {
                                        Intent intent = new Intent(BattleActivity.this, EndAcitivity.class);
                                        intent.putExtra("playtonext", (Serializable) lp);
                                        intent.putExtra("sdplayer", (Serializable) roundmac);
                                        intent.putExtra("pool", mpotpool + "");
                                        intent.putExtra("ifbtfold",ifbtfold[0]);
                                        startActivityForResult(intent,1);
                                    }
                                    pnum = 1;
                                    betpoint.setText(blind + "");
                                    for (int i = 0; i < lp.size(); i++) {
                                        if (lp.get(i).getId() == roundmac.get((pnum) % roundmac.size()).getBattleid()) {
                                            player.setText(lp.get(i).getName());
                                            ttpoint.setText(lp.get(i).getPoints() + "");
                                        }
                                    }
                                    for (int i = 0; i < roundmac.size(); i++) {
                                        roundmac.get(i).setBetnum(0);
                                    }
                                }
                            }
                            pnum = 1;
                            for (int i = 0; i < lp.size(); i++) {
                                if (lp.get(i).getId() == roundmac.get((pnum) % roundmac.size()).getBattleid()) {
                                    player.setText(lp.get(i).getName());
                                    ttpoint.setText(lp.get(i).getPoints() + "");
                                }
                            }
                        }
                    }
                }
            });
            check.setOnClickListener(new View.OnClickListener() {
                //check的思想是将Betnumm设置为跟注额度，但是不在玩家总点数做任何操作，相当于
                //0花费参与对局，下面和call几乎一致。
                @Override
                public void onClick(View v) {
                    if (cancheck[0]) {
                        if (pnum < roundmac.size()) {
                            int betp = Integer.parseInt(betpoint.getText().toString());
                            roundmac.get((pnum) % roundmac.size()).setBetnum(betp);
                            if (ifbetsame(roundmac)) {
                                if(ifp_allin[0])
                                {
                                    Intent intent = new Intent(BattleActivity.this, EndAcitivity.class);
                                    intent.putExtra("playtonext", (Serializable) lp);
                                    intent.putExtra("sdplayer", (Serializable) roundmac);
                                    intent.putExtra("pool", mpotpool + "");
                                    intent.putExtra("ifbtfold",ifbtfold[0]);
                                    startActivityForResult(intent, 1);
                                }
                                if (b_round < 4) {
                                    b_round++;
                                    cancheck[0]=true;
                                    round.setText(b_round + "");
                                } else {
                                    Intent intent = new Intent(BattleActivity.this, EndAcitivity.class);
                                    intent.putExtra("playtonext", (Serializable) lp);
                                    intent.putExtra("sdplayer", (Serializable) roundmac);
                                    intent.putExtra("pool", mpotpool + "");
                                    intent.putExtra("ifbtfold",ifbtfold[0]);
                                    startActivityForResult(intent,1);
                                }
                                pnum = 1;
                                betpoint.setText(blind + "");
                                for (int i = 0; i < lp.size(); i++) {
                                    if (lp.get(i).getId() == roundmac.get((pnum) % roundmac.size()).getBattleid()) {
                                        player.setText(lp.get(i).getName());
                                        ttpoint.setText(lp.get(i).getPoints() + "");
                                    }
                                }
                                for (int i = 0; i < roundmac.size(); i++) {
                                    roundmac.get(i).setBetnum(0);
                                }

                            } else {
                                //找到对应id的信息
                                for (int i = 0; i < lp.size(); i++) {
                                    if (lp.get(i).getId() == roundmac.get((pnum + 1) % roundmac.size()).getBattleid()) {
                                        player.setText(lp.get(i).getName());
                                        ttpoint.setText(lp.get(i).getPoints() + "");
                                    }
                                }
                                pnum++;
                            }
                            //加上bet
                        } else {
                            if (pnum == roundmac.size()) {
                                int betp = Integer.parseInt(betpoint.getText().toString());
                                roundmac.get((pnum) % roundmac.size()).setBetnum(betp);
                                if (ifbetsame(roundmac)) {
                                    if(ifp_allin[0])
                                    {
                                        Intent intent = new Intent(BattleActivity.this, EndAcitivity.class);
                                        intent.putExtra("playtonext", (Serializable) lp);
                                        intent.putExtra("sdplayer", (Serializable) roundmac);
                                        intent.putExtra("pool", mpotpool + "");
                                        intent.putExtra("ifbtfold",ifbtfold[0]);
                                        startActivityForResult(intent, 1);
                                    }
                                    if (b_round < 4) {
                                        b_round++;
                                        cancheck[0]=true;
                                        round.setText(b_round + "");
                                    } else {
                                        Intent intent = new Intent(BattleActivity.this, EndAcitivity.class);
                                        intent.putExtra("playtonext", (Serializable) lp);
                                        intent.putExtra("sdplayer", (Serializable) roundmac);
                                        intent.putExtra("pool", mpotpool + "");
                                        intent.putExtra("ifbtfold",ifbtfold[0]);
                                        startActivityForResult(intent,1);
                                    }
                                    pnum = 1;
                                    betpoint.setText(blind + "");
                                    for (int i = 0; i < lp.size(); i++) {
                                        if (lp.get(i).getId() == roundmac.get((pnum) % roundmac.size()).getBattleid()) {
                                            player.setText(lp.get(i).getName());
                                            ttpoint.setText(lp.get(i).getPoints() + "");
                                        }
                                    }
                                    for (int i = 0; i < roundmac.size(); i++) {
                                        roundmac.get(i).setBetnum(0);
                                    }
                                }
                            }
                            pnum = 1;
                            for (int i = 0; i < lp.size(); i++) {
                                if (lp.get(i).getId() == roundmac.get((pnum) % roundmac.size()).getBattleid()) {
                                    player.setText(lp.get(i).getName());
                                    ttpoint.setText(lp.get(i).getPoints() + "");
                                }
                            }
                        }
                    }
                }
            });
            b_p.setOnClickListener(new View.OnClickListener() {//这是下注额度旁边的加号按钮，让额度增加
                @Override
                public void onClick(View v) {
                    CharSequence num1 = betpoint.getText();
                    if (b_round == 1)
                        betpoint.setText(r_plusb(num1, Integer.parseInt(ttpoint.getText().toString())));
                    else {
                        if (pnum >= 1) {
                            betpoint.setText(r_plus(num1, blind, Integer.parseInt(ttpoint.getText().toString())));
                        }
                    }
                }
            });
            b_c.setOnClickListener(new View.OnClickListener() {//同上，是减少
                @Override
                public void onClick(View v) {
                    CharSequence num1 = betpoint.getText();
                    if (b_round == 1)
                        betpoint.setText(r_costb(num1));
                    else {
                        if (pnum >= 1) {
                            betpoint.setText(r_cost(num1, blind, roundmac.get(pnum).getBetnum()));
                        }
                    }
                }
            });
            allin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    betpoint.setText(ttpoint.getText());//点一下额度变为全押值，同时ifp_allin为true
                    ifp_allin[0]=true;
                }
            });
    }
    @Override//在进入结算界面后，我们需要回到新一轮。下面的逻辑代码和上面相同
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case 1:
                if(resultCode==2){
                    final boolean[] ifcancheck={false};
                    final boolean[] ifp_allin={false};
                    final boolean[] ifbtfold={false};
                    final  boolean[]  mswitch={false};
                    final  boolean[] ifnot={true};
                    pnum=1;blind=0;match++;b_round =1;mpotpool=0;betini=10;
                    lp=(List<Player>) data.getSerializableExtra("nlist");
                    TextView round = (TextView) findViewById(R.id.round);
                    TextView player = (TextView) findViewById(R.id.b_name);
                    //recive the playmessage
                    List<Battle> roundmac = new ArrayList<>();
                    TextView b_match = (TextView) findViewById(R.id.matchnum);
                    TextView ttpoint = (TextView) findViewById(R.id.ttpoint);
                    TextView potpool = (TextView) findViewById(R.id.potpool);
                    TextView betpoint = (TextView) findViewById(R.id.betpoint);
                    Button call = (Button) findViewById(R.id.call);
                    Button check = (Button) findViewById(R.id.check);
                    Button fold = (Button) findViewById(R.id.fold);
                    Button b_p = (Button) findViewById(R.id.risep);
                    Button b_c = (Button) findViewById(R.id.risec);
                    Button allin = (Button) findViewById(R.id.allin);
                    betpoint.setText(betini+"");
                    b_match.setText(match+"");
                    round.setText(b_round+"");
                    potpool.setText(mpotpool+"");
                    player.setText(lp.get(1).getName());
                    ttpoint.setText(lp.get(1).getPoints() + "");
                    //
                    for (int i = 0; i < lp.size(); i++) {
                        Battle battle = new Battle();
                        battle.setBattleid(lp.get(i).getId());
                        battle.setBetnum(0);
                        roundmac.add(battle);
                    }
                    call.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ifcancheck[0]=false;
                            if (pnum < roundmac.size()) {
                                mswitch[0]=true;
                                int r_round = Integer.parseInt(round.getText().toString());
                                int betp = Integer.parseInt(betpoint.getText().toString());
                                if (pnum == 1&&r_round == 1&&ifnot[0]) {
                                    blind = betp;
                                    betpoint.setText((blind * 2) + "");
                                    ifnot[0]=false;
                                }
                                int font = roundmac.get((pnum) % roundmac.size()).getBetnum();
                                roundmac.get((pnum) % roundmac.size()).setBetnum(betp);
                                for (int i = 0; i < lp.size(); i++) {
                                    if (lp.get(i).getId() == roundmac.get((pnum) % roundmac.size()).getBattleid()) {
                                        lp.get(i).setPoints(lp.get(i).getPoints() - (betp - font));
                                        mpotpool += (betp - font);
                                        potpool.setText(mpotpool + "");
                                    }
                                }
                                if (ifbetsame(roundmac)) {
                                    if(ifp_allin[0])
                                    {
                                        Intent intent = new Intent(BattleActivity.this, EndAcitivity.class);
                                        intent.putExtra("playtonext", (Serializable) lp);
                                        intent.putExtra("sdplayer", (Serializable) roundmac);
                                        intent.putExtra("pool", mpotpool + "");
                                        intent.putExtra("ifbtfold",ifbtfold[0]);
                                        startActivityForResult(intent, 1);
                                    }
                                    if (b_round < 4) {
                                        b_round++;
                                        ifcancheck[0]=true;
                                        round.setText(b_round + "");
                                    } else {//进入摊牌页面的结算.
                                        Intent intent = new Intent(BattleActivity.this, EndAcitivity.class);
                                        intent.putExtra("playtonext", (Serializable) lp);
                                        intent.putExtra("sdplayer", (Serializable) roundmac);
                                        intent.putExtra("pool", mpotpool + "");
                                        intent.putExtra("ifbtfold",ifbtfold[0]);
                                        startActivityForResult(intent, 1);
                                    }
                                    pnum = 1;
                                    betpoint.setText(blind + "");
                                    for (int i = 0; i < lp.size(); i++) {
                                        if (lp.get(i).getId() == roundmac.get((pnum) % roundmac.size()).getBattleid()) {
                                            player.setText(lp.get(i).getName());
                                            ttpoint.setText(lp.get(i).getPoints() + "");
                                        }
                                    }
                                    for (int i = 0; i < roundmac.size(); i++) {
                                        roundmac.get(i).setBetnum(0);
                                    }
                                } else {
                                    //找到对应id的信息
                                    if(pnum==roundmac.size()-1&&ifbtfold[0])
                                    {
                                        pnum=1;
                                        roundmac.get(0).setBetnum(betp);
                                        for (int i = 0; i < lp.size(); i++) {
                                            if (lp.get(i).getId() == roundmac.get((pnum) % roundmac.size()).getBattleid()) {
                                                player.setText(lp.get(i).getName());
                                                ttpoint.setText(lp.get(i).getPoints() + "");
                                            }
                                        }
                                    }else{
                                        for (int i = 0; i < lp.size(); i++) {
                                            if (lp.get(i).getId() == roundmac.get((pnum + 1) % roundmac.size()).getBattleid()) {
                                                player.setText(lp.get(i).getName());
                                                ttpoint.setText(lp.get(i).getPoints() + "");
                                            }
                                        }pnum++;
                                    }
                                }
                                //加上bet
                            } else {
                                if (pnum == roundmac.size()) {
                                    Log.v(TAG, blind + "");
                                    int font = roundmac.get((pnum) % roundmac.size()).getBetnum();
                                    int betp = Integer.parseInt(betpoint.getText().toString());
                                    roundmac.get((pnum) % roundmac.size()).setBetnum(betp);
                                    for (int i = 0; i < lp.size(); i++) {
                                        if (lp.get(i).getId() == roundmac.get((pnum) % roundmac.size()).getBattleid()) {
                                            lp.get(i).setPoints(lp.get(i).getPoints() - (betp - font));
                                            mpotpool += (betp - font);
                                            potpool.setText(mpotpool + "");
                                        }
                                    }
                                    if (ifbetsame(roundmac)) {
                                        if(ifp_allin[0])
                                        {
                                            Intent intent = new Intent(BattleActivity.this, EndAcitivity.class);
                                            intent.putExtra("playtonext", (Serializable) lp);
                                            intent.putExtra("sdplayer", (Serializable) roundmac);
                                            intent.putExtra("pool", mpotpool + "");
                                            intent.putExtra("ifbtfold",ifbtfold[0]);
                                            startActivityForResult(intent, 1);
                                        }
                                        if (b_round < 4) {
                                            b_round++;
                                            ifcancheck[0]=true;
                                            round.setText(b_round + "");
                                        } else {
                                            Intent intent = new Intent(BattleActivity.this, EndAcitivity.class);
                                            intent.putExtra("playtonext", (Serializable) lp);
                                            intent.putExtra("sdplayer", (Serializable) roundmac);
                                            intent.putExtra("pool", mpotpool + "");
                                            intent.putExtra("ifbtfold",ifbtfold[0]);
                                            startActivityForResult(intent,1);
                                        }
                                        pnum = 1;
                                        betpoint.setText(blind + "");
                                        for (int i = 0; i < lp.size(); i++) {
                                            if (lp.get(i).getId() == roundmac.get((pnum) % roundmac.size()).getBattleid()) {
                                                player.setText(lp.get(i).getName());
                                                ttpoint.setText(lp.get(i).getPoints() + "");
                                            }
                                        }
                                        for (int i = 0; i < roundmac.size(); i++) {
                                            roundmac.get(i).setBetnum(0);
                                        }
                                    }
                                }
                                pnum = 1;
                                for (int i = 0; i < lp.size(); i++) {
                                    if (lp.get(i).getId() == roundmac.get((pnum) % roundmac.size()).getBattleid()) {
                                        player.setText(lp.get(i).getName());
                                        ttpoint.setText(lp.get(i).getPoints() + "");
                                    }
                                }
                            }
                        }
                    });
                    fold.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(roundmac.size()==2){
                                Intent intent = new Intent(BattleActivity.this, EndAcitivity.class);
                                intent.putExtra("playtonext", (Serializable) lp);
                                intent.putExtra("sdplayer", (Serializable) roundmac);
                                intent.putExtra("pool", mpotpool + "");
                                intent.putExtra("ifbtfold",ifbtfold[0]);
                                startActivityForResult(intent,1);
                            }
                            if (mswitch[0]) {
                                if (pnum < roundmac.size()) {
                                    roundmac.remove(pnum);
                                    if (ifbetsame(roundmac)) {
                                        if(ifp_allin[0])
                                        {
                                            Intent intent = new Intent(BattleActivity.this, EndAcitivity.class);
                                            intent.putExtra("playtonext", (Serializable) lp);
                                            intent.putExtra("sdplayer", (Serializable) roundmac);
                                            intent.putExtra("pool", mpotpool + "");
                                            intent.putExtra("ifbtfold",ifbtfold[0]);
                                            startActivityForResult(intent, 1);
                                        }
                                        if (b_round < 4) {
                                            b_round++;
                                            ifcancheck[0]=true;
                                            round.setText(b_round + "");
                                        } else {
                                            Intent intent = new Intent(BattleActivity.this, EndAcitivity.class);
                                            intent.putExtra("playtonext", (Serializable) lp);
                                            intent.putExtra("sdplayer", (Serializable) roundmac);
                                            intent.putExtra("pool", mpotpool + "");
                                            startActivityForResult(intent,1);
                                        }
                                        pnum = 1;
                                        betpoint.setText(blind + "");
                                        for (int i = 0; i < lp.size(); i++) {
                                            if (lp.get(i).getId() == roundmac.get((pnum) % roundmac.size()).getBattleid()) {
                                                player.setText(lp.get(i).getName());
                                                ttpoint.setText(lp.get(i).getPoints() + "");
                                            }
                                        }
                                        for (int i = 0; i < roundmac.size(); i++) {
                                            roundmac.get(i).setBetnum(0);
                                        }

                                    } else {
                                        //找到对应id的信息
                                        for (int i = 0; i < lp.size(); i++) {
                                            if (lp.get(i).getId() == roundmac.get((pnum) % roundmac.size()).getBattleid()) {
                                                player.setText(lp.get(i).getName());
                                                ttpoint.setText(lp.get(i).getPoints() + "");
                                            }
                                        }
                                    }
                                    //加上bet
                                } else {
                                    if (pnum == roundmac.size()) {
                                        ifbtfold[0]=true;
                                        int betp = Integer.parseInt(betpoint.getText().toString());
                                        roundmac.get((pnum) % roundmac.size()).setBetnum(betp);
                                        if (ifbetsame(roundmac)) {
                                            if(ifp_allin[0])
                                            {
                                                Intent intent = new Intent(BattleActivity.this, EndAcitivity.class);
                                                intent.putExtra("playtonext", (Serializable) lp);
                                                intent.putExtra("sdplayer", (Serializable) roundmac);
                                                intent.putExtra("pool", mpotpool + "");
                                                intent.putExtra("ifbtfold",ifbtfold[0]);
                                                startActivityForResult(intent, 1);
                                            }
                                            if (b_round < 4) {
                                                b_round++;
                                                ifcancheck[0]=true;
                                                round.setText(b_round + "");
                                            } else {
                                                Intent intent = new Intent(BattleActivity.this, EndAcitivity.class);
                                                intent.putExtra("playtonext", (Serializable) lp);
                                                intent.putExtra("sdplayer", (Serializable) roundmac);
                                                intent.putExtra("pool", mpotpool + "");
                                                intent.putExtra("ifbtfold",ifbtfold[0]);
                                                startActivityForResult(intent,1);
                                            }
                                            pnum = 1;
                                            betpoint.setText(blind + "");
                                            for (int i = 0; i < lp.size(); i++) {
                                                if (lp.get(i).getId() == roundmac.get((pnum) % roundmac.size()).getBattleid()) {
                                                    player.setText(lp.get(i).getName());
                                                    ttpoint.setText(lp.get(i).getPoints() + "");
                                                }
                                            }
                                            for (int i = 0; i < roundmac.size(); i++) {
                                                roundmac.get(i).setBetnum(0);
                                            }
                                        }
                                    }
                                    pnum = 1;
                                    for (int i = 0; i < lp.size(); i++) {
                                        if (lp.get(i).getId() == roundmac.get((pnum) % roundmac.size()).getBattleid()) {
                                            player.setText(lp.get(i).getName());
                                            ttpoint.setText(lp.get(i).getPoints() + "");
                                        }
                                    }
                                }
                            }
                        }
                    });
                    check.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (ifcancheck[0]) {
                                if (pnum < roundmac.size()) {
                                    int r_round = Integer.parseInt(round.getText().toString());
                                    int betp = Integer.parseInt(betpoint.getText().toString());
                                    roundmac.get((pnum) % roundmac.size()).setBetnum(betp);
                                    if (ifbetsame(roundmac)) {
                                        if(ifp_allin[0])
                                        {
                                            Intent intent = new Intent(BattleActivity.this, EndAcitivity.class);
                                            intent.putExtra("playtonext", (Serializable) lp);
                                            intent.putExtra("sdplayer", (Serializable) roundmac);
                                            intent.putExtra("pool", mpotpool + "");
                                            intent.putExtra("ifbtfold",ifbtfold[0]);
                                            startActivityForResult(intent, 1);
                                        }
                                        if (b_round < 4) {
                                            b_round++;
                                            ifcancheck[0]=true;
                                            round.setText(b_round + "");
                                        } else {
                                            Intent intent = new Intent(BattleActivity.this, EndAcitivity.class);
                                            intent.putExtra("playtonext", (Serializable) lp);
                                            intent.putExtra("sdplayer", (Serializable) roundmac);
                                            intent.putExtra("pool", mpotpool + "");
                                            intent.putExtra("ifbtfold",ifbtfold[0]);
                                            startActivityForResult(intent,1);
                                        }
                                        pnum = 1;
                                        betpoint.setText(blind + "");
                                        for (int i = 0; i < lp.size(); i++) {
                                            if (lp.get(i).getId() == roundmac.get((pnum) % roundmac.size()).getBattleid()) {
                                                player.setText(lp.get(i).getName());
                                                ttpoint.setText(lp.get(i).getPoints() + "");
                                            }
                                        }
                                        for (int i = 0; i < roundmac.size(); i++) {
                                            roundmac.get(i).setBetnum(0);
                                        }

                                    } else {
                                        //找到对应id的信息
                                        for (int i = 0; i < lp.size(); i++) {
                                            if (lp.get(i).getId() == roundmac.get((pnum + 1) % roundmac.size()).getBattleid()) {
                                                player.setText(lp.get(i).getName());
                                                ttpoint.setText(lp.get(i).getPoints() + "");
                                            }
                                        }
                                        pnum++;
                                    }
                                    //加上bet
                                } else {
                                    if (pnum == roundmac.size()) {
                                        int betp = Integer.parseInt(betpoint.getText().toString());
                                        roundmac.get((pnum) % roundmac.size()).setBetnum(betp);
                                        if (ifbetsame(roundmac)) {
                                            if(ifp_allin[0])
                                            {
                                                Intent intent = new Intent(BattleActivity.this, EndAcitivity.class);
                                                intent.putExtra("playtonext", (Serializable) lp);
                                                intent.putExtra("sdplayer", (Serializable) roundmac);
                                                intent.putExtra("pool", mpotpool + "");
                                                intent.putExtra("ifbtfold",ifbtfold[0]);
                                                startActivityForResult(intent, 1);
                                            }
                                            if (b_round < 4) {
                                                b_round++;
                                                ifcancheck[0]=true;
                                                round.setText(b_round + "");
                                            } else {
                                                Intent intent = new Intent(BattleActivity.this, EndAcitivity.class);
                                                intent.putExtra("playtonext", (Serializable) lp);
                                                intent.putExtra("sdplayer", (Serializable) roundmac);
                                                intent.putExtra("pool", mpotpool + "");
                                                intent.putExtra("ifbtfold",ifbtfold[0]);
                                                startActivityForResult(intent,1);
                                            }
                                            pnum = 1;
                                            betpoint.setText(blind + "");
                                            for (int i = 0; i < lp.size(); i++) {
                                                if (lp.get(i).getId() == roundmac.get((pnum) % roundmac.size()).getBattleid()) {
                                                    player.setText(lp.get(i).getName());
                                                    ttpoint.setText(lp.get(i).getPoints() + "");
                                                }
                                            }
                                            for (int i = 0; i < roundmac.size(); i++) {
                                                roundmac.get(i).setBetnum(0);
                                            }
                                        }
                                    }
                                    pnum = 1;
                                    for (int i = 0; i < lp.size(); i++) {
                                        if (lp.get(i).getId() == roundmac.get((pnum) % roundmac.size()).getBattleid()) {
                                            player.setText(lp.get(i).getName());
                                            ttpoint.setText(lp.get(i).getPoints() + "");
                                        }
                                    }
                                }
                            }
                        }
                    });
                    b_p.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            CharSequence num1 = betpoint.getText();
                            if (b_round == 1)
                                betpoint.setText(r_plusb(num1, Integer.parseInt(ttpoint.getText().toString())));
                            else {
                                if (pnum >= 1) {
                                    betpoint.setText(r_plus(num1, blind, Integer.parseInt(ttpoint.getText().toString())));
                                }
                            }
                        }
                    });
                    b_c.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            CharSequence num1 = betpoint.getText();
                            if (b_round == 1)
                                betpoint.setText(r_costb(num1));
                            else {
                                if (pnum >= 1) {
                                    betpoint.setText(r_cost(num1, blind, roundmac.get(pnum).getBetnum()));
                                }
                            }
                        }
                    });
                    allin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            betpoint.setText(ttpoint.getText());
                            ifp_allin[0]=true;
                        }
                    });
                }
        }

    }
    public boolean ifbetsame(List<Battle> lb)
    {
        HashSet<Integer> hs=new HashSet<>();
        for(int i=0;i<lb.size();i++)
        {
            hs.add(lb.get(i).getBetnum());
        }
        if(hs.size()==1)
            return true;
        else
            return false;
    }
    public CharSequence r_plus(CharSequence cs,int cost,int ttpoint)
    {
        CharSequence result;
        int num =Integer.parseInt(cs.toString());
        if(num!=ttpoint){
            result=(num+cost)+"";
        }else
        {
            result=num+"";
        }
        return result;
    }
    public CharSequence r_cost(CharSequence cs,int cost,int limtbet)
    {
        CharSequence result;
        int num=Integer.parseInt(cs.toString());
        if(num!=limtbet) {
            result = (num -cost) + "";
        }else
            result=num+"";
        return  result;
    }
    public CharSequence r_plusb(CharSequence cs,int ttpoint)
    {
        CharSequence result;
        int num =Integer.parseInt(cs.toString());
        if(num!=ttpoint){
            result=(num+10)+"";
        }else
        {
            result=num+"";
        }
        return result;
    }
    public CharSequence r_costb(CharSequence cs)
    {
        CharSequence result;
        int num=Integer.parseInt(cs.toString());
        if(num!=0) {
            result = (num -10) + "";
        }else
            result=num+"";
        return  result;
    }

}
