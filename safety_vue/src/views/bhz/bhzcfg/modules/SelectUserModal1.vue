<template>
  <div>
    <a-modal
      centered
      :title="title"
      :width="1000"
      :visible="visible"
      @ok="handleOk"
      @cancel="handleCancel"
      cancelText="关闭">


      <!-- 查询区域 -->
      <div class="table-page-search-wrapper">
        <a-form layout="inline"  @keyup.enter.native="searchQuery">
          <a-row :gutter="24">

            <a-col :span="10">
              <a-form-item label="用户账号">
                <a-input placeholder="请输入用户账号" v-model="queryParam.username"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="8">
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
        <a-table
          size="small"
          bordered
          rowKey="id"
          :columns="columns1"
          :dataSource="dataSource1"
          :pagination="ipagination"
          :loading="loading"
          :scroll="{ y: 240 }"
          :rowSelection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange, type: 'radio' }"
          @change="handleTableChange">

        </a-table>
      </div>
      <!-- table区域-end -->


    </a-modal>
  </div>
</template>

<script>
  import {filterObj} from '@/utils/util'
  import {getAction} from '@/api/manage'

  export default {
    name: "SelectUserModal1",
    data() {
      return {
        title: "添加已有用户",
        names: [],
        visible: false,
        placement: 'right',
        description: '',
        type: '',
        // 查询条件
        queryParam: {},
        // 表头
        columns1: [
          {
            title: '#',
            dataIndex: '',
            key: 'rowIndex',
            width: 50,
            align: "center",
            customRender: function (t, r, index) {
              return parseInt(index) + 1;
            }
          },
          {
            title: '用户账号',
            align: "center",
            width: 100,
            dataIndex: 'username'
          },
          {
            title: '用户名称',
            align: "center",
            width: 100,
            dataIndex: 'realname'
          },
          {
            title: '性别',
            align: "center",
            width: 100,
            dataIndex: 'sex_dictText'
          },
          {
            title: '电话',
            align: "center",
            width: 100,
            dataIndex: 'phone'
          },
          {
            title: '部门',
            align: "center",
            width: 150,
            dataIndex: 'orgCode'
          }
        ],
        columns2: [
          {
            title: '用户账号',
            align: "center",
            dataIndex: 'username',

          },
          {
            title: '用户名称',
            align: "center",
            dataIndex: 'realname',
          },
          {
            title: '操作',
            dataIndex: 'action',
            align: "center",
            width: 100,
            scopedSlots: {customRender: 'action'},
          }
        ],
        //数据集
        dataSource1: [],
        dataSource2: [],
        // 分页参数
        ipagination: {
          current: 1,
          pageSize: 10,
          pageSizeOptions: ['10', '20', '30'],
          showTotal: (total, range) => {
            return range[0] + "-" + range[1] + " 共" + total + "条"
          },
          showQuickJumper: true,
          showSizeChanger: true,
          total: 0
        },
        isorter: {
          column: 'createTime',
          order: 'desc',
        },
        loading: false,
        selectedRowKeys: [],
        selectionRows: [],
        url: {
          list: "/sys/user/list",
        }
      }
    },
    created() {
      this.loadData();
    },
    methods: {
      searchQuery() {
        this.loadData(1);
      },
      searchReset() {
        this.queryParam = {};
        this.loadData(1);
      },
      handleCancel() {
        this.visible = false;
      },
      handleOk() {
        let data = this.selectionRows[0].username;
        this.$emit("selectFinished", data, this.type);
        this.visible = false;
      },
      add() {
        this.visible = true;
      },
      loadData(arg) {
        //加载数据 若传入参数1则加载第一页的内容
        if (arg === 1) {
          this.ipagination.current = 1;
        }
        var params = this.getQueryParams();//查询条件
        getAction(this.url.list, params).then((res) => {
          if (res.success) {
            this.dataSource1 = res.result.records;
            this.ipagination.total = res.result.total;
          }
        })
      },
      getQueryParams() {
        var param = Object.assign({}, this.queryParam, this.isorter);
        param.field = this.getQueryField();
        param.pageNo = this.ipagination.current;
        param.pageSize = this.ipagination.pageSize;
        return filterObj(param);
      },
      getQueryField() {
        //TODO 字段权限控制
      },
      onSelectChange(selectedRowKeys, selectedRows) {
        this.selectedRowKeys = selectedRowKeys;
        this.selectionRows = selectedRows;
      },
      onClearSelected() {
        this.selectedRowKeys = [];
        this.selectionRows = [];
      },
      handleDelete: function (record) {
        this.dataSource2.splice(this.dataSource2.indexOf(record), 1);
      },
      handleTableChange(pagination, filters, sorter) {
        //分页、排序、筛选变化时触发
        console.log(sorter);
        //TODO 筛选
        if (Object.keys(sorter).length > 0) {
          this.isorter.column = sorter.field;
          this.isorter.order = "ascend" == sorter.order ? "asc" : "desc"
        }
        this.ipagination = pagination;
        this.loadData();
      }
    }
  }
</script>
<style lang="less" scoped>
  .ant-card-body .table-operator {
    margin-bottom: 18px;
  }

  .ant-table-tbody .ant-table-row td {
    padding-top: 15px;
    padding-bottom: 15px;
  }

  .anty-row-operator button {
    margin: 0 5px
  }

  .ant-btn-danger {
    background-color: #ffffff
  }

  .ant-modal-cust-warp {
    height: 100%
  }

  .ant-modal-cust-warp .ant-modal-body {
    height: calc(100% - 110px) !important;
    overflow-y: auto
  }

  .ant-modal-cust-warp .ant-modal-content {
    height: 90% !important;
    overflow-y: hidden
  }
</style>