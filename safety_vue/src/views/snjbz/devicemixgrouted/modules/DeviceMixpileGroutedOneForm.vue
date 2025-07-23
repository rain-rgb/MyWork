<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
<!--          <a-col :span="12">-->
<!--            <a-form-model-item-->
<!--              label="设备编号"-->
<!--              :labelCol="labelCol"-->
<!--              :wrapperCol="wrapperCol"-->
<!--              prop="sbbh"-->
<!--            >-->
<!--              <a-input v-model="model.sbbh" placeholder="请输入设备编号"></a-input>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
          <a-col :span="12">
            <a-form-model-item
              label="里程"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              prop="pileMileage"
            >
              <a-input v-model="model.pileMileage" placeholder="请输入里程"></a-input>
            </a-form-model-item>
            <a-form-model-item
              label="桩号"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              prop="pileNo"
            >
              <a-input-number
                v-model="model.pileNo"
                placeholder="请输入桩号"
                style="width: 100%"
              />
            </a-form-model-item>
          </a-col>
          <a-col :span="12">

          </a-col>
          <a-col :span="12">
            <a-form-model-item
              label="设计深度"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              prop="pileDesigndep"
            >
              <a-input
                v-model="model.pileDesigndep"
                placeholder="请输入设计深度"
              ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item
              label="实际深度"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              prop="pileRealdep"
            >
              <a-input
                v-model="model.pileRealdep"
                placeholder="请输入实际深度"
              ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item
              label="灌浆时长"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              prop="pileWorktime"
            >
              <a-input
                v-model="model.pileWorktime"
                placeholder="请输入灌浆时长"
              ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item
              label="密度"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              prop="pileDensity"
            >
              <a-input
                v-model="model.pileDensity"
                placeholder="请输入密度"
              ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item
              label="结束时间"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              prop="pileTime"
            >
              <j-date
                placeholder="请选择成桩时间"
                v-model="model.pileTime"
                style="width: 100%"
              />
            </a-form-model-item>
          </a-col>

          <!-- <a-col :span="12">
            <a-form-model-item
              label="处置状态"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              prop="handlestate"
            >
              <a-input v-model="model.handlestate" placeholder="请输入处置状态"></a-input>
            </a-form-model-item>
          </a-col> -->
          <a-col :span="12">
            <a-form-model-item
              label="开始时间"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              prop="pileStarttime"
            >
              <j-date
                placeholder="请选择开始时间"
                v-model="model.pileStarttime"
                style="width: 100%"
              />
            </a-form-model-item>
          </a-col>
          <!-- <a-col :span="24">
            <a-form-model-item label="alertstate" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="alertstate">
              <a-input v-model="model.alertstate" placeholder="请输入alertstate"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="超标 0合格，1不合格" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="chaobiaodengji">
              <a-input v-model="model.chaobiaodengji" placeholder="请输入超标 0合格，1不合格"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="超标原因" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="problem">
              <a-input v-model="model.problem" placeholder="请输入超标原因"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="倾角-x" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pileX">
              <a-input v-model="model.pileX" placeholder="请输入倾角-x"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="倾角-y" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pileY">
              <a-input v-model="model.pileY" placeholder="请输入倾角-y"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="桩经度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pileLgd">
              <a-input v-model="model.pileLgd" placeholder="请输入桩经度"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="桩经度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pileLtd">
              <a-input v-model="model.pileLtd" placeholder="请输入桩经度"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="ts" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="ts">
              <a-input v-model="model.ts" placeholder="请输入ts"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="唯一码" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="uuid">
              <a-input v-model="model.uuid" placeholder="请输入唯一码"  ></a-input>
            </a-form-model-item>
          </a-col> -->
        </a-row>
      </a-form-model>
    </j-form-container>
    <div v-if="dataSource1.length > 0">
      <!-- <LineChartMultid1
        :title="dataSource1[0].name"
        :data-source="dataSource1"
      ></LineChartMultid1> -->
      <LineChartMultid
        :title="dataSource1[0].name"
        :data-source="dataList"
      ></LineChartMultid>
    </div>
  </a-spin>
</template>

<script>
import { httpAction, getAction } from "@/api/manage";
import { validateDuplicateValue } from "@/utils/util";
import LineChartMultid from "@comp/chart/snjbz";

export default {
  name: "DeviceMixpileGroutedOneForm",
  components: {
    LineChartMultid,
  },
  props: {
    //表单禁用
    disabled: {
      type: Boolean,
      default: false,
      required: false,
    },
  },
  data() {
    return {
      model: {},
      labelCol: {
        xs: { span: 12 },
        sm: { span: 8 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 15 },
      },
      confirmLoading: false,
      validatorRules: {},
      url: {
        add: "/grouted/deviceMixpileGroutedOne/add",
        edit: "/grouted/deviceMixpileGroutedOne/edit",
        queryById: "/grouted/deviceMixpileGroutedOne/queryById",
        list: "/grouted/deviceMixpileGroutedPart/list",
      },
      dataSource1: [],
      dataList: {
        dataSource1: [],
      },
    };
  },
  computed: {
    formDisabled() {
      return this.disabled;
    },
  },
  created() {
    //备份model原始值
    this.modelDefault = JSON.parse(JSON.stringify(this.model));
  },
  methods: {
    add() {
      this.edit(this.modelDefault);
    },
    edit(record) {
      this.model = Object.assign({}, record);
      console.log( this.model);
      this.visible = true;
      this.lzqx(); //力值过程值曲线图
    },
    lzqx: function () {
      //力值过程值曲线图
      // let params = { syjid: `6A83BE63-BC60-49FB-876F-4B8DAEE89716` };
      let params = { oneuuid: this.model.uuid, pageNo: 1, pageSize: 600 };
      getAction(this.url.list, params).then((res) => {
        this.dataSource1 = [];
        if (res.success) {
          let data = res.result.records || res.result;
         //  console.log(data, "data data");
          if (data.length > 0) {
            for (let i = 0; i < data.length; i++) {
              var pileDensity = data[i].pileDensity;
              var parttime = data[i].parttime;
              this.dataSource1.push({
                type: parttime,
                密度: parseFloat(pileDensity),
                name: "试件序号" + data[i].sjxh,
              });
            }
            this.dataList.dataSource1 = this.dataSource1;
          } else {
            this.$message.error("该设备的抗压力值没有数据");
          }
        }
      });
    },
    submitForm() {
      const that = this;
      // 触发表单验证
      this.$refs.form.validate((valid) => {
        if (valid) {
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
          httpAction(httpurl, this.model, method)
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
  },
};
</script>
