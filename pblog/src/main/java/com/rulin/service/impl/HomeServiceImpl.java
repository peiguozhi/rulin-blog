package com.rulin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rulin.common.*;
import com.rulin.service.RedisService;
import com.rulin.vo.CategoryCountVO;
import com.rulin.vo.HomeDataVO;
import com.rulin.entity.*;
import com.rulin.mapper.*;
import com.rulin.service.SystemConfigService;
import com.rulin.service.WebConfigService;
import com.rulin.util.DateUtils;
import com.rulin.util.IpUtils;
import com.rulin.vo.ContributeVO;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

import static com.rulin.common.FieldConstants.LIMIT_ONE;
import static com.rulin.enums.PublishEnum.PUBLISH;

@Service
@RequiredArgsConstructor
public class HomeServiceImpl {

    private final ArticleMapper articleMapper;

    private final MessageMapper messageMapper;

    private final TagsMapper tagsMapper;

    private final CategoryMapper categoryMapper;

    private final UserLogMapper sysLogMapper;

    private final SystemConfigService systemConfigService;

    private final WebConfigService webConfigService;

    private final RedisService redisService;

    private final PageMapper pageMapper;

    private final UserMapper userMapper;


    /**
     * 文章、留言、用户、ip统计
     * @return
     */
    public Map<String,Integer> lineCount(){
        Map<String,Integer> map = new HashMap<>();
        map.put("article", articleMapper.selectList(null).size());
        map.put("message",messageMapper.selectList(null).size());
        map.put("user",userMapper.selectCount(null));
        map.put("viewsCount",(Integer) getViewsCount());
        return map;
    }

    public HomeDataVO init() {
        //文章排行
        List<BlogArticle> blogArticles = articleMapper.selectList(new LambdaQueryWrapper<BlogArticle>()
                .select(BlogArticle::getQuantity,BlogArticle::getTitle,BlogArticle::getId)
                .eq(BlogArticle::getIsPublish, PUBLISH.getCode())
                .orderByDesc(BlogArticle::getQuantity).last("limit 6"));
        //文章贡献度
        Map<String, Object> contribute = this.contribute();
        //分类统计
        Map<String, Object> categoryCount = this.categoryCount();
        //用户访问量
        List<Map<String, Object>> userAccess = this.userAccess();

        List<Map<String,Object>> tagsList = tagsMapper.countTags();
        //弹出框
        SystemConfig systemConfig = systemConfigService.getCustomizeOne();

        HomeDataVO dto = HomeDataVO.builder().dashboard(systemConfig.getDashboardNotification())
                .categoryList(categoryCount).contribute(contribute).blogArticles(blogArticles).userAccess(userAccess).tagsList(tagsList).build();
        return dto;
    }

    /**
     * redis监控
     * @return
     */
    public ResponseResult getCacheInfo() {
        RedisTemplate redisTemplate = redisService.getRedisTemplate();
        Properties info = (Properties) redisTemplate.execute((RedisCallback<Object>) connection -> connection.info());
        Properties commandStats = (Properties) redisTemplate.execute((RedisCallback<Object>) connection -> connection.info("commandstats"));
        Object dbSize = redisTemplate.execute((RedisCallback<Object>) connection -> connection.dbSize());

        Map<String, Object> result = new HashMap<>(3);
        result.put("info", info);
        result.put("dbSize", dbSize);

        List<Map<String, String>> pieList = new ArrayList<>();
        commandStats.stringPropertyNames().forEach(key -> {
            Map<String, String> data = new HashMap<>(2);
            String property = commandStats.getProperty(key);
            data.put("name", StringUtils.removeStart(key, "cmdstat_"));
            data.put("value", StringUtils.substringBetween(property, "calls=", ",usec"));
            pieList.add(data);
        });
        result.put("commandStats", pieList);
        return ResponseResult.success(result);
    }

    //----------web端开始------------
    /**
     * 获取站点信息
     * @return
     */
    public ResponseResult webSiteInfo() {
        //网站信息
        WebConfig webConfig = webConfigService.getOne(new LambdaQueryWrapper<WebConfig>()
                .select(WebConfig::getAuthorAvatar,WebConfig::getIsMusicPlayer,WebConfig::getAuthorInfo,WebConfig::getTouristAvatar,WebConfig::getBulletin,
                        WebConfig::getQqNumber,WebConfig::getGitee,WebConfig::getGithub,WebConfig::getLogo,
                        WebConfig::getAboutMe,WebConfig::getEmail,WebConfig::getShowList,WebConfig::getLoginTypeList,
                        WebConfig::getRecordNum,WebConfig::getAuthor,WebConfig::getAliPay,WebConfig::getWeixinPay,
                        WebConfig::getWebUrl, WebConfig::getSummary,WebConfig::getName,WebConfig::getKeyword)
        .last(LIMIT_ONE));

        //文章分类标签
        Integer articleCount = articleMapper.selectCount(new QueryWrapper<BlogArticle>().eq(FieldConstants.IS_PUBLISH, PUBLISH.getCode()));
        Integer tagCount = tagsMapper.selectCount(null);
        Integer categoryCount = categoryMapper.selectCount(null);
        // 查询访问量
        String viewsCount = getViewsCount().toString();
        Map<String,Object> map = new HashMap<>();
        map.put("articleCount",articleCount);
        map.put("categoryCount",categoryCount);
        map.put("tagCount",tagCount);
        map.put("viewsCount",viewsCount);

        //查询页面信息
        List<Page> pages = pageMapper.selectList(new LambdaQueryWrapper<Page>()
                .select(Page::getPageCover,Page::getPageLabel));


        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("pageList",pages);
        resultMap.put("webSite",webConfig);
        resultMap.put("count",map);
        return ResponseResult.success(resultMap);

    }

    /**
     * 添加访问量
     * @param request
     * @return
     */
    public ResponseResult report(HttpServletRequest request) {
        // 获取ip
        String ipAddress = IpUtils.getIp(request);
        // 获取访问设备
        UserAgent userAgent = IpUtils.getUserAgent(request);
        Browser browser = userAgent.getBrowser();
        OperatingSystem operatingSystem = userAgent.getOperatingSystem();
        // 生成唯一用户标识
        String uuid = ipAddress + browser.getName() + operatingSystem.getName();
        String md5 = DigestUtils.md5DigestAsHex(uuid.getBytes());
        // 判断是否访问
        if (!redisService.sIsMember(RedisConstants.UNIQUE_VISITOR, md5)) {
            // 统计游客地域分布
            String ipSource = IpUtils.getIp2region(ipAddress);
            if (StringUtils.isNotBlank(ipSource)) {
                ipSource = ipSource.substring(0, 2)
                        .replaceAll(Constants.PROVINCE, "")
                        .replaceAll(Constants.CITY, "");
                redisService.hIncr(RedisConstants.VISITOR_AREA, ipSource, 1L);
            } else {
                redisService.hIncr(RedisConstants.VISITOR_AREA, Constants.UNKNOWN, 1L);
            }
            // 访问量+1
            redisService.incr(RedisConstants.BLOG_VIEWS_COUNT, 1);
            // 保存唯一标识
            redisService.sAdd(RedisConstants.UNIQUE_VISITOR, md5);
        }
        return ResponseResult.success();
    }



    //--------------自定义方法开始---------------
    public static List<String> getMonths() {
        List<String> dateList = new ArrayList<String>();
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -1);
        dateList.add(DateUtils.formateDate(calendar.getTime(), DateUtils.YYYY_MM_DD));
        while (date.after(calendar.getTime())) { //倒序时间,顺序after改before其他相应的改动。
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            dateList.add(DateUtils.formateDate(calendar.getTime(), DateUtils.YYYY_MM_DD));
        }
        return dateList;
    }
    /**
     * 获取文章贡献度
     * @return
     */
    public Map<String, Object> contribute() {
        Map<String, Object> map = new HashMap<>();
        List<Object> contributes = new ArrayList<>();
        List<Object> result = new ArrayList<>();
        List<String> months = getMonths();
        String lastTime = months.get(0), nowTime = months.get(months.size() - 1);
        List<ContributeVO> articles = articleMapper.contribute(lastTime, nowTime);
        months.forEach(item -> {
            List<Object> list = new ArrayList<>();
            list.add(item);
            List<ContributeVO> collect = articles.stream().filter(article -> article.getDate().equals(item)).collect(Collectors.toList());
            if (!collect.isEmpty()) list.add(collect.get(0).getCount());
            else list.add(0);
            result.add(list);
        });
        contributes.add(lastTime);
        contributes.add(nowTime);
        map.put("contributeDate", contributes);
        map.put("blogContributeCount", result);
        return map;
    }

    /**
     * 分类统计
     * @return
     */
    public Map<String, Object> categoryCount(){
        Map<String, Object> map = new HashMap<>();
        List<CategoryCountVO> result = categoryMapper.countArticle();
        List<String> list = new ArrayList<>();
        result.forEach(item -> list.add(item.getName()));
        map.put("result",result);
        map.put("categoryList",list);
        return map;
    }

    /**
     * 获取用户访问数据
     * @return
     */
    public List<Map<String,Object>> userAccess() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 7);
        List<Map<String,Object>> userAccess = sysLogMapper.getUserAccess(DateUtils.formateDate(calendar.getTime(), DateUtils.YYYY_MM_DD));
        return userAccess;
    }

    /**
     * 获取网站访问量
     * @return
     */
    private Object getViewsCount() {
        Object count = redisService.getCacheObject(RedisConstants.BLOG_VIEWS_COUNT);
        Object viewsCount = Optional.ofNullable(count).orElse(0);
        return viewsCount;
    }

}
