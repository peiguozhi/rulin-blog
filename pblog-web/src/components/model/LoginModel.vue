<template>
  <v-dialog v-model="loginFlag" max-width="460">
    <v-card class="login-container" style="border-radius:5px">
      <v-icon class="float-right" @click="loginFlag = false">
        mdi-close
      </v-icon>
      <div class="login-wrapper">
        <!-- 用户名 -->
        <v-text-field
          v-model="username"
          label="邮箱号"
          :disabled="false"
          placeholder="请输入您的邮箱号"
          clearable
          @keyup.enter="login"
        />
        <!-- 密码 -->
        <v-text-field
          v-model="password"
          class="mt-7"
          label="密码"
          :disabled="false"
          placeholder="请输入您的密码"
          @keyup.enter="login"
          :append-icon="show ? 'mdi-eye' : 'mdi-eye-off'"
          :type="show ? 'text' : 'password'"
          @click:append="show = !show"
        />
        <!-- 按钮 -->
        <v-btn
          class="mt-7"
          block
          color="blue"
          :disabled="false"
          style="color:#fff"
          @click="login"
        >
          登录
        </v-btn>
        <!-- 注册和找回密码 -->
        <div class="mt-10 login-tip">
          <!--          <span  @click="openRegister">立即注册</span>
                    <span  @click="openForget" class="float-right">忘记密码?</span> -->
          <span @click="openRegister">立即注册</span>
          <span @click="openForget" class="float-right">忘记密码?</span>
        </div>
        <div>
          <div class="social-login-title">社交账号登录</div>
          <div class="social-login-wrapper">
            <!-- 微博登录 -->
            <a
              v-if="showLogin(3)"
              class="mr-3 iconfont iconweibo"
              style="color:#e05244"
              @click="weiboLogin"
            />
            <!-- 码云登录 -->
            <a
              class="mr-3 iconfont icongitee-fill-round"
              style="color:#e05244"
              v-if="showLogin(4)"
              @click="giteeLogin"
            />
            <!-- qq登录 -->
            <a
              v-if="showLogin(2)"
              class="iconfont iconqq"
              style="color:#00AAEE"
              @click="qqLogin"
            />
          </div>
        </div>
      </div>
    </v-card>
  </v-dialog>
</template>

<script>
import {emailLogin } from "../../api";
export default {
  data: function() {
    return {
      username: "",
      password: "",
      show: false
    };
  },
  computed: {
    loginFlag: {
      set(value) {
        this.$store.state.loginFlag = value;
      },
      get() {
        return this.$store.state.loginFlag;
      }
    },
    isMobile() {
      const clientWidth = document.documentElement.clientWidth;
      if (clientWidth > 960) {
        return false;
      }
      return true;
    },
    socialLoginList() {
      return this.$store.state.blogInfo.webSite.loginTypeList;
    },
    showLogin() {
      return function(type) {
        return this.socialLoginList.indexOf(type) != -1;
      };
    }
  },
  methods: {
    openRegister() {
      this.$store.state.loginFlag = false;
      this.$store.state.registerFlag = true;
    },
    openForget() {
      this.$store.state.loginFlag = false;
      this.$store.state.forgetFlag = true;
    },
    login() {
      var reg = /^[A-Za-z0-9\u4e00-\u9fa5_]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/;
      if (!reg.test(this.username)) {
        this.$toast({ type: "error", message: "邮箱格式不正确" });
        return false;
      }
      if (this.password.trim().length === 0) {
        this.$toast({ type: "error", message: "密码不能为空" });
        return false;
      }
      //发送登录请求
      emailLogin({email:this.username,password:this.password}).then(res => {
        this.username = "";
        this.password = "";
        this.$store.commit("login", res.data);
        this.$store.commit("closeModel");
        this.$toast({ type: "success", message: "登录成功" });
      }).catch(err =>{
        this.$toast({ type: "error", message: err.message });
      });
    },
    qqLogin() {
      //保留当前路径
      this.$store.commit("saveLoginUrl", this.$route.path);
      if (
        navigator.userAgent.match(
          /(iPhone|iPod|Android|ios|iOS|iPad|Backerry|WebOS|Symbian|Windows Phone|Phone)/i
        )
      ) {
        // eslint-disable-next-line no-undef
        QC.Login.showPopup({
          appId: this.config.QQ_CLIENT_ID,
          redirectURI: this.config.QQ_REDIRECT_URL
        });
      } else {
        window.open(
          "https://graph.qq.com/oauth2.0/show?which=Login&display=pc&client_id=" +
          + this.config.QQ_CLIENT_ID +
          "&response_type=token&scope=all&redirect_uri=" +
          this.config.QQ_REDIRECT_URL,
          "_self"
        );
      }
    },
    giteeLogin() {
      //保留当前路径
      this.$store.commit("saveLoginUrl", this.$route.path);
      window.open(
        "https://gitee.com/oauth/authorize?client_id=" +
        this.config.GITEE_CLIENT_ID +
        "&response_type=code&redirect_uri=" +
        this.config.GITEE_REDIRECT_URL,
        "_self"
      );
    },
    weiboLogin() {
      //保留当前路径
      this.$store.commit("saveLoginUrl", this.$route.path);
      window.open(
        "https://api.weibo.com/oauth2/authorize?client_id=" +
        this.config.WEIBO_CLIENT_ID +
        "&response_type=code&redirect_uri=" +
        this.config.WEIBO_REDIRECT_URL,
        "_self"
      );
    }
  }
};
</script>

<style scoped>
.social-login-title {
  margin-top: 1.5rem;
  color: #b5b5b5;
  font-size: 0.75rem;
  text-align: center;
}

.social-login-title::before {
  content: "";
  display: inline-block;
  background-color: #d8d8d8;
  width: 60px;
  height: 1px;
  margin: 0 12px;
  vertical-align: middle;
}

.social-login-title::after {
  content: "";
  display: inline-block;
  background-color: #d8d8d8;
  width: 60px;
  height: 1px;
  margin: 0 12px;
  vertical-align: middle;
}

.social-login-wrapper {
  margin-top: 1rem;
  font-size: 2rem;
  text-align: center;
}

.social-login-wrapper a {
  text-decoration: none;
}

@media (max-width: 759px) {
    .login-container {
        height: 48vh;
        overflow: auto;
        padding: 0.5rem;
    }
    .login-wrapper {
        padding: 4vh 0.5em 0 0.5rem;
    }
}

</style>
