<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
<!--          <a-col :span="24">
            <a-form-model-item label="原材进场登记表ID" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="yuancaijinchangdengjiid">
              <a-input-number v-model="model.yuancaijinchangdengjiid" placeholder="" style="width: 100%" />
            </a-form-model-item>
          </a-col>-->
<!--          <a-col :span="24">
            <a-form-model-item label="取样状态" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="quyangzhuangtai">
              <a-input-number v-model="model.quyangzhuangtai" placeholder="" style="width: 100%" />
            </a-form-model-item>
          </a-col>-->
<!--          <a-col :span="24">
            <a-form-model-item label="取样人" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="quyangren">
              <a-input v-model="model.quyangren" placeholder=""  ></a-input>
            </a-form-model-item>
          </a-col>-->
          <a-col :span="24">
            <a-form-model-item label="施工单位" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shigongdanwei">
              <a-textarea v-model="model.shigongdanwei" rows="1" placeholder="" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="监理单位" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jianglidanwei">
              <a-textarea v-model="model.jianglidanwei" rows="1" placeholder="" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="检测项目" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jiancexiangmu">
              <a-textarea v-model="model.jiancexiangmu" rows="1" placeholder="" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="委托人" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="weituoren">
              <a-input v-model="model.weituoren" placeholder="" rows="1"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="委托日期" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="weituoriqi">
              <!--              <a-input v-model="model.weituoriqi" placeholder=""  ></a-input>-->
              <j-date
                placeholder="委托日期"
                v-model="model.weituoriqi"
                :showTime="true"
                dateFormat="YYYY-MM-DD HH:mm:ss"
              />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="取样时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="quyangshijian">
<!--              <a-input v-model="model.quyangshijian" placeholder=""  ></a-input>-->
              <j-date
                placeholder="取样时间"
                v-model="model.quyangshijian"
                :showTime="true"
                dateFormat="YYYY-MM-DD HH:mm:ss"
              />
            </a-form-model-item>
          </a-col>
<!--          <a-col :span="24">
            <a-form-model-item label="收样人" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shouyangren">
              <a-input v-model="model.shouyangren" placeholder=""  ></a-input>
            </a-form-model-item>
          </a-col>-->
<!--          <a-col :span="24">
            <a-form-model-item label="收样时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shouyangshijian">
              <a-input v-model="model.shouyangshijian" placeholder=""  ></a-input>
            </a-form-model-item>
          </a-col>-->
<!--          <a-col :span="24">
            <a-form-model-item label="收样状态" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shouyangzhuangtai">
              <a-input-number v-model="model.shouyangzhuangtai" placeholder="" style="width: 100%" />
            </a-form-model-item>
          </a-col>-->
<!--          <a-col :span="24">
            <a-form-model-item label="见证人" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jianzhengren">
              <a-input v-model="model.jianzhengren" placeholder=""  ></a-input>
            </a-form-model-item>
          </a-col>-->
<!--          <a-col :span="24">
            <a-form-model-item label="见证时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jianzhengshijian">
              <a-input v-model="model.jianzhengshijian" placeholder=""  ></a-input>
            </a-form-model-item>
          </a-col>-->
<!--          <a-col :span="24">
            <a-form-model-item label="见证状态" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jianzhengzhuangtai">
              <a-input-number v-model="model.jianzhengzhuangtai" placeholder="" style="width: 100%" />
            </a-form-model-item>
          </a-col>-->
<!--          <a-col :span="24">
            <a-form-model-item label="委托单编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="weituodanbianhao">
              <a-input v-model="model.weituodanbianhao" placeholder=""  ></a-input>
            </a-form-model-item>
          </a-col>-->
          <a-col :span="24">
            <a-form-model-item label="样品处理方式" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="yangpinchulifangshi">
              <a-textarea v-model="model.yangpinchulifangshi" rows="1" placeholder="" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="取样地点" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="quyangdidian">
              <a-textarea v-model="model.quyangdidian" rows="1" placeholder="" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="报告份数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="baogaofenshu">
              <a-input-number v-model="model.baogaofenshu" placeholder="" style="width: 100%" />
            </a-form-model-item>
          </a-col>


<!--          <a-col :span="24">
            <a-form-model-item label="施工单位合同号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shigongdanweihetonghao">
              <a-textarea v-model="model.shigongdanweihetonghao" rows="4" placeholder="" />
            </a-form-model-item>
          </a-col>-->
<!--          <a-col :span="24">
            <a-form-model-item label="监理单位合同号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jianlidanweihetonghao">
              <a-textarea v-model="model.jianlidanweihetonghao" rows="4" placeholder="" />
            </a-form-model-item>
          </a-col>-->
<!--          <a-col :span="24">
            <a-form-model-item label="监理单位组织机构id" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jianlidanweidepartid">
              <a-input v-model="model.jianlidanweidepartid" placeholder=""  ></a-input>
            </a-form-model-item>
          </a-col>-->
<!--          <a-col :span="24">
            <a-form-model-item label="监理单位组织机构编码" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jianlidanweiorgcode">
              <a-input v-model="model.jianlidanweiorgcode" placeholder=""  ></a-input>
            </a-form-model-item>
          </a-col>-->
<!--          <a-col :span="24">
            <a-form-model-item label="任务执行人" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="renwuzhixingren">
              <a-input v-model="model.renwuzhixingren" placeholder=""  ></a-input>
            </a-form-model-item>
          </a-col>-->
<!--          <a-col :span="24">
            <a-form-model-item label="执行状态" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="zhixingzhuangtai">
              <a-input-number v-model="model.zhixingzhuangtai" placeholder="" style="width: 100%" />
            </a-form-model-item>
          </a-col>-->
          <a-col :span="24">
            <a-form-model-item label="样品编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sampleno">
              <a-textarea v-model="model.sampleno" rows="1" placeholder="" />
            </a-form-model-item>
          </a-col>
<!--          <a-col :span="24">
            <a-form-model-item label="二维码编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="erweimabianhao">
              <a-input v-model="model.erweimabianhao" placeholder=""  ></a-input>
            </a-form-model-item>
          </a-col>-->
<!--          <a-col :span="24">
            <a-form-model-item label="二维码UUID" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="erweimaweiyima">
              <a-input v-model="model.erweimaweiyima" placeholder=""  ></a-input>
            </a-form-model-item>
          </a-col>-->
<!--          <a-col :span="24">
            <a-form-model-item label="委托状态" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="weituozhuangtai">
              <a-input-number v-model="model.weituozhuangtai" placeholder="" style="width: 100%" />
            </a-form-model-item>
          </a-col>-->
<!--          <a-col :span="24">
            <a-form-model-item label="指派的取样人名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="zhipaiquyangren">
              <a-input v-model="model.zhipaiquyangren" placeholder=""  ></a-input>
            </a-form-model-item>
          </a-col>-->
<!--          <a-col :span="24">
            <a-form-model-item label="指派时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="zhipaishijian">
              <a-input v-model="model.zhipaishijian" placeholder=""  ></a-input>
            </a-form-model-item>
          </a-col>-->
<!--          <a-col :span="24">
            <a-form-model-item label="指派人" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="zhipairen">
              <a-input v-model="model.zhipairen" placeholder=""  ></a-input>
            </a-form-model-item>
          </a-col>-->
<!--          <a-col :span="24">
            <a-form-model-item label="指派状态" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="zhipaizhuangtai">
              <a-input-number v-model="model.zhipaizhuangtai" placeholder="" style="width: 100%" />
            </a-form-model-item>
          </a-col>-->
<!--          <a-col :span="24">
            <a-form-model-item label="指派的取样人id" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="zhipaiquyangrenid">
              <a-input v-model="model.zhipaiquyangrenid" placeholder=""  ></a-input>
            </a-form-model-item>
          </a-col>-->
          <a-col :span="24">
            <a-form-model-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="beizhu">
<!--              <a-input v-model="model.beizhu" placeholder="" rows="4"></a-input>-->
              <a-textarea v-model="model.beizhu" rows="2" placeholder="" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="附件" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="fujian">
              <a-input v-model="model.fujian" placeholder=""  ></a-input>
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
    name: 'SyDpsYyYuancaiquyangweituoForm',
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
          add: "/qywtd/syDpsYyYuancaiquyangweituo/add",
          edit: "/qywtd/syDpsYyYuancaiquyangweituo/edit",
          queryById: "/qywtd/syDpsYyYuancaiquyangweituo/queryById"
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