package com.rulin.content.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rulin.content.model.dto.TeachplanDto;
import com.rulin.content.model.po.Teachplan;

import java.util.List;

/**
 * <p>
 * 课程计划 Mapper 接口
 * </p>
 *
 * @author 程序儒
 */
public interface TeachplanMapper extends BaseMapper<Teachplan> {
    /**
     * 查询某课程的课程计划，组成树型结构
     *
     * @param courseId 进程id
     * @return {@code List<TeachplanDto>}
     */
    public List<TeachplanDto> selectTreeNodes(long courseId);
}
