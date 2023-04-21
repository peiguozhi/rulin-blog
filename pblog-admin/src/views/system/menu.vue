<template>
  <div>
    <div class="app-container">
      <!-- 查询和其他操作 -->
      <el-row>
        <el-button size="small" v-if="canAdd" type="primary" icon="el-icon-plus"
                   @click="handleCreate(0)"
        >添加
        </el-button>
      </el-row>
      <el-table :data="menuData" style="width: 100%">
        <el-table-column type="expand">
          <template slot-scope="scope">
            <el-form label-position="left" inline class="demo-table-expand">
              <el-table :data="scope.row.children" :show-header="showHeader" style="width: 100%">

                <el-table-column label width="60" align="center">
                  <template slot-scope="scope_child">
                    <span>{{ scope_child.$index + 1 }}</span>
                  </template>
                </el-table-column>

                <el-table-column label width="150" align="center">
                  <template slot-scope="scope_child">
                    <span>{{ scope_child.row.title }}</span>
                  </template>
                </el-table-column>

                <el-table-column label width="100" align="center">
                  <template slot-scope="scope_child">
                    <el-tag :type="menuLevelType[scope_child.row.level]">
                      {{ menuLevelOptions[scope_child.row.level] }}
                    </el-tag>
                  </template>
                </el-table-column>

                <el-table-column label width="100" align="center">
                  <template slot-scope="scope_child">
                    <span v-if="scope_child.row.icon != null">
                         <i v-if="scope_child.row.icon.indexOf('el-') >-1" :class="scope_child.row.icon"></i>
                          <svg-icon :icon-class="scope_child.row.icon"/>
                   </span>
                  </template>
                </el-table-column>

                <el-table-column label width="200" align="center">
                  <template slot-scope="scope_child">
                    <span>{{ scope_child.row.url }}</span>
                  </template>
                </el-table-column>

                <el-table-column width="100" align="center">
                  <template slot-scope="scope_child">
                    <el-tag :type="hiddenTypes[scope_child.row.hidden]">
                      {{ hiddenOptions[scope_child.row.hidden] }}
                    </el-tag>
                  </template>
                </el-table-column>

                <el-table-column width="100" align="center">
                  <template slot-scope="scope_child">
                    <el-tag type="warning">{{ scope_child.row.sortNo }}</el-tag>
                  </template>
                </el-table-column>

                <el-table-column align="center" min-width="230">
                  <template slot-scope="scope_child">
                    <el-button v-if="canUpdate" type="primary" size="mini"  @click="handleUpdate(scope_child.row)">
                      编辑
                    </el-button>
                    <el-button v-if="canDel" size="mini"  type="danger" @click="remove(scope_child)">删除</el-button>
                  </template>
                </el-table-column>
              </el-table>
            </el-form>
          </template>
        </el-table-column>

        <el-table-column label="序号" width="60" align="center">
          <template slot-scope="scope">
            <span>{{ scope.$index + 1 }}</span>
          </template>
        </el-table-column>

        <el-table-column label="菜单名称" width="150" align="center">
          <template slot-scope="scope">
            <span>{{ scope.row.title }}</span>
          </template>
        </el-table-column>

        <el-table-column label="菜单级别" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="menuLevelType[scope.row.level]">
              {{ menuLevelOptions[scope.row.level] }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column label="图标" width="100" align="center">
          <template slot-scope="scope">
          <span v-if="scope.row.icon != null">
                         <i v-if="scope.row.icon.indexOf('el-') >-1" :class="scope.row.icon"></i>
                          <svg-icon :icon-class="scope.row.icon"/>
                   </span>
          </template>
        </el-table-column>

        <el-table-column label="路由" width="200" align="center">
          <template slot-scope="scope">
            <span>{{ scope.row.url }}</span>
          </template>
        </el-table-column>

        <el-table-column label="是否显示" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="hiddenTypes[scope.row.hidden]">
              {{ hiddenOptions[scope.row.hidden] }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column label="排序" width="100" align="center">
          <template slot-scope="scope">
            <el-tag type="warning">{{ scope.row.sortNo }}</el-tag>
          </template>
        </el-table-column>

        <el-table-column label="操作" align="center" min-width="270">
          <template slot-scope="scope">
            <el-button v-if="canUpdate" type="primary" size="mini"  @click="handleUpdate(scope.row)">编辑</el-button>
            <el-button type="warning" size="mini" v-if="canAdd&&scope.row.level === 0"
                       @click="handleCreate(scope.row.id)">添加下级
            </el-button>
            <el-button v-if="canDel" size="mini"  type="danger" @click="remove(scope)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>


    <el-dialog center :title="title" :visible.sync="dialogFormVisible">
      <el-form :rules="rules" ref="dataForm" :model="form">
        <el-form-item prop="url" label="路由" :label-width="formLabelWidth">
          <el-input v-model="form.url" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item prop="component" label="路由地址" :label-width="formLabelWidth">
          <el-input v-if="form.parentId" v-model="form.component" autocomplete="off"></el-input>
          <el-input v-else disabled v-model="form.component" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="图标" :label-width="formLabelWidth" prop="icon">
          <el-input v-model="form.icon" placeholder="请输入前图标名称">
            <el-button slot="append" icon="el-icon-setting" @click="openIconsDialog('prefix-icon')">
              选择
            </el-button>
          </el-input>
        </el-form-item>
        <el-form-item prop="title" label="菜单名称" :label-width="formLabelWidth">
          <el-input v-model="form.title" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item prop="title" label="路由名称" :label-width="formLabelWidth">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <!--        <el-form-item prop="type" label="菜单类型" :label-width="formLabelWidth">
                  <el-radio v-model="form.type" label="menu">菜单</el-radio>
                  <el-radio v-model="form.type" label="btn">按钮</el-radio>
                </el-form-item>-->
        <el-form-item prop="hidden" label="是否显示" :label-width="formLabelWidth">
          <el-radio-group v-model="form.hidden" size="small">
            <el-radio v-for="(item,index) in hiddenOptions" :key="index" :label="index" border>{{ item }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="菜单级别" :label-width="formLabelWidth" prop="level">
          <el-select v-model="form.level" placeholder="请选择">
            <el-option
              v-for="(item,index) in menuLevelOptions"
              :key="index"
              :label="item"
              :value="index"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="排序" prop="sortNo" :label-width="formLabelWidth">
          <el-input v-model="form.sortNo" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="备注" :label-width="formLabelWidth">
          <el-input v-model="form.remarks" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="submit">确 定</el-button>
      </div>
    </el-dialog>
    <icons-dialog :visible.sync="iconsVisible" :current="form.icon" @select="setIcon"/>
  </div>
</template>
<script>
import {fetchMenu, createMenu, removeMenu, updateMenu} from '@/api/system'
import IconsDialog from "../../components/IconsDialog";
import {mapGetters} from "vuex";
import {hasAuth} from "@/utils/auth";

export default {
  components: {
    IconsDialog
  },
  data() {
    return {
      showHeader: false, //是否显示表头
      hiddenTypes: ['warning', 'success'],
      hiddenOptions: ['否', '是'],
      menuLevelType: ['success', 'danger', 'warning'],
      menuLevelOptions: ['一级菜单', '二级菜单'],
      isEditForm: 0,
      title: null,
      // 加载层信息
      loading: [],
      form: {},
      dialogFormVisible: false,
      iconsVisible: false,
      formLabelWidth: '120px',
      menuData: [],
      rules: {
        url: [
          {required: true, message: '请输入url', trigger: 'change'}
        ],
        icon: [
          {required: true, message: '请选择图标', trigger: 'change'}
        ],
        level: [
          {required: true, message: '请选择菜单级别', trigger: 'change'}
        ],
        component: [
          {required: true, message: '请输入路由地址', trigger: 'change'}
        ],
        hidden: [
          {required: true, message: '请选择是否显示', trigger: 'change'}
        ],
        title: [
          {required: true, message: '请输入菜单名称', trigger: 'change'},
          {min: 1, max: 6, message: '长度在1到6个字符'},
        ],
        sortNo: [
          {required: true, message: '请输入排序', trigger: 'change'},
          {pattern: /^[0-9]\d*$/, message: '排序字段只能为自然数'},
        ]
      }
    }
  },
  computed: {
    ...mapGetters([
      'pres'
    ]),
    canAdd: function () {
      return hasAuth(this.pres, '/system/menu/create')
    },
    canDel: function () {
      return hasAuth(this.pres, '/system/menu/remove')
    },
    canUpdate: function () {
      return hasAuth(this.pres, '/system/menu/update')
    },
  },
  created() {
    this.openLoading();
    this.fetchMenu()
  },
  methods: {
    fetchMenu: function () {
      fetchMenu(this.query).then(res => {
        this.menuData = res.data
        this.menuData.forEach(item => {
          if (item.children){
            item.children.forEach(children => {
              children.children = [];
            })
          }
        })
        this.loading.close();
      }).catch(err => {
        console.error(err)
      })
    },
    submit: function () {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          if (this.isEditForm) {
            updateMenu(this.form).then(res => {
              this.$message.success("修改菜单成功")
              this.fetchMenu()
              this.close()
            }).catch(err => {
              console.error(err)
            })
          } else {
            createMenu(this.form).then(res => {
              this.$message.success("添加菜单成功")
              this.fetchMenu()
              this.close()
            }).catch(err => {
              console.error(err)
            })
          }
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },
    handleUpdate: function (row) {
      this.form = row
      this.beforeShow(1, '修改菜单')
    },
    handleCreate: function (id) {
      this.form = this.getFormObject(id)
      let title = '添加下级'
      if (!id) {
        this.form.component = 'Layout'
        title = '添加菜单'
      }
      this.beforeShow(0, title)
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    getFormObject: function (id) {
      return {
        parentId: id,
        url: '',
        component: null,
        type: 'menu',
        title: '',
        level: '',
        sortNo: '',
        hidden: '',
        remarks: ''
      }
    },
    beforeShow: function (isEditForm, title) {
      this.isEditForm = isEditForm
      this.title = title
      this.dialogFormVisible = true
    },
    remove: function (scope) {
      if (scope.row.level === 1 && scope.row.children) {
        this.$message.error('该菜单存在子菜单，请先删除子菜单')
        return;
      }
      this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        removeMenu(scope.row.id).then(res => {
          this.fetchMenu()
          this.$notify({
            title: '成功',
            message: res.message,
            type: 'success'
          })
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
    close: function () {
      this.dialogFormVisible = false
      this.form = {}
    },
    // 选择图标
    setIcon: function (val) {
      this.form.icon = val
    },
    openIconsDialog: function (model) {
      this.iconsVisible = true
      this.currentIconModel = model
    }
  },

}
</script>
