package com.rulin.service;

import com.rulin.common.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

public interface CloudOssService {
    /**
     * 上传
     * @param file 文件
     * @return
     */
    ResponseResult upload(MultipartFile file);

    /**
     * 批量删除文件
     * @param key 文件名
     * @return
     */
    ResponseResult delBatchFile(String ...key);
}
