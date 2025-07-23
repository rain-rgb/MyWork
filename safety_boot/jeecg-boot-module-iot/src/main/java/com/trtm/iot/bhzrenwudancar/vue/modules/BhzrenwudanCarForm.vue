<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="票据号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="recipe">
              <a-input v-model="model.recipe" placeholder="请输入票据号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="任务单编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="code">
              <a-input v-model="model.code" placeholder="请输入任务单编号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="生产线(1或2)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="station">
              <a-input v-model="model.station" placeholder="请输入生产线(1或2)"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="砂浆配合比编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="morrec">
              <a-input v-model="model.morrec" placeholder="请输入砂浆配合比编号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="单位工程" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="projname">
              <a-input v-model="model.projname" placeholder="请输入单位工程"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="分部工程" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="projtype">
              <a-input v-model="model.projtype" placeholder="请输入分部工程"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="分项工程" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="projgrade">
              <a-input v-model="model.projgrade" placeholder="请输入分项工程"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="里程" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pilemile">
              <a-input v-model="model.pilemile" placeholder="请输入里程"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="强度等级" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="betlev">
              <a-input v-model="model.betlev" placeholder="请输入强度等级"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="生产方量(含砂浆方量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="prodmete">
              <a-input-number v-model="model.prodmete" placeholder="请输入生产方量(含砂浆方量" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="砂浆方量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="mormete">
              <a-input-number v-model="model.mormete" placeholder="请输入砂浆方量" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="累计方量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="totmete">
              <a-input-number v-model="model.totmete" placeholder="请输入累计方量" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="运输方量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="transportvolume">
              <a-input-number v-model="model.transportvolume" placeholder="请输入运输方量" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="任务方量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="mete">
              <a-input-number v-model="model.mete" placeholder="请输入任务方量" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="节超方量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="dvalue">
              <a-input-number v-model="model.dvalue" placeholder="请输入节超方量" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="车辆编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="vehicle">
              <a-input v-model="model.vehicle" placeholder="请输入车辆编号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="驾驶员" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="driver">
              <a-input v-model="model.driver" placeholder="请输入驾驶员"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="发货日期" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="dattim">
              <a-input v-model="model.dattim" placeholder="请输入发货日期"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="发货时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="dattimShij">
              <a-input v-model="model.dattimShij" placeholder="请输入发货时间"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="浇注日期" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="begtim">
              <a-input v-model="model.begtim" placeholder="请输入浇注日期"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="截止日期" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="endtim">
              <a-input v-model="model.endtim" placeholder="请输入截止日期"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="调度人员" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="attamper">
              <a-input v-model="model.attamper" placeholder="请输入调度人员"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="到工作面时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="dgzmsj">
              <a-input v-model="model.dgzmsj" placeholder="请输入到工作面时间"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="卸料时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="xlsj">
              <a-input v-model="model.xlsj" placeholder="请输入卸料时间"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="卸完时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="xwsj">
              <a-input v-model="model.xwsj" placeholder="请输入卸完时间"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="账期" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="zq">
              <a-input v-model="model.zq" placeholder="请输入账期"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="使用单位" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sydw">
              <a-input v-model="model.sydw" placeholder="请输入使用单位"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="note">
              <a-input v-model="model.note" placeholder="请输入备注"  ></a-input>
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
    name: 'BhzrenwudanCarForm',
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
          add: "/bhzrenwudancar/bhzrenwudanCar/add",
          edit: "/bhzrenwudancar/bhzrenwudanCar/edit",
          queryById: "/bhzrenwudancar/bhzrenwudanCar/queryById"
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