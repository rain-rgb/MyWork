<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="生产线（0公用 1 第一生产线  2 第二生产线）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="station">
              <a-input-number v-model="model.station" placeholder="请输入生产线（0公用 1 第一生产线  2 第二生产线）" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="任务单编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="code">
              <a-input v-model="model.code" placeholder="请输入任务单编号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="创建时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="dattim">
              <j-date placeholder="请选择创建时间" v-model="model.dattim"  style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="任务性质" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="attribute">
              <a-input v-model="model.attribute" placeholder="请输入任务性质"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="1线配合比编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="recipe">
              <a-input v-model="model.recipe" placeholder="请输入1线配合比编号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="2线配合比编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="recipes">
              <a-input v-model="model.recipes" placeholder="请输入2线配合比编号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="合同信息" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="contract">
              <a-input v-model="model.contract" placeholder="请输入合同信息"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="客户名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="customer">
              <a-input v-model="model.customer" placeholder="请输入客户名称"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="工程名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="projname">
              <a-input v-model="model.projname" placeholder="请输入工程名称"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="工程类别" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="projtype">
              <a-input v-model="model.projtype" placeholder="请输入工程类别"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="工程级别" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="projgrade">
              <a-input v-model="model.projgrade" placeholder="请输入工程级别"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="开工面积" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="projarea">
              <a-input-number v-model="model.projarea" placeholder="请输入开工面积" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="施工地址" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="projadr">
              <a-input v-model="model.projadr" placeholder="请输入施工地址"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="运输距离" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="distance">
              <a-input-number v-model="model.distance" placeholder="请输入运输距离" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="施工部位" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="conspos">
              <a-input v-model="model.conspos" placeholder="请输入施工部位"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="浇筑方式" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pour">
              <a-input v-model="model.pour" placeholder="请输入浇筑方式"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="产品种类" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="variety">
              <a-input v-model="model.variety" placeholder="请输入产品种类"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="强度等级" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="betlev">
              <a-input v-model="model.betlev" placeholder="请输入强度等级"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="抗渗等级" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="filter">
              <a-input v-model="model.filter" placeholder="请输入抗渗等级"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="抗冻等级" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="freeze">
              <a-input v-model="model.freeze" placeholder="请输入抗冻等级"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="坍落度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="lands">
              <a-input v-model="model.lands" placeholder="请输入坍落度"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="水泥品种" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="cement">
              <a-input v-model="model.cement" placeholder="请输入水泥品种"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="石子种类" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="stone">
              <a-input v-model="model.stone" placeholder="请输入石子种类"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="骨料粒径" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="bnsize">
              <a-input v-model="model.bnsize" placeholder="请输入骨料粒径"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="外加剂要求" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="addliq">
              <a-input v-model="model.addliq" placeholder="请输入外加剂要求"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="技术要求" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="request">
              <a-input v-model="model.request" placeholder="请输入技术要求"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="搅拌时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="mixlast">
              <a-input-number v-model="model.mixlast" placeholder="请输入搅拌时间" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="任务方量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="mete">
              <a-input-number v-model="model.mete" placeholder="请输入任务方量" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="浇注日期" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="begtim">
              <j-date placeholder="请选择浇注日期" v-model="model.begtim"  style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="截止日期" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="endtim">
              <j-date placeholder="请选择截止日期" v-model="model.endtim"  style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="调度人员" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="attamper">
              <a-input v-model="model.attamper" placeholder="请输入调度人员"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="标识" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="flag">
              <a-input v-model="model.flag" placeholder="请输入标识"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="note">
              <a-input v-model="model.note" placeholder="请输入备注"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="是否删除 0未删除 1已删除" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="isdel">
              <a-input-number v-model="model.isdel" placeholder="请输入是否删除 0未删除 1已删除" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="0 未审核  1已审核" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="status">
              <a-input-number v-model="model.status" placeholder="请输入0 未审核  1已审核" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="施工部位节点id" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="treeid">
              <a-input v-model="model.treeid" placeholder="请输入施工部位节点id"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="生产方量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="metes">
              <a-input-number v-model="model.metes" placeholder="请输入生产方量" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="进度百分比" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="bfb">
              <a-input-number v-model="model.bfb" placeholder="请输入进度百分比" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="jzlsts" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jzlsts">
              <a-input-number v-model="model.jzlsts" placeholder="请输入jzlsts" style="width: 100%" />
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
    name: 'BhzrwdxxForm',
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
          add: "/bhzrwdxx/bhzrwdxx/add",
          edit: "/bhzrwdxx/bhzrwdxx/edit",
          queryById: "/bhzrwdxx/bhzrwdxx/queryById"
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