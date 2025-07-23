<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="设备名称">
              <j-search-select-tag placeholder="请选择设备名称" v-model="queryParam.shebeibianhao" :dictOptions="dictOption" >
              </j-search-select-tag>
              {{ selectValue }}
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="收货商">
              <j-search-select-tag placeholder="请选择收货商" v-model="queryParam.liaocangid" :dictOptions="dictOption2" >
              </j-search-select-tag>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="材料规格">
              <j-search-select-tag placeholder="请选择材料规格" v-model="queryParam.cailiaono" :dictOptions="dictOption3" >
              </j-search-select-tag>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="时间范围">
              <j-date placeholder="开始时间" v-model="queryParam.chuchangshijian_begin" :showTime="true" dateFormat="YYYY-MM-DD HH:mm:ss" />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="结束时间">
              <j-date placeholder="结束时间" v-model="queryParam.chuchangshijian_end" :showTime="true" dateFormat="YYYY-MM-DD HH:mm:ss" />
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
      <a-button v-has="'yclcl:add'" @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button @click="printXls" v-has="'yclcl:print'" type="primary" icon="printer">打印</a-button>
      <a-button type="primary" v-has="'yclcl:dc'" icon="download" @click="handleExportXls('原材料出料主表')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" v-has="'yclcl:dr'" icon="import">导入</a-button>
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
          <a v-has="'yclcl:edit'" @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item>
              <a-menu-item v-has="'yclcl:del'" >
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <wzyclchuchanggb-modal ref="modalForm" @ok="modalFormOk"></wzyclchuchanggb-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import WzyclchuchanggbModal from './modules/WzyclchuchanggbModal'
  import JSuperQuery from '@/components/jeecg/JSuperQuery.vue'
  import { SYS_DEPART_ORGCODE } from '@/store/mutation-types'
  import { usershebeiList,gongyingshangList,cailiaoList } from '@api/api'
  import { getAction} from '@/api/manage'
  import Vue from 'vue'

  export default {
    name: 'WzyclchuchanggbList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      WzyclchuchanggbModal,
      JSuperQuery,
    },
    data () {
      return {
        selectValue: '',
        asyncSelectValue: '',
        dictOption: [],
        dictOption2: [],
        dictOption3: [],
        description: 'wzyclchuchanggb管理页面',
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
            title:'进出料单号',
            align:"center",
            dataIndex: 'jinchuliaodanno'
          },
          {
            title:'材料名称',
            align:"center",
            dataIndex: 'cailiaono_dictText'
          },
          {
            title:'规格类型',
            align:"center",
            dataIndex: 'beizhu'
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
          // {
          //   title:'后车牌',
          //   align:"center",
          //   dataIndex: 'houchepai'
          // },
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
          // {
          //   title:'料仓',
          //   align:"center",
          //   dataIndex: 'liaocang'
          // },
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
            dataIndex: 'shebeibianhao_dictText'
          },
          // {
          //   title:'供应商',
          //   align:"center",
          //   dataIndex: 'gongyingshangdanweibianma_dictText'
          // },
          {
            title:'收货单位',
            align:"center",
            dataIndex: 'liaocangid'
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
          // {
          //   title:'STATUS',
          //   align:"center",
          //   dataIndex: 'status'
          // },
          // {
          //   title:'ISDEL',
          //   align:"center",
          //   dataIndex: 'isdel'
          // },
          // {
          //   title:'TS',
          //   align:"center",
          //   dataIndex: 'ts'
          // },
          // {
          //   title:'GUID',
          //   align:"center",
          //   dataIndex: 'guid'
          // },
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
          // {
          //   title:'candi',
          //   align:"center",
          //   dataIndex: 'candi'
          // },
          {
            title:'运输单位',
            align:"center",
            dataIndex: 'yunshudanwei'
          },
          {
            title:'料仓',
            align:"center",
            dataIndex: 'lcbianhao_dictText'
          },
          // {
          //   title:'ISSHOULIAO',
          //   align:"center",
          //   dataIndex: 'isshouliao'
          // },
          {
            title:'净重(吨)',
            align:"center",
            dataIndex: 'jingzhongt'
          },
          // {
          //   title:'ISTONGJI',
          //   align:"center",
          //   dataIndex: 'istongji'
          // },
          // {
          //   title:'进场高空',
          //   align:"center",
          //   dataIndex: 'jcgkpic'
          // },
          // {
          //   title:'进场车牌1',
          //   align:"center",
          //   dataIndex: 'jccppic'
          // },
          // {
          //   title:'进场车牌2',
          //   align:"center",
          //   dataIndex: 'jchcppic'
          // },
          // {
          //   title:'进场磅房',
          //   align:"center",
          //   dataIndex: 'jcbfpic'
          // },
          // {
          //   title:'出场高空',
          //   align:"center",
          //   dataIndex: 'ccgkpic'
          // },
          // {
          //   title:'出场车牌1',
          //   align:"center",
          //   dataIndex: 'cccppic'
          // },
          // {
          //   title:'出场车牌2',
          //   align:"center",
          //   dataIndex: 'cchcppic'
          // },
          // {
          //   title:'出场磅房',
          //   align:"center",
          //   dataIndex: 'ccbfpic'
          // },
          // {
          //   title:'料仓id',
          //   align:"center",
          //   dataIndex: 'liaocangid'
          // },
          // {
          //   title:'SERIALNO',
          //   align:"center",
          //   dataIndex: 'serialno'
          // },
          // {
          //   title:'原因',
          //   align:"center",
          //   dataIndex: 'reason'
          // },
          // {
          //   title:'文件上传',
          //   align:"center",
          //   dataIndex: 'fileUpload'
          // },
          // {
          //   title:'beizhu',
          //   align:"center",
          //   dataIndex: 'beizhu'
          // },
          // {
          //   title:'项目id',
          //   align:"center",
          //   dataIndex: 'projectid'
          // },
          // {
          //   title:'施工地点',
          //   align:"center",
          //   dataIndex: 'constructionsite'
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
          list: "/yclcl/wzyclchuchanggb/list",
          delete: "/yclcl/wzyclchuchanggb/delete",
          deleteBatch: "/yclcl/wzyclchuchanggb/deleteBatch",
          exportXlsUrl: "/yclcl/wzyclchuchanggb/exportXls",
          importExcelUrl: "yclcl/wzyclchuchanggb/importExcel",//
          printUrl:'jmreport/view/600927332895735808',
        },
        dictOptions:{},
        superFieldList:[],
      }
    },
    created() {
      this.getSuperFieldList();
      this.shebeiList();
      this.shouhuoshangData();
      this.cailiaoData();
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
          sbtypes:'27'
        }).then(res=>{
          this.dictOption=[];
          let result=res.result;
          result.forEach(re=>{
            this.dictOption.push({text:re.sbname,value:re.sbjno})
          })
          //console.log(res)
        })
      },
      shouhuoshangData:function (){
        var sys_depart_orgcode=Vue.ls.get('SYS_DEPART_ORGCODE');
        let params = {
          sys_depart_orgcode:sys_depart_orgcode,
          pageNo:1,
          pageSize:100
        };
        getAction('/wzshouhuodanwei/wzshouhuodanwei/list',params).then(res=>{
          this.dictOption2=[];
          let result=res.result.records;
          result.forEach(re=>{
            this.dictOption2.push({text:re.gongyingshangname,value:re.gongyingshangname})
          })
        })
      },
      cailiaoData:function (){
        var sys_depart_orgcode=Vue.ls.get('SYS_DEPART_ORGCODE');
        let params = {
          sys_depart_orgcode:sys_depart_orgcode,
          ne:0,
          pageNo:1,
          pageSize:100
        }
        getAction('/wzcailiaonamedict/wzcailiaonamedict/list',params).then(res=>{
          this.dictOption3=[];
          let result=res.result.records;
          result.forEach(re=>{
            this.dictOption3.push({text:`${re.cailiaoname}(${re.guigexinghao})`,value:re.cailiaono})
          })
        })
      },
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
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>