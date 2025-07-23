<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="车辆信息">
              <a-input placeholder="车辆信息" v-model="vehicle1" @change="poureLocation"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="驾驶员">
              <a-input placeholder="驾驶员" v-model="driver1" @change="poureLocation2"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="签收状态">
              <j-dict-select-tag placeholder="请选择签收状态" v-model="queryParam.status"
                                 dictCode="car_status"></j-dict-select-tag>
            </a-form-item>

          </a-col>
          <a-col  :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="处置状态">
              <j-dict-select-tag placeholder="请选择处置状态" v-model="queryParam.overhandle"
                                 dictCode="overproof_status" @change="chaobiao"></j-dict-select-tag>
            </a-form-item>
          </a-col>
<!--          <a-col :xl="6" :lg="7" :md="8" :sm="24">-->
<!--            <a-form-item label="签收状态">-->
<!--              <j-dict-select-tag placeholder="请选择签收状态" v-model="queryParam.overLevel"></j-dict-select-tag>-->
<!--            </a-form-item>-->
<!--          </a-col>-->

        </a-row>
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="任务单号">
              <a-input placeholder="任务单号" v-model="code1" @change="codeChange"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="浇筑部位">
              <a-input placeholder="浇筑部位" v-model="siteName1" @change="siteNameChange"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="5" :lg="7" :md="8" :sm="24">
            <a-form-item label="发车时间">
              <j-date placeholder="开始时间" v-model="queryParam.dattim_begin" :showTime="true" dateFormat="YYYY-MM-DD"/>
            </a-form-item>
          </a-col>
          <a-col :xl="5" :lg="7" :md="8" :sm="24">
            <a-form-item label="">
              <j-date placeholder="结束时间" v-model="queryParam.dattim_end" :showTime="true" dateFormat="YYYY-MM-DD"/>
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
      <a-button @click="handleAdd" type="primary" icon="plus" v-has="'ysfachedan:add'">新增</a-button>
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

        <template slot="siteName" slot-scope="siteName,record">
          <div style="width: 450px;white-space: break-spaces;">{{siteName}}</div>
        </template>

        <span  style="display: flex;flex-direction: column;margin-bottom: 5px" slot="tags" slot-scope="tags, record">
          <a-tag  style="margin-bottom: 5px" color="orange" v-if="record.status == 0">未签收</a-tag>
          <a-tag style="margin-bottom: 5px" color="green" v-if="record.status == 1">已签收</a-tag>
          <a-tag  style="margin-bottom: 5px" color="red" v-if="record.status1 == 3">超时</a-tag>
          <a-tag  color="red" v-if=" record.status1 == 3 && record.overhandle == 0">未处置</a-tag>
          <a-tag  color="orange" v-if=" record.status1 == 3 && record.overhandle == 10">监理核查中</a-tag>
            <a-tag  color="green" v-if=" record.status1 == 3 && record.overhandle === 20">已闭合</a-tag>
          <a-tag color="red" v-if=" record.status1 == 3 && record.overhandle === 30">监理驳回</a-tag>
          <a-tag color="yellow" v-if=" record.status1 == 3 && record.overhandle === 40">指挥部核查中</a-tag>
          <a-tag color="red" v-if=" record.status1 == 3 && record.overhandle === 60">指挥部驳回</a-tag>
        </span>

        <span slot="action" slot-scope="text, record">
<!--          <a @click="handleEdit(record)" v-has="'ysfachedan:edit'">编辑</a>-->
          <a @click="handleDetail1(record)">车辆轨迹</a>
          <a-divider type="vertical"/>
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down"/></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="showmadel1(record)">详情</a>
              </a-menu-item>
                <a-menu-item v-has="'hntcar:cz'" v-show="record.status1 === 3 &&(record.overhandle === 0 ||record.overhandle === 30||record.overhandle === 60)">
                <a @click="showmadel2(record,1)">施工处置</a>
              </a-menu-item>
              <a-menu-item v-has="'hntcar:sh'" v-show="record.status1 === 3 &&(record.overhandle === 10 )">
                <a @click="showmadel2(record,2)">监理审核</a>
              </a-menu-item>
              <a-menu-item v-has="'hntcar:sp'" v-show="record.status1 === 3 &&(record.overhandle === 40)">
                <a @click="showmadel2(record,3)">指挥部核查</a>
              </a-menu-item>
              <a-menu-item  v-show="record.status1 === 3">
                <a @click="showInfo(record)">处理详情</a>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <y-s-scheduling-modal ref="YSSchedulingModal"></y-s-scheduling-modal>
    <y-s-scheduling-car-modal ref="modalForm" @ok="modalFormOk"></y-s-scheduling-car-modal>
    <ClxxRealdataModal ref="modalForm1" @ok="modalFormOk"></ClxxRealdataModal>
    <CarCBForm ref="carCBForm" @ok="modalFormOk"></CarCBForm>
    <CarChuZhiTwo ref="CarChuZhiTwo" @ok="modalFormOk"></CarChuZhiTwo>
  </a-card>

</template>

<script>

import '@/assets/less/TableExpand.less'
import { mixinDevice } from '@/utils/mixin'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import YSSchedulingCarModal from '@views/car/ysmanage/modules/YSSchedulingCarModal'
import YSSchedulingModal from '@views/car/ysmanage/modules/YSSchedulingModal'
import ClxxRealdataModal from './modules/ClxxRealdataModal'
import CarCBForm from "@views/car/ysmanage/modules/CarCBForm";
import CarChuZhiTwo from "@views/car/ysmanage/modules/CarChuZhiTwo";

export default {
  name: 'YSSchedulingCarCLList',
  mixins: [JeecgListMixin, mixinDevice],
  components: {
    YSSchedulingCarModal,
    YSSchedulingModal,
    ClxxRealdataModal,
    CarCBForm,
    CarChuZhiTwo
  },
  data() {
    return {
      description: '调度车辆管理页面',
      isorter: {
        column: 'id',
        order: 'desc',
        status1: 3,
        superQueryParams:"%5B%7B%22rule%22:%22ne%22,%22type%22:%22string%22,%22val%22:%2220%22,%22field%22:%22overhandle%22%7D%5D",
        superQueryMatchType: "and"
      },
      vehicle1:"",
      driver1:"",
      siteName1:"",
      code1:"",
      queryParam: {},
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
          title: '任务单号',
          align: 'center',
          dataIndex: 'code'
        },
        {
          title: '浇筑部位',
          align: 'center',
          dataIndex: 'siteName',
           scopedSlots: { customRender: 'siteName' }
        },
        {
          title: '运输方量',
          align: 'center',
          dataIndex: 'prodmete',
          customRender: function (text) {
            return !text ? '' : text+'m³'
          }
        },

        {
          title: '车牌信息',
          align: 'center',
          dataIndex: 'vehicle'
        },
        {
          title: '驾驶员',
          align: 'center',
          dataIndex: 'driver'
        },
        {
          title: '状态',
          align: 'center',
          dataIndex: 'status',
          scopedSlots: { customRender: 'tags' },
        },
        {
          title: '发车时间',
          align: 'center',
          dataIndex: 'dattim',
          width: 147,
          customRender: function (text) {
            return !text ? '' : (text.length > 10 ? text : text)
          }
        },
        {
          title: '签收时间',
          align: 'center',
          dataIndex: 'qianshoutime',
          width: 147,
          customRender: function (text) {
            return !text ? '' : (text.length > 10 ? text : text)
          }
        },
        {
          title: '签收人',
          align: 'center',
          dataIndex: 'receiver'
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
        list: '/car/schedulingCar/list47',
        delete: '/car/schedulingCar/delete',
        deleteBatch: '/car/schedulingCar/deleteBatch',
        exportXlsUrl: '/car/schedulingCar/exportXls',
        importExcelUrl: 'car/schedulingCar/importExcel',

      },
      dictOptions: {},
      superFieldList: [],
    }
  },
  created() {
    this.getSuperFieldList()
  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`
    },
  },
  methods: {
    chaobiao() {
      if(this.queryParam.overhandle != undefined && this.queryParam.overhandle !=""){
        this.queryParam.status1 = 3
      }else{
        this.queryParam.status1 = ""
      }

    },
    codeChange(){
      this.queryParam.code = '*'+ this.code1+'*'
    },
    siteNameChange(){
      this.queryParam.siteName = '*'+ this.siteName1+'*'
    },
    poureLocation() {
      this.queryParam.vehicle = '*'+ this.vehicle1+'*'
    },
    poureLocation2() {
      this.queryParam.driver = '*'+ this.driver1+'*'
    },
    showmadel1(record) {
      this.$refs.YSSchedulingModal.model = Object.assign({}, record)
      this.$refs.YSSchedulingModal.getList(record)
      this.$refs.YSSchedulingModal.visible = true
    },
    showmadel2(record,level) {
      this.$refs.CarChuZhiTwo.batchNo = record.id
      this.$refs.CarChuZhiTwo.showModal(record,level)
    },
    handleDetail1: function (record) {
      console.log(record)
      this.$refs.modalForm1.detail(record);
      this.$refs.modalForm1.title = "轨迹回放";
      this.$refs.modalForm1.disableSubmit = false;
    },
    showInfo:function (record) {
       // this.$refs.carCBForm.edit1(record);
      this.$refs.carCBForm.title = "处置详情";
      this.$refs.carCBForm.visible = true;
      this.$nextTick(()=>{
        this.$refs.carCBForm.edit1(record);
      })

    },
    initDictConfig() {
    },
    getSuperFieldList() {
      let fieldList = []
      fieldList.push({ type: 'int', value: 'erpid', text: 'Erp端ID', dictCode: '' })
      fieldList.push({ type: 'int', value: 'station', text: '生产线(1或2)', dictCode: '' })
      fieldList.push({ type: 'string', value: 'code', text: '任务单编号', dictCode: '' })
      fieldList.push({ type: 'string', value: 'recipe', text: '施工配合比编号', dictCode: '' })
      fieldList.push({ type: 'string', value: 'morrec', text: '砂浆配合比编号', dictCode: '' })
      fieldList.push({ type: 'date', value: 'dattim', text: '创建日期' })
      fieldList.push({ type: 'string', value: 'vehicle', text: '车辆编号', dictCode: '' })
      fieldList.push({ type: 'string', value: 'driver', text: '驾驶员', dictCode: '' })
      fieldList.push({ type: 'double', value: 'prodmete', text: '生产方量(含砂浆方量)', dictCode: '' })
      fieldList.push({ type: 'double', value: 'mormete', text: '砂浆方量', dictCode: '' })
      fieldList.push({ type: 'int', value: 'totvehs', text: '累计车次', dictCode: '' })
      fieldList.push({ type: 'double', value: 'totmete', text: '累计方量', dictCode: '' })
      fieldList.push({ type: 'string', value: 'qualitor', text: '质检员', dictCode: '' })
      fieldList.push({ type: 'string', value: 'flag', text: '标识', dictCode: '' })
      fieldList.push({ type: 'string', value: 'note', text: '备注', dictCode: '' })
      this.superFieldList = fieldList
    }
  }
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>