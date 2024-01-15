package com.xxforest.baseweb.manager;

import cn.hutool.core.util.StrUtil;
import com.xxforest.baseweb.core.ServerDao;
import com.xxforest.baseweb.core.enums.GoodsStatus;
import com.xxforest.baseweb.domain.Admin;
import com.xxforest.baseweb.domain.Goods;
import com.xxforest.baseweb.domain.User;
import org.nutz.dao.Cnd;
import org.nutz.dao.QueryResult;
import org.nutz.dao.pager.Pager;
import org.nutz.dao.sql.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserManager {

    @Autowired
    private ServerDao dao ;

    /**
     * 根据token获取用户
     * @param token
     * @return
     */
    public User selectByToken(String token){
        return dao.fetch(User.class, Cnd.where("token", "=", token));
    }

    /**
     * 根据账号密码查询用户
     * @param account
     * @param pwd
     * @return
     */
    public User selectByAccountAndPwd(String account , String pwd){
        return dao.fetch(User.class,Cnd.where("account","=",account).and("pwd","=",pwd));
    }


    public void insert(User user){
        dao.insert(user);
    }

    public void update(User user){
        dao.update(user,"^schoolName|studentNumber|email|phoneNumber|birthday$");
    }

    public List<User> userName(List<Long> ids){
        return dao.query(User.class, Cnd.where("id","in",ids),null,"^id|userName$");

    }

    public String userName(long userId) {
        User fetch = dao.fetch(User.class, userId);
        if (fetch==null) {
            return null;
        }
        return fetch.getUserName();
    }

    public User selectById(long id){
        return dao.fetch(User.class,id);
    }

    public QueryResult selectPage(int pageNum, int pageSize, String keyword) {
        Criteria cri = Cnd.cri();
        if(StrUtil.isNotBlank(keyword)){
            cri.where().or("userName", "like", "%" + keyword + "%");
        }
        Pager pager = dao.createPager(pageNum, pageSize);
        List<User> list = dao.query(User.class, cri, pager);
        pager.setRecordCount(dao.count(User.class,cri));
        return new QueryResult(list, pager);
    }

    public Object deleteName(long userId) {
        User fetch = dao.fetch(User.class, userId);
        if (fetch==null) {
            return -1;
        }
        return dao.delete(fetch);
    }
    public Object adminUpdate(User user) {
       return dao.update(user,"^account|pwd|userName$");
    }
}
