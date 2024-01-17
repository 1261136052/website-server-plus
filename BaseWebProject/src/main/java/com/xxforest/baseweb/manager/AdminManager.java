package com.xxforest.baseweb.manager;

import com.xxforest.baseweb.core.ServerDao;
import com.xxforest.baseweb.domain.*;
import com.xxforest.baseweb.dto.StatisticsDto;
import org.nutz.dao.Cnd;
import org.nutz.dao.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.xml.stream.events.Comment;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class AdminManager {

    @Autowired
    private ServerDao dao ;

    @Autowired
    private UserManager userManager;

    /**
     * 根据toekn 获取管理员
     * @param token
     * @return
     */
    public Admin selectByToken(String token){
        return dao.fetch(Admin.class, Cnd.where("token", "=", token));
    }

    /**
     * 根据账号密码获取管理员
     * @param account 账号
     * @param pwd 密码
     * @return
     */
    public Admin selectByAccountAndPwd(String account , String pwd){
        return dao.fetch(Admin.class,Cnd.where("account","=",account).and("pwd","=",pwd));
    }

    /**
     * 添加管理员
     * @param admin
     */
    public void insert(Admin admin){
        dao.insert(admin);
    }

    /**
     * 修改管理员
     * @param admin
     */
    public void update(Admin admin){
        dao.update(admin);
    }

    public StatisticsDto selectStatistics() {
        StatisticsDto statistic = new StatisticsDto();
        statistic.setArticleCount(dao.count(Article.class));
        statistic.setCommentCount(dao.count(ArticleComment.class));
        statistic.setLikeCount(dao.count(ArticleLike.class));
        statistic.setUserCount(dao.count(User.class));
        statistic.setGoodsCount(dao.count(Goods.class));
        statistic.setOrderCount(dao.count(GoodsOrder.class));
        statistic.setChatCount(dao.count(GoodsChat.class));
        return statistic;
    }
}
