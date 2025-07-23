<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="设备名称">
              <j-search-select-tag placeholder="请选择设备名称 " v-model="queryParam.shebeino" :dictOptions="dictOption" >
              </j-search-select-tag>
              <!--              {{ selectValue }}-->
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="料仓名称">
              <a-input placeholder="请输入料仓名称" v-model="queryParam.liaocangname1" @change="mileage"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="材料规格">
              <a-input placeholder="请输入材料规格" v-model="queryParam.cailiaoguige1"  @change="cailiaoguige"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="24">

          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="时间范围">
              <j-date placeholder="开始时间" v-model="queryParam.jinchangshijian_begin" :showTime="true" dateFormat="YYYY-MM-DD" />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="">
              <j-date placeholder="结束时间" v-model="queryParam.jinchangshijian_end" :showTime="true" dateFormat="YYYY-MM-DD" />
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
<!--      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>-->
<!--      <a-button type="primary" icon="download" @click="handleExportXls('wzyclpidaichen')">导出</a-button>-->
<!--      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">-->
<!--        <a-button type="primary" icon="import">导入</a-button>-->
<!--      </a-upload>-->
<!--      &lt;!&ndash; 高级查询区域 &ndash;&gt;-->
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

    <wzyclpidaichen-modal ref="modalForm" @ok="modalFormOk"></wzyclpidaichen-modal>
  </a-card>
</template>

<script>

  import '@assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import WzyclpidaichenModal from './modules/WzyclpidaichenModal'
  import Vue from "vue";
  import {usershebeiList} from "@api/api";

  export default {
    name: 'WzyclpidaichenList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      WzyclpidaichenModal
    },
    data () {
      return {
        description: 'wzyclpidaichen管理页面',
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
          // {
          //   title:'材料编号',
          //   align:"center",
          //   dataIndex: 'cailiaono'
          // },
          {
            title:'材料名称',
            align:"center",
            dataIndex: 'cailiaoname'
          },
          {
            title:'材料规格',
            align:"center",
            dataIndex: 'cailiaoguige'
          },

          {
            title:'净重(吨)',
            align:"center",
            dataIndex: 'jingzhong'
          },

          // {
          //   title:'remark',
          //   align:"center",
          //   dataIndex: 'remark'
          // },
          {
            title:'设备名称',
            align:"center",
            dataIndex: 'shebeibianhao_dictText'
          },

          // {
          //   title:'料仓编号',
          //   align:"center",
          //   dataIndex: 'lcbianhao'
          // },
          {
            title:'料仓名称',
            align:"center",
            dataIndex: 'liaocangname'
          },
          // {
          //   title:'status',
          //   align:"center",
          //   dataIndex: 'status'
          // },
          // {
          //   title:'是否统计 默认为0:未统计，1:已统计,15:统计出错',
          //   align:"center",
          //   dataIndex: 'istongji'
          // },
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
          // {
          //   title: '操作',
          //   dataIndex: 'action',
          //   align:"center",
          //   fixed:"right",
          //   width:147,
          //   scopedSlots: { customRender: 'action' }
          // }
        ],
        url: {
          list: "/wzyclpidaichen/wzyclpidaichen/list",
          delete: "/wzyclpidaichen/wzyclpidaichen/delete",
          deleteBatch: "/wzyclpidaichen/wzyclpidaichen/deleteBatch",
          exportXlsUrl: "/wzyclpidaichen/wzyclpidaichen/exportXls",
          importExcelUrl: "wzyclpidaichen/wzyclpidaichen/importExcel",
          
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
        this.queryParam.liaocangname = '*'+this.queryParam.liaocangname1+'*'
      },
      zhuanghao:function () {
        this.queryParam.cailiaoguige = '*'+this.queryParam.cailiaoguige1+'*'
      },
      shebeiList:function (){
        var sys_depart_orgcode=Vue.ls.get('SYS_DEPART_ORGCODE');
        usershebeiList({
          sys_depart_orgcode:sys_depart_orgcode,
          sbtypes:'80'
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
        fieldList.push({type:'string',value:'jinchuliaodanno',text:'进出材料单'})
        fieldList.push({type:'string',value:'cailiaono',text:'材料编号'})
        fieldList.push({type:'string',value:'jinchangshijian',text:'进场时间'})
        fieldList.push({type:'string',value:'chuchangshijian',text:'出场时间'})
        fieldList.push({type:'string',value:'jingzhong',text:'净重'})
        fieldList.push({type:'string',value:'cailiaoguige',text:'材料规格'})
        fieldList.push({type:'string',value:'remark',text:'remark'})
        fieldList.push({type:'string',value:'shebeibianhao',text:'设备编号'})
        fieldList.push({type:'string',value:'cailiaoname',text:'材料名称'})
        fieldList.push({type:'string',value:'lcbianhao',text:'料仓编号'})
        fieldList.push({type:'string',value:'liaocangname',text:'料仓名称'})
        fieldList.push({type:'string',value:'guid',text:'唯一标识'})
        fieldList.push({type:'int',value:'ts',text:'时间戳'})
        fieldList.push({type:'string',value:'isdel',text:'是否删除'})
        fieldList.push({type:'string',value:'status',text:'status'})
        fieldList.push({type:'int',value:'istongji',text:'是否统计 默认为0:未统计，1:已统计,15:统计出错'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>