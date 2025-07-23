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
            <a-form-model-item label="剖面列表:varchar(40),剖面列表，以逗号分隔。 比如：1-2,1-3， 2-3" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="poumianlist">
              <a-input v-model="model.poumianlist" placeholder="请输入剖面列表:varchar(40),剖面列表，以逗号分隔。 比如：1-2,1-3， 2-3"  ></a-input>
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
            <a-form-model-item label="传输内容: 7：原始数据临界值包，17：分析数据临界值包" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="chuanshuleirong">
              <a-input-number v-model="model.chuanshuleirong" placeholder="请输入传输内容: 7：原始数据临界值包，17：分析数据临界值包" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="包序号：用于异步传输是统计回包是否全部传输完成" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="baoxuhao">
              <a-input-number v-model="model.baoxuhao" placeholder="请输入包序号：用于异步传输是统计回包是否全部传输完成" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="剖面数据1-5:以”,”分隔组成的数据,由平均声速，声速异常判定值（临界值），声速标准差，离散系数，均匀性等级，类别，波速最小值，平均波幅，波幅临界值，波幅最小值10个float类型数据组成" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="poumian1">
              <a-input v-model="model.poumian1" placeholder="请输入剖面数据1-5:以”,”分隔组成的数据,由平均声速，声速异常判定值（临界值），声速标准差，离散系数，均匀性等级，类别，波速最小值，平均波幅，波幅临界值，波幅最小值10个float类型数据组成"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="剖面数据1-5:以”,”分隔组成的数据,由平均声速，声速异常判定值（临界值），声速标准差，离散系数，均匀性等级，类别，波速最小值，平均波幅，波幅临界值，波幅最小值10个float类型数据组成" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="poumian2">
              <a-input v-model="model.poumian2" placeholder="请输入剖面数据1-5:以”,”分隔组成的数据,由平均声速，声速异常判定值（临界值），声速标准差，离散系数，均匀性等级，类别，波速最小值，平均波幅，波幅临界值，波幅最小值10个float类型数据组成"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="剖面数据1-5:以”,”分隔组成的数据,由平均声速，声速异常判定值（临界值），声速标准差，离散系数，均匀性等级，类别，波速最小值，平均波幅，波幅临界值，波幅最小值10个float类型数据组成" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="poumian3">
              <a-input v-model="model.poumian3" placeholder="请输入剖面数据1-5:以”,”分隔组成的数据,由平均声速，声速异常判定值（临界值），声速标准差，离散系数，均匀性等级，类别，波速最小值，平均波幅，波幅临界值，波幅最小值10个float类型数据组成"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="剖面数据1-5:以”,”分隔组成的数据,由平均声速，声速异常判定值（临界值），声速标准差，离散系数，均匀性等级，类别，波速最小值，平均波幅，波幅临界值，波幅最小值10个float类型数据组成" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="poumian4">
              <a-input v-model="model.poumian4" placeholder="请输入剖面数据1-5:以”,”分隔组成的数据,由平均声速，声速异常判定值（临界值），声速标准差，离散系数，均匀性等级，类别，波速最小值，平均波幅，波幅临界值，波幅最小值10个float类型数据组成"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="剖面数据1-5:以”,”分隔组成的数据,由平均声速，声速异常判定值（临界值），声速标准差，离散系数，均匀性等级，类别，波速最小值，平均波幅，波幅临界值，波幅最小值10个float类型数据组成" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="poumian5">
              <a-input v-model="model.poumian5" placeholder="请输入剖面数据1-5:以”,”分隔组成的数据,由平均声速，声速异常判定值（临界值），声速标准差，离散系数，均匀性等级，类别，波速最小值，平均波幅，波幅临界值，波幅最小值10个float类型数据组成"  ></a-input>
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
    name: 'ChaoshengboSyljzsForm',
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
          add: "/chaoshengbo/chaoshengboSyljzs/add",
          edit: "/chaoshengbo/chaoshengboSyljzs/edit",
          queryById: "/chaoshengbo/chaoshengboSyljzs/queryById"
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