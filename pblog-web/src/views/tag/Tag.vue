<template>
  <div>
    <!-- banner -->
    <div class="banner" :style="cover">
      <div class="bgShade">
        <h1 class="banner-title">标签</h1>
      </div>
    </div>
    <!-- 标签列表 -->
    <v-card class="blog-container">
      <div class="tag-cloud-title">标签 - {{ tagList.length }}</div>
      <div class="tag-cloud">
        <router-link
          :style="{ 'font-size': Math.floor(Math.random() * 10) + 18 + 'px' }"
          v-for="item of tagList"
          :key="item.id"
          :to="'/tags/' + item.id"
        >
          {{ item.name }}
        </router-link>
      </div>
    </v-card>
  </div>
</template>

<script>
  import { getTags } from "../../api";

  export default {
    metaInfo: {
      meta: [{
        name: "keyWords",
        content: "儒林小栈,开源博客,www.codescholar.cn"  //变量或字符串
      }, {
        name: "description",
        content: "一个专注于技术分享的博客平台,大家以共同学习,乐于分享,拥抱开源的价值观进行学习交流"
      }]
    },
    created() {
      this.listTags();
    },
    data: function() {
      return {
        tagList: [],
        count: 0
      };
    },
    methods: {
      listTags() {
        getTags().then(res => {
          this.tagList = res.data;
        });
      }
    },
    computed: {
      cover() {
        var cover = "";
        this.$store.state.blogInfo.pageList.forEach(item => {
          if (item.pageLabel === "tag") {
            cover = item.pageCover;
          }
        });
        return "background: url(" + cover + ") center center / cover no-repeat";
      }
    }
  };
</script>

<style scoped>
    .tag-cloud-title {
        line-height: 2;
        font-size: 36px;
        text-align: center;
    }

    @media (max-width: 759px) {
        .tag-cloud-title {
            font-size: 25px;
        }
    }

    .tag-cloud {
        text-align: center;
    }

    .tag-cloud a {
        color: #616161;
        display: inline-block;
        text-decoration: none;
        padding: 0 8px;
        line-height: 2;
        transition: all 0.3s;
    }

    .tag-cloud a:hover {
        color: #03a9f4 !important;
        transform: scale(1.1);
    }
</style>
