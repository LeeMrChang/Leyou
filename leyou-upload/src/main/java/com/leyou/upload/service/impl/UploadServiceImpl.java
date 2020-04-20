package com.leyou.upload.service.impl;

import com.leyou.upload.service.UploadService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
     * 文件上传
     * @param file
     * @return
     */
    @Override
    public String upload(MultipartFile file) {

        return null;
    }
}
