package com.example.a45773.Main;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnDismissListener;
import com.bigkoo.alertview.OnItemClickListener;
import com.example.a45773.Db.DBManager;
import com.example.a45773.bean.Giftbean;
import com.example.a45773.bean.Gjcbean;
import com.example.a45773.bean.Msgbean;
import com.example.a45773.bean.Setbean;
import com.example.a45773.bulletscreen.Douyu;
import com.example.a45773.bulletscreen.client.DyBulletScreenClient;
import com.example.a45773.Mydanmu.MainActivityqp;
import com.example.a45773.Mydanmu.Mqp;
import com.example.a45773.panda.Crawl;
import com.iflytek.cloud.SpeechUtility;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MainActivity extends Activity implements OnItemClickListener, OnDismissListener {

    private ListView lv;
    private Myadapter adapter;
    private Context context=this;
    public static ArrayList<Giftbean> giftbeans = new ArrayList<Giftbean>();
    private ArrayList<Msgbean> ad = new ArrayList<Msgbean>();
    private int suo = 0;
    private ImageView suov;
    private ImageView logo;
    private AlertView logoal;
    private AlertView listal;
    private AlertView topal;
    private NotificationManager nm;
    private ImageView del;
    private ImageView qp;
    public static int id ;
    private EditText edtop;
    private RelativeLayout layouttop;
    private DBManager DB;
    private TextView textView;
    private static Setbean set;

    private Thread xh;

    private Intent intent1;
    public String name;
    private List<Gjcbean> gjcbeen;
    private Douyu douyu;
    private ExecutorService service;
    private Crawl panda;
    private Notification noti;
    private View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(final View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        switch (view.getId()) {
                            case R.id.logo:
                                logo.setBackground(getResources().getDrawable(R.drawable.transparent));
                                logo.setPadding(40, 40, 40, 40);
                                break;
                            case R.id.del:
                                del.setBackground(getResources().getDrawable(R.drawable.transparent));
                                del.setPadding(45, 45, 45, 45);
                                break;
                            case R.id.suo:
                                suov.setBackground(getResources().getDrawable(R.drawable.transparent));
                                suov.setPadding(45, 0, 45, 0);
                                break;
                        }
                    }
                }, 500);
            } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                switch (view.getId()) {
                    case R.id.logo:
                        logo.setBackground(getResources().getDrawable(R.drawable.ripple_bg));
                        logo.setPadding(40, 40, 40, 40);
                        break;
                    case R.id.del:
                        del.setBackground(getResources().getDrawable(R.drawable.ripple_bg1));
                        del.setPadding(45, 45, 45, 45);
                        break;
                    case R.id.suo:
                        suov.setBackground(getResources().getDrawable(R.drawable.ripple_bg2));
                        suov.setPadding(45, 0, 45, 0);
                        break;
                }
            }
            return false;
        }
    };
    private Intent intent;
    private RelativeLayout bj;

    public boolean guolv(Msgbean msgbean) {
        if (set.getgl() == 0)
            return true;
        for (int i = 0; i < gjcbeen.size(); i++) {
            if (msgbean.getName().equals(gjcbeen.get(i).getName()))
                return false;
            if (msgbean.getText().contains(gjcbeen.get(i).getText()))
            {
                if(set.getGlpb()==1)
                    pb(msgbean);
                return false;

            }
        }
        return true;
    }

    public void pb(Msgbean msgbean){
        Gjcbean gjcbeang= new Gjcbean();
        gjcbeang.setName(msgbean.getName());
        gjcbeang.setText("");
        DB.addgjcn(gjcbeang);
        gjcbeen.add(gjcbeang);
    }


    public Handler handler = new Handler() {
        public void handleMessage(Message message) {

            if ("chatmsg".equals(((Msgbean) message.obj).getType()))
                if (guolv((Msgbean) message.obj) == true) {
                    if (set.getGift()==0)
                    ad.add((Msgbean) message.obj);
                }
            if ("dgb".equals(((Msgbean) message.obj).getType())) {
                if (set.getLiwu() == 1)
                    ad.add((Msgbean) message.obj);
            }
            adapter.notifyDataSetChanged();
            if (suo == 0)
                lv.setSelection(adapter.getCount() - 1);
            if (ad.size() > 1000)
                while (ad.size() > 500)
                    ad.remove(0);
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//不息屏
        setContentView(R.layout.activity_main);



        SpeechUtility.createUtility(MainActivity.this, "appid=" + "5885a73b");
        SharedPreferences sp = getSharedPreferences("Guide", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("isFirstIn", false);
        editor.commit();

        service= Executors.newFixedThreadPool(2);
        service.execute(new Thread(new Runnable() {
            @Override
            public void run() {
                new json_object();
            }
        }));

        douyu=new Douyu();
        panda=new Crawl();

       // mAlertView = new AlertView("提示", "是否退出程序？", "取消", new String[]{"退出"}, null, this, AlertView.Style.Alert, this).setCancelable(true).setOnDismissListener(this);
        DB = new DBManager(getApplicationContext());
        set = DB.queryallset();
        bj= (RelativeLayout) findViewById(R.id.activity_main);
        suov = (ImageView) findViewById(R.id.suo);
        lv = (ListView) findViewById(R.id.list);
        del = (ImageView) findViewById(R.id.del);
        qp = (ImageView) findViewById(R.id.qp);
        edtop = (EditText) findViewById(R.id.edtop);
        logo= (ImageView) findViewById(R.id.logo);
        layouttop = (RelativeLayout) findViewById(R.id.layouttop);
        gjcbeen = DB.queryallgjc();

        intent1 = this.getIntent();
        id = intent1.getIntExtra("id", -1);

        if (set.getType().equals("douyu")) {
            DyBulletScreenClient.sethand(handler);
            logo.setImageResource(R.drawable.logo);
        } else if (set.getType().equals("panda")) {
            Crawl.sethand(handler);
            logo.setImageResource(R.drawable.logo4);
        }

        if (id == -1&&set.getRoom()!=-1111) {
            id = set.getRoom();
            if (set.getType().equals("douyu")) {
                service.execute(new Thread(douyu));
            } else if (set.getType().equals("panda")) {
                service.execute(new Thread(panda));
            }
        }


        textView = (TextView) findViewById(R.id.textViewr);
        if (id != -1&&id != -1111) {
            textView.setText(id + "");
        }
        logo.setOnTouchListener(touchListener);
        suov.setOnTouchListener(touchListener);
        del.setOnTouchListener(touchListener);
        setbj();
        title();
        adapter = new Myadapter();
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String con = String.format("选中了“%s:%s”", ad.get(position).getName(), ad.get(position).getText());
                name = ad.get(position).getName();
                listal = new AlertView(con, null, "取消", null,
                        new String[]{"屏蔽该用户"},
                        MainActivity.this, AlertView.Style.ActionSheet, MainActivity.this);
                listal.show();
            }
        });





    }

    public void setbj() {



        if (DB.queryallset().getTouming() == 0) {
            bj.setBackgroundResource(R.drawable.beijing);
        } else bj.setBackgroundColor(0x80000000);

        del.setAlpha(150);
        suov.setAlpha(150);
        qp.setAlpha(150);

    }

    public void top(View v) {
        if (layouttop.getVisibility() == View.GONE)
            layouttop.setVisibility(View.VISIBLE);
    }

    public void tvtop(View v) {


        if (edtop.getText().toString().length() >= 1) {
            id = Integer.parseInt(edtop.getText().toString());
            if (edtop.getText().toString().matches("[0-9]+") == false) {
                Toast.makeText(getApplicationContext(), "无效房间号", Toast.LENGTH_LONG).show();
                return;
            }
            if (set.getType().equals("douyu")) {
                DyBulletScreenClient.getInstance().breakServer();
            }
            else if (set.getType().equals("panda")) {
                panda.Close();
            }

            topal = new AlertView("请选择平台", null, null, null,
                    new String[]{"斗鱼TV", "熊猫TV","后续将增加更多平台"},
                    this, AlertView.Style.Alert, this);
            topal.show();

            layouttop.setVisibility(View.GONE);
            ad.clear();
            adapter.notifyDataSetChanged();
            DB.updataset("room", id);
        }

        edtop.setText("");
        layouttop.setVisibility(View.GONE);
        textView.setText(id + "");

    }

    public void suo(View v) {
        if (suo == 0) {
            suo = 1;
            suov.setImageResource(R.drawable.suo);
        } else {
            suo = 0;
            suov.setImageResource(R.drawable.jiesuo);
        }
    }

    public void del(View v) {
        ad.clear();
        adapter.notifyDataSetChanged();
    }

    public void logo(View v) {

        logoal = new AlertView("弹幕助手", null, "取消", null,
                new String[]{"抽取观众", "朗读设置", "过滤设置", "系统设置","退出程序"},
                this, AlertView.Style.ActionSheet, this);
        logoal.show();

    }


    public void qp(View v) {

        Intent intent = new Intent(MainActivity.this, MainActivityqp.class);
        intent.putExtra("id", id);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, 0);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 500);

    }

    @Override
    public void onDismiss(Object o) {

    }

    @Override
    public void onItemClick(Object o, int position) {
        if (o == logoal) {
            switch (position) {
                case 0:
                    intent = new Intent(this, Select.class);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(intent);
                            overridePendingTransition(R.anim.in_from_right, 0);
                        }
                    }, 260);
                    break;
                case 1:
                    intent = new Intent(this, langdu_setting.class);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(intent);
                            overridePendingTransition(R.anim.in_from_right, 0);
                        }
                    }, 260);
                    break;
                case 2:
                    intent = new Intent(this, SetGuoLV.class);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(intent);
                            overridePendingTransition(R.anim.in_from_right, 0);
                        }
                    }, 260);
                    break;
                case 3:
                    intent = new Intent(this, SettingActivity.class);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(intent);
                            overridePendingTransition(R.anim.in_from_right, 0);
                        }
                    }, 260);
                    break;
                case 4:
                    if (set.getType().equals("douyu")) {
                        DyBulletScreenClient.getInstance().breakServer();
                    }
                    else if (set.getType().equals("panda")) {
                        panda.Close();
                    }
                    System.exit(0);
                    break;
            }

        } else if (o == listal) {
            if (position == 0) {
                Gjcbean gjcbean = new Gjcbean();
                gjcbean.setName(name);
                DB.addgjcn(gjcbean);
            }
        } else if (o == topal) {
            switch (position) {
                case 0:
                    DyBulletScreenClient.sethand(handler);
                    service.execute(new Thread(douyu));
                    DB.updataset("type","douyu");
                    logo.setImageResource(R.drawable.logo);
                    break;
                case 1:
                    Crawl.sethand(handler);
                    service.execute(new Thread(panda));
                    DB.updataset("type","panda");
                    logo.setImageResource(R.drawable.logo4);
                    break;
            }
        }
    }

    public class Myadapter extends BaseAdapter {

        @Override
        public int getCount() {
            return ad.size();
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
                view = View.inflate(getApplicationContext(), R.layout.item, null);
            } else {
                view = convertView;
            }
            if (ad.get(position).getType().equals("chatmsg")) {
                TextView danmu = (TextView) view.findViewById(R.id.danmu);
                danmu.setText(ad.get(position).getText());
                danmu.setTextColor(0xFFffffff);
                TextView name = (TextView) view.findViewById(R.id.name);
                name.setText(ad.get(position).getName() + ":");
            }
            if (ad.get(position).getType().equals("dgb")) {
                TextView danmu = (TextView) view.findViewById(R.id.danmu);
                danmu.setText("送了" + ad.get(position).getGfcnt() + "个" + ad.get(position).getGfid());
                danmu.setTextColor(0xFFFF0000);
                TextView name = (TextView) view.findViewById(R.id.name);
                name.setText(ad.get(position).getName() + ":");
            }
            return view;
        }
    }

    public  void title() {
        Intent intent = new Intent(MainActivity.this, Mqp.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, (new Random().nextInt(100)), intent, 0);

        //1.获取系统通知的管理者
        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //2.用notification工厂 创建一个notification
        noti = new Notification.Builder(this)
                .setContentTitle("弹幕悬浮模式")
                .setContentText("因此功能用于移动端直播，没有语音功能")
                .setSmallIcon(R.drawable.logo3)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo3))
                .setContentIntent(contentIntent)
                .build();
        //3.把notification显示出来
        nm.notify(1, noti);
        if (set.getTingzhu() == 0||!isBackground(this)) {
            nm.cancel(1);
            return;
        }
    }


    protected void onResume() {
        super.onResume();
        set = DB.queryallset();
        gjcbeen = DB.queryallgjc();
        setbj();
        title();
        if (set.getType().equals("douyu")) {
            DyBulletScreenClient.sethand(handler);
        }
        else if (set.getType().equals("panda")) {
            Crawl.sethand(handler);
        }

    }
    public static boolean isBackground(Context context) {

        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
                    return true;
                }else{
                    return false;
                }
            }
        }
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false);
        }

        return true;
    }
    protected void onStop() {
        super.onStop();
        if (set.getTingzhu()==1&&isBackground(this))
            nm.notify(1, noti);
    }

}
