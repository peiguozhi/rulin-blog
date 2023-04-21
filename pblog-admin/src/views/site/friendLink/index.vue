<template>
  <div class="app-container">
    <!-- 查询和其他操作 -->
    <el-form v-show="showSearch" :inline="true" ref="form" :model="params" label-width="68px">
      <el-form-item label="友链名称">
        <el-input style="width: 200px" size="small" v-model="params.name" placeholder="请输入友链名"/>
      </el-form-item>
      <el-form-item label="友链状态">
        <el-select @change="handleFind" size="small" v-model="params.status" clearable placeholder="友链状态">
          <el-option
            v-for="(item,index) in statusOptions"
            :key="index"
            :label="item"
            :value="index"
          ></el-option>
        </el-select>
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
          @click="handleAdd"
        >新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          :disabled="!multipleSelection.length"
          type="danger"
          icon="el-icon-delete"
          size="small"
          @click="handleDelete"
        >批量删除
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="fetchList"></right-toolbar>
    </el-row>


    <div style="margin-top: 5px">
      <el-table border :data="tableData" style="width: 100%" :default-sort="{prop: 'sort', order: 'descending'}"
                @selection-change="handleSelectionChange">
        <el-table-column type="selection" align="center"/>
        <el-table-column label="网站图标" width="80" align="center">
          <template slot-scope="scope">
            <img
              v-if="scope.row.avatar"
              :src="[scope.row.avatar]"
              style="width: 50px;height:50px;"
            >
          </template>
        </el-table-column>
        <el-table-column prop="name" align="center" label="网站名称" width="180"/>
        <el-table-column prop="info" align="center" width="180" label="网站描述"/>
        <el-table-column align="center" width="180" label="网站地址">
          <template slot-scope="scope">
            <el-link :underline="false" @click="onClick(scope.row.url)">{{ scope.row.url }}</el-link>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="status" label="状态">
          <template slot-scope="scope">
              <span>
                   <el-tag :type="statusTypes[scope.row.status]">
                     {{ statusOptions[scope.row.status] }}
                   </el-tag>
              </span>
          </template>
        </el-table-column>
        <el-table-column align="center" prop="sort" sortable label="排序">
          <template slot-scope="scope">
              <span>
                   <el-tag type="warning">{{ scope.row.sort }}</el-tag>
              </span>
          </template>
        </el-table-column>
        <el-table-column align="center" prop="createTime" width="200" label="创建时间">
          <template slot-scope="scope">
            <span>{{ dataFormat(scope.row.createTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" label="操作" width="230" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button v-if="canTop" @click="handleTop(scope)" type="warning" size="mini" slot="reference">置顶
            </el-button>
            <el-button v-if="canUpdate" @click="handleUpdate(scope)" type="primary" size="mini" slot="reference">编辑
            </el-button>
            <el-button v-if="canDel" size="mini" type="danger" @click="handleDelete(scope)">删除</el-button>
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

    <!-- 添加或修改对话框 -->
    <el-dialog center :title="title" :visible.sync="dialogFormVisible">
      <el-form :model="form" :rules="rules" ref="form">

        <el-form-item label="网站头像" :label-width="formLabelWidth" prop="avatar">
          <el-input v-model="form.avatar" auto-complete="off"></el-input>
        </el-form-item>

        <el-form-item label="网站名称" :label-width="formLabelWidth" prop="name">
          <el-input v-model="form.name" auto-complete="off"></el-input>
        </el-form-item>

        <el-form-item label="网站简介" :label-width="formLabelWidth" prop="info">
          <el-input v-model="form.info" auto-complete="off"></el-input>
        </el-form-item>

        <el-form-item label="网站地址" :label-width="formLabelWidth" prop="url">
          <el-input v-model="form.url" auto-complete="off"></el-input>
        </el-form-item>

        <el-form-item label="站长邮箱" :label-width="formLabelWidth" prop="email">
          <el-input v-model="form.email" auto-complete="off"></el-input>
        </el-form-item>

        <el-form-item label="网站状态" :label-width="formLabelWidth" prop="status">
          <el-select v-model="form.status" size="small" placeholder="请选择">
            <el-option
              v-for="(item,index) in statusOptions"
              :key="index"
              :label="item"
              :value="index"
            ></el-option>
          </el-select>
        </el-form-item>

        <el-form-item v-if="form.status === 0" label="下架原因" :label-width="formLabelWidth" prop="reason">
          <el-input v-model="form.reason" auto-complete="off"></el-input>
        </el-form-item>

        <el-form-item label="排序" :label-width="formLabelWidth" prop="sort">
          <el-input-number v-model="form.sort"></el-input-number>
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
import {fetchList, update, create, remove, top} from '@/api/friendLink'
import {parseTime} from '@/utils'
import {mapGetters} from "vuex";
import {hasAuth} from "@/utils/auth";

export default {

  data() {
    return {
      formLabelWidth: '120px',
      dialogFormVisible: false,
      title: null,
      img: process.env.VUE_APP_IMG_API,
      tableData: [],
      total: null,
      showSearch: true,
      isEditForm: 0,
      params: {
        pageNo: 1,
        pageSize: 10,
        name: null,
        status: null
      },
      // 加载层信息
      loading: [],
      form: {},
      multipleSelection: [],
      statusTypes: ['warning', "danger", 'success'],
      statusOptions: ["下架", "申请", "上架"],
      rules: {
        avatar: [
          {required: true, message: '网站头像不能为空', trigger: 'blur'},
        ],
        name: [
          {required: true, message: '网站名称不能为空', trigger: 'blur'},
          {min: 1, max: 20, message: '长度在1到20个字符'},
        ],
        url: [
          {required: true, message: '网站地址不能为空', trigger: 'blur'},
          {pattern: /^((https|http|ftp|rtsp|mms)?:\/\/)[^\s]+/, message: '请输入有效的网站地址'},
        ],
        info: [
          {required: true, message: '网站简介不能为空', trigger: 'blur'}
        ],
        email: [
          {required: true,pattern: /\w[-\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\.)+[A-Za-z]{2,14}/, message: '请输入正确的邮箱'},
        ],
        status: [
          {required: true, message: '网站状态不能为空', trigger: 'blur'}
        ],
        reason: [
          {required: true, message: '下架原因不能为空', trigger: 'blur'}
        ],
        sort: [
          {required: true, message: '排序字段不能为空', trigger: 'blur'},
          {pattern: /^[0-9]\d*$/, message: '排序字段只能为自然数'},
        ]
      }
    }
  },
  computed: {
    ...mapGetters([
      'pres'
    ]),
    canAdd: function () {
      return hasAuth(this.pres, '/system/friend/create')
    },
    canDel: function () {
      return hasAuth(this.pres, '/system/friend/remove')
    },
    canUpdate: function () {
      return hasAuth(this.pres, '/system/friend/update')
    },
    canTop: function () {
      return hasAuth(this.pres, '/system/friend/top')
    },
  },
  created() {
    this.openLoading();
    this.fetchList()
  },
  methods: {
    fetchList: function () {
      fetchList(this.params).then(res => {
        this.total = res.data.total
        this.tableData = res.data.records
        this.loading.close();
      }).catch(err => {
        console.error(err)
      })
    },
    handleDelete: function (scope) {
      this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        let ids = []
        if (scope != null) {
          ids.push(scope.row.id)
        } else {
          this.multipleSelection.forEach(item =>
            ids.push(item.id)
          )
        }
        remove(ids).then(res => {
          this.$message.success("删除友链成功")
          this.fetchList()
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
    submit: function () {
      this.$refs['form'].validate((valid) => {
        if (valid) {
          if (this.isEditForm) {
            update(this.form).then(res => {
              this.$message.success("修改友链成功")
              this.fetchList()
              this.close()
            }).catch(err => {
              console.error(err)
            })
          } else {
            create(this.form).then(res => {
              this.$message.success("添加友链成功")
              this.fetchList()
              this.close()
            }).catch(err => {
              console.error(err)
            })
          }
        } else {
          console.log('error submit!!');
          return false;
        }
      })
    },
    resetQuery: function () {
      this.params.name = null
      this.params.status = null
      this.fetchList()
    },
    handleFind: function () {
      this.params.pageNo = 1
      this.fetchList()
    },
    handleTop: function (scope) {
      this.$confirm('此操作将会把该友链放到首位, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        top(scope.row.id).then(res => {
          this.$message.success("置顶友链成功")
          this.fetchList()
        }).catch(err => {
          console.error(err)
        })
      }).catch(() => {
        this.$message.info("已取消置顶")
      })
    },
    beforeShow: function (title, isEditForm) {
      this.title = title
      this.isEditForm = isEditForm
      this.dialogFormVisible = true
    },
    handleAdd: function () {
      this.form = this.getFormObject()
      this.beforeShow('添加友链', 0)
    },
    handleUpdate: function (scope) {
      this.form = scope.row
      this.beforeShow('修改友链', 1)
    },
    getFormObject: function () {
      return {
        id: null,
        name: null,
        url: null,
        info: null,
        email: null,
        sort: null,
        status: null,
        createTime: null,
        updateTime: null,
        avatar: null
      }
    },
    close: function () {
      this.dialogFormVisible = false;
      this.form = {}
    },
    dataFormat: function (time) {
      return parseTime(time)
    },
    handleSizeChange: function (val) {
      this.params.pageSize = val
      this.fetchList()
    },
    handleCurrentChange: function (val) {
      this.params.pageNo = val
      this.fetchList()
    },
    handleSelectionChange: function (val) {
      this.multipleSelection = val;
    },
    onClick: function (url) {
      window.open(url)
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
  },
}
</script>
