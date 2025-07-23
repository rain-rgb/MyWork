<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="材料名称">
              <j-search-select-tag v-model="queryParam.cailiaono" :dictOptions="dictOption">
              </j-search-select-tag>
              {{ selectValue }}
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <!--            <a-form-item label="试验类型">
                          <sample-type v-model="queryParam.titCode" placeholder=""></sample-type>
                        </a-form-item>-->
            <a-form-item label="材料类型">
              <j-dict-select-tag placeholder="" v-model="queryParam.nodeType" dictCode="nodeType"></j-dict-select-tag>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="使用部位">
              <a-input placeholder="" v-model="queryParam.usePart"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="材料厂家">
              <j-search-select-tag v-model="queryParam.guids" :dictOptions="dictOption1">
              </j-search-select-tag>
              {{ selectValue }}
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="进场日期">
              <j-date
                placeholder=""
                v-model="queryParam.jinchangshijian"
                :showTime="true"
                dateFormat="YYYY-MM-DD"
              />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="委托状态">
              <!--              <j-search-select-tag v-model="queryParam.delegateState" :dictOptions="dictOption2">
                            </j-search-select-tag>-->
              <!--              <a-input placeholder="" v-model="queryParam.sampleName"></a-input>-->
              <a-radio-group name="radioGroup" :default-value="2" v-model="queryParam.delegateState">
                <a-radio :value="2">全部</a-radio>
                <a-radio :value="0">未委托</a-radio>
                <a-radio :value="1">已委托</a-radio>
              </a-radio-group>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col span="21">
            <span class="table-page-search-submitButtons">
              <a-button @click="entrustAdd" type="primary" style="margin-left: 8px">新增委托</a-button>
              <a-button @click="entrustDelete" type="primary" style="margin-left: 8px">撤销委托</a-button>
            </span>
          </a-col>
          <a-col span="3">
            <span class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>

    <!-- table区域-begin -->
<!--    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a
        style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>-->

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
          <a-tag color="green" v-if="record.delegateState == 0">未委托</a-tag>
          <a-tag color="orange" v-if="record.delegateState == 1">已委托</a-tag>
        </span>
        <span slot="tags2" slot-scope="tags2, record">
          <a-tag color="green" v-if="record.jianyanstate == 0">未检验</a-tag>
          <a-tag color="orange" v-if="record.jianyanstate == 1">合格</a-tag>
          <a-tag color="red" v-if="record.jianyanstate == 2">不合格</a-tag>
          <a-tag color="blue" v-if="record.jianyanstate == 3">检验中</a-tag>
        </span>
        <span slot="tags3" slot-scope="tags3, record">
          <a-tag color="green" v-if="record.choujianstate == 0">未检验</a-tag>
          <a-tag color="orange" v-if="record.choujianstate == 1">合格</a-tag>
          <a-tag color="red" v-if="record.choujianstate == 2">不合格</a-tag>
          <a-tag color="blue" v-if="record.choujianstate == 3">检验中</a-tag>
        </span>
        <span slot="tags4" slot-scope="tags4, record">
          <a-tag color="red" v-if="record.quyangzhuangtai == 0">未取样</a-tag>
          <a-tag color="green" v-if="record.quyangzhuangtai == 1">已取样</a-tag>
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

      </a-table>
    </div>

    <wztaizhang-modal ref="modalForm" @ok="modalFormOk"></wztaizhang-modal>
  </a-card>
</template>

<script>

import '@/assets/less/TableExpand.less'
import { mixinDevice } from '@/utils/mixin'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import { getAction, deleteAction } from '@api/manage'
import WztaizhangModal from './modules/WztaizhangModal'
import SampleType from '@/views/sy/dpssysample/modules/SampleType'
import Vue from 'vue'

export default {
  name: 'WztaizhangList',
  mixins: [JeecgListMixin, mixinDevice],
  components: {
    SampleType,
    WztaizhangModal
  },
  data() {
    return {
      selectValue: '',
      obj1: {},
      dictOption: [],
      dictOption1: [],
      dictOption2: [{
        text: '未委托',
        value: '0'
      }, {
        text: '已委托',
        value: '1'
      }],
      description: 'wztaizhang管理页面',
      // 表头
      columns: [
        {
          title: '#',
          dataIndex: '',
          key: 'rowIndex',
          width: 60,
          align: 'center',
          customRender: function (t, r, index) {
            return parseInt(index) + 1
          }
        },
        {
          title: '材料名称',
          align: 'center',
          dataIndex: 'cailiaono_dictText'
        },
        {
          title: '材料厂家',
          align: 'center',
          dataIndex: 'gongyingshangdanweibianma_dictText'
        },
        {
          title: '规格类型',
          align: 'center',
          dataIndex: 'guigexinghao'
        },
        {
          title: '批次',
          align: 'center',
          dataIndex: 'pici'
        },
        {
          title: '进场时间',
          align: 'center',
          dataIndex: 'jinchangshijian'
        },
        {
          title: '创建时间',
          align: 'center',
          dataIndex: 'createTime',
          customRender: function (text) {
            return !text ? '' : (text.length > 10 ? text.substr(0, 10) : text)
          }
        },
        {
          title: '存放地点',
          align: 'center',
          dataIndex: 'storagePlace'
        },
        {
          title: '使用部位',
          align: 'center',
          dataIndex: 'usePart'
        },
        {
          title: '净重(吨)',
          align: 'center',
          dataIndex: 'jingzhongt'
        },
        /*{
          title: '抽检状态',
          align: 'center',
          dataIndex: 'choujianstate',
          scopedSlots: { customRender: 'tags3' }
        },*/
        {
          title: '委托状态',
          align: 'center',
          dataIndex: 'delegateState',
          scopedSlots: { customRender: 'tags' }
        },
        {
          title: '取样状态',
          align: 'center',
          dataIndex: 'quyangzhuangtai',
          scopedSlots: { customRender: 'tags4' }
        },
        {
          title: '质保单',
          align: 'center',
          dataIndex: 'zhibaodan'
        }
      ],
      url: {
        list: '/qywtd/syDpsYyYuancaiquyangweituo/selectByVo',
        delete: '/qywtd/syDpsYyYuancaiquyangweituo/deleteDelegate/',
        qeurycailiaoName: '/wztaizhang/wztaizhang/queryList10',
        queryGongYingShangName: '/wzgongyingshang/wzgongyingshang/queryList',
        querySysDepart: '/sys/sysDepart/getDepartName',
      },
      dictOptions: {},
      superFieldList: [],
    }
  },
  created() {
    this.getSuperFieldList()
    this.qeurycailiaoName()
    this.queryGongYingShangName()
    this.querySysDepart()
  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`
    },
  },
  methods: {
    querySysDepart: function () {
      var sys_depart_orgcode = Vue.ls.get('SYS_DEPART_ORGCODE')
      getAction(this.url.querySysDepart, {
        orgCode: sys_depart_orgcode
      }).then((res) => {
        if (res.success) {
          let result = res.result
          this.obj1 = {
            jianglidanwei: result.supervisionUnit,
            shigongdanwei: result.constructionUnit,
            weituoren: this.$store.state.user.realname
          }
        }
      })
    },
    queryGongYingShangName: function () {
      var sys_depart_orgcode = Vue.ls.get('SYS_DEPART_ORGCODE')
      getAction(this.url.queryGongYingShangName, {
        sys_org_code: sys_depart_orgcode
      }).then((res) => {
        if (res.success) {
          this.dictOption1 = []
          let result = res.result
          result.forEach(re => {
            this.dictOption1.push({ text: re.gongyingshangName, value: re.guids })
          })
        }
      })
    },

    qeurycailiaoName: function () {
      var sys_depart_orgcode = Vue.ls.get('SYS_DEPART_ORGCODE')
      getAction(this.url.qeurycailiaoName, {
        sys_org_code: sys_depart_orgcode,
        type: 2
      }).then((res) => {
        if (res.success) {
          this.dictOption = []
          let result = res.result
          result.forEach(re => {
            this.dictOption.push({ text: re.cailiaoName, value: re.cailiaoNo })
          })
        }
      })
    },
    entrustAdd() {
      if (this.selectionRows.length == 1) {
        let obj = {
          ...this.selectionRows[0],
          ...this.obj1
        }
        this.$refs.modalForm.entrust(obj)
        this.$refs.modalForm.title = '新增委托'
        this.$refs.modalForm.disableSubmit = false
      } else {
        this.$message.error('请选择单行数据！')
      }
    },
    entrustDelete() {
      if (this.selectionRows.length == 1) {
        if (this.selectionRows[0].delegateState == 1) {
          let url = this.url.delete + this.selectionRows[0].id
          deleteAction(url).then(res => {
            if (res.success) {
              this.$message.success(res.message)
              this.loadData()
              this.onClearSelected()
            } else {
              this.$message.error(res.message)
            }
          })
        } else {
          this.$message.error('当前数据未委托！')
        }
      } else {
        this.$message.error('请选择单行数据！')
      }
    },
    initDictConfig() {
    },
    getSuperFieldList() {
      let fieldList = []
      fieldList.push({ type: 'string', value: 'jinchangshijian', text: '进场时间' })
      fieldList.push({ type: 'string', value: 'shebeibianhao', text: '设备编号' })
      fieldList.push({ type: 'string', value: 'lcbianhao', text: '料仓编号' })
      fieldList.push({ type: 'string', value: 'cailiaono', text: '材料编号' })
      fieldList.push({ type: 'string', value: 'maozhongt', text: '毛重(吨)' })
      fieldList.push({ type: 'string', value: 'pizhongt', text: '皮重(吨)' })
      fieldList.push({ type: 'string', value: 'jingzhongt', text: '净重(吨)' })
      fieldList.push({ type: 'string', value: 'pici', text: '批次' })
      fieldList.push({ type: 'string', value: 'guigexinghao', text: '规格类型' })
      fieldList.push({ type: 'int', value: 'gblx', text: '过磅类型' })
      fieldList.push({ type: 'int', value: 'jianyanstate', text: '检验状态' })
      fieldList.push({ type: 'int', value: 'choujianstate', text: '抽验状态' })
      fieldList.push({ type: 'string', value: 'gongyingshangdanweibianma', text: '供应商单位编码' })
      fieldList.push({ type: 'int', value: 'isfinish', text: '形成检验批状态' })
      fieldList.push({ type: 'int', value: 'isquyang', text: '是否已取样' })
      fieldList.push({ type: 'string', value: 'baogaofile', text: '报告附件' })
      fieldList.push({ type: 'int', value: 'yidongstatus', text: '数据推送义东高速状态' })
      fieldList.push({ type: 'int', value: 'ruleway', text: '计算规则方式 0：根据地磅等计算 1：根据项目等计算' })
      fieldList.push({ type: 'int', value: 'overproofStatus', text: '0:未处置；10:待审核，20已闭合' })
      fieldList.push({ type: 'int', value: 'jystatus', text: '检验批数据推送' })
      fieldList.push({ type: 'date', value: 'chuchangshijian', text: '出厂时间' })
      fieldList.push({ type: 'string', value: 'storagePlace', text: '存放地点' })
      fieldList.push({ type: 'string', value: 'usePart', text: '使用部位' })
      fieldList.push({ type: 'int', value: 'delegateState', text: '委托状态' })
      fieldList.push({ type: 'string', value: 'zhibaodan', text: '质保单' })
      this.superFieldList = fieldList
    }
  }
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>