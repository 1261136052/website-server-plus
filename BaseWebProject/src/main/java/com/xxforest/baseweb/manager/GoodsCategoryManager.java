package com.xxforest.baseweb.manager;

import cn.hutool.core.util.StrUtil;
import com.xxforest.baseweb.core.IdTool;
import com.xxforest.baseweb.core.ServerDao;
import com.xxforest.baseweb.domain.Goods;
import com.xxforest.baseweb.domain.GoodsCategory;
import com.xxforest.baseweb.domain.User;
import org.nutz.dao.Cnd;
import org.nutz.dao.QueryResult;
import org.nutz.dao.pager.Pager;
import org.nutz.dao.sql.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GoodsCategoryManager {

    @Autowired
    private ServerDao dao ;

    /**
     * 根据token获取用户
     * @param
     * @return
     */
    public List<GoodsCategory> listGoodsCategory(){
        return dao.query(GoodsCategory.class, null);
    }

    public QueryResult selectPage(int pageNum, int pageSize, String keyword) {
        Criteria cri = Cnd.cri();
        if(StrUtil.isNotBlank(keyword)){
            cri.where().or("categoryName", "like", "%" + keyword + "%");
        }
        Pager pager = dao.createPager(pageNum, pageSize);
        List<GoodsCategory> list = dao.query(GoodsCategory.class, cri, pager);
        pager.setRecordCount(dao.count(GoodsCategory.class,cri));
        return new QueryResult(list, pager);
    }

    public Object addGoodsCategory(GoodsCategory goodsCategory) {
        if (!StrUtil.isNotBlank(goodsCategory.getCategoryName())) return 0;
        goodsCategory.setId(IdTool.nextId());
        return dao.insert(goodsCategory);
    }

    public Object updateGoodsCategory(GoodsCategory goodsCategory) {
        if (!StrUtil.isNotBlank(goodsCategory.getCategoryName())) return 0;
        return dao.update(goodsCategory);
    }

    public Object deleteGoodsCategory(long id) {
        return dao.delete(GoodsCategory.class,id);
    }
}
