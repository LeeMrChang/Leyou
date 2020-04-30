package com.leyou.upload.service.impl;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.leyou.upload.service.UploadService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @ClassName:UploadServiceImpl
 * @Author：Mr.lee
 * @DATE：2020/04/20
 * @TIME： 17:19
 * @Description: TODO
 */
@Service
public class UploadServiceImpl implements UploadService {
    /**
     *  检验文件的后缀名类型
     */
    private static final List<String> CONTENT_TYPES = Arrays.asList("image/jpeg", "image/gif","image/jpg");

    /**
     *   Logger对象，可以用来打印错误日志信息
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(UploadService.class);

    /**
     * FDS的文件上传服务端工具类
     */
    @Autowired
    private FastFileStorageClient fileStorageClient;

    /**
     * 文件上传
     * @param file
     * @return
     */
    @Override
    public String upload(MultipartFile file) {
        //获取文件名称
        String filename = file.getOriginalFilename();
        //获取文件类型
        String contentType = file.getContentType();
        //检验文件类型
        if(!CONTENT_TYPES.contains(contentType)){
            //文件类型不合法, LOGGER 对象打印错误日志信息
            LOGGER.info("文件类型不合法：{}",filename);
            return null;
        }

        try {
            //检验文件内容  ImageIO对象用来读取一个文件
            BufferedImage image = ImageIO.read(file.getInputStream());
            if(image == null){
                LOGGER.info("文件内容不合法：{}", filename);
                return null;
            }

            String last = StringUtils.substringAfterLast(filename, ".");
            //保存到服务器
            //file.transferTo(new File("D:\\leyou\\images\\"+filename));
            //这个是FDS 生成的文件路径名
            StorePath storePath = this.fileStorageClient.uploadFile(
                    file.getInputStream(), //文件流
                    file.getSize(),    //文件大小
                    last,              //文件名分割好的字符串
                    null);

            //返回url，进行图片回显
            //return "http://image.leyou.com/"+filename;
            return "http://image.leyou.com/"+storePath.getFullPath();
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.info("服务器内部错误：{}", filename);
            e.printStackTrace();
        }
        return null;
    }
}
