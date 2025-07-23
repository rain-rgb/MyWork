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
      <a-button type="primary" icon="download" @click="handleExportXls('device_piplewall_one')">导出</a-button>
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

    <device-piplewall-one-modal ref="modalForm" @ok="modalFormOk"></device-piplewall-one-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import DevicePiplewallOneModal from './modules/DevicePiplewallOneModal'

  export default {
    name: 'DevicePiplewallOneList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      DevicePiplewallOneModal
    },
    data () {
      return {
        description: 'device_piplewall_one管理页面',
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
            title:'设备编号',
            align:"center",
            dataIndex: 'shebeino'
          },
          {
            title:'序号',
            align:"center",
            dataIndex: 'pileNo'
          },
          {
            title:'施工深度(m)',
            align:"center",
            dataIndex: 'pileRealdep'
          },
          {
            title:'工作时长(min)',
            align:"center",
            dataIndex: 'pileWorktime'
          },
          {
            title:'结束时间',
            align:"center",
            dataIndex: 'pileEndtime'
          },
          {
            title:'开始时间',
            align:"center",
            dataIndex: 'pileStarttime'
          },
          {
            title:'倾斜度',
            align:"center",
            dataIndex: 'pileY'
          },
          {
            title:'下钻速度(cm/min)',
            align:"center",
            dataIndex: 'downSpeed'
          },
          {
            title:'提钻速度(cm/min)',
            align:"center",
            dataIndex: 'upSpeed'
          },
          {
            title:'设计桩长',
            align:"center",
            dataIndex: 'pileDesigndep'
          },
          {
            title:'关联软基任务单 device_mixpile_rwd',
            align:"center",
            dataIndex: 'rjrwd'
          },
          {
            title:'里程',
            align:"center",
            dataIndex: 'pileMileage'
          },
          {
            title:'桩经度',
            align:"center",
            dataIndex: 'pileLgd'
          },
          {
            title:'桩纬度',
            align:"center",
            dataIndex: 'pileLtd'
          },
          {
            title:'超标等级0：合格；1不合格',
            align:"center",
            dataIndex: 'chaobiaodengji'
          },
          {
            title:'0未处置；10：处置；20：闭合，30驳回',
            align:"center",
            dataIndex: 'overproofStatus'
          },
          {
            title:'异常原因',
            align:"center",
            dataIndex: 'ycyy'
          },
          {
            title:'接桩次数',
            align:"center",
            dataIndex: 'jiegantimes'
          },
          {
            title:'接杆长度',
            align:"center",
            dataIndex: 'jieganchangdu'
          },
          {
            title:'长度(cm)',
            align:"center",
            dataIndex: 'changdu'
          },
          {
            title:'宽度(cm)',
            align:"center",
            dataIndex: 'kuandu'
          },
          {
            title:'总方量m³',
            align:"center",
            dataIndex: 'zongm'
          },
          {
            title:'电流',
            align:"center",
            dataIndex: 'dianliu'
          },
          {
            title:'泥浆密度',
            align:"center",
            dataIndex: 'pileDensity'
          },
          {
            title:'备注',
            align:"center",
            dataIndex: 'note'
          },
          {
            title:'唯一id',
            align:"center",
            dataIndex: 'uuid'
          },
          {
            title:'倾斜度x',
            align:"center",
            dataIndex: 'pileX'
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
          list: "/devicepiplewall/devicePiplewallOne/list",
          delete: "/devicepiplewall/devicePiplewallOne/delete",
          deleteBatch: "/devicepiplewall/devicePiplewallOne/deleteBatch",
          exportXlsUrl: "/devicepiplewall/devicePiplewallOne/exportXls",
          importExcelUrl: "devicepiplewall/devicePiplewallOne/importExcel",
          
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
        fieldList.push({type:'string',value:'shebeino',text:'设备编号'})
        fieldList.push({type:'string',value:'pileNo',text:'序号'})
        fieldList.push({type:'string',value:'pileRealdep',text:'施工深度(m)'})
        fieldList.push({type:'string',value:'pileWorktime',text:'工作时长(min)'})
        fieldList.push({type:'string',value:'pileEndtime',text:'结束时间'})
        fieldList.push({type:'string',value:'pileStarttime',text:'开始时间'})
        fieldList.push({type:'string',value:'pileY',text:'倾斜度'})
        fieldList.push({type:'string',value:'downSpeed',text:'下钻速度(cm/min)'})
        fieldList.push({type:'number',value:'upSpeed',text:'提钻速度(cm/min)'})
        fieldList.push({type:'int',value:'pileDesigndep',text:'设计桩长'})
        fieldList.push({type:'string',value:'rjrwd',text:'关联软基任务单 device_mixpile_rwd'})
        fieldList.push({type:'string',value:'pileMileage',text:'里程'})
        fieldList.push({type:'string',value:'pileLgd',text:'桩经度'})
        fieldList.push({type:'string',value:'pileLtd',text:'桩纬度'})
        fieldList.push({type:'int',value:'chaobiaodengji',text:'超标等级0：合格；1不合格'})
        fieldList.push({type:'int',value:'overproofStatus',text:'0未处置；10：处置；20：闭合，30驳回'})
        fieldList.push({type:'string',value:'ycyy',text:'异常原因'})
        fieldList.push({type:'int',value:'jiegantimes',text:'接桩次数'})
        fieldList.push({type:'string',value:'jieganchangdu',text:'接杆长度'})
        fieldList.push({type:'string',value:'changdu',text:'长度(cm)'})
        fieldList.push({type:'string',value:'kuandu',text:'宽度(cm)'})
        fieldList.push({type:'string',value:'zongm',text:'总方量m³'})
        fieldList.push({type:'string',value:'dianliu',text:'电流'})
        fieldList.push({type:'string',value:'pileDensity',text:'泥浆密度'})
        fieldList.push({type:'string',value:'note',text:'备注'})
        fieldList.push({type:'string',value:'uuid',text:'唯一id'})
        fieldList.push({type:'string',value:'pileX',text:'倾斜度x'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>