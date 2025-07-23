<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-item label="所属部门" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sysOrgCode">
              <JselectDqDepart v-model="model.sysOrgCode" ::multi="false"  />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="收货单位名" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="gongyingshangname">
              <a-input v-model="model.gongyingshangname" placeholder="请输入收货单位名"  ></a-input>
            </a-form-model-item>
          </a-col>
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="收货单位名字母缩写" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pinyin">-->
<!--              <a-input v-model="model.pinyin" placeholder="请输入收货单位名字母缩写"  ></a-input>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="单位编码" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="danweibianma">-->
<!--              <a-input v-model="model.danweibianma" placeholder="请输入单位编码"  ></a-input>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
          <a-col :span="24">
            <a-form-model-item label="收货材料" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="gongyingcailiao">
              <a-input v-model="model.gongyingcailiao" placeholder="请输入收货材料"  ></a-input>
            </a-form-model-item>
          </a-col>
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="所属地区" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="suoshudiqu">-->
<!--              <a-input v-model="model.suoshudiqu" placeholder="请输入所属地区"  ></a-input>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="联系人" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="lianxiren">-->
<!--              <a-input v-model="model.lianxiren" placeholder="请输入联系人"  ></a-input>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="联系电话" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="lianxidianhua">-->
<!--              <a-input v-model="model.lianxidianhua" placeholder="请输入联系电话"  ></a-input>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="手机" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shouji">-->
<!--              <a-input v-model="model.shouji" placeholder="请输入手机"  ></a-input>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="传真" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="chuanzhen">-->
<!--              <a-input v-model="model.chuanzhen" placeholder="请输入传真"  ></a-input>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="邮编" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="youbian">-->
<!--              <a-input v-model="model.youbian" placeholder="请输入邮编"  ></a-input>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="通讯地址" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="tongxundizhi">-->
<!--              <a-input v-model="model.tongxundizhi" placeholder="请输入通讯地址"  ></a-input>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="网址" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="wangzhi">-->
<!--              <a-input v-model="model.wangzhi" placeholder="请输入网址"  ></a-input>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="证书名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="zhengshumingcheng">-->
<!--              <a-input v-model="model.zhengshumingcheng" placeholder="请输入证书名称"  ></a-input>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="证书编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="zhengshubianhao">-->
<!--              <a-input v-model="model.zhengshubianhao" placeholder="请输入证书编号"  ></a-input>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="发证时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="fazhengshijian">-->
<!--              <j-date placeholder="请选择发证时间" v-model="model.fazhengshijian"  style="width: 100%" />-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="有效期限" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="youxiaoqixian">-->
<!--              <j-date placeholder="请选择有效期限" v-model="model.youxiaoqixian"  style="width: 100%" />-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="发证机关" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="fazhengjiguan">-->
<!--              <a-input v-model="model.fazhengjiguan" placeholder="请输入发证机关"  ></a-input>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="证件图片" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="zhengjiantupian">-->
<!--              <a-input v-model="model.zhengjiantupian" placeholder="请输入证件图片"  ></a-input>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="开户银行" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="kaihuyinhang">-->
<!--              <a-input v-model="model.kaihuyinhang" placeholder="请输入开户银行"  ></a-input>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="银行账号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="yinhangzhanghao">-->
<!--              <a-input v-model="model.yinhangzhanghao" placeholder="请输入银行账号"  ></a-input>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="企业税号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="qiyeshuihao">-->
<!--              <a-input v-model="model.qiyeshuihao" placeholder="请输入企业税号"  ></a-input>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="邮箱" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="youxiang">-->
<!--              <a-input v-model="model.youxiang" placeholder="请输入邮箱"  ></a-input>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="remark">-->
<!--              <a-input v-model="model.remark" placeholder="请输入备注"  ></a-input>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="企业状态:0=正常,1=异常" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="states">-->
<!--              <a-input v-model="model.states" placeholder="请输入企业状态:0=正常,1=异常"  ></a-input>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="收货材料名" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="gongyingcailiaoname">-->
<!--              <a-input v-model="model.gongyingcailiaoname" placeholder="请输入收货材料名"  ></a-input>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="UUID" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="guid">-->
<!--              <a-input v-model="model.guid" placeholder="请输入UUID"  ></a-input>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="时间戳" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="ts">-->
<!--              <a-input-number v-model="model.ts" placeholder="请输入时间戳" style="width: 100%" />-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="状态" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="status">-->
<!--              <a-input-number v-model="model.status" placeholder="请输入状态" style="width: 100%" />-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="是否删除:0=否,1=是" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="isdel">-->
<!--              <a-input-number v-model="model.isdel" placeholder="请输入是否删除:0=否,1=是" style="width: 100%" />-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="组织机构ID" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="departid">-->
<!--              <a-input v-model="model.departid" placeholder="请输入组织机构ID"  ></a-input>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="修改人" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="editperson">-->
<!--              <a-input v-model="model.editperson" placeholder="请输入修改人"  ></a-input>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="修改时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="editdatetime">-->
<!--              <j-date placeholder="请选择修改时间" v-model="model.editdatetime"  style="width: 100%" />-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
          <a-col :span="24">
            <a-form-model-item label="是否使用电子锁" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="iselocks">
              <j-dict-select-tag type="list"  v-model="model.iselocks" dictCode="yn" placeholder="请选择是否使用电子锁" />
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
  import JselectDqDepart from '@comp/jeecgbiz/JselectDqDepart'
  import { DEPART_ID } from '@/store/mutation-types'
  import Vue from 'vue'

  export default {
    name: 'WzshouhuodanweiForm',
    components: {
      JselectDqDepart
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
          add: "/wzshouhuodanwei/wzshouhuodanwei/add",
          edit: "/wzshouhuodanwei/wzshouhuodanwei/edit",
          queryById: "/wzshouhuodanwei/wzshouhuodanwei/queryById"
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
            this.model.departid=Vue.ls.get(DEPART_ID);
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