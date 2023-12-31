package com.sundwich.reggie.controller;

import com.sundwich.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

/**
 * file uploading and downloading
 *
 * @author JX Sun
 * @date 2023.07.24 21:22
 */

@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {

    @Value("${reggie.path}")
    String basePath;

    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) {
        log.info(file.toString());

        //原始文件名
        String originalFileName = file.getOriginalFilename();//abc.jpg
        //获取后缀
        String suffix = originalFileName.substring(originalFileName.lastIndexOf("."));
        //使用UUID随机生成新的文件名，防止文件名称重复造成文件覆盖
        String fileName = UUID.randomUUID().toString() + suffix;

        //创建一个目录对象
        File dir = new File(basePath);
        //判断当前目录是否存在
        if (!dir.exists()) {
            //目录不存在 需要创建
            dir.mkdirs();
        }

        try {
            //将临时文件转存到指定位置
            file.transferTo(new File(basePath + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success(fileName);
    }

    /**
     * 文件下载
     *
     * @param name
     * @param response
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) {
        //输入流，读取文件内容
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));
            //输出流，把图片写回浏览器，在浏览器展示图片
            ServletOutputStream outputStream=response.getOutputStream();

            response.setContentType("image/jpeg");

            int len=0;
            byte[] bytes=new byte[1024];
            while((len = fileInputStream.read(bytes))!=-1){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }

            //关闭资源
            outputStream.close();
            fileInputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
