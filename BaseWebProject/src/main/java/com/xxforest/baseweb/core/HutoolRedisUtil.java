package com.xxforest.baseweb.core;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.db.nosql.redis.RedisDS;
import cn.hutool.setting.Setting;
import org.springframework.stereotype.Component;

import java.io.File;

public class HutoolRedisUtil {

        private static Setting setting;
        static {
            //自定义数据库Setting，更多实用请参阅Hutool-Setting章节
            setting = new Setting(new File("BaseWebProject/src/main/resources/config/redis.setting"), CharsetUtil.CHARSET_UTF_8,false);
//            setting = new Setting(new File("/data/resources/config/redis.setting"), CharsetUtil.CHARSET_UTF_8,false);

        }
        public static RedisDS getRedisDS(String group_name) {
            return RedisDS.create(setting, group_name);
        }
}
