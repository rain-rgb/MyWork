<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="设备编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shebeino">
              <a-input v-model="model.shebeino" placeholder="请输入设备编号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="序号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pileNo">
              <a-input v-model="model.pileNo" placeholder="请输入序号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="施工深度(m)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pileRealdep">
              <a-input v-model="model.pileRealdep" placeholder="请输入施工深度(m)"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="工作时长(min)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pileWorktime">
              <a-input v-model="model.pileWorktime" placeholder="请输入工作时长(min)"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="结束时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pileEndtime">
              <a-input v-model="model.pileEndtime" placeholder="请输入结束时间"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="开始时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pileStarttime">
              <a-input v-model="model.pileStarttime" placeholder="请输入开始时间"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="倾斜度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pileY">
              <a-input v-model="model.pileY" placeholder="请输入倾斜度"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="下钻速度(cm/min)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="downSpeed">
              <a-input v-model="model.downSpeed" placeholder="请输入下钻速度(cm/min)"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="提钻速度(cm/min)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="upSpeed">
              <a-input-number v-model="model.upSpeed" placeholder="请输入提钻速度(cm/min)" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="设计桩长" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pileDesigndep">
              <a-input-number v-model="model.pileDesigndep" placeholder="请输入设计桩长" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="关联软基任务单 device_mixpile_rwd" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="rjrwd">
              <a-input v-model="model.rjrwd" placeholder="请输入关联软基任务单 device_mixpile_rwd"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="里程" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pileMileage">
              <a-input v-model="model.pileMileage" placeholder="请输入里程"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="桩经度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pileLgd">
              <a-input v-model="model.pileLgd" placeholder="请输入桩经度"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="桩纬度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pileLtd">
              <a-input v-model="model.pileLtd" placeholder="请输入桩纬度"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="超标等级0：合格；1不合格" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="chaobiaodengji">
              <a-input-number v-model="model.chaobiaodengji" placeholder="请输入超标等级0：合格；1不合格" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="0未处置；10：处置；20：闭合，30驳回" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="overproofStatus">
              <a-input-number v-model="model.overproofStatus" placeholder="请输入0未处置；10：处置；20：闭合，30驳回" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="异常原因" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="ycyy">
              <a-input v-model="model.ycyy" placeholder="请输入异常原因"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="接桩次数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jiegantimes">
              <a-input-number v-model="model.jiegantimes" placeholder="请输入接桩次数" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="接杆长度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jieganchangdu">
              <a-input v-model="model.jieganchangdu" placeholder="请输入接杆长度"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="长度(cm)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="changdu">
              <a-input v-model="model.changdu" placeholder="请输入长度(cm)"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="宽度(cm)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="kuandu">
              <a-input v-model="model.kuandu" placeholder="请输入宽度(cm)"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="总方量m³" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="zongm">
              <a-input v-model="model.zongm" placeholder="请输入总方量m³"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="电流" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="dianliu">
              <a-input v-model="model.dianliu" placeholder="请输入电流"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="泥浆密度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pileDensity">
              <a-input v-model="model.pileDensity" placeholder="请输入泥浆密度"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="note">
              <a-input v-model="model.note" placeholder="请输入备注"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="唯一id" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="uuid">
              <a-input v-model="model.uuid" placeholder="请输入唯一id"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="倾斜度x" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pileX">
              <a-input v-model="model.pileX" placeholder="请输入倾斜度x"  ></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </j-form-container>
  </a-spin>
</template>

<script>

  import { httpAction, getAction } from '@/api/manage'
  import { validateDuplicateValue } from '@/utils/util'

  export default {
    name: 'DevicePiplewallOneForm',
    components: {
    },
    props: {
      //表单禁用
      disabled: {
        type: Boolean,
        default: false,
        required: false
      }
    },
    data () {
      return {
        model:{
         },
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
        url: {
          add: "/devicepiplewall/devicePiplewallOne/add",
          edit: "/devicepiplewall/devicePiplewallOne/edit",
          queryById: "/devicepiplewall/devicePiplewallOne/queryById"
        }
      }
    },
    computed: {
      formDisabled(){
        return this.disabled
      },
    },
    created () {
       //备份model原始值
      this.modelDefault = JSON.parse(JSON.stringify(this.model));
    },
    methods: {
      add () {
        this.edit(this.modelDefault);
      },
      edit (record) {
        this.model = Object.assign({}, record);
        this.visible = true;
      },
      submitForm () {
        const that = this;
        // 触发表单验证
        this.$refs.form.validate(valid => {
          if (valid) {
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
            httpAction(httpurl,this.model,method).then((res)=>{
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
    }
  }
</script>