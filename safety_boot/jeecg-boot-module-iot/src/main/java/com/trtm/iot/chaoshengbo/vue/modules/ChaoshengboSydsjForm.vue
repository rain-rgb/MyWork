<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="设备商标识" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shebeino">
              <a-input v-model="model.shebeino" placeholder="请输入设备商标识"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="测试仪编号：仪器商必须保证编号的唯一性" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="ceshiyino">
              <a-input v-model="model.ceshiyino" placeholder="请输入测试仪编号：仪器商必须保证编号的唯一性"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="剖面列表:剖面列表，以逗号分隔。 比如：1-2,1-3， 2-3" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="poumianlist">
              <a-input v-model="model.poumianlist" placeholder="请输入剖面列表:剖面列表，以逗号分隔。 比如：1-2,1-3， 2-3"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="包序号：1、2、...（包分片后，需要按照指定顺序拼接，比如一个包序号为1，第二个包序号为2）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="baoxuhao">
              <a-input-number v-model="model.baoxuhao" placeholder="请输入包序号：1、2、...（包分片后，需要按照指定顺序拼接，比如一个包序号为1，第二个包序号为2）" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="大序号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="daxuhao">
              <a-input-number v-model="model.daxuhao" placeholder="请输入大序号" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="深度：数据以”,”分隔的128个短整型的点数据组成" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shendu">
              <a-input-number v-model="model.shendu" placeholder="请输入深度：数据以”,”分隔的128个短整型的点数据组成" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="跨距" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="kuaju">
              <a-input-number v-model="model.kuaju" placeholder="请输入跨距" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="接收高度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jieshoulength">
              <a-input-number v-model="model.jieshoulength" placeholder="请输入接收高度" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="增益" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="zengyi">
              <a-input-number v-model="model.zengyi" placeholder="请输入增益" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="延时" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="yanshi">
              <a-input-number v-model="model.yanshi" placeholder="请输入延时" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="声时索引值" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shengshiindex">
              <a-input-number v-model="model.shengshiindex" placeholder="请输入声时索引值" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="首波峰索引值" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shoubofengindex">
              <a-input-number v-model="model.shoubofengindex" placeholder="请输入首波峰索引值" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="流水号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="liushuihao">
              <a-input v-model="model.liushuihao" placeholder="请输入流水号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="试桩编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shizhuangno">
              <a-input v-model="model.shizhuangno" placeholder="请输入试桩编号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="传输内容: 8：分析数据临界值缺陷包，18：分析数据临界值缺陷包" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="chaungshuleirong">
              <a-input-number v-model="model.chaungshuleirong" placeholder="请输入传输内容: 8：分析数据临界值缺陷包，18：分析数据临界值缺陷包" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="剖面号1-10数据:以”,”分隔组成的数据，由剖面号，跨距，声时，波幅，主频，PSD，I17个数据组成的字符串,I1为(广东规范中的I值)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="poumianhao">
              <a-input v-model="model.poumianhao" placeholder="请输入剖面号1-10数据:以”,”分隔组成的数据，由剖面号，跨距，声时，波幅，主频，PSD，I17个数据组成的字符串,I1为(广东规范中的I值)"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="数据" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shuju">
              <a-textarea v-model="model.shuju" rows="4" placeholder="请输入数据" />
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
    name: 'ChaoshengboSydsjForm',
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
          add: "/chaoshengbo/chaoshengboSydsj/add",
          edit: "/chaoshengbo/chaoshengboSydsj/edit",
          queryById: "/chaoshengbo/chaoshengboSydsj/queryById"
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