<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="料仓名称">
              <a-input placeholder="请输入料仓名称" v-model="queryParam.name"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="材料类型">
              <j-dict-select-tag placeholder="请选择" v-model="queryParam.cailiaono"
                                 dictCode="cailiaono"></j-dict-select-tag>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="料仓状态">
              <j-dict-select-tag placeholder="请选择" v-model="queryParam.liaocangStatus"
                                 dictCode="liaocang_status"></j-dict-select-tag>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <!--        <a-button @click="CaiLiaoLy" type="primary" icon="plus">材料领用</a-button>-->
        <!--        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a-->
        <!--        style="font-weight: 600">{{ selectedRowKeys.length }}</a>项-->
        <!--        <a style="margin-left: 24px" @click="onClearSelected">清空</a>-->
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
          <a-tag color="geekblue" v-if="record.liaocangStatus == '1'">进场中</a-tag>
          <a-tag color="orange" v-if="record.liaocangStatus == '2'">检验中</a-tag>
          <a-tag color="green" v-if="record.liaocangStatus == '3'">合格</a-tag>
          <a-tag color="red" v-if="record.liaocangStatus == '4'">不合格</a-tag>
        </span>
        <span slot="choujianzt" slot-scope="choujianzt, record">
          <a-tag color="geekblue" v-if="record.choujianzt == '0'">未抽检</a-tag>
          <a-tag color="orange" v-if="record.choujianzt == '1'">取样</a-tag>
          <a-tag color="green" v-if="record.choujianzt == '2'">合格</a-tag>
          <a-tag color="red" v-if="record.choujianzt == '3'">不合格</a-tag>
        </span>

        <span slot="action" slot-scope="text, record">
          <a @click="siloDetail(record)">详情</a>
          <a-divider type="vertical"/>
          <a @click="correctEdit(record)">修正</a>
          <a-divider type="vertical" v-has="'cyly:ly'"/>
          <a @click="CaiLiaoLy(record)" v-has="'cyly:ly'">领用</a>
        </span>
      </a-table>
    </div>

    <cai-liao-ly ref="clly"></cai-liao-ly>
    <silo-analysis-detail ref="modalSilo"></silo-analysis-detail>
    <correct ref="modalCorrect"></correct>
  </a-card>
</template>

<script>

import '@/assets/less/TableExpand.less'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import { handertoken } from '@/mixins/getHanderToken'
import SiloAnalysisDetail from './module/SiloAnalysisDetail'
import correct from './module/correct'
import CaiLiaoLy from './module/CaiLiaoLy'

export default {
  name: 'SiloAnalysisList',
  mixins: [JeecgListMixin, handertoken],
  components: {
    SiloAnalysisDetail,
    correct,
    CaiLiaoLy
  },
  data() {
    return {
      // 表头
      columns: [
        {
          title: '序号',
          dataIndex: '',
          key: 'rowIndex',
          width: 60,
          align: 'center',
          customRender: function (t, r, index) {
            return parseInt(index) + 1
          }
        },
        {
          title: '组织机构',
          align: 'center',
          dataIndex: 'sysOrgCode_dictText'
        },
        {
          title: '料仓名称',
          align: 'center',
          dataIndex: 'name'
        },
        {
          title: '料斗号',
          align: 'center',
          dataIndex: 'cailiaoname',
        },
        {
          title: '材料类型',
          align: 'center',
          dataIndex: 'cailiaono_dictText',
        },
        {
          title: '规格类型',
          align: 'center',
          dataIndex: 'guigexinghao',
        },
        {
          title: '实时库存数量',
          align: 'center',
          dataIndex: 'sskucun'
        },
        {
          title: '料仓状态',
          align: 'center',
          dataIndex: 'liaocangStatus',
          scopedSlots: { customRender: 'tags' }
        },
        /*{
          title:'抽检状态',
          align:"center",
          dataIndex: 'choujianzt',
          scopedSlots: { customRender: 'choujianzt' }
        },*/
        {
          title: '累计进场数量',
          align: 'center',
          dataIndex: 'ljjinchang'
        },
        {
          title: '累计使用数量',
          align: 'center',
          dataIndex: 'ljshiyong'
        },
        {
          title: '库存损耗累计修正',
          align: 'center',
          dataIndex: 'ljxiuzheng'
        },
        {
          title: '进耗详情查询',
          dataIndex: 'action',
          align: 'center',
          fixed: 'right',
          width: 120,
          scopedSlots: { customRender: 'action' }
        }
      ],
      url: {
        list: '/wzliaocang/wzliaocang/lists',
      },
    }
  },
  created() {
    this.getToken()
  },
  computed: {},
  methods: {
    CaiLiaoLy(record) {
      // if (this.selectionRows.length == 1) {
      this.$refs.clly.wzliaocang = record
      this.$refs.clly.model.storageId = record.guid
      this.$refs.clly.model.storageName = record.name
      this.$refs.clly.model.createBy = this.$store.state.user.realname
      this.$refs.clly.model.sysOrgCode = this.$store.state.user.orgCode
      this.$refs.clly.getJYP()
      this.$refs.clly.visible = true
      // } else {
      //   this.$message.error('请选择单行数据！')
      // }
    },
    siloDetail(record) {
      this.$refs.modalSilo.guid = record.guid
      this.$refs.modalSilo.siloForm = Object.assign({}, record)
      this.$refs.modalSilo.getList()
      this.$refs.modalSilo.visible = true
    },
    correctEdit(record) {
      this.$refs.modalCorrect.guid = record.guid
      this.$refs.modalCorrect.visible = true
    }
  }
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>