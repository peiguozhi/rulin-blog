package com.rulin.strategy.imp;

import com.alibaba.fastjson.JSON;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.BatchStatus;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;
import com.rulin.entity.SystemConfig;
import com.rulin.enums.QiNiuAreaEnum;
import com.rulin.service.SystemConfigService;
import com.rulin.strategy.FileUploadStrategy;
import com.rulin.util.UUIDUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;


@Service("qiNiuUploadStrategyImpl")
@RequiredArgsConstructor
public class QiNiuUploadStrategyImpl implements FileUploadStrategy {

    private final Logger logger = LoggerFactory.getLogger(QiNiuUploadStrategyImpl.class);

    private final SystemConfigService systemConfigService;
    private String qi_niu_accessKey;
    private String qi_niu_secretKey;
    private String qi_niu_bucket;
    private Region region;
    private String qi_niu_url;

    @PostConstruct
    private void init(){
        SystemConfig systemConfig = systemConfigService.getCustomizeOne();
        qi_niu_accessKey = systemConfig.getQiNiuAccessKey();
        qi_niu_secretKey = systemConfig.getQiNiuSecretKey();
        qi_niu_bucket = systemConfig.getQiNiuBucket();
        qi_niu_url = systemConfig.getQiNiuPictureBaseUrl();
        region = QiNiuAreaEnum.getRegion(systemConfig.getQiNiuArea());
    }

    public void list() {
        Configuration configuration = new Configuration(region);
        Auth auth = Auth.create(qi_niu_accessKey, qi_niu_secretKey);
        BucketManager bucketManager = new BucketManager(auth,configuration);
        BucketManager.FileListIterator fileListIterator = bucketManager.createFileListIterator(qi_niu_bucket, null, 1000, null);
        while (fileListIterator.hasNext()) {
            FileInfo[] next = fileListIterator.next();
            for (FileInfo fileInfo : next) {
                logger.info("文件打印开始,文件名：{}",qi_niu_url + fileInfo.key);
                logger.info("文件类别打印开始,类别：{}",fileInfo.mimeType);
                logger.info("文件大小打印开始,大小：{}",fileInfo.fsize);
            }
        }
    }

    @Override
    public String fileUpload(MultipartFile file,String suffix) {
        String key = null;
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(region);
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传
        Auth auth = Auth.create(qi_niu_accessKey, qi_niu_secretKey);
        String upToken = auth.uploadToken(qi_niu_bucket);
        FileInputStream inputStream = null;
        try {
            inputStream = (FileInputStream) file.getInputStream();
            Response response = uploadManager.put(inputStream, UUIDUtils.getUuid() + "." + suffix, upToken,null,null);
            //解析上传成功的结果
            DefaultPutRet putRet = JSON.parseObject(response.bodyString(),DefaultPutRet.class);
            key =  qi_niu_url + putRet.key;
        } catch (QiniuException ex) {
            Response r = ex.response;
            logger.info("QiniuException:{}",r.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return key;
    }

    /**
     * 批量删除文件
     * @return
     */
    @Override
    public Boolean deleteFile(String ...keys) {
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.region2());
        //...其他参数参考类注释
        Auth auth = Auth.create(qi_niu_accessKey, qi_niu_secretKey);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            BucketManager.BatchOperations batchOperations = new BucketManager.BatchOperations();
            batchOperations.addDeleteOp(qi_niu_bucket, keys);
            Response response = bucketManager.batch(batchOperations);
            BatchStatus[] batchStatusList = response.jsonToObject(BatchStatus[].class);
            for (int i = 0; i < keys.length; i++) {
                BatchStatus status = batchStatusList[i];
                String key = keys[i];
                System.out.print(key + "\t");
                if (status.code == 200) {
                    System.out.println("delete success");
                } else {
                    System.out.println(status.data.error);
                }
            }
            return true;
        } catch (QiniuException ex) {
            System.err.println(ex.response.toString());
            return false;
        }
    }
}
