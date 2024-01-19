package com.xxforest.baseweb.core;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.db.nosql.redis.RedisDS;
import cn.hutool.json.JSONUtil;
import com.xxforest.baseweb.core.inter.VerificationImageCodeService;
import com.xxforest.baseweb.domain.ImageCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;

@Slf4j
@Service
public class VerificationImageCodeServiceImp implements VerificationImageCodeService {

    /**
     * 验证码失效时间
     */
    @Value("${constants.expire.second: 600}")
    private Long expireSecond;

    @Override
    public  String generateImageCode(String uuid) {
        String key = uuid;
        RedisDS jedis = RedisUtil.getInstance();

        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(100, 50);
        log.info("image code is {}", lineCaptcha.getCode());

        jedis.del(key);

        // 存入缓存
        ImageCode imageCode = ImageCode.builder()
                .code(lineCaptcha.getCode())
                .createTime(System.currentTimeMillis())
                .build();
//        jedis.setex(key, expireSecond, JSONUtil.toJsonStr(imageCode));
        RedisUtil.setToken(key,expireSecond, JSONUtil.toJsonStr(imageCode));
        // 浏览器中输入 data:image/png;base64, + base64， 即可显示图片
        return lineCaptcha.getImageBase64();
    }

    @Override
    public int checkImageCode(String uuid, String code)  {

        RedisDS jedis = RedisUtil.getInstance();

        // 获取缓存中数据
        String cache = jedis.getStr(uuid);
        ImageCode imageCode = JSONUtil.toBean(cache, ImageCode.class);

        if (StringUtils.isEmpty(cache)){
            return -1;
        }

        // 验证码不匹配
        if(!code.equals(imageCode.getCode())){
            return 1;
        }
        jedis.del(uuid);
        return 0;
    }
}