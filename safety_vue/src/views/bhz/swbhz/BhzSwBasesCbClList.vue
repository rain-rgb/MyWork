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
            <a-form-item label="超标等级">
              <j-dict-select-tag placeholder="请选择超标等级" v-model="queryParam.chaobiaodengji"
                                 dictCode="over_level"></j-dict-select-tag>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="出料时间范围">
              <j-date placeholder="开始出料时间" v-model="queryParam.chuliaoshijian_begin" :showTime="true"
                      dateFormat="YYYY-MM-DD HH:mm:ss"/>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="">
              <j-date placeholder="结束出料时间" v-model="queryParam.chuliaoshijian_end" :showTime="true"
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
      <a-button @click="handleAdd" v-has="'swbhz:add'" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" v-has="'swbhz:dc'" @click="handleExportXls('水稳主表')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl"
                @change="handleImportExcel">
        <a-button type="primary" v-has="'swbhz:dr'" icon="import">导入</a-button>
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
        rowKey="id"
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
        <a-tag color="green" v-if="record.chaobiaodengji == '0'">合格</a-tag>
        <a-tag color="orange" v-if="record.chaobiaodengji == '1'">初级超标</a-tag>
        <a-tag color="yellow" v-if="record.chaobiaodengji == '2'">中级超标</a-tag>
         <a-tag color="red" v-if="record.chaobiaodengji == '3'">高级超标</a-tag>
       </span>
        <span slot="tags1" slot-scope="tags1, record">
          <a-tag color="red" v-if="record.bhzCementOverHandler.overproofStatus === 0">未处理</a-tag>
          <a-tag color="orange" v-if="record.bhzCementOverHandler.overproofStatus === 10">监理核查中</a-tag>
          <a-tag color="green" v-if="record.bhzCementOverHandler.overproofStatus === 20">已闭合</a-tag>
          <a-tag color="red" v-if="record.bhzCementOverHandler.overproofStatus === 30">监理驳回</a-tag>
          <a-tag color="yellow" v-if="record.bhzCementOverHandler.overproofStatus  === 40">指挥部核查中</a-tag>
          <a-tag color="yellow" v-if="record.bhzCementOverHandler.overproofStatus === 50">指挥部已审核</a-tag>
          <a-tag color="red" v-if="record.bhzCementOverHandler.overproofStatus === 60">指挥部驳回</a-tag>
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
          <a v-has="'swbhz:edit'" @click="handleEdit(record)">编辑</a>
          <a @click="handleDetail(record)">详情</a>
          <a-divider type="vertical"/>
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down"/></a>
            <a-menu slot="overlay" v-show="record.bhzCementOverHandler.overproofStatus !== 20">
              <a-menu-item v-has="'swbhz:cz'" v-show="record.bhzCementOverHandler.overproofStatus === 0 ||record.bhzCementOverHandler.overproofStatus === 30">
                <a @click="showmadel1(record)">施工处置</a>
              </a-menu-item>
              <a-menu-item v-has="'swbhz:sh'" v-show="record.bhzCementOverHandler.overproofStatus === 10 ||record.bhzCementOverHandler.overproofStatus === 60">
                <a @click="showmadel2(record)">监理审核</a>
              </a-menu-item>
              <a-menu-item v-has="'swbhz:sp'" v-show="record.bhzCementOverHandler.overproofStatus === 40">
                <a @click="showmadel(record)">指挥部审批</a>
              </a-menu-item>

              <a-menu-item v-has="'swbhz:del'">
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>
    <chu-zhi-two ref="modalChuZhi" :bhz="bhz" :batchNo="guid" :level="chaobiaodengji" @change="change"></chu-zhi-two>
    <shen-he-two ref="modalShenHe" :bhz="bhz" :batchNo="guid" :level="chaobiaodengji" @change="change2"></shen-he-two>
    <shen-pi ref="modalShenPi" :bhz="bhz" :batchNo="guid" :level="chaobiaodengji" @change="change3"></shen-pi>

    <bhz-sw-bases-c-b-c-x-modal ref="modalForm" @ok="modalFormOk"/>
  </a-card>
</template>

<script>

import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import BhzSwBasesModal from './modules/BhzSwBasesModal'
import JDictSelectTag from '@/components/dict/JDictSelectTag.vue'
import { filterMultiDictText } from '@/components/dict/JDictSelectUtil'
import '@/assets/less/TableExpand.less'
import JSuperQuery from '@/components/jeecg/JSuperQuery.vue'
import ChuZhiTwo from '@views/bhz/czsh/ChuZhiTwo'
import ShenHeTwo from '@views/bhz/czsh/ShenHeTwo'
import ShenPi from '@views/bhz/czsh/ShenPi'
import Vue from 'vue'
import { SYS_DEPART_ORGCODE } from '@/store/mutation-types'
import { usershebeiList } from '@api/api'
import BhzSwBasesCBCXModal from '@views/bhz/swbhz/modules/BhzSwBasesCBCXModal'
import { handertoken } from '@/mixins/getHanderToken'

export default {
  name: 'BhzSwBasesCbClList',
  mixins: [JeecgListMixin,handertoken],
  components: {
    BhzSwBasesCBCXModal,
    ChuZhiTwo,
    ShenPi,
    ShenHeTwo,
    JDictSelectTag,
    BhzSwBasesModal,
    JSuperQuery
  },
  data() {
    return {
      guid:'',
      chaobiaodengji: '',
      selectValue: '',
      asyncSelectValue: '',
      dictOption: [],
      // flag: 0,
      bhz:2,
      description: '水稳主表管理页面',
      isorter: {
        column: 'chuliaoshijian',
        order: 'desc',
      },
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
        // {
        //   title: '所属组织机构',
        //   align: "center",
        //   dataIndex: 'sysOrgCode_dictText'
        // },
        {
          title: '设备名称',
          align: 'center',
          dataIndex: 'shebeibianhao',
          key: 'shebeibianhao'
        },
        {
          title: '出料时间',
          align: 'center',
          dataIndex: 'chuliaoshijian',
          key: 'chuliaoshijian',
        },

        {
          title: '总产量',
          align: 'center',
          dataIndex: 'zongchanliang',
          key: 'zongchanliang',
          customRender: function (text) {
            return text.toFixed(2)
          }
        },

        // {
        //   title: '施工部位',
        //   align: 'center',
        //   dataIndex: 'poureLocation',
        //   key: 'poureLocation'
        // },
        // {
        //   title: '施工地点',
        //   align: 'center',
        //   dataIndex: 'jobLocation',
        //   key: 'jobLocation'
        // },
        // {
        //   title: '工程名称',
        //   align: 'center',
        //   dataIndex: 'projectName',
        //   key: 'projectName'
        // },
        // {
        //   title: '强度等级',
        //   align: 'center',
        //   dataIndex: 'strengthRank',
        //   key: 'strengthRank'
        // },

        {
          title: '配方号',
          align: 'center',
          dataIndex: 'formulaNo',
          key: 'formulaNo'
        },
        {
          title: '超标等级',
          align: 'center',
          dataIndex: 'chaobiaodengji',
          key: 'chaobiaodengji',
          scopedSlots: { customRender: 'tags' },
        },
        {
          title: '处理状态',
          align: 'center',
          dataIndex: 'bhzCementOverHandler.overproofStatus',
          key: 'bhzCementOverHandler.overproofStatus',
          scopedSlots: { customRender: 'tags1' },
        },
        {
          title: '处置人',
          align: 'center',
          dataIndex: 'bhzCementOverHandler.handlePerson',
          key: 'bhzCementOverHandler.handlePerson',
        },
        {
          title: '审核人',
          align: 'center',
          dataIndex: 'bhzCementOverHandler.approvalPerson',
          key: 'bhzCementOverHandler.approvalPerson'
        },
        {
          title: '操作',
          dataIndex: 'action',
          align: 'center',
          fixed: 'right',
          width: 147,
          scopedSlots: { customRender: 'action' },
        },
      ],
      url: {
        list: '/swbhz/bhzSwBases//czshList',
        delete: '/swbhz/bhzSwBases/delete',
        deleteBatch: '/swbhz/bhzSwBases/deleteBatch',
        exportXlsUrl: '/swbhz/bhzSwBases/exportXls',
        importExcelUrl: 'swbhz/bhzSwBases/importExcel',

      },
      dictOptions: {},
      superFieldList: [],
    }
  },
  created() {
    this.getToken()
    this.getSuperFieldList()
    this.shebeiList()
  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`
    }
  },
  methods: {
    showmadel: function (record) {
      this.bhz = 2
      this.guid = record.guid
      this.chaobiaodengji = record.chaobiaodengji
      this.$refs.modalShenPi.showModal(record)
    },
    showmadel1: function (record) {
      this.bhz = 2
      this.guid = record.guid
      this.chaobiaodengji = record.chaobiaodengji
      this.$refs.modalChuZhi.showModal(record)
    },
    showmadel2: function (record) {
      this.bhz = 2
      this.guid = record.guid
      this.chaobiaodengji = record.chaobiaodengji
      this.$refs.modalShenHe.showModal(record)
    },
    shebeiList: function () {
      var sys_depart_orgcode = Vue.ls.get('SYS_DEPART_ORGCODE')
      usershebeiList({
        sys_depart_orgcode: sys_depart_orgcode,
        sbtypes: '2'
      }).then(res => {
        this.dictOption = []
        let result = res.result
        result.forEach(re => {
          this.dictOption.push({ text: re.sbname, value: re.sbjno })
        })
        //console.log(res)
      })
    },
    initDictConfig() {
    },
    getSuperFieldList() {
      let fieldList = []
      fieldList.push({ type: 'string', value: 'hunheliaoid', text: '混合料编号', dictCode: '' })
      fieldList.push({ type: 'string', value: 'chuliaoshijian', text: '出料时间', dictCode: '' })
      fieldList.push({ type: 'string', value: 'yonghu', text: '用户', dictCode: '' })
      fieldList.push({ type: 'double', value: 'zongchanliang', text: '总产量', dictCode: '' })
      fieldList.push({ type: 'double', value: 'leijichanliang', text: '累计产量', dictCode: '' })
      fieldList.push({ type: 'string', value: 'shebeibianhao', text: '设备编号', dictCode: '' })
      fieldList.push({ type: 'string', value: 'clientNo', text: '客户端编号', dictCode: '' })
      fieldList.push({ type: 'string', value: 'baocunshijian', text: '保存时间', dictCode: '' })
      fieldList.push({ type: 'date', value: 'caijishijian', text: '采集时间' })
      fieldList.push({ type: 'int', value: 'banheshijian', text: '拌合时间', dictCode: '' })
      fieldList.push({ type: 'string', value: 'isdel', text: '是否删除', dictCode: '' })
      fieldList.push({ type: 'int', value: 'chaobiaodengji', text: '超标等级', dictCode: 'over_level' })
      fieldList.push({ type: 'string', value: 'poureLocation', text: '施工部位', dictCode: '' })
      fieldList.push({ type: 'string', value: 'jobLocation', text: '施工地点', dictCode: '' })
      fieldList.push({ type: 'string', value: 'ts', text: '时间戳', dictCode: '' })
      fieldList.push({ type: 'string', value: 'guid', text: '唯一标识', dictCode: '' })
      fieldList.push({ type: 'string', value: 'strengthRank', text: '强度等级', dictCode: '' })
      fieldList.push({ type: 'string', value: 'projectName', text: '工程名称', dictCode: '' })
      fieldList.push({ type: 'int', value: 'istongji', text: '是否统计', dictCode: '' })
      fieldList.push({ type: 'int', value: 'timechaobiao', text: '时间超标', dictCode: '' })
      fieldList.push({ type: 'int', value: 'cailiaochaobiao', text: '材料超标', dictCode: '' })
      fieldList.push({ type: 'int', value: 'alertstate', text: '是否判断超标', dictCode: '' })
      fieldList.push({ type: 'string', value: 'formulaNo', text: '配方号', dictCode: '' })
      fieldList.push({ type: 'string', value: 'beiy1', text: '备用1', dictCode: '' })
      fieldList.push({ type: 'string', value: 'beiy2', text: '备用2', dictCode: '' })
      fieldList.push({ type: 'string', value: 'beiy3', text: '备用3', dictCode: '' })
      fieldList.push({ type: 'string', value: 'sysOrgCode', text: '权限控制', dictCode: '' })
      this.superFieldList = fieldList
    },
    change(type) {
      if(type){
        this.loadData()// 施工处置后请求数据
      }
    },
    change2(type) {
      if(type){
        this.loadData()// 监理审核后请求数据
      }
    },
    change3(type){
      if(type){
        this.loadData()// 指挥部审批后请求数据
      }
    }
  }
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>