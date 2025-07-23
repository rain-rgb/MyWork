<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="设备编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sbjno">-->
<!--              <a-input v-model="model.sbjno" placeholder="请输入设备编号"  ></a-input>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
          <a-row>
            <a-col :span="24">
              <a-form-model-item label="级配标准" :labelCol="labelCol1" :wrapperCol="wrapperCol1" prop="poureLocation">
                <a-input v-model="model.poureLocation" placeholder="请输入级配标准"  ></a-input>
              </a-form-model-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="8">
              <a-form-model-item label="0.075筛孔下限" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sk0075xiaxian">
                <a-input v-model="model.sk0075xiaxian" placeholder="请输入0.075筛孔下限"  ></a-input>
              </a-form-model-item>
            </a-col>
            <a-col :span="8">
              <a-form-model-item label="0.075筛孔中限" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sk0075zhongxian">
                <a-input v-model="model.sk0075zhongxian" placeholder="请输入0.075筛孔中限"  ></a-input>
              </a-form-model-item>
            </a-col>
            <a-col :span="8">
              <a-form-model-item label="0.075筛孔上限" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sk0075shangxian">
                <a-input v-model="model.sk0075shangxian" placeholder="请输入0.075筛孔上限"  ></a-input>
              </a-form-model-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="8">
              <a-form-model-item label="0.15筛孔下限" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sk015xiaxian">
                <a-input v-model="model.sk015xiaxian" placeholder="请输入0.15筛孔下限"  ></a-input>
              </a-form-model-item>
            </a-col>
            <a-col :span="8">
              <a-form-model-item label="0.15筛孔中限" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sk015zhongxian">
                <a-input v-model="model.sk015zhongxian" placeholder="请输入0.15筛孔中限"  ></a-input>
              </a-form-model-item>
            </a-col>
            <a-col :span="8">
              <a-form-model-item label="0.15筛孔上限" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sk015shangxian">
                <a-input v-model="model.sk015shangxian" placeholder="请输入0.15筛孔上限"  ></a-input>
              </a-form-model-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="8">
              <a-form-model-item label="0.3筛孔下限" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sk03xiaxian">
                <a-input v-model="model.sk03xiaxian" placeholder="请输入0.3筛孔下限"  ></a-input>
              </a-form-model-item>
            </a-col>
            <a-col :span="8">
              <a-form-model-item label="0.3筛孔中限" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sk03zhongxian">
                <a-input v-model="model.sk03zhongxian" placeholder="请输入0.3筛孔中限"  ></a-input>
              </a-form-model-item>
            </a-col>
            <a-col :span="8">
              <a-form-model-item label="0.3筛孔上限" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sk03shangxian">
                <a-input v-model="model.sk03shangxian" placeholder="请输入0.3筛孔上限"  ></a-input>
              </a-form-model-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="8">
              <a-form-model-item label="0.6筛孔下限" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sk06xiaxian">
                <a-input v-model="model.sk06xiaxian" placeholder="请输入0.6筛孔下限"  ></a-input>
              </a-form-model-item>
            </a-col>
            <a-col :span="8">
              <a-form-model-item label="0.6筛孔中限" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sk06zhongxian">
                <a-input v-model="model.sk06zhongxian" placeholder="请输入0.6筛孔中限"  ></a-input>
              </a-form-model-item>
            </a-col>
            <a-col :span="8">
              <a-form-model-item label="0.6筛孔上限" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sk06shangxian">
                <a-input v-model="model.sk06shangxian" placeholder="请输入0.6筛孔上限"  ></a-input>
              </a-form-model-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="8">
              <a-form-model-item label="1.18筛孔下限" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sk118xiaxian">
                <a-input v-model="model.sk118xiaxian" placeholder="请输入1.18筛孔下限"  ></a-input>
              </a-form-model-item>
            </a-col>
            <a-col :span="8">
              <a-form-model-item label="1.18筛孔中限" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sk118zhongxian">
                <a-input v-model="model.sk118zhongxian" placeholder="请输入1.18筛孔中限"  ></a-input>
              </a-form-model-item>
            </a-col>
            <a-col :span="8">
              <a-form-model-item label="1.18筛孔上限" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sk118shangxian">
                <a-input v-model="model.sk118shangxian" placeholder="请输入1.18筛孔上限"  ></a-input>
              </a-form-model-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="8">
              <a-form-model-item label="2.36筛孔下限" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sk236xiaxian">
                <a-input v-model="model.sk236xiaxian" placeholder="请输入2.36筛孔下限"  ></a-input>
              </a-form-model-item>
            </a-col>
            <a-col :span="8">
              <a-form-model-item label="2.36筛孔中限" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sk236zhongxian">
                <a-input v-model="model.sk236zhongxian" placeholder="请输入2.36筛孔中限"  ></a-input>
              </a-form-model-item>
            </a-col>
            <a-col :span="8">
              <a-form-model-item label="2.36筛孔上限" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sk236shangxian">
                <a-input v-model="model.sk236shangxian" placeholder="请输入2.36筛孔上限"  ></a-input>
              </a-form-model-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="8">
              <a-form-model-item label="4.75筛孔下限" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sk475xiaxian">
                <a-input v-model="model.sk475xiaxian" placeholder="请输入4.75筛孔下限"  ></a-input>
              </a-form-model-item>
            </a-col>
            <a-col :span="8">
              <a-form-model-item label="4.75筛孔中限" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sk475zhongxian">
                <a-input v-model="model.sk475zhongxian" placeholder="请输入4.75筛孔中限"  ></a-input>
              </a-form-model-item>
            </a-col>
            <a-col :span="8">
              <a-form-model-item label="4.75筛孔上限" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sk475shangxian">
                <a-input v-model="model.sk475shangxian" placeholder="请输入4.75筛孔上限"  ></a-input>
              </a-form-model-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="8">
              <a-form-model-item label="9.5筛孔下限" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sk95xiaxian">
                <a-input v-model="model.sk95xiaxian" placeholder="请输入9.5筛孔下限"  ></a-input>
              </a-form-model-item>
            </a-col>
            <a-col :span="8">
              <a-form-model-item label="9.5筛孔中限" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sk95zhongxian">
                <a-input v-model="model.sk95zhongxian" placeholder="请输入9.5筛孔中限"  ></a-input>
              </a-form-model-item>
            </a-col>
            <a-col :span="8">
              <a-form-model-item label="9.5筛孔上限" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sk95shangxian">
                <a-input v-model="model.sk95shangxian" placeholder="请输入9.5筛孔上限"  ></a-input>
              </a-form-model-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="8">
              <a-form-model-item label="13.2筛孔下限" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sk132xiaxian">
                <a-input v-model="model.sk132xiaxian" placeholder="请输入13.2筛孔下限"  ></a-input>
              </a-form-model-item>
            </a-col>
            <a-col :span="8">
              <a-form-model-item label="13.2筛孔中限" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sk132zhongxian">
                <a-input v-model="model.sk132zhongxian" placeholder="请输入13.2筛孔中限"  ></a-input>
              </a-form-model-item>
            </a-col>
            <a-col :span="8">
              <a-form-model-item label="13.2筛孔上限" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sk132shangxian">
                <a-input v-model="model.sk132shangxian" placeholder="请输入13.2筛孔上限"  ></a-input>
              </a-form-model-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="8">
              <a-form-model-item label="16筛孔下限" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sk16xiaxian">
                <a-input v-model="model.sk16xiaxian=100" placeholder="请输入16筛孔下限"  ></a-input>
              </a-form-model-item>
            </a-col>
            <a-col :span="8">
              <a-form-model-item label="16筛孔中限" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sk16zhongxian">
                <a-input v-model="model.sk16zhongxian=100" placeholder="请输入16筛孔中限"  ></a-input>
              </a-form-model-item>
            </a-col>
            <a-col :span="8">
              <a-form-model-item label="16筛孔上限" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sk16shangxian">
                <a-input v-model="model.sk16shangxian=100" placeholder="请输入16筛孔上限"  ></a-input>
              </a-form-model-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="8">
              <a-form-model-item label="19筛孔下限" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sk19xiaxian">
                <a-input v-model="model.sk19xiaxian=100" placeholder="请输入19筛孔下限"  ></a-input>
              </a-form-model-item>
            </a-col>
            <a-col :span="8">
              <a-form-model-item label="19筛孔中限" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sk19zhongxian">
                <a-input v-model="model.sk19zhongxian=100" placeholder="请输入19筛孔中限"  ></a-input>
              </a-form-model-item>
            </a-col>
            <a-col :span="8">
              <a-form-model-item label="19筛孔上限" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sk19shangxian">
                <a-input v-model="model.sk19shangxian=100" placeholder="请输入19筛孔上限"  ></a-input>
              </a-form-model-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="8">
              <a-form-model-item label="26.5筛孔下限" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sk265xiaxian">
                <a-input v-model="model.sk265xiaxian=100" placeholder="请输入26.5筛孔下限"  ></a-input>
              </a-form-model-item>
            </a-col>
            <a-col :span="8">
              <a-form-model-item label="26.5筛孔中限" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sk265zhongxian">
                <a-input v-model="model.sk265zhongxian=100" placeholder="请输入26.5筛孔中限"  ></a-input>
              </a-form-model-item>
            </a-col>
            <a-col :span="8">
              <a-form-model-item label="26.5筛孔上限" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sk265shangxian">
                <a-input v-model="model.sk265shangxian=100" placeholder="请输入26.5筛孔上限"  ></a-input>
              </a-form-model-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="8">
              <a-form-model-item label="31.5筛孔下限" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sk315xiaxian">
                <a-input v-model="model.sk315xiaxian=100" placeholder="请输入31.5筛孔下限"  ></a-input>
              </a-form-model-item>
            </a-col>
            <a-col :span="8">
              <a-form-model-item label="31.5筛孔中限" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sk315zhongxian">
                <a-input v-model="model.sk315zhongxian=100" placeholder="请输入31.5筛孔中限"  ></a-input>
              </a-form-model-item>
            </a-col>
            <a-col :span="8">
              <a-form-model-item label="31.5筛孔上限" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sk315shangxian">
                <a-input v-model="model.sk315shangxian=100" placeholder="请输入31.5筛孔上限"  ></a-input>
              </a-form-model-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="8">
              <a-form-model-item label="37.5筛孔下限" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sk375xiaxian">
                <a-input v-model="model.sk375xiaxian=100" placeholder="请输入37.5筛孔下限"  ></a-input>
              </a-form-model-item>
            </a-col>
            <a-col :span="8">
              <a-form-model-item label="37.5筛孔中限" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sk375zhongxian">
                <a-input v-model="model.sk375zhongxian=100" placeholder="请输入37.5筛孔中限"  ></a-input>
              </a-form-model-item>
            </a-col>
            <a-col :span="8">
              <a-form-model-item label="37.5筛孔上限" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sk375shangxian">
                <a-input v-model="model.sk375shangxian=100" placeholder="请输入37.5筛孔上限"  ></a-input>
              </a-form-model-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="8">
              <a-form-model-item label="53筛孔下限" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sk53xiaxian">
                <a-input v-model="model.sk53xiaxian=100" placeholder="请输入53筛孔下限"  ></a-input>
              </a-form-model-item>
            </a-col>
            <a-col :span="8">
              <a-form-model-item label="53筛孔中限" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sk53zhongxian">
                <a-input v-model="model.sk53zhongxian=100" placeholder="请输入53筛孔中限"  ></a-input>
              </a-form-model-item>
            </a-col>
            <a-col :span="8">
              <a-form-model-item label="53筛孔上限" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sk53shangxian">
                <a-input v-model="model.sk53shangxian=100" placeholder="请输入53筛孔上限"  ></a-input>
              </a-form-model-item>
            </a-col>
          </a-row>

        </a-row>
      </a-form-model>
    </j-form-container>
  </a-spin>
</template>

<script>

  import { httpAction, getAction } from '@/api/manage'
  import { validateDuplicateValue } from '@/utils/util'

  export default {
    name: 'BhzLqJipeiFanweiForm',
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
          sm: { span: 9 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 12 },
        },
        labelCol1: {
          xs: { span: 24 },
          sm: { span: 3 },
        },
        wrapperCol1: {
          xs: { span: 24 },
          sm: { span: 20 },
        },
        confirmLoading: false,
        validatorRules: {
        },
        url: {
          add: "/bhzlqjipeifanwei/bhzLqJipeiFanwei/add",
          edit: "/bhzlqjipeifanwei/bhzLqJipeiFanwei/edit",
          queryById: "/bhzlqjipeifanwei/bhzLqJipeiFanwei/queryById"
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