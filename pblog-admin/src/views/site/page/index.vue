<template>
  <div class="app-container">
    <el-card class="main-card">
      <div>
        <el-button
          v-if="canAdd"
          style="margin-bottom: 10px"
          type="primary"
          size="small"
          icon="el-icon-plus"
          @click="openModel(null)"
        >新建页面
        </el-button>
      </div>
      <!-- 相册列表 -->
      <el-row v-loading="loading" class="page-container" :gutter="12">
        <!-- 空状态 -->
        <el-empty v-if="pageList.length === 0" description="暂无图片" />
        <el-col v-for="item of pageList" :key="item.id" :md="6">
          <div class="page-item">
            <!-- 相册操作 -->
            <div class="page-opreation">
              <el-dropdown @command="handleCommand">
                <i class="el-icon-more" style="color:#fff" />
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item v-if="canUpdate" :command="'update' + JSON.stringify(item)">
                    <i class="el-icon-edit" />编辑
                  </el-dropdown-item>
                  <el-dropdown-item v-if="canDel" :command="'delete' + item.id">
                    <i class="el-icon-delete" />删除
                  </el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
            </div>
            <el-image fit="cover" class="page-cover" :src="item.pageCover" />
            <div class="page-name">{{ item.pageName }}</div>
          </div>
        </el-col>
      </el-row>
      <!-- 新增模态框 -->
      <el-dialog center :title="title" :visible.sync="dialogVisible " width="35%" top="10vh">
        <el-form ref="dataForm" :rules="rules" label-width="80px" size="medium" :model="pageForum">
          <el-form-item prop="pageName" label="页面名称">
            <el-input v-model="pageForum.pageName" style="width:220px" />
          </el-form-item>
          <el-form-item prop="pageLabel" label="页面标签">
            <el-input v-model="pageForum.pageLabel" style="width:220px" />
          </el-form-item>
          <el-form-item prop="pageCover" label="页面封面">
            <el-upload
              v-loading="imgLoading"
              class="upload-cover"
              drag
              :show-file-list="false"
              multiple
              :action="uploadPictureHost"
              :before-upload="beforeUpload"
              :http-request="uploadSectionFile"
            >
              <i v-if="pageForum.pageCover === ''" class="el-icon-upload" />
              <div v-if="pageForum.pageCover === ''" class="el-upload__text">
                将文件拖到此处，或<em>点击上传</em>
              </div>
              <img
                v-else
                :src="pageForum.pageCover"
                width="360px"
                height="180px"
              >
            </el-upload>
          </el-form-item>
        </el-form>
        <div slot="footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submit">确定</el-button>
        </div>
      </el-dialog>
    </el-card>

  </div>
</template>

<script>
import { fetchPages, addPage, updatePage, removePage } from '@/api/page'
import { upload } from '@/api/imgUpload'
import { addPicture } from '@/api/picture'
import { mapGetters } from 'vuex'
import { hasAuth } from '@/utils/auth'

export default {
  created() {
    this.listPages()
  },
  computed: {
    ...mapGetters([
      'pres'
    ]),
    canAdd: function() {
      return hasAuth(this.pres, '/system/page/add')
    },
    canDel: function() {
      return hasAuth(this.pres, '/system/page/delete')
    },
    canUpdate: function() {
      return hasAuth(this.pres, '/system/page/update')
    }
  },
  data: function() {
    return {
      keywords: '',
      title: '',
      loading: true,
      imgLoading: false,
      current: 1,
      size: 8,
      count: 0,
      isEditForm: true,
      dialogVisible: false,
      img: process.env.VUE_APP_IMG_API,
      uploadPictureHost: process.env.VUE_APP_BASE_API + '/file/upload',
      pageForum: {
        id: null,
        pageName: '',
        pageLabel: '',
        pageCover: ''
      },
      pageList: [],
      rules: {
        'pageName': [{ required: true, message: '必填字段', trigger: 'change' }],
        'pageLabel': [{ required: true, message: '必填字段', trigger: 'change' }],
        'pageCover': [{ required: true, message: '必填字段', trigger: 'change' }]
      }
    }
  },
  methods: {
    openModel: function(item) {
      if (item) {
        this.pageForum = JSON.parse(item)
        this.title = '修改页面'
      } else {
        this.pageForum = {
          id: null,
          pageName: '',
          pageLabel: '',
          pageCover: ''
        }
        this.title = '新建页面'
        this.isEditForm = false
      }
      this.dialogVisible = true
    },
    listPages: function() {
      fetchPages().then(res => {
        this.pageList = res.data
        this.loading = false
      })
    },
    submit() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          if (this.isEditForm) {
            updatePage(this.pageForum).then(res => {
              this.$message.success('修改页面成功')
              this.closeDialogForm()
              this.listPages()
            }).catch(err => {
              console.log(err)
            })
          } else {
            addPage(this.pageForum).then(res => {
              this.$message.success('添加页面成功')
              this.pageList.unshift(res.data)
              this.dialogVisible = false
            }).catch(err => {
              console.log(err)
            })
          }
          this.dialogVisible = false
        } else {
          this.$message.error('存在必填字段未填')
        }
      })
    },
    uploadSectionFile: function(param) {
      this.files = param.file
      // FormData 对象
      var formData = new FormData()
      // 文件对象
      formData.append('multipartFile', this.files)
      upload(formData).then(res => {
        this.pageForum.pageCover = res.data
        this.imgLoading = false
      })
    },
    beforeUpload() {
      this.imgLoading = true
    },

    handleCommand: function(command) {
      const type = command.substring(0, 6)
      const data = command.substring(6)
      if (type === 'delete') {
        this.deletePage(data)
      } else {
        this.openModel(data)
      }
    },
    deletePage: function(id) {
      this.$confirm('此操作将把页面删除, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        removePage(id).then(({ data }) => {
          this.$message.success('删除页面成功')
          this.listPages()
        }).catch(err => {
          console.log(err)
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '取消删除'
        })
      })
    }
  }
}
</script>

<style scoped>
.page-cover {
  position: relative;
  border-radius: 4px;
  width: 100%;
  height: 170px;
}

.page-name {
  text-align: center;
  margin-top: 0.5rem;
}

.page-item {
  position: relative;
  cursor: pointer;
  margin-bottom: 1rem;
}

.page-opreation {
  position: absolute;
  z-index: 1000;
  top: 0.5rem;
  right: 0.8rem;
}
</style>
