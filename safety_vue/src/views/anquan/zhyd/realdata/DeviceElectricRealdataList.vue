<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="设备名称">
              <j-search-select-tag placeholder="请选择设备名称" v-model="queryParam.imei" :dictOptions="dictOption" >
              </j-search-select-tag>
              {{ selectValue }}
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="时间范围">
              <j-date placeholder="上传时间" v-model="queryParam.elecdate_begin" :showTime="true" dateFormat="YYYY-MM-DD HH:mm:ss" />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="结束时间">
              <j-date placeholder="结束时间" v-model="queryParam.elecdate_end" :showTime="true" dateFormat="YYYY-MM-DD HH:mm:ss" />
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
      <a-button v-has="'real:add'" @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" v-has="'real:dc'" icon="download" @click="handleExportXls('智慧用电实时数据表')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" v-has="'real:dr'" icon="import">导入</a-button>
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
          <a-tag color="orange" v-if="record.status === 0">未检测</a-tag>
          <a-tag color="green" v-if="record.status ===  1">合格</a-tag>
          <a-tag color="red" v-if="record.status === 2 ">不合格</a-tag>
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
          <a v-has="'real:edit'" @click="handleEdit(record)">编辑</a>

<!--          <a-divider type="vertical" />-->
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item>
              <a-menu-item v-has="'real:del'" >
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <device-electric-realdata-modal ref="modalForm" @ok="modalFormOk"></device-electric-realdata-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import DeviceElectricRealdataModal from './modules/DeviceElectricRealdataModal'
  import JSuperQuery from '@/components/jeecg/JSuperQuery.vue'
  import { SYS_DEPART_ORGCODE } from '@/store/mutation-types'
  import { usershebeiList } from '@api/api'
  import Vue from 'vue'
  import { handertoken } from '@/mixins/getHanderToken'

  export default {
    name: 'DeviceElectricRealdataList',
    mixins:[JeecgListMixin, mixinDevice,handertoken],
    components: {
      DeviceElectricRealdataModal,
      JSuperQuery,
    },
    data () {
      return {
        selectValue: '',
        asyncSelectValue: '',
        dictOption: [],
        description: '智慧用电实时数据表管理页面',
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
            title:'设备名称',
            align:"center",
            dataIndex: 'imei_dictText'
          },
          {
            title:'电压A(V)',
            align:"center",
            dataIndex: 'voltagea'
          },
          {
            title:'电压B(V)',
            align:"center",
            dataIndex: 'voltageb'
          },
          {
            title:'电压C(V)',
            align:"center",
            dataIndex: 'voltagec'
          },
          {
            title:'频率A(hz)',
            align:"center",
            dataIndex: 'frequencya'
          },
          {
            title:'频率B(hz)',
            align:"center",
            dataIndex: 'frequencyb'
          },
          {
            title:'频率C(hz)',
            align:"center",
            dataIndex: 'frequencyc'
          },
          {
            title:'电流A(A)',
            align:"center",
            dataIndex: 'electricitya'
          },
          {
            title:'电流B(A)',
            align:"center",
            dataIndex: 'electricityb'
          },
          {
            title:'电流C(A)',
            align:"center",
            dataIndex: 'electricityc'
          },
          {
            title:'漏电流(mA)',
            align:"center",
            dataIndex: 'seepelectricity'
          },
          {
            title:'温度A(℃)',
            align:"center",
            dataIndex: 'tempa'
          },
          {
            title:'温度B(℃)',
            align:"center",
            dataIndex: 'tempb'
          },
          {
            title:'温度C(℃)',
            align:"center",
            dataIndex: 'tempc'
          },
          {
            title:'温度N(℃)',
            align:"center",
            dataIndex: 'tempn'
          },
          {
            title:'电能A(KW.H)',
            align:"center",
            dataIndex: 'energya'
          },
          {
            title:'电能B(KW.H)',
            align:"center",
            dataIndex: 'energyb'
          },
          {
            title:'电能C(KW.H)',
            align:"center",
            dataIndex: 'energyc'
          },
          {
            title:'状态',
            align:"center",
            dataIndex: 'status',
            scopedSlots: { customRender: 'tags' },
          },
          // {
          //   title:'是否超标',
          //   align:"center",
          //   dataIndex: 'alertstate'
          // },
          {
            title:'数据上传时间',
            align:"center",
            dataIndex: 'elecdate',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
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
          list: "/zhydreal/deviceElectricRealdata/list",
          delete: "/zhydreal/deviceElectricRealdata/delete",
          deleteBatch: "/zhydreal/deviceElectricRealdata/deleteBatch",
          exportXlsUrl: "/zhydreal/deviceElectricRealdata/exportXls",
          importExcelUrl: "zhydreal/deviceElectricRealdata/importExcel",

        },
        dictOptions:{},
        superFieldList:[],
      }
    },
    created() {
      this.getToken();
      // this.getSuperFieldList();
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
          sbtypes:'17'
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
        fieldList.push({type:'string',value:'imei',text:'设备编号',dictCode:''})
        fieldList.push({type:'double',value:'voltagea',text:'电压A',dictCode:''})
        fieldList.push({type:'double',value:'voltageb',text:'电压B',dictCode:''})
        fieldList.push({type:'double',value:'voltagec',text:'电压C',dictCode:''})
        fieldList.push({type:'double',value:'frequencya',text:'频率A',dictCode:''})
        fieldList.push({type:'double',value:'frequencyb',text:'频率B',dictCode:''})
        fieldList.push({type:'double',value:'frequencyc',text:'频率C',dictCode:''})
        fieldList.push({type:'double',value:'electricitya',text:'电流A',dictCode:''})
        fieldList.push({type:'double',value:'electricityb',text:'电流B',dictCode:''})
        fieldList.push({type:'double',value:'electricityc',text:'电流C',dictCode:''})
        fieldList.push({type:'double',value:'seepelectricity',text:'漏电流',dictCode:''})
        fieldList.push({type:'double',value:'tempa',text:'温度A',dictCode:''})
        fieldList.push({type:'double',value:'tempb',text:'温度B',dictCode:''})
        fieldList.push({type:'double',value:'tempc',text:'温度C',dictCode:''})
        fieldList.push({type:'double',value:'tempn',text:'温度N',dictCode:''})
        fieldList.push({type:'double',value:'energya',text:'电能A',dictCode:''})
        fieldList.push({type:'double',value:'energyb',text:'电能B',dictCode:''})
        fieldList.push({type:'double',value:'energyc',text:'电能C',dictCode:''})
        fieldList.push({type:'date',value:'elecdate',text:'数据上传时间'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>