package com.xxforest.baseweb.core;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.net.URL;

@Component
public class UrlTool {

    public File resourceFolder;
    public static UrlTool inst ;

    @Value("${spring.resources.static-locations}")
    public String staticLocations;

    public static UrlTool getInstance(){
        return inst ;
    }

    @PostConstruct
    public void init(){
        inst = this ;
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

    public File getFile(String path){
        return new File(resourceFolder.getAbsoluteFile() + "/" + path);
    }

}
