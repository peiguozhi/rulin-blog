<template>
  <div>
    <el-dialog :close-on-click-modal="false" :show-close="false" :width=width title="反馈" :visible.sync="this.$store.state.dialogFormVisible">
      <el-form :rules="rules" ref="dataForm" label-position="left" :model="form">
        <el-form-item label="反馈类型" prop="type">
          <el-radio v-model="form.type" :label="1">给 {{blogInfo.webSite.author}} 提交需求</el-radio>
          <el-radio v-model="form.type" :label="2">向 {{blogInfo.webSite.author}} 反馈缺陷</el-radio>
        </el-form-item>
        <el-form-item label="联系邮箱" prop="email" :label-width="formLabelWidth">
          <el-input placeholder="请填写您的联系邮箱"  v-model="form.email" />
        </el-form-item>
        <el-form-item label="需求" prop="title" :label-width="formLabelWidth">
          <el-input placeholder="请用一句话描述你的需求"  v-model="form.title"/>
        </el-form-item>
        <div class="contentInput">
          <el-form-item  label="需求简述" prop="content" :label-width="formLabelWidth">
            <el-input  placeholder="请在此详细描述你的需求" :rows="6" type="textarea" v-model="form.content" :label-width="formLabelWidth" />
            <a @click="screenshot" v-if="!form.imgUrl" style="color: #03a9f4"><i class="el-icon-camera"></i> 截取页面</a>
            <img v-else :src="form.imgUrl">
          </el-form-item>
        </div>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="closeDialog">取 消</el-button>
        <el-button type="primary" @click="submit">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {addFeedback} from '../api'
export default {
  metaInfo:{
    meta: [{
      name: 'keyWords',
      content: "程序儒,www.codescholar.cn,博客,个人博客,开源博客"  //变量或字符串
    }, {
      name: 'description',
      content: "一个专注于技术分享的博客平台,大家以共同学习,乐于分享,拥抱开源的价值观进行学习交流"
    }]
  },
  data: function() {
    return {
      formLabelWidth:"80px",
      width: '40%',
      screenWidth: null,  //屏幕尺寸
      form:{
        type:1,
        email:null,
        title:null,
        content:null,
        imgUrl:null
      },
      img: process.env.VUE_APP_IMG_API,
      rules:{
        type: [
          { required: true, message: '必填字段', trigger: 'blur' },
        ],
        email:[
          { required: true,message: '必填字段',trigger: 'blur'},
          {pattern: /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((.[a-zA-Z0-9_-]{2,3}){1,2})$/, message: '填写正确的邮箱'}
        ],
        title: [
          { required: true, message: '必填字段', trigger: 'blur' },
        ]
      },
    };
  },
  methods: {
    screenshot(){
      this.$toast({ type: "error", message: "敬请期待!" });
    },
    closeDialog(){
      this.form.type = 1
      this.form.email = null
      this.form.title = null
      this.form.content = null
      this.$store.commit("setDialogFormVisible")
    },
    submit(){
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          addFeedback(this.form).then(res => {
            this.$toast({ type: "success", message: "提交反馈成功" });
            this.closeDialog()
          }).catch(err => {
            this.$toast({ type: "error", message: "提交反馈失败" });
          })
        } else {
          this.$toast({ type: "error", message: "存在必填字段未填" });
        }
      })
    },
  },
  mounted () {
    this.screenWidth = document.body.clientWidth
    window.onresize = () => {   //屏幕尺寸变化就重新赋值
      return (() => {
        this.screenWidth = document.body.clientWidth
      })()
    }
  },
  computed: {
    blogInfo() {
      return this.$store.state.blogInfo;
    },
  },
  watch: {
    //  监听屏幕大小变化
    screenWidth(val,oldVal){
      if (val < 759) {
        this.width = '90%';
      } else {
        this.width = '40%';
      }
    },
  },
};
</script>
