<template>
  <a-spin :spinning="confirmLoading">

    <!--    <j-form-container :disabled="formDisabled">-->
    <!-- 主表单区域 -->
    <!--      <a-form :form="form" slot="detail">-->
    <!--        <a-row>-->
    <!--          <a-col :span="24" >-->
    <!--            <a-form-item label="唯一标识" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
    <!--              <a-input v-decorator="['syjid']" placeholder="唯一标识" ></a-input>-->
    <!--            </a-form-item>-->
    <!--          </a-col>-->
    <!--          <a-col :span="24" >-->
    <!--            <a-form-item label="设备名称" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
    <!--              <a-input v-decorator="['sbbh']" placeholder="请输入设备编号" ></a-input>-->
    <!--            </a-form-item>-->
    <!--          </a-col>-->
    <!--          <a-col :span="24" >-->
    <!--            <a-form-item label="试验类型" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
    <!--              <a-input-number v-decorator="['sylx']" placeholder="请输入实验类型" style="width: 100%" />-->
    <!--            </a-form-item>-->
    <!--          </a-col>-->
    <!--          <a-col :span="24" >-->
    <!--            <a-form-item label="试件编号" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
    <!--              <a-input-number v-decorator="['sjbh']" placeholder="请输入试件编号" style="width: 100%" />-->
    <!--            </a-form-item>-->
    <!--          </a-col>-->
    <!--          <a-col :span="24" >-->
    <!--            <a-form-item label="制件日期" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
    <!--              <a-input v-decorator="['zzrq']" placeholder="请输入制件日期" ></a-input>-->
    <!--            </a-form-item>-->
    <!--          </a-col>-->
    <!--          &lt;!&ndash;          <a-col :span="24" >&ndash;&gt;-->
    <!--          &lt;!&ndash;            <a-form-item label="客户端编号" :labelCol="labelCol" :wrapperCol="wrapperCol">&ndash;&gt;-->
    <!--          &lt;!&ndash;              <a-input v-decorator="['clientNo']" placeholder="请输入客户端编号" ></a-input>&ndash;&gt;-->
    <!--          &lt;!&ndash;            </a-form-item>&ndash;&gt;-->
    <!--          &lt;!&ndash;          </a-col>&ndash;&gt;-->
    <!--          <a-col :span="24" >-->
    <!--            <a-form-item label="试验日期" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
    <!--              <a-input v-decorator="['syrq']" placeholder="请输入实验日期" ></a-input>-->
    <!--            </a-form-item>-->
    <!--          </a-col>-->
    <!--          &lt;!&ndash;          <a-col :span="24" >&ndash;&gt;-->
    <!--          &lt;!&ndash;            <a-form-item label="拌合时间" :labelCol="labelCol" :wrapperCol="wrapperCol">&ndash;&gt;-->
    <!--          &lt;!&ndash;              <a-input-number v-decorator="['banheshijian']" placeholder="请输入拌合时间" style="width: 100%" />&ndash;&gt;-->
    <!--          &lt;!&ndash;            </a-form-item>&ndash;&gt;-->
    <!--          &lt;!&ndash;          </a-col>&ndash;&gt;-->
    <!--          &lt;!&ndash;          <a-col :span="24" >&ndash;&gt;-->
    <!--          &lt;!&ndash;            <a-form-item label="是否删除" :labelCol="labelCol" :wrapperCol="wrapperCol">&ndash;&gt;-->
    <!--          &lt;!&ndash;              <a-input v-decorator="['isdel']" placeholder="请输入是否删除" ></a-input>&ndash;&gt;-->
    <!--          &lt;!&ndash;            </a-form-item>&ndash;&gt;-->
    <!--          &lt;!&ndash;          </a-col>&ndash;&gt;-->
    <!--          <a-col :span="24" >-->
    <!--            <a-form-item label="龄期" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
    <!--              <a-input v-decorator="['lq']" placeholder="请输入龄期" ></a-input>-->
    <!--            </a-form-item>-->
    <!--          </a-col>-->
    <!--          <a-col :span="24" >-->
    <!--            <a-form-item label="试件尺寸" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
    <!--              <a-input v-decorator="['sjcc']" placeholder="请输入试件尺寸" ></a-input>-->
    <!--            </a-form-item>-->
    <!--          </a-col>-->
    <!--          &lt;!&ndash;          <a-col :span="24" >&ndash;&gt;-->
    <!--          &lt;!&ndash;            <a-form-item label="时间戳" :labelCol="labelCol" :wrapperCol="wrapperCol">&ndash;&gt;-->
    <!--          &lt;!&ndash;              <a-input v-decorator="['ts']" placeholder="请输入时间戳" ></a-input>&ndash;&gt;-->
    <!--          &lt;!&ndash;            </a-form-item>&ndash;&gt;-->
    <!--          &lt;!&ndash;          </a-col>&ndash;&gt;-->
    <!--          <a-col :span="24" >-->
    <!--            <a-form-item label="试件数量" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
    <!--              <a-input v-decorator="['sjsl']" placeholder="请输入试件数量" ></a-input>-->
    <!--            </a-form-item>-->
    <!--          </a-col>-->
    <!--          <a-col :span="24" >-->
    <!--            <a-form-item label="设计强度" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
    <!--              <a-input v-decorator="['sjqd']" placeholder="请输入设计强度" ></a-input>-->
    <!--            </a-form-item>-->
    <!--          </a-col>-->
    <!--          &lt;!&ndash;          <a-col :span="24" >&ndash;&gt;-->
    <!--          &lt;!&ndash;            <a-form-item label="是否统计" :labelCol="labelCol" :wrapperCol="wrapperCol">&ndash;&gt;-->
    <!--          &lt;!&ndash;              <a-input-number v-decorator="['istongji']" placeholder="请输入是否统计" style="width: 100%" />&ndash;&gt;-->
    <!--          &lt;!&ndash;            </a-form-item>&ndash;&gt;-->
    <!--          &lt;!&ndash;          </a-col>&ndash;&gt;-->
    <!--          &lt;!&ndash;          <a-col :span="24" >&ndash;&gt;-->
    <!--          &lt;!&ndash;            <a-form-item label="时间超标" :labelCol="labelCol" :wrapperCol="wrapperCol">&ndash;&gt;-->
    <!--          &lt;!&ndash;              <a-input-number v-decorator="['timechaobiao']" placeholder="请输入时间超标" style="width: 100%" />&ndash;&gt;-->
    <!--          &lt;!&ndash;            </a-form-item>&ndash;&gt;-->
    <!--          &lt;!&ndash;          </a-col>&ndash;&gt;-->
    <!--          &lt;!&ndash;          <a-col :span="24" >&ndash;&gt;-->
    <!--          &lt;!&ndash;            <a-form-item label="材料超标" :labelCol="labelCol" :wrapperCol="wrapperCol">&ndash;&gt;-->
    <!--          &lt;!&ndash;              <a-input-number v-decorator="['cailiaochaobiao']" placeholder="请输入材料超标" style="width: 100%" />&ndash;&gt;-->
    <!--          &lt;!&ndash;            </a-form-item>&ndash;&gt;-->
    <!--          &lt;!&ndash;          </a-col>&ndash;&gt;-->
    <!--          &lt;!&ndash;          <a-col :span="24" >&ndash;&gt;-->
    <!--          &lt;!&ndash;            <a-form-item label="是否判断超标" :labelCol="labelCol" :wrapperCol="wrapperCol">&ndash;&gt;-->
    <!--          &lt;!&ndash;              <a-input-number v-decorator="['alertstate']" placeholder="请输入是否判断超标" style="width: 100%" />&ndash;&gt;-->
    <!--          &lt;!&ndash;            </a-form-item>&ndash;&gt;-->
    <!--          &lt;!&ndash;          </a-col>&ndash;&gt;-->
    <!--          <a-col :span="24" >-->
    <!--            <a-form-item label="强度代表值" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
    <!--              <a-input v-decorator="['qddbz']" placeholder="请输入强度代表值" ></a-input>-->
    <!--            </a-form-item>-->
    <!--          </a-col>-->
    <!--          &lt;!&ndash;          <a-col :span="24" >&ndash;&gt;-->
    <!--          &lt;!&ndash;            <a-form-item label="备用1" :labelCol="labelCol" :wrapperCol="wrapperCol">&ndash;&gt;-->
    <!--          &lt;!&ndash;              <a-input v-decorator="['beiy1']" placeholder="请输入备用1" ></a-input>&ndash;&gt;-->
    <!--          &lt;!&ndash;            </a-form-item>&ndash;&gt;-->
    <!--          &lt;!&ndash;          </a-col>&ndash;&gt;-->
    <!--          &lt;!&ndash;          <a-col :span="24" >&ndash;&gt;-->
    <!--          &lt;!&ndash;            <a-form-item label="备用2" :labelCol="labelCol" :wrapperCol="wrapperCol">&ndash;&gt;-->
    <!--          &lt;!&ndash;              <a-input v-decorator="['beiy2']" placeholder="请输入备用2" ></a-input>&ndash;&gt;-->
    <!--          &lt;!&ndash;            </a-form-item>&ndash;&gt;-->
    <!--          &lt;!&ndash;          </a-col>&ndash;&gt;-->
    <!--          &lt;!&ndash;          <a-col :span="24" >&ndash;&gt;-->
    <!--          &lt;!&ndash;            <a-form-item label="备用3" :labelCol="labelCol" :wrapperCol="wrapperCol">&ndash;&gt;-->
    <!--          &lt;!&ndash;              <a-input v-decorator="['beiy3']" placeholder="请输入备用3" ></a-input>&ndash;&gt;-->
    <!--          &lt;!&ndash;            </a-form-item>&ndash;&gt;-->
    <!--          &lt;!&ndash;          </a-col>&ndash;&gt;-->
    <!--          <a-col :span="24" >-->
    <!--            <a-form-item label="判定结果" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
    <!--              <a-input v-decorator="['pdjg']" placeholder="请输入判定结果" ></a-input>-->
    <!--            </a-form-item>-->
    <!--          </a-col>-->

    <!--          <a-col :span="24" >-->
    <!--            <a-form-item label="操作人员" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
    <!--              <a-input v-decorator="['czry']" placeholder="请输入操作人员" ></a-input>-->
    <!--            </a-form-item>-->
    <!--          </a-col>-->

    <!--          <a-col :span="24" >-->
    <!--            <a-form-item label="工程名称" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
    <!--              <a-input v-decorator="['cjmc']" placeholder="请输入工程名称" ></a-input>-->
    <!--            </a-form-item>-->
    <!--          </a-col>-->
    <!--        </a-row>-->
    <!--      </a-form>-->
    <!--    </j-form-container>-->
    <!-- 子表单区域 -->
    <!--    <a-tabs v-model="activeKey" @change="handleChangeTabs">-->
    <!--      <a-tab-pane tab="压力机表信息" :key="refKeys[0]" :forceRender="true">-->
    <j-editable-table
      :ref="refKeys[0]"
      :loading="FYalijiTable.loading"
      :columns="FYalijiTable.columns"
      :dataSource="FYalijiTable.dataSource"
      :maxHeight="300"
      :disabled="formDisabled"
      :rowNumber="true"
      :rowSelection="true"
      :actionButton="false"
    >
      <template v-slot:action="props">
        <a-tag color="green" v-if="props.value == '0'">合格</a-tag>
        <a-tag color="orange" v-if="props.value == '1'">初级超标</a-tag>
        <a-tag color="yellow" v-if="props.value == '2'">中级超标</a-tag>
        <a-tag color="red" v-if="props.value == '3'">高级超标</a-tag>
      </template>
      <template v-slot:xh="props">
        <a-button type="primary" @click="showPic(props)">查看</a-button>
      </template>
    </j-editable-table>
    <div>
      <a-row>
        <a-col :span="24">
          <div class="imgButton" v-if="imgURl1.length > 0">
            <div class="viewerBox">
              <viewer v-if="imgURl1[0]">
                <!-- <img :src="imgURl1[0]" style="width: 50px; height: 50px"/> -->
                <!-- <div>开始图片</div> -->
              </viewer>
              <viewer v-if="imgURl1[1]">
                <!-- <img :src="imgURl1[1]" style="width: 50px; height: 50px"/> -->
                <!-- <div>破碎图片</div> -->
              </viewer>
            </div>
          </div>
          <div v-if="dataSource1.length > 0">
            <!-- <LineChartMultid1
              :title="dataSource1[0].name"
              :data-source="dataSource1"
            ></LineChartMultid1> -->
            <LineChartMultid
              :title="dataSource1[0].name"
              :data-source="dataList"
            ></LineChartMultid>
          </div>
        </a-col>
        <a-col :span="24">
          <div class="imgButton" v-if="imgURl2.length > 0">
            <div class="viewerBox">
              <viewer v-if="imgURl2[0]">
                <!-- <img :src="imgURl2[0]" style="width: 50px; height: 50px"/> -->
                <!-- <div>开始图片</div> -->
              </viewer>
              <viewer v-if="imgURl2[1]">
                <!-- <img :src="imgURl2[1]" style="width: 50px; height: 50px"/> -->
                <!-- <div>破碎图片</div> -->
              </viewer>
            </div>
          </div>
          <!-- <div v-if="dataSource2.length>0">
            <LineChartMultid :title="dataSource2[0].name" :data-source="dataSource2"></LineChartMultid>
          </div> -->
        </a-col>
        <a-col :span="24">
          <div class="imgButton" v-if="imgURl3.length > 0">
            <div class="viewerBox">
              <viewer v-if="imgURl3[0]">
                <!-- <img :src="imgURl3[0]" style="width: 50px; height: 50px"/> -->
                <!-- <div>开始图片</div> -->
              </viewer>
              <viewer v-if="imgURl3[1]">
                <!-- <img :src="imgURl3[1]" style="width: 50px; height: 50px"/> -->
                <!-- <div>破碎图片</div> -->
              </viewer>
            </div>
          </div>
          <!-- <div v-if="dataSource3.length>0">
            <LineChartMultid :title="dataSource3[0].name" :data-source="dataSource3"></LineChartMultid>
          </div> -->
        </a-col>
        <a-col :span="24">
          <div class="imgButton" v-if="imgURl4.length > 0">
            <div class="viewerBox">
              <viewer v-if="imgURl4[0]">
                <!-- <img :src="imgURl4[0]" style="width: 50px; height: 50px"/> -->
                <!-- <div>开始图片</div> -->
              </viewer>
              <viewer v-if="imgURl4[1]">
                <!-- <img :src="imgURl4[1]" style="width: 50px; height: 50px"/> -->
                <!-- <div>破碎图片</div> -->
              </viewer>
            </div>
          </div>
          <!-- <div v-if="dataSource4.length>0">
            <LineChartMultid :title="dataSource4[0].name" :data-source="dataSource4"></LineChartMultid>
          </div> -->
        </a-col>
        <a-col :span="24">
          <div class="imgButton" v-if="imgURl5.length > 0">
            <div class="viewerBox">
              <viewer v-if="imgURl5[0]">
                <!-- <img :src="imgURl5[0]" style="width: 50px; height: 50px"/> -->
                <!-- <div>开始图片</div> -->
              </viewer>
              <viewer v-if="imgURl5[1]">
                <!-- <img :src="imgURl5[1]" style="width: 50px; height: 50px"/> -->
                <!-- <div>破碎图片</div> -->
              </viewer>
            </div>
          </div>
          <!-- <div v-if="dataSource5.length>0">
            <LineChartMultid :title="dataSource5[0].name" :data-source="dataSource5"></LineChartMultid>
          </div> -->
        </a-col>
        <a-col :span="24">
          <div class="imgButton" v-if="imgURl6.length > 0">
            <div class="viewerBox">
              <viewer v-if="imgURl6[0]">
                <!-- <img :src="imgURl6[0]" style="width: 50px; height: 50px"/> -->
                <!-- <div>开始图片</div> -->
              </viewer>
              <viewer v-if="imgURl6[1]">
                <!-- <img :src="imgURl6[1]" style="width: 50px; height: 50px"/> -->
                <!-- <div>破碎图片</div> -->
              </viewer>
            </div>
          </div>
          <!-- <div v-if="dataSource6.length>0">
            <LineChartMultid :title="dataSource6[0].name" :data-source="dataSource6"></LineChartMultid>
          </div> -->
        </a-col>
      </a-row>
      <span v-has="'syj:lyjl'"  slot="action" slot-scope="text, record">
        <a>查看</a>
      </span>
    </div>
    <show-pic ref="modalForm" @ok="modalFormOk"></show-pic>
    <!--      </a-tab-pane>-->
    <!--    </a-tabs>-->
  </a-spin>
</template>

<script>
import pick from 'lodash.pick'
import { getAction } from '@/api/manage'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import { FormTypes, getRefPromise } from '@/utils/JEditableTableUtil'
import { JEditableTableMixin } from '@/mixins/JEditableTableMixin'
import JEditableTable from '@/components/jeecg/JEditableTable'
import { validateDuplicateValue } from '@/utils/util'
import JFormContainer from '@/components/jeecg/JFormContainer'
import JDate from '@/components/jeecg/JDate'
import JDictSelectTag from '@/components/dict/JDictSelectTag'
// import LineChartMultid1 from "@comp/chart/YaLijiLzqx";
import LineChartMultid from '@comp/chart/YaLijiLzqxNew'
import showPic from './showPic'

export default {
  name: 'SyyLjBasesForm',
  mixins: [JEditableTableMixin,JeecgListMixin],
  components: {
    JFormContainer,
    JDate,
    JDictSelectTag,
    LineChartMultid,
    JEditableTable,
    showPic
  },
  data() {
    return {
      dataSource1: [],
      dataSource2: [],
      dataSource3: [],
      dataSource4: [],
      dataSource5: [],
      dataSource6: [],
      dataList: {
        dataSource1: [],
        dataSource2: [],
        dataSource3: [],
        dataSource4: [],
        dataSource5: [],
        dataSource6: [],
      },
      imgURl1: [],
      imgURl2: [],
      imgURl3: [],
      imgURl4: [],
      imgURl5: [],
      imgURl6: [],
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
      validatorRules: {},
      refKeys: ['SyyljBases'],
      tableKeys: ['SyyljBases'],
      activeKey: 'SyyljBases',
      // 压力机数据信息
      FYalijiTable: {
        loading: false,
        dataSource: [],
        columns: [
          {
            title: '试件序号',
            key: 'sjxh',
            type: FormTypes.normal,
            width: '200px',
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
          // {
          //   title: '制件编号',
          //   key: 'zzjj',
          //   type: FormTypes.normal,
          //   width:"200px",
          //   placeholder: '请输入${title}',
          //   defaultValue:'',
          // },
          {
            title: '抗压力值',
            key: 'kylz',
            type: FormTypes.normal,
            width: '200px',
            placeholder: '请输入${title}',
            defaultValue: '',
          },
          {
            title: '抗压强度',
            key: 'kyqd',
            type: FormTypes.normal,
            width: '200px',
            placeholder: '请输入${title}',
            defaultValue: '',
          },
          {
            title: '试验时间',
            key: 'sysj',
            type: FormTypes.normal,
            width: '200px',
            placeholder: '请输入${title}',
            defaultValue: '',
          },

          {
            title: '完成时间',
            type: FormTypes.normal,
            key: 'wcsj',
            width: '200px',
            placeholder: '请输入${title}',
            defaultValue: '',
          },
          {
            title: '留样记录',
            slotName:'xh',
            key:'xh',
            width: '200px',
            align: 'center',
            dataIndex: 'sjxh',
            type: FormTypes.slot,
          },
        ],
      },
      url: {
        add: '/syj/tSyjzb/add',
        edit: '/syj/tSyjzb/edit',
        queryById: '/syj/tSyjzb/queryById',
        SyyljBases: {
          list: '/syj/tSyjzb/selectById',
        },
        list: '/syj/tSyjzb/selectById',
      },
    }
  },
  props: {
    //流程表单data
    formData: {
      type: Object,
      default: () => {
      },
      required: false,
    },
    //表单模式：false流程表单 true普通表单
    formBpm: {
      type: Boolean,
      default: false,
      required: false,
    },
    //表单禁用
    disabled: {
      type: Boolean,
      default: false,
      required: false,
    },
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
    },
  },
  created() {
    //如果是流程中表单，则需要加载流程表单data
    this.showFlowData()
  },
  methods: {
    showPic(props){
      this.$refs.modalForm.visible=true
      this.$refs.modalForm.edit(props);
    },
    addBefore() {
      this.form.resetFields()
      this.FYalijiTable.dataSource = []
    },
    getAllTable() {
      let values = this.tableKeys.map((key) => getRefPromise(this, key))
      return Promise.all(values)
    },
    /** 调用完edit()方法之后会自动调用此方法 */
    editAfter() {
      let fieldval = pick(this.model) //,'syjid','sbbh','sylx','sjbh','zzrq','syrq','lq','sjcc','sjsl','sjqd','qddbz','pdjg','czry','cjmc'
      this.$nextTick(() => {
        this.form.setFieldsValue(fieldval)
      })
      // 加载子表数据
      if (this.model.syjid) {
        let params = { syjid: this.model.syjid }
        this.requestSubTableData(this.url.SyyljBases.list, params, this.FYalijiTable)
        this.lzqx() //力值过程值曲线图
      }
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
      this.form.setFieldsValue(pick(row)) //,'syjid','sbbh','sylx','sjbh','zzrq','syrq','lq','sjcc','sjsl','sjqd','qddbz','pdjg','czry','cjmc'
    },
    lzqx: function () {
      //力值过程值曲线图
      let params = { syjid: this.model.syjid }
      getAction(this.url.list, params).then((res) => {
        this.dataSource1 = []
        this.dataSource2 = []
        this.dataSource3 = []
        this.dataSource4 = []
        this.dataSource5 = []
        this.dataSource6 = []
        if (res.success) {
          let data = res.result
          console.log(data, 'data data')
          if (data.length > 0) {
            for (let i = 0; i < data.length; i++) {
              var yskylz1 = data[i].yskylz.split(',')
              var sjgc1 = data[i].sjgc.split(',')
              // console.log(yskylz1);
              // console.log(sjgc1);
              for (let j = 0; j < yskylz1.length; j++) {
                if (i === 0) {
                  this.dataSource1.push({
                    type: parseFloat(sjgc1[j]),
                    抗压力值: parseFloat(yskylz1[j]),
                    name: '试件序号' + data[i].sjxh,
                  })
                  if (this.imgURl1.length < 2) {
                    this.imgURl1.push(data[i].spic, data[i].pspic)
                  }
                }
                if (i === 1) {
                  this.dataSource2.push({
                    type: parseFloat(sjgc1[j]),
                    抗压力值: parseFloat(yskylz1[j]),
                    name: '试件序号' + data[i].sjxh,
                  })
                  if (this.imgURl2.length < 2) {
                    this.imgURl2.push(data[i].spic, data[i].pspic)
                  }
                }
                if (i === 2) {
                  this.dataSource3.push({
                    type: parseFloat(sjgc1[j]),
                    抗压力值: parseFloat(yskylz1[j]),
                    name: '试件序号' + data[i].sjxh,
                  })
                  if (this.imgURl3.length < 2) {
                    this.imgURl3.push(data[i].spic, data[i].pspic)
                  }
                }
                if (i === 3) {
                  this.dataSource4.push({
                    type: parseFloat(sjgc1[j]),
                    抗压力值: parseFloat(yskylz1[j]),
                    name: '试件序号' + data[i].sjxh,
                  })
                  if (this.imgURl4.length < 2) {
                    this.imgURl4.push(data[i].spic, data[i].pspic)
                  }
                }
                if (i === 4) {
                  this.dataSource5.push({
                    type: parseFloat(sjgc1[j]),
                    抗压力值: parseFloat(yskylz1[j]),
                    name: '试件序号' + data[i].sjxh,
                  })
                  if (this.imgURl5.length < 2) {
                    this.imgURl5.push(data[i].spic, data[i].pspic)
                  }
                }
                if (i === 5) {
                  this.dataSource6.push({
                    type: parseFloat(sjgc1[j]),
                    抗压力值: parseFloat(yskylz1[j]),
                    name: '试件序号' + data[i].sjxh,
                  })
                  if (this.imgURl6.length < 2) {
                    this.imgURl6.push(data[i].spic, data[i].pspic)
                  }
                }
              }
            }
            this.dataList.dataSource1 = this.dataSource1
            this.dataList.dataSource2 = this.dataSource2
            this.dataList.dataSource3 = this.dataSource3
            this.dataList.dataSource4 = this.dataSource4
            this.dataList.dataSource5 = this.dataSource5
            this.dataList.dataSource6 = this.dataSource6
            // console.log(
            //   this.dataList.dataSource1,
            //   "this.dataList.dataSource1  )))))))))))"
            // );
          } else {
            this.$message.error('该设备的抗压力值没有数据')
          }
        }
      })
    },
  },
}
</script>

<style lang="less" scoped>
.imgButton {
  margin-top: 30px;
  position: absolute;
  left: 7%;
  top: -2%;

  .viewerBox {
    display: flex;

    img {
      margin-right: 20px;
    }
  }
}
</style>
