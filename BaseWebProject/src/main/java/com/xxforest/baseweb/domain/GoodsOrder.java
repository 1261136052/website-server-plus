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
@Table("goods_order")
@TableMeta("{'mysql-charset':'utf8mb4'}")
public class GoodsOrder  {

    /**
     * 编号
     */
    @Id(auto = false)
    private long id ;

    /**
     * 账号:admin
     */
    @Column("goods_id")
    private long goodsId ;


    /**
     * 密码:123456
     */
    @Column("buyer_id")
    private long buyerId;

    /**
     * 密码:123456
     */
    @Column("buyer_phone")
    private String buyerPhone;

    /**
     * 登录时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonField(dataFormat = "yyyy-MM-dd HH:mm:ss")
    @Column("buy_date")
    private Date buyDate;

    /**
     * 账号:admin
     */
    @Column
    @ColDefine(width = 128)
    private String remark;

    @One(field = "goodsId")
    // 1.r.59之前需要写target参数
    // @One(target = Master.class, field = "masterId")
    public Goods goods;
}
