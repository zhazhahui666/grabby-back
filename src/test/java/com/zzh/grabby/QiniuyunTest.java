package com.zzh.grabby;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.zzh.grabby.config.filestorage.FileStorageProperties;
import com.zzh.grabby.config.filestorage.FileStorageService;
import com.zzh.grabby.config.filestorage.QiniuServiceImpl;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileInputStream;

/**
 * @author zzh
 * @date 2019/1/8
 */
public class QiniuyunTest extends GrabbyApplicationTests {

    String accessKey = "n1y9_Ao5UptCVnJbaFPjEQVleep8RFF5rQTtcZiw";
    String secretKey = "QzAJjmM9uelI8SNg7F9jogYMzfT5ZhhNfFLVjclH";
    String bucket = "grabby";

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private FileStorageProperties fileStorageProperties;

//    @Test
//    public void testUpload() {
//        //构造一个带指定Zone对象的配置类
//        Configuration cfg = new Configuration(Zone.zone0());
//        //...其他参数参考类注释
//        UploadManager uploadManager = new UploadManager(cfg);
//        //...生成上传凭证，然后准备上传
//        //如果是Windows情况下，格式是 D:\\qiniu\\test.png
//        String localFilePath = "/home/qiniu/test.png";
//        //默认不指定key的情况下，以文件内容的hash值作为文件名
//        String key = null;
//        Auth auth = Auth.create(accessKey, secretKey);
//        String upToken = auth.uploadToken(bucket);
//        try {
//            String path = this.getClass().getClassLoader().getResource("image/qrcode.jpg").getPath();
//            File file = new File(path);
//
//            Response response = uploadManager.put(file, key, upToken);
//            //解析上传成功的结果
//            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
//            System.out.println(putRet.key);
//            System.out.println(putRet.hash);
//        } catch (QiniuException ex) {
//            Response r = ex.response;
//            System.err.println(r.toString());
//            try {
//                System.err.println(r.bodyString());
//            } catch (QiniuException ex2) {
//                //ignore
//            }
//        }
//    }


    @Test
    public void testUpload() {
        try {
            String path = this.getClass().getClassLoader().getResource("image/qrcode.jpg").getPath();
            File file = new File(path);
            FileInputStream in = new FileInputStream(file);
            String key = fileStorageService.upload(in);
            String url = fileStorageProperties.getQiniu().getDomainPrefix()
                    + fileStorageProperties.getQiniu().getDomain()
                    + "/" + key;
            System.out.println(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
