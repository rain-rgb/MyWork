<template>
    <a-modal
        title="修正"
        :width="width"
        :visible="visible"
        :confirm-loading="confirmLoading"
        @ok="handleOk"
        @cancel="handleCancel"
    >
        <a-form-item label="修正值" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input-number placeholder="请输入修正值(限数字)" v-model="correctValue" style="width: 100%"></a-input-number>
        </a-form-item>
        <a-form-item label="修正原因" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input placeholder="请输入修正原因" v-model="correctReason"></a-input>
        </a-form-item>
    </a-modal>
</template>

<script>
import { getAction, postAction } from '@/api/manage'

export default {
    name: 'correct',
    components: {},
    data() {
        return {
            correctValue: '',
            correctReason: '',
            guid: '',
            labelCol: {
                xs: { span: 24 },
                sm: { span: 6 },
            },
            wrapperCol: {
                xs: { span: 24 },
                sm: { span: 16 },
            },
            width: 800,
            visible: false,
            confirmLoading: false,
            url: {
                add: '/wzliaocang/wzliaocangXz/add',
            },
        }
    },
    methods: {
        subcommit() {
            let param = {
                guid: this.guid,
                updateValue: this.correctValue,
                remark: this.correctReason,
            }
            postAction(this.url.add, param).then((res) => {
                console.log(res, '修正结果')
                if (res.success) {
                    this.$message.success('修正成功')
                } else {
                    this.$message.error('修正失败')
                }
                this.confirmLoading = false
                this.visible = false
            })
            this.routeReload()
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
            console.log('刷新页面')
        },
        handleOk() {
            this.confirmLoading = true
            setTimeout(() => {
                this.subcommit()
                this.correctValue = ''
                this.correctReason = ''
            }, 1000)
        },
        handleCancel() {
            this.visible = false
        },
    },
}
</script>
<style scoped>
</style>