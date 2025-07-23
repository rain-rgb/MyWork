<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form :form="form" slot="detail">
        <a-row>
          <!--          <a-col :span="24">-->
          <!--            <a-form-item label="任务单号" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <a-input v-decorator="['uuid']" placeholder="请输入任务单号"  ></a-input>-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <a-row>
            <!--            <a-col :span="12">-->
            <!--              <a-form-item label="所属部门" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
            <!--                <JselectDqDepart v-decorator="['sysOrgCode']" ::multi="false"  />-->
            <!--              </a-form-item>-->
            <!--            </a-col>-->
            <a-col :span="12" v-if="title == `新增`">
              <a-form-item label="请选择施工部位" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <JSelectDqProjName v-decorator="['sgbwguid', validatorRules.sgbwguid]" ::multi="false"/>
              </a-form-item>
            </a-col>
            <a-col :span="24" v-if="title != `新增`">
              <a-form-item label="施工部位" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
                <a-input v-decorator="['sgbwname']" disabled></a-input>
              </a-form-item>
            </a-col>
          </a-row>

                    <a-row>
                      <a-col v-has="'jcrwd:gcmc'" :span="24">
                        <a-form-item label="工程名称" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
                          <a-input v-decorator="['projectname']" placeholder="请输入工程名称"  ></a-input>
                        </a-form-item>
                      </a-col>
                      <a-col v-has="'jcrwd:sgbw'" :span="24">
                        <a-form-item label="施工部位" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
                          <a-input v-decorator="['sgbwname']" placeholder="请输入施工部位"  ></a-input>
                        </a-form-item>
                      </a-col>
                    </a-row>

          <a-row>
            <!--            <a-col :span="24">-->
            <!--              <a-form-item label="张拉任务状态码：0：未使用  1：已使用" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
            <!--                <a-input v-decorator="['status']" placeholder="请输入张拉任务状态码：0：未使用  1：已使用"  ></a-input>-->
            <!--              </a-form-item>-->
            <!--            </a-col>-->
            <a-col :span="12">
              <a-form-item label="试验类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-dict-select-tag type="list" v-decorator="['component']" :trigger-change="true" dictCode="component"
                                   placeholder="请选择试验类型"/>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="试验日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-date placeholder="请选择试验日期" :showTime="showTime" v-decorator="['zldate']" :trigger-change="true"
                        style="width: 100%" :format="format"/>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item label="设备厂家" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-dict-select-tag type="list" v-decorator="['shebeichangjia',validatorRules.shebeichangjia]"
                                   :trigger-change="true" dictCode="shebeichangjia" placeholder="请选择设备厂家"/>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="设备名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-search-select-tag placeholder="请选择设备名称" v-decorator="['shebeibianhao', validatorRules.shebeibianhao]"
                                     :dictOptions="dictOption">
                </j-search-select-tag>
                {{ selectValue }}
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="24">
              <a-form-item label="检测单位" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
                <a-input v-decorator="['jcdw']" placeholder="请输入检测单位"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="24">
              <a-form-item label="施工单位" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
                <a-input v-decorator="['sgdw']" placeholder="请输入施工单位"></a-input>
              </a-form-item>
            </a-col>

          </a-row>
          <!--          <a-col :span="24">-->
          <!--            <a-form-item label="组织机构id" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <a-input v-decorator="['departid']" placeholder="请输入组织机构id"  ></a-input>-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <!--          <a-col :span="24">-->
          <!--            <a-form-item label="sytypeid" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <a-input v-decorator="['sytypeid']" placeholder="请输入sytypeid"  ></a-input>-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <!--          <a-col :span="24">-->
          <!--            <a-form-item label="departname" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <a-input v-decorator="['departname']" placeholder="请输入departname"  ></a-input>-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <!--          <a-col :span="24">-->
          <!--            <a-form-item label="创建人" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <a-input v-decorator="['createBy']" placeholder="请输入创建人"  ></a-input>-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <!--          <a-col :span="24">-->
          <!--            <a-form-item label="创建日期" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <j-date placeholder="请选择创建日期" v-decorator="['createTime']" :trigger-change="true" style="width: 100%" />-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <!--          <a-col :span="24">-->
          <!--            <a-form-item label="更新日期" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <j-date placeholder="请选择更新日期" v-decorator="['updateTime']" :trigger-change="true" style="width: 100%" />-->
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
  import JselectDqDepart from '@comp/jeecgbiz/JselectDqDepart'
  import JSelectDqProjName from '@comp/jeecgbiz/JselectDqProjName'
  import { usershebeiList } from '@api/api'
  import Vue from 'vue'

  export default {
    name: 'HnthtConsignForm',
    components: {
      JFormContainer,
      JDate,
      JselectDqDepart,
      JSelectDqProjName,
    },
    props: {
      //流程表单data
      formData: {
        type: Object,
        default: () => {
        },
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
      },
      title:{
        type: String,
      },
    },
    data() {
      return {
        selectValue: '',
        dictOption: [],
        showTime: true,
        format: 'YYYY-MM-DD HH:mm:ss',
        form: this.$form.createForm(this),
        model: {},
        labelCol: {
          xs: { span: 24 },
          sm: { span: 6 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 15 },
        },
        labelCol1: {
          xs: { span: 24 },
          sm: { span: 3 },
        },
        wrapperCol1: {
          xs: { span: 24 },
          sm: { span: 18 },
        },
        confirmLoading: false,
        validatorRules: {
          shebeibianhao: {
            rules: [{
              required: true, message: '请选择设备!'
            }]
          },
          shebeichangjia: {
            rules: [{
              required: true, message: '请选择设备厂家!'
            }]
          },
          sgbwguid: {
            rules: [{
              required: true, message: '请选择施工部位!'
            }]
          },
        },
        url: {
          add: '/sys/sysDepartproject47/renwudanxiafaadd',
          edit: '/sys/sysDepartproject47/renwudanxiafaedit',
          queryById: '/hnthtconsign/hnthtConsign/queryById',
          aaa: '/sys/sysDepartproject/queryByOrgcode'
        }
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
      add() {
        this.edit({})
      },
      edit(record) {
        this.form.resetFields()
        this.model = Object.assign({}, record)
        this.visible = true
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model, 'uuid', 'projectname', 'component', 'sgbwguid', 'sgbwname', 'status', 'zldate', 'departid', 'sytypeid', 'departname', 'createBy', 'createTime', 'updateTime', 'sysOrgCode', 'shebeichangjia', 'shebeibianhao'))
        })
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
      submitForm() {
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
            let params = { id: values.sgbwguid }
            getAction(this.url.aaa, params).then(res1 => {
              if (res1.result == 1 || this.model.id) {
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
              }else {
                that.$message.warning("请选择最底层部位！")
                that.confirmLoading = false
              }
            })
          }

        })
      },
      shebeiList: function () {
        var sys_depart_orgcode = Vue.ls.get('SYS_DEPART_ORGCODE')
        usershebeiList({
          sys_depart_orgcode: sys_depart_orgcode,
          sbtypes: '14,42,43,56,77,78,79,83'
        }).then(res => {
          this.dictOption = []
          let result = res.result
          result.forEach(re => {
            this.dictOption.push({ text: re.sbname, value: re.sbjno })
          })
          ////console.log(res)
        })
      },
      popupCallback(row) {
        this.form.setFieldsValue(pick(row, 'uuid', 'projectname', 'component', 'sgbwguid', 'sgbwname', 'status', 'zldate', 'departid', 'sytypeid', 'departname', 'createBy', 'createTime', 'updateTime', 'sysOrgCode', 'shebeichangjia', 'shebeibianhao'))
      },
    }
  }
</script>