<template>
  <div class="app-container">
    <!-- 查询和其他操作 -->
    <!--    <el-form v-show="showSearch" :inline="true" ref="form" :model="params" label-width="68px">
      <el-form-item label="用户名称">
        <el-input style="width: 200px" size="small" v-model="params.name" placeholder="请输入昵称"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="small" @click="handleFind">查找</el-button>
        <el-button icon="el-icon-refresh" size="small" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>-->

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          v-if="canDelete"
          :disabled="!multipleSelection.length"
          type="danger"
          icon="el-icon-delete"
          size="small"
          @click="handleDeleteBatch"
        >批量删除
        </el-button>
      </el-col>
      <right-toolbar :show-search.sync="showSearch" @queryTable="fetchList" />
    </el-row>

    <div style="margin-top: 5px">
      <el-table
        border
        :data="tableData"
        style="width: 100%"
        :default-sort="{prop: 'date', order: 'descending'}"
        @selection-change="handleSelectionChange"
      >
        <el-table-column align="center" type="selection" width="55" />
        <el-table-column prop="ip" align="center" width="130" label="IP" />
        <el-table-column prop="address" align="center" width="200" label="IP来源" />
        <el-table-column prop="accessOs" align="center" label="平台" />
        <el-table-column prop="browser" align="center" label="浏览器" />
        <el-table-column prop="type" align="center" label="操作类型" />
        <el-table-column prop="description" align="center" label="操作日志" />
        <el-table-column prop="model" align="center" label="操作模块" />
        <el-table-column prop="result" align="center" label="操作结果" />
        <el-table-column prop="createTime" align="center" width="200" sortable label="操作时间">
          <template slot-scope="scope">
            <span>{{ dataFormat(scope.row.createTime) }}</span>
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
import { fetchUserLog, deleteUserLog } from '@/api/sys_log'
import { parseTime } from '@/utils'
import { remove } from '@/api/tags'
import { mapGetters } from 'vuex'
import { hasAuth } from '@/utils/auth'
export default {
  data() {
    return {
      tableData: [],
      multipleSelection: [],
      showSearch: true,
      total: null,
      params: {
        pageNo: 1,
        pageSize: 10
      },
      // 加载层信息
      loading: []
    }
  },
  computed: {
    ...mapGetters([
      'pres'
    ]),
    canDelete() {
      return hasAuth(this.pres, '/system/userLog/delete')
    }
  },
  created() {
    this.openLoading()
    this.fetchList()
  },
  methods: {
    fetchList: function() {
      fetchUserLog(this.params).then(res => {
        this.total = res.data.total
        this.tableData = res.data.records
        this.loading.close()
      }).catch(err => {
        console.log(err)
      })
    },
    handleDeleteBatch: function() {
      const ids = []
      this.multipleSelection.forEach(item => {
        ids.push(item.id)
      })
      this.$confirm('此操作将永久删除记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteUserLog(ids).then(res => {
          this.$message.success('删除用户日志成功')
          this.fetchList()
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
    // 打开加载层
    openLoading: function() {
      this.loading = this.$loading({
        lock: true,
        text: '正在加载中~',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      })
    },
    handleSizeChange: function(val) {
      this.params.pageSize = val
      this.fetchList()
    },
    handleSelectionChange: function(val) {
      this.multipleSelection = val
    },
    handleCurrentChange: function(val) {
      this.params.pageNo = val
      this.fetchList()
    },
    dataFormat: function(time) {
      return parseTime(time)
    }
  }
}
</script>
