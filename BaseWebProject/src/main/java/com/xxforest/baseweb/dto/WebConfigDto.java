package com.xxforest.baseweb.dto;

import lombok.Data;

@Data
public class WebConfigDto extends CommentDto {
    /**
     * 配置名称
     */
    private String name ;
    /**
     * 值
     */
    private String val ;
    /**
     * 注释
     */
    private String note ;
    /**
     * 配置类型
     */
    private String configType;
}
