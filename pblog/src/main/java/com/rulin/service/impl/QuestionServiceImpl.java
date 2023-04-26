package com.rulin.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rulin.common.FieldConstants;
import com.rulin.common.ResponseResult;
import com.rulin.dto.QuestionDTO;
import com.rulin.entity.QCategory;
import com.rulin.entity.Question;
import com.rulin.entity.SystemConfig;
import com.rulin.enums.SearchModelEnum;
import com.rulin.exception.BusinessException;
import com.rulin.mapper.CommentMapper;
import com.rulin.mapper.QCategoryMapper;
import com.rulin.mapper.QuestionMapper;
import com.rulin.service.QuestionService;
import com.rulin.service.RedisService;
import com.rulin.service.SystemConfigService;
import com.rulin.strategy.context.SearchStrategyContext;
import com.rulin.util.BeanCopyUtils;
import com.rulin.util.DateUtils;
import com.rulin.util.ElasticsearchUtil;
import com.rulin.vo.QuestionListVO;
import com.rulin.vo.QuestionPreviewVO;
import com.rulin.vo.QuestionSearchVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

import static com.rulin.common.FieldConstants.LIMIT_ONE;
import static com.rulin.common.ResultCode.*;


/**
 * 面试题表 服务实现类
 *
 * @author 程序儒
 * @date 2023-04-13 17:01:14
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements QuestionService {

    private final QCategoryMapper qCategoryMapper;

    private final SystemConfigService systemConfigService;

    private final RedisService redisService;

    private final CommentMapper commentMapper;

    private final SearchStrategyContext searchStrategyContext;

    private final RestTemplate restTemplate;

    private final HttpServletRequest request;

    private final ElasticsearchUtil elasticsearchUtil;
    

    /**
     * 后台获取所有面试题
     *
     * @return
     */
    @Override
    public ResponseResult listQuestion(Map<String, Object> map) {
        Page<QuestionListVO> data = baseMapper.selectQuestion(new Page<>((Integer) map.get("pageNo"), (Integer) map.get("pageSize")), map);
        return ResponseResult.success(data);
    }

    /**
     * 后台获取面试题详情
     *
     * @return
     */
    @Override
    public ResponseResult getQuestionById(Long id) {
        QuestionDTO questionDTO = baseMapper.selectPrimaryKey(id);
        return ResponseResult.success(questionDTO);
    }

    /**
     * 添加面试题
     *
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult insertQuestion(QuestionDTO questionDto) {

        Question vo = baseMapper.selectOne(new QueryWrapper<Question>().eq(FieldConstants.QUCONTENT, questionDto.getQuContent()));

        if (ObjectUtil.isNotNull(vo)) {
            throw new BusinessException("该问题已存在!");
        }

        Question question = BeanCopyUtils.copyObject(questionDto, Question.class);
        question.setUserId(StpUtil.getLoginIdAsLong());
        //添加分类
        Long qCategoryId = savaQCategory(questionDto.getQCategoryName());

        question.setQCategoryId(qCategoryId);

        int insert = baseMapper.insert(question);
        return insert > 0 ? ResponseResult.success() : ResponseResult.error("添加面试题失败！");
    }

    /**
     * 修改面试题
     *
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult updateQuestion(QuestionDTO questionDto) {
        Question question = baseMapper.selectById(questionDto.getId());
        // 查询该问题是否还存在
        if (ObjectUtil.isNull(question)) {
            throw new BusinessException(Question_NOT_EXIST.getDesc());
        }

        // 问题内容判重
        Question vo = baseMapper.selectOne(new QueryWrapper<Question>().eq(FieldConstants.QUCONTENT, questionDto.getQuContent()));
        Assert.isTrue(!(vo != null && !vo.getId().equals(questionDto.getId())), Question_IS_EXIST.getDesc());

        //添加分类
        Long qCategoryId = savaQCategory(questionDto.getQCategoryName());

        question = BeanCopyUtils.copyObject(questionDto, Question.class);
        question.setQCategoryId(qCategoryId);
        question.setUserId(StpUtil.getLoginIdAsLong());
        int rows = baseMapper.updateById(question);
        return rows > 0 ? ResponseResult.success() : ResponseResult.error("修改面试题失败！");
    }

    /**
     * 删除面试题
     *
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult deleteQuestion(Long id) {
        int rows = baseMapper.deleteById(id);
/*        if (rows > 0) {
            this.deleteAfter(Collections.singletonList(id));
        }*/
        return rows > 0 ? ResponseResult.success() : ResponseResult.error("删除面试题失败！");
    }

    /**
     * 批量删除面试题
     *
     * @param ids
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult deleteBatchQuestion(List<Long> ids) {
        int rows = baseMapper.deleteBatchIds(ids);
/*        if (rows > 0) {
            this.deleteAfter(ids);
        }*/
        return rows > 0 ? ResponseResult.success() : ResponseResult.error("删除面试题失败！");
    }

    /**
     * 置顶面试题
     *
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult top(Long id) {
        Question question = baseMapper.selectOne(new QueryWrapper<Question>().orderByDesc(FieldConstants.SORT).last(LIMIT_ONE));

        Assert.isTrue(!question.getId().equals(id), CATEGORY_IS_TOP.getDesc());

        Question vo = Question.builder()
                .sort(question.getSort() + 1).updateTime(DateUtils.getNowDate()).id(id).build();
        int rows = baseMapper.updateById(vo);

        return rows > 0 ? ResponseResult.success() : ResponseResult.error("置顶失败");
    }


    /**
     * 发布或下架面试题
     *
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult publishAndShelf(QuestionDTO question) {
        baseMapper.publishAndShelf(question);
        return ResponseResult.success();
    }



    //    -----自定义方法开始-------

    /**
     * 删除面试题后的一些同步删除
     *
     * @param ids
     */
    private void deleteAfter(List<Long> ids) {
        //异步删除es面试题
        elasticsearchUtil.delete(ids);
    }

    /**
     * 如果分类不存在则新增
     *
     * @param qCategoryName 问题类别名称
     * @return {@link Long}
     */
    private Long savaQCategory(String qCategoryName) {
        QCategory qCategory = qCategoryMapper.selectOne(new QueryWrapper<QCategory>().eq(FieldConstants.NAME, qCategoryName));
        if (qCategory == null) {
            qCategory = QCategory.builder().name(qCategoryName).sort(0).build();
            qCategoryMapper.insert(qCategory);
        }
        return qCategory.getId();
    }


    //    ----------web端方法开始-------

    /**
     * 查询面试题分类信息和该分类下的问题列表
     *
     * @param cid cid 分类id
     * @return {@link ResponseResult}
     */
    @Override
    public ResponseResult listWebQuestion(Integer cid) {

        if (ObjectUtil.isNull(cid) || cid == 0){
            cid = 1;
        }

        QuestionPreviewVO data = new QuestionPreviewVO();
        data.setQuestionList(baseMapper.selectPreviewQuestion(cid));
        data.setQCategoryList(qCategoryMapper.selectAll());
        return ResponseResult.success(data);
    }

    /**
     * 搜索面试题
     *
     * @param keywords 关键字
     * @return {@link ResponseResult}
     */
    @Override
    public ResponseResult searchQuestion(String keywords) {
        if (StringUtils.isBlank(keywords)) {
            throw new BusinessException(PARAMS_ILLEGAL.getDesc());
        }
        //获取搜索模式（es搜索或mysql搜索）
        SystemConfig systemConfig = systemConfigService.getCustomizeOne();
        String strategy = SearchModelEnum.getStrategy(systemConfig.getSearchModel());
        //搜索逻辑
        List<QuestionSearchVO> questionSearchVOS = searchStrategyContext.executeQuestionSearchStrategy(strategy, keywords);
        return ResponseResult.success(questionSearchVOS);
    }

    /**
     * 收藏问题
     *
     * @param questionId 问题id
     * @param isFavorite 是否喜欢
     * @return {@link ResponseResult}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult favoriteQuestion(Integer questionId, Integer isFavorite) {
        baseMapper.favoriteQuestion(questionId, isFavorite);
        return ResponseResult.success();
    }

    /**
     * 查询收藏的问题列表
     *
     * @return {@link ResponseResult}
     */
    @Override
    public ResponseResult listFavoriteQuestions() {
        return ResponseResult.success(baseMapper.selectFavoriteQuestions());
    }
}
