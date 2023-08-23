package com.rulin.util;

import com.rulin.common.Constants;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    public static String replaceKeywords(String content, String keywords) {
        // 创建Pattern对象，并指定忽略大小写的标志
        Matcher matcher = Pattern.compile(keywords, Pattern.CASE_INSENSITIVE).matcher(content);
        // 查找匹配的内容
        while (matcher.find()) {
            // 打印找到的内容
            return content.replaceAll(matcher.group(), Constants.PRE_TAG + matcher.group() + Constants.POST_TAG);
        }
        return content;
    }

    public static String removeLabel(String content) {
        // 去除标签
        content = content.replaceAll("</?[^>]*>", "");
        if (content == null || content.length() == 0 || "".equals(content)){
            return content;
        }
        // 只保留部分内容
        int index = 0;
        for (int i = 0; i < 4; i++) {
            index = content.indexOf("\n", index + 1);
            if (index == -1){
                return content;
            }
        }
        //截取 字符串。得到结果
        content = content.substring(0, index);
        return content;
    }
}
