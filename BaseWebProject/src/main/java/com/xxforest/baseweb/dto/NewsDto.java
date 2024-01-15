package com.xxforest.baseweb.dto;

import lombok.Data;

import java.util.Date;

@Data
public class NewsDto {
    /**
     * 编号
     */
    private long id ;

    /**
     * 类型， 新闻咨询/活动通知
     */
    private String type ;

    /**
     * 封面
     */
    private String cover ;

    /**
     * 标题
     */
    private String title ;

    /**
     * 内容
     */
    private String content ;

    /**
     * 发布时间
     */
    private Date publishDate;

    /**
     * 附件下载地址
     */
    private String fileLink ;

    /**
     * 阅读次数
     */
    private int viewCount ;

}
