package com.rulin.strategy.imp;

import com.rulin.entity.SystemConfig;
import com.rulin.service.SystemConfigService;
import com.rulin.strategy.FileUploadStrategy;
import com.rulin.util.UUIDUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;


@Service("localUploadStrategyImpl")
@RequiredArgsConstructor
public class LocalUploadStrategyImpl implements FileUploadStrategy {

    private final Logger logger = LoggerFactory.getLogger(LocalUploadStrategyImpl.class);

    private final SystemConfigService systemConfigService;
    @Value("${file.upload-folder}")
    private String UPLOAD_FOLDER;

    private String localFileUrl;

    @PostConstruct
    private void init(){
        SystemConfig systemConfig = systemConfigService.getCustomizeOne();
        localFileUrl = systemConfig.getLocalFileUrl();
    }

    @Override
    public String fileUpload(MultipartFile file,String suffix) {
        String savePath = UPLOAD_FOLDER;
        File savePathFile = new File(savePath);
        if (!savePathFile.exists()) {
            //若不存在该目录，则创建目录
            savePathFile.mkdir();
        }
        //通过UUID生成唯一文件名
        String filename = UUIDUtils.getUuid() + "." + suffix;
        try {
            //将文件保存指定目录
            file.transferTo(new File(savePath + filename));
        } catch (Exception e) {
            e.printStackTrace();
        }
        //返回文件名称
        return localFileUrl + filename;
    }

    @Override
    public Boolean deleteFile(String... fileName) {
        return false;
    }
}
