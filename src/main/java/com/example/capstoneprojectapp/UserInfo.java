package com.example.capstoneprojectapp;

public class UserInfo {
    private String name;
    public String login;
    public String email;
    public String password;
    public int coin;

    public UserInfo(String name, String login, String email,String password, int coin){
        this.name=name;
        this.login=login;
        this.email=email;
        this.password=password;
        this.coin=coin;
    }

    public UserInfo(String name,String login, int coin){
        this.name=name;
        this.login=login;
        this.coin=coin;
    }
    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name=name;
    }

}


