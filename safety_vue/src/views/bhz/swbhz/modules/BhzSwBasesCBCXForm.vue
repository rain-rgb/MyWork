<template>
  <a-spin :spinning="confirmLoading">
    <div v-if="model.chaobiaodengji>0" class="reason">超标原因：{{ model.overReason }}</div>
    <!--    子表单区域-->
    <a-tabs v-model="activeKey" @change="handleChangeTabs">
      <a-tab-pane tab="拌合站子表材料信息" :key="refKeys[0]" :forceRender="true">
        <j-editable-table
          :ref="refKeys[0]"
          :loading="bhzSwCailiaoTable.loading"
          :columns="bhzSwCailiaoTable.columns"
          :dataSource="bhzSwCailiaoTable.dataSource"
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
          <template v-slot:shijiyongliang="props">
            <span>{{ parseFloat(props.text).toFixed(2) }}</span>
          </template>
          <template v-slot:lilunpb="props">
            <span>{{ parseFloat(props.text).toFixed(2) }}</span>
          </template>
          <template v-slot:chaobiao="props">
            <span>{{ parseFloat(props.text).toFixed(2) }}</span>
          </template>
          <template v-slot:wucha="props">
            <span>{{ parseFloat(props.text).toFixed(2) }}</span>
          </template>
        </j-editable-table>
      </a-tab-pane>
    </a-tabs>
    <j-form-container :disabled="formDisabled">
      <!-- 主表单区域 -->
      <a-form :form="form" slot="detail">
        <a-card title="处理信息" :bordered="false" :headStyle="{color:'#0785fd'}" :bodyStyle="{padding:'10'}" style="margin-top:10px">
          <a-divider type="horizontal" ><span style="color:#0785fd">施工处置</span></a-divider>
          <a-row>
            <a-col :span="12">
              <a-form-item label="问题原因" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-model="bhzCementOverHandler.problemReasons" placeholder="无"></a-input>
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
            <a-col :span="12">
              <a-form-item label="附件" :labelCol="labelCol" :wrapperCol="wrapperCol">
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

          <a-divider type="horizontal" ><span style="color:#0785fd">监理审核</span></a-divider>
          <a-row v-if="bhzCementOverHandler.remark">
            <a-col :span="12">
              <a-form-item label="监理驳回原因" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-model="bhzCementOverHandler.remark" placeholder="无"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item label="监理审批" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-model="bhzCementOverHandler.supervisorApproval" placeholder="无"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="监理结果" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-model="bhzCementOverHandler.supervisorResult" placeholder="无"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item label="监理处理时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-model="bhzCementOverHandler.supervisorHandleTime" placeholder="无"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="审核人" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-model="bhzCementOverHandler.approvalPerson" placeholder="无"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-divider type="horizontal" ><span style="color:#0785fd">指挥部审批</span></a-divider>
          <a-row v-if="bhzCementOverHandler.headquartersApproval && bhzCementOverHandler.overproofStatus == '60' ">
            <a-col :span="12">
              <a-form-item label="指挥部驳回原因" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-model="bhzCementOverHandler.headquartersApproval" placeholder="无"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item label="指挥部审批" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-model="bhzCementOverHandler.headquartersApproval" placeholder="无"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item label="审批时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-model="bhzCementOverHandler.headquartersHandleTime" placeholder="无"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="审批人" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-model="bhzCementOverHandler.headquartersPerson_dictText" placeholder="无"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
        </a-card>
      </a-form>
    </j-form-container>

<!--    <a-row>-->
<!--      <a-col :span="24">-->
<!--        <a-form-item label="审核附件" :labelCol="labelCol1" :wrapperCol="wrapperCol1">-->
<!--          <viewer :images="detailList1">-->
<!--            <img-->
<!--              v-for="(item,index) in detailList1"-->
<!--              :key="index"-->
<!--              style="height:100px;width: 100px;margin: 5px 10px 5px 10px;float: left"-->
<!--              :src="[item.icon] "-->
<!--              alt="">-->
<!--          </viewer>-->
<!--        </a-form-item>-->
<!--      </a-col>-->
<!--    </a-row>-->
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
  import JDictSelectTag from "@/components/dict/JDictSelectTag"

  export default {
    name: 'BhzSwBasesCBCXForm',
    mixins: [JEditableTableMixin],
    components: {
      JFormContainer,
      JDate,
      JDictSelectTag,
    },
    data() {
      return {
        detailList: [],
        detailList1:[],
        bhzCementOverHandler:[],
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
        },
        refKeys: ['bhzSwCailiao', ],
        tableKeys:['bhzSwCailiao', ],
        activeKey: 'bhzSwCailiao',
        // 水稳材料表信息
        bhzSwCailiaoTable: {
          loading: false,
          dataSource: [],
          columns: [
            {
              title: '材料名',
              key: 'cailiaoming',
              type: FormTypes.normal,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '实际用量',
              key: 'shijiyongliang',
              type: FormTypes.slot,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            // {
            //   title: '理论用量',
            //   key: 'theoryNumber',
            //   type: FormTypes.normal,
            //   width:"200px",
            //   placeholder: '请输入${title}',
            //   defaultValue:'',
            // },
            {
              title: '实际配比(%)',
              key: 'shijipb',
              type: FormTypes.normal,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '理论配比(%)',
              key: 'lilunpb',
              type: FormTypes.slot,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '误差值',
              key: 'wucha',
              type: FormTypes.slot,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            // {
            //   title: '超标值',
            //   key: 'chaobiao',
            //   type: FormTypes.normal,
            //   width:"200px",
            //   placeholder: '请输入${title}',
            //   defaultValue:'',
            // },
            {
              title: '超标等级',
              type:FormTypes.slot,
              slotName:'action',
              key: 'chaobiaodengji',
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            }
          ]
        },
        url: {
          add: "/swbhz/bhzSwBases/add",
          edit: "/swbhz/bhzSwBases/edit",
          queryById: "/swbhz/bhzSwBases/queryById",
          bhzSwCailiao: {
            list: '/swbhz/bhzSwBases/queryBhzSwCailiaoByMainId'
          },
          list1: '/czsh/bhzCementOverHandler/list'
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
        this.bhzSwCailiaoTable.dataSource=[]
      },
      getAllTable() {
        let values = this.tableKeys.map(key => getRefPromise(this, key))
        return Promise.all(values)
      },
      /** 调用完edit()方法之后会自动调用此方法 */
      editAfter() {
        let fieldval = pick(this.model)
        this.handledata();
        //,'chuliaoshijian','zongchanliang','leijichanliang','shebeibianhao_dictText','baocunshijian','caijishijian','chaobiaodengji_dictText','poureLocation','jobLocation','guid','strengthRank','projectName'
        this.$nextTick(() => {
          this.form.setFieldsValue(fieldval)
        })
        // 加载子表数据
        if (this.model.guid) {
          console.log(this.model.guid)
          //定义处置审核的表信息
          // this.bhzCementOverHandler = this.model.bhzCementOverHandler;
          let params = { baseGuid: this.model.guid }
          this.requestSubTableData(this.url.bhzSwCailiao.list, params, this.bhzSwCailiaoTable)
        }

      },
      /** 整理成formData */
      classifyIntoFormData(allValues) {
        let main = Object.assign(this.model, allValues.formValue)
        return {
          ...main, // 展开
          bhzSwCailiaoList: allValues.tablesValue[0].values,
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
      handledata:function (){
        this.bhzCementOverHandler = []
        let params = {baseid:this.model.guid}
        getAction(this.url.list1,params).then((res) => {
          if (res.success) {
            if(res.result.records.length>0) {
              this.bhzCementOverHandler = res.result.records[0]
              //console.log("this.bhzCementOverHandler", res.result)
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
            }else {
              this.$message.warn("暂无超标处理信息！")
            }
          }
        })
      },
     popupCallback(row){
       this.form.setFieldsValue(pick(row))
     //,'chuliaoshijian','zongchanliang','leijichanliang','shebeibianhao_dictText','baocunshijian','caijishijian','chaobiaodengji_dictText','poureLocation','jobLocation','guid','strengthRank','projectName'
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