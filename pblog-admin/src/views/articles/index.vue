<template>
  <div class="app-container">
    <!--查询or添加-->
    <el-form v-show="showSearch" :inline="true" ref="form" :model="params" label-width="68px">
      <el-form-item label="文章名称">
        <el-input style="width: 150px" size="small" v-model="params.title" placeholder="请输入文章名称"/>
      </el-form-item>
      <el-form-item label="标签名">
        <el-select style="width: 130px" size="small" v-model="params.tagId" filterable clearable reserve-keyword
                   @change='handleFind' placeholder="请选择标签"
        >
          <el-option v-for="item in tags" :key="item.id" :label="item.name" :value="item.id"/>
        </el-select>
      </el-form-item>
      <el-form-item label="分类名">
        <el-select style="width: 130px" size="small" v-model="params.categoryId" filterable clearable reserve-keyword
                   @change='handleFind' placeholder="请选择分类"
        >
          <el-option v-for="item in category" :key="item.id" :label="item.name" :value="item.id"/>
        </el-select>
      </el-form-item>
      <el-form-item label="发布状态">
        <el-select style="width: 130px" size="small" v-model="params.isPublish" filterable clearable reserve-keyword
                   placeholder="是否发布" @change='handleFind'>
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
                   icon="el-icon-plus">添加
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button v-if="canReptile" size="small" class="filter-item" @click="dialogTableVisible = true" type="primary"
                   icon="el-icon-cpu">文章抓取
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button size="small" v-if="canUpdate" :disabled="!multipleSelection.length" class="filter-item" @click="seo"
                   type="info"
                   icon="el-icon-position">批量SEO
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button size="small" :disabled="!multipleSelection.length" v-if="canDelBatch" class="filter-item" @click="handleDelete"
                   type="danger"
                   icon="el-icon-delete">批量删除
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
        @selection-change="handleSelectionChange">
        <el-table-column width="60" align="center" type="selection"/>
        <el-table-column
          width="170"
          align="center"
          label="文章封面">
          <template slot-scope="scope">
            <el-image
              class="article-cover"
              :src="scope.row.avatar"
             />
            <i id="imgIcon"
              :class="scope.row.isSecret === 0?'el-icon-view':'el-icon-lock'"
            />
          </template>
        </el-table-column>
        <el-table-column prop="title" align="center" label="文章名称" width="220">
          <template slot-scope="scope">
            <el-link :underline="false" @click="onClick(scope.row)">{{ scope.row.title }}</el-link>
          </template>
        </el-table-column>
<!--        <el-table-column prop="summary" align="center" width="180" :show-overflow-tooltip="true" label="文章简介"/>-->
        <el-table-column align="center" width="116" label="类型">
          <template slot-scope="scope">
            <el-tag
              :type="scope.row.isOriginal === 0?'warning':'success'"
              :key="index"
              v-if="scope.row.isOriginal===index"
              v-for="(item, index) in isOriginalList"
            >{{ item }} </el-tag>
          </template>
        </el-table-column>
        <el-table-column align="center" width="125" label="分类">
          <template slot-scope="scope">
            <el-tag
              style="margin-left: 3px"
              align="center"
              type="warning"
            >{{ scope.row.categoryName }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          width="220"
          label="标签">
          <template slot-scope="scope">
            <el-tag
              style="margin-left: 3px"
              align="center"
              type="primary"
              v-for="(item) in strSplit(scope.row.tagNames)"
            >{{item}}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="isStick"
          align="center"
          width="120"
          label="置顶">
          <template slot-scope="scope">
            <el-switch
              v-model="scope.row.isStick"
              :active-value="1"
              :inactive-value="0"
              @change="handleTop(scope)"
              active-color="#13ce66">
            </el-switch>
          </template>
        </el-table-column>
        <el-table-column prop="quantity" width="120" align="center" label="浏览量" />
        <el-table-column
          width="200"
          align="center"
          prop="createTime"
          sortable
          label="添加时间">
          <template slot-scope="scope">
            <span>{{ dataFormat(scope.row.createTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column width="300" align="center" label="操作">
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
                       layout="total, sizes,prev, pager, next,jumper" :total="total">
        </el-pagination>
      </div>

      <!--添加or修改区域-->
      <el-dialog
        :title="title"
        :visible.sync="centerDialogVisible"
        :before-close="closeModel"
        fullscreen
        center
      >
        <el-form :rules="rules" ref="dataForm" :model="article" style="margin-top: 10px">
          <el-row>
            <el-col :span="14">
              <el-form-item label="文章名称" :label-width="formLabelWidth" prop="title">
                <el-input v-model="article.title" auto-complete="off"></el-input>
              </el-form-item>
              <el-form-item label="文章简介" :label-width="formLabelWidth" prop="summary">
                <el-input auto-complete="off" v-model="article.summary" ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="标题图" :label-width="formLabelWidth">
                <el-col :span="2">
                  <el-popover
                    placement="top"
                    width="160"
                    trigger="hover"
                    v-model="visible">
                    <p>随机获取一张图片</p>
                    <div style="text-align: right; margin: 0">
                      <el-button size="mini" type="text" @click="visible = false">取消</el-button>
                      <el-button type="primary" size="mini" @click="randomImg()">确定</el-button>
                    </div>
                    <svg-icon slot="reference" icon-class="wenhao" />
                  </el-popover>
                </el-col>
                <el-col :span="2">
                  <el-upload
                    style="width: 80px;height: 80px"
                    class="avatar-uploader"
                    :show-file-list="false"
                    ref="upload"
                    name="filedatas"
                    :action="uploadPictureHost"
                    :before-upload="uploadBefore"
                    :http-request="uploadSectionFile"
                    multiple>
                    <img v-if="article.avatar" :src="article.avatar" class="imgAvatar">
                    <i v-else class="el-icon-plus avatar-img-icon"></i>
                  </el-upload>
                </el-col>

              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="6.5">
              <el-form-item label="标签" :label-width="formLabelWidth" prop="tags">
                <el-tag
                  v-for="(item, index) of article.tags"
                  :key="index"
                  style="margin:0 1rem 0 0"
                  :closable="true"
                  @close="removeTag(item)"
                >
                  {{ item }}
                </el-tag>
                <!-- 标签选项 -->
                <el-popover
                  placement="bottom-start"
                  width="460"
                  trigger="click"
                  v-if="article.tags && article.tags.length < 3"
                >
                  <div class="popover-title">标签</div>
                  <!-- 搜索框 -->
                  <el-input style="width:100%" v-model="tagName" placeholder="请输入标签名,enter添加自定义标签"
                    @keyup.enter.native="saveTag" />
                  <!-- 标签 -->
                  <div class="popover-container">
                    <div >添加标签</div>
                    <el-tag
                      v-for="(item, index) of tags"
                      :key="index"
                      style="margin-left: 3px;margin-top: 2px"
                      :class="tagClass(item)"
                      @click="addTag(item)"
                    >
                      {{ item.name }}
                    </el-tag>
                  </div>
                  <el-button type="primary" plain slot="reference" size="small">
                    添加标签
                  </el-button>
                </el-popover>
              </el-form-item>
            </el-col>
            <el-col :span="6.5">
              <el-form-item label="分类" :label-width="formLabelWidth" prop="categoryName">
                  <el-tag
                    type="success"
                    v-show="article.categoryName"
                    style="margin:0 1rem 0 0"
                    :closable="true"
                    @close="removeCategory"

                  >
                    {{ article.categoryName }}
                  </el-tag>
                  <!-- 分类选项 -->
                  <el-popover
                    placement="bottom-start"
                    width="460"
                    trigger="click"
                    v-if="!article.categoryName"
                  >
                    <div class="popover-title">分类</div>
                    <!-- 输入框 -->
                    <el-input style="width:100%" v-model="categoryName" placeholder="请输入分类名,enter添加自定义分类"
                              @keyup.enter.native="saveCategory" />
                    <!-- 分类 -->
                    <div class="popover-container">
                      <div >添加分类</div>
                      <el-tag
                        v-for="(item, index) of category"
                        :key="index"
                        style="margin-left: 3px;margin-top: 2px"
                        class="category-item"
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
            <el-col :span="6.5">
              <el-form-item :label-width="formLabelWidth" label="是否置顶" prop="isStick">
                <el-radio-group v-model="article.isStick" size="small">
                  <el-radio v-for="(item,index) in yesOrNoList" :label="index" border>{{ item }}</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
            <el-col :span="6.5">
              <el-form-item :label-width="formLabelWidth" label="是否发布" prop="isPublish">
                <el-radio-group v-model="article.isPublish" size="small">
                  <el-radio v-for="(item,index) in publishList" :key="index" :label="index" border>{{ item }}</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="5">
              <el-form-item :label-width="formLabelWidth" label="是否私密" prop="isSecret">
                <el-radio-group v-model="article.isSecret" size="small">
                  <el-radio v-for="(item,index) in secretList" :label="index" border>{{ item }}</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item :label-width="formLabelWidth" label="创作类型" prop="isOriginal">
                <el-radio-group v-model="article.isOriginal" size="small">
                  <el-radio v-for="(item,index) in isOriginalList" :label="index" border>{{ item }}</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
            <el-col v-if="article.isOriginal === 0" :span="5">
              <el-form-item :label-width="formLabelWidth" label="原文链接"  prop="originalUrl">
                <el-input v-model="article.originalUrl"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item :label-width="formLabelWidth" label="关键词">
                <el-input v-model="article.keywords"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item :label-width="formLabelWidth" label="备注">
                <el-input v-model="article.remark"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :spam="24">
              <el-form-item :label-width="formLabelWidth" label="内容" prop="contentMd">
                <mavon-editor placeholder="输入文章内容..."  style="height: 500px" ref=md
                              v-model="article.contentMd" @imgDel="imgDel" @change="" @imgAdd="imgAdd"/>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <span slot="footer" class="dialog-footer">
            <el-button @click="centerDialogVisible = false">取 消</el-button>
            <el-button v-if="canAdd || canUpdate" type="primary"  @click="submit">确定</el-button>
        </span>
      </el-dialog>
    </div>


    <el-dialog title="文章抓取" :visible.sync="dialogTableVisible">
      <el-form>
        <el-form-item label="抓取类型" :label-width="formLabelWidth">
          <el-radio-group v-model="reptile.type" size="small">
            <el-radio  :label="0" border>CSDN</el-radio>
            <el-radio disabled  :label="1" border>OSCHINA</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="目标地址url" :label-width="formLabelWidth">
          <el-input v-model="reptile.url" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="tip" :label-width="formLabelWidth" v-if="!isEditForm">
          <span style="color: limegreen">
            请注意如下几点:<br/>
            1.如果抓取成功则会自动保存到文章表中<br/>
            2.CSDN的文章没有封面图片，所以会是随机图片<br/>
            3.暂时只支持CSDN的文章抓取
          </span>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogTableVisible = false">取 消</el-button>
        <el-button :loading="loadingReptile" type="primary" @click="handleReptile">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  fetchArticle,
  remove,
  baiduSeo,
  update,
  top,
  save,
  info,
  reptile,
  deleteBatch,
  pubOrShelf,
  randomImg
} from '@/api/articles'
import {upload, delBatchFile} from '@/api/imgUpload'
import {fetchTags} from '@/api/tags'
import {fetchCategory} from '@/api/category'
import {parseTime} from '@/utils'
import {hasAuth} from '@/utils/auth'
import {mapGetters} from 'vuex'
import {getDataByDictType} from "@/api/dictData";
import axios from 'axios'
export default {
  data() {
    return {
      uploadPictureHost: process.env.VUE_APP_BASE_API + "/file/upload",
      files: {},
      visible: false,
      isEditForm: 0,
      loadingReptile: false,
      centerDialogVisible: false,
      dialogTableVisible: false,
      showSearch: true,
      formLabelWidth: "120px",
      tableData: [],
      dictYesOrNoList: [],
      yesOrNoDefaultValue:null,
      reptile:{
        type:0,
        url:null
      },
      img: process.env.VUE_APP_IMG_API,
      BLOG_WEB_URL: process.env.VUE_APP_BLOG_WEB_API,
      secretList: ["公开", "私密"],
      isOriginalList: ["转载", "原创"],
      yesOrNoList: ["否", "是"],
      yesOrNoStyle: ['danger', 'success'],
      publishList: ['下架', '发布'],
      total: null,
      multipleSelection: [],
      // 加载层信息
      loading: [],
      changeCount: 0,
      isChange: false,
      tags: [],
      category: [],
      title: null,
      categoryName: null,
      fileList: [],
      article: {},
      tagName:null,
      rules:
        {
          'title': [{required: true,message: '必填字段',trigger: 'blur'}],
          'summary': [{required: true,message: '必填字段',trigger: 'blur'}],
          'tags': [{required: true,message: '必填字段',trigger: 'blur'}],
          'categoryName': [{required: true,message: '必填字段',trigger: 'blur' }],
          'isSecret': [{required: true,message: '必填字段',trigger: 'change'}],
          'isStick': [{required: true,message: '必填字段', trigger: 'change'}],
          'isPublish': [{required: true,message: '必填字段',trigger: 'change'}],
          'isOriginal': [{required: true,message: '必填字段',trigger: 'change'}],
          'originalUrl': [{required: true,message: '必填字段',trigger: 'blur'}],
          'contentMd': [{required: true,message: '必填字段',trigger: 'change'}]
        },
      params: {
        title: null,
        tagId: null,
        categoryId: null,
        isPublish: null,
        pageNo: 1,
        pageSize: 10
      }
    }
  },
  created() {
    this.openLoading();
    this.getDictList();
    fetchTags({pageNo: 1, pageSize: 100}).then(res => {
      this.tags = res.data.records
    })
    this.getCategoryList()
    this.queryList();
  },
  computed: {
    ...mapGetters([
      'pres'
    ]),
    canAdd: function () {
      return hasAuth(this.pres, '/system/article/add')
    },
    canDel: function () {
      return hasAuth(this.pres, '/system/article/delete')
    },
    canDelBatch: function () {
      return hasAuth(this.pres, '/system/article/deleteBatch')
    },
    canUpdate: function () {
      return hasAuth(this.pres, '/system/article/update')
    },
    canReptile: function () {
      return hasAuth(this.pres, '/system/article/reptile')
    },
    tagClass() {
      return function(item) {
        const index = this.article.tags.indexOf(item.name);
        return index !== -1 ? "tag-item-select" : "tag-item";
      };
    }
  },
  methods: {
    getCategoryList:function (){
      fetchCategory({pageNo: 1, pageSize: 100}).then(res => {
        this.category = res.data.records
      })
    },
    addCategory:function (item){
      this.article.categoryName = item.name
    },
    saveCategory:function (){
      if (this.categoryName.trim() !== "") {
        this.article.categoryName = this.categoryName
      }
    },
    removeCategory:function (){
      this.article.categoryName = null
    },
    addTag:function (item){
      if (this.article.tags.length > 2 ){
        this.$message.error("最多添加三个标签,如需继续添加,请先删除一个!")
        return false;
      }
      if (this.article.tags.indexOf(item.name) === -1) {
        this.article.tags.push(item.name);
      }
    },
    saveTag:function (){
      if (this.tagName.trim() !== "") {
        this.addTag({
          name: this.tagName
        });
        this.tagName = "";
      }
    },
    removeTag: function (item){
      const index = this.article.tags.indexOf(item);
      this.article.tags.splice(index, 1);
    },
    getDictList: function () {
      let dictTypes = ['sys_publish_status', 'sys_yes_no'];
      getDataByDictType(dictTypes).then(response => {
        let dictMap = response.data;
        this.dictYesOrNoList = dictMap.sys_yes_no.list
        this.yesOrNoDefaultValue = dictMap.sys_yes_no.defaultValue
      }).catch(err => {
        console.error(err)
      })
    },
    queryList: function () {
      fetchArticle(this.params).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total
      }).catch(err => {
        console.log(err)
      })
      this.loading.close();
    },
    submit: function () {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.article.content = this.$refs.md.d_render
          if (this.isEditForm) {
            update(this.article).then(res => {
              this.$message.success("修改文章成功")
              this.closeDialogForm()
            }).catch(err => {
              console.log(err)
            })
          } else {
            save(this.article).then(res => {
              this.$message.success("添加文章成功")
              this.closeDialogForm()
            }).catch(err => {
              console.log(err)
            })
          }
        } else {
          this.$message.error("存在必填字段未填")
        }
      })
    },
    resetQuery: function (){
      this.params.title=null
      this.params.tagId=null
      this.params.isPublish=null
      this.queryList()
    },
    handleFind: function () {
      this.params.pageNo = 1;
      this.queryList()
    },
    release: function (scope) {
      let article = {id:scope.row.id,isPublish:1}
      pubOrShelf(article).then(res => {
        this.$message.success("发布文章成功")
        this.queryList()
      }).catch(err => {
        console.log(err)
      })
    },
    offShelf: function (scope) {
      let article = {id:scope.row.id,isPublish:0}
      pubOrShelf(article).then(res => {
        this.$message.success("下架文章成功")
        this.queryList()
      }).catch(err => {
        console.log(err)
      })
    },
    handleTop: function (scope) {
      let article = {
        id:scope.row.id,
        isStick:scope.row.isStick
      }
      top(article).then(res => {
        if (article.isStick === 1){
          this.$message.success("置顶文章成功")
        } else {
          this.$message.success("取消置顶成功")
        }
        this.queryList()
      }).catch(err => {
        console.log(err)
      })
    },
    seo: function () {
      let ids =[]
      this.multipleSelection.forEach(item =>{
        ids.push(item.id)
      })
      baiduSeo(ids).then(res => {
        this.$message.success("推送成功")
      }).catch(err => {
        console.log(err)
      })
    },
    onClick: function (row) {
      if (row.isPublish === 0) {
        this.$message.error("文章暂未发布，无法进行浏览")
        return false;
      }
      window.open(this.BLOG_WEB_URL + "articles/" + row.id);
    },
    handleSelectionChange: function (val) {
      this.multipleSelection = val;
    },
    remove: function (scope) {
      let id = scope.row.id
      this.$confirm('此操作将把文章删除, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        remove(id).then(res => {
          this.$message.success("删除文章成功")
          this.queryList();
        }).catch(err => {
          console.log(err)
        })
      }).catch(() => {
        this.$message.info("取消删除")
      })

    },
    handleDelete: function () {
      let ids = []
      this.multipleSelection.forEach(item =>{
        ids.push(item.id)
      })
      this.$confirm('此操作将把文章删除, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteBatch(ids).then(res => {
          this.$message.success("批量删除文章成功")
          this.queryList();
        }).catch(err => {
          console.log(err)
        })
      }).catch(() => {
        this.$message.info("取消删除")
      })

    },
    handleAdd: function () {
      this.fileList = []
      let article = sessionStorage.getItem("article");
      if (article != null){
        this.$confirm('还有上次未完成的博客编辑，是否继续编辑?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.initModel(JSON.parse(article))
        }).catch(() => {
          this.initModel(this.getFormObject())
          sessionStorage.removeItem("article")
        })
      }else {
        this.initModel(this.getFormObject())
      }
    },
    initModel:function (article){
      this.article = article
      if (this.article.id != null){
        this.beforeShow('修改文章', 1)
      }else {
        this.beforeShow('新增文章', 0)
      }
    },
    handleUpdate: function (scope) {
      this.fileList = []
      info(scope.row.id).then(res =>{
        this.article = res.data
        this.fileList.push({name: this.article.avatar, url: this.img + this.article.avatar})
        this.beforeShow("修改文章", 1)
      })
    },
    getFormObject: function () {
      return {
        id: null,
        title: '',
        avatar: '',
        summary: '',
        quantity: 0,
        content: '',
        contentMd: '',
        isSecret: 0,
        isStick: 0,
        isOriginal: 1,
        remark: '',
        keywords: '',
        categoryName: null,
        isPublish: 1,
        tags: []
      };
    },
    handleReptile: function (){
      if (this.reptile.url === '' || this.reptile.url == null){
        this.$message.error("请输入抓取地址!!")
        return false;
      }
      this.loadingReptile = true
      reptile(this.reptile.url).then(res =>{
        this.$message.success("OK")
        this.dialogTableVisible = false
        this.loadingReptile = false
        this.queryList()
      }).catch(err =>{
        this.loadingReptile = false
        console.log(err)
      })
    },
    beforeShow: function (title, isEditForm) {
      this.title = title
      this.isEditForm = isEditForm
      this.centerDialogVisible = true
    },
    closeDialogForm: function () {
      this.centerDialogVisible = false
      this.getCategoryList();
      this.queryList();
    },
    handleSizeChange: function (val) {
      this.params.pageSize = val
      this.queryList()
    },
    handleCurrentChange: function (val) {
      this.params.pageNo = val
      this.queryList()
    },
    closeModel:function (done){
      if(this.article.title != null && this.article.title !== ""
        && (this.article.tags.length > 0 || (this.article.categoryName != null && this.article.categoryName !== ""))) {
        this.$confirm("是否关闭博客编辑窗口", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(() => {
            sessionStorage.setItem("article", JSON.stringify(this.article));
            done();
          }).catch(() => {
            this.$message.info("已取消")
          });
      } else {
        done();
      }
    },
    randomImg: function (){
      this.openLoading()
      randomImg().then(res => {
        this.article.avatar = res.data;
        this.visible = false
        this.loading.close()
      }).catch(err =>{
        console.error(err)
      });
      this.loading.close()
    },
    uploadBefore: function (){
      this.openLoading()
    },
    uploadSectionFile: function (param) {
      this.files = param.file
      // FormData 对象
      var formData = new FormData()
      // 文件对象
      formData.append('multipartFile', this.files)
      upload(formData).then(res => {
        this.article.avatar = res.data
      })
      this.loading.close()
    },
    imgAdd: function (pos, $file) {
      var formdata = new FormData();
      formdata.append('multipartFile', $file);
      upload(formdata).then(res => {
        this.$refs.md.$img2Url(pos,res.data);
      }).catch(err => {
        console.log(err)
      })
    },
    imgDel: function (filename) {
      delBatchFile(filename[0].split(this.img)[1])
    },
    dataFormat: function (time) {
      return parseTime(time)
    },
    strSplit: function (item) {
      return item.split(",")
    },
    // 打开加载层
    openLoading: function () {
      this.loading = this.$loading({
        lock: true,
        text: "正在加载中~",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)"
      });
    }
  }
}
</script>

<style>
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  width: 200px;
  height: 110px;
}
.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}
.avatar-img-icon {
  font-size: 28px;
  color: #8c939d;
  line-height: 110px;
  text-align: center;
}
.imgAvatar {
  width: 200px;
  height: 110px;
  display: block;
}
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  margin: 0, 0, 0, 10px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
#imgIcon{
  color: #fff;
  font-size: 1.5rem;
  position: absolute;
  right: 1rem;
  bottom: 1.4rem;
}
.avatar-uploader .el-upload:hover {
  border-color: #409eff;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 195px;
  height: 105px;
  line-height: 105px;
  text-align: center;
}

.imgBody {
  width: 195px;
  height: 105px;
  border: solid 2px #ffffff;
  float: left;
  position: relative;
}

.uploadImgBody {
  margin-left: 5px;
  width: 195px;
  height: 105px;
  border: dashed 1px #c0c0c0;
  float: left;
  position: relative;
}

.uploadImgBody :hover {
  border: dashed 1px #00ccff;
}

.inputClass {
  position: absolute;
}
.article-cover {
  position: relative;
  width: 100%;
  height: 90px;
  border-radius: 4px;
}
.article-cover::after {
  content: "";
  background: rgba(0, 0, 0, 0.3);
  position: absolute;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
}
</style>

