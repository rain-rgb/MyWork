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
<!--              {{ selectValue }}-->
            </a-form-item>
          </a-col>          
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="里程">
              <a-input placeholder="请输入里程" v-model="queryParam.mileage" @change="mileage"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="桩号">
              <a-input placeholder="请输入桩号" v-model="queryParam.zhuanghao"  @change="zhuanghao"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
          <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="时间范围">
              <j-date placeholder="开始时间" v-model="queryParam.pileStarttime_begin" :showTime="true"
                      dateFormat="YYYY-MM-DD HH:mm:ss"/>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="">
              <j-date placeholder="结束时间" v-model="queryParam.pileStarttime_end" :showTime="true"
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
<!--    <div class="table-operator">-->
      <!-- <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button> -->
<!--      <a-button type="primary" icon="download" @click="handleExportXls('灌注桩详情')">导出</a-button>-->
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
<!--    </div>-->

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
          <!-- <a @click="handleEdit(record)">编辑</a> -->

          <!-- <a-divider type="vertical" /> -->
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

    <device-mixpile-grouted-one-modal ref="modalForm" @ok="modalFormOk"></device-mixpile-grouted-one-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import DeviceMixpileGroutedOneModal from './modules/DeviceMixpileGroutedOneModal'
  import { usershebeiList } from '@api/api'
  import Vue from 'vue'

  export default {
    name: 'DeviceMixpileGroutedOneList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      DeviceMixpileGroutedOneModal
    },
    data () {
      return {
        description: '灌注桩详情管理页面',
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
            dataIndex: 'sbbh_dictText'
          },
          {
            title:'里程',
            align:"center",
            dataIndex: 'pileMileage'
          },
          {
            title:'桩号',
            align:"center",
            dataIndex: 'pileNo'
          },
          {
            title:'设计深度(m)',
            align:"center",
            dataIndex: 'pileDesigndep'
          },
          {
            title:'实际深度(m)',
            align:"center",
            dataIndex: 'pileRealdep'
          },
          {
            title:'灌浆时长',
            align:"center",
            dataIndex: 'pileWorktime'
          },

          {
            title:'密度（g/m³）',
            align:"center",
            dataIndex: 'pileDensity'
          },
          {
            title:'开始时间',
            align:"center",
            dataIndex: 'pileStarttime',
          },
          {
            title:'结束时间',
            align:"center",
            dataIndex: 'pileTime',

          },

          // {
          //   title:'alertstate',
          //   align:"center",
          //   dataIndex: 'alertstate'
          // },
          // {
          //   title:'超标 0合格，1不合格',
          //   align:"center",
          //   dataIndex: 'chaobiaodengji'
          // },
          // {
          //   title:'超标原因',
          //   align:"center",
          //   dataIndex: 'problem'
          // },
          {
            title:'倾角X',
            align:"center",
            dataIndex: 'pileX'
          },
          {
            title:'倾角Y',
            align:"center",
            dataIndex: 'pileY'
          },
          // {
          //   title:'桩经度',
          //   align:"center",
          //   dataIndex: 'pileLgd'
          // },
          // {
          //   title:'桩经度',
          //   align:"center",
          //   dataIndex: 'pileLtd'
          // },

          // {
          //   title:'唯一码',
          //   align:"center",
          //   dataIndex: 'uuid'
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
          list: "/grouted/deviceMixpileGroutedOne/list",
          delete: "/grouted/deviceMixpileGroutedOne/delete",
          deleteBatch: "/grouted/deviceMixpileGroutedOne/deleteBatch",
          exportXlsUrl: "/grouted/deviceMixpileGroutedOne/exportXls",
          importExcelUrl: "grouted/deviceMixpileGroutedOne/importExcel",
          
        },
        dictOption: [],
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
      mileage:function () {
        this.queryParam.pileMileage = '*'+this.queryParam.mileage+'*'
      },
      zhuanghao:function () {
        this.queryParam.pileNo = '*'+this.queryParam.zhuanghao+'*'
      },
      shebeiList:function (){
        var sys_depart_orgcode=Vue.ls.get('SYS_DEPART_ORGCODE');
        usershebeiList({
          sys_depart_orgcode:sys_depart_orgcode,
          sbtypes:'81'
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
        fieldList.push({type:'string',value:'sbbh',text:'设备编号'})
        fieldList.push({type:'string',value:'pileMileage',text:'里程'})
        fieldList.push({type:'int',value:'pileNo',text:'桩号'})
        fieldList.push({type:'string',value:'pileDesigndep',text:'设计深度'})
        fieldList.push({type:'string',value:'pileRealdep',text:'实际灌浆深度'})
        fieldList.push({type:'string',value:'pileWorktime',text:'灌浆时长'})
        fieldList.push({type:'date',value:'pileTime',text:'成桩时间'})
        fieldList.push({type:'string',value:'pileDensity',text:'密度（水泥浆比重）'})
        fieldList.push({type:'string',value:'handlestate',text:'处置状态'})
        fieldList.push({type:'date',value:'pileStarttime',text:'开始时间'})
        fieldList.push({type:'string',value:'alertstate',text:'alertstate'})
        fieldList.push({type:'string',value:'chaobiaodengji',text:'超标 0合格，1不合格'})
        fieldList.push({type:'string',value:'problem',text:'超标原因'})
        fieldList.push({type:'string',value:'pileX',text:'倾角-x'})
        fieldList.push({type:'string',value:'pileY',text:'倾角-y'})
        fieldList.push({type:'string',value:'pileLgd',text:'桩经度'})
        fieldList.push({type:'string',value:'pileLtd',text:'桩经度'})
        fieldList.push({type:'string',value:'ts',text:'ts'})
        fieldList.push({type:'string',value:'uuid',text:'唯一码'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>