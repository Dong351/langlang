package com.doublefish.langlang.service;

import net.minidev.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class LogService{

    public JSONObject logUpload(MultipartFile file) throws Exception {
        JSONObject jsonObject = new JSONObject();
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("未选择需上传的日志文件");
        }

        String filePath = new File("/usr/java/tomcat/upload").getAbsolutePath();
        File fileUpload = new File(filePath);
        if (!fileUpload.exists()) {
            fileUpload.mkdirs();
        }

        fileUpload = new File(filePath, file.getOriginalFilename());
        if (fileUpload.exists()) {
            throw new RuntimeException("上传的日志文件已存在");
        }

        try {
            file.transferTo(fileUpload);
            jsonObject.put("message","success");
            return jsonObject;
        } catch (IOException e) {
            throw new RuntimeException("上传日志文件到服务器失败：" + e.toString());
        }
    }
}
