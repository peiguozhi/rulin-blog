import Vue from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import vuetify from "./plugins/vuetify";
import animated from "animate.css";
import "./assets/css/index.css";
import "./assets/css/iconfont.css";
import "./assets/css/markdown.css";
import "./assets/css/vue-social-share/client.css";
import config from "./assets/js/config";
import Share from "vue-social-share";
import dayjs from "dayjs";
import { vueBaberrage } from "vue-baberrage";
import axios from "axios";
import VueAxios from "vue-axios";
import InfiniteLoading from "vue-infinite-loading";
import "highlight.js/styles/monokai-sublime.css";
import VueImageSwipe from "vue-image-swipe";
import "vue-image-swipe/dist/vue-image-swipe.css";
import Toast from "./components/toast/index";
import NProgress from "nprogress";
import "nprogress/nprogress.css";
import ElementUI from "element-ui";
import "element-ui/lib/theme-chalk/index.css";
import MetaInfo from "vue-meta-info";
import "./assets/font/font.css";
import "@/assets/css/mac.scss";
import Loading from '@/components/loading/loading';
Vue.use(MetaInfo);
Vue.prototype.config = config;
Vue.config.productionTip = false;
Vue.use(ElementUI);
Vue.use(animated);
Vue.use(Share);
Vue.use(vueBaberrage);
Vue.use(InfiniteLoading);
Vue.use(VueAxios, axios);
Vue.use(VueImageSwipe);
Vue.use(Toast);
// 注册全局组件
Vue.component('loading', Loading);
// 创建一个 Vue 实例作为事件总线
Vue.prototype.$bus = new Vue();

Vue.filter("date", function(value) {
  return dayjs(value).format("YYYY-MM-DD");
});

Vue.filter("year", function(value) {
  return dayjs(value).format("YYYY");
});

Vue.filter("hour", function(value) {
  return dayjs(value).format("HH:mm:ss");
});

Vue.filter("num", function(value) {
  if (value >= 1000) {
    return (value / 1000).toFixed(1) + "k";
  }
  return value;
});

router.beforeEach((to, from, next) => {
  if (to.path) {
    if (window._hmt) {
      window._hmt.push(["_trackPageview", to.fullPath]);
    }
  }
  NProgress.start();
  if (to.meta.title) {
    document.title = to.meta.title;
  }
  next();
});

router.afterEach(() => {
  window.scrollTo({
    top: 0,
    behavior: "instant"
  });
  NProgress.done();
});

axios.interceptors.response.use(
  function(response) {
    switch (response.data.code) {
      case 50000:
        Vue.prototype.$toast({ type: "error", message: "系统异常" });
    }
    return response;
  },
  function(error) {
    return Promise.reject(error);
  }
);

window.vm = new Vue({
  router,
  store,
  vuetify,
  render: h => h(App)
}).$mount("#app");
