package com.xxforest.baseweb.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xxforest.baseweb.core.ServerDao;
import com.xxforest.baseweb.core.inter.IDataInitialize;
import lombok.Data;
import org.nutz.dao.entity.annotation.*;
import org.nutz.json.JsonField;
import org.w3c.dom.Text;

import java.util.Date;
import java.util.List;

/**
 * 管理员表
 */
@Data
@Table("goods")
@TableMeta("{'mysql-charset':'utf8mb4'}")
public class Goods {

    /**
     * 编号
     */
    @Id(auto = false)
    private long id ;

    /**
     * 用户编号
     */
    @Column("user_id")
    private long userId ;

    /**
     * 用户名
     */
    @Column("user_name")
    @ColDefine(width = 20)
    private String userName ;

    /**
     * 分类编号
     */
    @Column("category_id")
    private long categoryId ;

    /**
     * 商品名字
     */
    @Column
    @ColDefine(width = 20)
    private String title;

    /**
     * 商品状态
     */
    @Column
    @ColDefine(width = 20)
    private String status;

    /**
     * 内容
     */
    @Column
    @ColDefine(customType = "LONGTEXT")
    private String content ;

    /**
     * 售价
     */
    @Column
    @ColDefine(width = 36)
    private Double price ;

    /**
     *交易地址
     */
    @Column("transaction_address")
    private String transactionAddress;

    /**
     * 发布时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonField(dataFormat = "yyyy-MM-dd HH:mm:ss")
    @Column("publish_date")
    private Date publishDate;

    /**
     * 销售时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonField(dataFormat = "yyyy-MM-dd HH:mm:ss")
    @Column("sell_date")
    private Date sellDate;

    /**
     * 交易时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonField(dataFormat = "yyyy-MM-dd HH:mm:ss")
    @Column("transaction_date")
    private Date transactionDate;

    /**
     * 封面
     */
    @Column
    @ColDefine(width = 500)
    private String cover ;


    @Many(field = "goodsId")
    private List<UploadFile> files;
    

}
