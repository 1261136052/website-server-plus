package com.xxforest.baseweb.controller;

import cn.hutool.db.nosql.redis.RedisDS;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.xxforest.baseweb.core.IdTool;
import com.xxforest.baseweb.core.RedisUtil;
import com.xxforest.baseweb.core.ResponseMessage;
import com.xxforest.baseweb.core.ServerDao;
import com.xxforest.baseweb.core.anno.Auth;
import com.xxforest.baseweb.core.anno.AuthType;
import com.xxforest.baseweb.core.webSocket.WebSocketService;
import com.xxforest.baseweb.domain.Goods;
import com.xxforest.baseweb.domain.GoodsChat;
import com.xxforest.baseweb.domain.User;
import com.xxforest.baseweb.manager.GoodsChatManager;
import com.xxforest.baseweb.manager.GoodsManager;
import com.xxforest.baseweb.manager.UserManager;
import com.xxforest.baseweb.vo.ChatVo;
import org.nutz.dao.Cnd;
import org.nutz.dao.QueryResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private GoodsChatManager goodsChatManager;
    @Autowired
    private WebSocketService webSocketService;
    @Auth(AuthType.USER)
    @PostMapping("/sendMessage")
    public ResponseMessage sendMessage(@RequestBody ChatVo chatVo,
                                       @RequestHeader(value = "token",required = false) String token){
        String str = RedisUtil.getUserValue(token);
        User user = JSONUtil.toBean(str, User.class);
        if (user==null)return ResponseMessage.error("请登录");
        if(user.getId() != chatVo.getReaderId()) return ResponseMessage.error("用户验证失败");
        GoodsChat goodsChat = new GoodsChat();
        BeanUtils.copyProperties(chatVo,goodsChat);
        goodsChat.setId(IdTool.nextId());
        goodsChat.setPublishDate(new Date());
        goodsChatManager.insert(goodsChat);
        webSocketService.sendMessage2(goodsChat);
        return ResponseMessage.success("data",goodsChat);
    }

    @Auth(AuthType.USER)
    @GetMapping("/getLinks")
    public ResponseMessage getLinks(@RequestHeader(value = "token",required = false) String token){
        String str = RedisUtil.getUserValue(token);
        User user = JSONUtil.toBean(str, User.class);
        if (user==null)return ResponseMessage.error("请登录");
        return ResponseMessage.success("data",goodsChatManager.getLinks(user.getId()));
    }

    @Auth(AuthType.USER)
    @GetMapping("/getMessageList/{userId}")
    public ResponseMessage getMessageList(@PathVariable String userId,@RequestHeader(value = "token",required = false) String token){
        String str = RedisUtil.getUserValue(token);
        User user = JSONUtil.toBean(str, User.class);
        return ResponseMessage.success("data",goodsChatManager.getMessages(user.getId(),userId));
    }

    @Auth(AuthType.USER)
    @GetMapping("/getMessageList2/{userId}/{time}")
    public ResponseMessage getMessageList2(@PathVariable String userId,@PathVariable String time,@RequestHeader(value = "token",required = false) String token){
        String str = RedisUtil.getUserValue(token);
        User user = JSONUtil.toBean(str, User.class);
        Date date = new Date(Long.parseLong(time)*1000L+1000);
        return ResponseMessage.success("data",goodsChatManager.getMessages2(user.getId(),userId,date));
    }
    @Auth(AuthType.ADMIN)
    @GetMapping("/list/{pageNum}/{pageSize}")
    public ResponseMessage adminList(@PathVariable int pageNum,@PathVariable int pageSize,String keyword){
        QueryResult queryResult = goodsChatManager.selectPage(pageNum,pageSize,keyword);
        return ResponseMessage.success("data",queryResult);
    }
    @Auth(AuthType.ADMIN)
    @GetMapping("/update/{chatId}")
    public ResponseMessage update(@PathVariable long chatId,String content){
        return ResponseMessage.success("data", goodsChatManager.update(chatId,content));
    }
    @Auth(AuthType.ADMIN)
    @GetMapping("/delete/{chatId}")
    public ResponseMessage delete(@PathVariable long chatId){
        return ResponseMessage.success("data", goodsChatManager.delete(chatId));
    }
}
