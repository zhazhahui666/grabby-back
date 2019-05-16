package com.zzh.grabby.config.filestorage;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author zzh
 * @date 2019/1/10
 */
@Data
@Component
@ConfigurationProperties(prefix = "grabby.file-storage")
public class FileStorageProperties {
    private QiniuProperties qiniu;
}
