package com.rulin.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rulin.vo.*;
import com.rulin.entity.BlogArticle;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rulin.dto.ArticleDTO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 文章 Mapper 接口
 * @author CodeScholar
 * @date 2023年4月9日
 */
@Repository
public interface ArticleMapper extends BaseMapper<BlogArticle> {

    /**
     * 后台分页获取文章
     * @param page 分页对象
     * @param map 参数map
     * @return
     */
    Page<ArticleListVO> selectArticle(@Param("page") Page<Object> page, @Param("param") Map<String,Object> map);

    /**
     * 后台根据主键获取文章详情
     * @param id 主键id
     * @return
     */
    ArticleDTO selectPrimaryKey(Long id);

    /**
     * 置顶文章
     * @param article 文章对象
     */
    void putTopArticle(@Param("article") ArticleDTO article);

    /**
     * 发布或下架文章
     * @param article 文章对象
     */
    void publishAndShelf(@Param("article") ArticleDTO article);

    /**
     * 文章贡献度
     * @param lastTime 开始时间
     * @param nowTime 结束时间
     * @return
     */
    List<ContributeVO> contribute(@Param("lastTime") String lastTime, @Param("nowTime")String nowTime);

    /**
     * 首页获取推荐文章
     * @param articleId 文章id
     * @return
     */
    List<LatestArticleVO> listRecommendArticles(@Param("articleId") Integer articleId);

    /**
     * 首页获取下一篇或上一篇文章
     * @param id 文章id
     * @param type  类别
     * @param code 发布状态
     * @return
     */
    LatestArticleVO getNextOrLastArticle(@Param("id") Integer id, @Param("type") Integer type, @Param("publish")int code, @Param("categoryId")Long categoryId);

    /**
     * 首页获取最新文章
     * @param id 文章id
     * @param publish 发布状态
     * @return
     */
    List<LatestArticleVO> getNewArticles(@Param("id") Integer id, @Param("publish") int publish);

    /**
     * 首页根据主键获取文章详情
     * @param id 文章id
     * @return
     */
    ArticleInfoVO selectPrimaryKeyById(@Param("id") Integer id);

    /**
     * 首页分页获取文章
     * @param page 分页对象
     * @param publish 发布状态
     * @param categoryId 分类id
     * @param tagId 标签id
     * @return
     */
    Page<ArticlePreviewVO> selectPreviewPage(@Param("page") Page<Object> page, @Param("publish")int publish,
                                             @Param("categoryId") Long categoryId, @Param("tagId") Long tagId,
                                             @Param("orderByDescColumn") String orderByDescColumn);

    /**
     * 首页分页获取归档
     * @param page 分页对象
     * @param publish 发布状态
     * @return
     */
    Page<ArticlePreviewVO> selectArchivePage(@Param("page")Page<Object> page, @Param("publish")int publish);

}
