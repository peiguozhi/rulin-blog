package com.rulin.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 分类下面试题数量统计
 *
 * @author 程序儒
 * @date 2023-04-13 15:06:31
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QCategoryCountVO {
    private Integer value;
    private String name;
}
