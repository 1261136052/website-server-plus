package com.xxforest.baseweb.controller;

import cn.hutool.core.io.FileUtil;
import com.xxforest.baseweb.core.IdTool;
import com.xxforest.baseweb.core.ResponseMessage;
import com.xxforest.baseweb.core.ServerDao;
import com.xxforest.baseweb.core.anno.Auth;
import com.xxforest.baseweb.core.anno.AuthType;
import com.xxforest.baseweb.domain.UploadFile;
import com.xxforest.baseweb.manager.AdminManager;
import com.xxforest.baseweb.manager.UploadFileManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 统一上传接口
 */
@RestController
@RequestMapping("/upload")
public class UploadController {

    @Value("${spring.resources.static-locations}")
    public String staticLocations;

    public File resourceFolder;

    @Autowired
    private ServerDao serverDao;
    @Autowired
    private AdminManager adminManager ;
    @Autowired
    private UploadFileManager uploadFileManager;

    /**
     * 统一上传接口
     * @param file 文件实体
     * @param token token
     * @return
     */
    @Auth(AuthType.USER)
    @PostMapping("/upload")
    public ResponseMessage uploadFiles(@RequestParam("file")MultipartFile file,@RequestHeader(value = "adminToken",required = false) String token){

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        String format = simpleDateFormat.format(new Date());
        String savePath = "/public/uploads/"+format+"/";
        File uploadFolder = new File(resourceFolder.getAbsolutePath()+savePath);
        if(!uploadFolder.exists()){
            uploadFolder.mkdirs();
        }
        long id = IdTool.nextId();
        String name = file.getOriginalFilename().trim();
        String postfix = name.substring(name.lastIndexOf("."),name.length());
        String saveName = id+postfix ;
        savePath = savePath +saveName;

        UploadFile uploadFile = new UploadFile();
        uploadFile.setName(name);

        uploadFile.setId(id);
        uploadFile.setUploadDate(new Date());
        uploadFile.setFileSize(file.getSize());
        uploadFile.setTempFile(0);
        uploadFile.setSaveName(saveName);
        uploadFile.setPostfix(postfix);
        uploadFile.setPath(savePath);
        uploadFile.setSystemPath(resourceFolder.getAbsolutePath()+savePath);

        try {
            FileUtil.writeBytes(file.getBytes(),new File(uploadFile.getSystemPath()));
            serverDao.insert(uploadFile);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseMessage.error("上传失败！");
        }
        return ResponseMessage.success("file",uploadFile);
    }

    /**
     * 删除临时文件
     */
    @Scheduled(cron = "0 0 * * * *")  //cron接受cron表达式，根据cron表达式确定定时规则
    public void deleteTempFilesScheduled() {
        System.out.println("---------------删除临时文件------------------");
        uploadFileManager.deleteTempFiles();
    }

    /**
     * 初始化上传目录
     */
    @PostConstruct
    public void initUploadPath(){

        staticLocations = this.staticLocations.trim();
        String uploadFolderName = staticLocations;
        if(staticLocations.indexOf(":") != -1){
            uploadFolderName = staticLocations.substring(staticLocations.lastIndexOf(":")+1,staticLocations.length());
        }
        File tempUploadFolder = null ;
        if(staticLocations.indexOf("file:") == 0){
            tempUploadFolder = new File(uploadFolderName);
        }else{
            ClassLoader classLoader = getClass().getClassLoader();
            URL resource = classLoader.getResource(uploadFolderName);
            tempUploadFolder = new File(resource.getFile());
        }
        if(!tempUploadFolder.exists()){
            tempUploadFolder.mkdirs();
        }
        resourceFolder = tempUploadFolder ;
    }
}
