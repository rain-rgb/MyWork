<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form :form="form" slot="detail">
        <a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item label="进场时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['jinchangshijian']" placeholder="请输入进场时间"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="材料名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['cailiaono_dictText']" placeholder="请输入材料编号"></a-input>
              </a-form-item>
            </a-col>
            <!--            <a-col :span="12">-->
            <!--              <a-form-item label="设备名称" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
            <!--                <a-input v-decorator="['shebeibianhao_dictText']" placeholder="请输入设备编号"  ></a-input>-->
            <!--              </a-form-item>-->
            <!--            </a-col>-->
          </a-row>

          <a-row>
            <!--            <a-col :span="12">-->
            <!--              <a-form-item label="料仓名称" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
            <!--                <a-input v-decorator="['lcbianhao_dictText']" placeholder="请输入料仓编号"  ></a-input>-->
            <!--              </a-form-item>-->
            <!--            </a-col>-->
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item label="规格类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['guigexinghao']" placeholder="请输入规格类型"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="净重(吨)" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['jingzhongt']" placeholder="请输入皮重(吨)"></a-input>
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <!--            <a-col :span="12">-->
            <!--              <a-form-item label="净重(吨)" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
            <!--                <a-input v-decorator="['jingzhongt']" placeholder="请输入净重(吨)"  ></a-input>-->
            <!--              </a-form-item>-->
            <!--            </a-col>-->
            <a-col :span="12">
              <a-form-item label="批次" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['pici']" placeholder="请输入批次"></a-input>
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
    <a-tabs v-model="activeKey">
      <a-tab-pane tab="明细" :key="refKeys[0]" :forceRender="true">
        <a-table
          rowKey="id"
          size="middle"
          :columns="columns"
          :dataSource="dataSource"
          :loading="loading"
          :pagination="ipagination1"
          class="j-table-force-nowrap"
          :customRow="handleClickRow"
          @change="handleTableChange1"
        >
        </a-table>
      </a-tab-pane>
      <a-tab-pane key="2" tab="检测详情">
        <a-table
          rowKey="id"
          :columns="columns2"
          :dataSource="dataSource2"
          :loading="loading"
          :pagination="false"
          class="j-table-force-nowrap">
           <span slot="tags2" slot-scope="tags2, record">
          <a-tag color="orange" v-if="record.testStatus == '0'">未检验</a-tag>
          <a-tag color="purple" v-if="record.testStatus == '3'">检验中</a-tag>
          <a-tag color="green" v-if="record.testStatus == '1'">合格</a-tag>
          <a-tag color="red" v-if="record.testStatus == '2'">不合格</a-tag>
        </span>
          <template slot="report" slot-scope="report, record">
            <a-button type="primary" @click="checkFile(record)">查看</a-button>
          </template>
        </a-table>
      </a-tab-pane>
      <a-tab-pane key="3" tab="使用详情">
        <a-table
          rowKey="id"
          :columns="columns3"
          :dataSource="dataSource3"
          :loading="loading"
          :pagination="ipagination3"
          @change="handleTableChange3"
          class="j-table-force-nowrap">
        </a-table>
      </a-tab-pane>
      <a-tab-pane key="4" tab="处置详情">
        <a-table
          rowKey="id"
          :columns="columns4"
          :dataSource="dataSource4"
          :loading="loading"
          :pagination="false"
          class="j-table-force-nowrap">
          <span slot="tags" slot-scope="tags, record">
          <a-tag color="orange" v-if="record.overproofStatus == '0'">未处理</a-tag>
          <a-tag color="green" v-if="record.overproofStatus == '10'">施工方已处理</a-tag>
          <a-tag color="red" v-if="record.overproofStatus == '20'">监理方已处理</a-tag>
            <a-tag color="blue" v-if="record.overproofStatus == '30'">监理驳回</a-tag>
        </span>
        </a-table>
      </a-tab-pane>

    </a-tabs>

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
      validatorRules: {},
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
        queryById: '/wztaizhang/wztaizhang/queryById'
      }
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
    handleClickRow(record, index) {
      return {
        on: {
          click: () => {
            // handleDetail(record);
            console.log(record)
            this.$refs.modalForm.edit(record)
            this.$refs.modalForm.disableSubmit = true
            this.$refs.modalForm.title = '详情'
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
        this.form.setFieldsValue(pick(this.model, 'jinchangshijian', 'shebeibianhao_dictText', 'lcbianhao_dictText', 'cailiaono_dictText', 'maozhongt', 'pizhongt', 'jingzhongt', 'pici', 'guigexinghao'))
        this.mingxis()
        this.jiance()
        this.chuzhi()
        this.shiyong()
        // console.log(this.mingxi.dataSource, '调用')
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
      if(record.testStatus == '3' || record.testStatus == '0'){
        this.$message.error("暂未生成报告")
      }else{
        window.open(record.report, '_blank')
      }

    }
  }
}
</script>