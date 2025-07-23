<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="设备名称">
              <j-search-select-tag placeholder="请选择设备名称" v-model="queryParam.shebeibanhao" :dictOptions="dictOption" >
              </j-search-select-tag>
              {{ selectValue }}
            </a-form-item>
          </a-col>
<!--          <a-col :xl="6" :lg="7" :md="8" :sm="24">-->
<!--            <a-form-item label="状态">-->
<!--              <j-dict-select-tag placeholder="请选择" v-model="queryParam.isUse" dictCode="is_use"></j-dict-select-tag>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :xl="6" :lg="7" :md="8" :sm="24">-->
<!--            <a-form-item label="切面名称">-->
<!--              <a-input placeholder="请输入切面名称" v-model="queryParam.sectionName"></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :xl="6" :lg="7" :md="8" :sm="24">-->
<!--            <a-form-item label="时间范围">-->
<!--              <j-date placeholder="开始测量时间" v-model="queryParam.birthTime_begin" :showTime="true" dateFormat="YYYY-MM-DD HH:mm:ss" />-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :xl="6" :lg="7" :md="8" :sm="24">-->
<!--            <a-form-item label="">-->
<!--              <j-date placeholder="结束测量时间" v-model="queryParam.birthTime_end" :showTime="true" dateFormat="YYYY-MM-DD HH:mm:ss" />-->
<!--            </a-form-item>-->
<!--          </a-col>-->

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
      <a-button v-has="'gualan:add'" @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" v-has="'gualan:dc'" icon="download" @click="handleExportXls('挂篮数据主表')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" v-has="'gualan:dr'" icon="import">导入</a-button>
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
       <span slot="windspeedwarn" slot-scope="windspeedwarn, record">
         <a-tag color="green" v-if="record.windspeedwarn === 0">正常</a-tag>
         <a-tag color="red" v-if="record.windspeedwarn === 1">报警</a-tag>
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
          <a v-has="'gualan:edit'" @click="handleEdit(record)">编辑</a>

<!--          <a-divider type="vertical" />-->
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item>
              <a-menu-item v-has="'gualan:del'" >
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <gualan-base-modal ref="modalForm" @ok="modalFormOk"></gualan-base-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import GualanBaseModal from './modules/GualanBaseModal'
  import JSuperQuery from '@/components/jeecg/JSuperQuery.vue'
  import { handertoken } from '@/mixins/getHanderToken'
  import { SYS_DEPART_ORGCODE } from '@/store/mutation-types'
  import { usershebeiList } from '@api/api'
  import Vue from 'vue'

  export default {
    name: 'GualanBaseList',
    mixins:[JeecgListMixin, mixinDevice,handertoken],
    components: {
      GualanBaseModal,
      JSuperQuery,
    },
    data () {
      return {
        selectValue: '',
        asyncSelectValue: '',
        dictOption: [],
        description: '挂篮数据主表管理页面',
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
            dataIndex: 'shebeibanhao_dictText'
          },
          // {
          //   title:'数据类标志',
          //   align:"center",
          //   dataIndex: 'dataType'
          // },
          {
            title:'1#螺纹钢拉力',
            align:"center",
            dataIndex: 'tension1'
          },
          {
            title:'2#螺纹钢拉力',
            align:"center",
            dataIndex: 'tension2'
          },
          {
            title:'3#螺纹钢拉力',
            align:"center",
            dataIndex: 'tension3'
          },
          {
            title:'4#螺纹钢拉力',
            align:"center",
            dataIndex: 'tension4'
          },
          {
            title:'下支撑横梁水平度1',
            align:"center",
            dataIndex: 'levelness1'
          },
          {
            title:'下支撑横梁水平度2',
            align:"center",
            dataIndex: 'levelness2'
          },
          {
            title:'1#螺纹钢是否超重 ',
            align:"center",
            dataIndex: 'overweight1_dictText'
          },
          {
            title:'2#螺纹钢是否超重 ',
            align:"center",
            dataIndex: 'overweight2_dictText'
          },
          {
            title:'3#螺纹钢是否超重 ',
            align:"center",
            dataIndex: 'overweight3_dictText'
          },
          {
            title:'4#螺纹钢是否超重 ',
            align:"center",
            dataIndex: 'overweight4_dictText'
          },
          {
            title:'挂篮前端下沉量（mm）',
            align:"center",
            dataIndex: 'basket'
          },
          {
            title:'风速（m/s）',
            align:"center",
            dataIndex: 'windspeed'
          },
          {
            title:'风速报警',
            align:"center",
            dataIndex: 'windspeedwarn_dictText',
            key: 'windspeedwarn',
            scopedSlots: {customRender: 'windspeedwarn'},
          },
          {
            title:'下托梁有效长度',
            align:"center",
            dataIndex: 'reservedOne'
          },
          // {
          //   title:'预留2',
          //   align:"center",
          //   dataIndex: 'reservedTwo'
          // },
          // {
          //   title:'段结束符',
          //   align:"center",
          //   dataIndex: 'sf'
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
          list: "/gualan/gualanBase/list",
          delete: "/gualan/gualanBase/delete",
          deleteBatch: "/gualan/gualanBase/deleteBatch",
          exportXlsUrl: "/gualan/gualanBase/exportXls",
          importExcelUrl: "gualan/gualanBase/importExcel",

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
          sbtypes:'22'
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
        fieldList.push({type:'string',value:'shebeibanhao',text:'设备编号',dictCode:''})
        fieldList.push({type:'int',value:'dataType',text:'数据类标志',dictCode:''})
        fieldList.push({type:'int',value:'tension1',text:'1#螺纹钢拉力(放大100倍单位T)(实际显示为30.00T)',dictCode:''})
        fieldList.push({type:'int',value:'tension2',text:'2#螺纹钢拉力(放大100倍单位T)(实际显示为30.00T)',dictCode:''})
        fieldList.push({type:'int',value:'tension3',text:'3#螺纹钢拉力(放大100倍单位T)(实际显示为30.00T)',dictCode:''})
        fieldList.push({type:'int',value:'tension4',text:'4#螺纹钢拉力(放大100倍单位T)(实际显示为30.00T)',dictCode:''})
        fieldList.push({type:'int',value:'levelness1',text:'下支撑横梁水平度（放大10倍单位度）(实际显示为1.0度)',dictCode:''})
        fieldList.push({type:'int',value:'levelness2',text:'下支撑横梁水平度（放大10倍单位度）(实际显示为1.0度)',dictCode:''})
        fieldList.push({type:'int',value:'overweight1',text:'1#螺纹钢是否超重  0  正常  1  超重',dictCode:''})
        fieldList.push({type:'int',value:'overweight2',text:'2#螺纹钢是否超重 0  正常  1  超重',dictCode:''})
        fieldList.push({type:'int',value:'overweight3',text:'3#螺纹钢是否超重 0  正常  1  超重',dictCode:''})
        fieldList.push({type:'int',value:'overweight4',text:'4#螺纹钢是否超重 0  正常 1  超重',dictCode:''})
        fieldList.push({type:'int',value:'basket',text:'挂篮前端下沉量（放大10倍单位mm）',dictCode:''})
        fieldList.push({type:'int',value:'windspeed',text:'风速（放大10倍 单位m/s）',dictCode:''})
        fieldList.push({type:'int',value:'windspeedwarn',text:'风速报警0---正常  1---报警',dictCode:''})
        fieldList.push({type:'int',value:'reservedOne',text:'预留',dictCode:''})
        fieldList.push({type:'int',value:'reservedTwo',text:'预留',dictCode:''})
        fieldList.push({type:'int',value:'sf',text:'段结束符',dictCode:''})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>