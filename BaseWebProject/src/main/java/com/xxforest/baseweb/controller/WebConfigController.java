package com.xxforest.baseweb.controller;

import com.xxforest.baseweb.core.ResponseMessage;
import com.xxforest.baseweb.core.ServerDao;
import com.xxforest.baseweb.core.anno.Auth;
import com.xxforest.baseweb.core.anno.AuthType;
import com.xxforest.baseweb.domain.WebConfig;
import com.xxforest.baseweb.manager.WebConfigManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 配置信息接口
 */
@RestController
@RequestMapping("/config")
public class WebConfigController {

    @Autowired
    private ServerDao dao ;
    @Autowired
    private WebConfigManager webConfigManager;

    /**
     * 获取所有配置
     * @return
     */
    @GetMapping("/load")
    public ResponseMessage loadConfigs(){
        List<WebConfig> webConfigs = webConfigManager.loadConfigs();
        return ResponseMessage.success("data",webConfigs) ;
    }

    /**
     * 修改配置信息
     * @param webConfig
     * @return
     */
    @Auth(AuthType.ADMIN)
    @PostMapping("/update")
    public ResponseMessage updateConfig(@RequestBody WebConfig webConfig){
        webConfigManager.update(webConfig);
        return ResponseMessage.success("data",webConfig);
    }

    /**
     * 根据名称获取配置信息
     * @param name
     * @return
     */
    @GetMapping("/load_by_name/{name}")
    public ResponseMessage loadConfigByName(@PathVariable String name){
        WebConfig webConfig = webConfigManager.loadByName(name);
        return ResponseMessage.success("data",webConfig) ;
    }


}
