<template>
  <div>
    <Loading></Loading>
    <!-- banner -->
    <div class="banner" :style="cover">
      <div class="bgShade">
        <h1 class="banner-title">分类</h1>
      </div>
    </div>
    <!-- 分类列表 -->
    <v-card class="blog-container">
      <div class="category-title">分类 - {{ count }}</div>
      <ul class="category-list">
        <li
          class="category-list-item"
          v-for="item of categoryList"
          :key="item.id"
        >
          <router-link :to="'/categories/' + item.id">
            {{ item.name }}
            <span class="category-count">({{ item.articleNum }})</span>
          </router-link>
        </li>
      </ul>
    </v-card>
  </div>
</template>

<script>
  import { getCategory } from "../../api";
  import Loading from "@/components/loading/loading";

  export default {
    components: {
      Loading
    },
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
      this.listCategories();
    },
    data: function() {
      return {
        categoryList: [],
        count: 0
      };
    },
    methods: {
      listCategories() {
        getCategory().then(res => {
          this.categoryList = res.data;
          this.count = res.data.length;
        });
      }
    },
    computed: {
      cover() {
        var cover = "";
        this.$store.state.blogInfo.pageList.forEach(item => {
          if (item.pageLabel === "category") {
            cover = item.pageCover;
          }
        });
        return "background: url(" + cover + ") center center / cover no-repeat";
      }
    }
  };
</script>

<style scoped>
    .category-title {
        text-align: center;
        font-size: 36px;
        line-height: 2;
    }

    @media (max-width: 759px) {
        .category-title {
            font-size: 28px;
        }
    }

    .category-list {
        margin: 0 1.8rem;
        list-style: none;
    }

    .category-list-item {
        padding: 8px 1.8rem 8px 0;
    }

    .category-list-item:before {
        display: inline-block;
        position: relative;
        left: -0.75rem;
        width: 12px;
        height: 12px;
        border: 0.2rem solid #49b1f5;
        border-radius: 50%;
        background: #fff;
        content: "";
        transition-duration: 0.3s;
    }

    .category-list-item:hover:before {
        border: 0.2rem solid #ff7242;
    }

    .category-list-item a:hover {
        transition: all 0.3s;
        color: #8e8cd8;
    }

    .category-list-item a:not(:hover) {
        transition: all 0.3s;
    }

    .category-count {
        margin-left: 0.5rem;
        font-size: 0.75rem;
        color: #858585;
    }
</style>
