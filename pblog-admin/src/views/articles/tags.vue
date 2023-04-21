<template>
  <div class="app-container">
    <!--查询or添加-->
    <el-form v-show="showSearch" :inline="true" ref="form" :model="params" label-width="68px">
      <el-form-item label="标签名称">
        <el-input style="width: 200px" size="small" v-model="params.name" placeholder="请输入标签名称"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="small" @click="handleFind">查找</el-button>
        <el-button icon="el-icon-refresh" size="small" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button v-if="canAdd" size="small" class="filter-item" @click="handleCreate" type="primary"
                   icon="el-icon-plus">添加
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button size="small" v-if="canDeleteBatch" :disabled="!multipleSelection.length" class="filter-item"
                   @click="handleDelete"
                   type="danger" icon="el-icon-delete">批量删除
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="fetchTags"></right-toolbar>
    </el-row>

    <div style="margin-top: 5px">
      <el-table
        border
        :data="tableData"
        style="width: 100%"
        :default-sort="{prop: 'sort', order: 'descending'}"
        @selection-change="handleSelectionChange"
      >
        <el-table-column align="center" type="selection"/>
        <el-table-column align="center" label="序号">
          <template slot-scope="scope">
            <span>{{ scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="name" align="center" label="标签名" width="180"/>
        <el-table-column prop="articleCount" align="center" label="文章量" />
        <el-table-column align="center" prop="sort" sortable label="排序">
          <template slot-scope="scope">
            <el-tag type="warning">{{ scope.row.sort }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column align="center" prop="clickVolume"  label="点击量">
          <template slot-scope="scope">
            <el-tag type="warning">{{ scope.row.clickVolume }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column width="180" align="center" label="添加时间">
          <template slot-scope="scope">
            <span>{{ dataFormat(scope.row.createTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column width="220" align="center" label="操作"  class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button v-if="canTop" type="warning" size="mini" @click="handleTop(scope)">置顶</el-button>
            <el-button v-if="canUpdate" type="primary" size="mini" @click="handleEdit(scope)">编辑</el-button>
            <el-button v-if="canDel" size="mini" type="danger" @click="remove(scope)">删除
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
      <el-form :rules="rules" ref="dataForm" :model="tag" >
        <el-form-item label="标签名" prop="name" :label-width="formLabelWidth">
          <el-input v-model="tag.name"></el-input>
        </el-form-item>
        <el-form-item label="排序" prop="sort" :label-width="formLabelWidth">
          <el-input v-model="tag.sort"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="closeDialogForm">取 消</el-button>
        <el-button type="primary" @click="submit">确认</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import {fetchTags, remove, add, info, update, deleteBatch, top} from '@/api/tags'
import {parseTime} from '@/utils'
import {hasAuth} from '@/utils/auth'
import {mapGetters} from 'vuex'

export default {
  data() {
    return {
      // 加载层信息
      loading: [],
      tableData: [],
      formLabelWidth: '120px',
      dialogFormVisible: false,
      showSearch: true,
      isEditForm: 0,
      total: null,
      multipleSelection: [],
      title:null,
      tag: {},
      params: {
        name: null,
        pageNo: 1,
        pageSize: 10
      },
      rules:
        {
          'name': [
            {required: true, message: '必填字段', trigger: 'change'},
            {min: 1, max: 20, message: '长度在1到20个字符'},
          ],
          'sort': [
            {required: true, message: '必填字段', trigger: 'change'},
            {pattern: /^[0-9]\d*$/, message: '排序字段只能为自然数'},
          ]
        }
    }
  },
  created() {
    this.openLoading();
    this.fetchTags()
  },
  computed: {
    ...mapGetters([
      'pres'
    ]),
    canAdd: function () {
      return hasAuth(this.pres, '/system/tags/add')
    },
    canDel: function () {
      return hasAuth(this.pres, '/system/tags/remove')
    },
    canDeleteBatch: function () {
      return hasAuth(this.pres, '/system/tags/deleteBatch')
    },
    canUpdate: function () {
      return hasAuth(this.pres, '/system/tags/update')
    },
    canTop: function () {
      return hasAuth(this.pres, '/system/tags/top')
    },
  },
  methods: {
    fetchTags: function () {
      fetchTags(this.params).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total
        this.loading.close();
      }).catch(err => {
        console.log(err)
      })
    },
    submit: function () {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          if (this.isEditForm) {
            update(this.tag).then(res => {
              this.$message.success("修改标签成功")
              this.fetchTags()
              this.closeDialogForm()
            }).catch(err => {
              console.log(err)
            })
          } else {
            add(this.tag).then(res => {
              this.$message.success("添加标签成功")
              this.tag = res.data
              this.fetchTags()
              this.closeDialogForm()
            }).catch(err => {
              console.log(err)
            })
          }
        } else {
          console.error("no submit")
        }
      })
    },
    remove: function (scope) {
      this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        remove(scope.row.id).then(res => {
          this.$message.success("删除标签成功")
          this.fetchTags()
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
    handleCreate: function () {
      this.isEditForm = 0
      this.tag = this.getFormObject()
      this.title = "新增标签"
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    handleEdit: function (scope) {
      info(scope.row.id).then(res => {
        this.tag = res.data
        this.isEditForm = 1
        this.title = "修改标签"
        this.dialogFormVisible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].clearValidate()
        })
      })
    },
    getFormObject: function () {
      return {
        name: '',
        sort: ''
      }
    },
    handleTop: function (scope) {
      this.$confirm('此操作将会把该标签放到首位, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        top(scope.row.id).then(res => {
          this.$message.success("置顶标签成功")
          this.fetchTags()
        }).catch(err => {
          console.log(err)
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消置顶'
        })
      })
    },
    resetQuery: function () {
      this.params.name = null
      this.fetchTags()
    },
    handleFind: function () {
      this.params.pageNo = 1;
      this.fetchTags()
    },
    handleDelete: function () {
      if (!this.multipleSelection.length) {
        this.$message.error("请选择要删除的标签")
        return false
      }
      this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        let ids =[]
        this.multipleSelection.forEach(item =>{
          ids.push(item.id)
        })
        deleteBatch(ids).then(res => {
          this.$message.success("批量删除标签成功")
          this.fetchTags()
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
    closeDialogForm: function () {
      this.tag = {}
      this.dialogFormVisible = false
    },
    handleSizeChange: function (val) {
      this.params.pageSize = val
      this.fetchTags()
    },
    handleCurrentChange: function (val) {
      this.params.pageNo = val
      this.fetchTags()
    },
    handleSelectionChange: function (val) {
      this.multipleSelection = val
    },
    // 打开加载层
    openLoading() {
      this.loading = this.$loading({
        lock: true,
        text: "正在加载中~",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)"
      });
    },
    dataFormat: function (time) {
      return parseTime(time)
    },
  }
}
</script>
