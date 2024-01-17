package com.xxforest.baseweb.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.json.JsonField;

import java.util.Date;

/**
 * 管理员表
 */
@Data
public class StatisticsDto {

    private int articleCount;
    private int goodsCount;
    private int orderCount;
    private int commentCount;
    private int likeCount;
    private int userCount;
    private int chatCount;

}
