<template>
  <div class="app-container">
    <el-form v-show="showSearch" :inline="true" ref="form" :model="params" label-width="68px">
      <el-form-item label="任务名称">
        <el-input style="width: 200px" size="small" v-model="params.jobName" placeholder="请输入任务名称" />
      </el-form-item>
      <el-form-item label="任务组名">
        <el-select size="small" @change='handleFind' clearable style="margin-left: 5px" v-model="params.jobGroup" placeholder="请选择组名">
          <el-option
            v-for="item in jobDictList"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          >
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="执行状态">
        <el-select size="small" @change='handleFind' clearable style="margin-left: 5px" v-model="params.status" placeholder="请选择任务状态">
          <el-option value="0" label="成功"/>
          <el-option value="1" label="失败"/>
        </el-select>
      </el-form-item>
      <el-form-item label="执行时间">
        <el-date-picker
          size="small"
          v-model="dateRange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleFind">查找</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          v-if="canDeleteBatch"
          :disabled="!ids.length"
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          @click="handleDelete"
        >批量删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          @click="handleClean"
          v-if="canClean"
        >清空
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-back"
          size="mini"
          @click="handleClose"
        >返回</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="fetchList"></right-toolbar>
    </el-row>

    <!--表格-->
    <el-table :default-sort = "{prop: 'createTime', order: 'descending'}" style="width: 100%" :data="jobLogList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="日志编号" width="80" align="center" prop="id">
        <template slot-scope="scope">
          {{scope.$index +1}}
        </template>
      </el-table-column>
      <el-table-column label="任务名称" align="center" prop="jobName" :show-overflow-tooltip="true"/>
      <el-table-column label="任务组名" align="center" prop="jobGroup" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <el-tag v-for="item in jobDictList" :type="item.style" v-if="scope.row.jobGroup === item.value">
            {{item.label}}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="调用目标字符串" align="center" prop="invokeTarget" :show-overflow-tooltip="true"/>
      <el-table-column label="日志信息" align="center" prop="jobMessage" :show-overflow-tooltip="true"/>
      <el-table-column label="执行状态" align="center" prop="status">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === '0'" type="success">成功</el-tag>
          <el-tag v-if="scope.row.status === '1'" type="danger">失败</el-tag>
        </template>
      </el-table-column>
      <el-table-column sortable label="执行时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ dataFormat(scope.row.startTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleView(scope.row)"

          >详细
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!--分页区域-->
    <div class="pagination-container" style="float: right;margin-bottom: 1.25rem;margin-top: 1.25rem;">
      <el-pagination background @size-change="handleSizeChange" @current-change="handleCurrentChange"
                     :current-page="params.pageNo" :page-size="params.pageSize" :page-sizes="[10, 20, 30]"
                     layout="total, sizes,prev, pager, next,jumper" :total="total">
      </el-pagination>
    </div>

    <!-- 调度日志详细 -->
    <el-dialog title="调度日志详细" :visible.sync="open" width="700px" append-to-body>
      <el-form ref="form" :model="form" label-width="100px" size="mini">
        <el-row>
          <el-col :span="12">
            <el-form-item label="日志序号：">{{ form.id }}</el-form-item>
            <el-form-item label="任务名称：">{{ form.jobName }}</el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="任务分组：">{{ form.jobGroup }}</el-form-item>
            <el-form-item label="执行时间：">{{ dataFormat(form.startTime) }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="调用方法：">{{ form.invokeTarget }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="日志信息：">{{ form.jobMessage }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="执行状态：">
              <div v-if="form.status === '0'">成功</div>
              <div v-else-if="form.status === '1'">失败</div>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="异常信息：" v-if="form.status === 1">{{ form.exceptionInfo }}</el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="open = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {fetchList,deleteBatch,clean} from "@/api/quartzLog";
import {parseTime} from '@/utils'
import {getDataByDictType} from "@/api/dictData"
import {mapGetters} from "vuex";
import {hasAuth} from "@/utils/auth";
export default {
  name: "JobLog",
  data() {
    return {
      // 加载层信息
      loading: [],
      // 选中数组
      ids: [],
      jobDictList:[],
      jobDictDefaultValue: null,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 调度日志表格数据
      jobLogList: [],
      // 是否显示弹出层
      open: false,
      // 日期范围
      dateRange: [],
      // 表单参数
      form: {},
      // 查询参数
      params: {
        pageNo: 1,
        pageSize: 10,
        startTime:null,
        endTime:null,
        jobId: null,
        jobName: null,
        jobGroup: null,
        status: null
      }
    };
  },
  created() {
    this.openLoading();
    let jobId = this.$route.query.jobId;
    if (jobId) {
      this.params.jobId = jobId
    }
    this.getDictList()
    this.fetchList();
  },
  computed:{
    ...mapGetters([
      'pres'
    ]),
    canDeleteBatch: function() {
      return hasAuth(this.pres, '/system/jobLog/deleteBatch')
    },
    canClean: function() {
      return hasAuth(this.pres, '/system/jobLog/clean')
    },
  },
  methods: {
    getDictList: function () {
      let dictTypeList = ['sys_job_group']
      getDataByDictType(dictTypeList).then(response => {
        let dictMap = response.data;
        this.jobDictList = dictMap.sys_job_group.list
        this.jobDictDefaultValue = dictMap.sys_job_group.defaultValue
      });
    },
    /** 查询调度日志列表 */
    fetchList: function () {
      fetchList(this.params).then(response => {
          this.jobLogList = response.data.records
          this.total = response.data.total;
          this.loading.close();
      }).catch(err =>{
        console.log(err)
      });
    },
    // 返回按钮
    handleClose() {
      this.$router.push({ path: '/quartz'})
    },
    /** 搜索按钮操作 */
    handleFind: function () {
      this.params.pageNo = 1;
      if (this.dateRange.length){
        this.params.startTime = this.dateRange[0]
        this.params.endTime = this.dateRange[1]
      }
      this.fetchList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = []
      this.params .startTime = null
      this.params .endTime = null
      this.params .jobName = null
      this.params .jobGroup = null
      this.params .status = null
      this.fetchList();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id);
    },
    /** 详细按钮操作 */
    handleView(row) {
      this.open = true;
      this.form = row;
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteBatch(this.ids).then(res => {
          this.$message.success("OK")
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
    /** 清空按钮操作 */
    handleClean() {
      this.$confirm('是否确认清空所有调度日志数据项?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        clean().then(res => {
          this.$message.success("OK")
          this.fetchList()
        }).catch(err => {
          console.error(err)
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '取消清空'
        })
      })
    },
    handleSizeChange: function (val) {
      this.params.pageSize = val
      this.fetchList()
    },
    handleCurrentChange: function (val) {
      this.params.pageNo = val
      this.fetchList()
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
    dataFormat: function(time){
      return parseTime(time)
    },
  }
};
</script>
