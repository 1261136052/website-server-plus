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
@Table("goods_chat")
@TableMeta("{'mysql-charset':'utf8mb4'}")
public class GoodsChat  {

    /**
     * 编号
     */
    @Id(auto = false)
    private long id ;

    /**
     * 账号:admin
     */
    @Column("reader_id")
    private long readerId;

    /**
     * 账号:admin
     */
    @Column("listener_id")
    private long listenerId;

    /**
     * 账号:admin
     */
    @Column
    @ColDefine(width = 128)
    private String content ;

    /**
     * 登录时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonField(dataFormat = "yyyy-MM-dd HH:mm:ss")
    @Column("publish_date")
    private Date publishDate;

}
