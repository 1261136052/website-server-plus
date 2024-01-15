package com.xxforest.baseweb.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.nutz.json.JsonField;

import java.util.Date;


@Data
public class GoodsOrderDto   {

    private long id ;

    private long goodsId ;


    private long buyerId;

    private String buyerPhone;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonField(dataFormat = "yyyy-MM-dd HH:mm:ss")
    private Date buyDate;

    private String remark;

    private String buyer;

    private String vendor;
    private String title;


    private long vendorId;

    public GoodsOrderDto(long id, long goodsId, long buyerId, String buyerPhone, Date buyDate, String remark, String title,
                         String buyer, String vendor, long vendorId){
        this.title = title;
        this.id = id;
        this.goodsId = goodsId;
        this.buyerId = buyerId;
        this.buyerPhone = buyerPhone;
        this.buyDate = buyDate;
        this.remark = remark;
        this.buyer = buyer;
        this.vendor = vendor;
        this.vendorId = vendorId;
    }

}
