package com.rulin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rulin.common.ResponseResult;
import com.rulin.dto.QuestionDTO;
import com.rulin.entity.Question;

import java.util.List;
import java.util.Map;


/**
 * 博客面试题表 服务类
 *
 * @author 程序儒
 * @date 2023-04-13 17:54:54
 */
public interface QuestionService extends IService<Question> {

    /**
     * 后台分页获取面试题
     *
     * @param map 参数map
     * @return
     */
    ResponseResult listQuestion(Map<String, Object> map);

    /**
     * 后台根据主键获取面试题详情
     *
     * @param id 主键id
     * @return
     */
    ResponseResult getQuestionById(Long id);

    /**
     * 添加面试题
     *
     * @param question 面试题对象
     * @return
     */
    ResponseResult insertQuestion(QuestionDTO question);

    /**
     * 修改面试题
     *
     * @param question 面试题对象
     * @return
     */
    ResponseResult updateQuestion(QuestionDTO question);

    /**
     * 后台根据面试题id删除面试题
     *
     * @param id 面试题id
     * @return
     */
    ResponseResult deleteQuestion(Long id);

    /**
     * 后台批量删除面试题
     *
     * @param ids 面试题id集合
     * @return
     */
    ResponseResult deleteBatchQuestion(List<Long> ids);

    /**
     * 置顶面试题
     *
     * @param id 面试题id
     * @return
     */
    ResponseResult top(Long id);

    /**
     * 发布或下架面试题
     *
     * @param question 面试题对象
     * @return
     */
    ResponseResult publishAndShelf(QuestionDTO question);



    //    ----------web端开始------

    /**
     * 查询面试题分类信息和该分类下的问题列表
     *
     * @param cid cid 分类id
     * @return {@link ResponseResult}
     */
    ResponseResult listWebQuestion(Integer cid);


    /**
     * 搜索面试题
     *
     * @param keywords 关键字
     * @return {@link ResponseResult}
     */
    ResponseResult searchQuestion(String keywords);


    /**
     * 收藏问题
     *
     * @param questionId 问题id
     * @param isFavorite 是最喜欢
     * @return {@link ResponseResult}
     */
    ResponseResult favoriteQuestion(Integer questionId, Integer isFavorite);

    /**
     * 查询收藏的问题列表
     *
     *
     * @return {@link ResponseResult}
     */
    ResponseResult listFavoriteQuestions();
}
