package com.doublefish.langlang.controller;

import com.doublefish.langlang.exception.CommonException;
import com.doublefish.langlang.mapper.CourseWorkMapper;
import com.doublefish.langlang.pojo.entity.CourseWork;
import com.doublefish.langlang.pojo.entity.User;
import com.doublefish.langlang.service.CourseWorkService;
import com.doublefish.langlang.service.UnitService;
import com.doublefish.langlang.token.Token;
import com.doublefish.langlang.utils.HttpUtils;
import com.doublefish.langlang.utils.UploadFileStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping(value = "/")
public class UploadFileController {

    @Autowired
    CourseWorkService courseWorkService;
    @Autowired
    CourseWorkMapper courseWorkMapper;
    @Autowired
    UnitService unitService;
    /**
     * 接收上传的文件，并且将上传的文件存储在指定路径下
     * @param request
     * @return
     */
    @RequestMapping(value = "/upload")
    public String upload(HttpServletRequest request) {

        ServletInputStream sis = null;
        FileOutputStream fos = null;
        try {
            // 文件名
            String filename = request.getHeader("fileName");
            filename = new String(filename.getBytes("ISO-8859-1"),"utf-8");
            // 文件类型，例如：jpg、png、pdf...
            String fileType = request.getHeader("fileType");
            // 存储路径
            String filePath = request.getHeader("filePath");

            File file = new File(filePath+filename+"."+fileType);
            if(!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if(!file.exists()) {
                file.createNewFile();
            }

            sis = request.getInputStream();
            fos = new FileOutputStream(file);
            byte[] content = new byte[1024];
            int len = 0;
            while((len=sis.read(content)) > -1) {
                fos.write(content, 0, len);
            }
            fos.flush();

            return "success";
        } catch (Exception ex) {
            ex.printStackTrace();

            return "fail";
        } finally {
            try {
                if(sis!=null) {
                    sis.close();
                }
                if(fos!=null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @InitBinder
    public void initBinder(WebDataBinder binder, WebRequest request) {

        //转换日期
        DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));// CustomDateEditor为自定义日期编辑器
    }

    //老师上传作业
    @PostMapping("/uploadCourseWork/{subjectId}")
    public void teacherUpload(HttpServletRequest request, @PathVariable("subjectId") Integer subjectId,
                           @RequestParam("introduction") String introduction,@RequestParam("name") String name,
                           @RequestParam("start_time") String start_time,@RequestParam("end_time") String end_time,
                           @RequestParam("files") MultipartFile[] multiReq) throws IOException, ParseException {

        System.out.println(start_time);
        System.out.println(end_time);

        courseWorkService.Insert(subjectId,introduction,name,multiReq.length,start_time,end_time);

        CourseWork find = new CourseWork();
        find.setName(name);
        Integer workId = courseWorkMapper.selectOne(find).getId();
        if(multiReq.length > 0){
//            System.out.println(2);
            for(int i = 0;i < multiReq.length;i++){
                // 获取上传文件的路径
                String uploadFilePath = multiReq[i].getOriginalFilename();
                System.out.println("uploadFlePath:" + uploadFilePath);
                // 截取上传文件的文件名
                String uploadFileName = uploadFilePath.substring(
                        uploadFilePath.lastIndexOf('\\') + 1, uploadFilePath.indexOf('.'));
                System.out.println("multiReq.getFile()" + uploadFileName);
                // 截取上传文件的后缀
                String uploadFileSuffix = uploadFilePath.substring(
                        uploadFilePath.indexOf('.') + 1, uploadFilePath.length());
                System.out.println("uploadFileSuffix:" + uploadFileSuffix);

                FileInputStream inputStream = (FileInputStream) multiReq[i].getInputStream();
                UploadFileStatus fileStatus = new UploadFileStatus();
                fileStatus.setFileName(workId.toString()+"-"+i);
                // 上传到服务器的哪个位置
                fileStatus.setFilePath("/root/usr/local/webapp/");
                // 文件的大小
                fileStatus.setFileSize(inputStream.available());
                // 文件的类型
                fileStatus.setFileType(uploadFileSuffix);

                fileStatus.setInputStream(inputStream);

                String result = HttpUtils.postFile("http://101.132.132.173:8080/upload", fileStatus);
                System.out.println(result);
            }
        }
        else if(multiReq.length == 0){

        }
        else {
            throw new CommonException("上传失败");
        }
    }

    //学生上传作业
    @PostMapping("/student/uploadCourseWork/{courseworkId}")
    public void studentUpload(HttpServletRequest request, @PathVariable("courseworkId") Integer cwid,
                           @RequestParam("introduction") String introduction,@Token User user,
                           @RequestParam("files") MultipartFile[] multiReq) throws IOException, ParseException {

//        User user = new User();
//        user.setUid(2);

        courseWorkService.InsertStudent(cwid,introduction,user,multiReq.length);


        if(multiReq.length > 0){
            for(int i = 0;i < multiReq.length;i++){
                // 获取上传文件的路径
                String uploadFilePath = multiReq[i].getOriginalFilename();
                System.out.println("uploadFlePath:" + uploadFilePath);
                // 截取上传文件的文件名
                String uploadFileName = uploadFilePath.substring(
                        uploadFilePath.lastIndexOf('\\') + 1, uploadFilePath.indexOf('.'));
                System.out.println("multiReq.getFile()" + uploadFileName);
                // 截取上传文件的后缀
                String uploadFileSuffix = uploadFilePath.substring(
                        uploadFilePath.indexOf('.') + 1, uploadFilePath.length());
                System.out.println("uploadFileSuffix:" + uploadFileSuffix);

                FileInputStream inputStream = (FileInputStream) multiReq[i].getInputStream();
                UploadFileStatus fileStatus = new UploadFileStatus();
                fileStatus.setFileName(cwid.toString()+"-"+user.getUid()+"-"+i);
                // 上传到服务器的哪个位置
                fileStatus.setFilePath("/root/usr/local/webapp/");
                // 文件的大小
                fileStatus.setFileSize(inputStream.available());
                // 文件的类型
                fileStatus.setFileType(uploadFileSuffix);

                fileStatus.setInputStream(inputStream);

                String result = HttpUtils.postFile("http://101.132.132.173:8080/upload", fileStatus);
                System.out.println(result);
            }
        }
        else if(multiReq.length == 0){

        }
        else {
            throw new CommonException("上传失败");
        }
    }


    @PostMapping("/uploadUnitFiles/{unitId}")
    public String UnitFilesUpload(HttpServletRequest request, @PathVariable("unitId") Integer unitId,
                                  @RequestParam("files") MultipartFile[] multiReq) throws IOException, ParseException {



        if(multiReq.length > 0){
            for(int i = 0;i < multiReq.length;i++){
                // 获取上传文件的路径
                String uploadFilePath = multiReq[i].getOriginalFilename();
                System.out.println("uploadFlePath:" + uploadFilePath);
                // 截取上传文件的文件名
                String uploadFileName = uploadFilePath.substring(
                        uploadFilePath.lastIndexOf('\\') + 1, uploadFilePath.indexOf('.'));
                System.out.println("multiReq.getFile()" + uploadFileName);
                // 截取上传文件的后缀
                String uploadFileSuffix = uploadFilePath.substring(
                        uploadFilePath.indexOf('.') + 1, uploadFilePath.length());
                System.out.println("uploadFileSuffix:" + uploadFileSuffix);
                FileInputStream inputStream = (FileInputStream) multiReq[i].getInputStream();
                UploadFileStatus fileStatus = new UploadFileStatus();

                String fileName = uploadFileName+"_"+unitId;
                fileStatus.setFileName(fileName);
                // 上传到服务器的哪个位置
                fileStatus.setFilePath("/root/usr/local/webapp/");
                // 文件的大小
                fileStatus.setFileSize(inputStream.available());
                // 文件的类型
                fileStatus.setFileType(uploadFileSuffix);

                fileStatus.setInputStream(inputStream);

                String result = HttpUtils.postFile("http://101.132.132.173:8080/upload", fileStatus);
                System.out.println(result);

                unitService.insertFile(fileName+"."+uploadFileSuffix,unitId);
            }
        }
        else {
            throw new CommonException("上传失败");
        }
        return "上传成功";
    }

    //删除上传的文件
    @PostMapping("/deleteFile")
    public String delFile(String fileName) {
        //上篇中前端穿过来的是/imctemp-rainy/*.jpg格式的，只需要取出最后的文件名，写死路径，就可以准确找到文件了
        //前端页面处理路径，
        //var imgpath=path.split("/");
        //imgpath = imgpath[imgpath.length-1];
        String resultInfo = null;
        //System.out.println(path);///imctemp-rainy/2.jpg
//        int lastIndexOf = path.lastIndexOf("/");
//        String img_path = path.substring(lastIndexOf + 1, path.length());
        //System.out.println(img_path);
        String filePath = "/root/usr/local/webapp/" + fileName;
        //System.out.println(img_path);//输出测试一下文件路径是否正确
        File file = new File(filePath);
        if (file.exists()) {//文件是否存在
            if (file.delete()) {//存在就删了
                unitService.DeleteUnitFile(fileName);
                resultInfo =  "删除成功";
            } else {
                resultInfo =  "删除失败";
            }
        } else {
            resultInfo = "文件不存在！";
        }
        return resultInfo;
    }

}
