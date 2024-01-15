package com.xxforest.baseweb.manager;

import com.xxforest.baseweb.core.ServerDao;
import com.xxforest.baseweb.domain.Admin;
import com.xxforest.baseweb.domain.GoodsChat;
import com.xxforest.baseweb.domain.User;
import com.xxforest.baseweb.dto.AttentionDto;
import com.xxforest.baseweb.dto.GoodsChatDto;
import com.xxforest.baseweb.dto.Links;
import org.nutz.dao.Cnd;
import org.nutz.dao.QueryResult;
import org.nutz.dao.Sqls;
import org.nutz.dao.pager.Pager;
import org.nutz.dao.sql.Criteria;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.sql.SqlCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Component
public class GoodsChatManager {

    @Autowired
    private ServerDao dao ;

    @Autowired
    private UserManager userManager;

    public void insert(GoodsChat goodsChat){
        dao.insert(goodsChat);
    }

    public List<Links> getLinks(long id) {
        List<Long> readerIds = dao.query(GoodsChat.class, Cnd.where("listenerId", "=", id).groupBy("readerId"),
                null, "^readerId$")
                .stream().collect(ArrayList::new, (list, item) -> list.add(item.getReaderId()), ArrayList::addAll);
        List<Long> listenerIds = dao.query(GoodsChat.class, Cnd.where("readerId", "=", id).groupBy("listenerId"),
                null, "^listenerId$")
                .stream().collect(ArrayList::new, (list, item) -> list.add(item.getListenerId()), ArrayList::addAll);

        for (Long readerId : readerIds) {
            if (!listenerIds.contains(readerId)){
                listenerIds.add(readerId);
            }
        }
        List<User> users = userManager.userName(listenerIds);
        List<Links> links = new ArrayList<>();
        for (User user : users) {
            links.add(new Links(user.getId(), user.getUserName()));
        }
        return links;
    }

    public List<GoodsChat> getMessages(long id, String userId) {
        Criteria cri = Cnd.cri();
        cri.where().andIn("readerId",id,Long.parseLong (userId)).andIn("listenerId",id,Long.parseLong(userId));
        cri.getOrderBy().asc("publishDate");

        return dao.query(GoodsChat.class,cri);

    }

    public List<GoodsChat> getMessages2(long id, String userId, Date date) {

        Criteria cri = Cnd.cri();
        cri.where().andIn("readerId",id,Long.parseLong (userId)).
                andIn("listenerId",id,Long.parseLong(userId)).andBetween("publishDate",date,new Date());
        cri.getOrderBy().asc("publishDate");

        return dao.query(GoodsChat.class,cri);
    }

    public QueryResult selectPage(int pageNum, int pageSize, String keyword) {
        Sql sql = Sqls.create("SELECT d.*,u.user_name as listener FROM \n" +
                "(SELECT c.*,u.user_name as reader FROM goods_chat c JOIN `user` u ON c.reader_id = u.id where c.content like @keyword) d \n" +
                "JOIN `user` u ON d.listener_id = u.id LIMIT @front,@rear");
        sql.params().set("front", pageNum*pageSize - pageSize);
        sql.params().set("rear", pageNum*pageSize);
        sql.params().set("keyword", "%"+keyword+"%");

        sql.setCallback(new SqlCallback() {
            public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
                List<GoodsChatDto> list = new LinkedList<GoodsChatDto>();
                while (rs.next()){
                    System.out.println(rs);
                    list.add(new GoodsChatDto(rs.getLong("id"),rs.getLong("reader_id"),rs.getLong("listener_id"),
                            rs.getString("content"),rs.getDate("publish_date"),rs.getString("reader"),rs.getString("listener")));
                }

                return list;
            }
        });
        dao.execute(sql);
        Sql countSql = Sqls.create("SELECT COUNT(*) AS total FROM (SELECT d.*,u.user_name as listener FROM \n" +
                "(SELECT c.*,u.user_name reader FROM goods_chat c JOIN `user` u ON c.reader_id = u.id WHERE c.content LIKE @keyword) d \n" +
                "JOIN `user` u ON d.listener_id = u.id) AS a ");
        countSql.params().set("keyword", "%"+keyword+"%");
        countSql.setCallback(Sqls.callback.integer());
        dao.execute(countSql);
        Pager pager = dao.createPager(pageNum, pageSize);
        pager.setRecordCount(countSql.getInt());
        return new QueryResult(sql.getList(AttentionDto.class), pager);
    }

    public Object update(long chatId, String content) {
        GoodsChat chat = dao.fetch(GoodsChat.class, chatId);
        if (chat == null) return 0;
        chat.setContent(content);
        return dao.update(chat);
    }

    public Object delete(long chatId) {
        return dao.delete(GoodsChat.class,chatId);
    }
}
