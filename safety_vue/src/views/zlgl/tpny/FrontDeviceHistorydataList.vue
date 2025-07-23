<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="设备名称">
              <j-search-select-tag placeholder="请选择设备名称" v-model="queryParam.shebeiNo" :dictOptions="dictOption" >
              </j-search-select-tag>
              {{ selectValue }}
            </a-form-item>
          </a-col>
<!--          <a-col :xl="6" :lg="7" :md="8" :sm="24">-->
<!--            <a-form-item label="设备类型">-->
<!--              <j-dict-select-tag placeholder="请选择" v-model="queryParam.deviceType" dictCode="device_type"></j-dict-select-tag>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :xl="4" :lg="7" :md="8" :sm="24">-->
<!--            <a-form-item label="切面名称">-->
<!--              <a-input placeholder="请输入切面名称" v-model="queryParam.sectionName"></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="时间范围">
              <j-date placeholder="开始时间" v-model="queryParam.datatime_begin" :showTime="true" dateFormat="YYYY-MM-DD HH:mm:ss" />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="">
              <j-date placeholder="结束时间" v-model="queryParam.datatime_end" :showTime="true" dateFormat="YYYY-MM-DD HH:mm:ss" />
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
      <a-button v-has="'tpnyhistory:add'" @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" v-has="'tpnyhistory:dc'" icon="download" @click="handleExportXls('摊铺碾压数据监测主表')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" v-has="'tpnyhistory:dr'" icon="import">导入</a-button>
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
          <a v-has="'tpnyhistory:edit'" @click="handleEdit(record)">编辑</a>

<!--          <a-divider type="vertical" />-->
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
<!--              <a-menu-item>-->
<!--                <a @click="handleDetail(record)">详情</a>-->
<!--              </a-menu-item>-->
              <a-menu-item>
                <a @click="handleDetail1(record)">历史轨迹</a>
              </a-menu-item>
              <a-menu-item v-has="'tpnyhistory:del'" >
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <front-device-realdata-modal ref="modalForm" @ok="modalFormOk"></front-device-realdata-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import FrontDeviceRealdataModal from './modules/FrontDeviceRealdataModal'
  import JSuperQuery from '@/components/jeecg/JSuperQuery.vue'
  import { handertoken } from '@/mixins/getHanderToken'
  import { getAction } from '@api/manage'
  import Vue from 'vue'
  import { SYS_DEPART_ORGCODE } from '@/store/mutation-types'
  import { usershebeiList } from '@api/api'

  export default {
    name: 'FrontDeviceHistorydataList',
    mixins:[JeecgListMixin, mixinDevice,handertoken],
    components: {
      FrontDeviceRealdataModal,
      JSuperQuery,
    },
    data () {
      return {
        selectValue: '',
        asyncSelectValue: '',
        dictOption: [],
        description: '摊铺碾压数据监测主表管理页面',
        // 表头
        columns: [
          {
            title: '序号',
            dataIndex: '',
            key:'rowIndex',
            width:60,
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
          },
          {
            title:'设备类型',
            align:"center",
            dataIndex: 'deviceType_dictText'
          },
          {
            title:'设备名称',
            align:"center",
            dataIndex: 'shebeiNo_dictText'
          },
          {
            title:'项目',
            align:"center",
            dataIndex: 'projectId'
          },
          {
            title:'速度',
            align:"center",
            dataIndex: 'speed'
          },
          {
            title:'温度',
            align:"center",
            dataIndex: 'temperature'
          },
          {
            title:'Gps 经度',
            align:"center",
            dataIndex: 'longitude'
          },
          {
            title:'Gps 纬度',
            align:"center",
            dataIndex: 'latitude'
          },
          {
            title:'数据上传时间',
            align:"center",
            dataIndex: 'datatime',
          },
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
          list: "/clgl/frontDeviceHistorydata/list1",
          delete: "/tpny/frontDeviceRealdata/delete",
          deleteBatch: "/tpny/frontDeviceRealdata/deleteBatch",
          exportXlsUrl: "/tpny/frontDeviceRealdata/exportXls",
          importExcelUrl: "tpny/frontDeviceRealdata/importExcel",

        },
        dictOptions:{},
        superFieldList:[],
      }
    },
    created() {
      this.getToken();
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
          sbtypes:'24,25'
        }).then(res=>{
          // this.dictOption=[];
          let result=res.result;
          result.forEach(re=>{
            this.dictOption.push({text:re.sbname,value:re.sbjno})
            // console.log('+++++++++++'+re.sbname)
          })
          //console.log(res)
        });
      },
      handleDetail1: function (record) {
        this.$refs.modalForm.detail(record);
        this.$refs.modalForm.title = "轨迹回放";
        this.$refs.modalForm.disableSubmit = false;
      },
      initDictConfig(){
      },
      getSuperFieldList(){
        let fieldList=[];
        fieldList.push({type:'string',value:'deviceType',text:'设备类型A:压路机、B:摊铺机',dictCode:''})
        fieldList.push({type:'string',value:'shebeiNo',text:'设备编号',dictCode:''})
        fieldList.push({type:'string',value:'projectId',text:'项目id',dictCode:''})
        fieldList.push({type:'double',value:'speed',text:'速度A:km/h B:m/min',dictCode:''})
        fieldList.push({type:'double',value:'temperature',text:'温度',dictCode:''})
        fieldList.push({type:'double',value:'longitude',text:'经度',dictCode:''})
        fieldList.push({type:'double',value:'latitude',text:'纬度',dictCode:''})
        fieldList.push({type:'date',value:'datatime',text:'数据上传时间'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>