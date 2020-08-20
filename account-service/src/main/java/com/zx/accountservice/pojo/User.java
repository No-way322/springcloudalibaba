package com.zx.accountservice.pojo;


import lombok.Data;

@Data
public class User {

    private int id;
    private String name;
    private Balance balance;

    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public User() {
    }

    //setter/getter略
}
