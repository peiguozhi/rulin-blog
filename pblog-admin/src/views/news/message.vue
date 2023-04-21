<template>
  <div class="app-container">
    <!-- 查询和其他操作 -->
    <el-form v-show="showSearch" ref="form" :inline="true" :model="params" label-width="68px">
      <el-form-item label="用户名称">
        <el-input v-model="params.name" style="width: 200px" size="small" placeholder="请输入昵称" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="small" @click="handleFind">查找</el-button>
        <el-button icon="el-icon-refresh" size="small" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          v-if="canDelBatch"
          :disabled="!multipleSelection.length"
          type="danger"
          icon="el-icon-delete"
          size="small"
          @click="handleDeleteBatch"
        >批量删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-if="canPassBatch"
          :disabled="!multipleSelection.length"
          type="success"
          icon="el-icon-circle-check"
          size="small"
          @click="handlePassBatch(null)"
        >批量通过
        </el-button>
      </el-col>
      <right-toolbar :show-search.sync="showSearch" @queryTable="fetchMessage" />
    </el-row>

    <div style="margin-top: 5px">
      <el-table
        border
        :data="tableData"
        style="width: 100%"
        @selection-change="handleSelectionChange"
      >
        <el-table-column
          type="selection"
          align="center"
        />
        <el-table-column prop="avatar" align="center" label="头像">
          <template slot-scope="scope">
            <el-avatar shape="square" :size="50" :src="scope.row.avatar" />
          </template>
        </el-table-column>
        <el-table-column prop="nickname" align="center" width="180" label="昵称" />
        <el-table-column prop="content" align="center" width="180" label="内容" />
        <el-table-column prop="ipAddress" align="center" width="150" label="ip地址" />
        <el-table-column prop="ipSource" align="center" width="160" label="ip来源" />
        <el-table-column prop="status" align="center" label="状态">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.status === 1" type="success">正常</el-tag>
            <el-tag v-else type="info">审核中</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" width="160" align="center" label="留言时间">
          <template slot-scope="scope">
            <span>{{ formatTime(scope.row.createTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" label="操作" width="160" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button v-if="canPassBatch&&!scope.row.status" type="primary" size="mini" @click="handlePassBatch(scope)">通过</el-button>
            <el-button v-if="canDel" size="mini" type="danger" @click="remove(scope)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!--分页区域-->
    <div class="pagination-container" style="float: right;margin-bottom: 1.25rem;margin-top: 1.25rem;">
      <el-pagination
        background
        :current-page="params.pageNo"
        :page-size="params.pageSize"
        :page-sizes="[10, 20, 30]"
        layout="total, sizes,prev, pager, next,jumper"
        :total="total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
  </div>
</template>
<script>
import { fetchMessage, passBatch, deleteBatch, remove } from '@/api/message'
import { parseTime } from '@/utils'
import { mapGetters } from 'vuex'
import { hasAuth } from '@/utils/auth'

export default {
  data() {
    return {
      tableData: [],
      multipleSelection: [],
      // 加载层信息
      loading: [],
      total: 0,
      showSearch: true,
      params: {
        name: null,
        pageNo: 1,
        pageSize: 10
      }
    }
  },
  created: function() {
    this.openLoading()
    this.fetchMessage()
  },
  computed: {
    ...mapGetters([
      'pres'
    ]),
    canDel: function() {
      return hasAuth(this.pres, '/system/message/remove')
    },
    canDelBatch: function() {
      return hasAuth(this.pres, '/system/message/deleteBatch')
    },
    canPassBatch: function() {
      return hasAuth(this.pres, '/system/message/passBatch')
    }
  },
  methods: {
    fetchMessage: function() {
      fetchMessage(this.params).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total
        this.loading.close()
      }).catch(err => {
        console.log(err)
      })
    },
    resetQuery: function() {
      this.params.name = null
      this.params.pageNo = 1
      this.fetchMessage()
    },
    handleFind: function() {
      this.params.pageNo = 1
      this.fetchMessage()
    },
    handlePassBatch: function(scope) {
      const ids = []
      if (scope != null) {
        ids.push(scope.row.id)
      } else {
        this.multipleSelection.forEach(item => {
          ids.push(item.id)
        })
      }
      passBatch(ids).then(res => {
        this.$message.success('OK')
        this.fetchMessage()
      }).catch(err => {
        console.error(err)
      })
    },
    handleDeleteBatch: function() {
      this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const ids = []
        this.multipleSelection.forEach(item => {
          ids.push(item.id)
        })
        deleteBatch(ids).then(res => {
          this.$message.success('OK')
          this.fetchMessage()
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
    remove: function(scope) {
      this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        remove(scope.row.id).then(res => {
          this.$message.success('OK')
          this.fetchMessage()
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
    openLoading: function() {
      this.loading = this.$loading({
        lock: true,
        text: '正在加载中~',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      })
    },
    handleSelectionChange: function(val) {
      this.multipleSelection = val
    },
    handleSizeChange: function(val) {
      this.params.pageSize = val
      this.fetchMessage()
    },
    handleCurrentChange: function(val) {
      this.params.pageNo = val
      this.fetchMessage()
    },
    formatTime: function(time) {
      return parseTime(time)
    }
  }
}
</script>
