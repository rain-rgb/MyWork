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
      <a-button type="primary" icon="download" @click="handleExportXls('混凝土成熟度监测历史表')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>
      <!-- 高级查询区域 -->
      <j-super-query :fieldList="superFieldList" ref="superQueryModal" @handleSuperQuery="handleSuperQuery"></j-super-query>
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

    <device-concrete-historydata-modal ref="modalForm" @ok="modalFormOk"></device-concrete-historydata-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import DeviceConcreteHistorydataModal from './modules/DeviceConcreteHistorydataModal'

  export default {
    name: 'DeviceConcreteHistorydataList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      DeviceConcreteHistorydataModal
    },
    data () {
      return {
        description: '混凝土成熟度监测历史表管理页面',
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
            title:'施工地址',
            align:"center",
            dataIndex: 'sgdz'
          },
          {
            title:'工程名称',
            align:"center",
            dataIndex: 'gcmc'
          },
          {
            title:'项目名称',
            align:"center",
            dataIndex: 'xmmc'
          },
          {
            title:'设备编号',
            align:"center",
            dataIndex: 'sbbh'
          },
          {
            title:'数据更新时间',
            align:"center",
            dataIndex: 'updatetime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'浇筑时间',
            align:"center",
            dataIndex: 'jiaozhudate',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'龄期（天）',
            align:"center",
            dataIndex: 'lq'
          },
          {
            title:'等效龄期（天）',
            align:"center",
            dataIndex: 'equivalentlq'
          },
          {
            title:'外置等效龄期（天）',
            align:"center",
            dataIndex: 'equivalentlqoutside'
          },
          {
            title:'成熟度（°C*天）',
            align:"center",
            dataIndex: 'maturity'
          },
          {
            title:'外置成熟度（°C*天）',
            align:"center",
            dataIndex: 'maturityoutside'
          },
          {
            title:'内置温度（°C）',
            align:"center",
            dataIndex: 'temperature'
          },
          {
            title:'外置温度（°C）',
            align:"center",
            dataIndex: 'temperatureoutside'
          },
          {
            title:'强度（Mpa）',
            align:"center",
            dataIndex: 'strength'
          },
          {
            title:'外置强度（Mpa）',
            align:"center",
            dataIndex: 'strengthoutside'
          },
          {
            title:'时间过程值',
            align:"center",
            dataIndex: 'time'
          },
          {
            title:'成熟度过程值',
            align:"center",
            dataIndex: 'maturitygcz'
          },
          {
            title:'外置成熟度过程值',
            align:"center",
            dataIndex: 'maturityoutsidegcz'
          },
          {
            title:'强度过程值',
            align:"center",
            dataIndex: 'strengthgcz'
          },
          {
            title:'外置强度过程值',
            align:"center",
            dataIndex: 'longtextoutsidegcz'
          },
          {
            title:'温度过程值',
            align:"center",
            dataIndex: 'tempgcz'
          },
          {
            title:'外置温度过程值',
            align:"center",
            dataIndex: 'tempoutsidegcz'
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
          list: "/device_concrete_historydata/deviceConcreteHistorydata/list",
          delete: "/device_concrete_historydata/deviceConcreteHistorydata/delete",
          deleteBatch: "/device_concrete_historydata/deviceConcreteHistorydata/deleteBatch",
          exportXlsUrl: "/device_concrete_historydata/deviceConcreteHistorydata/exportXls",
          importExcelUrl: "device_concrete_historydata/deviceConcreteHistorydata/importExcel",
          
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
        fieldList.push({type:'string',value:'sgdz',text:'施工地址'})
        fieldList.push({type:'string',value:'gcmc',text:'工程名称'})
        fieldList.push({type:'string',value:'xmmc',text:'项目名称'})
        fieldList.push({type:'string',value:'sbbh',text:'设备编号'})
        fieldList.push({type:'date',value:'updatetime',text:'数据更新时间'})
        fieldList.push({type:'date',value:'jiaozhudate',text:'浇筑时间'})
        fieldList.push({type:'string',value:'lq',text:'龄期（天）'})
        fieldList.push({type:'string',value:'equivalentlq',text:'等效龄期（天）'})
        fieldList.push({type:'string',value:'equivalentlqoutside',text:'外置等效龄期（天）'})
        fieldList.push({type:'string',value:'maturity',text:'成熟度（°C*天）'})
        fieldList.push({type:'string',value:'maturityoutside',text:'外置成熟度（°C*天）'})
        fieldList.push({type:'string',value:'temperature',text:'内置温度（°C）'})
        fieldList.push({type:'string',value:'temperatureoutside',text:'外置温度（°C）'})
        fieldList.push({type:'string',value:'strength',text:'强度（Mpa）'})
        fieldList.push({type:'string',value:'strengthoutside',text:'外置强度（Mpa）'})
        fieldList.push({type:'string',value:'time',text:'时间过程值'})
        fieldList.push({type:'string',value:'maturitygcz',text:'成熟度过程值'})
        fieldList.push({type:'string',value:'maturityoutsidegcz',text:'外置成熟度过程值'})
        fieldList.push({type:'string',value:'strengthgcz',text:'强度过程值'})
        fieldList.push({type:'string',value:'longtextoutsidegcz',text:'外置强度过程值'})
        fieldList.push({type:'string',value:'tempgcz',text:'温度过程值'})
        fieldList.push({type:'string',value:'tempoutsidegcz',text:'外置温度过程值'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>