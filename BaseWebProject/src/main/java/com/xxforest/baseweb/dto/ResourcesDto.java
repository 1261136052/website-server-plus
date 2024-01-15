package com.xxforest.baseweb.dto;

import lombok.Data;

@Data
public class ResourcesDto {
    /**
     * 资源编号
     */
    private long id ;
    /**
     * 标题
     */
    private String title ;
    /**
     * 下载次数
     */
    private int downloadCount ;
    /**
     * 密码
     */
    private String pwd ;
    /**
     * 连接
     */
    private String link ;
}
