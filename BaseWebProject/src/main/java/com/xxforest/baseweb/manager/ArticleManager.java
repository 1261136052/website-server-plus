package com.xxforest.baseweb.manager;

import cn.hutool.core.util.StrUtil;
import com.xxforest.baseweb.domain.Article;
import com.xxforest.baseweb.domain.ArticleLike;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.QueryResult;
import org.nutz.dao.pager.Pager;
import org.nutz.dao.sql.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class ArticleManager {

    @Autowired
    private Dao dao;
    @Autowired
    private AttentionManager attentionManager;

    /**
     * 分页查询
     * @param pageNum 页码
     * @param pageSize 分页大小
     * @param keyword 关键字
     * @param startTimeDate 开始时间
     * @param endTimeDate 结束时间
     * @param categoryId
     * @return
     */
    public QueryResult selectPage(int pageNum, int pageSize, String keyword, Date startTimeDate, Date endTimeDate, Integer categoryId) {
        Criteria cri = Cnd.cri();
        if(StrUtil.isNotBlank(keyword)){
            cri.where().or("title", "like", "%" + keyword + "%")
                    .or("content", "like", "%" + keyword + "%")
            .or("author", "like", "%" + keyword + "%");
        }
        if(startTimeDate != null){
            cri.where().and("publishDate",">=",startTimeDate);
        }
        if(endTimeDate != null){
            cri.where().and("publishDate","<=",endTimeDate);
        }

        if(categoryId != null){
            cri.where().and("articleCategoryId","=",categoryId);
        }

        Pager pager = dao.createPager(pageNum, pageSize);

        List<Article> list = dao.query(Article.class, cri, pager,"^title|id|cover|author|publishDate" +
                "|articleCategoryId|likeCount|commentCount$");
        pager.setRecordCount(dao.count(Article.class,cri));
        return new QueryResult(list, pager);

    }


    public void updateLikeCount(long articleId,boolean addOrReduce) {
        Article article = dao.fetch(Article.class, articleId);
        if (article!=null){
            if (addOrReduce){
                article.setLikeCount(article.getLikeCount() + 1);
            }else {
                article.setLikeCount(article.getLikeCount() - 1);
            }
            dao.update(article);
        }
    }

    public QueryResult selectHot() {
        Pager pager = dao.createPager(1, 5);
        Criteria cri = Cnd.cri();
        cri.getOrderBy().desc("likeCount");
        List<Article> list = dao.query(Article.class, cri, pager,"^title|id|cover|author|publishDate|articleCategoryId");
        pager.setRecordCount(dao.count(Article.class,cri));
        return new QueryResult(list, pager);
    }

    public QueryResult TempleQuery(long id) {
        Pager pager = dao.createPager(1, 10000);
        Criteria cri = Cnd.cri();

        List<Long> temple = attentionManager.temple(id);
        temple.add(id);

        cri.where().and("authorId", "in",temple);
        cri.getOrderBy().desc("publishDate");
        List<Article> list = dao.query(Article.class, cri, pager,"^title|id|cover|author|publishDate|articleCategoryId");
        pager.setRecordCount(dao.count(Article.class,cri));
        return new QueryResult(list, pager);
    }

    public Article queryById(long articleId) {
        return dao .fetch(Article.class, articleId);
    }

    public void addCommentCount(long articleId,int count) {
         Article article = dao.fetch(Article.class, articleId);
         if (article==null) return;
         article.setCommentCount(article.getCommentCount() + count);
         dao.update(article);
    }
}
