<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="4" :lg="7" :md="8" :sm="24">
            <a-form-item label="材料名称">
              <j-search-select-tag v-model="queryParam.cailiaono" :dictOptions="dictOption">
              </j-search-select-tag>
              {{ selectValue }}
            </a-form-item>
          </a-col>
          <a-col :xl="4" :lg="7" :md="8" :sm="24">
            <a-form-item label="材料类型">
              <j-dict-select-tag placeholder="" v-model="queryParam.nodeType" dictCode="nodeType"></j-dict-select-tag>
            </a-form-item>
          </a-col>
          <a-col :xl="4" :lg="7" :md="8" :sm="24">
            <a-form-item label="收样人">
              <a-input placeholder="" v-model="queryParam.shouyangren"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="5" :lg="7" :md="8" :sm="24">
            <a-form-item label="收样时间">
              <j-date
                placeholder=""
                v-model="queryParam.shouyangshijian"
                :showTime="true"
                dateFormat="YYYY-MM-DD"
              />
            </a-form-item>
          </a-col>
          <a-col :xl="5" :lg="7" :md="8" :sm="24">
            <a-form-item label="收样状态">
              <a-radio-group name="radioGroup" :default-value="1" v-model="queryParam.shouyangzhuangtai">
                <a-radio :value="1">全部</a-radio>
                <a-radio :value="2">未收样</a-radio>
                <a-radio :value="3">已收样</a-radio>
              </a-radio-group>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col span="21">
            <span class="table-page-search-submitButtons">
              <a-button @click="entrustAdd" type="primary" style="margin-left: 8px">新增样品</a-button>
              <a-button @click="entrustDelete" type="primary" style="margin-left: 8px">退回</a-button>
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
          <a-tag color="red" v-if="record.quyangzhuangtai == 0">未取样</a-tag>
          <a-tag color="green" v-if="record.quyangzhuangtai == 1">已取样</a-tag>
        </span>
        <span slot="tags1" slot-scope="tags1, record">
          <a-tag color="red" v-if="record.shouyangzhuangtai == 0">未收样</a-tag>
          <a-tag color="green" v-if="record.shouyangzhuangtai == 1">已收样</a-tag>
          <a-tag color="orange" v-if="record.shouyangzhuangtai == 2">已提交</a-tag>
          <a-tag color="blue" v-if="record.shouyangzhuangtai == 3">在检</a-tag>
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
          <a @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical"/>
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down"/></a>
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

    <quyangguanli-modal ref="modalForm" @ok="modalFormOk"></quyangguanli-modal>
  </a-card>
</template>

<script>

import '@/assets/less/TableExpand.less'
import { mixinDevice } from '@/utils/mixin'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import quyangguanliModal from './modules/quyangguanliModal'
import { getAction, deleteAction } from '@api/manage'
import Vue from 'vue'

export default {
  name: 'quyangguanli',
  mixins: [JeecgListMixin, mixinDevice],
  components: {
    quyangguanliModal
  },
  data() {
    return {
      selectValue: '',
      dictOption: [],
      description: 'quyangguanli管理页面',
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
          title: '规格型号',
          align: 'center',
          dataIndex: 'guigexinghao'
        },
        {
          title: '生产批号',
          align: 'center',
          dataIndex: 'pici'
        },
        {
          title: '收样状态',
          align: 'center',
          dataIndex: 'shouyangzhuangtai',
          scopedSlots: { customRender: 'tags1' }
        },
        {
          title: '试验状态',
          align: 'center',
          dataIndex: ''
        }
      ],
      url: {
        list: '/qywtd/syDpsYyYuancaiquyangweituo/getSyList',
        qeurycailiaoName: '/wztaizhang/wztaizhang/queryList10',
      },
      dictOptions: {},
      superFieldList: [],
    }
  },
  created() {
    this.getSuperFieldList()
    this.qeurycailiaoName()
  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`
    },
  },
  methods: {
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
    initDictConfig() {
    },
    getSuperFieldList() {
      let fieldList = []
      fieldList.push({ type: 'int', value: 'yuancaijinchangdengjiid', text: '外键，原材进场登记表ID' })
      fieldList.push({ type: 'int', value: 'quyangzhuangtai', text: '取样状态（0：未取样  1：已取样）默认0' })
      fieldList.push({ type: 'string', value: 'quyangren', text: '外键，取样人，获取当前登录用户外键T_S_Base_User表的UserName字段' })
      fieldList.push({ type: 'string', value: 'quyangshijian', text: '取样时间（YYYY-MM-dd HH:mm:ss）' })
      fieldList.push({ type: 'string', value: 'shouyangren', text: '外键，收样人，获取当前登录用户外键T_S_Base_User表的UserName字段' })
      fieldList.push({ type: 'string', value: 'shouyangshijian', text: '收样时间（YYYY-MM-dd HH:mm:ss）' })
      fieldList.push({ type: 'int', value: 'shouyangzhuangtai', text: '收样状态（0：未收样  1：已收样 2：已提交 3：在检）默认0' })
      fieldList.push({ type: 'string', value: 'jianzhengren', text: '外键，见证人，获取当前登录用户外键T_S_Base_User表的UserName字段' })
      fieldList.push({ type: 'string', value: 'jianzhengshijian', text: '见证时间（YYYY-MM-dd HH:mm:ss）' })
      fieldList.push({ type: 'int', value: 'jianzhengzhuangtai', text: '见证状态（0：未见证  1：已见证）默认0' })
      fieldList.push({ type: 'string', value: 'weituodanbianhao', text: '委托单编号，唯一' })
      fieldList.push({ type: 'string', value: 'yangpinchulifangshi', text: '样品处理方式' })
      fieldList.push({ type: 'string', value: 'quyangdidian', text: '取样地点' })
      fieldList.push({ type: 'int', value: 'baogaofenshu', text: '报告份数' })
      fieldList.push({ type: 'string', value: 'jiancexiangmu', text: '检测项目' })
      fieldList.push({ type: 'string', value: 'weituoren', text: '委托人' })
      fieldList.push({ type: 'string', value: 'weituoriqi', text: '委托日期' })
      fieldList.push({ type: 'string', value: 'shigongdanwei', text: '施工单位' })
      fieldList.push({ type: 'string', value: 'jianglidanwei', text: '监理单位' })
      fieldList.push({ type: 'string', value: 'shigongdanweihetonghao', text: '施工单位合同号' })
      fieldList.push({ type: 'string', value: 'jianlidanweihetonghao', text: '监理单位合同号' })
      fieldList.push({ type: 'string', value: 'jianlidanweidepartid', text: '外键，监理单位组织机构id' })
      fieldList.push({ type: 'string', value: 'jianlidanweiorgcode', text: '外键，监理单位组织机构编码' })
      fieldList.push({
        type: 'string',
        value: 'renwuzhixingren',
        text: '外键，任务执行人（由收样人分配），获取当前登录用户外键T_S_Base_User表的UserName字段'
      })
      fieldList.push({ type: 'int', value: 'zhixingzhuangtai', text: '执行状态（0：未执行 1：执行中 2：已完成）' })
      fieldList.push({ type: 'string', value: 'sampleno', text: '外键，样品编号' })
      fieldList.push({ type: 'string', value: 'erweimabianhao', text: '二维码编号(处理后字段，id+序号)' })
      fieldList.push({ type: 'string', value: 'erweimaweiyima', text: '二维码UUID' })
      fieldList.push({ type: 'int', value: 'weituozhuangtai', text: '委托状态（0：未提交 1：已提交）默认0，已提交时需更新原材登记表的委托状态为已委托' })
      fieldList.push({ type: 'string', value: 'zhipaiquyangren', text: '指派的取样人名称，获取当前登录用户外键T_S_Base_User表的UserName字段' })
      fieldList.push({ type: 'string', value: 'zhipaishijian', text: '指派时间（YYYY-MM-dd HH:mm:ss）' })
      fieldList.push({ type: 'string', value: 'zhipairen', text: '指派人，获取当前登录用户外键T_S_Base_User表的UserName字段' })
      fieldList.push({ type: 'int', value: 'zhipaizhuangtai', text: '指派状态（0：未指派1：已指派）' })
      fieldList.push({ type: 'string', value: 'zhipaiquyangrenid', text: '指派的取样人id，获取当前登录用户外键T_S_Base_User表的id字段' })
      fieldList.push({ type: 'string', value: 'beizhu', text: 'beizhu' })
      fieldList.push({ type: 'string', value: 'fujian', text: 'fujian' })
      this.superFieldList = fieldList
    }
  }
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>