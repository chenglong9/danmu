package com.example.a45773.Mydanmu;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.Random;
import java.util.Set;


/**
 * Created by asus on 2016/12/12.
 */
public class BarrageView extends TextView{
    private Paint paint=new Paint();
    private int x=0;
    private int y=0;
    private int i;
    private int flag=1;
    private int flag2=1;
    private int size;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public int getFlag2() {
        return flag2;
    }

    public void setFlag2(int flag2) {
        this.flag2 = flag2;
    }

    public int getWindowWidth() {
        return windowWidth;
    }

    private int windowWidth;
    private int windowHeight;
    private Thread rollThread;
    private boolean isStop=false;
    private int times=1;
    private Set<Integer> hang;
    private Handler handler3;

/*
    interface OnRollEndListener {
        void onRollEnd();
    }

    private OnRollEndListener mOnRollEndListener;

    public void setOnRollEndListener(OnRollEndListener onRollEndListener) {
        this.mOnRollEndListener = onRollEndListener;
    }
*/

    public BarrageView(Context context,Set<Integer> hang,int size,int i) {
        super(context);
        this.size=size;
        this.hang=hang;
        this.i=i;
        init();

    }

    protected void init(){
        Rect rect = new Rect();
        getWindowVisibleDisplayFrame(rect);
        windowWidth = rect.width();
        Random random=new Random();
        //设置x为屏幕宽
        x =windowWidth-10+random.nextInt(10);

/*        Random random=new Random();
        i=random.nextInt(windowHeight/(size+20));
        while (hang.contains(i)==true){
            i=random.nextInt(windowHeight/(size+20));
        }
  *//*      i=1;
        while (hang.contains(i)==true){
            i=(i+1)%(windowHeight/(size+20));
        }*//*
        hang.add(i);*/

        y=(i+1)*(size+40)+95;
    }
    public void move(){
        x-=1;
    }
    public int getx(){
        return x;
    }

    public Set<Integer> getHang() {
        return hang;
    }

    public void setHang(Set<Integer> hang) {
        this.hang = hang;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public float getlength(){
        return paint.measureText((String) getText());
    }
    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        paint.setTextSize(size);
        paint.setColor(0xffffffff);
        if (times<=1)
            canvas.drawText((String) getText(),x,y,paint);
        else {
            paint.setColor(0xff15f900);
            canvas.drawText(getText() + "*" + times, x, y, paint);
        }
       /* if (rollThread == null) {
            rollThread = new RollThread(this,h);
            rollThread.start();
        }*/

    }
    public void timesUp(){
        times++;
    }
}
