package com.zzh.grabby.config.filestorage;

import lombok.Data;

/**
 * @author zzh
 * @date 2019/1/10
 */
@Data
public class QiniuProperties {
    private String bucket;
    private String accessKey;
    private String secretKey;
    private String domainPrefix;
    private String domain;
}
