package com.example.a45773.Main;

import android.app.Activity;

import android.app.AlertDialog;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;


import com.example.a45773.Db.DBManager;


public class SettingActivity extends Activity {

    private DBManager DB;
    private TextView touming;
    private TextView tongzhi;
/*    private TextView glpb;*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //状态栏 @ 顶部
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //A //导航栏 @ 底部
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        setContentView(R.layout.activity_setting);

        DB=new DBManager(getApplicationContext());
        touming=(TextView)findViewById(R.id.tv_touming);
        tongzhi= (TextView) findViewById(R.id.tongzhi);
/*
        glpb=(TextView)findViewById(R.id.glpb_boutton);
*/

        settm();
        settz();
/*        setglpb();*/

        SeekBar seekBar = (SeekBar) findViewById(R.id.seekbar);
        seekBar.setProgress(DB.queryallset().getSize());
        final TextView textView = (TextView) findViewById(R.id.s2);
        textView.setTextSize((seekBar.getProgress()+10));

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (i<10)
                    i=10;
                textView.setTextSize(i+10);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (seekBar.getProgress()>10)
                DB.updataset("size", seekBar.getProgress());
                else  DB.updataset("size", 10);
            }
        });

    }
    public void settm()
    {
        if(DB.queryallset().getTouming()==1) {
            touming.setText("已开启");
        }
        else {
            touming.setText("已关闭");
        }
    }
    public void settz()
    {
        if(DB.queryallset().getTingzhu()==1) {
            tongzhi.setText("已开启");
        }
        else {
            tongzhi.setText("已关闭");
        }
    }
/*
    public void setglpb()
    {
        if(DB.queryallset().getGlpb()==1) {
            glpb.setText("已开启");
        }
        else {
            glpb.setText("已关闭");
        }
    }
*/


    public void touming(View v)
    {

        if(DB.queryallset().getTouming()==1) {
            DB.updataset("touming",0);
        }
        else {
            DB.updataset("touming",1);
        }
        settm();

    }
    public void tongzhi(View v)
    {
        if(DB.queryallset().getTingzhu()==1) {
            DB.updataset("tingzhu",0);
        }
        else {
            DB.updataset("tingzhu",1);
        }
        settz();

    }
    public void banben(View v)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        builder.setMessage("版本：V1.0\n\n语音技术由科大讯飞提供");
        builder.setPositiveButton("取消",null);
        builder.show();
    }

  /*  public void glpb(View v)
    {
        if(DB.queryallset().getGlpb()==1) {
            DB.updataset("glpb",0);
        }
        else {
            DB.updataset("glpb",1);
        }
        setglpb();
    }*/

    public void fanhuist(View view){
        this.finish();
        overridePendingTransition(0, R.anim.out_to_right);
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            SettingActivity.this.finish();
            overridePendingTransition(0, R.anim.out_to_right);
        }
        return true;
    }
}
