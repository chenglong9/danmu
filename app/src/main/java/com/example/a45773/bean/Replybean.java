package com.example.a45773.bean;

/**
 * Created by pop on 2017/1/25.
 */
public class Replybean {

    private String ask;
    private String reply;

    public Replybean(String ask, String reply) {
        this.ask = ask;
        this.reply = reply;

    }

    public String getAsk() {
        return ask;
    }

    public String getReply() {
        return reply;
    }

    public void setAsk(String ask) {
        this.ask = ask;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

}
