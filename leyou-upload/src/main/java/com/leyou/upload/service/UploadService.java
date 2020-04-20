package com.leyou.upload.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @ClassName:UploadService
 * @Author：Mr.lee
 * @DATE：2020/04/20
 * @TIME： 17:19
 * @Description: TODO
 */
public interface UploadService {
    String upload(MultipartFile file);
}
