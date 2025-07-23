<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form :form="form" slot="detail">
        <a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item label="进场时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input
                  v-decorator="['jinchangshijian']"
                  placeholder="请输入进场时间"
                ></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="设备名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input
                  v-decorator="['shebeibianhao_dictText']"
                  placeholder="请输入设备编号"
                ></a-input>
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item label="料仓名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input
                  v-decorator="['lcbianhao_dictText']"
                  placeholder="请输入料仓编号"
                ></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="材料名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input
                  v-decorator="['cailiaono_dictText']"
                  placeholder="请输入材料编号"
                ></a-input>
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item label="毛重(吨)" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input
                  v-decorator="['maozhongt']"
                  placeholder="请输入毛重(吨)"
                ></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="皮重(吨)" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input
                  v-decorator="['pizhongt']"
                  placeholder="请输入皮重(吨)"
                ></a-input>
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item label="净重(吨)" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input
                  v-decorator="['jingzhongt']"
                  placeholder="请输入净重(吨)"
                ></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="批次" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['pici']" placeholder="请输入批次"></a-input>
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="24">
              <a-form-item label="规格类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input
                  v-decorator="['guigexinghao']"
                  placeholder="请输入规格类型"
                ></a-input>
              </a-form-item>
            </a-col>
          </a-row>

          <a-col v-if="showFlowSubmitButton" :span="24" style="text-align: center">
            <a-button @click="submitForm">提 交</a-button>
          </a-col>
        </a-row>
      </a-form>
    </j-form-container>
    <a-tabs v-model="activeKey">
      <a-tab-pane tab="明细" :key="refKeys[0]" :forceRender="true">
        <a-table
          :columns="columns"
          :dataSource="dataSource"
          :loading="loading"
          :pagination="false"
          class="j-table-force-nowrap"
          @change="handleTableChange"
          :customRow="handleClick"
          :rowClassName="setRowClassName"
        >
        </a-table>
      </a-tab-pane>
      <a-tab-pane key="2" tab="检测详情"></a-tab-pane>
      <a-tab-pane key="3" tab="使用详情"></a-tab-pane>
    </a-tabs>
  </a-spin>
</template>

<script>
import { httpAction, getAction } from "@/api/manage";
import pick from "lodash.pick";
import { validateDuplicateValue } from "@/utils/util";
import JFormContainer from "@/components/jeecg/JFormContainer";
import { FormTypes } from "@/utils/JEditableTableUtil";
import { JEditableTableMixin } from "@/mixins/JEditableTableMixin";
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import { mixinDevice } from "@/utils/mixin";

export default {
  name: "WztaizhangForm",
  mixins: [JeecgListMixin, mixinDevice],
  components: {
    JFormContainer,
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
      current: 1,
      num: 0,
      form: this.$form.createForm(this),
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
      refKeys: ["mingxi"],
      tableKeys: ["mingxi"],
      activeKey: "mingxi",
      loading: false,
      dataSource: [],
      columns: [
        //供应商名称	进场时间	出场时间	磅单号	车牌号	司磅员	毛重（吨）	皮重（吨）	净重（吨）
        {
          title: "供应商单位",
          dataIndex: "gongyingshangdanweibianma_dictText",
        },
        {
          title: "进场时间",
          dataIndex: "jinchangshijian",
        },
        {
          title: "出场时间",
          dataIndex: "chuchangshijian",
          type: FormTypes.normal,
        },
        {
          title: "进出材料单",
          dataIndex: "jinchuliaodanno",
          type: FormTypes.normal,
        },
        {
          title: "车牌号",
          dataIndex: "qianchepai",
          type: FormTypes.normal,
        },
        {
          title: "司磅员",
          dataIndex: "sibangyuan",
          type: FormTypes.normal,
        },
        {
          title: "毛重(吨)",
          dataIndex: "maozhongt",
          type: FormTypes.normal,
        },
        {
          title: "皮重(吨)",
          dataIndex: "pizhongt",
          type: FormTypes.normal,
        },
        {
          title: "净重(吨)",
          dataIndex: "jingzhongt",
          type: FormTypes.normal,
        },
      ],
      url: {
        mingxi: "/yclsl/wzycljinchanggb/list",
        add: "/wztaizhang/wztaizhang/add",
        edit: "/wztaizhang/wztaizhang/edit",
        queryById: "/wztaizhang/wztaizhang/queryById",
      },
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
    this.showFlowData();
  },
  methods: {
    add() {
      this.edit({});
    },
    edit(record) {
      this.form.resetFields();
      this.model = Object.assign({}, record);
      this.visible = true;
      this.$nextTick(() => {
        this.form.setFieldsValue(
          pick(
            this.model,
            "jinchangshijian",
            "shebeibianhao_dictText",
            "lcbianhao_dictText",
            "cailiaono_dictText",
            "maozhongt",
            "pizhongt",
            "jingzhongt",
            "pici",
            "guigexinghao"
          )
        );
        this.mingxis();
        // console.log(this.mingxi.dataSource, '调用')
      });
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
    mingxis: function () {
      //明细
      let params = { taizhangid: this.model.id, pageSize: 30 };
      getAction(this.url.mingxi, params).then((res) => {
        this.dataSource = [];
        if (res.success) {
          this.dataSource = res.result.records;
          console.log(this.dataSource, "明细");
        } else {
          this.$message.error("暂无明细！");
        }
      });
    },
    popupCallback(row) {
      this.form.setFieldsValue(
        pick(
          row,
          "jinchangshijian",
          "shebeibianhao_dictText",
          "lcbianhao_dictText",
          "cailiaono_dictText",
          "maozhongt",
          "pizhongt",
          "jingzhongt",
          "pici",
          "guigexinghao"
        )
      );
    },
  },
};
</script>
