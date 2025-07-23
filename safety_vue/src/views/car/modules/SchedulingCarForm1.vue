<template>
  <div>
    <div class="topItem" v-if="dataSource.length > 0">
      <div class="item">车牌号： {{ dataList.vehicle }}</div>
      <div>司机： {{ dataList.driver }}</div>
      <div>发车时间 {{ dataList.dattim }}</div>
      <div>到达时间 {{ dataList.qianshoutime }}</div>
    </div>
    <el-table :data="dataSource" v-if="dataSource.length > 0">
      <el-table-column width="180" prop="productDatetime" label="出料时间" align="center">
      </el-table-column>
      <el-table-column width="80" prop="estimateNumber" label="生产量" align="center">
        <template slot-scope="scope"> {{ scope.row.estimateNumber }}m³ </template>
      </el-table-column>
      <el-table-column prop="detailLists" label="说明" align="center">
        <template slot-scope="scope">
          {{ scope.row.detailLists[0][`para`] }}<br />
          {{ scope.row.detailLists[1][`para`] }}<br />
          {{ scope.row.detailLists[2][`para`] }}<br />
          <!-- {{ scope.row.detailLists[3][`para`] }} -->
        </template>
      </el-table-column>
      <el-table-column
        v-for="(column, index) in columnsAll"
        :key="index"
        :label="column.title"
        :prop="column.prop"
        align="center"
      >
        <template slot-scope="scope">
          {{ scope.row.detailLists[0][column.title] }}<br />
          {{ scope.row.detailLists[1][column.title] }}<br />
          <span
            :class="
              scope.row.detailLists[3][column.title] == 0
                ? `green`
                : scope.row.detailLists[3][column.title] == 1
                ? `yellow`
                : scope.row.detailLists[3][column.title] == 2
                ? `orange`
                : `red`
            "
          >
            {{ scope.row.detailLists[2][column.title] }}
          </span>
          <br />
          <!-- {{ scope.row.detailLists[3][column.title] }} -->
        </template>
      </el-table-column>
    </el-table>
    <div class="btmItem" v-if="dataSource.length > 0">
      <div class="item">
        施工部位： {{ dataSource[0].jobLocation }} ~ {{ dataSource[0].poureLocation }}
      </div>
      <div>理论总值 {{ Number(onellz.toFixed(2)) }}kg</div>
      <div>实际总值 {{ Number(onesjz.toFixed(2)) }}kg</div>
      <div>总产量 {{ estimateNumber }}m³</div>
    </div>
    <div v-else class="noData">暂无数据</div>
    <!-- <a-table
      ref="table"
      size="middle"
      :scroll="{ x: true }"
      bordered
      rowKey="id"
      :columns="columnsAll"
      :dataSource="dataSource"
      :pagination="ipagination"
      :loading="loading"
      :rowSelection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
      class="j-table-force-nowrap"
      @change="handleTableChange"
    ></a-table> -->
    <!-- <a-table :columns="columns" :data-source="dataSource">
      <span class="separate" slot="骨料1" slot-scope="text, record">
        <div v-for="(item, index) in record.detailLists" :key="index">
          {{ columns }}
          <a-divider style="margin: 0; height: 1px" />
          <div>{{ item }}</div>
        </div>
      </span>
    </a-table> -->
  </div>
</template>

<script>
import { httpAction, getAction } from "@/api/manage";
import pick from "lodash.pick";
import { validateDuplicateValue } from "@/utils/util";
import JFormContainer from "@/components/jeecg/JFormContainer";
import JDate from "@/components/jeecg/JDate";
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import { mixinDevice } from "@/utils/mixin";
import SchedulingCarModal from "@views/car/modules/SchedulingCarModal";
import JSuperQuery from "@comp/jeecg/JSuperQuery";

export default {
  name: "SchedulingCarForm",
  mixins: [JeecgListMixin, mixinDevice],
  components: {
    JeecgListMixin,
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
      form: this.$form.createForm(this),
      dataSource: [],
      columns: [
        // {
        //   title: "盘数",
        //   dataIndex: "",
        //   key: "rowIndex",
        //   width: 60,
        //   align: "center",
        //   customRender: function (t, r, index) {
        //     return parseInt(index) + 1;
        //   },
        // },
        // {
        //   title: "出料时间",
        //   align: "center",
        //   dataIndex: "productDatetime",
        //   // customRender:function (text) {
        //   //   return !text?"":(text.length>10?text.substr(0,10):text)
        //   // }
        // },
        // {
        //   title: "出料时间",
        //   align: "center",
        //   dataIndex: "productDatetime",
        //   // customRender:function (text) {
        //   //   return !text?"":(text.length>10?text.substr(0,10):text)
        //   // }
        // },
      ],
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
      validatorRules: {
        erpid: {
          rules: [{ required: true, message: "请输入Erp端ID!" }],
        },
      },
      url: {
        add: "/car/schedulingCar/add",
        edit: "/car/schedulingCar/edit",
        queryById: "/car/schedulingCar/queryById",
      },
      columnsAll: [],
      dataList: [],
      onellz: 0,
      onesjz: 0,
      estimateNumber: 0,
    };
  },
  computed: {
    formDisabled() {
      if (this.formBpm === true) {
        if (this.formData.disabled === false) {
          return false;
        }
        return true;
      }
      return this.disabled;
    },
    showFlowSubmitButton() {
      if (this.formBpm === true) {
        if (this.formData.disabled === false) {
          return true;
        }
      }
      return false;
    },
  },
  created() {
    //如果是流程中表单，则需要加载流程表单data
    // this.showFlowData();
    // this.loadbhz(this.model.danhao);
  },
  methods: {
    loadbhz(danhao) {
      let params = {
        clientNo: danhao,
        pageSize: 20,
      };
      getAction("/hntbhz/bhzCementBase/list3car", params).then((res) => {
        if (res.success) {
          let result = res.result.records;
          this.dataSource = res.result.records;
          console.log(this.columns, result, "this.columns------------111");
          if (result !== []) {
            // this.columns = this.columns + result[0].detailcolumns;
            // this.columns = this.columns.concat(result[0].detailcolumns);
            this.columnsAll = this.columns.concat(result[0].detailcolumns);
            // console.log(this.columns, "this.columns------------222");
          }
          result.forEach((e) => {
            this.onellz += e.onellz;
            this.onesjz += e.onesjz;
            this.estimateNumber += e.estimateNumber;
          });
          // result[0].detailcolumns.forEach((e) => {
          //   const newColumn = {
          //     title: e.title,
          //     dataIndex: e.dataIndex,
          //     align: e.align,
          //     scopedSlots: { customRender: e.dataIndex },
          //   };
          //   this.columns.push(newColumn);
          // });
          // result.forEach((re) => {
          //   this.dictOption3.push({
          //     text: `${re.cailiaoname}(${re.guigexinghao})`,
          //     value: re.cailiaono,
          //   });
          // });
        }
      });
    },
    add() {
      this.edit({});
    },
    edit(record) {
      console.log(record, "record____________-----------------");
      this.dataList = record;
      this.loadbhz(record.danhao);
      // this.form.resetFields();
      // this.model = Object.assign({}, record);
      // this.visible = true;
      // this.$nextTick(() => {
      //   this.form.setFieldsValue(
      //     pick(
      //       this.model,
      //       "erpid",
      //       "station",
      //       "code",
      //       "recipe",
      //       "morrec",
      //       "dattim",
      //       "vehicle",
      //       "driver",
      //       "prodmete",
      //       "mormete",
      //       "totvehs",
      //       "totmete",
      //       "qualitor",
      //       "flag",
      //       "note",
      //       "danhao"
      //     )
      //   );
      // });
    },
    //渲染流程表单数据
    showFlowData() {
      if (this.formBpm === true) {
        let params = { id: this.formData.dataId };
        getAction(this.url.queryById, params).then((res) => {
          if (res.success) {
            this.edit(res.result);
          }
        });
      }
    },
    submitForm() {
      const that = this;
      // 触发表单验证
      this.form.validateFields((err, values) => {
        if (!err) {
          that.confirmLoading = true;
          let httpurl = "";
          let method = "";
          if (!this.model.id) {
            httpurl += this.url.add;
            method = "post";
          } else {
            httpurl += this.url.edit;
            method = "put";
          }
          let formData = Object.assign(this.model, values);
          console.log("表单提交数据", formData);
          httpAction(httpurl, formData, method)
            .then((res) => {
              if (res.success) {
                that.$message.success(res.message);
                that.$emit("ok");
              } else {
                that.$message.warning(res.message);
              }
            })
            .finally(() => {
              that.confirmLoading = false;
            });
        }
      });
    },
    popupCallback(row) {
      this.form.setFieldsValue(
        pick(
          row,
          "erpid",
          "station",
          "code",
          "recipe",
          "morrec",
          "dattim",
          "vehicle",
          "driver",
          "prodmete",
          "mormete",
          "totvehs",
          "totmete",
          "qualitor",
          "flag",
          "note"
        )
      );
    },
  },
};
</script>
<style lang="less" scoped>
.topItem,
.btmItem {
  display: flex;
  font-size: 20px;
  div {
    margin-right: 20px;
  }
}
.topItem {
  margin-bottom: 30px;
  .item {
    margin-right: 350px;
  }
}
.btmItem {
  margin-top: 30px;
  .item {
    margin-right: 300px;
  }
}
.green,
.yellow,
.orange,
.red {
  width: 100px;
  color: #ffffff;
  display: inline-block;
}
.green {
  background-color: #92d050;
}
.yellow {
  color: #2b2b2b;
  background-color: #ffff00;
}
.orange {
  background-color: #ffc000;
}
.red {
  background-color: #f60000;
}
.noData{
  text-align: center;
}
</style>
