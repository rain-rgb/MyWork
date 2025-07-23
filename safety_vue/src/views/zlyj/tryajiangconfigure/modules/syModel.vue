<template>
  <j-modal
    centered
    :title="title"
    :width="500"
    :visible="visible"
    @ok="handleOk"
    :okButtonProps="{ class: { 'jee-hidden': disableSubmit } }"
    @cancel="handleCancel"
    cancelText="关闭"
  >
    <!--          <a-input placeholder="委派人员" v-model="newSampleNo"></a-input>-->
    <a-form-model ref="form" :model="model">
      <a-row>
        <a-col :span="12">
          <a-form-model-item label="委派人员" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <!--              <j-select-select-tag v-model="model.inspectionLotNumber" placeholder="请输入检验批编号"></j-select-select-tag>-->
            <j-search-select-tag v-model="model.inspectionLotNumber" :dictOptions="dictOption">
            </j-search-select-tag>
          </a-form-model-item>
        </a-col>
      </a-row>
    </a-form-model>
    <!--    <a-col :xl="4" :lg="7" :md="8" :sm="24">-->
    <!--      <a-form-item label="委派人员">-->
    <!--        <j-search-select-tag v-model="model.zhipaiquyangren" :dictOptions="dictOption">-->
    <!--        </j-search-select-tag>-->
    <!--        {{ selectValue }}-->
    <!--      </a-form-item>-->
    <!--    </a-col>-->
  </j-modal>
</template>
<script>
import { getAction } from '@/api/manage'

export default {
  name: 'syModel',
  components: {},
  data() {
    return {
      model:'',
      dictOption: [],
      visible: false,
      disableSubmit: false,
      title: '指派',
      url: {
        add: '',
        getUser: '/sys/user/list'
      },
    }
  },
  mounted() {
  },
  created() {
    // this.getUser();
  },
  computed: {},
  methods: {
    getUser() {
      getAction(this.url.getUser).then((res) => {
        if (res.success) {
          let data = res.result.records
          data.forEach(item => {
            this.dictOption.push({ text: item.realname, value: item.id })
          })
        } else {
          this.$message.error('无！')
        }
      })
    },

    handleCancel() {
      this.visible = false
    },
    handleOk() {
      this.submitForm();
      this.visible = false
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
  },
}
</script>
<style lang="less" scoped>
</style>