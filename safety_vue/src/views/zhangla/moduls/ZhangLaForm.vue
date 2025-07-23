<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <!-- 主表单区域 -->
      <a-form :form="form" slot="detail">
        <a-row>
          <a-col :span="12" >
            <a-form-item label="设备编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['deviceno_dictText']" placeholder="无" ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12" >
            <a-form-item label="梁型" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['beamtype']" placeholder="无" style="width: 100%" />
            </a-form-item>
          </a-col>
          </a-row>
            <a-row>
          <a-col :span="12" >
            <a-form-item label="砼设计强度" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['concretestrength']" placeholder="无" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="12" >
            <a-form-item label="弹性模量" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['modulusofelasticity']" placeholder="无" ></a-input>
            </a-form-item>
          </a-col>
            </a-row>
        <a-row>
          <a-col :span="12" >
            <a-form-item label="钢束编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['steelbeamno']" placeholder="无" ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12" >
            <a-form-item label="钢绞线根数" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-date placeholder="无" v-decorator="['steelstrand']" :trigger-change="true" style="width: 100%" />
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12" >
            <a-form-item label="张拉断面" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['tensioningsection']" placeholder="无" ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12" >
            <a-form-item label="张拉时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['tensioningdate']" placeholder="无" ></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12" >
            <a-form-item label="张拉力0" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['tensioningforce0']" placeholder="无" ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12" >
            <a-form-item label="张拉力1" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['tensioningforce1']" placeholder="无" ></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12" >
            <a-form-item label="张拉力2" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['tensioningforce2']" placeholder="无" ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12" >
            <a-form-item label="张拉力3" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['tensioningforce3']" placeholder="无" ></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12" >
            <a-form-item label="张拉力4" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['tensioningforce4']" placeholder="无" ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12" >
            <a-form-item label="张拉力0" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['tensioningsection0']" placeholder="无" ></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12" >
            <a-form-item label="张拉力1" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['tensioningsection1']" placeholder="无" ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12" >
            <a-form-item label="张拉力2" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['tensioningsection2']" placeholder="无" ></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12" >
            <a-form-item label="张拉力3" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['tensioningsection3']" placeholder="无" ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12" >
            <a-form-item label="张拉力4" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['tensioningsection4']" placeholder="无" ></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12" >
            <a-form-item label="伸长量0" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['elongation0']" placeholder="无" ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12" >
            <a-form-item label="伸长量1" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['elongation1']" placeholder="无" ></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12" >
            <a-form-item label="伸长量2" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['elongation2']" placeholder="无" ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12" >
            <a-form-item label="伸长量3" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['elongation3']" placeholder="无" ></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12" >
            <a-form-item label="伸长量4" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['elongation4']" placeholder="无" ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12" >
            <a-form-item label="油压0" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['oilpressure0']" placeholder="无" ></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12" >
            <a-form-item label="油压1" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['oilpressure1']" placeholder="无" ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12" >
            <a-form-item label="油压2" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['oilpressure2']" placeholder="无" ></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12" >
            <a-form-item label="油压3" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['oilpressure3']" placeholder="无" ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12" >
            <a-form-item label="油压4" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['oilpressure4']" placeholder="无" ></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12" >
            <a-form-item label="设计张拉控制力" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['tensioncontrol']" placeholder="无" ></a-input>
            </a-form-item>
          </a-col>

          <a-col :span="12" >
            <a-form-item label="总伸长量" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['totalelongation']" placeholder="无" ></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12" >
            <a-form-item label="理论伸长量" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['theoreticalelongation']" placeholder="无" ></a-input>
            </a-form-item>
          </a-col>

          <a-col :span="12" >
            <a-form-item label="延伸误差量" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['extenderror']" placeholder="无" ></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12" >
            <a-form-item label="是否合格" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['result_dictText']" placeholder="无" ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12" >
            <a-form-item label="操作员" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['userid']" placeholder="无" ></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12" >
            <a-form-item label="工程名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['engineeringname']" placeholder="无" ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12" >
            <a-form-item label="工程（施工）部位" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['engineeringsite']" placeholder="无" ></a-input>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </j-form-container>
    <!-- 子表单区域 -->
<!--    <a-tabs v-model="activeKey" @change="handleChangeTabs">-->
<!--      <a-tab-pane tab="压力机表信息" :key="refKeys[0]" :forceRender="true">-->
<!--        <j-editable-table-->
<!--          :ref="refKeys[0]"-->
<!--          :loading="bhzSwCailiaoTable.loading"-->
<!--          :columns="bhzSwCailiaoTable.columns"-->
<!--          :dataSource="bhzSwCailiaoTable.dataSource"-->
<!--          :maxHeight="300"-->
<!--          :disabled="formDisabled"-->
<!--          :rowNumber="true"-->
<!--          :rowSelection="true"-->
<!--          :actionButton="true">-->
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
  import { getAction } from '@/api/manage'
  import { FormTypes,getRefPromise } from '@/utils/JEditableTableUtil'
  import { JEditableTableMixin } from '@/mixins/JEditableTableMixin'
  import { validateDuplicateValue } from '@/utils/util'
  import JFormContainer from '@/components/jeecg/JFormContainer'
  import JDate from '@/components/jeecg/JDate'
  import JDictSelectTag from "@/components/dict/JDictSelectTag"

  export default {
    name: 'ZhangLaFrom',
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
        refKeys: ['SyyljBases', ],
        tableKeys:['SyyljBases', ],
        activeKey: 'SyyljBases',
        // 万能机表信息
        bhzSwCailiaoTable: {
          loading: false,
          dataSource: [],
          columns: [
        //     {
        //       title: '原始标距',
        //       key: 'wsbz',
        //       type: FormTypes.normal,
        //       width:"200px",
        //       placeholder: '请输入${title}',
        //       defaultValue:'',
        //     },
        //     {
        //       title: '断后标距',
        //       key: 'dhbz',
        //       type: FormTypes.normal,
        //       width:"200px",
        //       placeholder: '请输入${title}',
        //       defaultValue:'',
        //     },
        //     {
        //       title: '最大力值',
        //       key: 'lz',
        //       type: FormTypes.normal,
        //       width:"200px",
        //       placeholder: '请输入${title}',
        //       defaultValue:'',
        //     },
        //     {
        //       title: '抗拉强度',
        //       key: 'lzqd',
        //       type: FormTypes.normal,
        //       width:"200px",
        //       placeholder: '请输入${title}',
        //       defaultValue:'',
        //     },
        //     {
        //       title: '屈服力值',
        //       key: 'qflz',
        //       type: FormTypes.normal,
        //       width:"200px",
        //       placeholder: '请输入${title}',
        //       defaultValue:'',
        //     },
        //     {
        //       title: '屈服强度',
        //       type: FormTypes.normal,
        //       slotName:'action',
        //       key: 'qfqd',
        //       width:"200px",
        //       placeholder: '请输入${title}',
        //       defaultValue:'',
        //     },
        //     {
        //       title: '伸长率',
        //       type: FormTypes.normal,
        //       slotName:'action',
        //       key: 'scl',
        //       width:"200px",
        //       placeholder: '请输入${title}',
        //       defaultValue:'',
        //     },
        //     {
        //       title: '实验时间',
        //       type: FormTypes.normal,
        //       slotName:'action',
        //       key: 'sysj',
        //       width:"200px",
        //       placeholder: '请输入${title}',
        //       defaultValue:'',
        //     },
        //     {
        //       title: '最大力值伸长率',
        //       type: FormTypes.normal,
        //       slotName:'action',
        //       key: 'zdlzscl',
        //       width:"200px",
        //       placeholder: '请输入${title}',
        //       defaultValue:'',
        //     },
        //     {
        //       title: '拉断处描述',
        //       type: FormTypes.normal,
        //       slotName:'action',
        //       key: 'ldcms',
        //       width:"200px",
        //       placeholder: '请输入${title}',
        //       defaultValue:'',
        //     },
        //     {
        //       title: '断口位置',
        //       type: FormTypes.normal,
        //       slotName:'action',
        //       key: 'dkwz',
        //       width:"200px",
        //       placeholder: '请输入${title}',
        //       defaultValue:'',
        //     }
          ]
        },
        url: {
          add: "/syj/tSyjzb/add",
          edit: "/syj/tSyjzb/edit",
          queryById: "/syj/tSyjzb/queryById",
          SyyljBases: {
            list: '/syj/tSyjzb/querysyjByMainId'
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
        let fieldval = pick(this.model,'deviceno_dictText','beamtype','concretestrength','modulusofelasticity','steelbeamno','steelstrand','tensioningsection','tensioningdate','tensioningforce0','tensioningsection1','tensioningsection2','tensioningsection3','tensioningsection4','elongation0','elongation1','elongation2','elongation3','elongation4','oilpressure0','oilpressure1','oilpressure2','oilpressure3','oilpressure4','tensioncontrol','totalelongation','theoreticalelongation','extenderror','result_dictText','userid','engineeringname','engineeringsite')
        this.$nextTick(() => {
          this.form.setFieldsValue(fieldval)
        })
        // 加载子表数据
        // if (this.model.syjid) {
        //   let params = { syjid: this.model.syjid }
        //   this.requestSubTableData(this.url.SyyljBases.list, params, this.bhzSwCailiaoTable)
        // }
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
        // this.form.setFieldsValue(pick(row,'shebeibianhao','shiyanleixing','weituobianhao','zhijianriqi','shiyanriqi','lingqi','shijianchicun','shijianmianji','shiianshuliang','shejiqiangdu','zhesuanxishu','qiangdudaibiaozhi','pandingjieguo','caozuorenyuan','gongchengmingcheng','pinzhongbianma','gongchengzhijing','chengyamianji','shengchanchangjia','beizhu','shujushangchuanshijian'))
        this.form.setFieldsValue(pick(row,'deviceno_dictText','beamtype','concretestrength','modulusofelasticity','steelbeamno','steelstrand','tensioningsection','tensioningdate','tensioningforce0','tensioningsection1','tensioningsection2','tensioningsection3','tensioningsection4','elongation0','elongation1','elongation2','elongation3','elongation4','oilpressure0','oilpressure1','oilpressure2','oilpressure3','oilpressure4','tensioncontrol','totalelongation','theoreticalelongation','extenderror','result_dictText','userid','engineeringname','engineeringsite'))

      },

    }
  }
</script>

<style scoped>
</style>