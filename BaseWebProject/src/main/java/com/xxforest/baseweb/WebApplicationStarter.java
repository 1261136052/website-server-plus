package com.xxforest.baseweb;

import com.xxforest.baseweb.core.EmailService;
import com.xxforest.baseweb.core.ServerCache;
import com.xxforest.baseweb.core.ServerDao;
import org.nutz.dao.Dao;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.annotation.PostConstruct;

@SpringBootApplication
@EnableScheduling
public class WebApplicationStarter {
    public static void main(String[] args) {
        //启动springboot
        SpringApplication.run(WebApplicationStarter.class, args);
    }

    @PostConstruct
    public void dataBaseInit(){
        ServerDao.getInstance().init();
        ServerCache.getInstance();
    }

    /**
     * 跨域过滤器
     * @return
     */
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.setMaxAge(1000*60L);
        source.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(source);
    }


    @Bean
    public ServerDao nutzServerDao(){
        return ServerDao.getInstance();
    }


}
