<template>
    <div>
        <a-tree-select
            v-model="treeValue"
            style="width: 100%"
            :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
            :tree-data="treeData"
            :replace-fields="replaceFields"
            :placeholder="placeholder"
            allow-clear
        >
        </a-tree-select>
    </div>
</template>

<script>
import { getAction } from '@/api/manage'

export default {
    name: 'SampleType',
    props: {
        type: {
            type: Number,
            default: 0,
        },
        value: {
            type: String | Number,
        },
        placeholder: {
            type: String,
            default: '请选择',
        },
    },
    data() {
        return {
            treeData: [],
            replaceFields: {
                children: 'children',
                title: 'titname',
                key: 'titCode',
                value: 'titcode',
            },
            url: {
                query: '/sylxdps/syDpsJcTestitemtype/queryItemType',
            },
        }
    },
    mounted() {
        this.getList()
    },
    methods: {
        getList() {
            let params = {
                type: this.type,
            }
            getAction(this.url.query, params).then((res) => {
                if (res.success) {
                    this.treeData = res.result
                }
            })
        },
    },
    computed: {
        treeValue: {
            get: function () {
                return this.value
            },
            set: function (newValue) {
                this.$emit('change', newValue)
                return newValue
            },
        },
    },
    model: {
        prop: 'value',
        event: 'change',
    },
}
</script>

<style scoped>
</style>