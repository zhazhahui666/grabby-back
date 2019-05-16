package com.zzh.grabby.service;

import com.zzh.grabby.entity.Document;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zzh.grabby.entity.DocumentDirectory;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zzh
 * @since 2019-01-19
 */
public interface DocumentService extends IService<Document> {

    List<DocumentDirectory> getDirectoryTree();
}
