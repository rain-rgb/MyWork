<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form :form="form" slot="detail">
        <a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item label="任务单编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['code',validatorRules.code]" placeholder="请输入任务单编号"  ></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="请选择施工部位" :labelCol="labelCol" :wrapperCol="wrapperCol" >
                <JSelectDqProjName  v-decorator="['projgrade',validatorRules.projgrade]" ::multi="false" />
<!--                {{ getFormFieldValue('projgrade') }}-->
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="24">
              <a-form-item label="工程名称" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
                <a-input v-decorator="['projname']" placeholder="请输入工程名称"  ></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="24">
              <a-form-item label="施工部位" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
                <a-input v-decorator="['conspos']" placeholder="请输入施工部位"  ></a-input>
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item label="制梁台座" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-search-select-tag  placeholder="请选择台座名称" v-decorator="['taizuono']" :dictOptions="dictOption" >
                </j-search-select-tag>
                {{ selectValue }}
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item label="生产线" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-dict-select-tag type="list" v-decorator="['station']" :trigger-change="true" dictCode="stations" placeholder="请选择生产线" />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="施工地址" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['projadr']" placeholder="请输入施工地址"  ></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item label="浇筑方式" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-dict-select-tag type="list" v-decorator="['pour']" :trigger-change="true" dictCode="pour" placeholder="请选择浇筑方式" />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="产品种类" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['variety']" placeholder="请输入产品种类"  ></a-input>
              </a-form-item>
            </a-col>
          </a-row>

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
          <a-row>
            <a-col :span="12">
              <a-form-item label="任务方量" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['mete']" placeholder="请输入任务方量" style="width: 100%" />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="搅拌时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['mixlast']" placeholder="请输入搅拌时间" style="width: 100%" />
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>

            <a-col :span="12">
              <a-form-item label="浇注日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-date placeholder="请选择浇注日期" v-decorator="['begtim']" :trigger-change="true" style="width: 100%"/>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="截止日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-date placeholder="请选择截止日期" v-decorator="['endtim']" :trigger-change="true" style="width: 100%"/>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item label="计划生产时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-date placeholder="请选择计划生产时间" v-decorator="['productiontime',validatorRules.productiontime]" :trigger-change="true" style="width: 100%"/>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['note']" placeholder="请输入备注"  ></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item label="计划架梁时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-date placeholder="请选择计划架梁时间" v-decorator="['productiontime1',validatorRules.productiontime1]" :trigger-change="true" style="width: 100%"/>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="粱订单" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-search-select-tag  placeholder="请选择粱订单" v-decorator="['oderno']" :dictOptions="dictOptions" >
                </j-search-select-tag>
<!--                <a-input v-decorator="['oderno']" placeholder="请选择粱订单"  ></a-input>-->
              </a-form-item>
            </a-col>
          </a-row>

        </a-row>
      </a-form>
    </j-form-container>
    <!-- 子表单区域 -->
    <a-tabs v-model="activeKey" @change="handleChangeTabs">
      <a-tab-pane tab="制梁任务单工序选择" :key="refKeys[0]" :forceRender="true">
        <j-editable-table
          :ref="refKeys[0]"
          :loading="zhiliangGongxuTable.loading"
          :columns="zhiliangGongxuTable.columns"
          :dataSource="zhiliangGongxuTable.dataSource"
          :maxHeight="300"
          :disabled="formDisabled"
          :rowNumber="true"
          :rowSelection="true"
          :actionButton="true">
        </j-editable-table>
      </a-tab-pane>
    </a-tabs>

  </a-spin>
</template>

<script>

  import { httpAction, getAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import JFormContainer from '@/components/jeecg/JFormContainer'
  import JDate from '@/components/jeecg/JDate'
  import JselectDqDepart from '@comp/jeecgbiz/JselectDqDepart'
  import JSelectDqProjName from '@comp/jeecgbiz/JselectDqProjName'
  import { FormTypes,getRefPromise } from '@/utils/JEditableTableUtil'
  import JEditableTable from '@comp/jeecg/JEditableTable'
  import { JEditableTableMixin } from '@/mixins/JEditableTableMixin'
  import { duplicateCheck3 } from '@api/api'
  import Vue from 'vue'
  export default {
    name: 'ZhiliangrenwudanForm',
    mixins: [JEditableTableMixin],
    components: {
      JFormContainer,
      JDate,
      JselectDqDepart,
      JSelectDqProjName,
      JEditableTable
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
        dictOptions:[],
        codeid:0,
        selectValue:'',
        dictOption:[],
        //form: this.$form.createForm(this),
        model: {},
        labelCol: {
          xs: { span: 24 },
          sm: { span: 6 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
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
        addDefaultRowNum:0,
        refKeys: ['zhiliangGongxu',],
        tableKeys: ['zhiliangGongxu',],
        activeKey: 'zhiliangGongxu',
        // 砼拌合站理论配合比子表
        zhiliangGongxuTable: {
          loading: false,
          dataSource: [],
          columns: [
            {
              title: '工序',
              key: 'xuhao',
              dictCode:'xuhao',
              width: '300px',
              type: FormTypes.select,
              //slotName: 'materialTypes',
              options: [],
              placeholder: '请输入${title}',
              validateRules: [{ required: true, message: '请选择${title}' }]
            },
            {
              title: '工序时长',
              key: 'remark',
              width: '300px',
              type: FormTypes.input,
              //slotName: 'materialTypes',
              options: [],
              placeholder: '请输入${title}',
              validateRules: [{ required: true, message: '请选择${title}' }]
            },
            {
              title: '时长单位',
              key: 'unit',
              dictCode:'unit',
              width: '300px',
              type: FormTypes.select,
              //slotName: 'materialTypes',
              options: [],
              placeholder: '请输入${title}',
              validateRules: [{ required: true, message: '请选择${title}' }]
            },
          ]
        },
        validatorRules: {
          code: {
            rules: [
              { validator: this.validatecode},
            ]
          },
          projgrade: {
            rules: [
              { validator: this.validateprojgrade},
            ]
          },
          productiontime: {
            rules: [{
              required: true, message: '请选择计划生产时间!'
            }]
          }
          // productiontime1: {
          //   rules: [{
          //     required: true, message: '请选择计划架梁时间!'
          //   }]
          // }
        },
        url: {
          add: "/sys/sysDepartproject47/zlrenwudanadd",
          edit: "/sys/sysDepartproject47/zlrenwudanedit",
          queryById: "/zhiliangrenwudan/zhiliangrenwudan/queryById",
          list: '/zhiliangtaizuocfg/zhiliangTaizuoCfg/list1',
          zhiliangGongxu: {
            list: '/zhiliangrenwudan/zhiliangrenwudan/queryZhiliangGongxuByMainId'
          },
          list1:"/beamorder/beamOrder/list1"
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
      this.taizuoList();
      this.orderList();
    },
    methods: {
      addBefore() {
        this.form.resetFields();
        this.zhiliangGongxuTable.dataSource = []
      },
      getAllTable() {
        let values = this.tableKeys.map(key => getRefPromise(this, key))
        return Promise.all(values)
      },
      /** 调用完edit()方法之后会自动调用此方法 */
      editAfter() {
        let fieldval = pick(this.model,'station','code','dattim','attribute','recipe','recipes','contract',
          'customer','projname','projtype','projgrade','projarea','projadr','distance','conspos','pour','variety',
          'betlev','filter','freeze','lands','cement','stone','bnsize','addliq','request','mixlast','mete','begtim',
          'endtim','attamper','flag','note','createBy','sysOrgCode','isdel','status','jiaozhustatus','jingzhistatus',
          'zhengyangstatus1','zhengyangstatus2','zhanglastatus','tiliangstatus','productiontime','taizuono','xuhao')
        this.$nextTick(() => {
          this.form.setFieldsValue(fieldval)
        })
        // 加载子表数据
        if (this.model.uuid) {
          let params = { id: this.model.uuid }
          this.requestSubTableData(this.url.zhiliangGongxu.list, params, this.zhiliangGongxuTable)
        }
        this.codeid = this.model.id
      },
      /** 整理成formData */
      classifyIntoFormData(allValues) {
        let main = Object.assign(this.model, allValues.formValue)
        return {
          ...main, // 展开
          zhiliangGongxuList: allValues.tablesValue[0].values,
        }
      },
      //渲染流程表单数据
      showFlowData() {
        if (this.formBpm === true) {
          let params = { id: this.formData.dataId }
          getAction(this.url.queryById, params).then((res) => {
            if (res.success) {
              this.edit(res.result)
            }
          })
        }
      },
      validateError(msg) {
        this.$message.error(msg)
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
      validatecode(rule, value, callback){
        if(!value){
          callback()
        }else{
          if(value){
            var params = {
              tableName: 'zhiliangrenwudan',
              fieldName: 'code',
              fieldVal: value,
              dataId: this.model.id,
            };
            duplicateCheck3(params).then((res) => {
              console.log(res)
              if (res.success) {
                callback()
              } else {
                callback("任务单编号已存在!")
              }
            })
          }else{
            callback("请输入正确格式的任务单编号!")
          }
        }
      },
      validateprojgrade(rule, value, callback){
        if(!value){
          callback()
        }else{
          if(value){
            const params = {
              tableName: 'zhiliangrenwudan',
              fieldName: 'ProjGrade',
              fieldVal: value,
              dataId: this.model.id,
            }
            duplicateCheck3(params).then((res) => {
              console.log(res)
              if (res.success) {
                callback()
              } else {
                callback("施工部位已存在!")
              }
            })
          }else{
            callback("请输入正确格式的施工部位!")
          }
        }
      },
      taizuoList:function (){
        let params = {}
        this.dictOption=[];
        getAction(this.url.list,params).then(res=>{
          if (res.success){
            let result=res.result;
            result.forEach(re=>{
              this.dictOption.push({text:re.taizuoname,value:re.taizuono})
            })
          }
        })
      },
      orderList:function (){
        var sys_depart_orgcode=Vue.ls.get('SYS_DEPART_ORGCODE');
        console.log("sys_depart_orgcode",sys_depart_orgcode)
        getAction(this.url.list1, {
          sys_org_code:sys_depart_orgcode,
          column: 'id',
          order: 'desc'
        }).then(res=>{
          if (res.success){
            let result=res.result;
            result.forEach(re=>{
              this.dictOptions.push({text:re.orderno,value:re.orderno})
            })
          }
        })
      },
      // getFormFieldValue(field){
      //   if (this.form.getFieldValue(field) !== undefined) {
      //     this.value = this.form.getFieldValue(field)
      //     console.log("_____+++++++++++++++++++++field", this.value)
      //   }
      // },
      popupCallback(row){
        this.form.setFieldsValue(pick(row,'station','code','dattim','attribute','recipe','recipes','contract','customer','projname','projtype','projgrade','projarea','projadr','distance','conspos','pour','variety','betlev','filter','freeze','lands','cement','stone','bnsize','addliq','request','mixlast','mete','begtim','endtim','attamper','flag','note','createBy','sysOrgCode','isdel','status','jiaozhustatus','jingzhistatus','zhengyangstatus1','zhengyangstatus2','zhanglastatus','tiliangstatus','productiontime','taizuono','xuhao'))
      },
    }
  }
</script>