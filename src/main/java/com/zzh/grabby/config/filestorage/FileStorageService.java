package com.zzh.grabby.config.filestorage;

import com.qiniu.common.QiniuException;

import java.io.InputStream;

/**
 * @author zzh
 * @date 2019/1/10
 */
public interface FileStorageService {

     String upload(InputStream in) throws QiniuException;
}
