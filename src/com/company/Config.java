package com.company;

//will contain config files or functions
public class Config {

    public Config(){}

    //get the time the graphs should be updated
    public String getUpdateConfig(){

        return "11:24";

    };

    //get the credentials for certain accounts
    public String getTwitterConfig(){
        return "username_password";
    }



}
