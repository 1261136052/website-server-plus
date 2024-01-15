package com.xxforest.baseweb.manager;

import cn.hutool.core.util.StrUtil;
import com.xxforest.baseweb.core.ServerDao;
import com.xxforest.baseweb.core.enums.GoodsStatus;
import com.xxforest.baseweb.domain.Article;
import com.xxforest.baseweb.domain.Goods;
import org.nutz.dao.Cnd;
import org.nutz.dao.QueryResult;
import org.nutz.dao.pager.Pager;
import org.nutz.dao.sql.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class GoodsManager {

    @Autowired
    private ServerDao dao ;
    @Autowired
    private UploadFileManager uploadFileManager ;
    /**
     * 添加管理员
     * @param
     */
    public void insert(Goods goods){
        dao.insert(goods);
    }


    public List<Goods> listGoodsByUser(long userId) {
        return dao.query(Goods.class,
                Cnd.where("userId", "=", userId).desc("publishDate")
                ,null,"^id|title|cover|categoryId|userId$");
    }


    public List<Goods> listGoods() {
        return dao.query(Goods.class,
                Cnd.where("status","=", GoodsStatus.LAUNCH).desc("publishDate")
                ,null,"^id|title|cover|categoryId|userId|content$");
    }

    public Goods queryGoodsById(long id) {
        return dao.fetchLinks(dao.fetch(Goods.class,id),"files");
    }

    public void update(Goods goods) {
        dao.update(goods);
    }

    public void delete(Goods goods) {
        Goods goods1 = dao.fetchLinks(goods, "files");
        dao.delete(goods1);
       List<Long> ids = goods1.getFiles().stream().collect(ArrayList::new,
                (list,item)->list.add(item.getId()),ArrayList::addAll);
        uploadFileManager.deleteFiles(ids);
    }

    /**
     * 分页查询
     * @param pageNum 页码
     * @param pageSize 分页大小
     * @param keyword 关键字
     * @param startTimeDate 开始时间
     * @param endTimeDate 结束时间
     * @return
     */
    public QueryResult selectPage(int pageNum, int pageSize, String keyword,String categoryId, Date startTimeDate, Date endTimeDate) {
        Criteria cri = Cnd.cri();
        if(StrUtil.isNotBlank(keyword)){
            cri.where().or("title", "like", "%" + keyword + "%").or("content", "like", "%" + keyword + "%");
        }
        if(startTimeDate != null){
            cri.where().and("publishDate",">=",startTimeDate);
        }
        if(endTimeDate != null){
            cri.where().and("publishDate","<=",endTimeDate);
        }
        if (StrUtil.isNotBlank(categoryId)){
            cri.where().and("categoryId","=",Integer.parseInt(categoryId));
        }
        cri.where().and("status","=", GoodsStatus.LAUNCH);
        cri.getOrderBy().desc("publishDate");
        Pager pager = dao.createPager(pageNum, pageSize);

        List<Goods> list = dao.query(Goods.class, cri, pager, "^id|title|cover|categoryId|userId|content|price$");
        pager.setRecordCount(dao.count(Goods.class,cri));
        return new QueryResult(list, pager);

    }


    public QueryResult selectPage2(int pageNum, int pageSize, String keyword,String categoryId, Date startTimeDate, Date endTimeDate) {
        Criteria cri = Cnd.cri();
        if(StrUtil.isNotBlank(keyword)){
            cri.where().or("title", "like", "%" + keyword + "%").or("content", "like", "%" + keyword + "%");
        }
        if(startTimeDate != null){
            cri.where().and("publishDate",">=",startTimeDate);
        }
        if(endTimeDate != null){
            cri.where().and("publishDate","<=",endTimeDate);
        }
        cri.where().and("status","=", GoodsStatus.LAUNCH);
        cri.getOrderBy().desc("publishDate");
        Pager pager = dao.createPager(pageNum, pageSize);

        List<Goods> list = dao.query(Goods.class, cri, pager, "^id|title|cover|categoryId|userId|content|price|userName$");
        pager.setRecordCount(dao.count(Goods.class,cri));
        return new QueryResult(list, pager);
    }

}
