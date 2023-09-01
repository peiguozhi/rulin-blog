package com.rulin.content.model.dto;

import com.rulin.content.model.po.CourseCategory;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


/**
 * 课程分类树型结点dto
 *
 * @author 程序儒
 * @date 2023-08-14 15:09:44
 */
@Data
public class CourseCategoryTreeDto extends CourseCategory implements Serializable {

    /**
     * 子节点
     */
    private List<CourseCategoryTreeDto> childrenTreeNodes;
}

