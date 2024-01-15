package com.xxforest.baseweb.manager;

import com.xxforest.baseweb.core.ServerDao;
import com.xxforest.baseweb.domain.UploadFile;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
public class UploadFileManager {

    @Autowired
    private ServerDao serverDao;

    /**
     * 删除临时文件
     */
    public void deleteTempFiles() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,-1);
        Date time = calendar.getTime();
        List<UploadFile> uploadFiles = serverDao.query(UploadFile.class, Cnd.where("tempFile", "=", "0").and("uploadDate", "<", time));
        for (UploadFile uploadFile : uploadFiles) {
            serverDao.delete(UploadFile.class,uploadFile.getId());
            //windows下环境不删除
//            if(!System.getenv("os").toLowerCase().contains("win")){
                File file = new File(uploadFile.getSystemPath());
                file.delete();
//            }
        }
    }

    /**
     * 保留该上传文件
     * @param path
     */
    public void retentionFile(String path){
        serverDao.update(UploadFile.class, Chain.make("tempFile",1), Cnd.where("path" ,"=",path));
    }


    /**
     * 保留该上传文件
     * @param
     */
    public void goodsFile(List<Long> ids,long goodsId){

            serverDao.update(UploadFile.class,Chain.make("goodsId",goodsId),Cnd.where("id","in",ids));
            serverDao.update(UploadFile.class,Chain.make("tempFile",1),Cnd.where("id","in",ids));

    }

    public List<Long> getOldFilesId(long goodsId) {
       return serverDao.query(UploadFile.class,Cnd.where("goodsId","=",goodsId),null,
                "^id$").stream().collect(ArrayList::new,(list,file) -> list.add(file.getId()),ArrayList::addAll);
    }

    public void deleteFiles(List<Long> delFilesIds) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,-1);
        List<UploadFile> uploadFiles = serverDao.query(UploadFile.class, Cnd.where("id", "in", delFilesIds));
        for (UploadFile uploadFile : uploadFiles) {
            serverDao.delete(UploadFile.class,uploadFile.getId());
            //windows下环境不删除
//            if(!System.getenv("os").toLowerCase().contains("win")){
                File file = new File(uploadFile.getSystemPath());
                file.delete();
//            }
        }
    }
}
