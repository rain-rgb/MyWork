<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form :form="form" slot="detail">
        <a-card title="基础信息" :bordered="false" :headStyle="{color:'#0785fd'}" :bodyStyle="{padding:'10'}"
                style="margin-top:10px">
          <a-row>
            <a-row>
              <a-col :span="12">
                <a-form-item label="设备名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-decorator="['shebeino_dictText']" placeholder="请输入设备名称"></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item label="单桩编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-decorator="['pileNo']" placeholder="请输入桩号"></a-input>
                </a-form-item>
              </a-col>
            </a-row>
            <a-row>
              <a-col :span="12">
                <a-form-item label="里程桩号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-decorator="['pileMileage']" placeholder="请输入桩号"></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item label="水泥用量" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-decorator="['pileCement']" placeholder="请输入设备名称"></a-input>
                </a-form-item>
              </a-col>
            </a-row>

            <a-row>
              <a-col :span="12">
                <a-form-item label="设计桩长(m)" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-decorator="['pileDesigndep']" placeholder="请输入设计桩长"></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item label="实际桩长(m)" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-decorator="['pileRealdep']" placeholder="请输入实际桩长"></a-input>
                </a-form-item>
              </a-col>
            </a-row>

            <a-row>
              <a-col :span="12">
                <a-form-item label="下钻时长(S)" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-decorator="['pileDowntime']" placeholder="请输入下钻时长"></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item label="下钻浆量(L)" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-decorator="['pileDownbeton']" placeholder="请输入下钻浆量"></a-input>
                </a-form-item>
              </a-col>
            </a-row>

            <a-row>
              <a-col :span="12">
                <a-form-item label="下钻平均速度(m/min)" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-decorator="['pileDspeed']" placeholder="下钻平均速度"></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item label="下钻平均电流(A)" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-decorator="['pileDelectr']" placeholder="下钻平均电流"></a-input>
                </a-form-item>
              </a-col>
            </a-row>

            <a-row>
              <a-col :span="12">
                <a-form-item label="提钻时长(S)" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-decorator="['pileUptime']" placeholder="请输入抬钻时长"></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item label="提钻浆量(L)" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-decorator="['pileUobeton']" placeholder="请输入抬钻浆量"></a-input>
                </a-form-item>
              </a-col>
            </a-row>

            <a-row>
              <a-col :span="12">
                <a-form-item label="提钻平均速度(m/min)" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-decorator="['pileUspeed']" placeholder="请输入上钻平均速度"></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item label="提钻平均电流(A)" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-decorator="['pileUelectr']" placeholder="请输入平均电流"></a-input>
                </a-form-item>
              </a-col>
            </a-row>

            <a-row v-if="model.piletype == 1">
              <a-col :span="12">
                <a-form-item label="提钻压力(MPa)" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-decorator="['pileUpress']" placeholder="请输入提钻压力"></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item label="下钻压力(MPa)" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-decorator="['pileDpress']" placeholder="请输入下钻压力"></a-input>
                </a-form-item>
              </a-col>

            </a-row>

            <a-row>
              <a-col :span="12">
                <a-form-item label="水灰比" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-decorator="['pileRatio']" placeholder="请输入水灰比"></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item label="喷浆时长(S)" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input-number v-decorator="['pileWorktime']" placeholder="请输入喷浆时长" style="width: 100%"/>
                </a-form-item>
              </a-col>
            </a-row>

            <a-row>
              <a-col :span="12">
                <a-form-item label="x轴角度" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-decorator="['pileX']" placeholder="请输入x轴角度"></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item label="y轴角度" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-decorator="['pileY']" placeholder="请输入y轴角度"></a-input>
                </a-form-item>
              </a-col>

            </a-row>

            <a-row>
              <a-col :span="12">
                <a-form-item label="经度" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-decorator="['pileLgd']" placeholder="请输入经度"></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item label="纬度" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-decorator="['pileLtd']" placeholder="请输入纬度"></a-input>
                </a-form-item>
              </a-col>
            </a-row>


            <!--          <a-row>-->
            <!--            <a-col :span="12">-->
            <!--              <a-form-item label="设计水灰比" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
            <!--                <a-input v-decorator="['designratio']" placeholder="请输入设计水灰比"  ></a-input>-->
            <!--              </a-form-item>-->
            <!--            </a-col>-->
            <!--            <a-col :span="12">-->
            <!--              <a-form-item label="设计灌浆量" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
            <!--                <a-input v-decorator="['designgrout']" placeholder="请输入设计灌浆量"  ></a-input>-->
            <!--              </a-form-item>-->
            <!--            </a-col>-->
            <!--          </a-row>-->

            <!--          <a-row>-->
            <!--            <a-col :span="12">-->
            <!--              <a-form-item label="水泥量" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
            <!--                <a-input v-decorator="['cement']" placeholder="请输入水泥量"  ></a-input>-->
            <!--              </a-form-item>-->
            <!--            </a-col>-->
            <!--            <a-col :span="12">-->
            <!--              <a-form-item label="用水量" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
            <!--                <a-input v-decorator="['water']" placeholder="请输入用水量"  ></a-input>-->
            <!--              </a-form-item>-->
            <!--            </a-col>-->
            <!--          </a-row>-->

            <!--          <a-row>-->
            <!--            <a-col :span="12">-->
            <!--              <a-form-item label="盘次" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
            <!--                <a-input v-decorator="['serial']" placeholder="请输入盘次"  ></a-input>-->
            <!--              </a-form-item>-->
            <!--            </a-col>-->
            <!--          </a-row>-->

            <a-row>
              <a-col :span="12">
                <a-form-item label="水泥浆比重" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-decorator="['pileDensity']" placeholder="请输入累计浆量"></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item label="成桩时间(S)" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-decorator="['pileTime']" placeholder="请输入成桩时间(秒)"></a-input>
                </a-form-item>
              </a-col>

              <!--            <a-col :span="12">-->
              <!--              <a-form-item label="操作员" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
              <!--                <a-input v-decorator="['username']" placeholder="请输入操作员"  ></a-input>-->
              <!--              </a-form-item>-->
              <!--            </a-col>-->
            </a-row>

            <a-row>
              <a-col :span="12">

                <a-form-item label="喷浆深度" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-decorator="['pjdep']" placeholder=""></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="12" v-if="model.chaobiaodengji !== null && model.chaobiaodengji>0 ">
                <a-form-item label="预警原因" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-textarea v-decorator="['problem']" placeholder=""></a-textarea>
                </a-form-item>
              </a-col>
              <!--            <a-col :span="12">-->
              <!--              <a-form-item label="灌浆量" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
              <!--                <a-input v-decorator="['grouting']" placeholder="请输入灌浆量"  ></a-input>-->
              <!--              </a-form-item>-->
              <!--            </a-col>-->
              <!--            <a-col :span="12">-->

              <!--              <a-form-item label="数据时间" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
              <!--                <j-date placeholder="请选择数据时间" v-decorator="['datatime']" :trigger-change="true" style="width: 100%" date-format="YYYY-MM-DD HH:mm:ss" />-->
              <!--              </a-form-item>-->
              <!--            </a-col>-->
            </a-row>
            <!--          <a-row>-->
            <!--            <a-col :span="12">-->
            <!--              <a-form-item label="瞬时流量" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
            <!--                <a-input v-decorator="['flowlst']" placeholder="请输入瞬时流量"  ></a-input>-->
            <!--              </a-form-item>-->
            <!--            </a-col>-->

            <!--          </a-row>-->

            <!--          <a-row>-->
            <!--            <a-col :span="12">-->
            <!--              <a-form-item label="开始时间" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
            <!--                <j-date placeholder="请选择开始时间" v-decorator="['starttime']" :trigger-change="true" style="width: 100%" />-->
            <!--              </a-form-item>-->
            <!--            </a-col>-->
            <!--            <a-col :span="12">-->
            <!--              <a-form-item label="结束时间" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
            <!--                <j-date placeholder="请选择结束时间" v-decorator="['endtime']" :trigger-change="true" style="width: 100%" />-->
            <!--              </a-form-item>-->
            <!--            </a-col>-->
            <!--          </a-row>-->

            <!--          <a-row>-->

            <!--          </a-row>-->

            <!--          <a-row>-->


            <!--          </a-row>-->

            <a-col v-if="showFlowSubmitButton" :span="24" style="text-align: center">
              <a-button @click="submitForm">提 交</a-button>
            </a-col>
          </a-row>
        </a-card>
        <a-card title="处理信息" :bordered="false" :headStyle="{color:'#0785fd'}" :bodyStyle="{padding:'10'}" style="margin-top:10px">
          <a-row>
            <a-col :span="12">
              <a-form-item label="问题原因" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-model="handlemodel.problemReasons" placeholder="无"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="处理方式" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-model="handlemodel.handleWay" placeholder="无"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item label="处理结果" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-model="handlemodel.handleResult" placeholder="无"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="处理时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-model="handlemodel.handleTime" placeholder="无"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item label="处置人" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-model="handlemodel.handlePerson" placeholder="无"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item label="监理驳回原因" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-model="handlemodel.remark" placeholder="无"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item label="监理审批" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-model="handlemodel.supervisorApproval" placeholder="无"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="监理结果" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-model="handlemodel.supervisorResult" placeholder="无"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item label="监理处理时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-model="handlemodel.supervisorHandleTime" placeholder="无"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="审核人" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-model="handlemodel.approvalPerson" placeholder="无"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
        </a-card>
      </a-form>
    </j-form-container>
    <a-row>
      <a-col :span="24">
        <a-form-item label="处置附件" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
          <viewer :images="detailList">
            <img
              v-for="(item,index) in detailList"
              :key="index"
              style="height:100px;width: 100px;margin: 5px 10px 5px 10px;float: left"
              :src="[item.icon] "
              alt="">
          </viewer>
        </a-form-item>
      </a-col>
    </a-row>
    <a-row>
      <a-col :span="24">
        <a-form-item label="审核附件" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
          <viewer :images="detailList1">
            <img
              v-for="(item,index) in detailList1"
              :key="index"
              style="height:100px;width: 100px;margin: 5px 10px 5px 10px;float: left"
              :src="[item.icon] "
              alt="">
          </viewer>
        </a-form-item>
      </a-col>
    </a-row>
    <a-tabs v-model="activeKey">
      <a-tab-pane tab="分段数据" :key="refKeys[0]" :forceRender="true">
        <a-table
          rowKey="id"
          :columns="columns"
          :dataSource="dataSource"
          :loading="loading"
          :pagination="ipagination"
          class="j-table-force-nowrap"
          @change="tableChange"
        :customRow="handleClick"
        :rowClassName="setRowClassName"
      >
        </a-table>
      </a-tab-pane>
    </a-tabs>
  </a-spin>
</template>

<script>

import { httpAction, getAction } from '@/api/manage'
import pick from 'lodash.pick'
import { validateDuplicateValue } from '@/utils/util'
import JFormContainer from '@/components/jeecg/JFormContainer'
import JDate from '@/components/jeecg/JDate'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import { mixinDevice } from '@/utils/mixin'
import { FormTypes } from '@/utils/JEditableTableUtil'

export default {
  name: 'DeviceMixpileHistorydataOneCBForm',
  mixins: [JeecgListMixin, mixinDevice],
  components: {
    JFormContainer,
    JDate,
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
      detailList: [],
      detailList1:[],
      labelCol1: {
        xs: { span: 24 },
        sm: { span: 3 },
      },
      wrapperCol1: {
        xs: { span: 24 },
        sm: { span: 20 },
      },
      handlemodel:{},
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
      refKeys: ['partdata',],
      tableKeys: ['partdata',],
      activeKey: 'partdata',
      loading: false,
      dataSource: [],
      ipagination: {
        current: 1,
        pageSize: 10,
        pageSizeOptions: ['10', '20', '30'],
        showTotal: (total, range) => {
          return range[0] + '-' + range[1] + ' 共' + total + '条'
        },
        showQuickJumper: true,
        showSizeChanger: true,
        total: 0
      },
      columns: [
        //供应商名称	进场时间	出场时间	磅单号	车牌号	司磅员	毛重（吨）	皮重（吨）	净重（吨）
        {
          title: '段号',
          dataIndex: 'partPilec',
        }, {
          title: '段深度',
          dataIndex: 'partDep',
        },
        {
          title: '段浆量',
          dataIndex: 'partBeton',
          type: FormTypes.normal,
        },
        {
          title: '段速度',
          dataIndex: 'partSpeed',
          type: FormTypes.normal,
        },
        {
          title: '段时长',
          dataIndex: 'partTime',
          type: FormTypes.normal,
        },
        {
          title: '段x',
          dataIndex: 'partX',
          type: FormTypes.normal,
        },
        {
          title: '段y',
          dataIndex: 'partY',
          type: FormTypes.normal,
        },
        {
          title: '段状态',
          dataIndex: 'partState_dictText',
          type: FormTypes.normal,
        },
        {
          title: '段结束时间',
          dataIndex: 'partEndtime',
          type: FormTypes.normal,
        },
      ],

      url: {
        add: '/deviceMixpileHistorydataOne/deviceMixpileHistorydataOne/add',
        edit: '/deviceMixpileHistorydataOne/deviceMixpileHistorydataOne/edit',
        queryById: '/deviceMixpileHistorydataOne/deviceMixpileHistorydataOne/queryById',
        partdata: '/devicemixpilehistorydatapart/deviceMixpileHistorydataPart/list',
        list:'/mixpileoneoverhandler/mixpileOneOverHandler/list'
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
    tableChange(pagination) {
      this.ipagination.current = pagination.current
      this.ipagination.pageSize = pagination.pageSize
      this.partdatas()
    },
    add() {
      this.edit({})
    },
    edit(record) {
      this.form.resetFields()
      this.model = Object.assign({}, record)
      this.visible = true
      this.gethandledata()
      this.$nextTick(() => {
        this.form.setFieldsValue(pick(this.model, 'shebeino_dictText', 'pileDesigndep', 'pileRealdep', 'pileX', 'pileY', 'pileMaxelectr', 'grouting', 'pileNo', 'designratio', 'designgrout', 'cement', 'water', 'pileRatio', 'serial', 'username', 'datatime', 'pileLgd', 'pileLtd', 'flowlst', 'pileSpeed', 'starttime', 'endtime', 'pileFlowtotal', 'pileWorktime', 'pileDownbeton', 'pileUobeton', 'pileDowntime', 'pileUptime', 'pileTime', 'pileUspeed', 'pileUelectr', 'pileDspeed', 'pileDelectr', 'pileCement', 'pileDensity', 'pileMileage', 'pileUpress', 'pileDpress', 'piletype', 'problem', 'pjdep'))
        this.partdatas()
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
    partdatas: function () {//分段数据
      let params = {
        shebeino: this.model.shebeino,
        pileNo: this.model.pileNo,
        pageSize: this.ipagination.pageSize,
        pageNo: this.ipagination.current
      }
      getAction(this.url.partdata, params).then((res) => {
          if (res.success) {
            this.dataSource = res.result.records
            this.ipagination.total = res.result.total
            console.log(this.dataSource, '分段数据')
          } else {
            this.$message.error('暂无分段数据！')
          }
        }
      )
    },
    gethandledata:function (){//处置信息
      let params={
        baseid:this.model.id
      }
      getAction(this.url.list,params).then(res=>{
        if (res.success) {
          if (res.result.records.length > 0) {
            this.handlemodel = res.result.records[0]
            if (this.handlemodel.filePath2 !== null) {
              var filePath1 = this.handlemodel.filePath2.split(',')
              filePath1.forEach(re => {
                this.detailList.push({ icon: re })
              })
              //console.log('filePath1', filePath1)
            }
            if (this.handlemodel.filePath !== null) {
              var filePath3 = this.handlemodel.filePath.split(',')
              filePath3.forEach(re => {
                this.detailList1.push({ icon: re })
              })
            }
          }else {
            this.$message.warn('暂无超标处理信息！')
          }
        }
      })
    },
    popupCallback(row) {
      this.form.setFieldsValue(pick(row, 'shebeino_dictText', 'pileDesigndep', 'pileRealdep', 'pileX', 'pileY', 'pileMaxelectr', 'grouting', 'pileNo', 'designratio', 'designgrout', 'cement', 'water', 'pileRatio', 'serial', 'username', 'datatime', 'pileLgd', 'pileLtd', 'flowlst', 'pileSpeed', 'starttime', 'endtime', 'pileFlowtotal', 'pileWorktime', 'pileDownbeton', 'pileUobeton', 'pileDowntime', 'pileUptime', 'pileTime', 'pileUpress', 'pileDpress', 'piletype', 'problem', 'pjdep'))
    },
  }
}
</script>