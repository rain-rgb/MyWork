<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form :form="form" slot="detail">
        <a-card
          title="填报"
          :bordered="false"
          :headStyle="{ color: '#0785fd' }"
          :bodyStyle="{ padding: '10' }"
        >
          <a-row>
            <a-col :span="12">
              <a-form-item
                label="任务单选择"
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
              >
                <div style="display: flex">
                  <a-button type="primary" @click="searchRwd" icon="search"
                  :disabled="model.bindrwd !== null"
                    >选择</a-button
                  >
                  <a-input
                    :disabled="true"
                    v-decorator="['uuid', validatorRules.uuid]"
                    placeholder="请输入任务单选择"
                  ></a-input>
                </div>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="部位名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input
                  :disabled="true"
                  v-if="model.bindrwd === null"
                  v-decorator="['sgbwname']"
                  placeholder=""
                ></a-input>
                <a-input
                v-else
                  :disabled="true"
                  v-decorator="['handleWay']"
                  placeholder=""
                ></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="说明" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-textarea
                  placeholder=""
                  v-decorator="['memo']"
                  v-if="model.bindrwd == null"
                  :auto-size="{ minRows: 5, maxRows: 20 }"
                ></a-textarea>
                <a-textarea
                  :disabled="true"
                  placeholder=""
                  v-decorator="['handleResult']"
                  v-else
                  :auto-size="{ minRows: 5, maxRows: 20 }"
                ></a-textarea>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="填报人" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input
                  v-decorator="['handlePerson']"
                  placeholder=""
                  :disabled="true"
                ></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="填报时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input
                  v-decorator="['handleTime']"
                  placeholder=""
                  :disabled="true"
                ></a-input>
              </a-form-item>
            </a-col>
            <a-col v-if="showFlowSubmitButton" :span="24" style="text-align: center">
              <a-button @click="submitForm">提 交</a-button>
            </a-col>
          </a-row>
        </a-card>
        <a-card
          title="审核"
          :bordered="false"
          :headStyle="{ color: '#0785fd' }"
          :bodyStyle="{ padding: '10' }"
          v-if="model.bindrwd != null"
        >
          <a-row>
            <a-col :span="12">
              <a-form-item label="意见" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-textarea
                  placeholder=""
                  v-decorator="['memo']"
                  v-if="model.bindrwd == 10"
                  :auto-size="{ minRows: 5, maxRows: 20 }"
                ></a-textarea>
                <a-textarea
                  :disabled="true"
                  placeholder=""
                  v-decorator="['headquartersApproval']"
                  v-else
                  :auto-size="{ minRows: 5, maxRows: 20 }"
                ></a-textarea>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="审核人" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input
                  v-decorator="['approvalPerson']"
                  placeholder=""
                  :disabled="true"
                ></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="审核时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input
                  v-decorator="['supervisorHandleTime']"
                  :disabled="true"
                ></a-input>
              </a-form-item>
            </a-col>
            <a-col v-if="showFlowSubmitButton" :span="24" style="text-align: center">
              <a-button @click="submitForm">提 交</a-button>
            </a-col>
          </a-row>
        </a-card>
      </a-form>
    </j-form-container>
    <RwdTableYJ ref="rwd" @change="handleChange"></RwdTableYJ>
  </a-spin>
</template>

<script>
import { httpAction, getAction } from "@api/manage";
import pick from "lodash.pick";
import { validateDuplicateValue } from "@/utils/util";
import JFormContainer from "@comp/jeecg/JFormContainer";
import RwdTableYJ from "./RwdTableYJ";

export default {
  name: "TrZhanglaXxbForm",
  components: {
    JFormContainer,
    RwdTableYJ,
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
        uuid: {
          rules: [
            {
              required: true,
              message: "请选择任务单!",
            },
          ],
        },
      },
      url: {
        add: "/zhanglaxxb/trZhanglaXxb/add",
        edit: "/yajiangm/trYajiangM/updateUse",
        queryById: "/zhanglaxxb/trZhanglaXxb/queryById",
        detail: "/zhanglam/zhanglaYajiangOverHandler/list",
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
      console.log(this.model, "this.model---------");
      
      this.visible = true;
      this.$nextTick(() => {
        // this.form.setFieldsValue(pick(this.model, "sgbwname", "uuid", "memo", "yzlc"));        
        this.form.setFieldsValue({
          ...pick(this.model, "sgbwname", "uuid", "yzlc"),
          memo: pick("", "memo").memo
        });
        if (this.model.bindrwd !== null) {
          this.getDataList();
        }
      });
    },
    //渲染流程表单数据
    getDataList() {
      let params = { baseid: this.model.yjsbbh+this.model.id };
      getAction(this.url.detail, params).then((res) => {
        if (res.success) {
          let data = res.result.records[0];
          this.form.setFieldsValue(pick(data, "sgbwname", "uuid", "memo", "handleResult", "handlePerson", "handleTime", "headquartersApproval", "approvalPerson", "supervisorHandleTime","handleWay"));
        }
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
          console.log(formData, "formData");
          that.confirmLoading = true;
          let httpurl = "";
          let method = "";
          httpurl += this.url.edit;
          method = "post";
          // if (!this.model.id) {
          //   httpurl += this.url.add;
          //   method = "post";
          // } else {
          //   httpurl += this.url.edit;
          //   method = "post";
          // }
          let params = Object.assign(this.model, values);
          let formData = {
            id: params.id, //id
            yjsbbh: params.yjsbbh,
            uuid: params.uuid, //任务单号
            sgbw: params.sgbwname, //部位
            memo: params.memo, //意见说明
            bindrwd: params.bindrwd == null? 10 : params.bindrwd == 10 ? 20: "", //10 填报关联，20 审核完成
          };
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
          "sgbwname", "uuid", "memo", "yzlc"
        )
      );
    },
    searchRwd() {
      this.$refs.rwd.visible = true;
    },
    handleChange(dictOptions1) {
      // this.form.setFieldsValue(
      //   pick(
      //     dictOptions1,
      //     "sgbwname",
      //     "uuid"
      //   )
      // );

      //选择任务单编号显示工程名称及施工部位等信息
      // this.form.setFieldsValue({
      //   uuid: dictOptions1.uuid,
      // });
      this.model.uuid = dictOptions1.uuid;
      this.model.sgbwname = dictOptions1.sgbwname;
      this.form.setFieldsValue(pick(this.model, "sgbwname", "uuid"));
      console.log(dictOptions1, "dictOptions1 handleChange");
      console.log(this.model, "this.form handleChange");
      //console.log(dictOptions1);
      // let params = { code: dictOptions1 };
      // getAction(this.url.list, params).then((res) => {
      //   //console.log(res)
      //   if (res.success) {
      //     let list = res.result;
      //     // this.projgrade1 = list[0].projgrade;
      //     this.model.betlev = list[0].betlev; //强度等级
      //     this.model.filter = list[0].filter; //抗渗等级
      //     this.model.freeze = list[0].freeze; //抗冻等级
      //     this.model.lands = list[0].lands; //坍落度
      //     this.model.projname = list[0].projname; //工程名称
      //     this.model.conspos = list[0].conspos; //施工部位
      //     this.projgrade1 = this.model.conspos;
      //     this.model.customer = list[0].customer; //客户名称
      //     this.model.projadr = list[0].projadr; //客户名称
      //     this.model.pour = list[0].pour; //浇筑方式
      //     this.model.mete = list[0].mete; //计划方量
      //     this.model.jztime = list[0].begtim;
      //     this.model.station = list[0].station; //生产线
      //     this.form.setFieldsValue(
      //       pick(
      //         this.model,
      //         "betlev",
      //         "filter",
      //         "freeze",
      //         "lands",
      //         "projname",
      //         "conspos",
      //         "pour",
      //         "mete",
      //         "station",
      //         "customer",
      //         "jztime",
      //         "projadr"
      //       )
      //     );
      //     // //console.log(this.model.betlev,"lllllllllllllllllllllllll")
      //   }
      // });
    },
  },
};
</script>
