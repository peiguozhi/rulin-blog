<template>
  <div>
    <!-- banner -->

    <div class="banner" :style="cover">
      <div class="bgShade">
        <div class="banner-title">
          面试题
          <div class="banner-intro">
            {{ obj.output }}<span class="typed-cursor">|</span>
          </div>
        </div>
      </div>
    </div>
    <v-card class="left-side-title">
      <span class="mid-title">题目分类：</span>
      <el-select v-model="cIndex"
                 @change="getQuestionList" placeholder="请选择分类"
      >
        <el-option v-for="item in categoryList" :key="item.id" :label="item.name + ' (' + item.questionNum + ')'"
                   :value="item.id" />
      </el-select>
    </v-card>

    <!-- 中间内容 -->
    <v-row class="blog-container">
      <!--   中间当前题目内容   -->
      <v-card class="article-wrapper" style="border-radius: 5px">
        <div style="position: sticky; top: 60px">
          <div ref="dragDiv" class="float-drag-button">
            <span><i class="iconfont iconhanbao" /></span>
          </div>
        </div>
        <!--题目信息-->
        <div style="overflow: hidden">
            <span class="mid-side-title"><i class="num">{{ curIndex + 1 }}</i>{{ questionList[curIndex].quContent
              }}:</span>

          <el-divider />

          <!-- 参考答案：  -->
          <div class="mid-title">参考答案</div>

          <div
            id="analysis"
            ref="analysis"
            class="analysis-content markdown-body"
            v-html="analysisContent"
          />

          <div style="float: right;margin-top: 30px">
            <el-button type="primary" :disabled="curIndex<1" @click="getAnalysis(--curIndex)">上一题</el-button>
            <el-button type="primary" :disabled="curIndex>=questionList.length-1" @click="getAnalysis(++curIndex)">下一题
            </el-button>
          </div>
        </div>
      </v-card>
    </v-row>

    <v-dialog v-model="questionFlag" width="90%">
      <v-card style="padding-top: 20px">
        <el-menu
          :default-active="curIndex.toString()"
          active-text-color="#5794F7">
          <el-menu-item
            class="right-directory"
            v-for="(item, index) in questionList" :key="item.id"
            @click="getAnalysis(curIndex = index)"
            :index="index.toString()">
            {{ index + 1 + "、" + item.quContent }}
          </el-menu-item>
        </el-menu>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
  import Clipboard from "clipboard";
  import EasyTyper from "easy-typer-js";
  import { getCategoryAndQuestionList } from "../../api";

  export default {
    created() {
      this.getQuestionList();
      this.changeHitokoto();
    },
    destroyed() {
      this.clipboard.destroy();
    },
    metaInfo: {
      meta: [{
        name: "keyWords",
        content: "儒林小栈,开源博客,www.codescholar.cn"  //变量或字符串
      }, {
        name: "description",
        content: "这是我的个人博客、会分享关于编程、写作、思考相关的任何内容，希望可以给来到这儿的人有些帮助..."
      }]
    },
    data: function() {
      return {
        analysisContent: "",
        // 从路由中获取问题id
        qCategoryId: this.$route.params.qCategoryId,
        // 从路由中获取问题id
        questionId: this.$route.params.questionId,

        // 问题目录
        questionFlag: false,

        // 题目索引
        curIndex: 0,

        // 分类索引
        cIndex: "",

        // 查询到的问题列表
        questionList: [
          {
            id: "",
            quContent: "",
            analysisMd: ""
          }
        ],
        // 问题分类列表
        categoryList: [],

        img: process.env.VUE_APP_IMG_API,
        clipboard: null,
        imgList: [],
        // 打字机特效配置变量
        obj: {
          output: "",
          isEnd: false,
          speed: 300,
          singleBack: false,
          sleep: 0,
          type: "rollback",
          backSpeed: 40,
          sentencePause: true
        }
      };
    },
    methods: {
      // 获取分类和题目信息
      getQuestionList(cid, qid) {

        if (typeof (cid) === "undefined" && typeof (qid) === "undefined") {
          cid = this.qCategoryId;
          qid = this.questionId;
        }

        if (cid === null || cid === 0 || cid === "" || typeof (cid) === "undefined") {
          this.qCategoryId = cid = 1;
        }

        getCategoryAndQuestionList(cid).then(res => {
          this.questionList = res.data.questionList;
          this.categoryList = res.data.qCategoryList;

          for (const index in this.categoryList) {
            if (this.categoryList[index].id == cid) {
              this.cIndex = this.categoryList[index].name + " (" + this.categoryList[index].questionNum + ")";
            }
          }

          if (qid !== null && qid !== 0 && qid !== "" && typeof (qid) !== "undefined") {
            for (const index in this.questionList) {
              if (this.questionList[index].id == qid) {
                this.curIndex = parseInt(index);
              }
            }
          } else {
            this.curIndex = 0;
          }
          this.getAnalysis(this.curIndex);
        });
      },

      getAnalysis(curIndex) {
        this.markdownToHtml(this.questionList[curIndex].analysisMd);
        this.$nextTick(() => {
          // 添加代码复制功能
          this.clipboard = new Clipboard(".copy-btn");
          this.clipboard.on("success", () => {
            this.$toast({ type: "success", message: "复制成功" });
          });
          // 添加图片预览功能
          const imgList = this.$refs.analysis.getElementsByTagName("img");
          for (var i = 0; i < imgList.length; i++) {
            this.imgList.push(imgList[i].src);
            imgList[i].addEventListener("click", function(e) {
              that.previewImg(e.target.currentSrc);
            });
          }
          // 关闭模态框
          this.questionFlag = false;
          // 回到顶部
          document.documentElement.scrollTop = 0;
        });
      },

      markdownToHtml(data) {
        const MarkdownIt = require("markdown-it");
        const hljs = require("highlight.js");
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
        this.analysisContent = md.render(data);
      },
      previewImg(img) {
        this.$imagePreview({
          images: this.imgList,
          index: this.imgList.indexOf(img)
        });
      },
      changeHitokoto() {
        // 加载一言名句 a 动画，b 漫画，c 游戏，d 文学，e 原创，f 来自网络，g 其他，h 影视，i 诗词，j 网易云，k 哲学，l 抖机灵
        fetch("https://v1.hitokoto.cn?c=i&c=j&c=k&c=d")
          .then(res => {
            return res.json();
          })
          .then(({ hitokoto }) => {
            this.initTyped(hitokoto);
          });
      },
      // 一言Api进行打字机循环输出效果  by  程序儒  2023年4月11日
      initTyped(input, fn, hooks) {
        const obj = this.obj;
        const typed = new EasyTyper(obj, input, fn, hooks);
      },
      step(i) {
        return "index" + i;
      }
    },
    mounted() {
      this.$nextTick(() => {
        // 获取DOM元素
        let dragDiv = this.$refs.dragDiv;
        // 缓存 clientX clientY 的对象: 用于判断是点击事件还是移动事件
        let clientOffset = {};
        // 绑定鼠标按下事件
        dragDiv.addEventListener("mousedown", (event) => {
          let offsetX = dragDiv.getBoundingClientRect().left; // 获取当前的x轴距离
          let offsetY = dragDiv.getBoundingClientRect().top; // 获取当前的y轴距离
          let innerX = event.clientX - offsetX; // 获取鼠标在方块内的x轴距
          let innerY = event.clientY - offsetY; // 获取鼠标在方块内的y轴距
          // 缓存 clientX clientY
          clientOffset.clientX = event.clientX;
          clientOffset.clientY = event.clientY;
          // 鼠标移动的时候不停的修改div的left和top值
          document.onmousemove = function(event) {
            dragDiv.style.left = event.clientX - innerX + "px";
            dragDiv.style.top = event.clientY - innerY + "px";
            // dragDiv 距离顶部的距离
            let dragDivTop = window.innerHeight - dragDiv.getBoundingClientRect().height;
            // dragDiv 距离左部的距离
            let dragDivLeft = window.innerWidth - dragDiv.getBoundingClientRect().width;
            // 边界判断处理
            // 1、设置左右不能动
            dragDiv.style.left = dragDivLeft + "px";
            // 2、超出顶部处理
            if (dragDiv.getBoundingClientRect().top <= 0) {
              dragDiv.style.top = "0px";
            }
            // 3、超出底部处理
            if (dragDiv.getBoundingClientRect().top >= dragDivTop) {
              dragDiv.style.top = dragDivTop + "px";
            }
          };
          // 鼠标抬起时，清除绑定在文档上的mousemove和mouseup事件；否则鼠标抬起后还可以继续拖拽方块
          document.onmouseup = function() {
            document.onmousemove = null;
            document.onmouseup = null;
          };
        }, false);
        // 绑定鼠标松开事件
        dragDiv.addEventListener("mouseup", (event) => {
          let clientX = event.clientX;
          let clientY = event.clientY;
          if (clientX === clientOffset.clientX && clientY === clientOffset.clientY) {
            this.questionFlag = true;
          }
        });
      });
    },
    computed: {
      cover() {
        var cover = "";
        this.$store.state.blogInfo.pageList.forEach(item => {
          if (item.pageLabel === "question") {
            cover = item.pageCover;
          }
        });
        return "background: url(" + cover + ") center center / cover no-repeat";
      }
    }
  };
</script>


<style lang="stylus">
  .typed-cursor
    opacity: 1
    animation: blink 0.7s infinite

  @keyframes blink
    0%
      opacity: 1
    50%
      opacity: 0
    100%
      opacity: 1
</style>

<style scoped>
    /*  调整首图背景不透明度  by 程序儒  2023年4月11日  */
    .bgShade {
        background-color: rgba(0, 0, 0, 0.15);
    }

    /* 首图高度调节 */
    .banner {
        height: 200px;
    }

    /* 标题文字位置 */
    .banner-title {
        top: 4.5rem;
    }

    /* 一言文字格式调整 */
    .banner-intro {
        font-size: 1.1rem;
        font-family: 'STXingkai', serif !important;
        max-width: 80%;
        margin: 0.6rem auto;
    }

    /* 主区域样式 */
    .blog-container {
        display: block;
        width: 98%;
        margin: 140px auto 0 auto;
        padding: 10px 0;
    }

    /* 数字标号样式 */
    .num {
        display: inline-block;
        background: url('../../assets/imgs/examTitle.png') no-repeat 100% 100%;
        background-size: contain;
        height: 37px;
        width: 37px;
        line-height: 30px;
        color: #fff;
        font-size: 19px;
        text-align: center;
        margin-right: 15px;
        vertical-align: middle;
    }

    /* 参考答案 */
    .mid-title {
        font-size: 1.2rem;
        margin-bottom: 5px;
        color: #5794F7;
        font-family: 'STXingkai', serif !important;
    }

    /* 中间标题 */
    .mid-side-title {
        font-size: 1.3rem;
        font-weight: 600;
        margin-bottom: 5px
    }

    /* 左边目录列表 */
    .left-directory {
        /*margin-left: 15px;*/
        font-size: 1.1rem !important;
        display: block;
        line-height: 35px;
        height: initial;
        white-space: pre-wrap;
    }

    /* 右边目录列表 */
    .right-directory {
        /*margin-left: 15px;*/
        font-size: 1.1rem !important;
        display: block;
        line-height: 25px;
        height: initial;
        white-space: pre-wrap;
    }

    #analysis {
        word-break: break-word;
        line-height: 2rem;
        font-size: 1rem;
    }

    .left-side-title {
        border-radius: 5px !important;
        position: relative;
        width: 98%;
        margin: auto;
        /*text-align: center;*/
        top: 9rem;
        padding: 5px 14px;
        font-size: 1rem;
        font-weight: bold;
    }

    ::v-deep .el-select,
    ::v-deep .el-input,
    ::v-deep .el-input__inner {
        color: #5794F7;
        border: none;
        text-align: center;
        border-radius: 4px;
        font-size: 1rem;
    }

    /*input框 右侧的箭头*/
    ::v-deep .el-select .el-input .el-select__caret {
        color: rgba(242, 224, 224, 0.5);
    }

    /*移入option选项 样式调整*/
    ::v-deep .el-select-dropdown__item.hover, .el-select-dropdown__item:hover {
        background-color: rgba(73, 208, 185, 0.27);
        color: #5794F7 !important;
    }

    ::v-deep .el-popper[x-placement^=bottom] .popper__arrow{
        margin: auto;
    }

</style>

<style lang="scss">
  pre.hljs {
    padding: 30px 2px 12px 40px !important;
    border-radius: 5px !important;
    position: relative;
    font-size: 1rem !important;
    line-height: 22px !important;
    overflow: hidden !important;
    background: #21252B !important;
    color: #f8f8f8 !important;
    box-shadow: 20px 10px 25px 0 rgb(0 0 0 / 10%);
    font-family: "Consolas", serif !important;

    &:hover .copy-btn {
      display: flex;
      justify-content: center;
      align-items: center;
    }

    code {
      display: block !important;
      margin: 0 10px !important;
      overflow-x: auto !important;
      font-family: "Consolas", serif !important;

      &::-webkit-scrollbar {
        z-index: 11;
        width: 6px;
      }

      &::-webkit-scrollbar:horizontal {
        height: 6px;
      }

      &::-webkit-scrollbar-thumb {
        border-radius: 5px;
        width: 6px;
        background: #666;
      }

      &::-webkit-scrollbar-corner,
      &::-webkit-scrollbar-track {
        background: #1e1e1e;
      }

      &::-webkit-scrollbar-track-piece {
        background: #1e1e1e;
        width: 6px;
      }
    }

    .line-numbers-rows {
      position: absolute;
      pointer-events: none;
      top: 12px;
      bottom: 12px;
      left: 0;
      font-size: 100%;
      width: 40px;
      text-align: center;
      letter-spacing: -1px;
      border-right: 1px solid rgba(0, 0, 0, 0.66);
      user-select: none;
      counter-reset: linenumber;

      span {
        pointer-events: none;
        display: block;
        counter-increment: linenumber;

        &:before {
          content: counter(linenumber);
          color: #999;
          display: block;
          text-align: center;
        }
      }
    }

    b.name {
      position: absolute;
      top: 7px;
      right: 45px;
      z-index: 1;
      color: #999;
      pointer-events: none;
    }

    .copy-btn {
      position: absolute;
      top: 6px;
      right: 6px;
      z-index: 1;
      color: #ccc;
      background-color: #525252;
      border-radius: 6px;
      display: none;
      font-size: 16px;
      width: 32px;
      height: 24px;
      outline: none;
    }
  }


  .hljs-ln {
    display: inline-block;
    overflow-x: auto;
    padding-bottom: 5px
  }

  .hljs-ln td {
    padding: 0;
    background-color: #21252B
  }

  .hljs-ln::-webkit-scrollbar {
    height: 10px;
    border-radius: 5px;
    background: #333;
  }

  .hljs-ln::-webkit-scrollbar-thumb {
    background-color: #bbb;
    border-radius: 5px;
  }

  .hljs-ln::-webkit-scrollbar-thumb:hover {
    background: #ddd;
  }

  .hljs table tbody tr {
    border: none
  }

  .hljs .hljs-ln-line {
    padding: 1px 10px;
    border: none
  }

  td.hljs-ln-line.hljs-ln-numbers {
    border-right: 1px solid #666;
  }

  .hljs-keyword, .hljs-literal, .hljs-symbol, .hljs-name {
    color: #f92672
  }

  .hljs-link {
    color: #569cd6;
    text-decoration: underline
  }

  .hljs-built_in, .hljs-type {
    color: #66d9ef
  }

  .hljs-number, .hljs-class {
    color: #b8d7a3
  }

  .hljs-string, .hljs-meta-string {
    color: #E6DB74
  }

  .hljs-regexp, .hljs-template-tag {
    color: #9a5334
  }

  .hljs-subst, .hljs-function, .hljs-title, .hljs-params, .hljs-formula {
    color: #66d9ef
  }

  /*注释颜色*/
  /*  .hljs-comment, .hljs-quote {
      color: #57a64a;
      font-style: italic
    }*/

  .hljs-doctag {
    color: #608b4e
  }

  .hljs-meta, .hljs-meta-keyword, .hljs-tag {
    color: #9b9b9b
  }

  .hljs-variable, .hljs-template-variable {
    color: #bd63c5
  }

  .hljs-attr, .hljs-attribute, .hljs-builtin-name {
    color: #9cdcfe
  }

  .hljs-section {
    color: gold
  }

  .hljs-emphasis {
    font-style: italic
  }

  .hljs-strong {
    font-weight: bold
  }

  .hljs-bullet, .hljs-selector-tag, .hljs-selector-id, .hljs-selector-class, .hljs-selector-attr, .hljs-selector-pseudo {
    color: #d7ba7d
  }

  .hljs-addition {
    background-color: #144212;
    display: inline-block;
    width: 100%
  }

  .hljs-deletion {
    background-color: #600;
    display: inline-block;
    width: 100%
  }

  .float-drag-button {
    position: absolute;
    right: 0;
    top: -30px;
    z-index: 6666;
    padding: 5px;
    width: 40px;
    opacity: 0.6;
    background-color: #38abab;
    border-radius: 8px 0px 0px 8px;
    box-shadow: 0px 2px 15px 0px rgba(11, 45, 84, 0.2);
    cursor: move;

    span {
      font-size: 1.7rem;
      color: #ffffff;
      line-height: 24px;
      user-select: none;
    }
  }

</style>
