package com.example.a45773.Main;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.a45773.Db.DBManager;
import com.example.a45773.bean.Gjcbean;

import java.util.List;

public class SetGuoLV extends Activity{
    List<Gjcbean> strings;
    private DBManager DB;
    private MyAdapter adapter;
    private int flag = 1;
    private TextView textView;
    private int flag2;
    private ListView listView;
    private TextView glpb;
    private TextView gift;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //状态栏 @ 顶部
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //A //导航栏 @ 底部
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        setContentView(R.layout.activity_set_guo_lv);

        DB = new DBManager(getApplicationContext());
        textView = (TextView) findViewById(R.id.t);

        strings = DB.queryallgjcn();
        Gjcbean gjcbean = new Gjcbean();
        Gjcbean gjcbean1 = new Gjcbean();
        gjcbean.setName("添加");
        gjcbean.setText("添加");
        gjcbean1.setName("选中所有");
        gjcbean.setText("选中所有");
        strings.add(gjcbean);
        strings.add(gjcbean1);

        flag2 = DB.queryallset().getgl();

        glpb=(TextView)findViewById(R.id.glpb_boutton);
        gift=(TextView)findViewById(R.id.gift_boutton);
        setglpb();
        setgift();
        if (flag2 == 1)
            textView.setText("已开启");
        else
            textView.setText("已关闭");

        listView = (ListView) findViewById(R.id.list);
        listView.setVisibility(View.GONE);
        adapter = new MyAdapter();



        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view, int position, long id) {
                final EditText inputServer = new EditText(SetGuoLV.this);
                final int i=position;
                inputServer.setFocusable(true);
                AlertDialog.Builder builder = new AlertDialog.Builder(SetGuoLV.this,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                if (position<strings.size()-2){
                    //delete_single
                    builder.setTitle("选中了'"+strings.get(position).getText()+"'，是否删除？").setNegativeButton(
                            "no", null);
                    builder.setPositiveButton("yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    if(flag==1){
                                        DB.delgjcn(strings.get(i).getText());
                                        strings=DB.queryallgjcn();
                                        Gjcbean gjcbean = new Gjcbean();
                                        Gjcbean gjcbean1= new Gjcbean();
                                        gjcbean.setName("添加");
                                        gjcbean.setText("添加");
                                        gjcbean1.setName("选中所有");
                                        gjcbean.setText("选中所有");
                                        strings.add(gjcbean);
                                        strings.add(gjcbean1);
                                        adapter.notifyDataSetChanged();}
                                    else{
                                        DB.delgjc(strings.get(i).getText());
                                        strings=DB.queryallgjc();
                                        Gjcbean gjcbean = new Gjcbean();
                                        Gjcbean gjcbean1= new Gjcbean();
                                        gjcbean.setName("添加");
                                        gjcbean.setText("添加");
                                        gjcbean1.setName("选中所有");
                                        gjcbean.setText("选中所有");
                                        strings.add(gjcbean);
                                        strings.add(gjcbean1);
                                        adapter.notifyDataSetChanged();}

                                }

                            });

                }
                else if (position==strings.size()-2){
                    //add
                    builder.setTitle("添加").setView(inputServer).setNegativeButton(
                            "no", null);

                    builder.setPositiveButton("yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Gjcbean g =new Gjcbean();
                                    g.setName(inputServer.getText().toString());
                                    g.setText(inputServer.getText().toString());
                                    if(flag==1){
                                        DB.addgjcn(g);
                                        strings=DB.queryallgjcn();
                                        Gjcbean gjcbean = new Gjcbean();
                                        Gjcbean gjcbean1= new Gjcbean();
                                        gjcbean.setName("添加");
                                        gjcbean.setText("添加");
                                        gjcbean1.setName("选中所有");
                                        gjcbean.setText("选中所有");
                                        strings.add(gjcbean);
                                        strings.add(gjcbean1);
                                        adapter.notifyDataSetChanged();}
                                    else{
                                        DB.addgjc(g);
                                        strings=DB.queryallgjc();
                                        Gjcbean gjcbean = new Gjcbean();
                                        Gjcbean gjcbean1= new Gjcbean();
                                        gjcbean.setName("添加");
                                        gjcbean.setText("添加");
                                        gjcbean1.setName("选中所有");
                                        gjcbean.setText("选中所有");
                                        strings.add(gjcbean);
                                        strings.add(gjcbean1);
                                        adapter.notifyDataSetChanged();}

                                }
                            });

                }
                else{
                    //delete_all
                    builder.setTitle("选中了所有，是否删除？").setNegativeButton(
                            "no", null).setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(flag==1){
                                DB.delallgjcn();
                                strings=DB.queryallgjcn();
                                Gjcbean gjcbean = new Gjcbean();
                                Gjcbean gjcbean1= new Gjcbean();
                                gjcbean.setName("添加");
                                gjcbean.setText("添加");
                                gjcbean1.setName("选中所有");
                                gjcbean.setText("选中所有");
                                strings.add(gjcbean);
                                strings.add(gjcbean1);
                                adapter.notifyDataSetChanged();}
                            else {
                                DB.delallgjc();
                                strings=DB.queryallgjc();
                                Gjcbean gjcbean = new Gjcbean();
                                Gjcbean gjcbean1= new Gjcbean();
                                gjcbean.setName("添加");
                                gjcbean.setText("添加");
                                gjcbean1.setName("选中所有");
                                gjcbean.setText("选中所有");
                                strings.add(gjcbean);
                                strings.add(gjcbean1);
                                adapter.notifyDataSetChanged();
                            }

                        }
                    });

                }



                builder.show();

            }
        });

    }

    public void setgift() {
        if(DB.queryallset().getGift()==1) {
            gift.setText("已开启");
        }
        else {
            gift.setText("已关闭");
        }
    }

    public void gift(View v)
    {
        if(DB.queryallset().getGift()==1) {
            DB.updataset("gift",0);
        }
        else {
            DB.updataset("gift",1);
        }
        setgift();
    }

    public void black(View view) {
        strings = DB.queryallgjcn();
        flag = 1;
        Gjcbean gjcbean = new Gjcbean();
        Gjcbean gjcbean1 = new Gjcbean();
        gjcbean.setName("添加");
        gjcbean.setText("添加");
        gjcbean1.setName("选中所有");
        gjcbean.setText("选中所有");
        strings.add(gjcbean);
        strings.add(gjcbean1);
        // textView.setText("黑名单");
        adapter.notifyDataSetChanged();
        listView.setVisibility(View.VISIBLE);
    }

    public void mg(View view) {

        strings = DB.queryallgjc();
        Gjcbean gjcbean = new Gjcbean();
        Gjcbean gjcbean1 = new Gjcbean();
        gjcbean.setName("添加");
        gjcbean.setText("添加");
        gjcbean1.setName("选中所有");
        gjcbean.setText("选中所有");
        strings.add(gjcbean);
        strings.add(gjcbean1);
        flag = 2;
        // textView.setText("敏感词");
        adapter.notifyDataSetChanged();
        listView.setVisibility(View.VISIBLE);
    }
    public void setglpb()
    {
        if(DB.queryallset().getGlpb()==1) {
            glpb.setText("已开启");
        }
        else {
            glpb.setText("已关闭");
        }
    }
    public void glpb(View v)
    {
        if(DB.queryallset().getGlpb()==1) {
            DB.updataset("glpb",0);
            glpb.setText("已关闭");
        }
        else {
            DB.updataset("glpb",1);
            glpb.setText("已开启");
        }
    }

    public void turn(View view) {
        if (flag2 == 1) {
            flag2 = 0;
            DB.updataset("guolv", 0);
            textView.setText("已关闭");
        } else {
            flag2 = 1;
            DB.updataset("guolv", 1);
            textView.setText("已开启");
        }
    }


    public void fanhuigl(View view) {
        this.finish();
        overridePendingTransition(0, R.anim.out_to_right);
    }

    public class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return strings.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = null;
            if (convertView == null) {
                view = View.inflate(getApplicationContext(), R.layout.item2, null);
            } else {
                view = convertView;
            }
            TextView name = (TextView) view.findViewById(R.id.name);
            name.setText(strings.get(position).getName());
            name.setBackgroundColor(Color.parseColor("#00ffffff"));
            if (position == strings.size() - 1 || position == strings.size() - 2)
                name.setBackgroundColor(Color.parseColor("#55ffffff"));
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
}
