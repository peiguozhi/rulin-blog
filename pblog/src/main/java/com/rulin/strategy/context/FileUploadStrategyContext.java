package com.rulin.strategy.context;

import com.rulin.strategy.FileUploadStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;


/**
 * @apiNote 文件上传策略上下文
 */
@Service
@RequiredArgsConstructor
public class FileUploadStrategyContext {

    private final Map<String, FileUploadStrategy> fileUploadStrategyMap;

    /**
     * 执行文件上传策略
     *
     * @param file 文件对象
     * @return {@link String} 文件名
     */
    public String executeFileUploadStrategy(String fileUploadMode, MultipartFile file,String suffix) {
         return fileUploadStrategyMap.get(fileUploadMode).fileUpload(file,suffix);
    }

    /**
     * 删除文件策略
     * @param fileUploadMode
     * @param key
     * @return
     */
    public Boolean executeDeleteFileStrategy(String fileUploadMode,String ...key) {
         return fileUploadStrategyMap.get(fileUploadMode).deleteFile(key);
    }
}
