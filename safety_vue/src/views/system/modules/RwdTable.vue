<template>
  <a-modal
    centered
    :title="title"
    :width="1200"
    :visible="visible"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭"
  >
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row >
          <a-col :xl="8" :lg="7" :md="8" :sm="8">
            <a-form-item label="浇筑部位">
              <a-input placeholder="请输入浇筑部位" v-model="conspos"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="8" :lg="7" :md="8" :sm="8">
            <a-form-item label="创建时间">
              <j-date
                placeholder="开始"
                v-model="queryParam.dattim_begin"
                :showTime="true"
                dateFormat="YYYY-MM-DD HH:mm:ss"
              />
            </a-form-item>
          </a-col>
          <a-col :xl="8" :lg="6" :md="6" :sm="6">
            <a-form-item label="">
              <j-date
                placeholder="结束"
                v-model="queryParam.dattim_end"
                :showTime="true"
                dateFormat="YYYY-MM-DD HH:mm:ss"
              />
            </a-form-item>
          </a-col>
        </a-row>
          <a-row>
          <a-col :xl="8" :lg="7" :md="8" :sm="8">
            <a-form-item label="强度等级">
              <j-dict-select-tag
                placeholder="请选择强度等级"
                v-model="queryParam.betlev"
                dictCode="betlev"
              ></j-dict-select-tag>
            </a-form-item>
          </a-col>
          <a-col :xl="8" :lg="7" :md="8" :sm="8">
            <a-form-item label="状态">
              <j-dict-select-tag
                placeholder="请选择状态"
                v-model="queryParam.status"
                dictCode="task_status"
              ></j-dict-select-tag>
            </a-form-item>
          </a-col>
          <a-col :xl="8" :lg="7" :md="8" :sm="8">
            <span style="float: left; overflow: hidden" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <!-- <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button> -->
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <div>
      <a-table
        ref="table"
        size="middle"
        :scroll="{ x: true }"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{ selectedRowKeys: selectedRowKeys1, onChange: onSelectChange1, type: 'radio' }"
        class="j-table-force-nowrap"
        @change="handleTableChange"
      >
        <span slot="status" slot-scope="status, record">
          <a-tag color="red" v-if="record.status == 0">待配料</a-tag>
          <a-tag color="green" v-if="record.status == 1">已通过</a-tag>
          <a-tag color="blue" v-if="record.status == 3">已配料</a-tag>
          <a-tag color="yellow" v-if="record.status == 4">生产中</a-tag>
          <a-tag color="green" v-if="record.status == 5">已完成</a-tag>
          <a-tag color="red" v-if="record.status == 6">滞后</a-tag>
        </span>
        <span slot="tags" slot-scope="tags, record">
          <a-tag color="blue">{{ record.code }}</a-tag>
        </span>
        <!-- <span slot="recipe" slot-scope="recipe, record">
                    <a-tag color="blue">{{ record.recipe }}</a-tag>
                </span>
                <span slot="recipes" slot-scope="recipes, record">
                    <a-tag color="blue">{{ record.recipes }}</a-tag>
                </span> -->
        <span slot="isdel" slot-scope="isdel, record">
          <a-tag color="blue" v-if="record.isdel == 0">有效</a-tag>
          <a-tag color="red" v-if="record.isdel == 1">无效</a-tag>
        </span>
      </a-table>
    </div>
  </a-modal>
</template>

<script>
import '@/assets/less/TableExpand.less'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import moment from 'moment'

export default {
  name: 'RwdTable',
  mixins: [JeecgListMixin],
  components: {},
  data() {
    return {
      selectedRowKeys1: [],
      model1: {},
      visible: false,
      title: '任务单浇筑令',
      conspos: '',
      queryParam: {
        isfinish:0,
        dattim_begin: moment().subtract(7, 'days').format('YYYY-MM-DD'),
        dattim_end: moment().format('YYYY-MM-DD'),
      },
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
          },
        },
        {
          title: '状态',
          align: 'center',
          dataIndex: 'status',
          key: 'status',
          scopedSlots: { customRender: 'status' },
          filters: [
            {
              text: '已通过',
              value: '1',
            },
          ],
          onFilter: (value, record) => record.status == value,
        },
        {
          title: '所属组织机构', //（0公用 1 第一生产线  2 第二生产线）
          align: 'center',
          dataIndex: 'sysOrgCode_dictText',
        },
        {
          title: '生产线', //（0公用 1 第一生产线  2 第二生产线）
          align: 'center',
          dataIndex: 'station_dictText',
        },
        {
          title: '任务单编号',
          align: 'center',
          dataIndex: 'code',
          key: 'code',
          scopedSlots: { customRender: 'tags' },
        },
        {
          title: '浇筑日期',
          align: 'center',
          dataIndex: 'begtim',
          // customRender: function (text) {
          //   return !text ? '' : (text.length > 10 ? text.substr(0, 10) : text)
          // }
        },
        // {
        //   title: '任务性质',
        //   align: 'center',
        //   dataIndex: 'attribute'
        // },
        // {
        //   title: '1线配合比编号',
        //   align: 'center',
        //   dataIndex: 'recipe',
        //   key: 'recipe',
        //   scopedSlots: { customRender: 'recipe' },
        // },
        // {
        //   title: '2线配合比编号',
        //   align: 'center',
        //   dataIndex: 'recipes',
        //   key: 'recipes',
        //   scopedSlots: { customRender: 'recipes' },
        // },
        // {
        //   title:'合同信息',
        //   align:"center",
        //   dataIndex: 'contract'
        // },
        // {
        //   title:'客户名称',
        //   align:"center",
        //   dataIndex: 'customer'
        // },
        {
          title: '工程名称',
          align: 'center',
          dataIndex: 'projname',
        },
        // {
        //   title: '工程类别',
        //   align: 'center',
        //   dataIndex: 'projtype'
        // },
        // {
        //   title:'工程级别',
        //   align:"center",
        //   dataIndex: 'projgrade'
        // },
        // {
        //   title:'开工面积',
        //   align:"center",
        //   dataIndex: 'projarea'
        // },
        // {
        //   title:'施工地址',
        //   align:"center",
        //   dataIndex: 'projadr'
        // },
        // {
        //   title:'运输距离',
        //   align:"center",
        //   dataIndex: 'distance'
        // },
        {
          title: '施工部位',
          align: 'center',
          dataIndex: 'conspos',
        },
        {
          title: '浇筑方式',
          align: 'center',
          dataIndex: 'pour',
        },
        // {
        //   title:'产品种类',
        //   align:"center",
        //   dataIndex: 'variety'
        // },
        {
          title: '强度等级',
          align: 'center',
          dataIndex: 'betlev',
        },
        {
          title: '抗渗等级',
          align: 'center',
          dataIndex: 'filter',
        },
        {
          title: '抗冻等级',
          align: 'center',
          dataIndex: 'freeze',
        },
        {
          title: '坍落度',
          align: 'center',
          dataIndex: 'lands',
        },
        // {
        //   title:'水泥品种',
        //   align:"center",
        //   dataIndex: 'cement'
        // },
        // {
        //   title:'石子种类',
        //   align:"center",
        //   dataIndex: 'stone'
        // },
        // {
        //   title:'骨料粒径',
        //   align:"center",
        //   dataIndex: 'bnsize'
        // },
        // {
        //   title:'外加剂要求',
        //   align:"center",
        //   dataIndex: 'addliq'
        // },
        // {
        //   title:'技术要求',
        //   align:"center",
        //   dataIndex: 'request'
        // },
        // {
        //   title: '搅拌时间',
        //   align: 'center',
        //   dataIndex: 'mixlast'
        // },
        {
          title: '任务方量',
          align: 'center',
          dataIndex: 'mete',
        },
        // {
        //   title: '浇注日期',
        //   align: 'center',
        //   dataIndex: 'begtim',
        //   customRender: function (text) {
        //     return !text ? '' : (text.length > 10 ? text.substr(0, 10) : text)
        //   }
        // },
        // {
        //   title: '截止日期',
        //   align: 'center',
        //   dataIndex: 'endtim',
        //   customRender: function (text) {
        //     return !text ? '' : (text.length > 10 ? text.substr(0, 10) : text)
        //   }
        // },
        // {
        //   title: '调度人员',
        //   align: 'center',
        //   dataIndex: 'attamper'
        // },
        // {
        //   title:'标识',
        //   align:"center",
        //   dataIndex: 'flag'
        // },
        // {
        //   title:'备注',
        //   align:"center",
        //   dataIndex: 'note'
        // },
        {
          title: '创建人',
          align: 'center',
          dataIndex: 'createBy',
        },
        // {
        //   title:'权限',
        //   align:"center",
        //   dataIndex: 'sysOrgCode'
        // },
        // {
        //   title:'是否删除 0未删除 1已删除',
        //   align:"center",
        //   dataIndex: 'isdel'
        // },
        {
          title: '是否有效',
          align: 'center',
          dataIndex: 'isdel',
          key: 'isdel',
          scopedSlots: { customRender: 'isdel' },
        },
      ],
      url: {
        list: '/system/bhzrenwudan/list',
      },
    }
  },
  methods: {
    onSelectChange1(selectedRowKeys, selectionRows) {
      // console.log(selectedRowKeys, selectionRows)
      this.selectedRowKeys1 = selectedRowKeys
      this.model1 = Object.assign({}, selectionRows[0])
      // console.log(this.model1)
      // this.loadData()
    },
    handleCancel() {
      this.visible = false
    },
    handleOk() {
      this.$emit('change', this.model1.code)
      this.visible = false
    },
  },
  watch: {
    conspos(val) {
      if (val) {
        this.queryParam.conspos = `*${val}*`
      } else {
        this.queryParam.conspos = undefined
      }
    },
  },
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>