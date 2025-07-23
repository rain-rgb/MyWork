<template>
    <j-modal
        centered
        :title="title"
        :width="1200"
        :visible="visible"
        switchFullscreen
        @ok="handleOk"
        :okButtonProps="{ class: { 'jee-hidden': disableSubmit } }"
        @cancel="handleCancel"
        cancelText="关闭"
    >
        <div class="cattj">
            <a-button type="primary" ghost>车次数：{{carQuantity}}</a-button>
            <a-button type="primary" ghost>浇筑时长：{{castingTime}}</a-button>
            <a-button type="primary" ghost>超标：{{overProof}}</a-button>
            <a-button type="primary" ghost>闭合：{{close}}</a-button>
            <a-button type="primary" ghost>运输超时：{{transportTimeout}}</a-button>
        </div>
        <a-table
            :rowKey="(record,index)=>index"
            size="middle"
            bordered
            :columns="columns"
            :loading="loading"
            :data-source="carAndMixingStationList"
            :pagination="false"
        >
            <span slot="qianshoutimeSlot" slot-scope="text, record">
                <a-tag color="green" v-if="text">已签收</a-tag>
                <a-tag color="red" v-else>未签收</a-tag>
            </span> 
            <template slot="track" slot-scope="track, record">
                <a @click="trackDetail(record)">查看</a>
            </template>
            <template slot="expandedRowRender" slot-scope="record">
                <a-table
                    :rowKey="(record,index)=>index"
                    :columns="innerColumns"
                    :data-source="record.bhzCementBasePOList"
                    :pagination="false"
                >
                    <span slot="overLevel" slot-scope="overLevel, record">
                        <a-tag color="green" v-if="record.overLevel == 0">合格</a-tag>
                        <a-tag color="orange" v-if="record.overLevel == 1">初级</a-tag>
                        <a-tag color="blue" v-if="record.overLevel == 2">中级</a-tag>
                        <a-tag color="red" v-if="record.overLevel == 3">高级</a-tag>
                    </span>
                    <span slot="state" slot-scope="state, record">
                        <a-tag color="green" v-if="record.state == 30">已闭合</a-tag>
                        <a-tag color="red" v-else>未闭合</a-tag>
                    </span>
                    <template slot="detail" slot-scope="detail, record">
                        <a @click="trackDetail(record)">查看</a>
                    </template>
                </a-table>
            </template>
        </a-table>

        <track-modal ref="track"></track-modal>
    </j-modal>
</template>

<script>
import trackModal from './trackModal1'
// import trackModal from './trackModal'
import { getAction } from '@/api/manage'

export default {
    name: 'TransportDetail',
    components: {
        trackModal
    },
    data() {
        return {
            visible: false,
            loading: false,
            disableSubmit: true,
            title: '运输生产详情',
            carQuantity: '',//车次数
            overProof: '',//超标数
            close: '',//闭合数
            castingTime: '',//浇筑时长
            transportTimeout: '',//运输时长
            carAndMixingStationList: [],
            columns: [
                {
                    title: '序号',
                    dataIndex: '',
                    key: 'rowIndex',
                    width: 60,
                    align: 'center',
                    customRender: function (t, r, index) {
                        return parseInt(index) + 1
                    },
                },
                {
                    title: '运输车号',
                    align: 'center',
                    dataIndex: 'vehicle',
                    key: 'vehicle',
                },
                {
                    title: '司机姓名',
                    align: 'center',
                    dataIndex: 'driver',
                    key: 'driver',
                },
                {
                    title: '运输方量',
                    align: 'center',
                    dataIndex: 'transportVolume',
                    key: 'transportVolume',
                },
                {
                    title: '出场时间',
                    align: 'center',
                    dataIndex: 'dattim',
                    key: 'dattim',
                },
                {
                    title: '签收状态',
                    align: 'center',
                    dataIndex: 'qianshoutime',
                    key: 'qianshoutime',
                    scopedSlots: { customRender: 'qianshoutimeSlot' },
                },
                {
                    title: '运输时长',
                    align: 'center',
                    dataIndex: 'shippingLength',
                    key: 'shippingLength',
                },
                {
                    title: '查看轨迹',
                    align: 'center',
                    dataIndex: 'track',
                    key: 'track',
                    scopedSlots: { customRender: 'track' },
                },
            ],
            innerColumns: [
                {
                    title: '盘序号',
                    dataIndex: '',
                    key: 'rowIndex',
                    width: 90,
                    align: 'center',
                    customRender: function (t, r, index) {
                        return parseInt(index) + 1
                    },
                },
                {
                    title: '单盘方量',
                    align: 'center',
                    dataIndex: 'estimateNumber',
                    key: 'estimateNumber',
                },
                {
                    title: '强度等级',
                    align: 'center',
                    dataIndex: 'strengthRank',
                    key: 'strengthRank',
                },
                {
                    title: '搅拌时间',
                    align: 'center',
                    dataIndex: 'stirDatetime',
                    key: 'stirDatetime',
                },
                {
                    title: '出料时间',
                    align: 'center',
                    dataIndex: 'productDatetime',
                    key: 'productDatetime',
                },
                {
                    title: '超标等级',
                    align: 'center',
                    dataIndex: 'overLevel',
                    key: 'overLevel',
                    scopedSlots: { customRender: 'overLevel' },
                },
                // {
                //     title: '闭合状态',
                //     align: 'center',
                //     dataIndex: 'overproofStatus',
                //     key: 'overproofStatus',
                //     scopedSlots: { customRender: 'state' },
                // },
                // {
                //     title: '查看详情',
                //     align: 'center',
                //     dataIndex: 'detail',
                //     key: 'detail',
                //     scopedSlots: { customRender: 'detail' },
                // },
            ],
        }
    },
    created() {},
    methods: {
        handleCancel() {
            this.visible = false
        },
        handleOk() {
            this.visible = false
        },
        trackDetail(record) {
            this.$refs.track.title = `轨迹回放`
            this.$refs.track.visible = true
            this.$nextTick(()=>{
                // this.$refs.track.Carguiji(record);
                this.$refs.track.detail(record);
            })
        },
        getTransport(code) {
            this.loading = true
            let params = {
                code: code,
            }
            this.carQuantity = ''
            this.castingTime = ''
            this.close = ''
            this.overProof = ''
            this.transportTimeout = ''
            this.carAndMixingStationList = []
            getAction('/car/schedulingCar/getCarAndMixingStationData', params).then((res) => {
                if(res.code == 200){
                    this.carQuantity = res.result.carQuantity
                    this.castingTime = res.result.castingTime
                    this.close = res.result.close
                    this.overProof = res.result.overProof
                    this.transportTimeout = res.result.transportTimeout
                    this.carAndMixingStationList = res.result.carAndMixingStationList
                }else{
                    this.$message.warning(res.message)
                }
            }).finally(() => {
                this.loading = false
            })
        },
    },
}
</script>
<style scoped>
.cattj{
    margin-bottom: 24px;
    display: flex;
    justify-content: space-around;
}
</style>