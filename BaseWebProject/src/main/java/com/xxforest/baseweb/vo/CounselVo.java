package com.xxforest.baseweb.vo;

import lombok.Data;

@Data
public class CounselVo {

    /**
     * 姓名
     */
    private String name ;

    /**
     * 电话
     */
    private String phone ;

    /**
     * 类型
     */
    private String type;

    /**
     * 所在地
     */
    private String location ;

    /**
     * 院校/企业
     */
    private String company ;

    /**
     * 资讯内容
     */
    private String content ;

}
