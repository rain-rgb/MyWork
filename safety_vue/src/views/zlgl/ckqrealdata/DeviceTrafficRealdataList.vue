<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <!-- <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="标段号">
              <j-search-select-tag placeholder="请选择设备名称" v-model="queryParam.sid" :dictOptions="dictOption" >
              </j-search-select-tag>
              {{ selectValue }}
            </a-form-item>
          </a-col> -->
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="标段号">
              <j-dict-select-tag placeholder="请选择" v-model="queryParam.bidCode" dictCode="ckqbd"></j-dict-select-tag>
            </a-form-item>
          </a-col>
          <!-- <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="标段号">
              <a-input placeholder="请输入标段号" v-model="queryParam.bidCode"></a-input>
            </a-form-item>
          </a-col> -->
          <!-- <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="时间范围">
              <j-date placeholder="开始时间" v-model="queryParam.datatime_begin" :showTime="true"
                      dateFormat="YYYY-MM-DD HH:mm:ss"/>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="">
              <j-date placeholder="结束时间" v-model="queryParam.datatime_end" :showTime="true"
                      dateFormat="YYYY-MM-DD HH:mm:ss"/>
            </a-form-item>
          </a-col> -->
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
      <a-button @click="handleAdd" v-has="'ckqreal:add'" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" v-has="'ckqreal:dc'" icon="download" @click="handleExportXls('采空区实时表信息')">导出</a-button>
      <a-upload name="file" v-has="'ckqreal:dr'" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
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

        <span slot="action"  slot-scope="text, record">
          <a  v-has="'ckqreal:edit'" @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical" v-has="'ckqreal:edit'"/>
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item>
              <a-menu-item v-has="'ckqreal:del'">
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <device-traffic-realdata-modal ref="modalForm" @ok="modalFormOk"></device-traffic-realdata-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import DeviceTrafficRealdataModal from './modules/DeviceTrafficRealdataModal'
  import JSuperQuery from '@/components/jeecg/JSuperQuery.vue'
  import Vue from 'vue'
  import { SYS_DEPART_ORGCODE } from '@/store/mutation-types'
  import { usershebeiList } from '@api/api'
  export default {
    name: 'DeviceTrafficRealdataList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      DeviceTrafficRealdataModal,
      JSuperQuery,
    },
    data () {
      return {
        selectValue: '',
        asyncSelectValue: '',
        dictOption: [],
        description: '数据统计',
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
            title:'标段号',
            align:"center",
            dataIndex: 'bidCode'
          },
          {
            title:'搅拌站号',
            align:"center",
            dataIndex: 'mixinStationCode'
          },
          {
            title:'配料生产总量(kg)',
            align:"center",
            dataIndex: 'batchingProductionTotal'
          },
          {
            title:'已注浆井数量',
            align:"center",
            dataIndex: 'groutingWellsNumber'
          },
          {
            title:'注浆总量(m³)',
            align:"center",
            dataIndex: 'groutingTotal'
          },
          // {
          //   title:'ms',
          //   align:"center",
          //   dataIndex: 'ms'
          // },
          // {
          //   title:'e',
          //   align:"center",
          //   dataIndex: 'e'
          // },
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
          list: "/mixin/station/selectMixinStationListPage",
          // delete: "/ckqrealdata/deviceTrafficRealdata/delete",
          // deleteBatch: "/ckqrealdata/deviceTrafficRealdata/deleteBatch",
          // exportXlsUrl: "/ckqrealdata/deviceTrafficRealdata/exportXls",
          // importExcelUrl: "ckqrealdata/deviceTrafficRealdata/importExcel",

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
          sbtypes:'36,37'
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
        fieldList.push({type:'string',value:'bidCode',text:'标段号'})
        fieldList.push({type:'string',value:'mixinStationCode',text:'搅拌站号'})
        fieldList.push({type:'number',value:'batchingProductionTotal',text:'配料生产总量'})
        fieldList.push({type:'integer',value:'groutingWellsNumber',text:'已注浆井数量'})
        fieldList.push({type:'number',value:'groutingTotal',text:'注浆总量'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>