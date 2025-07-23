<template>
    <div>
        <a-modal
            title="驳回"
            :width="width"
            :visible="visible"
            :confirm-loading="confirmLoading"
            @ok="handleOk"
            @cancel="handleCancel"
        >
            <a-form-item label="驳回原因" :labelCol="labelCol" :wrapperCol="wrapperCol" centered>
                <a-input placeholder="请输入驳回原因" v-model="beizhu"></a-input>
            </a-form-item>
        </a-modal>
    </div>
</template>
<script>
import { getAction } from '../../../api/manage'
import JImageUpload from '../../../components/jeecg/JImageUpload'

export default {
    name: 'bohui',
    components: { JImageUpload },
    props: {
        id: {
            type: String,
            default: '',
        }
    },
    data() {
        return {
            flag: 3,
            beizhu: '',
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
                edit: '/deviceLargeOverHandler/deviceLargeOverHandler/deal',
            },
        }
    },
    methods: {
        subcommit() {
            let param = {
                remark: this.beizhu,
                baseid: this.id,
                flag: this.flag,
            }
            //console.log(this.id)
            getAction(this.url.edit, param).then((res) => {
                // console.log(res, '驳回结果')
                if (res.success) {
                    this.$message.success('驳回成功')
                } else {
                    this.$message.error('驳回失败')
                }
                this.confirmLoading = false
                this.visible = false
                // const demo = 0;
                // this.$emit('change', demo);
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
        handleOk(e) {
            this.confirmLoading = true
            // formData.img = that.fileList;
            setTimeout(() => {
                this.subcommit()
            }, 1000)
        },
        handleCancel(e) {
            this.visible = false
            // const demo = 0
            // this.$emit('change', demo)
        },
    },
}
</script>
<style>
.ant-upload-picture-card-wrapper {
    display: flex !important;
    flex-wrap: wrap;
    padding: 0 150px;
}
.ant-upload-picture-card-wrapper::before {
    display: block;
}

.ant-upload.ant-upload-select-picture-card {
    display: block;
}
.ant-upload-select-picture-card i {
    font-size: 32px;
    color: #999;
}

.ant-upload-select-picture-card .ant-upload-text {
    margin-top: 8px;
    color: #666;
}
.ant-upload-list {
    display: flex;
    flex-wrap: wrap;
}

.ant-upload.ant-upload-select-picture-card > .ant-upload {
    display: block !important;
    margin-top: 15px;
}
</style>