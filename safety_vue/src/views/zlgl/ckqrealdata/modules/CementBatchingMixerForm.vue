<template>
  <a-spin :spinning="confirmLoading">
      <a-table
        ref="table"
        size="middle"
        :scroll="{ x: true }"
        bordered
        rowKey="materialName"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        class="j-table-force-nowrap"
        :customRow="handleClick"
        :rowClassName="setRowClassName"
      
      >
      </a-table>
  </a-spin>
</template>

<script>
import '@/assets/less/TableExpand.less'
import { mixinDevice } from '@/utils/mixin'
import JSuperQuery from '@/components/jeecg/JSuperQuery.vue'
import JFormContainer from '@/components/jeecg/JFormContainer'
import JDate from '@/components/jeecg/JDate'
import { getAction } from '@/api/manage'
export default {
  name: 'CementBatchingMixerForm',
  mixins: [mixinDevice],
  components: {
    JFormContainer,
    JDate,
    JSuperQuery,
  },
  data() {
    return {
      model: '',
      labelCol: {
        xs: { span: 24 },
        sm: { span: 6 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 },
      },
      labelCol2: {
        xs: { span: 24 },
        sm: { span: 3 },
      },
      wrapperCol2: {
        xs: { span: 24 },
        sm: { span: 20 },
      },
      columns: [
        {
          title: '材料名称',
          dataIndex: 'materialName',
          key: 'materialName',
          align: 'center',
          // scopedSlots: { customRender: 'materialname' },
        },
        {
          title: '实际值(施工)',
          dataIndex: 'realityCement',
          key: 'realityCement',
          align: 'center',
        },
        {
          title: '配比',
          dataIndex: 'theoryCement',
          key: 'theoryCement',
          align: 'center',
        },
        {
          title: '误差(kg)',
          dataIndex: 'errorValues',
          key: 'errorValues',
          align: 'center',
        },
        {
          title: '误差百分比(%)',
          dataIndex: 'errorRate',
          key: 'errorRate',
          align: 'center',
        },
      ],
      confirmLoading: false,
      loading: false,
      dataSource: [],
      ipagination: false,
      url: {
        list: '/ckqrealdata/deviceTrafficRealdata/list',
        delete: '/ckqrealdata/deviceTrafficRealdata/delete',
        deleteBatch: '/ckqrealdata/deviceTrafficRealdata/deleteBatch',
        exportXlsUrl: '/ckqrealdata/deviceTrafficRealdata/exportXls',
        importExcelUrl: 'ckqrealdata/deviceTrafficRealdata/importExcel',
        details: '/mixin/station/selectMixinStationDetails',
      },
    }
  },
  methods: {
    edit(record) {
      this.model = Object.assign({}, record)
      this.loaddata()
    },
    // 表格数据查询
    loaddata() {
      // console.log(this.model,'this.model')
      let params = { sid: this.model.sid,uploadTime:this.model.uploadTime }
      getAction(this.url.details, params).then((res) => {
        if (res.success) {
          console.log(res.result)
          this.dataSource = res.result.deviceMixinStationDetailsList
          let arr = res.result.id
          console.log(arr)
        }
      })
    },
    initDictConfig() {},
  },
}
</script>

<style scoped>
</style>