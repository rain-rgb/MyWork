<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form :form="form" slot="detail">
<!--        <a-row>-->
<!--          <a-col :span="12">-->
<!--            <a-form-item label="唯一ID" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['batchNo', validatorRules.batchNo]" placeholder="请输入唯一ID"  ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="12">-->
<!--            <a-form-item label="设备编号" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['shebeiNo']" placeholder="请输入设备编号"  ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          </a-row>-->
<!--        <a-row>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="施工部位" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['jobLocation']" placeholder="请输入施工部位"  ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
        <a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item label="切面名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['sectionName']" placeholder="请输入切面名称"  ></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="切面里程" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['sectionLength']" placeholder="请输入切面里程"  ></a-input>
              </a-form-item>
            </a-col>
            </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item label="切面宽度" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['sectionWidth']" placeholder="请输入切面宽度"  ></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="开挖方式" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['excavationType']" placeholder="请输入开挖方式"  ></a-input>
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item label="围岩级别" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['wallRockLevel']" placeholder="请输入围岩级别"  ></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="测点" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['measuringPoint']" placeholder="请输入测点"  ></a-input>
              </a-form-item>
            </a-col>
          </a-row>

<!--          <a-col :span="24">-->
<!--            <a-form-item label="测量值（单位m）" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input-number v-decorator="['meauringNumber']" placeholder="请输入测量值（单位m）" style="width: 100%" />-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="单次变形量（单位mm）" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input-number v-decorator="['onceDeformation']" placeholder="请输入单次变形量（单位mm）" style="width: 100%" />-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="间隔时间  （单位天）" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input-number v-decorator="['intervalTime']" placeholder="请输入间隔时间  （单位天）" style="width: 100%" />-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="变形速率" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input-number v-decorator="['deformationRate']" placeholder="请输入变形速率" style="width: 100%" />-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="单次是否超标 (0:未超标，1:超标)" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input-number v-decorator="['onceStatus']" placeholder="请输入单次是否超标 (0:未超标，1:超标)" style="width: 100%" />-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="累计是否超标  (0:未超标，1:超标)" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input-number v-decorator="['accumulatedStatus']" placeholder="请输入累计是否超标  (0:未超标，1:超标)" style="width: 100%" />-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="累计变形量单位（mm）" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input-number v-decorator="['accumulatedDeformation']" placeholder="请输入累计变形量单位（mm）" style="width: 100%" />-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="是否判断超标并报警0为没有判断过1为已判断并且进行过了生产统计      40为数据异常" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input-number v-decorator="['alertstate']" placeholder="请输入是否判断超标并报警0为没有判断过1为已判断并且进行过了生产统计      40为数据异常" style="width: 100%" />-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="测量时间" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <j-date placeholder="请选择测量时间" v-decorator="['birthTime']" :trigger-change="true" style="width: 100%" />-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="数据上传时间" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <j-date placeholder="请选择数据上传时间" v-decorator="['uploadTime']" :trigger-change="true" style="width: 100%" />-->
<!--            </a-form-item>-->
<!--          </a-col>-->
          <a-row>
            <a-col :span="12">
              <a-form-item label="状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['isUse_dictText']" placeholder="请输入状态" style="width: 100%" />
              </a-form-item>
            </a-col>
            <!--          <a-col :span="24">-->
            <!--            <a-form-item label="报警级别(0,代表合格，1代表黄色报警，21代表红色报警)" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
            <!--              <a-input-number v-decorator="['callLevel']" placeholder="请输入报警级别(0,代表合格，1代表黄色报警，21代表红色报警)" style="width: 100%" />-->
            <!--            </a-form-item>-->
            <!--          </a-col>-->
<!--            <a-col :span="12">-->
<!--              <a-form-item label="报警数量" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <a-input-number v-decorator="['callLevel']" placeholder="请输入报警级别(0,代表合格，1代表黄色报警，21代表红色报警)" style="width: 100%" />-->
<!--              </a-form-item>-->
<!--            </a-col>-->
<!--          </a-row>-->

<!--          <a-col :span="24">-->
<!--            <a-form-item label="检测类型（0:速率，1累计）" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input-number v-decorator="['superviseType']" placeholder="请输入检测类型（0:速率，1累计）" style="width: 100%" />-->
<!--            </a-form-item>-->
<!--          </a-col>-->
          <a-col :span="12">
            <a-form-item label="报警数量" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['callSum']" placeholder="请输入callSum" style="width: 100%" />
            </a-form-item>
          </a-col>
          </a-row>
<!--          <a-col :span="24">-->
<!--            <a-form-item label="间隔时间(距离初次测试时间)" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input-number v-decorator="['intervalFirstTime']" placeholder="请输入间隔时间(距离初次测试时间)" style="width: 100%" />-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="测量状态（0：正常 ,1：重新布设）" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['measuringStatus']" placeholder="请输入测量状态（0：正常 ,1：重新布设）"  ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="超标问题中文描述" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['problemRecord']" placeholder="请输入超标问题中文描述"  ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="临建再统计后的测点" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['measuringPointLj']" placeholder="请输入临建再统计后的测点"  ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->


          <a-col v-if="showFlowSubmitButton" :span="24" style="text-align: center">
            <a-button @click="submitForm">提 交</a-button>
          </a-col>
        </a-row>
      </a-form>
    </j-form-container>

      测点：<a-select
        label-in-value
        :default-value="measuringPoint[0]"
        style="width: 120px"
        v-if="measuringPoint.length !== 0"
        @change="handleChange">
        <a-select-option v-for="(item, index) in measuringPoint"  :key="item.key">
          {{item.label}}
        </a-select-option>
      </a-select>




    <a-tabs default-active-key="1" :active-key="tabskey" size="large" :tab-bar-style="{marginBottom: '24px', paddingLeft: '16px'}" @change="qiehuan">
      <a-tab-pane loading="true" tab="变形曲线" key="1">
        <a-row>
          <a-col :xl="24" :lg="12" :md="12" :sm="24" :xs="24">
            <a-col :sm="24" :md="12" :xl="6" :style="{ marginBottom: '24px' }">
            </a-col>
            <div>
              <LineChartMultid  :data-source="dataSource"/>
            </div>
          </a-col>
          <a-col :xl="24" :lg="12" :md="12" :sm="24" :xs="24">
            <a-col :sm="24" :md="12" :xl="6" :style="{ marginBottom: '24px' }">
            </a-col>
            <div>
              <LineChartMultid1  :data-source="dataSource1"/>
            </div>
          </a-col>
          <a-col :xl="24" :lg="12" :md="12" :sm="24" :xs="24">
            <a-col :sm="24" :md="12" :xl="6" :style="{ marginBottom: '24px' }">
            </a-col>
            <div>
              <LineChartMultid2  :data-source="dataSource2"/>
            </div>
          </a-col>
        </a-row>
      </a-tab-pane>

      <a-tab-pane loading="true" tab="测量数据" key="2">
        <a-row>
          <a-col :xl="24" :lg="12" :md="12" :sm="24" :xs="24">
            <j-editable-table
              :loading="clsj.loading"
              :columns="clsj.columns"
              :dataSource="clsj.dataSource"
              :maxHeight="300"
              :disabled="formDisabled"
              :rowNumber="true"
              :rowSelection="true"
              :actionButton="false"
              bordered>
            </j-editable-table>
            <!--&lt;!&ndash;            <<bar title="销售额排行" :dataSource="barData"/>&ndash;&gt;-->

          </a-col>
        </a-row>
      </a-tab-pane>

      <a-tab-pane loading="true" tab="回归分析" key="3">
        <a-row>
          <a-col :xl="24" :lg="12" :md="12" :sm="24" :xs="24">
            <div>
              <LineChartMultid3  :data-source="dataSource3"/>
            </div>
          </a-col>
        </a-row>
      </a-tab-pane>



    </a-tabs>

  </a-spin>
</template>

<script>

  import { httpAction, getAction } from '@api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import JFormContainer from '@comp/jeecg/JFormContainer'
  import JDate from '@comp/jeecg/JDate'
  import {FormTypes} from "@/utils/JEditableTableUtil";
  import JeecgOrderMainListForJEditableTable from "@views/jeecg/JeecgOrderMainListForJEditableTable";
  import { JeecgListMixin } from '@/mixins/JeecgListMixin';
  import {mixinDevice} from "@/utils/mixin";
  import LineChartMultid from '@comp/chart/WeiyanBasedcbxl'
  import LineChartMultid1 from '@comp/chart/WeiyanBasebxsl'
  import LineChartMultid2 from '@comp/chart/WeiyanBaseljbx'
  import LineChartMultid3 from '@comp/chart/WeiyanBasehuigui'

  export default {
    name: 'WeiyanBaseForm',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      JeecgOrderMainListForJEditableTable,
      JFormContainer,
      JDate,
      LineChartMultid,
      LineChartMultid1,
      LineChartMultid2,
      LineChartMultid3,
    },
    props: {
      //流程表单data
        formData: {
        type: Object,
        default: ()=>{},
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
    data () {
      return {
        tabskey:'1',
        form: this.$form.createForm(this),
        model: {},
        Date:{},
        cedian:'',
        dataSource:[],
        dataSource1:[],
        dataSource2:[],
        measuringPoint:[],
        dataSource3:[],

        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        confirmLoading: false,
        // validatorRules: {
        //   batchNo: {
        //     rules: [
        //       { required: true, message: '请输入唯一ID!'},
        //     ]
        //   },
        // },
        refKeys: ['clsj',],
        tableKeys: ['clsj',],
        activeKey: 'clsj',
        // 测量数据表
        clsj: {
          loading: false,
          dataSource: [],
          columns: [

            {
              title: '测量时间',
              key: 'birthTime',
              type: FormTypes.normal,
              width:"150px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '上传时间',
              key: 'uploadTime',
              type: FormTypes.normal,
              width:"150px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '测量值（m）',
              key: 'measuringNumber',
              type: FormTypes.normal,
              width:"150px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '单次变形量（mm）',
              key: 'onceDeformation',
              type: FormTypes.normal,
              width:"150px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '累计变形（mm）',
              key: 'accumulatedDeformation',
              type: FormTypes.normal,
              width:"150px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '时隔天数（天）',
              type:FormTypes.normal,
              slotName:'action',
              key: 'intervalTime',
              width:"150px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '变形速率（mm/d）',
              type:FormTypes.normal,
              slotName:'action',
              key: 'deformationRate',
              width:"150px",
              placeholder: '请输入${title}',
              defaultValue:'',
            }
          ]
        },

        url: {
          add: "/weiyan/weiyanBase/add",
          edit: "/weiyan/weiyanBase/edit",
          queryById: "/weiyan/weiyanBase/queryById",
          clsj:"/weiyan/weiyanBase/list1",
          cdcx:"/weiyan/weiyanBase/list2"
        }
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
      // this.clsjs();

      //this.bxsl();
      //this.ljbx();
      // setTimeout(this.cdcxs(), 1000)
    },
    mounted() {

    },
    methods: {
      qiehuan(key){
        // console.log(key)
        this.tabskey = key
        this.clsjs();
        this.dcbxl();//单次变形量曲线图
        this.bxsl();//变形速率曲线图
        this.ljbx();//累计变形曲线图
        this.huigui();//回归分析
      },

      handleChange(e) {
       // console.log(value); // { key: "lucy", label: "Lucy (101)" }
        this.cedian=e.key;
        console.log("this.cedian",this.cedian)
        this.clsjs();
        this.dcbxl();//单次变形量曲线图
        this.bxsl();//变形速率曲线图
        this.ljbx();//累计变形曲线图
        this.huigui();//回归分析
      },

      add () {
        this.edit({});
      },
      edit (record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        const that = this
        this.Date=this.model;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'sectionName','sectionLength','sectionWidth','excavationType','wallRockLevel','measuringPoint','isUse_dictText','callSum'))
          that.tabskey = '1'
        })
        this.cdcx();//测点筛选
      },
      clsjs:function (){  //测量数据
        // console.log(this.model)
        this.clsj.dataSource=[]
        let params = {measuringPoint:this.cedian,sectionName:this.model.sectionName};
        getAction(this.url.clsj,params).then((res)=>{
            this.clsj.dataSource=[];
            if(res.success)  {
              //console.log(res)
              this.clsj.dataSource=res.result;
            }else{
              this.$message.error("暂无测量数据！")
            }
          }
        );
      },
      dcbxl:function (){//单次变形量曲线图
        // console.log(this.cedian)
        this.dataSource=[]
        //let params = {sectionName:this.model.sectionName};
        let params = {measuringPoint:this.cedian,sectionName:this.model.sectionName};
        getAction(this.url.clsj,params)
          .then(res=>{
            this.dataSource=[];
            // console.log(res)
            if(res.success){
              for(let i=0;i<res.result.length;i++){
                this.dataSource.push({type:res.result[i].birthTime,jeecg:res.result[i].onceDeformation});
              }
            }
          })
      },
      bxsl:function (){//变形速率曲线图
        this.dataSource1=[]
        let params = {measuringPoint:this.cedian,sectionName:this.model.sectionName};
        getAction(this.url.clsj,params)
          .then(res=>{
            this.dataSource1=[];
            // console.log(res)
            if(res.success){
              for(let i=0;i<res.result.length;i++){
                this.dataSource1.push({type:res.result[i].birthTime,jeecg:res.result[i].deformationRate});
              }
            }
          })
      },
      ljbx:function (){//累计变形曲线图
        this.dataSource2=[]
        let params = {measuringPoint:this.cedian,sectionName:this.model.sectionName};
        getAction(this.url.clsj,params)
          .then(res=>{
            this.dataSource2=[];
            // console.log(res)
            if(res.success){
              for(let i=0;i<res.result.length;i++){
                this.dataSource2.push({type:res.result[i].birthTime,jeecg:res.result[i].accumulatedDeformation});
              }
            }
          })
      },
      cdcx:function(){  //测点筛选
        // console.log(this.model)
        this.cedian=''
        this.measuringPoint=[];
        let params = {sectionName:this.model.sectionName};
        getAction(this.url.cdcx, params).then((res)=>{
          // console.log(res)
          if(res.success)  {
            let list=res.result;
            list.forEach(re=>{
              this.measuringPoint.push({key:re.measuringPoint,label:re.measuringPoint})
            })
            this.cedian=list[0].measuringPoint
            // console.log("this.cedians",this.cedian)
            this.dcbxl();//单次变形量曲线图
            this.bxsl();//变形速率曲线图
            this.ljbx();//累计变形曲线图
            this.huigui();//回归分析
          }
        });
      },
      huigui:function(){//回归分析
        this.dataSource3=[]
        let params = {measuringPoint:this.cedian,sectionName:this.model.sectionName};
        getAction(this.url.clsj,params)
          .then(res=>{
            this.dataSource3=[];
            // console.log(res)
            if(res.success){
              for(let i=0;i<res.result.length;i++){
                this.dataSource3.push({type:res.result[i].birthTime,jeecg:res.result[i].measuringNumber});
              }
            }
          })
      },

      //渲染流程表单数据
      showFlowData(){
        if(this.formBpm === true){
          let params = {id:this.formData.dataId};
          getAction(this.url.queryById,params).then((res)=>{
            if(res.success){
              this.edit (res.result);
            }
          });
        }
      },
      submitForm () {
        const that = this;
        //触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true;
            let httpurl = '';
            let method = '';
            if(!this.model.id){
              httpurl+=this.url.add;
              method = 'post';
            }else{
              httpurl+=this.url.edit;
               method = 'put';
            }
            let formData = Object.assign(this.model, values);
            console.log("表单提交数据",formData)
            httpAction(httpurl,formData,method).then((res)=>{
              if(res.success){
                that.$message.success(res.message);
                that.$emit('ok');
              }else{
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
            })
          }

        })
      },
      popupCallback(row){
        this.form.setFieldsValue(pick(this.model,'sectionName','sectionLength','sectionWidth','excavationType','wallRockLevel','measuringPoint','isUse_dictText','callSum'))
      },
    }
  }
</script>