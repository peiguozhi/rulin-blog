<template>
  <div class="app-container">
    <!--查询or添加-->
    <el-form v-show="showSearch" ref="form" :inline="true" :model="params" label-width="68px">
      <el-form-item label="用户名">
        <el-input v-model="params.name" style="width: 150px" size="small" placeholder="请输入用户名称" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="small" @click="handleFind">查找</el-button>
        <el-button icon="el-icon-refresh" size="small" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 表格区域 -->
    <div>
      <el-table border :data="tableData" style="width: 100%">
        <el-table-column align="center" prop="avatar" label="头像" width="180">
          <template slot-scope="scope">
            <div class="block"><el-avatar :size="50" :src="scope.row.avatar" /></div>
          </template>
        </el-table-column>
        <el-table-column align="center" prop="nickname" label="昵称" width="180" />
        <el-table-column align="center" prop="ip" label="IP地址" />
        <el-table-column align="center" prop="city" label="登录地址" />
        <el-table-column align="center" prop="browser" label="浏览器" />
        <el-table-column align="center" prop="os" label="操作系统" />
        <el-table-column align="center" prop="loginTime" label="登录时间" width="180" />
        <el-table-column align="center" label="操作">
          <template slot-scope="scope">
            <el-button v-if="canKick" type="danger" size="mini" @click="kick(scope)">强制下线</el-button>
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
import { onlineUser, kick } from '@/api/user'
import { mapGetters } from 'vuex'
import { hasAuth } from '@/utils/auth'
export default {
  data() {
    return {
      showSearch: true,
      params: {
        name: null,
        pageNo: 1,
        pageSize: 10
      },
      total: 0,
      // 加载层信息
      loading: [],
      tableData: []
    }
  },
  computed: {
    ...mapGetters([
      'pres'
    ]),
    canKick: function() {
      return hasAuth(this.pres, '/system/user/kick')
    }
  },
  created() {
    this.openLoading()
    this.fetchList()
  },
  methods: {
    fetchList: function() {
      onlineUser(this.params).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total
        this.loading.close()
      }).catch(err => {
        console.log(err)
      })
    },
    kick: function(scope) {
      kick({ token: scope.row.tokenValue }).then(res => {
        this.$message.success(res.message)
        this.fetchList()
      }).catch(err => {
        console.log(err)
      })
    },
    handleFind: function() {
      this.params.pageNo = 1
      this.fetchList()
    },
    resetQuery: function() {
      this.params.name = null
      this.fetchList()
    },
    handleSizeChange: function(val) {
      this.params.pageSize = val
      this.fetchList()
    },
    handleCurrentChange: function(val) {
      this.params.pageNo = val
      this.fetchList()
    },
    // 打开加载层
    openLoading: function() {
      this.loading = this.$loading({
        lock: true,
        text: '正在加载中~',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      })
    }
  }
}
</script>
