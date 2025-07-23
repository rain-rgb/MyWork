<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="取样单流水号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="quyangdanliushuihao">
              <a-input v-model="model.quyangdanliushuihao" placeholder="请输入取样单流水号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="主键，自增长" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="quyangdanid">
              <a-input-number v-model="model.quyangdanid" placeholder="请输入主键，自增长" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="材料名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="yangpingmingcheng">
              <a-textarea v-model="model.yangpingmingcheng" rows="4" placeholder="请输入材料名称" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="材料厂家" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="cailiaochangjia">
              <a-textarea v-model="model.cailiaochangjia" rows="4" placeholder="请输入材料厂家" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="guigexinghao" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="guigexinghao">
              <a-input-number v-model="model.guigexinghao" placeholder="请输入guigexinghao" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="材料规格" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="guigexinghao1">
              <a-textarea v-model="model.guigexinghao1" rows="4" placeholder="请输入材料规格" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="guigexinghao2" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="guigexinghao2">
              <a-input v-model="model.guigexinghao2" placeholder="请输入guigexinghao2"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="guigexinghao3" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="guigexinghao3">
              <a-input v-model="model.guigexinghao3" placeholder="请输入guigexinghao3"  ></a-input>
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
            <a-form-model-item label="外键，施工单位组织机构编码" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="orgcode">
              <a-input v-model="model.orgcode" placeholder="请输入外键，施工单位组织机构编码"  ></a-input>
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
            <a-form-model-item label="取样地点" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="quyangdidian">
              <a-textarea v-model="model.quyangdidian" rows="4" placeholder="请输入取样地点" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="成型日期(YYYY-MM-dd)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="chengxingriqi">
              <a-input v-model="model.chengxingriqi" placeholder="请输入成型日期(YYYY-MM-dd)"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="样品数量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="yangpingshuliang">
              <a-input v-model="model.yangpingshuliang" placeholder="请输入样品数量"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="样品描述" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="yangpinmiaoshu">
              <a-textarea v-model="model.yangpinmiaoshu" rows="4" placeholder="请输入样品描述" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="daibiaoshuliang" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="daibiaoshuliang">
              <a-input-number v-model="model.daibiaoshuliang" placeholder="请输入daibiaoshuliang" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="进场数量(代表数量)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jinchangshuliang1">
              <a-input v-model="model.jinchangshuliang1" placeholder="请输入进场数量(代表数量)"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="jinchangshuliang2" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jinchangshuliang2">
              <a-input v-model="model.jinchangshuliang2" placeholder="请输入jinchangshuliang2"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="jinchangshuliang3" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jinchangshuliang3">
              <a-input v-model="model.jinchangshuliang3" placeholder="请输入jinchangshuliang3"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="检测项目" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jiancexiangmu">
              <a-textarea v-model="model.jiancexiangmu" rows="4" placeholder="请输入检测项目" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="抽样人员" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="chouyangrenyuan">
              <a-input v-model="model.chouyangrenyuan" placeholder="请输入抽样人员"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="外键，见证人，获取当前登录用户外键T_S_Base_User表的UserName字段" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jianzhengren">
              <a-input v-model="model.jianzhengren" placeholder="请输入外键，见证人，获取当前登录用户外键T_S_Base_User表的UserName字段"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="外键，收样人，获取当前登录用户外键T_S_Base_User表的UserName字段" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shouyangren">
              <a-input v-model="model.shouyangren" placeholder="请输入外键，收样人，获取当前登录用户外键T_S_Base_User表的UserName字段"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="主键，自动增长" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="wtdid">
              <a-input-number v-model="model.wtdid" placeholder="请输入主键，自动增长" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="收样状态（0：未收样  1：已收样 2：已提交 3：在检）默认0" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shouyangzhuangtai">
              <a-input-number v-model="model.shouyangzhuangtai" placeholder="请输入收样状态（0：未收样  1：已收样 2：已提交 3：在检）默认0" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="收样时间（YYYY-MM-dd HH:mm:ss）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shouyangshijian">
              <a-input v-model="model.shouyangshijian" placeholder="请输入收样时间（YYYY-MM-dd HH:mm:ss）"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="外键，取样人，获取当前登录用户外键T_S_Base_User表的UserName字段" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="quyangren">
              <a-input v-model="model.quyangren" placeholder="请输入外键，取样人，获取当前登录用户外键T_S_Base_User表的UserName字段"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="取样时间（YYYY-MM-dd HH:mm:ss）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="quyangshijian">
              <a-input v-model="model.quyangshijian" placeholder="请输入取样时间（YYYY-MM-dd HH:mm:ss）"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="取样状态（0：未取样  1：已取样）默认0" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="quyangzhuangtai">
              <a-input-number v-model="model.quyangzhuangtai" placeholder="请输入取样状态（0：未取样  1：已取样）默认0" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="二维码编号(处理后字段，id+序号)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="erweimabianhao">
              <a-input v-model="model.erweimabianhao" placeholder="请输入二维码编号(处理后字段，id+序号)"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="gongcghengmingcheng" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="gongcghengmingcheng">
              <a-input v-model="model.gongcghengmingcheng" placeholder="请输入gongcghengmingcheng"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="主键，自动增长" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="yuancaijinchangdengjiid">
              <a-input-number v-model="model.yuancaijinchangdengjiid" placeholder="请输入主键，自动增长" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="外键，样品编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sampleno">
              <a-textarea v-model="model.sampleno" rows="4" placeholder="请输入外键，样品编号" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="样品类型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="titcode">
              <a-input v-model="model.titcode" placeholder="请输入样品类型"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="renwudanliushuihao" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="renwudanliushuihao">
              <a-input v-model="model.renwudanliushuihao" placeholder="请输入renwudanliushuihao"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="检测人员" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jiancerenyuan">
              <a-input v-model="model.jiancerenyuan" placeholder="请输入检测人员"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="外委状态" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="waiweizhuangtai">
              <a-input v-model="model.waiweizhuangtai" placeholder="请输入外委状态"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="外键，试验项目类型（dps_jc_testItemType表）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sampletitcode">
              <a-input v-model="model.sampletitcode" placeholder="请输入外键，试验项目类型（dps_jc_testItemType表）"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="材料名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="cailiaomingcheng">
              <a-textarea v-model="model.cailiaomingcheng" rows="4" placeholder="请输入材料名称" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="委托日期" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="weituoriqi">
              <a-input v-model="model.weituoriqi" placeholder="请输入委托日期"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="指派时间（YYYY-MM-dd HH:mm:ss）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="zhipaishijian">
              <a-input v-model="model.zhipaishijian" placeholder="请输入指派时间（YYYY-MM-dd HH:mm:ss）"  ></a-input>
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
    name: 'SyDpsYyQydlistviewForm',
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
           quyangdanid: [
              { required: true, message: '请输入主键，自增长!'},
           ],
        },
        url: {
          add: "/sylxdps/syDpsYyQydlistview/add",
          edit: "/sylxdps/syDpsYyQydlistview/edit",
          queryById: "/sylxdps/syDpsYyQydlistview/queryById"
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