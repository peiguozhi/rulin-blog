package com.rulin.content.service;

import cn.hutool.core.lang.tree.Tree;

import java.util.List;

public interface CourseCategoryService {

    /**
     * 课程分类树形结构查询  (使用hutool工具类的TreeUtil实现)
     *
     * @param id id
     * @return {@code List<Tree<String>>}
     */
    public List<Tree<String>> queryTreeNodes(String id);
}
