<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="材料名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="cailiaomingcheng">
              <a-textarea v-model="model.cailiaomingcheng" rows="4" placeholder="请输入材料名称" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="材料厂家" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="cailiaochangjia">
              <a-textarea v-model="model.cailiaochangjia" rows="4" placeholder="请输入材料厂家" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="材料规格" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="guigexinghao">
              <a-textarea v-model="model.guigexinghao" rows="4" placeholder="请输入材料规格" />
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
            <a-form-model-item label="存放地点" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="chunfangdidian">
              <a-textarea v-model="model.chunfangdidian" rows="4" placeholder="请输入存放地点" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="进场数量(代表数量)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jinchangshuliang">
              <a-input v-model="model.jinchangshuliang" placeholder="请输入进场数量(代表数量)"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="使用部位" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shiyongbuwei">
              <a-textarea v-model="model.shiyongbuwei" rows="4" placeholder="请输入使用部位" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="质保单（附件）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="zhibaodan">
              <a-textarea v-model="model.zhibaodan" rows="4" placeholder="请输入质保单（附件）" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="监理抽检（0：否 1：是）默认0" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jianlichoujianzhuangtai">
              <a-input-number v-model="model.jianlichoujianzhuangtai" placeholder="请输入监理抽检（0：否 1：是）默认0" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="材料登记人" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="cailiaodengjiren">
              <a-textarea v-model="model.cailiaodengjiren" rows="4" placeholder="请输入材料登记人" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="材料登记时间（YYYY-MM-dd HH:mm:ss）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="cailiaodengjishijian">
              <a-input v-model="model.cailiaodengjishijian" placeholder="请输入材料登记时间（YYYY-MM-dd HH:mm:ss）"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="取样状态（0：未取样  1：已取样）默认0" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="quyangzhuangtai">
              <a-input-number v-model="model.quyangzhuangtai" placeholder="请输入取样状态（0：未取样  1：已取样）默认0" style="width: 100%" />
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
            <a-form-model-item label="外键，收样人，获取当前登录用户外键T_S_Base_User表的UserName字段" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shouyangren">
              <a-input v-model="model.shouyangren" placeholder="请输入外键，收样人，获取当前登录用户外键T_S_Base_User表的UserName字段"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="收样时间（YYYY-MM-dd HH:mm:ss）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shouyangshijian">
              <a-input v-model="model.shouyangshijian" placeholder="请输入收样时间（YYYY-MM-dd HH:mm:ss）"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="收样状态（0：未收样  1：已收样 2：在检）默认0" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shouyangzhuangtai">
              <a-input-number v-model="model.shouyangzhuangtai" placeholder="请输入收样状态（0：未收样  1：已收样 2：在检）默认0" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="外键，见证人，获取当前登录用户外键T_S_Base_User表的UserName字段" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jianzhengren">
              <a-input v-model="model.jianzhengren" placeholder="请输入外键，见证人，获取当前登录用户外键T_S_Base_User表的UserName字段"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="见证时间（YYYY-MM-dd HH:mm:ss）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jianzhengshijian">
              <a-input v-model="model.jianzhengshijian" placeholder="请输入见证时间（YYYY-MM-dd HH:mm:ss）"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="见证状态（0：未见证  1：已见证）默认0" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jianzhengzhuangtai">
              <a-input-number v-model="model.jianzhengzhuangtai" placeholder="请输入见证状态（0：未见证  1：已见证）默认0" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="委托单编号，唯一" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="weituodanbianhao">
              <a-input v-model="model.weituodanbianhao" placeholder="请输入委托单编号，唯一"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="委托状态（0：未委托 1：已委托 2:已取样 3：已收样 4：未执行 5：执行中 6：已完成）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="weituozhuangtai">
              <a-input-number v-model="model.weituozhuangtai" placeholder="请输入委托状态（0：未委托 1：已委托 2:已取样 3：已收样 4：未执行 5：执行中 6：已完成）" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="外键，施工单位组织机构id" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shigongdasnweidepartid">
              <a-input v-model="model.shigongdasnweidepartid" placeholder="请输入外键，施工单位组织机构id"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="外键，施工单位组织机构编码" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shigongdanweiorgcode">
              <a-input v-model="model.shigongdanweiorgcode" placeholder="请输入外键，施工单位组织机构编码"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="外键，任务执行人（由收样人分配），获取当前登录用户外键T_S_Base_User表的UserName字段" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="renwuzhixingren">
              <a-input v-model="model.renwuzhixingren" placeholder="请输入外键，任务执行人（由收样人分配），获取当前登录用户外键T_S_Base_User表的UserName字段"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="执行状态（0：未执行 1：执行中 2：已完成）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="zhixingzhuangtai">
              <a-input-number v-model="model.zhixingzhuangtai" placeholder="请输入执行状态（0：未执行 1：执行中 2：已完成）" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="外键，样品编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sampleno">
              <a-textarea v-model="model.sampleno" rows="4" placeholder="请输入外键，样品编号" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="二维码编号(处理后字段，id+序号)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="erweimabianhao">
              <a-input v-model="model.erweimabianhao" placeholder="请输入二维码编号(处理后字段，id+序号)"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="二维码UUID" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="erweimaweiyima">
              <a-input v-model="model.erweimaweiyima" placeholder="请输入二维码UUID"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="检测依据" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jianceyiju">
              <a-textarea v-model="model.jianceyiju" rows="4" placeholder="请输入检测依据" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="评定依据" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pandingyiju">
              <a-textarea v-model="model.pandingyiju" rows="4" placeholder="请输入评定依据" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="试验完成期限" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shiyanwanchengqixian">
              <a-input v-model="model.shiyanwanchengqixian" placeholder="请输入试验完成期限"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="jinchangshuliang1" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jinchangshuliang1">
              <a-input v-model="model.jinchangshuliang1" placeholder="请输入jinchangshuliang1"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="jinchangshuliang2" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jinchangshuliang2">
              <a-input v-model="model.jinchangshuliang2" placeholder="请输入jinchangshuliang2"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="guigexinghao1" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="guigexinghao1">
              <a-input v-model="model.guigexinghao1" placeholder="请输入guigexinghao1"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="guigexinghao2" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="guigexinghao2">
              <a-input v-model="model.guigexinghao2" placeholder="请输入guigexinghao2"  ></a-input>
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
          <a-col :span="24">
            <a-form-model-item label="guigexinghao3" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="guigexinghao3">
              <a-input v-model="model.guigexinghao3" placeholder="请输入guigexinghao3"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="jinchangshuliang3" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jinchangshuliang3">
              <a-input v-model="model.jinchangshuliang3" placeholder="请输入jinchangshuliang3"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="sgdwwaiweizhuangtai" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sgdwwaiweizhuangtai">
              <a-input v-model="model.sgdwwaiweizhuangtai" placeholder="请输入sgdwwaiweizhuangtai"  ></a-input>
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
    name: 'SyDpsYyYuancaijinchangdengjiForm',
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
          add: "/syycjcdj/syDpsYyYuancaijinchangdengji/add",
          edit: "/syycjcdj/syDpsYyYuancaijinchangdengji/edit",
          queryById: "/syycjcdj/syDpsYyYuancaijinchangdengji/queryById"
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