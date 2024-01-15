package com.xxforest.baseweb.controller;

import cn.hutool.db.nosql.redis.RedisDS;
import cn.hutool.json.JSONUtil;
import com.xxforest.baseweb.core.RedisUtil;
import com.xxforest.baseweb.core.ResponseMessage;
import com.xxforest.baseweb.core.ServerDao;
import com.xxforest.baseweb.core.anno.Auth;
import com.xxforest.baseweb.core.anno.AuthType;
import com.xxforest.baseweb.domain.Admin;
import com.xxforest.baseweb.domain.ArticleLike;
import com.xxforest.baseweb.domain.User;
import com.xxforest.baseweb.manager.ArticleLikeManager;
import com.xxforest.baseweb.manager.ArticleManager;
import org.nutz.dao.Cnd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/like")
public class ArticleLikeController {

    @Autowired
    private ArticleLikeManager articleLikeManager ;
    @Autowired
    private ArticleManager articleManager ;

    @Auth(AuthType.USER)
    @GetMapping("/query/{article}/{user}")
    public ResponseMessage query(@PathVariable long article,@PathVariable long user){
        ArticleLike articleLike = articleLikeManager.selectByKey(user, article);
        if (articleLike==null){
            return ResponseMessage.success("data",false);
        }else {
            return ResponseMessage.success("data",true);
        }

    }


    @Auth(AuthType.USER)
    @GetMapping("/like/{article}/{user}")
    public ResponseMessage like(@PathVariable long article,@PathVariable long user){
        boolean isAdd = false;
        ArticleLike articleLike = articleLikeManager.selectByKey(user, article);
        if (articleLike==null){
            isAdd = true;
            ArticleLike newData = new ArticleLike();
            newData.setArticleId(article);
            newData.setUserId(user);
            newData.setLikeDate(new Date());
            articleLikeManager.addLike(newData);
            articleManager.updateLikeCount(article,isAdd);
            return ResponseMessage.success("data",isAdd);

        }else {
            articleLikeManager.deleteLike(articleLike);
            articleManager.updateLikeCount(articleLike.getArticleId(),isAdd);
            return ResponseMessage.success("data",isAdd);
        }

    }

}
