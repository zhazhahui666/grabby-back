package com.zzh.grabby.config.filestorage;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.zzh.grabby.exception.GrabbyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;

/**
 * @author zzh
 * @date 2019/1/10
 */
@Component
@Slf4j
public class QiniuServiceImpl implements FileStorageService {

    @Autowired
    private Auth auth;

    @Autowired
    private UploadManager uploadManager;

    @Autowired
    private FileStorageProperties fileStorageProperties;

    @Override
    public String upload(InputStream in) {
        try {
            Response response = uploadManager.put(in, null, getUploadToken(),
                    null, null);
            int retry = 0;
            while (response.needRetry() && retry < 3) {
                response = uploadManager.put(in, null, getUploadToken(),
                        null, null);
                retry++;
            }
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            QiniuProperties qiniu = fileStorageProperties.getQiniu();
            return qiniu.getDomainPrefix()+qiniu.getDomain()+"/"+putRet.key;
        } catch (QiniuException e) {
            log.error("七牛云上传异常，请检查七牛云配置", e.getMessage());
            throw new GrabbyException("七牛云上传异常，请检查七牛云配置");
        }
    }

    private String getUploadToken() {
        return auth.uploadToken(fileStorageProperties.getQiniu().getBucket());
    }


}
