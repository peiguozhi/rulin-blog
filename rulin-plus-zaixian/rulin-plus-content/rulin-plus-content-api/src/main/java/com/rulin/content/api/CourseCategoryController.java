package com.rulin.content.api;


import cn.hutool.core.lang.tree.Tree;
import com.rulin.content.service.CourseCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * 课程类别控制器
 *
 * @author 程序儒
 * @date 2023-08-14 15:11:07
 */
@Slf4j
@RestController
public class CourseCategoryController {

    @Autowired
    private CourseCategoryService courseCategoryService;

    @GetMapping("/course-category/tree-nodes")
    public List<Tree<String>> queryTreeNodes() {
        return courseCategoryService.queryTreeNodes("1");
    }
}
