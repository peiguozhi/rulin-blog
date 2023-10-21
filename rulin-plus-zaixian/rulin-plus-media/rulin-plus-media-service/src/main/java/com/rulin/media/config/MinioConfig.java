package com.rulin.media.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * minio配置
 *
 * @author 程序儒
 * @date 2023-08-26 16:36:32
 */
@Configuration
public class MinioConfig {


 @Value("${minio.endpoint}")
 private String endpoint;
 @Value("${minio.accessKey}")
 private String accessKey;
 @Value("${minio.secretKey}")
 private String secretKey;

 @Bean
 public MinioClient minioClient() {

  MinioClient minioClient =
          MinioClient.builder()
                  .endpoint(endpoint)
                  .credentials(accessKey, secretKey)
                  .build();
  return minioClient;
 }
}
