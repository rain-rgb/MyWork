<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form :form="form" slot="detail">
<!--        <a-row>-->
<!--          <a-col :span="12">-->
<!--            <a-form-item label="设备编码" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['equipmentcode_dictText']" placeholder="请输入设备编码"  ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="12">-->
<!--            <a-form-item label="桩号" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['constructionstakenum']" placeholder="请输入桩号"  ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          </a-row>-->
<!--            <a-row>-->
<!--          <a-col :span="12">-->
<!--            <a-form-item label="开始时间" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['starttime']" rows="4" placeholder="请输入开始时间" />-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="12">-->
<!--            <a-form-item label="记录状态 " :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input-number v-decorator="['recordstatus_dictText']" placeholder="请输入记录状态 " style="width: 100%" />-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--            </a-row>-->
<!--        <a-row>-->
<!--          <a-col :span="12">-->
<!--            <a-form-item label="钻杆状态" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input-number v-decorator="['pipestatus_dictText']" placeholder="请输入钻杆状态" style="width: 100%" />-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="12">-->
<!--            <a-form-item label="喷浆状态" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input-number v-decorator="['nozzlestatus_dictText']" placeholder="请输入喷浆状态" style="width: 100%" />-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--        </a-row>-->
<!--        <a-row>-->
<!--          <a-col :span="12">-->
<!--            <a-form-item label="水灰比" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input-number v-decorator="['waterashratio']" placeholder="请输入水灰比" style="width: 100%" />-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="12">-->
<!--            <a-form-item label="段浆量(L)" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input-number v-decorator="['segmentpulp']" placeholder="请输入段浆量(L)" style="width: 100%" />-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--        </a-row>-->
<!--        <a-row>-->
<!--          <a-col :span="12">-->
<!--            <a-form-item label="段灰量(kg)" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input-number v-decorator="['segmentash']" placeholder="请输入段灰量(kg)" style="width: 100%" />-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="12">-->
<!--            <a-form-item label="累计浆量(L)" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input-number v-decorator="['totalpulp']" placeholder="请输入累计浆量(L)" style="width: 100%" />-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--        </a-row>-->
<!--        <a-row>-->
<!--          <a-col :span="12">-->
<!--            <a-form-item label="累计灰量(kg)" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input-number v-decorator="['totalash']" placeholder="请输入累计灰量(kg)" style="width: 100%" />-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="12">-->
<!--            <a-form-item label="电流(A)" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input-number v-decorator="['electricity']" placeholder="请输入电流(A)" style="width: 100%" />-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--        </a-row>-->
<!--        <a-row>-->
<!--          <a-col :span="12">-->
<!--            <a-form-item label="密度(g/cm3)" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input-number v-decorator="['density']" placeholder="请输入密度(g/cm3)" style="width: 100%" />-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="12">-->
<!--            <a-form-item label="喷压(MPa)" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input-number v-decorator="['pressure']" placeholder="请输入喷压(MPa)" style="width: 100%" />-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--        </a-row>-->
<!--        <a-row>-->
<!--          <a-col :span="12">-->
<!--            <a-form-item label="速度(cm/min)" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input-number v-decorator="['speed']" placeholder="请输入速度(cm/min)" style="width: 100%" />-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="12">-->
<!--            <a-form-item label="深度(m)" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input-number v-decorator="['currentdepth']" placeholder="请输入深度(m)" style="width: 100%" />-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--        </a-row>-->
<!--        <a-row>-->
<!--          <a-col :span="12">-->
<!--            <a-form-item label="最大深度(m)" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input-number v-decorator="['maxdepth']" placeholder="请输入最大深度(m)" style="width: 100%" />-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="12">-->
<!--            <a-form-item label="流量" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input-number v-decorator="['flow']" placeholder="请输入流量" style="width: 100%" />-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--        </a-row>-->
<!--        <a-row>-->
<!--          <a-col :span="12">-->
<!--            <a-form-item label="前后倾角" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input-number v-decorator="['xangle']" placeholder="请输入前后倾角" style="width: 100%" />-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="12">-->
<!--            <a-form-item label="左右倾角" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input-number v-decorator="['yangle']" placeholder="请输入左右倾角" style="width: 100%" />-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--        </a-row>-->
<!--        <a-row>-->
<!--          <a-col :span="12">-->
<!--            <a-form-item label="经度" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input-number v-decorator="['longitudu']" placeholder="请输入经度" style="width: 100%" />-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="12">-->
<!--            <a-form-item label="纬度" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input-number v-decorator="['latitude']" placeholder="请输入纬度" style="width: 100%" />-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--        </a-row>-->
<!--        <a-row>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="与子表关联" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['guid']" placeholder="请输入与子表关联"  ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="状态在线不在线" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input-number v-decorator="['status']" placeholder="请输入状态在线不在线" style="width: 100%" />-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col v-if="showFlowSubmitButton" :span="24" style="text-align: center">-->
<!--            <a-button @click="submitForm">提 交</a-button>-->
<!--          </a-col>-->
<!--        </a-row>-->
      </a-form>
    </j-form-container>
    <j-editable-table
      :ref="refKeys[0]"
      :loading="SmwsegmentataTable.loading"
      :columns="SmwsegmentataTable.columns"
      :dataSource="SmwsegmentataTable.dataSource"
      :maxHeight="300"
      :disabled="formDisabled"
      :rowNumber="true"
      :rowSelection="true"
      :actionButton="false">
      <template v-slot:action="props">
        <a-tag color="green" v-if="props.value == '0'">合格</a-tag>
        <a-tag color="orange" v-if="props.value == '1'">初级超标</a-tag>
        <a-tag color="yellow" v-if="props.value == '2'">中级超标</a-tag>
        <a-tag color="red" v-if="props.value == '3'">高级超标</a-tag>
      </template>
    </j-editable-table>
  </a-spin>
</template>

<script>

  import { httpAction, getAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import JFormContainer from '@/components/jeecg/JFormContainer'
  import { FormTypes } from '@/utils/JEditableTableUtil'

  export default {
    name: 'SmwsegmentataBaseForm',
    components: {
      JFormContainer,
    },
    props: {
      //流程表单data
      formData: {
        type: Object,
        default: ()=>{},
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
    data () {
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
        },
        refKeys: ['Smwsegmentata', ],
        tableKeys:['Smwsegmentata', ],
        activeKey: 'bhzCementDetail',
        // 水泥搅拌站信息
        SmwsegmentataTable: {
          loading: false,
          dataSource: [],
          columns: [

            {
              title: '段深度(m)',
              key: 'segmentdepth',
              type: FormTypes.normal,
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '段浆量(L)',
              key: 'segmentpulp',
              type: FormTypes.normal,
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '段灰量(kg)',
              key: 'segmentash',
              type: FormTypes.normal,
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '段电流(A)',
              key: 'segmentelectricity',
              type: FormTypes.normal,
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '段密度(g/cm3)',
              key: 'segmentdensity',
              type: FormTypes.normal,
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '段喷压(MPa)',
              type:FormTypes.normal,
              key: 'segmentpressure',
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '速度(cm/min)',
              type:FormTypes.normal,
              key: 'segmentspeed',
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '段时长(s)',
              type:FormTypes.normal,
              key: 'segmentage',
              placeholder: '请输入${title}',
              defaultValue:'',
            }


          ]
        },
        url: {
          add: "/Snjbz/smwsegmentataBase/add",
          edit: "/Snjbz/smwsegmentataBase/edit",
          queryById: "/Snjbz/smwsegmentataBase/queryById",
          queryByIds: "/Snjbz/smwsegmentataBase/queryByIds"
        }
      }
    },
    computed: {
      formDisabled(){
        if(this.formBpm===true){
          if(this.formData.disabled===false){
            return false
          }
          return true
        }
        return this.disabled
      },
      showFlowSubmitButton(){
        if(this.formBpm===true){
          if(this.formData.disabled===false){
            return true
          }
        }
        return false
      }
    },
    created () {
      //如果是流程中表单，则需要加载流程表单data
      this.showFlowData();
    },
    methods: {
      add () {
        this.edit({});
      },
      edit (record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model))//,'equipmentcode_dictText','constructionstakenum','starttime','recordstatus_dictText','pipestatus_dictText','nozzlestatus_dictText','waterashratio','segmentpulp','segmentash','totalpulp','totalash','electricity','density','pressure','speed','currentdepth','maxdepth','flow','xangle','yangle','longitudu','latitude'
        })
        // 加载子表数据
        if (this.model.guid) {
          let params = { guid: this.model.guid }
          getAction(this.url.queryByIds,params).then((res)=>{
            if(res.success){
              this.SmwsegmentataTable.dataSource=res.result;
            }
          })
        }
      },
      //渲染流程表单数据
      showFlowData(){
        if(this.formBpm === true){
          let params = {id:this.formData.dataId};
          getAction(this.url.queryById,params).then((res)=>{
            if(res.success){
              this.edit (res.result);
            }
          });
        }
      },
      submitForm () {
        const that = this;
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true;
            let httpurl = '';
            let method = '';
            if(!this.model.id){
              httpurl+=this.url.add;
              method = 'post';
            }else{
              httpurl+=this.url.edit;
               method = 'put';
            }
            let formData = Object.assign(this.model, values);
            console.log("表单提交数据",formData)
            httpAction(httpurl,formData,method).then((res)=>{
              if(res.success){
                that.$message.success(res.message);
                that.$emit('ok');
              }else{
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
            })
          }

        })
      },
      popupCallback(row){
        this.form.setFieldsValue(pick(row))//,'equipmentcode_dictText','constructionstakenum','starttime','recordstatus_dictText','pipestatus_dictText','nozzlestatus_dictText','waterashratio','segmentpulp','segmentash','totalpulp','totalash','electricity','density','pressure','speed','currentdepth','maxdepth','flow','xangle','yangle','longitudu','latitude'
      },
    }
  }
</script>