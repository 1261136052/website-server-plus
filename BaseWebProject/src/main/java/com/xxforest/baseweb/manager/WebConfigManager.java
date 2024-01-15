package com.xxforest.baseweb.manager;

import com.xxforest.baseweb.core.ServerDao;
import com.xxforest.baseweb.domain.WebConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WebConfigManager {

    @Autowired
    private ServerDao dao ;

    private List<WebConfig> cacheConfigs ;

    public List<WebConfig> loadConfigs(){
        if(cacheConfigs == null){
            cacheConfigs = dao.query(WebConfig.class,null);
        }
        return cacheConfigs;
    }


    public void update(WebConfig webConfig) {
        dao.update(webConfig);
        //更新缓存
        cacheConfigs = dao.query(WebConfig.class,null);
    }

    public WebConfig loadByName(String name) {
        if(cacheConfigs == null){
            this.loadConfigs();
        }
        for (WebConfig cacheConfig : cacheConfigs) {
            if(cacheConfig.getConfigName().equals(name)){
                return cacheConfig;
            }
        }
        return null;
    }
}
