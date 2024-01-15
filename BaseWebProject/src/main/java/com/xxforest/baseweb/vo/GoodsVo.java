package com.xxforest.baseweb.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xxforest.baseweb.domain.UploadFile;
import lombok.Data;
import org.nutz.dao.entity.annotation.*;
import org.nutz.json.JsonField;

import java.util.Date;
import java.util.List;

/**
 * 管理员表
 */
@Data
public class GoodsVo {

    private long id ;

    /**
     * 用户编号
     */
    private long userId ;

    /**
     * 分类编号
     */
    private long categoryId ;

    /**
     * 商品名字
     */

    private String title;

    /**
     * 商品状态
     */
    private String status;

    /**
     * 内容
     */
    private String content ;

    /**
     * 售价
     */
    private Double price ;

    /**
     *交易地址
     */
    private String transactionAddress;

    /**
     * 发布时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonField(dataFormat = "yyyy-MM-dd HH:mm:ss")
    private Date publishDate;

    /**
     * 销售时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
    @JsonField(dataFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
    private Date sellDate;

    /**
     * 交易时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
    @JsonField(dataFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
    private Date transactionDate;

    /**
     * 封面
     */
    private String cover ;


    private List<Long> filesIds;

}
