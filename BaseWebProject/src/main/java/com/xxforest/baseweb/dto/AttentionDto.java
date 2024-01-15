package com.xxforest.baseweb.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.PK;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.TableMeta;
import org.nutz.json.JsonField;

import java.util.Date;

@Data
public class AttentionDto {


    private long userId ;


    private long attentionId ;

    private int likeCount ;

    private String userName;


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonField(dataFormat = "yyyy-MM-dd HH:mm:ss")
    private Date likeDate;

    public AttentionDto(long userId, long attentionId, int likeCount, String userName, Date likeDate){
        this.userId = userId;
        this.attentionId = attentionId;
        this.likeCount = likeCount;
        this.userName = userName;
        this.likeDate = likeDate;
    }

}
