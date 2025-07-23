<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="设备名称">
              <j-search-select-tag placeholder="请选择设备名称" v-model="queryParam.fsbbh" :dictOptions="dictOption" >
              </j-search-select-tag>
              {{ selectValue }}
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="是否合格">
              <j-dict-select-tag placeholder="请选择是否合格" v-model="queryParam.isqualified"
                                 dictCode="over_level"></j-dict-select-tag>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="时间范围">
              <j-date placeholder="开始时间范围" v-model="queryParam.submittime_begin" :showTime="true"
                      dateFormat="YYYY-MM-DD HH:mm:ss"/>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="">
              <j-date placeholder="结束时间范围" v-model="queryParam.submittime_end" :showTime="true"
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
        <a-tag color="green" v-if="record.isqualified == '0'">合格</a-tag>
        <a-tag color="orange" v-if="record.isqualified == '1'">初级超标</a-tag>
        <a-tag color="yellow" v-if="record.isqualified == '2'">中级超标</a-tag>
         <a-tag color="red" v-if="record.isqualified == '3'">高级超标</a-tag>
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

    <wending-d-modal ref="modalForm" @ok="modalFormOk"></wending-d-modal>
  </a-card>
</template>

<script>

import '@/assets/less/TableExpand.less'
import { mixinDevice } from '@/utils/mixin'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import WendingDModal from './modules/WendingDModal'
import JSuperQuery from '@/components/jeecg/JSuperQuery.vue'
import { SYS_DEPART_ORGCODE } from '@/store/mutation-types'
import { usershebeiList } from '@api/api'
import Vue from 'vue'
import { handertoken } from '@/mixins/getHanderToken'

export default {
  name: 'WendingDList',
  mixins:[JeecgListMixin, mixinDevice, handertoken],
  components: {
    WendingDModal,
    JSuperQuery,
  },
  data () {
    return {
      selectValue: '',
      asyncSelectValue: '',
      dictOption: [],
      description: 'w_wendingdu_m管理页面',
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
          title:'马歇尔设备名称',
          align:"center",
          dataIndex: 'fsbbh_dictText'
        },
        {
          title:'采集时间',
          align:"center",
          dataIndex: 'isTesttime'
        },
        {
          title:'工程名称',
          align:"center",
          dataIndex: 'projectname'
        },
        {
          title:'样品编号',
          align:"center",
          dataIndex: 'sampleno'
        },

        {
          title:'工程部位',
          align:"center",
          dataIndex: 'gcbuwei'
        },
        {
          title:'样品名称',
          align:"center",
          dataIndex: 'samplename'
        },
        {
          title:'样品描述',
          align:"center",
          dataIndex: 'samplems'
        },
        {
          title: '流值标准上限值(mm)',
          align: "center",
          dataIndex: 'biaozhunzhi1',
        },
        {
          title: '流值标准下限值(mm)',
          align: "center",
          dataIndex: 'biaozhunzhi2',
        },
        {
          title:'流值平均值(mm)',
          align:"center",
          dataIndex: 'liuzhiavg'
        },
        {
          title:'稳定度标准值(KN)',
          align:"center",
          dataIndex: 'biaozhunzhi3'
        },
        {
          title:'稳定度平均值(KN)',
          align:"center",
          dataIndex: 'wddavg'
        },
        {
          title: '是否合格',
          align: 'center',
          dataIndex: 'isqualified_dictText',//overLevel_dictText
          key: 'isqualified_dictText',
          scopedSlots: { customRender: 'tags' },
        },
        {
          title:'审核结果',
          align:"center",
          dataIndex: 'biaozhunzhi3'
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
        list: "/mxe/wendingD/list",
        delete: "/mxe/wendingD/delete",
        deleteBatch: "/mxe/wendingD/deleteBatch",
        exportXlsUrl: "/mxe/wendingD/exportXls",
        importExcelUrl: "mxe/wendingD/importExcel",

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
        sbtypes:'8'
      }).then(res=>{
        // this.dictOption=[];
        let result=res.result;
        console.log(result)
        result.forEach(re=>{
          this.dictOption.push({text:re.sbname,value:re.sbjno})
          // console.log('+++++++++++'+re.sbname)
        })
        //console.log(res)
      });
    },
    initDictConfig(){
    },
    getSuperFieldList(){
      let fieldList=[];
      fieldList.push({type:'string',value:'syjid',text:'syjid'})
      fieldList.push({type:'string',value:'isEnd',text:'isEnd'})
      fieldList.push({type:'string',value:'isTesttime',text:'isTesttime'})
      fieldList.push({type:'string',value:'fSbbh',text:'fSbbh'})
      fieldList.push({type:'string',value:'projectname',text:'projectname'})
      fieldList.push({type:'string',value:'sampleno',text:'sampleno'})
      fieldList.push({type:'string',value:'gcbuwei',text:'gcbuwei'})
      fieldList.push({type:'string',value:'samplename',text:'samplename'})
      fieldList.push({type:'string',value:'samplems',text:'samplems'})
      fieldList.push({type:'number',value:'liuzhiavg',text:'liuzhiavg'})
      fieldList.push({type:'number',value:'wddavg',text:'wddavg'})
      fieldList.push({type:'number',value:'biaozhunzhi1',text:'biaozhunzhi1'})
      fieldList.push({type:'number',value:'biaozhunzhi2',text:'biaozhunzhi2'})
      fieldList.push({type:'number',value:'biaozhunzhi3',text:'biaozhunzhi3'})
      fieldList.push({type:'string',value:'isqualified',text:'isqualified'})
      fieldList.push({type:'int',value:'status',text:'status'})
      fieldList.push({type:'int',value:'groupnum',text:'groupnum'})
      fieldList.push({type:'string',value:'submittime',text:'submittime'})
      fieldList.push({type:'string',value:'shjieguo',text:'shjieguo'})
      this.superFieldList = fieldList
    }
  }
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>