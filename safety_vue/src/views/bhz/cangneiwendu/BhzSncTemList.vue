<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="设备名称">
              <j-search-select-tag placeholder="请选择设备名称" v-model="queryParam.deviceno" :dictOptions="dictOption">
              </j-search-select-tag>
              {{ selectValue }}
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="时间范围">
              <j-date placeholder="开始时间" v-model="queryParam.recordtime_begin" :showTime="true"
                      dateFormat="YYYY-MM-DD HH:mm:ss"/>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="">
              <j-date placeholder="结束时间" v-model="queryParam.recordtime_end" :showTime="true"
                      dateFormat="YYYY-MM-DD HH:mm:ss"/>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <div id="echartLineW"></div>
    <!-- 操作按钮区域 -->
    <!--    <div class="table-operator">-->
    <!--      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>-->
    <!--      <a-button type="primary" icon="download" @click="handleExportXls('bhz_snc_tem')">导出</a-button>-->
    <!--      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl"-->
    <!--                @change="handleImportExcel">-->
    <!--        <a-button type="primary" icon="import">导入</a-button>-->
    <!--      </a-upload>-->
    <!--    </div>-->

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a
        style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table
        ref="table"
        size="middle"
        :scroll="{x:true}"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        class="j-table-force-nowrap"
        @change="handleTableChange">

        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <template slot="imgSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无图片</span>
          <img v-else :src="getImgView(text)" height="25px" alt=""
               style="max-width:80px;font-size: 12px;font-style: italic;"/>
        </template>
        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无文件</span>
          <a-button
            v-else
            :ghost="true"
            type="primary"
            icon="download"
            size="small"
            @click="downloadFile(text)">
            下载
          </a-button>
        </template>

        <!--        <span slot="action" slot-scope="text, record">-->
        <!--          <a @click="handleEdit(record)">编辑</a>-->

        <!--          <a-divider type="vertical"/>-->
        <!--          <a-dropdown>-->
        <!--            <a class="ant-dropdown-link">更多 <a-icon type="down"/></a>-->
        <!--            <a-menu slot="overlay">-->
        <!--              <a-menu-item>-->
        <!--                <a @click="handleDetail(record)">详情</a>-->
        <!--              </a-menu-item>-->
        <!--              <a-menu-item>-->
        <!--                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">-->
        <!--                  <a>删除</a>-->
        <!--                </a-popconfirm>-->
        <!--              </a-menu-item>-->
        <!--            </a-menu>-->
        <!--          </a-dropdown>-->
        <!--        </span>-->

      </a-table>
    </div>

    <bhz-snc-tem-modal ref="modalForm" @ok="modalFormOk"></bhz-snc-tem-modal>
  </a-card>
</template>

<script>

import '@/assets/less/TableExpand.less'
import { mixinDevice } from '@/utils/mixin'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import BhzSncTemModal from './modules/BhzSncTemModal'
import { usershebeiList } from '@api/api'
import * as echarts from 'echarts'
import Vue from "vue";

export default {
  name: 'BhzSncTemList',
  mixins: [JeecgListMixin, mixinDevice],
  components: {
    BhzSncTemModal
  },
  data() {
    return {
      description: 'bhz_snc_tem管理页面',
      dictOption: [],
      selectValue: '',
      // 表头
      columns: [
        {
          title: '#',
          dataIndex: '',
          key: 'rowIndex',
          width: 60,
          align: 'center',
          customRender: function (t, r, index) {
            return parseInt(index) + 1
          }
        },
        // {
        //   title:'节点',
        //   align:"center",
        //   dataIndex: 'nodeid'
        // },
        {
          title: '设备名称',
          align: 'center',
          dataIndex: 'deviceno_dictText'
        },
        {
          title: '温度',
          align: 'center',
          dataIndex: 'tem'
        },
        // {
        //   title: '湿度',
        //   align: 'center',
        //   dataIndex: 'hum'
        // },
        {
          title: '记录时间',
          align: 'center',
          dataIndex: 'recordtime',
          customRender: function (text) {
            return !text ? '' : (text.length > 10 ? text : text)
          }
        },
        // {
        //   title:'coordinatetype',
        //   align:"center",
        //   dataIndex: 'coordinatetype'
        // },
        // {
        //   title:'经度',
        //   align:"center",
        //   dataIndex: 'lng'
        // },
        // {
        //   title:'纬度',
        //   align:"center",
        //   dataIndex: 'lat'
        // },
        // {
        //   title:'temh',
        //   align:"center",
        //   dataIndex: 'temh'
        // },
        // {
        //   title:'teml',
        //   align:"center",
        //   dataIndex: 'teml'
        // },
        // {
        //   title:'humh',
        //   align:"center",
        //   dataIndex: 'humh'
        // },
        // {
        //   title:'huml',
        //   align:"center",
        //   dataIndex: 'huml'
        // },
        // {
        //   title:'remar',
        //   align:"center",
        //   dataIndex: 'remar'
        // },
        // {
        //   title: '操作',
        //   dataIndex: 'action',
        //   align: 'center',
        //   fixed: 'right',
        //   width: 147,
        //   scopedSlots: { customRender: 'action' }
        // }
      ],
      url: {
        list: '/bhzsnctem/bhzSncTem/list',
        delete: '/bhzsnctem/bhzSncTem/delete',
        deleteBatch: '/bhzsnctem/bhzSncTem/deleteBatch',
        exportXlsUrl: '/bhzsnctem/bhzSncTem/exportXls',
        importExcelUrl: 'bhzsnctem/bhzSncTem/importExcel',

      },
      dictOptions: {},
      superFieldList: [],
    }
  },
  created() {
    this.shebeiList()
    this.getSuperFieldList()
  },
  watch: {
    dataSource: {
      handler() {
        this.getEcarts(this.dataSource)
      }
    }
  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`
    },
  },
  methods: {
    shebeiList: function () {
      var sys_depart_orgcode = Vue.ls.get('SYS_DEPART_ORGCODE')
      usershebeiList({
        sys_depart_orgcode: sys_depart_orgcode,
        sbtypes: '69'
      }).then(res => {
        this.dictOption = []
        let result = res.result
        result.forEach(re => {
          this.dictOption.push({ text: re.sbname, value: re.sbjno })
        })
      })
    },
    getEcarts(eData) {


      let dataList = eData.map((e) => {
        return (Number(e.tem) ).toFixed(2)
      })
      let xData = eData.map((e) => {
        var str = e.recordtime
        return str.substr(str.length - 8)
      })
      // 选择设备时才出现曲线
      if(this.queryParam.deviceno == "" || this.queryParam.deviceno == undefined){
        dataList = []
      }

      let option = {
        title: {
          // text: title,
          x: '4%',
          y: '8%',
          textStyle: {
            color: '#3c40d0',
            fontSize: '22',
          },
          subtextStyle: {
            color: '#90979c',
            fontSize: '16',
          },
        },
        // tooltip: {
        //   trigger: "axis",
        //   axisPointer: {
        //     type: "shadow",
        //     textStyle: {
        //       color: "#fff",
        //     },
        //   },
        // },
        grid: {
          borderWidth: 0,
          top: 110,
          bottom: 95,
          left: '5%',
          right: '1%',
          textStyle: {
            color: '#fff',
          },
        },

        tooltip: {
          trigger: 'axis',
          axisPointer: { // 坐标轴指示器，坐标轴触发有效
            type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
          },
          formatter: function (params) {
            // console.log(params,'params-------------');
            return eData[params[0].dataIndex].deviceno_dictText +
              '<br/>' +
              `<div  style='width:10px;height:10px;background:#fce630;display:inline-block;border-radius:10px;'></div> ` + '温度(℃)' + ': ' + params[0].value
          }
        },
        calculable: true,
        xAxis: [
          {
            type: 'category',
            name: '时间',
            nameLocation: 'middle',
            nameTextStyle: {
              padding: 15,
            },
            axisLine: {
              lineStyle: {
                color: '#90979c',
              },
            },
            splitLine: {
              show: false,
            },
            axisTick: {
              show: false,
            },
            splitArea: {
              show: false,
            },
            axisLabel: {
              // interval: 0,
            },
            data: xData,
          },
        ],
        yAxis: [
          {
            type: 'value',
            name: '温度(℃)',
            nameLocation: 'middle',
            nameTextStyle: {
              padding: 45,
            },
            splitLine: {
              show: false,
            },
            axisLine: {
              lineStyle: {
                color: '#90979c',
              },
            },
            axisTick: {
              show: false,
            },
            axisLabel: {
              interval: 0,
            },
            splitArea: {
              show: false,
            },
          },
        ],

        series: [
          {
            name: '温度(℃)',
            type: 'line',
            symbolSize: 10,
            symbol: 'circle',
            itemStyle: {
              normal: {
                color: 'rgba(252,230,48,1)',
                barBorderRadius: 0,
                label: {
                  show: true,
                  position: 'top',
                  formatter: function (p) {
                    return p.value > 0 ? p.value : ''
                  },
                },
              },
            },
            data: dataList,
          },
        ],
      }
      this.charts = echarts.init(document.getElementById('echartLineW'))
      // this.charts = this.$refs.chart
      this.charts.setOption(option)


    },
    initDictConfig() {
    },
    getSuperFieldList() {
      let fieldList = []
      fieldList.push({ type: 'int', value: 'nodeid', text: '节点' })
      fieldList.push({ type: 'string', value: 'tem', text: '温度' })
      fieldList.push({ type: 'string', value: 'hum', text: '湿度' })
      fieldList.push({ type: 'date', value: 'recordtime', text: '记录时间' })
      fieldList.push({ type: 'string', value: 'coordinatetype', text: 'coordinatetype' })
      fieldList.push({ type: 'string', value: 'lng', text: '经度' })
      fieldList.push({ type: 'string', value: 'lat', text: '纬度' })
      fieldList.push({ type: 'string', value: 'temh', text: 'temh' })
      fieldList.push({ type: 'string', value: 'teml', text: 'teml' })
      fieldList.push({ type: 'string', value: 'humh', text: 'humh' })
      fieldList.push({ type: 'string', value: 'huml', text: 'huml' })
      fieldList.push({ type: 'string', value: 'deviceno', text: '设备编号' })
      fieldList.push({ type: 'string', value: 'remar', text: 'remar' })
      this.superFieldList = fieldList
    }
  }
}
</script>
<style scoped>
@import '~@assets/less/common.less';

#echartLineW {
  /* width: 1000px; */
  height: 350px;
}

.chartTooltip {
  background: #78EC4E;
  display: inline-block;
  /* // margin-right:5px; */
  border-radius: 10px;
  width: 10px;
  height: 10px;


}

.chartTooltip::before {
  content: "";
  display: inline-block;
  position: absolute;
  top: 5px;
  left: -11px;
  width: 6px;
  margin-right: 10px;
  height: 6px;
  border-radius: 50%;
  background-color: #778cff;
}
</style>