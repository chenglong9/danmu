package com.example.a45773.bean;

/**
 * Created by 45773 on 2017-02-07.
 */

public class Giftbean {
    public Giftbean(String id, String name) {
        this.id = id;
        this.name = name;
    }
    public Giftbean() {
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String id ;
    String name ;
}
