package com.xxforest.baseweb.manager;

import cn.hutool.core.util.StrUtil;
import com.xxforest.baseweb.core.IdTool;
import com.xxforest.baseweb.domain.Article;
import com.xxforest.baseweb.domain.ArticleCategory;
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
public class ArticleCategoryManager {

    @Autowired
    private Dao dao;

    /**
     * 分页查询
     * @param pageNum 页码
     * @param pageSize 分页大小
     * @param keyword 关键字
     * @return
     */
    public QueryResult selectPage(int pageNum, int pageSize, String keyword) {
        Criteria cri = Cnd.cri();
        if(StrUtil.isNotBlank(keyword)){
            cri.where().or("name", "like", "%" + keyword + "%");
        }
        Pager pager = dao.createPager(pageNum, pageSize);

        List<ArticleCategory> list = dao.query(ArticleCategory.class, cri, pager);
        pager.setRecordCount(dao.count(ArticleCategory.class,cri));
        return new QueryResult(list, pager);

    }


    public Object addArticleCategory(ArticleCategory articleCategory) {
        if (!StrUtil.isNotBlank(articleCategory.getName())) return 0;
        articleCategory.setId(IdTool.nextId());
        return dao.insert(articleCategory);
    }

    public Object updateArticleCategory(ArticleCategory articleCategory) {
        if (!StrUtil.isNotBlank(articleCategory.getName())) return 0;
        return dao.update(articleCategory);
    }

    public Object deleteArticleCategory(long id) {
        return dao.delete(ArticleCategory.class, id);
    }
}
