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
@Table("goods_category")
@TableMeta("{'mysql-charset':'utf8mb4'}")
public class GoodsCategory {

    /**
     * 编号
     */
    @Id(auto = false)
    private long id ;

    /**
     * 账号:admin
     */
    @Column("category_name")
    @ColDefine(width = 20)
    private String categoryName ;


}
