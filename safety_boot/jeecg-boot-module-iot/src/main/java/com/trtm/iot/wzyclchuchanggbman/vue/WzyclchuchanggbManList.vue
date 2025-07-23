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
      <a-button type="primary" icon="download" @click="handleExportXls('wzyclchuchanggb_man')">导出</a-button>
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

    <wzyclchuchanggb-man-modal ref="modalForm" @ok="modalFormOk"></wzyclchuchanggb-man-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import WzyclchuchanggbManModal from './modules/WzyclchuchanggbManModal'

  export default {
    name: 'WzyclchuchanggbManList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      WzyclchuchanggbManModal
    },
    data () {
      return {
        description: 'wzyclchuchanggb_man管理页面',
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
            title:'进出料单号',
            align:"center",
            dataIndex: 'jinchuliaodanno'
          },
          {
            title:'材料单号',
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
            title:'车辆编号',
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
            title:'毛重',
            align:"center",
            dataIndex: 'maozhong'
          },
          {
            title:'皮重',
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
            title:'料仓',
            align:"center",
            dataIndex: 'liaocang'
          },
          {
            title:'司磅员',
            align:"center",
            dataIndex: 'sibangyuan'
          },
          {
            title:'出场原因',
            align:"center",
            dataIndex: 'remark'
          },
          {
            title:'设备编号',
            align:"center",
            dataIndex: 'shebeibianhao'
          },
          {
            title:'供应商',
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
            title:'STATUS',
            align:"center",
            dataIndex: 'status'
          },
          {
            title:'ISDEL',
            align:"center",
            dataIndex: 'isdel'
          },
          {
            title:'TS',
            align:"center",
            dataIndex: 'ts'
          },
          {
            title:'GUID',
            align:"center",
            dataIndex: 'guid'
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
            title:'料仓',
            align:"center",
            dataIndex: 'lcbianhao'
          },
          {
            title:'ISSHOULIAO',
            align:"center",
            dataIndex: 'isshouliao'
          },
          {
            title:'净重(吨)',
            align:"center",
            dataIndex: 'jingzhongt'
          },
          {
            title:'ISTONGJI',
            align:"center",
            dataIndex: 'istongji'
          },
          {
            title:'进场高空',
            align:"center",
            dataIndex: 'jcgkpic'
          },
          {
            title:'进场车牌1',
            align:"center",
            dataIndex: 'jccppic'
          },
          {
            title:'进场车牌2',
            align:"center",
            dataIndex: 'jchcppic'
          },
          {
            title:'进场磅房',
            align:"center",
            dataIndex: 'jcbfpic'
          },
          {
            title:'出场高空',
            align:"center",
            dataIndex: 'ccgkpic'
          },
          {
            title:'出场车牌1',
            align:"center",
            dataIndex: 'cccppic'
          },
          {
            title:'出场车牌2',
            align:"center",
            dataIndex: 'cchcppic'
          },
          {
            title:'出场磅房',
            align:"center",
            dataIndex: 'ccbfpic'
          },
          {
            title:'料仓id',
            align:"center",
            dataIndex: 'liaocangid'
          },
          {
            title:'SERIALNO',
            align:"center",
            dataIndex: 'serialno'
          },
          {
            title:'原因',
            align:"center",
            dataIndex: 'reason'
          },
          {
            title:'文件上传',
            align:"center",
            dataIndex: 'fileUpload'
          },
          {
            title:'beizhu',
            align:"center",
            dataIndex: 'beizhu'
          },
          {
            title:'项目id',
            align:"center",
            dataIndex: 'projectid'
          },
          {
            title:'施工地点',
            align:"center",
            dataIndex: 'constructionsite'
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
            title: '操作',
            dataIndex: 'action',
            align:"center",
            fixed:"right",
            width:147,
            scopedSlots: { customRender: 'action' }
          }
        ],
        url: {
          list: "/wzyclchuchanggbman/wzyclchuchanggbMan/list",
          delete: "/wzyclchuchanggbman/wzyclchuchanggbMan/delete",
          deleteBatch: "/wzyclchuchanggbman/wzyclchuchanggbMan/deleteBatch",
          exportXlsUrl: "/wzyclchuchanggbman/wzyclchuchanggbMan/exportXls",
          importExcelUrl: "wzyclchuchanggbman/wzyclchuchanggbMan/importExcel",
          
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
        fieldList.push({type:'string',value:'jinchuliaodanno',text:'进出料单号'})
        fieldList.push({type:'string',value:'cailiaono',text:'材料单号'})
        fieldList.push({type:'string',value:'jinchangshijian',text:'进场时间'})
        fieldList.push({type:'string',value:'chuchangshijian',text:'出场时间'})
        fieldList.push({type:'string',value:'pici',text:'批次'})
        fieldList.push({type:'string',value:'cheliangbianhao',text:'车辆编号'})
        fieldList.push({type:'string',value:'qianchepai',text:'前车牌'})
        fieldList.push({type:'string',value:'houchepai',text:'后车牌'})
        fieldList.push({type:'string',value:'maozhong',text:'毛重'})
        fieldList.push({type:'string',value:'pizhong',text:'皮重'})
        fieldList.push({type:'string',value:'jingzhong',text:'净重'})
        fieldList.push({type:'string',value:'kouzhong',text:'扣重'})
        fieldList.push({type:'string',value:'koulv',text:'扣率'})
        fieldList.push({type:'string',value:'chengzhongpiancha',text:'称重偏差'})
        fieldList.push({type:'string',value:'liaocang',text:'料仓'})
        fieldList.push({type:'string',value:'sibangyuan',text:'司磅员'})
        fieldList.push({type:'string',value:'remark',text:'出场原因'})
        fieldList.push({type:'string',value:'shebeibianhao',text:'设备编号'})
        fieldList.push({type:'string',value:'gongyingshangdanweibianma',text:'供应商'})
        fieldList.push({type:'string',value:'guobangleibie',text:'过磅类别'})
        fieldList.push({type:'string',value:'cheliangleixing',text:'车辆类型'})
        fieldList.push({type:'string',value:'status',text:'STATUS'})
        fieldList.push({type:'string',value:'isdel',text:'ISDEL'})
        fieldList.push({type:'int',value:'ts',text:'TS'})
        fieldList.push({type:'string',value:'guid',text:'GUID'})
        fieldList.push({type:'string',value:'maozhongt',text:'毛重(吨)'})
        fieldList.push({type:'string',value:'pizhongt',text:'皮重(吨)'})
        fieldList.push({type:'string',value:'candi',text:'candi'})
        fieldList.push({type:'string',value:'yunshudanwei',text:'运输单位'})
        fieldList.push({type:'string',value:'lcbianhao',text:'料仓'})
        fieldList.push({type:'string',value:'isshouliao',text:'ISSHOULIAO'})
        fieldList.push({type:'string',value:'jingzhongt',text:'净重(吨)'})
        fieldList.push({type:'int',value:'istongji',text:'ISTONGJI'})
        fieldList.push({type:'string',value:'jcgkpic',text:'进场高空'})
        fieldList.push({type:'string',value:'jccppic',text:'进场车牌1'})
        fieldList.push({type:'string',value:'jchcppic',text:'进场车牌2'})
        fieldList.push({type:'string',value:'jcbfpic',text:'进场磅房'})
        fieldList.push({type:'string',value:'ccgkpic',text:'出场高空'})
        fieldList.push({type:'string',value:'cccppic',text:'出场车牌1'})
        fieldList.push({type:'string',value:'cchcppic',text:'出场车牌2'})
        fieldList.push({type:'string',value:'ccbfpic',text:'出场磅房'})
        fieldList.push({type:'string',value:'liaocangid',text:'料仓id'})
        fieldList.push({type:'string',value:'serialno',text:'SERIALNO'})
        fieldList.push({type:'string',value:'reason',text:'原因'})
        fieldList.push({type:'string',value:'fileUpload',text:'文件上传'})
        fieldList.push({type:'string',value:'beizhu',text:'beizhu'})
        fieldList.push({type:'string',value:'projectid',text:'项目id'})
        fieldList.push({type:'string',value:'constructionsite',text:'施工地点'})
        fieldList.push({type:'int',value:'taizhangtj',text:'台账统计状态 0;未统计，1:已统计，10:没有料仓配置，15:没有供应商单位，20;没有材料字典，40:异常'})
        fieldList.push({type:'int',value:'taizhangid',text:'台账id'})
        fieldList.push({type:'string',value:'istaizhangtj',text:'istaizhangtj'})
        fieldList.push({type:'string',value:'songhuodanpic',text:'送货单'})
        fieldList.push({type:'string',value:'shifouhege',text:'是否合格'})
        fieldList.push({type:'string',value:'yuancaimiaoshu',text:'原材描述'})
        fieldList.push({type:'string',value:'jianlipic',text:'jianlipic'})
        fieldList.push({type:'string',value:'sibanpic',text:'sibanpic'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>