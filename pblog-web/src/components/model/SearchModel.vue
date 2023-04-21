<template>
  <!-- 搜索框 -->
  <v-dialog v-model="searchFlag" max-width="600">
    <v-card class="search-wrapper" style="border-radius:4px">
      <div class="mb-3">
        <span :class="0 === select ? 'search-title-selected':'search-title'" :key="0" @click="tab(0)">文章搜索</span>
        <span :class="1 === select ? 'search-title-selected':'search-title'" :key="1" @click="tab(1)">面试题搜索</span>
        <!-- 关闭按钮 -->
        <v-icon class="float-right" @click="searchFlag = false">
          mdi-close
        </v-icon>
      </div>
      <!--   文章搜索   -->
      <div id="archive" v-show="select === 0">
        <!-- 输入框 -->
        <div class="search-input-wrapper">
          <v-icon>mdi-magnify</v-icon>
          <input v-model="keywords" placeholder="输入文章标题或内容..." />
        </div>

        <!-- 搜索结果 -->
        <div class="search-result-wrapper">
          <hr class="divider" />
          <ul>
            <li class="search-reslut" v-for="item of articleList" :key="item.id">
              <!-- 文章标题 -->
              <a @click="goTo(item.id)" v-html="item.title" />
              <!-- 文章内容 -->
              <p
                class="search-reslut-content text-justify"
                v-html="item.content"
              />
            </li>
          </ul>
          <!-- 搜索结果不存在提示 -->
          <div
            v-show="flag && articleList.length == 0"
            style="font-size:0.875rem"
          >
            找不到您查询的内容：{{ keywords }}
          </div>
        </div>
        <!--      <div style="margin-top: 10px">
              <span style="font-weight: bold">每日热搜</span><br/>
              <div style="display:inline-block;background-color: #b3b3b3;color: rgb(102,102,102);border-radius: 10px;margin-left: 10px;margin-top: 8px">
                <span style="padding: 15px">博客112</span>
              </div>
            </div>-->
      </div>

      <!--   面试题搜索   -->
      <div id="question" v-show="select === 1">
        <!-- 输入框 -->
        <div class="search-input-wrapper">
          <v-icon>mdi-magnify</v-icon>
          <input v-model="keywords" placeholder="输入问题..." />
        </div>

        <!-- 搜索结果 -->
        <div class="search-result-wrapper">
          <hr class="divider" />
          <ul>
            <li class="search-reslut" v-for="item of questionList" :key="item.id">
              <!-- 问题 -->
              <a @click="goToQuestion(item.qCategoryId, item.id)" v-html="item.quContent" />
              <!-- 答案内容 -->
              <p
                class="search-reslut-content text-justify"
                v-html="item.analysis"
              />
            </li>
          </ul>
          <!-- 搜索结果不存在提示 -->
          <div
            v-show="flag && questionList.length == 0"
            style="font-size:0.875rem"
          >
            找不到您查询的内容：{{ keywords }}
          </div>
        </div>
      </div>

    </v-card>
  </v-dialog>
</template>

<script>
  import { searchArticle, searchQuestion } from "../../api";

  export default {
    data: function() {
      return {
        select: 0, //点击后的值，与下标同步，为0表示默认第一个按钮与div为选中状态
        keywords: "",
        articleList: [],
        questionList: [],
        flag: false
      };
    },
    methods: {
      goTo(articleId) {
        this.$store.state.searchFlag = false;
        this.$router.push({ path: "/articles/" + articleId });
      },
      goToQuestion(cid, qid) {
        this.$store.state.searchFlag = false;
        if (document.body.offsetWidth > 959){
          this.$router.push({ path: "/questions/" + cid + "/" + qid });
        } else {
          this.$router.push({ path: "/questions-app/" + cid + "/" + qid });
        }
      },
      //定义切换方法
      tab(index) {
        this.select = index;
      }
    },
    computed: {
      searchFlag: {
        set(value) {
          this.keywords = "";
          this.articleList = [];
          this.questionList = [];
          this.$store.state.searchFlag = value;
        },
        get() {
          return this.$store.state.searchFlag;
        }
      },
      isMobile() {
        const clientWidth = document.documentElement.clientWidth;
        return clientWidth <= 960;
      }
    },
    watch: {
      keywords(value) {
        this.flag = value.trim() !== "";
        if (this.flag) {
          if (this.select === 0) {
            searchArticle(value).then(res => {
              this.articleList = res.data;
            });
          } else {
            searchQuestion(value).then(res => {
              this.questionList = res.data;
            });
          }
        }
      }
    }
  };
</script>

<style scoped>
    .search-wrapper {
        padding: 1.25rem;
        height: 100%;
        background: #fff !important;
    }

    .search-title {
        color: #49b1f5;
        font-size: 1.25rem;
        line-height: 1;
        margin-right: 1rem;
    }

    .search-title-selected {
        color: #49b1f5;
        font-size: 1.25rem;
        line-height: 1;
        margin-right: 1rem;
        border-bottom: 5px solid #db4c2b;
    }

    .search-input-wrapper {
        display: flex;
        padding: 5px;
        height: 35px;
        width: 100%;
        border: 2px solid #8e8cd8;
        border-radius: 2rem;
    }

    .search-input-wrapper input {
        width: 100%;
        margin-left: 5px;
        outline: none;
    }

    @media (min-width: 960px) {
        .search-result-wrapper {
            padding-right: 5px;
            height: 450px;
            overflow: auto;
        }
    }

    @media (max-width: 959px) {
        .search-result-wrapper {
            height: 40vh;
            overflow: auto;
        }
    }

    .search-reslut a {
        color: #555;
        font-weight: bold;
        border-bottom: 1px solid #999;
        text-decoration: none;
    }

    .search-reslut-content {
        color: #555;
        cursor: pointer;
        border-bottom: 1px dashed #ccc;
        padding: 5px 0;
        line-height: 2;
        overflow: hidden;
        text-overflow: ellipsis;
        display: -webkit-box;
        -webkit-line-clamp: 3;
        -webkit-box-orient: vertical;
    }

    .divider {
        margin: 20px 0;
        border: 2px dashed #d2ebfd;
    }
</style>
