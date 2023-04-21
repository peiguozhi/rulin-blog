<template>
  <div class="app-container">
    <!-- 查询和其他操作 -->
    <el-form v-show="showSearch" :inline="true" ref="form" :model="params" label-width="68px">
      <el-form-item label="用户名称">
        <el-input style="width: 200px" size="small" v-model="params.username" placeholder="请输入用户名称" />
      </el-form-item>
      <el-form-item label="登录方式">
        <el-select style="width: 150px" size="small" v-model="params.loginType" filterable clearable reserve-keyword
                   @change='handleFind' placeholder="请选择登录方式"
        >
          <el-option v-for="item in dictLoginTypeList" :key="item.value" :label="item.label" :value="item.value"/>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="small" @click="handleFind">查找</el-button>
        <el-button icon="el-icon-refresh" size="small" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
<!--        <el-button
          v-if="canAdd"
          type="primary"
          icon="el-icon-plus"
          size="small"
          @click="handleCreate"
        >新增
        </el-button>-->
      </el-col> <el-col :span="1.5">
      <el-button
        v-if="canDel"
        :disabled="!multipleSelection.length"
        type="danger"
        icon="el-icon-delete"
        size="small"
        @click="handleDelete"
      >批量删除
      </el-button>
    </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="fetchUser"></right-toolbar>
    </el-row>

    <div style="margin-top: 5px">
      <el-table border :data="userData" fit style="width: 100%" @selection-change="handleSelectionChange">
        <el-table-column type="selection" align="center"/>
        <el-table-column prop="avatar" align="center" width="100" label="头像">
          <template slot-scope="scope">
            <img :src="scope.row.avatar" width="60" height="60" />
          </template>
        </el-table-column>
        <el-table-column prop="nickname" width="150px" align="center" label="昵称"/>
        <el-table-column prop="loginType" align="center" label="登录方式">
          <template slot-scope="scope">
            <el-tag v-for="item in dictLoginTypeList" v-if="scope.row.loginType === parseInt(item.value)" :type="item.style">
              {{item.label}}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column align="center" label="用户角色">
          <template slot-scope="scope">
            <el-tag v-for="item in roleList" v-if="scope.row.roleId === item.id">
              {{item.name}}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="ipAddress" width="130px" align="center" label="登录IP"/>
        <el-table-column prop="ipSource" width="150px" align="center" label="登录地址"/>
        <el-table-column prop="createTime" align="center" width="180" label="创建时间">
          <template slot-scope="scope">
            <span>{{formatTime(scope.row.createTime)}}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" width="180" label="最后登录时间">
          <template slot-scope="scope">
            <span>{{formatTime(scope.row.lastLoginTime)}}</span>
          </template>
        </el-table-column>
<!--        <el-table-column align="center" label="状态">
          <template slot-scope="scope">
            <span>{{statusOptions[scope.row.status]}}</span>
          </template>
        </el-table-column>-->
        <el-table-column align="center" label="操作" width="150" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button v-if="canUpdate" type="primary"  size="mini" @click="handleUpdate(scope)">编辑</el-button>
            <el-button v-if="canDel"  size="mini"  type="danger"
                       @click="handleDelete(scope.row)">删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!--分页区域-->
    <div class="pagination-container" style="float: right;margin-bottom: 1.25rem;margin-top: 1.25rem;">
      <el-pagination background @size-change="handleSizeChange" @current-change="handleCurrentChange"
                     :current-page="params.pageNo" :page-size="params.pageSize" :page-sizes="[10, 20, 30]"
                     layout="total, sizes,prev, pager, next,jumper" :total="total">
      </el-pagination>
    </div>

    <el-dialog center :title="title" :visible.sync="dialogFormVisible">
      <el-form :rules="rules" ref="dataForm" :model="form">
        <el-form-item prop="nickname" label="昵称" :label-width="formLabelWidth">
          <el-input disabled="true" v-model="form.nickname" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item prop="status" label="状态" :label-width="formLabelWidth">
          <div>
            <el-radio v-for="(item,index) in statusOptions" v-model="form.status" :label="index" border>{{item}}</el-radio>
          </div>
        </el-form-item>
        <el-form-item prop="roleId" label="角色" :label-width="formLabelWidth">
          <div>
            <el-radio v-for="item in roleList" v-model="form.roleId" :label="item.id" border>{{item.name}}</el-radio>
          </div>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="submit">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import {fetchUser,remove,create,update,info} from '@/api/user'
import {fetchRole} from '@/api/system'
import {parseTime} from '@/utils'
import { upload } from '@/api/imgUpload'
import {mapGetters} from "vuex";
import {hasAuth} from "@/utils/auth";
import {getDataByDictType} from "@/api/dictData";
export default {
  data() {
    return {
      formLabelWidth: '120px',
      icon: false, //控制删除图标的显示
      img:process.env.VUE_APP_IMG_API,
      uploadPictureHost: process.env.VUE_APP_BASE_API + "/file/upload",
      isEditForm:0,
      sexColor:['success','warning'],
      sexOptions:['男','女'],
      statusOptions:['禁用','正常'],
      dialogFormVisible: false,
      showSearch: true,
      isFirstPhotoVisible: true, // 图片选择器是否首次显示【用于懒加载】
      photoVisible: false, //控制图片选择器的显示
      photoList: [],
      fileIds: "",
      multipleSelection:[],
      // 加载层信息
      loading: [],
      title:'',
      dictLoginTypeList:[],
      loginTypeDefaultValue:null,
      total: 0,
      files: {},
      params:{
        pageNo:1,
        pageSize:10,
        username:null,
        loginType:null
      },
      roleList:[],
      form:{},
      userData: [],
      rules: {
        username: [
          { required: true, message: '请输入账号', trigger: 'blur' },
          {min: 1, max: 20, message: '长度在1到20个字符'},
        ],
        nickname: [
          { required: true, message: '请输入昵称', trigger: 'blur' },
          {min: 1, max: 20, message: '长度在1到20个字符'},
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
        ],
        status: [
          { required: true, message: '请选择状态', trigger: 'change' },
        ],
        roleId: [
          { required: true, message: '请选择角色', trigger: 'change' },
        ]
      }
    }
  },
  computed:{
    ...mapGetters([
      'pres'
    ]),
    canAdd: function () {
      return hasAuth(this.pres, '/system/user/create')
    },
    canDel: function () {
      return hasAuth(this.pres, '/system/user/remove')
    },
    canUpdate: function () {
      return hasAuth(this.pres, '/system/user/update')
    },
  },
  created(){
    this.openLoading();
    this.getDictList()
    this.fetchRole()
    this.fetchUser()
  },
  methods: {
    getDictList: function () {
      let dictTypes = ['sys_login_method'];
      getDataByDictType(dictTypes).then(response => {
        let dictMap = response.data;
        this.dictLoginTypeList = dictMap.sys_login_method.list
        this.loginTypeDefaultValue = dictMap.sys_login_method.defaultValue
      }).catch(err => {
        console.error(err)
      })
    },
    fetchUser: function (){
      fetchUser(this.params).then(res =>{
        this.userData = res.data.records
        this.total = res.data.total
        this.loading.close();
      }).catch(err =>{
        console.error(err)
      })
    },
    fetchRole:function (){
      fetchRole({pageNo:0,pageSize:10}).then(res =>{
        this.roleList = res.data.records
      })
    },
    resetQuery: function (){
      this.params.username = null
      this.fetchUser()
    },
    handleFind: function (){
      this.params.pageNo =1
      this.fetchUser()
    },
    handleCreate: function (){
      this.form = this.getFormObject()
      this.beforeShow("添加用户",0)
    },
    handleUpdate: function (scope){
      info(scope.row.id).then(res =>{
        this.form = res.data
      })
      this.beforeShow("修改用户",1)
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    getFormObject: function (){
      return{
        id:null,
        username:'',
        password:'',
        nickname:'',
        avatar:'',
        roleId:'',
        status:1,
        pwd:'',
        sex:0,
        email:''
      }
    },
    beforeShow: function (title,isEditForm){
      this.title=title
      this.isEditForm = isEditForm
      this.dialogFormVisible = true
    },
    submit: function (){
      this.$refs['dataForm'].validate((valid) => {
        if (valid){
          if (this.isEditForm){
            update(this.form).then(res =>{
              this.$message.success("修改成功")
              this.fetchUser()
              this.close()
            }).catch(err =>{
              console.error(err)
            })
          }else {
            create(this.form).then(res =>{
              this.$message.success("添加成功")
              this.userData.unshift(res.data)
              this.close()
            }).catch(err =>{
              console.error(err)
            })
          }

        }else {
          console.log('error submit!!');
          return false;
        }
      })
    },
    handleDelete: function (row){
      this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        let ids =[]
        if (row != null){
          ids.push(row.id)
        }else {
          this.multipleSelection.forEach(item =>{
            ids.push(item.id)
          })
        }
        remove(ids).then(res => {
          this.$message.success("删除用户成功")
          this.fetchUser()
        }).catch(err => {
          console.log(err)
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '取消删除'
        })
      })
    },
    handleSizeChange: function (val) {
      this.params.pageSize = val
      this.fetchUser()
    },
    handleCurrentChange: function (val) {
      this.params.pageNo = val
      this.fetchUser()
    },
    close(){
      this.dialogFormVisible = false;
      this.form={}
    },
    handleClose: function (done) {
      done();
    },
    formatTime: function (time){
      return parseTime(time)
    },
    //关闭模态框
    cancelModel() {
      this.photoVisible = false;
    },
    getChooseData(data) {
      var that = this;
      this.photoVisible = false;
      this.photoList = data.photoList;
      if (this.photoList.length >= 1) {
        this.form.avatar = this.photoList[0];
      }
    },
    deletePhoto: function () {
      this.form.avatar = null
    },
    checkPhoto: function() {
      this.photoList = [];
      this.fileIds = "";
      this.photoVisible = true;
      this.isFirstPhotoVisible = false
    },
    uploadBefore: function (){
      this.openLoading()
    },
    uploadSectionFile: function (param) {
      let file = param.file
      this.files = file
      // FormData 对象
      var formData = new FormData()
      // 文件对象
      formData.append('multipartFile', this.files)
      upload(formData).then(res => {
        this.form.avatar = res.data
        this.loading.close()
      })
    },
    handleSelectionChange: function (val) {
      this.multipleSelection = val;
    },
    // 打开加载层
    openLoading: function () {
      this.loading = this.$loading({
        lock: true,
        text: "正在加载中~",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)"
      });
    },
  }
}
</script>
<style>
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}
.avatar {
  width: 178px;
  height: 178px;
  display: block;
}
</style>
