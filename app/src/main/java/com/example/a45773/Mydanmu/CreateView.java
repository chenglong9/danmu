package com.example.a45773.Mydanmu;

import android.os.Handler;
import android.os.Message;

import com.example.a45773.bean.Msgbean;

import java.util.Queue;
import java.util.Random;
import java.util.Set;

/**
 * Created by  on 2017/1/21.
 */

public class CreateView extends Thread{
    private Handler handler;
    private Queue<Msgbean> queue;
    private Set<Integer> hang;
    private int size;
    private  int height;
    private  int width;

    public CreateView(Handler handler,Queue<Msgbean> queue, Set<Integer> hang, int size,int height,int width){
        super();
        this.handler=handler;
        this.queue=queue;
        this.hang=hang;
        this.size=size;
        this.width=width;
        this.height=height-95;
    }


    @Override
    public void run() {
        int i;
      while (MainActivityqp.msgg){
          if (hang.size()<size) {
              Message message = new Message();
              Random random=new Random();
              i=random.nextInt(size);
              while (hang.contains(i)==true){
                  i=random.nextInt(size);
              }
              if ((message.obj = queue.poll()) != null) {
                  hang.add(i);
                  message.what = i;
                  handler.sendMessage(message);

              }
          }
      }


    }
}
