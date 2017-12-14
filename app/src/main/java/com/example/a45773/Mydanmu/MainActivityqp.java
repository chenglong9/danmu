package com.example.a45773.Mydanmu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.a45773.Db.DBManager;
import com.example.a45773.bean.Gjcbean;
import com.example.a45773.bean.Msgbean;
import com.example.a45773.bean.Replybean;
import com.example.a45773.bean.Setbean;
import com.example.a45773.bulletscreen.client.DyBulletScreenClient;
import com.example.a45773.Main.MainActivity;
import com.example.a45773.Main.R;
import com.example.a45773.panda.Crawl;
import com.example.a45773.yuyin.Yuyin;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MainActivityqp extends Activity {
    private ViewGroup.LayoutParams lp =new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
    private int liwu;
    private ArrayList<BarrageView> barrageViews = new ArrayList<BarrageView>();
    private ArrayList<BarrageView> b = new ArrayList<BarrageView>();
    private Set<Integer> hang;
    private Queue<Msgbean> queue = new LinkedList<>();
    private int size;
    private TextView textView;
    private TextView nameText;
    private SimpleDraweeView rocketGif;
    private FrameLayout container;
    private RelativeLayout relativeLayout;
    public static boolean msgg=true;

    public Handler handler = new Handler() {
        public void handleMessage(Message message) {

            if (((Msgbean) message.obj).getType().equals("chatmsg")) {
                if (match(((Msgbean) message.obj).getText()) == false) {
                        String m = pipei(((Msgbean) message.obj));
                        if (m != null)
                            adg.offer(m);
                        BarrageView barrageView = new BarrageView(MainActivityqp.this, hang, size, message.what);
                        barrageView.setText(((Msgbean) message.obj).getText());
                        addContentView(barrageView, lp);
                        barrageViews.add(barrageView);
                } else hang.remove(message.what);
            }
            if (((Msgbean) message.obj).getType().equals("dgb")) {
                String x = "感谢" + ((Msgbean) message.obj).getName() + "送的" + ((Msgbean) message.obj).getGfid();
                String name = ((Msgbean) message.obj).getName();
                String gifName = ((Msgbean) message.obj).getGfid();
                if (match(x) == false && liwu == 1) {
                    Log.e("asdada", x);
                    BarrageView barrageView = new BarrageView(MainActivityqp.this, hang, size, message.what);
                    barrageView.setText(x);
                    addContentView(barrageView, lp);
                    barrageViews.add(barrageView);
                    adg.offer(x);
                    if(gifName.equals("火箭")) {
                        final View rocketLayout = LayoutInflater.
                                from(getBaseContext()).inflate(R.layout.rocket, null);
                        initRocketLayout(rocketLayout, name);
                        startAnimation(rocketLayout);
                    }
                } else if (suosuosuo == 1 && liwu == 0) {
                    adg.offer(x);
                    hang.remove(message.what);
                } else hang.remove(message.what);
            }
        }

    };
    private Display display;

    private void initRocketLayout(final View rocketLayout, String name) {
        PipelineDraweeController controller =
                (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                        .setUri(Uri.parse("res://" + getPackageName() + "/" + R.drawable.rocket))
                        .setAutoPlayAnimations(true) //自动播放gif动画
                        .build();
        nameText = (TextView) rocketLayout.findViewById(R.id.name);
        rocketGif = (SimpleDraweeView) rocketLayout.findViewById(R.id.rocket_gif);
        nameText.setText(name);
        rocketGif.setController(controller);
        rocketLayout.setId(R.id.rocketView);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams
                (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        container.addView(rocketLayout, lp);
    }

    private void startAnimation(final View rocketLayout) {
        int w = relativeLayout.getMeasuredWidth();
        long time = (w + 1500) * 3;
        TranslateAnimation move = new TranslateAnimation(w, -1500, 0, 0);
        move.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                rocketLayout.clearAnimation();
                container.removeView(rocketLayout);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        move.setInterpolator(new LinearInterpolator());
        move.setDuration(time);
        rocketLayout.setAnimation(move);
        move.start();
    }

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
                        if (p.getx() <= p.getWindowWidth() - p.getlength() - 100 && p.getFlag2() == 1) {
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


    private ImageView bj;
    private int id;
    private List<Gjcbean> gjcbeen;
    private int gl;
    private Thread xh;
    private int suosuosuo;
    private Yuyin yuyin;
    private Queue<String> adg = new LinkedList<>();
    ;
    private Setbean set;
    private DBManager DB;
    private int screenHeight;
    private boolean suos = true;
    private static Lock lock = new ReentrantLock();
    List<Replybean> replybeans = new ArrayList<Replybean>();
    private int gift;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //状态栏 @ 顶部
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //A //导航栏 @ 底部
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Fresco.initialize(this);
        setContentView(R.layout.activity_main_activityqp);

        WindowManager windowManager = getWindowManager();
        display = windowManager.getDefaultDisplay();
        screenHeight = display.getHeight();

        container = (FrameLayout) findViewById(R.id.container);
        relativeLayout = (RelativeLayout) findViewById(R.id.activity_main_activityqp);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        DB = new DBManager(getApplicationContext());
        gjcbeen = DB.queryallgjc();
        replybeans = DB.queryAllReplys();

        set = DB.queryallset();
        Intent intent1 = this.getIntent();
        id = intent1.getIntExtra("id", -1);
        liwu = set.getLiwu();
        glpb = set.getGlpb();
        size = (set.getSize() + 10) * 3;

        suosuosuo = 1;
        suos = true;
        msgg=true;

        if (set.getType().equals("douyu")) {
            DyBulletScreenClient.sethand(handlers);
        }
        else if (set.getType().equals("panda")) {
            Crawl.sethand(handlers);
        }
        setbj();
        hang = new HashSet<Integer>();
        gl = set.getgl();
        gift = set.getGift();
        suosuosuo = set.getLangdu();

        yuyin = new Yuyin(this);
        textView = (TextView) findViewById(R.id.textView);
        textView.setText("" + id);

        Thread thread = new CreateView(handler, queue, hang, (screenHeight-95) / (size + 40), screenHeight, display.getWidth());
        thread.start();

        final Handler h = new Handler();
        xh = new Thread(new Runnable() {
            @Override
            public void run() {
                if (suosuosuo == 1) {
                    if (adg.size() != 0 && !yuyin.isSpeak) {
                        yuyin.mTts.startSpeaking(adg.poll(), yuyin.mTtsListener);
                    }
                }
                h.postDelayed(this, 2000);
            }
        });
        xh.start();

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

    public String pipei(Msgbean msgbean) {

        for (Replybean tmp : replybeans) {
            if (msgbean.getText().contains(tmp.getAsk())) {
                return tmp.getReply();
            }
        }
        return null;
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

    public void setbj() {
        bj = (ImageView) findViewById(R.id.mybeijing);

        if (DB.queryallset().getTouming() == 0) {
            bj.setVisibility(View.VISIBLE);
        } else bj.setVisibility(View.GONE);

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            suosuosuo = 0;
            suos = false;
            handler.removeCallbacks(xh);
            Intent intent = new Intent(MainActivityqp.this, MainActivity.class);
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

        return true;
    }

    public void qp(View v) {
        suosuosuo = 0;
        suos = false;
        handler.removeCallbacks(xh);
        Intent intent = new Intent(MainActivityqp.this, MainActivity.class);
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

    public void add(View v) {
        final EditText inputServer = new EditText(MainActivityqp.this);
        inputServer.setFocusable(true);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivityqp.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        builder.setTitle("添加敏感词").setView(inputServer).setNegativeButton(
                "no", null);
        builder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Gjcbean g = new Gjcbean();
                        g.setText(inputServer.getText().toString());
                        DB.addgjc(g);
                        gjcbeen = DB.queryallgjc();
                    }
                });
        builder.show();
    }
    protected void onStop() {
        super.onStop();
        suosuosuo = 0;
        suos = false;
        msgg=false;
        handler.removeCallbacks(xh);
    }

}

