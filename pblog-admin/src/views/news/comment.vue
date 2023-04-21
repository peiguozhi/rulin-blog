<template>
  <div class="app-container">
    <!-- 查询和其他操作 -->
    <el-form v-show="showSearch" ref="form" :inline="true" :model="params" label-width="68px">
      <el-form-item label="用户名称">
        <el-input v-model="params.keywords" style="width: 200px" size="small" placeholder="请输入评论或回复人昵称" />
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

      <right-toolbar :show-search.sync="showSearch" @queryTable="fetchComment" />
    </el-row>

    <div style="margin-top: 5px">
      <el-table
        border
        :data="tableData"
        style="width: 100%"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" align="center" />
        <el-table-column prop="avatar" align="center" width="150" label="头像">
          <template slot-scope="scope">
            <el-avatar shape="square" :size="50" :src="scope.row.avatar" />
          </template>
        </el-table-column>
        <el-table-column prop="nickname" align="center" width="200" label="评论用户" />
        <el-table-column prop="replyNickname" align="center" width="200" label="回复用户" />
        <el-table-column prop="articleTitle" align="center" width="250" label="所属文章" />
        <el-table-column prop="content" align="center" width="300" label="内容">
          <template slot-scope="scope">
            <span class="comment-content" v-html="scope.row.content" />
          </template>
        </el-table-column>
        <el-table-column prop="createTime" width="200" align="center"label="评论时间">
          <template slot-scope="scope">
            <span>{{ formatTime(scope.row.createTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" label="操作" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button v-if="canDelBatch" size="mini" type="danger" @click="handleDeleteBatch(scope.row.id)">删除</el-button>
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
import { fetchComment, deleteBatch } from '@/api/comment'
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
        keywords: null,
        pageNo: 1,
        pageSize: 10
      }
    }
  },
  created: function() {
    this.openLoading()
    this.fetchComment()
  },
  computed: {
    ...mapGetters([
      'pres'
    ]),
    canDelBatch: function() {
      return hasAuth(this.pres, '/system/comment/deleteBatch')
    }
  },
  methods: {
    fetchComment: function() {
      fetchComment(this.params).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total
        this.loading.close()
      }).catch(err => {
        console.log(err)
      })
    },
    resetQuery: function() {
      this.params.keywords = null
      this.params.pageNo = 1
      this.fetchComment()
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
        this.fetchComment()
      }).catch(err => {
        console.error(err)
      })
    },
    handleDeleteBatch: function(id) {
      this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const ids = []
        if (id != null) {
          ids.push(id)
        } else {
          this.multipleSelection.forEach(item => {
            ids.push(item.id)
          })
        }
        deleteBatch(ids).then(res => {
          this.$message.success('OK')
          this.fetchComment()
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
      this.fetchComment()
    },
    handleCurrentChange: function(val) {
      this.params.pageNo = val
      this.fetchComment()
    },
    formatTime: function(time) {
      return parseTime(time)
    }
  }
}
</script>
