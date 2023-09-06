package com.rulin.content.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rulin.content.model.dto.CourseCategoryTreeDto;
import com.rulin.content.model.po.CourseCategory;

import java.util.List;

/**
 * <p>
 * 课程分类 Mapper 接口
 * </p>
 *
 * @author 程序儒
 */
public interface CourseCategoryMapper extends BaseMapper<CourseCategory> {

    /**
     * 递归查询课程分类
     *
     * @param id id
     * @return {@code List<CourseCategoryTreeDto>}
     */
    public List<CourseCategoryTreeDto> selectTreeNodes(String id);
}
