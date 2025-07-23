<template>
  <j-modal
    centered
    :title="title"
    :width="1000"
    :visible="visible"
    @ok="handleOk"
    @cancel="handleCancel"
    :okButtonProps="{ class: { 'jee-hidden': disableSubmit } }"
    cancelText="关闭"
  >
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="24">
          <a-col :span="10">
            <a-form-item label="用户账号">
              <a-input placeholder="请输入用户账号" v-model="username"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
            <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <a-table
      size="small"
      bordered
      rowKey="id"
      :columns="columns"
      :dataSource="dataSource"
      :pagination="ipagination"
      :loading="loading"
      :scroll="{ y: 240 }"
      :rowSelection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange, type: 'radio' }"
      @change="handleTableChange"
    >
    </a-table>
  </j-modal>
</template>
<script>
import { getAction, putAction } from '@/api/manage'

export default {
  name: 'InstructUser',
  components: {},
  data() {
    return {
      visible: false,
      title: '用户',
      loading: false,
      disableSubmit: false,
      username: '',
      type: null,
      // 表头
      columns: [
        {
          title: '#',
          dataIndex: '',
          key: 'rowIndex',
          width: 50,
          align: 'center',
          customRender: function (t, r, index) {
            return parseInt(index) + 1
          },
        },
        {
          title: '用户账号',
          align: 'center',
          width: 100,
          dataIndex: 'username',
        },
        {
          title: '用户名称',
          align: 'center',
          width: 100,
          dataIndex: 'realname',
        },
        {
          title: '性别',
          align: 'center',
          width: 100,
          dataIndex: 'sex_dictText',
        },
        {
          title: '电话',
          align: 'center',
          width: 100,
          dataIndex: 'phone',
        },
        {
          title: '部门',
          align: 'center',
          width: 150,
          dataIndex: 'orgCode',
        },
      ],
      dataSource: [],
      ipagination: {
        current: 1,
        pageSize: 10,
        pageSizeOptions: ['10', '20', '30'],
        showTotal: (total, range) => {
          return range[0] + '-' + range[1] + ' 共' + total + '条'
        },
        showQuickJumper: true,
        showSizeChanger: true,
        total: 0,
      },
      selectedRowKeys: [],
      selectionRows: [],
      url: {
        list: '/sys/user/list',
      },
    }
  },
  mounted() {
    this.getData()
  },
  computed: {},
  methods: {
    handleCancel() {
      this.visible = false
    },
    handleOk() {
      let data = this.selectionRows[0];
      this.$emit('ok', data, this.type)
      this.visible = false
    },
    getData() {
      this.loading = true
      let params = {
        pageNo: this.ipagination.current,
        pageSize: this.ipagination.pageSize,
        username: this.username,
        orgcode: 'A05A01A02A01A01*',
      }
      getAction(this.url.list, params)
        .then((res) => {
          if (res.success) {
            this.dataSource = res.result.records
            this.ipagination.total = res.result.total
          }
        })
        .finally(() => {
          this.loading = false
        })
    },
    searchQuery() {
      this.ipagination.current = 1
      this.getData()
    },
    searchReset() {
      this.username = ''
      this.ipagination.current = 1
      this.getData()
    },
    onSelectChange(selectedRowKeys, selectedRows) {
      this.selectedRowKeys = selectedRowKeys
      this.selectionRows = selectedRows
    },
    handleTableChange(pagination) {
      this.ipagination = pagination
      this.getData()
    },
  },
}
</script>
<style lang="less" scoped>
</style>