<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form :form="form" slot="detail">
        <a-row>
          <a-card title = "基础信息" :bordered="false" :headStyle="{color:'#0785fd'}" :bodyStyle="{padding:'10'}">
            <a-row>
              <a-col :span="12">
                <a-form-item label="车牌号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-decorator="['qianchepai']" placeholder="请输入车牌号"  ></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item label="设备编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-decorator="['shebeibianhao_dictText']" placeholder="请输入设备编号"  ></a-input>
                </a-form-item>
              </a-col>
            </a-row>
            <a-row>
              <a-col :span="12">
                <a-form-item label="进出材料单" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-decorator="['jinchuliaodanno']" placeholder="请输入进出材料单" ></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item label="批次" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-decorator="['pici']" placeholder="请输入批次"  ></a-input>
                </a-form-item>
              </a-col>
            </a-row>
            <a-row>
              <a-col :span="12">
                <a-form-item label="供应商单位" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-decorator="['gongyingshangdanweibianma_dictText']" placeholder="请输入供应商单位编码"  ></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item label="料仓名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-decorator="['lcbianhao_dictText']" placeholder="请输入料仓编号"  ></a-input>
                </a-form-item>
              </a-col>
            </a-row>
            <a-row>
              <a-col :span="12">
                <a-form-item label="进场时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-decorator="['jinchangshijian']" placeholder="请输入进场时间" ></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item label="出场时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-decorator="['chuchangshijian']" placeholder="请输入出场时间" ></a-input>
                </a-form-item>
              </a-col>
            </a-row>
            <a-row>
              <a-col :span="12">
                <a-form-item label="司磅员" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-decorator="['sibangyuan']" placeholder="请输入司磅员"  ></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item label="船号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-decorator="['cheliangbianhao']" placeholder="请输入船号"  ></a-input>
                </a-form-item>
              </a-col>
            </a-row>
            <a-row>
              <a-col :span="12">
                <a-form-item label="过磅类别" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-decorator="['guobangleibie']" placeholder="请输入过磅类别"  ></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item label="车辆类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-decorator="['cheliangleixing']" placeholder="请输入车辆类型"  ></a-input>
                </a-form-item>
              </a-col>
            </a-row>
            <a-row>
              <a-col :span="12">
                <a-form-item label="卸货地点" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-decorator="['liaocangid']" placeholder="请输入卸货地点"  ></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item label="运输单位" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-decorator="['yunshudanwei']" placeholder="请输入运输单位"  ></a-input>
                </a-form-item>
              </a-col>
            </a-row>
            <a-row>
              <a-col :span="12">
                <a-form-item label="送货单" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-decorator="['songhuodanpic']" placeholder="请输入送货单"  ></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item label="是否合格" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-decorator="['shifouhege']" placeholder="请输入是否合格"  ></a-input>
                </a-form-item>
              </a-col>
            </a-row>
            <a-row>
              <a-col :span="12">
                <a-form-item label="原材描述" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-decorator="['yuancaimiaoshu']" placeholder="请输入原材描述"  ></a-input>
                </a-form-item>
              </a-col>
            </a-row>
          </a-card >
<!--          <a-row>-->
            <!--          <a-col :span="24">-->
            <!--            <a-form-item label="后车牌" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
            <!--              <a-input v-decorator="['houchepai']" placeholder="请输入后车牌"  ></a-input>-->
            <!--            </a-form-item>-->
            <!--          </a-col>-->
<!--            <a-col :span="12">-->
<!--              <a-form-item label="毛重" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <a-input v-decorator="['maozhong']" placeholder="请输入毛重"  ></a-input>-->
<!--              </a-form-item>-->
<!--            </a-col>-->
<!--          </a-row>-->

<!--          <a-row>-->
<!--            <a-col :span="12">-->
<!--              <a-form-item label="皮重" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <a-input v-decorator="['pizhong']" placeholder="请输入皮重"  ></a-input>-->
<!--              </a-form-item>-->
<!--            </a-col>-->
<!--            <a-col :span="12">-->
<!--              <a-form-item label="净重" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <a-input v-decorator="['jingzhong']" placeholder="请输入净重"  ></a-input>-->
<!--              </a-form-item>-->
<!--            </a-col>-->
<!--          </a-row>-->
          <a-card title = "材料信息" :bordered="false" :headStyle="{color:'#0785fd'}" :bodyStyle="{padding:'10'}">
            <a-row>
              <a-col :span="12">
                <a-form-item label="材料名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-decorator="['cailiaono_dictText']" placeholder="请输入材料编号" ></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item label="毛重(吨)" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-decorator="['maozhongt']" placeholder="请输入毛重(吨)"  ></a-input>
                </a-form-item>
              </a-col>
            </a-row>
            <a-row>
              <a-col :span="12">
                <a-form-item label="皮重(吨)" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-decorator="['pizhongt']" placeholder="请输入皮重(吨)"  ></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item label="净重(吨)" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-decorator="['jingzhongt']" placeholder="请输入净重(吨)"  ></a-input>
                </a-form-item>
              </a-col>
            </a-row>
            <a-row>
              <a-col :span="12">
                <a-form-item label="扣重" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-decorator="['kouzhong']" placeholder="请输入扣重"  ></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item label="扣率" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-decorator="['koulv']" placeholder="请输入扣率"  ></a-input>
                </a-form-item>
              </a-col>
            </a-row>
            <a-row>
              <a-col :span="12">
                <a-form-item label="称重偏差" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-decorator="['chengzhongpiancha']" placeholder="请输入称重偏差"  ></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item label="累计" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-decorator="['liaocang']" placeholder="请输入累计"  ></a-input>
                </a-form-item>
              </a-col>
            </a-row>
          </a-card>


<!--          <a-row>-->

            <!--          <a-col :span="24">-->
            <!--            <a-form-item label="remark" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
            <!--              <a-input v-decorator="['remark']" placeholder="请输入remark"  ></a-input>-->
            <!--            </a-form-item>-->
            <!--          </a-col>-->

<!--          </a-row>-->
<!--          <a-row>-->

            <!--          <a-col :span="24">-->
            <!--            <a-form-item label="唯一标识" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
            <!--              <a-input v-decorator="['guid']" placeholder="请输入唯一标识"  ></a-input>-->
            <!--            </a-form-item>-->
            <!--          </a-col>-->
            <!--          <a-col :span="24">-->
            <!--            <a-form-item label="时间戳" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
            <!--              <a-input-number v-decorator="['ts']" placeholder="请输入时间戳" style="width: 100%" />-->
            <!--            </a-form-item>-->
            <!--          </a-col>-->
            <!--          <a-col :span="24">-->
            <!--            <a-form-item label="是否删除" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
            <!--              <a-input v-decorator="['isdel']" placeholder="请输入是否删除"  ></a-input>-->
            <!--            </a-form-item>-->
            <!--          </a-col>-->
            <!--          <a-col :span="24">-->
            <!--            <a-form-item label="status" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
            <!--              <a-input v-decorator="['status']" placeholder="请输入status"  ></a-input>-->
            <!--            </a-form-item>-->
            <!--          </a-col>-->

<!--          </a-row>-->

<!--          <a-row>-->

            <!--          <a-col :span="24">-->
            <!--            <a-form-item label="candi" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
            <!--              <a-input v-decorator="['candi']" placeholder="请输入candi"  ></a-input>-->
            <!--            </a-form-item>-->
            <!--          </a-col>-->

<!--          </a-row>-->

<!--          <a-row>-->

            <!--          <a-col :span="24">-->
            <!--            <a-form-item label="isshouliao" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
            <!--              <a-input v-decorator="['isshouliao']" placeholder="请输入isshouliao"  ></a-input>-->
            <!--            </a-form-item>-->
            <!--          </a-col>-->

<!--          </a-row>-->

<!--          <a-row>-->
<!--            <a-col :span="12">-->
<!--              <a-form-item label="是否统计 " :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <a-input-number v-decorator="['istongji_dictText']" placeholder="请输入是否统计 默认为0:未统计，1:已统计,15:统计出错" style="width: 100%" />-->
<!--              </a-form-item>-->
<!--            </a-col>-->

<!--          </a-row>-->

<!--          <a-col :span="24">-->
<!--            <a-form-item label="jcgkpic" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['jcgkpic']" placeholder="请输入jcgkpic"  ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="jccppic" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['jccppic']" placeholder="请输入jccppic"  ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="jchcppic" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['jchcppic']" placeholder="请输入jchcppic"  ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="jcbfpic" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['jcbfpic']" placeholder="请输入jcbfpic"  ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="ccgkpic" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['ccgkpic']" placeholder="请输入ccgkpic"  ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="cccppic" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['cccppic']" placeholder="请输入cccppic"  ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="cchcppic" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['cchcppic']" placeholder="请输入cchcppic"  ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="ccbfpic" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['ccbfpic']" placeholder="请输入ccbfpic"  ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->

<!--          <a-col :span="24">-->
<!--            <a-form-item label="serialno" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['serialno']" placeholder="请输入serialno"  ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="reason" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['reason']" placeholder="请输入reason"  ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="fileUpload" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['fileUpload']" placeholder="请输入fileUpload"  ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="istaizhangtj" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['istaizhangtj']" placeholder="请输入istaizhangtj"  ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->


<!--          <a-col :span="24">-->
<!--            <a-form-item label="beizhu" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['beizhu']" placeholder="请输入beizhu"  ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="jianlipic" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['jianlipic']" placeholder="请输入jianlipic"  ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="sibanpic" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['sibanpic']" placeholder="请输入sibanpic"  ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
          <a-col v-if="showFlowSubmitButton" :span="24" style="text-align: center">
            <a-button @click="submitForm">提 交</a-button>
          </a-col>
        </a-row>
      </a-form>
    </j-form-container>
    <a-tabs default-active-key="1">
      <a-tab-pane key="1" tab="图片展示">
        <viewer :images="detailList">
          <img
            v-for="(item,index) in detailList"
            :key="index"
            style="height:450px;width: 550px;margin: 5px 10px 5px 10px;float: left"
            :src="['http://web.traiot.cn/docs/wz/' + item.icon] "
            alt="">
        </viewer>
      </a-tab-pane>

    </a-tabs>

  </a-spin>
</template>

<script>

  import { httpAction, getAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import JFormContainer from '@/components/jeecg/JFormContainer'

  export default {
    name: 'WzycljinchanggbForm',
    components: {
      JFormContainer,
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
        detailList:[],
        form: this.$form.createForm(this),
        model: {},
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        confirmLoading: false,
        validatorRules: {
        },
        url: {
          list:"/yclsl/wzycljinchanggb/list1",
          add: "/yclsl/wzycljinchanggb/add",
          edit: "/yclsl/wzycljinchanggb/edit",
          queryById: "/yclsl/wzycljinchanggb/queryById"
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
    },
    methods: {
      add () {
        this.edit({});
      },
      edit (record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'jinchuliaodanno','cailiaono_dictText','jinchangshijian','chuchangshijian','pici','cheliangbianhao','qianchepai','houchepai','maozhong','pizhong','jingzhong','kouzhong','koulv','chengzhongpiancha','liaocang','sibangyuan','remark','shebeibianhao_dictText','gongyingshangdanweibianma_dictText','guobangleibie','cheliangleixing','guid','ts','isdel','status','maozhongt','pizhongt','candi','yunshudanwei','lcbianhao_dictText','isshouliao','jingzhongt','istongji_dictText','jcgkpic','jccppic','jchcppic','jcbfpic','ccgkpic','cccppic','cchcppic','ccbfpic','liaocangid','serialno','reason','fileUpload','istaizhangtj','songhuodanpic','shifouhege','yuancaimiaoshu','beizhu','jianlipic','sibanpic'))
          this.getData();
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
        // 触发表单验证
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
      getData:function (){//图片显示
        let params = {id:this.model.id}
        this.detailList = [];
        getAction(this.url.list,params).then((res)=> {
            if (res.success) {
              let data = res.result;
              //console.log(data, "+______________________________+")
              if (data[0].jcgkpic !== null && data[0].jcgkpic !=='') {
                this.detailList.push({icon:data[0].jcgkpic})
              }
              if (data[0].jccppic !== null && data[0].jccppic !== '') {
                this.detailList.push({icon:data[0].jccppic})
              }
              if (data[0].jchcppic !== null && data[0].jchcppic !== '') {
                this.detailList.push({icon:data[0].jchcppic})
              }
              if (data[0].jcbfpic !== null && data[0].jcbfpic !== '') {
                this.detailList.push({icon:data[0].jcbfpic})
              }
              if (data[0].ccgkpic !== null && data[0].ccgkpic !== '') {
                this.detailList.push({icon:data[0].ccgkpic})
              }
              if (data[0].cccppic !== null && data[0].cccppic !== '') {
                this.detailList.push({icon:data[0].cccppic})
              }
              if (data[0].cchcppic !== null && data[0].cchcppic !== '') {
                this.detailList.push({icon:data[0].cchcppic})
              }
              if (data[0].ccbfpic !== null && data[0].ccbfpic !== '') {
                this.detailList.push({icon:data[0].ccbfpic})
              }
              //console.log(this.detailList, "++++++++++++++++")
            }
          }
        );
      },
        popupCallback(row){
        this.form.setFieldsValue(pick(row,'jinchuliaodanno','cailiaono_dictText','jinchangshijian','chuchangshijian','pici','cheliangbianhao','qianchepai','houchepai','maozhong','pizhong','jingzhong','kouzhong','koulv','chengzhongpiancha','liaocang','sibangyuan','remark','shebeibianhao_dictText','gongyingshangdanweibianma_dictText','guobangleibie','cheliangleixing','guid','ts','isdel','status','maozhongt','pizhongt','candi','yunshudanwei','lcbianhao_dictText','isshouliao','jingzhongt','istongji_dictText','jcgkpic','jccppic','jchcppic','jcbfpic','ccgkpic','cccppic','cchcppic','ccbfpic','liaocangid','serialno','reason','fileUpload','istaizhangtj','songhuodanpic','shifouhege','yuancaimiaoshu','beizhu','jianlipic','sibanpic'))
      },
    }
  }
</script>