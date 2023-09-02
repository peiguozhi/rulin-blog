package com.rulin.content.model.dto;

import com.rulin.content.model.po.Teachplan;
import com.rulin.content.model.po.TeachplanMedia;
import lombok.Data;
import lombok.ToString;

import java.util.List;


/**
 * @apiNote 课程计划树型结构dto
 * @author 程序儒
 * @date 2023-08-16 17:16:12
 */
@Data
@ToString
public class TeachplanDto extends Teachplan {

  //课程计划关联的媒资信息
  TeachplanMedia teachplanMedia;

  //子结点
  List<TeachplanDto> teachPlanTreeNodes;

}
