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
@Table("article")
@TableMeta("{'mysql-charset':'utf8mb4'}")
public class Article  {

    /**
     * 编号
     */
    @Id(auto = false)
    private long id ;

    /**
     * 分类编号:1
     */
    @Column("article_category_id")
    private long articleCategoryId;

    /**
     * 标题:hello
     */
    @Column
    @ColDefine(width = 200)
    private String title ;

    /**
     * 作者:张三
     */
    @Column
    @ColDefine(width = 200)
    private String author ;

    /**
     * 作者:张三
     */
    @Column("author_id")
    private long authorId ;

    /**
     * 发布时间,不用传:2021-01-01 10:01:01
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonField(dataFormat = "yyyy-MM-dd HH:mm:ss")
    @Column("publish_date")
    private Date publishDate;

    /**
     * 内容
     */
    @Column
    @ColDefine(customType = "LONGTEXT")
    private String content ;

    /**
     * 封面
     */
    @Column
    @ColDefine(width = 500)
    private String cover ;


    /**
     * 权重
     */
    @Column
    @ColDefine(customType = "LONGTEXT")
    private Integer weight ;

    @Default("0")
    @Column("like_count")
    private Integer likeCount ;
}
