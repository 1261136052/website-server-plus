package com.xxforest.baseweb.domain;

import com.xxforest.baseweb.core.ServerDao;
import com.xxforest.baseweb.core.inter.IDataInitialize;
import lombok.Data;
import org.nutz.dao.entity.annotation.*;

@Data
@Table("article_category")
@TableMeta("{'mysql-charset':'utf8mb4'}")
public class ArticleCategory implements IDataInitialize {

    /**
     * 编号
     */
    @Id(auto = false)
    private long id ;

    /**
     * 分类名称
     */
    @Column
    @ColDefine(width = 200)
    private String name ;


    @Override
    public void dataInitialize() {
        ServerDao serverDao = ServerDao.getInstance();
        ArticleCategory articleCategory = new ArticleCategory();
        articleCategory.setId(1);
        articleCategory.setName("默认分类");
        serverDao.insertIfNone(articleCategory);
    }
}
