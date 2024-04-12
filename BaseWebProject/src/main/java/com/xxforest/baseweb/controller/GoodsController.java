package com.xxforest.baseweb.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.xxforest.baseweb.core.IdTool;
import com.xxforest.baseweb.core.RedisUtil;
import com.xxforest.baseweb.core.ResponseMessage;
import com.xxforest.baseweb.core.anno.Auth;
import com.xxforest.baseweb.core.anno.AuthType;
import com.xxforest.baseweb.core.enums.GoodsStatus;
import com.xxforest.baseweb.domain.Admin;
import com.xxforest.baseweb.domain.Goods;
import com.xxforest.baseweb.domain.GoodsCategory;
import com.xxforest.baseweb.domain.User;
import com.xxforest.baseweb.manager.AdminManager;
import com.xxforest.baseweb.manager.GoodsCategoryManager;
import com.xxforest.baseweb.manager.GoodsManager;
import com.xxforest.baseweb.manager.UploadFileManager;
import com.xxforest.baseweb.vo.GoodsVo;
import com.xxforest.baseweb.vo.GoodsVo2;
import com.xxforest.baseweb.vo.main;
import org.nutz.dao.Dao;
import org.nutz.dao.QueryResult;
import org.nutz.dao.entity.annotation.Id;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 闲置商品接口
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private UploadFileManager uploadFileManager ;
    @Autowired
    private GoodsManager goodsManager ;
    @Autowired
    private GoodsCategoryManager goodsCategoryManager ;

    /**
     * 新增商品接口
     * @param
     * @return
     */
    @Auth(AuthType.USER)
    @PostMapping("/add")
    public ResponseMessage add(@RequestBody GoodsVo goodsVo,@RequestHeader(value = "token",required = false) String token){
        String str = RedisUtil.getInstance().getStr(token);
        User user = JSONUtil.toBean(str, User.class);
        if (goodsVo == null) return ResponseMessage.error("不可为空");

        if(user.getId() != goodsVo.getUserId()) return ResponseMessage.error("用户验证失败");
        Goods goods = new Goods();
        goods.setUserName(user.getUserName());
        BeanUtils.copyProperties(goodsVo,goods);
        goods.setId(IdTool.nextId());
        goods.setPublishDate(new Date());
        goods.setId(IdTool.nextId());
        goods.setUserName(user.getUserName());
        List<Long> filesIds = goodsVo.getFilesIds();
        if(filesIds==null) return ResponseMessage.error("图片异常");
        if (filesIds.size() <= 0||filesIds.size() > 6) return ResponseMessage.error("图片数量异常");
        else {
            uploadFileManager.goodsFile(filesIds,goods.getId());
        }
        goodsManager.insert(goods);
       return ResponseMessage.success("data",goods);
    }

    /**
     * 新增商品接口
     * @param
     * @return
     */
    @Auth(AuthType.USER)
    @PostMapping("/update")
    public ResponseMessage update(@RequestBody GoodsVo2 goodsVo, @RequestHeader(value = "token",required = false) String token){
        String str = RedisUtil.getInstance().getStr(token);
        User user = JSONUtil.toBean(str, User.class);
        if (goodsVo == null) return ResponseMessage.error("不可为空");
        if(user.getId() != goodsVo.getUserId()) return ResponseMessage.error("用户验证失败");
        Goods goods = new Goods();
        BeanUtils.copyProperties(goodsVo,goods);
        List<Long> newFilesIds = goodsVo.getFilesIds();
        if(newFilesIds==null) return ResponseMessage.error("图片异常");
        if (newFilesIds.size() <= 0||newFilesIds.size() > 6) return ResponseMessage.error("图片数量异常");
        List<Long> oldFilesId = uploadFileManager.getOldFilesId(goods.getId());
        List<Long> addFilesIds = new ArrayList<>();
        List<Long> delFilesIds = new ArrayList<>();

        for (Long newFilesId : newFilesIds) {
            if (oldFilesId.contains(newFilesId)) continue;
            addFilesIds.add(newFilesId);
        }

        for (Long aLong : oldFilesId) {
            if (newFilesIds.contains(aLong)) continue;
            delFilesIds.add(aLong);
        }


        uploadFileManager.goodsFile(addFilesIds,goods.getId());
        uploadFileManager.deleteFiles(delFilesIds);
        goodsManager.update(goods);
        return ResponseMessage.success("data",goods);
    }


    /**
     * 新增商品接口
     * @param
     * @return
     */
    @Auth(AuthType.USER)
    @GetMapping("/listGoodsByUser")
    public ResponseMessage listGoodsByUser(@RequestHeader(value = "token",required = false) String token){
        String str = RedisUtil.getInstance().getStr(token);
        User user = JSONUtil.toBean(str, User.class);
        return ResponseMessage.success("data",goodsManager.listGoodsByUser(user.getId()));
    }

    /**
     * 新增商品接口
     * @param
     * @return
     */
    @Auth(AuthType.USER)
    @GetMapping("/listGoods")
    public ResponseMessage listGoods(){
        return ResponseMessage.success("data",goodsManager.listGoods());
    }

    /**
     * 获取文章列表
     * @param page 页码:1
     * @param pageSize 分页大小:20
     * @param keyword 关键字
     * @param startTime 开始时间:2021-01-01 10:01:01
     * @param endTime 结束时间:2022-01-01 10:01:01
     * @return
     */
    @Auth(AuthType.USER)
    @GetMapping("/list/{page}/{pageSize}")
    public ResponseMessage loadGoodsList(@PathVariable("page") int page , @PathVariable int pageSize ,
                                           String keyword ,String categoryId ,String startTime , String endTime) {
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

        QueryResult queryResult = goodsManager.selectPage(page,pageSize,keyword,categoryId,startTimeDate,endTimeDate);
        return ResponseMessage.success("data",queryResult);
    }

    @Auth(AuthType.USER)
    @GetMapping("/queryGoodsById/{id}")
    public ResponseMessage queryGoodsById(@PathVariable long id){
        return ResponseMessage.success("data",goodsManager.queryGoodsById(id));
    }




    @Auth(AuthType.USER)
    @GetMapping("/delete/{id}")
    public ResponseMessage delete(@PathVariable long id,@RequestHeader(value = "token",required = false) String token){
        String str = RedisUtil.getInstance().getStr(token);
        User user = JSONUtil.toBean(str, User.class);
        Goods goods = goodsManager.queryGoodsById(id);
        if (goods == null) return ResponseMessage.error("删除失败");
        if(user.getId() != goods.getUserId()) return ResponseMessage.error("用户验证失败");


        goodsManager.delete(goods);
        return ResponseMessage.success("data","success");
    }



    @Auth(AuthType.USER)
    @GetMapping("/listGoodsCategory")
    public ResponseMessage listGoodsCategory(){
        return ResponseMessage.success("data",goodsCategoryManager.listGoodsCategory());
    }

    /**
     * 新增商品接口
     * @param
     * @return
     */
    @Auth(AuthType.USER)
    @GetMapping("/listGoodsStatus")
    public ResponseMessage listGoodsStatus(){
        return ResponseMessage.success("data", Arrays.stream(GoodsStatus.values()).filter(item->item.getValue()!="待确认"
                &&item.getValue()!="售卖").toArray());
    }

    @Auth(AuthType.ADMIN)
    @GetMapping("/listGoodsCategory/{pageNum}/{pageSize}")
    public ResponseMessage listGoodsCategory(@PathVariable int pageNum,@PathVariable int pageSize,String keyword){
        QueryResult queryResult = goodsCategoryManager.selectPage(pageNum,pageSize,keyword);
        return ResponseMessage.success("data",queryResult);
    }

    @Auth(AuthType.ADMIN)
    @PostMapping("/addGoodsCategory")
    public ResponseMessage listGoodsCategory(@RequestBody GoodsCategory goodsCategory){
        return ResponseMessage.success("data",goodsCategoryManager.addGoodsCategory(goodsCategory));
    }

    @Auth(AuthType.ADMIN)
    @PostMapping("/updateGoodsCategory")
    public ResponseMessage updateGoodsCategory(@RequestBody GoodsCategory goodsCategory){
        return ResponseMessage.success("data",goodsCategoryManager.updateGoodsCategory(goodsCategory));
    }

    @Auth(AuthType.ADMIN)
    @GetMapping("/deleteGoodsCategory/{id}")
    public ResponseMessage deleteGoodsCategory(@PathVariable long id){
        return ResponseMessage.success("data",goodsCategoryManager.deleteGoodsCategory(id));
    }

    @Auth(AuthType.ADMIN)
    @GetMapping("/adminDelete/{id}")
    public ResponseMessage adminDelete(@PathVariable long id){
        Goods goods = goodsManager.queryGoodsById(id);
        if (goods == null) return ResponseMessage.error("删除失败");
        goodsManager.delete(goods);
        return ResponseMessage.success("data",1);
    }

    @Auth(AuthType.ADMIN)
    @GetMapping("/adminList/{page}/{pageSize}")
    public ResponseMessage adminList(@PathVariable("page") int page , @PathVariable int pageSize ,
                                         String keyword ,String categoryId ,String startTime , String endTime) {
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

        QueryResult queryResult = goodsManager.selectPage2(page,pageSize,keyword,categoryId,startTimeDate,endTimeDate);
        return ResponseMessage.success("data",queryResult);
    }

}
