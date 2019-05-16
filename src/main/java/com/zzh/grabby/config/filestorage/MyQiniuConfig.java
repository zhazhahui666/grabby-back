package com.zzh.grabby.config.filestorage;

import com.qiniu.common.Zone;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zzh
 * @date 2019/1/10
 */
@Configuration
public class MyQiniuConfig {

    @Autowired
    private FileStorageProperties fileStorageProperties;

    /**
     * 设置机房(华东)
     * @return
     */
    @Bean
    public com.qiniu.storage.Configuration qiniuConfig(){
        return new com.qiniu.storage.Configuration(Zone.zone0());
    }

    /**
     * 上传工具实例
     * @return
     */
    @Bean
    public UploadManager uploadManager(){
        return new UploadManager(qiniuConfig());
    }

    /**
     * 认证信息实例
     * @return
     */
    @Bean
    public Auth auth(){
        return Auth.create(fileStorageProperties.getQiniu().getAccessKey(), fileStorageProperties.getQiniu().getSecretKey());
    }

    /**
     * 七牛仓库管理实例
     */
    @Bean
    public BucketManager bucketManager(){
        return new BucketManager(auth(), qiniuConfig());
    }

}
