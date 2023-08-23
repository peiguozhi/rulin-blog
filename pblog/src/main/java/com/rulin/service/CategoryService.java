package com.rulin.service;

import com.rulin.common.ResponseResult;
import com.rulin.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 博客分类表 服务类
 * </p>
 *
 * @author CodeScholar
 * @date 2023年4月9日
 */
public interface CategoryService extends IService<Category> {

    /**
     * 分类列表
     * @param name 分类名
     * @return
     */
    ResponseResult listCategory(String name);

    /**
     * 分类详情
     * @param id 分类id
     * @return
     */
    ResponseResult getCategoryById(Long id);

    /**
     * 添加分类
     * @param category 分类对象
     * @return
     */
    ResponseResult insertCategory(Category category);

    /**
     * 修改分类
     * @param category 分类对象
     * @return
     */
    ResponseResult updateCategory(Category category);

    /**
     * 删除分类
     * @param id 分类id
     * @return
     */
    ResponseResult deleteCategory(Long id);

    /**
     * 批量删除分类
     * @param list 分类对象集合
     * @return
     */
    ResponseResult deleteBatch(List<Category> list);


    /**
     * 置顶分类
     * @return 置顶分类
     */
    ResponseResult top(Long id);


    //-----------web端方法开始------------------
    /**
     * 首页分类列表
     * @return
     */
    ResponseResult webList();


    /**
     * 笔记分类列表
     *
     * @return {@code ResponseResult}
     */
    ResponseResult categoryBookList();
}
