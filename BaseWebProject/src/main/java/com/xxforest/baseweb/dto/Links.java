package com.xxforest.baseweb.dto;

import lombok.Data;

@Data
public class Links {
    private String userName;
    private long id;
//    public Links(String userName,long LinkId){
//        this.userName = userName;
//        this.LinkId = LinkId;
//    }

    public Links(long LinkId, String userName) {
        this.userName = userName;
        this.id = LinkId;
    }
}
