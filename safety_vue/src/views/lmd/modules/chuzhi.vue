<template>
    <div>
        <a-modal
            title="处置"
            :width="width"
            :visible="visible"
            :confirm-loading="confirmLoading"
            @ok="handleOk"
            @cancel="handleCancel"
        >
            <a-form-item label="问题原因" :labelCol="labelCol" :wrapperCol="wrapperCol" centered>
                <a-input placeholder="请输入问题原因" v-model="wtyy"></a-input>
            </a-form-item>
            <a-form-item label="处理方式" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input placeholder="请输入处理方式" v-model="clfs"></a-input>
            </a-form-item>
            <a-form-item label="处理结果" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input placeholder="请输入处理结果" v-model="cljg"></a-input>
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
    name: 'chuzhi',
    components: { JImageUpload },
    props: {
        id: {
            type: String,
            default: '',
        },
        shebeiNo: {
            type: String,
            default: '',
        },
    },
    data() {
        return {
            flag: 2,
            counts: 0,
            fileList: [],
            cljg: '',
            wtyy: '',
            clfs: '',
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
                deal: '/deviceLargeOverHandler/deviceLargeOverHandler/deal',
            },
        }
    },
    methods: {
        subcommit() {
            let param = {
                baseid: this.id,
                flag: this.flag,
                handleResult: this.cljg,
                problemReasons: this.wtyy,
                handleWay: this.clfs,
                filePath2: this.fileList,
                shebeiNo: this.shebeiNo,
                counts: this.counts
            }
            getAction(this.url.deal, param).then((res) => {
                // console.log(res, '处置结果')
                if (res.success) {
                    this.$message.success('处置成功')
                } else {
                    this.$message.error('处置失败')
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
<style scoped>
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