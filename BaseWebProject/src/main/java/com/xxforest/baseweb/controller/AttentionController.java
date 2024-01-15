package com.xxforest.baseweb.controller;

import cn.hutool.db.nosql.redis.RedisDS;
import com.xxforest.baseweb.core.RedisUtil;
import com.xxforest.baseweb.core.ResponseMessage;
import com.xxforest.baseweb.core.ServerDao;
import com.xxforest.baseweb.core.anno.Auth;
import com.xxforest.baseweb.core.anno.AuthType;
import com.xxforest.baseweb.domain.ArticleLike;
import com.xxforest.baseweb.domain.Attention;
import com.xxforest.baseweb.domain.User;
import com.xxforest.baseweb.manager.AttentionManager;
import org.nutz.dao.Cnd;
import org.nutz.dao.QueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/attention")
public class AttentionController {

    @Autowired
    private AttentionManager attentionManager ;



    @Auth(AuthType.USER)
    @GetMapping("/isAttention/{user}/{attention}")
    public ResponseMessage isAttention(@PathVariable long user, @PathVariable long attention){
        Attention data = attentionManager.selectByKey(user, attention);
        if (data==null){
            return ResponseMessage.success("data",false);
        }else {
            return ResponseMessage.success("data",true);
        }

    }


    @Auth(AuthType.USER)
    @GetMapping("/addOrCancel/{user}/{attention}")
    public ResponseMessage addOrCancel(@PathVariable long user, @PathVariable long attention){
        Attention data = attentionManager.selectByKey(user, attention);
        if (data==null){
            data  =new  Attention();
            data.setUserId(user);
            data.setAttentionId(attention);
            data.setLikeDate(new Date());
            attentionManager.addAttention(data);
            return ResponseMessage.success("data",true);
        }else {
            attentionManager.del(data);
            return ResponseMessage.success("data",false);
        }

    }

    @Auth(AuthType.ADMIN)
    @GetMapping("/list/{page}/{pageSize}")
    public ResponseMessage loadUserList(@PathVariable("page") int page , @PathVariable int pageSize,String keyword ) {
        QueryResult queryResult = attentionManager.selectPage(page,pageSize,keyword);
        return ResponseMessage.success("data",queryResult);
    }

    @Auth(AuthType.ADMIN)
    @GetMapping("/detail/{page}/{pageSize}/{attentionId}")
    public ResponseMessage detail(@PathVariable("page") int page , @PathVariable int pageSize ,@PathVariable long attentionId) {
        QueryResult queryResult = attentionManager.selectDetailPage(page,pageSize,attentionId);
        return ResponseMessage.success("data",queryResult);
    }
    @Auth(AuthType.ADMIN)
    @GetMapping("/deleteAttention/{attentionId}/{userId}")
    public ResponseMessage deleteAttention(@PathVariable long attentionId,@PathVariable long userId) {
        return ResponseMessage.success("data",attentionManager.delete(attentionId,userId));
    }
}
