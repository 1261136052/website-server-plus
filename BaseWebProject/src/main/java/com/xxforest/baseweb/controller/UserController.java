package com.xxforest.baseweb.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.xxforest.baseweb.core.IdTool;
import com.xxforest.baseweb.core.RedisUtil;
import com.xxforest.baseweb.core.ResponseMessage;
import com.xxforest.baseweb.core.VerificationImageCodeServiceImp;
import com.xxforest.baseweb.core.anno.Auth;
import com.xxforest.baseweb.core.anno.AuthType;
import com.xxforest.baseweb.domain.User;
import com.xxforest.baseweb.manager.UserManager;
import com.xxforest.baseweb.vo.LoginVo;
import org.nutz.dao.Dao;
import org.nutz.dao.QueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.UUID;

/**
 * 管理员接口
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private Dao dao ;
    @Autowired
    private UserManager userManager;
    /**
     * 登录接口
     * @param loginVo
     * @return
     */
    @PostMapping("/login")
    public ResponseMessage login(@RequestBody LoginVo loginVo){
        String account = loginVo.getAccount();
        String pwd = loginVo.getPwd();
        String verifyCode = loginVo.getVerifyCode();
        if (!StrUtil.isNotBlank(verifyCode)) return ResponseMessage.error("验证码为空");
        VerificationImageCodeServiceImp verification =  new VerificationImageCodeServiceImp();

        int i = verification.checkImageCode(account, verifyCode);
        if (i==-1) return ResponseMessage.error("验证码过期");
        if (i==1) return ResponseMessage.error("验证码错误");

        User admin = userManager.selectByAccountAndPwd(account, pwd);
        if(admin != null){
            String s = UUID.randomUUID().toString();
            RedisUtil.setToken(s ,JSONUtil.toJsonStr(admin));//放入redis中
            admin.setLoginTimes(1);
            admin.setLoginDate(new Date());
//            admin.setToken(UUID.randomUUID().toString());
            admin.setToken(s);//开发测试中固定TOKEN
            dao.update(admin,"^token|loginTimes|loginDate$");
            return ResponseMessage.success("data",admin);
        }else{
            return ResponseMessage.error("不存在用户");
        }

    }

    @GetMapping("/verification/{uuid}")
    public ResponseMessage verification(@PathVariable String uuid){
       VerificationImageCodeServiceImp verification =  new VerificationImageCodeServiceImp();
        return ResponseMessage.success("data", verification.generateImageCode(uuid));
    }

    /**
     * 残缺注册接口
     * @param registerVo
     * @return
     */
    @PostMapping("/register")
    public ResponseMessage register(@RequestBody User registerVo){
        String account = registerVo.getAccount();
        String pwd = registerVo.getPwd();
        User admin = userManager.selectByAccountAndPwd(account, pwd);
        if(admin != null){
            return ResponseMessage.error("用户已存在");
        }
        registerVo.setId(IdTool.nextId());
//        admin = new User();
//        admin.setId(IdTool.nextId());
//        admin.setAccount(account);
//        admin.setPwd(pwd);
        dao.insert(registerVo);
        return ResponseMessage.success("data",admin);
    }


    /**
     * 残缺注册接口
     * @param
     * @return
     */
    @GetMapping("/down")
    public ResponseMessage down(@RequestHeader(value = "token",required = false) String token){
        RedisUtil.del(token);//开发测试中固定TOKEN
        return ResponseMessage.success("data","success");
    }

    /**
     * 残缺注册接口
     * @param
     * @return
     */
    @Auth(AuthType.USER)
    @GetMapping("/getUserName/{userId}")
    public ResponseMessage getUserName(@PathVariable long userId){
        return ResponseMessage.success("data",userManager.userName(userId));
    }

    /**
     * 残缺注册接口
     * @param
     * @return
     */
    @Auth(AuthType.USER)
    @GetMapping("/getUserData")
    public ResponseMessage getUserData(@RequestHeader(value = "token",required = false) String token){
        String value = RedisUtil.getUserValue(token);
        User user = JSONUtil.toBean(value, User.class);
        if (user == null) return ResponseMessage.error("错误");
        return ResponseMessage.success("data",user);
    }


    /**
     * 残缺注册接口
     * @param
     * @return
     */
    @Auth(AuthType.USER)
    @PostMapping("/updateUserData")
    public ResponseMessage updateUserData(
            @RequestBody User user
            ,@RequestHeader(value = "token",required = false) String token){
        String value = RedisUtil.getUserValue(token);
        User user2 = JSONUtil.toBean(value, User.class);
        if (user == null) return ResponseMessage.error("错误");
        if (user2.getId() != user.getId()) return ResponseMessage.error("错误");
        userManager.update(user);
        user =   userManager.selectById(user.getId());
        String s = JSONUtil.toJsonStr(user);
        RedisUtil.setToken(token,s);
        return ResponseMessage.success("data",user);
    }

    /**
     * 获取用户列表
     * @param page 页码:1
     * @param pageSize 分页大小:20
     * @param keyword 关键字
     * @return
     */
    @Auth(AuthType.ADMIN)
    @GetMapping("/list/{page}/{pageSize}")
    public ResponseMessage loadUserList(@PathVariable("page") int page , @PathVariable int pageSize ,
                                         String keyword ) {
        QueryResult queryResult = userManager.selectPage(page,pageSize,keyword);
        return ResponseMessage.success("data",queryResult);
    }

    /**
     * delete
     * @param
     * @return
     */
    @Auth(AuthType.ADMIN)
    @GetMapping("/delete/{userId}")
    public ResponseMessage delete(@PathVariable long userId){
        return ResponseMessage.success("data",userManager.deleteName(userId));
    }

    /**
     * update
     * @param
     * @return
     */
    @Auth(AuthType.ADMIN)
    @PostMapping("/update")
    public ResponseMessage update(@RequestBody User user){
        return ResponseMessage.success("data",userManager.adminUpdate(user));
    }
}
