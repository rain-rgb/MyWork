<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="设备名称">
              <j-search-select-tag placeholder="请选择设备名称" v-model="queryParam.shebeibianhao" :dictOptions="dictOption">
              </j-search-select-tag>
              {{ selectValue }}
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="供应商">
              <j-search-select-tag placeholder="请选择供应商" v-model="queryParam.gongyingshangdanweibianma"
                                   :dictOptions="dictOption2">
              </j-search-select-tag>
            </a-form-item>
          </a-col>
<!--          <a-col :xl="6" :lg="7" :md="8" :sm="24">-->
<!--            <a-form-item label="时间范围">-->
<!--              <j-date placeholder="开始时间" v-model="queryParam.jinchangshijian_begin" :showTime="true"-->
<!--                      dateFormat="YYYY-MM-DD HH:mm:ss"/>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :xl="6" :lg="7" :md="8" :sm="24">-->
<!--            <a-form-item label="结束时间">-->
<!--              <j-date placeholder="结束时间" v-model="queryParam.jinchangshijian_end" :showTime="true"-->
<!--                      dateFormat="YYYY-MM-DD HH:mm:ss"/>-->
<!--            </a-form-item>-->
<!--          </a-col>-->

          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <!--      <a-button v-has="'yclsl:add'" @click="handleAdd" type="primary" icon="plus">新增</a-button>-->
      <a-button @click="printXls" v-has="'yclsl:print'" type="primary" icon="printer">打印</a-button>
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
          <a-divider type="vertical"/>
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down"/></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>
  </a-card>
</template>

<script>

import '@/assets/less/TableExpand.less'
import { mixinDevice } from '@/utils/mixin'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import JSuperQuery from '@/components/jeecg/JSuperQuery.vue'
import { handertoken } from '@/mixins/getHanderToken'
import Vue from 'vue'
import { SYS_DEPART_ORGCODE } from '@/store/mutation-types'
import { usershebeiList, gongyingshangList, cailiaoList } from '@api/api'
import { getAction } from '@/api/manage'

export default {
  name: 'WzRuKuTaiZhangList',
  mixins: [JeecgListMixin, mixinDevice, handertoken],
  components: {
    JSuperQuery,
  },
  data() {
    return {
      selectValue: '',
      asyncSelectValue: '',
      dictOption: [],
      dictOption2: [],
      dictOption3: [],
      description: '入库台账管理页面',
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
          title: '库别',
          align: 'center',
          dataIndex: ''
        },
        {
          title: '类别',
          align: 'center',
          dataIndex: ''
        },
        {
          title: '物资名称',
          align: 'center',
          dataIndex: 'cailiaono_dictText'
        },
        {
          title: '规格型号',
          align: 'center',
          dataIndex: 'guigexinghao',
        },
        {
          title: '辅助规格',
          align: 'center',
          dataIndex: '',
        },
        {
          title: '单位',
          align: 'center',
          dataIndex: 'danwei'
        },
        {
          title: '生产厂家',
          align: 'center',
          dataIndex: 'gongyingshangdanweibianma_dictText'
        },
        {
          title: '品牌',
          align: 'center',
          dataIndex: ''
        },
        {
          title: '数量',
          align: 'center',
          dataIndex: 'jingzhongt'
        },
        {
          title: '生产批号',
          align: 'center',
          dataIndex: 'pici'
        },
        {
          title: '备注',
          align: 'center',
          dataIndex: 'beizhu'
        },
      ],
      url: {
        // list: '/yclsl/wzycljinchanggb/listRuKu',
      },
      dictOptions: {},
      superFieldList: [],
    }
  },
  created() {
    this.getToken()
    this.getSuperFieldList()
    this.shebeiList()
    this.gongyingshangData()
    this.cailiaoData()

  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`
    },
  },
  methods: {
    shebeiList: function () {
      var sys_depart_orgcode = Vue.ls.get('SYS_DEPART_ORGCODE')
      usershebeiList({
        sys_depart_orgcode: sys_depart_orgcode,
        sbtypes: '27'
      }).then(res => {
        this.dictOption = []
        let result = res.result
        result.forEach(re => {
          this.dictOption.push({ text: re.sbname, value: re.sbjno })
        })
        //console.log(res)
      })
    },
    gongyingshangData: function () {
      var sys_depart_orgcode = Vue.ls.get('SYS_DEPART_ORGCODE')
      gongyingshangList({
        sys_depart_orgcode: sys_depart_orgcode,
        pageNo: 1,
        pageSize: 100
      }).then(res => {
        this.dictOption2 = []
        let result = res.result.records
        result.forEach(re => {
          this.dictOption2.push({ text: re.gongyingshangname, value: re.guid })
        })
      })
    },
    cailiaoData: function () {
      var sys_depart_orgcode = Vue.ls.get('SYS_DEPART_ORGCODE')
      let params = {
        sys_depart_orgcode: sys_depart_orgcode,
        ne: 1,
        pageNo: 1,
        pageSize: 100
      }
      getAction('/wzcailiaonamedict/wzcailiaonamedict/list', params).then(res => {
        this.dictOption3 = []
        let result = res.result.records
        result.forEach(re => {
          this.dictOption3.push({ text: `${re.cailiaoname}(${re.guigexinghao})`, value: re.cailiaono })
        })
      })
    },
    initDictConfig() {
    },
    getSuperFieldList() {
      let fieldList = []
      fieldList.push({ type: 'string', value: 'jinchuliaodanno', text: '进出材料单', dictCode: '' })
      fieldList.push({ type: 'string', value: 'cailiaono', text: '材料编号', dictCode: '' })
      fieldList.push({ type: 'date', value: 'jinchangshijian', text: '进场时间', dictCode: '' })
      fieldList.push({ type: 'date', value: 'chuchangshijian', text: '出场时间', dictCode: '' })
      fieldList.push({ type: 'string', value: 'pici', text: '批次', dictCode: '' })
      fieldList.push({ type: 'string', value: 'cheliangbianhao', text: '船号', dictCode: '' })
      fieldList.push({ type: 'string', value: 'qianchepai', text: '前车牌', dictCode: '' })
      fieldList.push({ type: 'string', value: 'houchepai', text: '后车牌', dictCode: '' })
      fieldList.push({ type: 'string', value: 'maozhong', text: '毛重', dictCode: '' })
      fieldList.push({ type: 'string', value: 'pizhong', text: '皮重', dictCode: '' })
      fieldList.push({ type: 'string', value: 'jingzhong', text: '净重', dictCode: '' })
      fieldList.push({ type: 'string', value: 'kouzhong', text: '扣重', dictCode: '' })
      fieldList.push({ type: 'string', value: 'koulv', text: '扣率', dictCode: '' })
      fieldList.push({ type: 'string', value: 'chengzhongpiancha', text: '称重偏差', dictCode: '' })
      fieldList.push({ type: 'string', value: 'liaocang', text: '累计', dictCode: '' })
      fieldList.push({ type: 'string', value: 'sibangyuan', text: '司磅员', dictCode: '' })
      fieldList.push({ type: 'string', value: 'remark', text: 'remark', dictCode: '' })
      fieldList.push({ type: 'string', value: 'shebeibianhao', text: '设备编号', dictCode: '' })
      fieldList.push({ type: 'string', value: 'gongyingshangdanweibianma', text: '供应商单位编码', dictCode: '' })
      fieldList.push({ type: 'string', value: 'guobangleibie', text: '过磅类别', dictCode: '' })
      fieldList.push({ type: 'string', value: 'cheliangleixing', text: '车辆类型', dictCode: '' })
      fieldList.push({ type: 'string', value: 'guid', text: '唯一标识', dictCode: '' })
      fieldList.push({ type: 'int', value: 'ts', text: '时间戳', dictCode: '' })
      fieldList.push({ type: 'string', value: 'isdel', text: '是否删除', dictCode: '' })
      fieldList.push({ type: 'string', value: 'status', text: 'status', dictCode: '' })
      fieldList.push({ type: 'string', value: 'maozhongt', text: '毛重(吨)', dictCode: '' })
      fieldList.push({ type: 'string', value: 'pizhongt', text: '皮重(吨)', dictCode: '' })
      fieldList.push({ type: 'string', value: 'candi', text: 'candi', dictCode: '' })
      fieldList.push({ type: 'string', value: 'yunshudanwei', text: '运输单位', dictCode: '' })
      fieldList.push({ type: 'string', value: 'lcbianhao', text: '料仓编号', dictCode: '' })
      fieldList.push({ type: 'string', value: 'isshouliao', text: 'isshouliao', dictCode: '' })
      fieldList.push({ type: 'string', value: 'jingzhongt', text: '净重(吨)', dictCode: '' })
      fieldList.push({ type: 'int', value: 'istongji', text: '是否统计 默认为0:未统计，1:已统计,15:统计出错', dictCode: '' })
      fieldList.push({ type: 'string', value: 'jcgkpic', text: 'jcgkpic', dictCode: '' })
      fieldList.push({ type: 'string', value: 'jccppic', text: 'jccppic', dictCode: '' })
      fieldList.push({ type: 'string', value: 'jchcppic', text: 'jchcppic', dictCode: '' })
      fieldList.push({ type: 'string', value: 'jcbfpic', text: 'jcbfpic', dictCode: '' })
      fieldList.push({ type: 'string', value: 'ccgkpic', text: 'ccgkpic', dictCode: '' })
      fieldList.push({ type: 'string', value: 'cccppic', text: 'cccppic', dictCode: '' })
      fieldList.push({ type: 'string', value: 'cchcppic', text: 'cchcppic', dictCode: '' })
      fieldList.push({ type: 'string', value: 'ccbfpic', text: 'ccbfpic', dictCode: '' })
      fieldList.push({ type: 'string', value: 'liaocangid', text: '卸货地点', dictCode: '' })
      fieldList.push({ type: 'string', value: 'serialno', text: 'serialno', dictCode: '' })
      fieldList.push({ type: 'string', value: 'reason', text: 'reason', dictCode: '' })
      fieldList.push({ type: 'string', value: 'fileUpload', text: 'fileUpload', dictCode: '' })
      fieldList.push({ type: 'string', value: 'istaizhangtj', text: 'istaizhangtj', dictCode: '' })
      fieldList.push({ type: 'string', value: 'songhuodanpic', text: '送货单', dictCode: '' })
      fieldList.push({ type: 'string', value: 'shifouhege', text: '是否合格', dictCode: '' })
      fieldList.push({ type: 'string', value: 'yuancaimiaoshu', text: '原材描述', dictCode: '' })
      fieldList.push({ type: 'string', value: 'beizhu', text: 'beizhu', dictCode: '' })
      fieldList.push({ type: 'string', value: 'jianlipic', text: 'jianlipic', dictCode: '' })
      fieldList.push({ type: 'string', value: 'sibanpic', text: 'sibanpic', dictCode: '' })
      this.superFieldList = fieldList
    }
  }
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>