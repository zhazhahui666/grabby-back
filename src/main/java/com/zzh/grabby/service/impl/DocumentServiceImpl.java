package com.zzh.grabby.service.impl;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.zzh.grabby.entity.Department;
import com.zzh.grabby.entity.Document;
import com.zzh.grabby.entity.DocumentDirectory;
import com.zzh.grabby.mapper.DocumentDirectoryMapper;
import com.zzh.grabby.mapper.DocumentMapper;
import com.zzh.grabby.service.DocumentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zzh
 * @since 2019-01-19
 */
@Service
public class DocumentServiceImpl extends ServiceImpl<DocumentMapper, Document> implements DocumentService {

    @Autowired
    private DocumentMapper documentMapper;

    @Autowired
    private DocumentDirectoryMapper documentDirectoryMapper;

    @Override
    public List<DocumentDirectory> getDirectoryTree() {
        List<DocumentDirectory> directoryList = documentDirectoryMapper.selectList(null);
        Multimap<Integer, DocumentDirectory> dirIdMap = ArrayListMultimap.create();
        directoryList.forEach(dir->dirIdMap.put(dir.getPid(), dir));

        List<DocumentDirectory> rootList = Lists.newArrayList();

        directoryList.forEach(dir->{
            if (dir.getPid() == null || dir.getPid().equals(0)) {
                rootList.add(dir);
            }

            //差排序
            List<DocumentDirectory> deps = (List<DocumentDirectory>) dirIdMap.get(dir.getId());
            dir.setChildren(deps);
        });


        return rootList;
    }
}
