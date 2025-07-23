<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <!-- 主表单区域 -->
      <a-form :form="form" slot="detail">
        <a-row>
          <!--          <a-col :span="24" >-->
          <!--            <a-form-item label="主键uuid" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <a-input v-decorator="['uid', validatorRules.uid]" placeholder="请输入主键uuid" ></a-input>-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <!--          <a-col :span="24" >-->
          <!--            <a-form-item label="所属组织机构id" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <a-input v-decorator="['departid']" placeholder="请输入所属组织机构id" ></a-input>-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <a-col :span="24">
            <a-form-item label="设备名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-search-select-tag placeholder="请选择设备名称" v-decorator="['bhjno']" :dictOptions="dictOption">
              </j-search-select-tag>
              {{ selectValue }}
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="所属部门" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <JselectDqDepart v-decorator="['sysOrgCode']" multi/>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="是否报警" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['isCall']" :trigger-change="true" dictCode="is_call"
                                 placeholder="请选择是否报警"/>
            </a-form-item>
          </a-col>
          <!--          <a-col :span="24" >-->
          <!--            <a-form-item label="搅拌时间报警设计值" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <a-input-number v-decorator="['stirDatetimeDesign']" placeholder="请输入搅拌时间报警设计值" style="width: 100%" />-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <a-col :span="24">
            <a-form-item label="初级号码组" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['primaryPhones']" :trigger-change="true"
                                 dictCode="bhz_phone,names,uid,phonesname='1'" dictTable="bhz_phone"
                                 placeholder="请选择初级号码组"/>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="中级号码组" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['middlePhones']" :trigger-change="true"
                                 dictCode="bhz_phone,names,uid,phonesname='2'" dictTable="bhz_phone"
                                 placeholder="请选择中级号码组"/>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="高级号码组" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['advancedPhones']" :trigger-change="true"
                                 dictCode="bhz_phone,names,uid,phonesname='3'" dictTable="bhz_phone"
                                 placeholder="请选择高级号码组"/>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="出料温度预警号码组" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['wenduyujingPhones']" :trigger-change="true"
                                 dictCode="bhz_phone,names,uid,phonesname='19'" dictTable="bhz_phone"
                                 placeholder="请选择温度预警号码组"/>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="处置负责人" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <!--              <a-input v-decorator="['czperson']" placeholder="请选择处置负责人"></a-input>-->
              <div style="display: flex;">
                <a-button type="primary" @click="handleAddUserRole(1)" icon="search">选择</a-button>
                <a-input v-decorator="['czperson', personChoose.czperson]" :disabled="true"
                         placeholder="请选择处置负责人"></a-input>
              </div>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="审核负责人" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <!--              <a-input v-decorator="['shperson']" placeholder="请选择审核负责人"></a-input>-->
              <div style="display: flex;">
                <a-button type="primary" @click="handleAddUserRole(2)" icon="search">选择</a-button>
                <a-input v-decorator="['shperson', personChoose.shperson]" :disabled="true"
                         placeholder="请选择审核负责人"></a-input>
              </div>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="总监理工程师" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <!--              <a-input v-decorator="['spperson']" placeholder="请选择审批负责人"></a-input>-->
              <div style="display: flex;">
                <a-button type="primary" @click="handleAddUserRole(4)" icon="search">选择</a-button>
                <a-input v-decorator="['supervisorPerson', personChoose.supervisorPerson]" :disabled="true"
                         placeholder="请选择总监理工程师"></a-input>
              </div>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="审批负责人" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <!--              <a-input v-decorator="['spperson']" placeholder="请选择审批负责人"></a-input>-->
              <div style="display: flex;">
                <a-button type="primary" @click="handleAddUserRole(3)" icon="search">选择</a-button>
                <a-input v-decorator="['spperson', personChoose.spperson]" :disabled="true"
                         placeholder="请选择审批负责人"></a-input>
              </div>
            </a-form-item>
          </a-col>
          <!--          <a-col :span="24" >-->
          <!--            <a-form-item label="所属上级初级号码组" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <a-input v-decorator="['topprimaryPhones']" placeholder="请输入所属上级初级号码组" ></a-input>-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <!--          <a-col :span="24" >-->
          <!--            <a-form-item label="所属上级中级号码组" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <a-input v-decorator="['topmiddlePhones']" placeholder="请输入所属上级中级号码组" ></a-input>-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <!--          <a-col :span="24" >-->
          <!--            <a-form-item label="所属上级高级号码组" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <a-input v-decorator="['topadvancedPhones']" placeholder="请输入所属上级高级号码组" ></a-input>-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <!--          <a-col :span="24" >-->
          <!--            <a-form-item label="创建时间" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <j-date placeholder="请选择创建时间" v-decorator="['createTime']" :trigger-change="true" style="width: 100%" />-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <!--          <a-col :span="24" >-->
          <!--            <a-form-item label="更新时间" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <j-date placeholder="请选择更新时间" v-decorator="['updateTime']" :trigger-change="true" style="width: 100%" />-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <!--          <a-col :span="24" >-->
          <!--            <a-form-item label="权限配置" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <a-input v-decorator="['sysOrgCode']" placeholder="请输入权限配置" ></a-input>-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <!--          <a-col :span="24" >-->
          <!--            <a-form-item label="创建人" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <a-input v-decorator="['createBy']" placeholder="请输入创建人" ></a-input>-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <a-col :span="24">
            <a-form-item label="未处置超时是否预警" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['csyj']" :trigger-change="true" dictCode="is_call"
                                 placeholder="请选择是否报警"/>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="请输入超时时长" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['cssj']" placeholder="注意：请输入整数"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="请设置超时单位" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['cssjdw']" :trigger-change="true" dictCode="cssjdw"
                                 placeholder="时长单位 分钟或小时"/>
            </a-form-item>
          </a-col>
          <a-col v-if="showFlowSubmitButton" :span="24" style="text-align: center">
            <a-button @click="submitForm">提 交</a-button>
          </a-col>
        </a-row>
      </a-form>
    </j-form-container>
    <!-- 子表单区域 -->
    <a-tabs v-model="activeKey" @change="handleChangeTabs">
      <a-tab-pane tab="拌合站超标配置子表" :key="refKeys[0]" :forceRender="true">
        <j-editable-table
          :ref="refKeys[0]"
          :loading="bhzChaobiaoCfgTable.loading"
          :columns="bhzChaobiaoCfgTable.columns"
          :dataSource="bhzChaobiaoCfgTable.dataSource"
          :maxHeight="300"
          :disabled="formDisabled"
          :rowNumber="true"
          :rowSelection="true"
          :actionButton="true"
          @arrdata="treaaaa"/>
      </a-tab-pane>
    </a-tabs>
    <a-card :bordered="false">
      <Select-User-Modal1 ref="selectUserModal1" @selectFinished="selectOK"></Select-User-Modal1>
    </a-card>
  </a-spin>
</template>

<script>

import pick from 'lodash.pick'
import { getAction, httpAction, postAction } from '@/api/manage'
import { FormTypes, getRefPromise } from '@/utils/JEditableTableUtil'
import { JEditableTableMixin } from '@/mixins/JEditableTableMixin'
import { filterObj, validateDuplicateValue } from '@/utils/util'
import JFormContainer from '@/components/jeecg/JFormContainer'
import JDate from '@/components/jeecg/JDate'
import JselectDqDepart from '@/components/jeecgbiz/JselectDqDepart'
import { usershebeiList } from '@api/api'
import { SYS_DEPART_ORGCODE } from '@/store/mutation-types'
import Vue from 'vue'
import SelectUserModal1 from '@views/bhz/bhzcfg/modules/SelectUserModal1'
//当前用户的组织机构权限（当前使用）
export default {
  name: 'BhzCallCfgForm',
  mixins: [JEditableTableMixin],
  components: {
    JFormContainer,
    JDate,
    JselectDqDepart,
    SelectUserModal1,
  },
  data() {
    return {
      dictOption: [],
      selectValue: '',
      personChoose: {
        czperson: {
          rules: [{
            required: false, message: '请选择处置负责人!'
          }]
        },
        shperson: {
          rules: [{
            required: false, message: '请选择审核负责人!'
          }]
        },
        spperson: {
          rules: [{
            required: false, message: '请选择审批负责人!'
          }]
        },
         supervisorPerson: {
          rules: [{
            required: false, message: '请选择总监理工程师!'
          }]
        },
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
      addDefaultRowNum: 0,
      refKeys: ['bhzChaobiaoCfg',],
      tableKeys: ['bhzChaobiaoCfg',],
      activeKey: 'bhzChaobiaoCfg',
      // 拌合站超标配置子表
      bhzChaobiaoCfgTable: {
        loading: false,
        dataSource: [],
        columns: [
          {
            title: '材料类型',
            key: 'materialType',
            dictCode: 'material_type',
            width: '300px',
            type: FormTypes.select,
            options: [],
            placeholder: '请选择${title}',
            validateRules: [{ required: true, message: '请选择${title}' }]
          },
          {
            title: '初级标准值',
            key: 'rimary',
            type: FormTypes.inputNumber,
            //width:"200px",
            placeholder: '请输入${title}',
            defaultValue: '3',
            validateRules: [
              {
                required: true, // 必填
                message: '${title}不能为空' // 提示的文本
              }
            ]
          },
          {
            title: '中级标准值',
            key: 'middle',
            type: FormTypes.inputNumber,
            //width:"200px",
            placeholder: '请输入${title}',
            defaultValue: '5',
            validateRules: [
              {
                required: true, // 必填
                message: '${title}不能为空' // 提示的文本
              }
            ]
          },
          {
            title: '高级标准值',
            key: 'advanced',
            type: FormTypes.inputNumber,
            //width:"200px",
            placeholder: '请输入${title}',
            defaultValue: '8',
            validateRules: [
              {
                required: true, // 必填
                message: '${title}不能为空' // 提示的文本
              }
            ]
          },
        ]
      },
      url: {
        add: '/bhzcfg/bhzCallCfg/add',
        edit: '/bhzcfg/bhzCallCfg/edit',
        queryById: '/bhzcfg/bhzCallCfg/queryById',
        bhzChaobiaoCfg: {
          list: '/bhzcfg/bhzCallCfg/queryBhzChaobiaoCfgByMainId'
        },
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
    this.shebeiList()
  },
  methods: {
    shebeiList: function () {
      var sys_depart_orgcode = Vue.ls.get('SYS_DEPART_ORGCODE')
      usershebeiList({
        sys_depart_orgcode: sys_depart_orgcode,
        sbtypes: '0,1,2'
      }).then(res => {
        this.dictOption = []
        let result = res.result
        result.forEach(re => {
          this.dictOption.push({ text: re.sbname, value: re.sbjno })
        })
        //console.log(res)
      })
    },
    handleOk() {//重写提交方法
      this.submitForm()
    },
    submitForm() {//重写提交方法
      let bhzChaobiaoCfgLists = this.$refs[this.refKeys[0]].getValuesSync().values//异步获取子表数据
      for (let i = 0; i < bhzChaobiaoCfgLists.length; i++) {//此处为了删除添加子表数据所产生的id
        bhzChaobiaoCfgLists[i].id = ''
      }
      const that = this
      // 触发表单验证
      this.form.validateFields((err, values) => {
        if (!err) {
          that.confirmLoading = true
          let httpurl = ''
          let method = ''
          if (!this.model.id) {
            httpurl += this.url.add
            method = 'post'
          } else {
            httpurl += this.url.edit
            method = 'put'
          }
          let formData = Object.assign(this.model, values)
          formData.bhzChaobiaoCfgList = bhzChaobiaoCfgLists
          console.log('表单提交数据', formData)
          httpAction(httpurl, formData, method).then((res) => {
            if (res.success) {
              that.$message.success(res.message)
              that.$emit('ok')
            } else {
              that.$message.warning(res.message)
            }
          }).finally(() => {
            that.confirmLoading = false
          })
        }

      })
    },
    treaaaa(obj) {
      console.log(obj)
    },
    addBefore() {
      this.form.resetFields()
      this.bhzChaobiaoCfgTable.dataSource = []
    },
    getAllTable() {
      let values = this.tableKeys.map(key => getRefPromise(this, key))
      return Promise.all(values)
    },
    /** 调用完edit()方法之后会自动调用此方法 */
    editAfter() {
      let fieldval = pick(this.model, 'bhjno', 'sysOrgCode', 'isCall', 'primaryPhones', 'middlePhones', 'advancedPhones', 'wenduyujingPhones','czperson','shperson','spperson','supervisorPerson')
      this.$nextTick(() => {
        this.form.setFieldsValue(fieldval)
      })
      // 加载子表数据
      if (this.model.uid) {
        let params = { id: this.model.uid }
        this.requestSubTableData(this.url.bhzChaobiaoCfg.list, params, this.bhzChaobiaoCfgTable)
      }
    },
    /** 整理成formData */
    classifyIntoFormData(allValues) {
      let main = Object.assign(this.model, allValues.formValue)
      return {
        ...main, // 展开
        bhzChaobiaoCfgList: allValues.tablesValue[0].values,
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
    popupCallback(row) {
      this.form.setFieldsValue(pick(row, 'bhjno', 'isCall', 'sysOrgCode', 'primaryPhones', 'middlePhones', 'advancedPhones', 'wenduyujingPhones','czperson','shperson','spperson','supervisorPerson'))
    },
    handleAddUserRole(type) {
      if (this.currentRoleId == '') {
        this.$message.error('请选择一个角色!')
      } else {
        this.$refs.selectUserModal1.type = type
        this.$refs.selectUserModal1.visible = true
      }
    },
    selectOK(data,type) {
      if(type === 1){
        this.form.setFieldsValue({
          czperson: data,
        })
      }else if(type === 2){
        this.form.setFieldsValue({
          shperson: data,
        })
      }else if(type === 3){
        this.form.setFieldsValue({
          spperson: data,
        })
      }else if(type === 4){
        this.form.setFieldsValue({
          supervisorPerson: data,
        })
      }
    },
  }
}
</script>

<style scoped>
</style>