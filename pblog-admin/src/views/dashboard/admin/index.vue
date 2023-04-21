<template>
  <div class="dashboard-editor-container">
    <github-corner class="github-corner" />

    <panel-group />

    <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:32px;">
      <div id="container" style=" width: 100%; height: 300px;" />
    </el-row>

    <el-row :gutter="20" style="margin-top:1.25rem">
      <el-col :xs="24" :sm="24" :lg="8">
        <el-card>
          <div class="e-title">文章阅读量排行</div>
          <el-table :data="list" style="width: 100%;padding-top: 15px">
            <el-table-column label="标题" min-width="200">
              <template slot-scope="scope">
                <el-link :underline="false" @click="onClick(scope.row)">{{ scope.row.title }}</el-link>
              </template>
            </el-table-column>
            <el-table-column label="阅读量" prop="quantity" width="100" align="center" />
          </el-table>
        </el-card>
      </el-col>

      <el-col :xs="24" :sm="24" :lg="8">
        <el-card>
          <div class="e-title">文章分类统计</div>
          <div class="chart-wrapper">
            <div id="categoryChart" class="chart" style="height:310px;width:100%" />
          </div>
        </el-card>
      </el-col>

      <!-- 文章标签统计 -->
      <el-col :xs="24" :sm="24" :lg="8">
        <el-card style="height: 417px">
          <div class="e-title">文章标签统计</div>
          <div>
            <tag-cloud style="margin-top:1.5rem" :data="tagDTOList" />
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row style="margin-top:1.25rem">
      <el-card>
        <div class="e-title">一周访问量</div>
        <div id="access" class="chart" style="height:350px;width:100%" />
      </el-card>
    </el-row>

    <el-dialog
      title="通知"
      :close-on-click-modal="false"
      :visible.sync="centerDialogVisible"
      width="50%"
      :before-close="closeNotificationDialogVisible"
      center
    >
      <span v-html="dashboard" />
    </el-dialog>
  </div>
</template>

<script>
import GithubCorner from '@/components/GithubCorner'
import PanelGroup from './components/PanelGroup'
import BoxCard from './components/BoxCard'
import * as echarts from 'echarts'
require('echarts/theme/macarons') // echarts theme
// import resize from './mixins/resize'
import { init } from '@/api/home'
export default {
  name: 'DashboardAdmin',
  components: {
    GithubCorner,
    PanelGroup,
    BoxCard
  },
  data() {
    return {
      tagDTOList: [],
      categoryChart: null,
      chart: null,
      list: [],
      BLOG_WEB_URL: process.env.VUE_APP_BLOG_WEB_API,
      blogContributeCount: [],
      centerDialogVisible: this.$store.state.app.openNotificationDialogVisible,
      dashboard: null
    }
  },
  mounted() {
    this.$nextTick(() => {
      this.init()
    })
  },
  beforeDestroy() {
    if (!this.categoryChart) {
      return
    }
    this.categoryChart.dispose()
    this.categoryChart = null
  },
  methods: {
    init: function() {
      init().then(res => {
        this.list = res.data.blogArticles
        this.dashboard = res.data.dashboard
        this.tagDTOList = res.data.tagsList
        this.initContributeDate(res.data.contribute.contributeDate, res.data.contribute.blogContributeCount)
        this.initCategoryChart(res.data.categoryList.categoryList, res.data.categoryList.result)
        this.initChart(res.data.userAccess)
      }).catch(err => {
        console.log(err)
      })
    },
    // 初始化文章贡献度
    initContributeDate: function(contributeDate, blogContributeCount) {
      const chart = echarts.init(document.getElementById('container'))
      const option = {
        // 设置背景
        // backgroundColor: '#d0d0d0',
        title: {
          top: 30,
          text: '文章贡献度',
          subtext: '一年内博客提交的数量',
          left: 'center',
          textStyle: {
            color: '#000'
          }
        },
        tooltip: {
          trigger: 'item',
          formatter: function(params) {
            return (params.data[0] + '<br>文章数：' + params.data[1])
          }
        },
        legend: {
          top: '30',
          left: '100',
          data: ['文章数', 'Top 12'],
          textStyle: {
            // 设置字体颜色
            color: '#000'
          }
        },
        calendar: [{
          top: 100,
          left: 'center',
          range: contributeDate,
          splitLine: {
            show: true,
            lineStyle: {
              // 设置月份分割线的颜色
              color: '#D3D3D3',
              width: 4,
              type: 'solid'
            }
          },
          yearLabel: { show: false },
          dayLabel: {
            nameMap: ['周日', '周一', '周二', '周三', '周四', '周五', '周六'], // 设置中文显示
            textStyle: {
              // 设置周显示颜色
              color: '#000'
            },
            firstDay: 1 // 从周一开始
          },
          monthLabel: {
            nameMap: 'cn', // 设置中文显示
            textStyle: {
              // 设置月显示颜色
              color: '#000'
            }
          },
          itemStyle: {
            normal: {
              // 设置背景颜色
              color: '#ffffff',
              borderWidth: 1,
              // 设置方块分割线段颜色
              borderColor: '#D3D3D3'
            }
          }
        }],
        series: [
          {
            name: '文章数',
            type: 'scatter',
            coordinateSystem: 'calendar',
            data: blogContributeCount,
            // 根据值设置原点大小
            symbolSize: function(val) {
              if (val[1] === 0) {
                return val[1]
              } else {
                let size = 8 + val[1] * 2
                if (size > 18) {
                  size = 18
                }
                return size
              }
            },
            itemStyle: {
              normal: {
                // 设置圆点颜色
                color: '#2ec7c9'
              }
            }
          }
        ]
      }
      chart.setOption(option)
      window.addEventListener('resize', function() {
        chart.resize()
      })
    },

    // 初始化分类统计
    initCategoryChart: function(categoryList, result) {
      this.categoryChart = echarts.init(document.getElementById('categoryChart'))
      this.categoryChart.setOption({
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b} : {c} ({d}%)'
        },
        legend: {
          left: 'center',
          bottom: '10',
          data: categoryList
        },
        series: [
          {
            name: 'WEEKLY WRITE ARTICLES',
            type: 'pie',
            roseType: 'radius',
            radius: [15, 95],
            center: ['50%', '38%'],
            data: result,
            animationEasing: 'cubicInOut',
            animationDuration: 2600
          }
        ]
      })
      const chart = this.categoryChart
      window.addEventListener('resize', function() {
        chart.resize()
      })
    },

    // 初始化访问线状图
    initChart: function(datas) {
      this.chart = echarts.init(document.getElementById('access'))
      const data = {}
      const createData = []
      const countData = []
      const accessData = []
      datas.forEach(item => {
        createData.push(item.createTime)
        countData.push(item.ip)
        accessData.push(item.access)
      })
      data.createData = createData
      data.countData = countData
      data.accessData = accessData
      this.setOptions(data)
    },

    setOptions({ createData, countData, accessData } = {}) {
      this.chart.setOption({
        xAxis: {
          data: createData,
          boundaryGap: false,
          axisTick: {
            show: false
          }
        },
        grid: {
          left: 10,
          right: 10,
          bottom: 20,
          top: 30,
          containLabel: true
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross'
          },
          padding: [5, 10]
        },
        yAxis: {
          axisTick: {
            show: false
          }
        },
        legend: {
          data: ['独立用户(UV)', '访问量(PV)']
        },
        series: [
          {
            name: '独立用户(UV)',
            smooth: true,
            type: 'line',
            itemStyle: {
              normal: {
                color: '#FF005A',
                lineStyle: {
                  color: '#FF005A',
                  width: 2
                }
              }
            },
            data: countData,
            animationDuration: 2800,
            animationEasing: 'quadraticOut'
          },
          {
            name: '访问量(PV)',
            smooth: true,
            type: 'line',
            itemStyle: {
              normal: {
                color: '#3888fa',
                lineStyle: {
                  color: '#3888fa',
                  width: 2
                },
                areaStyle: {
                  color: '#f3f8ff'
                }
              }
            },
            data: accessData,
            animationDuration: 2800,
            animationEasing: 'quadraticOut'
          }]
      })
      const chart = this.chart
      window.addEventListener('resize', function() {
        chart.resize()
      })
    },
    // 关闭弹出框
    closeNotificationDialogVisible: function(done) {
      this.$store.dispatch('app/setOpenNotification', false)
      done()
    },
    onClick: function(row) {
      window.open(this.BLOG_WEB_URL + 'articles/' + row.id)
    }
  }
}
</script>

<style lang="scss" scoped>
.dashboard-editor-container {
  padding: 32px;
  background-color: rgb(240, 242, 245);
  position: relative;

  .github-corner {
    position: absolute;
    top: 0px;
    border: 0;
    right: 0;
  }

  .chart-wrapper {
    background: #fff;
    padding: 16px 16px 0;
    margin-bottom: 32px;
  }
}

@media (max-width:1024px) {
  .chart-wrapper {
    padding: 8px;
  }
}
</style>
