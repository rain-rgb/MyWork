<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" slot="detail">
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="设备名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-model="model.shebeino_dictText" placeholder="请输入设备名称"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="桩号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-model="model.pileNo" placeholder="请输入桩号"></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="里程" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-model="model.pileMileage" placeholder="请输入里程"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="判定结果" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input :placeholder="model.chaobiaodengji == '0' ? '合格' : '不合格'"></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="预警原因" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-model="model.ycyy" placeholder="无"></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </j-form-container>
    <!-- 主表单区域 -->
    <a-form>
      <a-card
        title="施工方处置信息"
        :bordered="false"
        :headStyle="{ color: '#0785fd' }"
        :bodyStyle="{ padding: '10' }"
        style="margin-top: 10px"
      >
        <j-form-container :disabled="formDisabled">
          <a-row>
            <a-col :span="12">
              <a-form-item label="问题原因" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-textarea
                  placeholder="无"
                  v-model="bhzCementOverHandler.problemReasons"
                  :auto-size="{ minRows: 5, maxRows: 20 }"
                ></a-textarea>
                <!-- <a-input v-model="bhzCementOverHandler.problemReasons" placeholder="无"></a-input> -->
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="处理方式" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-model="bhzCementOverHandler.handleWay" placeholder="无"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item label="处理结果" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-model="bhzCementOverHandler.handleResult" placeholder="无"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="处理时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-model="bhzCementOverHandler.handleTime" placeholder="无"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item label="处置人" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-model="bhzCementOverHandler.handlePerson" placeholder="无"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
        </j-form-container>
        <a-row>
          <a-col :span="24">
            <a-form-item label="处置附件" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
              <viewer :images="detailList">
                <img
                  v-for="(item, index) in detailList"
                  :key="index"
                  style="height: 100px; width: 100px; margin: 5px 10px 5px 10px; float: left"
                  :src="[item.icon]"
                  alt=""
                />
              </viewer>
            </a-form-item>
          </a-col>
        </a-row>
      </a-card>
      <a-card
        title="监理单位审核"
        :bordered="false"
        :headStyle="{ color: '#0785fd' }"
        :bodyStyle="{ padding: '10' }"
        style="margin-top: 10px"
      >
        <j-form-container :disabled="formDisabled">
          <a-row>
            <a-col :span="12">
              <a-form-item label="监理审核" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input
                  v-if="bhzCementOverHandler.overproofStatus === 30"
                  v-model="bhzCementOverHandler.remark"
                  placeholder="无"
                ></a-input>
                <a-input v-else v-model="bhzCementOverHandler.supervisorApproval" placeholder="无"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="审核结果" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-if="bhzCementOverHandler.overproofStatus === 30" placeholder="驳回"></a-input>
                <a-input v-else v-model="bhzCementOverHandler.supervisorResult" placeholder="无"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item label="审核人" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-model="bhzCementOverHandler.approvalPerson" placeholder="无"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="监理处理时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-model="bhzCementOverHandler.supervisorHandleTime" placeholder="无"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
        </j-form-container>
        <a-row>
          <a-col :span="24">
            <a-form-item label="审核附件" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
              <viewer :images="detailList1">
                <img
                  v-for="(item, index) in detailList1"
                  :key="index"
                  style="height: 100px; width: 100px; margin: 5px 10px 5px 10px; float: left"
                  :src="[item.icon]"
                  alt=""
                />
              </viewer>
            </a-form-item>
          </a-col>
        </a-row>
      </a-card>
    </a-form>
  </a-spin>
</template>

<script>
import '@/assets/less/TableExpand.less'
import { httpAction, getAction } from '@/api/manage'
import pick from 'lodash.pick'
import JFormContainer from '@/components/jeecg/JFormContainer'
import JDate from '@/components/jeecg/JDate'
// import { JeecgListMixin } from '@/mixins/JeecgListMixin'
// import { mixinDevice } from '@/utils/mixin'

export default {
  name: 'DevicePipepileHistorydataOneForm',
  // mixins: [JeecgListMixin, mixinDevice],
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
      labelCol1: {
        xs: { span: 24 },
        sm: { span: 3 },
      },
      wrapperCol1: {
        xs: { span: 24 },
        sm: { span: 20 },
      },
      confirmLoading: false,
      detailList: [],
      detailList1: [],
      bhzCementOverHandler: {},
      url: {
        add: '/devicepipepilehistorydataone/devicePipepileHistorydataOne/add',
        edit: '/devicepipepilehistorydataone/devicePipepileHistorydataOne/edit',
        queryById: '/devicepipepilehistorydataone/devicePipepileHistorydataOne/queryById',
        exportXlsUrl: '/devicepipepilereport/devicePipepileReport/exportXls',
        importExcelUrl: '/devicepipepilereport/devicePipepileReport/importExcel',
        list: '/pippileOneOverHandler/pippileOneOverHandler/list',
      },
      dictOptions: {},
    }
  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`
    },
    formDisabled() {
      if (this.formBpm === true) {
        if (this.formData.disabled === false) {
          return false
        }
        return true
      }
      return this.disabled
    },
    showFlowSubmitButton() {
      if (this.formBpm === true) {
        if (this.formData.disabled === false) {
          return true
        }
      }
      return false
    },
  },
  created() {
    //如果是流程中表单，则需要加载流程表单data
    this.showFlowData()
  },
  methods: {
    add() {
      this.edit({})
    },
    edit(record) {
      // this.form.resetFields()
      this.model = Object.assign({}, record)
      this.visible = true
      this.$nextTick(() => {
        // this.form.setFieldsValue(
        //   pick(
        //     this.model,
        //     'shebeino_dictText',
        //     'pileNo',
        //     'pileRealdep',
        //     'pileWorktime',
        //     'pileY',
        //     'pileTime',
        //     'pileStarttime',
        //     'pileUpress',
        //     'pileDpress',
        //     'pileSpeed',
        //     'times',
        //     'pileDesigndep',
        //     'datatime',
        //     'address'
        //   )
        // )
        this.handledata()
      })
    },
    handledata() {
      this.detailList = []
      this.detailList1 = []
      this.bhzCementOverHandler = {}
      let params = { baseid: this.model.id }
      getAction(this.url.list, params).then((res) => {
        if (res.success) {
          if (res.result.records.length > 0) {
            this.bhzCementOverHandler = res.result.records[0]
            if (this.bhzCementOverHandler.filePath2 !== null) {
              let filePath1 = this.bhzCementOverHandler.filePath2.split(',')
              filePath1.forEach((re) => {
                this.detailList.push({ icon: re })
              })
              //console.log('filePath1', filePath1)
            }
            if (this.bhzCementOverHandler.filePath !== null) {
              let filePath3 = this.bhzCementOverHandler.filePath.split(',')
              filePath3.forEach((re) => {
                this.detailList1.push({ icon: re })
              })
            }
            //console.log("this.bhzCementOverHandler", res.result)
          } else {
            this.$message.warn('暂无超标处理信息！')
          }
        }
      })
    },
    //渲染流程表单数据
    showFlowData() {
      if (this.formBpm === true) {
        let params = { id: this.formData.dataId }
        getAction(this.url.queryById, params).then((res) => {
          if (res.success) {
            this.edit(res.result)
          }
        })
      }
    },
    submitForm() {
      const that = this
      // 触发表单验证
      this.form.validateFields((err, values) => {
        if (!err) {
          that.confirmLoading = true
          let httpurl = ''
          let method = ''
          if (!this.model.id) {
            httpurl += this.url.add
            method = 'post'
          } else {
            httpurl += this.url.edit
            method = 'put'
          }
          let formData = Object.assign(this.model, values)
          console.log('表单提交数据', formData)
          httpAction(httpurl, formData, method)
            .then((res) => {
              if (res.success) {
                that.$message.success(res.message)
                that.$emit('ok')
              } else {
                that.$message.warning(res.message)
              }
            })
            .finally(() => {
              that.confirmLoading = false
            })
        }
      })
    },
    // popupCallback(row) {
    //   this.form.setFieldsValue(
    //     pick(
    //       row,
    //       'shebeino_dictText',
    //       'pileNo',
    //       'pileRealdep',
    //       'pileWorktime',
    //       'pileY',
    //       'pileTime',
    //       'pileStarttime',
    //       'pileUpress',
    //       'pileDpress',
    //       'pileSpeed',
    //       'times',
    //       'pileDesigndep',
    //       'datatime',
    //       'address'
    //     )
    //   )
    // },
  },
}
</script>