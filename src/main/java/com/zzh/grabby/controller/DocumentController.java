package com.zzh.grabby.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzh.grabby.common.dto.PageDto;
import com.zzh.grabby.common.dto.ResultDto;
import com.zzh.grabby.common.util.ResultUtil;
import com.zzh.grabby.entity.Document;
import com.zzh.grabby.entity.DocumentDirectory;
import com.zzh.grabby.entity.UserVo;
import com.zzh.grabby.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zzh
 * @since 2019-01-19
 */
@RestController
@RequestMapping("/document")
@Validated
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @GetMapping("/get-directory-tree")
    public ResultDto getDirectoryTree() {
        List<DocumentDirectory> list = documentService.getDirectoryTree();
        return ResultUtil.setData(list);
    }

    /**
     * 文档分页数据
     *
     * @param dirId
     * @param pageDto
     * @return
     */
    @GetMapping("/get-page")
    public ResultDto getPage(@RequestParam Integer dirId,
                             @Valid @ModelAttribute PageDto pageDto) {
        Page<Document> page = new Page<>(pageDto.getPageNumber(), pageDto.getPageSize());

        QueryWrapper<Document> wrapper = new QueryWrapper<>();
        if (dirId != null) {
            wrapper.eq("directory_id", dirId);
        }
        IPage<Document> pageData = documentService.page(page, wrapper);
        return ResultUtil.setData(pageData);
    }

    /**
     * 获取文档详情
     *
     * @param documentId
     * @return
     */
    @GetMapping("/get-detail/{documentId}")
    public ResultDto getDetail(@PathVariable Integer documentId) {
        return ResultUtil.setData(documentService.getById(documentId));
    }


    @PostMapping("/save")
    public ResultDto add(@RequestParam Integer id,
                         @NotBlank(message = "标题不能为空") String title,
                         @NotBlank(message = "正文不能为空") String content,
                         @NotNull(message = "目录不能为空") Integer directoryId,
                         @NotNull boolean isNormal) {
        Document document = new Document();
        if(id != null){ //更新
            document = documentService.getById(id);
        }
        document.setTitle(title);
        document.setContent(content);
        document.setDirectoryId(directoryId);
        if (isNormal) {
            document.setState(Document.STATE_NORMAL);
        } else {
            document.setState(Document.STATE_DRAFT);
        }

        documentService.saveOrUpdate(document);
        return ResultUtil.setSuccess();
    }


}
