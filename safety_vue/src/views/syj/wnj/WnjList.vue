<template>
  <a-card :bordered="false">
       <a-row :gutter="24">
      <a-col :sm="24" :md="12" :xl="4" :style="{ marginBottom: '24px' }"><span style="font-size: 22px;font-weight: bold">万能机预警：{{wnjAllWarn}}</span></a-col>
      <a-col :sm="24" :md="12" :xl="4" :style="{ marginBottom: '24px' }"><span style="font-size: 22px;font-weight: bold">本月预警：{{wnjMonthWarn}}</span></a-col>
    </a-row>
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="设备名称">
              <j-search-select-tag placeholder="请选择设备名称" v-model="queryParam.sbbh" :dictOptions="dictOption" >
              </j-search-select-tag>
              {{ selectValue }}
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="判定结果">
              <j-dict-select-tag placeholder="请选择判定结果" v-model="queryParam.pdjg"
                                 dictCode="pdjg"></j-dict-select-tag>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="试验日期范围">
              <j-date placeholder="开始试验日期" v-model="queryParam.syrq_begin" :showTime="true"
                      dateFormat="YYYY-MM-DD HH:mm:ss"/>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="">
              <j-date placeholder="结束试验日期" v-model="queryParam.syrq_end" :showTime="true"
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
      <a-button @click="handleAdd" v-has="'syj:add'" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" v-has="'syj:dc'" @click="handleExportXls('水稳主表')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl"
                @change="handleImportExcel">
        <a-button type="primary" v-has="'syj:dr'" icon="import">导入</a-button>
      </a-upload>
      <!-- 高级查询区域 -->
      <!--      <j-super-query :fieldList="superFieldList" ref="superQueryModal"-->
      <!--                     @handleSuperQuery="handleSuperQuery"></j-super-query>-->
      <!--      <a-dropdown v-if="selectedRowKeys.length > 0">-->
      <!--        <a-menu slot="overlay">-->
      <!--          <a-menu-item key="1" @click="batchDel">-->
      <!--            <a-icon type="delete"/>-->
      <!--            删除-->
      <!--          </a-menu-item>-->
      <!--        </a-menu>-->
      <!--        <a-button style="margin-left: 8px"> 批量操作-->
      <!--          <a-icon type="down"/>-->
      <!--        </a-button>-->
      <!--      </a-dropdown>-->
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a
        style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table
        ref="table"
        size="middle"
        bordered
        rowKey="syjid"
        class="j-table-force-nowrap"
        :scroll="{x:true}"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        @change="handleTableChange"
        :customRow="handleClick"
        :rowClassName="setRowClassName"
      >
         <span slot="tags" slot-scope="tags, record">
        <a-tag color="green" v-if="record.pdjg == '合格'">合格</a-tag>
         <a-tag color="red" v-if="record.pdjg == '不合格'">不合格</a-tag>
            <a-tag color="green" v-if="record.pdjg == '有效'">合格</a-tag>
         <a-tag color="red" v-if="record.pdjg == '无效'">不合格</a-tag>
       </span>

        <span slot="tag1" slot-scope="tag1, record">
         <span  v-if="record.sylx == '100047' || record.sylx == '100048' || record.sylx == '100049' ">{{record.pzbm}}</span>
         <span v-else >{{record.qddbz}}</span>
       </span>

        <span slot="tag2" slot-scope="tag2, record">
         <span  v-if="record.sylx == '100047' || record.sylx == '100048' || record.sylx == '100049' ">{{record.gczj}}</span>
         <span v-else >{{record.sjcc}}</span>
       </span>

        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <template slot="imgSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无图片</span>
          <img v-else :src="getImgView(text)" height="25px" alt=""
               style="max-width:80px;font-size: 12px;font-style: italic;"/>
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
          <a  v-has="'syj:edit'" @click="handleEdit(record)">编辑</a>

<!--          <a-divider type="vertical"/>-->
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down"/></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item>
              <a-menu-item v-has="'syj:del'">
                <a-popconfirm  title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <WnjModal ref="modalForm" @ok="modalFormOk"/>
  </a-card>
</template>

<script>

  import {JeecgListMixin} from '@/mixins/JeecgListMixin'
  import JDictSelectTag from '@/components/dict/JDictSelectTag.vue'
  import '@/assets/less/TableExpand.less'
  import JSuperQuery from '@/components/jeecg/JSuperQuery.vue'
  import WnjModal from './moduls/WnjModal'
  import { handertoken } from '@/mixins/getHanderToken'
  import Vue from 'vue'
  import { SYS_DEPART_ORGCODE } from '@/store/mutation-types'
  import { usershebeiList } from '@api/api'
import { getAction } from '@api/manage'

  export default {
    name: "WnjList",
    mixins: [JeecgListMixin,handertoken],
    components: {
      JDictSelectTag,
      WnjModal,
      JSuperQuery
    },
    data() {
      return {
        wnjAllWarn:'',
        wnjMonthWarn:'',
        selectValue: '',
        asyncSelectValue: '',
        dictOption: [],
        description: '试验机主表管理页面',
        // 表头
        columns: [
          {
            title: '序号',
            dataIndex: '',
            key: 'rowIndex',
            width: 60,
            align: "center",
            customRender: function (t, r, index) {
              return parseInt(index) + 1;
            }
          },
          // {
          //   title: '所属组织机构',
          //   align: "center",
          //   dataIndex: 'sysOrgCode_dictText'
          // },
          {
            title: '设备名称',
            align: "center",
            dataIndex: 'sbbh_dictText',
          },
          {
            title: '试验类型',
            align: "center",
            dataIndex: 'sylx_dictText'
          },
          {
            title:'委托编号',
            align: "center",
            dataIndex: 'wtbh',
          },
          {
            title: '工程名称',
            align: "center",
            dataIndex: 'cjmc'
          },
          {
            title: '试件编号',
            align: "center",
            dataIndex: 'sjbh'
          },

          // {
          //   title: '龄期',
          //   align: "center",
          //   dataIndex: 'lq'
          // },
          {
            title: '试件尺寸',
            align: "center",
            dataIndex: 'sjcc',
            scopedSlots: {customRender: 'tag2'},
          },
          {
            title: '试件数量',
            align: "center",
            dataIndex: 'sjsl'
          },
          // {
          //   title: '设计强度',
          //   align: "center",
          //   dataIndex: 'sjqd'
          // },
          // {
          //   title: '钢筋原材强度',
          //   align: "center",
          //   dataIndex: 'pzbm'
          // },
          {
            title: '强度代表值',
            align: "center",
            dataIndex: 'qddbz',
            scopedSlots: {customRender: 'tag1'},
          },
          {
            title: '试验日期',
            align: "center",
            dataIndex: 'syrq'
          },
          {
            title: '判定结果',
            align: "center",
            dataIndex: 'pdjg',
            key: 'pdjg',
            scopedSlots: {customRender: 'tags'},
          },
          // {
          //   title: '操作人员',
          //   align: "center",
          //   dataIndex: 'czry'
          // },
          {
            title: '操作',
            dataIndex: 'action',
            align: "center",
            fixed: "right",
            width: 147,
            scopedSlots: {customRender: 'action'},
          }
        ],
        url: {
          list: "/syj/tSyjzb/list7",
          delete: "/syj/tSyjzb/delete",
          deleteBatch: "/syj/tSyjzb/deleteBatch",
          exportXlsUrl: "/syj/tSyjzb/exportXls",
          importExcelUrl: "syj/tSyjzb/importExcel",
          syjwarn:"/tsyjzbStatistics/tSyjzbStatistics/wnjwarn"
        },
        dictOptions: {},
        superFieldList: [],
      }
    },
    created() {
      this.getToken();
      this.getSuperFieldList();
      this.shebeiList();
       this.tongji();
    },
    computed: {
      importExcelUrl: function () {
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      }
    },
    methods: {
      tongji:function(){
        var sys_depart_orgcode=Vue.ls.get('SYS_DEPART_ORGCODE');
        getAction(this.url.syjwarn,{sys_depart_orgcode})
          .then(res=>{
            var data = res.result
            this.wnjMonthWarn = data.mwarn;
            this.wnjAllWarn = data.allWarn;
          })
      },
      shebeiList:function (){
        var sys_depart_orgcode=Vue.ls.get('SYS_DEPART_ORGCODE');
        usershebeiList({
          sys_depart_orgcode:sys_depart_orgcode,
          sbtypes:'3'
        }).then(res=>{
          this.dictOption=[];
          let result=res.result;
          result.forEach(re=>{
            this.dictOption.push({text:re.sbname,value:re.sbjno})
          })
          //console.log(res)
        })
      },
      initDictConfig() {
      },
      getSuperFieldList() {
        let fieldList = [];
        fieldList.push({type: 'int', value: 'id', text: 'id', dictCode: ''})
        fieldList.push({type: 'int', value: 'version', text: 'version', dictCode: ''})
        fieldList.push({type: 'string', value: 'syjid', text: '唯一码', dictCode: ''})
        fieldList.push({type: 'string', value: 'wtid', text: '委托ID', dictCode: ''})
        fieldList.push({type: 'string', value: 'sbbh', text: '设备编号', dictCode: ''})
        fieldList.push({type: 'string', value: 'sylx', text: '试验类型', dictCode: ''})
        fieldList.push({type: 'string', value: 'wtbh', text: '委托编号', dictCode: ''})
        fieldList.push({type: 'string', value: 'sjbh', text: '试件编号', dictCode: ''})
        fieldList.push({type: 'string', value: 'syrq', text: '制件日期'})
        fieldList.push({type: 'string', value: 'sywcsj', text: '试验完成时间', dictCode: ''})
        fieldList.push({type: 'int', value: 'lq', text: '龄期', dictCode: ''})
        fieldList.push({type: 'string', value: 'sjcc', text: '试件尺寸', dictCode: ''})
        fieldList.push({type: 'string', value: 'sjmj', text: '试件面积', dictCode: ''})
        fieldList.push({type: 'string', value: 'sjsl', text: '试件数量', dictCode: ''})
        fieldList.push({type: 'string', value: 'sjqd', text: '设计强度', dictCode: ''})
        fieldList.push({type: 'string', value: 'zsxs', text: '折算系数', dictCode: ''})
        fieldList.push({type: 'string', value: 'qddbz', text: '强度代表值', dictCode: ''})
        fieldList.push({type: 'string', value: 'pdjg', text: '判定结果', dictCode: ''})
        fieldList.push({type: 'string', value: 'czry', text: '操作人员', dictCode: ''})
        fieldList.push({type: 'string', value: 'cjmc', text: '工程名称', dictCode: ''})
        fieldList.push({type: 'string', value: 'pzbm', text: '品种编码', dictCode: ''})
        fieldList.push({type: 'string', value: 'gczj', text: '公称直径', dictCode: ''})
        fieldList.push({type: 'string', value: 'area', text: '承压面积', dictCode: ''})
        fieldList.push({type: 'int', value: 'iswjj', text: 'iswjj', dictCode: ''})
        fieldList.push({type: 'string', value: 'rtcode', text: 'rtcode', dictCode: ''})
        fieldList.push({type: 'string', value: 'szfw', text: 'szfw', dictCode: ''})
        fieldList.push({type: 'string', value: 'fbl', text: '生产厂家', dictCode: ''})
        fieldList.push({type: 'int', value: 'status', text: 'status', dictCode: ''})
        fieldList.push({type: 'string', value: 'wtzs', text: 'wtzs', dictCode: ''})
        fieldList.push({type: 'string', value: 'recGuid', text: 'recGuid', dictCode: ''})
        fieldList.push({type: 'string', value: 'beizhu', text: '备注', dictCode: ''})
        fieldList.push({type: 'int', value: 'tjstate', text: 'tjstate', dictCode: ''})
        fieldList.push({type: 'int', value: 'judgestate', text: 'judgestate', dictCode: ''})
        fieldList.push({type: 'date', value: 'sjscsj', text: '数据上传时间', dictCode: ''})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>