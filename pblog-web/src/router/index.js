import Vue from "vue";
import VueRouter from "vue-router";

Vue.use(VueRouter);

const routes = [
  {
    path: "/",
    component: resolve => require(["../views/home/Home.vue"], resolve)
  },
  {
    path: "/articles/:articleId",
    component: resolve => require(["../views/article/Article.vue"], resolve)
  },
  {
    path: "/archives",
    component: resolve => require(["../views/archive/Archive.vue"], resolve),
    meta: {
      title: "归档"
    }
  },
  {
    path: "/albums",
    component: resolve => require(["../views/album/Album.vue"], resolve),
    meta: {
      title: "相册"
    }
  },
  {
    path: "/albums/:albumId",
    component: resolve => require(["../views/album/Photo.vue"], resolve)
  },
  {
    path: "/tags",
    component: resolve => require(["../views/tag/Tag.vue"], resolve),
    meta: {
      title: "标签"
    }
  },
  {
    path: "/categories",
    component: resolve => require(["../views/category/Category.vue"], resolve),
    meta: {
      title: "分类"
    }
  },
  {
    path: "/categories/:categoryId",
    component: resolve => require(["../views/article/ArticleList.vue"], resolve)
  },
  {
    path: "/tags/:tagId",
    component: resolve => require(["../views/article/ArticleList.vue"], resolve)
  },
  {
    path: "/links",
    component: resolve => require(["../views/link/Link.vue"], resolve),
    meta: {
      title: "友链列表"
    }
  },
  {
    path: "/about",
    component: resolve => require(["../views/about/About.vue"], resolve),
    meta: {
      title: "关于我"
    }
  },
  {
    path: "/message",
    component: resolve => require(["../views/message/Message.vue"], resolve),
    meta: {
      title: "留言板"
    }
  },
  {
    path: "/user",
    component: resolve => require(["../views/user/User.vue"], resolve),
    meta: {
      title: "个人中心"
    }
  },
  {
    path: "/callback/qq",
    component: resolve => require(["../components/OauthLogin.vue"], resolve)
  },
  {
    path: "/callback/gitee",
    component: resolve => require(["../components/OauthLogin.vue"], resolve)
  },
  {
    path: "/callback/weibo",
    component: resolve => require(["../components/OauthLogin.vue"], resolve)
  },
  {
    path: "/questions",
    component: resolve => require(["../views/question/Question.vue"], resolve)
  },
  {
    path: "/questions/:qCategoryId/:questionId",
    component: resolve => require(["../views/question/Question.vue"], resolve)
  },
  {
    path: "/questions-app",
    component: resolve => require(["../views/question/Question-app.vue"], resolve)
  },
  {
    path: "/questions-app/:qCategoryId/:questionId",
    component: resolve => require(["../views/question/Question-app.vue"], resolve)
  },
  {
    path: "/favoriteQuestions",
    component: resolve => require(["../views/question/FavoriteQuestion.vue"], resolve)
  },
  {
    path: "/favoriteQuestions-app",
    component: resolve => require(["../views/question/FavoriteQuestion-app.vue"], resolve)
  },
  // 访问意料之外的路由时重定向到/personal
  {
    path: "*",
    redirect: "/"
  }
];

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes
});

export default router;
