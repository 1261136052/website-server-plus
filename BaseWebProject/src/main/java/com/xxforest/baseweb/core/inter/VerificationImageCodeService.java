package com.xxforest.baseweb.core.inter;

public interface VerificationImageCodeService {

    /**
     * 生成图片验证码对应的编码
     * @param uuid 用户id
     * @return 图片的base64编码
     */
    String generateImageCode(String uuid);

    /**
     * 验证图片验证码内容
     * @param uuid 用户id
     * @param code 验证码内容
     * @return 验证结果
     */
   int checkImageCode(String uuid, String code) throws Exception;
}
