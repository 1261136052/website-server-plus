package com.xxforest.baseweb.vo;

import lombok.Data;

@Data

public class ChatVo {
    /**
     * 账号:admin
     */
    private long readerId;

    /**
     * 账号:admin
     */
    private long listenerId;

    private String content ;

}
