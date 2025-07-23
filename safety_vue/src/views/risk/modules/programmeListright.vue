<template>
  <a-card class="j-address-list-right-card-box" :loading="cardLoading" :bordered="false">
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="10">
          <a-col :md="6" :sm="12">
            <a-form-item label="名称" style="margin-left: 8px">
              <a-input
                placeholder="请输入名称查询"
                v-model="queryParam.planName"
              ></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="12">
            <a-form-item label="类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag
                v-model="queryParam.type"
                placeholder="请选择类型"
                dictCode="list_type"
              />
            </a-form-item>
          </a-col>
          <a-col :md="8" :sm="12">
            <a-form-item
              label="开始时间"
              style="margin-left: 8px"
              :wrapperCol="wrapperCol"
            >
              <j-date
                v-model="queryParam.beginTime"
                :showTime="true"
                dateFormat="YYYY-MM-DD HH:mm:ss"
              />
            </a-form-item>
          </a-col>

          <template v-if="toggleSearchStatus">
            <a-col :md="8" :sm="12">
              <a-form-item
                label="结束时间"
                style="margin-left: 8px"
                :wrapperCol="wrapperCol"
              >
                <j-date
                  v-model="queryParam.endTime"
                  :showTime="true"
                  dateFormat="YYYY-MM-DD HH:mm:ss"
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
      <a-button v-has="'program:add'" @click="handleAdd" type="primary" icon="plus"
        >添加</a-button
      >
    </div>
    <a-table
      ref="table"
      size="middle"
      bordered
      rowKey="planName"
      :pagination="ipagination"
      :columns="columns"
      :dataSource="dataSource"
      :loading="loading"
      @change="handleTableChange"
      :customRow="handleClick"
      :rowClassName="setRowClassName"
    >
      <span slot="action" slot-scope="text, record">
        <a v-has="'program:edit'" @click="handleEdit(record)">编辑</a>
        <a-divider type="vertical" />
        <a-dropdown v-has="'program:del'">
          <a class="ant-dropdown-link"> 更多 <a-icon type="down" /> </a>
          <a-menu slot="overlay">
            <!-- <a-menu-item>
                <a href="javascript:;" @click="handleDetail(record)">详情</a>
              </a-menu-item> -->
            <a-menu-item v-has="'program:del'">
              <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                <a>删除</a>
              </a-popconfirm>
            </a-menu-item>
          </a-menu>
        </a-dropdown>
      </span>
    </a-table>
    <programme-modal ref="modalForm" @ok="modalFormOk"></programme-modal>
  </a-card>
</template>
<script>
import JDate from "@/components/jeecg/JDate";
import ProgrammeModal from "./ProgrammeModal";
import { getAction } from "@/api/manage";
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import { getDictItemsFromCache } from "@/api/api";
export default {
  name: "ProgrammeListright",
  mixins: [JeecgListMixin],
  components: { JDate, ProgrammeModal },
  props: ["value"],
  data() {
    return {
      reslut: [],
      dataSource: [],
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
        },
        {
          title: "名称",
          align: "center",
          dataIndex: "uploadFilenames",
        },
        {
          title: "类型",
          align: "center",
          dataIndex: "type",
        },
        {
          title: "时间",
          align: "center",
          dataIndex: "time",
          customRender: (text) =>
            (text || "")
              .split(",")
              .map((t) => (this.positionInfo[t] ? this.positionInfo[t] : t))
              .join(","),
        },
        {
          title: "上传人",
          align: "center",
          dataIndex: "uploadBy",
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
        list: "/safety/specSafeConsScheme/listByCritetia",
        listByPosition: "/sys/position/list",
      },
      labelCol: {
        xs: { span: 24 },
        sm: { span: 5 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 },
      },
    };
  },
  watch: {
    value: {
      immediate: true,
      handler(orgCode) {
        this.dataSource = [];
        this.loadData(1, orgCode);
      },
    },
  },
  created() {
    this.queryPositionInfo();
  },
  methods: {
    loadData(pageNum, orgCode) {
      console.log(pageNum, orgCode);
      this.loading = true;
      if (pageNum === 1) {
        this.ipagination.current = 1;
      }
      // update-begin- --- author:wangshuai ------ date:20200102 ---- for:传过来的部门编码为空全查
      if (!orgCode) {
        getAction(this.url.list, {
          ...this.getQueryParams(),
        })
          .then((res) => {
            console.log(res);
            if (res.success) {
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
        getAction(this.url.list, {
          orgCode,
          ...this.getQueryParams(),
        })
          .then((res) => {
            console.log(res);
            if (res.success) {
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
    handleEdit: function (record) {
      console.log(record);
      this.$refs.modalForm.edit(record);
      this.$refs.modalForm.title = "编辑";
      this.$refs.modalForm.disableSubmit = false;
    },
    searchQuery() {
      console.log(this.value);
      this.loadData(1, this.value);
    },
    searchReset() {
      this.queryParam = {};

      this.loadData(1, this.value);
    },

    handleTableChange(pagination, filters, sorter) {
      if (Object.keys(sorter).length > 0) {
        this.isorter.column = sorter.field;
        this.isorter.order = "ascend" === sorter.order ? "asc" : "desc";
      }
      this.ipagination = pagination;
      this.loadData(null, this.value);
    },

    // 查询职务信息
    queryPositionInfo() {
      getAction(this.url.listByPosition, { pageSize: 99999 }).then((res) => {
        if (res.success) {
          let positionInfo = {};
          res.result.records.forEach((record) => {
            positionInfo[record["code"]] = record["name"];
          });
          this.positionInfo = positionInfo;
        }
      });
    },
    handleChange(info) {
      console.log(info.key);
    },
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
