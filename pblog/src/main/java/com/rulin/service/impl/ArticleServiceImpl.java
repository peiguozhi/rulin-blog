package com.rulin.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rulin.common.FieldConstants;
import com.rulin.common.RedisConstants;
import com.rulin.common.ResponseResult;
import com.rulin.dto.ArticleDTO;
import com.rulin.entity.*;
import com.rulin.enums.SearchModelEnum;
import com.rulin.enums.YesOrNoEnum;
import com.rulin.exception.BusinessException;
import com.rulin.mapper.ArticleMapper;
import com.rulin.mapper.CategoryMapper;
import com.rulin.mapper.CommentMapper;
import com.rulin.mapper.TagsMapper;
import com.rulin.service.ArticleService;
import com.rulin.service.RedisService;
import com.rulin.service.SystemConfigService;
import com.rulin.strategy.context.SearchStrategyContext;
import com.rulin.util.BeanCopyUtils;
import com.rulin.util.ElasticsearchUtil;
import com.rulin.util.IpUtils;
import com.rulin.util.PageUtils;
import com.rulin.vo.*;
import com.vladsch.flexmark.html2md.converter.FlexmarkHtmlConverter;
import com.vladsch.flexmark.util.data.MutableDataSet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.rulin.common.Constants.*;
import static com.rulin.common.RedisConstants.*;
import static com.rulin.common.ResultCode.*;
import static com.rulin.enums.PublishEnum.PUBLISH;

/**
 * <p>
 * 博客文章表 服务实现类
 * </p>
 *
 * @author CodeScholar
 * @date 2023年4月9日
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, BlogArticle> implements ArticleService {

    private final CategoryMapper categoryMapper;

    private final SystemConfigService systemConfigService;

    private final RedisService redisService;

    private final TagsMapper tagsMapper;

    private final CommentMapper commentMapper;

    private final SearchStrategyContext searchStrategyContext;

    private final RestTemplate restTemplate;

    private final HttpServletRequest request;

    private final ElasticsearchUtil elasticsearchUtil;

    private final ArticleMapper articleMapper;

    @Value("${baidu.url}")
    private String baiduUrl;


    /**
     * 后台获取所有文章
     *
     * @return
     */
    @Override
    public ResponseResult listArticle(Map<String, Object> map) {
        Page<ArticleListVO> data = baseMapper.selectArticle(new Page<>((Integer) map.get("pageNo"), (Integer) map.get("pageSize")), map);
        return ResponseResult.success(data);
    }

    /**
     * 后台获取文章详情
     *
     * @return
     */
    @Override
    public ResponseResult getArticleById(Long id) {
        ArticleDTO articleDTO = baseMapper.selectPrimaryKey(id);
        articleDTO.setTags(tagsMapper.selectByArticleId(id));
        return ResponseResult.success(articleDTO);
    }

    /**
     * 添加文章
     *
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult insertArticle(ArticleDTO article) {
        BlogArticle blogArticle = BeanCopyUtils.copyObject(article, BlogArticle.class);
        blogArticle.setUserId(StpUtil.getLoginIdAsLong());
        //添加分类
        Long categoryId = savaCategory(article.getCategoryName());
        //添加标签
        List<Long> tagList = getTagsList(article);

        blogArticle.setCategoryId(categoryId);

        int insert = baseMapper.insert(blogArticle);
        if (insert > 0) {
            tagsMapper.saveArticleTags(blogArticle.getId(), tagList);
        }
        return ResponseResult.success();
    }

    /**
     * 修改文章
     *
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult updateArticle(ArticleDTO article) {
        BlogArticle blogArticle = baseMapper.selectById(article.getId());
        if (ObjectUtil.isNull(blogArticle)) {
            throw new BusinessException(ARTICLE_NOT_EXIST.getDesc());
        }

        //添加分类
        Long categoryId = savaCategory(article.getCategoryName());
        //添加标签
        List<Long> tagList = getTagsList(article);

        blogArticle = BeanCopyUtils.copyObject(article, BlogArticle.class);
        blogArticle.setCategoryId(categoryId);
        blogArticle.setUserId(StpUtil.getLoginIdAsLong());
        baseMapper.updateById(blogArticle);

        //先删出所有标签
        tagsMapper.deleteByArticleIds(Collections.singletonList(blogArticle.getId()));
        //然后新增标签
        tagsMapper.saveArticleTags(blogArticle.getId(), tagList);

        return ResponseResult.success();
    }

    /**
     * 删除文章
     *
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult deleteArticle(Long id) {
        int row = baseMapper.deleteById(id);
        if (row > 0) {
            this.deleteAfter(Collections.singletonList(id));
        }
        return ResponseResult.success();
    }

    /**
     * 批量删除文章
     *
     * @param ids
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult deleteBatchArticle(List<Long> ids) {
        int rows = baseMapper.deleteBatchIds(ids);
        if (rows > 0) {
            this.deleteAfter(ids);
        }
        return ResponseResult.success();
    }

    /**
     * 置顶文章
     *
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult putTopArticle(ArticleDTO article) {
        baseMapper.putTopArticle(article);
        return ResponseResult.success();
    }


    /**
     * 文章百度推送
     *
     * @return
     */
    @Override
    public ResponseResult articleSeo(List<Long> ids) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Host", "data.zz.baidu.com");
        headers.add("User-Agent", "curl/7.12.1");
        headers.add("Content-Length", "83");
        headers.add("Content-Type", "text/plain");

        ids.forEach(item -> {
            String url = "http://www.codescholar.cn/article/" + item;
            HttpEntity<String> entity = new HttpEntity<>(url, headers);
            restTemplate.postForObject(baiduUrl, entity, String.class);
        });
        return ResponseResult.success();
    }

    /**
     * 抓取文章
     *
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult reptile(String url) {
        try {
            Document document = Jsoup.connect(url).get();
            Elements title = document.getElementsByClass("title-article");
            Elements tags = document.getElementsByClass("tag-link");
            Elements content = document.getElementsByClass("article_content");
            if (StringUtils.isBlank(content.toString())) {
                throw new BusinessException(CRAWLING_ARTICLE_FAILED.getDesc());
            }

            //爬取的是HTML内容，需要转成MD格式的内容
            String newContent = content.get(0).toString().replaceAll("<code>", "<code class=\"lang-java\">");
            MutableDataSet options = new MutableDataSet();
            String markdown = FlexmarkHtmlConverter.builder(options).build().convert(newContent)
                    .replace("lang-java", "java");

            //文章封面图片 由https://api.btstu.cn/该网站随机获取
            String strResult = restTemplate.getForObject(IMG_URL_API, String.class);
            JSONObject jsonObject = JSON.parseObject(strResult);
            Object imgUrl = jsonObject.get("imgurl");

            BlogArticle entity = BlogArticle.builder().userId(7L).contentMd(markdown)
                    .categoryId(OTHER_CATEGORY_ID).isOriginal(YesOrNoEnum.NO.getCode()).originalUrl(url)
                    .title(title.get(0).text()).avatar(imgUrl.toString()).content(newContent).build();

            baseMapper.insert(entity);
            //为该文章添加标签
            List<Long> tagsId = new ArrayList<>();
            tags.forEach(item -> {
                String tag = item.text();
                Tags result = tagsMapper.selectOne(new QueryWrapper<Tags>().eq(FieldConstants.NAME, tag));
                if (result == null) {
                    result = Tags.builder().name(tag).build();
                    tagsMapper.insert(result);
                }
                tagsId.add(result.getId());
            });
            tagsMapper.saveArticleTags(entity.getId(), tagsId);

            log.info("文章抓取成功，内容为:{}", JSON.toJSONString(entity));
        } catch (IOException e) {
            throw new BusinessException(e);
        }
        return ResponseResult.success();
    }

    /**
     * 发布或下架文章
     *
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult publishAndShelf(ArticleDTO article) {
        baseMapper.publishAndShelf(article);
        return ResponseResult.success();
    }

    @Override
    public ResponseResult randomImg() {
        //文章封面图片 由https://api.btstu.cn/该网站随机获取
        String result = restTemplate.getForObject(IMG_URL_API, String.class);
        Object imgUrl = JSON.parseObject(result).get("imgurl");
        return ResponseResult.success(imgUrl);
    }

    //    ----------web端方法开始-------

    /**
     * 获取文章列表
     *
     * @return
     */
    @Override
    public ResponseResult listWebArticle(Long categoryId,Long tagId,String orderByDescColumn) {
        Page<ArticlePreviewVO> articlePreviewDTOPage = baseMapper.selectPreviewPage(new Page<>(PageUtils.getPageNo(), PageUtils.getPageSize()), PUBLISH.code, categoryId, tagId, orderByDescColumn);
        articlePreviewDTOPage.getRecords().forEach(item -> item.setTagVOList(tagsMapper.findByArticleIdToTags(item.getId())));
        return ResponseResult.success(articlePreviewDTOPage);
    }

    /**
     * 获取文章详情
     *
     * @return
     */
    @Override
    public ResponseResult webArticleInfo(Integer id) {
        ArticleInfoVO blogArticle = baseMapper.selectPrimaryKeyById(id);
        //标签
        List<TagVO> tags = tagsMapper.findByArticleIdToTags(blogArticle.getId());
        blogArticle.setTagList(tags);
        //分类
        Category category = categoryMapper.selectOne(new LambdaQueryWrapper<Category>()
                .select(Category::getId, Category::getName)
                .eq(Category::getId, blogArticle.getCategoryId()));
        Long categoryId = category.getId();
        // 查看是否为笔记类系列文章
        if (categoryMapper.isBookCaregory(categoryId) > 0) {
            blogArticle.setArticleBookList(
                    articleMapper
                            .selectPreviewPage(new Page<>(1, 1000), PUBLISH.code, category.getId(),
                                    null, "id").getRecords()
                            .stream().map(item -> BeanUtil.copyProperties(item, ArticleBookVO.class))
                            .collect(Collectors.toList())
                    );
        } else {
            categoryId = null;
        }

        blogArticle.setCategory(category);
        //评论
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<Comment>()
                .eq(FieldConstants.ARTICLE_ID, blogArticle.getId()).orderByDesc(FieldConstants.CREATE_TIME);
        List<Comment> list = commentMapper.selectList(queryWrapper);
        blogArticle.setComments(list);

        //最新文章
        List<LatestArticleVO> blogArticles = baseMapper.getNewArticles(id, PUBLISH.code);
        blogArticle.setNewestArticleList(blogArticles);

        // 查询上一篇下一篇文章
        LatestArticleVO lastArticle = baseMapper.getNextOrLastArticle(id, 0, PUBLISH.code, categoryId);
        blogArticle.setLastArticle(lastArticle);
        LatestArticleVO nextArticle = baseMapper.getNextOrLastArticle(id, 1, PUBLISH.code, categoryId);
        blogArticle.setNextArticle(nextArticle);

        //相关推荐 2023年9月8日 取消文章推荐
/*        List<LatestArticleVO> recommendArticleList = baseMapper.listRecommendArticles(id);
        blogArticle.setRecommendArticleList(recommendArticleList);*/

        // 封装点赞量和浏览量
        blogArticle.setLikeCount((Integer) redisService.hGet(RedisConstants.ARTICLE_LIKE_COUNT, blogArticle.getId().toString()));

        //校验私密文章是否已经进行过验证
        if (blogArticle.getIsSecret().equals(YesOrNoEnum.YES.getCode())) {
            List<Object> cacheList = redisService.getCacheList(RedisConstants.CHECK_CODE_IP);
            String ip = IpUtils.getIp(request);
            if (cacheList.contains(ip)) {
                blogArticle.setIsSecret(YesOrNoEnum.NO.getCode());
            }
        }

        //增加文章阅读量
        redisService.incrArticle(id.longValue(), ARTICLE_READING);
        return ResponseResult.success(blogArticle);
    }

    /**
     * 获取分类or标签文章
     *
     * @return
     */
    @Override
    public ResponseResult condition(Long categoryId, Long tagId, Integer pageSize) {
        Map<String, Object> result = new HashMap<>();
        Page<ArticlePreviewVO> blogArticlePage = baseMapper.selectPreviewPage(new Page<>(PageUtils.getPageNo(), pageSize), PUBLISH.getCode(), categoryId, tagId, null);
        blogArticlePage.getRecords().forEach(item -> {
            List<TagVO> tagList = tagsMapper.findByArticleIdToTags(item.getId());
            item.setTagVOList(tagList);
        });
        String name;
        if (categoryId != null) {
            name = categoryMapper.selectById(categoryId).getName();
        } else {
            name = tagsMapper.selectById(tagId).getName();
            redisService.incrArticle(tagId, TAG_CLICK_VOLUME);
        }
        result.put(FieldConstants.NAME, name);
        result.put(RECORDS, blogArticlePage.getRecords());
        return ResponseResult.success(result);
    }

    /**
     * 获取归档
     *
     * @return
     */
    @Override
    public ResponseResult archive() {
        Page<ArticlePreviewVO> articlePage = baseMapper.selectArchivePage(new Page<>(PageUtils.getPageNo(), PageUtils.getPageSize()), PUBLISH.code);
        return ResponseResult.success(articlePage);
    }

    /**
     * 搜索文章
     *
     * @return
     */
    @Override
    public ResponseResult searchArticle(String keywords) {
        if (StringUtils.isBlank(keywords)) {
            throw new BusinessException(PARAMS_ILLEGAL.getDesc());
        }
        //获取搜索模式（es搜索或mysql搜索）
        SystemConfig systemConfig = systemConfigService.getCustomizeOne();
        String strategy = SearchModelEnum.getStrategy(systemConfig.getSearchModel());
        //搜索逻辑
        List<ArticleSearchVO> articleSearchVOS = searchStrategyContext.executeSearchStrategy(strategy, keywords);
        return ResponseResult.success(articleSearchVOS);
    }

    /**
     * 文章点赞
     *
     * @param articleId
     * @return
     */
    @Override
    public ResponseResult articleLike(Integer articleId) {
        // 判断是否点赞
        String articleLikeKey = ARTICLE_USER_LIKE + StpUtil.getLoginId();
        if (redisService.sIsMember(articleLikeKey, articleId)) {
            // 点过赞则删除文章id
            redisService.sRemove(articleLikeKey, articleId);
            // 文章点赞量-1
            redisService.hDecr(ARTICLE_LIKE_COUNT, articleId.toString(), 1L);
        } else {
            // 未点赞则增加文章id
            redisService.sAdd(articleLikeKey, articleId);
            // 文章点赞量+1
            redisService.hIncr(ARTICLE_LIKE_COUNT, articleId.toString(), 1L);
        }
        return ResponseResult.success();
    }

    /**
     * 校验文章验证码(验证码通过关注公众号获取)
     *
     * @return
     */
    @Override
    public ResponseResult checkSecret(String code) {
        //校验验证码
        String key = RedisConstants.WECHAT_CODE + code;
        Object redisCode = redisService.getCacheObject(key);
        if (ObjectUtil.isNull(redisCode)) {
            throw new BusinessException(ERROR_EXCEPTION_MOBILE_CODE.getDesc());
        }

        //将ip存在redis 有效期一天，当天无需再进行验证码校验
        List<Object> cacheList = redisService.getCacheList(CHECK_CODE_IP);
        if (cacheList.isEmpty()) {
            cacheList = new ArrayList<>();
        }
        cacheList.add(IpUtils.getIp(request));
        redisService.setCacheList(CHECK_CODE_IP, cacheList);
        //通过删除验证码
        redisService.deleteObject(key);
        return ResponseResult.success("验证成功");
    }


    //    -----自定义方法开始-------

    /**
     * 删除文章后的一些同步删除
     *
     * @param ids
     */
    private void deleteAfter(List<Long> ids) {
        tagsMapper.deleteByArticleIds(ids);
        //异步删除es文章
        elasticsearchUtil.delete(ids);
    }

    /**
     * 将数据库不存在的标签新增
     *
     * @param article
     * @return
     */
    private List<Long> getTagsList(ArticleDTO article) {
        List<Long> tagList = new ArrayList<>();
        article.getTags().forEach(item -> {
            Tags tags = tagsMapper.selectOne(new QueryWrapper<Tags>().eq(FieldConstants.NAME, item));
            if (tags == null) {
                tags = Tags.builder().name(item).sort(0).build();
                tagsMapper.insert(tags);
            }
            tagList.add(tags.getId());
        });
        return tagList;
    }

    /**
     * 如果分类不存在则新增
     *
     * @param categoryName
     * @return
     */
    private Long savaCategory(String categoryName) {
        Category category = categoryMapper.selectOne(new QueryWrapper<Category>().eq(FieldConstants.NAME, categoryName));
        if (category == null) {
            category = Category.builder().name(categoryName).sort(0).build();
            categoryMapper.insert(category);
        }
        return category.getId();
    }
}
