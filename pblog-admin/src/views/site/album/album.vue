<template>
  <div class="app-container">
    <el-card class="main-card">
      <!-- 标题 -->
      <div style="margin-bottom: 10px" class="operation-container">
        <el-input
          v-model="params.name"
          prefix-icon="el-icon-search"
          size="small"
          placeholder="请输入相册名"
          style="width:200px"
          @keyup.enter.native="handleFind"
        />
        <el-button
          type="primary"
          size="small"
          icon="el-icon-search"
          style="margin-left:1rem"
          @click="handleFind"
        >
          搜索
        </el-button>
        <el-button
          v-if="canAdd"
          type="primary"
          size="small"
          icon="el-icon-plus"
          @click="handleAdd()"
        >
          新建相册
        </el-button>

      </div>
      <!-- 相册列表 -->
      <el-row v-loading="loading" class="album-container" :gutter="12">
        <!-- 空状态 -->
        <el-empty v-if="albumList == null" description="暂无相册" />
        <el-col v-for="item of albumList" :key="item.id" :md="6">
          <div class="album-item" @click="checkPhoto(item)">
            <!-- 相册操作 -->
            <div class="album-opreation">
              <el-dropdown @command="handleCommand">
                <i class="el-icon-more" style="color:#fff" />
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item v-if="canUpdate" :command="'update' + item.id">
                    <i class="el-icon-edit" />编辑
                  </el-dropdown-item>
                  <el-dropdown-item v-if="canDel" :command="'delete' + item.id">
                    <i class="el-icon-delete" />删除
                  </el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
            </div>
            <div class="album-photo-count">
              <div>{{ item.photoCount }}</div>
              <i v-if="item.status === 2" class="iconfont el-icon-mymima" />
            </div>
            <el-image fit="cover" class="album-cover" :src="item.cover" />
            <div class="album-name">{{ item.name }}</div>
          </div>
        </el-col>
      </el-row>
      <!-- 分页 -->
      <el-pagination
        :hide-on-single-page="true"
        class="pagination-container"
        :current-page="params.pageNo"
        :page-size="params.pageSize"
        :total="total"
        layout="total, sizes,prev, pager, next,jumper"
        @size-change="sizeChange"
        @current-change="currentChange"
      />
      <!-- 新增模态框 -->
      <el-dialog center :title="title" :visible.sync="dialogVisible" width="35%" top="10vh">
        <el-form ref="dataForm" :rules="rules" label-width="80px" size="medium" :model="albumForum">
          <el-form-item label="相册名称" prop="name">
            <el-input v-model="albumForum.name" style="width:220px" />
          </el-form-item>
          <el-form-item label="相册描述" prop="info">
            <el-input v-model="albumForum.info" style="width:220px" />
          </el-form-item>
          <el-form-item label="相册封面" prop="cover">
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
              <i v-if="albumForum.cover === ''" class="el-icon-upload" />
              <div v-if="albumForum.cover === ''" class="el-upload__text">
                将文件拖到此处，或<em>点击上传</em>
              </div>
              <img
                v-else
                :src="albumForum.cover"
                width="360px"
                height="180px"
              >
            </el-upload>
          </el-form-item>
          <el-form-item label="发布形式" prop="status">
            <el-radio-group v-model="albumForum.status">
              <el-radio :label="0">公开</el-radio>
              <el-radio :label="1">私密</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-form>
        <div slot="footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submit"> 确定</el-button>
        </div>
      </el-dialog>
    </el-card>

  </div>
</template>
<script>
import { fetchAlbum, remove, addAlbum, updateAlbum, info } from '@/api/photoAlbum'
import { upload } from '@/api/imgUpload'
import { removePage } from '@/api/page'
import { mapGetters } from 'vuex'
import { hasAuth } from '@/utils/auth'
export default {
  data: function() {
    return {
      loading: true,
      imgLoading: false,
      img: process.env.VUE_APP_IMG_API,
      uploadPictureHost: process.env.VUE_APP_BASE_API + '/file/upload',
      isEditForm: false,
      dialogVisible: false,
      albumForum: {
        id: null,
        name: '',
        info: '',
        cover: '',
        status: 0
      },
      params: {
        pageNo: 1,
        pageSize: 10,
        name: null
      },
      albumList: [],
      total: 0,
      title: null,
      rules: {
        'name': [{ required: true, message: '必填字段', trigger: 'blur' }],
        'cover': [{ required: true, message: '必填字段', trigger: 'change' }],
        'info': [{ required: true, message: '必填字段', trigger: 'blur' }],
        'status': [{ required: true, message: '必填字段', trigger: 'change' }]
      }
    }
  },
  created() {
    this.listAlbums()
  },
  computed: {
    ...mapGetters([
      'pres'
    ]),
    canAdd: function() {
      return hasAuth(this.pres, '/system/album/add')
    },
    canDel: function() {
      return hasAuth(this.pres, '/system/album/delete')
    },
    canUpdate: function() {
      return hasAuth(this.pres, '/system/album/update')
    }
  },
  methods: {
    listAlbums: function() {
      fetchAlbum(this.params).then(res => {
        this.albumList = res.data.records
        this.total = res.data.total
        this.loading = false
      })
    },
    submit: function() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          if (this.isEditForm) {
            updateAlbum(this.albumForum).then(res => {
              this.$message.success('修改相册成功')
              this.listAlbums()
              this.dialogVisible = false
            }).catch(err => {
              console.log(err)
            })
          } else {
            addAlbum(this.albumForum).then(res => {
              this.$message.success('添加相册成功')
              this.listAlbums()
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
    handleAdd: function() {
      this.albumForum = {
        id: null,
        name: '',
        info: '',
        cover: '',
        status: 0
      }
      this.title = '新建相册'
      this.dialogVisible = true
    },
    handleEdit: function() {
      info(this.albumForum.id).then(res => {
        this.albumForum = res.data
      })
      this.title = '修改相册'
      this.isEditForm = true
      this.dialogVisible = true
    },
    checkPhoto: function(item) {
      this.$router.push({ path: '/photos', query: { albumId: item.id }})
    },
    beforeUpload: function() {
      this.imgLoading = true
    },
    uploadSectionFile: function(param) {
      this.files = param.file
      // FormData 对象
      var formData = new FormData()
      // 文件对象
      formData.append('multipartFile', this.files)
      upload(formData).then(res => {
        this.albumForum.cover = res.data
        this.imgLoading = false
      })
    },
    handleCommand: function(command) {
      const type = command.substring(0, 6)
      this.albumForum.id = command.substring(6)
      if (type === 'delete') {
        this.deleteAlbum()
      } else {
        this.handleEdit()
      }
    },
    deleteAlbum: function() {
      this.$confirm('此操作将把页面删除, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        remove(this.albumForum.id).then(res => {
          this.$message.success('删除相册成功')
          this.listAlbums()
        }).catch(err => {
          console.log(err)
        })
      }).catch(() => {
        this.$message.info('取消删除')
      })
    },
    handleFind: function() {
      this.params.pageNo = 1
      this.listAlbums()
    },
    sizeChange: function(size) {
      this.params.pageSize = size
      this.listAlbums()
    },
    currentChange: function(current) {
      this.params.pageNo = current
      this.listAlbums()
    }
  }
}
</script>

<style scoped>
.album-cover {
  position: relative;
  border-radius: 4px;
  width: 100%;
  height: 170px;
}
.album-cover::before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
}
.album-photo-count {
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 1.5rem;
  z-index: 1000;
  position: absolute;
  left: 0;
  right: 0;
  padding: 0 0.5rem;
  bottom: 2.6rem;
  color: #fff;
}
.album-name {
  text-align: center;
  margin-top: 0.5rem;
}
.album-item {
  position: relative;
  cursor: pointer;
  margin-bottom: 1rem;
}
.album-opreation {
  position: absolute;
  z-index: 1000;
  top: 0.5rem;
  right: 0.8rem;
}
</style>
