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
      <a-button type="primary" icon="download" @click="handleExportXls('物资过磅数据记录表')">导出</a-button>
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

    <wzyclgbjilu-modal ref="modalForm" @ok="modalFormOk"></wzyclgbjilu-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import WzyclgbjiluModal from './modules/WzyclgbjiluModal'

  export default {
    name: 'WzyclgbjiluList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      WzyclgbjiluModal
    },
    data () {
      return {
        description: '物资过磅数据记录表管理页面',
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
            title:'进出材料单',
            align:"center",
            dataIndex: 'jinchuliaodanno'
          },
          {
            title:'材料编号',
            align:"center",
            dataIndex: 'cailiaono'
          },
          {
            title:'进场时间',
            align:"center",
            dataIndex: 'jinchangshijian'
          },
          {
            title:'出场时间',
            align:"center",
            dataIndex: 'chuchangshijian'
          },
          {
            title:'批次',
            align:"center",
            dataIndex: 'pici'
          },
          {
            title:'船号',
            align:"center",
            dataIndex: 'cheliangbianhao'
          },
          {
            title:'前车牌',
            align:"center",
            dataIndex: 'qianchepai'
          },
          {
            title:'后车牌',
            align:"center",
            dataIndex: 'houchepai'
          },
          {
            title:'第一次重量',
            align:"center",
            dataIndex: 'maozhong'
          },
          {
            title:'第二次重量',
            align:"center",
            dataIndex: 'pizhong'
          },
          {
            title:'净重',
            align:"center",
            dataIndex: 'jingzhong'
          },
          {
            title:'扣重',
            align:"center",
            dataIndex: 'kouzhong'
          },
          {
            title:'扣率',
            align:"center",
            dataIndex: 'koulv'
          },
          {
            title:'称重偏差',
            align:"center",
            dataIndex: 'chengzhongpiancha'
          },
          {
            title:'累计',
            align:"center",
            dataIndex: 'liaocang'
          },
          {
            title:'司磅员',
            align:"center",
            dataIndex: 'sibangyuan'
          },
          {
            title:'remark',
            align:"center",
            dataIndex: 'remark'
          },
          {
            title:'设备编号',
            align:"center",
            dataIndex: 'shebeibianhao'
          },
          {
            title:'供应商单位编码',
            align:"center",
            dataIndex: 'gongyingshangdanweibianma'
          },
          {
            title:'过磅类别',
            align:"center",
            dataIndex: 'guobangleibie'
          },
          {
            title:'车辆类型',
            align:"center",
            dataIndex: 'cheliangleixing'
          },
          {
            title:'唯一标识',
            align:"center",
            dataIndex: 'guid'
          },
          {
            title:'时间戳',
            align:"center",
            dataIndex: 'ts'
          },
          {
            title:'是否删除',
            align:"center",
            dataIndex: 'isdel'
          },
          {
            title:'0完成第一次称重1第一次称重后已确认2完成第二次称重',
            align:"center",
            dataIndex: 'status'
          },
          {
            title:'毛重(吨)',
            align:"center",
            dataIndex: 'maozhongt'
          },
          {
            title:'皮重(吨)',
            align:"center",
            dataIndex: 'pizhongt'
          },
          {
            title:'candi',
            align:"center",
            dataIndex: 'candi'
          },
          {
            title:'运输单位',
            align:"center",
            dataIndex: 'yunshudanwei'
          },
          {
            title:'料仓编号',
            align:"center",
            dataIndex: 'lcbianhao'
          },
          {
            title:'1先秤毛重0先秤皮重',
            align:"center",
            dataIndex: 'isshouliao'
          },
          {
            title:'净重(吨)',
            align:"center",
            dataIndex: 'jingzhongt'
          },
          {
            title:'是否统计 默认为0:未统计，1:已统计,15:统计出错',
            align:"center",
            dataIndex: 'istongji'
          },
          {
            title:'卸货地点',
            align:"center",
            dataIndex: 'liaocangid'
          },
          {
            title:'serialno',
            align:"center",
            dataIndex: 'serialno'
          },
          {
            title:'reason',
            align:"center",
            dataIndex: 'reason'
          },
          {
            title:'fileUpload',
            align:"center",
            dataIndex: 'fileUpload'
          },
          {
            title:'istaizhangtj',
            align:"center",
            dataIndex: 'istaizhangtj'
          },
          {
            title:'送货单',
            align:"center",
            dataIndex: 'songhuodanpic'
          },
          {
            title:'是否合格',
            align:"center",
            dataIndex: 'shifouhege'
          },
          {
            title:'原材描述',
            align:"center",
            dataIndex: 'yuancaimiaoshu'
          },
          {
            title:'beizhu',
            align:"center",
            dataIndex: 'beizhu'
          },
          {
            title:'jianlipic',
            align:"center",
            dataIndex: 'jianlipic'
          },
          {
            title:'sibanpic',
            align:"center",
            dataIndex: 'sibanpic'
          },
          {
            title:'jcgkpic',
            align:"center",
            dataIndex: 'jcgkpic'
          },
          {
            title:'jccppic',
            align:"center",
            dataIndex: 'jccppic'
          },
          {
            title:'jchcppic',
            align:"center",
            dataIndex: 'jchcppic'
          },
          {
            title:'jcbfpic',
            align:"center",
            dataIndex: 'jcbfpic'
          },
          {
            title:'ccgkpic',
            align:"center",
            dataIndex: 'ccgkpic'
          },
          {
            title:'cccppic',
            align:"center",
            dataIndex: 'cccppic'
          },
          {
            title:'cchcppic',
            align:"center",
            dataIndex: 'cchcppic'
          },
          {
            title:'ccbfpic',
            align:"center",
            dataIndex: 'ccbfpic'
          },
          {
            title:'台账统计状态 0;未统计，1:已统计，10:没有料仓配置，15:没有供应商单位，20;没有材料字典，40:异常',
            align:"center",
            dataIndex: 'taizhangtj'
          },
          {
            title:'台账id',
            align:"center",
            dataIndex: 'taizhangid'
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
          list: "/wzyclgbjilu/wzyclgbjilu/list",
          delete: "/wzyclgbjilu/wzyclgbjilu/delete",
          deleteBatch: "/wzyclgbjilu/wzyclgbjilu/deleteBatch",
          exportXlsUrl: "/wzyclgbjilu/wzyclgbjilu/exportXls",
          importExcelUrl: "wzyclgbjilu/wzyclgbjilu/importExcel",
          
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
        fieldList.push({type:'string',value:'jinchuliaodanno',text:'进出材料单',dictCode:''})
        fieldList.push({type:'string',value:'cailiaono',text:'材料编号',dictCode:''})
        fieldList.push({type:'string',value:'jinchangshijian',text:'进场时间',dictCode:''})
        fieldList.push({type:'string',value:'chuchangshijian',text:'出场时间',dictCode:''})
        fieldList.push({type:'string',value:'pici',text:'批次',dictCode:''})
        fieldList.push({type:'string',value:'cheliangbianhao',text:'船号',dictCode:''})
        fieldList.push({type:'string',value:'qianchepai',text:'前车牌',dictCode:''})
        fieldList.push({type:'string',value:'houchepai',text:'后车牌',dictCode:''})
        fieldList.push({type:'string',value:'maozhong',text:'第一次重量',dictCode:''})
        fieldList.push({type:'string',value:'pizhong',text:'第二次重量',dictCode:''})
        fieldList.push({type:'string',value:'jingzhong',text:'净重',dictCode:''})
        fieldList.push({type:'string',value:'kouzhong',text:'扣重',dictCode:''})
        fieldList.push({type:'string',value:'koulv',text:'扣率',dictCode:''})
        fieldList.push({type:'string',value:'chengzhongpiancha',text:'称重偏差',dictCode:''})
        fieldList.push({type:'string',value:'liaocang',text:'累计',dictCode:''})
        fieldList.push({type:'string',value:'sibangyuan',text:'司磅员',dictCode:''})
        fieldList.push({type:'string',value:'remark',text:'remark',dictCode:''})
        fieldList.push({type:'string',value:'shebeibianhao',text:'设备编号',dictCode:''})
        fieldList.push({type:'string',value:'gongyingshangdanweibianma',text:'供应商单位编码',dictCode:''})
        fieldList.push({type:'string',value:'guobangleibie',text:'过磅类别',dictCode:''})
        fieldList.push({type:'string',value:'cheliangleixing',text:'车辆类型',dictCode:''})
        fieldList.push({type:'string',value:'guid',text:'唯一标识',dictCode:''})
        fieldList.push({type:'int',value:'ts',text:'时间戳',dictCode:''})
        fieldList.push({type:'string',value:'isdel',text:'是否删除',dictCode:''})
        fieldList.push({type:'string',value:'status',text:'0完成第一次称重1第一次称重后已确认2完成第二次称重',dictCode:''})
        fieldList.push({type:'string',value:'maozhongt',text:'毛重(吨)',dictCode:''})
        fieldList.push({type:'string',value:'pizhongt',text:'皮重(吨)',dictCode:''})
        fieldList.push({type:'string',value:'candi',text:'candi',dictCode:''})
        fieldList.push({type:'string',value:'yunshudanwei',text:'运输单位',dictCode:''})
        fieldList.push({type:'string',value:'lcbianhao',text:'料仓编号',dictCode:''})
        fieldList.push({type:'string',value:'isshouliao',text:'1先秤毛重0先秤皮重',dictCode:''})
        fieldList.push({type:'string',value:'jingzhongt',text:'净重(吨)',dictCode:''})
        fieldList.push({type:'int',value:'istongji',text:'是否统计 默认为0:未统计，1:已统计,15:统计出错',dictCode:''})
        fieldList.push({type:'string',value:'liaocangid',text:'卸货地点',dictCode:''})
        fieldList.push({type:'string',value:'serialno',text:'serialno',dictCode:''})
        fieldList.push({type:'string',value:'reason',text:'reason',dictCode:''})
        fieldList.push({type:'string',value:'fileUpload',text:'fileUpload',dictCode:''})
        fieldList.push({type:'string',value:'istaizhangtj',text:'istaizhangtj',dictCode:''})
        fieldList.push({type:'string',value:'songhuodanpic',text:'送货单',dictCode:''})
        fieldList.push({type:'string',value:'shifouhege',text:'是否合格',dictCode:''})
        fieldList.push({type:'string',value:'yuancaimiaoshu',text:'原材描述',dictCode:''})
        fieldList.push({type:'string',value:'beizhu',text:'beizhu',dictCode:''})
        fieldList.push({type:'string',value:'jianlipic',text:'jianlipic',dictCode:''})
        fieldList.push({type:'string',value:'sibanpic',text:'sibanpic',dictCode:''})
        fieldList.push({type:'string',value:'jcgkpic',text:'jcgkpic',dictCode:''})
        fieldList.push({type:'string',value:'jccppic',text:'jccppic',dictCode:''})
        fieldList.push({type:'string',value:'jchcppic',text:'jchcppic',dictCode:''})
        fieldList.push({type:'string',value:'jcbfpic',text:'jcbfpic',dictCode:''})
        fieldList.push({type:'string',value:'ccgkpic',text:'ccgkpic',dictCode:''})
        fieldList.push({type:'string',value:'cccppic',text:'cccppic',dictCode:''})
        fieldList.push({type:'string',value:'cchcppic',text:'cchcppic',dictCode:''})
        fieldList.push({type:'string',value:'ccbfpic',text:'ccbfpic',dictCode:''})
        fieldList.push({type:'int',value:'taizhangtj',text:'台账统计状态 0;未统计，1:已统计，10:没有料仓配置，15:没有供应商单位，20;没有材料字典，40:异常',dictCode:''})
        fieldList.push({type:'int',value:'taizhangid',text:'台账id',dictCode:''})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>