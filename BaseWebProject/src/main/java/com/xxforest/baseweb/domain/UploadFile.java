package com.xxforest.baseweb.domain;

import lombok.Data;
import lombok.ToString;
import org.nutz.dao.entity.annotation.*;

import java.util.Date;

@Data
@ToString
@Table("upload_file")
@TableMeta("{'mysql-charset':'utf8mb4'}")
public class UploadFile {

    public static final String DEF_GROUP_NAME = "默认分组";

    /**
     * 编号
     */
    @Id(auto = false)
    private long id;

    /**
     * 编号
     */
    @Column("goods_id")
    private long goodsId;

    /**
     * 文件名
     */
    @Column
    @ColDefine(width = 50)
    private String name;

    @Column("save_name")
    @ColDefine(width = 50)
    private String saveName;

    /**
     * 上传日期
     */
    @Column("upload_date")
    private Date uploadDate;

    /**
     * 路径
     */
    @Column
    @ColDefine(width = 500)
    private String path;

    /**
     * 真实存储路径
     */
    @Column("system_path")
    @ColDefine(width = 500)
    private String systemPath;

    /**
     * 文件大小
     */
    @Column
    private long fileSize;

    /**
     * 是否临时文件 0是，1否
     */
    @Column("temp_file")
    private int tempFile;

    /**
     * 文件后缀
     */
    @Column
    private String postfix;
}
