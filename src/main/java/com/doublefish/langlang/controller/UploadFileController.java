package com.doublefish.langlang.controller;

import com.doublefish.langlang.pojo.entity.User;
import com.doublefish.langlang.token.Token;
import com.doublefish.langlang.utils.HttpUtils;
import com.doublefish.langlang.utils.UploadFileStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

@RestController
@RequestMapping(value = "/")
public class UploadFileController {

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

    @PostMapping("/uploadCourseWork/{subjectId}")
    public void uploadInfo(HttpServletRequest request, @PathVariable("subjectId") Integer subjectId,
                           String introduction,@Token User user,
                           MultipartHttpServletRequest multiReq) throws IOException {
        // 获取上传文件的路径
        String uploadFilePath = multiReq.getFile("file").getOriginalFilename();
        System.out.println("uploadFlePath:" + uploadFilePath);
        // 截取上传文件的文件名
        String uploadFileName = uploadFilePath.substring(
                uploadFilePath.lastIndexOf('\\') + 1, uploadFilePath.indexOf('.'));
        System.out.println("multiReq.getFile()" + uploadFileName);
        // 截取上传文件的后缀
        String uploadFileSuffix = uploadFilePath.substring(
                uploadFilePath.indexOf('.') + 1, uploadFilePath.length());
        System.out.println("uploadFileSuffix:" + uploadFileSuffix);

        FileInputStream inputStream = (FileInputStream) multiReq.getFile("file").getInputStream();
        UploadFileStatus fileStatus = new UploadFileStatus();
        fileStatus.setFileName(subjectId.toString());
        // 上传到服务器的哪个位置
        fileStatus.setFilePath("/root/myFile/");
        // 文件的大小
        fileStatus.setFileSize(inputStream.available());
        // 文件的类型
        fileStatus.setFileType(uploadFileSuffix);

        fileStatus.setInputStream(inputStream);

        String result = HttpUtils.postFile("http://47.106.83.201/upload", fileStatus);
        System.out.println(result);
    }
}
