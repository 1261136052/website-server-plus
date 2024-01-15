package com.xxforest.baseweb.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xxforest.baseweb.domain.Goods;
import lombok.Data;
import org.nutz.dao.entity.annotation.*;
import org.nutz.json.JsonField;

import java.util.Date;

@Data
public class BuyerVo {

    /**
     * 账号:admin
     */
    private long goodsId ;

    /**
     * 密码:123456
     */
    private long buyerId;

    /**
     * 密码:123456
     */
    private String buyerPhone;

    /**
     * 账号:admin
     */
    private String remark;

}
