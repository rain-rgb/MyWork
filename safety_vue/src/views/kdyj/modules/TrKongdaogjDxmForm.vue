<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <!-- 主表单区域 -->
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
<!--          <a-col :span="24" >-->
<!--            <a-form-model-item label="设备商标识" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="vendorid">-->
<!--              <a-input v-model="model.vendorid" placeholder="请输入设备商标识" ></a-input>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
          <a-col :span="24" >
            <a-form-model-item label="流水号 " :labelCol="labelCol" :wrapperCol="wrapperCol" prop="serialno">
              <a-input v-model="model.serialno" placeholder="请输入流水号 " ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="测试仪编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="machineid">
              <a-input v-model="model.machineid" placeholder="请输入测试仪编号" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="孔道编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="holeno">
              <a-input v-model="model.holeno" placeholder="请输入孔道编号" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="梁板名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="beamname">
              <a-input v-model="model.beamname" placeholder="请输入梁板名称" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="检测日期" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="testdate">
              <j-date placeholder="请选择检测日期" v-model="model.testdate" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="检测时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="testtime">
              <j-date placeholder="请选择检测时间" v-model="model.testtime" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="检测人员" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="worker">
              <a-input v-model="model.worker" placeholder="请输入检测人员" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="检测单位" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="testunit">
              <a-input v-model="model.testunit" placeholder="请输入检测单位" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="施工单位" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="constructionunit">
              <a-input v-model="model.constructionunit" placeholder="请输入施工单位" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="工程名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="projectname">
              <a-input v-model="model.projectname" placeholder="请输入工程名称" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="梁板长度 " :labelCol="labelCol" :wrapperCol="wrapperCol" prop="beamlength">
              <a-input-number v-model="model.beamlength" placeholder="请输入梁板长度 " style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="混凝土波速" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="concretev">
              <a-input-number v-model="model.concretev" placeholder="请输入混凝土波速" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="采样间隔 （μs）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sampleinterval">
              <a-input-number v-model="model.sampleinterval" placeholder="请输入采样间隔 （μs）" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="低通（kHz）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="lowfilter">
              <a-input-number v-model="model.lowfilter" placeholder="请输入低通（kHz）" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="高通 （Hz）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="highfilter">
              <a-input-number v-model="model.highfilter" placeholder="请输入高通 （Hz）" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="采样长度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="samplelength">
              <a-input-number v-model="model.samplelength" placeholder="请输入采样长度" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="延迟点数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="delaydot">
              <a-input-number v-model="model.delaydot" placeholder="请输入延迟点数" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="触发通道（0:任意 1:CH1 2:CH2）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="triggerchannel">
              <a-input-number v-model="model.triggerchannel" placeholder="请输入触发通道（0:任意 1:CH1 2:CH2）" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="触发电平" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="triggerlevel">
              <a-input-number v-model="model.triggerlevel" placeholder="请输入触发电平" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="测试模式（0：无线 1：有线）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="testmode">
              <a-input-number v-model="model.testmode" placeholder="请输入测试模式（0：无线 1：有线）" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="GPS 状态（0：无效 1：有效）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="gpsvalid">
              <a-input-number v-model="model.gpsvalid" placeholder="请输入GPS 状态（0：无效 1：有效）" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="经度 " :labelCol="labelCol" :wrapperCol="wrapperCol" prop="gpslongitude">
              <a-input-number v-model="model.gpslongitude" placeholder="请输入经度 " style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="纬度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="gpslatitude">
              <a-input-number v-model="model.gpslatitude" placeholder="请输入纬度" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="remarks">
              <a-input v-model="model.remarks" placeholder="请输入备注" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="孔道高差(m)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="holede">
              <a-input-number v-model="model.holede" placeholder="请输入孔道高差(m)" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="锚索波速(m/s)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="anchorropev">
              <a-input-number v-model="model.anchorropev" placeholder="请输入锚索波速(m/s)" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="传感器 1 到梁板长度(m)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="ch1length">
              <a-input-number v-model="model.ch1length" placeholder="请输入传感器 1 到梁板长度(m)" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="传感器 2 到梁板长度(m)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="ch2length">
              <a-input-number v-model="model.ch2length" placeholder="请输入传感器 2 到梁板长度(m)" style="width: 100%" />
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </j-form-container>
      <!-- 子表单区域 -->
    <a-tabs v-model="activeKey" @change="handleChangeTabs">
      <a-tab-pane tab="孔道灌浆（定性检测）孔道信息表" :key="refKeys[0]" :forceRender="true">
        <j-editable-table
          :ref="refKeys[0]"
          :loading="trKongdaogjDxkdxxTable.loading"
          :columns="trKongdaogjDxkdxxTable.columns"
          :dataSource="trKongdaogjDxkdxxTable.dataSource"
          :maxHeight="300"
          :disabled="formDisabled"
          :rowNumber="true"
          :rowSelection="true"
          :actionButton="true"/>
      </a-tab-pane>
      <a-tab-pane tab="孔道灌浆（定性检测）子表" :key="refKeys[1]" :forceRender="true">
        <j-editable-table
          :ref="refKeys[1]"
          :loading="trKongdaogjDxsTable.loading"
          :columns="trKongdaogjDxsTable.columns"
          :dataSource="trKongdaogjDxsTable.dataSource"
          :maxHeight="300"
          :disabled="formDisabled"
          :rowNumber="true"
          :rowSelection="true"
          :actionButton="true"/>
      </a-tab-pane>
    </a-tabs>
  </a-spin>
</template>

<script>

  import { getAction } from '@/api/manage'
  import { FormTypes,getRefPromise,VALIDATE_NO_PASSED } from '@/utils/JEditableTableUtil'
  import { JEditableTableModelMixin } from '@/mixins/JEditableTableModelMixin'
  import { validateDuplicateValue } from '@/utils/util'

  export default {
    name: 'TrKongdaogjDxmForm',
    mixins: [JEditableTableModelMixin],
    components: {
    },
    data() {
      return {
        labelCol: {
          xs: { span: 24 },
          sm: { span: 6 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        labelCol2: {
          xs: { span: 24 },
          sm: { span: 3 },
        },
        wrapperCol2: {
          xs: { span: 24 },
          sm: { span: 20 },
        },
        model:{
        },
        // 新增时子表默认添加几行空数据
        addDefaultRowNum: 1,
        validatorRules: {
        },
        refKeys: ['trKongdaogjDxkdxx', 'trKongdaogjDxs', ],
        tableKeys:['trKongdaogjDxkdxx', 'trKongdaogjDxs', ],
        activeKey: 'trKongdaogjDxkdxx',
        // 孔道灌浆（定性检测）孔道信息表
        trKongdaogjDxkdxxTable: {
          loading: false,
          dataSource: [],
          columns: [
            {
              title: '通道号 ',
              key: 'channelnum',
              type: FormTypes.inputNumber,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '传感器类型 (0:加速度计1:速度计)',
              key: 'sensortype',
              type: FormTypes.inputNumber,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '传感器灵敏度系数',
              key: 'sensorsensitive',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '积分状态(是否积分, 1:积分 0：不积分)',
              key: 'integralflag',
              type: FormTypes.inputNumber,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            // {
            //   title: '基本信息id',
            //   key: 'zbid',
            //   type: FormTypes.inputNumber,
            //   width:"200px",
            //   placeholder: '请输入${title}',
            //   defaultValue:'',
            //   validateRules: [{ required: true, message: '${title}不能为空' }],
            // },
          ]
        },
        // 孔道灌浆（定性检测）子表
        trKongdaogjDxsTable: {
          loading: false,
          dataSource: [],
          columns: [
            {
              title: '当前锤号',
              key: 'curnumber',
              type: FormTypes.inputNumber,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '采样时间',
              key: 'sampletime',
              type: FormTypes.date,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: 'CH1数据波形数值',
              key: 'wavedata1',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: 'CH2数据波形数值',
              key: 'wavedata2',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '总锤数',
              key: 'totalcount',
              type: FormTypes.inputNumber,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
          ]
        },
        url: {
          add: "/trkongdaogjdxm/trKongdaogjDxm/add",
          edit: "/trkongdaogjdxm/trKongdaogjDxm/edit",
          queryById: "/trkongdaogjdxm/trKongdaogjDxm/queryById",
          trKongdaogjDxkdxx: {
            list: '/trkongdaogjdxm/trKongdaogjDxm/queryTrKongdaogjDxkdxxByMainId'
          },
          trKongdaogjDxs: {
            list: '/trkongdaogjdxm/trKongdaogjDxm/queryTrKongdaogjDxsByMainId'
          },
        }
      }
    },
    props: {
      //表单禁用
      disabled: {
        type: Boolean,
        default: false,
        required: false
      }
    },
    computed: {
      formDisabled(){
        return this.disabled
      },
    },
    created () {
    },
    methods: {
      addBefore(){
        this.trKongdaogjDxkdxxTable.dataSource=[]
        this.trKongdaogjDxsTable.dataSource=[]
      },
      getAllTable() {
        let values = this.tableKeys.map(key => getRefPromise(this, key))
        return Promise.all(values)
      },
      /** 调用完edit()方法之后会自动调用此方法 */
      editAfter() {
        this.$nextTick(() => {
        })
        // 加载子表数据
        if (this.model.id) {
          let params = { id: this.model.id }
          this.requestSubTableData(this.url.trKongdaogjDxkdxx.list, params, this.trKongdaogjDxkdxxTable)
          this.requestSubTableData(this.url.trKongdaogjDxs.list, params, this.trKongdaogjDxsTable)
        }
      },
      //校验所有一对一子表表单
      validateSubForm(allValues){
          return new Promise((resolve,reject)=>{
            Promise.all([
            ]).then(() => {
              resolve(allValues)
            }).catch(e => {
              if (e.error === VALIDATE_NO_PASSED) {
                // 如果有未通过表单验证的子表，就自动跳转到它所在的tab
                this.activeKey = e.index == null ? this.activeKey : this.refKeys[e.index]
              } else {
                console.error(e)
              }
            })
          })
      },
      /** 整理成formData */
      classifyIntoFormData(allValues) {
        let main = Object.assign(this.model, allValues.formValue)
        return {
          ...main, // 展开
          trKongdaogjDxkdxxList: allValues.tablesValue[0].values,
          trKongdaogjDxsList: allValues.tablesValue[1].values,
        }
      },
      validateError(msg){
        this.$message.error(msg)
      },

    }
  }
</script>

<style scoped>
</style>