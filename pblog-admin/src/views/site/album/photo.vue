<template>
  <div class="app-container">
    <el-card class="main-card">
      <!-- 相册信息 -->
      <div class="album-info">
        <el-image fit="cover" class="album-cover" :src="albumInfo.cover"/>
        <div class="album-detail">
          <div style="margin-bottom:0.6rem">
            <span class="album-name">{{ albumInfo.name }}</span>
            <span class="photo-count">{{ albumInfo.photoCount }}张</span>
          </div>
          <div>
          <span v-if="albumInfo.info" class="album-desc">
            {{ albumInfo.info }}
          </span>
            <el-button
              v-if="canAdd"
              icon="el-icon-picture"
              type="primary"
              size="small"
              @click="handleAdd"
            >
              添加照片
            </el-button>
          </div>
        </div>
        <!-- 相册操作 -->
        <div class="operation">
          <div class="all-check">
            <el-checkbox
              :indeterminate="isIndeterminate"
              v-model="checkAll"
              @change="handleCheckAllChange"
            >
              全选
            </el-checkbox>
            <div class="check-count">已选择{{ selectPhotoIdList.length }}张</div>
          </div>
          <el-button
            v-if="canMove"
            type="success"
            @click="movePhoto = true"
            :disabled="selectPhotoIdList.length === 0"
            size="small"
            icon="el-icon-deleteItem"
          >
            移动到
          </el-button>
          <el-button
            v-if="canDel"
            type="danger"
            @click="deletePhoto"
            :disabled="selectPhotoIdList.length === 0"
            size="small"
            icon="el-icon-deleteItem"
          >
            批量删除
          </el-button>
        </div>
      </div>
      <!-- 照片列表 -->
      <el-row class="photo-container" :gutter="10" v-loading="loading">
        <!-- 空状态 -->
        <el-empty v-if="photoList.length === 0" description="暂无照片"/>
        <el-checkbox-group
          v-model="selectPhotoIdList"
          @change="handleCheckedPhotoChange"
        >
          <el-col :md="4" v-for="item of photoList" :key="item.id">
            <el-checkbox :label="item.id">
              <div class="photo-item">
                <!-- 照片操作 -->
                <div class="photo-opreation">
                  <el-dropdown @command="handleCommand">
                    <i class="el-icon-more" style="color:#fff"/>
                    <el-dropdown-menu slot="dropdown">
                      <el-dropdown-item v-if="canUpdate" :command="'update' + item.id">
                        <i class="el-icon-edit"/>编辑
                      </el-dropdown-item>
                      <el-dropdown-item v-if="canDel" :command="'delete' + item.id">
                        <i class="el-icon-delete"/>删除
                      </el-dropdown-item>
                    </el-dropdown-menu>
                  </el-dropdown>
                </div>
                <el-image
                  fit="cover"
                  class="photo-img"
                  :src="item.url"
                  :preview-photoSrc-list="photoList"
                />
                <div class="photo-name">{{ item.name }}</div>
              </div>
            </el-checkbox>
          </el-col>
        </el-checkbox-group>
      </el-row>
      <!-- 分页 -->
      <el-pagination
        :hide-on-single-page="true"
        class="pagination-container"
        @size-change="sizeChange"
        @current-change="currentChange"
        :current-page="params.pageNo"
        :page-size="params.pageSize"
        :total="total"
        layout="prev, pager, next"
      />

      <!-- 新增编辑对话框 -->
      <el-dialog center :title="title" :visible.sync="dialogVisible" width="35%">
        <el-form :rules="rules" ref="dataForm" label-width="80px" size="medium" :model="photoForm">
          <el-form-item label="照片名称" prop="name">
            <el-input style="width:220px" v-model="photoForm.name"/>
          </el-form-item>
          <el-form-item label="照片描述" prop="info">
            <el-input style="width:220px" v-model="photoForm.info"/>
          </el-form-item>
          <el-form-item label="照片" prop="url">
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
              <i class="el-icon-upload" v-if="photoForm.url === ''"/>
              <div class="el-upload__text" v-if="photoForm.url === ''">
                将文件拖到此处，或<em>点击上传</em>
              </div>
              <img
                v-else
                :src="photoForm.url"
                width="360px"
                height="180px"
              />
            </el-upload>
          </el-form-item>
        </el-form>
        <div slot="footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submit">确定</el-button>
        </div>
      </el-dialog>

      <!-- 移动对话框 -->
      <el-dialog :visible.sync="movePhoto" width="30%">
        <div class="dialog-title-container" slot="title">
          移动照片
        </div>
        <el-empty v-if="albumList.length < 2" description="暂无其他相册"/>
        <el-form v-else label-width="80px" size="medium" :model="photoForm">
          <el-radio-group v-model="albumId">
            <div class="album-check-item">
              <template v-for="item of albumList">
                <el-radio
                  v-if="item.id !== albumInfo.id"
                  :key="item.id"
                  :label="item.id"
                  style="margin-bottom:1rem"
                >
                  <div class="album-check">
                    <el-image
                      fit="cover"
                      class="album-check-cover"
                      :src="item.cover"
                    />
                    <div style="margin-left:0.5rem">{{ item.name }}</div>
                  </div>
                </el-radio>
              </template>
            </div>
          </el-radio-group>
        </el-form>
        <div slot="footer">
          <el-button @click="movePhoto = false">取 消</el-button>
          <el-button
            :disabled="albumId == null"
            type="primary"
            @click="updatePhotoAlbum"
          >
            确 定
          </el-button>
        </div>
      </el-dialog>
    </el-card>
  </div>
</template>

<script>
import {fetchPhoto, deleteBatch, addPhoto, updatePhoto, infoPhoto,movePhoto} from '@/api/photo'
import {fetchAlbum, info} from '@/api/photoAlbum'
import {upload} from "@/api/imgUpload";
import {mapGetters} from "vuex";
import {hasAuth} from "@/utils/auth";

export default {
  created() {
    this.getAlbumInfo();
    this.listAlbums();
    this.listPhotos();
  },
  data: function () {
    return {
      loading: true,
      checkAll: false,
      isIndeterminate: false,
      imgLoading: false,
      uploadPictureHost: process.env.VUE_APP_BASE_API + "/file/upload",
      img: process.env.VUE_APP_IMG_API,
      dialogVisible: false,
      isEditForm: false,
      title: null,
      movePhoto: false,
      photoList: [],
      photoIdList: [],
      selectPhotoIdList: [],
      albumList: [],
      albumInfo: {
        id: null,
        name: "",
        info: "",
        cover: "",
        photoCount: null
      },
      photoForm: {
        id: null,
        name: "",
        info: ""
      },
      albumId: null,
      total: 0,
      params: {
        pageNo: 1,
        pageSize: 18,
        albumId: this.$route.query.albumId,
      },
      rules: {
        'name': [{required: true, message: '必填字段', trigger: 'blur'}],
        'info': [{required: true, message: '必填字段', trigger: 'blur'}],
        'url': [{required: true, message: '必填字段', trigger: 'change'}],
      }
    };
  },
  computed:{
    ...mapGetters([
      'pres'
    ]),
    canAdd: function () {
      return hasAuth(this.pres, '/system/photo/add')
    },
    canDel: function () {
      return hasAuth(this.pres, '/system/photo/deleteBatch')
    },
    canMove: function () {
      return hasAuth(this.pres, '/system/photo/movePhoto')
    },
    canUpdate: function () {
      return hasAuth(this.pres, '/system/photo/update')
    },
  },
  methods: {
    getAlbumInfo: function () {
      info(this.$route.query.albumId).then(res => {
        this.albumInfo = res.data;
      });
    },
    listAlbums: function () {
      fetchAlbum({pageNo: 1, pageSize: 100}).then(res => {
        this.albumList = res.data.records;
      });
    },
    listPhotos: function () {
      fetchPhoto(this.params).then(res => {
        this.photoList = res.data.records;
        this.total = res.data.total;
        this.loading = false;
      });
    },
    submit: function () {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          if (this.isEditForm) {
            updatePhoto(this.photoForm).then(res => {
              this.$message.success("修改照片成功")
              this.listPhotos();
              this.dialogVisible = false;
            }).catch(err => {
              console.log(err)
            })
          } else {
            addPhoto(this.photoForm).then(res => {
              this.$message.success("添加照片成功")
              this.listPhotos()
              this.dialogVisible = false;
            }).catch(err => {
              console.log(err)
            })
          }
        } else {
          this.$message.error("存在必填字段未填")
        }
      })
    },

    handleAdd: function () {
      this.photoForm = {
        id: null,
        albumId: this.$route.query.albumId,
        name: "",
        info: "",
        url: "",
      }
      this.title = "新增照片"
      this.dialogVisible = true;
    },
    updatePhotoAlbum: function () {
      movePhoto({albumId: this.albumId, ids: this.selectPhotoIdList}).then(res => {
        this.$message.success("移动照片成功")
        this.getAlbumInfo();
        this.listPhotos();
        this.movePhoto = false;
      }).catch(err =>{
        console.log(err)
      });
    },
    beforeUpload: function () {
      this.imgLoading = true
    },
    uploadSectionFile: function (param) {
      this.files = param.file
      // FormData 对象
      var formData = new FormData()
      // 文件对象
      formData.append('multipartFile', this.files)
      upload(formData).then(res => {
        this.photoForm.url =  res.data
        this.imgLoading = false
      })
    },
    handleCheckAllChange: function (val) {
      this.selectPhotoIdList = val ? this.photoIdList : [];
      this.isIndeterminate = false;
    },
    handleCheckedPhotoChange: function (value) {
      let checkedCount = value.length;
      this.checkAll = checkedCount === this.photoIdList.length;
      this.isIndeterminate =
        checkedCount > 0 && checkedCount < this.photoIdList.length;
    },
    handleCommand: function (command) {
      const type = command.substring(0, 6);
      let id = command.substring(6);
      if (type === 'delete') {
        this.deletePhoto(id)
      } else {
        this.handleUpdate(id)
      }
    },
    handleUpdate: function (id) {
      infoPhoto(id).then(res => {
        this.photoForm = res.data
      })
      this.title = "修改照片"
      this.isEditForm = true
      this.dialogVisible = true;
    },
    deletePhoto: function (id) {
      if (id == null) {
        this.selectPhotoIdList.push(id)
      }
      this.$confirm('此操作将把页面删除, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteBatch(this.selectPhotoIdList).then(res => {
          this.$message.success("删除照片成功")
          this.listPhotos();
        }).catch(err => {
          console.log(err)
        });
      }).catch(() => {
        this.$message.info("取消删除")
      })
    },
    sizeChange: function (size) {
      this.params.pageSize = size;
      this.listPhotos();
    },
    currentChange: function (current) {
      this.params.pageNo = current;
      this.listPhotos();
    },
  },
  watch: {
    photoList() {
      this.photoIdList = [];
      this.photoList.forEach(item => {
        this.photoIdList.push(item.id);
      });
    }
  }
};
</script>

<style scoped>
.album-info {
  display: flex;
  margin-top: 2.25rem;
  margin-bottom: 2rem;
}

.album-cover {
  border-radius: 4px;
  width: 5rem;
  height: 5rem;
}

.album-check-cover {
  border-radius: 4px;
  width: 4rem;
  height: 4rem;
}

.album-detail {
  padding-top: 0.4rem;
  margin-left: 0.8rem;
}

.album-desc {
  font-size: 14px;
  margin-right: 0.8rem;
}

.operation {
  padding-top: 1.5rem;
  margin-left: auto;
}

.all-check {
  display: inline-flex;
  align-items: center;
  margin-right: 1rem;
}

.check-count {
  margin-left: 1rem;
  font-size: 12px;
}

.album-name {
  font-size: 1.25rem;
}

.photo-count {
  font-size: 12px;
  margin-left: 0.5rem;
}

.photo-item {
  width: 100%;
  position: relative;
  cursor: pointer;
  margin-bottom: 1rem;
}

.photo-img {
  width: 100%;
  height: 7rem;
  border-radius: 4px;
}

.photo-name {
  font-size: 14px;
  margin-top: 0.3rem;
  text-align: center;
}

.upload-container {
  height: 400px;
}

.upload {
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.upload-footer {
  display: flex;
  align-items: center;
}

.photo-opreation {
  position: absolute;
  z-index: 1000;
  top: 0.3rem;
  right: 0.5rem;
}

.album-check {
  display: flex;
  align-items: center;
}
</style>
