package com.xxforest.baseweb.domain;

import com.xxforest.baseweb.core.ServerDao;
import com.xxforest.baseweb.core.inter.IDataInitialize;
import lombok.Data;
import org.nutz.dao.entity.annotation.*;

/**
 * 网站配置信息
 */
@Data
@Table("web_config")
@TableMeta("{'mysql-charset':'utf8mb4'}")
public class WebConfig implements IDataInitialize {

    /**
     * 配置类型
     */
    public enum ConfigType{
        /**文本*/
        TEXT,
        /**文件*/
        FILE
    }

    public enum Config{

        COMPANY_EMAIL("公司邮箱",ConfigType.TEXT,"Service@1daoyun.com"),
        /**公司地址*/
        COMPANY_ADDRESS("公司地址",ConfigType.TEXT,"公司地址XXXXXX"),
        /**公司名称*/
        COMPANY_NAME("公司名称",ConfigType.TEXT,"江苏一道云"),
        /**公司二维码*/
        COMPANY_QR_CODE("公司二维码",ConfigType.FILE,"http://二维码.png"),

        ;
        /**配置类型*/
        private ConfigType configType ;
        /**默认值*/
        private String defVal ;
        /**注释*/
        private String note ;

        Config(String note, ConfigType configType , String defVal){
            this.note = note;
            this.configType = configType ;
            this.defVal = defVal ;
        }

        public ConfigType getConfigType(){
            return this.configType ;
        }

        public String getDefVal(){
            return this.defVal ;
        }

        public String getNote() {
            return note;
        }
    }

    /**
     * 配置信息名称
     */
    @Name
    @Column("config_name")
    @ColDefine(width = 50)
    private String configName ;

    /**
     * 配置值
     */
    @Column
    @ColDefine(width = 500)
    private String val ;

    /**
     * 注释
     */
    @Column
    @ColDefine(width = 100)
    private String note;


    @Override
    public void dataInitialize() {
        ServerDao serverDao = ServerDao.getInstance();

        for (Config config : Config.values()) {
            WebConfig webConfig = new WebConfig();
            webConfig.setConfigName(config.name());
            webConfig.setVal(config.getDefVal());
            webConfig.setNote(config.getNote());
            serverDao.insertIfNone(webConfig);
        }

    }
}
