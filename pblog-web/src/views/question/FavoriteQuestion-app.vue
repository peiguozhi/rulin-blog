<template>
  <div>
    <Loading></Loading>
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
              }}</span>

          <el-divider />

          <!-- 参考答案：  -->
          <div class="mid-title">参考答案</div>

          <div
            id="analysis"
            ref="analysis"
            class="analysis-content markdown-body"
            v-html="analysisContent"
          />

          <div style="float: left;margin-top: 30px">
              <el-button
                @click="isFavorite"
                v-if="!showFavorite"
                style="border: none;padding: 8px 4px;font-size: 1.5rem"
                class="animated swing"
                icon="el-icon-star-off" />

              <el-button
                @click="isFavorite"
                v-if="showFavorite"
                style="border: none;padding: 8px 4px;font-size: 1.5rem"
                class="animated swing"
                type="warning"
                icon="el-icon-star-off" />

          </div>
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
  import { favoriteQuestion, getCategoryAndQuestionList, getFavoriteQuestionList } from "../../api";
  import hljs from "../../plugins/hightLight/index";
  import Loading from "@/components/loading/loading";


  export default {
    components: {
      Loading
    },
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
        showFavorite: false,
        analysisContent: "",

        // 问题目录
        questionFlag: false,

        // 题目索引
        curIndex: 0,

        // 查询到的问题列表
        questionList: [
          {
            id: "",
            quContent: "",
            analysisMd: "",
            isFavorite: 0
          }
        ],

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
      // 收藏方法
      isFavorite() {
        if (this.showFavorite) {
          favoriteQuestion(this.questionList[this.curIndex].id, 0).then(res => {
            if (this.curIndex > 0){
              this.curIndex--;
            }
            this.getQuestionList();
          }).catch(err => {
            console.log(err);
          });
        }
      },
      // 获取分类和题目信息
      getQuestionList() {
        getFavoriteQuestionList().then(res => {
          if (res.data.length === 0) {
            this.questionList[0] = {id: "0", quContent: "没有问题了，快去收藏吧", analysisMd: "你是最棒的！！", isFavorite: 0};
          } else {
            this.questionList = res.data;
          }
          this.getAnalysis(this.curIndex);
        });
      },

      getAnalysis(curIndex) {
        const that = this;
        if (that.questionList[curIndex].isFavorite === 1){
          that.showFavorite = true;
        } else {
          that.showFavorite = false;
        }
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
