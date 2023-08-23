<template>
  <div>
    <Loading></Loading>
    <!-- banner -->
    <div class="banner" :style="cover">
      <div class="bgShade">
        <h1 class="banner-title">归档</h1>
      </div>
    </div>
    <!-- 归档列表 -->
    <v-card class="blog-container">
      <timeline>
        <timeline-title> 目前共计{{ count }}篇文章，继续加油 </timeline-title>
        <timeline-item v-for="item of archiveList" :key="item.id">
          <!-- 日期 -->
          <span class="time">{{ item.createTime | date }}</span>
          <!-- 文章标题 -->
          <router-link
            :to="'/articles/' + item.id"
            style="color:#666;text-decoration: none"
          >
            {{ item.title }}
          </router-link>
        </timeline-item>
      </timeline>
      <!-- 分页按钮 -->
      <v-pagination
        color="#00C4B6"
        v-model="current"
        :length="Math.ceil(count / 10)"
        total-visible="7"
      />
    </v-card>
  </div>
</template>

<script>
import { Timeline, TimelineItem, TimelineTitle } from "vue-cute-timeline";
import {getArchive} from '../../api'
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
  created() {
    this.listArchives();
  },
  components: {
    Timeline,
    TimelineItem,
    Loading,
    TimelineTitle
  },
  data: function() {
    return {
      count: 0,
      current: 1,
      archiveList: [],
    };
  },
  methods: {
    listArchives() {
      getArchive({pageNo:this.current,pageSize:10}).then(res => {
          this.archiveList = res.data.records;
          this.count =  res.data.total;
        });
    }
  },
  computed: {
    cover() {
      var cover = "";
      this.$store.state.blogInfo.pageList.forEach(item => {
        if (item.pageLabel === "archive") {
          cover = item.pageCover;
        }
      });
      return "background: url(" + cover + ") center center / cover no-repeat";
    }
  },
  watch: {
    current(value) {
      this.current=value
      this.listArchives()
    }
  }
};
</script>

<style scoped>
.time {
  font-size: 0.75rem;
  color: #555;
  margin-right: 1rem;
}
</style>
