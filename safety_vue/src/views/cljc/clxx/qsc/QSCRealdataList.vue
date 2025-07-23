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
      <a-button @click="handleAdd" type="primary" v-has="'ssc:add'" icon="plus">新增</a-button>
      <a-button type="primary" v-has="'ssc:dc'" icon="download" @click="handleExportXls('车辆信息实时数据表')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" v-has="'ssc:dr'" icon="import">导入</a-button>
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
        <span slot="status" slot-scope="status1, record">
          <a-tag color="cyan" v-if="record.status==0">初始状态</a-tag>
          <a-tag color="red" v-if="record.status==1">运输中</a-tag>
          <a-tag color="green" v-if="record.status==2">已完成</a-tag>
          <a-tag color="blue" v-if="record.status==3">空闲</a-tag>
          <a-tag color="lightpink" v-if="record.status==4">接料中</a-tag>
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
          <a  v-has="'ssc:edit'" @click="handleEdit(record)">编辑</a>

<!--          <a-divider type="vertical" />-->
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
<!--              <a-menu-item>-->
<!--                <a @click="handleDetail(record)">详情</a>-->
<!--              </a-menu-item>-->
              <a-menu-item>
                <a @click="handleDetail1(record)">车辆轨迹</a>
              </a-menu-item>
              <a-menu-item v-has="'ssc:del'" >
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <clxx-realdata-modal ref="modalForm" @ok="modalFormOk"></clxx-realdata-modal>
  </a-card>
</template>

<script>

  import '@assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import JSuperQuery from '@comp/jeecg/JSuperQuery.vue'
  import ClxxRealdataModal from '../modules/ClxxRealdataModal'
  import { SYS_DEPART_ORGCODE } from '@/store/mutation-types'
  import { usershebeiList } from '@api/api'
  import Vue from 'vue'
  import { handertoken } from '@/mixins/getHanderToken'

  export default {
    name: 'QSCRealdataList',
    mixins:[JeecgListMixin, mixinDevice,handertoken],
    components: {
      ClxxRealdataModal,
      JSuperQuery,
    },
    data () {
      return {
        selectValue: '',
        asyncSelectValue: '',
        dictOption: [],
        description: '车辆信息实时数据表管理页面',
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
          // {
          //   title:'设备类型',
          //   align:"center",
          //   dataIndex: 'deviceType'
          // },
          {
            title:'标段',
            align:"center",
            dataIndex: 'projectId'
          },
          {
            title:'车号',
            align:"center",
            dataIndex: 'shebeiNo_dictText'
          },
          {
            title:'速度',
            align:"center",
            dataIndex: 'speed'
          },
          // {
          //   title:'温度',
          //   align:"center",
          //   dataIndex: 'temperature'
          // },
          {
            title:'经度',
            align:"center",
            dataIndex: 'longitude'
          },
          {
            title:'纬度',
            align:"center",
            dataIndex: 'latitude'
          },
          {
            title:'数据上传时间',
            align:"center",
            dataIndex: 'datatime',
            // customRender:function (text) {
            //   return !text?"":(text.length>10?text.substr(0,10):text)
            // }
          },
          {
            title: '运输状态',
            align: 'center',
            dataIndex: 'status',
            key: 'status',
            scopedSlots: { customRender: 'status' },
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
          list: "/clgl/clxxRealdata/listQSC",
          delete: "/clgl/clxxRealdata/delete",
          deleteBatch: "/clgl/clxxRealdata/deleteBatch",
          exportXlsUrl: "/clgl/clxxRealdata/exportXls",
          importExcelUrl: "clgl/clxxRealdata/importExcel",

        },
        dictOptions:{},
        superFieldList:[],
      }
    },
    created() {
      this.getSuperFieldList();
      this.shebeiList();
      this.getToken();
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      },
    },
    methods: {
      handleDetail: function (record) {
        console.log(record)
        this.$refs.modalForm.edit(record);
        this.$refs.modalForm.title = "详情";
        this.$refs.modalForm.disableSubmit = false;
      },
      handleDetail1: function (record) {
        console.log(record)
        this.$refs.modalForm.detail(record);
        this.$refs.modalForm.title = "轨迹回放";
        this.$refs.modalForm.disableSubmit = false;
      },
      shebeiList:function (){
        var sys_depart_orgcode=Vue.ls.get('SYS_DEPART_ORGCODE');
        usershebeiList({
          sys_depart_orgcode:sys_depart_orgcode,
          sbtypes:'73'
        }).then(res=>{
          // this.dictOption=[];
          let result=res.result;
          console.log(result)
          result.forEach(re=>{
            this.dictOption.push({text:re.sbname,value:re.sbjno})
            // console.log('+++++++++++'+re.sbname)
          })
          //console.log(res)
        });
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