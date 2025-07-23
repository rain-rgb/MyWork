<template>
  <j-modal :title="title" :width="1400" :visible="visible" @ok="handleOk" @cancel="handleCancel">
    <a-tabs default-active-key="1">
      <a-tab-pane key="1" tab="马歇尔试验详情数据">
        <j-form-container disabled>
          <a-row>
              <a-col :span="12">
                <a-form-item label="延度设备名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-model="data.fsbbh_dictText" placeholder="无"></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item label="试验时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-model="data.isTesttime" placeholder="无"></a-input>
                </a-form-item>
              </a-col>
            </a-row>
            <a-row>
              <a-col :span="12">
                <a-form-item label="工程名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-model="data.projectname" placeholder="无"></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item label="样品编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-model="data.sampleno" placeholder="无"></a-input>
                </a-form-item>
              </a-col>
            </a-row>
            <a-row>
              <a-col :span="12">
                <a-form-item label="工程部位" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-model="data.gcbuwei" placeholder="无"></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item label="样品名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-model="data.samplename" placeholder="无"></a-input>
                </a-form-item>
              </a-col>
            </a-row>
             <a-row>
              <a-col :span="12">
                <a-form-item label="样品描述" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-model="data.samplems" placeholder="无"></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item label="预警原因" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-model="data.overReason" placeholder="无"></a-input>
                </a-form-item>
              </a-col>
            </a-row>
        </j-form-container>
      </a-tab-pane>
    </a-tabs>
    <a-card title="处理信息" :bordered="false" :headStyle="{ color: '#0785fd' }">
      <a-form>
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
      <a-form-model ref="formModel" :rules="rules" :model="formModel">
        <a-row v-show="isAgree == 2">
          <a-col :span="12">
            <a-form-model-item label="监理驳回原因" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jlbh">
              <a-input placeholder="请输入监理驳回原因" v-model="formModel.jlbh"></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row v-show="isAgree == 1">
          <a-col :span="12">
            <a-form-model-item label="监理审批" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="spyj">
              <a-auto-complete v-model="formModel.spyj" placeholder="请选择监理审批">
                <template slot="dataSource">
                  <a-select-option v-for="item in spyjlist" :key="item.id" :value="item.describeinfo">{{
                    item.describeinfo
                  }}</a-select-option>
                </template>
              </a-auto-complete>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="监理结果" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="spjg">
              <a-auto-complete v-model="formModel.spjg" placeholder="请选择监理结果">
                <template slot="dataSource">
                  <a-select-option v-for="item in spjglist" :key="item.id" :value="item.describeinfo">{{
                    item.describeinfo
                  }}</a-select-option>
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
              <j-image-upload v-model="fileList" isMultiple></j-image-upload>
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </a-card>

  </j-modal>
</template>

<script>
import { getAction } from '@api/manage'
export default {
  name: 'ShenHeTwo',
  components: {  },
  props: ['type'],
  data() {
    let validatePass = (rule, value, callback) => {
      if (value === '' && this.isAgree == 1) {
        callback(new Error('请输入'))
      } else {
        callback()
      }
    }
    let validatePass2 = (rule, value, callback) => {
      if (value === '' && this.isAgree == 2) {
        callback(new Error('请输入'))
      } else {
        callback()
      }
    }
    return {
      data: [],
      loading: false,
      ipagination: false,
      title: '',
      width: 800,
      visible: false,
      syjid: '',
      labelCol: {
        xs: { span: 24 },
        sm: { span: 6 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 },
      },
      rules: {
        jlbh: [{ required: true, message: '监理驳回原因不能为空', validator: validatePass2 }],
        spyj: [{ required: true, message: '监理审批不能为空', validator: validatePass }],
        spjg: [{ required: true, message: '监理结果不能为空', validator: validatePass }],
      },
      fileList: [],
      detailList: [],
      spyjlist: [],
      spjglist: [],
      dealObj: {},
      formModel: {
        jlbh: '',
        spyj: '',
        spjg: '',
      },
      isAgree: 1,
      url: {
        list: '/syjoverhandler/syjOverHandler/list',
        kuanglist: '/bhzTerminology/bhzTerminology/list',
        HBZCZAddOrUpdate: '/syjoverhandler/syjOverHandler/HBZCZAddOrUpdate',
      },
    }
  },
  methods: {
    getclfslist() {
      let params = { typeinfo: '监理审批' }
      getAction(this.url.kuanglist, params).then((res) => {
        if (res.success) {
          this.spyjlist = res.result.records
        }
      })
      let params2 = { typeinfo: '监理结果' }
      getAction(this.url.kuanglist, params2).then((res) => {
        if (res.success) {
          this.spjglist = res.result.records
        }
      })
    },
    
    overHandler() {
      this.dealObj = {}
      this.detailList = []
      let param = { baseid: this.syjid }
      getAction(this.url.list, param).then((res) => {
        if (res.success) {
          if (res.result.records.length > 0) {
            this.dealObj = res.result.records[0]
            this.detailList = this.dealObj.filePath2 ? this.dealObj.filePath2.split(',') : []
          }
        }
      })
    },
    showModal(e) {
      this.spyjlist = []
      this.spjglist = []
      this.fileList = []
      this.syjid = e.syjid
      this.getclfslist()
      this.overHandler()
      this.data = e
      this.visible = true
    },
    handleOk() {
      this.$refs.formModel.validate((valid) => {
        if (valid) {
          let params = {}
          if (this.isAgree == 1) {
            params = {
              spyj: this.formModel.spyj,
              spjg: this.formModel.spjg,
              syjbatch: this.syjid,
              syj:3,
              type: this.type,
              status: 40,
              fileList: this.fileList,
            }
          } else {
            params = {
              jlbh: this.formModel.jlbh,
              syjbatch: this.syjid,
              syj:3,
              type: this.type,
              status: 30,
              fileList: this.fileList,
            }
          }
          getAction(this.url.HBZCZAddOrUpdate, params).then((res) => {
            if (res.success) {
              this.isAgree == 1 ? this.$message.success('审核成功') : this.$message.success('驳回成功')
            } else {
              this.isAgree == 1 ? this.$message.error('审核失败') : this.$message.error('驳回失败')
            }
            this.$refs.formModel.resetFields()
            this.visible = false
            this.$emit('change', true)
          })
        }
      })
    },
    handleCancel() {
      this.$refs.formModel.resetFields()
      this.visible = false
      this.$emit('change', false)
    },
  },
}
</script>

<style scoped>
::v-deep .ant-upload-picture-card-wrapper {
  padding: 0;
}
</style>