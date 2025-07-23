<template>
    <j-modal
        centered
        :title="title"
        :width="800"
        :visible="visible"
        @ok="handleOk"
        :okButtonProps="{ class: { 'jee-hidden': disableSubmit } }"
        @cancel="handleCancel"
        cancelText="关闭"
    >
        <a-form :form="form">
            <a-row>
                <a-col :span="10">
                    <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol">
                        <a-range-picker :style="{ width: '300px' }" @change="onChange" />
                    </a-form-item>
                </a-col>
                <a-col :span="4">
                    <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol">
                        <a-button type="primary" @click="search">搜索</a-button>
                    </a-form-item>
                </a-col>
            </a-row>
            <a-table
                ref="table"
                size="middle"
                bordered
                rowKey="id"
                class="j-table-force-nowrap"
                :scroll="{ y: 210 }"
                :columns="columns"
                :dataSource="dataSource"
                :pagination="ipagination"
                @change="tableChange"
            >
            </a-table>
        </a-form>
    </j-modal>
</template>

<script>
import { getAction } from '@/api/manage'
import Vue from 'vue'

export default {
    name: 'ProjectStatistics',
    components: {},
    data() {
        return {
            dataSource: [],
            startTime: '',
            endTime: '',
            form: this.$form.createForm(this),
            visible: false,
            disableSubmit: true,
            title: '项目统计',
            labelCol: {
                xs: { span: 24 },
                sm: { span: 6 },
            },
            wrapperCol: {
                xs: { span: 24 },
                sm: { span: 16 },
            },
            ipagination: {
                current: 1,
                pageSize: 10,
                pageSizeOptions: ['10', '20', '30'],
                showTotal: (total, range) => {
                    return range[0] + '-' + range[1] + ' 共' + total + '条'
                },
                showQuickJumper: true,
                showSizeChanger: true,
                total: 0,
            },
            columns: [
                {
                    title: '日期',
                    align: 'center',
                    dataIndex: 'stadate',
                    key: 'stadate',
                },
                {
                    title: '桩数',
                    align: 'center',
                    dataIndex: 'mileageid',
                    key: 'mileageid',
                },
                {
                    title: '时间(s)',
                    align: 'center',
                    dataIndex: 'utilname',
                    key: 'utilname',
                },
                {
                    title: '桩长(m)',
                    align: 'center',
                    dataIndex: 'worklength',
                    key: 'worklength',
                    customRender: (text) => Number(text).toFixed(2),
                },
                {
                    title: '节数',
                    align: 'center',
                    dataIndex: 'pilecount',
                    key: 'pilecount',
                },
            ],
            url: {
                listXq: 'devicepipepilestatistics/devicePipepileStatistics/listXq',
            },
        }
    },
    created() {},
    methods: {
        getListXq() {
            let params = {
                pageNo: this.ipagination.current,
                pageSize: this.ipagination.pageSize,
                stadate_begin: this.startTime,
                stadate_end: this.endTime,
            }
            getAction(this.url.listXq, params).then((res) => {
                if (res.success) {
                    this.dataSource = res.result.records
                    if (res.result.total) {
                        this.ipagination.total = res.result.total
                    } else {
                        this.ipagination.total = 0
                    }
                }
            })
        },
        getList() {
            this.getListXq()
            this.visible = true
        },
        tableChange(pagination) {
            this.ipagination.current = pagination.current
            this.ipagination.pageSize = pagination.pageSize
            this.getListXq()
        },
        onChange(date, dateString) {
            this.startTime = dateString[0]
            this.endTime = dateString[1]
        },
        search() {
            this.ipagination.current = 1
            this.ipagination.pageSize = 10
            this.getListXq()
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
<style scoped>
</style>