<template>
  <a-card class="j-address-list-right-card-box" :loading="cardLoading" :bordered="false">
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="10">
          <a-col :md="6" :sm="8">
            <a-form-item label="计划名称" style="margin-left: 8px">
              <a-input
                placeholder="请输入计划名称查询"
                v-model="queryParam.planName"
              ></a-input>
            </a-form-item>
          </a-col>

          <a-col :md="6" :sm="8">
            <a-form-item label="开始时间" style="margin-left: 8px">
              <j-date
                v-model="queryParam.beginTime"
                :showTime="true"
                dateFormat="YYYY-MM-DD HH:mm:ss"
              />
            </a-form-item>
          </a-col>

          <template v-if="toggleSearchStatus">
            <a-col :md="6" :sm="8">
              <a-form-item label="结束时间" style="margin-left: 8px">
                <j-date
                  v-model="queryParam.endTime"
                  :showTime="true"
                  dateFormat="YYYY-MM-DD HH:mm:ss"
                />
              </a-form-item>
            </a-col>

            <a-col :md="6" :sm="8">
              <a-form-item label="审核状态">
                <j-dict-select-tag
                  v-model="queryParam.isPass"
                  placeholder="请选择审核状态"
                  dictCode="plan_status"
                />
              </a-form-item>
            </a-col>

            <a-col :md="6" :sm="8">
              <a-form-item label="落实状态">
                <j-dict-select-tag
                  v-model="queryParam.isHandle"
                  placeholder="请选择落实状态"
                  dictCode="swake_status"
                />
              </a-form-item>
            </a-col>
          </template>

          <a-col :md="6" :sm="8">
            <span
              style="float: left; overflow: hidden"
              class="table-page-search-submitButtons"
            >
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button
                type="primary"
                @click="searchReset"
                icon="reload"
                style="margin-left: 8px"
                >重置</a-button
              >
              <a @click="handleToggleSearch" style="margin-left: 8px">
                {{ toggleSearchStatus ? "收起" : "展开" }}
                <a-icon :type="toggleSearchStatus ? 'up' : 'down'" />
              </a>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <div class="table-operator" style="border-top: 5px">
      <a-button v-has="'plan:add'" @click="handleAdd" type="primary" icon="plus"
        >添加</a-button
      >
    </div>
    <a-table
      ref="table"
      size="middle"
      bordered
      rowKey="fireNumber"
      :pagination="ipagination"
      :columns="columns"
      :dataSource="dataSource"
      :loading="loading"
      @change="handleTableChange"
      :customRow="handleClick"
      :rowClassName="setRowClassName"
    >
      <a slot="name" slot-scope="text">{{ text }}</a>
      <span slot="tags" slot-scope="tags, record">
        <a-tag color="yellow" v-if="record.isPass == '0'">审核</a-tag>
        <a-tag color="orange" v-if="record.isPass == '1'">未审核</a-tag>
        <a-tag color="green" v-if="record.isPass == '2'">通过</a-tag>
        <a-tag color="red" v-if="record.isPass == '3'">未通过</a-tag>
      </span>
      <span slot="action" slot-scope="text, record">
        <a v-has="'plan:edit'" @click="handleEdit(record)">编辑</a>
        <a-divider type="vertical" />
        <a-dropdown v-has="'plan:del'">
          <a class="ant-dropdown-link"> 更多 <a-icon type="down" /> </a>
          <a-menu slot="overlay">
            <a-menu-item>
              <a href="javascript:;" @click="handleDetail(record)">详情</a>
            </a-menu-item>
            <a-menu-item v-has="'plan:del'">
              <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                <a>删除</a>
              </a-popconfirm>
            </a-menu-item>
          </a-menu>
        </a-dropdown>
      </span>
    </a-table>
    <management-modal ref="modalForm" @ok="modalFormOk"></management-modal>
  </a-card>
</template>

<script>
import JDate from "@/components/jeecg/JDate";
import ManagementModal from "./ManagementModal";
import { getAction, postAction } from "@/api/manage";
import { JeecgListMixin } from "@/mixins/JeecgListMixin";

export default {
  name: "AddressListRight",
  mixins: [JeecgListMixin],
  components: { JDate, ManagementModal },
  props: ["value"],
  data() {
    return {
      description: "用户信息",
      cardLoading: true,
      positionInfo: {},
      columns: [
        {
          title: "#",
          key: "rowIndex",
          dataIndex: "",
          width: 60,
          align: "center",
          customRender: (t, r, i) => parseInt(i) + 1,
        },
        {
          title: "计划名称",
          align: "center",
          dataIndex: "planName",
          scopedSlots: { customRender: "name" },
        },
        {
          title: "施工部位",
          align: "center",
          dataIndex: "consSite",
        },
        {
          title: "开始时间",
          align: "center",
          dataIndex: "beginTime",
        },
        {
          title: "结束时间",
          align: "center",
          dataIndex: "endTime",
        },
        {
          title: "计划发起人",
          align: "center",
          dataIndex: "planSponsor",
        },
        {
          title: "存在风险数",
          align: "center",
          dataIndex: "riskCount",
        },
        {
          title: "审核人",
          align: "center",
          dataIndex: "passPeople",
        },
        {
          title: "审核状态",
          align: "center",
          dataIndex: "isPass",
          scopedSlots: { customRender: "tags" },
        },
        {
          title: "操作",
          dataIndex: "action",
          scopedSlots: { customRender: "action" },
          align: "center",
          width: 170,
        },
      ],
      url: {
        list: "/safety/riskManagePlan/listByCritetia",
        listByPosition: "/sys/position/list",
      },
    };
  },
  watch: {
    value: {
      immediate: true,
      handler(projectNo) {
        this.dataSource = [];
        console.log(projectNo);
        this.loadData(1, projectNo);
      },
    },
  },
  created() {
    // this.queryPositionInfo()
  },
  methods: {
    loadData(pageNum, projectNo) {
      this.loading = true;
      if (pageNum === 1) {
        this.ipagination.current = 1;
      }
      debugger;
      // update-begin- --- author:wangshuai ------ date:20200102 ---- for:传过来的部门编码为空全查
      if (!projectNo) {
        postAction(this.url.list, {
          ...this.getQueryParams(),
        })
          .then((res) => {
            if (res.success) {
              console.log(res.result.records);
              // 此处修改数据来源文件 Mock数据
              this.dataSource = res.result.records;
              this.ipagination.total = res.result.total;
            }
          })
          .finally(() => {
            this.loading = false;
            this.cardLoading = false;
          });
        // update-end- --- author:wangshuai ------ date:20200102 ---- for:传过来的部门编码为空全查
      } else {
        //加载数据 若传入参数1则加载第一页的内容
        postAction(this.url.list, {
          projectNo,
          ...this.getQueryParams(),
        })
          .then((res) => {
            if (res.success) {
              console.log(res.result.records);
              this.dataSource = res.result.records;
              this.ipagination.total = res.result.total;
            }
          })
          .finally(() => {
            this.loading = false;
            this.cardLoading = false;
          });
      }
    },

    searchQuery() {
      this.loadData(1, this.value);
    },
    searchReset() {
      this.queryParam = {};
      this.loadData(1, this.value);
    },

    handleTableChange(pagination, filters, sorter) {
      debugger;
      if (Object.keys(sorter).length > 0) {
        this.isorter.column = sorter.field;
        this.isorter.order = "ascend" === sorter.order ? "asc" : "desc";
      }
      // 待确认
      this.ipagination = pagination;
      this.loadData(null, this.value);
    },

    // // 查询职务信息
    // queryPositionInfo() {
    //   getAction(this.url.listByPosition, { pageSize: 99999 }).then(res => {
    //     if (res.success) {
    //       let positionInfo = {}
    //       res.result.records.forEach(record => {
    //         positionInfo[record['code']] = record['name']
    //       })
    //       this.positionInfo = positionInfo
    //     }
    //   })
    // }
  },
};
</script>
<style>
.j-address-list-right-card-box .ant-table-placeholder {
  min-height: 46px;
}
</style>
<style scoped>
.j-address-list-right-card-box {
  height: 100%;
  min-height: 300px;
}
</style>
