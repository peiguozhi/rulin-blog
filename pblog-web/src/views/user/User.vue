<template>
  <div>
    <div class="banner" :style="cover">
      <div class="bgShade">
        <h1 class="banner-title">个人中心</h1>
      </div>
    </div>
    <v-card class="blog-container">
      <div>
        <span class="info-title">基本信息</span>
      </div>
      <v-row class="info-wrapper">
        <v-col md="3" cols="12">
          <!--          <button id="pick-avatar">
                      <v-avatar size="140">
                        <img :src="this.$store.state.avatar" />
                      </v-avatar>
                    </button>
                    <avatar-cropper
                      @uploaded="uploadAvatar"
                      trigger="#pick-avatar"
                      upload-url="http://localhost:8888/rulin/user/updateAvatar"
                    />-->
          <el-upload
            class="avatar-uploader"
            :show-file-list="false"
            ref="upload"
            name="filedatas"
            :action="uploadPictureHost"
            :before-upload="uploadBefore"
            :http-request="uploadSectionFile"
            multiple>
            <img style="border-radius: 50%;width: 120px;" v-if="this.$store.state.avatar"
                 :src="this.$store.state.avatar" class="imgAvatar">
            <i v-else class="el-icon-plus avatar-img-icon"></i>
          </el-upload>
        </v-col>
        <v-col md="7" cols="12">
          <v-text-field
            v-model="userInfo.nickname"
            label="昵称"
            placeholder="请输入您的昵称"
          />
          <v-text-field
            v-model="userInfo.webSite"
            class="mt-7"
            label="个人网站"
            placeholder="http://你的网址"
          />
          <v-text-field
            v-model="userInfo.intro"
            class="mt-7"
            label="简介"
            placeholder="介绍下自己吧"
          />
          <div v-if="loginType !== 0" class="mt-7 binding">
            <v-text-field
              disabled
              v-model="email"
              label="邮箱号"
              placeholder="请绑定邮箱"
            />
            <v-btn v-if="email" text small @click="openEmailModel">
              修改绑定
            </v-btn>
            <v-btn v-else text small @click="openEmailModel">
              绑定邮箱
            </v-btn>
          </div>
          <v-btn @click="updateUserInfo" outlined class="mt-5">修改</v-btn>
        </v-col>
      </v-row>
    </v-card>
  </div>
</template>

<script>
  import { updateUser, upload } from "../../api";
  import AvatarCropper from "vue-avatar-cropper";

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
    components: { AvatarCropper },
    data: function() {
      return {
        uploadPictureHost: process.env.VUE_APP_BASE_API + "/file/upload",
        // 加载层信息
        loading: [],
        files: {},
        userInfo: {
          nickname: this.$store.state.nickname,
          intro: this.$store.state.intro,
          avatar: this.$store.state.avatar,
          webSite: this.$store.state.webSite
        }
      };
    },
    methods: {
      updateUserInfo() {
        this.userInfo.avatar = this.$store.state.avatar,
          updateUser(this.userInfo).then(res => {
            this.$store.commit("updateUserInfo", this.userInfo);
            this.$toast({ type: "success", message: "修改成功" });
          }).catch(err => {
            this.$toast({ type: "error", message: err.message });
          });
      },
      uploadBefore: function() {
        this.openLoading();
      },
      uploadSectionFile: function(param) {
        this.files = param.file;
        // FormData 对象
        var formData = new FormData();
        // 文件对象
        formData.append("multipartFile", this.files);
        upload(formData).then(res => {
          this.$store.commit("updateAvatar", res.data);
          this.$toast({ type: "success", message: "上传成功" });
        }).catch(err => {
          this.$toast({ type: "error", message: err.message });
        });
        this.loading.close();
      },
      // 打开加载层
      openLoading: function() {
        this.loading = this.$loading({
          lock: true,
          text: "正在加载中~",
          spinner: "el-icon-loading",
          background: "rgba(0, 0, 0, 0.7)"
        });
      },
      openEmailModel() {
        this.$store.state.emailFlag = true;
      }
    },
    computed: {
      email() {
        return this.$store.state.email;
      },
      loginType() {
        return this.$store.state.loginType;
      },
      cover() {
        var cover = "";
        this.$store.state.blogInfo.pageList.forEach(item => {
          if (item.pageLabel == "user") {
            cover = item.pageCover;
          }
        });
        return "background: url(" + cover + ") center center / cover no-repeat";
      }
    }
  };
</script>

<style scoped>
    .info-title {
        font-size: 1.25rem;
        font-weight: bold;
    }

    .info-wrapper {
        margin-top: 1rem;
        display: flex;
        align-items: center;
        justify-content: center;
    }

    #pick-avatar {
        outline: none;
    }

    .binding {
        display: flex;
        align-items: center;
    }
</style>
