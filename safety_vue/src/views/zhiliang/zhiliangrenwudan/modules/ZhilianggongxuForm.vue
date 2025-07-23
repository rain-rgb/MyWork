<template>
  <a-spin :spinning="confirmLoading">
    <!-- <j-form-container :disabled="formDisabled"> -->
    <j-form-container :disabled="false">
      <a-form :form="form" slot="detail">
        <a-row>
          <template>
            <a-steps direction="horizontal" :current="1" >
              <a-step v-for="(item,index) in gongxudata" :key="index" :title="item.xuhao" :status= "item.gxstatus" >
                <div v-show="item.ok" class="biank" slot="description">
<!--                    <p class="endtime" >开始时间：</p>-->
<!--                    <p>{{item.starttime}}</p>-->
                  <p class="endtime" >确认时间：</p>
                  <p>{{item.endtime}}</p>
                  <p class="endtime">责任人：{{item.responsible}}</p>
                </div>
                <div v-show="item.no" class="biank" slot="description">
                  <p class="endtime" >存入梁时间：</p>
                  <p>{{item.cuntime}}</p>
                  <p class="endtime">存入梁监管人：{{item.cunpeople}}</p>
                  <p class="endtime" >出场时间：</p>
                  <p>{{item.chutime}}</p>
                  <p class="endtime">出场监管人：{{item.chupeople}}</p>
                  <p class="endtime">梁所存层：{{item.liangceng != 0 ? item.liangceng : ''}}</p>
                  <!-- <p class="endtime">存梁行：{{item.cunlianghang}}</p> -->
                  <!-- <p class="endtime">存梁列：{{item.cunlianglie}}</p> -->
                  <p class="endtime">台座：{{item.taizuoname}}</p>
                  <p class="endtime">梁场名称：{{item.shebeino}}</p>
                </div>
                <div class="tag" slot="description">
                  <a-tag color="red" v-if="item.gxstatus == 'wait'">未完成</a-tag>
                  <a-tag color="green" v-if="item.gxstatus == 'finish'">已完成</a-tag>
                  <a-tag color="yellow" v-if="item.gxstatus == 'process'">进行中</a-tag>
                </div>
                <div class="msg-btn" slot="description" v-show="index <= lastFinishIndex+1">
                  <a-button type="primary" v-has="'beam:edit'" @click="msgEdit(item,index)" icon="edit" style="width:82px;">编辑</a-button>
                </div>
                <div class="msg-btn" slot="description" v-show="index == lastFinishIndex">
                  <a-button type="primary" v-has="'beam:back'" @click="fallBack(index)" icon="reload" style="width:82px;">回退</a-button>
                </div>
              </a-step>
            </a-steps>
          </template>
          <a-col v-if="showFlowSubmitButton" :span="24" style="text-align: center">
            <a-button @click="submitForm">提 交</a-button>
          </a-col>
        </a-row>
      </a-form>
    </j-form-container>
    <zhilianggongxu-edit ref="gxedit" @change="gxChange"></zhilianggongxu-edit>
  </a-spin>
</template>

<script>
  import zhilianggongxuEdit from './zhilianggongxuEdit'
  import { httpAction, getAction, postAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import JFormContainer from '@/components/jeecg/JFormContainer'
  import JDate from '@/components/jeecg/JDate'
  import JselectDqDepart from '@comp/jeecgbiz/JselectDqDepart'
  import JSelectDqProjName from '@comp/jeecgbiz/JselectDqProjName'

  export default {
    name: 'ZhilianggongxuForm',
    components: {
      JFormContainer,
      JDate,
      JselectDqDepart,
      JSelectDqProjName,
      zhilianggongxuEdit
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
        gongxudata: [],
        gxOriginalData: [],
        xuhaoData: {},
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
          add: "/sys/sysDepartproject47/zlrenwudanadd",
          edit: "/sys/sysDepartproject47/zlrenwudanedit",
          queryById: "/zhiliangrenwudan/zhiliangrenwudan/queryById",
          list:'/zhilianggongxu/zhiliangGongxu/list1',
          gxedit:'zhilianggongxu/zhiliangGongxu/gongxuGdit',
          gxback:'zhilianggongxu/zhiliangGongxu/gongxuGdit1',
          gxcheck:'/cunliang/beammanagementProcedure/list1'
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
      },
      lastFinishIndex(){
        let num = -1;
        this.gongxudata.forEach(item => {
          if(item.gxstatus == 'finish'){
            num++
          }
        });
        return num
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
          this.form.setFieldsValue(pick(this.model))
          this.getData();
            //'station','code','dattim','attribute','recipe','recipes','contract','customer','projname','projtype','projgrade','projarea','projadr','distance','conspos','pour','variety','betlev','filter','freeze','lands','cement','stone','bnsize','addliq','request','mixlast','mete','begtim','endtim','attamper','flag','note','createBy','sysOrgCode','isdel','status','jiaozhustatus','jingzhistatus','zhengyangstatus1','zhengyangstatus2','zhanglastatus','tiliangstatus','productiontime','uuid'
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
      getData:function (){
        let params = {uuid:this.model.uuid}
        this.gongxudata = []
        getAction(this.url.list,params).then((res=> {
          if (res.success) {
            let data = res.result.records
            console.log("data", data)
            this.gxOriginalData = data
            if (data.length>0){
              for (let i = 0; i < data.length; i++) {
                if (data[i].xuhao !== 7) {
                  if (data[i].status === 0) {
                    this.gongxudata.push(
                      {
                        xuhao: data[i].xuhao_dictText, gxstatus: 'wait',
                        endtime: data[i].finishtime, responsible: data[i].responsible, ok: true, no: false
                      })
                  } else if (data[i].status === 1) {
                    this.gongxudata.push(
                      {
                        xuhao: data[i].xuhao_dictText, gxstatus: 'process',
                        endtime: data[i].finishtime, responsible: data[i].responsible, ok: true, no: false
                      })
                  } else if (data[i].status === 2) {
                    this.gongxudata.push(
                      {
                        xuhao: data[i].xuhao_dictText, gxstatus: 'finish',
                        endtime: data[i].finishtime, responsible: data[i].responsible, ok: true, no: false
                      })
                  }
                } else {
                  if (data[i].status === 0) {
                    this.gongxudata.push(
                      {
                        xuhao: data[i].xuhao_dictText, cuntime: data[i].cuntime, gxstatus: 'wait', taizuoname: data[i].taizuoname,
                        chutime: data[i].chutime, cunpeople: data[i].cunpeople, chupeople: data[i].chupeople,
                        ok: false, no: true, liangceng: data[i].liangceng, cunlianghang: data[i].cunlianghang,
                        cunlianglie: data[i].cunlianglie, shebeino: data[i].shebeino_dictText
                      })
                  } else if (data[i].status === 1) {
                    this.gongxudata.push(
                      {
                        xuhao: data[i].xuhao_dictText, cuntime: data[i].cuntime, gxstatus: 'process', taizuoname: data[i].taizuoname,
                        chutime: data[i].chutime, cunpeople: data[i].cunpeople, chupeople: data[i].chupeople,
                        ok: false, no: true, liangceng: data[i].liangceng, cunlianghang: data[i].cunlianghang,
                        cunlianglie: data[i].cunlianglie, shebeino: data[i].shebeino_dictText
                      })
                  } else if (data[i].status === 2) {
                    this.gongxudata.push(
                      {
                        xuhao: data[i].xuhao_dictText, cuntime: data[i].cuntime, gxstatus: 'finish', taizuoname: data[i].taizuoname,
                        chutime: data[i].chutime, cunpeople: data[i].cunpeople, chupeople: data[i].chupeople,
                        ok: false, no: true, liangceng: data[i].liangceng, cunlianghang: data[i].cunlianghang,
                        cunlianglie: data[i].cunlianglie, shebeino: data[i].shebeino_dictText
                      })
                  }
                }
              }
            }else {
              this.$message.warning("暂无工序信息")
            }
          }
        }))
      },
      popupCallback(row){
        this.form.setFieldsValue(pick(row))
          //'station','code','dattim','attribute','recipe','recipes','contract','customer','projname','projtype','projgrade','projarea','projadr','distance','conspos','pour','variety','betlev','filter','freeze','lands','cement','stone','bnsize','addliq','request','mixlast','mete','begtim','endtim','attamper','flag','note','createBy','sysOrgCode','isdel','status','jiaozhustatus','jingzhistatus','zhengyangstatus1','zhengyangstatus2','zhanglastatus','tiliangstatus','productiontime','uuid'
      },
      fallBack(i){
        let params = {
          xuhao: this.gxOriginalData[i].xuhao,
          uuid: this.gxOriginalData[i].uuid,
        }
        postAction(this.url.gxback,params).then((res)=>{
          if(res.code == 200){
            this.getData();
            this.$message.success("回退成功！")
          }else {
            this.$message.warning("回退失败！")
          }
        });
      },
      msgEdit(value,i){
        this.xuhaoData = {
          xuhao: this.gxOriginalData[i].xuhao,
          uuid: this.gxOriginalData[i].uuid,
          id: this.gxOriginalData[i].id
        }
        // console.log(this.gxOriginalData[i].xuhao,"序号")
        // console.log(this.model.uuid,"uuid")
        if(value.xuhao == '存梁/取梁'){
          i < this.lastFinishIndex ? this.$refs.gxedit.disStatus2 = true : this.$refs.gxedit.disStatus2 = false
          this.$refs.gxedit.type = true
          this.$refs.gxedit.cuntime = value.cuntime
          this.$refs.gxedit.cunpeople = value.cunpeople
          this.$refs.gxedit.chutime = value.chutime
          this.$refs.gxedit.chupeople = value.chupeople
          value.gxstatus == 'finish' ? this.$refs.gxedit.status2 = 2 : value.gxstatus == 'wait' ? this.$refs.gxedit.status2 = 0 : this.$refs.gxedit.status2 = 1;
        }else{
          i < this.lastFinishIndex ? this.$refs.gxedit.disStatus = true : this.$refs.gxedit.disStatus = false
          this.$refs.gxedit.type = false
          this.$refs.gxedit.endtime = value.endtime
          this.$refs.gxedit.responsible = value.responsible
          value.gxstatus == 'finish' ? this.$refs.gxedit.status1 = 2 : value.gxstatus == 'wait' ? this.$refs.gxedit.status1 = 0 : this.$refs.gxedit.status1 = 1;
        }
        this.$refs.gxedit.visible = true
      },
      gxEdit(url,params){
        postAction(url,params).then((res)=>{
          if(res.code == 200){
            this.getData();
            this.$message.success(res.message)
          }else {
            this.$message.warning(res.message)
          }
          this.$refs.gxedit.visible = false
        });
      },
      gxChange(value){
        if(this.xuhaoData.xuhao == 7){
          let checkParams = {
            liangceng: value.liangceng,
            taizuoname: value.taizuoname,
            shebeino: value.shebeino,
            status: 1,
            uuid: this.xuhaoData.uuid,
            cluuid: this.model.code
          }
          getAction(this.url.gxcheck,checkParams).then((res)=>{
            if(res.code == 200){
              let params ={...value,...this.xuhaoData}
              this.gxEdit(this.url.gxedit,params)
            }else{
              this.$message.warning(`${res.message}`)
            }
          });
        }else{
          let params ={...value,...this.xuhaoData}
          this.gxEdit(this.url.gxedit,params)
        }
      }
    }
  }
</script>
<style>
.biank{
  width: 150px;
  border: 1px solid #F0F0F0;
  padding: 5px;
  margin-top: 5px;
  border-radius: 5px;
  background-color: #F0F0F0;
  /*color: #faad14;*/
}
.endtime{
  /*color: #222222;*/

}
.tag{
  width: 150px;
  padding: 10px;
  margin-left: 8px;
  text-align: center;
}
.msg-btn{
  width: 150px;
  text-align: center;
  margin-top: 10px;
}

</style>