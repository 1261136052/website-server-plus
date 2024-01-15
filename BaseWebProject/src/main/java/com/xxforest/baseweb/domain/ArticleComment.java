package com.xxforest.baseweb.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.nutz.dao.entity.annotation.*;
import org.nutz.json.JsonField;

import java.util.Date;

/**
 * 管理员表
 */
@Data
@Table("article_comment")
@TableMeta("{'mysql-charset':'utf8mb4'}")
public class ArticleComment {

    /**
     * 编号
     */
    @Id(auto = false)
    private long id ;

    /**
     * 分类编号:1
     */
    @Column("article_id")
    private long articleId;

    /**
     * 作者:张三
     */
    @Column("user_id")
    private long userId ;

    /**
     * 作者:张三
     */
    @Column("user_name")
    private String userName ;


    /**
     * 内容
     */
    @Column
    @ColDefine(customType = "LONGTEXT")
    private String content ;

    /**
     * 发布时间,不用传:2021-01-01 10:01:01
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonField(dataFormat = "yyyy-MM-dd HH:mm:ss")
    @Column("send_date")
    private Date sendDate;

}
