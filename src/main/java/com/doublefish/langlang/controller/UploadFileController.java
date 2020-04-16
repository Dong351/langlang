package com.doublefish.langlang.controller;

import com.doublefish.langlang.exception.CommonException;
import com.doublefish.langlang.mapper.CourseWorkMapper;
import com.doublefish.langlang.pojo.entity.CourseWork;
import com.doublefish.langlang.service.CourseWorkService;
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

    @PostMapping("/uploadCourseWork/{subjectId}")
    public void uploadInfo(HttpServletRequest request, @PathVariable("subjectId") Integer subjectId,
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

                String result = HttpUtils.postFile("http://47.106.83.201/upload", fileStatus);
                System.out.println(result);
            }
        }
        else if(multiReq.length == 0){

        }
        else {
            throw new CommonException("上传失败");
        }
    }
}
