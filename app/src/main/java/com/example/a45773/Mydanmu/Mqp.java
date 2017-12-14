package com.example.a45773.Mydanmu;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import com.example.a45773.Db.DBManager;
import com.example.a45773.Main.MainActivity;
import com.example.a45773.bean.Gjcbean;
import com.example.a45773.bean.Msgbean;
import com.example.a45773.bean.Setbean;
import com.example.a45773.bulletscreen.client.DyBulletScreenClient;
import com.example.a45773.Main.R;
import com.example.a45773.panda.Crawl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;


public class Mqp extends Activity {
    private ViewGroup lpp ;
    private ViewGroup.LayoutParams lp ;
    private int liwu;
    private ArrayList<BarrageView> barrageViews = new ArrayList<BarrageView>();
    private ArrayList<BarrageView> b = new ArrayList<BarrageView>();
    private Set<Integer> hang;
    private Queue<Msgbean> queue = new LinkedList<>();
    private int size;

    public Handler handler = new Handler() {
        public void handleMessage(Message message) {

            if (((Msgbean) message.obj).getType().equals("chatmsg")) {
                if (match(((Msgbean) message.obj).getText()) == false) {
                        BarrageView barrageView = new BarrageView(Mqp.this, hang, size, message.what);
                        barrageView.setText(((Msgbean) message.obj).getText());
                        lpp.addView(barrageView, lp);
                        barrageViews.add(barrageView);
                } else hang.remove(message.what);
            }
            if (((Msgbean) message.obj).getType().equals("dgb")) {
                String x = "感谢" + ((Msgbean) message.obj).getName() + "送的" + ((Msgbean) message.obj).getGfid();
                if (match(x) == false && liwu == 1) {
                    Log.e("asdada", x);
                    BarrageView barrageView = new BarrageView(Mqp.this, hang, size, message.what);
                    barrageView.setText(x);
                    lpp.addView(barrageView, lp);
                    barrageViews.add(barrageView);
                }  else hang.remove(message.what);
            }


        }

    };
    private WindowManager.LayoutParams layoutParams;
    private View view;
    private WindowManager wm;



    public Handler handlers = new Handler() {
        public void handleMessage(Message message) {
            if (((Msgbean) message.obj).getType().equals("chatmsg")) {
                if (guolv((Msgbean) message.obj) == true) {

                        if (gift == 0)
                            queue.offer((Msgbean) message.obj);

                }
            }
            if (((Msgbean) message.obj).getType().equals("dgb")) {
                queue.offer((Msgbean) message.obj);
            }
        }
    };
    private Handler hs = new Handler() {
        public void handleMessage(Message msg) {
            if (barrageViews != null && barrageViews.size() != 0) {
                b.clear();
                b.addAll(barrageViews);
                for (BarrageView p : b) {
                    if (p != null && p.getFlag() == 1) {
                        p.move();
                        if (p.getx() <= p.getWindowWidth() - p.getlength() - 150 && p.getFlag2() == 1) {
                            p.getHang().remove(p.getI());
                            p.setFlag2(0);
                        }
                        if (p.getx() <= -p.getlength()) {
                            p.setFlag(0);
                            ((ViewGroup) p.getParent()).removeView(p);
                            barrageViews.remove(p);
                            continue;
                        }
                       p.invalidate();
                    }

                }

            }
        }
    };

    private List<Gjcbean> gjcbeen;
    private int gl;
    private Setbean set;
    private DBManager DB;
    private int screenHeight;
    private boolean suos = true;
    private int gift;

    private static final int REQUEST_CODE = 1;
    private Display display;
    private int currentapiVersion=android.os.Build.VERSION.SDK_INT;

    @TargetApi(Build.VERSION_CODES.M)
    private  void requestAlertWindowPermission() {
        if (currentapiVersion>=23&&!Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, REQUEST_CODE);

        }else {
            hou();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager windowManager = getWindowManager();
        display = windowManager.getDefaultDisplay();
        screenHeight = display.getHeight();
        suos=true;

        layoutParams = new WindowManager.LayoutParams();
        layoutParams.width = display.getWidth();
        layoutParams.height = screenHeight;
        //这里是关键，使控件始终在最上方
        layoutParams.type =  WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        layoutParams.format= PixelFormat.RGBA_8888;

        LayoutInflater inflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view=inflater.inflate(R.layout.activity_mqp, null);

        lpp=(ViewGroup) view.findViewById(R.id.layoutParams);
        lp=lpp.getLayoutParams();


        wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        requestAlertWindowPermission();

       // hou();
        finish();
    }

    public void hou(){
        wm.addView(view, layoutParams);
        DB = new DBManager(getApplicationContext());
        gjcbeen = DB.queryallgjc();

        set = DB.queryallset();
        liwu = set.getLiwu();
        glpb = set.getGlpb();
        size = (set.getSize() + 10) * 3;

        if (set.getType().equals("douyu")) {
            DyBulletScreenClient.sethand(handlers);
        }
        else if (set.getType().equals("panda")) {
            Crawl.sethand(handlers);
        }
        hang = new HashSet<Integer>();
        gl = set.getgl();
        gift = set.getGift();

        final Thread thread = new CreateView(handler, queue, hang, (screenHeight-95) / (size + 20), screenHeight, display.getWidth());
        thread.start();

        new Thread(new Runnable() {

            @Override
            public void run() {
                while (suos) {
                    if (barrageViews!=null&&barrageViews.size()!=0) {
                        Message msg = new Message();
                        hs.sendMessage(msg);

                        try {
                            Thread.sleep(5);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();


    }

    private int glpb;

    public void close(View v)
    {
        suos=false;
        wm.removeView(view);
    }

    public boolean guolv(Msgbean msgbean) {
        if (gl == 0)
            return true;
        for (Gjcbean tmp : gjcbeen) {
            if (msgbean.getName().equals(tmp.getName()))
                return false;
            if (msgbean.getText().contains(tmp.getText())) {
                if (glpb == 1)
                    pb(msgbean);
                return false;

            }

        }
        return true;
    }

    public void pb(Msgbean msgbean) {
        Gjcbean gjcbeang = new Gjcbean();
        gjcbeang.setName(msgbean.getName());
        gjcbeang.setText("");
        DB.addgjcn(gjcbeang);
        gjcbeen.add(gjcbeang);
    }

    public boolean match(String s) {
        ArrayList<BarrageView> bx = new ArrayList<BarrageView>();
        bx.addAll(barrageViews);
        String string = null;
        if (s.matches("[1]+")) {
            string = "[1]+";
        } else if (s.matches("[2]+")) {
            string = "[2]+";
        } else if (s.matches("[23]+")) {
            string = "[23]+";
        } else if (s.matches("[6]+")) {
            string = "[6]+";
        } else if (s.matches("[9]+")) {
            string = "[9]+";
        } else if (s.matches("[8]+")) {
            string = "[8]+";
        } else if (s.matches("[7]+")) {
            string = "[7]+";
        } else if (s.matches("[5]+")) {
            string = "[5]+";
        } else if (s.matches("[4]+")) {
            string = "[4]+";
        } else if (s.matches("[3]+")) {
            string = "[3]+";
        } else if (s.matches("[0]+")) {
            string = "[0]+";
        } else {
            for (BarrageView tmp : bx) {
                if (tmp != null)
                    if (tmp.getText().toString().contains(s)) {
                        tmp.timesUp();
                        return true;
                    }
            }
            return false;
        }

        for (BarrageView tmp : bx) {

            if (tmp.getText().toString().matches(string)) {
                tmp.timesUp();
                return true;
            }

        }
        return false;
    }


}

