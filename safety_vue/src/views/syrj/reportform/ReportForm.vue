<template>
    <j-modal
        centered
        :title="title"
        :width="1000"
        :visible="visible"
        switchFullscreen
        @ok="handleOk"
        :okButtonProps="{ class: { 'jee-hidden': disableSubmit } }"
        @cancel="handleCancel"
        cancelText="关闭"
    >
        <a-tabs type="card">
            <!-- <a-tab-pane key="form" tab="表单">
                <component :is="'SyDpsSySampleForm'"></component>
            </a-tab-pane> -->
            <a-tab-pane v-for="(tab, i) in reportList" :key="tab.uuid" :tab="tab.tiName">
                <div :id="'printContent' + i">
                    <a-button class="not-print print" v-print="`#printContent${i}`" type="primary">打印</a-button>
                    <a-button class="not-print save" type="primary" @click="saveReport(i)">保存</a-button>
                    <component ref="report" :formData.sync="formData[i]" :is="getComponent(tab.tiNo)"></component>
                </div>
            </a-tab-pane>
        </a-tabs>
    </j-modal>
</template>
<script>
import { getAction, postAction } from '@/api/manage'
import SyDpsSySampleForm from '@/views/sy/dpssysample/modules/SyDpsSySampleForm'

export default {
    name: 'ReportForm',
    components: {
        SyDpsSySampleForm,
    },
    data() {
        return {
            componentMap: {},
            reportList: [],
            visible: false,
            disableSubmit: true,
            title: '详情',
            formData: [],
            sampleForm: {},
        }
    },
    mounted() {},
    computed: {},
    methods: {
        saveReport(i) {
            let params = {
                bg: this.$refs.report[i].bg,
                hz: {},
                ...this.formData[i],
            }
            postAction('/sydpssysample/syDpsSySample/save', params).then((res) => {
                if (res.success) {
                    this.$message.success('保存成功！')
                } else {
                    this.$message.error('保存失败！')
                }
            })
        },
        show(id) {
            this.visible = true
            this.$nextTick(() => {
                this.getReport(id)
            })
        },
        getReport(uuid) {
            this.reportList = []
            this.sampleForm = {}
            this.formData = []
            let url = 'sydpssysample/syDpsSySample/getSList/' + uuid
            getAction(url).then((res) => {
                if (res.success) {
                    this.reportList = res.result.list
                    this.sampleForm = res.result.sample
                    if (this.reportList.length > 0) {
                        this.reportList.forEach((item, i) => {
                            this.getList(item.uuid, i)
                        })
                    } else {
                        this.$message.warn('报表数据为空！')
                        this.visible = false
                    }
                } else {
                    this.$message.error(res.message)
                    this.visible = false
                }
            })
        },
        getList(uuid, i) {
            let params = {
                id: uuid,
            }
            getAction('/sylxdps/syDpsJcTestitem/findById', params).then((res) => {
                if (res.success) {
                    this.$set(this.formData, i, res.result)
                }
            })
        },
        getComponent(name) {
            if (this.componentMap[name]) {
                return this.componentMap[name]
            }
            const syncImport = () => import(`@/views/syrj/reportform/form/${name}.vue`)
            this.componentMap[name] = syncImport
            return syncImport
        },
        handleCancel() {
            this.visible = false
        },
        handleOk() {
            this.visible = false
        },
    },
}
</script>
<style lang="less" scoped>
.print {
    position: absolute;
    top: 6%;
    right: 15px;
}
.save {
    position: absolute;
    top: 6%;
    left: 15px;
}
@media print {
    /* 隐藏不需要打印的部分 */
    .not-print {
        display: none !important;
    }
}
</style>