<template>
  <a-spin :spinning="confirmLoading">
<!--    <j-form-container :disabled="formDisabled">-->
<!--      <a-form :form="form" slot="detail">-->
<!--        <a-row>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="工程名称" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['projectName']" placeholder="请输入工程名称"  ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="材料消耗部位" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['poureLocation']" placeholder="请输入材料消耗部位"  ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="砼消耗数量(m³)" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input-number v-decorator="['estimateNumber']" placeholder="请输入砼消耗数量(m³)" style="width: 100%" />-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="砼标记" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['strengthRank']" placeholder="请输入砼标记"  ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="控制权限" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['sysOrgCode']" placeholder="请输入控制权限"  ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col v-if="showFlowSubmitButton" :span="24" style="text-align: center">-->
<!--            <a-button @click="submitForm">提 交</a-button>-->
<!--          </a-col>-->
<!--        </a-row>-->
<!--      </a-form>-->
<!--    </j-form-container>-->
    <j-editable-table
      :ref="refKeys[0]"
      :loading="wzconsumetaizhangDetailTable.loading"
      :columns="wzconsumetaizhangDetailTable.columns"
      :dataSource="wzconsumetaizhangDetailTable.dataSource"
      :disabled="formDisabled"
      :rowNumber="true"
      :rowSelection="false"
      :actionButton="false">
    </j-editable-table>
  </a-spin>
</template>

<script>

  import { httpAction, getAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import JFormContainer from '@/components/jeecg/JFormContainer'
  import { FormTypes } from '@/utils/JEditableTableUtil'

  export default {
    name: 'WzconsumetaizhangForm',
    components: {
      JFormContainer,
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
        refKeys: ['wzconsumetaizhangDetail', ],
        tableKeys:['wzconsumetaizhangDetail', ],
        activeKey: 'wzconsumetaizhangDetail',
        // 拌合站子表材料信息
        wzconsumetaizhangDetailTable: {
          loading: false,
          dataSource: [],
          columns: [

            {
              title: '材料名称',
              key: 'materialeName',
              type: FormTypes.normal,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '料仓名称',
              key: 'name',
              type: FormTypes.normal,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '检验批次',
              key: 'pici',
              type: FormTypes.normal,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '批次库存(吨)',
              key: 'picizhong',
              type: FormTypes.normal,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '材料消耗(吨)',
              key: 'realityNumber',
              type: FormTypes.normal,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '材料剩余(吨)',
              key: 'cailiaoshengyut',
              type: FormTypes.normal,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            }

          ]
        },
        url: {
          add: "/wzconsumetaizhang/wzconsumetaizhang/add",
          edit: "/wzconsumetaizhang/wzconsumetaizhang/edit",
          queryById: "/wzconsumetaizhang/wzconsumetaizhang/queryById",
          wzconsumetaizhangDetail: {
            list: '/wzconsumetaizhangdetail/wzconsumetaizhangDetail/list'
          },
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
          this.form.setFieldsValue(pick(this.model))
          this.getData();
            //,'projectName','poureLocation','estimateNumber','strengthRank','sysOrgCode'
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
      getData:function (){
        this.wzconsumetaizhangDetailTable.dataSource=[]
        let params = {xhId:this.model.id,pageSize:30};
        getAction(this.url.wzconsumetaizhangDetail.list, params,).then((res)=>{
          //console.log(res)
          if(res.success)  {
            this.wzconsumetaizhangDetailTable.dataSource = res.result.records;
          }
        });
      },
      popupCallback(row){
        this.form.setFieldsValue(pick(row))
          //,'projectName','poureLocation','estimateNumber','strengthRank','sysOrgCode'
      },
    }
  }
</script>