<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">

        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="设备名称">
              <j-search-select-tag placeholder="请选择设备名称" v-model="queryParam.shebeino" :dictOptions="dictOption" >
              </j-search-select-tag>
              {{ selectValue }}
            </a-form-item>
          </a-col>

<!--          <a-col :xl="6" :lg="7" :md="8" :sm="24">-->
<!--            <a-form-item label="时间范围">-->
<!--              <j-date placeholder="开始时间" v-model="queryParam.stadate_begin" :showTime="true"-->
<!--                      dateFormat="YYYY-MM-DD HH:mm:ss"/>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :xl="6" :lg="7" :md="8" :sm="24">-->
<!--            <a-form-item label="">-->
<!--              <j-date placeholder="结束时间" v-model="queryParam.stadate_end" :showTime="true"-->
<!--                      dateFormat="YYYY-MM-DD HH:mm:ss"/>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
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

        <a-row :gutter="24">
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" v-has="'device_mixpile_static:add'" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" v-has="'device_mixpile_static:dc'" icon="download" @click="handleExportXls('软基数据统计分析')">导出</a-button>
      <a-upload name="file"  v-has="'device_mixpile_static:dr'" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
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

    <device-mixpile-static-modal ref="modalForm" @ok="modalFormOk"></device-mixpile-static-modal>
  </a-card>
</template>

<script>

  import '@assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import DeviceMixpileStaticModal from './modules/DeviceMixpileStaticModal'
  import JSuperQuery from '@comp/jeecg/JSuperQuery.vue'
  import { usershebeiList } from '@api/api'
  import Vue from 'vue'

  export default {
    name: 'DeviceMixpileSheBeiStaticList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      DeviceMixpileStaticModal,
      JSuperQuery,
    },
    data () {
      return {
        dictOption: [],
        selectValue:'',
        description: 'device_mixpile_static管理页面',
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
          // {
          //   title:'组织机构',
          //   align:"center",
          //   dataIndex: 'orgcode'
          // },
          // {
          //   title:'单位名称',
          //   align:"center",
          //   dataIndex: 'utilname'
          // },
          // {
          //   title:'统计日期',
          //   align:"center",
          //   dataIndex: 'stadate'
          // },
          {
            title:'设备名称',
            align:"center",
            dataIndex: 'shebeino_dictText',
          },
          {
            title:'桩基数（根）',
            align:"center",
            dataIndex: 'alertstate'
          },
          // {
          //   title:'里程id',
          //   align:"center",
          //   dataIndex: 'mileageid'
          // },
          {
            title:'施工总长度（m）',
            align:"center",
            dataIndex: 'pileRealdep'
          },
          // {
          //   title:'水泥浆总量',
          //   align:"center",
          //   dataIndex: 'allbeton'
          // },
          {
            title:'水泥总用量（kg）',
            align:"center",
            dataIndex: 'pileCement'
          },
          // {
          //   title:'水泥浆总量',
          //   align:"center",
          //   dataIndex: 'allbeton'
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
          list: "/deviceMixpileHistorydataOne/deviceMixpileHistorydataOne/stalist",
          delete: "/devicemixpilestatic/deviceMixpileStatic/delete",
          deleteBatch: "/devicemixpilestatic/deviceMixpileStatic/deleteBatch",
          exportXlsUrl: "/devicemixpilestatic/deviceMixpileStatic/exportXlsTJ",
          importExcelUrl: "devicemixpilestatic/deviceMixpileStatic/importExcel",

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
          sbtypes:'16,53'
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
        fieldList.push({type:'string',value:'orgcode',text:'组织机构'})
        fieldList.push({type:'string',value:'utilname',text:'单位名称'})
        fieldList.push({type:'string',value:'stadate',text:'统计日期'})
        fieldList.push({type:'string',value:'mileage',text:'里程名称'})
        fieldList.push({type:'string',value:'pilecount',text:'桩基数（根）'})
        fieldList.push({type:'int',value:'mileageid',text:'里程id'})
        fieldList.push({type:'string',value:'worklength',text:'施工总长度（m）'})
        fieldList.push({type:'string',value:'allash',text:'总用灰量(方)'})
        fieldList.push({type:'string',value:'allcement',text:'水泥总用量（kg）'})
        fieldList.push({type:'string',value:'allbeton',text:'水泥浆总量'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>