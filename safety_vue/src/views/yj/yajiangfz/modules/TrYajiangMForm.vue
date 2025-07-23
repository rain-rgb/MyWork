<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form :form="form" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-item label="32位全球唯一码" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['syjid', validatorRules.syjid]" placeholder="请输入32位全球唯一码"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="施工单位" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['sgdw']" placeholder="请输入施工单位"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="监理单位" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['jldw']" placeholder="请输入监理单位"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="合同号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['htbh']" placeholder="请输入合同号"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="工程名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['gcmc']" placeholder="请输入工程名称"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="压浆时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['yjsj']" placeholder="请输入压浆时间"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="桩号及部位" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['zhbw']" placeholder="请输入桩号及部位"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="施工部位" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['sgbw']" placeholder="请输入施工部位"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="构件结构" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['gjjg']" placeholder="请输入构件结构"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="构件编号及长度" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['gjbh']" placeholder="请输入构件编号及长度"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="气温" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['qw']" placeholder="请输入气温"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="掺减水剂量" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['cjsjl']" placeholder="请输入掺减水剂量"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="掺膨胀剂量" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['cpzjl']" placeholder="请输入掺膨胀剂量"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="水温" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['sw']" placeholder="请输入水温"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="水胶比" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['shuijiaobi']" placeholder="请输入水胶比"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="构件压浆水泥用量" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['snyl']" placeholder="请输入构件压浆水泥用量"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="压浆温度" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['yjwd']" placeholder="请输入压浆温度"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="泌水率" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['msl']" placeholder="请输入泌水率"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="备用" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['beiyong']" placeholder="请输入备用"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="压浆设备编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['yjsbbh']" placeholder="请输入压浆设备编号"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="梁板类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['lblx']" placeholder="请输入梁板类型"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="梁号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['lianghao']" placeholder="请输入梁号"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="张拉时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['zlsj']" placeholder="请输入张拉时间"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="压浆剂" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['yajiangji']" placeholder="请输入压浆剂"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="水泥名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['snmc']" placeholder="请输入水泥名称"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="孔道数" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['kongdaoshu']" placeholder="请输入孔道数"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="压浆方向" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['yajiangfang']" placeholder="请输入压浆方向"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="压浆步骤" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['yajiangbuzh']" placeholder="请输入压浆步骤"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="压浆过程" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['yajiangguoc']" placeholder="请输入压浆过程"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="初始流动速度" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['chushisudu']" placeholder="请输入初始流动速度"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['memo']" placeholder="请输入备注"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="liudongdu" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['liudongdu']" placeholder="请输入liudongdu"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="完成状态(0为未完成,状态为1代表" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['status']" placeholder="请输入完成状态(0为未完成,状态为1代表"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="32位全球唯一码" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['guid']" placeholder="请输入32位全球唯一码"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="压浆任务单下发时任务单id（由设备返回）" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['uuid']" placeholder="请输入压浆任务单下发时任务单id（由设备返回）"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="issend" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['issend']" placeholder="请输入issend"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col v-if="showFlowSubmitButton" :span="24" style="text-align: center">
            <a-button @click="submitForm">提 交</a-button>
          </a-col>
        </a-row>
      </a-form>
    </j-form-container>
  </a-spin>
</template>

<script>

  import { httpAction, getAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import JFormContainer from '@/components/jeecg/JFormContainer'

  export default {
    name: 'TrYajiangMForm',
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
          syjid: {
            rules: [
              { required: true, message: '请输入32位全球唯一码!'},
            ]
          },
        },
        url: {
          add: "/yajiangm/trYajiangM/add",
          edit: "/yajiangm/trYajiangM/edit",
          queryById: "/yajiangm/trYajiangM/queryById"
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
          this.form.setFieldsValue(pick(this.model,'syjid','sgdw','jldw','htbh','gcmc','yjsj','zhbw','sgbw','gjjg','gjbh','qw','cjsjl','cpzjl','sw','shuijiaobi','snyl','yjwd','msl','beiyong','yjsbbh','lblx','lianghao','zlsj','yajiangji','snmc','kongdaoshu','yajiangfang','yajiangbuzh','yajiangguoc','chushisudu','memo','liudongdu','status','guid','uuid','issend'))
        })
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
        this.form.setFieldsValue(pick(row,'syjid','sgdw','jldw','htbh','gcmc','yjsj','zhbw','sgbw','gjjg','gjbh','qw','cjsjl','cpzjl','sw','shuijiaobi','snyl','yjwd','msl','beiyong','yjsbbh','lblx','lianghao','zlsj','yajiangji','snmc','kongdaoshu','yajiangfang','yajiangbuzh','yajiangguoc','chushisudu','memo','liudongdu','status','guid','uuid','issend'))
      },
    }
  }
</script>