package com.xxforest.baseweb.manager;

import cn.hutool.core.util.StrUtil;
import com.xxforest.baseweb.core.ServerDao;
import com.xxforest.baseweb.core.enums.GoodsStatus;
import com.xxforest.baseweb.domain.*;
import com.xxforest.baseweb.dto.AttentionDto;
import org.nutz.dao.*;
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
import java.util.LinkedList;
import java.util.List;

@Component
public class AttentionManager {

    @Autowired
    private ServerDao dao;

    @Autowired
    private UserManager userManager;


    public Attention selectByKey(long user, long attention) {
        Condition c = Cnd.where("userId","=",user).
                and("attentionId", "=", attention);
        return dao.fetch(Attention.class, c);
    }



    public void del(Attention data) {
        dao.delete(data);
    }

    public void addAttention(Attention data) {
        dao.insert(data);
    }
    public List<Long> temple(long userId) {
        List<Attention> query = dao.query(Attention.class, Cnd.where("userId", "=", userId), null, "^attentionId$");

        return  query.stream().collect(ArrayList::new,(list,i) -> list.add(i.getAttentionId()),ArrayList::addAll);
    }

    public QueryResult selectPage(int pageNum, int pageSize, String keyword) {
        Sql sql = Sqls.create("Select attention_id,COUNT(*) AS `like_count`,u.user_name from attention a " +
                "join `user` u ON  a.attention_id = u.id where u.user_name like @keyword  group by attention_id LIMIT @front,@rear");
        sql.params().set("front", pageNum*pageSize - pageSize);
        sql.params().set("rear", pageNum*pageSize);
        sql.params().set("keyword", "%"+keyword+"%");

        sql.setCallback(new SqlCallback() {
            public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
                List<AttentionDto> list = new LinkedList<AttentionDto>();
                while (rs.next()){
                    System.out.println(rs);
                    list.add(new AttentionDto(-1L,rs.getLong("attention_id"),
                            rs.getInt("like_count"),rs.getString("user_name"),null));
                }
                    
                return list;
            }
        });
        dao.execute(sql);


        Sql countSql = Sqls.create("SELECT COUNT(*) AS `total_count`\n" +
                "FROM (\n" +
                "    SELECT attention_id, like_date, COUNT(*) AS `like_count`, u.user_name\n" +
                "    FROM attention a\n" +
                "    JOIN `user` u ON a.attention_id = u.id\n" +
                "    where u.user_name like @keyword \n" +
                "    GROUP BY attention_id\n" +
                ") AS subquery;");
        countSql.params().set("keyword", "%"+keyword+"%");
        countSql.setCallback(Sqls.callback.integer());
        dao.execute(countSql);
        Pager pager = dao.createPager(pageNum, pageSize);
        pager.setRecordCount(countSql.getInt());
        return new QueryResult(sql.getList(AttentionDto.class), pager);
    }

    public QueryResult selectDetailPage(int pageNum, int pageSize, long attentionId) {
        Criteria cri = Cnd.cri();
        cri.where().or("attentionId", "=", attentionId);
        Pager pager = dao.createPager(pageNum, pageSize);
        List<Attention> list = dao.query(Attention.class, cri, pager);
        List<AttentionDto> ans = new LinkedList<AttentionDto>();
        for (Attention attention : list) {
            ans.add(new AttentionDto(attention.getUserId(),attentionId,0,
                    userManager.userName(attention.getUserId()),attention.getLikeDate()));
        }
        pager.setRecordCount(dao.count(Attention.class,cri));
        return new QueryResult(ans, pager);
    }

    public Object delete(long attentionId,long userId) {
       return dao.delete(dao.fetch(Attention.class,
               Cnd.where("attentionId", "=", attentionId).and("userId", "=", userId)));
    }
}
