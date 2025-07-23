<template>
    <div>
        <a-modal
            title="审核"
            :width="width"
            :visible="visible"
            :confirm-loading="confirmLoading"
            @ok="handleOk"
            @cancel="handleCancel"
        >
            <a-form-item label="审批意见" :labelCol="labelCol" :wrapperCol="wrapperCol" centered>
                <a-input placeholder="请输入审批意见" v-model="spyj"></a-input>
            </a-form-item>
            <a-form-item label="审批结果" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input placeholder="无" v-model="spjg"></a-input>
            </a-form-item>
            <a-form-item label="上传文件/图片" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-image-upload v-model="fileList" :isMultiple="isMultiple"></j-image-upload>
            </a-form-item>
        </a-modal>
    </div>
</template>
<script>
import { getAction } from '../../../api/manage'
import JImageUpload from '../../../components/jeecg/JImageUpload'
export default {
    name: 'shenhe',
    components: { JImageUpload },
    props: {
        id: {
            type: String,
            default: '',
        }
    },
    data() {
        return {
            flag: 1,
            fileList: [],
            spyj: '',
            spjg: '',
            isMultiple: true,
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
                baseid: this.id,
                flag: this.flag,
                supervisorApproval: this.spyj,
                supervisorResult: this.spjg,
                filePath: this.fileList,
            }
            //console.log(this.id)
            getAction(this.url.edit, param).then((res) => {
                // console.log(res, '审核结果')
                if (res.success) {
                    this.$message.success('审核成功')
                } else {
                    this.$message.error('审核失败')
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