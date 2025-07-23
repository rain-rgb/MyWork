<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
<!--          <a-col :xl="6" :lg="7" :md="8" :sm="24">-->
<!--            <a-form-item label="拌合站设备">-->
<!--              <j-search-select-tag placeholder="请选择拌合站设备名称" v-model="queryParam.bhsbjno" :dictOptions="dictOption">-->
<!--              </j-search-select-tag>-->
<!--              {{ selectValue }}-->
<!--            </a-form-item>-->
<!--          </a-col>-->

          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="含水率设备">
              <j-search-select-tag placeholder="请选择含水率设备" v-model="queryParam.shebeiid" :dictOptions="dictOption1">
              </j-search-select-tag>
              {{ selectValue }}
            </a-form-item>
          </a-col>

          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="时间范围">
              <j-date placeholder="开始时间" v-model="queryParam.uploadTime_begin" :showTime="true"
                      dateFormat="YYYY-MM-DD HH:mm:ss"/>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="">
              <j-date placeholder="结束时间" v-model="queryParam.uploadTime_end" :showTime="true"
                      dateFormat="YYYY-MM-DD HH:mm:ss"/>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <a-button type="primary" icon="download" @click="handleExportXls('bhz_cement_waterrate')" style="margin-left: 8px" v-if="toggleSearchStatus">导出</a-button>
              <j-super-query :fieldList="superFieldList" ref="superQueryModal" @handleSuperQuery="handleSuperQuery" style="margin-left: 8px" v-if="toggleSearchStatus"></j-super-query>
              <a @click="handleToggleSearch" style="margin-left: 8px">
                {{ toggleSearchStatus ? '收起' : '展开' }}
                <a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>
              </a>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
<!--      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>-->
<!--      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">-->
<!--        <a-button type="primary" icon="import">导入</a-button>-->
<!--      </a-upload>-->
      <!-- 高级查询区域 -->
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
      </a-dropdown>
    </div>

    <!-- echart- -->
    <div id="echartLineW"></div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
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
          <img v-else :src="getImgView(text)" height="25px" alt="" style="max-width:80px;font-size: 12px;font-style: italic;"/>
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

        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item>
              <a-menu-item>
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <bhz-cement-waterrate-modal ref="modalForm" @ok="modalFormOk"></bhz-cement-waterrate-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import BhzCementWaterrateModal from './modules/BhzCementWaterrateModal'
  import Vue from "vue";
  import {usershebeiList} from "@api/api";
  import * as echarts from "echarts";

  export default {
    name: 'BhzCementWaterrateList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      BhzCementWaterrateModal
    },
    data () {
      return {
        dictOption1:[],
        dictOption:[],
        description: 'bhz_cement_waterrate管理页面',
        // 表头
        columns: [
          {
            title: '#',
            dataIndex: '',
            key:'rowIndex',
            width:60,
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
          },
          {
            title:'含水率设备',
            align:"center",
            dataIndex: 'shebeiid_dictText'
          },
          {
            title:'温度(°C)',
            align:"center",
            dataIndex: 'temperature'
          },
          // {
          //   title:'含水率(%)',
          //   align:"center",
          //   dataIndex: 'water'
          // },
          // {
          //   title:'湿度(%)',
          //   align:"center",
          //   dataIndex: 'humidity'
          // },
          {
            title:'材料名称',
            align:"center",
            dataIndex: 'cailiaoname'
          },
          {
            title:'拌合站设备',
            align:"center",
            dataIndex: 'bhsbjno_dictText'
          },
          // {
          //   title:'料仓id',
          //   align:"center",
          //   dataIndex: 'liaocangid'
          // },
          {
            title:'料仓名称',
            align:"center",
            dataIndex: 'lcname'
          },
          {
            title:'平均含水率(%)',
            align:"center",
            dataIndex: 'averate'
          },
          // {
          //   title:'平均湿度(%)',
          //   align:"center",
          //   dataIndex: 'avehum'
          // },
          {
            title:'上传时间',
            align:"center",
            dataIndex: 'uploadTime',
            customRender:function (text) {
              return !text?"":(text.length>10?text:text)
            }
          }
        ],
        url: {
          list: "/bhzcementwaterrate/bhzCementWaterrate/list",
          delete: "/bhzcementwaterrate/bhzCementWaterrate/delete",
          deleteBatch: "/bhzcementwaterrate/bhzCementWaterrate/deleteBatch",
          exportXlsUrl: "/bhzcementwaterrate/bhzCementWaterrate/exportXls",
          importExcelUrl: "bhzcementwaterrate/bhzCementWaterrate/importExcel",
          
        },
        dictOptions:{},
        superFieldList:[],
      }
    },
    created() {
      this.shebeiList();
      this.shebeiList1();
      this.getSuperFieldList();
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      },
    },
    watch: {
      dataSource:{
        handler(){
          this.getEcarts(this.dataSource);
          console.log(this.dataSource,'dataSource');
        }
      }
    },
    methods: {
      handleTableChange1(){
        console.log(11111111222222222);
      },
      shebeiList: function () {
        var sys_depart_orgcode = Vue.ls.get('SYS_DEPART_ORGCODE')
        usershebeiList({
          sys_depart_orgcode: sys_depart_orgcode,
          sbtypes: '0'
        }).then(res => {
          this.dictOption = []
          let result = res.result
          result.forEach(re => {
            this.dictOption.push({ text: re.sbname, value: re.sbjno })
          })
          //console.log(res)
        })
      },
      shebeiList1: function () {
        var sys_depart_orgcode = Vue.ls.get('SYS_DEPART_ORGCODE')
        usershebeiList({
          sys_depart_orgcode: sys_depart_orgcode,
          sbtypes: '66'
        }).then(res => {
          this.dictOption1 = []
          let result = res.result
          result.forEach(re => {
            this.dictOption1.push({ text: re.sbname, value: re.sbjno })
          })
          //console.log(res)
        })
      },

      initDictConfig(){
      },
      getSuperFieldList(){
        let fieldList=[];
        fieldList.push({type:'date',value:'uploadTime',text:'uploadTime'})
        fieldList.push({type:'string',value:'temperature',text:'温度'})
        fieldList.push({type:'string',value:'water',text:'含水率'})
        fieldList.push({type:'string',value:'humidity',text:'湿度'})
        fieldList.push({type:'string',value:'shebeiid',text:'设备id'})
        fieldList.push({type:'string',value:'cailiaoname',text:'材料名称'})
        fieldList.push({type:'string',value:'bhsbjno',text:'拌合站设备'})
        fieldList.push({type:'string',value:'liaocangid',text:'liaocangid'})
        this.superFieldList = fieldList
      },
      getEcarts(eData) {
        let dataList = eData.map((e) => {
          return e.averate;
        });
        let xData = eData.map((e) => {
          return e.uploadTime;
        });
        // var xData = (function () {
        //   var data = [];
        //   for (var i = 1; i < 13; i++) {
        //     data.push(i + "月份");
        //   }
        //   return data;
        // })();
        // let title = `平均值采集完成`;
        // 选择设备时才出现曲线
        if(this.queryParam.shebeiid == "" || this.queryParam.shebeiid == undefined){
          dataList = []
        }
        let option = {
          title: {
            // text: title,
            x: "4%",
            y: "8%",
            textStyle: {
              color: "#3c40d0",
              fontSize: "22",
            },
            subtextStyle: {
              color: "#90979c",
              fontSize: "16",
            },
          },
          tooltip: {
            trigger: "axis",
            axisPointer: {
              type: "shadow",
              textStyle: {
                color: "#fff",
              },
            },
          },
          grid: {
            borderWidth: 0,
            top: 110,
            bottom: 95,
            left: '5%',
            right: '1%',
            textStyle: {
              color: "#fff",
            },
          },
          // legend: {
          //   x: "4%",
          //   top: "8%",
          //   textStyle: {
          //     color: "#90979c",
          //   },
          //   // data: ["女", "男", "平均"],
          // },
          tooltip: {
            trigger: 'axis',
            axisPointer: { // 坐标轴指示器，坐标轴触发有效
              type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
            },
            formatter:function(params){
              // console.log(params,'params-------------');
						  return eData[params[0].dataIndex].shebeiid_dictText+
              '<br/>'+
              `<div  style='width:10px;height:10px;background:#fce630;display:inline-block;border-radius:10px;'></div> `+'平均含水率(%)'+': '+params[0].value
            }
          },
          calculable: true,
          xAxis: [
            {
              type: "category",
              name: "时间",
              nameLocation: "middle",
              nameTextStyle: {
                padding: 15,
              },
              axisLine: {
                lineStyle: {
                  color: "#90979c",
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
              type: "value",
              name: "平均含水率(%)",
              nameLocation: "middle",
              nameTextStyle: {
                padding: 45,
              },
              splitLine: {
                show: false,
              },
              axisLine: {
                lineStyle: {
                  color: "#90979c",
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
          // dataZoom: [
          //   {
          //     show: true,
          //     height: 30,
          //     xAxisIndex: [0],
          //     bottom: 20,
          //     start: 10,
          //     end: 40,
          //     handleIcon:
          //       "path://M306.1,413c0,2.2-1.8,4-4,4h-59.8c-2.2,0-4-1.8-4-4V200.8c0-2.2,1.8-4,4-4h59.8c2.2,0,4,1.8,4,4V413z",
          //     handleSize: "110%",
          //     handleStyle: {
          //       color: "#d3dee5",
          //     },
          //     textStyle: {
          //       color: "#fff",
          //     },
          //     borderColor: "#90979c",
          //   },
          //   {
          //     type: "inside",
          //     show: true,
          //     height: 15,
          //     start: 1,
          //     end: 35,
          //   },
          // ],
          series: [
            {
              name: "平均含水率(%)",
              type: "line",
              symbolSize: 10,
              symbol: "circle",
              itemStyle: {
                normal: {
                  color: "rgba(252,230,48,1)",
                  barBorderRadius: 0,
                  label: {
                    show: true,
                    position: "top",
                    formatter: function (p) {
                      return p.value > 0 ? p.value : "";
                    },
                  },
                },
              },
              data: dataList,
            },
          ],
        };
        this.charts = echarts.init(document.getElementById("echartLineW"));
        // this.charts = this.$refs.chart
        this.charts.setOption(option);
      },
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
  #echartLineW {
    /* width: 1000px; */
    height: 350px;
  }
  .chartTooltip{
    background:#78EC4E;
    display:inline-block;
    /* // margin-right:5px; */
    border-radius:10px;
    width:10px;
    height:10px;


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