package com.rulin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rulin.entity.QCategory;
import com.rulin.vo.QCategoryCountVO;
import com.rulin.vo.QCategoryVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 博客面试题分类表 Mapper 接口
 * </p>
 *
 * @author 程序儒
 * @date 2023年4月13日 15点01分
 */
@Repository
public interface QCategoryMapper extends BaseMapper<QCategory> {

    /**
     * 分页获取面试题分类
     *
     * @param objectPage 分页对象
     * @param name       面试题分类名
     * @return
     */
    Page<QCategory> selectQCategory(@Param("page") Page<QCategory> objectPage, @Param("name") String name);

    /**
     * 统计面试题分类
     *
     * @return
     */
    List<QCategoryCountVO> countQuestion();

    /**
     * 首页获取所有面试题分类
     *
     * @return
     */
    List<QCategoryVO> selectAll();

}
