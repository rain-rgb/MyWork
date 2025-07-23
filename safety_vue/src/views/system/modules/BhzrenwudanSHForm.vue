<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form :form="form" slot="detail">
        <a-row>
          <a-row>
<!--            <a-col :span="12">-->
<!--              <a-form-item label="所属组织机构" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <JselectDqDepart v-decorator="['sysOrgCode']" ::multi="false"  />-->
<!--              </a-form-item>-->
<!--            </a-col>-->
            <a-col :span="12">
              <a-form-item label="生产线" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-dict-select-tag type="list" v-decorator="['station']" :trigger-change="true" dictCode="station" placeholder="请选择生产线" />
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
<!--            <a-col :span="12">-->
<!--              <a-form-item label="任务单编号" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <a-input v-decorator="['code']" placeholder="请输入任务单编号"  ></a-input>-->
<!--              </a-form-item>-->
<!--            </a-col>-->
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item label="任务性质" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['attribute']" placeholder="请输入任务性质"  ></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="创建时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-date placeholder="请选择创建时间" :showTime="showTime" :format="format" v-decorator="['dattim']" :trigger-change="true" style="width: 100%" />
              </a-form-item>
            </a-col>
<!--            <a-col :span="12">-->
<!--              <a-form-item label="1线配合比编号" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <a-input v-decorator="['recipe']" placeholder="请输入1线配合比编号"  ></a-input>-->
<!--              </a-form-item>-->
<!--            </a-col>-->
          </a-row>

<!--          <a-row>-->
<!--            <a-col :span="12">-->
<!--              <a-form-item label="2线配合比编号" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <a-input v-decorator="['recipes']" placeholder="请输入2线配合比编号"  ></a-input>-->
<!--              </a-form-item>-->
<!--            </a-col>-->

<!--          </a-row>-->

          <a-row>
            <a-col :span="12">
              <a-form-item label="请选择施工部位" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <JSelectDqProjName  v-decorator="['projgrade']" ::multi="false"  />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="客户名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['customer']" placeholder="请输入客户名称"  ></a-input>
              </a-form-item>
            </a-col>

<!--            <a-col :span="12">-->
<!--              <a-form-item label="运输距离" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <a-input-number v-decorator="['distance']" placeholder="请输入运输距离" style="width: 100%" />-->
<!--              </a-form-item>-->
<!--            </a-col>-->
          </a-row>



          <a-row>
            <a-col :span="12">
              <a-form-item label="工程名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['projname']" placeholder="请输入工程名称"  disabled="true"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="施工部位" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['conspos']" placeholder="请输入施工部位"  disabled="true"></a-input>
              </a-form-item>
            </a-col>
            <!--          <a-col :span="24">-->
            <!--            <a-form-item label="工程类别" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
            <!--              <a-input v-decorator="['projtype']" placeholder="请输入工程类别"  ></a-input>-->
            <!--            </a-form-item>-->
            <!--          </a-col>-->

          </a-row>

<!--          <a-row>-->

<!--            <a-col :span="12">-->
<!--              <a-form-item label="合同信息" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <a-input v-decorator="['contract']" placeholder="请输入合同信息"  ></a-input>-->
<!--              </a-form-item>-->
<!--            </a-col>-->
<!--          </a-row>-->

          <a-row>
            <a-col :span="12">
              <a-form-item label="浇筑方式" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-dict-select-tag type="list" v-decorator="['pour']" :trigger-change="true" dictCode="pour" placeholder="请选择浇筑方式" />
              </a-form-item>
            </a-col>
<!--            <a-col :span="12">-->
<!--              <a-form-item label="开工面积" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <a-input-number v-decorator="['projarea']" placeholder="请输入开工面积" style="width: 100%" />-->
<!--              </a-form-item>-->
<!--            </a-col>-->
            <a-col :span="12">
              <a-form-item label="施工地址" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['projadr']" placeholder="请输入施工地址"  ></a-input>
              </a-form-item>
            </a-col>
          </a-row>

<!--          <a-row>-->

<!--            <a-col :span="12">-->
<!--              <a-form-item label="产品种类" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <a-input v-decorator="['variety']" placeholder="请输入产品种类"  ></a-input>-->
<!--              </a-form-item>-->
<!--            </a-col>-->
<!--          </a-row>-->

          <a-row>
            <a-col :span="12">
              <a-form-item label="强度等级" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-dict-select-tag type="list" v-decorator="['betlev']" :trigger-change="true" dictCode="betlev" placeholder="请选择强度等级" />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="抗渗等级" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['filter']" placeholder="请输入抗渗等级"  ></a-input>
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item label="抗冻等级" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['freeze']" placeholder="请输入抗冻等级"  ></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="坍落度" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-dict-select-tag type="list" v-decorator="['lands']" :trigger-change="true" dictCode="lands" placeholder="请选择坍落度" />
              </a-form-item>
            </a-col>
          </a-row>

<!--          <a-row>-->
<!--            <a-col :span="12">-->
<!--              <a-form-item label="水泥品种" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <a-input v-decorator="['cement']" placeholder="请输入水泥品种"  ></a-input>-->
<!--              </a-form-item>-->
<!--            </a-col>-->
<!--            <a-col :span="12">-->
<!--              <a-form-item label="石子种类" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <a-input v-decorator="['stone']" placeholder="请输入石子种类"  ></a-input>-->
<!--              </a-form-item>-->
<!--            </a-col>-->
<!--          </a-row>-->

<!--          <a-row>-->
<!--            <a-col :span="12">-->
<!--              <a-form-item label="骨料粒径" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <a-input v-decorator="['bnsize']" placeholder="请输入骨料粒径"  ></a-input>-->
<!--              </a-form-item>-->
<!--            </a-col>-->
<!--            <a-col :span="12">-->
<!--              <a-form-item label="外加剂要求" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <a-input v-decorator="['addliq']" placeholder="请输入外加剂要求"  ></a-input>-->
<!--              </a-form-item>-->
<!--            </a-col>-->
<!--          </a-row>-->

<!--          <a-row>-->
<!--            <a-col :span="12">-->
<!--              <a-form-item label="技术要求" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <a-input v-decorator="['request']" placeholder="请输入技术要求"  ></a-input>-->
<!--              </a-form-item>-->
<!--            </a-col>-->
<!--            <a-col :span="12">-->
<!--              <a-form-item label="搅拌时间" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <a-input-number v-decorator="['mixlast']" placeholder="请输入搅拌时间" style="width: 100%" />-->
<!--              </a-form-item>-->
<!--            </a-col>-->
<!--          </a-row>-->

          <a-row>
            <a-col :span="12">
              <a-form-item label="任务方量" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['mete']" placeholder="请输入任务方量" style="width: 100%" />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="浇注日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-date placeholder="请选择浇注日期" :showTime="showTime"  v-decorator="['begtim']" :trigger-change="true" style="width: 100%" :format="format" />
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item label="截止日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-date placeholder="请选择截止日期" :showTime="showTime" v-decorator="['endtim']" :trigger-change="true" style="width: 100%" :format="format"/>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="调度人员" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['attamper']" placeholder="请输入调度人员"  ></a-input>
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
<!--            <a-col :span="12">-->
<!--              <a-form-item label="标识" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <a-input v-decorator="['flag']" placeholder="请输入标识"  ></a-input>-->
<!--              </a-form-item>-->
<!--            </a-col>-->
            <a-col :span="12">
              <a-form-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['note']" placeholder="请输入备注"  ></a-input>
              </a-form-item>
            </a-col>
          </a-row>

<!--          <a-col :span="24">-->
<!--            <a-form-item label="创建人" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['createBy']" placeholder="请输入创建人"  ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->

<!--          <a-col :span="24">-->
<!--            <a-form-item label="是否删除 0未删除 1已删除" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input-number v-decorator="['isdel']" placeholder="请输入是否删除 0未删除 1已删除" style="width: 100%" />-->
<!--            </a-form-item>-->
<!--          </a-col>-->
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
  import JselectDqDepart from '@/components/jeecgbiz/JselectDqDepart'
  import JSelectDqProjName from '@comp/jeecgbiz/JselectDqProjName'
  export default {
    name: 'BhzrenwudanSHForm',
    components: {
      JSelectDqProjName,
      JFormContainer,
      JDate,
      JselectDqDepart
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
        showTime:true,
        format:'YYYY-MM-DD HH:mm:ss',
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
          add: "/sys/sysDepartproject47/rewudanadd",
          edit: "/sys/sysDepartproject47/renwudanedit",
          queryById: "/system/bhzrenwudan/queryById"
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
          this.form.setFieldsValue(pick(this.model,'station','code','dattim','attribute','recipe','recipes','customer','projname','projtype','projgrade','projadr','conspos','pour','betlev','filter','freeze','lands','mete','begtim','endtim','attamper','note','createBy','sysOrgCode'))
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
        this.form.setFieldsValue(pick(row,'station','code','dattim','attribute','recipe','recipes','customer','projname','projtype','projgrade','projadr','conspos','pour','betlev','filter','freeze','lands','mete','begtim','endtim','attamper','note','createBy','sysOrgCode'))
      },
    }
  }
</script>