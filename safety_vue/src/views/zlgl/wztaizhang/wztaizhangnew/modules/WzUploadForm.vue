<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form :form="form" slot="detail">
        <a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item label="试验人员" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['tester', validatorRules.tester]" placeholder="请输入试验人员"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="试验名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['testName', validatorRules.testName]" placeholder="请输入试验名称"></a-input>
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item label="自检状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-dict-select-tag type="list" v-decorator="['zijianStatus',validatorRules.zijianStatus]"
                                   :trigger-change="true" dictCode="syzhuangtai" placeholder="请选择自检状态"/>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="试验日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-date placeholder="请选择试验日期" :showTime="showTime" v-decorator="['testTime',validatorRules.testTime]" :trigger-change="true"
                        style="width: 100%" :format="format"/>
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item label="抽检状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-dict-select-tag type="list" v-decorator="['choujianStatus',validatorRules.choujianStatus]"
                                   :trigger-change="true" dictCode="syzhuangtai" placeholder="请选择抽检状态"/>
              </a-form-item>
            </a-col>
            <!-- <a-col :span="12">
              <a-form-item label="文件上传" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-upload :fileType="fileType" v-decorator="['report', validatorRules.realname]"></j-upload>
              </a-form-item>
            </a-col> -->
            <a-col :span="12">
              <a-form-item label="检验批次号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['pici',validatorRules.choujianStatus]" placeholder="请输入检验批次号" v-model="model.pici"></a-input>
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item label="检验状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-dict-select-tag type="list" v-decorator="['testStatus',validatorRules.testStatus]"
                                   :trigger-change="true" dictCode="syzhuangtai" placeholder="请选择检验状态"/>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="样品编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['sampleNumber',validatorRules.sampleNumber]" placeholder="请输入样品编号"></a-input>
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item label="检验类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-dict-select-tag type="list" v-decorator="['jianyanType',validatorRules.jianyanType]"
                                   :trigger-change="true" dictCode="jytype" placeholder="请选择检验类型"/>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="报告地址" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['report',validatorRules.report]" placeholder="请输入报告地址"></a-input>
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item label="取样时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-date placeholder="请选择取样时间" :showTime="showTime" v-decorator="['samplingTime',validatorRules.samplingTime]" :trigger-change="true"
                        style="width: 100%" :format="format"/>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="结论" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['conclusion',validatorRules.conclusion]" placeholder="请输入结论"></a-input>
              </a-form-item>
            </a-col>
          </a-row>

          <!--          <a-row>-->
          <!--            -->
          <!--          </a-row>-->

          <a-col v-if="showFlowSubmitButton" :span="24" style="text-align: center">
            <a-button @click="submitForm">提 交</a-button>
          </a-col>
        </a-row>
      </a-form>
    </j-form-container>

    <wzycljinchanggb-modal ref="modalForm" @ok="modalFormOk"></wzycljinchanggb-modal>
  </a-spin>
</template>

<script>

import { httpAction, getAction } from '@/api/manage'
import pick from 'lodash.pick'
import JFormContainer from '@/components/jeecg/JFormContainer'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import { mixinDevice } from '@/utils/mixin'
import WzycljinchanggbModal from '../../../yclsl/modules/WzycljinchanggbModal'

export default {
  name: 'WztaizhangForm',
  mixins: [JeecgListMixin, mixinDevice],
  components: {
    WzycljinchanggbModal,
    JFormContainer,
  },
  props: {
    //流程表单data
    formData: {
      type: Object,
      default: () => {
      },
      required: false
    },
    //表单模式：true流程表单 false普通表单
    formBpm: {
      type: Boolean,
      default: false,
      required: false
    },
    //表单禁用
    disabled: {
      type: Boolean,
      default: false,
      required: false
    }
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
      validatorRules: {
        tester: {
          rules: [{
            required: true, message: '请输入试验人员!'
          }]
        },
        testName: {
          rules: [{
            required: true, message: '请输入试验名称!'
          }]
        },
        zijianStatus: {
          rules: [{
            required: true, message: '请选择自检状态!'
          }]
        },
        testTime: {
          rules: [{
            required: true, message: '请选择试验日期!'
          }]
        },
        choujianStatus: {
          rules: [{
            required: true, message: '请选择抽检状态!'
          }]
        },
        pici: {
          rules: [{
            required: true, message: '请输入检验批次号!'
          }]
        },
        testStatus: {
          rules: [{
            required: true, message: '请选择检验状态!'
          }]
        },
        sampleNumber: {
          rules: [{
            required: true, message: '请输入样品编号!'
          }]
        },
        jianyanType: {
          rules: [{
            required: true, message: '请输入检验类型!'
          }]
        },
        report: {
          rules: [{
            required: true, message: '请输入报告地址!'
          }]
        },
        samplingTime: {
          rules: [{
            required: true, message: '请选择取样时间!'
          }]
        },
        conclusion: {
          rules: [{
            required: true, message: '请输入结论!'
          }]
        },
      },
      refKeys: ['mingxi',],
      tableKeys: ['mingxi',],
      activeKey: 'mingxi',
      loading: false,
      dataSource: [],
      columns: [
        //供应商名称	进场时间	出场时间	磅单号	车牌号	司磅员	毛重（吨）	皮重（吨）	净重（吨）
        {
          title: '地磅',
          align: 'center',
          dataIndex: 'shebeibianhao_dictText',
        },
        {
          title: '料仓',
          align: 'center',
          dataIndex: 'lcbianhao_dictText',
        },
        {
          title: '供应商单位',
          align: 'center',
          dataIndex: 'gongyingshangdanweibianma_dictText',
        },
        {
          title: '进场时间',
          align: 'center',
          dataIndex: 'jinchangshijian',
        },
        {
          title: '出场时间',
          align: 'center',
          dataIndex: 'chuchangshijian',
        },
        {
          title: '进出材料单',
          align: 'center',
          dataIndex: 'jinchuliaodanno',
        },
        {
          title: '车牌号',
          align: 'center',
          dataIndex: 'qianchepai',
        },
        {
          title: '司磅员',
          align: 'center',
          dataIndex: 'sibangyuan',
        },
        {
          title: '数量',
          align: 'center',
          dataIndex: 'jingzhongt',
        },
        {
          title: '单位',
          align: 'center',
          dataIndex: 'guobangleibie',
          customRender:function (t,r,index) {
            return t || '吨'
          }
        },
      ],
      ipagination1: {
        current: 1,
        pageSize: 5,
        pageSizeOptions: ['5', '10', '15'],
        showTotal: (total, range) => {
          return range[0] + '-' + range[1] + ' 共' + total + '条'
        },
        showQuickJumper: true,
        showSizeChanger: true,
        total: 0
      },
      ipagination3: {
        current: 1,
        pageSize: 5,
        pageSizeOptions: ['5', '10', '15'],
        showTotal: (total, range) => {
          return range[0] + '-' + range[1] + ' 共' + total + '条'
        },
        showQuickJumper: true,
        showSizeChanger: true,
        total: 0
      },
      dataSource2: [],
      columns2: [
        //样品编号  试验名称  取样时间  试验时间  试验结论  试验员  报告详情
        {
          title: '样品编号',
          align: 'center',
          dataIndex: 'sampleNumber',
        }, {
          title: '试验名称',
          align: 'center',
          dataIndex: 'testName',
        },
        {
          title: '取样时间',
          align: 'center',
          dataIndex: 'samplingTime',
        },
        {
          title: '试验时间',
          align: 'center',
          dataIndex: 'testTime',
        },
        {
          title: '状态',
          align: 'center',
          dataIndex: 'testStatus',
          scopedSlots: { customRender: 'tags2' }
        },
        {
          title: '试验员',
          align: 'center',
          dataIndex: 'tester',
        },
        {
          title: '报告详情',
          align: 'center',
          dataIndex: 'report',
          scopedSlots: { customRender: 'report' },
        },
      ],
      dataSource3: [],
      columns3: [
        //配料单号  配料时间  工程部位  使用量  料仓  检验批编号  创建时间
        {
          title: '配料单号',
          align: 'center',
          dataIndex: 'dosingOrderNumber',
        }, {
          title: '配料时间',
          align: 'center',
          dataIndex: 'dosingTime',
        },
        {
          title: '工程部位',
          align: 'center',
          dataIndex: 'projectPart',
        },
        {
          title: '使用量',
          align: 'center',
          dataIndex: 'uses',
        },
        {
          title: '料仓',
          align: 'center',
          dataIndex: 'storageId_dictText',
        },
        {
          title: '检验批编号',
          align: 'center',
          dataIndex: 'inspectionLotNumber',
        },
        {
          title: '报告详情',
          align: 'center',
          dataIndex: 'report',
          scopedSlots: { customRender: 'report' },
        },
      ],
      dataSource4: [],
      columns4: [
        //问题原因  处理方式  处理结果  处理时间  处理人  审批人  驳回原因  审核附件
        {
          title: '问题原因',
          align: 'center',
          dataIndex: 'problemReasons',
        },
        {
          title: '处理方式',
          align: 'center',
          dataIndex: 'handleWay',
        },
        {
          title: '处理结果',
          align: 'center',
          dataIndex: 'handleResult',
        },
        {
          title: '处理时间',
          align: 'center',
          dataIndex: 'handleTime',
        },
        {
          title: '处理人',
          align: 'center',
          dataIndex: 'handlePerson',
        },
        {
          title: '驳回原因',
          align: 'center',
          dataIndex: 'remark',
        },
        {
          title: '审核附件',
          align: 'center',
          dataIndex: 'filePath',
        },
        {
          title: '超标状态',
          align: 'center',
          dataIndex: 'overproofStatus',
          scopedSlots: { customRender: 'tags' },
        },
      ],
      url: {
        mingxi: '/yclsl/wzycljinchanggb/list',
        jiance: '/ycltd/yclTestDetail/list',
        shiyong: '/yclud/yclUsageDetail/list',
        chuzhi: '/wzyclHandler/wzyclHandler/list',
        add: '/wztaizhang/wztaizhang/add',
        edit: '/wztaizhang/wztaizhang/edit',
        queryById: '/wztaizhang/wztaizhang/queryById',

        upload: '/ycltd/yclTestDetail/list1',
      },
      fileType: ['pdf'],
    }
  },
  computed: {
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
    }
  },
  created() {
    //如果是流程中表单，则需要加载流程表单data
    this.showFlowData()
  },
  methods: {
    beforeUpload: function (file) {
      var fileType = file.type
      if (fileType.indexOf('pdf') < 0) {
        this.$message.warning('请上传pdf')
        return false
      }
      //TODO 验证文件大小
    },
    handleClickRow(record, index) {
      return {
        on: {
          click: () => {
            // handleDetail(record);
            console.log(record)
            this.$refs.modalForm.edit(record)
            this.$refs.modalForm.disableSubmit = true
            this.$refs.modalForm.title = '上传'
          }
        }
      }

    },
    handleTableChange1(pagination) {
      this.ipagination1 = pagination
      this.mingxis()
    },
    handleTableChange3(pagination) {
      this.ipagination3.current = pagination.current;
      this.ipagination3.pageSize = pagination.pageSize;
    },
    add() {
      this.edit({})
    },
    edit(record) {
      this.form.resetFields()
      this.model = Object.assign({}, record)
      this.visible = true
      this.$nextTick(() => {
        // this.form.setFieldsValue(pick(this.model, 'jinchangshijian', 'shebeibianhao_dictText', 'lcbianhao_dictText', 'cailiaono_dictText', 'maozhongt', 'pizhongt', 'jingzhongt', 'pici', 'guigexinghao'))
        this.form.setFieldsValue(pick(this.model, 'tester', 'testName', 'zijianStatus','testTime','choujianStatus','pici','testStatus','sampleNumber','jianyanType','report','samplingTime','conclusion'))
        this.mingxis()
        this.jiance()
        this.chuzhi()
        this.shiyong()
        // console.log(this.mingxi.dataSource, '调用')
        console.log(this.form, '调用 form')
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
          let httpurl = this.url.upload;
          let method = 'post'
          // if (!this.model.id) {
          //   httpurl += this.url.add
          //   method = 'post'
          // } else {
          //   httpurl += this.url.edit
          //   method = 'put'
          // }
          // let formData = Object.assign(this.model, values)
          let formData = Object.assign(values)
          formData.inspectionLotNumber = this.model.pici;
          console.log('表单提交数据', formData)
          httpAction(httpurl, formData, method).then((res) => {
            if (res.success) {
              that.$message.success(res.message)
              that.$emit('ok')
            } else {
              that.$message.warning(res.message)
            }
          }).finally(() => {
            that.confirmLoading = false
          })
        }

      })
    },
    mingxis() {//明细
      let params = {
        taizhangid: this.model.id,
        pageNo: this.ipagination1.current,
        pageSize: this.ipagination1.pageSize
      }
      getAction(this.url.mingxi, params).then((res) => {
          this.dataSource = []
          if (res.success) {
            this.dataSource = res.result.records
            if (res.result.total) {
              this.ipagination1.total = res.result.total
            } else {
              this.ipagination1.total = 0
            }
            // console.log(this.dataSource, '明细')
          } else {
            this.$message.error('暂无明细！')
          }
        }
      )
    },
    jiance() {//检测
      let params = { inspectionLotNumber: this.model.pici }
      getAction(this.url.jiance, params).then((res) => {
          this.dataSource2 = []
          if (res.success) {
            this.dataSource2 = res.result.records
            console.log(this.dataSource2, '检测')
          } else {
            this.$message.error('暂无明细！')
          }
        }
      )
    },
    shiyong() {//使用
      let params = { inspectionLotNumber: this.model.pici }
      getAction(this.url.shiyong, params).then((res) => {
          this.dataSource3 = []
          if (res.success) {
            this.dataSource3 = res.result.records
            console.log(this.dataSource3, '使用')
          } else {
            this.$message.error('暂无明细！')
          }
        }
      )
    },
    chuzhi() {//处置
      let params = { baseid: this.model.id }
      getAction(this.url.chuzhi, params).then((res) => {
          this.dataSource4 = []
          if (res.success) {
            this.dataSource4 = res.result.records
            console.log(this.dataSource4, '处置')
          } else {
            this.$message.error('暂无明细！')
          }
        }
      )
    },
    popupCallback(row) {
      this.form.setFieldsValue(pick(row, 'jinchangshijian', 'shebeibianhao_dictText', 'lcbianhao_dictText', 'cailiaono_dictText', 'maozhongt', 'pizhongt', 'jingzhongt', 'pici', 'guigexinghao'))
    },
    checkFile(record) {
      window.open(record.report, '_blank')
    }
  }
}
</script>