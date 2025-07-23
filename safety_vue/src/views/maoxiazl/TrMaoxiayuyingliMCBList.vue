<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="4" :sm="24">
            <a-form-item label="设备名称">
              <j-search-select-tag
                placeholder="请选择设备名称"
                v-model="queryParam.sbbh"
                :dictOptions="dictOption"
              >
              </j-search-select-tag>
              {{ selectValue }}
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="检测部位">
              <a-input placeholder="请输入检测部位" v-model="queryParam.gjbh"></a-input>
            </a-form-item>
          </a-col>

          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="检测日期范围">
              <j-date
                placeholder="开始检测日期"
                v-model="queryParam.jcrq_begin"
                :showTime="true"
                dateFormat="YYYY-MM-DD"
              />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="">
              <j-date
                placeholder="结束检测日期"
                v-model="queryParam.jcrq_end"
                :showTime="true"
                dateFormat="YYYY-MM-DD"
              />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <!--      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>-->
      <!--      <a-button type="primary" icon="download" @click="handleExportXls('锚下预应力张拉主表')">导出</a-button>-->
      <!--      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">-->
      <!--        <a-button type="primary" icon="import">导入</a-button>-->
      <!--      </a-upload>-->
      <!-- 高级查询区域 -->
      <j-super-query
        :fieldList="superFieldList"
        ref="superQueryModal"
        @handleSuperQuery="handleSuperQuery"
      ></j-super-query>
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete" />删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down"/></a-button>
      </a-dropdown>
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择
        <a style="font-weight: 600">{{ selectedRowKeys.length }}</a
        >项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table
        ref="table"
        size="middle"
        bordered
        rowKey="id"
        class="j-table-force-nowrap"
        :scroll="{ x: true }"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
        @change="handleTableChange"
      >
        <span slot="overLevel" slot-scope="overLevel, record">
        <a-tag color="green" v-if="record.overLevel == '0'">合格</a-tag>
         <a-tag color="red" v-if="record.overLevel == '1'">不合格</a-tag>
       </span>
        <span slot="overproofStatus" slot-scope="overproofStatus, record">
        <a-tag color="green" v-if="record.overproofStatus == '0'">未处置</a-tag>
         <a-tag color="orange" v-if="record.overproofStatus == '10'">已处置</a-tag>
         <a-tag color="yellow" v-if="record.overproofStatus == '20'">已闭合</a-tag>
       </span>

        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <template slot="imgSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无图片</span>
          <img
            v-else
            :src="getImgView(text)"
            height="25px"
            alt=""
            style="max-width:80px;font-size: 12px;font-style: italic;"
          />
        </template>
        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无文件</span>
          <a-button v-else :ghost="true" type="primary" icon="download" size="small" @click="downloadFile(text)">
            下载
          </a-button>
        </template>

        <span slot="action" slot-scope="text, record">
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item>
        </span>
      </a-table>
    </div>

    <tr-maoxiayuyingli-m-modal ref="modalForm" @ok="modalFormOk" />
  </a-card>
</template>

<script>
// function setquery(obj) {
//   Object.keys(obj).map((item,index) => {
//     console.log(item)
//   })
// }
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import JDictSelectTag from '@/components/dict/JDictSelectTag.vue'
import TrMaoxiayuyingliMCBModal from './modules/TrMaoxiayuyingliMCBModal.vue'
import '@/assets/less/TableExpand.less'
import {formatDate} from "@/utils/util"
import Vue from 'vue'
import { usershebeiList } from '@api/api'
import { getAction } from '@api/manage'
import TrMaoxiayuyingliMModal from '@views/maoxiazl/modules/TrMaoxiayuyingliMModal.vue'
export default {
  name: 'TrMaoxiayuyingliMCBList',
  mixins: [JeecgListMixin],
  components: {
    TrMaoxiayuyingliMModal
  },
  data() {
    return {
      description: '锚下预应力张拉主表管理页面',
      // 表头
      columns: [
        {
          title: '#',
          dataIndex: '',
          key: 'rowIndex',
          width: 60,
          align: 'center',
          customRender: function(t, r, index) {
            return parseInt(index) + 1
          }
        },
        {
          title: '设备名称',
          align: 'center',
          dataIndex: 'sbbh_dictText'
        },
        {
          title: '任务单 ',
          align: 'center',
          dataIndex: 'serialno'
        },
        {
          title: '设备厂家',
          align: 'center',
          dataIndex: 'shebeichangjia'
        },
        {
          title: '检测部位',
          align: 'center',
          dataIndex: 'gjbh'
        },
        {
          title: '检测日期',
          align: 'center',
          dataIndex: 'jcrq'
        },
        {
          title: '检测内容',
          align: 'center',
          dataIndex: 'jcnr'
        },
        {
          title: '设计力值',
          align: 'center',
          dataIndex: 'sjlz'
        },
        {
          title: '孔道钢绞线束数',
          align: 'center',
          dataIndex: 'kdgs'
        },
        {
          title: '设计砼强度',
          align: 'center',
          dataIndex: 'sjqd'
        },
        {
          title: '是否合格',
          align: 'center',
          dataIndex: 'overLevel',
          scopedSlots: { customRender: 'overLevel' },
        },
        {
          title: '处置状态',
          align: 'center',
          dataIndex: 'overproofStatus',
          scopedSlots: { customRender: 'overproofStatus' },
        },
        {
          title: '处置人',
          align: 'center',
          dataIndex: 'trMaoxiayuyingliOverHandler.handlePerson',
        },
        {
          title: '操作',
          dataIndex: 'action',
          align:"center",
          fixed:"right",
          width:147,
          scopedSlots: { customRender: 'action' },
        }
      ],
      url: {
        list: '/trmaoxiayuyinglim/trMaoxiayuyingliM/cblist',
        delete: '/trmaoxiayuyinglim/trMaoxiayuyingliM/delete',
        deleteBatch: '/trmaoxiayuyinglim/trMaoxiayuyingliM/deleteBatch',
        exportXlsUrl: '/trmaoxiayuyinglim/trMaoxiayuyingliM/exportXls',
        importExcelUrl: 'trmaoxiayuyinglim/trMaoxiayuyingliM/importExcel'
      },
      dictOptions: {},

      //
      dictOption: [],
      //选中值
      selectValue: '',
      superFieldList: []
    }
  },
  created() {
    // this.getToken();
    this.getSuperFieldList()
    this.shebeiList()
  },
  computed: {
    importExcelUrl: function() {
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`
    }
  },
  methods: {
    initDictConfig() {},
    getSuperFieldList() {
      let fieldList = []
      fieldList.push({ type: 'string', value: 'shebeichangjia', text: '设备厂家', dictCode: '' })
      fieldList.push({ type: 'string', value: 'shebeichangjia', text: '设备厂家' })
      fieldList.push({ type: 'string', value: 'sbbh', text: '设备编号', dictCode: '' })
      fieldList.push({ type: 'string', value: 'sbbh', text: '设备编号' })
      fieldList.push({ type: 'string', value: 'dwgc', text: '单位工程' })
      fieldList.push({ type: 'string', value: 'dwgc', text: '单位工程', dictCode: '' })
      fieldList.push({ type: 'string', value: 'fbgc', text: '分部工程', dictCode: '' })
      fieldList.push({ type: 'string', value: 'fbgc', text: '分部工程' })
      fieldList.push({ type: 'string', value: 'fxgc', text: '分项工程', dictCode: '' })
      fieldList.push({ type: 'string', value: 'fxgc', text: '分项工程' })
      fieldList.push({ type: 'string', value: 'gjbh', text: '构件编号', dictCode: '' })
      fieldList.push({ type: 'string', value: 'gjbh', text: '构件编号' })
      fieldList.push({ type: 'string', value: 'jcrq', text: '检测日期' })
      fieldList.push({ type: 'string', value: 'jcrq', text: '检测日期', dictCode: '' })
      fieldList.push({ type: 'string', value: 'jcnr', text: '检测内容', dictCode: '' })
      fieldList.push({ type: 'string', value: 'jcnr', text: '检测内容' })
      fieldList.push({ type: 'string', value: 'sjlz', text: '设计力值' })
      fieldList.push({ type: 'string', value: 'sjlz', text: '设计力值', dictCode: '' })
      fieldList.push({ type: 'string', value: 'kdgs', text: '孔道钢绞线束数', dictCode: '' })
      fieldList.push({ type: 'string', value: 'kdgs', text: '孔道钢绞线束数' })
      fieldList.push({ type: 'string', value: 'sjqd', text: '设计砼强度（强度等级）', dictCode: '' })
      fieldList.push({ type: 'string', value: 'sjqd', text: '设计砼强度（强度等级）' })
      fieldList.push({ type: 'int', value: 'overLevel', text: '超标等级(0合格，123，初中高  默认为0）', dictCode: '' })
      fieldList.push({ type: 'int', value: 'overLevel', text: '超标等级(0合格，123，初中高  默认为0）' })
      fieldList.push({ type: 'string', value: 'uuid', text: '唯一id', dictCode: '' })
      fieldList.push({ type: 'string', value: 'uuid', text: '唯一id' })
      fieldList.push({ type: 'string', value: 'lh', text: '梁号' })
      fieldList.push({ type: 'string', value: 'lh', text: '梁号', dictCode: '' })
      this.superFieldList = fieldList
    },
    shebeiList: function() {
      var sys_depart_orgcode = Vue.ls.get('SYS_DEPART_ORGCODE')
      usershebeiList({
        sys_depart_orgcode: sys_depart_orgcode,
        sbtypes: '79'
      }).then(res => {
        this.dictOption = []
        let result = res.result
        result.forEach(re => {
          this.dictOption.push({ text: re.sbname, value: re.sbjno })
        })
        //console.log(res)
      })
    },
    searchQuery() {
      let param = this.queryParam
      console.log(param)
      let params = {}
      const stragy = {
        rqEdit(value) {
          if (value.includes('rq')) {
            return formatDate(value, "YYYY-MM-DD")
          } else {
            return value
          }
        },
        getValue(key,value) {
             if (key.includes('field')) {
            params[key] = `${value}`
          } else {
            params[key] = `*${value}*`

          }
        }
      }
      Object.entries(param).forEach(([key, value]) => {
        stragy.rqEdit(key)
        stragy.getValue(key,value)
      })
      console.log(params)
      getAction(this.url.list, params).then((res) => {
        let result = res.result.records
        this.dataSource=result
      })
    }



  }
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>
