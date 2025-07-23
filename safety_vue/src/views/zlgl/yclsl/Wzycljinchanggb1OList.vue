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
<!--              {{ selectValue }}-->
            </a-form-item>
          </a-col>

          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label= "批次" >
              <a-input placeholder="批次" v-model="queryParam.pici"  ></a-input>
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



          <a-col :xl="4" :lg="7" :md="8" :sm="24">
            <a-form-item label="时间">
              <j-date placeholder="开始时间" v-model="queryParam.jinchangshijian_begin" :showTime="true" dateFormat="YYYY-MM-DD HH:mm:ss" />
            </a-form-item>
          </a-col>
          <a-col :xl="4" :lg="7" :md="8" :sm="24">
            <a-form-item >
              <j-date placeholder="结束时间" v-model="queryParam.jinchangshijian_end" :showTime="true" dateFormat="YYYY-MM-DD HH:mm:ss" />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <j-super-query v-show="toggleSearchStatus" style="margin-left: 8px" :fieldList="superFieldList" ref="superQueryModal" @handleSuperQuery="handleSuperQuery"></j-super-query>
              <a @click="handleToggleSearch" style="margin-left: 8px">
                {{ toggleSearchStatus ? '收起' : '展开' }}
                <a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>
              </a>

            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
<!--      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>-->
<!--      <a-button type="primary" icon="download" @click="handleExportXls('wzycljinchanggb')">导出</a-button>-->
<!--      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">-->
<!--        <a-button type="primary" icon="import">导入</a-button>-->
<!--      </a-upload>-->
      <!-- 高级查询区域 -->

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



         <span slot="tags" slot-scope="tags, record">
         <a-tag color="blue" v-if="record.jystatus == 0">未推送</a-tag>
         <a-tag color="green" v-else-if="record.jystatus == 1 ">已推送</a-tag>
         <a-tag color="red" v-else>推送失败</a-tag>
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
          <a @click="handleEdit(record)">编辑</a>
          <a-divider type="vertical" />
          <a @click="handlePreview(record)">预览</a>
          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item>
               <a-menu-item>
                <a-popconfirm title="确定推送试验平台吗?" @confirm="() => handlePushsy(record)">
                  <a>推送</a>
                </a-popconfirm>
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

    <wzycljinchanggb1-o-modal ref="modalForm" @ok="modalFormOk"></wzycljinchanggb1-o-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import Wzycljinchanggb1OModal from './modules/Wzycljinchanggb1OModal'
  import Vue from "vue";
  import {gongyingshangList, usershebeiList} from "@api/api";
  import {getAction,putAction} from "@api/manage";
  import { pushdepartidShebei } from '@/mixins/pushdepartidShebei'
  import {base64Encode} from "@api/kkfileView";
  export default {
    name: 'Wzycljinchanggb1OList',
    mixins:[JeecgListMixin, mixinDevice,pushdepartidShebei],
    components: {
      Wzycljinchanggb1OModal
    },
    data () {
      return {
        queryParam: {gblx:0},
        description: 'wzycljinchanggb管理页面',
        dictOption:[],
        dictOption2:[],
        dictOption3:[],
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
            title:'材料名称',
            align:"center",
            dataIndex: 'cailiaono_dictText'
          },
          {
            title:'规格类型',
            align:"center",
            dataIndex: 'guigexinghao'
          },
          {
            title:'进场时间',
            align:"center",
            dataIndex: 'jinchangshijian',
          },
          {
            title:'出厂时间',
            align:"center",
            dataIndex: 'chuchangshijian',
          },
          {
            title:'检验批次',
            align:"center",
            dataIndex: 'pici'
          },
          {
            title:'质保单号',
            align:"center",
            dataIndex: 'shengchanpihao'
          },
          {
            title:'供应商单位',
            align:"center",
            dataIndex: 'gongyingshangdanweibianma_dictText'
          },

          {
            title:'净重(吨)',
            align:"center",
            dataIndex: 'jingzhongt'
          },
          {
            title:'料仓名称',
            align:"center",
            dataIndex: 'lcbianhao_dictText'
          },
          {
            title:'存放地点',
            align:"center",
            dataIndex: 'storagePlace'
          },
          // {
          //   title:'使用部位',
          //   align:"center",
          //   dataIndex: 'usePart'
          // },

          // {
          //   title:'送货单',
          //   align:"center",
          //   dataIndex: 'songhuodanpic'
          // },

          {
            title:'推送状态',// 0;未推送，1:推送成功，10:没有料仓配置，15:没有供应商单位，20;没有材料字典，40:推送异常
            align:"center",
            dataIndex: 'jystatus',
            scopedSlots: { customRender: 'tags'}
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
          list: "/wztaizhang/wztaizhang/listsp",
          delete: "/yclsl/wzycljinchanggb/delete",
          deleteBatch: "/yclsl/wzycljinchanggb/deleteBatch",
          exportXlsUrl: "/yclsl/wzycljinchanggb/exportXls",
          importExcelUrl: "/yclsl/wzycljinchanggb/importExcel",
          pushSyData:"/sys/systems/sysMessages/pushSyDataO"
          
        },
        dictOptions:{},
        superFieldList:[],
      }
    },
    created() {
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

      handlePushsy(record){

        putAction(this.url.pushSyData,record).then(res=>{
          if (res.success) {
            this.loadData();
            this.$message.success(res.message)
            this.$emit('ok');
          } else {
            this.$message.error(res.message)
          }
        })
      },

      handlePreview(record) {
        if (record.zhibaodan) {
          if (record.zhibaodan.indexOf('http') > -1) {
            let url = window._CONFIG['onlinePreviewDomainURL'] + '?url=' + encodeURIComponent(base64Encode(record.zhibaodan))
            window.open(url, '_blank')
          } else {
            let url = 'http://web.traiot.cn/docs/wz/' + record.zhibaodan
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
        fieldList.push({type:'string',value:'jinchuliaodanno',text:'进出材料单'})
        fieldList.push({type:'string',value:'cailiaono',text:'材料编号'})
        fieldList.push({type:'string',value:'jinchangshijian',text:'进场时间'})
        fieldList.push({type:'string',value:'chuchangshijian',text:'出场时间'})
        fieldList.push({type:'string',value:'pici',text:'批次'})
        fieldList.push({type:'string',value:'cheliangbianhao',text:'船号'})
        fieldList.push({type:'string',value:'qianchepai',text:'前车牌'})
        fieldList.push({type:'string',value:'houchepai',text:'后车牌(发车单)'})
        fieldList.push({type:'string',value:'maozhong',text:'毛重'})
        fieldList.push({type:'string',value:'pizhong',text:'皮重'})
        fieldList.push({type:'string',value:'jingzhong',text:'净重'})
        fieldList.push({type:'string',value:'kouzhong',text:'扣重'})
        fieldList.push({type:'string',value:'koulv',text:'扣率'})
        fieldList.push({type:'string',value:'chengzhongpiancha',text:'称重偏差'})
        fieldList.push({type:'string',value:'liaocang',text:'累计'})
        fieldList.push({type:'string',value:'sibangyuan',text:'司磅员'})
        fieldList.push({type:'string',value:'remark',text:'remark'})
        fieldList.push({type:'string',value:'shebeibianhao',text:'设备编号'})
        fieldList.push({type:'string',value:'gongyingshangdanweibianma',text:'供应商单位编码'})
        fieldList.push({type:'string',value:'guobangleibie',text:'过磅类别'})
        fieldList.push({type:'string',value:'cheliangleixing',text:'车辆类型'})
        fieldList.push({type:'string',value:'guid',text:'唯一标识'})
        fieldList.push({type:'int',value:'ts',text:'时间戳'})
        fieldList.push({type:'string',value:'isdel',text:'是否删除'})
        fieldList.push({type:'string',value:'status',text:'status'})
        fieldList.push({type:'string',value:'maozhongt',text:'毛重(吨)'})
        fieldList.push({type:'string',value:'pizhongt',text:'皮重(吨)'})
        fieldList.push({type:'string',value:'candi',text:'candi'})
        fieldList.push({type:'string',value:'yunshudanwei',text:'运输单位'})
        fieldList.push({type:'string',value:'lcbianhao',text:'料仓编号'})
        fieldList.push({type:'string',value:'isshouliao',text:'isshouliao'})
        fieldList.push({type:'string',value:'jingzhongt',text:'净重(吨)'})
        fieldList.push({type:'int',value:'istongji',text:'是否统计 默认为0:未统计，1:已统计,15:统计出错'})
        fieldList.push({type:'string',value:'liaocangid',text:'卸货地点'})
        fieldList.push({type:'string',value:'serialno',text:'serialno'})
        fieldList.push({type:'string',value:'reason',text:'reason'})
        fieldList.push({type:'string',value:'fileUpload',text:'fileUpload'})
        fieldList.push({type:'string',value:'istaizhangtj',text:'istaizhangtj'})
        fieldList.push({type:'string',value:'songhuodanpic',text:'送货单'})
        fieldList.push({type:'string',value:'shifouhege',text:'是否合格'})
        fieldList.push({type:'string',value:'yuancaimiaoshu',text:'原材描述'})
        fieldList.push({type:'string',value:'beizhu',text:'beizhu'})
        fieldList.push({type:'string',value:'jianlipic',text:'jianlipic'})
        fieldList.push({type:'string',value:'sibanpic',text:'sibanpic'})
        fieldList.push({type:'string',value:'jcgkpic',text:'进场高空图片'})
        fieldList.push({type:'string',value:'jccppic',text:'jccppic'})
        fieldList.push({type:'string',value:'jchcppic',text:'jchcppic'})
        fieldList.push({type:'string',value:'jcbfpic',text:'jcbfpic'})
        fieldList.push({type:'string',value:'ccgkpic',text:'ccgkpic'})
        fieldList.push({type:'string',value:'cccppic',text:'cccppic'})
        fieldList.push({type:'string',value:'cchcppic',text:'cchcppic'})
        fieldList.push({type:'string',value:'ccbfpic',text:'ccbfpic'})
        fieldList.push({type:'int',value:'taizhangtj',text:'台账统计状态 0;未统计，1:已统计，10:没有料仓配置，15:没有供应商单位，20;没有材料字典，40:异常'})
        fieldList.push({type:'int',value:'taizhangid',text:'台账id'})
        fieldList.push({type:'int',value:'jingzhongttj',text:'净重T统计(0：未统计(如果有些LCbianhao为null，则不统计)，1：统计成功，)'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>