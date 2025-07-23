<template>
  <a-spin :spinning="confirmLoading">
    <div v-if="model.chaobiaodengji>0" class="reason">超标原因：{{ model.overReason }}</div>
<!--    <j-form-container :disabled="formDisabled">-->
<!--      &lt;!&ndash; 主表单区域 &ndash;&gt;-->
<!--      <a-form :form="form" slot="detail">-->
<!--        <a-row>-->
<!--          <a-col :span="24" >-->
<!--            <a-form-item label="出料时间" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['chuliaoshijian']" placeholder="请输入出料时间" ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24" >-->
<!--            <a-form-item label="总产量" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input-number v-decorator="['zongchanliang']" placeholder="请输入总产量" style="width: 100%" />-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24" >-->
<!--            <a-form-item label="累计产量" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input-number v-decorator="['leijichanliang']" placeholder="请输入累计产量" style="width: 100%" />-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24" >-->
<!--            <a-form-item label="设备名称" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['shebeibianhao_dictText']" placeholder="请输入设备名称" ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24" >-->
<!--            <a-form-item label="客户端编号" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['clientNo']" placeholder="请输入客户端编号" ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24" >-->
<!--            <a-form-item label="保存时间" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['baocunshijian']" placeholder="请输入保存时间" ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24" >-->
<!--            <a-form-item label="采集时间" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <j-date placeholder="请选择采集时间" v-decorator="['caijishijian']" :trigger-change="true" style="width: 100%" />-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24" >-->
<!--            <a-form-item label="拌合时间" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input-number v-decorator="['banheshijian']" placeholder="请输入拌合时间" style="width: 100%" />-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24" >-->
<!--            <a-form-item label="是否删除" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['isdel']" placeholder="请输入是否删除" ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24" >-->
<!--            <a-form-item label="超标等级" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['chaobiaodengji_dictText']" placeholder="请输入超标等级" ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24" >-->
<!--            <a-form-item label="施工部位" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['poureLocation']" placeholder="无数据" ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24" >-->
<!--            <a-form-item label="施工地点" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['jobLocation']" placeholder="无数据" ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24" >-->
<!--            <a-form-item label="时间戳" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['ts']" placeholder="请输入时间戳" ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24" >-->
<!--            <a-form-item label="唯一标识" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['guid']" placeholder="请输入唯一标识" ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24" >-->
<!--            <a-form-item label="强度等级" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['strengthRank']" placeholder="无数据" ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24" >-->
<!--            <a-form-item label="工程名称" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['projectName']" placeholder="无数据" ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24" >-->
<!--            <a-form-item label="是否统计" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input-number v-decorator="['istongji']" placeholder="请输入是否统计" style="width: 100%" />-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24" >-->
<!--            <a-form-item label="时间超标" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input-number v-decorator="['timechaobiao']" placeholder="请输入时间超标" style="width: 100%" />-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24" >-->
<!--            <a-form-item label="材料超标" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input-number v-decorator="['cailiaochaobiao']" placeholder="请输入材料超标" style="width: 100%" />-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24" >-->
<!--            <a-form-item label="是否判断超标" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input-number v-decorator="['alertstate']" placeholder="请输入是否判断超标" style="width: 100%" />-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24" >-->
<!--            <a-form-item label="配方号" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['formulaNo']" placeholder="请输入配方号" ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24" >-->
<!--            <a-form-item label="备用1" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['beiy1']" placeholder="请输入备用1" ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24" >-->
<!--            <a-form-item label="备用2" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['beiy2']" placeholder="请输入备用2" ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24" >-->
<!--            <a-form-item label="备用3" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['beiy3']" placeholder="请输入备用3" ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24" >-->
<!--            <a-form-item label="所属组织机构" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['sysOrgCode_dictText']" placeholder="请输入所属组织机构" ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--        </a-row>-->
<!--      </a-form>-->
<!--    </j-form-container>-->
      <!-- 子表单区域 -->
<!--    <a-tabs v-model="activeKey" @change="handleChangeTabs">-->
<!--      <a-tab-pane tab="水稳材料表信息" :key="refKeys[0]" :forceRender="true">-->
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
  import JDictSelectTag from "@/components/dict/JDictSelectTag"

  export default {
    name: 'BhzSwBasesForm',
    mixins: [JEditableTableMixin],
    components: {
      JFormContainer,
      JDate,
      JDictSelectTag,
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
              width:"170px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '实际用量',
              key: 'shijiyongliang',
              type: FormTypes.slot,
              width:"170px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '实际配比（%）',
              key: 'shijipb',
              type: FormTypes.normal,
              width:"170px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '理论配比（%）',
              key: 'lilunpb',
              type: FormTypes.normal,
              width:"170px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '误差值（%）',
              key: 'wucha',
              type: FormTypes.slot,
              slotName:'wucha',
              width:"170px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '超标值',
              key: 'chaobiao',
              type: FormTypes.slot,
              width:"170px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '超标等级',
              type:FormTypes.slot,
              slotName:'action',
              key: 'chaobiaodengji',
              width:"170px",
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
        //,'chuliaoshijian','zongchanliang','leijichanliang','shebeibianhao_dictText','baocunshijian','caijishijian','chaobiaodengji_dictText','poureLocation','jobLocation','guid','strengthRank','projectName'
        this.$nextTick(() => {
          this.form.setFieldsValue(fieldval)
        })
        // 加载子表数据
        if (this.model.guid) {
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