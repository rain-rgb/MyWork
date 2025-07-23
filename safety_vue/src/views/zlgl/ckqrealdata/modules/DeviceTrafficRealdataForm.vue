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
import { httpAction, getAction } from "@/api/manage";
import pick from "lodash.pick";
import { validateDuplicateValue } from "@/utils/util";
import JFormContainer from "@/components/jeecg/JFormContainer";
import JDate from "@/components/jeecg/JDate";

export default {
  name: "DeviceTrafficRealdataForm",
  components: {
    JFormContainer,
    JDate,
  },
  props: {
    //流程表单data
    formData: {
      type: Object,
      default: () => {},
      required: false,
    },
    //表单模式：true流程表单 false普通表单
    formBpm: {
      type: Boolean,
      default: false,
      required: false,
    },
    //表单禁用
    disabled: {
      type: Boolean,
      default: false,
      required: false,
    },
  },
  data() {
    return {
      loading: false,
      dataSource: [],
      ipagination: false,
      // form: this.$form.createForm(this),
      model: {},
      labelCol: {
        xs: { span: 24 },
        sm: { span: 5 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 },
      },
      confirmLoading: false,
      validatorRules: {},
      columns: [
        {
          title: "设备编号",
          dataIndex: "pid",
          key: "pid",
          align: "center",
          // scopedSlots: { customRender: 'materialname' },
        },
        {
          title: "流量计",
          dataIndex: "v",
          key: "v",
          align: "center",
        },
        {
          title: "数据上传时间",
          dataIndex: "datatime",
          key: "datatime",
          align: "center",
        },
      ],
      url: {
        add: "/ckqrealdata/deviceTrafficRealdata/add",
        edit: "/ckqrealdata/deviceTrafficRealdata/edit",
        queryById: "/ckqrealdata/deviceTrafficRealdata/queryById",
        details: "/grouting/pump/realTime/data/selectGroutingPumpPage",
      },
    };
  },
  methods: {
    add() {
      this.edit({});
    },
    edit(record) {
      this.model = Object.assign({}, record);
      this.loaddata();
    },
    // 表格数据查询
    loaddata() {
      // console.log(this.model,'this.model')
      let params = { sid: this.model.sid, pageNo: 1, pageSize: 10 };
      getAction(this.url.details, params).then((res) => {
        if (res.success) {
          console.log(res, "res");
          this.dataSource = res.deviceTrafficHistoryData;
        }
      });
    },
  },
};
</script>
