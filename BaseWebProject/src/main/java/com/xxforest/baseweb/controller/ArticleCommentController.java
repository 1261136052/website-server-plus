package com.xxforest.baseweb.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.db.nosql.redis.RedisDS;
import cn.hutool.json.JSONUtil;
import com.xxforest.baseweb.core.IdTool;
import com.xxforest.baseweb.core.RedisUtil;
import com.xxforest.baseweb.core.ResponseMessage;
import com.xxforest.baseweb.core.ServerDao;
import com.xxforest.baseweb.core.anno.Auth;
import com.xxforest.baseweb.core.anno.AuthType;
import com.xxforest.baseweb.domain.Article;
import com.xxforest.baseweb.domain.ArticleComment;
import com.xxforest.baseweb.domain.GoodsChat;
import com.xxforest.baseweb.domain.User;
import com.xxforest.baseweb.manager.ArticleCommentManager;
import com.xxforest.baseweb.manager.ArticleManager;
import com.xxforest.baseweb.vo.ChatVo;
import org.nutz.dao.Cnd;
import org.nutz.dao.QueryResult;
import org.nutz.dao.entity.annotation.Id;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/comment")
public class ArticleCommentController {

    @Autowired
    private ArticleCommentManager articleCommentManager ;

    @Autowired
    private ArticleManager articleManager ;

    @Auth(AuthType.USER)
    @GetMapping("/comment/{articleId}")
    public ResponseMessage test(@RequestHeader(value = "token",required = false) String token,
            @PathVariable long articleId,String content){
        String str = RedisUtil.getUserValue(token);
        User user = JSONUtil.toBean(str, User.class);
        if (user==null)return ResponseMessage.error("请登录");
        if ("".equals(content)||content==null) return ResponseMessage.error("内容不可为空");
        ArticleComment comment = new ArticleComment();
        comment.setId(IdTool.nextId());
        comment.setSendDate(new Date());
        comment.setArticleId(articleId);
        comment.setContent(content);
        comment.setUserId(user.getId());
        comment.setUserName(user.getUserName());
        articleCommentManager.addComment(comment);
        articleManager.addCommentCount(articleId,1);
        return ResponseMessage.success("data","success");
    }


    @GetMapping("/query/{articleId}/{page}/{pageSize}")
    public ResponseMessage query(@PathVariable long articleId,
                                 @PathVariable("page") int page , @PathVariable int pageSize){
        QueryResult queryResult = articleCommentManager.selectPage(page,pageSize,articleId);
        return ResponseMessage.success("data",queryResult);
    }



    @Auth(AuthType.ADMIN)
    @GetMapping("/delete/{id}")
    public ResponseMessage deleteAttention(@PathVariable long id) {
        articleManager.addCommentCount(id,-1);
        return ResponseMessage.success("data",articleCommentManager.delete(id));
    }
}
