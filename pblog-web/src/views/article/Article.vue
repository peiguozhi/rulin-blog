<template>
  <div>
    <Loading></Loading>
    <!-- 封面图 -->
    <div class="banner" :style="articleCover">
      <div class="article-info-container">
        <!-- 文章标题 -->
        <div class="article-title">{{ article.title }}</div>
        <div class="article-info">
          <div class="first-line">
            <!-- 发表时间 -->
            <span>
              <i class="iconfont iconrili" />
              发表于 {{ article.createTime | date }}
            </span>
            <span class="separator">|</span>
            <!-- 发表时间 -->
            <span>
              <i class="iconfont icongengxinshijian" />
              更新于
              <template v-if="article.lastTime">
                {{ article.lastTime | date }}
              </template>
              <template v-else>
                {{ article.createTime | date }}
              </template>
            </span>
            <span class="separator">|</span>
            <!-- 文章分类 -->
            <span class="article-category">
              <i class="iconfont iconfenlei1" />
              <router-link :to="'/categories/' + article.category.id">
                {{ article.category.name }}
              </router-link>
            </span>
          </div>
          <div class="second-line">
            <!-- 字数统计 -->
            <span>
              <i class="iconfont iconzishu" />
              字数统计: {{ wordNum | num }}
            </span>
            <span class="separator">|</span>
            <!-- 阅读时长 -->
            <span>
              <i class="iconfont iconshijian" />
              阅读时长: {{ readTime }}
            </span>
          </div>
          <div class="third-line">
            <span class="separator">|</span>
            <!-- 阅读量 -->
            <span>
              <i class="iconfont iconliulan" /> 阅读量: {{ article.quantity }}
            </span>
            <span class="separator">|</span>
            <!-- 评论量 -->
            <span>
              <i class="iconfont iconpinglunzu1" />评论数:
              <template v-if="count">{{ count }}</template>
              <template v-else>0</template>
            </span>
          </div>
        </div>
      </div>
    </div>
    <!-- 内容 -->
    <v-row class="article-container">
      <v-col :md="leftMd"></v-col>
      <!--   左边目录内容   -->
      <v-col md="2" :class="bookHide">
        <!-- 左侧内容  当前类型下笔记列表  -->
        <v-card class="right-container" style="position: sticky; top: 20px;">
          <div class="right-title">
            <i class="el-icon-s-management" style="font-size:18px" />
            <span style="margin-left:10px">{{ article.category.name }}</span>
          </div>
          <div class="article-list">
            <div
              class="article-item"
              v-for="item of article.articleBookList"
              :key="item.id"
            >
              <div class="content">
                <div class="content-title">
                  <router-link :to="'/articles/' + item.id">
                    {{ item.title }}
                  </router-link>
                </div>
                <div class="content-time">{{ item.createTime | date }}</div>
              </div>
              <!--      文章首图显示        -->
<!--              <router-link :to="'/articles/' + item.id" class="content-cover">
                <img :src="item.avatar" />
              </router-link>-->
            </div>
          </div>
        </v-card>
      </v-col>
      <!--   中间内容   -->
      <v-col :md="articleMd" cols="12">
        <v-card class="article-wrapper">
          <article
            v-show="!isCheck"
            id="write"
            class="article-content markdown-body"
            v-html="article.content"
            ref="article"
          />
          <div style="" v-show="isCheck">该文章为私密文章，如需查看请点
            <a href="javaScript:void(0);" @click="dialogVisible=true" style="color: red">
              <i class="el-icon-lock"></i>此处
            </a>
            获取验证码
          </div>
          <!-- 版权声明 -->
          <div class="aritcle-copyright">
            <div>
              <span>创作类型：</span>
              <font v-if="article.isOriginal === 0">转载</font>
              <font v-else>原创</font>
            </div>
            <div v-if="article.isOriginal === 1">
              <span>文章作者：</span>
              <router-link to="/">
                {{ blogInfo.webSite.author }}
              </router-link>
            </div>
            <div>
              <span>文章链接：</span>
              <a :href="articleHref" target="_blank">{{ articleHref }} </a>
            </div>
            <div v-if="article.isOriginal === 0">
              <span>原文链接：</span>
              <a :href="article.originalUrl" target="_blank">{{ article.originalUrl }} </a>
            </div>
            <div>
              <span>版权声明：</span>本博客所有文章除特别声明外，均采用
              <a
                href="https://creativecommons.org/licenses/by-nc-sa/4.0/"
                target="_blank"
              >
                CC BY-NC-SA 4.0
              </a>
              许可协议。转载请注明文章出处。
            </div>
          </div>
          <!-- 转发 -->
          <div class="article-operation">
            <div class="tag-container">
              <router-link
                v-for="item of article.tagList"
                :key="item.id"
                :to="'/tags/' + item.id"
              >
                {{ item.name }}
              </router-link>
            </div>
            <share style="margin-left:auto" :config="config" />
          </div>
          <!-- 点赞打赏等 -->
          <div class="article-reward">
            <!-- 点赞按钮 -->
            <a :class="isLike" @click="like">
              <v-icon size="14" color="#fff">mdi-thumb-up</v-icon>
              点赞
              <span v-show="article.likeCount > 0">{{
                  article.likeCount
                }}</span>
            </a>
            <a class="reward-btn" v>
              <!-- 打赏按钮 -->
              <i class="iconfont iconerweima" /> 打赏
              <!-- 二维码 -->
              <div class="animated fadeInDown reward-main">
                <ul class="reward-all">
                  <li class="reward-item">
                    <img
                      class="reward-img"
                      :src="blogInfo.webSite.aliPay"
                    />
                    <div class="reward-desc">微信</div>
                  </li>
                  <li class="reward-item">
                    <img
                      class="reward-img"
                      :src="blogInfo.webSite.weixinPay"
                    />
                    <div class="reward-desc">支付宝</div>
                  </li>
                </ul>
              </div>
            </a>
          </div>
          <div class="pagination-post">
            <!-- 上一篇 -->
            <div
              :class="isFull(article.lastArticle.id)"
              v-if="article.lastArticle"
            >
              <router-link :to="'/articles/' + article.lastArticle.id">
                <img
                  class="post-cover"
                  :src="article.lastArticle.avatar"
                />
                <div class="post-info">
                  <div class="label">上一篇</div>
                  <div class="post-title">
                    {{ article.lastArticle.title }}
                  </div>
                </div>
              </router-link>
            </div>
            <!-- 下一篇 -->
            <div
              :class="isFull(article.nextArticle.id)"
              v-if="article.nextArticle"
            >
              <router-link :to="'/articles/' + article.nextArticle.id">
                <img
                  class="post-cover"
                  :src="article.nextArticle.avatar"
                />
                <div class="post-info" style="text-align: right">
                  <div class="label">下一篇</div>
                  <div class="post-title">
                    {{ article.nextArticle.title }}
                  </div>
                </div>
              </router-link>
            </div>
          </div>
          <!-- 推荐文章 -->
<!--          <div
            class="recommend-container"
            v-if="article.recommendArticleList.length"
          >
            <div class="recommend-title">
              <v-icon size="20" color="#4c4948">mdi-thumb-up</v-icon>
              相关推荐
            </div>
            <div class="recommend-list">
              <div
                class="recommend-item"
                v-for="item of article.recommendArticleList"
                :key="item.id"
              >
                <router-link :to="'/articles/' + item.id">
                  <img class="recommend-cover" :src="item.avatar" />
                  <div class="recommend-info">
                    <div class="recommend-date">
                      <i class="iconfont iconrili" />
                      {{ item.createTime | date }}
                    </div>
                    <div>{{ item.title }}</div>
                  </div>
                </router-link>
              </div>
            </div>
          </div>-->
          <!-- 分割线 -->
          <hr />
          <!-- 评论 -->
          <comment
            :commentList="commentList"
            :count="count"
            @reloadComment="listComment"
          />
        </v-card>
      </v-col>
      <!-- 右边功能 -->
      <v-col md="2" cols="12" class="d-md-block d-none">
        <div style="position: sticky;top: 20px;">
          <!-- 文章目录 -->
          <v-card class="right-container">
            <div class="right-title">
              <i class="iconfont iconhanbao" style="font-size:16.8px" />
              <span style="margin-left:10px">目录</span>
            </div>
            <div id="toc" />
          </v-card>

          <!-- 最新文章 -->
          <v-card class="right-container" style="margin-top:20px">
            <div class="right-title">
              <i class="iconfont icongengxinshijian" style="font-size:16.8px" />
              <span style="margin-left:10px">最新文章</span>
            </div>
            <div class="article-list">
              <div
                class="article-item"
                v-for="item of article.newestArticleList"
                :key="item.id"
              >
                <router-link :to="'/articles/' + item.id" class="content-cover">
                  <img :src="item.avatar" />
                </router-link>
                <div class="content">
                  <div class="content-title">
                    <router-link :to="'/articles/' + item.id">
                      {{ item.title }}
                    </router-link>
                  </div>
                  <div class="content-time">{{ item.createTime | date }}</div>
                </div>
              </div>
            </div>
          </v-card>
        </div>
      </v-col>
      <v-col md="1"></v-col>
    </v-row>
    <div>
      <el-dialog
        title="加载校验"
        :visible.sync="dialogVisible"
        width="30%"
      >
        <div style="display :flex;justify-content: center;">
          <font >扫码关注公众号「<span style="color: #005cc5">程序儒青衫</span>」</font><br />
        </div>
        <div style="display :flex;justify-content: center;">
          <font > 回复 「<span style="color: red">验证码</span>」获取验证码</font>
        </div>
        <div style="display :flex;justify-content: center;">
          <img style="width: 50%;height: 50%" src="http://img.codescholar.cn/gzh-qrcode.jpg" />
        </div>
        <el-input v-model="code" oninput="value=value.replace(/[^\d]/g,'')" placeholder="请输入6位数的验证码" style="display :flex;justify-content: center;width: 45%;margin: 0 auto;"></el-input>
        <span style="display :flex;justify-content: center;" >
          <el-button style="width: 45%;margin-top: 5px" type="primary" @click="checkSecret">提 交</el-button>
         </span>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import Clipboard from "clipboard";
import Comment from "../../components/Comment";
import tocbot from "tocbot";
import { getArticleInfo, fetchComments, like,checkSecret} from "../../api";
import hljs from "../../plugins/hightLight/index";
import Loading from "@/components/loading/loading";

export default {
  metaInfo:{
    meta: [{
      name: 'keyWords',
      content: "儒林小栈,开源博客,www.codescholar.cn"  //变量或字符串
    }, {
      name: 'description',
      content: "一个专注于技术分享的博客平台,大家以共同学习,乐于分享,拥抱开源的价值观进行学习交流"
    }]
  },
  components: {
    Comment,
    Loading
  },
  created() {
    this.getArticle();
    this.listComment();
  },
   destroyed() {
     this.clipboard.destroy();
     tocbot.destroy();
   },
  data: function() {
    return {
      config: {
        sites: ["qzone", "wechat", "weibo", "qq"]
      },
      bookHide: "d-none",
      articleMd: 7,
      leftMd: 2,
      imgList: [],
      articleId: this.$route.params.articleId,
      dialogVisible: false,
      code:null,
      isCheck:false,
      article: {
        nextArticle: {
          id: 0,
          articleCover: ""
        },
        lastArticle: {
          id: 0,
          articleCover: ""
        },
        category: {
          id: 0,
          name: ""
        },
        recommendArticleList: [],
        newestArticleList: [],
        articleBookList: []
      },
      commentList: [],
      count: 0,
      wordNum: "",
      readTime: "",
      articleHref: window.location.href,
      img: process.env.VUE_APP_IMG_API,
      clipboard: null
    };
  },
  methods: {
    checkSecret: function() {
      if (this.code == null || this.code.length !== 6){
        this.$toast({ type: "error", message: "请输入有效的验证码" });
        return false;
      }
      checkSecret({code:this.code}).then(res =>{
        this.isCheck = false
        this.dialogVisible = false
        this.code = null
      }).catch(err =>{
        this.$toast({ type: "error", message: err.message });
      })
    },
    getArticle() {
      const that = this;
      //查询文章
      getArticleInfo(this.articleId).then(res => {
        document.title = res.data.title;
        if (res.data.keywords != null){
          document.querySelector('meta[name="keywords"]').setAttribute('content',  res.data.keywords)
        }
        this.isCheck = res.data.isSecret !== 0
        //将markdown转换为Html
        this.markdownToHtml(res.data);
        this.$nextTick(() => {
          // 统计文章字数
          this.wordNum = this.deleteHTMLTag(this.article.content).length;
          // 计算阅读时间
          this.readTime = Math.round(this.wordNum / 400) + "分钟";
          // 添加代码复制功能
          this.clipboard = new Clipboard(".copy-btn");
          this.clipboard.on("success", () => {
            this.$toast({ type: "success", message: "复制成功" });
          });
            // 添加文章生成目录功能
            let nodes = this.$refs.article.children;
            if (nodes.length) {
              for (let i = 0; i < nodes.length; i++) {
                let node = nodes[i];
                let reg = /^H[1-4]{1}$/;
                if (reg.exec(node.tagName)) {
                  node.id = i;
                }
              }
            }
            tocbot.init({
              tocSelector: "#toc", //要把目录添加元素位置，支持选择器
              contentSelector: ".article-content", //获取html的元素
              headingSelector: "h1, h2, h3,h4", //要显示的id的目录
              hasInnerContainers: true,
              onClick: function(e) {
                e.preventDefault();
              }
            });
            // 添加图片预览功能
            const imgList = this.$refs.article.getElementsByTagName("img");
            for (var i = 0; i < imgList.length; i++) {
              this.imgList.push(imgList[i].src);
              imgList[i].addEventListener("click", function(e) {
                that.previewImg(e.target.currentSrc);
              });
            }

        });
      });
    },
    listComment() {
      fetchComments({ pageNo: 1, pageSize: 10, articleId: this.articleId }).then(res => {
        this.commentList = res.data != null ? res.data.commentDTOList : [];
        this.count = res.data != null ? res.data.commentCount : 0;
      });
    },
    like() {
      // 判断登录
      if (!this.$store.state.userId) {
        this.$store.state.loginFlag = true;
        return false;
      }
      //发送请求
      like(this.article.id).then(res => {
        //判断是否点赞
        if (
          this.$store.state.articleLikeSet.indexOf(this.article.id) != -1
        ) {
          this.$set(this.article, "likeCount", this.article.likeCount - 1);
        } else {
          this.$set(this.article, "likeCount", this.article.likeCount + 1);
        }
        this.$store.commit("articleLike", this.article.id);
      });
    },
    markdownToHtml(article) {
      const MarkdownIt = require("markdown-it");
      const md = new MarkdownIt({
        html: true,
        linkify: true,
        typographer: true,
        highlight: function(str, lang) {
          // 当前时间加随机数生成唯一的id标识
          var d = new Date().getTime();
          if (
            window.performance &&
            typeof window.performance.now === "function"
          ) {
            d += performance.now();
          }
          const codeIndex = "xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx".replace(
            /[xy]/g,
            function(c) {
              var r = (d + Math.random() * 16) % 16 | 0;
              d = Math.floor(d / 16);
              return (c == "x" ? r : (r & 0x3) | 0x8).toString(16);
            }
          );
          // 复制功能主要使用的是 clipboard.js
          let html = `<button class="copy-btn iconfont iconfuzhi" type="button" data-clipboard-action="copy" data-clipboard-target="#copy${codeIndex}"></button>`;
          const linesLength = str.split(/\n/).length - 1;
          // 生成行号
          let linesNum = "<span aria-hidden=\"true\" class=\"line-numbers-rows\">";
          for (let index = 0; index < linesLength; index++) {
            linesNum = linesNum + "<span></span>";
          }
          linesNum += "</span>";
          if (lang && hljs.getLanguage(lang)) {
            // highlight.js 高亮代码
            const preCode = hljs.highlight(lang, str, true).value;
            html = html + preCode;
            if (linesLength) {
              html += "<b class=\"name\">" + lang + "</b>";
            }
            // 将代码包裹在 textarea 中，由于防止textarea渲染出现问题，这里将 "<" 用 "<" 代替，不影响复制功能
            return `<pre class="hljs"><code>${html}</code>${linesNum}</pre><textarea style="position: absolute;top: -9999px;left: -9999px;z-index: -9999;" id="copy${codeIndex}">${str.replace(
              /<\/textarea>/g,
              "</textarea>"
            )}</textarea>`;
          }
        }
      });
      // 将markdown替换为html标签
      article.content = md.render(article.contentMd ? article.contentMd : article.content);
      this.article = article;
      // 判断是不是笔记类文章，调整页面显示样式
      this.editCol();
    },
    previewImg(img) {
      this.$imagePreview({
        images: this.imgList,
        index: this.imgList.indexOf(img)
      });
    },
    deleteHTMLTag(content) {
      return content
        .replace(/<\/?[^>]*>/g, "")
        .replace(/[|]*\n/, "")
        .replace(/&npsp;/gi, "");
    },
    editCol() {
      if (this.article.articleBookList.length > 0){
        this.leftMd = 1;
        this.bookHide = "d-md-block d-none";
        this.articleMd = 6;
      }
    }
  },
  computed: {
    blogInfo() {
      return this.$store.state.blogInfo;
    },
    articleCover() {
      return (
        "background: url(" +
        this.article.avatar +
        ") center center / cover no-repeat"
      );
    },
    isLike() {
      var articleLikeSet = this.$store.state.articleLikeSet;
      return articleLikeSet.indexOf(this.article.id) != -1
        ? "like-btn-active"
        : "like-btn";
    },
    isFull() {
      return function(id) {
        return id ? "post full" : "post";
      };
    }
  }
};
</script>

<style scoped>
.banner:before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
}

.article-info i {
  font-size: 14px;
}

.article-info {
  font-size: 14px;
  line-height: 1.9;
  display: inline-block;
}

@media (min-width: 760px) {
  .banner {
    color: #eee !important;
  }

  .article-info span {
    font-size: 95%;
  }

  .article-info-container {
    position: absolute;
    bottom: 6.25rem;
    padding: 0 8%;
    width: 100%;
    text-align: center;
  }

  .second-line,
  .third-line {
    display: inline;
  }

  .article-title {
    font-size: 35px;
    margin: 20px 0 8px;
    cursor: text;
  }

  .pagination-post {
    display: flex;
  }

  .post {
    width: 50%;
  }

  .recommend-item {
    position: relative;
    display: inline-block;
    overflow: hidden;
    margin: 3px;
    width: calc(33.333% - 6px);
    height: 200px;
    background: #000;
    vertical-align: bottom;
  }
}

@media (max-width: 759px) {
  .banner {
    color: #eee !important;
    height: 360px;
  }

  .article-info span {
    font-size: 90%;
  }

  .separator:first-child {
    display: none;
  }

  .blog-container {
    margin: 322px 5px 0 5px;
  }

  .article-info-container {
    position: absolute;
    bottom: 1.3rem;
    padding: 0 5%;
    width: 100%;
    color: #eee;
    text-align: left;
  }

  .article-title {
    font-size: 1.5rem;
    margin-bottom: 0.4rem;
  }

  .post {
    width: 100%;
  }

  .pagination-post {
    display: block;
  }

  .recommend-item {
    position: relative;
    display: inline-block;
    overflow: hidden;
    margin: 3px;
    width: calc(100% - 4px);
    height: 150px;
    margin: 2px;
    background: #000;
    vertical-align: bottom;
  }
}

.article-content {
  word-break: break-word;
  font-size: 18px;
  line-height: 2;
}

.article-operation {
  display: flex;
  align-items: center;
}

.article-category a {
  color: #fff !important;
}

.tag-container a {
  display: inline-block;
  margin: 0.5rem 0.5rem 0.5rem 0;
  padding: 0 0.75rem;
  width: fit-content;
  border: 1px solid #49b1f5;
  border-radius: 1rem;
  color: #49b1f5 !important;
  font-size: 12px;
  line-height: 2;
}

.tag-container a:hover {
  color: #fff !important;
  background: #49b1f5;
  transition: all 0.5s;
}

.aritcle-copyright {
  position: relative;
  margin-top: 40px;
  margin-bottom: 10px;
  font-size: 0.875rem;
  line-height: 2;
  padding: 0.625rem 1rem;
  border: 1px solid #eee;
}

.aritcle-copyright span {
  color: #49b1f5;
  font-weight: bold;
}

.aritcle-copyright a {
  text-decoration: underline !important;
  color: #99a9bf !important;
}

.aritcle-copyright:before {
  position: absolute;
  top: 0.7rem;
  right: 0.7rem;
  width: 1rem;
  height: 1rem;
  border-radius: 1rem;
  background: #49b1f5;
  content: "";
}

.aritcle-copyright:after {
  position: absolute;
  top: 0.95rem;
  right: 0.95rem;
  width: 0.5rem;
  height: 0.5rem;
  border-radius: 0.5em;
  background: #fff;
  content: "";
}

.article-reward {
  margin-top: 5rem;
  display: flex;
  justify-content: center;
  align-items: center;
}

.reward-btn {
  position: relative;
  display: inline-block;
  width: 100px;
  background: #49b1f5;
  margin: 0 1rem;
  color: #fff !important;
  text-align: center;
  line-height: 36px;
  font-size: 0.875rem;
}

.reward-btn:hover .reward-main {
  display: block;
}

.reward-main {
  display: none;
  position: absolute;
  bottom: 40px;
  left: 0;
  margin: 0;
  padding: 0 0 15px;
  width: 100%;
}

.reward-all {
  display: inline-block;
  margin: 0 0 0 -110px;
  padding: 20px 10px 8px !important;
  width: 320px;
  border-radius: 4px;
  background: #f5f5f5;
}

.reward-all:before {
  position: absolute;
  bottom: -10px;
  left: 0;
  width: 100%;
  height: 20px;
  content: "";
}

.reward-all:after {
  content: "";
  position: absolute;
  right: 0;
  bottom: 2px;
  left: 0;
  margin: 0 auto;
  width: 0;
  height: 0;
  border-top: 13px solid #f5f5f5;
  border-right: 13px solid transparent;
  border-left: 13px solid transparent;
}

.reward-item {
  display: inline-block;
  padding: 0 8px;
  list-style-type: none;
}

.reward-img {
  width: 130px;
  height: 130px;
  display: block;
}

.reward-desc {
  margin: -5px 0;
  color: #858585;
  text-align: center;
}

.like-btn {
  display: inline-block;
  width: 100px;
  background: #969696;
  color: #fff !important;
  text-align: center;
  line-height: 36px;
  font-size: 0.875rem;
}

.like-btn-active {
  display: inline-block;
  width: 100px;
  background: #ec7259;
  color: #fff !important;
  text-align: center;
  line-height: 36px;
  font-size: 0.875rem;
}

.pagination-post {
  margin-top: 40px;
  overflow: hidden;
  width: 100%;
  background: #000;
}

.post {
  position: relative;
  height: 150px;
  overflow: hidden;
}

.post-info {
  position: absolute;
  top: 50%;
  padding: 20px 40px;
  width: 100%;
  transform: translate(0, -50%);
  line-height: 2;
  font-size: 16px;
}

.post-cover {
  position: absolute;
  width: 100%;
  height: 100%;
  opacity: 0.4;
  transition: all 0.6s;
  object-fit: cover;
}

.post a {
  position: relative;
  display: block;
  overflow: hidden;
  height: 150px;
}

.post:hover .post-cover {
  opacity: 0.8;
  transform: scale(1.1);
}

.label {
  font-size: 90%;
  color: #eee;
}

.post-title {
  font-weight: 500;
  color: #fff;
}

hr {
  position: relative;
  margin: 40px auto;
  border: 2px dashed #d2ebfd;
  width: calc(100% - 4px);
}

.full {
  width: 100% !important;
}

.right-container {
  padding: 20px 24px;
  font-size: 18px;
}

.right-title {
  display: flex;
  align-items: center;
  line-height: 2;
  font-size: 20px;
  margin-bottom: 6px;
}

.right-title i {
  font-weight: bold;
}

.recommend-container {
  margin-top: 40px;
}

.recommend-title {
  font-size: 22px;
  line-height: 2;
  font-weight: bold;
  margin-bottom: 5px;
}

.recommend-cover {
  width: 100%;
  height: 100%;
  opacity: 0.4;
  transition: all 0.6s;
  object-fit: cover;
}

.recommend-info {
  line-height: 2;
  color: #fff;
  position: absolute;
  top: 50%;
  padding: 0 20px;
  width: 100%;
  transform: translate(0, -50%);
  text-align: center;
  font-size: 16px;
}

.recommend-date {
  font-size: 90%;
}

.recommend-item:hover .recommend-cover {
  opacity: 0.8;
  transform: scale(1.1);
}

.article-item {
  display: flex;
  align-items: center;
  padding: 6px 0;
}

.article-item:first-child {
  padding-top: 0;
}

.article-item:last-child {
  padding-bottom: 0;
}

.article-item:not(:last-child) {
  border-bottom: 1px dashed #f5f5f5;
}

.article-item img {
  width: 100%;
  height: 100%;
  transition: all 0.6s;
  object-fit: cover;
}

.article-item img:hover {
  transform: scale(1.1);
}

.content {
  flex: 1;
  padding-left: 10px;
  word-break: break-all;
  display: -webkit-box;
  overflow: hidden;
  -webkit-box-orient: vertical;
}

.content-cover {
  width: 58.8px;
  height: 58.8px;
  overflow: hidden;
}

.content-title a {
  transition: all 0.2s;
  font-size: 95%;
}

.content-title a:hover {
  color: #ff7242 ;
}

.content-time {
  color: #858585;
  font-size: 85%;
  line-height: 2;
}
</style>

