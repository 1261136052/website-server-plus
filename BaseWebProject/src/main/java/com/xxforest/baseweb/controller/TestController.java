package com.xxforest.baseweb.controller;

import cn.hutool.cache.impl.LRUCache;
import cn.hutool.db.nosql.redis.RedisDS;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.xxforest.baseweb.core.*;
import com.xxforest.baseweb.core.anno.Auth;
import com.xxforest.baseweb.core.anno.AuthType;
import com.xxforest.baseweb.domain.User;
import lombok.Data;
import org.nutz.dao.Cnd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import javax.mail.MessagingException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private ServerDao dao;

//    @GetMapping("/test")
//    public ResponseMessage test(Map<String, Object> args) {
//        String result1 = HttpUtil.get("http://weather.uwyo.edu/cgi-bin/bufrraob_json.py");
//        result1 = result1.replace("=", ":");
//        InsertionSortInLinkedList.MetaData metaData = JSONUtil.toBean(result1, InsertionSortInLinkedList.MetaData.class);
//        return ResponseMessage.success("a", metaData);
//    }

    @Auth(AuthType.ADMIN)
    @GetMapping("/test2")
    public ResponseMessage test() {
        RedisDS redisDS = RedisUtil.getInstance();
        redisDS.setStr("key", "value");
        String key = redisDS.getStr("key");

        Jedis jedis = redisDS.getJedis();
        SetParams params = new SetParams();
        params.ex(120L);
        jedis.set("key2", "value", params);
        return ResponseMessage.success();
    }


    @GetMapping("/receive-emails")
    public ResponseMessage receiveEmails() {
//        try {
//            emailService.receive("Inbox");
//            return ResponseMessage.success("Emails received successfully.","");
//        } catch (MessagingException | IOException e) {
//            e.printStackTrace();
//            return ResponseMessage.error(HttpStatus.INTERNAL_SERVER_ERROR+"Failed to receive emails.");
//        }
        return null;
    }


    @GetMapping("/test3")
    public ResponseMessage test3() {
        return ResponseMessage.success();
    }

    //    private LRUCache cache = new LRUCache(5);
//    @PostMapping(value = "/alarm", produces = "application/json; charset=utf-8")
    public Mono<Object> alarm(@RequestParam String data) {
        data += "/n";
        String finalData = data;
        return Mono.create(s -> {

            try {

//                Logger.getLogger(this.getClass().getSimpleName()).info("alarm aes:  " + finalData + "\n");
                //这里存入了缓存，实际开发可替换成自己的代码，实现具体的业务逻辑。不建议在此执行耗时较长的任务，可能会引起推送超时，导致事件重发。

                // 获取当前日期
                LocalDate currentDate = LocalDate.now();
                String fileName = currentDate.toString() + ".txt";

                // 指定目标目录
                String targetDirectory = "E:\\test2\\website-server\\BaseWebProject\\src\\main\\resources\\mi";

                try {
                    // 创建目标目录
                    Files.createDirectories(Paths.get(targetDirectory));

                    // 创建文件并写入内容
                    Path filePath = Paths.get(targetDirectory, fileName);
                    FileWriter fileWriter = new FileWriter(filePath.toString(), true); // 打开文件的追加模式
                    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

                    bufferedWriter.write(finalData);
                    bufferedWriter.newLine();
                    bufferedWriter.close();

                    System.out.println("内容已成功写入文件：" + filePath.toString());
                } catch (IOException e) {
                    System.out.println("写入文件时发生错误：" + e.getMessage());
                }

            } catch (Exception e) {

                e.printStackTrace();

            }

            s.success(ObjectResult.SUCCESS);

        }).subscribeOn(Schedulers.elastic());

    }


//    private LRUCache cache = new LRUCache(5);

    @PostMapping(value = "/alarm2", produces = "application/json; charset=utf-8")
    public Mono<Object> alarm2(@RequestParam String data) {
        data += "/n";
        String finalData = data;
        return Mono.create(s -> {

            try {

//                Logger.getLogger(this.getClass().getSimpleName()).info("alarm aes:  " + finalData + "\n");
                //这里存入了缓存，实际开发可替换成自己的代码，实现具体的业务逻辑。不建议在此执行耗时较长的任务，可能会引起推送超时，导致事件重发。
                // 获取当前日期
                LocalDate currentDate = LocalDate.now();
                String fileName = currentDate.toString() + ".txt";

                // 指定目标目录
                String targetDirectory = "E:\\test2\\website-server\\BaseWebProject\\src\\main\\resources\\ton";

                try {
                    // 创建目标目录
                    Files.createDirectories(Paths.get(targetDirectory));

                    // 创建文件并写入内容
                    Path filePath = Paths.get(targetDirectory, fileName);
                    FileWriter fileWriter = new FileWriter(filePath.toString(), true); // 打开文件的追加模式
                    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

                    bufferedWriter.write(finalData);
                    bufferedWriter.newLine();
                    bufferedWriter.close();

                    System.out.println("内容已成功写入文件：" + filePath.toString());
                } catch (IOException e) {
                    System.out.println("写入文件时发生错误：" + e.getMessage());
                }

            } catch (Exception e) {

                e.printStackTrace();

            }

            s.success(ObjectResult.SUCCESS);

        }).subscribeOn(Schedulers.elastic());

    }


//    @PostMapping(value = "/alarm3", produces = "application/json; charset=utf-8")
    public Mono<Object> alarm3(@RequestParam String data) {
        data += "/n";
        String finalData = data;
        return Mono.create(s -> {

            try {

                Logger.getLogger(this.getClass().getSimpleName()).info("alarm aes:  " + finalData + "\n");
                //这里存入了缓存，实际开发可替换成自己的代码，实现具体的业务逻辑。不建议在此执行耗时较长的任务，可能会引起推送超时，导致事件重发。
                // 获取当前日期
                LocalDate currentDate = LocalDate.now();
                String fileName = currentDate.toString() + ".txt";

                // 指定目标目录
                String targetDirectory = "E:\\test2\\website-server\\BaseWebProject\\src\\main\\resources\\car";

                try {
                    // 创建目标目录
                    Files.createDirectories(Paths.get(targetDirectory));

                    // 创建文件并写入内容
                    Path filePath = Paths.get(targetDirectory, fileName);
                    FileWriter fileWriter = new FileWriter(filePath.toString(), true); // 打开文件的追加模式
                    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

                    bufferedWriter.write(finalData);
                    bufferedWriter.newLine();
                    bufferedWriter.close();

                    System.out.println("内容已成功写入文件：" + filePath.toString());
                } catch (IOException e) {
                    System.out.println("写入文件时发生错误：" + e.getMessage());
                }

            } catch (Exception e) {

                e.printStackTrace();

            }

            s.success(ObjectResult.SUCCESS);

        }).subscribeOn(Schedulers.elastic());

    }

}

@Data
class ObjectResult implements Serializable {

    private static final long serialVersionUID = -9146805371831100892L;

    final public static ObjectResult SUCCESS = new ObjectResult("200", "操作成功");

    final public static ObjectResult ERROR = new ObjectResult("400", "操作失败");

    final public static ObjectResult EXCEPTION = new ObjectResult("500", "服务异常");

    //返回码

    private String code;

    //返回事件

    private String msg;

    //返回数据

    private String data;

    public ObjectResult(String s, String msg) {
        this.code = s;
        this.msg = msg;
    }
}
