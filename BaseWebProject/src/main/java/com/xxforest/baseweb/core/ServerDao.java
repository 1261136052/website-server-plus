package com.xxforest.baseweb.core;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ClassUtil;
import com.xxforest.baseweb.core.inter.IDataInitialize;
import com.xxforest.baseweb.domain.Admin;
import org.apache.commons.dbcp2.BasicDataSource;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.FieldFilter;
import org.nutz.dao.entity.Entity;
import org.nutz.dao.entity.MappingField;
import org.nutz.dao.impl.NutDao;
import org.nutz.dao.impl.SimpleDataSource;
import org.nutz.dao.util.Daos;
import org.nutz.lang.*;

import javax.sql.DataSource;
import java.util.Set;
import java.util.logging.Logger;

public class ServerDao extends NutDao{
    private static ServerDao inst ;

    public static ServerDao getInstance(){
        if(inst == null){
            inst = new ServerDao();
        }
        return inst;
    }

    public void init(){
        Logger.getLogger(this.getClass().getSimpleName()).info("链接数据库...");
//        使用dbcp连接池
        BasicDataSource ds = new BasicDataSource();
//        ds.setUrl("jdbc:mysql://139.9.189.19:3306/webtestdb?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai");
//        ds.setUsername("root");
//        ds.setPassword("2001+07+12ZSDzsd");
        ds.setUrl("jdbc:mysql://127.0.0.1:3306/webtestdb?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai");
        ds.setUsername("root");
        ds.setPassword("ZSDzsd123");
        ds.setValidationQuery("select 1");

        ds.setTestOnBorrow(true);
        ds.setTestOnReturn(true);
        ds.setTestWhileIdle(true);
        // 大于0 ，进行连接空闲时间判断，或为0，对空闲的连接不进行验证
        ds.setMinEvictableIdleTimeMillis(5 * 60 * 1000);
        // 失效检查线程运行时间间隔，如果小于等于0，不会启动检查线程
        ds.setTimeBetweenEvictionRunsMillis(10 * 60 * 1000);
        ds.setMaxTotal(3); // 最大激活连接数
        ds.setInitialSize(3); // 初始连接池连接个数

        this.setDataSource(ds);
        Logger.getLogger(this.getClass().getSimpleName()).info("更新表结构...");
        this.migrationTables();
        this.initializationData();
        Logger.getLogger(this.getClass().getSimpleName()).info("数据库初始化成功");

    }

    public Dao getDao(){
        return this ;
    }

    /**
     * 更新表数据
     */
    private void migrationTables() {
        Daos.createTablesInPackage(this, "com.xxforest.baseweb.domain", false);
//        Daos.migration(this, "com.xxforest.baseweb.domain", true, false, false;        Daos.migration(this, "com.xxforest.baseweb.domain", true, true, true);
        Daos.migration(this, "com.xxforest.baseweb.domain", true, false, false);

    }

    /**
     * 初始化数据
     */
    private void initializationData() {
        Set<Class<?>> classes = ClassUtil.scanPackage("com.xxforest.baseweb.domain");
        for (Class<?> aClass : classes) {
            try{
                if(ArrayUtil.contains(aClass.getInterfaces(), IDataInitialize.class)){
                    Object newInstance = aClass.newInstance();
                    IDataInitialize dataInitialize = (IDataInitialize)newInstance ;
                    dataInitialize.dataInitialize();
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    public <T> T insertIfNone(T t) {
        if (t == null)
            return null;

        FieldFilter insertFieldFilter = null ;
        Object obj = Lang.first(t);
        final Entity<?> en = getEntity(obj.getClass());
        Lang.each(t, new Each<Object>() {

            public void invoke(int index, Object ele, int length) throws ExitLoop, ContinueLoop, LoopException {

                boolean shall_update = false;
                MappingField mf = en.getNameField();
                if (mf != null) {
                    Object val = mf.getValue(ele);
                    if (val != null && fetch(en.getType(), Cnd.where(mf.getName(), "=", val)) != null) {
                        shall_update = true;
                    }
                }
                else if (en.getIdField() != null) {
                    mf = en.getIdField();
                    Object val = mf.getValue(ele);
                    if (val != null && fetch(ele) != null) {
                        shall_update = true;
                    }
                }
                else {
                    shall_update = fetch(ele) != null;
                }
                if (!shall_update){
                    insert(ele, insertFieldFilter);
                }

            }
        });
        return t;
    }


}
