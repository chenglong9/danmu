package com.example.a45773.Main;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import com.example.a45773.Db.DBManager;
import com.example.a45773.bean.Msgbean;
import com.example.a45773.bean.Setbean;
import com.example.a45773.bulletscreen.client.DyBulletScreenClient;
import com.example.a45773.panda.Crawl;

import java.util.ArrayList;
import java.util.Random;

public class Select extends Activity {
    private Chronometer timer;

    private Button button;
    private TextView textView;
    private int ting=1;
    private Setbean set;

    private ArrayList<String> ad=new ArrayList<String>();
    public Handler handler=new Handler() {


        public void handleMessage(Message message) {
            if(ting==1) {
                if (((Msgbean) message.obj).getType().equals("chatmsg"))
                    ad.add(((Msgbean) message.obj).getName());
                if (((Msgbean) message.obj).getType().equals("dgb"))
                    ad.add(((Msgbean) message.obj).getName());
            }
        }

    };
    private DBManager DB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //状态栏 @ 顶部
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //A //导航栏 @ 底部
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        setContentView(R.layout.activity_select);

        timer = (Chronometer)this.findViewById(R.id.chronometer);
        button = (Button) findViewById(R.id.start);
        textView= (TextView) findViewById(R.id.textView2);
        textView.setVisibility(View.GONE);
        DB = new DBManager(getApplicationContext());
        set = DB.queryallset();
    }
    public void start(View view){

        if (button.getText().toString().equals("开始抽取")){
            ad.clear();
            ting=1;
        timer.setBase(SystemClock.elapsedRealtime());
        //开始计时
        timer.start();
            if (set.getType().equals("douyu")) {
                DyBulletScreenClient.sethand(handler);
            }
            else if (set.getType().equals("panda")) {
                Crawl.sethand(handler);
            }
            button.setText("结束");
        }
        else
        {   ting=0;
            button.setText("开始抽取");
            timer.stop();
            if(ad.size()>0){
          int i=  (new Random()).nextInt(ad.size());
            textView.setText(ad.get(i));
            textView.setVisibility(View.VISIBLE);
            }
        }

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            this.finish();
            overridePendingTransition(0, R.anim.out_to_right);
        }
        return true;
    }
    public void fanhuicq(View view){
        this.finish();
        overridePendingTransition(0, R.anim.out_to_right);
    }
}
