package com.rulin.vo;

import com.rulin.entity.BlogArticle;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @author CodeScholar
 * @date 2023年4月9日
 * @apiNote
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HomeDataVO {
    private Map<String, Object> contribute;
    private Map<String, Object> categoryList;
    private List<Map<String,Object>> userAccess;
    private List<Map<String,Object>> tagsList;
    private String dashboard;
    private List<BlogArticle> blogArticles;
}
