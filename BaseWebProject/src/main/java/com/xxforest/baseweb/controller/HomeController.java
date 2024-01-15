package com.xxforest.baseweb.controller;

import com.xxforest.baseweb.core.ResponseMessage;
import com.xxforest.baseweb.dto.*;
import com.xxforest.baseweb.vo.CounselVo;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

/**
 * 首页接口
 */
@RestController
@RequestMapping("/home")
public class HomeController {

    /**
     * 获取资源类型
     * @return
     */
    @GetMapping("/loadResourcesType")
    public ResponseMessage loadResourcesType(){
        return ResponseMessage.success("data",new ArrayList<CommonTypeDto>());
    }

    /**
     * 获取资源列表
     * @param page  页数:1
     * @param pageSize 分页大小:20
     * @param type  类型:教学资源
     * @return
     */
    @GetMapping("/listResources/{page}/{pageSize}")
    public ResponseMessage loadResourcesList(@PathVariable("page") int page , @PathVariable int pageSize , String type){
        return ResponseMessage.success("data",new ArrayList<NewsDto>())
                .putData("page",page).putData("pageSize",pageSize)
                .putData("type",type).putData("newType",new ArrayList<ResourcesDto>());
    }


    /**
     * 提交资讯内容
     * @param counselVo
     * @return
     */
    @PostMapping("/submitCounsel")
    public ResponseMessage submitCounsel(@RequestBody CounselVo counselVo){
        return ResponseMessage.success("data","ok");
    }

    /**
     * 获取新闻类型
     * @return
     */
    @GetMapping("/loadNewsType")
    public ResponseMessage loadNewsType(){
        return ResponseMessage.success("data",new ArrayList<CommonTypeDto>());
    }

    /**
     * 获取新闻列表
     * @param page  页数:1
     * @param pageSize 分页大小:20
     * @param type  类型:行业资讯
     * @return
     */
    @GetMapping("/listNews/{page}/{pageSize}")
    public ResponseMessage loadNewsList(@PathVariable("page") int page , @PathVariable int pageSize , String type){
        return ResponseMessage.success("data",new ArrayList<NewsDto>())
                .putData("page",page).putData("pageSize",pageSize)
                .putData("type",type).putData("newType",new ArrayList<CommonTypeDto>());
    }

    /**
     * C1 首页 新闻咨询/活动通知
     * @return
     */
    @GetMapping("/loadNews")
    private ResponseMessage loadNews(){
        return ResponseMessage.success("news",new ArrayList<NewsDto>()).putData("notice",new ArrayList<NewsDto>());
    }

    /**
     * 根据编号获取新闻内容
     * @param id
     * @return
     */
    @GetMapping("/loadNewDetail/{id}")
    public ResponseMessage loadNewDetail(long id){
        return ResponseMessage.success("data",new NewsDto());
    }

    /**
     * 获取友情链接
     * @return
     */
    @GetMapping("/loadFriendLinks")
    public ResponseMessage loadFriendLinks(){
        return ResponseMessage.success("data",new ArrayList<FriendsDto>());
    }



}
