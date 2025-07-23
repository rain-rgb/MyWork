<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="true">
      <!-- 主表单区域 -->
      <a-form :form="form" slot="detail">
        <a-row>

          <a-col :span="12" >
            <a-form-item label="设备名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-model=model.shebeiNo_dictText placeholder="请输入设备编号" ></a-input>
            </a-form-item>
          </a-col>

          <a-col :span="12" >
            <a-form-item label="工程名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-model=model.projectName placeholder="请输入工程名称" ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12" >
            <a-form-item label="浇筑部位" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['poureLocation']" placeholder="请输入浇筑部位" ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12" >
            <a-form-item label="配方号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['formulaNo']" placeholder="请输入配方号" ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12" >
            <a-form-item label="强度等级" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['strengthRank']" placeholder="请输入强度等级" ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12" >
            <a-form-item label="搅拌时长" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['stirDatetime']" placeholder="请输入搅拌时长" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="12" >
            <a-form-item label="方量" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['estimateNumber']" placeholder="请输入方量" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="12" >
            <a-form-item label="出料时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-date placeholder="请选择出料时间" v-decorator="['productDatetime']" :trigger-change="true" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="12" >
            <a-form-item label="采集时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-date placeholder="请选择采集时间" v-decorator="['collectDatetime']" :trigger-change="true" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="12" >
            <a-form-item label="坍落度" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['slump']" placeholder="请输入坍落度" ></a-input>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </j-form-container>
    <a-form-model ref="bhzCementOverHandler" :form="form" :model="bhzCementOverHandler">
    <a-card  v-if="model.renwudanstatus>10" title="施工方原因说明" :bordered="false" :headStyle="{color:'#0785fd'}" :bodyStyle="{padding:'10'}" style="margin-top:10px">
       <div >
        <a-row>
          <a-col :span="12">
            <a-form-item label="说明原因" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-textarea placeholder="无" v-model="bhzCementOverHandler.problemReasons"   :auto-size="{ minRows: 5, maxRows: 20 }" :disabled="model.renwudanstatus !== 30" ></a-textarea>
              <!-- <a-input v-model="bhzCementOverHandler.problemReasons" placeholder="无"></a-input> -->
            </a-form-item>

          </a-col>
          <a-col :span="12"  v-if="model.renwudanstatus == 28 || model.renwudanstatus==29">
            <a-form-item label="说明人" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-model="bhzCementOverHandler.handlePerson_dictText" placeholder="无" disabled="true"></a-input>
            </a-form-item>
            <a-form-item label="说明时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-model="bhzCementOverHandler.handleTime" placeholder="无" disabled="true"></a-input>
            </a-form-item>
          </a-col>
<!--          <a-col :span="12">-->
<!--            <a-form-model-item v-if="model.renwudanstatus == 30" label="上传图片" :labelCol="labelCol1" :wrapperCol="wrapperCol1">-->
<!--              <j-image-upload v-model="bhzCementOverHandler.filePath2"  :isMultiple=true></j-image-upload>-->
<!--            </a-form-model-item>-->
<!--            <a-form-item v-if="model.renwudanstatus == 28 || model.renwudanstatus==29" label="填报附件" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <viewer :images="detailList">-->
<!--                <img-->
<!--                  v-for="(img,index) in detailList"-->
<!--                  :key="index"-->
<!--                  style="height:100px;width: 100px;margin: 5px 10px 5px 10px;float: left"-->
<!--                  :src="img"-->
<!--                  alt="">-->
<!--              </viewer>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
        </a-row>
<!--        <a-row v-if="model.renwudanstatus == 28 || model.renwudanstatus==29">-->
<!--          <a-col :span="12">-->
<!--            <a-form-item label="说明人" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-model="bhzCementOverHandler.handlePerson" placeholder="无" disabled="true"></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="12">-->
<!--            <a-form-item label="说明时间" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-model="bhzCementOverHandler.handleTime" placeholder="无" disabled="true"></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--        </a-row>-->

      </div>
    </a-card>
    <a-card  v-if="  [28,29,27].includes(model.renwudanstatus)" title="监理审核" :bordered="false" :headStyle="{color:'#0785fd'}" :bodyStyle="{padding:'10'}" style="margin-top:10px">
    <div >
        <a-row>
          <a-col :span="12">
            <a-form-item label="审核意见" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-textarea placeholder="无" v-model="bhzCementOverHandler.remark"   :auto-size="{ minRows: 5, maxRows: 20 }" :disabled="model.renwudanstatus !== 28"  ></a-textarea>
              <!-- <a-input v-model="bhzCementOverHandler.problemReasons" placeholder="无"></a-input> -->
            </a-form-item>
          </a-col>
          <a-col :span="12" v-if=" [29,27].includes(model.renwudanstatus)">
            <a-form-item label="审核人" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-model="bhzCementOverHandler.approvalPerson_dictText" placeholder="无"disabled ></a-input>
            </a-form-item>

            <a-form-item label="审核时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input   v-model="bhzCementOverHandler.supervisorHandleTime"  placeholder="无"  disabled  ></a-input>
            </a-form-item>
          </a-col>
        </a-row>

    </div>
    </a-card>

    </a-form-model>


      <!-- 子表单区域 -->
<!--    <a-tabs v-model="activeKey" @change="handleChangeTabs">-->
<!--      <a-tab-pane tab="拌合站子表材料信息" :key="refKeys[0]" :forceRender="true">-->
<!--        <j-editable-table-->
<!--          :ref="refKeys[0]"-->
<!--          :loading="bhzCementDetailTable.loading"-->
<!--          :columns="bhzCementDetailTable.columns"-->
<!--          :dataSource="bhzCementDetailTable.dataSource"-->
<!--          :disabled="formDisabled"-->
<!--          :rowNumber="true"-->
<!--          :rowSelection="false"-->
<!--          :actionButton="false"-->
<!--          class="jtable">-->

<!--          <span  slot="materialeName" slot-scope="text, record">-->

<!--            <text color="green" v-if = "record.materialeOverLevel == '0'"> {{text}} </text>-->
<!--            <text color="orange" v-else-if = "record.materialeOverLevel == '1'"> {{text}} </text>-->
<!--            <text color="yellow" v-else-if = "record.materialeOverLevel == '2'"> {{text}} </text>-->
<!--            <text color="red" v-else-if = "record.materialeOverLevel == '3'"> {{text}} </text>-->

<!--         </span>-->
<!--          -->

<!--          <template v-slot:action="props">-->
<!--            <a-tag color="green" v-if="props.value == '0'">合格</a-tag>-->
<!--            <a-tag color="orange" v-if="props.value == '1'">初级超标</a-tag>-->
<!--            <a-tag color="yellow" v-if="props.value == '2'">中级超标</a-tag>-->
<!--            <a-tag color="red" v-if="props.value == '3'">高级超标</a-tag>-->
<!--          </template>-->
<!--        </j-editable-table>-->
<!--      </a-tab-pane>-->
<!--    </a-tabs>-->
  </a-spin>
</template>

<script>

  import pick from 'lodash.pick'
  import { getAction,postAction } from '@/api/manage'
  import { FormTypes,getRefPromise } from '@/utils/JEditableTableUtil'
  import { JEditableTableMixin } from '@/mixins/JEditableTableMixin'
  import { validateDuplicateValue } from '@/utils/util'
  import JFormContainer from '@/components/jeecg/JFormContainer'
  import JDate from '@/components/jeecg/JDate'
  import {initDictOptions, filterDictText} from '@/components/dict/JDictSelectUtil'
  export default {
    name: 'BhzCementBaseForm',
    mixins: [JEditableTableMixin],
    components: {
      JFormContainer,
      JDate,
    },
    data() {
      return {
        labelCol1: {
          xs: { span: 24 },
          sm: { span: 3 },
        },
        wrapperCol1: {
          xs: { span: 24 },
          sm: { span: 20 },
        },
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
        detailList: [],
        bhzCementOverHandler:{},
        bhz: 0,
        // 拌合站子表材料信息
        url: {
          add: '/czsh/bhzCementOverHandler/addOrUpdate',
          list: '/czsh/bhzCementOverHandler/list'

          // bhzCementDetail: {
          //   // list: '/hntbhz/bhzCementBase/queryBhzCementDetailByMainId'
          //   list: '/hntbhz/bhzCementBase/cementDetailAndStockBin'
          // },
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
      handleOk() {
        this.confirmLoading = true
        this.subcommit()
      },
      subcommit() {
        this.$refs.bhzCementOverHandler.validate((valid) => {
          if (valid) {
            this.bhzCementOverHandler.baseid = this.model.id
            this.bhzCementOverHandler.shebeiNo = this.model.batchNo
            if(this.model.renwudanstatus == 30 ){
              this.bhzCementOverHandler.overproofStatus = 28
            }else if(this.model.renwudanstatus == 28){
              this.bhzCementOverHandler.overproofStatus = 29
            }else{
              this.bhzCementOverHandler.overproofStatus =this.model.renwudanstatus
            }

            postAction(this.url.add, this.bhzCementOverHandler).then((res) => {
              if (res.success) {
                this.$message.success('请求成功')
              } else {
                this.$message.error('请求失败')
              }
              this.confirmLoading = false
              this.visible = false
              this.$emit('change', true)
            })
             this.routeReload();
          } else {
            this.confirmLoading = false
            this.$message.error('请填写必填项');
            return false
          }
        })
      },
      routeReload() {//刷新页面
        this.reloadFlag = false
        let ToggleMultipage = 'ToggleMultipage'
        this.$store.dispatch(ToggleMultipage, false)
        this.$nextTick(() => {
          this.$store.dispatch(ToggleMultipage, true)
          this.reloadFlag = true
        })
        console.log("刷新页面")
      },
      handleData() {
        this.detailList = []
        this.bhzCementOverHandler = {}
        let params = { baseid: this.model.id }
        getAction(this.url.list, params).then((res) => {
          if (res.success) {
            if (res.result.records.length > 0) {
              this.bhzCementOverHandler = res.result.records[0]
              this.detailList = this.bhzCementOverHandler.filePath2 ? this.bhzCementOverHandler.filePath2.split(',') : []
            } else {
              this.$message.warn('暂无未使用浇筑令填报审批信息！')
            }
          }
        })
      },
      addBefore(){
        this.form.resetFields()
        this.bhzCementDetailTable.dataSource=[]
      },
      getAllTable() {
        let values = this.tableKeys.map(key => getRefPromise(this, key))
        return Promise.all(values)
      },
      /** 调用完edit()方法之后会自动调用此方法 */
      editAfter() {
        let fieldval = pick(this.model,'batchNo','shebeiNo','shebeiNo_dictText','workNo','handlers','projectName','jobLocation','poureLocation','cementVariety','additiveVariety','formulaNo','strengthRank','stirDatetime','saveDatetime','clientNo','status','collectDatetime','estimateNumber','productDatetime','slump','overLevel','alertstate','formulaId','timeOverLevel')
        //,'batchNo','shebeiNo_dictText','handlers','projectName','jobLocation','poureLocation','formulaNo','strengthRank','stirDatetime','saveDatetime','collectDatetime','estimateNumber','productDatetime','slump','overLevel_dictText'
        this.$nextTick(() => {
          this.form.setFieldsValue(fieldval)
        })
        // 加载子表数据
        if (this.model.batchNo) {
          this.handleData()
        }
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
      handleCancel() {
        this.visible = false
        this.$emit('change', false)
      },
     popupCallback(row){
       this.form.setFieldsValue(pick(row),'batchNo','shebeiNo','shebeiNo_dictText','workNo','handlers','projectName','jobLocation','poureLocation','cementVariety','additiveVariety','formulaNo','strengthRank','stirDatetime','saveDatetime','clientNo','status','collectDatetime','estimateNumber','productDatetime','slump','overLevel','alertstate','formulaId','timeOverLevel')

     },

    }
  }
</script>

<style scoped>
.jtable{
  text-align: center;
}
</style>