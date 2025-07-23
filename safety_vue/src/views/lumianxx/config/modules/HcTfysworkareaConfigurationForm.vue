<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item
              label="项目"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              prop="projectid"
            >
              <j-search-select-tag
                style="width: 200px"
                placeholder="请选择项目名称"
                v-model="model.projectid"
                :dictOptions="dictOptionPro"
                @change="handleChange"
              >
              </j-search-select-tag>
              <!-- <a-input v-model="model.projectid" placeholder="请输入项目"  ></a-input> -->
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item
              label="标段"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              prop="sectionid"
            >
              <j-search-select-tag
                style="width: 200px"
                placeholder="请选择标段名称"
                v-model="model.sectionid"
                :dictOptions="dictOptionBD"
                @change="handleChangeBD"
              >
              </j-search-select-tag>
              <!-- <a-input v-model="model.sectionid" placeholder="请输入标段"  ></a-input> -->
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="开振占比初级预警" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="vibrateratioMin">
              <a-input v-model="model.vibrateratioMin" placeholder="请输入开振占比（%）初级预警"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="开振占比中级预警" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="vibrateratioMin">
              <a-input v-model="model.vibrateratioZon" placeholder="请输入开振占比（%）中级预警"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="开振占比高级预警" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="vibrateratioMax">
              <a-input v-model="model.vibrateratioMax" placeholder="请输入开振占比（%）高级预警"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="平均碾压遍数初级预警" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="timesavgMin">
              <a-input v-model="model.timesavgMin" placeholder="请输入平均碾压遍数初级预警"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="平均碾压遍数中级预警" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="timesavgMin">
              <a-input v-model="model.timesavgZon" placeholder="请输入平均碾压遍数中级预警"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="平均碾压遍数高级预警" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="timesavgMax">
              <a-input v-model="model.timesavgMax" placeholder="请输入平均碾压遍数高级预警"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="平均厚度初级预警" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="thickavgMin">
              <a-input v-model="model.thickavgMin" placeholder="请输入平均厚度（cm）初级预警"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="平均厚度中级预警" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="thickavgMin">
              <a-input v-model="model.thickavgZon" placeholder="请输入平均厚度（cm）中级预警"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="平均厚度高级预警" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="thickavgMax">
              <a-input v-model="model.thickavgMax" placeholder="请输入平均厚度（cm）高级预警"  ></a-input>
            </a-form-model-item>
          </a-col>
<!--          <a-col :span="24">-->
<!--            <a-form-item label='发车单手机号码' :labelCol='labelCol' :wrapperCol='wrapperCol' prop='phone'>-->
<!--              <j-dict-select-tag type='list' v-model='model.phone' :trigger-change='true'-->
<!--                                 dictCode="bhz_phone,names,uid,phonesname='25'" dictTable='phonesname'-->
<!--                                 placeholder='请选择发车单手机号码' />-->
<!--            </a-form-item>-->
<!--          </a-col>-->
          <a-col :span="24">
            <a-form-item label="初级号码组" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-model='model.primaryPhones' :trigger-change="true"
                                 dictCode="bhz_phone,names,uid,phonesname='1'" dictTable="bhz_phone"
                                 placeholder="请选择初级号码组"/>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="中级号码组" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-model='model.middlePhones' :trigger-change="true"
                                 dictCode="bhz_phone,names,uid,phonesname='2'" dictTable="bhz_phone"
                                 placeholder="请选择中级号码组"/>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="高级号码组" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-model='model.advancedPhones' :trigger-change="true"
                                 dictCode="bhz_phone,names,uid,phonesname='3'" dictTable="bhz_phone"
                                 placeholder="请选择高级号码组"/>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label='是否报警' :labelCol='labelCol' :wrapperCol='wrapperCol' prop='stauts'>
              <j-dict-select-tag type='list' v-model='model.stauts' :trigger-change='true' dictCode='is_call'
                                 placeholder='请选择是否报警' />
            </a-form-item>
          </a-col>
        </a-row>
      </a-form-model>
    </j-form-container>
  </a-spin>
</template>

<script>

  import { httpAction, getAction } from '@/api/manage'
  import { validateDuplicateValue } from '@/utils/util'
  import JFormContainer from '@/components/jeecg/JFormContainer'
  import JselectDqDepart from '@comp/jeecgbiz/JselectDqDepart'
  import { usershebeiList } from '@api/api'
  import Vue from 'vue'
  export default {
    name: 'HcTfysworkareaConfigurationForm',
    components: {
      JFormContainer,
      JselectDqDepart
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
          add: "/hctfysworkareaconfiguration/hcTfysworkareaConfiguration/add",
          edit: "/hctfysworkareaconfiguration/hcTfysworkareaConfiguration/edit",
          hcProject: "/hc_project/hcProject/list",
          hcSection: "/hc_section/hcSection/list",
          hcProject1: "/hc_section/hcSection/listls",
          queryById: "/hctfysworkareaconfiguration/hcTfysworkareaConfiguration/queryById"
        },
        dictOptionPro: [],
        dictOptionBD: [],
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
      getProject() {
        // let username = this.$route.query.username;
        let orgCode = Vue.ls.get("SYS_DEPART_ORGCODE");
        let param = { orgCode };
        getAction(this.url.hcProject, param).then((res) => {
          console.log(res, "getProject-------------------------");
          this.dictOptionPro = [];
          this.orgcode = "";
          let result = res.result.records;
          if (result == null) {
            this.getProject1();
          } else {
            this.projectList = res.result.records;
            this.orgcode = result[0].orgcode;
            // this.projectId = result[0].pjid;
            this.model.projectid = this.model.pjid || result[0].pjid;
            result.forEach((res) => {
              this.dictOptionPro.push({ text: res.pjname, value: res.pjid });
            });
            console.log(this.dictOptionPro, "this.dictOptionPro------------");
            this.getDataBD(this.orgcode);
          }
        });
      },
      getProject1() {
        // let username = this.$route.query.username;
        let param = {};
        getAction(this.url.hcProject1, param).then((res) => {
          console.log(res, "getProject-------------------------");
          this.dictOptionPro = [];
          this.orgcode = "";
          let result = res.result.records;
          this.projectList = res.result.records;
          this.orgcode = result[0].orgcode;
          // this.projectId = result[0].pjid;
          // this.model.projectid = result[0].pjid;
          this.model.projectid = this.model.projectid || result[0].pjid;
          result.forEach((res) => {
            this.dictOptionPro.push({ text: res.pjname, value: res.pjid });
          });
          this.getDataBD(this.orgcode);
        });
      },
      getDataBD() {
        let orgCode = Vue.ls.get("SYS_DEPART_ORGCODE");
        let param = {
          orgcode: orgCode,
        };
        getAction(this.url.hcSection, param).then((res) => {
          console.log(res, "hcSection------------");
          this.dictOptionBD = [];
          // this.model.sectionid = "";
          let result = res.result.records;
          this.sectionList = res.result.records;
          // this.sectionId = result[0].sectionid;
          // this.model.biaoduan = result[0].sectionname;
          // this.model.sectionid = result[0].sectionid;
          result.forEach((res) => {
            this.dictOptionBD.push({ text: res.sectionname, value: res.sectionid });
          });
          this.model.sectionid = this.model.sectionid || result[0].sectionid;
          // if(this.model.sectionid){
          //   let arr = this.sectionList.filter((e) => {
          //     return e.sectionid == this.model.sectionid;
          //   });
          //   this.model.sectionid = arr[0].sectionname;
          // }else{
          //   this.model.sectionid = result[0].sectionid;
          // }
          // if (this.firstGetData) {
          //   this.firstGetData = false;
          //   this.getData();
          // }
        });
      },
      add () {
        this.edit(this.modelDefault);
      },
      edit (record) {
        this.model = Object.assign({}, record);
        this.visible = true;
        this.getProject();
      },
      handleChange(selectedValue) {
        let arr = this.projectList.filter((e) => {
          return e.orgcode == selectedValue;
        });
        this.projectId = arr[0].pjid;
        console.log("selectedValue", arr, arr.pjid);
        this.getDataBD(selectedValue);
        console.log(this.projectId, this.sectionId, "this.projectId------------sectionId");
      },
      handleChangeBD(selectedValue) {
        let arr = this.sectionList.filter((e) => {
          return e.sectionid == selectedValue;
        });
        this.sectionId = arr[0].sectionid;
        // console.log("selectedValue", selectedValue);
        // this.sectionId = selectedValue;
        console.log(this.projectId, this.sectionId, "this.projectId------------sectionId");
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