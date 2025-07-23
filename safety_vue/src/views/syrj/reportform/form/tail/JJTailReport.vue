<template>
    <div class="tail">
        <table class="la-table bz">
            <tr>
                <td class="beizhuRemark">
                    <span class="beizhu">附加说明：</span>
                    <textarea v-model="reportData.remark" style="text-indent: 5em"></textarea>
                </td>
            </tr>
        </table>
        <table class="foot">
            <tr>
                <td>检测：</td>
                <td>记录：</td>
                <td>复核：</td>
                <td style="width: 130px">日期：</td>
                <td style="width: 50px">年</td>
                <td style="width: 50px">月</td>
                <td style="width: 20px">日</td>
            </tr>
        </table>
    </div>
</template>
<script>
export default {
    name: 'JJTailReport',
    props: ['formData'],
    components: {},
    data() {
        return {}
    },
    mounted() {},
    computed: {
        reportData: {
            get: function () {
                return this.formData ? this.formData : {}
            },
            set: function (newVal) {
                this.$emit('update:formData', newVal)
                return newVal
            },
        },
    },
    methods: {},
}
</script>
<style lang="less" scoped>
.tail {
    height: 100%;
    .la-table {
        width: 100%;
        margin: 0;
        border-right: 2px solid #000;
        border-left: 2px solid #000;
        color: #000;
        background-color: #fff9ec;
        td {
            padding: 1px;
            height: 26px;
            font-size: 12px;
            line-height: 18px;
            text-align: center;
            border: 1px solid #000;
            position: relative;
        }
        .beizhuRemark {
            border-bottom: 2px solid #000;
        }
        .beizhu {
            position: absolute;
            left: 2px;
            top: 4px;
            line-height: normal;
        }
        textarea {
            width: 100%;
            height: 100%;
            resize: none;
            border: none;
            outline: none;
            padding: 1px;
            vertical-align: middle;
        }
    }
    .bz {
        height: calc(100% - 26px);
        border-top: none;
        margin-top: -1px;
    }
    .foot {
        width: 100%;
    }
}
</style>