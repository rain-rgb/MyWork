<template>
    <div>
        <div class="check-box" v-for="(item, index) in reportData" :key="index">
            <a-divider orientation="left">{{ item.plist.tiName }}</a-divider>
            <p>
                <a-button type="primary" @click="onCheckAllChange(item, index)">全选</a-button>
            </p>
            <a-checkbox-group v-model="item.checkedList" @change="onChange($event, index)">
                <a-row :gutter="[0, 10]">
                    <a-col v-for="ritem in item.list" :key="ritem.tiNo">
                        <a-checkbox :value="ritem.tiNo">{{ ritem.tiName }}</a-checkbox>
                    </a-col>
                </a-row>
            </a-checkbox-group>
        </div>
    </div>
</template>

<script>
import { getAction } from '@/api/manage'

export default {
    name: 'Report',
    props: {
        id: {
            type: String | Number,
        },
    },
    data() {
        return {
            reportData: [],
        }
    },
    mounted() {
        this.getList()
    },
    methods: {
        getList() {
            let url = '/sylxdps/syDpsJcTestitemtype/getList/' + this.id
            getAction(url).then((res) => {
                if (res.success) {
                    this.reportData = res.result.map((item) => {
                        return {
                            ...item,
                            checkedList: [],
                        }
                    })
                } else {
                    this.reportData = []
                }
            })
        },
        onChange(val, ind) {
            this.reportData.forEach((item, index) => {
                if (index != ind) {
                    item.checkedList = []
                }
            })
            if (val.length > 1) {
                let arr = []
                this.reportData[ind].list.forEach((item) => {
                    val.forEach((ritem) => {
                        if (item.tiNo === ritem) {
                            arr.push(item.tiParameterNum)
                        }
                    })
                })
                for (let i = 0; i < arr.length - 1; i++) {
                    if (arr[i] == arr[arr.length - 1]) {
                        val.pop()
                        this.$message.warning('试验项目不能重复！')
                    }
                }
            }
            let reportName = this.reportData[ind].plist.tiNo
            let str = this.getChange()
            this.$emit('change', str, reportName)
        },
        onCheckAllChange(data, i) {
            this.reportData.forEach((item, index) => {
                if (index != i) {
                    item.checkedList = []
                }
            })
            if (data.list.length > 1) {
                let arr = this.getTempArr(data.list)
                data.checkedList = arr.map((item) => item.tiNo)
                if (arr.length < data.list.length) {
                    this.$message.warning('试验项目不能重复！')
                }
            } else {
                data.checkedList = data.list.map((item) => item.tiNo)
            }
            let reportName = data.plist.tiNo
            let str = this.getChange()
            this.$emit('change', str, reportName)
        },
        getChange() {
            let arr = []
            this.reportData.forEach((item) => {
                arr = [...arr, ...item.checkedList]
            })
            return arr.join()
        },
        getTempArr(tempArr) {
            let result = []
            let obj = {}
            for (let i = 0; i < tempArr.length; i++) {
                if (!obj[tempArr[i].tiParameterNum]) {
                    result.push(tempArr[i])
                    obj[tempArr[i].tiParameterNum] = true
                }
            }
            return result
        },
    },
    watch: {
        id() {
            this.getList()
        },
    },
}
</script>

<style scoped>
</style>