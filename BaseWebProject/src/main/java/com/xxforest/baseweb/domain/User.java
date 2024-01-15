package com.xxforest.baseweb.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xxforest.baseweb.core.ServerDao;
import com.xxforest.baseweb.core.inter.IDataInitialize;
import lombok.Data;
import org.nutz.dao.entity.annotation.*;
import org.nutz.json.JsonField;

import java.util.Date;

@Data
@Table("user")
@TableMeta("{'mysql-charset':'utf8mb4'}")
public class User implements IDataInitialize {

    /**
     * 编号
     */
    @Id(auto = false)
    private long id ;

    /**
     * 账号
     */
    @Column
    @ColDefine(width = 20)
    private String account ;

    /**
     * token
     */
//    @Column
//    @ColDefine(width = 50)
    private String token ;

    /**
     * 用户名
     */
    @Column("user_name")
    @ColDefine(width = 20)
    private String userName ;

    /**
     * 密码
     */
    @Column
    @ColDefine(width = 20)
    private String pwd ;


    /**
     * 电话
     */
    @Column("phone_number")
    @ColDefine(width = 15)
    private String phoneNumber ;


    /**
     * 学校
     */
    @Column("school_name")
    @ColDefine(width = 15)
    private String schoolName ;

    /**
     * 学校
     */
    @Column("student_number")
    @ColDefine(width = 15)
    private String studentNumber ;



    /**
     * 邮箱
     */
    @Column
    @ColDefine(width = 36)
    private String email ;

    /**
     * 登录时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonField(dataFormat = "yyyy-MM-dd")
    @Column("birthday")
    private Date birthday;


    /**
     *  登录次数
     */
    @Column("login_times")
    private int loginTimes;

    /**
     *  登录次数
     */
    @Column("grade")
    private int grade;

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
        User user = new User();
        user.setId(1);
        user.setAccount("admin");
        user.setPwd("123456");
        user.setLoginDate(new Date());
        user.setLoginTimes(0);
//        user.setToken("000");
        serverDao.insertIfNone(user);//不存在数据的时候插入
    }

}
