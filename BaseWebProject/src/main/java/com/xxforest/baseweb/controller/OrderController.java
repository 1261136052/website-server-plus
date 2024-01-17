package com.xxforest.baseweb.controller;

import cn.hutool.json.JSONUtil;
import com.xxforest.baseweb.core.IdTool;
import com.xxforest.baseweb.core.RedisUtil;
import com.xxforest.baseweb.core.ResponseMessage;
import com.xxforest.baseweb.core.ServerDao;
import com.xxforest.baseweb.core.anno.Auth;
import com.xxforest.baseweb.core.anno.AuthType;
import com.xxforest.baseweb.core.enums.GoodsStatus;
import com.xxforest.baseweb.domain.*;
import com.xxforest.baseweb.manager.GoodsChatManager;
import com.xxforest.baseweb.manager.GoodsManager;
import com.xxforest.baseweb.manager.OrderManager;
import com.xxforest.baseweb.vo.BuyerVo;
import com.xxforest.baseweb.vo.ChatVo;
import org.nutz.dao.QueryResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.beans.Beans;
import java.util.Date;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private GoodsManager goodsManager;

    @Autowired
    private OrderManager orderManager;

    @Auth(AuthType.USER)
    @PostMapping("/buyGoods")
    public ResponseMessage buyGoods(@RequestBody BuyerVo buyerVo,
                                    @RequestHeader(value = "token",required = false) String token){
        String str = RedisUtil.getUserValue(token);
        User user = JSONUtil.toBean(str, User.class);
        if (user.getId() != buyerVo.getBuyerId()) ResponseMessage.error("用户验证失败");
        Goods goods = goodsManager.queryGoodsById(buyerVo.getGoodsId());
        if (goods==null) return ResponseMessage.error("商品不存在");

        if (!goods.getStatus().equals(GoodsStatus.LAUNCH.name())) return ResponseMessage.error("商品不存在");

        if (goods.getUserId() == buyerVo.getBuyerId()) return ResponseMessage.error("不可以购买自己的商品");
        goods.setStatus(GoodsStatus.WAIT.name());
        goodsManager.update(goods);
        GoodsOrder goodsOrder = new GoodsOrder();
        BeanUtils.copyProperties(buyerVo,goodsOrder);
        goodsOrder.setId(IdTool.nextId());
        goodsOrder.setBuyDate(new Date());
        orderManager.add(goodsOrder);
        return ResponseMessage.success("data","goodsChat");
    }



    @Auth(AuthType.USER)
    @GetMapping("/listBuyGoods")
    public ResponseMessage listBuyGoods(
                                    @RequestHeader(value = "token",required = false) String token){
        String str = RedisUtil.getUserValue(token);
        User user = JSONUtil.toBean(str, User.class);

        return ResponseMessage.success("data",orderManager.listBuyGoods(user.getId()));
    }

    @Auth(AuthType.USER)
    @GetMapping("/listWaitGoods")
    public ResponseMessage listWaitGoods(
            @RequestHeader(value = "token",required = false) String token){
        String str = RedisUtil.getUserValue(token);
        User user = JSONUtil.toBean(str, User.class);
        return ResponseMessage.success("data",orderManager.listWaitGoods(user.getId()));
    }

    @Auth(AuthType.USER)
    @GetMapping("/confirm/{orderId}/{isConfirm}")
    public ResponseMessage confirm(@PathVariable long orderId,@PathVariable int isConfirm,
                                   @RequestHeader(value = "token",required = false) String token){
        String str = RedisUtil.getUserValue(token);
        User user = JSONUtil.toBean(str, User.class);
        return ResponseMessage.success("data",orderManager.confirm(isConfirm,orderId,user.getId()));
    }



    @Auth(AuthType.USER)
    @GetMapping("/listSellGoods")
    public ResponseMessage listSellGoods(@RequestHeader(value = "token",required = false) String token){
        String str = RedisUtil.getUserValue(token);
        User user = JSONUtil.toBean(str, User.class);
        return ResponseMessage.success("data",orderManager.listSellGoods(user.getId()));
    }


    @Auth(AuthType.ADMIN)
    @GetMapping("/listOrder/{pageNum}/{pageSize}")
    public ResponseMessage listOrder(@PathVariable int pageNum,@PathVariable int pageSize,String keyword){
        QueryResult queryResult = orderManager.selectPage(pageNum,pageSize,keyword);
        return ResponseMessage.success("data",queryResult);
    }

    @Auth(AuthType.ADMIN)
    @GetMapping("/deleteOrder/{id}")
    public ResponseMessage deleteArticleCategory(@PathVariable long id){
        return ResponseMessage.success("data",orderManager.deleteOrder(id));
    }

}
