package com.xxforest.baseweb.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xxforest.baseweb.core.ServerDao;
import com.xxforest.baseweb.core.inter.IDataInitialize;
import lombok.Data;
import org.nutz.dao.entity.annotation.*;
import org.nutz.json.JsonField;

import java.util.Date;

/**
 * 管理员表
 */
@Data
@Table("article_like")
@TableMeta("{'mysql-charset':'utf8mb4'}")
@PK( {"userId", "articleId"} )
public class ArticleLike  {

    /**
     * 编号
     */
    @Column("user_id")
    private long userId ;

    /**
     * 账号:admin
     */
    @Column("article_id")
    private long articleId ;

    /**
     * 登录时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonField(dataFormat = "yyyy-MM-dd HH:mm:ss")
    @Column("like_date")
    private Date likeDate;


}
