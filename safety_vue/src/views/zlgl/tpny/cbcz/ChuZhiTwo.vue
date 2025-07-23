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
    <a-card title="施工单位处理" :bordered="false" :headStyle="{ color: '#0785fd' }">
      <a-form-model ref="formModel" :model="formModel">
        <a-row>
          <a-col :span="12">
            <a-form-model-item
              label="问题原因"
              :rules="[{ required: true, message: '问题原因不能为空' }]"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              prop="wtyy"
            >
              <a-textarea
                placeholder="请输入问题原因"
                v-model="formModel.wtyy"
                :auto-size="{ minRows: 5, maxRows: 20 }"
              ></a-textarea>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item
              label="处理方式"
              :rules="[{ required: true, message: '处理方式不能为空' }]"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              prop="clfs"
            >
              <a-auto-complete v-model="formModel.clfs" placeholder="请选择处理方式">
                <template slot="dataSource">
                  <a-select-option v-for="item in clfslist" :key="item.id" :value="item.describeinfo">{{
                    item.describeinfo
                  }}</a-select-option>
                </template>
              </a-auto-complete>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-model-item
              label="处理结果"
              :rules="[{ required: true, message: '处理结果不能为空' }]"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              prop="cljg"
            >
              <a-auto-complete v-model="formModel.cljg" placeholder="请选择处理结果">
                <template slot="dataSource">
                  <a-select-option v-for="item in cljglist" :key="item.id" :value="item.describeinfo">{{
                    item.describeinfo
                  }}</a-select-option>
                </template>
              </a-auto-complete>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="上传图片" :labelCol="labelCol" :wrapperCol="wrapperCol">
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
  name: 'ChuZhiTwo',
  components: {},
  props: ['type'],
  data() {
    return {
      data: {},
     
      loading: false,
      ipagination: false,
      title: '',
      width: 800,
      visible: false,
      id: '',
      formModel: {
        cljg: '',
        wtyy: '',
        clfs: '',
      },
      fileList: [],
      clfslist: [],
      cljglist: [],
      labelCol: {
        xs: { span: 24 },
        sm: { span: 6 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 },
      },
      url: {
        list1s: '/yd/wYanduM/list',
        kuanglist: '/bhzTerminology/bhzTerminology/list',
        HBZCZAddOrUpdate: '/syjoverhandler/syjOverHandler/HBZCZAddOrUpdate',
      },
    }
  },
  methods: {
    getclfslist() {
      let params = { typeinfo: '处理方式' }
      getAction(this.url.kuanglist, params).then((res) => {
        if (res.success) {
          this.clfslist = res.result.records
        }
      })
      let params2 = { typeinfo: '处理结果' }
      getAction(this.url.kuanglist, params2).then((res) => {
        if (res.success) {
          this.cljglist = res.result.records
        }
      })
    },
    showModal(e) {
      console.log(e, '123456789')
      this.clfslist = []
      this.cljglist = []
      this.fileList = []
      this.id = e.id
      this.data = e
      this.getclfslist()

      this.visible = true
    },
    handleOk() {
      this.$refs.formModel.validate((valid) => {
        if (valid) {
          let params = {
            cljg: this.formModel.cljg,
            wtyy: this.formModel.wtyy,
            clfs: this.formModel.clfs,
            syjbatch: this.id,
            type: this.type,
            syj:4,
            status: 10,
            fileList: this.fileList,
          }
          getAction(this.url.HBZCZAddOrUpdate, params).then((res) => {
            if (res.success) {
              this.$message.success('处置成功')
            } else {
              this.$message.error('处置失败')
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
