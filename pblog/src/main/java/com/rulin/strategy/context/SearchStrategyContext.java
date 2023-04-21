package com.rulin.strategy.context;

import com.rulin.vo.ArticleSearchVO;
import com.rulin.strategy.SearchStrategy;
import com.rulin.vo.QuestionSearchVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author CodeScholar
 * @date 2023年4月9日
 * @apiNote 搜索策略上下文
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SearchStrategyContext {

    private final Map<String, SearchStrategy> searchStrategyMap;

    /**
     * 执行搜索策略
     *
     * @param keywords 关键字
     * @return {@link List< ArticleSearchVO >} 搜索文章
     */
    public List<ArticleSearchVO> executeSearchStrategy(String searchMode, String keywords) {
        return searchStrategyMap.get(searchMode).searchArticle(keywords);
    }

    /**
     * 执行搜索策略
     *
     * @param keywords 关键字
     * @return {@link List< QuestionSearchVO >} 搜索面试题
     */
    public List<QuestionSearchVO> executeQuestionSearchStrategy(String searchMode, String keywords) {
        return searchStrategyMap.get(searchMode).searchQuestion(keywords);
    }

}
