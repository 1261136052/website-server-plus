package com.xxforest.baseweb.manager;

import cn.hutool.core.util.StrUtil;
import com.xxforest.baseweb.domain.Admin;
import com.xxforest.baseweb.domain.Article;
import com.xxforest.baseweb.domain.ArticleLike;
import org.nutz.dao.Cnd;
import org.nutz.dao.Condition;
import org.nutz.dao.Dao;
import org.nutz.dao.QueryResult;
import org.nutz.dao.pager.Pager;
import org.nutz.dao.sql.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class ArticleLikeManager {

    @Autowired
    private Dao dao;

    public ArticleLike selectByKey(long userid, long articleId){
        Condition c = Cnd.where("articleId","=",articleId).
                and("userId", "=", userid);
        return dao.fetch(ArticleLike.class, c);
    }

    public void addLike(ArticleLike newData) {
        dao.insert(newData);
    }

    public void deleteLike(ArticleLike articleLike) {
        dao.delete(articleLike);
    }
}
