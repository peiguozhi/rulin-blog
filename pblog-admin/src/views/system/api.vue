<template>
  <div>
    <div class="app-container">
      <!-- 查询和其他操作 -->
      <el-form v-show="showSearch" :inline="true" ref="form" label-width="68px">
        <el-form-item label="菜单名称">
          <el-cascader
            size="small"
            :options="options"
            placeholder="请选择菜单名"
            v-model="keyword"
            :props="{ checkStrictly: false }"
            @change="handleFind"
            clearable></el-cascader>
        </el-form-item>
        <el-form-item>
          <el-button  v-if="canAdd" type="primary" icon="el-icon-search" size="small" @click="handleCreate">新增</el-button>
        </el-form-item>
      </el-form>

      <div>
        <el-table  :data="menuData" style="width: 100%">
          <el-table-column type="expand">
            <template slot-scope="scope">
              <el-form label-position="left" inline class="demo-table-expand">
                <el-table :data="scope.row.children" :show-header="showHeader" style="width: 100%">

                  <el-table-column label width="60" align="center">
                    <template slot-scope="scope_child">
                      <span>{{ scope_child.$index + 1 }}</span>
                    </template>
                  </el-table-column>

                  <el-table-column label width="150" align="center">
                    <template slot-scope="scope_child">
                      <span>{{ scope_child.row.title }}</span>
                    </template>
                  </el-table-column>

                  <el-table-column label width="100" align="center">
                    <template slot-scope="scope_child">
                      <el-tag :type="menuLevelType[scope_child.row.level]">
                        {{ menuLevelOptions[scope_child.row.level] }}
                      </el-tag>
                    </template>
                  </el-table-column>

                  <el-table-column label="菜单类型" width="100" align="center">
                    <template slot-scope="scope_child">
                      <el-tag :type="menuTypes[scope_child.row.type === 'menu'?0:1]">
                        {{ menuTypeOptions[scope_child.row.type === 'menu' ? 0 : 1] }}
                      </el-tag>
                    </template>
                  </el-table-column>

                  <el-table-column label width="100" align="center">
                    <template slot-scope="scope_child">
                    <span v-if="scope_child.row.icon != null">
                       <i v-if="scope_child.row.icon.indexOf('el-') >-1" :class="scope_child.row.icon"></i>
                        <svg-icon :icon-class="scope_child.row.icon"/>
                 </span>
                    </template>
                  </el-table-column>

                  <el-table-column label width="200" align="center">
                    <template slot-scope="scope_child">
                      <span>{{ scope_child.row.url }}</span>
                    </template>
                  </el-table-column>

                  <el-table-column width="100" align="center">
                    <template slot-scope="scope_child">
                      <el-tag :type="hiddenTypes[scope_child.row.hidden]">
                        {{ hiddenOptions[scope_child.row.hidden] }}
                      </el-tag>
                    </template>
                  </el-table-column>

                  <el-table-column width="100" align="center">
                    <template slot-scope="scope_child">
                      <el-tag type="warning">{{ scope_child.row.sortNo }}</el-tag>
                    </template>
                  </el-table-column>

                  <el-table-column align="center" min-width="230">
                    <template slot-scope="scope_child">
                      <el-button v-if="canUpdate" type="primary" size="mini"  @click="handleUpdate(scope.row,scope_child.row)">编辑</el-button>
                      <el-button v-if="canDel" size="mini"  type="danger" @click="remove(scope_child)">删除</el-button>
                    </template>
                  </el-table-column>
                </el-table>
              </el-form>
            </template>
          </el-table-column>

          <el-table-column label="序号" width="60" align="center">
            <template slot-scope="scope">
              <span>{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>

          <el-table-column label="菜单名称" width="150" align="center">
            <template slot-scope="scope">
              <span>{{ scope.row.title }}</span>
            </template>
          </el-table-column>

          <el-table-column label="菜单级别" width="100" align="center">
            <template slot-scope="scope">
              <el-tag :type="menuLevelType[scope.row.level]">
                {{ menuLevelOptions[scope.row.level] }}
              </el-tag>
            </template>
          </el-table-column>

          <el-table-column label="菜单类型" width="100" align="center">
            <template slot-scope="scope">
              <el-tag :type="menuTypes[scope.row.type === 'menu'?0:1]">
                {{ menuTypeOptions[scope.row.type === 'menu' ? 0 : 1] }}
              </el-tag>
            </template>
          </el-table-column>

          <el-table-column label="图标" width="100" align="center">
            <template slot-scope="scope">
            <span v-if="scope.row.icon != null">
                 <i v-if="scope.row.icon.indexOf('el-') >-1" :class="scope.row.icon"></i>
                 <svg-icon :icon-class="scope.row.icon"/>
            </span>
            </template>
          </el-table-column>

          <el-table-column label="路由" width="200" align="center">
            <template slot-scope="scope">
              <span>{{ scope.row.url }}</span>
            </template>
          </el-table-column>

          <el-table-column label="是否显示" width="100" align="center">
            <template slot-scope="scope">
              <el-tag :type="hiddenTypes[scope.row.hidden]">
                {{ hiddenOptions[scope.row.hidden] }}
              </el-tag>
            </template>
          </el-table-column>

          <el-table-column label="排序" width="100" align="center">
            <template slot-scope="scope">
              <el-tag type="warning">{{ scope.row.sortNo }}</el-tag>
            </template>
          </el-table-column>

          <el-table-column label="操作" align="center"  min-width="270">
            <template slot-scope="scope">

            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>


    <el-dialog center :title="title" :visible.sync="dialogFormVisible">
      <el-form :rules="rules" ref="dataForm" :model="form">
        <el-form-item prop="url" label="路由" :label-width="formLabelWidth">
          <el-input v-model="form.url" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item prop="title" label="接口名称" :label-width="formLabelWidth">
          <el-input v-model="form.title" autocomplete="off"></el-input>
        </el-form-item>
        <!-- 用于接口 -->
        <el-form-item
          prop="parentId"
          label="父菜单名"
          :label-width="formLabelWidth"
        >
          <el-cascader
            :options="options"
            placeholder="请选择父菜单"
            v-model="buttonParentUid"
            filterable
            :props="{ checkStrictly: false }"
            clearable></el-cascader>
        </el-form-item>
        <el-form-item label="排序" :label-width="formLabelWidth">
          <el-input v-model="form.sortNo" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="备注" :label-width="formLabelWidth">
          <el-input v-model="form.remarks" autocomplete="off"></el-input>
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
import {fetchMenu,fetchApi, createMenu, removeMenu, updateMenu} from '@/api/system'
import IconsDialog from "../../components/IconsDialog";
import {mapGetters} from "vuex";
import {hasAuth} from "@/utils/auth";

export default {
  components: {
    IconsDialog
  },
  data() {
    return {
      keyword:null,
      buttonParentUid:[],
      showHeader: false, //是否显示表头
      hiddenTypes: ['warning', 'success'],
      hiddenOptions: ['否', '是'],
      menuTypes: ['success', 'warning'],
      menuTypeOptions: ['菜单', '按钮'],
      menuLevelType: ['success', 'danger', 'info'],
      menuLevelOptions: ['一级菜单', '二级菜单', '三级菜单'],
      isEditForm: 0,
      title: null,
      form: {},
      query:{
        id:null
      },
      // 加载层信息
      loading: [],
      options:[],
      dialogFormVisible: false,
      showSearch: true,
      formLabelWidth: '120px',
      menuData: [],
      rules: {
        url: [
          {required: true, message: '请输入url', trigger: 'change'}
        ],
        icon: [
          {required: false, message: '请选择图标', trigger: 'change'}
        ],
        level: [
          {required: true, message: '请选择菜单级别', trigger: 'change'}
        ],
        component: [
          {required: false, message: '请输入路由地址', trigger: 'change'}
        ],
        hidden: [
          {required: true, message: '请选择是否显示', trigger: 'change'}
        ],
        title: [
          {required: true, message: '请输入菜单名称', trigger: 'change'}
        ]
      }
    }
  },
  computed:{
    ...mapGetters([
      'pres'
    ]),
    canAdd: function () {
      return hasAuth(this.pres, '/system/menu/create')
    },
    canDel: function () {
      return hasAuth(this.pres, '/system/menu/remove')
    },
    canUpdate: function () {
      return hasAuth(this.pres, '/system/menu/update')
    },
  },
  created() {
    this.openLoading();
    this.fetchApi()
    fetchMenu().then(res =>{
      res.data.forEach(item =>{
        let parent = {}
        parent.label = item.title;
        parent.value = item.id;
        let childList = []
        item.children.forEach(childItem =>{
          let child = {}
          child.label = childItem.title;
          child.value = childItem.id;
          childList.push(child)
        })
        parent.children = childList
        this.options.push(parent)
      })
    })
  },
  methods: {
    fetchApi: function () {
      fetchApi(this.query).then(res => {
        this.menuData=res.data
        this.loading.close();
      }).catch(err => {
        console.error(err)
      })
    },
    handleFind: function () {
      if (this.keyword == null){
        this.$message.error("请选择菜单")
        return false
      }
      this.query.id = this.keyword[1]
      this.fetchApi();
    },
   submit: function (){
     this.$refs['dataForm'].validate((valid) => {
       if (valid) {
         this.form.parentId=this.buttonParentUid[1]
         if (this.isEditForm){
           updateMenu(this.form).then(res => {
             this.$message.success(res.message)
             this.fetchApi()
             this.close()
           }).catch(err => {
             console.error(err)
           })
         }else {
           createMenu(this.form).then(res => {
             this.$message.success(res.message)
             this.fetchApi()
             this.close()
           }).catch(err => {
             console.error(err)
           })
         }
       } else {
         console.log('error submit!!')
         return false
       }
     })
   },
    remove: function (scope) {
      if (scope.row.children) {
        this.$message.error('该菜单存在子菜单，请先删除子菜单')
        return;
      }
      this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        removeMenu(scope.row.id).then(res => {
          this.fetchApi()
          this.$notify({
            title: '成功',
            message: res.message,
            type: 'success'
          })
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
    handleUpdate: function (parentRow,row) {
      // 设置级联的父菜单名
      let parentUid = []
      parentUid.push(parentRow.parentId)
      parentUid.push(parentRow.id)
      this.buttonParentUid = parentUid
      this.form = row
      this.beforeShow('修改接口',1)
    },
    handleCreate: function (id) {
      this.form=this.getFormObject()
      this.buttonParentUid=[]
      this.beforeShow('添加接口',0 )
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    beforeShow: function (title,isEditForm) {
      this.title = title
      this.isEditForm = isEditForm
      this.dialogFormVisible = true
    },
    getFormObject: function (){
      return {
        parentId: null,
        url: '',
        component: '',
        type: 'btn',
        title: '',
        level: 2,
        sortNo: '',
        hidden: 0,
        remarks: ''
      }
    },
    close: function () {
      this.dialogFormVisible = false
      this.form = {}
    },
    // 打开加载层
    openLoading: function () {
      this.loading = this.$loading({
        lock: true,
        text: "正在加载中~",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)"
      });
    }
  },

}
</script>
