<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form :form="form" slot="detail">
        <a-row>
<!--          <a-col :span="24">-->
<!--            <a-form-item label="台账id" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input-number v-decorator="['taizhangid']" placeholder="请输入台账id" style="width: 100%" />-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="取样时间" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['quyangshijian']" placeholder="请输入取样时间"  ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="qypic" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['qypic']" placeholder="请输入qypic"  ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="gzpic" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['gzpic']" placeholder="请输入gzpic"  ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="jlpic" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['jlpic']" placeholder="请输入jlpic"  ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="pzpic" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['pzpic']" placeholder="请输入pzpic"  ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="进场时间" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['jinchangshijian']" placeholder="请输入进场时间"  ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="sy1pic" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['sy1pic']" placeholder="请输入sy1pic"  ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="sy2pic" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['sy2pic']" placeholder="请输入sy2pic"  ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="systate" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['systate']" placeholder="请输入systate"  ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="shouyang1pic" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['shouyang1pic']" placeholder="请输入shouyang1pic"  ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="shouyang2pic" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['shouyang2pic']" placeholder="请输入shouyang2pic"  ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="提交判断 1为已提交" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['tjstate']" placeholder="请输入提交判断 1为已提交"  ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->

          <a-col v-if="showFlowSubmitButton" :span="24" style="text-align: center">
            <a-button @click="submitForm">提 交</a-button>
          </a-col>
        </a-row>
      </a-form>
    </j-form-container>
    <a-tabs default-active-key="1">
      <a-tab-pane key="1" tab="取样照片">
        <viewer :images="detailList">
          <img
            v-for="(item,index) in detailList"
            :key="index"
            style="height:250px;width: 350px;margin: 5px 10px 5px 10px;float: left"
            :src="[item.icon] "
            alt="取样照片">
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
    name: 'WzquyangsyForm',
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
        id:0,
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
          add: "/wzquyangsy/wzquyangsy/add",
          edit: "/wzquyangsy/wzquyangsy/edit",
          queryById: "/wzquyangsy/wzquyangsy/queryById",
          list:"/wzquyangsy/wzquyangsy/list"
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
        this.id = record.id
        this.getData();
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model))
          //,'taizhangid','quyangshijian','qypic','gzpic','jlpic','pzpic','jinchangshijian','sy1pic','sy2pic','systate','shouyang1pic','shouyang2pic','tjstate'
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
        let params = {taizhangid:this.id}
        this.detailList = [];
        getAction(this.url.list,params).then((res)=> {
            if (res.success) {
              let data = res.result.records;
              //console.log(data, "+______________________________+")
              if (data.length > 0){
                if (data[0].qypic !== null && data[0].qypic !=='') {
                  this.detailList.push({icon:data[0].qypic})
                }
                if (data[0].gzpic !== null && data[0].gzpic !== '') {
                  this.detailList.push({icon:data[0].gzpic})
                }
                if (data[0].jlpic !== null && data[0].jlpic !== '') {
                  this.detailList.push({icon:data[0].jlpic})
                }
                if (data[0].pzpic !== null && data[0].pzpic !== '') {
                  this.detailList.push({icon:data[0].pzpic})
                }
                if (data[0].sy1pic !== null && data[0].sy1pic !== '') {
                  this.detailList.push({icon:data[0].sy1pic})
                }
                if (data[0].sy2pic !== null && data[0].sy2pic !== '') {
                  this.detailList.push({icon:data[0].sy2pic})
                }
                if (data[0].shouyang1pic !== null && data[0].shouyang1pic !== '') {
                  this.detailList.push({icon:data[0].shouyang1pic})
                }
                if (data[0].shouyang2pic !== null && data[0].shouyang2pic !== '') {
                  this.detailList.push({icon:data[0].shouyang2pic})
                }
              } else {
                this.$message.warning("暂无取样图片！")
              }
              //console.log(this.detailList, "++++++++++++++++")
            }

          }
        );
      },
      popupCallback(row){
        this.form.setFieldsValue(pick(row))
          //,'taizhangid','quyangshijian','qypic','gzpic','jlpic','pzpic','jinchangshijian','sy1pic','sy2pic','systate','shouyang1pic','shouyang2pic','tjstate'
      },
    }
  }
</script>