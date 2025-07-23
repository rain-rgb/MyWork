<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form :form="form" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-item label="报告类型
1:粗骨料
2：粉煤灰
3：水泥
4：细骨料
" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['state']" placeholder="请输入报告类型
1:粗骨料
2：粉煤灰
3：水泥
4：细骨料
"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="报表" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['biaohao']" placeholder="请输入报表"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="标准代号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['biaozhundaihao']" placeholder="请输入标准代号"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="委托单位" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['weituodanwei']" placeholder="请输入委托单位"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="委托编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['weituobianhao']" placeholder="请输入委托编号"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="样品名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['yangpingname']" placeholder="请输入样品名称"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="送样编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['songyangno']" placeholder="请输入送样编号"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="产地厂名" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['chandichangming']" placeholder="请输入产地厂名"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="试样数量" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['shiyangnum']" placeholder="请输入试样数量"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="规格种类" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['guigezhonglei']" placeholder="请输入规格种类"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="取样地点" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['quyangdidian']" placeholder="请输入取样地点"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="代表数量(吨)" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['daibiaonum']" placeholder="请输入代表数量(吨)"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="样品描述" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['yangpingmiaoshu']" placeholder="请输入样品描述"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="取样日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['quyangtime']" placeholder="请输入取样日期"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="工程名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['gongchengmingchen']" placeholder="请输入工程名称"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="施工部位" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['shigongbuwei']" placeholder="请输入施工部位"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="检验标准" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['jianyanbiaozhun']" placeholder="请输入检验标准"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="试验项目" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['shiyanxiangmu']" placeholder="请输入试验项目"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="收样日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['shouyangtime']" placeholder="请输入收样日期"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="试样验收意见" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['yanshouyijian']" placeholder="请输入试样验收意见"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="取样人" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['quyangren']" placeholder="请输入取样人"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="取样见证人" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['quyangjianzhengren']" placeholder="请输入取样见证人"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="委托人" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['weituoren']" placeholder="请输入委托人"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="委托单位负责人" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['weituodanweifuzeren']" placeholder="请输入委托单位负责人"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="住址及邮编" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['zhuzhaijiyoubian']" placeholder="请输入住址及邮编"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="联系电话" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['lianxiphone']" placeholder="请输入联系电话"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="收样人" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['shouyangren']" placeholder="请输入收样人"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="预计报告发出日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-date placeholder="请选择预计报告发出日期" v-decorator="['yujifachutime']" :trigger-change="true" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="tzid" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['tzid']" placeholder="请输入tzid" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="moduleid" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['moduleid']" placeholder="请输入moduleid"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="modulename" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['modulename']" placeholder="请输入modulename"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="是否录入" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['shifouluru']" placeholder="请输入是否录入"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="是否删除" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['isdel']" placeholder="请输入是否删除"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="材料编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['cailiaono']" placeholder="请输入材料编号"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="批次" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['pici']" placeholder="请输入批次"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="设备编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['shebeibianhao']" placeholder="请输入设备编号"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="净重（吨）" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['jingzhongt']" placeholder="请输入净重（吨）"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="进场时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['jinchangshijian']" placeholder="请输入进场时间"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="收样id" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['shouyangid']" placeholder="请输入收样id"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="创建时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-date placeholder="请选择创建时间" v-decorator="['createtime']" :trigger-change="true" style="width: 100%" />
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
  import JDate from '@/components/jeecg/JDate'  

  export default {
    name: 'WztaizhangweituodanForm',
    components: {
      JFormContainer,
      JDate,
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
        },
        url: {
          add: "/wztaizhangweituodan/wztaizhangweituodan/add",
          edit: "/wztaizhangweituodan/wztaizhangweituodan/edit",
          queryById: "/wztaizhangweituodan/wztaizhangweituodan/queryById"
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
          this.form.setFieldsValue(pick(this.model,'state','biaohao','biaozhundaihao','weituodanwei','weituobianhao','yangpingname','songyangno','chandichangming','shiyangnum','guigezhonglei','quyangdidian','daibiaonum','yangpingmiaoshu','quyangtime','gongchengmingchen','shigongbuwei','jianyanbiaozhun','shiyanxiangmu','shouyangtime','yanshouyijian','quyangren','quyangjianzhengren','weituoren','weituodanweifuzeren','zhuzhaijiyoubian','lianxiphone','shouyangren','yujifachutime','tzid','moduleid','modulename','shifouluru','isdel','cailiaono','pici','shebeibianhao','jingzhongt','jinchangshijian','shouyangid','createtime'))
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
        this.form.setFieldsValue(pick(row,'state','biaohao','biaozhundaihao','weituodanwei','weituobianhao','yangpingname','songyangno','chandichangming','shiyangnum','guigezhonglei','quyangdidian','daibiaonum','yangpingmiaoshu','quyangtime','gongchengmingchen','shigongbuwei','jianyanbiaozhun','shiyanxiangmu','shouyangtime','yanshouyijian','quyangren','quyangjianzhengren','weituoren','weituodanweifuzeren','zhuzhaijiyoubian','lianxiphone','shouyangren','yujifachutime','tzid','moduleid','modulename','shifouluru','isdel','cailiaono','pici','shebeibianhao','jingzhongt','jinchangshijian','shouyangid','createtime'))
      },
    }
  }
</script>