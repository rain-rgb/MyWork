<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="设备名称">
              <j-search-select-tag placeholder="请选择设备名称" v-model="queryParam.shebeibianhao" :dictOptions="dictOption" >
              </j-search-select-tag>
              {{ selectValue }}
            </a-form-item>
          </a-col>
<!--          <a-col :xl="6" :lg="7" :md="8" :sm="24">-->
<!--            <a-form-item label="是否完成">-->
<!--              <j-dict-select-tag placeholder="请选择是否完成" v-model="queryParam.status"-->
<!--                                 dictCode="mstatus"></j-dict-select-tag>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
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
<!--      <a-button @click="handleAdd" v-has="'zlsy:add'" type="primary" icon="plus">新增</a-button>-->
<!--      <a-button type="primary" v-has="'zlsy:dc'" icon="download" @click="handleExportXls('张拉信息表')">导出</a-button>-->
<!--      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl"-->
<!--                @change="handleImportExcel">-->
<!--        <a-button type="primary" v-has="'zlsy:dr'" icon="import">导入</a-button>-->
<!--      </a-upload>-->
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
        :scroll="{x:true}"
        bordered
        rowKey="id"
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

        <!--        <span class="separate" slot="cement1" slot-scope="text,record" >-->
        <!--          <div style="margin:0;height:1px;width: 150px;word-wrap:anywhere" >-->
        <!--             <div>{{record.trZhanglaXxbList[0].gjbh}}</div>-->
        <!--          </div>-->
        <!--        </span>-->
        <!--        <span class="separate" slot="cement2" slot-scope="text,record" >-->
        <!--          <div v-for="(item,index) in record.trZhanglaXxbList">-->
        <!--             <div>{{item.yzlc}}</div>-->
        <!--          </div>-->
        <!--        </span>-->
        <span  class="separate" slot="cement3" slot-scope="text,record" >
          <div >{{record.trZhanglaXxbList[0].gjmc}}</div>
          <!--          <div v-for="(item,index) in record.trZhanglaXxbList" :key="index">-->
          <!--             -->
          <!--          </div>-->
        </span>
        <!--        <span class="separate" slot="cement4" slot-scope="text,record" >-->
        <!--          <div v-for="(item,index) in record.zhanglaMList" :key="index">-->
        <!--             <a-divider style="margin:0;height:1px" />-->
        <!--             <div>{{item.gsbh}}</div>-->
        <!--          </div>-->
        <!--        </span>-->
        <template slot="overReason" slot-scope="text,record">
          <a-tag color="yellow" v-if="record.trZhanglaXxbList[0].overLevel == 1">{{record.trZhanglaXxbList[0].overReason}}</a-tag>
          <a-tag color="orange" v-if="record.trZhanglaXxbList[0]. overLevel== 2">{{record.trZhanglaXxbList[0].overReason}}</a-tag>
          <a-tag color="red" v-if="record.trZhanglaXxbList[0].overLevel == 3">{{record.trZhanglaXxbList[0].overReason}}</a-tag>
        </template>
        <!--        <span class="separate" slot="cement5" slot-scope="text,record" >-->
        <!--          <div v-for="(item,index) in record.zhanglaMList" :key="index">-->
        <!--             <a-divider style="margin:0;height:1px" />-->
        <!--             <div>{{item.zlsj}}</div>-->
        <!--          </div>-->
        <!--        </span>-->
        <!--        <span class="separate" slot="yxpc" slot-scope="text,record" >-->
        <!--          <div v-for="(item,index) in record.zhanglaMList" :key="index">-->
        <!--             <a-divider style="margin:0;height:1px" />-->
        <!--             <div>{{item.yxpc}}</div>-->
        <!--          </div>-->
        <!--        </span>-->


        <span slot="hege" slot-scope="status, record">
          <a-tag color="green" v-if="record.overLevel == 0">合格</a-tag>
          <a-tag color="yellow" v-if="record.overLevel == 1">初级预警</a-tag>
          <a-tag color="orange" v-if="record.overLevel == 2">中级预警</a-tag>
          <a-tag color="red" v-if="record.overLevel == 3">高级预警</a-tag>
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
          <a-divider  type="vertical"/>
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down"/></a>
            <a-menu slot="overlay">
              <a-menu-item v-has="'zhangla1:cz'" v-if="record.zhanglaYajiangOverHandler.overproofStatus === 0 || record.zhanglaYajiangOverHandler.overproofStatus === 30|| record.zhanglaYajiangOverHandler.overproofStatus === 60">
                <a @click="showmadelchuzhi(record)">施工处置</a>
              </a-menu-item>
              <a-menu-item v-has="'zhangla1:sh'" v-if="record.zhanglaYajiangOverHandler.overproofStatus === 10">
                <a @click="showmadelshenhe(record)" >监理审核</a>
              </a-menu-item>
              <a-menu-item v-has="'zhangla1:sp'" v-if="record.zhanglaYajiangOverHandler.overproofStatus === 40">
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
    <TrZhanglaMModalChuZhiShenHeTwo ref="TrZhanglaMModalChuZhiShenHeTwo" ></TrZhanglaMModalChuZhiShenHeTwo>
    <tr-zhangla-xxb-modal ref="modalForm" @ok="modalFormOk"></tr-zhangla-xxb-modal>
  </a-card>
</template>

<script>

import '@assets/less/TableExpand.less'
import { mixinDevice } from '@/utils/mixin'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import TrZhanglaXxbModal from './modules/TrZhanglaXxbModal'
import JSuperQuery from '@comp/jeecg/JSuperQuery.vue'
import { usershebeiList } from '@api/api'
import TrZhanglaMModalChuZhiShenHeTwo from '@views/zl/modules/TrZhanglaMModalChuZhiShenHeTwo'
import Vue from 'vue'
import ChuZhiTwo from '@views/zl/czsh/ChuZhiTwo'
import ShenHeTwo from '@views/zl/czsh/ShenHeTwo'
import ShenPi from '@views/zl/czsh/ShenPi'
export default {
  name: 'TrZhanglachaobiaoCLList',
  mixins: [JeecgListMixin, mixinDevice],
  components: {
    TrZhanglaXxbModal,
    JSuperQuery,
    TrZhanglaMModalChuZhiShenHeTwo,
    ChuZhiTwo,
    ShenHeTwo,
    ShenPi
  },
  data() {
    return {
      dictOption: [],
      selectValue:'',
      description: '张拉信息表管理页面',
      // 表头
      columns: [
        {
          title: '序号',
          dataIndex: '',
          key: 'rowIndex',
          align: 'center',
          customRender: function (t, r, index) {
            return parseInt(index) + 1
          }
        },
        {
          title: '工程部位',
          align: 'center',
          width: 150,
          dataIndex: 'trZhanglaXxbList[0].gjbh',
          key:'trZhanglaXxbList[0].gjbh',
          ellipsis: true

        },
        {
          title: '张拉梁型',
          align: 'center',
          dataIndex: 'gjmc',
          scopedSlots: { customRender: 'cement3' },
        },
        {
          title: '设备名称',
          align: 'center',
          dataIndex: 'shebeibianhao'
        },
        {
          title: '延伸量误差',
          align: 'center',
          dataIndex: 'sclper'
        },
        // {
        //   title: '张拉时间',
        //   align: 'center',
        //   dataIndex: 'zlsj',
        //   scopedSlots: { customRender: 'cement5' },
        // },
        // {
        //   title: '允许偏差值',
        //   align: 'center',
        //   dataIndex: 'yxpc',
        //   scopedSlots: { customRender: 'yxpc' },
        // },
        // {
        //   title: '钢束编号',
        //   align: 'center',
        //   dataIndex: 'gsbh',
        //   scopedSlots: { customRender: 'cement4' },
        // },
        {
          title: '施工时间',
          align: 'center',
          dataIndex: 'trZhanglaXxbList[0].sgsj',
          key:'trZhanglaXxbList[0].sgsj',
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
        list: '/zhanglam/trZhanglaM/list547',
        delete: '/zhanglaxxb/trZhanglaXxb/delete',
        deleteBatch: '/zhanglaxxb/trZhanglaXxb/deleteBatch',
        exportXlsUrl: '/zhanglaxxb/trZhanglaXxb/exportXls',
        importExcelUrl: 'zhanglaxxb/trZhanglaXxb/importExcel',

      },
      dictOptions: {},
      superFieldList: [],
      syjid:'',
      flag:'',
      type: 0 //张拉0，压浆1
    }
  },
  created() {
    //this.getToken();
    this.getSuperFieldList()
    this.shebeiList();
  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`
    },
  },
  methods: {
    showmadel1(record){
      this.$refs.TrZhanglaMModalChuZhiShenHeTwo.title="详情";
      this.$refs.TrZhanglaMModalChuZhiShenHeTwo.callchuzhishenhe(record);
    },
    showmadelchuzhi(record){
      this.$refs.chuzhi.showModal(record);
    },
    showmadelshenhe(record){
      this.$refs.shenhe.showModal(record);
    },
    showmadelshenpi(record){
      this.$refs.shenpi.showModal(record);
    },
    shebeiList:function (){
      var sys_depart_orgcode=Vue.ls.get('SYS_DEPART_ORGCODE');
      usershebeiList({
        sys_depart_orgcode:sys_depart_orgcode,
        sbtypes:'9'
      }).then(res=>{
        //console.log(res)
        this.dictOption=[];
        let result=res.result;
        result.forEach(re=>{
          this.dictOption.push({text:re.sbname,value:re.sbjno})
        })

      })
    },
    initDictConfig() {
    },
    getSuperFieldList() {
      let fieldList = []
      fieldList.push({ type: 'string', value: 'syjid', text: '唯一码', dictCode: '' })
      fieldList.push({ type: 'string', value: 'gcmc', text: '工程名称', dictCode: '' })
      fieldList.push({ type: 'string', value: 'yzlc', text: '预制梁场', dictCode: '' })
      fieldList.push({ type: 'string', value: 'gjbh', text: '构件编号', dictCode: '' })
      fieldList.push({ type: 'string', value: 'shebeibianhao', text: '设备编号', dictCode: '' })
      fieldList.push({ type: 'string', value: 'gjmc', text: '构件名称', dictCode: '' })
      fieldList.push({ type: 'string', value: 'snsjqd', text: '砼设计强度', dictCode: '' })
      fieldList.push({ type: 'string', value: 'sgsj', text: '施工时间', dictCode: '' })
      fieldList.push({ type: 'string', value: 'snskqd', text: '砼试块强度', dictCode: '' })
      fieldList.push({ type: 'string', value: 'zly1', text: '张拉仪1编号', dictCode: '' })
      fieldList.push({ type: 'string', value: 'ybbh1', text: '油表编号1', dictCode: '' })
      fieldList.push({ type: 'string', value: 'bdrq1', text: '标定日期1', dictCode: '' })
      fieldList.push({ type: 'string', value: 'zly2', text: '张拉仪2编号', dictCode: '' })
      fieldList.push({ type: 'string', value: 'ybbh2', text: '油表编号2', dictCode: '' })
      fieldList.push({ type: 'string', value: 'bdrq2', text: '标定日期2', dictCode: '' })
      fieldList.push({ type: 'string', value: 'cbunit', text: '承包单位,施工单位', dictCode: '' })
      fieldList.push({ type: 'string', value: 'jlunit', text: '监理单位', dictCode: '' })
      fieldList.push({ type: 'string', value: 'htbh', text: '土建合同号', dictCode: '' })
      fieldList.push({ type: 'string', value: 'zlcsu', text: '张拉参数u', dictCode: '' })
      fieldList.push({ type: 'string', value: 'zlcsk', text: '张拉参数k', dictCode: '' })
      fieldList.push({ type: 'string', value: 'zlcsep', text: '张拉参数ep', dictCode: '' })
      fieldList.push({ type: 'string', value: 'zlbwpic', text: '张拉部位图片', dictCode: '' })
      fieldList.push({ type: 'string', value: 'kualiang', text: '跨梁', dictCode: '' })
      fieldList.push({ type: 'string', value: 'scljss', text: '伸长量计算式', dictCode: '' })
      fieldList.push({ type: 'string', value: 'fmqkms', text: '封锚情况描述', dictCode: '' })
      fieldList.push({ type: 'string', value: 'memo', text: '备注', dictCode: '' })
      fieldList.push({ type: 'string', value: 'status', text: '完成状态(0为未完成,状态为1代表', dictCode: '' })
      fieldList.push({ type: 'string', value: 'uuid', text: '张拉任务单号', dictCode: '' })
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