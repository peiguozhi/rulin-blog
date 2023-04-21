<template>
  <div class="app-container">
    <!-- 查询和其他操作 -->
    <el-form v-show="showSearch" :inline="true" ref="form" :model="params" label-width="68px">
      <el-form-item label="反馈类型">
        <el-select  size="small" v-model="params.type" filterable clearable reserve-keyword
                   @change='handleFind' placeholder="请选择反馈类型"
        >
          <el-option :key="1" label="需求" :value="1"/>
          <el-option :key="2" label="反馈" :value="2"/>
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
          v-if="canDelBatch"
          :disabled="!multipleSelection.length"
          type="danger"
          icon="el-icon-delete"
          size="small"
          @click="handleDeleteBatch"
        >批量删除
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="fetchFeedback"></right-toolbar>
    </el-row>

    <div style="margin-top: 5px">
      <el-table border :data="tableData" style="width: 100%" @selection-change="handleSelectionChange">
        <el-table-column type="selection" align="center"/>
        <el-table-column prop="type" align="center"  label="反馈类型">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.type === 1" type="success">需求</el-tag>
            <el-tag v-else type="danger">缺陷</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="email" align="center"  width="180" label="联系邮箱" />
        <el-table-column prop="title" align="center" width="250" label="需求标题" />
        <el-table-column prop="content" align="center" width="250" label="详细内容" />
        <el-table-column prop="imgUrl" width="160" align="center"label="附加图片" />
        <el-table-column prop="createTime" width="160" align="center"label="反馈时间" >
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
      <el-pagination background @size-change="handleSizeChange" @current-change="handleCurrentChange"
                     :current-page="params.pageNo" :page-size="params.pageSize" :page-sizes="[10, 20, 30]"
                     layout="total, sizes,prev, pager, next,jumper" :total="total">
      </el-pagination>
    </div>
  </div>
</template>
<script>
import {deleteBatchFeedback, fetchFeedback} from '@/api/feedback'
import {parseTime} from '@/utils'
import {mapGetters} from "vuex";
import {hasAuth} from "@/utils/auth";

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
        pageNo: 1,
        pageSize: 10,
        type:null
      }
    }
  },
  created: function () {
    this.openLoading();
    this.fetchFeedback()
  },
  computed: {
    ...mapGetters([
      'pres'
    ]),
    canDelBatch: function () {
      return hasAuth(this.pres, '/system/feedback/deleteBatch')
    },
  },
  methods: {
    fetchFeedback: function () {
      fetchFeedback(this.params).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total
        this.loading.close();
      }).catch(err => {
        console.log(err)
      })
    },
    resetQuery : function (){
      this.params.pageNo = 1
      this.params.type = null
      this.fetchFeedback()
    },
    handleFind : function (){
      this.params.pageNo = 1
      this.fetchFeedback()
    },
    handleDeleteBatch: function (id) {
      this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        let ids =[]
        if (id != null) {
          ids.push(id)
        }else {
          this.multipleSelection.forEach(item =>{
            ids.push(item.id)
          })
        }
        deleteBatchFeedback(ids).then(res => {
          this.$message.success("删除成功")
          this.fetchFeedback()
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
    handleSelectionChange: function (val) {
      this.multipleSelection = val;
    },
    handleSizeChange: function (val) {
      this.params.size = val
      this.fetchFeedback()
    },
    handleCurrentChange: function (val) {
      this.params.page = val
      this.fetchFeedback()
    },
    formatTime: function (time) {
      return parseTime(time)
    },
  }
}
</script>
