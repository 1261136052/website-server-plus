package com.xxforest.baseweb.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.xxforest.baseweb.core.*;
import com.xxforest.baseweb.core.anno.Auth;
import com.xxforest.baseweb.core.anno.AuthType;
import com.xxforest.baseweb.domain.Article;
import com.xxforest.baseweb.domain.ArticleCategory;
import com.xxforest.baseweb.domain.User;
import com.xxforest.baseweb.manager.ArticleCategoryManager;
import com.xxforest.baseweb.manager.ArticleManager;
import com.xxforest.baseweb.manager.UploadFileManager;
import org.nutz.dao.Cnd;
import org.nutz.dao.QueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 文章接口
 */
@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ServerDao serverDao;
    @Autowired
    private ArticleManager articleManager;
    @Autowired
    private UploadFileManager uploadFileManager;
    @Autowired
    private ArticleCategoryManager articleCategoryManager;

    /**
     * 获取文章列表
     * @param page 页码:1
     * @param pageSize 分页大小:20
     * @param keyword 关键字
     * @param startTime 开始时间:2021-01-01 10:01:01
     * @param endTime 结束时间:2022-01-01 10:01:01
     * @return
     */
    @GetMapping("/list/{page}/{pageSize}")
    public ResponseMessage loadArticleList(@PathVariable("page") int page , @PathVariable int pageSize ,
                                           String keyword , String startTime , String endTime,Integer categoryId ) {

        //转换时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startTimeDate = null;
        Date endTimeDate = null;
        try{
            if(StrUtil.isNotBlank(startTime)){
                startTimeDate = simpleDateFormat.parse(startTime);
            }
            if(StrUtil.isNotBlank(endTime)){
                endTimeDate = simpleDateFormat.parse(endTime);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        QueryResult queryResult = articleManager.selectPage(page,pageSize,keyword,startTimeDate,endTimeDate,categoryId);
        return ResponseMessage.success("data",queryResult);
    }


    /**
     * 根据编号删除文章
     * @param id 文章编号
     * @return
     */
    @Auth(AuthType.USER)
    @GetMapping("/delUserArticle/{id}")
    public ResponseMessage delUserArticle(@PathVariable long id,@RequestHeader(value = "token",required = false) String token){
        String str = RedisUtil.getUserValue(token);
        User user = JSONUtil.toBean(str, User.class);
        if (user==null)return ResponseMessage.error("请登录");
        Article article = serverDao.fetch(Article.class, Cnd.where("id", "=", id).and("author_id", "=", user.getId()));
        if (article==null){
            return ResponseMessage.error("删除失败");
        }
        serverDao.delete(article);
        return ResponseMessage.success("data","删除成功");
    }


    @GetMapping("/hot")
    public ResponseMessage loadArticleList() {
        QueryResult queryResult = articleManager.selectHot();
        return ResponseMessage.success("data",queryResult);
    }


    /**
     * 根据id获取文章
     * @param id 文章编号
     * @return
     */
    @GetMapping("/load/{id}")
    public ResponseMessage loadArticleById(@PathVariable long id){
        Article article = serverDao.fetch(Article.class, id);
        return ResponseMessage.success("data",article);
    }

    /**
     * 发布文章
     * @param article 文章数据
     * @return
     */
//    @Auth(AuthType.ADMIN)
    @PostMapping("/add")
    public ResponseMessage addArticle(@RequestBody Article article){
        article.setId(IdTool.nextId());
        article.setPublishDate(new Date());
        boolean contentValid = Filter.isContentValid(article.getContent());
        if (!contentValid){
            return ResponseMessage.error("敏感词不可发布");
        }
        //保留图片
        uploadFileManager.retentionFile(article.getCover());
        serverDao.insert(article);
        return ResponseMessage.success("data",article);
    }

    /**
     * 修改文章
     * @param article 文章数据
     * @return
     */
    @Auth(AuthType.ADMIN)
    @PostMapping("/update")
    public ResponseMessage updateArticle(@RequestBody Article article){
        article.setPublishDate(new Date());
        //保留图片
        uploadFileManager.retentionFile(article.getCover());
        serverDao.update(article);
        return ResponseMessage.success("data",article);
    }

    /**
     * 根据编号删除文章
     * @param id 文章编号
     * @return
     */
    @Auth(AuthType.ADMIN)
    @GetMapping("/del/{id}")
    public ResponseMessage delArticle(@PathVariable long id){
        serverDao.delete(Article.class,id);
        return ResponseMessage.success("data","删除成功");
    }

    /**
     * 获取文章分类
     * @return
     */
    @GetMapping("/categorys")
    private ResponseMessage loadArticleCategorys(){
        List<ArticleCategory> articleCategories = serverDao.query(ArticleCategory.class, null);
        return ResponseMessage.success("data",articleCategories);
    }


    /**
     * 获取文章分类
     * @return
     */
    @Auth(AuthType.USER)
    @GetMapping("/Temple")
    private ResponseMessage Temple(@RequestHeader(value = "token",required = false) String token){
        String str = RedisUtil.getUserValue(token);
        User user = JSONUtil.toBean(str, User.class);
        if (user == null) ResponseMessage.error("请登录");
        QueryResult queryResult = articleManager.TempleQuery(user.getId());
        return ResponseMessage.success("data",queryResult);
    }


    @Auth(AuthType.ADMIN)
    @GetMapping("/listArticleCategory/{pageNum}/{pageSize}")
    public ResponseMessage listArticleCategory(@PathVariable int pageNum,@PathVariable int pageSize,String keyword){
        QueryResult queryResult = articleCategoryManager.selectPage(pageNum,pageSize,keyword);
        return ResponseMessage.success("data",queryResult);
    }

    @Auth(AuthType.ADMIN)
    @PostMapping("/addArticleCategory")
    public ResponseMessage listArticleCategory(@RequestBody ArticleCategory articleCategory){
        return ResponseMessage.success("data",articleCategoryManager.addArticleCategory(articleCategory));
    }

    @Auth(AuthType.ADMIN)
    @PostMapping("/updateArticleCategory")
    public ResponseMessage updateArticleCategory(@RequestBody ArticleCategory articleCategory){
        return ResponseMessage.success("data",articleCategoryManager.updateArticleCategory(articleCategory));
    }

    @Auth(AuthType.ADMIN)
    @GetMapping("/deleteArticleCategory/{id}")
    public ResponseMessage deleteArticleCategory(@PathVariable long id){
        return ResponseMessage.success("data",articleCategoryManager.deleteArticleCategory(id));
    }

}
