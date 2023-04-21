<template>
  <div class="app-container">
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
        <el-table-column type="expand">
          <template slot-scope="scope">
            <el-form label-position="left" inline class="demo-table-expand">
              <el-row>
                <el-form-item label="请求接口">
                  <span>{{ scope.row.classPath + scope.row.requestUrl }}</span>
                </el-form-item>
              </el-row>
              <el-row>
                <el-form-item label="请求参数">
                  <span>{{ scope.row.paramsJson }}</span>
                </el-form-item>
              </el-row>
            </el-form>
          </template>
        </el-table-column>
        <el-table-column align="center" type="selection" width="55" />
        <el-table-column prop="username" align="center" width="100" label="操作人" />
        <el-table-column prop="requestUrl" align="center" width="200" label="请求接口" />
        <el-table-column prop="type" align="center" label="请求方式">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.type === 'POST'" type="success">{{ scope.row.type }}</el-tag>
            <el-tag v-if="scope.row.type === 'DELETE'" type="danger">{{ scope.row.type }}</el-tag>
            <el-tag v-if="scope.row.type === 'GET'">{{ scope.row.type }}</el-tag>
            <el-tag v-if="scope.row.type === 'PUT'" type="warning">{{ scope.row.type }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="operationName" align="center" width="130" label="接口名" />
        <el-table-column prop="ip" width="130" align="center" label="IP" />
        <el-table-column prop="source" align="center" width="200" label="IP来源" />
        <el-table-column prop="spendTime" align="center" label="请求耗时">
          <template slot-scope="scope">
            <span><el-tag type="info">{{ scope.row.spendTime }} ms</el-tag></span>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" align="center" width="200" label="创建时间">
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
import { deleteAdminLog, fetchAdminLog } from '@/api/sys_log'
import { parseTime } from '@/utils'
import { mapGetters } from 'vuex'
import { hasAuth } from '@/utils/auth'
export default {
  data() {
    return {
      tableData: [],
      multipleSelection: [],
      total: null,
      showSearch: true,
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
      return hasAuth(this.pres, '/system/adminLog/delete')
    }
  },
  created() {
    this.openLoading()
    this.fetchList()
  },
  methods: {
    fetchList: function() {
      fetchAdminLog(this.params).then(res => {
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
        deleteAdminLog(ids).then(res => {
          this.$message.success('删除操作日志成功')
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
    handleSelectionChange: function(val) {
      this.multipleSelection = val
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
