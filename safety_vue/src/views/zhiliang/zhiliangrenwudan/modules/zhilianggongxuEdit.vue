<template>
    <a-modal
        centered
        :title="title"
        :width="1200"
        :visible="visible"
        @ok="handleOk"
        @cancel="handleCancel"
        cancelText="关闭"
    >
        <div class="select-msg" v-show="!type">
            <div>
                <span>完成时间：</span>
                <j-date placeholder="开始生产日期" v-model="endtime" :showTime="true" dateFormat="YYYY-MM-DD HH:mm:ss"/>
            </div>
            <div>
                <span>责任人：</span>
                <a-input style="width:195px;" placeholder="请输入责任人" allowClear v-model="responsible"></a-input>
            </div>
            <div>
                <span>是否完成：</span>
                <j-dict-select-tag style="width:195px;" :disabled="disStatus" type="list" v-model.number="status1" :trigger-change="true" dictCode="status1"/>
            </div>
        </div>
        <div class="select-msg" v-show="type">
            <!-- <div style="width:100%;color:red;text-align: center;">梁场→台座→存梁层</div> -->
            <div>
                <span>存梁时间：</span>
                <j-date placeholder="开始生产日期" v-model="cuntime" :showTime="true" dateFormat="YYYY-MM-DD HH:mm:ss"/>
            </div>
            <div>
                <span>存梁责任人：</span>
                <a-input style="width:195px;" placeholder="请输入存梁责任人" allowClear v-model="cunpeople"></a-input>
            </div>
            <div>
                <span>取梁时间：</span>
                <j-date placeholder="开始生产日期" v-model="chutime" :showTime="true" dateFormat="YYYY-MM-DD HH:mm:ss"/>
            </div>
            <div>
                <span>取梁责任人：</span>
                <a-input style="width:195px;" placeholder="请输入取梁责任人" allowClear v-model="chupeople"></a-input>
            </div>
            <div>
                <span>是否完成：</span>
                <j-dict-select-tag style="width:195px;" :disabled="disStatus2" type="list" v-model.number="status2" :trigger-change="true" dictCode="status1"/>
            </div>
            <div>
                <span>梁场：</span>
                <j-search-select-tag style="width:195px;" placeholder="请选择梁场" v-model="shebeino" :dictOptions="dictOption1" >
                </j-search-select-tag>
            </div>
            <div>
                <span>台座：</span>
                <j-search-select-tag style="width:195px;" placeholder="请选择台座" v-model="taizuoname" :dictOptions="dictOption2" >
                </j-search-select-tag>
            </div>
            <div>
                <span>存梁层：</span>
                <j-search-select-tag style="width:195px;" placeholder="请选择存梁层" v-model="liangceng" :dictOptions="dictOption3" >
                </j-search-select-tag>
            </div>
        </div>
    </a-modal>
</template>

<script>
import { usershebeiList } from '@api/api'
import Vue from 'vue'
import { getAction } from '@/api/manage'

export default {
    name: 'zhilianggongxuEdit',
    data() {
        return {
            shebeino: null,
            taizuoname: null,
            liangceng: null,
            cunlianghang: '',
            cunlianglie: '',
            dictOption1: [],
            dictOption2: [],
            dictOption3: [],
            visible: false,
            title: '编辑',
            type: false,
            disStatus: false,
            disStatus2: false,
            status1: 0,
            status2: 0,
            endtime: '', //完成时间
            responsible: '', //责任人
            cuntime: '', //存梁时间
            cunpeople: '', //存梁责任人
            chutime: '', //取梁时间
            chupeople: '', //取梁责任人
        }
    },
    watch: {
        shebeino() {
            this.taizuoname = null
            this.liangceng = null
            this.gettaizuo()
        },
        taizuoname() {
            this.liangceng = null
            this.getchl()
        },
    },
    mounted() {
        this.shebeiList()
    },
    methods: {
        shebeiList() {
            var sys_depart_orgcode = Vue.ls.get('SYS_DEPART_ORGCODE')
            usershebeiList({
                sys_depart_orgcode: sys_depart_orgcode,
                sbtypes: '41',
            }).then((res) => {
                this.dictOption1 = []
                res.result.forEach((re) => {
                    this.dictOption1.push({ text: re.sbname, value: re.sbjno })
                })
            })
        },
        // 获取台座
        gettaizuo() {
            let params = {
                shebeino: this.shebeino,
            }
            getAction('/cunliangprocedureconfig/cunliangProcedureConfig/list2', params).then((res) => {
                this.dictOption2 = []
                res.result.forEach((re) => {
                    this.dictOption2.push({ text: re.liangzuoname, value: re.liangzuoname })
                })
            })
        },
        // 获取存梁层和行列
        getchl() {
            let params = {
                shebeino: this.shebeino,
                liangzuoname: this.taizuoname,
            }
            getAction('/cunliangprocedureconfig/cunliangProcedureConfig/list2', params).then((res) => {
                this.dictOption3 = []
                if (res.result.length > 0) {
                    this.cunlianghang = res.result[0].lianghang
                    this.cunlianglie = res.result[0].lianglie
                    for (let i = 1; i <= res.result[0].cengshu; i++) {
                        this.dictOption3.push({ text: i, value: i })
                    }
                }
            })
        },
        handleCancel() {
            this.visible = false
        },
        handleOk() {
            if (this.type) {
                if (!this.shebeino) {
                    this.$message.warning('请选择梁场！')
                    return false
                }
                if (!this.taizuoname) {
                    this.$message.warning('请选择台座！')
                    return false
                }
                if (!this.liangceng) {
                    this.$message.warning('请选择存梁层！')
                    return false
                }
                let obj = {
                    cuntime: this.cuntime,
                    cunpeople: this.cunpeople,
                    chutime: this.chutime,
                    chupeople: this.chupeople,
                    status: this.status2,
                    shebeino: this.shebeino,
                    liangceng: this.liangceng,
                    taizuoname: this.taizuoname,
                    responsible: this.chupeople || this.cunpeople,
                    cunlianghang: this.cunlianghang,
                    cunlianglie: this.cunlianglie,
                }
                this.$emit('change', obj)
            } else {
                let obj = {
                    finishtime: this.endtime || null,
                    responsible: this.responsible,
                    status: this.status1,
                }
                this.$emit('change', obj)
            }
            // this.visible = false
        },
    },
}
</script>
<style scoped>
.select-msg {
    height: 100px;
    display: flex;
    justify-content: space-around;
    align-content: space-around;
    flex-wrap: wrap;
}
</style>