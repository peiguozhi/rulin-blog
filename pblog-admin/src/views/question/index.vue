<template>
  <div class="app-container">
    <!--查询or添加-->
    <el-form v-show="showSearch" :inline="true" ref="form" :model="params" label-width="68px">
      <el-form-item label="问题名称">
        <el-input style="width: 150px" size="small" v-model="params.quContent" placeholder="请输入问题名称"/>
      </el-form-item>
      <el-form-item label="分类名">
        <el-select style="width: 130px" size="small" v-model="params.qCategoryId" filterable clearable reserve-keyword
                   @change="handleFind" placeholder="请选择分类"
        >
          <el-option v-for="item in qCategory" :key="item.id" :label="item.name" :value="item.id"/>
        </el-select>
      </el-form-item>
      <el-form-item label="发布状态">
        <el-select style="width: 130px" size="small" v-model="params.isPublish" clearable
                   placeholder="是否发布" @change="handleFind"
        >
          <el-option v-for="(item,index) in publishList" :key="index" :label="item" :value="index"/>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="small" @click="handleFind">查找</el-button>
        <el-button icon="el-icon-refresh" size="small" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button v-if="canAdd" size="small" class="filter-item" @click="handleAdd" type="primary"
                   icon="el-icon-plus"
        >添加
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button size="small" :disabled="!multipleSelection.length" v-if="canDelBatch" class="filter-item"
                   @click="handleDelete"
                   type="danger"
                   icon="el-icon-delete"
        >批量删除
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="queryList"></right-toolbar>
    </el-row>

    <!--表格区域-->
    <div style="margin-top: 5px">
      <el-table
        border
        :data="tableData"
        style="width: 100%"
        :default-sort="{prop: 'sort', order: 'ascending'}"
        @selection-change="handleSelectionChange"
      >
        <el-table-column align="center" type="selection"/>
        <el-table-column align="center" width="60" label="序号">
          <template slot-scope="scope">
            <span>{{ scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="quContent" align="center" label="问题">
          <template slot-scope="scope">
            <el-link :underline="false" @click="onClick(scope.row)">{{ scope.row.quContent }}</el-link>
          </template>
        </el-table-column>
        <el-table-column align="center" label="分类" width="220">
          <template slot-scope="scope">
            <el-tag
              style="margin-left: 3px"
              align="center"
              type="warning"
            >{{ scope.row.qCategoryName }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column align="center" prop="sort" sortable label="排序" width="100">
          <template slot-scope="scope">
            <el-tag type="warning">{{ scope.row.sort }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="quantity" align="center" label="浏览量" width="100"/>
        <el-table-column
          width="200"
          align="center"
          prop="createTime"
          sortable
          label="添加时间"
        >
          <template slot-scope="scope">
            <span>{{ dataFormat(scope.row.createTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" label="操作">
          <template slot-scope="scope">
            <el-button v-if="canUpdate&& scope.row.isPublish === 1" type="info" size="mini" @click="offShelf(scope)">
              下架
            </el-button>
            <el-button v-if="canUpdate&& scope.row.isPublish === 0" type="success" size="mini" @click="release(scope)">
              发布
            </el-button>
            <el-button v-if="canUpdate" type="primary" size="mini" @click="handleUpdate(scope)">编辑</el-button>
            <el-button v-if="canDel" size="mini" type="danger" @click="remove(scope)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!--分页区域-->
      <div class="pagination-container" style="float: right;margin-bottom: 1.25rem;margin-top: 1.25rem;">
        <el-pagination background @size-change="handleSizeChange" @current-change="handleCurrentChange"
                       :current-page="params.pageNo" :page-size="params.pageSize" :page-sizes="[10, 20, 30]"
                       layout="total, sizes,prev, pager, next,jumper" :total="total"
        >
        </el-pagination>
      </div>

      <!--添加or修改区域-->
      <el-dialog
        :title="title"
        :visible.sync="centerDialogVisible"
        :before-close="closeModel"
        width="60%"
        center
      >
        <el-form :rules="rules" ref="dataForm" :model="question" style="margin-top: 10px">
          <el-row>
            <el-col :span="16">
              <el-form-item label="问题" :label-width="formLabelWidth" prop="quContent">
                <el-input v-model="question.quContent" auto-complete="off"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="6">
              <el-form-item label="分类" :label-width="formLabelWidth" prop="qCategoryName">
                <el-tag
                  type="success"
                  v-show="question.qCategoryName"
                  style="margin:0 1rem 0 0"
                  :closable="true"
                  @close="removeCategory"
                >
                  {{ question.qCategoryName }}
                </el-tag>
                <!-- 分类选项 -->
                <el-popover
                  placement="bottom-start"
                  width="460"
                  trigger="click"
                  v-if="!question.qCategoryName"
                >
                  <div class="popover-title">分类</div>
                  <!-- 输入框 -->
                  <el-input style="width:100%" v-model="qCategoryName" placeholder="请输入分类名,enter添加自定义分类"
                            @keyup.enter.native="saveCategory"
                  />
                  <!-- 分类 -->
                  <div class="popover-container">
                    <div>添加分类</div>
                    <el-tag
                      v-for="(item, index) of qCategory"
                      :key="index"
                      style="margin-left: 3px;margin-top: 2px"
                      class="qCategory-item"
                      @click="addCategory(item)"
                    >
                      {{ item.name }}
                    </el-tag>
                  </div>
                  <el-button type="success" plain slot="reference" size="small">
                    添加分类
                  </el-button>
                </el-popover>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item label="排序" prop="sort" :label-width="formLabelWidth">
                <el-input v-model="question.sort"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6.5">
              <el-form-item :label-width="formLabelWidth" label="是否发布" prop="isPublish">
                <el-radio-group v-model="question.isPublish" size="small">
                  <el-radio v-for="(item,index) in publishList" :key="index" :label="index" border>{{ item }}</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :spam="24">
              <el-form-item :label-width="formLabelWidth" label="答案" prop="analysisMd">
                <mavon-editor placeholder="输入答案..." style="height: 500px" ref="md"
                              v-model="question.analysisMd" @imgDel="imgDel" @change="" @imgAdd="imgAdd"
                />
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <span slot="footer" class="dialog-footer">
            <el-button @click="centerDialogVisible = false">取 消</el-button>
            <el-button v-if="canAdd || canUpdate" type="primary" @click="submit">确定</el-button>
        </span>
      </el-dialog>
    </div>
  </div>
</template>

<script>
  import {
    fetchQuestion,
    remove,
    update,
    top,
    save,
    info,
    deleteBatch,
    pubOrShelf
  } from '@/api/questions'
  import { upload, delBatchFile } from '@/api/imgUpload'
  import { fetchQCategory } from '@/api/qCategory'
  import { parseTime } from '@/utils'
  import { hasAuth } from '@/utils/auth'
  import { mapGetters } from 'vuex'
  import { getDataByDictType } from '@/api/dictData'

  export default {
    data() {
      return {
        uploadPictureHost: process.env.VUE_APP_BASE_API + '/file/upload',
        files: {},
        visible: false,
        isEditForm: 0,
        loadingReptile: false,
        centerDialogVisible: false,
        showSearch: true,
        formLabelWidth: '120px',
        tableData: [],
        dictYesOrNoList: [],
        yesOrNoDefaultValue: null,
        img: process.env.VUE_APP_IMG_API,
        BLOG_WEB_URL: process.env.VUE_APP_BLOG_WEB_API,
        yesOrNoList: ['否', '是'],
        yesOrNoStyle: ['danger', 'success'],
        publishList: ['下架', '发布'],
        total: null,
        multipleSelection: [],
        title: null,
        // 加载层信息
        loading: [],
        changeCount: 0,
        isChange: false,
        qCategory: [],
        quContent: null,
        qCategoryName: null,
        question: {},
        rules:
          {
            'quContent': [{ required: true, message: '必填字段', trigger: 'blur' }],
            'qCategoryName': [{ required: true, message: '必填字段', trigger: 'blur' }],
            'sort': [
              { required: true, message: '必填字段', trigger: 'blur' },
              { pattern: /^\d{1,7}$/, message: '请输入数字且不超过7位', trigger: 'blur' },
            ],
            'isPublish': [{ required: true, message: '必填字段', trigger: 'change' }],
            'analysisMd': [{ required: true, message: '必填字段', trigger: 'change' }]
          },
        params: {
          quContent: null,
          qCategoryId: null,
          isPublish: null,
          pageNo: 1,
          pageSize: 10
        }
      }
    },
    created() {
      this.openLoading()
      this.getDictList()
      this.getCategoryList()
      this.queryList()
    },
    computed: {
      ...mapGetters([
        'pres'
      ]),
      canAdd: function() {
        return hasAuth(this.pres, '/system/question/add')
      },
      canDel: function() {
        return hasAuth(this.pres, '/system/question/delete')
      },
      canDelBatch: function() {
        return hasAuth(this.pres, '/system/question/deleteBatch')
      },
      canUpdate: function() {
        return hasAuth(this.pres, '/system/question/update')
      }
    },
    methods: {
      getCategoryList: function() {
        fetchQCategory({ pageNo: 1, pageSize: 100 }).then(res => {
          this.qCategory = res.data.records
        })
      },
      addCategory: function(item) {
        this.question.qCategoryName = item.name
      },
      saveCategory: function() {
        if (this.qCategoryName.trim() !== '') {
          this.question.qCategoryName = this.qCategoryName
        }
      },
      removeCategory: function() {
        this.question.qCategoryName = null
      },
      getDictList: function() {
        let dictTypes = ['sys_publish_status', 'sys_yes_no']
        getDataByDictType(dictTypes).then(response => {
          let dictMap = response.data
          this.dictYesOrNoList = dictMap.sys_yes_no.list
          this.yesOrNoDefaultValue = dictMap.sys_yes_no.defaultValue
        }).catch(err => {
          console.error(err)
        })
      },
      queryList: function() {
        fetchQuestion(this.params).then(res => {
          this.tableData = res.data.records
          this.total = res.data.total
        }).catch(err => {
          console.log(err)
        })
        this.loading.close()
      },
      submit: function() {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            this.question.analysis = this.$refs.md.d_render
            if (this.isEditForm) {
              update(this.question).then(res => {
                this.$message.success('修改问题成功')
                this.closeDialogForm()
              }).catch(err => {
                console.log(err)
              })
            } else {
              save(this.question).then(res => {
                this.$message.success('添加问题成功')
                this.closeDialogForm()
              }).catch(err => {
                console.log(err)
              })
            }
          } else {
            this.$message.error('请填写正确的数据')
          }
        })
      },
      resetQuery: function() {
        this.params.quContent = null
        this.params.isPublish = null
        this.queryList()
      },
      handleFind: function() {
        this.params.pageNo = 1
        this.queryList()
      },
      release: function(scope) {
        let question = { id: scope.row.id, isPublish: 1 }
        pubOrShelf(question).then(res => {
          this.$message.success('发布问题成功')
          this.queryList()
        }).catch(err => {
          console.log(err)
        })
      },
      offShelf: function(scope) {
        let question = { id: scope.row.id, isPublish: 0 }
        pubOrShelf(question).then(res => {
          this.$message.success('下架问题成功')
          this.queryList()
        }).catch(err => {
          console.log(err)
        })
      },
      handleTop: function(scope) {
        let question = {
          id: scope.row.id,
          sort: scope.row.sort
        }
        top(question).then(res => {
          if (question.sort === 1) {
            this.$message.success('置顶问题成功')
          } else {
            this.$message.success('取消置顶成功')
          }
          this.queryList()
        }).catch(err => {
          console.log(err)
        })
      },
      onClick: function(row) {
        if (row.isPublish === 0) {
          this.$message.error('问题暂未发布，无法进行浏览')
          return false
        }
        window.open(this.BLOG_WEB_URL + 'questions/' + row.qCategoryId + '/' + row.id)
      },
      handleSelectionChange: function(val) {
        this.multipleSelection = val
      },
      remove: function(scope) {
        let id = scope.row.id
        this.$confirm('此操作将把问题删除, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          remove(id).then(res => {
            this.$message.success('删除问题成功')
            this.queryList()
          }).catch(err => {
            console.log(err)
          })
        }).catch(() => {
          this.$message.info('取消删除')
        })

      },
      handleDelete: function() {
        let ids = []
        this.multipleSelection.forEach(item => {
          ids.push(item.id)
        })
        this.$confirm('此操作将把问题删除, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          deleteBatch(ids).then(res => {
            this.$message.success('批量删除问题成功')
            this.queryList()
          }).catch(err => {
            console.log(err)
          })
        }).catch(() => {
          this.$message.info('取消删除')
        })

      },
      handleAdd: function() {
        let question = sessionStorage.getItem('question')
        if (question != null) {
          this.$confirm('还有上次未完成的问题编辑，是否继续编辑?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            this.initModel(JSON.parse(question))
          }).catch(() => {
            this.initModel(this.getFormObject())
            sessionStorage.removeItem('question')
          })
        } else {
          this.initModel(this.getFormObject())
        }
      },
      initModel: function(question) {
        this.question = question
        if (this.question.id != null) {
          this.beforeShow('修改问题', 1)
        } else {
          this.beforeShow('新增问题', 0)
        }
      },
      handleUpdate: function(scope) {
        info(scope.row.id).then(res => {
          this.question = res.data
          this.beforeShow('修改问题', 1)
        })
      },
      getFormObject: function() {
        return {
          id: null,
          quContent: '',
          quantity: 0,
          analysis: '',
          analysisMd: '',
          sort: 0,
          qCategoryName: null,
          isPublish: 1,
        }
      },
      beforeShow: function(title, isEditForm) {
        this.title = title
        this.isEditForm = isEditForm
        this.centerDialogVisible = true
      },
      closeDialogForm: function() {
        this.centerDialogVisible = false
        this.getCategoryList()
        this.queryList()
      },
      handleSizeChange: function(val) {
        this.params.pageSize = val
        this.queryList()
      },
      handleCurrentChange: function(val) {
        this.params.pageNo = val
        this.queryList()
      },
      closeModel: function(done) {
        if (this.question.quContent != null && this.question.quContent !== ''
          && ((this.question.qCategoryName != null && this.question.qCategoryName !== ''))) {
          this.$confirm('是否关闭博客编辑窗口', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            sessionStorage.setItem('question', JSON.stringify(this.question))
            done()
          }).catch(() => {
            this.$message.info('已取消')
          })
        } else {
          done()
        }
      },
      imgAdd: function(pos, $file) {
        var formdata = new FormData()
        formdata.append('multipartFile', $file)
        upload(formdata).then(res => {
          this.$refs.md.$img2Url(pos, res.data)
        }).catch(err => {
          console.log(err)
        })
      },
      imgDel: function(filename) {
        delBatchFile(filename[0].split(this.img)[1])
      },
      dataFormat: function(time) {
        return parseTime(time)
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

<style>

</style>

