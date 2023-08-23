package com.rulin.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rulin.vo.CategoryBookVO;
import com.rulin.vo.CategoryCountVO;
import com.rulin.vo.CategoryVO;
import com.rulin.entity.Category;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 博客分类表 Mapper 接口
 * </p>
 *
 * @author CodeScholar
 * @date 2023年4月9日
 */
@Repository
public interface CategoryMapper extends BaseMapper<Category> {

    /**
     * 分页获取分类
     *
     * @param objectPage 分页对象
     * @param name       分类名
     * @return
     */
    Page<Category> selectCategory(@Param("page") Page<Category> objectPage, @Param("name") String name);

    /**
     * 统计分类
     *
     * @return
     */
    List<CategoryCountVO> countArticle();

    /**
     * 首页获取所有分类
     *
     * @return
     */
    List<CategoryVO> selectAll();

    /**
     * 首页获取所有笔记分类
     *
     * @return {@code List<CategoryBookVO>}
     */
    List<CategoryBookVO> selectBookList();


    /**
     * 查询是否为笔记分类
     *
     * @param categoryId 类别id
     * @return int
     */
    int isBookCaregory(@Param("categoryId") Long categoryId);
}
