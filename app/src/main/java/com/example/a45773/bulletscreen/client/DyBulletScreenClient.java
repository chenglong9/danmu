package com.example.a45773.bulletscreen.client;

import android.os.Handler;
import android.os.Message;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;


import com.example.a45773.bean.Giftbean;
import com.example.a45773.bean.Msgbean;
import com.example.a45773.bulletscreen.msg.DyMessage;
import com.example.a45773.bulletscreen.msg.MsgView;
import com.example.a45773.Main.MainActivity;

public class DyBulletScreenClient {

    private static DyBulletScreenClient instance;

    //第三方弹幕协议服务器地址
    private static final String hostName = "openbarrage.douyutv.com";

    //第三方弹幕协议服务器端口
    private static final int port = 8601;

    //设置字节获取buffer的最大值
    private static final int MAX_BUFFER_LENGTH = 4096;

    //socket相关配置
    private Socket sock;
    private BufferedOutputStream bos;
    private BufferedInputStream bis;

    //获取弹幕线程及心跳线程运行和停止标记
    private boolean readyFlag = false;


    private DyBulletScreenClient() {
    }

    /**
     * 单例获取方法，客户端单例模式访问
     *
     * @return
     */
    public static DyBulletScreenClient getInstance() {
        if (null == instance) {
            instance = new DyBulletScreenClient();
        }
        return instance;
    }
    public void breakServer() {
        try {
            readyFlag=false;
            if (sock!=null&&sock.isConnected()&&bos!=null&&bis!=null) {
                sock.close();
                bis.close();
                bos.close();
            }
            handler=null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 客户端初始化，连接弹幕服务器并登陆房间及弹幕池
     *
     * @param roomId  房间ID
     * @param groupId 弹幕池分组ID
     */
    public void init(int roomId, int groupId) {
        //连接弹幕服务器

        Msgbean msgbean=new Msgbean();
        msgbean.setName("弹幕助手");
        msgbean.setText("正在连接弹幕服务器，请稍等。。。");
        msgbean.setType("chatmsg");
        Message message = new Message();
        message.obj = msgbean;
        handler.sendMessage(message);

        this.connectServer();
        //登陆指定房间
        this.loginRoom(roomId);
        //加入指定的弹幕池
        this.joinGroup(roomId, groupId);
        //设置客户端就绪标记为就绪状态
        readyFlag = true;
    }

    /**
     * 获取弹幕客户端就绪标记
     *
     * @return
     */
    public boolean getReadyFlag() {
        return readyFlag;
    }

    /**
     * 连接弹幕服务器
     */
    private void connectServer() {
        try {
            //获取弹幕服务器访问host
            String host = InetAddress.getByName(hostName).getHostAddress();
            //建立socke连接
            sock = new Socket(host, port);
            //设置socket输入及输出
            bos = new BufferedOutputStream(sock.getOutputStream());
            bis = new BufferedInputStream(sock.getInputStream());



        } catch (Exception e) {
            e.printStackTrace();
        }
        Msgbean msgbean=new Msgbean();
        msgbean.setName("弹幕助手");
        msgbean.setText("连接成功！");
        msgbean.setType("chatmsg");
        Message message = new Message();
        message.obj = msgbean;
        handler.sendMessage(message);
        System.out.println("Server Connect Successfully!");
    }

    /**
     * 登录指定房间
     *
     * @param roomId
     */
    private void loginRoom(int roomId) {
        //获取弹幕服务器登陆请求数据包
        byte[] loginRequestData = DyMessage.getLoginRequestData(roomId);


        try {
            //发送登陆请求数据包给弹幕服务器
            bos.write(loginRequestData, 0, loginRequestData.length);
            bos.flush();

            //初始化弹幕服务器返回值读取包大小
            byte[] recvByte = new byte[MAX_BUFFER_LENGTH];
            //获取弹幕服务器返回值
            bis.read(recvByte, 0, recvByte.length);

            //解析服务器返回的登录信息
            if (DyMessage.parseLoginRespond(recvByte)) {
                System.out.println("Receive login response successfully!");
            } else {
                System.out.println("Receive login response failed!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 加入弹幕分组池
     *
     * @param roomId
     * @param groupId
     */
    private void joinGroup(int roomId, int groupId) {
        //获取弹幕服务器加弹幕池请求数据包
        byte[] joinGroupRequest = DyMessage.getJoinGroupRequest(roomId, groupId);

        try {
            //想弹幕服务器发送加入弹幕池请求数据
            bos.write(joinGroupRequest, 0, joinGroupRequest.length);
            bos.flush();

            System.out.println("Send join group request successfully!");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Send join group request failed!");
        }
    }

    /**
     * 服务器心跳连接
     */
    public void keepAlive() {
        //获取与弹幕服务器保持心跳的请求数据包
        byte[] keepAliveRequest = DyMessage.getKeepAliveData((int) (System.currentTimeMillis() / 1000));

        try {
            //向弹幕服务器发送心跳请求数据包
            bos.write(keepAliveRequest, 0, keepAliveRequest.length);
            bos.flush();
            System.out.println("Send keep alive request successfully!");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Send keep alive request failed!");
        }
    }

    private static Handler handler = null;

    public static void sethand(Handler handlers) {
        handler = handlers;
    }
    public static Handler gethand() {
        return handler;
    }

    public void getServerMsg() {
        //初始化获取弹幕服务器返回信息包大小
        byte[] recvByte = new byte[MAX_BUFFER_LENGTH];
        //定义服务器返回信息的字符串
        String dataStr;
        try {
            //读取服务器返回信息，并获取返回信息的整体字节长度
            int recvLen = bis.read(recvByte, 0, recvByte.length);

            //根据实际获取的字节数初始化返回信息内容长度
            byte[] realBuf = new byte[recvLen];
            //按照实际获取的字节长度读取返回信息
            System.arraycopy(recvByte, 0, realBuf, 0, recvLen);
            //根据TCP协议获取返回信息中的字符串信息
            dataStr = new String(realBuf, 12, realBuf.length - 12);


            //循环处理socekt黏包情况
            while (dataStr.lastIndexOf("type@=") > 5) {
                //对黏包中最后一个数据包进行解析
                MsgView msgView = new MsgView(StringUtils.substring(dataStr, dataStr.lastIndexOf("type@=")));
                //分析该包的数据类型，以及根据需要进行业务操作
                parseServerMsg(msgView.getMessageList());
                //处理黏包中的剩余部分
                dataStr = StringUtils.substring(dataStr, 0, dataStr.lastIndexOf("type@=") - 12);
            }
            //对单一数据包进行解析
            MsgView msgView = new MsgView(StringUtils.substring(dataStr, dataStr.lastIndexOf("type@=")));
            //分析该包的数据类型，以及根据需要进行业务操作
            parseServerMsg(msgView.getMessageList());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析从服务器接受的协议，并根据需要订制业务需求
     *
     * @param msg
     */
    private void parseServerMsg(Map<String, Object> msg) {
        Msgbean msgbean = new Msgbean();
        if (msg.get("type") != null) {
            //服务器反馈错误信息
            if (msg.get("type").equals("error")) {
                System.out.println(msg.toString());
                //结束心跳和获取弹幕线程
                this.readyFlag = false;
                return;
            }
            //判断消息类型

            if (msg.get("type").equals("chatmsg")) {//普通消息
                String name = "";
                if (msg.get("nn") != null)
                    name = msg.get("nn").toString();
                int lv = 0;
                if (msg.get("level") != null)
                    lv = Integer.parseInt(msg.get("level").toString());
                String type = msg.get("type").toString();
                String text = "";
                if (msg.get("txt") != null)
                    text = msg.get("txt").toString();
                int rg = 1;
                if (msg.get("rg") != null)
                    rg = Integer.parseInt(msg.get("rg").toString());
                msgbean.setName(name);
                msgbean.setType(type);
                msgbean.setText(text);
                msgbean.setLv(lv);
                msgbean.setRg(rg);
            } else /*if (msg.get("type").equals("uenter")) {//用户进房间
                String name = msg.get("nn").toString();
				int lv=0;
				if(msg.get("level")!=null)
					lv=Integer.parseInt(msg.get("level").toString());
				String type=msg.get("type").toString();
				int rg=1;
				if(msg.get("rg")!=null)
					rg=Integer.parseInt(msg.get("rg").toString());
				msgbean.setName(name);
				msgbean.setType(type);
				msgbean.setLv(lv);
				msgbean.setRg(rg);
			}
			else */if (msg.get("type").equals("dgb")) {//普通礼物
                String name = msg.get("nn").toString();
                int lv = 0;
                if (msg.get("level") != null)
                    lv = Integer.parseInt(msg.get("level").toString());
                int gfid = Integer.parseInt(msg.get("gfid").toString());
                String type = msg.get("type").toString();
                int rg = 1;
                if (msg.get("rg") != null)
                    rg = Integer.parseInt(msg.get("rg").toString());
                int gfcnt = 1;
                if (msg.get("gfcnt") != null)
                    gfcnt = Integer.parseInt(msg.get("gfcnt").toString());
                String x=null;
                for (Giftbean tmp: MainActivity.giftbeans) {
                    if(tmp.getId().equals(gfid+"")) {
                        x = tmp.getName();
                        break;
                    }
                }
                if (x==null)
                    x="礼物";
                msgbean.setGfid(x);
                msgbean.setGfcnt(gfcnt);
                msgbean.setName(name);
                msgbean.setType(type);
                msgbean.setLv(lv);
                msgbean.setRg(rg);
            } /*else if (msg.get("type").equals("spbc")) {//大礼物
                String name = msg.get("sn").toString();
                String type = msg.get("type").toString();
                int lv = 0;
                int rg = 1;
                if (msg.get("rg") != null)
                    rg = Integer.parseInt(msg.get("rg").toString());
                int gc = Integer.parseInt(msg.get("gc").toString());
                String gn = msg.get("gn").toString();

                msgbean.setName(name);
                msgbean.setType(type);
                msgbean.setLv(lv);
                msgbean.setRg(rg);
                msgbean.setGc(gc);
                msgbean.setGn(gn);

            }*/ else return;

            Message message = new Message();
            message.obj = msgbean;
            handler.sendMessage(message);


			/*if(msg.get("type").equals("chatmsg")){//弹幕消息
				System.out.println("弹幕消息===>" + msg.toString());

			} else if(msg.get("type").equals("dgb")){//赠送礼物信息
				System.out.println("礼物消息===>" + msg.toString());

			} else {
				System.out.println("其他消息===>" + msg.toString());

			}*/


        }
    }
}
