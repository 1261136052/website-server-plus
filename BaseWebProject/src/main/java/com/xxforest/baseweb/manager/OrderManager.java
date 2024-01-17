package com.xxforest.baseweb.manager;

import com.xxforest.baseweb.core.enums.GoodsStatus;
import com.xxforest.baseweb.domain.Goods;
import com.xxforest.baseweb.domain.GoodsOrder;
import com.xxforest.baseweb.dto.AttentionDto;
import com.xxforest.baseweb.dto.GoodsOrderDto;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.QueryResult;
import org.nutz.dao.Sqls;
import org.nutz.dao.pager.Pager;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.sql.SqlCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Component
public class OrderManager {

    @Autowired
    private Dao dao;
    @Autowired
    private GoodsManager goodsManager;

    public List<GoodsOrder> listBuyGoods(long id) {
        List<GoodsOrder> query = dao.query(GoodsOrder.class, Cnd.where("buyerId", "=", id).desc("buyDate"));
        return dao.fetchLinks(query,"goods");
    }

    public List<GoodsOrder> listWaitGoods(long id) {
        List<Goods> query = dao.query(Goods.class, Cnd.where("userId", "=", id).and("status","=",GoodsStatus.WAIT.name()));
        List<Long> ids =  query.stream().collect(ArrayList::new, (list, item) -> list.add(item.getId()), ArrayList::addAll);
        List<GoodsOrder> query2 = dao.query(GoodsOrder.class, Cnd.where("goodsId", "in", ids).desc("buyDate"));
        return dao.fetchLinks(query2,"goods");
    }

    public List<GoodsOrder> listSellGoods(long id) {
        List<Long> ids = dao.query(Goods.class, Cnd.where("userId", "=", id).
                and("status","=", GoodsStatus.SELL)).stream().collect(ArrayList::new
                , (list, item) -> list.add(item.getId()), ArrayList::addAll);
        List<GoodsOrder> query = dao.query(GoodsOrder.class, Cnd.where("goodsId", "in", ids).desc("buyDate"));
        return dao.fetchLinks(query,"goods");
    }

    public void add(GoodsOrder goodsOrder) {
        dao.insert(goodsOrder);
    }

    public QueryResult selectPage(int pageNum, int pageSize, String keyword) {
        Sql sql = Sqls.create("SELECT d.*,u.user_name AS buyer FROM " +
                "(SELECT go.*,g.title AS `title`,g.user_name AS vendor,g.user_id AS vendor_id FROM " +
                "goods_order go JOIN goods g ON go.goods_id = g.id where g.title like @keyword ) d " +
                "JOIN `user` u ON d.buyer_id = u.id LIMIT @front,@rear");
        sql.params().set("front", pageNum*pageSize - pageSize);
        sql.params().set("rear", pageNum*pageSize);
        sql.params().set("keyword", "%"+keyword+"%");
//        sql.params().set("categoryId", categoryId);


        sql.setCallback(new SqlCallback() {
            public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
                List<GoodsOrderDto> list = new LinkedList<GoodsOrderDto>();
                while (rs.next()){
                    System.out.println(rs);
                    list.add(new GoodsOrderDto(rs.getLong("id"),rs.getLong("goods_id"),rs.getLong("buyer_id"),
                            rs.getString("buyer_phone"),rs.getDate("buy_date"),rs.getString("remark"),rs.getString("title"),rs.getString("buyer")
                    ,rs.getString("vendor"),rs.getLong("vendor_id")));
                }

                return list;
            }
        });
        dao.execute(sql);
        Sql countSql = Sqls.create("SELECT count(*) FROM " +
                "(SELECT go.*,g.title AS `title`,g.user_name AS vendor,g.user_id AS vendor_id FROM " +
                "goods_order go JOIN goods g ON go.goods_id = g.id where g.title like @keyword) d " +
                "JOIN `user` u ON d.buyer_id = u.id");
        countSql.params().set("keyword", "%"+keyword+"%");
        countSql.setCallback(Sqls.callback.integer());
        dao.execute(countSql);
        Pager pager = dao.createPager(pageNum, pageSize);
        pager.setRecordCount(countSql.getInt());
        return new QueryResult(sql.getList(GoodsOrderDto.class), pager);
    }

    public Object deleteOrder(long id) {
        return dao.delete(GoodsOrder.class,id);
    }

    public Object confirm(int isConfirm, Long orderId, long id) {
        GoodsOrder order = dao.fetch(GoodsOrder.class,orderId);
        Goods goods = dao.fetch(Goods.class, order.getGoodsId());
        if (isConfirm==0&& goods.getStatus().equals(GoodsStatus.WAIT.name()) &&goods.getUserId() == id){
            goods.setStatus(GoodsStatus.SELL.name());
            return dao.update(goods);
        }else if (isConfirm==1&& goods.getStatus().equals(GoodsStatus.WAIT.name()) &&goods.getUserId() == id){
            goods.setStatus(GoodsStatus.LAUNCH.name());
            dao.delete(order);
            return dao.update(goods);
        }
        return  -1;
    }
}
