package com.xxforest.baseweb.core.enums;

public enum GoodsStatus {
    LAUNCH("上架"),
    DOWN("下架"),
    SELL("售卖");

    private String value;

    private GoodsStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
