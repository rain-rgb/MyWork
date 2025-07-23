<template>
  <a-spin :spinning="confirmLoading">
<!--    <j-form-container :disabled="formDisabled">-->
<!--      &lt;!&ndash; 主表单区域 &ndash;&gt;-->
<!--      <a-form :form="form" slot="detail">-->
<!--        <a-row>-->
<!--          <a-col :span="24" >-->
<!--            <a-form-item label="唯一ID" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['batchNo', validatorRules.batchNo]" placeholder="请输入唯一ID" ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24" >-->
<!--            <a-form-item label="设备名称" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['shebeiNo_dictText']" placeholder="请输入设备编号" ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
          <!--          <a-col :span="24" >-->
          <!--            <a-form-item label="工单号" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <a-input v-decorator="['workNo']" placeholder="请输入工单号" ></a-input>-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
<!--          <a-col :span="24" >-->
<!--            <a-form-item label="操作者" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['handlers']" placeholder="请输入操作者" ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24" >-->
<!--            <a-form-item label="工程名称" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['projectName']" placeholder="请输入工程名称" ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24" >-->
<!--            <a-form-item label="施工地点" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['jobLocation']" placeholder="请输入施工地点" ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24" >-->
<!--            <a-form-item label="浇筑部位" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['poureLocation']" placeholder="请输入浇筑部位" ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
          <!--          <a-col :span="24" >-->
          <!--            <a-form-item label="水泥品种" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <a-input v-decorator="['cementVariety']" placeholder="请输入水泥品种" ></a-input>-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <!--          <a-col :span="24" >-->
          <!--            <a-form-item label="外加剂品种" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <a-input v-decorator="['additiveVariety']" placeholder="请输入外加剂品种" ></a-input>-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
<!--          <a-col :span="24" >-->
<!--            <a-form-item label="配方号" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['formulaNo']" placeholder="请输入配方号" ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24" >-->
<!--            <a-form-item label="强度等级" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['strengthRank']" placeholder="请输入强度等级" ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24" >-->
<!--            <a-form-item label="搅拌时间" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input-number v-decorator="['stirDatetime']" placeholder="请输入搅拌时间" style="width: 100%" />-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24" >-->
<!--            <a-form-item label="保存时间" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <j-date placeholder="请选择保存时间" v-decorator="['saveDatetime']" :trigger-change="true" style="width: 100%" />-->
<!--            </a-form-item>-->
<!--          </a-col>-->
          <!--          <a-col :span="24" >-->
          <!--            <a-form-item label="客户端编号" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <a-input v-decorator="['clientNo']" placeholder="请输入客户端编号" ></a-input>-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <!--          <a-col :span="24" >-->
          <!--            <a-form-item label="状态" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <a-input-number v-decorator="['status']" placeholder="请输入状态" style="width: 100%" />-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
<!--          <a-col :span="24" >-->
<!--            <a-form-item label="采集时间" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <j-date placeholder="请选择采集时间" v-decorator="['collectDatetime']" :trigger-change="true" style="width: 100%" />-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24" >-->
<!--            <a-form-item label="方量" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input-number v-decorator="['estimateNumber']" placeholder="请输入方量" style="width: 100%" />-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24" >-->
<!--            <a-form-item label="出料时间" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <j-date placeholder="请选择出料时间" v-decorator="['productDatetime']" :trigger-change="true" style="width: 100%" />-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24" >-->
<!--            <a-form-item label="坍落度" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['slump']" placeholder="请输入坍落度" ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24" >-->
<!--            <a-form-item label="超标等级" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input-number v-decorator="['overLevel_dictText']" placeholder="请输入超标等级" style="width: 100%" />-->
<!--            </a-form-item>-->
<!--          </a-col>-->
          <!--          <a-col :span="24" >-->
          <!--            <a-form-item label="是否超标" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <a-input-number v-decorator="['alertstate']" placeholder="请输入是否超标" style="width: 100%" />-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <!--          <a-col :span="24" >-->
          <!--            <a-form-item label="配方uuid(车结束符)" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <a-input v-decorator="['formulaId']" placeholder="请输入配方uuid(车结束符)" ></a-input>-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <!--          <a-col :span="24" >-->
          <!--            <a-form-item label="搅拌时间超标等级" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <a-input-number v-decorator="['timeOverLevel']" placeholder="请输入搅拌时间超标等级" style="width: 100%" />-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
<!--        </a-row>-->
<!--      </a-form>-->
<!--    </j-form-container>-->
      <!-- 子表单区域 -->
<!--    <a-tabs v-model="activeKey" @change="handleChangeTabs">-->
<!--      <a-tab-pane tab="拌合站子表材料信息" :key="refKeys[0]" :forceRender="true">-->
        <j-editable-table
          :ref="refKeys[0]"
          :loading="bhzCementDetailTable.loading"
          :columns="bhzCementDetailTable.columns"
          :dataSource="bhzCementDetailTable.dataSource"
          :disabled="formDisabled"
          :rowNumber="true"
          :rowSelection="false"
          :actionButton="false">
          <template v-slot:action="props">
            <a-tag color="green" v-if="props.value == '0'">合格</a-tag>
            <a-tag color="orange" v-if="props.value == '1'">初级超标</a-tag>
            <a-tag color="yellow" v-if="props.value == '2'">中级超标</a-tag>
            <a-tag color="red" v-if="props.value == '3'">高级超标</a-tag>
          </template>
        </j-editable-table>
<!--      </a-tab-pane>-->
<!--    </a-tabs>-->
  </a-spin>
</template>

<script>

  import pick from 'lodash.pick'
  import { getAction } from '@/api/manage'
  import { FormTypes,getRefPromise } from '@/utils/JEditableTableUtil'
  import { JEditableTableMixin } from '@/mixins/JEditableTableMixin'
  import { validateDuplicateValue } from '@/utils/util'
  import JFormContainer from '@/components/jeecg/JFormContainer'
  import JDate from '@/components/jeecg/JDate'
  import {initDictOptions, filterDictText} from '@/components/dict/JDictSelectUtil'
  export default {
    name: 'BhzCailiaoxhForm',
    mixins: [JEditableTableMixin],
    components: {
      JFormContainer,
      JDate,
    },
    data() {
      return {
        labelCol: {
          xs: { span: 24 },
          sm: { span: 6 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        labelCol2: {
          xs: { span: 24 },
          sm: { span: 3 },
        },
        wrapperCol2: {
          xs: { span: 24 },
          sm: { span: 20 },
        },
        // 新增时子表默认添加几行空数据
        addDefaultRowNum: 1,
        validatorRules: {
          batchNo: {
            rules: [
              { required: true, message: '请输入唯一ID!'},
            ]
          },
        },
        refKeys: ['bhzCementDetail', ],
        tableKeys:['bhzCementDetail', ],
        activeKey: 'bhzCementDetail',
        // 拌合站子表材料信息
        bhzCementDetailTable: {
          loading: false,
          dataSource: [],
          columns: [

            {
              title: '材料名',
              key: 'materialeName',
              type: FormTypes.normal,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '实际用量',
              key: 'realityNumber',
              type: FormTypes.normal,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '理论用量',
              key: 'theoryNumber',
              type: FormTypes.normal,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '初级超标次数',
              key: 'primaryNumber',
              type: FormTypes.normal,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '中级超标次数',
              key: 'middleNumber',
              type: FormTypes.normal,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '高级超标次数',
              slotName:'action',
              key: 'advancedNumber',
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            }

          ]
        },
        url: {
          add: "/hntbhz/bhzCementBase/add",
          edit: "/hntbhz/bhzCementBase/edit",
          queryById: "/hntbhz/bhzCementBase/queryById",
          bhzCementDetail: {
            list: '/TbhzcailiaoStatistics/bhzCementDetailStatistics/list'
          },
        }
      }
    },
    props: {
      //流程表单data
      formData: {
        type: Object,
        default: ()=>{},
        required: false
      },
      //表单模式：false流程表单 true普通表单
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
      addBefore(){
        this.form.resetFields()
        // this.bhzCementDetailTable.dataSource=[]
      },
      getAllTable() {
        let values = this.tableKeys.map(key => getRefPromise(this, key))
        return Promise.all(values)
      },
      /** 调用完edit()方法之后会自动调用此方法 */
      editAfter() {
        let fieldval = pick(this.model)
        //,'batchNo','shebeiNo_dictText','handlers','projectName','jobLocation','poureLocation','formulaNo','strengthRank','stirDatetime','saveDatetime','collectDatetime','estimateNumber','productDatetime','slump','overLevel_dictText'
        this.$nextTick(() => {
          this.form.setFieldsValue(fieldval)
          this.getData();
        })
        // 加载子表数据
        // if (this.model.batchNo) {
        //   let params = { batchNo: this.model.batchNo }
        //   this.requestSubTableData(this.url.bhzCementDetail.list, params, this.bhzCementDetailTable)
        // }
      },

      /** 整理成formData */
      classifyIntoFormData(allValues) {
        let main = Object.assign(this.model, allValues.formValue)
        return {
          ...main, // 展开
          bhzCementDetailList: allValues.tablesValue[0].values,
        }
      },
      //渲染流程表单数据
      showFlowData(){
        if(this.formBpm === true){
          let params = {id:this.formData.dataId};
          getAction(this.url.queryById,params).then((res)=>{
            if(res.success){
              this.edit (res.result);
            }
          })
        }
      },
      validateError(msg){
        this.$message.error(msg)
      },
      getData:function (){
        this.bhzCementDetailTable.dataSource=[]
        let params = {csId:this.model.id,pageSize:30};
        getAction(this.url.bhzCementDetail.list, params,).then((res)=>{
          //console.log(res)
          if(res.success)  {
            this.bhzCementDetailTable.dataSource = res.result.records;
          }
        });
      },
     popupCallback(row){
       this.form.setFieldsValue(pick(row))
     //,'batchNo','shebeiNo','workNo','handlers','projectName','jobLocation','poureLocation','cementVariety','additiveVariety','formulaNo','strengthRank','stirDatetime','saveDatetime','clientNo','status','collectDatetime','estimateNumber','productDatetime','slump','overLevel','alertstate','formulaId','timeOverLevel'
     },

    }
  }
</script>

<style scoped>
</style>