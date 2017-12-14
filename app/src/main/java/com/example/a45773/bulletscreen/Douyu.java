package com.example.a45773.bulletscreen;

import com.example.a45773.bulletscreen.client.DyBulletScreenClient;
import com.example.a45773.bulletscreen.utils.KeepAlive;
import com.example.a45773.bulletscreen.utils.KeepGetMsg;
import com.example.a45773.Main.MainActivity;

/**
 * Created by 45773 on 2017-02-11.
 */

public class Douyu implements Runnable{
    public DyBulletScreenClient danmuClient;
    private KeepGetMsg keepGetMsg;

    public void run() {
        danmuClient = DyBulletScreenClient.getInstance();
        if (danmuClient.getReadyFlag())
            return;
        danmuClient.init(MainActivity.id, -9999);
        //保持弹幕服务器心跳
        KeepAlive keepAlive = new KeepAlive();
        keepAlive.start();
        //获取弹幕服务器发送的所有信息
        keepGetMsg = new KeepGetMsg();
        keepGetMsg.start();


    }
}
