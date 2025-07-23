<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <!-- 主表单区域 -->
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <!--        <a-row>-->
        <!--          <a-col :span="24">-->
        <!--            <a-form-model-item label="检测部位" :labelCol="{ span: 4 }" :wrapperCol="{ span: 20 }" prop="gjbh">-->
        <!--              <a-input v-model="model.gjbh" placeholder="请输入检测部位"></a-input>-->
        <!--            </a-form-model-item>-->
        <!--          </a-col>-->
        <!--          <a-col :span="12">-->
        <!--            <a-form-model-item label="设备厂家" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shebeichangjia">-->
        <!--              <a-input v-model="model.shebeichangjia" placeholder="请输入设备厂家"></a-input>-->
        <!--            </a-form-model-item>-->
        <!--          </a-col>-->
        <!--          <a-col :span="12">-->
        <!--            <a-form-model-item label="设备编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sbbh">-->
        <!--              <a-input v-model="model.sbbh" placeholder="请输入设备编号"></a-input>-->
        <!--            </a-form-model-item>-->
        <!--          </a-col>-->
        <!--          <a-col :span="12">-->
        <!--            <a-form-model-item label="单位工程" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="dwgc">-->
        <!--              <a-input v-model="model.dwgc" placeholder="请输入单位工程"></a-input>-->
        <!--            </a-form-model-item>-->
        <!--          </a-col>-->
        <!--          <a-col :span="12">-->
        <!--            <a-form-model-item label="分部工程" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="fbgc">-->
        <!--              <a-input v-model="model.fbgc" placeholder="请输入分部工程"></a-input>-->
        <!--            </a-form-model-item>-->
        <!--          </a-col>-->
        <!--          <a-col :span="12">-->
        <!--            <a-form-model-item label="分项工程" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="fxgc">-->
        <!--              <a-input v-model="model.fxgc" placeholder="请输入分项工程"></a-input>-->
        <!--            </a-form-model-item>-->
        <!--          </a-col>-->
        <!--          <a-col :span="12">-->
        <!--            <a-form-model-item label="梁号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="lh">-->
        <!--              <a-input v-model="model.lh" placeholder="请输入梁号"></a-input>-->
        <!--            </a-form-model-item>-->
        <!--          </a-col>-->
        <!--          <a-col :span="12">-->
        <!--            <a-form-model-item label="检测日期" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jcrq">-->
        <!--              <a-input v-model="model.jcrq" placeholder="请输入检测日期"></a-input>-->
        <!--            </a-form-model-item>-->
        <!--          </a-col>-->
        <!--          <a-col :span="12">-->
        <!--            <a-form-model-item label="检测内容" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jcnr">-->
        <!--              <a-input v-model="model.jcnr" placeholder="请输入检测内容"></a-input>-->
        <!--            </a-form-model-item>-->
        <!--          </a-col>-->
        <!--          <a-col :span="12">-->
        <!--            <a-form-model-item label="设计力值" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sjlz">-->
        <!--              <a-input v-model="model.sjlz" placeholder="请输入设计力值"></a-input>-->
        <!--            </a-form-model-item>-->
        <!--          </a-col>-->
        <!--          <a-col :span="12">-->
        <!--            <a-form-model-item label="孔道钢绞线束数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="kdgs">-->
        <!--              <a-input v-model="model.kdgs" placeholder="请输入孔道钢绞线束数"></a-input>-->
        <!--            </a-form-model-item>-->
        <!--          </a-col>-->
        <!--          <a-col :span="12">-->
        <!--            <a-form-model-item label="设计砼强度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sjqd">-->
        <!--              <a-input v-model="model.sjqd" placeholder="请输入设计砼强度"></a-input>-->
        <!--            </a-form-model-item>-->
        <!--          </a-col>-->
        <!--          <a-col :span="12">-->
        <!--            <a-form-model-item label="超标等级" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="overLevel">-->
        <!--              <a-input-number v-model="model.overLevel" placeholder="请输入超标等级" style="width: 100%" />-->
        <!--            </a-form-model-item>-->
        <!--          </a-col>-->
        <!--        </a-row>-->
      </a-form-model>
    </j-form-container>
    <!-- 子表单区域 -->
    <a-tabs v-model="activeKey" @change="handleChangeTabs">
      <a-tab-pane tab="锚下预应力张拉子表" :key="refKeys[0]" :forceRender="true">
        <!-- <j-editable-table
          :ref="refKeys[0]"
          :loading="trMaoxiayuyingliSTable.loading"
          :columns="trMaoxiayuyingliSTable.columns"
          :dataSource="trMaoxiayuyingliSTable.dataSource"
          :maxHeight="300"
          :disabled="formDisabled"
          :rowNumber="true"
          :rowSelection="true"
          :actionButton="true"/> -->
        <a-table
          ref="table"
          size="middle"
          bordered
          rowKey="id"
          :scroll="{ x: true }"
          :columns="columns"
          :dataSource="trMaoxiayuyingliSTable.dataSource"
          :pagination="true"
          :loading="loading"
        >

        <span slot="overLevel" slot-scope="overLevel, record">
        <a-tag color="green" v-if="record.overLevel == '0'">合格</a-tag>
         <a-tag color="red" v-if="record.overLevel == '1'">不合格</a-tag>
       </span>
          <template slot="qxLineSlot" slot-scope="text, record">
            <a @click="showModalLine(record)">查看</a>
          </template>
          <!-- <template slot="qxSlot" slot-scope="text, record">
            <div class="tooltip_box" :style="`text-align: center;`">
              <a-tooltip
                placement="top"
                :title="text"
                trigger="hover"
                overlayClassName="Ysjldfh_tooltip_class"
              >
                <div :style="`minWidth:150px`">
                  <template slot="title"></template>
                  <div
                    :style="`cusor: pointer;
                      overflow: hidden;
                      white-space: nowrap;
                      text-overflow: ellipsis;
                      width: 150px`"
                  >
                    {{ text }}
                  </div>
                </div>
              </a-tooltip>
            </div>
          </template> -->
        </a-table>
      </a-tab-pane>
    </a-tabs>
    <a-modal :width="1400" v-model="visibleLine" title="过程曲线" @ok="handleOkLine">
      <div id="EChartLine">
      </div>
    </a-modal>
  </a-spin>
</template>

<script>

import { getAction } from '@/api/manage'
import { FormTypes, getRefPromise, VALIDATE_NO_PASSED } from '@/utils/JEditableTableUtil'
import { JEditableTableModelMixin } from '@/mixins/JEditableTableModelMixin'
import { validateDuplicateValue } from '@/utils/util'
import * as echarts from 'echarts'

export default {
  name: 'TrMaoxiayuyingliMForm1',
  mixins: [JEditableTableModelMixin],
  components: {},
  data() {
    return {
      labelCol: {
        xs: { span: 12 },
        sm: { span: 6 }
      },
      wrapperCol: {
        xs: { span: 12 },
        sm: { span: 16 }
      },
      labelCol2: {
        xs: { span: 12 },
        sm: { span: 3 }
      },
      wrapperCol2: {
        xs: { span: 12 },
        sm: { span: 20 }
      },
      model: {},
      // 新增时子表默认添加几行空数据
      addDefaultRowNum: 1,
      validatorRules: {},
      refKeys: ['trMaoxiayuyingliS'],
      tableKeys: ['trMaoxiayuyingliS'],
      activeKey: 'trMaoxiayuyingliS',
      // 锚下预应力张拉子表
      trMaoxiayuyingliSTable: {
        loading: false,
        dataSource: [],
        columns: [
          {
            title: '束号',
            key: 'gsh',
            type: FormTypes.input,
            width: '200px',
            placeholder: '请输入${title}',
            defaultValue: ''
          },
          {
            title: '孔号',
            key: 'kh',
            type: FormTypes.input,
            width: '200px',
            placeholder: '请输入${title}',
            defaultValue: ''
          },
          {
            title: '设计力值',
            key: 'sjlz',
            type: FormTypes.input,
            width: '200px',
            placeholder: '请输入${title}',
            defaultValue: ''
          },
          {
            title: '实测有效应力',
            key: 'yxlz',
            type: FormTypes.input,
            width: '200px',
            placeholder: '请输入${title}',
            defaultValue: ''
          },
          {
            title: '警戒值',
            key: 'jjz',
            type: FormTypes.input,
            width: '200px',
            placeholder: '请输入${title}',
            defaultValue: ''
          },
          {
            title: '标准有效应力',
            key: 'bzyxlz',
            type: FormTypes.input,
            width: '200px',
            placeholder: '请输入${title}',
            defaultValue: ''
          },
          {
            title: '同束平均值',
            key: 'avg',
            type: FormTypes.input,
            width: '200px',
            placeholder: '请输入${title}',
            defaultValue: ''
          },
          {
            title: '有效预应力相对偏差',
            key: 'piancha',
            type: FormTypes.input,
            width: '200px',
            placeholder: '请输入${title}',
            defaultValue: ''
          },
          {
            title: '同断面不均匀度',
            key: 'bjjd',
            type: FormTypes.input,
            width: '200px',
            placeholder: '请输入${title}',
            defaultValue: ''
          },
          {
            title: '千斤顶工作长度',
            key: 'qjdcd',
            type: FormTypes.input,
            width: '200px',
            placeholder: '请输入${title}',
            defaultValue: ''
          },
          {
            title: '钢绞线长度',
            key: 'gjxcd',
            type: FormTypes.input,
            width: '200px',
            placeholder: '请输入${title}',
            defaultValue: ''
          },
          {
            title: '弹性模量',
            key: 'txml',
            type: FormTypes.input,
            width: '200px',
            placeholder: '请输入${title}',
            defaultValue: ''
          },
          {
            title: '公称截面积',
            key: 'gcjmj',
            type: FormTypes.input,
            width: '200px',
            placeholder: '请输入${title}',
            defaultValue: ''
          },
          {
            title: '张拉日期',
            key: 'zlrq',
            type: FormTypes.input,
            width: '200px',
            placeholder: '请输入${title}',
            defaultValue: ''
          },
          {
            title: '曲线',
            key: 'qx',
            type: FormTypes.input,
            width: '200px',
            placeholder: '请输入${title}',
            defaultValue: ''
          },
          {
            title: '设计砼强度',
            key: 'qddj',
            type: FormTypes.input,
            width: '200px',
            placeholder: '请输入${title}',
            defaultValue: ''
          },
          {
            title: '是否合格',
            key: 'overLevel',
            type: FormTypes.inputNumber,
            width: '200px',
            placeholder: '请输入${title}',
            defaultValue: ''
          }
        ]
      },
      loading: false,
      visibleLine: false,
      charts: null,
      columns: [
        {
          title: '过程查看',
          key: 'qx',
          dataIndex: 'qx',
          align: 'center',
          width: '150px',
          scopedSlots: { customRender: 'qxLineSlot' }
        },
        {
          title: '束号',
          key: 'gsh',
          dataIndex: 'gsh',
          align: 'center',
          width: '150px'
        },
        {
          title: '孔号',
          key: 'kh',
          dataIndex: 'kh',
          align: 'center',
          width: '150px'
        },
        {
          title: '设计力值',
          key: 'sjlz',
          dataIndex: 'sjlz',
          align: 'center',
          width: '150px'
        },
        {
          title: '实测有效应力',
          key: 'yxlz',
          dataIndex: 'yxlz',
          align: 'center',
          width: '150px'
        },
        {
          title: '警戒值',
          key: 'jjz',
          dataIndex: 'jjz',
          align: 'center',
          width: '150px'
        },
        {
          title: '标准有效应力',
          key: 'bzyxlz',
          dataIndex: 'bzyxlz',
          align: 'center',
          width: '150px'
        },
        {
          title: '同束平均值',
          key: 'avg',
          dataIndex: 'avg',
          align: 'center',
          width: '150px'
        },
        {
          title: '有效预应力相对偏差',
          key: 'piancha',
          dataIndex: 'piancha',
          align: 'center',
          width: '150px'
        },
        {
          title: '同断面不均匀度',
          key: 'bjjd',
          dataIndex: 'bjjd',
          align: 'center',
          width: '150px'
        },
        {
          title: '千斤顶工作长度',
          key: 'qjdcd',
          dataIndex: 'qjdcd',
          align: 'center',
          width: '150px'
        },
        {
          title: '钢绞线长度',
          key: 'gjxcd',
          dataIndex: 'gjxcd',
          align: 'center',
          width: '150px'
        },
        {
          title: '弹性模量',
          key: 'txml',
          dataIndex: 'txml',
          align: 'center',
          width: '150px'
        },
        {
          title: '公称截面积',
          key: 'gcjmj',
          dataIndex: 'gcjmj',
          align: 'center',
          width: '150px'
        },
        {
          title: '张拉日期',
          key: 'zlrq',
          dataIndex: 'zlrq',
          align: 'center',
          width: '150px'
        },
        // {
        //   title: '曲线',
        //   width:"100px",
        //   key: 'qx',
        //   dataIndex:"qx",
        //   align: "center",
        //   width:"150px",
        //   scopedSlots: {customRender: "qxSlot"},
        // },
        {
          title: '设计砼强度',
          key: 'qddj',
          dataIndex: 'qddj',
          align: 'center',
          width: '150px'
        },
        {
          title: '是否合格',
          key: 'overLevel',
          dataIndex: 'overLevel',
          scopedSlots: { customRender: 'overLevel' },
          align: 'center',
          width: '150px'
        }
      ],
      url: {
        add: '/trmaoxiayuyinglim/trMaoxiayuyingliM/add',
        edit: '/trmaoxiayuyinglim/trMaoxiayuyingliM/edit',
        queryById: '/trmaoxiayuyinglim/trMaoxiayuyingliM/queryById',
        trMaoxiayuyingliS: {
          list: '/trmaoxiayuyinglim/trMaoxiayuyingliM/queryTrMaoxiayuyingliSByMainId1'
        }
      }
    }
  },
  props: {
    //表单禁用
    disabled: {
      type: Boolean,
      default: false,
      required: false
    }
  },
  computed: {
    formDisabled() {
      return this.disabled
    }
  },
  created() {
  },
  methods: {
    showModalLine(record) {
      this.visibleLine = true
      this.$nextTick(() => {
        this.getECharts(record.qx)
      })
      console.log(`record==========`, record)
    },
    handleOkLine(e) {
      console.log(e)
      this.visibleLine = false
    },
    getECharts(eData) {
      let arrAll = eData.split(';')
      arrAll = arrAll.map(e => e.split(','))
      let xData = arrAll.map(e => e[0])
      let dataList1 = arrAll.map(e => e[1])
      let dataList2 = arrAll.map(e => e[2])
      console.log(`arrAll=`, arrAll)
      // console.log(`xData=`,xData);
      // arrAll.forEach(e => {
      //   e = e.split(",");
      // });
      // let arrNew = arrAll.map((e)=>{ return e = e.split(",")})
      // console.log(`arrNew=`,arrNew);

      let option = {
        title: {
          // text: title,
          x: '4%',
          y: '8%',
          textStyle: {
            color: '#3c40d0',
            fontSize: '22'
          },
          subtextStyle: {
            color: '#90979c',
            fontSize: '16'
          }
        },
        // tooltip: {
        //   trigger: "axis",
        //   axisPointer: {
        //     type: "shadow",
        //     textStyle: {
        //       color: "#fff",
        //     },
        //   },
        // },
        grid: {
          borderWidth: 0,
          // top: 110,
          // bottom: 95,
          left: '5%',
          right: '5%',
          textStyle: {
            color: '#fff'
          }
        },
        legend: {
          // icon: 'circle',
          // top: '5%',
          // right: '5%',
          // itemWidth: 6,
          // itemGap: 20,
          // textStyle: {
          //     color: '#556677'
          // }
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: { // 坐标轴指示器，坐标轴触发有效
            type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
          }
          // formatter:function(params){
          //   console.log(params,'params-------------');
          //   let str1 = `${params[0].seriesName}${params[0].value}${params[0].value}`
          //   return eData[params[0].dataIndex].deviceId_dictText+
          //     '<br/>'+
          //     `<div  style='width:10px;height:10px;background:#fce630;display:inline-block;border-radius:10px;'></div> `+'温度(℃)'+': '+params[0].value
          // }
        },
        calculable: true,
        xAxis: [
          {
            type: 'category',
            name: '时间',
            nameLocation: 'middle',
            nameTextStyle: {
              padding: 15
            },
            axisLine: {
              lineStyle: {
                color: '#90979c'
              }
            },
            splitLine: {
              show: false
            },
            axisTick: {
              show: false
            },
            splitArea: {
              show: false
            },
            axisLabel: {
              // interval: 0,
            },
            data: xData
          }
        ],
        yAxis: [
          {
            type: 'value',
            name: '轴力(KN)',
            // nameLocation: "middle",
            // nameTextStyle: {
            //   padding: 45,
            // },
            splitLine: {
              show: false
            },
            axisLine: {
              lineStyle: {
                color: '#90979c'
              }
            },
            axisTick: {
              show: false
            },
            axisLabel: {
              interval: 0
            },
            splitArea: {
              show: false
            }
          },
          {
            type: 'value',
            name: '位移(mm)',
            // nameLocation: "middle",
            // nameTextStyle: {
            //   padding: 45,
            // },
            splitLine: {
              show: false
            },
            axisLine: {
              lineStyle: {
                color: '#90979c'
              }
            },
            axisTick: {
              show: false
            },
            axisLabel: {
              interval: 0
            },
            splitArea: {
              show: false
            }
          }
        ],

        series: [
          {
            name: '轴力(KN)',
            type: 'line',
            symbolSize: 10,
            symbol: 'circle',
            itemStyle: {
              normal: {
                // color: "rgba(252,230,48,1)",
                barBorderRadius: 0,
                label: {
                  show: false,
                  position: 'top',
                  formatter: function(p) {
                    return p.value > 0 ? p.value : ''
                  }
                }
              }
            },
            data: dataList1
          },
          {
            name: '位移(mm)',
            type: 'line',
            symbolSize: 10,
            symbol: 'circle',
            yAxisIndex: 1,
            itemStyle: {
              normal: {
                // color: "rgba(252,230,48,1)",
                barBorderRadius: 0,
                label: {
                  show: false,
                  position: 'top',
                  formatter: function(p) {
                    return p.value > 0 ? p.value : ''
                  }
                }
              }
            },
            data: dataList2
          }
        ]
      }
      this.charts = echarts.init(document.getElementById('EChartLine'))
      // this.charts = this.$refs.chart
      this.charts.setOption(option)
    },
    addBefore() {
      this.trMaoxiayuyingliSTable.dataSource = []
    },
    getAllTable() {
      let values = this.tableKeys.map(key => getRefPromise(this, key))
      return Promise.all(values)
    },
    /** 调用完edit()方法之后会自动调用此方法 */
    editAfter() {
      this.$nextTick(() => {
      })
      // 加载子表数据
      if (this.model.id) {
        let params = { id: this.model.id }
        this.requestSubTableData(this.url.trMaoxiayuyingliS.list, params, this.trMaoxiayuyingliSTable)
        setTimeout(() => {
          console.log(`this.trMaoxiayuyingliSTable===========`, this.trMaoxiayuyingliSTable)
        }, 3000)
      }
    },
    //校验所有一对一子表表单
    validateSubForm(allValues) {
      return new Promise((resolve, reject) => {
        Promise.all([]).then(() => {
          resolve(allValues)
        }).catch(e => {
          if (e.error === VALIDATE_NO_PASSED) {
            // 如果有未通过表单验证的子表，就自动跳转到它所在的tab
            this.activeKey = e.index == null ? this.activeKey : this.refKeys[e.index]
          } else {
            console.error(e)
          }
        })
      })
    },
    /** 整理成formData */
    classifyIntoFormData(allValues) {
      let main = Object.assign(this.model, allValues.formValue)
      return {
        ...main, // 展开
        trMaoxiayuyingliSList: allValues.tablesValue[0].values
      }
    },
    validateError(msg) {
      this.$message.error(msg)
    }

  }
}
</script>

<style lang="less" scoped>
// .Ysjldfh_tooltip_class {
//   min-width: 1200px !important;
// }
// ::v-deep .ant-tooltip-inner{
//   min-width: 1200px !important;
// }
#EChartLine {
  width: 100%;
  height: 550px;
}
</style>