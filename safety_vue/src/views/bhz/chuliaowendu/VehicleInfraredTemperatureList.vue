<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="料温设备">
              <j-search-select-tag placeholder="请选择料温设备名称" v-model="queryParam.deviceId" :dictOptions="dictOption">
              </j-search-select-tag>
            </a-form-item>
          </a-col>

          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="时间范围">
              <j-date placeholder="开始时间" v-model="queryParam.collectionTime_begin" :showTime="true"
                      dateFormat="YYYY-MM-DD HH:mm:ss"/>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="">
              <j-date placeholder="结束时间" v-model="queryParam.collectionTime_end" :showTime="true"
                      dateFormat="YYYY-MM-DD HH:mm:ss"/>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <a-button type="primary" icon="download" @click="handleExportXls('vehicle_infrared_temperature')" style="margin-left: 8px" v-if="toggleSearchStatus">导出</a-button>
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

    <div id="echartLineW"></div>
    <!-- 操作按钮区域 -->
    <div class="table-operator">
<!--      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>-->
<!--      <a-button type="primary" icon="download" @click="handleExportXls('vehicle_infrared_temperature')">导出</a-button>-->
<!--      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">-->
<!--        <a-button type="primary" icon="import">导入</a-button>-->
<!--      </a-upload>-->
      <!-- 高级查询区域 -->
<!--      <j-super-query :fieldList="superFieldList" ref="superQueryModal" @handleSuperQuery="handleSuperQuery"></j-super-query>-->
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
      </a-dropdown>
    </div>

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

        <template slot="tags" slot-scope="tags, record">
          <span>{{(Number(record.t)/10).toFixed(2) }}</span>
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

    <vehicle-infrared-temperature-modal ref="modalForm" @ok="modalFormOk"></vehicle-infrared-temperature-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import VehicleInfraredTemperatureModal from './modules/VehicleInfraredTemperatureModal'
  import Vue from "vue";
  import {usershebeiList} from "@api/api";
  import * as echarts from "echarts";

  export default {
    name: 'VehicleInfraredTemperatureList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      VehicleInfraredTemperatureModal
    },
    data () {
      return {
        dictOption:[],
        description: 'vehicle_infrared_temperature管理页面',
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
            title:'设备名称',
            align:"center",
            dataIndex: 'deviceId_dictText'
          },
          {
            title:'温度',
            align:"center",
            dataIndex: 't',
            scopedSlots: {customRender: 'tags'},
          },
          {
            title:'采集时间',
            align:"center",
            dataIndex: 'collectionTime'
          },
          // {
          //   title:'网络传输类型：WIFI、4G',
          //   align:"center",
          //   dataIndex: 'type'
          // },
          // {
          //   title: '操作',
          //   dataIndex: 'action',
          //   align:"center",
          //   fixed:"right",
          //   width:147,
          //   scopedSlots: { customRender: 'action' }
          // }
        ],
        url: {
          list: "/vehicleinfraredtemperature/vehicleInfraredTemperature/list",
          delete: "/vehicleinfraredtemperature/vehicleInfraredTemperature/delete",
          deleteBatch: "/vehicleinfraredtemperature/vehicleInfraredTemperature/deleteBatch",
          exportXlsUrl: "/vehicleinfraredtemperature/vehicleInfraredTemperature/exportXls",
          importExcelUrl: "vehicleinfraredtemperature/vehicleInfraredTemperature/importExcel",
          
        },
        dictOptions:{},
        superFieldList:[],
      }
    },
    created() {
      this.shebeiList();
    this.getSuperFieldList();
    },
    watch: {
      dataSource:{
        handler(){
          this.getEcarts(this.dataSource);
          console.log(this.dataSource,'dataSource');
        }
      }
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      },
    },
    methods: {
      shebeiList:function (){
        var sys_depart_orgcode=Vue.ls.get('SYS_DEPART_ORGCODE');
        usershebeiList({
          sys_depart_orgcode:sys_depart_orgcode,
          sbtypes:'68'
        }).then(res=>{
          //console.log(res)
          this.dictOption=[];
          let result=res.result;
          result.forEach(re=>{
            this.dictOption.push({text:re.sbname,value:re.sbjno})
          })

        })
      },
      initDictConfig(){
      },
      getSuperFieldList(){
        let fieldList=[];
        fieldList.push({type:'string',value:'deviceId',text:'设备编号'})
        fieldList.push({type:'string',value:'collectionTime',text:'采集时间'})
        fieldList.push({type:'number',value:'t',text:'温度'})
        fieldList.push({type:'string',value:'type',text:'网络传输类型：WIFI、4G'})
        this.superFieldList = fieldList
      },

      getEcarts(eData) {
        let dataList = eData.map((e) => {
          return (Number(e.t)/10).toFixed(2);
        });
        let xData = eData.map((e) => {
          var str = e.collectionTime
          return str.substr(str.length - 8);
        });
         // 选择设备时才出现曲线
        if(this.queryParam.deviceId == "" || this.queryParam.deviceId == undefined){
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
              color: "#fff",
            },
          },

          tooltip: {
            trigger: 'axis',
            axisPointer: { // 坐标轴指示器，坐标轴触发有效
              type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
            },
            formatter:function(params){
              // console.log(params,'params-------------');
              return eData[params[0].dataIndex].deviceId_dictText+
                '<br/>'+
                `<div  style='width:10px;height:10px;background:#fce630;display:inline-block;border-radius:10px;'></div> `+'温度(℃)'+': '+params[0].value
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
              name: "温度(℃)",
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

          series: [
            {
              name: "温度(℃)",
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