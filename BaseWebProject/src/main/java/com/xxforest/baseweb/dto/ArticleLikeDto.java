package com.xxforest.baseweb.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.PK;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.TableMeta;
import org.nutz.json.JsonField;

import java.util.Date;

/**
 * 管理员表
 */
@Data
public class ArticleLikeDto {

    private long userId ;

    private long articleId ;

    private String userName;

    /**
     * 登录时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonField(dataFormat = "yyyy-MM-dd HH:mm:ss")
    @Column("like_date")
    private Date likeDate;


}
