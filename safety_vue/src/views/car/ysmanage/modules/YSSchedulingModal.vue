<template>
  <j-modal
    :title="title"
    :width="width"
    :visible="visible"
    switchFullscreen
    @ok="handleOk"
    :okButtonProps="{ class:{'jee-hidden': disableSubmit} }"
    @cancel="handleCancel"
    cancelText="关闭">

    <a-form :form="form">
      <j-form-container disabled>
        <a-card title="基础信息" :bordered="false" :headStyle="{ color: '#0785fd' }" :bodyStyle="{ padding: '10' }">
          <a-row>
            <a-col :span="12">
              <a-form-model-item label="任务单编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="code">
                <a-input v-model="model.code" placeholder=""></a-input>
              </a-form-model-item>
            </a-col>
            <a-col :span="12">
              <a-form-model-item label="施工配合比编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="recipe">
                <a-input v-model="model.recipe" placeholder=""></a-input>
              </a-form-model-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-model-item label="车辆编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="vehicle">
                <a-input v-model="model.vehicle" placeholder=""></a-input>
              </a-form-model-item>
            </a-col>
            <a-col :span="12">
              <a-form-model-item label="驾驶员" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="driver">
                <a-input v-model="model.driver" placeholder=""></a-input>
              </a-form-model-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-model-item label="发车日期" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="dattim">
                <j-date placeholder="" v-model="model.dattim" style="width: 100%"/>
              </a-form-model-item>
            </a-col>
            <a-col :span="12">
              <a-form-model-item label="签收时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="qianshoutime">
                <j-date placeholder="" v-model="model.qianshoutime" style="width: 100%"/>
              </a-form-model-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-model-item label="运输方量(方)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="prodmete">
                <a-input-number v-model="model.prodmete" placeholder="" style="width: 100%"/>
              </a-form-model-item>
            </a-col>
            <a-col :span="12">
              <a-form-model-item label="质检员" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="qualitor">
                <a-input v-model="model.qualitor" placeholder=""></a-input>
              </a-form-model-item>
            </a-col>
          </a-row>
        </a-card>
      </j-form-container>
    </a-form>
    <a-card title="发车单拌合站详情" :bordered="false" :headStyle="{ color: '#0785fd' }">
      <a-table
        :rowKey="(record, index) => index"
        size="middle"
        bordered
        :columns="columns1"
        :dataSource="data1"
        :pagination="false"
      >
        <span slot="overLevel" slot-scope="overLevel, record">
          <a-tag color="green" v-if="record.overLevel == '0'">合格</a-tag>
          <a-tag color="orange" v-if="record.overLevel == '1'">初级超标</a-tag>
          <a-tag color="yellow" v-if="record.overLevel == '2'">中级超标</a-tag>
          <a-tag color="red" v-if="record.overLevel == '3'">高级超标</a-tag>
        </span>
      </a-table>
    </a-card>
  </j-modal>
</template>

<script>

import { getAction } from '@api/manage'

export default {
  name: 'YSSchedulingModal',
  components: {},
  data() {
    return {
      title: '',
      width: 1200,
      visible: false,
      disableSubmit: false,
      form: this.$form.createForm(this),
      labelCol: {
        xs: { span: 24 },
        sm: { span: 6 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 },
      },
      model: {
        danhao: ''
      },
      data1: [],
      columns1: [
        {
          title: '#',
          dataIndex: '',
          key: 'rowIndex',
          width: 60,
          align: 'center',
          customRender: function (t, r, index) {
            return parseInt(index) + 1
          },
        },
        {
          title: '工程名称',
          align: 'center',
          dataIndex: 'projectName'
        },
        {
          title: '搅拌时间',
          align: 'center',
          dataIndex: 'stirDatetime'
        },
        {
          title: '浇筑部位',
          align: 'center',
          dataIndex: 'poureLocation'
        },
        {
          title: '配方号',
          align: 'center',
          dataIndex: 'formulaNo'
        },
        {
          title: '方量',
          align: 'center',
          dataIndex: 'estimateNumber'
        },
        // {
        //   title: '操作人',
        //   align: 'center',
        //   dataIndex: 'handlers'
        // },
        {
          title: '出料时间',
          align: 'center',
          dataIndex: 'productDatetime',
          customRender: function (text) {
            return !text ? '' : (text.length > 10 ? text : text)
          }
        },
        {
          title: '超标等级',
          align: 'center',
          dataIndex: 'overLevel',
          scopedSlots: { customRender: 'overLevel' },
        }
      ],
      url: {
        getList: '/car/schedulingCar/getBhzBaseByCarDH'
      }
    }
  },
  methods: {
    getList({ danhao }) {
      this.data1 = []
      let params = {
        clientNo: danhao
      }
      getAction(this.url.getList, params).then((res) => {
        if (res.success) {
          this.data1 = res.result
        } else {
          this.$message.warning('暂无此发车单号数据!')
        }
      })
    },
    add() {
      this.visible = true
      this.$nextTick(() => {
        this.$refs.realForm.add()
      })
    },
    edit(record) {
      this.visible = true
      this.$nextTick(() => {
        this.$refs.realForm.edit(record)
      })
    },
    close() {
      this.$emit('close')
      this.visible = false
    },
    handleOk() {
      this.$refs.realForm.submitForm()
    },
    submitCallback() {
      this.$emit('ok')
      this.visible = false
    },
    handleCancel() {
      this.close()
    }
  }
}
</script>