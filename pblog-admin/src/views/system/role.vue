<template>
<div class="app-container">
  <!-- 查询和其他操作 -->
  <el-form v-show="showSearch" :inline="true" ref="form" :model="params" label-width="68px">
    <el-form-item label="角色名称">
      <el-input style="width: 200px" size="small" v-model="params.name" placeholder="请输入角色名称" />
    </el-form-item>
    <el-form-item>
      <el-button type="primary" icon="el-icon-search" size="small" @click="handleFind">查找</el-button>
      <el-button icon="el-icon-refresh" size="small" @click="resetQuery">重置</el-button>
    </el-form-item>
  </el-form>

  <el-row :gutter="10" class="mb8">
    <el-col :span="1.5">
      <el-button
        v-if="canAdd"
        type="primary"
        icon="el-icon-plus"
        size="small"
        @click="handleCreate"
      >新增
      </el-button>
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
    <right-toolbar :showSearch.sync="showSearch" @queryTable="fetchRole"></right-toolbar>
  </el-row>

  <div style="margin-top: 5px">
    <el-table border :data="roleData" style="width: 100%;margin-bottom: 20px;" default-expand-all @selection-change="handleSelectionChange">
      <el-table-column align="center" type="selection"/>
      <el-table-column align="center" prop="code" label="编码" width="180" />
      <el-table-column align="center" prop="name" label="名称" />
      <el-table-column align="center" prop="remarks" label="创建时间">
        <template slot-scope="scope">
          <span>{{dateFormat(scope.row.createdTime)}}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="remarks" label="备注" />
      <el-table-column align="center" label="操作" width="230" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button v-if="canUpdate"  type="primary" size="mini" @click="handleUpdate(scope)">编辑</el-button>
          <el-button v-if="canDel"  size="mini" type="danger"
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

  <el-dialog center :title="title" :visible.sync="dialogVisible" width="50%" :before-close="handleClose">
    <el-form :rules="rules" ref="dataForm" :model="form" label-width="80px">
      <el-form-item prop="code" label="角色编码">
        <el-input v-model="form.code"></el-input>
      </el-form-item>
      <el-form-item prop="name" label="角色名称">
        <el-input v-model="form.name"></el-input>
      </el-form-item>
      <el-form-item prop="remarks" label="备注">
        <el-input v-model="form.remarks"></el-input>
      </el-form-item>
      <el-form-item label="角色权限">
        <el-tree
          :data="menuData"
          show-checkbox
          node-key="id"
          ref="permsTree"
          default-expand-all
          :props="defaultProps">
        </el-tree>
      </el-form-item>
    </el-form>

    <span slot="footer" class="dialog-footer">
    <el-button @click="dialogVisible = false">取 消</el-button>
    <el-button type="primary" @click="submit">确 定</el-button>
  </span>
  </el-dialog>

</div>
</template>
<script>
import {fetchRole,fetchMenu,queryRoleId,updateRole,createRole,removeRole} from '@/api/system'
import {parseTime} from '@/utils'
import ScrollPane from "@/layout/components/TagsView/ScrollPane";
import {mapGetters} from "vuex";
import {hasAuth} from "@/utils/auth";
  export default {
    components: {ScrollPane},
    data() {
      return {
        checkedTree:[],
        multipleSelection:[],
        defaultProps: {
          children: 'children',
          label: 'title'
        },
        isEditForm:0,
        dialogVisible: false,
        showSearch: true,
        title: null,
        total: 0,
        params:{
          pageNo:1,
          pageSize:10,
          name:null
        },
        form:{},
        menuData:[],
        roleData: [],
        // 加载层信息
        loading: [],
        rules: {
          code: [
            { required: true, message: '请输入角色编码', trigger: 'change' },
          ],
          name: [
            { required: true, message: '请输入角色名称', trigger: 'change' },
          ]
        }
      }
    },
    computed:{
      ...mapGetters([
        'pres'
      ]),
      canAdd:function() {
        return hasAuth(this.pres, '/system/role/create')
      },
      canDel: function() {
        return hasAuth(this.pres, '/system/role/remove')
      },
      canUpdate: function() {
        return hasAuth(this.pres, '/system/role/update')
      },
    },
    created(){
      this.openLoading();
      this.fetchRole()
      fetchMenu().then(res =>{
        this.menuData = res.data
      }).catch(err =>{
        console.log(err)
      })
    },
    methods: {
      fetchRole: function (){
        fetchRole(this.params).then(res =>{
          this.roleData = res.data.records
          this.total = res.data.total
          this.loading.close();
        }).catch(err =>{
          console.log(err)
        })
      },
      resetQuery: function (){
        this.params.name = null
        this.fetchRole()
      },
      handleFind: function (){
        if (this.params.name == null){
          this.$message.error("请输入角色名")
          return false
        }
        this.params.pageNo = 1
        this.fetchRole()
      },
      handleUpdate: function (scope){
        this.form = scope.row
        this.beforeShow('修改角色',1)
        this.$nextTick(() => {
          this.afterFormShow(scope.row)
          this.$refs['dataForm'].clearValidate()
        })
      },
      handleCreate: function (){
        this.form = this.getFormObject()
        this.beforeShow('添加角色',0)
      },
      getFormObject: function (){
        return{
          id:null,
          code:'',
          remarks:'',
          name:'',
          menus:[]
        }
      },
      beforeShow: function (title,isEditForm){
        this.isEditForm = isEditForm
        this.title =title
        this.dialogVisible = true
      },
      submit: function (){
        this.$refs['dataForm'].validate((valid) => {
          if (valid){
            //获取父节点 子节点选中了的父节点
            let parentArr = this.$refs.permsTree.getHalfCheckedKeys();
            this.form.menus = this.$refs.permsTree.getCheckedKeys()
            parentArr.forEach(item =>{
              this.form.menus.push(item)
            })
            if (this.isEditForm){
              updateRole(this.form).then(res =>{
                this.$message.success("修改角色成功")
                this.fetchRole()
                this.close()
              }).catch(err =>{
                console.error(err)
              })
            }else {
              createRole(this.form).then(res =>{
                this.$message.success("添加角色成功")
                this.roleData.unshift(res.data)
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
          if (row.id != null){
            ids.push(row.id)
          }else {
            this.multipleSelection.forEach(item =>{
              ids.push(item.id)
            })
          }
          removeRole(ids).then(res => {
            this.$message.success("删除角色成功")
            this.fetchRole()
          }).catch(err => {
            console.error(err)
          })
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '取消删除'
          })
        })
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
      dateFormat: function (time){
        return parseTime(time)
      },
      handleClose: function (done) {
        done();
      },
      handleSelectionChange: function(val) {
        this.multipleSelection = val
      },
      afterFormShow: function (row){
        // 清除
        this.$refs.permsTree.setCheckedKeys([])
        if (row) {
          queryRoleId(row.id).then(res =>{
            let that = this
            setTimeout(function() {
              res.data.forEach(value => {
                that.$refs.permsTree.setChecked(value, true, false);
              });
            }, 100);
          }).catch(err =>{
            console.error(err)
          })
        }
      },
      handleSizeChange: function (val) {
        this.params.size = val
        this.fetchRole()
      },
      handleCurrentChange: function (val) {
        this.params.pageNo = val
        this.fetchRole()
      },
      close: function (){
        this.dialogVisible = false;
        this.form={}
        this.$refs.permsTree.setCheckedKeys([])
      },
    },

  }
</script>
