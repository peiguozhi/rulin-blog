package com.rulin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rulin.dto.QuestionDTO;
import com.rulin.entity.Question;
import com.rulin.vo.QuestionListVO;
import com.rulin.vo.QuestionVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


/**
 * 面试题 Mapper 接口
 *
 * @author 程序儒
 * @date 2023-04-13 17:09:20
 */
@Repository
public interface QuestionMapper extends BaseMapper<Question> {

    /**
     * 后台分页获取面试题
     * @param page 分页对象
     * @param map 参数map
     * @return
     */
    Page<QuestionListVO> selectQuestion(@Param("page") Page<Object> page, @Param("param") Map<String,Object> map);

    /**
     * 后台根据主键获取面试题详情
     * @param id 主键id
     * @return
     */
    QuestionDTO selectPrimaryKey(Long id);

    /**
     * 发布或下架面试题
     * @param question 面试题对象
     */
    void publishAndShelf(@Param("question") QuestionDTO question);


    /**
     * 根据分类id查询问题列表
     *
     * @param cid cid
     * @return {@link List}<{@link QuestionVO}>
     */
    List<QuestionVO> selectPreviewQuestion(Integer cid);
}
