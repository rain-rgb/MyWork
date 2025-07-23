<template>
  <j-modal
    :title="title"
    :width="800"
    :visible="visible"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭">
      <a-form-model ref="form" :model="model">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="使用时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-date placeholder="请选择使用时间" v-model="model.dosingTime" style="width: 100%"/>
            </a-form-model-item>
          </a-col>
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="配料单号" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-model="model.dosingOrderNumber" placeholder="请输入配料单号"></a-input>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
          <a-col :span="24">
            <a-form-model-item label="工程部位" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <JSelectDqProjName v-model="model.code" ::multi="false"/>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="使用量(kg)" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-model="model.uses" placeholder="请输入使用量(kg)" style="width: 100%"/>
            </a-form-model-item>
          </a-col>
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="料仓名称" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-model="model.storageName" placeholder="请输入材料名称" disabled></a-input>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
          <a-col :span="24">
            <a-form-model-item label="检验批编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
<!--              <j-select-select-tag v-model="model.inspectionLotNumber" placeholder="请输入检验批编号"></j-select-select-tag>-->
                        <j-search-select-tag v-model="model.inspectionLotNumber" :dictOptions="dictOption1">
                        </j-search-select-tag>
            </a-form-model-item>
          </a-col>
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="创建人" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-model="model.createBy" placeholder="请输入创建人" disabled></a-input>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="创建时间" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <j-date v-model="model.createTime" placeholder="请选择创建时间" style="width: 100%"/>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="组织机构" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-model="model.sysOrgCode" placeholder="请输入组织机构"></a-input>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="任务单号" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-model="model.renwudan" placeholder="请输入任务单号"></a-input>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
          <a-col :span="24">
            <a-form-model-item label="领用原因" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-model="model.lingyongyuanyin" placeholder="请输入领用原因"></a-input>
            </a-form-model-item>
          </a-col>
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="料仓id" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-model="model.storageId" placeholder="请输入料仓id"></a-input>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
        </a-row>
      </a-form-model>
  </j-modal>
</template>

<script>

import { getAction, httpAction } from '@api/manage'
import JSelectDqProjName from '@comp/jeecgbiz/JselectDqProjName'

export default {
  name: 'CaiLiaoLy',
  components: {
    JSelectDqProjName
  },
  data() {
    return {
      dictOption1: [],
      title: '材料领用',
      width: 800,
      model: { storageId: '' },
      labelCol: {
        xs: { span: 24 },
        sm: { span: 5 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 },
      },
      selectValue: '',
      url: {
        add:'/yclud/yclUsageDetail/add',
        JYP: '/wzliaocang/wzliaocang/getJYP'
      },
      visible: false,
      disableSubmit: false
    }
  },
  created() {
    // this.getJYP()
  },
  methods: {
    getJYP() {
      getAction(this.url.JYP, { storageId: this.model.storageId }).then((res) => {
        if (res.success) {
          let data=res.result;
          data.forEach(item=>{
            this.dictOption1.push({text:item.pici,value:item.pici})
          })
        } else {
          this.$message.error('检验批查询失败！')
        }
      })
    },
    close() {
      this.$emit('close')
      this.visible = false
    },
    handleCancel() {
      this.visible = false
    },
    handleOk() {
      this.submitForm();
      this.visible = false
      this.routeReload()
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
    routeReload() {
      //刷新页面
      this.reloadFlag = false
      let ToggleMultipage = 'ToggleMultipage'
      this.$store.dispatch(ToggleMultipage, false)
      this.$nextTick(() => {
        this.$store.dispatch(ToggleMultipage, true)
        this.reloadFlag = true
      })
    },
  }
}
</script>