package com.xxforest.baseweb.manager;

import cn.hutool.core.util.StrUtil;
import com.xxforest.baseweb.domain.Admin;
import com.xxforest.baseweb.domain.Article;
import com.xxforest.baseweb.domain.ArticleComment;
import com.xxforest.baseweb.domain.ArticleLike;
import com.xxforest.baseweb.dto.ArticleLikeDto;
import org.nutz.dao.Cnd;
import org.nutz.dao.Condition;
import org.nutz.dao.Dao;
import org.nutz.dao.QueryResult;
import org.nutz.dao.pager.Pager;
import org.nutz.dao.sql.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class ArticleLikeManager {

    @Autowired
    private Dao dao;

    @Autowired
    private UserManager userManager;

    @Autowired
    private ArticleManager articleManager;

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

    public QueryResult selectPage(int page, int pageSize, long articleId) {
        Pager pager = dao.createPager(page, pageSize);
        Criteria cri = Cnd.cri();
        cri.where().and("articleId","=",articleId);
        cri.getOrderBy().desc("likeDate");
        List<ArticleLike> list = dao.query(ArticleLike.class, cri, pager);
        List<ArticleLikeDto> lists = new ArrayList<>();
        for (ArticleLike a: list){
            ArticleLikeDto dto = new ArticleLikeDto();
            dto.setArticleId(a.getArticleId());
            dto.setLikeDate(a.getLikeDate());
            dto.setUserId(a.getUserId());
            dto.setUserName(userManager.userName(a.getUserId()));
            lists.add(dto);
        }
        pager.setRecordCount(dao.count(ArticleLike.class,cri));
        return new QueryResult(lists, pager);
    }


    public Object delete(long articleId, long userId) {
        ArticleLike articleLike = selectByKey(userId, articleId);
        if (articleLike!= null){
            articleManager.updateLikeCount(articleId,false);
            return  dao.delete(articleLike);
        }
        return -1;
    }
}
