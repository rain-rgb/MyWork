<template>
  <div>
    <a-modal
      title="监理审核"
      :width="width"
      :visible="visible"
      :confirm-loading="confirmLoading"
      @ok="handleOk"
      @cancel="handleCancel"
    >
      <a-card title="拌合站材料信息" :bordered="false" :headStyle="{ color: '#0785fd' }">
        <j-editable-table
          ref="bhzCementDetail"
          :loading="bhzCementDetailTable.loading"
          :columns="bhzCementDetailTable.columns"
          :dataSource="bhzCementDetailTable.dataSource"
          :maxHeight="300"
          :disabled="false"
          :rowNumber="true"
          :actionButton="false"
          class="jtable"
        >
          <template v-slot:action="props">
            <a-tag color="green" v-if="props.value == '0'">合格</a-tag>
            <a-tag color="orange" v-if="props.value == '1'">初级超标</a-tag>
            <a-tag color="yellow" v-if="props.value == '2'">中级超标</a-tag>
            <a-tag color="red" v-if="props.value == '3'">高级超标</a-tag>
          </template>
        </j-editable-table>
      </a-card>
      <a-card title="处理信息" :bordered="false" :headStyle="{ color: '#0785fd' }">
        <a-form :form="form">
          <a-row>
            <a-col :span="12">
              <a-form-item label="问题原因" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input placeholder="无" v-model="dealObj.problemReasons" disabled></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="处理方式" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input placeholder="无" v-model="dealObj.handleWay" disabled></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item label="处理结果" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input placeholder="无" v-model="dealObj.handleResult" disabled></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="处理时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input placeholder="无" v-model="dealObj.handleTime" disabled></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item label="处置人" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input placeholder="无" v-model="dealObj.handlePerson" disabled></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="附件" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <viewer :images="detailList">
                  <img
                    v-for="(img, index) in detailList"
                    :key="index"
                    style="height: 100px; width: 100px; margin: 5px 10px 5px 10px; float: left"
                    :src="img"
                    alt=""
                  />
                </viewer>
              </a-form-item>
            </a-col>
          </a-row>
        </a-form>
      </a-card>
      <a-card title="监理审核" :bordered="false" :headStyle="{ color: '#0785fd' }">
        <a-form-model ref="formModel" :form="form" :rules='rules' :model="formModel">
          <a-row v-show="isAgree == 2">
            <a-col :span="12">
              <a-form-model-item
                label="监理驳回原因"
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                prop="jlbh"
              >
                <a-input placeholder="请输入监理驳回原因" v-model="formModel.jlbh"></a-input>
              </a-form-model-item>
            </a-col>
          </a-row>
          <a-row v-show="isAgree == 1">
            <a-col :span="12">
              <a-form-model-item
                label="监理审批"
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                prop="spyj"
              >
                <!-- <j-multi-select-tag
                  placeholder="请选择监理审批"
                  v-model="formModel.spyj"
                  dictCode="bhz_terminology,describeinfo,describeinfo,typeinfo='监理审批'"
                ></j-multi-select-tag> -->
                <a-auto-complete v-model="formModel.spyj" placeholder="请选择监理审批">
                  <template slot="dataSource">
                    <a-select-option
                      v-for="item in spyjlist"
                      :key="item.id"
                      :value="item.describeinfo"
                      >{{ item.describeinfo }}</a-select-option
                    >
                  </template>
                </a-auto-complete>
              </a-form-model-item>
            </a-col>
            <a-col :span="12">
              <a-form-model-item
                label="监理结果"
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                prop="spjg"
              >
                 <!-- <j-multi-select-tag
                  placeholder="请选择监理结果"
                  v-model="formModel.spjg"
                  dictCode="bhz_terminology,describeinfo,describeinfo,typeinfo='监理结果'"
                ></j-multi-select-tag> -->
                <a-auto-complete v-model="formModel.spjg" placeholder="请选择监理结果">
                  <template slot="dataSource">
                    <a-select-option
                      v-for="item in spjglist"
                      :key="item.id"
                      :value="item.describeinfo"
                      >{{ item.describeinfo }}</a-select-option
                    >
                  </template>
                </a-auto-complete>
              </a-form-model-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-model-item label="是否同意" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-radio-group v-model="isAgree">
                  <a-radio :value="1">是</a-radio>
                  <a-radio :value="2">否</a-radio>
                </a-radio-group>
              </a-form-model-item>
            </a-col>
            <a-col :span="12">
              <a-form-model-item label="上传图片" :labelCol="labelCol" :wrapperCol="wrapperCol" v-show="isAgree == 1">
                <j-image-upload v-model="fileList" :isMultiple="isMultiple"></j-image-upload>
              </a-form-model-item>
            </a-col>
          </a-row>
        </a-form-model>
      </a-card>
    </a-modal>
  </div>
</template>
<script>
import { getAction } from '@/api/manage'
import JImageUpload from '@/components/jeecg/JImageUpload'
import { FormTypes } from '@/utils/JEditableTableUtil'

export default {
  name: 'ShenHeTwoLone',
  components: { JImageUpload },
  data() {
    let validatePass = (rule, value, callback) => {
      if (value === ''&&this.isAgree==1) {
        callback(new Error('请输入1'));
      } else {
        callback();
      }
    };
    let validatePass2 = (rule, value, callback) => {
      if (value === ''&&this.isAgree==2) {
        callback(new Error('请输入2'));
      } else {
        callback();
      }
    };
    return {
      spyjlist:[],
      spjglist:[],
      batchNo: undefined,
      bhz: 0,
      level: undefined,
      form: this.$form.createForm(this),
      // 拌合站子表材料信息
      bhzCementDetailTable: {
        loading: false,
        dataSource: [],
        columns: [
          {
            title: '材料名',
            key: 'materialeName',
            type: FormTypes.normal,
          },
          {
            title: '实际用量',
            key: 'realityNumber',
            type: FormTypes.normal,
          },
          {
            title: '理论用量',
            key: 'theoryNumber',
            type: FormTypes.normal,
          },
          {
            title: '误差值',
            key: 'errorValue',
            type: FormTypes.normal,
          },
          {
            title: '超标值',
            key: 'overValue',
            type: FormTypes.normal,
          },
          {
            title: '超标等级',
            type: FormTypes.slot,
            slotName: 'action',
            key: 'materialeOverLevel',
          },
        ],
      },
      rules:{
        jlbh: [{ required: true, message: '监理驳回原因不能为空',validator: validatePass2 }],
        spyj: [{ required: true, message: '监理审批不能为空',validator: validatePass }],
        spjg: [{ required: true, message: '监理结果不能为空',validator: validatePass }],
      },
      formModel: {
        jlbh: '',
        spyj: '',
        spjg: '',
      },
      isAgree: 1,
      fileList: [],
      detailList: [],
      isMultiple: true,
      width: 1200,
      visible: false,
      confirmLoading: false,

      labelCol: {
        xs: { span: 24 },
        sm: { span: 6 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 },
      },
      modalObj: {},
      dealObj: {},
      url: {
        add: '/RCUnifiedProcessBHZJob/HBZCZAddOrUpdate2',
        tableList: '/hntbhz/bhzCementBase/queryBhzCementDetailByMainId',
        kuanglist: '/bhzTerminology/bhzTerminology/list',
      },
    }
  },
  mounted() {
    this.batchNo = this.$route.query.batchNo
    this.batchNo && this.showModal()
  },
  methods: {
    getclfslist() {
      let params = { typeinfo: "监理审批" }
      getAction(this.url.kuanglist, params).then((res) => {
        if (res.success) {
          this.spyjlist = res.result.records
        }
      })
      let params2 = { typeinfo: "监理结果" }
      getAction(this.url.kuanglist, params2).then((res) => {
        if (res.success) {
          this.spjglist = res.result.records
        }
      })
    },
    getTable() {
      this.bhzCementDetailTable.loading = true
      let params = { batchNo: this.batchNo }
      getAction(this.url.tableList, params)
        .then((res) => {
          if (res.success) {
            this.bhzCementDetailTable.dataSource = res.result.records
          }
        })
        .finally(() => {
          this.bhzCementDetailTable.loading = false
        })
    },
    showModal() {
      let params = {
        batchNo: this.batchNo
      }
      getAction('/hntbhz/bhzCementBase/listczsh', params).then((res) => {
        if (res.success) {
          let record = res.result.records[0] || {}
          this.level = record.overLevel
          this.modalObj = Object.assign({}, record)
          this.dealObj = Object.assign({}, record.bhzCementOverHandler)
          this.getTable()
          this.getclfslist()
          this.handleImg()
          this.visible = true
        } else {
          this.$message.warning(res.message)
        }
      })
    },
    subcommit() {
      this.$refs.formModel.validate((valid) => {
        if (valid) {
          let params = {}
          if (this.isAgree == 1) {
            params = {
              spyj: this.formModel.spyj,
              spjg: this.formModel.spjg,
              fileList: this.fileList,
              bhz: this.bhz,
              hntbatch: this.batchNo,
              level: this.level,
              status: 40,
            }
          } else {
            params = {
              jlbh: this.formModel.jlbh,
              bhz: this.bhz,
              hntbatch: this.batchNo,
              level: this.level,
              status: 30,
            }
          }
          getAction(this.url.add, params).then((res) => {
            if (res.success) {
              if (this.isAgree == 1) {
                this.$message.success('审核成功')
              } else {
                this.$message.success('驳回成功')
              }
            } else {
              if (this.isAgree == 1) {
                this.$message.error('审核失败')
              } else {
                this.$message.error('驳回失败')
              }
            }
            this.confirmLoading = false
            this.visible = false
            this.$emit('change', true)
          })
          // this.routeReload();
        } else {
          this.confirmLoading = false
          this.$message.error('请填写必填项');
          return false
        }
      })
    },
    handleImg() {
      if (this.dealObj.filePath2) {
        this.detailList = this.dealObj.filePath2.split(',')
      }
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
    handleOk() {
      this.confirmLoading = true
      this.subcommit()
    },
    handleCancel() {
      this.visible = false
      this.$emit('change', false)
    },
  },
}
</script>
<style lang="less" scoped>
::v-deep .ant-upload-picture-card-wrapper {
  padding: 0;
}
.jtable {
  text-align: center;
}
</style>