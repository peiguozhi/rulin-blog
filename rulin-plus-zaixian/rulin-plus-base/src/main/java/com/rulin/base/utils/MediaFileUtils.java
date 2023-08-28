package com.rulin.base.utils;

import com.j256.simplemagic.ContentInfo;
import com.j256.simplemagic.ContentInfoUtil;
import org.springframework.http.MediaType;

public class MediaFileUtils {

    public static String getContentType(String objectName) {
        String contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE; // 默认content-type为未知二进制流
        if (objectName.indexOf(".") >= 0) { // 判断对象名是否包含 .
            // 有 .  则划分出扩展名
            String extension = objectName.substring(objectName.lastIndexOf("."));
            // 根据扩展名得到content-type，如果为未知扩展名，例如 .abc之类的东西，则会返回null
            ContentInfo extensionMatch = ContentInfoUtil.findExtensionMatch(extension);
            // 如果得到了正常的content-type，则重新赋值，覆盖默认类型
            if (extensionMatch != null) {
                contentType = extensionMatch.getMimeType();
            }
        }
        return contentType;
    }
}
