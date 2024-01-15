package com.xxforest.baseweb.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.nutz.dao.entity.annotation.*;
import org.nutz.json.JsonField;

import java.util.Date;

@Data
public class GoodsChatDto {


    private long id ;

    private long readerId;

    private long listenerId;

    private String content ;

    private Date publishDate;

    private String reader;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonField(dataFormat = "yyyy-MM-dd HH:mm:ss")
    private String listener;

    public GoodsChatDto(long id, long readerId, long listenerId, String content,
                        Date publishDate, String reader, String listener){
        this.id = id;
        this.readerId = readerId;
        this.listenerId = listenerId;
        this.content = content;
        this.publishDate = publishDate;
        this.reader = reader;
        this.listener = listener;
    }

}
