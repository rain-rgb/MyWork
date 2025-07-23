<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
<!--      <a-button type="primary" icon="download" @click="handleExportXls('土方压实配置表')">导出</a-button>-->
<!--      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">-->
<!--        <a-button type="primary" icon="import">导入</a-button>-->
<!--      </a-upload>-->
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
        <span slot="tags" slot-scope="tags, record">
          <a-tag color="green" v-if="record.stauts == '0'">报警</a-tag>
          <a-tag color="red" v-if="record.stauts == '1'">不报警</a-tag>
        </span>
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

    <hc-tfysworkarea-configuration-modal ref="modalForm" @ok="modalFormOk"></hc-tfysworkarea-configuration-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import HcTfysworkareaConfigurationModal from './modules/HcTfysworkareaConfigurationModal'

  export default {
    name: 'HcTfysworkareaConfigurationList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      HcTfysworkareaConfigurationModal
    },
    data () {
      return {
        description: '土方压实配置表管理页面',
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
            title:'项目名称',
            align:"center",
            dataIndex: 'projectid_dictText'
          },
          {
            title:'标段名称',
            align:"center",
            dataIndex: 'sectionid_dictText'
          },
          {
            title:'开振占比初级预警(%)',
            align:"center",
            dataIndex: 'vibrateratioMin'
          },
          {
            title:'开振占比中级预警(%)',
            align:"center",
            dataIndex: 'vibrateratioZon'
          },
          {
            title:'开振占比高级预警(%)',
            align:"center",
            dataIndex: 'vibrateratioMax'
          },
          {
            title:'平均碾压遍数初级预警',
            align:"center",
            dataIndex: 'timesavgMin'
          },
          {
            title:'平均碾压遍数中级预警',
            align:"center",
            dataIndex: 'timesavgZon'
          },
          {
            title:'平均碾压遍数高级预警',
            align:"center",
            dataIndex: 'timesavgMax'
          },
          {
            title:'平均厚度初级预警(cm)',
            align:"center",
            dataIndex: 'thickavgMin'
          },
          {
            title:'平均厚度中级预警(cm)',
            align:"center",
            dataIndex: 'thickavgZon'
          },
          {
            title:'平均厚度高级预警(cm)',
            align:"center",
            dataIndex: 'thickavgMax'
          },
          {
            title:'初级号码组',
            align:"center",
            dataIndex: 'primaryPhones_dictText'
          },
          {
            title:'中级号码组',
            align:"center",
            dataIndex: 'middlePhones_dictText'
          },
          {
            title:'高级号码组',
            align:"center",
            dataIndex: 'advancedPhones_dictText'
          },
          {
            title:'是否发送短信',
            align:"center",
            dataIndex: 'stauts',
            scopedSlots: {customRender: 'tags'},
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
          list: "/hctfysworkareaconfiguration/hcTfysworkareaConfiguration/list",
          delete: "/hctfysworkareaconfiguration/hcTfysworkareaConfiguration/delete",
          deleteBatch: "/hctfysworkareaconfiguration/hcTfysworkareaConfiguration/deleteBatch",
          exportXlsUrl: "/hctfysworkareaconfiguration/hcTfysworkareaConfiguration/exportXls",
          importExcelUrl: "hctfysworkareaconfiguration/hcTfysworkareaConfiguration/importExcel",
          
        },
        dictOptions:{},
        superFieldList:[],
      }
    },
    created() {
    this.getSuperFieldList();
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      },
    },
    methods: {
      initDictConfig(){
      },
      getSuperFieldList(){
        let fieldList=[];
        fieldList.push({type:'string',value:'projectid',text:'项目名称',dictCode:''})
        fieldList.push({type:'string',value:'sectionid',text:'标段名称',dictCode:''})
        fieldList.push({type:'string',value:'vibrateratioMin',text:'开振占比（%）最小值',dictCode:''})
        fieldList.push({type:'string',value:'vibrateratioMax',text:'开振占比（%）最大值',dictCode:''})
        fieldList.push({type:'string',value:'timesavgMin',text:'平均碾压遍数最小值',dictCode:''})
        fieldList.push({type:'string',value:'timesavgMax',text:'平均碾压遍数最大值',dictCode:''})
        fieldList.push({type:'string',value:'thickavgMin',text:'平均厚度（m）最小值',dictCode:''})
        fieldList.push({type:'string',value:'thickavgMax',text:'平均厚度（m）最大值',dictCode:''})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>