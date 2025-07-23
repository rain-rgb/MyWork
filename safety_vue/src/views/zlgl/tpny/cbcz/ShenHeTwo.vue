<template>
  <j-modal :title="title" :width="1400" :visible="visible" @ok="handleOk" @cancel="handleCancel">
    <a-tabs default-active-key="1">
      <a-tab-pane key="1" tab="摊铺碾压详情数据">
        <j-form-container disabled>
                    <a-form>
            <a-row>
              <a-col :span="12">
                <a-form-item label="告警类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-model="data.alarmtypeid_dictText" placeholder="无"></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item label="告警级别" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input :default-value="data.alarmlevel == '0'?'合格':data.alarmlevel == '1'?'初级超标':data.alarmlevel == '2'?'中级超标':'高级超标'"></a-input>
                </a-form-item>
              </a-col>
            </a-row>
            <a-row>
              <a-col :span="12">
                <a-form-item label="机械名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-model="data.machinename" placeholder="无"></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item label="告警时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-model="data.datatime" placeholder="无"></a-input>
                </a-form-item>
              </a-col>
            </a-row>
            <a-row>
              <a-col :span="12">
                <a-form-item label="标准值" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-model="data.standard" placeholder="无"></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item label="真实值" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-model="data.actual" placeholder="无"></a-input>
                </a-form-item>
              </a-col>
            </a-row>
             <a-row>
              <a-col :span="12">
                <a-form-item label="桩号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-model="data.roadstation" placeholder="无"></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item label="告警内容" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-model="data.alarminfo" placeholder="无"></a-input>
                </a-form-item>
              </a-col>
            </a-row>
          </a-form>
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
      id: '',
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
      let param = { baseid: this.id }
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
      this.id = e.id
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
              syjbatch: this.id,
              syj:4,
              type: this.type,
              status: 40,
              fileList: this.fileList,
            }
          } else {
            params = {
              jlbh: this.formModel.jlbh,
              syjbatch: this.id,
              syj:4,
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