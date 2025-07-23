<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="主键，自动增长" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="wtdid">
              <a-input-number v-model="model.wtdid" placeholder="请输入主键，自动增长" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="外键，原材进场登记表ID" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="yuancaijinchangdengjiid">
              <a-input-number v-model="model.yuancaijinchangdengjiid" placeholder="请输入外键，原材进场登记表ID" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="beizhu" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="beizhu">
              <a-input v-model="model.beizhu" placeholder="请输入beizhu"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="fujian" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="fujian">
              <a-input v-model="model.fujian" placeholder="请输入fujian"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="委托单编号，唯一" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="weituodanbianhao">
              <a-input v-model="model.weituodanbianhao" placeholder="请输入委托单编号，唯一"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="施工单位" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shigongdanwei">
              <a-textarea v-model="model.shigongdanwei" rows="4" placeholder="请输入施工单位" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="监理单位" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jianglidanwei">
              <a-textarea v-model="model.jianglidanwei" rows="4" placeholder="请输入监理单位" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="外键，施工单位组织机构编码" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jianlidanweiorgcode">
              <a-input v-model="model.jianlidanweiorgcode" placeholder="请输入外键，施工单位组织机构编码"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="监理单位" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="weituodanwei">
              <a-textarea v-model="model.weituodanwei" rows="4" placeholder="请输入监理单位" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="委托人" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="weituoren">
              <a-input v-model="model.weituoren" placeholder="请输入委托人"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="委托日期" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="weituoriqi">
              <a-input v-model="model.weituoriqi" placeholder="请输入委托日期"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="取样地点" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="quyangdidian">
              <a-textarea v-model="model.quyangdidian" rows="4" placeholder="请输入取样地点" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="材料名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="cailiaomingcheng">
              <a-textarea v-model="model.cailiaomingcheng" rows="4" placeholder="请输入材料名称" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="guigexinghao" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="guigexinghao">
              <a-input-number v-model="model.guigexinghao" placeholder="请输入guigexinghao" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="生产批号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shengchanpihao">
              <a-input v-model="model.shengchanpihao" placeholder="请输入生产批号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="出厂日期（YYYY-MM-dd）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="chuchangriqi">
              <a-input v-model="model.chuchangriqi" placeholder="请输入出厂日期（YYYY-MM-dd）"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="进场日期（YYYY-MM-dd）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jinchangriqi">
              <a-input v-model="model.jinchangriqi" placeholder="请输入进场日期（YYYY-MM-dd）"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="使用部位" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shiyongbuwei">
              <a-textarea v-model="model.shiyongbuwei" rows="4" placeholder="请输入使用部位" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="报告份数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="baogaofenshu">
              <a-input-number v-model="model.baogaofenshu" placeholder="请输入报告份数" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="样品处理方式" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="yangpinchulifangshi">
              <a-textarea v-model="model.yangpinchulifangshi" rows="4" placeholder="请输入样品处理方式" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="检测项目" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jiancexiangmu">
              <a-textarea v-model="model.jiancexiangmu" rows="4" placeholder="请输入检测项目" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="材料厂家" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="cailiaochangjia">
              <a-textarea v-model="model.cailiaochangjia" rows="4" placeholder="请输入材料厂家" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="委托状态（0：未提交 1：已提交）默认0，已提交时需更新原材登记表的委托状态为已委托" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="weituozhuangtai">
              <a-input-number v-model="model.weituozhuangtai" placeholder="请输入委托状态（0：未提交 1：已提交）默认0，已提交时需更新原材登记表的委托状态为已委托" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="指派状态（0：未指派1：已指派）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="zhipaizhuangtai">
              <a-input-number v-model="model.zhipaizhuangtai" placeholder="请输入指派状态（0：未指派1：已指派）" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="指派人，获取当前登录用户外键T_S_Base_User表的UserName字段" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="zhipairen">
              <a-input v-model="model.zhipairen" placeholder="请输入指派人，获取当前登录用户外键T_S_Base_User表的UserName字段"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="指派时间（YYYY-MM-dd HH:mm:ss）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="zhipaishijian">
              <a-input v-model="model.zhipaishijian" placeholder="请输入指派时间（YYYY-MM-dd HH:mm:ss）"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="指派的取样人名称，获取当前登录用户外键T_S_Base_User表的UserName字段" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="zhipaiquyangren">
              <a-input v-model="model.zhipaiquyangren" placeholder="请输入指派的取样人名称，获取当前登录用户外键T_S_Base_User表的UserName字段"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="jinchangshuliang" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jinchangshuliang">
              <a-input-number v-model="model.jinchangshuliang" placeholder="请输入jinchangshuliang" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="样品类型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="titcode">
              <a-input v-model="model.titcode" placeholder="请输入样品类型"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="外委状态" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="waiweizhuangtai">
              <a-input v-model="model.waiweizhuangtai" placeholder="请输入外委状态"  ></a-input>
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
    name: 'SyDpsYyWeiduodanlistviewForm',
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
           wtdid: [
              { required: true, message: '请输入主键，自动增长!'},
           ],
        },
        url: {
          add: "/sylxdps/syDpsYyWeiduodanlistview/add",
          edit: "/sylxdps/syDpsYyWeiduodanlistview/edit",
          queryById: "/sylxdps/syDpsYyWeiduodanlistview/queryById"
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