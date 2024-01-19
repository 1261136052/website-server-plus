package com.xxforest.baseweb.vo;

import lombok.Data;

@Data
public class LoginVo {
    private String account;
    private String pwd;
    private String verifyCode;
}
