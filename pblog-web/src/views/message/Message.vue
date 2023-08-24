<template>
  <div>
    <Loading></Loading>
    <!-- banner -->
    <div class="message-banner" :style="cover">
      <div class="bgShade">
        <!-- 弹幕输入框 -->
        <div class="message-container">
          <h1 class="message-title">留言板</h1>
          <div class="animated fadeInUp message-input-wrapper">
            <input
              v-model="content"
              @click="show = true"
              @keyup.enter="addToList"
              placeholder="说点什么吧"
            />
            <button
              class="ml-3 animated bounceInLeft"
              @click="addToList"
              v-show="show"
            >
              发送
            </button>
          </div>
        </div>
        <!-- 弹幕列表 -->
        <div class="barrage-container">
          <vue-baberrage :barrageList="barrageList">
            <template v-slot:default="slotProps">
            <span class="barrage-items">
              <img
                :src="slotProps.item.avatar"
                width="30"
                height="30"
                style="border-radius:50%"
              />
              <span class="ml-2">{{ slotProps.item.nickname }} :</span>
              <span class="ml-2">{{ slotProps.item.content }}</span>
            </span>
            </template>
          </vue-baberrage>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import { listMessage, addMessage } from "../../api";
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
    mounted() {
      this.listMessage();
    },
    data() {
      return {
        show: false,
        img: process.env.VUE_APP_IMG_API,
        content: "",
        count: null,
        timer: null,
        barrageList: []
      };
    },
    methods: {
      addToList() {
        if (this.count) {
          this.$toast({ type: "error", message: "30秒后才能再次留言" });
          return false;
        }
        if (this.content.trim() === "") {
          this.$toast({ type: "error", message: "留言不能为空" });
          return false;
        }
        const userAvatar = this.$store.state.avatar
          ? this.$store.state.avatar
          : this.$store.state.blogInfo.webSite.touristAvatar;
        const userNickname = this.$store.state.nickname
          ? this.$store.state.nickname
          : "游客";
        var message = {
          avatar: userAvatar,
          status: 1,
          nickname: userNickname,
          content: this.content,
          time: Math.floor(Math.random() * (10 - 7)) + 7
        };
        this.barrageList.push(message);
        this.content = "";
        addMessage(message);
        const TIME_COUNT = 30;
        if (!this.timer) {
          this.count = TIME_COUNT;
          this.timer = setInterval(() => {
            if (this.count > 0 && this.count <= 30) {
              this.count--;
            } else {
              clearInterval(this.timer);
              this.timer = null;
            }
          }, 1000);
        }
      },
      listMessage() {
        listMessage().then(res => {
          this.barrageList = res.data;
        });
      }
    },
    computed: {
      cover() {
        var cover = "";
        this.$store.state.blogInfo.pageList.forEach(item => {
          if (item.pageLabel == "message") {
            cover = item.pageCover;
          }
        });
        return "background: url(" + cover + ") center center / cover no-repeat";
      }
    }
  };
</script>

<style scoped>
    .message-banner {
        position: absolute;
        top: -60px;
        left: 0;
        right: 0;
        height: 100vh;
        background-color: #49b1f5;
        animation: header-effect 1s;
    }

    .message-title {
        color: #eee;
        animation: title-scale 1s;
        font-family: 'STXingkai', 'pgz', serif !important;
    }

    .message-container {
        position: absolute;
        width: 360px;
        top: 35%;
        left: 0;
        right: 0;
        text-align: center;
        z-index: 5;
        margin: 0 auto;
        color: #fff;
    }

    .message-input-wrapper {
        display: flex;
        justify-content: center;
        height: 2.5rem;
        margin-top: 2rem;
    }

    .message-input-wrapper input {
        outline: none;
        width: 70%;
        border-radius: 20px;
        height: 100%;
        padding: 0 1.25rem;
        color: #eee;
        border: #fff 1px solid;
    }

    .message-input-wrapper input::-webkit-input-placeholder {
        color: #eeee;
    }

    .message-input-wrapper button {
        outline: none;
        border-radius: 20px;
        height: 100%;
        padding: 0 1.25rem;
        border: #fff 1px solid;
    }

    .barrage-container {
        position: absolute;
        top: 50px;
        left: 0;
        right: 0;
        bottom: 0;
        height: calc(100% - 50px);
        width: 100%;
    }

    .barrage-items {
        background: rgba(0, 0, 0, 0.7);
        border-radius: 100px;
        color: #fff;
        padding: 5px 10px 5px 5px;
        align-items: center;
        display: flex;
    }
</style>
