<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-row>
            <a-col :span="12">
              <a-form-model-item label="设备名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sbbh">
                <a-input v-model="model.sbbh_dictText" placeholder="请输入设备名称"  ></a-input>
              </a-form-model-item>
            </a-col>
            <a-col :span="12">
              <a-form-model-item label="到达时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="ddtime">
                <j-date placeholder="请选择到达时间" v-model="model.ddtime"  style="width: 100%" />
              </a-form-model-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-model-item label="出发时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="cftime">
                <j-date placeholder="请选择出发时间" v-model="model.cftime"  style="width: 100%" />
              </a-form-model-item>
            </a-col>
            <a-col :span="12">
              <a-form-model-item label="单号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="danhao">
                <a-input v-model="model.danhao" placeholder="请输入单号"  ></a-input>
              </a-form-model-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-model-item label="车牌号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="cph">
                <a-input v-model="model.cph" placeholder="请输入车牌号"  ></a-input>
              </a-form-model-item>
            </a-col>
            <!--          <a-col :span="24">-->
            <!--            <a-form-model-item label="是否到达0为未到达，1为到达" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="zhuangtai">-->
            <!--              <a-input-number v-model="model.zhuangtai" placeholder="请输入是否到达0为未到达，1为到达" style="width: 100%" />-->
            <!--            </a-form-model-item>-->
            <!--          </a-col>-->
            <a-col :span="12">
              <a-form-model-item label="是否合格" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sfhg">
                <a-input v-model="model.sfhg" placeholder="请输入是否合格"  ></a-input>
              </a-form-model-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-model-item label="供货单位" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="ghdw">
                <a-input v-model="model.ghdw" placeholder="供货单位"  ></a-input>
              </a-form-model-item>
            </a-col>
            <a-col :span="12">
              <a-form-model-item label="材料" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="cailiao">
                <a-input v-model="model.cailiao" placeholder="请输入材料"  ></a-input>
              </a-form-model-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-model-item label="规格" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="guige">
                <a-input v-model="model.guige" placeholder="请输入规格"  ></a-input>
              </a-form-model-item>
            </a-col>
            <a-col :span="12">
              <a-form-model-item label="发车数量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jcsl">
                <a-input v-model="model.jcsl" placeholder="请输入发车数量"  ></a-input>
              </a-form-model-item>
            </a-col>
<!--            <a-col :span="12">-->
<!--              <a-form-model-item label="发车纬度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="fclat">-->
<!--                <a-input v-model="model.fclat" placeholder="请输入发车纬度"  ></a-input>-->
<!--              </a-form-model-item>-->
<!--            </a-col>-->
          </a-row>

<!--          <a-row>-->
<!--            <a-col :span="12">-->
<!--              <a-form-model-item label="发车经度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="fclng">-->
<!--                <a-input v-model="model.fclng" placeholder="请输入发车经度"  ></a-input>-->
<!--              </a-form-model-item>-->
<!--            </a-col>-->
<!--            <a-col :span="12">-->
<!--              <a-form-model-item label="到达纬度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="ddlat">-->
<!--                <a-input v-model="model.ddlat" placeholder="请输入到达纬度"  ></a-input>-->
<!--              </a-form-model-item>-->
<!--            </a-col>-->
<!--          </a-row>-->

          <a-row>
<!--            <a-col :span="12">-->
<!--              <a-form-model-item label="到达经度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="ddlng">-->
<!--                <a-input v-model="model.ddlng" placeholder="请输入到达经度"  ></a-input>-->
<!--              </a-form-model-item>-->
<!--            </a-col>-->
            <a-col :span="12">
              <a-form-model-item label="负责人" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="fzr">
                <a-input v-model="model.fzr" placeholder="请输入负责人"  ></a-input>
              </a-form-model-item>
            </a-col>
            <a-col :span="12">
              <a-form-model-item label="目的地" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="mdd">
                <a-input v-model="model.mdd" placeholder="请输入目的地"  ></a-input>
              </a-form-model-item>
            </a-col>
          </a-row>

          <a-row>

<!--            <a-col :span="12">-->
<!--              <a-form-model-item label="进场过磅数量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jcgbl">-->
<!--                <a-input v-model="model.jcgbl" placeholder="请输入进场过磅数量"  ></a-input>-->
<!--              </a-form-model-item>-->
<!--            </a-col>-->
          </a-row>
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="0为报警 1为不报警" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sfbj">-->
<!--              <a-input v-model="model.sfbj" placeholder="请输入0为报警 1为不报警"  ></a-input>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="0为未发送1为已发送 发送到达信息" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="fsxx">-->
<!--              <a-input v-model="model.fsxx" placeholder="请输入0为未发送1为已发送 发送到达信息"  ></a-input>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->

<!--          <a-row>-->
<!--            <a-col :span="12">-->
<!--              <a-form-model-item label="出场过磅数量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="ccgbl">-->
<!--                <a-input v-model="model.ccgbl" placeholder="请输入出场过磅数量"  ></a-input>-->
<!--              </a-form-model-item>-->
<!--            </a-col>-->
<!--            <a-col :span="12">-->
<!--              <a-form-model-item label="当前库存" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="dqkc">-->
<!--                <a-input v-model="model.dqkc" placeholder="请输入当前库存"  ></a-input>-->
<!--              </a-form-model-item>-->
<!--            </a-col>-->
<!--          </a-row>-->

<!--          <a-row>-->
<!--            <a-col :span="12">-->
<!--              <a-form-model-item label="实际消耗数量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sjxhsl">-->
<!--                <a-input v-model="model.sjxhsl" placeholder="请输入实际消耗数量"  ></a-input>-->
<!--              </a-form-model-item>-->
<!--            </a-col>-->
<!--            <a-col :span="12">-->
<!--              <a-form-model-item label="理论消耗数量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="llxhsl">-->
<!--                <a-input v-model="model.llxhsl" placeholder="请输入理论消耗数量由理论配合比和工程任务单计算得出"  ></a-input>-->
<!--              </a-form-model-item>-->
<!--            </a-col>-->
<!--          </a-row>-->

<!--          <a-row>-->
<!--            <a-col :span="12">-->
<!--              <a-form-model-item label="理论库存" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="llkc">-->
<!--                <a-input v-model="model.llkc" placeholder="请输入理论库存 = 实际进场 - 理论消耗"  ></a-input>-->
<!--              </a-form-model-item>-->
<!--            </a-col>-->
<!--            <a-col :span="12">-->
<!--              <a-form-model-item label="理论消耗" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="llxh">-->
<!--                <a-input v-model="model.llxh" placeholder="请输入理论消耗"  ></a-input>-->
<!--              </a-form-model-item>-->
<!--            </a-col>-->
<!--          </a-row>-->

          <a-row>
<!--            <a-col :span="12">-->
<!--              <a-form-model-item label="理论配合比" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="llphb">-->
<!--                <a-input v-model="model.llphb" placeholder="请输入理论配合比"  ></a-input>-->
<!--              </a-form-model-item>-->
<!--            </a-col>-->
            <!--          <a-col :span="24">-->
            <!--            <a-form-model-item label="0为已拿到地磅数据1为未拿到" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sfcssj">-->
            <!--              <a-input v-model="model.sfcssj" placeholder="请输入0为已拿到地磅数据1为未拿到"  ></a-input>-->
            <!--            </a-form-model-item>-->
            <!--          </a-col>-->

          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-model-item label="铅封号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="qianfenghao">
                <a-input v-model="model.qianfenghao" placeholder="请输入铅封号"  ></a-input>
              </a-form-model-item>
            </a-col>
<!--            <a-col :span="12">-->
<!--              <a-form-model-item label="所属标段" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="userdepartid">-->
<!--                <a-input v-model="model.userdepartid" placeholder="请输入所属标段"  ></a-input>-->
<!--              </a-form-model-item>-->
<!--            </a-col>-->
            <a-col :span="12">
              <a-form-model-item label="解锁时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="timestatime">
                <j-date placeholder="请选择解锁时间" v-model="model.timestatime"  style="width: 100%" />
              </a-form-model-item>
            </a-col>
          </a-row>

<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="0 未到达  1已到达 2已解锁" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="isjiesuo">-->
<!--              <a-input-number v-model="model.isjiesuo" placeholder="请输入0 未到达  1已到达 2已解锁" style="width: 100%" />-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
          <a-row>
            <a-col :span="12">
              <a-form-model-item label="解锁人" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="name">
                <a-input v-model="model.name" placeholder="请输入解锁人"  ></a-input>
              </a-form-model-item>
            </a-col>
          </a-row>
        </a-row>
      </a-form-model>
    </j-form-container>
    <a-tabs default-active-key="1">
      <a-tab-pane key="1" tab="上锁图片">
        <viewer :images="model">
          <img
            style=""
            :src="model.imgfile"
            alt="上锁图片">
        </viewer>
      </a-tab-pane>
      <a-tab-pane key="2" tab="质保单图片">
        <viewer :images="model">
          <img
            style=""
            :src="model.jyimgfile"
            alt="质保单图片">
        </viewer>
      </a-tab-pane>
    </a-tabs>
  </a-spin>
</template>

<script>

  import { httpAction, getAction } from '@/api/manage'
  import { validateDuplicateValue } from '@/utils/util'

  export default {
    name: 'WbshebeiDetailForm',
    components: {
    },
    props: {
      //表单禁用
      disabled: {
        type: Boolean,
        default: false,
        required: false
      }
    },
    data () {
      return {
        model:{
         },
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
          add: "/wbshebeidetail/wbshebeiDetail/add",
          edit: "/wbshebeidetail/wbshebeiDetail/edit",
          queryById: "/wbshebeidetail/wbshebeiDetail/queryById"
        }
      }
    },
    computed: {
      formDisabled(){
        return this.disabled
      },
    },
    created () {
       //备份model原始值
      this.modelDefault = JSON.parse(JSON.stringify(this.model));
    },
    methods: {
      add () {
        this.edit(this.modelDefault);
      },
      edit (record) {
        this.model = Object.assign({}, record);
        this.visible = true;
      },
      submitForm () {
        const that = this;
        // 触发表单验证
        this.$refs.form.validate(valid => {
          if (valid) {
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
            httpAction(httpurl,this.model,method).then((res)=>{
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
    }
  }
</script>