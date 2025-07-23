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
            <a-form-item label="供应商">
              <j-search-select-tag placeholder="请选择供应商" v-model="queryParam.gongyingshangdanweibianma" :dictOptions="dictOption2" >
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
              <j-date placeholder="开始时间" v-model="queryParam.jinchangshijian_begin" :showTime="true" dateFormat="YYYY-MM-DD HH:mm:ss" />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="结束时间">
              <j-date placeholder="结束时间" v-model="queryParam.jinchangshijian_end" :showTime="true" dateFormat="YYYY-MM-DD HH:mm:ss" />
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
      <a-button v-has="'yclsl:add'" @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button @click="printXls" v-has="'yclsl:print'" type="primary" icon="printer">打印</a-button>
      <a-button type="primary" v-has="'yclsl:dc'" icon="download" @click="handleExportXls('原材料收料主表')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" v-has="'yclsl:dr'" icon="import">导入</a-button>
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
        <a-tag color="green" v-if="record.shifouhege == '合格'">合格</a-tag>
         <a-tag color="red" v-if="record.shifouhege == '不合格'">不合格</a-tag>
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
          <a v-has="'yclsl:edit'" @click="handleEdit(record)">编辑</a>
          <a @click="handlePreview(record)">预览</a>
          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item>
              <a-menu-item v-has="'yclsl:del'" >
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <wzycljinchanggb-modal ref="modalForm" @ok="modalFormOk"></wzycljinchanggb-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import WzycljinchanggbModal from './modules/WzycljinchanggbModal'
  import JSuperQuery from '@/components/jeecg/JSuperQuery.vue'
  import { handertoken } from '@/mixins/getHanderToken'
  import Vue from 'vue'
  import { SYS_DEPART_ORGCODE } from '@/store/mutation-types'
  import { usershebeiList,gongyingshangList,cailiaoList } from '@api/api'
  import { getAction} from '@/api/manage'
  import {base64Encode} from "@api/kkfileView";

  export default {
    name: 'WzycljinchanggbList',
    mixins:[JeecgListMixin, mixinDevice,handertoken],
    components: {
      WzycljinchanggbModal,
      JSuperQuery,
    },
    data () {
      return {
        selectValue: '',
        asyncSelectValue: '',
        dictOption: [],
        dictOption2: [],
        dictOption3: [],
        description: '原材料收料主表管理页面',
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
            dataIndex: 'shebeibianhao_dictText'
          },
          {
            title:'进场时间',
            align:"center",
            dataIndex: 'jinchangshijian',
          },

          // {
          //   title:'进出材料单',
          //   align:"center",
          //   dataIndex: 'jinchuliaodanno'
          // },
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
            title:'净重(吨)',
            align:"center",
            dataIndex: 'jingzhongt'
          },
          // {
          //   title:'出场时间',
          //   align:"center",
          //   dataIndex: 'chuchangshijian',
          // },

          // {
          //   title:'质保单号',
          //   align:"center",
          //   dataIndex: 'yuancaimiaoshu'
          // },
          // {
          //   title:'船号',
          //   align:"center",
          //   dataIndex: 'cheliangbianhao'
          // },
          {
            title:'车牌号',
            align:"center",
            dataIndex: 'qianchepai'
          },
          // {
          //   title:'后车牌',
          //   align:"center",
          //   dataIndex: 'houchepai'
          // },
          // {
          //   title:'毛重',
          //   align:"center",
          //   dataIndex: 'maozhong'
          // },
          // {
          //   title:'皮重',
          //   align:"center",
          //   dataIndex: 'pizhong'
          // },
          // {
          //   title:'净重',
          //   align:"center",
          //   dataIndex: 'jingzhong'
          // },
          // {
          //   title:'扣重',
          //   align:"center",
          //   dataIndex: 'kouzhong'
          // },
          // {
          //   title:'扣率',
          //   align:"center",
          //   dataIndex: 'koulv'
          // },
          // {
          //   title:'称重偏差',
          //   align:"center",
          //   dataIndex: 'chengzhongpiancha'
          // },
          // {
          //   title:'累计',
          //   align:"center",
          //   dataIndex: 'liaocang'
          // },
          // {
          //   title:'司磅员',
          //   align:"center",
          //   dataIndex: 'sibangyuan'
          // },
          // {
          //   title:'remark',
          //   align:"center",
          //   dataIndex: 'remark'
          // },

          {
            title:'供应商单位',
            align:"center",
            dataIndex: 'gongyingshangdanweibianma_dictText'
          },
          // {
          //   title:'过磅类别',
          //   align:"center",
          //   dataIndex: 'guobangleibie'
          // },
          // {
          //   title:'车辆类型',
          //   align:"center",
          //   dataIndex: 'cheliangleixing'
          // },
          // {
          //   title:'唯一标识',
          //   align:"center",
          //   dataIndex: 'guid'
          // },
          // {
          //   title:'时间戳',
          //   align:"center",
          //   dataIndex: 'ts'
          // },
          // {
          //   title:'是否删除',
          //   align:"center",
          //   dataIndex: 'isdel'
          // },
          // {
          //   title:'status',
          //   align:"center",
          //   dataIndex: 'status'
          // },

          // {
          //   title:'candi',
          //   align:"center",
          //   dataIndex: 'candi'
          // },
          {
            title:'使用部位',
            align:"center",
            dataIndex: 'yunshudanwei'
          },
          {
            title:'料仓名称',
            align:"center",
            dataIndex: 'lcbianhao_dictText'
          },
          // {
          //   title:'isshouliao',
          //   align:"center",
          //   dataIndex: 'isshouliao'
          // },

          // {
          //   title:'是否统计',
          //   align:"center",
          //   dataIndex: 'istongji_dictText'
          // },
          // {
          //   title:'jcgkpic',
          //   align:"center",
          //   dataIndex: 'jcgkpic'
          // },
          // {
          //   title:'jccppic',
          //   align:"center",
          //   dataIndex: 'jccppic'
          // },
          // {
          //   title:'jchcppic',
          //   align:"center",
          //   dataIndex: 'jchcppic'
          // },
          // {
          //   title:'jcbfpic',
          //   align:"center",
          //   dataIndex: 'jcbfpic'
          // },
          // {
          //   title:'ccgkpic',
          //   align:"center",
          //   dataIndex: 'ccgkpic'
          // },
          // {
          //   title:'cccppic',
          //   align:"center",
          //   dataIndex: 'cccppic'
          // },
          // {
          //   title:'cchcppic',
          //   align:"center",
          //   dataIndex: 'cchcppic'
          // },
          // {
          //   title:'ccbfpic',
          //   align:"center",
          //   dataIndex: 'ccbfpic'
          // },
          // {
          //   title:'卸货地点',
          //   align:"center",
          //   dataIndex: 'liaocangid'
          // },
          // {
          //   title:'serialno',
          //   align:"center",
          //   dataIndex: 'serialno'
          // },
          // {
          //   title:'reason',
          //   align:"center",
          //   dataIndex: 'reason'
          // },
          // {
          //   title:'fileUpload',
          //   align:"center",
          //   dataIndex: 'fileUpload'
          // },
          // {
          //   title:'istaizhangtj',
          //   align:"center",
          //   dataIndex: 'istaizhangtj'
          // },
          // {
          //   title:'送货单',
          //   align:"center",
          //   dataIndex: 'songhuodanpic'
          // },
          // {
          //   title:'是否合格',
          //   align:"center",
          //   dataIndex: 'shifouhege',
          //   key:'pdjg',
          //   scopedSlots: { customRender: 'tags'},
          // },
          // {
          //   title:'原材描述',
          //   align:"center",
          //   dataIndex: 'yuancaimiaoshu'
          // },
          {
            title:'备注',
            align:"center",
            dataIndex: 'rengon'
          },
          {
            title:'检验批次',
            align:"center",
            dataIndex: 'pici'
          },
          // {
          //   title:'jianlipic',
          //   align:"center",
          //   dataIndex: 'jianlipic'
          // },
          // {
          //   title:'sibanpic',
          //   align:"center",
          //   dataIndex: 'sibanpic'
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
          list: "/yclsl/wzycljinchanggb/list",
          delete: "/yclsl/wzycljinchanggb/delete",
          deleteBatch: "/yclsl/wzycljinchanggb/deleteBatch",
          exportXlsUrl: "/yclsl/wzycljinchanggb/exportXls",
          importExcelUrl: "yclsl/wzycljinchanggb/importExcel",
          printUrl:'jmreport/view/567199533894959104',

        },
        dictOptions:{},
        superFieldList:[],
      }
    },
    created() {
      this.getToken();
      this.getSuperFieldList();
      this.shebeiList();
      this.gongyingshangData();
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
      gongyingshangData:function (){
        var sys_depart_orgcode=Vue.ls.get('SYS_DEPART_ORGCODE');
        gongyingshangList({
          sys_depart_orgcode:sys_depart_orgcode,
          pageNo:1,
          pageSize:100
        }).then(res=>{
          this.dictOption2=[];
          let result=res.result.records;
          result.forEach(re=>{
            this.dictOption2.push({text:re.gongyingshangname,value:re.guid})
          })
        })
      },
      cailiaoData:function (){
        var sys_depart_orgcode=Vue.ls.get('SYS_DEPART_ORGCODE');
        let params = {
          sys_depart_orgcode:sys_depart_orgcode,
          ne:1,
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

      handlePreview(record) {
        if (record.songhuodanpic) {
          if (record.songhuodanpic.indexOf('http') > -1) {
            let url = window._CONFIG['onlinePreviewDomainURL'] + '?url=' + encodeURIComponent(base64Encode(record.songhuodanpic))
            window.open(url, '_blank')
          } else {
            let url = 'http://web.traiot.cn/docs/wz/' + record.songhuodanpic
            window.open(url, '_blank')
          }
          // let url = window._CONFIG['onlinePreviewDomainURL'] + '?url=' + encodeURIComponent(base64Encode("http://web.traiot.cn/docs/wz/"+record.songhuodanpic))

        } else {
          this.$message.info('未上传质保单！')
        }
      },


      initDictConfig(){
      },
      getSuperFieldList(){
        let fieldList=[];
        fieldList.push({type:'string',value:'jinchuliaodanno',text:'进出材料单',dictCode:''})
        fieldList.push({type:'string',value:'cailiaono',text:'材料编号',dictCode:''})
        fieldList.push({type:'date',value:'jinchangshijian',text:'进场时间',dictCode:''})
        fieldList.push({type:'date',value:'chuchangshijian',text:'出场时间',dictCode:''})
        fieldList.push({type:'string',value:'pici',text:'批次',dictCode:''})
        fieldList.push({type:'string',value:'cheliangbianhao',text:'船号',dictCode:''})
        fieldList.push({type:'string',value:'qianchepai',text:'前车牌',dictCode:''})
        fieldList.push({type:'string',value:'houchepai',text:'后车牌',dictCode:''})
        fieldList.push({type:'string',value:'maozhong',text:'毛重',dictCode:''})
        fieldList.push({type:'string',value:'pizhong',text:'皮重',dictCode:''})
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
        fieldList.push({type:'string',value:'status',text:'status',dictCode:''})
        fieldList.push({type:'string',value:'maozhongt',text:'毛重(吨)',dictCode:''})
        fieldList.push({type:'string',value:'pizhongt',text:'皮重(吨)',dictCode:''})
        fieldList.push({type:'string',value:'candi',text:'candi',dictCode:''})
        fieldList.push({type:'string',value:'yunshudanwei',text:'运输单位',dictCode:''})
        fieldList.push({type:'string',value:'lcbianhao',text:'料仓编号',dictCode:''})
        fieldList.push({type:'string',value:'isshouliao',text:'isshouliao',dictCode:''})
        fieldList.push({type:'string',value:'jingzhongt',text:'净重(吨)',dictCode:''})
        fieldList.push({type:'int',value:'istongji',text:'是否统计 默认为0:未统计，1:已统计,15:统计出错',dictCode:''})
        fieldList.push({type:'string',value:'jcgkpic',text:'jcgkpic',dictCode:''})
        fieldList.push({type:'string',value:'jccppic',text:'jccppic',dictCode:''})
        fieldList.push({type:'string',value:'jchcppic',text:'jchcppic',dictCode:''})
        fieldList.push({type:'string',value:'jcbfpic',text:'jcbfpic',dictCode:''})
        fieldList.push({type:'string',value:'ccgkpic',text:'ccgkpic',dictCode:''})
        fieldList.push({type:'string',value:'cccppic',text:'cccppic',dictCode:''})
        fieldList.push({type:'string',value:'cchcppic',text:'cchcppic',dictCode:''})
        fieldList.push({type:'string',value:'ccbfpic',text:'ccbfpic',dictCode:''})
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
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>