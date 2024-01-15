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
@Table("goods_file")
@TableMeta("{'mysql-charset':'utf8mb4'}")
@PK( {"fileId", "goodsId"} )
public class GoodsFile {

    /**
     * 编号
     */
    @Column("file_id")
    private long fileId ;

    /**
     * 账号:admin
     */
    @Column("goods_id")
    private long goodsId ;


}
