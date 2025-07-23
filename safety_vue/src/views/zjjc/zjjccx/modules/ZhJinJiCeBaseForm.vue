<template>
  <a-spin :spinning="confirmLoading">
    <a-tabs default-active-key="1">
      <a-tab-pane key="1" tab="桩信息" :column="{ xs: 8, sm: 16, md: 24}">
        <a-descriptions bordered>
          <a-descriptions-item label="桩号:">
            {{ lists.shizhuangno }}
          </a-descriptions-item>
          <a-descriptions-item label="桩长(m):">
            {{ lists.zhuangchang }}
          </a-descriptions-item>
          <a-descriptions-item label="桩径(mm):">
            {{ lists.zhuangjing }}
          </a-descriptions-item>
          <a-descriptions-item label="管数:">
            {{ lists.guanshu }}
          </a-descriptions-item>
          <a-descriptions-item label="方位角:">
            {{ lists.fangweijiao }}
          </a-descriptions-item>
          <a-descriptions-item label="声测管外径(mm):">
            {{ lists.shengceguanwaijing }}
          </a-descriptions-item>
          <a-descriptions-item label="声测管内径(mm):">
            {{ lists.shengceguanneijing }}
          </a-descriptions-item>
          <a-descriptions-item label="声测管材料速度(km/s):">
            {{ lists.shengceguan }}
          </a-descriptions-item>
          <a-descriptions-item label="剖面数(mm):">
            {{ lists.poumianshu }}
          </a-descriptions-item>
          <a-descriptions-item label="采样频率(μs):">
            {{ lists.caiyangpinlv }}
          </a-descriptions-item>
          <a-descriptions-item label="采样长度:">
            {{ lists.caiyanglength }}
          </a-descriptions-item>
          <a-descriptions-item label="轮径(mm):">
            {{ lists.lunjing }}
          </a-descriptions-item>
          <a-descriptions-item label="线径(mm):">
            {{ lists.xianjing }}
          </a-descriptions-item>
        </a-descriptions>
      </a-tab-pane>
      <a-tab-pane key="2" tab="监测信息" force-render :column="24">
        <a-row :column="24">
          <a-col :span="2">
            <div>
              剖面：
            </div>
          </a-col>
          <a-col :span="2">
            <div>
              测试深度(m)：
            </div>
          </a-col>
          <a-col :span="3">
            <div>
              传感器高差(m)：
            </div>
          </a-col>
          <a-col :span="2">
            <div>
              跨距(mm)：
            </div>
          </a-col>
          <a-col :span="2">
            <div>
              增益：
            </div>
          </a-col>
          <a-col :span="2">
            <div>
              校零(μs)：
            </div>
          </a-col>
          <a-col :span="2">
            <div>
              延迟时间(μs)：
            </div>
          </a-col>
          <a-col :span="2">
            <div>
              高通(kHz)：
            </div>
          </a-col>
          <a-col :span="2">
            <div>
              低通(kHz)：
            </div>
          </a-col>
        </a-row>
        <a-row :column="24">
          <a-col :span="index===2 ? 3 : 2" v-for="(item,index) in list1" v-bind:key="item.id">
            <span>{{ item }}</span>
          </a-col>
        </a-row>
        <a-row :column="24">
          <a-col :span="index===2 ? 3 : 2" v-for="(item,index) in list2" v-bind:key="item.id">
            <span>{{ item }}</span>
          </a-col>
        </a-row>
        <a-row :column="24">
          <a-col :span="index===2 ? 3 : 2" v-for="(item,index) in list3" v-bind:key="item.id">
            <span>{{ item }}</span>
          </a-col>
        </a-row>
        <a-row :column="24">
          <a-col :span="index===2 ? 3 : 2" v-for="(item,index) in list4" v-bind:key="item.id">
            <span>{{ item }}</span>
          </a-col>
        </a-row>
        <a-row :column="24">
          <a-col :span="index===2 ? 3 : 2" v-for="(item,index) in list5" v-bind:key="item.id">
            <span>{{ item }}</span>
          </a-col>
        </a-row>
        <a-row :column="24">
          <a-col :span="index===2 ? 3 : 2" v-for="(item,index) in list6" v-bind:key="item.id">
            <span>{{ item }}</span>
          </a-col>
        </a-row>
        <a-row :column="24">
          <a-col :span="index===2 ? 3 : 2" v-for="(item,index) in list7" v-bind:key="item.id">
            <span>{{ item }}</span>
          </a-col>
        </a-row>
        <a-row :column="24" v-if="list9.length>0">
          <a-col :span="index===2 ? 3 : 2" v-for="(item,index) in list8" v-bind:key="item.id">
            <span>{{ item }}</span>
          </a-col>
        </a-row>
        <a-row :column="24">
          <a-col :span="index===2 ? 3 : 2" v-for="(item,index) in list9" v-bind:key="item.id">
            <span>{{ item }}</span>
          </a-col>
        </a-row>
        <a-row :column="24">
          <a-col :span="index===2 ? 3 : 2" v-for="(item,index) in list10" v-bind:key="item.id">
            <span>{{ item }}</span>
          </a-col>
        </a-row>
      </a-tab-pane>
      <a-tab-pane key="3" tab="曲线">
        <a-row>
          <a-col :xl="24" :lg="12" :md="12" :sm="24" :xs="24">
            <a-col :sm="24" :md="12" :xl="6" :style="{ marginBottom: '24px' }">
            </a-col>
            <div v-if="pmhs1.length>0">
              <LinCharMultideare :title="'剖面'+pmhs1[0]" :data-source="dataSource1"/>
            </div>
          </a-col>
          <a-col :xl="24" :lg="12" :md="12" :sm="24" :xs="24">
            <a-col :sm="24" :md="12" :xl="6" :style="{ marginBottom: '24px' }">
            </a-col>
            <div v-if="pmhs2.length>0">
              <LinCharMultideare :title="'剖面'+pmhs2[0]" :data-source="dataSource2"/>
            </div>
          </a-col>
          <a-col :xl="24" :lg="12" :md="12" :sm="24" :xs="24">
            <a-col :sm="24" :md="12" :xl="6" :style="{ marginBottom: '24px' }">
            </a-col>
            <div v-if="pmhs3.length>0">
              <LinCharMultideare :title="'剖面'+pmhs3[0]" :data-source="dataSource3"/>
            </div>
          </a-col>
          <a-col :xl="24" :lg="12" :md="12" :sm="24" :xs="24">
            <a-col :sm="24" :md="12" :xl="6" :style="{ marginBottom: '24px' }">
            </a-col>
            <div v-if="pmhs4.length>0">
              <LinCharMultideare :title="'剖面'+pmhs4[0]" :data-source="dataSource4"/>
            </div>
          </a-col>
          <a-col :xl="24" :lg="12" :md="12" :sm="24" :xs="24">
            <a-col :sm="24" :md="12" :xl="6" :style="{ marginBottom: '24px' }">
            </a-col>
            <div v-if="pmhs5.length>0">
              <LinCharMultideare :title="'剖面'+pmhs5[0]" :data-source="dataSource5"/>
            </div>
          </a-col>
          <a-col :xl="24" :lg="12" :md="12" :sm="24" :xs="24">
            <a-col :sm="24" :md="12" :xl="6" :style="{ marginBottom: '24px' }">
            </a-col>
            <div v-if="pmhs6.length>0">
              <LinCharMultideare :title="'剖面'+pmhs6[0]" :data-source="dataSource6"/>
            </div>
          </a-col>
          <a-col :xl="24" :lg="12" :md="12" :sm="24" :xs="24">
            <a-col :sm="24" :md="12" :xl="6" :style="{ marginBottom: '24px' }">
            </a-col>
            <div v-if="pmhs7.length>0">
              <LinCharMultideare :title="'剖面'+pmhs7[0]" :data-source="dataSource7"/>
            </div>
          </a-col>
          <a-col :xl="24" :lg="12" :md="12" :sm="24" :xs="24">
            <a-col :sm="24" :md="12" :xl="6" :style="{ marginBottom: '24px' }">
            </a-col>
            <div v-if="pmhs8.length>0">
              <LinCharMultideare :title="'剖面'+pmhs8[0]" :data-source="dataSource8"/>
            </div>
          </a-col>
          <a-col :xl="24" :lg="12" :md="12" :sm="24" :xs="24">
            <a-col :sm="24" :md="12" :xl="6" :style="{ marginBottom: '24px' }">
            </a-col>
            <div v-if="pmhs9.length>0">
              <LinCharMultideare :title="'剖面'+pmhs9[0]" :data-source="dataSource9"/>
            </div>
          </a-col>
          <a-col :xl="24" :lg="12" :md="12" :sm="24" :xs="24">
            <a-col :sm="24" :md="12" :xl="6" :style="{ marginBottom: '24px' }">
            </a-col>
            <div v-if="pmhs10.length>0">
              <LinCharMultideare :title="'剖面'+pmhs10[0]" :data-source="dataSource10"/>
            </div>
          </a-col>
        </a-row>
      </a-tab-pane>
      <a-tab-pane key="4" tab="波列图">
        <viewer :images="detailList">
          <img
            v-for="(item,index) in detailList"
            :key="index"
            style="height:550px;width: 550px;margin: 5px 10px 5px 10px;float: left"
            :src="apipostimage+'/chaoshengbo/chaoshengboSybltsj/list2?id='+item.id"
            :alt="'剖面'">
       </viewer>
      </a-tab-pane>
    </a-tabs>
    <a-col v-if="showFlowSubmitButton" :span="24" style="text-align: center">
      <a-button @click="submitForm">提 交</a-button>
    </a-col>
  </a-spin>
</template>

<script>

import { httpAction, getAction, getActionblob } from '@/api/manage'
import pick from 'lodash.pick'
import { validateDuplicateValue } from '@/utils/util'
import JFormContainer from '@/components/jeecg/JFormContainer'
import JDate from '@/components/jeecg/JDate'
import LinCharMultideare from '@comp/dhzx/LinCharMultideare'

export default {
  name: 'ZhJinJiCeBaseForm',
  components: {
    JFormContainer,
    JDate,
    LinCharMultideare
  },
  props: {
    //流程表单data
    formData: {
      type: Object,
      default: () => {
      },
      required: false
    },
    //表单模式：true流程表单 false普通表单
    formBpm: {
      type: Boolean,
      default: false,
      required: false
    },
    //表单禁用
    disabled: {
      type: Boolean,
      default: false,
      required: false
    }
  },
  data() {
    return {
      apipostimage:window._CONFIG['domianURL'] || '/jeecg-boot',
      imgsrc:'',
      detailList: [],
      lists: [],
      dataSource1: [],
      dataSource2: [],
      dataSource3: [],
      dataSource4: [],
      dataSource5: [],
      dataSource6: [],
      dataSource7: [],
      dataSource8: [],
      dataSource9: [],
      dataSource10: [],
      list1: [],
      list2: [],
      list3: [],
      list4: [],
      list5: [],
      list6: [],
      list7: [],
      list8: [],
      list9: [],
      list10: [],
      pmhs1: [],
      pmhs2: [],
      pmhs3: [],
      pmhs4: [],
      pmhs5: [],
      pmhs6: [],
      pmhs7: [],
      pmhs8: [],
      pmhs9: [],
      pmhs10: [],
      form: this.$form.createForm(this),
      model: {},
      labelCol: {
        xs: { span: 24 },
        sm: { span: 5 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 },
      },
      confirmLoading: false,
      validatorRules: {},
      url: {
        add: '/chaoshengbo/chaoshengboSyjbsj/add',
        edit: '/chaoshengbo/chaoshengboSyjbsj/edit',
        queryById: '/chaoshengbo/chaoshengboSyjbsj/queryById',
        list: '/chaoshengbo/chaoshengboSybsj/list1',
        list1: '/chaoshengbo/chaoshengboSyjbsj/list1',
        list2: '/chaoshengbo/chaoshengboSybltsj/list1',
        list3:'/chaoshengbo/chaoshengboSybltsj/list2'
      }
    }
  },
  computed: {
    formDisabled() {
      if (this.formBpm === true) {
        if (this.formData.disabled === false) {
          return false
        }
        return true
      }
      return this.disabled
    },
    showFlowSubmitButton() {
      if (this.formBpm === true) {
        if (this.formData.disabled === false) {
          return true
        }
      }
      return false
    }
  },
  created() {
    //如果是流程中表单，则需要加载流程表单data
    this.showFlowData()
  },
  methods: {
    add() {
      this.edit({})
    },
    edit(record) {
      this.form.resetFields()
      this.model = Object.assign({}, record)
      this.visible = true
      this.$nextTick(() => {
        this.form.setFieldsValue(pick(this.model))
        this.bofu()
        this.lists = this.model
        this.jiance()
        this.bolie()
      })
    },
    arrayBufferToBase64  (buffer) {
      var binary = ''
      var bytes = new Uint8Array(buffer)
      var len = bytes.byteLength
      for (var i = 0; i < len; i++) {
        binary += String.fromCharCode(bytes[i])
      }
      return window.btoa(binary)
    },
    //渲染流程表单数据
    showFlowData() {
      if (this.formBpm === true) {
        let params = { id: this.formData.dataId }
        getAction(this.url.queryById, params).then((res) => {
          if (res.success) {
            this.edit(res.result)
          }
        })
      }
    },
    submitForm() {
      const that = this
      // 触发表单验证
      this.form.validateFields((err, values) => {
        if (!err) {
          that.confirmLoading = true
          let httpurl = ''
          let method = ''
          if (!this.model.id) {
            httpurl += this.url.add
            method = 'post'
          } else {
            httpurl += this.url.edit
            method = 'put'
          }
          let formData = Object.assign(this.model, values)
          // console.log('表单提交数据', formData)
          httpAction(httpurl, formData, method).then((res) => {
            if (res.success) {
              that.$message.success(res.message)
              that.$emit('ok')
            } else {
              that.$message.warning(res.message)
            }
          }).finally(() => {
            that.confirmLoading = false
          })
        }

      })
    },
    bofu: function () {//波幅 波速曲线
      let params = {
        shebeino: this.model.shebeino,
        liushuihao: this.model.liushuihao,
        shizhuangno: this.model.shizhuangno
      }
      getAction(this.url.list, params)
        .then(res => {
          if (res.success) {
            let data = res.result
            this.dataSource1 = []
            this.dataSource2 = []
            this.dataSource3 = []
            this.dataSource4 = []
            this.dataSource5 = []
            this.dataSource6 = []
            this.dataSource7 = []
            this.dataSource8 = []
            this.dataSource9 = []
            this.dataSource10 = []
            this.pmhs1 = []
            this.pmhs2 = []
            this.pmhs3 = []
            this.pmhs4 = []
            this.pmhs5 = []
            this.pmhs6 = []
            this.pmhs7 = []
            this.pmhs8 = []
            this.pmhs9 = []
            this.pmhs10 = []
            //console.log(data,"data")
            if (data.length > 0) {
              for (let i = 0; i < data.length; i++) {
                if (data[i].poumianhao1 != null && data[i].poumianhao1 != '') {
                  this.pmhs1 = data[i].poumianhao1.split(',')
                  let pmhlist = this.pmhs1
                  // console.log(pmhlist[4],"this.pmhs1_________________")
                  this.dataSource1.push({ type: data[i].shendu, '波幅': parseFloat(this.pmhs1[3]),'波速': (parseFloat(this.pmhs1[1])/parseFloat(this.pmhs1[2])*1000).toFixed(2)})
                }
                //console.log(this.dataSource1,"this.dataSource1")
                if (data[i].poumianhao2 != null && data[i].poumianhao2 != '') {
                  this.pmhs2 = data[i].poumianhao2.split(',')
                  this.dataSource2.push({ type: data[i].shendu, '波幅': parseFloat(this.pmhs2[3]),'波速': (parseFloat(this.pmhs2[1])/parseFloat(this.pmhs2[2])*1000).toFixed(2) })
                }
                if (data[i].poumianhao3 != null && data[i].poumianhao3 != '') {
                  this.pmhs3 = data[i].poumianhao3.split(',')
                  this.dataSource3.push({ type: data[i].shendu, '波幅': parseFloat(this.pmhs3[3]) ,'波速': (parseFloat(this.pmhs3[1])/parseFloat(this.pmhs3[2])*1000).toFixed(2)})
                }
                if (data[i].poumianhao4 != null && data[i].poumianhao4 != '') {
                  this.pmhs4 = data[i].poumianhao4.split(',')
                  this.dataSource4.push({ type: data[i].shendu, '波幅': parseFloat(this.pmhs4[3]),'波速': (parseFloat(this.pmhs4[1])/parseFloat(this.pmhs4[2])*1000).toFixed(2) })
                }
                if (data[i].poumianhao5 != null && data[i].poumianhao5 != '') {
                  this.pmhs5 = data[i].poumianhao5.split(',')
                  this.dataSource5.push({ type: data[i].shendu, '波幅': parseFloat(this.pmhs5[3]) ,'波速': (parseFloat(this.pmhs5[1])/parseFloat(this.pmhs5[2])*1000).toFixed(2)})
                }
                if (data[i].poumianhao6 != null && data[i].poumianhao6 != '') {
                  this.pmhs6 = data[i].poumianhao6.split(',')
                  this.dataSource6.push({ type: data[i].shendu, '波幅': parseFloat(this.pmhs6[3]) ,'波速': (parseFloat(this.pmhs6[1])/parseFloat(this.pmhs6[2])*1000).toFixed(2)})
                }
                if (data[i].poumianhao7 != null && data[i].poumianhao7 != '') {
                  this.pmhs7 = data[i].poumianhao7.split(',')
                  this.dataSource7.push({ type: data[i].shendu, '波幅': parseFloat(this.pmhs7[3]) ,'波速': (parseFloat(this.pmhs7[1])/parseFloat(this.pmhs7[2])*1000).toFixed(2)})
                }
                if (data[i].poumianhao8 != null && data[i].poumianhao8 != '') {
                  this.pmhs8 = data[i].poumianhao8.split(',')
                  this.dataSource8.push({ type: data[i].shendu, '波幅': parseFloat(this.pmhs8[3]) ,'波速': (parseFloat(this.pmhs8[1])/parseFloat(this.pmhs8[2])*1000).toFixed(2)})
                }
                if (data[i].poumianhao9 != null && data[i].poumianhao9 != '') {
                  this.pmhs9 = data[i].poumianhao9.split(',')
                  this.dataSource9.push({ type: data[i].shendu, '波幅': parseFloat(this.pmhs9[3]) ,'波速': (parseFloat(this.pmhs9[1])/parseFloat(this.pmhs9[2])*1000).toFixed(2)})
                }
                if (data[i].poumianhao10 != null && data[i].poumianhao10 != '') {
                  this.pmhs10 = data[i].poumianhao10.split(',')
                  this.dataSource10.push({ type: data[i].shendu, '波幅': parseFloat(this.pmhs10[3]) ,'波速': (parseFloat(this.pmhs10[1])/parseFloat(this.pmhs10[2])*1000).toFixed(2)})
                }
              }
            }
          }
        })
    },
    jiance: function () {//监测信息
      let params = { id: this.model.id }
      getAction(this.url.list1, params)
        .then(res => {
          if (res.success) {
            this.list1 = []
            this.list2 = []
            this.list3 = []
            this.list4 = []
            this.list5 = []
            this.list6 = []
            this.list7 = []
            this.list8 = []
            this.list9 = []
            this.list10 = []
            let data = res.result
            //console.log(data,"HJhgjksqgsdjasbcfjasbcf b")
            let pm1 = data[0].poumian1
            let pm2 = data[0].poumian2
            let pm3 = data[0].poumian3
            let pm4 = data[0].poumian4
            let pm5 = data[0].poumian5
            let pm6 = data[0].poumian6
            let pm7 = data[0].poumian7
            let pm8 = data[0].poumian8
            let pm9 = data[0].poumian9
            let pm10 = data[0].poumian10
            if (pm1 != null) {
              this.list1 = pm1.split(',')
            }
            if (pm2 != null) {
              this.list2 = pm2.split(',')
            }
            if (pm3 != null) {
              this.list3 = pm3.split(',')
            }
            if (pm4 != null) {
              this.list4 = pm4.split(',')
            }
            if (pm5 != null) {
              this.list5 = pm5.split(',')
            }
            if (pm6 != null) {
              this.list6 = pm6.split(',')
            }
            if (pm7 != null) {
              this.list7 = pm7.split(',')
            }
            if (pm8 != null) {
              this.list8 = pm8.split(',')
            }
            if (pm9 != null) {
              this.list9 = pm9.split(',')
            }
            if (pm10 != null) {
              this.list10 = pm10.split(',')
            }
          }
        })
    },
    bolie: function () {//波列图
      this.detailList = []
      let params = {
        shebeino: this.model.shebeino,
        liushuihao: this.model.liushuihao,
        shizhuangno: this.model.shizhuangno
      }
      getAction(this.url.list2, params).then((res) => {
        console.log(res)
          if (res.success) {
            let data = res.result;
            for (let i=0; i<data.length;i++){
                this.detailList.push({id:data[i].id,pm:data[i].poumianhao})
              }
            }
          }
      )
    },
    popupCallback(row) {
      this.form.setFieldsValue(pick(row))
    },
  }
}
</script>