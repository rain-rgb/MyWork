<template>
  <div>
    <a-modal
      title="超标处理"
      :width="width"
      :visible="visible"
      :confirm-loading="confirmLoading"
      @ok="handleOk"
      @cancel="handleCancel"
    >

      <div class="reason">超标原因：{{ modalObj.flag }}</div>
      <a-card v-if="level>1" title="处理信息" :bordered="false" :headStyle="{ color: '#0785fd' }">
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
                    v-for="(item, index) in detailList"
                    :key="index"
                    style="height: 100px; width: 100px; margin: 5px 10px 5px 10px; float: left"
                    :src="[item.icon] "
                    alt=""
                  />
                </viewer>
              </a-form-item>
            </a-col>
          </a-row>
        </a-form>
      </a-card>

      <a-card v-if="level==3" title="监理信息" :bordered="false" :headStyle="{ color: '#0785fd' }">
        <a-form :form="form">
          <a-row>
            <a-col  :span="12">
              <a-form-item label="监理审批" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input placeholder="无" v-model="dealObj.supervisorApproval" disabled></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="监理结果" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input placeholder="无" v-model="dealObj.supervisorResult" disabled></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item label="监理处理时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input placeholder="无" v-model="dealObj.supervisorHandleTime" disabled></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="审核人" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input placeholder="无" v-model="dealObj.approvalPerson" disabled></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12" >
              <a-form-item label="附件" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <viewer :images="detailList1">
                  <img
                    v-for="(item, index) in detailList1"
                    :key="index"
                    style="height: 100px; width: 100px; margin: 5px 10px 5px 10px; float: left"
                    :src="[item.icon]"
                    alt=""
                    v-if ="item.icon !=''"
                  />
                </viewer>
              </a-form-item>
            </a-col>
          </a-row>
        </a-form>
      </a-card>

      <a-card v-if="level == 1" title="施工单位处理" :bordered="false" :headStyle="{ color: '#0785fd' }">
        <a-form-model ref="formModel" :form="form" :model="formModel">
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

                <a-auto-complete v-model="formModel.clfs" placeholder="请输入处理方式">
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

                  <a-auto-complete v-model="formModel.cljg" placeholder="请输入处理结果">

                  </a-auto-complete>
              </a-form-model-item>
            </a-col>
            <a-col :span="12">
              <a-form-model-item label="上传图片" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-image-upload v-model="fileList" :isMultiple="isMultiple"></j-image-upload>
              </a-form-model-item>
            </a-col>
          </a-row>
        </a-form-model>
      </a-card>

      <a-card v-if="level == 2" title="监理审核" :bordered="false" :headStyle="{ color: '#0785fd' }">
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

                <a-auto-complete v-model="formModel.spyj" placeholder="请选择监理审批">

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

                <a-auto-complete v-model="formModel.spjg" placeholder="请选择监理结果">

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
                <j-image-upload v-model="fileList1" :isMultiple="isMultiple"></j-image-upload>
              </a-form-model-item>
            </a-col>
          </a-row>
        </a-form-model>
      </a-card>

      <a-card v-if="level == 3" title="指挥部审批" :bordered="false" :headStyle="{ color: '#0785fd' }">
        <a-form-model ref="formModel" :form="form" :rules="rules" :model="formModel">
          <a-row>
            <a-col :span="12">
              <a-form-model-item label="是否同意" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-radio-group v-model="isAgree">
                  <a-radio :value="1">是</a-radio>
                  <a-radio :value="2">否</a-radio>
                </a-radio-group>
              </a-form-model-item>
            </a-col>
          </a-row>
          <a-row v-show="isAgree == 2">
            <a-col :span="12">
              <a-form-model-item label="指挥部驳回原因" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="zhbbh">
                <a-input placeholder="请输入指挥部驳回原因" v-model="formModel.zhbbh"></a-input>
              </a-form-model-item>
            </a-col>
          </a-row>
          <a-row v-show="isAgree == 1">
            <a-col :span="12">
              <a-form-model-item label="指挥部审批" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="zhbyj">

                <a-auto-complete v-model="formModel.zhbyj" placeholder="请选择指挥部审批">
                </a-auto-complete>
              </a-form-model-item>
            </a-col>
            <a-col :span="12">
              <a-form-model-item label="指挥部结果" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="zhbjg">
                <a-auto-complete v-model="formModel.zhbjg" placeholder="请选择指挥部结果">
                </a-auto-complete>
              </a-form-model-item>
            </a-col>
          </a-row>
        </a-form-model>
      </a-card>
    </a-modal>
  </div>
</template>
<script>
import { getAction,postAction } from '@/api/manage'
import JImageUpload from '@/components/jeecg/JImageUpload'
import { FormTypes, getRefPromise } from '@/utils/JEditableTableUtil'

export default {
  name: 'CarChuZhiTwo',
  components: { JImageUpload },
  props: ['batchNo', 'bhz'],
  data() {
    return {
      level:1,
      sonloading:false,
      dealObj: {},
      form: this.$form.createForm(this),
      fileList:[],
      fileList1:"",
      detailList: [],
      detailList1:[],
      isAgree: 1,
      isMultiple: true,
      width: 1200,
      visible: false,
      confirmLoading: false,

      formModel: {
        cljg: '',
        wtyy: '',
        clfs: '',
      },
      labelCol: {
        xs: { span: 24 },
        sm: { span: 6 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 },
      },
      modalObj: {},
      url: {
        carHandle: '/czsh/bhzCementOverHandler/carHandle',
        list1: '/czsh/bhzCementOverHandler/list',
        tableList1: '/lqbhz/bhzLqBases/queryBhzLqCailiaoByMainId', // baseGuid
        tableList2: '/swbhz/bhzSwBases/queryBhzSwCailiaoByMainId', // baseGuid
        kuanglist: '/bhzTerminology/bhzTerminology/list',
      },
    }
  },
  mounted() {},
  methods: {
    handledata: function () {
      this.dealObj  = []
      let params = { baseid: this.modalObj.id }
      getAction(this.url.list1, params).then((res) => {
        if (res.success) {
          if (res.result.records.length > 0) {
            this.dealObj = res.result.records[0]
            if (this.dealObj.filePath2 !== null) {
              var filePath1 = this.dealObj.filePath2.split(',')
              filePath1.forEach(re => {
                this.detailList.push({ icon: re })
              })
              //console.log('filePath1', filePath1)
            }
            if (this.dealObj.filePath !== null){
              var filePath3 = this.dealObj.filePath.split(',')
              filePath3.forEach(re => {
                this.detailList1.push({ icon: re })
              })
            }
            //console.log("this.bhzCementOverHandler", res.result)
          } else {
            this.$message.warn('暂无超标处理信息！')
          }
        }
      })
    },

    showModal(record,level) {
      this.level = level
      this.fileList = []
      this.detailList = []
      this.detailList1 = []
      this.modalObj = Object.assign({}, record)
     // this.getclfslist()
      this.visible = true
      this.$nextTick(()=>{
        this.handledata()
        this.$refs.formModel.resetFields()
      })
    },
    subcommit() {
      this.$refs.formModel.validate((valid) => {
        if (valid) {
          postAction(this.url.carHandle, {
            handleResult: this.formModel.cljg,
            problemReasons: this.formModel.wtyy,
            handleWay: this.formModel.clfs,
            baseid: this.modalObj.id,
            filePath2: this.fileList != []?this.fileList:"",
            overproofStatus: 10,
          }).then((res) => {
            if (res.success) {
              this.$message.success('处置成功')
            } else {
              this.$message.error('处置失败')
            }
            this.confirmLoading = false
            this.visible = false
            this.$emit('change', true)
          })
        } else {
          this.confirmLoading = false
          this.$message.error('请填写必填项')
          return false
        }
      })

      // this.routeReload();
    },

    subcommit2() {
      this.$refs.formModel.validate((valid) => {
        if (valid) {
          let params = {}
          if (this.isAgree == 1) {
            console.log("this.fileList1",this.fileList1 )
            params = {
              supervisorApproval: this.formModel.spyj,
              supervisorResult: this.formModel.spjg,
              filePath: this.fileList1 ,
              baseid: this.modalObj.id,
              overproofStatus: 40,
            }
          } else {
            params = {
              remark: this.formModel.jlbh,
              baseid: this.modalObj.id,
              overproofStatus: 30,
            }
          }
          postAction(this.url.carHandle, params).then((res) => {
            if (res.success) {
              this.$message.success('审核成功')
            } else {
              this.$message.error('审核失败')
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

    subcommit3() {
      this.$refs.formModel.validate((valid) => {
        if (valid) {
          let params = {}
          if (this.isAgree == 1) {
            params = {
              headquartersApproval: this.formModel.zhbyj,
              headquartersResult: this.formModel.zhbjg,
              baseid: this.modalObj.id,
              overproofStatus: 20,
            }
          } else {
            params = {
              headquartersRemark: this.formModel.zhbbh,
              baseid: this.modalObj.id,
              overproofStatus: 60,
            }
          }
          postAction(this.url.carHandle, params).then((res) => {
            if (res.success) {
              this.$message.success('审批成功')
            } else {
              this.$message.error('审批失败')
            }
            this.confirmLoading = false
            this.visible = false
            this.$emit('change', true)
          })
          // this.routeReload();
        } else {
          this.confirmLoading = false
          console.log('请填写必填项')
          return false
        }
      })
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
      if(this.level == 1){
        if (this.fileList.length == 0) {
          this.$message.error('请上传附件图片！')
          return false
        }
        this.subcommit()
      }else if(this.level == 2){
        this.subcommit2()
      }else{
        this.subcommit3()
      }
      this.confirmLoading = true
      this.routeReload()
    },
    handleCancel() {
      this.visible = false
      this.$emit('change', false)
    },
  },
}
</script>
<style lang="less" scoped>
.reason {

  color: rgb(239, 11, 11);
  line-height: 30px;
  text-align: center;
  font-size: 18px;
}

::v-deep .ant-upload-picture-card-wrapper {
  padding: 0;
}
.jtable {
  text-align: center;
}
</style>