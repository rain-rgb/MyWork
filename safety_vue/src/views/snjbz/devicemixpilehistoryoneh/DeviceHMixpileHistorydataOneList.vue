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
            <a-form-item label="时间范围">
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
      <a-button @click="handleAdd" v-has="'mixpilehistoryoneh:add'" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" v-has="'mixpilehistoryone:dc'" @click="handleExportXls2h('搅拌桩成桩记录信息表')">导出</a-button>
      <a-upload name="file"  :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" v-has="'mixpilehistoryone:dr'" icon="import">导入</a-button>
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

        <span  slot="pileRealdep" slot-scope="pileRealdep, record" v-if="record.pileRealdep!==null"> {{ record.pileRealdep.substring(0,record.pileRealdep.indexOf(".")+3) }}</span>
        <span  slot="pileDelectr" slot-scope="pileDelectr, record" v-if="record.pileDelectr!==null"> {{ record.pileDelectr.substring(0,record.pileDelectr.indexOf(".")+3) }}</span>
        <span  slot="pileDpress" slot-scope="pileDpress, record" v-if="record.pileDpress!==null"> {{ record.pileDpress.substring(0,record.pileDpress.indexOf(".")+3) }}</span>
        <span  slot="pileDspeed" slot-scope="pileDspeed, record" v-if="record.pileDspeed!==null"> {{ record.pileDspeed.substring(0,record.pileDspeed.indexOf(".")+3) }}</span>
        <span  slot="pileUspeed" slot-scope="pileUspeed, record" v-if="record.pileUspeed!==null"> {{ record.pileUspeed.substring(0,record.pileUspeed.indexOf(".")+3) }}</span>
        <span  slot="pileUelectr" slot-scope="pileUelectr, record" v-if="record.pileUelectr!==null"> {{ record.pileUelectr.substring(0,record.pileUelectr.indexOf(".")+3) }}</span>
        <span  slot="pileUpress" slot-scope="pileUpress, record" v-if="record.pileUpress!==null"> {{ record.pileUpress.substring(0,record.pileUpress.indexOf(".")+3) }}</span>
        <span  slot="pileX" slot-scope="pileX, record" v-if="record.pileX!==null"> {{ record.pileX.substring(0,record.pileX.indexOf(".")+3) }}</span>

        <span  slot="pileUobeton" slot-scope="pileUobeton, record" v-if="record.pileUobeton!==null"> {{ record.pileUobeton.substring(0,record.pileUobeton.indexOf(".")+4) }}</span>
        <span  slot="pileDownbeton" slot-scope="pileDownbeton, record" v-if="record.pileDownbeton!==null"> {{ record.pileDownbeton.substring(0,record.pileDownbeton.indexOf(".")+4) }}</span>
        <span  slot="pileCement" slot-scope="pileCement, record" v-if="record.pileCement!==null"> {{ record.pileCement.substring(0,record.pileCement.indexOf(".")+4) }}</span>


        <span slot="tags" slot-scope="tags, record">
        <a-tag color="green" v-if="record.chaobiaodengji === 0 && record.alertstate !== 40 ">合格</a-tag>
        <a-tag color="red" v-if="record.chaobiaodengji === 1 && record.alertstate !== 40 ">不合格</a-tag>
          <a-tag color="orange" v-if="record.chaobiaodengji === null && record.alertstate !== 40 ">未检测</a-tag>
          <a-tag color="grey" v-if="record.alertstate === 40 ">检测异常</a-tag>

       </span>

        <span slot="sum" slot-scope="text,record">{{ (parseFloat(record.pileDownbeton) + parseFloat(record.pileUobeton)).toFixed(3) }}</span>

        <span slot="action" slot-scope="text, record">
          <a v-has="'mixpilehistoryoneh:edit'" @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item>
              <a-menu-item>
                <a-popconfirm v-has="'mixpilehistoryoneh:del'" title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <device-mixpile-historydata-one-modal ref="modalForm" @ok="modalFormOk"></device-mixpile-historydata-one-modal>
  </a-card>
</template>

<script>

  import '@assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import DeviceMixpileHistorydataOneModal from '../devicemixpilehistorydataone/modules/DeviceMixpileHistorydataOneModal'
  import JSuperQuery from '@comp/jeecg/JSuperQuery.vue'
  import { usershebeiList } from '@api/api'
  import Vue from 'vue'
  import { handertoken } from '@/mixins/getHanderToken'
  export default {
    name: 'DeviceMixpileHistorydataOneList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      DeviceMixpileHistorydataOneModal,
      JSuperQuery,
    },
    data () {
      return {
        dictOption: [],
        selectValue:'',
        description: 'device_mixpile_historydata_one管理页面',
        // 表头
        columns: [
          {
            title: '序号',
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
            fixed:"left",
            dataIndex: 'shebeino_dictText'
          },
          {
            title:'单桩编号',
            align:"center",
            fixed:"left",
            dataIndex: 'pileNo'
          },
          {
            title:'设计桩长(m)',
            align:"center",
            dataIndex: 'pileDesigndep'
          },
          {
            title:'实际桩长(m)',
            align:"center",
            dataIndex: 'pileRealdep',
            scopedSlots: { customRender: 'pileRealdep' }
          },
          {
            title:'里程桩号',
            align:"center",
            dataIndex: 'pileMileage'
          },
          {
            title:'喷浆时长(S)',
            align:"center",
            dataIndex: 'pileWorktime'
          },
          {
            title:'下钻时长(S)',
            align:"center",
            dataIndex: 'pileDowntime'
          },
          {
            title:'下钻平均速度(m/min)',
            align:"center",
            dataIndex: 'pileDspeed',
            scopedSlots: { customRender: 'pileDspeed' }
          },
          {
            title:'下钻平均电流(A)',
            align:"center",
            dataIndex: 'pileDelectr',
            scopedSlots: { customRender: 'pileDelectr' }
          },
          {
            title:'下钻浆量(L)',
            align:"center",
            dataIndex: 'pileDownbeton',
            scopedSlots: { customRender: 'pileDownbeton' }
          },
          {
            title:'下钻平均压力(MPa)',
            align:"center",
            dataIndex: 'pileDpress',
            scopedSlots: { customRender: 'pileDpress' }
          },
          {
            title:'提钻时长(S)',
            align:"center",
            dataIndex: 'pileUptime'
          },
          {
            title:'提钻平均速度(m/min)',
            align:"center",
            dataIndex: 'pileUspeed',
            scopedSlots: { customRender: 'pileUspeed' }
          },
          {
            title:'提钻平均电流(A)',
            align:"center",
            dataIndex: 'pileUelectr',
            scopedSlots: { customRender: 'pileUelectr' }
          },
          {
            title:'提钻浆量(L)',
            align:"center",
            dataIndex: 'pileUobeton',
            scopedSlots: { customRender: 'pileUobeton' }
          },
          {
            title:'提钻平均压力(MPa)',
            align:"center",
            dataIndex: 'pileUpress',
            scopedSlots: { customRender: 'pileUpress' }
          },
          {
            title: '桩号注浆量(L)',
            dataIndex: 'sum',
            align:"center",
            scopedSlots: { customRender: 'sum' }
          },
          {
            title:'水泥浆比重',
            align:"center",
            dataIndex: 'pileDensity'
          },
          {
            title:'水泥用量(KG)',
            align:"center",
            dataIndex: 'pileCement',
            scopedSlots: { customRender: 'pileCement' }

          },
          {
            title:'水灰比(%)',
            align:"center",
            dataIndex: 'pileRatio'
          },

          {
            title:'倾角',
            align:"center",
            dataIndex: 'pileX',
            scopedSlots: { customRender: 'pileX' }
          },
          // {
          //   title:'纬度',
          //   align:"center",
          //   dataIndex: 'pileLtd'
          // },

          {
            title:'成桩时间',
            align:"center",
            dataIndex: 'pileTime'
          },
          {
            title: '判定结果',
            align: 'center',
            dataIndex: 'chaobiaodengji',//overLevel_dictText
            key: 'chaobiaodengji',
            scopedSlots: { customRender: 'tags' },
          },

          // {
          //   title:'操作员',
          //   align:"center",
          //   dataIndex: 'username'
          // },
          // {
          //   title:'数据时间',
          //   align:"center",
          //   dataIndex: 'datatime',
          // },
          // {
          //   title:'瞬时流量',
          //   align:"center",
          //   dataIndex: 'flowlst'
          // },
          // {
          //   title:'开始时间',
          //   align:"center",
          //   dataIndex: 'starttime',
          // },
          // {
          //   title:'结束时间',
          //   align:"center",
          //   dataIndex: 'endtime',
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
          list: "/deviceMixpileHistorydataOne/deviceMixpileHistorydataOne/hlist",
          delete: "/deviceMixpileHistorydataOne/deviceMixpileHistorydataOne/delete",
          deleteBatch: "/deviceMixpileHistorydataOne/deviceMixpileHistorydataOne/deleteBatch",
          exportXlsUrl: "jmreport/view/635734078082105344",
          importExcelUrl: "deviceMixpileHistorydataOne/deviceMixpileHistorydataOne/importExcel",
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
          sbtypes:'19,54'
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
        fieldList.push({type:'string',value:'designdep',text:'设计深度'})
        fieldList.push({type:'string',value:'curdep',text:'深度'})
        fieldList.push({type:'string',value:'x',text:'x轴角度'})
        fieldList.push({type:'string',value:'y',text:'y轴角度'})
        fieldList.push({type:'string',value:'elec',text:'电流'})
        fieldList.push({type:'string',value:'grouting',text:'灌浆量'})
        fieldList.push({type:'string',value:'pileno',text:'桩号'})
        fieldList.push({type:'string',value:'designratio',text:'设计水灰比'})
        fieldList.push({type:'string',value:'designgrout',text:'设计灌浆量'})
        fieldList.push({type:'string',value:'cement',text:'水泥量'})
        fieldList.push({type:'string',value:'water',text:'用水量'})
        fieldList.push({type:'string',value:'ratio',text:'水灰比'})
        fieldList.push({type:'string',value:'serial',text:'盘次'})
        fieldList.push({type:'string',value:'username',text:'操作员'})
        fieldList.push({type:'date',value:'datatime',text:'数据时间'})
        fieldList.push({type:'string',value:'ltdfloat',text:'经度'})
        fieldList.push({type:'string',value:'lgdfloat',text:'纬度'})
        fieldList.push({type:'string',value:'flowlst',text:'瞬时流量'})
        fieldList.push({type:'string',value:'speed',text:'速度'})
        fieldList.push({type:'date',value:'starttime',text:'开始时间'})
        fieldList.push({type:'date',value:'endtime',text:'结束时间'})
        fieldList.push({type:'string',value:'leflowlst',text:'累计流量'})
        fieldList.push({type:'int',value:'pjtime',text:'喷浆时长'})
        fieldList.push({type:'string',value:'xzjl',text:'下钻浆量'})
        fieldList.push({type:'string',value:'tzjl',text:'抬钻浆量'})
        fieldList.push({type:'string',value:'xztime',text:'下钻时长'})
        fieldList.push({type:'string',value:'tztime',text:'抬钻时长'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>