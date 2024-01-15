package com.xxforest.baseweb.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.*;
import com.thoughtworks.qdox.model.expression.AnnotationValue;
import com.xxforest.baseweb.core.ResponseMessage;
import com.xxforest.baseweb.core.UrlTool;
import com.xxforest.baseweb.core.domain.TypeAnalyser;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/doc")
public class DocController {

    private static String OUT_PROJECT_URL = "BaseWebProject/src/main/java";
//    private static String GREE_PROJECT_URL = "D:\\workplaces\\geli\\greecloud-backend\\cloud-management-service\\src\\main\\java";
    private static String GREE_PROJECT_URL = "E:\\test\\GeLiProject\\greecloud-backend\\cloud-management-service\\src\\main\\java";
    private static JavaProjectBuilder builder ;
    private static Map<String,JavaClassDoc> stringJavaClassDocMap = new ConcurrentHashMap<>();
    private static List<ActionDocInfo> CACHE_DATA ;

    public static class ActionDocInfo{

        public String actionName ;
        public String mapperUrl ;
        public String comment ;
        public String keys ;
        public List<MethodDocInfo> methodDocInfos = new ArrayList<>();

        public static ActionDocInfo create(JavaClass actionClass) {
            return new ActionDocInfo().init(actionClass) ;
        }

        private ActionDocInfo init(JavaClass actionClass) {
            this.actionName = actionClass.getSimpleName();
            this.comment = actionClass.getComment();
            this.mapperUrl = "" ;
            for (JavaAnnotation annotation : actionClass.getAnnotations()) {
                if("RequestMapping".equals(annotation.getType().getSimpleName())){
                    this.mapperUrl = annotation.getProperty("value").toString().replaceFirst("/","").replace("\"","");
                }
            }
            this.keys += this.comment +";"+ this.actionName;
            return this;
        }

        public MethodDocInfo createMethod(JavaMethod method) {
            MethodDocInfo methodDocInfo = MethodDocInfo.create(method);
            methodDocInfos.add(methodDocInfo);
            return methodDocInfo;
        }

        public void addMethodDocInfo(MethodDocInfo methodDocInfo) {
            methodDocInfos.add(methodDocInfo);
            this.keys += methodDocInfo.actionName+";"+methodDocInfo.methodName+";"+methodDocInfo.comment+";"+methodDocInfo.paramDocInfos;
        }
    }

    public static class MethodDocInfo{

        public String actionName ;
        public String actionMapperUrl ;
        public String methodName ;
        public String meothdMapperUrl ;
        public String comment ;
        public String methodType ;
        public String greeJsonVal ;

        public JavaClassDoc returnClassDoc ;
        public List<JavaClassDoc> paramDocInfos = new ArrayList<>();
        public List<JavaClassDoc> relateJavaClassDocsList = new ArrayList<>();

        public static MethodDocInfo create(JavaMethod method) {
            return new MethodDocInfo().init(method);
        }

        private MethodDocInfo init(JavaMethod method) {
            this.methodName = method.getName();
            this.comment = method.getComment();
            DocletTag apiParamExample = method.getTagByName("apiParamExample");
            if(apiParamExample != null){
                String jsonValue = apiParamExample.getValue();
                this.greeJsonVal = jsonValue.substring(jsonValue.indexOf(":")+1,jsonValue.length());
            }
            List<JavaAnnotation> annotations = method.getAnnotations();
            methodType = "ALL";
            for (JavaAnnotation annotation : annotations) {
                if(annotation.getType().getName().toLowerCase().contains("post")){
                    methodType = "POST";
                }else if(annotation.getType().getName().toLowerCase().contains("get")){
                    methodType = "GET";
                }else if(annotation.getType().getName().toLowerCase().contains("delete")){
                    methodType = "DELETE";
                }
                if(annotation.getType().getName().toLowerCase().contains("mapping")){
                    AnnotationValue property = annotation.getProperty("value");
                    if(property != null){
                        this.meothdMapperUrl = property.toString().replaceFirst("/","").replace("\"","");
                    }else{
                        this.meothdMapperUrl = "" ;
                    }

                }
            }
            return this;
        }

        public void addJavaParamter(JavaParameter parameter, String comment) {
            JavaClassDoc javaClassDoc = JavaClassDoc.create();
            for (JavaAnnotation annotation : parameter.getAnnotations()) {
                AnnotationValue value = annotation.getProperty("value");
                if(annotation.getType().getName().toLowerCase().contains("pathvariable")){
                    javaClassDoc.pathType = true;
                    javaClassDoc.pathTypeName = value!= null ? value.toString() :"";
                    javaClassDoc.pathTypeName = javaClassDoc.pathTypeName.replace("\"","");
                }
                if(annotation.getType().getName().toLowerCase().contains("header")){
                    javaClassDoc.headerType = true ;
                    javaClassDoc.headerTypeName = value!= null ? value.toString() :"";
                    javaClassDoc.headerTypeName = javaClassDoc.headerTypeName.replace("\"","");
                }
                if(annotation.getType().getName().toLowerCase().contains("requestbody")){
                    javaClassDoc.requestBodyType= true ;
                }
            }
            javaClassDoc.init(parameter.getName(), comment, parameter.getJavaClass());
            paramDocInfos.add(javaClassDoc);

        }

        public void addReturnType(JavaClass returns) {
            if(returns.getBinaryName().equals(Object.class.getName())){return;}
            JavaClassDoc javaClassDoc = JavaClassDoc.create();
            javaClassDoc.init(returns.getSimpleName(),null,returns);
            this.returnClassDoc = javaClassDoc;
        }

        public void settle() {

            Map<String,JavaClassDoc> stringJavaClassDocMap = new HashMap<>();

            for (JavaClassDoc paramDocInfo : paramDocInfos) {
                findRelateClasses(stringJavaClassDocMap,paramDocInfo);
            }

            for (JavaClassDoc classDoc : stringJavaClassDocMap.values()) {
                relateJavaClassDocsList.add(classDoc);
            }
            Collections.reverse(relateJavaClassDocsList);
        }

        private void findRelateClasses(Map<String, JavaClassDoc> stringJavaClassDocMap, JavaClassDoc paramDocInfo) {
            if(paramDocInfo.baseType){return;}
            if(!stringJavaClassDocMap.containsKey(paramDocInfo.typeName)){
                stringJavaClassDocMap.put(paramDocInfo.typeName,paramDocInfo);
            }
            for (JavaClassDoc javaClassDoc : paramDocInfo.javaClassDocs) {
                findRelateClasses(stringJavaClassDocMap,javaClassDoc);
            }
        }
    }

    public static class JavaClassDoc{

        public String comment ;
        public String fieldName ;
        public String typeName ;
        public boolean arrayType;
        public String simpleTypeName;
        public boolean baseType;
        public boolean number;
        public String value ;
        public boolean dateType ;
        public boolean pathType ;
        public String pathTypeName ;
        public boolean headerType ;
        public String headerTypeName ;
        public boolean requestBodyType ;
        public boolean mapType ;
        public List<JavaClassDoc> javaClassDocs = new ArrayList<>();

        public static JavaClassDoc create() {
            return new JavaClassDoc();
        }

        @Override
        public String toString() {
            return "JavaClassDoc{" +
                    "comment='" + comment + '\'' +
                    ", fieldName='" + fieldName + '\'' +
                    ", typeName='" + typeName + '\'' +
                    ", arrayType=" + arrayType +
                    ", simpleTypeName='" + simpleTypeName + '\'' +
                    ", baseType=" + baseType +
                    ", number=" + number +
                    ", value='" + value + '\'' +
                    ", javaClassDocs=" + javaClassDocs +
                    '}';
        }

        public JavaClassDoc init(String fieldName, String comment, JavaClass javaClass) {
            JavaClass tmpJavaClass = javaClass ;
            this.comment = StrUtil.isBlank(comment)?tmpJavaClass.getComment():comment;
            this.arrayType = tmpJavaClass.isArray() ;
            this.fieldName = fieldName ;
            this.dateType = false ;
            if(!this.arrayType && tmpJavaClass.getName().equals("List")){
                this.arrayType = true ;
            }
            if(tmpJavaClass.getName().equals("Date")){
                this.dateType = true ;
            }

            String resolvedGenericFullyQualifiedName = tmpJavaClass.getGenericFullyQualifiedName();
            if(resolvedGenericFullyQualifiedName.contains("<")){
                this.typeName = resolvedGenericFullyQualifiedName.substring(resolvedGenericFullyQualifiedName.indexOf("<")+1,resolvedGenericFullyQualifiedName.indexOf(">"));
            }else{
                this.typeName = resolvedGenericFullyQualifiedName ;
            }

            this.typeName = this.typeName.replace("[","").replace("]","");
            if(this.typeName.contains(".")){
                this.simpleTypeName = this.typeName.substring(this.typeName.lastIndexOf(".")+1,this.typeName.length());
            }else{
                this.simpleTypeName = this.typeName;
            }

            tmpJavaClass = builder.getClassByName(this.typeName);

            TypeAnalyser typeAnalyser = TypeAnalyser.create(this.simpleTypeName);
            this.baseType = typeAnalyser.isBaseType();
            this.number = typeAnalyser.isNumber();

            if(arrayType){
                value ="[";
            }else{
                value="";
            }

            if(this.number){
                value += "0";
            }else if(typeAnalyser.isBooleanType()){
                value += "false" ;
            }else{
                if(arrayType){
                    value +="\"\"";
                }else{
                    value += "" ;
                }
            }

            if(arrayType){
                value +="]";
            }

            if(StrUtil.isNotBlank(comment) && comment.indexOf(":") != -1){
                value = comment.substring(comment.indexOf(":")+1,comment.length());
            }

            if(tmpJavaClass.getName().toLowerCase().contains("file")){
                this.baseType = true ;
            }

            //兼容格力项目
            if(javaClass.getName().toLowerCase().contains("map")){
                this.mapType = true ;
                this.requestBodyType = true;
            }

            //兼容格力项目
            if(javaClass.getName().toLowerCase().contains("page")){
                this.mapType = true ;
                this.requestBodyType = true;
            }

            if(!this.baseType && !this.dateType && !this.mapType && !this.typeName.equalsIgnoreCase("void")){
                StringBuilder stringBuilder = new StringBuilder();
                if(arrayType){
                    stringBuilder.append("[");
                }
                stringBuilder.append("{ ");
                List<JavaField> fields = tmpJavaClass.getFields();
                for (JavaField field : fields) {
                    String dockey = field.getType().getGenericFullyQualifiedName() + "_"+ field.getName()+"_"+field.getComment() ;
                    JavaClassDoc javaClassDoc = stringJavaClassDocMap.get(dockey);
                    if(javaClassDoc == null){
                        javaClassDoc = JavaClassDoc.create();
                        stringJavaClassDocMap.put(dockey,javaClassDoc);
                        javaClassDoc.init(field.getName(), field.getComment(), field.getType());
                    }
                    javaClassDocs.add(javaClassDoc);

                    if(field.getType().getSimpleName().equalsIgnoreCase("string") || field.getType().getSimpleName().equalsIgnoreCase("date") ){
                        stringBuilder.append("\"").append(field.getName()).append("\":").append(StrUtil.isBlank(javaClassDoc.value)?"\"\"":"\""+javaClassDoc.value + "\"").append(",");
                    }else{
                        stringBuilder.append("\"").append(field.getName()).append("\":").append(StrUtil.isBlank(javaClassDoc.value)?"\"\"":javaClassDoc.value).append(",");
                    }

                }
                stringBuilder.delete(stringBuilder.length()-1,stringBuilder.length());
                stringBuilder.append("}");
                if(arrayType){
                    stringBuilder.append("]");
                    JSONArray objects = JSONObject.parseArray(stringBuilder.toString());
                    String pretty = JSON.toJSONString(objects, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue,
                            SerializerFeature.WriteDateUseDateFormat);

                    value = pretty;
                }else{
                    try{
                        JSONObject object = JSONObject.parseObject(stringBuilder.toString());
                        String pretty = JSON.toJSONString(object, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue,
                                SerializerFeature.WriteDateUseDateFormat);
                        value = pretty;
                    }catch (Exception e){
                        System.out.println(javaClass);
                        e.printStackTrace();
                    }

                }

            }

            return this ;
        }
    }

    @PostMapping("/doc")
    public ResponseMessage loadDoc(int platformType){
        String projectUrl = OUT_PROJECT_URL;
        if(platformType == 1){
            projectUrl = GREE_PROJECT_URL ;
        }
        File directory = new File(projectUrl);
        if(directory.exists()) {

//            if (CACHE_DATA != null) {
//                return ResponseMessage.success("data",CACHE_DATA);
//            }
            stringJavaClassDocMap = new ConcurrentHashMap<>();
            builder = new JavaProjectBuilder();

            builder.addSourceTree(directory);
            Collection<JavaClass> classes = builder.getClasses();
            Set<JavaClass> actionClasses = new HashSet<>();
            for (JavaClass aClass : classes) {
                for (JavaAnnotation annotation : aClass.getAnnotations()) {
                    if (annotation.getType().getName().toLowerCase().contains("controller")) {
                        actionClasses.add(aClass);
                    }
                }
            }

            List<ActionDocInfo> actionDocInfos = new ArrayList<>();
            for (JavaClass actionClass : actionClasses) {
                ActionDocInfo actionDocInfo = ActionDocInfo.create(actionClass);
                for (JavaMethod method : actionClass.getMethods()) {
                    if (method.isPublic() && !method.isStatic()) {
                        MethodDocInfo methodDocInfo = MethodDocInfo.create(method);
                        List<JavaParameter> parameters = method.getParameters();

                        List<DocletTag> tags = method.getTags();
                        Map<String, String> docletTagMap = new HashMap<>();
                        for (DocletTag tag : tags) {
                            List<String> tagParams = tag.getParameters();
                            if (tagParams.size() >= 2) {
                                docletTagMap.put(tagParams.get(0), tag.getValue());
                            }
                        }
                        for (JavaParameter parameter : parameters) {
                            if (parameter.getType().getBinaryName().equals(HttpServletRequest.class.getName())) {
                                continue;
                            }
                            if (parameter.getType().getBinaryName().equals(HttpServletResponse.class.getName())) {
                                continue;
                            }
                            if (parameter.getType().getBinaryName().equals(Request.class.getName())) {
                                continue;
                            }
                            if (parameter.getType().getBinaryName().equals(Response.class.getName())) {
                                continue;
                            }
                            methodDocInfo.addJavaParamter(parameter, docletTagMap.get(parameter.getName()));
                        }

//                        JavaClass returns = method.getReturns();
//                        methodDocInfo.addReturnType(returns);
                        methodDocInfo.actionName = actionDocInfo.actionName;
                        methodDocInfo.actionMapperUrl = actionDocInfo.mapperUrl ;
                        methodDocInfo.settle();
                        actionDocInfo.addMethodDocInfo(methodDocInfo);
                    }
                }
                if (actionDocInfo.methodDocInfos.size() > 0) {
                    actionDocInfos.add(actionDocInfo);
                }

            }
            CACHE_DATA = actionDocInfos;
            FileUtil.writeString(JSON.toJSONString(CACHE_DATA),UrlTool.getInstance().getFile("/doc/interface.json"), Charset.forName("UTF-8"));
        }else{
            String json = FileUtil.readString(UrlTool.getInstance().getFile("/doc/interface.json"), Charset.forName("UTF-8"));
            CACHE_DATA = JSON.parseArray(json,ActionDocInfo.class);
        }
        return ResponseMessage.success("data",CACHE_DATA);
    }

}
