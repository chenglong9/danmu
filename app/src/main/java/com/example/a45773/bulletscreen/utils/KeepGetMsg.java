package com.example.a45773.bulletscreen.utils;

import android.content.Context;
import android.os.Handler;

import com.example.a45773.bulletscreen.client.DyBulletScreenClient;


public class KeepGetMsg extends Thread {

    public KeepGetMsg()
    {

    }
	@Override
    public void run()
    {
		////获取弹幕客户端
    	DyBulletScreenClient danmuClient = DyBulletScreenClient.getInstance();
    	
    	//判断客户端就绪状态
        while(danmuClient.getReadyFlag())
        {
        	//获取服务器发送的弹幕信息
        	danmuClient.getServerMsg();

        }
    }
}
