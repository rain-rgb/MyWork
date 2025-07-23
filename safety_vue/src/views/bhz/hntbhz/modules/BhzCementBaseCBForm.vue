<template>
  <a-spin :spinning="confirmLoading">
    <!--    子表单区域-->
    <div class="reason">浇筑部位：{{ model.poureLocation }}</div>
    <div class="reason">超标原因：{{ model.overReason }}</div>
    <a-tabs v-model="activeKey" @change="handleChangeTabs">
      <a-tab-pane tab="拌合站子表材料信息" :key="refKeys[0]" :forceRender="true">
        <j-editable-table
          :ref="refKeys[0]"
          :loading="bhzCementDetailTable.loading"
          :columns="bhzCementDetailTable.columns"
          :dataSource="bhzCementDetailTable.dataSource"
          :maxHeight="300"
          :disabled="formDisabled"
          :rowNumber="true"
          :rowSelection="true"
          :actionButton="false">
          <template v-slot:action="props">
            <a-tag color="green" v-if="props.value == '0'">合格</a-tag>
            <a-tag color="orange" v-if="props.value == '1'">初级超标</a-tag>
            <a-tag color="yellow" v-if="props.value == '2'">中级超标</a-tag>
            <a-tag color="red" v-if="props.value == '3'">高级超标</a-tag>
          </template>
          <template v-slot:materialeName="{ index,text }">
            <span :style="{color:(bhzCementDetailTable.dataSource[index].materialeOverLevel == '1'?'orange':bhzCementDetailTable.dataSource[index].materialeOverLevel == '2'?'yellow':bhzCementDetailTable.dataSource[index].materialeOverLevel == '3'?'red':'')}">{{ text }}</span>
          </template>
          <template v-slot:realityNumber="{ index,text }">
            <span :style="{color:(bhzCementDetailTable.dataSource[index].materialeOverLevel == '1'?'orange':bhzCementDetailTable.dataSource[index].materialeOverLevel == '2'?'yellow':bhzCementDetailTable.dataSource[index].materialeOverLevel == '3'?'red':'')}">{{ text }}</span>
          </template>
          <template v-slot:theoryNumber="{ index,text }">
            <span :style="{color:(bhzCementDetailTable.dataSource[index].materialeOverLevel == '1'?'orange':bhzCementDetailTable.dataSource[index].materialeOverLevel == '2'?'yellow':bhzCementDetailTable.dataSource[index].materialeOverLevel == '3'?'red':'')}">{{ text }}</span>
          </template>
          <template v-slot:errorValue="{ index,text }">
            <span :style="{color:(bhzCementDetailTable.dataSource[index].materialeOverLevel == '1'?'orange':bhzCementDetailTable.dataSource[index].materialeOverLevel == '2'?'yellow':bhzCementDetailTable.dataSource[index].materialeOverLevel == '3'?'red':'')}">{{ text }}</span>
          </template>
          <template v-slot:overValue="{ index,text }">
            <span :style="{color:(bhzCementDetailTable.dataSource[index].materialeOverLevel == '1'?'orange':bhzCementDetailTable.dataSource[index].materialeOverLevel == '2'?'yellow':bhzCementDetailTable.dataSource[index].materialeOverLevel == '3'?'red':'')}">{{ text }}%</span>
          </template>
        </j-editable-table>
      </a-tab-pane>
    </a-tabs>
      <!-- 主表单区域 -->
      <a-form :form="form">
        <a-card title="施工方处置信息" :bordered="false" :headStyle="{color:'#0785fd'}" :bodyStyle="{padding:'10'}" style="margin-top:10px">
          <j-form-container :disabled="formDisabled">
            <a-row>
              <a-col :span="12">
                <a-form-item label="问题原因" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-textarea
                    placeholder="无"
                    v-model="bhzCementOverHandler.problemReasons"
                    :auto-size="{ minRows: 5, maxRows: 20 }"
                  ></a-textarea>
                  <!-- <a-input v-model="bhzCementOverHandler.problemReasons" placeholder="无"></a-input> -->
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item label="处理方式" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-model="bhzCementOverHandler.handleWay" placeholder="无"></a-input>
                </a-form-item>
              </a-col>
            </a-row>
            <a-row>
              <a-col :span="12">
                <a-form-item label="处理结果" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-model="bhzCementOverHandler.handleResult" placeholder="无"></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item label="处理时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-model="bhzCementOverHandler.handleTime" placeholder="无"></a-input>
                </a-form-item>
              </a-col>
            </a-row>
            <a-row>
              <a-col :span="12">
                <a-form-item label="处置人" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-model="bhzCementOverHandler.handlePerson" placeholder="无"></a-input>
                </a-form-item>
              </a-col>
            </a-row>
          </j-form-container>
          <a-row>
            <a-col :span="24">
              <a-form-item label="处置附件" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
                <viewer :images="detailList">
                  <img
                    v-for="(item,index) in detailList"
                    :key="index"
                    style="height:100px;width: 100px;margin: 5px 10px 5px 10px;float: left"
                    :src="[item.icon] "
                    alt="">
                </viewer>
              </a-form-item>
            </a-col>
          </a-row>
        </a-card>
        <a-card v-show="this.model.overLevel >1" title="监理单位审核" :bordered="false" :headStyle="{color:'#0785fd'}" :bodyStyle="{padding:'10'}" style="margin-top:10px">
          <j-form-container :disabled="formDisabled">
            <a-row>
              <a-col :span="12">
                <a-form-item label="监理审核" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-if="bhzCementOverHandler.overproofStatus === 30 " v-model="bhzCementOverHandler.remark" placeholder="无"></a-input>
                  <a-input v-else v-model="bhzCementOverHandler.supervisorApproval" placeholder="无"></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item label="审核结果" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-if="bhzCementOverHandler.overproofStatus === 30 " placeholder="驳回"></a-input>
                  <a-input v-else v-model="bhzCementOverHandler.supervisorResult" placeholder="无"></a-input>
                </a-form-item>
              </a-col>
            </a-row>
            <a-row>
              <a-col :span="12">
                <a-form-item label="审核人" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-model="bhzCementOverHandler.approvalPerson" placeholder="无"></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item label="监理处理时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-model="bhzCementOverHandler.supervisorHandleTime" placeholder="无"></a-input>
                </a-form-item>
              </a-col>
            </a-row>
          </j-form-container>
          <a-row>
            <a-col :span="24">
              <a-form-item label="审核附件" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
                <viewer :images="detailList1">
                  <img
                    v-for="(item,index) in detailList1"
                    :key="index"
                    style="height:100px;width: 100px;margin: 5px 10px 5px 10px;float: left"
                    :src="[item.icon] "
                    alt="">
                </viewer>
              </a-form-item>
            </a-col>
          </a-row>
        </a-card>
        <a-card v-show="this.model.overLevel === 3" title="指挥部核查" :bordered="false" :headStyle="{color:'#0785fd'}" :bodyStyle="{padding:'10'}" style="margin-top:10px">
          <j-form-container :disabled="formDisabled">
            <a-row>
              <a-col :span="12">
                <a-form-item label="指挥部核查" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-if="bhzCementOverHandler.overproofStatus === 60 " v-model="bhzCementOverHandler.headquartersRemark" placeholder="无"></a-input>
                  <a-input v-else-if="bhzCementOverHandler.overproofStatus !== 20 &&bhzCementOverHandler.headquartersRemark !== null"  v-model="bhzCementOverHandler.headquartersRemark" placeholder="无"></a-input>
                  <a-input v-else v-model="bhzCementOverHandler.headquartersApproval" placeholder="无"></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item label="核查结果" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-if="bhzCementOverHandler.overproofStatus === 60 " placeholder="驳回"></a-input>
                  <a-input v-else-if="bhzCementOverHandler.overproofStatus !== 20 &&bhzCementOverHandler.headquartersRemark !== null " placeholder="驳回"></a-input>
                  <a-input v-else v-model="bhzCementOverHandler.headquartersResult" placeholder="无"></a-input>
                </a-form-item>
              </a-col>
            </a-row>
            <a-row>
              <a-col :span="12">
                <a-form-item label="核查人" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-model="bhzCementOverHandler.headquartersPerson" placeholder="无"></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item label="核查时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-model="bhzCementOverHandler.headquartersHandleTime" placeholder="无"></a-input>
                </a-form-item>
              </a-col>
            </a-row>
          </j-form-container>
        </a-card>
      </a-form>

  </a-spin>
</template>

<script>

import pick from 'lodash.pick'
import { getAction } from '@/api/manage'
import { FormTypes, getRefPromise } from '@/utils/JEditableTableUtil'
import { JEditableTableMixin } from '@/mixins/JEditableTableMixin'
import { validateDuplicateValue } from '@/utils/util'
import JFormContainer from '@/components/jeecg/JFormContainer'
import JDate from '@/components/jeecg/JDate'
import { initDictOptions, filterDictText } from '@/components/dict/JDictSelectUtil'

export default {
  name: 'BhzCementBaseForm',
  mixins: [JEditableTableMixin],
  components: {
    JFormContainer,
    JDate,
  },
  data() {
    return {
      detailList: [],
      detailList1:[],
      bhzCementOverHandler: [],
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
      // 新增时子表默认添加几行空数据
      addDefaultRowNum: 1,
      validatorRules: {
        batchNo: {
          rules: [
            { required: true, message: '请输入唯一ID!' },
          ]
        },
      },
      refKeys: ['bhzCementDetail',],
      tableKeys: ['bhzCementDetail',],
      activeKey: 'bhzCementDetail',
      // 拌合站子表材料信息
      bhzCementDetailTable: {
        loading: false,
        dataSource: [],
        columns: [
          {
            title: '材料名',
            key: 'materialeName',
            type: FormTypes.slot,
            slotName: 'materialeName',
            width: '200px',
            placeholder: '请输入${title}',
            defaultValue: '',
          },
          {
            title: '实际用量',
            key: 'realityNumber',
            type: FormTypes.slot,
            slotName: 'realityNumber',
            width: '200px',
            placeholder: '请输入${title}',
            defaultValue: '',
          },
          {
            title: '理论用量',
            key: 'theoryNumber',
            type: FormTypes.slot,
            slotName: 'theoryNumber',
            width: '200px',
            placeholder: '请输入${title}',
            defaultValue: '',
          },
          {
            title: '误差值',
            key: 'errorValue',
            type: FormTypes.slot,
            slotName: 'errorValue',
            width: '200px',
            placeholder: '请输入${title}',
            defaultValue: '',
          },
          {
            title: '超标值',
            key: 'overValue',
            type: FormTypes.slot,
            slotName: 'overValue',
            width: '200px',
            placeholder: '请输入${title}',
            defaultValue: '',
          },
          {
            title: '超标等级',
            type: FormTypes.slot,
            slotName: 'action',
            key: 'materialeOverLevel',
            width: '200px',
            placeholder: '请输入${title}',
            defaultValue: '',
          }

        ]
      },

      url: {
        add: '/hntbhz/bhzCementBase/add',
        edit: '/hntbhz/bhzCementBase/edit',
        queryById: '/hntbhz/bhzCementBase/queryById',
        bhzCementDetail: {
          list: '/hntbhz/bhzCementBase/queryBhzCementDetailByMainId'
        },
        list1: '/czsh/bhzCementOverHandler/list'
      }
    }
  },
  props: {
    //流程表单data
    formData: {
      type: Object,
      default: () => {
      },
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
    formDisabled() {
      if (this.formBpm === true) {
        if (this.formData.disabled === false) {
          return false
        }
        return true
      }
      return this.disabled
    },
    showFlowSubmitButton() {
      if (this.formBpm === true) {
        if (this.formData.disabled === false) {
          return true
        }
      }
      return false
    }
  },
  created() {
    //如果是流程中表单，则需要加载流程表单data
    this.showFlowData()
  },
  methods: {
    addBefore() {
      this.form.resetFields()
      this.bhzCementDetailTable.dataSource = []
    },
    getAllTable() {
      let values = this.tableKeys.map(key => getRefPromise(this, key))
      return Promise.all(values)
    },
    /** 调用完edit()方法之后会自动调用此方法 */
    editAfter() {
      // let fieldval = pick(this.model,'batchNo','shebeiNo_dictText','handlers','projectName','jobLocation','poureLocation','formulaNo','strengthRank','stirDatetime','saveDatetime','collectDatetime','estimateNumber','productDatetime','slump','overLevel','overLevel_dictText')
      this.handledata()
      // this.$nextTick(() => {
      //   this.form.setFieldsValue(fieldval)
      // })
      // 加载子表数据
      if (this.model.batchNo) {
        //定义处置审核的表信息
        // this.bhzCementOverHandler = this.model.bhzCementOverHandler;
        let params = { batchNo: this.model.batchNo }
        this.requestSubTableData(this.url.bhzCementDetail.list, params, this.bhzCementDetailTable)
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
    handledata: function () {
      this.detailList = []
      this.detailList1 = []
      this.bhzCementOverHandler = []
      let params = { baseid: this.model.batchNo }
      getAction(this.url.list1, params).then((res) => {
        if (res.success) {
          if (res.result.records.length > 0) {
            this.bhzCementOverHandler = res.result.records[0]
            if (this.bhzCementOverHandler.filePath2 !== null) {
              var filePath1 = this.bhzCementOverHandler.filePath2.split(',')
              filePath1.forEach(re => {
                this.detailList.push({ icon: re })
              })
              //console.log('filePath1', filePath1)
            }
            if (this.bhzCementOverHandler.filePath !== null){
              var filePath3 = this.bhzCementOverHandler.filePath.split(',')
              filePath3.forEach(re => {
                this.detailList1.push({ icon: re })
              })
            }
            //console.log("this.bhzCementOverHandler", res.result)
          } else {
            this.$message.warn('暂无超标处理信息！')
          }
        }
      })
    },
    popupCallback(row) {
      this.form.setFieldsValue(pick(row  ,'batchNo','shebeiNo','workNo','handlers','projectName','jobLocation','poureLocation','cementVariety','additiveVariety','formulaNo','strengthRank','stirDatetime','saveDatetime','clientNo','status','collectDatetime','estimateNumber','productDatetime','slump','overLevel','alertstate','formulaId','timeOverLevel','overLevel'))

    },

  }
}
</script>

<style scoped>
  .reason {

    color: rgb(239, 11, 11);
    line-height: 30px;
    text-align: center;
    font-size: 18px;
  }
</style>