## 儒林小栈

<p align=center>
  <a href="http://www.codescholar.cn">
    <img src="./README.assets/me.png" alt="儒林小栈" width="150px">
  </a>
</p>



<p align="center">
   <a target="_blank" href="https://github.com/X1192176811/blog">
      <img src="https://img.shields.io/hexpm/l/plug.svg"/>
      <img src="https://img.shields.io/badge/JDK-1.8+-green.svg"/>
      <img src="https://img.shields.io/badge/springboot-2.4.1.RELEASE-green"/>
      <img src="https://img.shields.io/badge/vue-2.5.17-green"/>
      <img src="https://img.shields.io/badge/mysql-5.7.27-green"/>
      <img src="https://img.shields.io/badge/mybatis--plus-3.4.0-green"/>
      <img src="https://img.shields.io/badge/redis-6.0.19-green"/>
      <img src="https://img.shields.io/badge/elasticsearch-7.9.2-green"/>
   </a>
</p>
[项目介绍](#项目介绍) | [站点演示](#站点演示) | [项目特点](#项目特点) | [项目目录](#项目目录)  | [环境搭建](#环境搭建)  | [未来计划](#未来计划)  

## 项目介绍

**儒林小栈( pblog )**，一个**基于微服务架构的前后端分离博客系统**。前端使用 **Vue + ElementUi**。后端使用 **SpringBoot + Mybatis-plus**进行开发，使用 **sa-token** 做登录验证和权限校验，使用 **ElasticSearch**作为全文检索服务，文件支持**上传本地、七牛云 和 阿里云OSS**

- 儒林小栈是我在其他博客的基础上改造而来，因能力有限，其中很多技术都是一边学习一边使用的，可以说儒林小栈也是一个我用来熟悉技术的项目，所以很多地方可能考虑不周，故有能改正的地方，还请各位老哥能够指出~
- 现在挺多是SSM或者SSH的博客管理系统，想用 **SpringBoot**+ **Vue** 的微服务架构进行尝试项目的构建，里面很多功能可能只是**为了满足自己的学习需求**而引入的，大家可以**根据自己服务器配置来选择启动的服务**，因此本博客也是一个非常好的 **SpringBoot**以及 **Vue** 技术的入门学习项目。
- 原来做过 **Vue** + **ElementUi** 做过管理系统，所以现在打算做一套自己的、基于当前最新技术栈、前后端分离的微服务博客系统。
- [**儒林小栈**](http://www.codescholar.cn)中的一些文章，都来自我平时的学习笔记，实践过程，还有一些大佬的分享，主要的目的是为了帮助我进行记忆，需要用到哪些知识可以很方便的进行查找使用。
- 目前没有遇见比较好的背面试题八股文的网站，所以在博客中简单的集成了一些该功能，目前只是完成了基础的增删改查，简单的优化了移动端显示，后面会增加更多的功能。

## 站点演示

【演示前端】：http://www.codescholar.cn

【演示后端】：http://www.codescholar.cn/admin

【Gitee地址】：https://gitee.com/chengxuru/rulin-blog

【演示账号】：test 密码:123456

您的star是我坚持的动力，感谢大家的支持，欢迎提交pr共同改进项目。

## 项目中初始用户和密码

- **后台登录**：管理员：admin，密码：123456
- **后台登录**：演示账号：test，密码：123456
- **Mysql**：用户：root，密码：root
- **Redis**：密码：无

- **knife4j接口文档**：用户：admin，密码：admin
- **Druid初始密码：** 用户：admin，密码：123456

## 项目特点

- 友好的代码结构及注释，便于阅读及二次开发
- 实现前后端分离，通过 **Json** 进行数据交互，前端再也不用关注后端技术
- 页面交互使用 **Vue2.x**，极大的提高了开发效率。
- 引入**knife4j** 文档支持，方便编写 **API** 接口文档。
- 支持 **Gitee**、**QQ**、**微信** 、**微博**等第三方账号登录。
- 引入**ElasticSearch** 作为全文检索服务，并支持可插拔配置
- 引入七牛云对象存储，同时支持本地文件存储
- 引入 **RBAC** 权限管理设计，灵活的权限控制，按钮级别的细粒度权限控制，满足绝大部分的权限需求
- 采用**自定义参数校验注解**，轻松实现后端参数校验
- 采用 **AOP** + 自定义注解 + **Redis** 实现限制IP接口访问次数
- 留言采用弹幕墙，更加炫酷。
- 支持代码高亮和复制，图片预览，深色模式等功能，提升用户体验。
- 后台管理支持修改背景图片，博客配置等信息，操作简单，支持上传相册。
- 采用自研的评论模块，实现评论邮件通知
- 采用 **Docker Compose** 完成容器编排，**Portainer** 实现容器可视化，支持一键部署线上环境

## 项目目录

前端项目pblog-web为前台，pblog-admin为后台。

后端项目位于pblog下。

SQL文件位于根目录下的pblog.sql

可直接导入该项目于本地，修改后端配置文件中的数据库等连接信息，项目中使用到的关于七牛云功能和第三方授权登录等需要自行开通。

当你克隆项目到本地后可使用账号：admin，密码：123456 进行登录

本地访问接口文档地址：http://127.0.0.1:8888/rulin/doc.html

**ps：请先运行后端项目，再启动前端项目，前端项目配置由后端动态加载。** 

pblog

- annotation：自定义注解
- aspect：aop模块
- config：配置模块
- common：常量模块
- controller：控制器模块
- entity：实体类模块
- dto：数据传输对象模块
- enums：枚举模块
- exception ：自定义异常模块
- mapper：框架核心模块
- service：服务模块
- strategy ：策略模块（用于扩展第三方登录，搜索模式，上传文件模式等策略）
- utils：工具类模块
- quartz：定时功能模块
- webmagic ：文章爬虫模块
- vo：与前端进行交互的视图对象模块

### 后端技术

|      技术      |           说明           |                             官网                             |
| :------------: | :----------------------: | :----------------------------------------------------------: |
|   SpringBoot   |         MVC框架          | [ https://spring.io/projects/spring-boot](https://spring.io/projects/spring-boot) |
|    sa-token    | 轻量级 java 权限认证框架 |                https://sa-token.cc/index.html                |
|  MyBatis-Plus  |         ORM框架          |                   https://mp.baomidou.com/                   |
|   Swagger-UI   |       文档生产工具       | [ https://github.com/swagger-api/swagger-ui](https://github.com/swagger-api/swagger-ui) |
|     Kibana     |     分析和可视化平台     |               https://www.elastic.co/cn/kibana               |
| Elasticsearch  |         搜索引擎         | [ https://github.com/elastic/elasticsearch](https://github.com/elastic/elasticsearch) |
|     Redis      |        分布式缓存        |                      https://redis.io/                       |
|     Docker     |        容器化部署        |      [ https://www.docker.com](https://www.docker.com/)      |
|     Druid      |       数据库连接池       | [ https://github.com/alibaba/druid](https://github.com/alibaba/druid) |
|     七牛云     |    七牛云 - 对象储存     |         https://developer.qiniu.com/sdk#official-sdk         |
|      JWT       |       JWT登录支持        |                 https://github.com/jwtk/jjwt                 |
|     log4j      |         日志框架         |            https://logging.apache.org/log4j/2.x/             |
|     Lombok     |     简化对象封装工具     | [ https://github.com/rzwitserloot/lombok](https://github.com/rzwitserloot/lombok) |
|     Nginx      | HTTP和反向代理web服务器  |                      http://nginx.org/                       |
|   Ip2region    |     离线IP地址定位库     |          https://github.com/lionsoul2014/ip2region           |
| Docker Compose |      Docker容器编排      |               https://docs.docker.com/compose/               |
|   Portainer    |     Docker可视化管理     |            https://github.com/portainer/portainer            |

### 前端技术

|            技术            |        说明        |                             官网                             |
| :------------------------: | :----------------: | :----------------------------------------------------------: |
|           Vue.js           |      前端框架      |                      https://vuejs.org/                      |
|         Vue-router         |      路由框架      |                  https://router.vuejs.org/                   |
|            Vuex            |  全局状态管理框架  |                   https://vuex.vuejs.org/                    |
|          Element           |     前端ui框架     |    [ https://element.eleme.io](https://element.eleme.io/)    |
|           Axios            |    前端HTTP框架    | [ https://github.com/axios/axios](https://github.com/axios/axios) |
|          Echarts           |      图表框架      |                      www.echartsjs.com                       |
|        mavonEditor         | 在线markdown编辑器 |                 http://www.mavoneditor.com/                  |
|        Highlight.js        |  代码语法高亮插件  |         https://github.com/highlightjs/highlight.js          |
|        clipboard.js        |  现代化的拷贝文字  |                  http://www.clipboardjs.cn/                  |
|        markdown-it         |  markdown 解析器   |              https://markdown-it.docschina.org/              |
|          webpack           |     模块打包器     |                  https://www.webpackjs.com/                  |
| compression-webpack-plugin |   代码压缩包插件   | https://github.com/webpack-contrib/compression-webpack-plugin |

## 环境搭建

### 开发工具

|     工具     |       说明        |                             官网                             |
| :----------: | :---------------: | :----------------------------------------------------------: |
|     IDEA     |    Java开发IDE    |           https://www.jetbrains.com/idea/download            |
|   WebStorm   |    前端开发IDE    |             https://www.jetbrains.com/webstorm/              |
| RedisDesktop |  Redis可视化工具  | [ https://redisdesktop.com/download](https://redisdesktop.com/download) |
| SwitchHosts  |   本地Host管理    |             https://oldj.github.io/SwitchHosts/              |
|   X-shell    | Linux远程连接工具 |               https://xshell.en.softonic.com/                |
|    X-ftp     | Linux文件传输工具 |         https://www.netsarang.com/zh/all-downloads/          |
|   Navicat    |  数据库连接工具   |                   https://navicat.com.cn/                    |

### 开发环境

|     工具      | 版本号 |                             下载                             |
| :-----------: | :----: | :----------------------------------------------------------: |
|      JDK      |  1.8   | https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html |
|     Maven     | 3.6.0+ |                   http://maven.apache.org/                   |
| Elasticsearch | 7.9.2  |               https://www.elastic.co/downloads               |
|     MySQL     | 5.7.27 |                    https://www.mysql.com/                    |
|     Nginx     |  1.10  |              http://nginx.org/en/download.html               |
|     Redis     | 6.0.19 |                  https://redis.io/download                   |

## 致谢

**儒林小栈**参考了很多**开源项目**的**解决方案**，**开源不易，感谢分享**

- 感谢 [七牛云](https://portal.qiniu.com/signup?utm_source=kaiyuan&utm_media=mogu) 提供的 **免费云存储** 和 **CDN** 服务
- 感谢 **杨青小姐姐** 的博客模板：[http://www.yangqq.com/](http://www.yangqq.com/)
- 感谢**PanJiaChen**的Vue后台管理模板：[vue-element-admin](https://github.com/PanJiaChen/vue-element-admin)
- **Vue** 项目搭建参考这篇博客：[https://segmentfault.com/a/1190000009506097](https://segmentfault.com/a/1190000009506097)
- 感谢 **拾壹博客** 的博客模板：https://gitee.com/quequnlong/shiyi-blog
- 感谢 **苞米豆** 提供的 **Mybatis-plus**框架：[http://mp.baomidou.com/](http://mp.baomidou.com/)
- 感谢 **bihell** 的 **Dice** 博客项目：[https://github.com/bihell/Dice](https://github.com/bihell/Dice)
- 感谢 **pppercyWang** 提供的Emoji表情评论组件：[vue-emoji-comment](https://github.com/pppercyWang/vue-emoji-comment)
- 感谢 **若依** 提供的 **RuoYi** 项目：[https://gitee.com/y_project/RuoYi](https://gitee.com/y_project/RuoYi)

## 未来计划

- [x] 门户网站增加登录页面
- [x] 集成ElasticSearch
- [x] 将图片存储在七牛云中
- [x] 写一个评论模块
- [x] 按钮级别的细粒度权限控制
- [x] 增加数据字典管理
- [x] 前端增加用户个人中心
- [x] 增加博客详情页目录导航
- [x] 使用Docker Compose完成博客一键部署
- [x] 使用Portainer对Docker镜像可视化管理
- [x] 适配门户页面的移动端布局
- [x] 增加定时任务模块
- [x] 增加大屏数据展示页面
- [x] 增加面试题管理模块
- [x] 增加评论表情
- [x] 支持第三方登录
- [ ] 面试题增加标签
- [ ] 面试题多维度筛选题目（难度、标签、题型）
- [ ] 面试题多维度排序题目（热度、收藏数、频率）
- [ ] 面试题题目推荐
- [ ] 面试题题目遇见次数
- [ ] 面试题共同编辑解析
- [ ] 面试题精选题目、上传题目
- [ ] 面试题题目收藏功能（语音读题、选题练习、分析报告）
- [ ] 面试题不熟题集
- [ ] 富文本编辑器和Markdown编辑器任意切换
- [ ] 使用ELK搭建博客日志收集功能
- [ ] 完善爬虫模块
- [ ] 增加一个FAQ常见问题文档
- [ ] 使用Freemark页面静态化技术对博客详情页静态化
- [ ] 让原创文章能够同步到多平台，如：CSDN，掘金，博客园等
- [ ] 增加博客迁移功能，让其它平台的博客，如：CSDN、博客园，WordPress能够同步到蘑菇博客中

## 网站截图

### 前台web端

| ![image-20230421152829758](README.assets/image-20230421152829758.png) | ![image-20230421152857902](README.assets/image-20230421152857902.png) |
| :----------------------------------------------------------: | ------------------------------------------------------------ |
| ![image-20230421152916029](README.assets/image-20230421152916029.png) | ![image-20230421152929446](README.assets/image-20230421152929446.png) |
| ![image-20230421152943045](README.assets/image-20230421152943045.png) | ![image-20230421153007618](README.assets/image-20230421153007618.png) |
| ![image-20230421153044253](README.assets/image-20230421153044253.png) | ![image-20230421153242655](README.assets/image-20230421153242655.png) |

### 前台移动端

| ![image-20230421153429751](README.assets/image-20230421153429751.png) | ![image-20230421153450116](README.assets/image-20230421153450116.png) |
| :----------------------------------------------------------: | ------------------------------------------------------------ |
| ![image-20230421153511073](README.assets/image-20230421153511073.png) | ![image-20230421153521132](README.assets/image-20230421153521132.png) |



### 后台

| ![image-20230421151848790](README.assets/image-20230421151848790.png) | ![image-20230421151924326](README.assets/image-20230421151924326.png) |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| ![image-20230421151815027](README.assets/image-20230421151815027.png) | ![image-20230421152140954](README.assets/image-20230421152140954.png) |
| ![image-20230421152257038](README.assets/image-20230421152257038.png) | ![image-20230421152311674](README.assets/image-20230421152311674.png) |
| ![image-20230421152326797](README.assets/image-20230421152326797.png) | ![image-20230421152338715](README.assets/image-20230421152338715.png) |
| ![image-20230421152420066](README.assets/image-20230421152420066.png) | ![image-20230421152433741](README.assets/image-20230421152433741.png) |
| ![image-20230421152448209](README.assets/image-20230421152448209.png) | ![image-20230421152502633](README.assets/image-20230421152502633.png) |
| ![image-20230421152522041](README.assets/image-20230421152522041.png) | ![image-20230421152535878](README.assets/image-20230421152535878.png) |
| ![image-20230421152549821](README.assets/image-20230421152549821.png) | ![image-20230421152607347](README.assets/image-20230421152607347.png) |
| ![image-20230421152640242](README.assets/image-20230421152640242.png) | ![image-20230421152654228](README.assets/image-20230421152654228.png) |
| ![image-20230421152707208](README.assets/image-20230421152707208.png) | ![image-20230421152720481](README.assets/image-20230421152720481.png) |







