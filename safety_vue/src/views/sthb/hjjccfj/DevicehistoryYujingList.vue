<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="设备名称">
              <j-search-select-tag placeholder="请选择设备名称" v-model="queryParam.devaddr" :dictOptions="dictOption" >
              </j-search-select-tag>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
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
      <a-button @click="handleAdd" type="primary" icon="plus" v-has="'devicecfg:add'">新增</a-button>
      <a-button type="primary" icon="download"  v-has="'devicecfg:dc'" @click="handleExportXls('预警配置表')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" v-has="'devicecfg:dr'" icon="import">导入</a-button>
      </a-upload>
      <!-- 高级查询区域 -->
<!--      <j-super-query :fieldList="superFieldList" ref="superQueryModal" @handleSuperQuery="handleSuperQuery"></j-super-query>-->
<!--      <a-dropdown v-if="selectedRowKeys.length > 0">-->
<!--        <a-menu slot="overlay">-->
<!--          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>-->
<!--        </a-menu>-->
<!--        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>-->
<!--      </a-dropdown>-->
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
        @change="handleTableChange"
        :customRow="handleClick"
        :rowClassName="setRowClassName"
      >

        <span slot="tags" slot-scope="tags, record">
          <a-tag color="red" v-if="record.isCall === 0">报警</a-tag>
          <a-tag color="green" v-if="record.isCall === 1">不报警</a-tag>
        </span>
        <span slot="tags1" slot-scope="tags1, record">
        <a-tag color="geekblue" v-if="record.primaryPhones_dictText">{{record.primaryPhones_dictText}}</a-tag>
       </span>
        <span slot="tags2" slot-scope="tags2, record">
          <a-tag color="orange" v-if="record.middlePhones_dictText">{{record.middlePhones_dictText}}</a-tag>
       </span>
        <span slot="tags3" slot-scope="tags3, record">
          <a-tag color="red" v-if="record.advancedPhones_dictText">{{record.advancedPhones_dictText}}</a-tag>
       </span>
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
          <a v-has="'devicecfg:edit'" @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item>
              <a-menu-item v-has="'devicecfg:del'" >
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <devicehistory-yujing-modal ref="modalForm" @ok="modalFormOk"></devicehistory-yujing-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import DevicehistoryYujingModal from './modules/DevicehistoryYujingModal'
  import { usershebeiList } from '@api/api'
  import Vue from 'vue'

  export default {
    name: 'DevicehistoryYujingList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      DevicehistoryYujingModal
    },
    data () {
      return {
        dictOption:[],
        description: '预警配置表管理页面',
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
            title: '所属部门',
            align: "center",
            dataIndex: 'sysOrgCode_dictText',

          },
          {
            title:'设备名称',
            align:"center",
            dataIndex: 'devaddr_dictText'
          },
          // {
          //   title:'PM10(ug/m³)（最高200）',
          //   align:"center",
          //   dataIndex: 'pm10'
          // },
          // {
          //   title:'PM2.5(ug/m³)（最高500）',
          //   align:"center",
          //   dataIndex: 'pm25'
          // },
          // {
          //   title:'噪声(dB)（最高不高于100）',
          //   align:"center",
          //   dataIndex: 'noise'
          // },
          // {
          //   title:'温度(℃)',
          //   align:"center",
          //   dataIndex: 'temperature'
          // },
          // {
          //   title:'湿度(%RH)（最高100）',
          //   align:"center",
          //   dataIndex: 'humidity'
          // },
          // {
          //   title:'风力（最高18）',
          //   align:"center",
          //   dataIndex: 'wind'
          // },
          // {
          //   title:'风速(m/s) （最高不大于100）',
          //   align:"center",
          //   dataIndex: 'windspeed'
          // },
          // {
          //   title:'风向  （风向度最高360）',
          //   align:"center",
          //   dataIndex: 'winddirection'
          // },
          // {
          //   title:'数据保存的时间点',
          //   align:"center",
          //   dataIndex: 'timevalue',
          //   customRender:function (text) {
          //     return !text?"":(text.length>10?text.substr(0,10):text)
          //   }
          // },
          // {
          //   title:'是否删除（0：未删除，1：已删除）',
          //   align:"center",
          //   dataIndex: 'isdel'
          // },
          {
            title:'是否报警',
            align:"center",
            dataIndex: 'isCall_dictText',
            scopedSlots: {customRender: 'tags'},
          },
          {
            title:'初级预警手机号码',
            align:"center",
            dataIndex: 'primaryPhones_dictText',
            scopedSlots: {customRender: 'tags1'},
          },
          {
            title:'中级预警手机号码',
            align:"center",
            dataIndex: 'middlePhones_dictText',
            scopedSlots: {customRender: 'tags2'},
          },
          {
            title:'高级预警手机号码',
            align:"center",
            dataIndex: 'advancePhones_dictText',
            scopedSlots: {customRender: 'tags3'},
          },
          {
            title: '创建人',
            align: "center",
            dataIndex: 'createBy'
          },
          {
            title: '创建日期',
            align: "center",
            dataIndex: 'createTime',
            customRender: function (text) {
              return !text ? "" : (text.length > 10 ? text.substr(0, 10) : text)
            }
          },
          {
            title: '更新人',
            align: "center",
            dataIndex: 'updateBy'
          },
          {
            title: '更新日期',
            align: "center",
            dataIndex: 'updateTime',
            customRender: function (text) {
              return !text ? "" : (text.length > 10 ? text.substr(0, 10) : text)
            }
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            fixed:"right",
            width:147,
            scopedSlots: { customRender: 'action' }
          }
        ],
        url: {
          list: "/devicehistoryyujing/devicehistoryYujing/list",
          delete: "/devicehistoryyujing/devicehistoryYujing/delete",
          deleteBatch: "/devicehistoryyujing/devicehistoryYujing/deleteBatch",
          exportXlsUrl: "/devicehistoryyujing/devicehistoryYujing/exportXls",
          importExcelUrl: "devicehistoryyujing/devicehistoryYujing/importExcel",

        },
        dictOptions:{},
        superFieldList:[],
      }
    },
    created() {
    this.getSuperFieldList();
    this.shebeiList();
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
          sbtypes:'15'
        }).then(res=>{
          this.dictOption=[];
          let result=res.result;
          result.forEach(re=>{
            this.dictOption.push({text:re.sbname,value:re.sbjno})
          })
          //console.log(res)
        })
      },
      initDictConfig(){
      },
      getSuperFieldList(){
        let fieldList=[];
        fieldList.push({type:'string',value:'devaddr',text:'设备地址',dictCode:''})
        fieldList.push({type:'string',value:'pm10',text:'PM10(ug/m³)（最高200）',dictCode:''})
        fieldList.push({type:'string',value:'pm25',text:'PM2.5(ug/m³)（最高500）',dictCode:''})
        fieldList.push({type:'string',value:'noise',text:'噪声(dB)（最高不高于100）',dictCode:''})
        fieldList.push({type:'string',value:'temperature',text:'温度(℃)',dictCode:''})
        fieldList.push({type:'string',value:'humidity',text:'湿度(%RH)（最高100）',dictCode:''})
        fieldList.push({type:'string',value:'wind',text:'风力（最高18）',dictCode:''})
        fieldList.push({type:'string',value:'windspeed',text:'风速(m/s) （最高不大于100）',dictCode:''})
        fieldList.push({type:'string',value:'winddirection',text:'风向  （风向度最高360）',dictCode:''})
        fieldList.push({type:'date',value:'timevalue',text:'数据保存的时间点'})
        fieldList.push({type:'int',value:'isdel',text:'是否删除（0：未删除，1：已删除）',dictCode:''})
        fieldList.push({type:'int',value:'isCall',text:'是否报警:0=报警,1=不报警',dictCode:''})
        fieldList.push({type:'string',value:'yjPhones',text:'预警手机号码',dictCode:''})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>