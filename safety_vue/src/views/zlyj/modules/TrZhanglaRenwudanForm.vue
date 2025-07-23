<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form :form="form" slot="detail">
        <a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item label="设备名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-search-select-tag placeholder="请选择设备名称" v-decorator="['shebeibianh', validatorRules.shebeibianh]"
                                     :dictOptions="dictOption">
                </j-search-select-tag>
                {{ selectValue }}
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="请选择施工部位" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <JSelectDqProjName v-decorator="['sgbwuuid']" ::multi="false"/>
              </a-form-item>
            </a-col>
          </a-row>

          <!--        <a-row>-->
          <!--          <a-col :span="24">-->
          <!--            <a-form-item label="施工部位" :labelCol="labelCol1" :wrapperCol="wrapperCol1">-->
          <!--              <a-input v-decorator="['sgbwname']" placeholder="施工部位"  ></a-input>-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <!--        </a-row>-->
          <!--        <a-row>-->
          <!--          <a-col :span="24">-->
          <!--          <a-form-item label="工程名称" :labelCol="labelCol1" :wrapperCol="wrapperCol1">-->
          <!--            <a-input v-decorator="['projectname']" placeholder="工程名称"  ></a-input>-->
          <!--          </a-form-item>-->
          <!--            </a-col>-->
          <!--        </a-row>-->
          <a-row>
            <a-col :span="12">
              <a-form-item label="一次张拉日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-date placeholder="请选择一次张拉日期" v-decorator="['zldate']" style="width: 100%"
                               :trigger-change="true"/>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="一次张拉力" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['sjzll']" placeholder="请输入一次张拉力"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <!--        <a-row>
                    <a-col :span="12">
                      <a-form-item label="二次张拉日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
                        <a-date-picker placeholder="请输入二次张拉日期" v-decorator="['zldate2']" style="width: 100%" :trigger-change="true" />
                      </a-form-item>
                    </a-col>
                    <a-col :span="12">
                      <a-form-item label="二次张拉力" :labelCol="labelCol" :wrapperCol="wrapperCol">
                        <a-input v-decorator="['sjzll2']" placeholder="请输入二次张拉力"  ></a-input>
                      </a-form-item>
                    </a-col>
                  </a-row>
                  <a-row>
                    <a-col :span="12">
                      <a-form-item label="预应力钢材种类" :labelCol="labelCol" :wrapperCol="wrapperCol">
                        <a-input v-decorator="['yylgczlgg']" placeholder="请输入预应力钢材种类规格"  ></a-input>
                      </a-form-item>
                    </a-col>
                    <a-col :span="12">
                      <a-form-item label="预应力钢材产地" :labelCol="labelCol" :wrapperCol="wrapperCol">
                        <a-input v-decorator="['yylgccdpp']" placeholder="请输入预应力钢材产地品牌"  ></a-input>
                      </a-form-item>
                    </a-col>
                  </a-row>
                  <a-row>
                    <a-col :span="12">
                      <a-form-item label="预应力钢材检验单" :labelCol="labelCol" :wrapperCol="wrapperCol">
                        <a-input v-decorator="['yylgcbh']" placeholder="请输入预应力钢材检验单编号"  ></a-input>
                      </a-form-item>
                    </a-col>
                    <a-col :span="12">
                      <a-form-item label="锚夹具种类规格" :labelCol="labelCol" :wrapperCol="wrapperCol">
                        <a-input v-decorator="['mjjzlgg']" placeholder="请输入锚夹具种类规格"  ></a-input>
                      </a-form-item>
                    </a-col>
                  </a-row>
                  <a-row>
                    <a-col :span="12">
                      <a-form-item label="锚夹具产地品牌" :labelCol="labelCol" :wrapperCol="wrapperCol">
                        <a-input v-decorator="['mjjcdpp']" placeholder="请输入锚夹具产地品牌"  ></a-input>
                      </a-form-item>
                    </a-col>
                    <a-col :span="12">
                      <a-form-item label="锚夹具检验单编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                        <a-input v-decorator="['mjjbh']" placeholder="请输入锚夹具检验单编号"  ></a-input>
                      </a-form-item>
                    </a-col>
                  </a-row>
                  <a-row>
                    <a-col :span="12">
                      <a-form-item label="锚垫板种类规格" :labelCol="labelCol" :wrapperCol="wrapperCol">
                        <a-input v-decorator="['mdbzlgg']" placeholder="请输入锚垫板种类规格"  ></a-input>
                      </a-form-item>
                    </a-col>
                    <a-col :span="12">
                      <a-form-item label="锚垫板产地品牌" :labelCol="labelCol" :wrapperCol="wrapperCol">
                        <a-input v-decorator="['mdbcdpp']" placeholder="请输入锚垫板产地品牌"  ></a-input>
                      </a-form-item>
                    </a-col>
                  </a-row>
                  <a-row>
                    <a-col :span="12">
                      <a-form-item label="锚垫板检验单编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                        <a-input v-decorator="['mdbbh']" placeholder="请输入锚垫板检验单编号"  ></a-input>
                      </a-form-item>
                    </a-col>
                    <a-col :span="12">
                      <a-form-item label="梁板理论上拱度" :labelCol="labelCol" :wrapperCol="wrapperCol">
                        <a-input v-decorator="['lbgd']" placeholder="请输入梁板理论上拱度"  ></a-input>
                      </a-form-item>
                    </a-col>
                  </a-row>-->
          <a-row>
            <a-col :span="12">
              <a-form-item label="混凝土的设计强度" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-dict-select-tag type="list" v-decorator="['hntsjqd']" :trigger-change="true" dictCode="betlev"
                                   placeholder="请选择混凝土的设计强度"/>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="两端千斤顶的张拉力误差指标" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['zllwc']" placeholder="请输入两端千斤顶的张拉力误差指标"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item label="梁型" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-dict-select-tag v-decorator="['lx']" placeholder="请选择梁型" dictCode="lx"/>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="孔道数" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['kds']" placeholder="请输入孔道数"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
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
  import JSelectDqProjName from '@comp/jeecgbiz/JselectDqProjName'
  import { usershebeiList } from '@api/api'
  import Vue from 'vue'

  export default {
    name: 'TrZhanglaRenwudanForm',
    components: {
      JFormContainer,
      JSelectDqProjName
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
      }
    },
    data() {
      return {
        selectValue: '',
        dictOption: [],
        dictOptions: [{
          text: '箱型梁',
          value: '0'
        }, {
          text: 'T型梁',
          value: '1'
        }, {
          text: '槽形梁',
          value: '2'
        }, {
          text: '空心板梁',
          value: '3'
        }, {
          text: '其他',
          value: '10'
        }],
        showTime: true,
        format: 'YYYY-MM-DD',
        form: this.$form.createForm(this),
        model: {},
        labelCol: {
          xs: { span: 24 },
          sm: { span: 8 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 12 },
        },
        labelCol1: {
          xs: { span: 24 },
          sm: { span: 4 },
        },
        wrapperCol1: {
          xs: { span: 24 },
          sm: { span: 18 },
        },
        confirmLoading: false,
        validatorRules: {
          shebeibianh: {
            rules: [{
              required: true, message: '请选择设备!'
            }]
          }
        },
        url: {
          //add: "/trzhanglarenwudan/trZhanglaRenwudan/add",
          add: '/sys/sysDepartproject47/Zlrwdadd',
          //edit: "/trzhanglarenwudan/trZhanglaRenwudan/edit",
          edit: '/sys/sysDepartproject47/Zlrwdedit',
          queryById: '/trzhanglarenwudan/trZhanglaRenwudan/queryById',
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
      this.shebeiList()
      this.showFlowData()
    },
    methods: {
      shebeiList: function () {
        var sys_depart_orgcode = Vue.ls.get('SYS_DEPART_ORGCODE')
        usershebeiList({
          sys_depart_orgcode: sys_depart_orgcode,
          sbtypes: '9'
        }).then(res => {
          this.dictOption = []
          let result = res.result
          result.forEach(re => {
            this.dictOption.push({ text: re.sbname, value: re.sbjno })
          })
          ////console.log(res)
        })
      },
      add() {
        this.edit({})
      },
      edit(record) {
        this.form.resetFields()
        this.model = Object.assign({}, record)
        this.visible = true
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model, 'uuid', 'projectname', 'girderplace', 'component', 'sgbwuuid', 'sgbwname', 'pedestal', 'status', 'zldate', 'createTime', 'updateTime', 'departid', 'sjzll', 'createBy', 'sysOrgCode', 'zldate2', 'sjzll2', 'status2', 'departname', 'shebeibianh', 'yylgczlgg', 'yylgccdpp', 'yylgcbh', 'mjjzlgg', 'mjjcdpp', 'mjjbh', 'mdbzlgg', 'mdbcdpp', 'mdbbh', 'lbgd', 'hntsjqd', 'zllwc','lx','kds'))
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
            console.log('表单提交数据', formData)
            let params = { id: values.sgbwuuid }
            getAction(this.url.aaa, params).then(res1 => {
              if (res1.result == 1) {
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
              } else {
                that.$message.warning('请选择最底层部位！')
                that.confirmLoading = false
              }
            })
          }

        })
      },
      popupCallback(row) {
        this.form.setFieldsValue(pick(row, 'uuid', 'projectname', 'girderplace', 'component', 'sgbwuuid', 'sgbwname', 'pedestal', 'status', 'zldate', 'createTime', 'updateTime', 'departid', 'sjzll', 'createBy', 'sysOrgCode', 'zldate2', 'sjzll2', 'status2', 'departname', 'shebeibianh', 'yylgczlgg', 'yylgccdpp', 'yylgcbh', 'mjjzlgg', 'mjjcdpp', 'mjjbh', 'mdbzlgg', 'mdbcdpp', 'mdbbh', 'lbgd', 'hntsjqd', 'zllwc','lx','kds'))
      },
    }
  }
</script>