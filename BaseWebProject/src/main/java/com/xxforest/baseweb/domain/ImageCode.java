package com.xxforest.baseweb.domain;

import lombok.Data;

@Data
public class ImageCode {

    private String code;
    private long createTime;

    // 构造方法私有化，通过Builder模式构建对象
    private ImageCode(String code, long createTime) {
        this.code = code;
        this.createTime = createTime;
    }

    // 提供公共方法获取验证码
    public String getCode() {
        return code;
    }

    // 提供公共方法获取创建时间
    public long getCreateTime() {
        return createTime;
    }

    // 嵌套的Builder类用于构建ImageCode对象
    public static class Builder {
        private String code;
        private long createTime;

        // 设置验证码
        public Builder code(String code) {
            this.code = code;
            return this;
        }

        // 设置创建时间
        public Builder createTime(long createTime) {
            this.createTime = createTime;
            return this;
        }

        // 构建ImageCode对象
        public ImageCode build() {
            return new ImageCode(code, createTime);
        }
    }

    // 静态方法获取Builder实例
    public static Builder builder() {
        return new Builder();
    }
}