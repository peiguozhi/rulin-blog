package com.rulin.strategy.imp;

import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.rulin.entity.SystemConfig;
import com.rulin.service.SystemConfigService;
import com.rulin.strategy.FileUploadStrategy;
import com.rulin.util.UUIDUtils;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

@Service("aliUploadStrategyImpl")
@RequiredArgsConstructor
public class AliUploadStrategyImpl implements FileUploadStrategy {

    private final Logger logger = LoggerFactory.getLogger(AliUploadStrategyImpl.class);

    private final SystemConfigService systemConfigService;
    private String ali_accessKey;
    private String ali_secretKey;
    private String ali_endpoint;
    private String ali_bucket;

    @PostConstruct
    private void init(){
        SystemConfig systemConfig = systemConfigService.getCustomizeOne();
        ali_accessKey = systemConfig.getAliYunAccessKey();
        ali_secretKey = systemConfig.getAliYunSecretKey();
        ali_endpoint = systemConfig.getAliYunEndpoint();
        ali_bucket = systemConfig.getAliYunBucket();
    }

    @Override
    public String fileUpload(MultipartFile file,String suffix) {
        String key = null;
        ClientBuilderConfiguration conf = getClientBuilderConfiguration();

        // 创建OSS实例
        OSS ossClient = new OSSClientBuilder().build(ali_endpoint, ali_accessKey, ali_secretKey, conf);

        //获取上传文件输入流
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
            //在文件名称里面添加随机唯一值（因为如果上传文件名称相同的话，后面的文件会将前面的文件给覆盖了）
            String newFileName = UUIDUtils.getUuid() + "." + suffix;

            // 调用oss方法实现上传
            // 参数一：Bucket名称  参数二：上传到oss文件路径和文件名称  比如 /aa/bb/1.jpg 或者直接使用文件名称  参数三：上传文件的流
            ossClient.putObject(ali_bucket, newFileName, inputStream);

            //把上传之后的文件路径返回，需要把上传到阿里云路径返回    https://edu-guli-eric.oss-cn-beijing.aliyuncs.com/1.jpg
            key = "https://" + ali_bucket + "." + ali_endpoint + "/" + newFileName;
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return key;
    }

    @NotNull
    private ClientBuilderConfiguration getClientBuilderConfiguration() {
        // 创建ClientConfiguration。ClientConfiguration是OSSClient的配置类，可配置代理、连接超时、最大连接数等参数。
        ClientBuilderConfiguration conf = new ClientBuilderConfiguration();
        // 设置OSSClient允许打开的最大HTTP连接数，默认为1024个。
        conf.setMaxConnections(200);
        // 设置Socket层传输数据的超时时间，默认为50000毫秒。
        conf.setSocketTimeout(10000);
        // 设置建立连接的超时时间，默认为50000毫秒。
        conf.setConnectionTimeout(10000);
        // 设置从连接池中获取连接的超时时间（单位：毫秒），默认不超时。
        conf.setConnectionRequestTimeout(1000);
        // 设置连接空闲超时时间。超时则关闭连接，默认为60000毫秒。
        conf.setIdleConnectionTime(60000);
        // 设置失败请求重试次数，默认为3次。
        conf.setMaxErrorRetry(3);
        // 设置是否支持将自定义域名作为Endpoint，默认支持。
        conf.setSupportCname(true);
        // 设置是否开启HTTP重定向，默认开启。
        conf.setRedirectEnable(true);
        // 设置是否开启SSL证书校验，默认开启。
        conf.setVerifySSLEnable(false);
        return conf;
    }


    /**
     * 删除文件 -- 阿里云OSS
     *
     * @param key   文件url
     * @return      ResponseResult
     */
    @Override
    public Boolean deleteFile(String ...key) {

        // 创建OSS实例
        OSS ossClient = new OSSClientBuilder().build(ali_endpoint, ali_accessKey, ali_secretKey);

        try {
            // 判断文件是否存在。如果返回值为true，则文件存在，否则存储空间或者文件不存在。
            boolean found = ossClient.doesObjectExist(ali_bucket, Arrays.toString(key));
            if (found) {
                // 文件存在，删除文件
                ossClient.deleteObject(ali_bucket, Arrays.toString(key));
                return true;
            } else {
                return false;
            }
        } catch (OSSException oe) {
            // 获取OSS异常
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
            return false;
        } finally {
            if (ossClient != null) {
                // 最终一定要执行关闭OSSClient
                ossClient.shutdown();
            }
        }

    }

}
