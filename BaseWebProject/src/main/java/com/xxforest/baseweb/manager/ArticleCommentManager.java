package com.xxforest.baseweb.manager;

import com.xxforest.baseweb.domain.Article;
import com.xxforest.baseweb.domain.ArticleComment;
import com.xxforest.baseweb.domain.ArticleLike;
import org.nutz.dao.Cnd;
import org.nutz.dao.Condition;
import org.nutz.dao.Dao;
import org.nutz.dao.QueryResult;
import org.nutz.dao.pager.Pager;
import org.nutz.dao.sql.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ArticleCommentManager {

    @Autowired
    private Dao dao;

    public ArticleLike selectByKey(long userid, long articleId){
        Condition c = Cnd.where("articleId","=",articleId).
                and("userId", "=", userid);
        return dao.fetch(ArticleLike.class, c);
    }

    public void addComment(ArticleComment comment) {
        dao.insert(comment);
    }


    public QueryResult selectPage(int page, int pageSize, long articleId) {
        Pager pager = dao.createPager(page, pageSize);
        Criteria cri = Cnd.cri();
        cri.where().and("articleId","=",articleId);
        cri.getOrderBy().desc("sendDate");
        List<ArticleComment> list = dao.query(ArticleComment.class, cri, pager);
        pager.setRecordCount(dao.count(ArticleComment.class,cri));
        return new QueryResult(list, pager);

    }
}
