<template>
  <a-modal
    :title="title"
    :width="800"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭"
  >
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
<!--        <a-form-item-->
<!--          :labelCol="labelCol"-->
<!--          :wrapperCol="wrapperCol"-->
<!--          label="名称">-->
<!--          <a-input placeholder="请输入名称" v-decorator.trim="['itemText', validatorRules.itemText]"/>-->
<!--        </a-form-item>-->
        <a-form-item :labelCol="labelCol"
                     :wrapperCol="wrapperCol" label="app功能">
          <j-dict-select-tag placeholder="请分配app功能" v-model="appfunction"
                             dictCode="appfunction" ></j-dict-select-tag>
        </a-form-item>
      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>
import pick from 'lodash.pick'
import { addDictItem, editDictItem, addappfunction } from '@/api/api'
import {pushdepartidShebei} from '@/mixins/pushdepartidShebei'
export default {
  name: 'AppModal',
  //mixins: [pushdepartidShebei],
  data() {

    return {
      title: '操作',
      visible: false,
      visibleCheck: true,
      model: {},
      dictId: '',
      roleId: '',
      appfunction:'',
      status: 1,
      labelCol: {
        xs: { span: 24 },
        sm: { span: 5 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 },
      },
      confirmLoading: false,
      form: this.$form.createForm(this),
      validatorRules: {
        appfunction: { rules: [{ required: true, message: '请输入名称!' }] },
        //itemValue: { rules: [{ required: true, message: '请输入数据值!' }, { validator: this.validateItemValue }] },
      },
    }
  },
  created() {
  },
  methods: {
    add(roleId) {
      this.roleId = roleId
      this.edit({})
    },
    edit(record) {
      if (record.id) {
        this.dictId = record.dictId
        this.visibleCheck = (record.status == 1) ? true : false
      }
      this.form.resetFields()
      this.model = Object.assign({}, record)
      this.model.dictId = this.dictId
      this.model.status = this.status
      this.visible = true
      this.$nextTick(() => {
        this.form.setFieldsValue(pick(this.model, 'itemText', 'itemValue', 'description', 'sortOrder'))
      })
    },
    onChose(checked) {
      if (checked) {
        this.status = 1
        this.visibleCheck = true
      } else {
        this.status = 0
        this.visibleCheck = false
      }
    },
    // 确定
    handleOk() {
      const that = this
      if(that.appfunction==''){
        that.$message.warning("请选择app应用");
        return;
      }
      let formdata={roleids:that.roleId,appfunction:that.appfunction};
      addappfunction(formdata).then((res)=>{
            if (res.success) {
              that.$message.success(res.message)
              that.$emit('ok')
            } else {
              that.$message.warning(res.message)
            }

        setTimeout(() => {
          this.visible = false;
          this.confirmLoading = false;
        }, 1000);
        this.routeReload();
        //that.close()
      })
      // 触发表单验证
      //this.form.validateFields((err, values) => {
       // console.log(values)
        // if (!err) {
        //   that.confirmLoading = true
        //   values.itemText = (values.itemText || '').trim()
        //   values.itemValue = (values.itemValue || '').trim()
        //   let formData = Object.assign(this.model, values)
        //   formData.status = this.status
        //   let obj
        //   if (!this.model.id) {
        //     obj = addappfunction(formData)
        //   }
        //   // else {
        //   //   obj = editDictItem(formData);
        //   // }
        //   obj.then((res) => {
        //     if (res.success) {
        //       that.$message.success(res.message)
        //       that.$emit('ok')
        //     } else {
        //       that.$message.warning(res.message)
        //     }
        //   }).finally(() => {
        //     that.confirmLoading = false
        //     that.close()
        //   })
        // }
      //})
    },
    routeReload() {//选择全局组织机构之后刷新当前的右侧页面  解决 没这个方法之前点击之后缓存数据不更新
      this.reloadFlag = false
      let ToggleMultipage = 'ToggleMultipage'
      this.$store.dispatch(ToggleMultipage, false)
      this.$nextTick(() => {
        this.$store.dispatch(ToggleMultipage, true)
        this.reloadFlag = true
      })
      console.log("刷新页面")
    },
    // 关闭
    handleCancel() {
      this.close()
    },
    close() {
      this.$emit('close')
      this.visible = false
    },
    validateItemValue(rule, value, callback) {
      if (value) {
        let reg = new RegExp('[`_~!@#$^&*()=|{}\'.<>《》/?！￥（）—【】‘；：”“。，、？]')
        if (reg.test(value)) {
          callback('数据值不能包含特殊字符！')
        } else {
          callback()
        }
      } else {
        callback()
      }
    }
  }
}
</script>
