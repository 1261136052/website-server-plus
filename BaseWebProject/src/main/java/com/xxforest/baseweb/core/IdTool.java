package com.xxforest.baseweb.core;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;

public class IdTool {

    private static Snowflake snowflake = IdUtil.getSnowflake(1, 1);

    public static long nextId(){
        return snowflake.nextId();
    }
}
