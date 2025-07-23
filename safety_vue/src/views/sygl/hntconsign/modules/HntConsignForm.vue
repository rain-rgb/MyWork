<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form :form="form" slot="detail">
        <a-row>
          <!--          <a-col :span="24">-->
          <!--            <a-form-item label="委托ID&#45;&#45;guid" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <a-input v-decorator="['wtid']" placeholder="请输入委托ID&#45;&#45;guid"  ></a-input>-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <a-row>
            <a-col :span="12">
              <a-form-item label="组织机构" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input
                  v-decorator="['orgcode_dictText']"
                  placeholder="请输入组织机构"
                ></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="委托编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['wtbh']" placeholder="请输入委托编号"></a-input>
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item label="样品名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['ypmc']" placeholder="请输入样品名称"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="样品描述" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['ypms']" placeholder="请输入样品描述"></a-input>
              </a-form-item>
            </a-col>
          </a-row>

          <!--          <a-col :span="24">-->
          <!--            <a-form-item label="产地厂名（生产厂家）" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <a-input v-decorator="['cdcm']" placeholder="请输入产地厂名（生产厂家）"  ></a-input>-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <!--          <a-col :span="24">-->
          <!--            <a-form-item label="代表数量" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <a-input v-decorator="['sysl']" placeholder="请输入代表数量"  ></a-input>-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <!--          <a-col :span="24">-->
          <!--            <a-form-item label="取样地点\取样位置" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <a-input v-decorator="['qydd']" placeholder="请输入取样地点\取样位置"  ></a-input>-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <a-row>
            <a-col :span="12">
              <a-form-item label="取样日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['qyrq']" placeholder="请输入取样日期"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="取样人" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['qyr']" placeholder="请输入取样人"></a-input>
              </a-form-item>
            </a-col>
          </a-row>

          <!--          <a-col :span="24">-->
          <!--            <a-form-item label="配比通知单ID" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <a-input v-decorator="['phbtzdid']" placeholder="请输入配比通知单ID"  ></a-input>-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <a-row>
            <a-col :span="12">
              <a-form-item
                label="配比通知单编号"
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
              >
                <a-input
                  v-decorator="['phbtzdbh']"
                  placeholder="请输入配比通知单编号"
                ></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="工程名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['gcmc']" placeholder="请输入工程名称"></a-input>
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item label="施工部位" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['sgbw']" placeholder="请输入施工部位"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="试验类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number
                  v-decorator="['testtype_dictText']"
                  placeholder="请输入试验类型"
                  style="width: 100%"
                />
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item
                label="试验项目编号"
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
              >
                <a-input
                  v-decorator="['syxm']"
                  placeholder="请输入试验项目编号"
                ></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item
                label="试验项目名称"
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
              >
                <a-input
                  v-decorator="['syxmmc']"
                  placeholder="请输入试验项目名称"
                ></a-input>
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item
                label="样品试验组数"
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
              >
                <a-input
                  v-decorator="['sysuliang']"
                  placeholder="请输入样品试验组数"
                ></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="强度等级" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['qddj']" placeholder="请输入强度等级"></a-input>
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item
                label="混凝土种类"
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
              >
                <a-input v-decorator="['hntzl']" placeholder="请输入混凝土种类"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="样品龄期" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['yplq']" placeholder="请输入样品龄期"></a-input>
              </a-form-item>
            </a-col>
          </a-row>

          <!--          <a-col :span="24">-->
          <!--            <a-form-item label="搅拌方式（混凝土）" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <a-input v-decorator="['jbfs']" placeholder="请输入搅拌方式（混凝土）"  ></a-input>-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->

          <a-row>
            <a-col :span="12">
              <a-form-item label="样品备注" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['sybz']" placeholder="请输入样品备注"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item
                label="现场是否完成试验"
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
              >
                <a-input-number
                  v-decorator="['mstatus_dictText']"
                  placeholder="请输入现场是否完成试验"
                  style="width: 100%"
                />
              </a-form-item>
            </a-col>
          </a-row>

          <!--          <a-col :span="24">-->
          <!--            <a-form-item label="砂浆种类" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <a-input v-decorator="['sjzl']" placeholder="请输入砂浆种类"  ></a-input>-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <!--          <a-col :span="24">-->
          <!--            <a-form-item label="钢筋直径" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <a-input v-decorator="['gjzj']" placeholder="请输入钢筋直径"  ></a-input>-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <!--          <a-col :span="24">-->
          <!--            <a-form-item label="钢筋种类" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <a-input v-decorator="['gjzl']" placeholder="请输入钢筋种类"  ></a-input>-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <!--          <a-col :span="24">-->
          <!--            <a-form-item label="委托单位" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <a-input v-decorator="['wtdw']" placeholder="请输入委托单位"  ></a-input>-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <!--          <a-col :span="24">-->
          <!--            <a-form-item label="试验编号" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <a-input v-decorator="['sybh']" placeholder="请输入试验编号"  ></a-input>-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <!--          <a-col :span="24">-->
          <!--            <a-form-item label="试验单位" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <a-input v-decorator="['sydw']" placeholder="请输入试验单位"  ></a-input>-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <!--          <a-col :span="24">-->
          <!--            <a-form-item label="规格种类" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <a-input v-decorator="['ggzl']" placeholder="请输入规格种类"  ></a-input>-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <!--          <a-col :span="24">-->
          <!--            <a-form-item label="单位数量" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <a-input v-decorator="['ddsl']" placeholder="请输入单位数量"  ></a-input>-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <!--          <a-col :span="24">-->
          <!--            <a-form-item label="检验标准" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <a-input v-decorator="['jybz']" placeholder="请输入检验标准"  ></a-input>-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <!--          <a-col :span="24">-->
          <!--            <a-form-item label="ysyj" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <a-input v-decorator="['ysyj']" placeholder="请输入ysyj"  ></a-input>-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <!--          <a-col :span="24">-->
          <!--            <a-form-item label="试验日期" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <a-input v-decorator="['syrq']" placeholder="请输入试验日期"  ></a-input>-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <!--          <a-col :span="24">-->
          <!--            <a-form-item label="取证见证人" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <a-input v-decorator="['qyjzr']" placeholder="请输入取证见证人"  ></a-input>-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <!--          <a-col :span="24">-->
          <!--            <a-form-item label="委托人" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <a-input v-decorator="['wtr']" placeholder="请输入委托人"  ></a-input>-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <!--          <a-col :span="24">-->
          <!--            <a-form-item label="委托单位负责人" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <a-input v-decorator="['wtfzr']" placeholder="请输入委托单位负责人"  ></a-input>-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <!--          <a-col :span="24">-->
          <!--            <a-form-item label="地址及邮件" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <a-input v-decorator="['zzyb']" placeholder="请输入地址及邮件"  ></a-input>-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <!--          <a-col :span="24">-->
          <!--            <a-form-item label="联系电话" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <a-input v-decorator="['lldh']" placeholder="请输入联系电话"  ></a-input>-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <!--          <a-col :span="24">-->
          <!--            <a-form-item label="收样人" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <a-input v-decorator="['syr']" placeholder="请输入收样人"  ></a-input>-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <!--          <a-col :span="24">-->
          <!--            <a-form-item label="预计报告发出日期" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <a-input v-decorator="['yjbgrq']" placeholder="请输入预计报告发出日期"  ></a-input>-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <!--          <a-col :span="24">-->
          <!--            <a-form-item label="sfsyj" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <a-input-number v-decorator="['sfsyj']" placeholder="请输入sfsyj" style="width: 100%" />-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->

          <!--          <a-col :span="24">-->
          <!--            <a-form-item label="外键，组织机构id" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <a-input v-decorator="['departid']" placeholder="请输入外键，组织机构id"  ></a-input>-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <!--          <a-col :span="24">-->
          <!--            <a-form-item label="二维码，多项以逗号隔开，最后一个二维码不用加逗号" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <a-textarea v-decorator="['qrcode']" rows="4" placeholder="请输入二维码，多项以逗号隔开，最后一个二维码不用加逗号" />-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <a-row>
            <a-col :span="12">
              <a-form-item label="经度" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['samplejidu']" placeholder="请输入经度"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="纬度" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['sampleweidu']" placeholder="请输入纬度"></a-input>
              </a-form-item>
            </a-col>
          </a-row>

          <!--          <a-col :span="24">-->
          <!--            <a-form-item label="ts" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <a-input v-decorator="['ts']" placeholder="请输入ts"  ></a-input>-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <a-col :span="24">
            <a-form-item label="试验尺寸" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['chicun']" placeholder="请输入试验尺寸"></a-input>
            </a-form-item>
          </a-col>
          <!--          <a-col :span="24">-->
          <!--            <a-form-item label="判断试件的状态开始时间" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <j-date placeholder="请选择判断试件的状态开始时间" v-decorator="['starttime']" :trigger-change="true" style="width: 100%" />-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <!--          <a-col :span="24">-->
          <!--            <a-form-item label="判断试件的状态结束时间" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <j-date placeholder="请选择判断试件的状态结束时间" v-decorator="['endtime']" :trigger-change="true" style="width: 100%" />-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->

          <a-col v-if="showFlowSubmitButton" :span="24" style="text-align: center">
            <a-button @click="submitForm">提 交</a-button>
          </a-col>
        </a-row>
      </a-form>
    </j-form-container>

    <a-tabs default-active-key="1" :active-key="tabskey" @change="qiehuan">
      <a-tab-pane loading="true" tab="二维码" key="1">
        <div v-for="(item, index) in codeUrl" :key="index" style="float: left">
          <vue-qr
            :correctLevel="3"
            :autoColor="false"
            colorDark="black"
            :text="item.codes"
            :size="150"
            :margin="10"
          >
          </vue-qr>
          <div
            style="width: 150px; text-align: center; font-weight: bold; color: #cc0000"
          >
            {{ item.bh }}
          </div>
        </div>
      </a-tab-pane>

      <a-tab-pane loading="true" tab="养护" key="2" :forceRender="true">
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

      <a-tab-pane loading="true" tab="样品图" key="3">
        <!--        <viewer :images="yangpinList">-->
        <!--          <img-->
        <!--            v-for="(item,index) in yangpinList"-->
        <!--            :key="index"-->
        <!--            style="height:250px;width: 350px;margin: 5px 10px 5px 10px;float: left"-->
        <!--            :src="['http://web.traiot.cn/docs/wz/' + item.icon] "-->
        <!--            alt="">-->
        <!--        </viewer>-->
      </a-tab-pane>
    </a-tabs>
  </a-spin>
</template>

<script>
import { httpAction, getAction } from "@api/manage";
import pick from "lodash.pick";
import { validateDuplicateValue } from "@/utils/util";
import JFormContainer from "@comp/jeecg/JFormContainer";
import JDate from "@comp/jeecg/JDate";
import VueQr from "vue-qr";
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import { mixinDevice } from "@/utils/mixin";
import { FormTypes } from "@/utils/JEditableTableUtil";

export default {
  name: "HntConsignForm",
  mixins: [JeecgListMixin, mixinDevice],
  components: {
    JFormContainer,
    JDate,
    VueQr,
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
      yangpinList: [],
      tabskey: "1",
      codeUrl: [],
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
      // refKeys: ['list',],
      // tableKeys: ['list',],
      // activeKey: 'list',
      loading: false,
      dataSource: [],
      columns: [
        //委托编号	状态 	养护时间	进养护人	出养护室时间 	出养护人	取样人	取样时间	货架名称 货架所在层数 二维码处理字段 序号
        {
          title: "委托编号",
          dataIndex: "wtbh",
          type: FormTypes.normal,
        },
        {
          title: "试验状态",
          dataIndex: "cstatus_dictText",
        },
        {
          title: "养护时间",
          dataIndex: "jyhdate",
          type: FormTypes.normal,
        },
        {
          title: "进养护人",
          dataIndex: "jyhr",
          type: FormTypes.normal,
        },
        {
          title: "出养护室时间",
          dataIndex: "cyhdate",
          type: FormTypes.normal,
        },
        {
          title: "出养护人",
          dataIndex: "cyhr",
          type: FormTypes.normal,
        },
        {
          title: "取样人",
          dataIndex: "qyr",
          type: FormTypes.normal,
        },
        {
          title: "取样时间",
          dataIndex: "qydate",
          type: FormTypes.normal,
        },
        {
          title: "货架名称",
          dataIndex: "huojianname",
          type: FormTypes.normal,
        },
        {
          title: "货架所在层数",
          dataIndex: "huojiacenshu",
          type: FormTypes.normal,
        },
        // {
        //   title:'二维码处理字段',
        //   dataIndex: 'code',
        //   type: FormTypes.normal,
        // },
        {
          title: "序号",
          dataIndex: "no",
          type: FormTypes.normal,
        },
      ],
      url: {
        list1: "/hntconsignpic/hntConsignPic/list1",
        list: "/hntconsigncode/hntConsignCode/list",
        list2: "/hntconsigncode/hntConsignCode/list1",
        add: "/hntconsign/hntConsign/add",
        edit: "/hntconsign/hntConsign/edit",
        queryById: "/hntconsign/hntConsign/queryById",
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
            "wtid",
            "wtbh",
            "ypmc",
            "cdcm",
            "sysl",
            "qydd",
            "ypms",
            "qyrq",
            "qyr",
            "phbtzdid",
            "phbtzdbh",
            "gcmc",
            "sgbw",
            "testtype_dictText",
            "syxm",
            "syxmmc",
            "sysuliang",
            "qddj",
            "hntzl",
            "jbfs",
            "yplq",
            "sybz",
            "sjzl",
            "gjzj",
            "gjzl",
            "wtdw",
            "sybh",
            "sydw",
            "ggzl",
            "ddsl",
            "jybz",
            "ysyj",
            "syrq",
            "qyjzr",
            "wtr",
            "wtfzr",
            "zzyb",
            "lldh",
            "syr",
            "yjbgrq",
            "sfsyj",
            "mstatus_dictText",
            "departid",
            "orgcode_dictText",
            "qrcode",
            "samplejidu",
            "sampleweidu",
            "ts",
            "chicun",
            "starttime",
            "endtime"
          )
        );
        this.qrcode(); //二维码
        //this.getData();//样品图
        this.yanghu(); //养护
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
    qiehuan(key) {
      console.log(key);
      this.tabskey = key;
    },
    qrcode: function () {
      //二维码
      let params = { wtbh: this.model.wtbh };
      getAction(this.url.list2, params).then((res) => {
        this.codeUrl = [];
        if (res.success) {
          let data = res.result;
          console.log(data, "+______________________________+");
          if (data != null) {
            for (let i = 0; i < data.length; i++) {
              this.codeUrl.push({
                codes: data[i].code + "#" + data[i].codeno,
                bh: data[i].code,
              });
              console.log(this.codeUrl);
            }
          } else {
            this.$message.error("该见证取样没有二维码");
          }
        }
      });
    },
    getData: function () {
      //样品图
      let params = { wtbh: this.model.wtbh };
      getAction(this.url.list1, params).then((res) => {
        this.yangpinList = [];
        if (res.success) {
          let data = res.result;
          console.log(data, "+______________________________+");
          if (data[0].wtpic1 !== null && data[0].wtpic1 !== "") {
            this.yangpinList.push({ icon: data[0].wtpic1 });
          }
          if (data[0].wtpic2 !== null && data[0].wtpic2 !== "") {
            this.yangpinList.push({ icon: data[0].wtpic2 });
          }
          if (data[0].wtpic3 !== null && data[0].wtpic3 !== "") {
            this.yangpinList.push({ icon: data[0].wtpic3 });
          }
          if (data[0].wtpic4 !== null && data[0].wtpic4 !== "") {
            this.yangpinList.push({ icon: data[0].wtpic4 });
          }
          console.log(this.yangpinList, "++++++++++++++++");
        }
      });
    },
    yanghu: function () {
      //养护
      let params = { wtbh: this.model.wtbh, pageSize: 20 };
      getAction(this.url.list, params).then((res) => {
        this.dataSource = [];
        if (res.success) {
          this.dataSource = res.result.records;
          console.log(this.dataSource, "养护");
        } else {
          this.$message.error("暂无养护数据！");
        }
      });
    },
    popupCallback(row) {
      this.form.setFieldsValue(
        pick(
          row,
          "wtid",
          "wtbh",
          "ypmc",
          "cdcm",
          "sysl",
          "qydd",
          "ypms",
          "qyrq",
          "qyr",
          "phbtzdid",
          "phbtzdbh",
          "gcmc",
          "sgbw",
          "testtype_dictText",
          "syxm",
          "syxmmc",
          "sysuliang",
          "qddj",
          "hntzl",
          "jbfs",
          "yplq",
          "sybz",
          "sjzl",
          "gjzj",
          "gjzl",
          "wtdw",
          "sybh",
          "sydw",
          "ggzl",
          "ddsl",
          "jybz",
          "ysyj",
          "syrq",
          "qyjzr",
          "wtr",
          "wtfzr",
          "zzyb",
          "lldh",
          "syr",
          "yjbgrq",
          "sfsyj",
          "mstatus_dictText",
          "departid",
          "orgcode_dictText",
          "qrcode",
          "samplejidu",
          "sampleweidu",
          "ts",
          "chicun",
          "starttime",
          "endtime"
        )
      );
    },
  },
};
</script>
