package com.xxforest.baseweb.controller;

import cn.hutool.db.nosql.redis.RedisDS;
import cn.hutool.json.JSONUtil;
import com.xxforest.baseweb.core.IdTool;
import com.xxforest.baseweb.core.RedisUtil;
import com.xxforest.baseweb.core.ResponseMessage;
import com.xxforest.baseweb.core.ServerDao;
import com.xxforest.baseweb.domain.Admin;
import com.xxforest.baseweb.domain.User;
import com.xxforest.baseweb.manager.AdminManager;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 管理员接口
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private Dao dao ;
    @Autowired
    private AdminManager adminManager;

    /**
     * 登录接口
     * @param loginVo
     * @return
     */
    @PostMapping("/login")
    public ResponseMessage login(@RequestBody Admin loginVo){
        String account = loginVo.getAccount();
        String pwd = loginVo.getPwd();

        Admin admin = adminManager.selectByAccountAndPwd(account, pwd);
        if(admin != null){
            RedisUtil.setToken("ADMIN_TEST_TOKEN_"+admin.getId(), JSONUtil.toJsonStr(admin));//开发测试中固定TOKEN
            admin.alterLoginTimes(1);
            admin.setLoginDate(new Date());
//            admin.setToken(UUID.randomUUID().toString());
            admin.setToken("ADMIN_TEST_TOKEN_"+admin.getId());//开发测试中固定TOKEN
            dao.update(admin,"^loginTimes|loginDate$");
            return ResponseMessage.success("data",admin);
        }else{
            return ResponseMessage.error("不存在用户");
        }

    }

    /**
     * 注册接口
     * @param registerVo
     * @return
     */
    @PostMapping("/register")
    public ResponseMessage register(@RequestBody Admin registerVo){
        String account = registerVo.getAccount();
        String pwd = registerVo.getPwd();
        Admin admin = adminManager.selectByAccountAndPwd(account, pwd);
        if(admin != null){
            return ResponseMessage.error("用户已存在");
        }
        admin = new Admin();
        admin.setId(IdTool.nextId());
        admin.setAccount(account);
        admin.setPwd(pwd);
        dao.insert(admin);
        return ResponseMessage.success("admin",admin);
    }


    @GetMapping("/statistics")
    public ResponseMessage statistics(){
        return ResponseMessage.success("data",adminManager.selectStatistics());
    }

}

//class SmallestInfiniteSet {
//    PriorityQueue<Integer> pq = new PriorityQueue<>();
//    Set<Integer> set = new HashSet<>();
//    public SmallestInfiniteSet() {
//        for (int i = 1; i <= 1000; i++) {
//            addBack(i);
//        }
//    }
//
//    public int popSmallest() {
//        if (pq.isEmpty()) return -1;
//        Integer poll = pq.poll();
//        set.remove(poll);
//        return poll;
//    }
//
//    public void addBack(int num) {
//        if (set.add(num)){
//            pq.offer(num);
//        }
//    }
//}

