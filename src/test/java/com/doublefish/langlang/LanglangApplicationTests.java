package com.doublefish.langlang;

import com.doublefish.langlang.utils.HttpUtils;
import com.doublefish.langlang.utils.UploadFileStatus;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@SpringBootTest
class LanglangApplicationTests {

    @Test
    public void uploadFileTest() throws FileNotFoundException {

        // 要上传的文件
        File file = new File("X:\\uploads\\疫情采访.docx");
        UploadFileStatus fileStatus = new UploadFileStatus();
        // 上传到服务器后的文件名
        fileStatus.setFileName("1");
        // 上传到服务器的哪个位置
        fileStatus.setFilePath("/root/myFile/");
        // 文件的大小
        fileStatus.setFileSize(file.length());
        // 文件的类型
        fileStatus.setFileType("docx");
        fileStatus.setInputStream(new FileInputStream(file));

        String result = HttpUtils.postFile("http://101.132.132.173:8081/upload", fileStatus);
        System.out.println(result);
    }


}
