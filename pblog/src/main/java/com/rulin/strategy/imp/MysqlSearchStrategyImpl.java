package com.rulin.strategy.imp;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rulin.common.Constants;
import com.rulin.entity.BlogArticle;
import com.rulin.entity.Question;
import com.rulin.mapper.ArticleMapper;
import com.rulin.mapper.QuestionMapper;
import com.rulin.strategy.SearchStrategy;
import com.rulin.util.StringUtils;
import com.rulin.vo.ArticleSearchVO;
import com.rulin.vo.QuestionSearchVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.rulin.enums.PublishEnum.PUBLISH;

@Service("mysqlStrategyImpl")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MysqlSearchStrategyImpl implements SearchStrategy {

    private final ArticleMapper articleMapper;

    private final QuestionMapper questionMapper;

    @Override
    public List<ArticleSearchVO> searchArticle(String keywords){
        // 搜索文章
        List<BlogArticle> blogArticles = articleMapper.selectList(new LambdaQueryWrapper<BlogArticle>()
                .eq(BlogArticle::getIsPublish, PUBLISH.getCode())
                .and(i -> i.like(BlogArticle::getTitle, keywords)
                        .or()
                        .like(BlogArticle::getSummary, keywords)).orderByDesc(BlogArticle::getIsStick,BlogArticle::getCreateTime));
        //高亮处理
        return blogArticles.stream().map(item -> {
            // 获取关键词第一次出现的位置
            String articleSummary = StringUtils.replaceKeywords(item.getSummary(), keywords);
/*            int index = item.getSummary().indexOf(keywords);
            if (index != -1) {
                // 获取关键词前面的文字
                int preIndex = index > 25 ? index - 25 : 0;
                String preText = item.getSummary().substring(preIndex, index);
                // 获取关键词到后面的文字
                int last = index + keywords.length();
                int postLength = item.getSummary().length() - last;
                int postIndex = postLength > 175 ? last + 175 : last + postLength;
                String postText = item.getSummary().substring(index, postIndex);
                // 文章内容高亮
                articleSummary = (preText + postText).replaceAll(keywords, Constants.PRE_TAG + keywords + Constants.POST_TAG);
            }*/
            // 文章标题高亮
            String articleTitle = StringUtils.replaceKeywords(item.getTitle(), keywords);
            return ArticleSearchVO.builder()
                    .id(item.getId())
                    .title(articleTitle)
                    .content(articleSummary)
                    .build();
        }).collect(Collectors.toList());
    }

    @Override
    public List<QuestionSearchVO> searchQuestion(String keywords) {
        // 搜索面试题
        List<Question> question = questionMapper.selectList(new LambdaQueryWrapper<Question>()
                .eq(Question::getIsPublish, PUBLISH.getCode())
                .and(i -> i.like(Question::getQuContent, keywords)).orderByDesc(Question::getSort,Question::getCreateTime));
        //高亮处理
        return question.stream().map(item -> {
            String quContent = item.getQuContent();
            // 文章标题高亮
            Matcher matcher = Pattern.compile(keywords, Pattern.CASE_INSENSITIVE).matcher(quContent);
            // 查找匹配的内容
            while (matcher.find()) {
                // 打印找到的内容
                quContent = quContent.replaceAll("(?i)"+matcher.group(), Constants.PRE_TAG + matcher.group() + Constants.POST_TAG);
            }

            return QuestionSearchVO.builder()
                    .id(item.getId())
                    .quContent(quContent)
                    .analysis(StringUtils.removeLabel(item.getAnalysis()))
                    .qCategoryId(item.getQCategoryId())
                    .build();
        }).collect(Collectors.toList());
    }
}
