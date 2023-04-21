package com.rulin.strategy;

import com.rulin.vo.ArticleSearchVO;
import com.rulin.vo.QuestionSearchVO;

import java.util.List;

/**
 * @author CodeScholar
 * @date 2023年4月9日
 * @apiNote 搜索策略
 */
public interface SearchStrategy {
    /**
     * 搜索文章
     *
     * @param keywords 关键字
     * @return {@link List< ArticleSearchVO >} 文章列表
     */
    List<ArticleSearchVO> searchArticle(String keywords);

    List<QuestionSearchVO> searchQuestion(String keywords);

}
