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

          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="里程名称">
              <a-input placeholder="请输入里程名称" v-model="queryParam.pileMileage"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="成桩时间范围">
              <j-date placeholder="开始时间" v-model="queryParam.pileTime_begin" :showTime="true"
                      dateFormat="YYYY-MM-DD HH:mm:ss"/>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="">
              <j-date placeholder="结束时间" v-model="queryParam.pileTime_end" :showTime="true"
                      dateFormat="YYYY-MM-DD HH:mm:ss"/>
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
<!--      <a-button @click="handleAdd" v-has="'pipepilehistoryone:add'" type="primary" icon="plus">新增</a-button>-->
<!--      <a-button type="primary" icon="download" @click="handleExportXls('管桩成桩记录信息表')">导出</a-button>-->
<!--      <a-button type="primary" icon="download" v-has="'pipepilehistoryone:dc'" @click="handleExportXls('管桩成桩记录信息表')">导出</a-button>-->
<!--      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">-->
<!--        <a-button type="primary" v-has="'pipepilehistoryone:dr'" icon="import">导入</a-button>-->
<!--      </a-upload>-->
<!--      <a-button type="primary" icon="download" @click="handleExportXls('管桩成桩记录信息表')">导出</a-button>-->
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
        <span slot="tags1" slot-scope="tags1, record">
          <a-tag color="green" v-if="record.handler.overproofStatus === 0">未处理</a-tag>
          <a-tag color="orange" v-if="record.handler.overproofStatus === 10">已处置</a-tag>
          <a-tag color="yellow" v-if="record.handler.overproofStatus === 20">监理已审核</a-tag>
          <a-tag color="red" v-if="record.handler.overproofStatus === 30">监理已驳回</a-tag>
       </span>
        <span slot="tags" slot-scope="tags, record">
        <a-tag color="green" v-if="record.chaobiaodengji === 0 && record.istongji === 1">合格</a-tag>
        <a-tag color="red" v-if="record.chaobiaodengji === 1 && record.istongji === 1">不合格</a-tag>
          <a-tag color="orange" v-if="record.chaobiaodengji === 0 && record.istongji === 0 ">未检测</a-tag>
          <a-tag color="grey" v-if="record.istongji === 40 ">检测异常</a-tag>

       </span>
        <span  slot="pileRealdep" slot-scope="pileRealdep, record" v-if="record.pileRealdep!==null"> {{ record.pileRealdep.substring(0,record.pileRealdep.indexOf(".")+3) }}</span>
        <!--        <span  slot="pileDelectr" slot-scope="pileDelectr, record" v-if="record.pileDelectr!==null"> {{ record.pileDelectr.substring(0,record.pileDelectr.indexOf(".")+3) }}</span>-->
        <!--        <span  slot="pileDspeed" slot-scope="pileDspeed, record" v-if="record.pileDspeed!==null"> {{ record.pileDspeed.substring(0,record.pileDspeed.indexOf(".")+3) }}</span>-->
        <!--        <span  slot="pileUspeed" slot-scope="pileUspeed, record" v-if="record.pileUspeed!==null"> {{ record.pileUspeed.substring(0,record.pileUspeed.indexOf(".")+3) }}</span>-->
        <!--        <span  slot="pileUelectr" slot-scope="pileUelectr, record" v-if="record.pileUelectr!==null"> {{ record.pileUelectr.substring(0,record.pileUelectr.indexOf(".")+3) }}</span>-->
        <span  slot="pileX" slot-scope="pileX, record" v-if="record.pileX!==null"> {{ record.pileX.substring(0,record.pileX.indexOf(".")+3) }}</span>

        <!--        <span  slot="pileUobeton" slot-scope="pileUobeton, record" v-if="record.pileUobeton!==null"> {{ record.pileUobeton.substring(0,record.pileUobeton.indexOf(".")+4) }}</span>-->
        <!--        <span  slot="pileDownbeton" slot-scope="pileDownbeton, record" v-if="record.pileDownbeton!==null"> {{ record.pileDownbeton.substring(0,record.pileDownbeton.indexOf(".")+4) }}</span>-->
        <span  slot="pileCement" slot-scope="pileCement, record" v-if="record.pileCement!==null"> {{ record.pileCement.substring(0,record.pileCement.indexOf(".")+4) }}</span>
        <span slot="sum" slot-scope="text,record">{{ (parseFloat(record.pileDownbeton) + parseFloat(record.pileUobeton)).toFixed(3) }}</span>
        <!--        <div slot="sum2" slot-scope="text,record">-->
        <!--          <span v-if="record.shebeino==='P202204005'">{{ (parseFloat(record.pileCement) / (parseFloat(record.pileRealdep)- 1)).toFixed(3) }}</span>-->
        <!--          <span v-else>{{ (parseFloat(record.pileCement) / (parseFloat(record.pileRealdep)- 1)).toFixed(3) }}</span>-->
        <!--        </div>-->


        <span slot="tags" slot-scope="tags, record">
        <a-tag color="green" v-if="record.chaobiaodengji === 0 && record.istongji === 1 ">合格</a-tag>
        <a-tag color="red" v-if="record.chaobiaodengji === 1 && record.istongji === 1 ">不合格</a-tag>
          <a-tag color="orange" v-if="record.chaobiaodengji === 0 && record.istongji === 0 ">未检测</a-tag>
          <a-tag color="grey" v-if="record.istongji === 40 ">检测异常</a-tag>

       </span>

        <span slot="action" slot-scope="text, record">
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item>
<!--              <a-menu-item>-->
<!--                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">-->
<!--                  <a>删除</a>-->
<!--                </a-popconfirm>-->
<!--              </a-menu-item>-->
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <device-pipepile-historydata-one-modal ref="modalForm" @ok="modalFormOk"></device-pipepile-historydata-one-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import DevicePipepileHistorydataOneModal from './modules/DevicePipepileHistorydataOneModal'
  import JSuperQuery from '@/components/jeecg/JSuperQuery.vue'
  import { usershebeiList } from '@api/api'
  import { handertoken } from '@/mixins/getHanderToken'
  import Vue from 'vue'
  export default {
    name: 'DevicePipepileHistorydataOneList',
    mixins:[JeecgListMixin, mixinDevice,handertoken],
    components: {
      DevicePipepileHistorydataOneModal,
      JSuperQuery,
    },
    data () {
      return {
        dictOption: [],
        selectValue:'',
        description: 'device_pipepile_historydata_one管理页面',
        // 表头
        columns: [
          {
            title: '#',
            dataIndex: '',
            key:'rowIndex',
            width:60,
            align:"center",
            fixed:"left",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
          },
          {
            title:'设备名称',
            align:"center",
            dataIndex: 'shebeino_dictText'
          },
          {
            title:'桩号',
            align:"center",
            dataIndex: 'pileNo'
          },
          {
            title:'施工长度(m)',
            align:"center",
            dataIndex: 'pileRealdep'
          },
          {
            title:'设计桩长',
            align:"center",
            dataIndex: 'pileDesigndep'
          },
          {
            title:'成桩时间',
            align:"center",
            dataIndex: 'pileWorktime'
          },
          {
            title:'最大垂直度(%)',
            align:"center",
            dataIndex: 'pileY'
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
            title:'结束时间',
            align:"center",
            dataIndex: 'pileTime'
          },
          {
            title:'开始时间',
            align:"center",
            dataIndex: 'pileStarttime'
          },
          {
            title:'最大压桩力(兆帕)',
            align:"center",
            dataIndex: 'pileUpress'
          },
          {
            title:'最大夹持力(兆帕)',
            align:"center",
            dataIndex: 'pileDpress'
          },
          {
            title:'平均速度(cm/min)',
            align:"center",
            dataIndex: 'pileSpeed'
          },
          // {
          //   title:'接桩次数',
          //   align:"center",
          //   dataIndex: 'times'
          // },

          {
            title:'数据时间',
            align:"center",
            dataIndex: 'datatime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title: '判定结果',
            align: 'center',
            dataIndex: 'chaobiaodengji',//overLevel_dictText
            key: 'chaobiaodengji',
            scopedSlots: { customRender: 'tags' },
          },
          {
            title:'预警原因',
            align:"center",
            dataIndex: 'ycyy'
          },
          {
            title: '处理状态',
            align: 'center',
            dataIndex: 'handler.overproofStatus',
            key: 'handler.overproofStatus',
            scopedSlots: { customRender: 'tags1' },
          },
          {
            title: '处置人',
            align: 'center',
            dataIndex: 'handler.handlePerson',
            key: 'handler.handlePerson',
          },
          {
            title: '审核人',
            align: 'center',
            dataIndex: 'handler.approvalPerson',
            key: 'handler.approvalPerson'
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
          list: "/devicepipepilehistorydataone/devicePipepileHistorydataOne/cbcxlist",
          delete: "/devicepipepilehistorydataone/devicePipepileHistorydataOne/delete",
          deleteBatch: "/devicepipepilehistorydataone/devicePipepileHistorydataOne/deleteBatch",
          exportXlsUrl: "/devicepipepilehistorydataone/devicePipepileHistorydataOne/exportXls",
          importExcelUrl: "devicepipepilehistorydataone/devicePipepileHistorydataOne/importExcel",

        },
        dictOptions:{},
        superFieldList:[],
      }
    },
    created() {
      this.getToken();
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
          sbtypes:'61'
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
        fieldList.push({type:'string',value:'shebeino',text:'设备编号'})
        fieldList.push({type:'string',value:'pileNo',text:'桩号'})
        fieldList.push({type:'string',value:'pileRealdep',text:'施工长度(m)'})
        fieldList.push({type:'string',value:'pileWorktime',text:'成桩时间'})
        fieldList.push({type:'string',value:'pileY',text:'最大垂直度(%)'})
        fieldList.push({type:'string',value:'pileTime',text:'结束时间'})
        fieldList.push({type:'string',value:'pileStarttime',text:'开始时间'})
        fieldList.push({type:'string',value:'pileUpress',text:'最大压桩力(KN)'})
        fieldList.push({type:'string',value:'pileDpress',text:'最大夹持力(KN)'})
        fieldList.push({type:'string',value:'pileSpeed',text:'平均速度(cm/min)'})
        fieldList.push({type:'int',value:'times',text:'接桩次数'})
        fieldList.push({type:'string',value:'pileDesigndep',text:'设计桩长'})
        fieldList.push({type:'date',value:'datatime',text:'数据时间'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>