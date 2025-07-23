<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <!-- 主表单区域 -->
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24" >
            <a-form-model-item label="报检单ID" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="inspectionformid">
              <a-input-number v-model="model.inspectionformid" placeholder="请输入报检单ID" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="设备商标识" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="vendorid">
              <a-input v-model="model.vendorid" placeholder="请输入设备商标识" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="设备编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="machineno">
              <a-input v-model="model.machineno" placeholder="请输入设备编号" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="施工部位" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sgbw">
              <a-input v-model="model.sgbw" placeholder="请输入施工部位" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="流水号(任务单编号)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="serialno">
              <a-input v-model="model.serialno" placeholder="请输入流水号(任务单编号)" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="桩id" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pileid">
              <a-input-number v-model="model.pileid" placeholder="请输入桩id" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="桩编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pileno">
              <a-input v-model="model.pileno" placeholder="请输入桩编号" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="开始检测时间(yyyy-MM-dd HH:mm:ss)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="starttime">
              <a-input v-model="model.starttime" placeholder="请输入开始检测时间(yyyy-MM-dd HH:mm:ss)" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="实际桩径（mm）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pilediameter">
              <a-input-number v-model="model.pilediameter" placeholder="请输入实际桩径（mm）" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="实际桩长（m）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pilelength">
              <a-input-number v-model="model.pilelength" placeholder="请输入实际桩长（m）" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="预设波速(m/s)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="velocity">
              <a-input-number v-model="model.velocity" placeholder="请输入预设波速(m/s)" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="检测数量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="samplecount">
              <a-input-number v-model="model.samplecount" placeholder="请输入检测数量" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="桩完整性" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="intergrityevaluation">
              <a-input v-model="model.intergrityevaluation" placeholder="请输入桩完整性" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="桩等级" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="integrityclassification">
              <a-input-number v-model="model.integrityclassification" placeholder="请输入桩等级" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="有效数据总条数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="validdatacount">
              <a-input-number v-model="model.validdatacount" placeholder="请输入有效数据总条数" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="经度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="gpslongitude">
              <a-input v-model="model.gpslongitude" placeholder="请输入经度" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="纬度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="gpslatitude">
              <a-input v-model="model.gpslatitude" placeholder="请输入纬度" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="上传时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="uploaded">
              <a-input v-model="model.uploaded" placeholder="请输入上传时间" ></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </j-form-container>
      <!-- 子表单区域 -->
    <a-tabs v-model="activeKey" @change="handleChangeTabs">
      <a-tab-pane tab="低应变子表" :key="refKeys[0]" :forceRender="true">
        <j-editable-table
          :ref="refKeys[0]"
          :loading="lowstrainSTable.loading"
          :columns="lowstrainSTable.columns"
          :dataSource="lowstrainSTable.dataSource"
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
    name: 'LowstrainMForm',
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
        refKeys: ['lowstrainS', ],
        tableKeys:['lowstrainS', ],
        activeKey: 'lowstrainS',
        // 低应变子表
        lowstrainSTable: {
          loading: false,
          dataSource: [],
          columns: [
            {
              title: '序列号',
              key: 'seq',
              type: FormTypes.inputNumber,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '采样点号',
              key: 'pointno',
              type: FormTypes.inputNumber,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '当前锤次',
              key: 'curblowindex',
              type: FormTypes.inputNumber,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '采样时间',
              key: 'sampletime',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '信号增益值',
              key: 'nchngain',
              type: FormTypes.inputNumber,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '滤波方式（-1-无，0－低通，1－高通，2－带通）',
              key: 'fitertype',
              type: FormTypes.inputNumber,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '高通滤波（Hz）',
              key: 'highpass',
              type: FormTypes.inputNumber,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '低通滤波（Hz）',
              key: 'lowpass',
              type: FormTypes.inputNumber,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '是否积分（0：积分（加速度计）； 1：不积分（速度计））',
              key: 'integralflag',
              type: FormTypes.inputNumber,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '平滑点数',
              key: 'smooth',
              type: FormTypes.inputNumber,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '放大类型 （0-指数放大  1-线性放大）',
              key: 'magtype',
              type: FormTypes.inputNumber,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '放大系数',
              key: 'magfactor',
              type: FormTypes.inputNumber,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '放大起点（m）',
              key: 'magstart',
              type: FormTypes.inputNumber,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '旋转百分比',
              key: 'rotation',
              type: FormTypes.inputNumber,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '小波因子',
              key: 'waveletscale',
              type: FormTypes.inputNumber,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '探头类型（0：加速度计；  1：速度计）',
              key: 'sensortype',
              type: FormTypes.inputNumber,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '采样间隔（us）',
              key: 'samplinginterval',
              type: FormTypes.inputNumber,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '传感器系数 探头类型为加速度计 单位: mv/(m/s2)探头类型为速度计 单位: mv/(m/s)',
              key: 'sensitivity',
              type: FormTypes.inputNumber,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '桩顶位置',
              key: 'piletop',
              type: FormTypes.inputNumber,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '桩底位置',
              key: 'pilebot',
              type: FormTypes.inputNumber,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '延迟点数',
              key: 'delaypoints',
              type: FormTypes.inputNumber,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '采样点数',
              key: 'samplepoints',
              type: FormTypes.inputNumber,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '最大信号百分比',
              key: 'maxdatapercent',
              type: FormTypes.inputNumber,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '原始波形数值',
              key: 'orgwavedata',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '处理后波形数值',
              key: 'wavedata',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
          ]
        },
        url: {
          add: "/lowstrain_m/lowstrainM/add",
          edit: "/lowstrain_m/lowstrainM/edit",
          queryById: "/lowstrain_m/lowstrainM/queryById",
          lowstrainS: {
            list: '/lowstrain_m/lowstrainM/queryLowstrainSByMainId'
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
        this.lowstrainSTable.dataSource=[]
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
          this.requestSubTableData(this.url.lowstrainS.list, params, this.lowstrainSTable)
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
          lowstrainSList: allValues.tablesValue[0].values,
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