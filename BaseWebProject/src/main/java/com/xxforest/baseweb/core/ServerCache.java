package com.xxforest.baseweb.core;

public class ServerCache {
    public static ServerCache inst ;
    
    public static ServerCache getInstance(){
        if(inst == null){
            inst = new ServerCache();
            inst.init();
        }
        return inst;
    }

    private void init() {
    }
}
