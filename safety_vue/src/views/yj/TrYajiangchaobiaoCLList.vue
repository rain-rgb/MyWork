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
      <a-button @click="handleAdd" v-has="'tryj:add'" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" v-has="'tryj:dc'" icon="download" @click="handleExportXls('压浆主表信息')">导出</a-button>
      <a-upload name="file" v-has="'tryj:dr'" :showUploadList="false" :multiple="false" :headers="tokenHeader"
                :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
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
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a
        style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table
        ref="table"
        size="middle"
        :scroll="{x:true}"
        bordered
        rowKey="syjid"
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

        <span class="separate" slot="kongdaohao" slot-scope="text,record">
          <div v-for="(item,index) in record.yajiangs" :key="index">
             <a-divider style="margin:0;height:1px"/>
             <div>{{ item.kongdao }}</div>
          </div>
        </span>
        <template slot="overReason" slot-scope="text,record">
          <span style="color:red;">{{record.overReason}}</span>
        </template>
        <span class="separate" slot="yajiangsj" slot-scope="text,record">
          <div v-for="(item,index) in record.yajiangs" :key="index">
             <a-divider style="margin:0;height:1px"/>
             <div>{{ item.yajiangsj }}</div>
          </div>
        </span>
        <span slot="hege" slot-scope="status, record">
         <a-tag color="red">不合格</a-tag>
       </span>
        <span slot="tags1" slot-scope="tags1, record">
          <a-tag color="red" v-if="record.zhanglaYajiangOverHandler.overproofStatus === 0">未处理</a-tag>
          <a-tag color="orange" v-if="record.zhanglaYajiangOverHandler.overproofStatus === 10">监理核查中</a-tag>
          <a-tag color="green" v-if="record.zhanglaYajiangOverHandler.overproofStatus === 20">已闭合</a-tag>
          <a-tag color="red" v-if="record.zhanglaYajiangOverHandler.overproofStatus === 30">监理驳回</a-tag>
          <a-tag color="yellow" v-if="record.zhanglaYajiangOverHandler.overproofStatus === 40">指挥部核查中</a-tag>
          <a-tag color="red" v-if="record.zhanglaYajiangOverHandler.overproofStatus === 60">指挥部驳回</a-tag>
        </span>
        <span slot="tags2" slot-scope="tags2, record">
        <a-tag >{{record.zhanglaYajiangOverHandler.handlePerson}}</a-tag>
       </span>

        <span slot="tags3" slot-scope="tags3, record">
        <a-tag >{{record.zhanglaYajiangOverHandler.approvalPerson}}</a-tag>
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
          <a @click="showmadel1(record)">详情</a>
          <a-divider type="vertical"/>
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down"/></a>
            <a-menu slot="overlay">
              <a-menu-item v-has="'yajiang1:cz'" v-if="record.zhanglaYajiangOverHandler.overproofStatus === 0 || record.zhanglaYajiangOverHandler.overproofStatus === 30|| record.zhanglaYajiangOverHandler.overproofStatus === 60">
                <a @click="showmadelchuzhi(record)">施工处置</a>
              </a-menu-item>
              <a-menu-item v-has="'yajiang1:sh'" v-if="record.zhanglaYajiangOverHandler.overproofStatus === 10">
                <a @click="showmadelshenhe(record)">监理审核</a>
              </a-menu-item>
              <a-menu-item v-has="'yajiang1:sp'" v-if="record.zhanglaYajiangOverHandler.overproofStatus === 40">
                <a @click="showmadelshenpi(record)">指挥部审批</a>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>
    <shen-pi ref="shenpi" :type="type" @change="change"></shen-pi>
    <shen-he-two ref="shenhe" :type="type" @change="change"></shen-he-two>
    <chu-zhi-two ref="chuzhi" :type="type" @change="change"></chu-zhi-two>
    <TrYajiangMSModalChuZhiShenHeTwo ref="TrYajiangMSModalTwo"></TrYajiangMSModalChuZhiShenHeTwo>
    <tr-yajiang-m-modal ref="modalForm" @ok="modalFormOk"></tr-yajiang-m-modal>
  </a-card>
</template>

<script>

import '@/assets/less/TableExpand.less'
import { mixinDevice } from '@/utils/mixin'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import TrYajiangMModal from './modules/TrYajiangMModal'
import JSuperQuery from '@/components/jeecg/JSuperQuery.vue'
import { usershebeiList } from '@api/api'
import TrYajiangMSModalChuZhiShenHeTwo from '@views/yj/modules/TrYajiangMSModalChuZhiShenHeTwo'
import Vue from 'vue'
// import ChuZhi from '@views/yj/czsh/ChuZhi'
// import ShenHe from '@views/yj/czsh/ShenHe'
// import BoHui from '@views/yj/czsh/BoHui'
import ChuZhiTwo from '@views/yj/czsh/ChuZhiTwo'
import ShenHeTwo from '@views/yj/czsh/ShenHeTwo'
import ShenPi from '@views/yj/czsh/ShenPi'
export default {
  name: 'TrYajiangchaobiaoCLList',
  mixins: [JeecgListMixin, mixinDevice],
  components: {
    TrYajiangMModal,
    JSuperQuery,
    TrYajiangMSModalChuZhiShenHeTwo,
    ChuZhiTwo,
    ShenHeTwo,
    ShenPi
  },
  data() {
    return {
      dictOption: [],
      selectValue: '',
      description: '压浆主表信息管理页面',
      // 表头
      columns: [
        {
          title: '#',
          dataIndex: '',
          key: 'rowIndex',
          align: 'center',
          customRender: function (t, r, index) {
            return parseInt(index) + 1
          }
        },
        {
          title: '工程名称',
          align: 'center',
          dataIndex: 'gcmc'
        },
        {
          title: '设备名称',
          align: 'center',
          dataIndex: 'yjsbbh'
        },
        // {
        //   title: '工程部位',
        //   align: 'center',
        //   dataIndex: 'gjbh'
        // },
        {
          title: '梁号',
          align: 'center',
          dataIndex: 'lianghao'
        },
        {
          title: '施工部位',
          align: 'center',
          dataIndex: 'sgbw'
        },
        // {
        //   title: '张拉时间',
        //   align: 'center',
        //   dataIndex: 'zlsj'
        // },
        {
          title: '孔道数',
          align: 'center',
          dataIndex: 'kongdaoshu'
        },
        {
          title: '压浆时间',
          align: 'center',
          dataIndex: 'yajiangsj',
          scopedSlots: { customRender: 'yajiangsj' },
        },
        {
          title: '孔道号',
          align: 'center',
          dataIndex: 'kongdaohao',
          scopedSlots: { customRender: 'kongdaohao' },
        },
        {
          title: '超标原因',
          align: 'center',
          dataIndex: 'overReason',
          scopedSlots: { customRender: 'overReason' },
        },
        {
          title: '是否合格',
          align: 'center',
          dataIndex: 'hege',
          scopedSlots: { customRender: 'hege' },
        },
        {
          title:'处理状态',
          align:"center",
          dataIndex: 'overproofStatus',
          key:'overproofStatus',
          scopedSlots: { customRender: 'tags1' },
        },
        {
          title:'处置人',
          align:"center",
          dataIndex: 'cementVariety',
          key:'cementVariety',
          scopedSlots: { customRender: 'tags2' },
        },
        {
          title:'审核人',
          align:"center",
          dataIndex: 'additiveVariety',
          key:'additiveVariety',
          scopedSlots: { customRender: 'tags3' },
        },
        {
          title: '操作',
          dataIndex: 'action',
          align: 'center',
          fixed: 'right',
          width: 147,
          scopedSlots: { customRender: 'action' }
        }
      ],
      url: {
        list: '/yajiangs/trYajiangS/list3',
        delete: '/yajiangm/trYajiangM/delete',
        deleteBatch: '/yajiangm/trYajiangM/deleteBatch',
        exportXlsUrl: '/yajiangm/trYajiangM/exportXls',
        importExcelUrl: 'yajiangm/trYajiangM/importExcel',

      },
      dictOptions: {},
      superFieldList: [],
      type: 1 //张拉0，压浆1
    }
  },
  created() {
    this.shebeiList()
    this.getSuperFieldList()
  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`
    },
  },
  methods: {
    showmadelchuzhi(record){
      this.$refs.chuzhi.showModal(record);
    },
    showmadelshenhe(record){
      this.$refs.shenhe.showModal(record);
    },
    showmadelshenpi(record){
      this.$refs.shenpi.showModal(record);
    },
    showmadel1(record) {
      this.$refs.TrYajiangMSModalTwo.title = '详情'
      this.$refs.TrYajiangMSModalTwo.callchuzhishenhe(record)
    },
    shebeiList: function () {
      var sys_depart_orgcode = Vue.ls.get('SYS_DEPART_ORGCODE')
      usershebeiList({
        sys_depart_orgcode: sys_depart_orgcode,
        sbtypes: '10'
      }).then(res => {
        //console.log(res)
        this.dictOption = []
        let result = res.result
        result.forEach(re => {
          this.dictOption.push({ text: re.sbname, value: re.sbjno })
        })

      })
    },
    initDictConfig() {
    },
    getSuperFieldList() {
      let fieldList = []
      fieldList.push({ type: 'string', value: 'syjid', text: '32位全球唯一码', dictCode: '' })
      fieldList.push({ type: 'string', value: 'sgdw', text: '施工单位', dictCode: '' })
      fieldList.push({ type: 'string', value: 'jldw', text: '监理单位', dictCode: '' })
      fieldList.push({ type: 'string', value: 'htbh', text: '合同号', dictCode: '' })
      fieldList.push({ type: 'string', value: 'gcmc', text: '工程名称', dictCode: '' })
      fieldList.push({ type: 'string', value: 'yjsj', text: '压浆时间', dictCode: '' })
      fieldList.push({ type: 'string', value: 'zhbw', text: '桩号及部位', dictCode: '' })
      fieldList.push({ type: 'string', value: 'sgbw', text: '施工部位', dictCode: '' })
      fieldList.push({ type: 'string', value: 'gjjg', text: '构件结构', dictCode: '' })
      fieldList.push({ type: 'string', value: 'gjbh', text: '构件编号及长度', dictCode: '' })
      fieldList.push({ type: 'string', value: 'qw', text: '气温', dictCode: '' })
      fieldList.push({ type: 'string', value: 'cjsjl', text: '掺减水剂量', dictCode: '' })
      fieldList.push({ type: 'string', value: 'cpzjl', text: '掺膨胀剂量', dictCode: '' })
      fieldList.push({ type: 'string', value: 'sw', text: '水温', dictCode: '' })
      fieldList.push({ type: 'string', value: 'shuijiaobi', text: '水胶比', dictCode: '' })
      fieldList.push({ type: 'string', value: 'snyl', text: '构件压浆水泥用量', dictCode: '' })
      fieldList.push({ type: 'string', value: 'yjwd', text: '压浆温度', dictCode: '' })
      fieldList.push({ type: 'string', value: 'msl', text: '泌水率', dictCode: '' })
      fieldList.push({ type: 'string', value: 'beiyong', text: '备用', dictCode: '' })
      fieldList.push({ type: 'string', value: 'yjsbbh', text: '压浆设备编号', dictCode: '' })
      fieldList.push({ type: 'string', value: 'lblx', text: '梁板类型', dictCode: '' })
      fieldList.push({ type: 'string', value: 'lianghao', text: '梁号', dictCode: '' })
      fieldList.push({ type: 'string', value: 'zlsj', text: '张拉时间', dictCode: '' })
      fieldList.push({ type: 'string', value: 'yajiangji', text: '压浆剂', dictCode: '' })
      fieldList.push({ type: 'string', value: 'snmc', text: '水泥名称', dictCode: '' })
      fieldList.push({ type: 'string', value: 'kongdaoshu', text: '孔道数', dictCode: '' })
      fieldList.push({ type: 'string', value: 'yajiangfang', text: '压浆方向', dictCode: '' })
      fieldList.push({ type: 'string', value: 'yajiangbuzh', text: '压浆步骤', dictCode: '' })
      fieldList.push({ type: 'string', value: 'yajiangguoc', text: '压浆过程', dictCode: '' })
      fieldList.push({ type: 'string', value: 'chushisudu', text: '初始流动速度', dictCode: '' })
      fieldList.push({ type: 'string', value: 'memo', text: '备注', dictCode: '' })
      fieldList.push({ type: 'string', value: 'liudongdu', text: 'liudongdu', dictCode: '' })
      fieldList.push({ type: 'string', value: 'status', text: '完成状态(0为未完成,状态为1代表', dictCode: '' })
      fieldList.push({ type: 'string', value: 'guid', text: '32位全球唯一码', dictCode: '' })
      fieldList.push({ type: 'string', value: 'uuid', text: '压浆任务单下发时任务单id（由设备返回）', dictCode: '' })
      fieldList.push({ type: 'string', value: 'issend', text: 'issend', dictCode: '' })
      this.superFieldList = fieldList
    },
    change(type) {
      if(type) this.loadData()
    }
  }
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>