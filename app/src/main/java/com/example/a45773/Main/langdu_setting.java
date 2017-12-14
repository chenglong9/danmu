package com.example.a45773.Main;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.a45773.Db.DBManager;
import com.example.a45773.Db.SqlHelper;
import com.example.a45773.bean.Replybean;

import java.util.ArrayList;
import java.util.List;


public class langdu_setting extends Activity {
    private SqlHelper dbHelper;   //定义数据库
    private DBManager DB;
    private TextView tv_langdu;
    private TextView gift;
    private List<Replybean> replys = new ArrayList<Replybean>();
    private MyAdapter adapter;
    private ListView replyList;
    private RelativeLayout replyBySpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //状态栏 @ 顶部
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //A //导航栏 @ 底部
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        setContentView(R.layout.activity_langdu_setting);

        tv_langdu = (TextView) findViewById(R.id.tv_langdu);
        gift = (TextView) findViewById(R.id.gift_set);
        replyList = (ListView) findViewById(R.id.list);
        replyBySpeech = (RelativeLayout) findViewById(R.id.top3);
        DB = new DBManager(getApplicationContext());
        initData();
        adapter = new MyAdapter(this, R.layout.item4, replys);
        replyList.setAdapter(adapter);
        replyBySpeech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replyList.setVisibility(View.VISIBLE);
            }
        });

        replyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> adapterView, View view, final int position, long id) {
                View view1 = LayoutInflater.from(view.getContext()).inflate(R.layout.item5, null);
                final EditText ask = (EditText) view1.findViewById(R.id.ask);
                final EditText reply = (EditText) view1.findViewById(R.id.reply);
                ask.setFocusable(true);
                AlertDialog.Builder builder = new AlertDialog.Builder(langdu_setting.this,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                if (position < replys.size() - 2) {
                    //delete_single
                    builder.setTitle("选中了问题'" + replys.get(position).getAsk() + "'，是否删除？").setNegativeButton(
                            "no", null);
                    builder.setPositiveButton("yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    String ask = replys.get(position).getAsk();
                                    DB.delreply(ask);
                                    for(int i = 0; i < replys.size(); i++){
                                        if(replys.get(i).getAsk().equals(ask))
                                            replys.remove(i);
                                    }
                                    adapter.notifyDataSetChanged();
                                }
                            });

                } else if (position == replys.size() - 2) {
                    //add
                    builder.setTitle("添加").setView(view1).setNegativeButton(
                            "no", null);

                    builder.setPositiveButton("yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Replybean r = new Replybean(ask.getText().toString(),
                                            reply.getText().toString());
                                    DB.addreply(r);
                                    replys.add(0, r);
                                    adapter.notifyDataSetChanged();
                                }
                            });
                } else {
                    //delete_all
                    builder.setTitle("选中了所有，是否删除？").setNegativeButton(
                            "no", null).setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DB.delAllReply();
                            replys.clear();
                            replys.add(new Replybean("", ""));
                            replys.add(new Replybean("", ""));
                            adapter.notifyDataSetChanged();
                        }
                    });

                }
                builder.show();
            }
        });
        setgift();
        setyuying();
    }

    public void initData() {
        replys = DB.queryAllReplys();
        replys.add(new Replybean("", ""));
        replys.add(new Replybean("", ""));
    }

    public void langdu1(View v) {
        if (DB.queryallset().getLangdu() == 1) {
            DB.updataset("langdu", 0);
        } else {
            DB.updataset("langdu", 1);
        }
        setyuying();
    }

    public void langdugift(View v) {
        if (DB.queryallset().getLiwu() == 1) {
            DB.updataset("liwu", 0);
        } else {
            DB.updataset("liwu", 1);
        }
        setgift();
    }

    public void setgift() {
        if (DB.queryallset().getLiwu() == 1) {
            gift.setText("已开启");
        } else {
            gift.setText("已关闭");
        }
    }

    public void setyuying() {
        if (DB.queryallset().getLangdu() == 1) {
            tv_langdu.setText("已开启");
        } else {
            tv_langdu.setText("已关闭");
        }
    }

    public class MyAdapter extends ArrayAdapter<Replybean> {
        private int resourceId;

        public MyAdapter(Context context, int textViewResourceId, List<Replybean> objects) {
            super(context, textViewResourceId, objects);
            resourceId = textViewResourceId;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Replybean replyBean = getItem(position);
            View view;
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            LinearLayout back = (LinearLayout) view.findViewById(R.id.back);
            TextView ask = (TextView) view.findViewById(R.id.ask);
            TextView reply = (TextView) view.findViewById(R.id.reply);
            ask.setText(replyBean.getAsk());
            reply.setText(replyBean.getReply());
            back.setBackgroundColor(Color.parseColor("#00ffffff"));
            if (position == replys.size() - 1 || position == replys.size() - 2) {
                view = LayoutInflater.from(getContext()).inflate(R.layout.item2, null);
                TextView text = (TextView) view.findViewById(R.id.name);
                text.setBackgroundColor(Color.parseColor("#55ffffff"));
                if (position == replys.size() - 2)
                    text.setText("添加");
                else
                    text.setText("选中所有");
                return view;
            }
            return view;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.finish();
            overridePendingTransition(0, R.anim.out_to_right);
        }
        return true;
    }

    public void fanhuild(View view) {
        this.finish();
        overridePendingTransition(0, R.anim.out_to_right);
    }
}
