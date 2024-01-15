package com.xxforest.baseweb.domain;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.lang.generator.SnowflakeGenerator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xxforest.baseweb.core.IdTool;
import com.xxforest.baseweb.core.ServerDao;
import com.xxforest.baseweb.core.inter.IDataInitialize;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import lombok.Value;
import org.nutz.dao.entity.annotation.*;
import org.nutz.json.JsonField;

import java.util.Date;

/**
 * 管理员表
 */
@Data
@Table("admin")
@TableMeta("{'mysql-charset':'utf8mb4'}")
public class Admin implements IDataInitialize {

    /**
     * 编号
     */
    @Id(auto = false)
    private long id ;

    /**
     * 账号:admin
     */
    @Column
    @ColDefine(width = 20)
    private String account ;

//    /**
//     * token
//     */
//    @Column
//    @ColDefine(width = 50)
    private String token ;

    /**
     * 密码:123456
     */
    @Column
    @ColDefine(width = 20)
    private String pwd ;

    /**
     *  登录次数
     */
    @Column("login_times")
    private int loginTimes;

    /**
     * 登录时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonField(dataFormat = "yyyy-MM-dd HH:mm:ss")
    @Column("login_date")
    private Date loginDate;


    /**
     * 初始化数据
     */
    @Override
    public void dataInitialize() {
        ServerDao serverDao = ServerDao.getInstance();
        Admin admin = new Admin();
        admin.setId(1);
        admin.setAccount("admin");
        admin.setPwd("123456");
        admin.setLoginDate(new Date());
        admin.setLoginTimes(0);
//        admin.setToken("000");
        serverDao.insertIfNone(admin);//不存在数据的时候插入
    }

    public void alterLoginTimes(int val) {
        this.loginTimes += val ;
    }
}
