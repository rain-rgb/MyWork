<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <!-- 主表单区域 -->
      <a-form :form="form" slot="detail">
        <a-row>
          <a-col :span="12">
            <a-form-item label="设备名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['deviceno_dictText']" placeholder="无"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="梁识别码" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['componentid']" placeholder="无"></a-input>
            </a-form-item>
          </a-col>
          </a-row>
            <a-row>
          <a-col :span="12">
            <a-form-item label="梁名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['componentparts']" placeholder="无"></a-input>
            </a-form-item>
          </a-col>

          <a-col :span="12">
            <a-form-item label="梁型" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['beamtype']" placeholder="无"></a-input>
            </a-form-item>
          </a-col>
            </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-item label="砼设计强度" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['concretestrength']" placeholder="无"></a-input>
            </a-form-item>
          </a-col>

          <a-col :span="12">
            <a-form-item label="弹性模量" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['modulusofelasticity']" placeholder="无"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-item label="钢束编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['steelbeamno']" placeholder="无"></a-input>
            </a-form-item>
          </a-col>

          <a-col :span="12">
            <a-form-item label="钢绞线根数" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['steelstrand']" placeholder="无"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-item label="压浆方向" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['pulpingorientation']" placeholder="无"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="压浆顺序" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['pulpingorder']" placeholder="无"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-item label="初始流动速度" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['flowvelocity']" placeholder="无"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="流动度" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['fluidity']" placeholder="无"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-item label="配合比" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['mixproportion']" placeholder="无"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="水胶比" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['waterratio']" placeholder="无"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-item label="搅拌时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['stirringtime']" placeholder="无"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="开始时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['startdate']" placeholder="无"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-item label="结束时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['enddate']" placeholder="无"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="进浆量" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['pulpingvolume']" placeholder="无"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-item label="理论浆量" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['standardvolume']" placeholder="无"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="进浆压力" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['pulpingpressurein']" placeholder="无"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-item label="出浆压力" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['pulpingpressureout']" placeholder="无"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="持压时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['holdingtime']" placeholder="无"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-item label="操作员" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['userid']" placeholder="无"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="其他信息" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['otherinformation']" placeholder="无"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-item label="工程名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['engineeringname']" placeholder="无"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="工程部位" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['engineeringsite']" placeholder="无"></a-input>
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
    <!--          :loading="FYalijiTable.loading"-->
    <!--          :columns="FYalijiTable.columns"-->
    <!--          :dataSource="FYalijiTable.dataSource"-->
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
import {getAction} from '@/api/manage'
import {FormTypes, getRefPromise} from '@/utils/JEditableTableUtil'
import {JEditableTableMixin} from '@/mixins/JEditableTableMixin'
import {validateDuplicateValue} from '@/utils/util'
import JFormContainer from '@/components/jeecg/JFormContainer'
import JDate from '@/components/jeecg/JDate'
import JDictSelectTag from "@/components/dict/JDictSelectTag"

export default {
  name: 'yjBasesForm',
  mixins: [JEditableTableMixin],
  components: {
    JFormContainer,
    JDate,
    JDictSelectTag,
  },
  data() {
    return {
      labelCol: {
        xs: {span: 24},
        sm: {span: 6},
      },
      wrapperCol: {
        xs: {span: 24},
        sm: {span: 16},
      },
      labelCol2: {
        xs: {span: 24},
        sm: {span: 3},
      },
      wrapperCol2: {
        xs: {span: 24},
        sm: {span: 20},
      },
      // 新增时子表默认添加几行空数据
      addDefaultRowNum: 1,
      validatorRules: {},
      refKeys: ['yjBases',],
      tableKeys: ['yjBases',],
      activeKey: 'yjBases',
      // 水稳材料表信息
      FYalijiTable: {
        loading: false,
        dataSource: [],
        columns: [
          {
            title: '试件序号',
            key: 'sjxh',
            type: FormTypes.normal,
            width: "200px",
            placeholder: '请输入${title}',
            defaultValue: '',
          },
          // {
          //   title: '实际配比',
          //   key: 'shijipb',
          //   type: FormTypes.normal,
          //   width:"200px",
          //   placeholder: '请输入${title}',
          //   defaultValue:'',
          // },
          // {
          //   title: '理论配比',
          //   key: 'lilunpb',
          //   type: FormTypes.normal,
          //   width:"200px",
          //   placeholder: '请输入${title}',
          //   defaultValue:'',
          // },
          {
            title: '制件编号',
            key: 'zzjj',
            type: FormTypes.normal,
            width: "200px",
            placeholder: '请输入${title}',
            defaultValue: '',
          },
          {
            title: '抗压力值',
            key: 'kylz',
            type: FormTypes.normal,
            width: "200px",
            placeholder: '请输入${title}',
            defaultValue: '',
          },
          {
            title: '抗压强度',
            key: 'kyqd',
            type: FormTypes.normal,
            width: "200px",
            placeholder: '请输入${title}',
            defaultValue: '',
          },
          {
            title: '试验时间',
            key: 'sysj',
            type: FormTypes.normal,
            width: "200px",
            placeholder: '请输入${title}',
            defaultValue: '',
          },

          {
            title: '完成时间',
            type: FormTypes.slot,
            slotName: 'wcsj',
            key: 'chaobiaodengji',
            width: "200px",
            placeholder: '请输入${title}',
            defaultValue: '',
          }
        ]
      },
      url: {
        add: "/zl/YaJiang/add",
        edit: "/zl/YaJiang/edit",
        queryById: "/zl/YaJiang/queryById",
        SyyljBases: {
          list: '/zl/YaJiang/selectById'
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
    this.showFlowData();
  },
  methods: {
    addBefore() {
      this.form.resetFields()
      this.FYalijiTable.dataSource = []
    },
    getAllTable() {
      let values = this.tableKeys.map(key => getRefPromise(this, key))
      return Promise.all(values)
    },
    /** 调用完edit()方法之后会自动调用此方法 */
    editAfter() {
      let fieldval = pick(this.model, 'deviceno_dictText', 'componentid', 'componentparts', 'beamtype', 'concretestrength', 'modulusofelasticity', 'steelbeamno', 'steelstrand', 'pulpingorientation', 'pulpingorder', 'flowvelocity', 'fluidity', 'mixproportion', 'waterratio', 'stirringtime', 'startdate', 'enddate', 'pulpingvolume', 'standardvolume', 'pulpingpressurein', 'pulpingpressureout', 'holdingtime', 'userid', 'otherinformation', 'volumecurve', 'pressureincurve', 'pressureoutcurve', 'engineeringname', 'engineeringsite')
      this.$nextTick(() => {
        this.form.setFieldsValue(fieldval)
      })
      // 加载子表数据
      // if (this.model.syjid) {
      //   let params = { syjid: this.model.syjid }
      //   this.requestSubTableData(this.url.SyyljBases.list, params, this.FYalijiTable)
      // }
    },
    /** 整理成formData */
    classifyIntoFormData(allValues) {
      let main = Object.assign(this.model, allValues.formValue)
      return {
        ...main, // 展开
        FYalijiTableList: allValues.tablesValue[0].values,
      }
    },
    //渲染流程表单数据
    showFlowData() {
      if (this.formBpm === true) {
        let params = {id: this.formData.dataId};
        getAction(this.url.queryById, params).then((res) => {
          if (res.success) {
            this.edit(res.result);
          }
        })
      }
    },
    validateError(msg) {
      this.$message.error(msg)
    },
    popupCallback(row) {
      this.form.setFieldsValue(pick(row, 'deviceno_dictText', 'componentid', 'componentparts', 'beamtype', 'concretestrength', 'modulusofelasticity', 'steelbeamno', 'steelstrand', 'pulpingorientation', 'pulpingorder', 'flowvelocity', 'fluidity', 'mixproportion', 'waterratio', 'stirringtime', 'startdate', 'enddate', 'pulpingvolume', 'standardvolume', 'pulpingpressurein', 'pulpingpressureout', 'holdingtime', 'userid', 'otherinformation', 'volumecurve', 'pressureincurve', 'pressureoutcurve', 'engineeringname', 'engineeringsite'))
    },

  }
}
</script>

<style scoped>
</style>