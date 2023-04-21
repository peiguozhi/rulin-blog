package com.rulin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rulin.common.ResponseResult;
import com.rulin.entity.QCategory;

import java.util.List;

/**
 * <p>
 * 博客面试题分类表 服务类
 * </p>
 *
 * @author 程序儒
 * @date 2023年4月13日 14点55分
 */
public interface QCategoryService extends IService<QCategory> {

    /**
     * 面试题分类列表
     * @param name 面试题分类名
     * @return
     */
    ResponseResult listQCategory(String name);

    /**
     * 面试题分类详情
     * @param id 面试题分类id
     * @return
     */
    ResponseResult getQCategoryById(Long id);

    /**
     * 添加面试题分类
     * @param qCategory 面试题分类对象
     * @return
     */
    ResponseResult insertQCategory(QCategory qCategory);

    /**
     * 修改面试题分类
     * @param qCategory 面试题分类对象
     * @return
     */
    ResponseResult updateQCategory(QCategory qCategory);

    /**
     * 删除面试题分类
     * @param id 面试题分类id
     * @return
     */
    ResponseResult deleteQCategory(Long id);

    /**
     * 批量删除面试题分类
     * @param list 面试题分类对象集合
     * @return
     */
    ResponseResult deleteBatch(List<QCategory> list);


    /**
     * 置顶面试题分类
     * @return 置顶面试题分类
     */
    ResponseResult top(Long id);


    //-----------web端方法开始------------------
    /**
     * 首页面试题分类列表
     * @return
     */
    ResponseResult webList();

}
