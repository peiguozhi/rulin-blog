package com.rulin.content.service;

import com.rulin.base.model.PageParams;
import com.rulin.base.model.PageResult;
import com.rulin.content.model.dto.AddCourseDto;
import com.rulin.content.model.dto.CourseBaseInfoDto;
import com.rulin.content.model.dto.EditCourseDto;
import com.rulin.content.model.dto.QueryCourseParamsDto;
import com.rulin.content.model.po.CourseBase;

/**
 * @author 程序儒
 * @version 1.0.0
 * @date 2023/8/14
 * @apiNote 课程基本信息管理业务接口
 */
public interface CourseBaseInfoService {

    /**
     * 课程查询接口
     *
     * @param pageParams           分页参数
     * @param queryCourseParamsDto 条件条件
     * @return {@code PageResult<CourseBase>}
     */
    PageResult<CourseBase> queryCourseBaseList(PageParams pageParams, QueryCourseParamsDto queryCourseParamsDto);

    /**
     * 添加课程基本信息
     *
     * @param companyId    教学机构id
     * @param addCourseDto 课程基本信息
     * @return {@code CourseBaseInfoDto}
     */
    CourseBaseInfoDto createCourseBase(Long companyId, AddCourseDto addCourseDto);

    /**
     * 根据id查询课程基本信息
     *
     * @param courseId 进程id
     * @return {@code CourseBaseInfoDto}
     */
    CourseBaseInfoDto getCourseBaseInfo(long courseId);

    /**
     * 修改课程信息
     *
     * @param companyId 机构id
     * @param dto       dto
     * @return {@code CourseBaseInfoDto}
     */
    CourseBaseInfoDto updateCourseBase(Long companyId, EditCourseDto dto);

    void delectCourse(Long companyId, Long courseId);
}
