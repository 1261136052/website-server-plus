package com.xxforest.baseweb.core;

import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.db.nosql.redis.RedisDS;
import com.xxforest.baseweb.core.anno.Auth;
import com.xxforest.baseweb.core.anno.AuthType;
import com.xxforest.baseweb.domain.Admin;
import com.xxforest.baseweb.domain.User;
import com.xxforest.baseweb.manager.AdminManager;
import com.xxforest.baseweb.manager.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import redis.clients.jedis.Jedis;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;

@Component
public class AuthorityInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private AdminManager adminManager;
    @Autowired
    private UserManager userManager;

    public AuthorityInterceptor() {
        super();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        Auth auth = method.getAnnotation(Auth.class);
        if(auth == null){return true;}
        AuthType authType = auth.value();
        if(authType == AuthType.ADMIN){
            if(!doAdminAuth(request,response)){
                response.setStatus(403);
                response.setCharacterEncoding("utf-8");
                response.setContentType("application/json; charset=utf-8");
                response.getWriter().write(ResponseMessage.error("请先登录").setCode(403).toJson());

                return false;
            }
        }else if(authType == AuthType.USER){
            if(!doUserAuth(request,response)){
                response.setStatus(403);
                response.setCharacterEncoding("utf-8");
                response.setContentType("application/json; charset=utf-8");
                response.getWriter().write(ResponseMessage.error("请先登录").setCode(403).toJson());
                return false;
            }
        }
        return true;
    }

    private boolean doUserAuth(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader("token");
        if(StrUtil.isBlankIfStr(token)){
            return false;
        }
//        User user = userManager.selectByToken(token);
        String str = RedisUtil.getInstance().getStr(token);
        if(StrUtil.isBlank(str)){
            return false;
        }
        return true ;
    }

    private boolean doAdminAuth(HttpServletRequest request, HttpServletResponse response) {
        String adminToken = request.getHeader("token");
        if(StrUtil.isBlankIfStr(adminToken)||!adminToken.contains("ADMIN")){
            return false;
        }
//        Admin admin = adminManager.selectByToken(adminToken);
        String str = RedisUtil.getInstance().getStr(adminToken);
        if(StrUtil.isBlank(str)){
            return false;
        }
        return true ;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
    }

    @Override
    public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        super.afterConcurrentHandlingStarted(request, response, handler);
    }

}
