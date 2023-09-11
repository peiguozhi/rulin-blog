package com.rulin.content.service;

import com.rulin.content.model.dto.SaveTeachplanDto;
import com.rulin.content.model.dto.TeachplanDto;

import java.util.List;


/**
 * 课程基本信息管理业务接口
 *
 * @author 程序儒
 * @date 2023-08-16 18:17:10
 */
public interface TeachplanService {


    /**
     * 查询课程计划树型结构
     *
     * @param courseId 进程id
     * @return {@code List<TeachplanDto>}
     */
    public List<TeachplanDto> findTeachplanTree(long courseId);

    /**
     * 保存teachplan
     *
     * @param teachplanDto teachplan dto
     */
    public void saveTeachplan(SaveTeachplanDto teachplanDto);

    void deleteTeachplan(Long teachplanId);

    void orderByTeachplan(String moveType, Long teachplanId);

}
