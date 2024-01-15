package com.xxforest.baseweb.core;

import cn.hutool.db.nosql.redis.RedisDS;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

public class RedisUtil {
    private RedisUtil(){};
    private volatile static RedisDS redisDS;

    public static RedisDS getInstance(){
        if (redisDS==null){
            synchronized (RedisUtil.class){
                if (redisDS == null){
                    redisDS  = HutoolRedisUtil.getRedisDS("custom");
                }
            }
        }
        return redisDS;
    }

    public static void setToken(String token,String value){
        if (redisDS == null) RedisUtil.getInstance();
        Jedis jedis = redisDS.getJedis();
        SetParams params = new  SetParams();
        params.ex(36000L);//设置token过期时间
        jedis.set(token,value,params);
        jedis.close();
    }

    public static String getUserValue(String token){
        if (redisDS == null) RedisUtil.getInstance();
        return redisDS.getStr(token);
    }

    public static void del(String s) {
        redisDS.del(s);
    }
}
