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
        fixed="right"
        @selection-change="handleSelectionChange"
      >
        <el-table-column
          align="center"
          type="selection"
          width="55"
        />
        <el-table-column prop="exceptionMessage" :show-overflow-tooltip="true" align="center" width="270" label="异常内容" />
        <el-table-column prop="operation" align="center" width="200" label="接口名" />
        <el-table-column prop="ip" align="center" width="130" label="IP" />
        <el-table-column prop="ipSource" align="center" width="200" label="IP来源" />
        <el-table-column prop="username" align="center" width="130" label="操作人" />
        <el-table-column prop="createTime" align="center" width="200" label="创建时间">
          <template slot-scope="scope">
            <span>{{ dataFormat(scope.row.createTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center">
          <template slot-scope="scope">
            <el-button type="info" size="mini" @click="details(scope.row)">详情</el-button>
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
    <div>
      <el-dialog
        title="异常详情"
        :visible.sync="dialogVisible"
        :fullscreen="true"
      >
        <h3>请求参数</h3>
        <span>{{ object.params }}</span>
        <h3>异常详情</h3>
        <span>{{ object.exceptionJson }}}</span>
      </el-dialog>
    </div>
  </div>
</template>
<script>
import { deleteExceptionLog, fetchExceptionLog } from '@/api/sys_log'
import { parseTime } from '@/utils'
import { mapGetters } from 'vuex'
import { hasAuth } from '@/utils/auth'

export default {
  data() {
    return {
      tableData: [],
      multipleSelection: [],
      total: null,
      object: {},
      dialogVisible: false,
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
      return hasAuth(this.pres, '/system/exceptionLog/delete')
    }
  },
  created() {
    this.openLoading()
    this.fetchList()
  },
  methods: {
    fetchList: function() {
      fetchExceptionLog(this.params).then(res => {
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
        deleteExceptionLog(ids).then(res => {
          this.$message.success('删除异常日志成功')
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
    details: function(row) {
      this.dialogVisible = true
      this.object = row
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
