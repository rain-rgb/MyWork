<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="进出料单号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jinchuliaodanno">
              <a-input v-model="model.jinchuliaodanno" placeholder="请输入进出料单号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="材料单号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="cailiaono">
              <a-input v-model="model.cailiaono" placeholder="请输入材料单号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="进场时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jinchangshijian">
              <a-input v-model="model.jinchangshijian" placeholder="请输入进场时间"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="出场时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="chuchangshijian">
              <a-input v-model="model.chuchangshijian" placeholder="请输入出场时间"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="批次" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pici">
              <a-input v-model="model.pici" placeholder="请输入批次"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="车辆编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="cheliangbianhao">
              <a-input v-model="model.cheliangbianhao" placeholder="请输入车辆编号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="前车牌" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="qianchepai">
              <a-input v-model="model.qianchepai" placeholder="请输入前车牌"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="后车牌" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="houchepai">
              <a-input v-model="model.houchepai" placeholder="请输入后车牌"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="毛重" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="maozhong">
              <a-input v-model="model.maozhong" placeholder="请输入毛重"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="皮重" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pizhong">
              <a-input v-model="model.pizhong" placeholder="请输入皮重"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="净重" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jingzhong">
              <a-input v-model="model.jingzhong" placeholder="请输入净重"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="扣重" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="kouzhong">
              <a-input v-model="model.kouzhong" placeholder="请输入扣重"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="扣率" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="koulv">
              <a-input v-model="model.koulv" placeholder="请输入扣率"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="称重偏差" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="chengzhongpiancha">
              <a-input v-model="model.chengzhongpiancha" placeholder="请输入称重偏差"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="料仓" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="liaocang">
              <a-input v-model="model.liaocang" placeholder="请输入料仓"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="司磅员" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sibangyuan">
              <a-input v-model="model.sibangyuan" placeholder="请输入司磅员"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="出场原因" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="remark">
              <a-input v-model="model.remark" placeholder="请输入出场原因"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="设备编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shebeibianhao">
              <a-input v-model="model.shebeibianhao" placeholder="请输入设备编号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="供应商" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="gongyingshangdanweibianma">
              <a-input v-model="model.gongyingshangdanweibianma" placeholder="请输入供应商"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="过磅类别" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="guobangleibie">
              <a-input v-model="model.guobangleibie" placeholder="请输入过磅类别"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="车辆类型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="cheliangleixing">
              <a-input v-model="model.cheliangleixing" placeholder="请输入车辆类型"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="STATUS" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="status">
              <a-input v-model="model.status" placeholder="请输入STATUS"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="ISDEL" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="isdel">
              <a-input v-model="model.isdel" placeholder="请输入ISDEL"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="TS" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="ts">
              <a-input-number v-model="model.ts" placeholder="请输入TS" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="GUID" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="guid">
              <a-input v-model="model.guid" placeholder="请输入GUID"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="毛重(吨)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="maozhongt">
              <a-input v-model="model.maozhongt" placeholder="请输入毛重(吨)"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="皮重(吨)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pizhongt">
              <a-input v-model="model.pizhongt" placeholder="请输入皮重(吨)"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="candi" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="candi">
              <a-input v-model="model.candi" placeholder="请输入candi"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="运输单位" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="yunshudanwei">
              <a-textarea v-model="model.yunshudanwei" rows="4" placeholder="请输入运输单位" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="料仓" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="lcbianhao">
              <a-input v-model="model.lcbianhao" placeholder="请输入料仓"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="ISSHOULIAO" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="isshouliao">
              <a-input v-model="model.isshouliao" placeholder="请输入ISSHOULIAO"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="净重(吨)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jingzhongt">
              <a-input v-model="model.jingzhongt" placeholder="请输入净重(吨)"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="ISTONGJI" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="istongji">
              <a-input-number v-model="model.istongji" placeholder="请输入ISTONGJI" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="进场高空" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jcgkpic">
              <a-input v-model="model.jcgkpic" placeholder="请输入进场高空"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="进场车牌1" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jccppic">
              <a-input v-model="model.jccppic" placeholder="请输入进场车牌1"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="进场车牌2" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jchcppic">
              <a-input v-model="model.jchcppic" placeholder="请输入进场车牌2"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="进场磅房" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jcbfpic">
              <a-input v-model="model.jcbfpic" placeholder="请输入进场磅房"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="出场高空" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="ccgkpic">
              <a-input v-model="model.ccgkpic" placeholder="请输入出场高空"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="出场车牌1" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="cccppic">
              <a-input v-model="model.cccppic" placeholder="请输入出场车牌1"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="出场车牌2" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="cchcppic">
              <a-input v-model="model.cchcppic" placeholder="请输入出场车牌2"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="出场磅房" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="ccbfpic">
              <a-input v-model="model.ccbfpic" placeholder="请输入出场磅房"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="料仓id" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="liaocangid">
              <a-input v-model="model.liaocangid" placeholder="请输入料仓id"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="SERIALNO" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="serialno">
              <a-input v-model="model.serialno" placeholder="请输入SERIALNO"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="原因" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="reason">
              <a-input v-model="model.reason" placeholder="请输入原因"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="文件上传" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="fileUpload">
              <a-input v-model="model.fileUpload" placeholder="请输入文件上传"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="beizhu" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="beizhu">
              <a-input v-model="model.beizhu" placeholder="请输入beizhu"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="项目id" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="projectid">
              <a-input v-model="model.projectid" placeholder="请输入项目id"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="施工地点" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="constructionsite">
              <a-input v-model="model.constructionsite" placeholder="请输入施工地点"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="台账统计状态 0;未统计，1:已统计，10:没有料仓配置，15:没有供应商单位，20;没有材料字典，40:异常" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="taizhangtj">
              <a-input-number v-model="model.taizhangtj" placeholder="请输入台账统计状态 0;未统计，1:已统计，10:没有料仓配置，15:没有供应商单位，20;没有材料字典，40:异常" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="台账id" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="taizhangid">
              <a-input-number v-model="model.taizhangid" placeholder="请输入台账id" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="istaizhangtj" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="istaizhangtj">
              <a-input v-model="model.istaizhangtj" placeholder="请输入istaizhangtj"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="送货单" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="songhuodanpic">
              <a-input v-model="model.songhuodanpic" placeholder="请输入送货单"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="是否合格" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shifouhege">
              <a-input v-model="model.shifouhege" placeholder="请输入是否合格"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="原材描述" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="yuancaimiaoshu">
              <a-input v-model="model.yuancaimiaoshu" placeholder="请输入原材描述"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="jianlipic" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jianlipic">
              <a-input v-model="model.jianlipic" placeholder="请输入jianlipic"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="sibanpic" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sibanpic">
              <a-input v-model="model.sibanpic" placeholder="请输入sibanpic"  ></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </j-form-container>
  </a-spin>
</template>

<script>

  import { httpAction, getAction } from '@/api/manage'
  import { validateDuplicateValue } from '@/utils/util'

  export default {
    name: 'WzyclchuchanggbManForm',
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
          add: "/wzyclchuchanggbman/wzyclchuchanggbMan/add",
          edit: "/wzyclchuchanggbman/wzyclchuchanggbMan/edit",
          queryById: "/wzyclchuchanggbman/wzyclchuchanggbMan/queryById"
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