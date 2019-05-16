package com.zzh.grabby.common.controller;

import com.zzh.grabby.common.dto.ResultDto;
import com.zzh.grabby.common.util.ResultUtil;
import com.zzh.grabby.config.filestorage.FileStorageService;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zzh
 * @date 2019/1/10
 */
@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping("/upload")
    public ResultDto upload(@RequestParam("file") MultipartFile file) throws IOException {

        String url = fileStorageService.upload(file.getInputStream());
        //todo 是否需要保存上传记录到mysql
        return ResultUtil.setData(url);
    }

    @RequestMapping(value = "/ueditor",method={RequestMethod.GET, RequestMethod.POST})
    public Object ueditorUpload(@RequestParam String action,
                                   @RequestParam(required = false) MultipartFile file) throws IOException {

        Map<String,Object> map = new HashMap<>();
        if("config".equals(action)){ //获取配置
            String[] imageAllowFiles = {".png", ".jpg", ".jpeg", ".gif", ".bmp"};
            map.put("imageActionName", "upload");
            map.put("imageFieldName","file" );
            map.put("imageMaxSize", "2048000");
            map.put("imageAllowFiles", imageAllowFiles);
            map.put("imageCompressEnable", true);
            map.put("imageCompressBorder", 1600);
            map.put("imageUrlPrefix", "");
        }else{ //上传文件
            String url = fileStorageService.upload(file.getInputStream());
            map.put("state", "SUCCESS");
            map.put("url", url);
            map.put("title", "demo.jpg");
        }

//        String url = fileStorageService.upload(file.getInputStream());
        //todo 是否需要保存上传记录到mysql
        return map;
    }


}
