<template>
  <div>
    <a-modal
      title="施工处置"
      :width="width"
      :visible="visible"
      :confirm-loading="confirmLoading"
      @ok="handleOk"
      @cancel="handleCancel"
    >
      <div class="reason">浇筑部位：{{ modalObj.poureLocation }}</div>
      <!-- <div  v-if="bhz === 1" class="reason">浇筑部位：{{ modalObj.poureLocation }}</div> -->
      <div class="reason">超标原因：{{ modalObj.overReason }}</div>
      <a-card title="拌合站材料信息" :bordered="false" :headStyle="{ color: '#0785fd' }">
        <j-editable-table
          ref="bhzCementDetail"
          :loading="bhzCementDetailTable.loading"
          :columns=" bhz === 0?bhzCementDetailTable.columns:bhzCementDetailTable.columns1"
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
          <template v-slot:materialeName="{ index,text }">
            <span :style="{color:(bhzCementDetailTable.dataSource[index].materialeOverLevel == '1'?'orange':bhzCementDetailTable.dataSource[index].materialeOverLevel == '2'?'yellow':bhzCementDetailTable.dataSource[index].materialeOverLevel == '3'?'red':'')}">{{ text }}</span>
          </template>
          <template v-slot:realityNumber="{ index,text }">
            <span :style="{color:(bhzCementDetailTable.dataSource[index].materialeOverLevel == '1'?'orange':bhzCementDetailTable.dataSource[index].materialeOverLevel == '2'?'yellow':bhzCementDetailTable.dataSource[index].materialeOverLevel == '3'?'red':'')}">{{ text }}</span>
          </template>
          <template v-slot:errorValue="{ index,text }">
            <span :style="{color:(bhzCementDetailTable.dataSource[index].materialeOverLevel == '1'?'orange':bhzCementDetailTable.dataSource[index].materialeOverLevel == '2'?'yellow':bhzCementDetailTable.dataSource[index].materialeOverLevel == '3'?'red':'')}">{{ text }}</span>
          </template>
          <template v-slot:overValue="{ index,text }">
            <span :style="{color:(bhzCementDetailTable.dataSource[index].materialeOverLevel == '1'?'orange':bhzCementDetailTable.dataSource[index].materialeOverLevel == '2'?'yellow':bhzCementDetailTable.dataSource[index].materialeOverLevel == '3'?'red':'')}">{{ text }}%</span>
          </template>
          <template v-slot:shijiyongliang="props">
            <span>{{ parseFloat(props.text).toFixed(2) }}</span>
          </template>
          <template v-slot:theoryNumber="{ index,text }" v-if="bhz === 0">
            <span :style="{color:(bhzCementDetailTable.dataSource[index].materialeOverLevel == '1'?'orange':bhzCementDetailTable.dataSource[index].materialeOverLevel == '2'?'yellow':bhzCementDetailTable.dataSource[index].materialeOverLevel == '3'?'red':'')}">{{ text }}</span>
          </template>
          <template v-slot:theoryNumber="props" v-else>
            <span>{{ parseFloat(props.text).toFixed(2) }}</span>
          </template>
          <template v-slot:chaobiao="props">
            <span>{{ parseFloat(props.text).toFixed(2) }}</span>
          </template>
          <template v-slot:wucha="props">
            <span>{{ parseFloat(props.text).toFixed(2) }}</span>
          </template>
        </j-editable-table>
      </a-card>
      <a-card title="施工单位处理" :bordered="false" :headStyle="{ color: '#0785fd' }">
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
                <!-- <j-multi-select-tag
                  placeholder="请选择问题原因"
                  v-model="formModel.wtyy"
                  dictCode="bhz_terminology,describeinfo,describeinfo,typeinfo='问题原因'"
                ></j-multi-select-tag> -->
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
                <!-- <j-multi-select-tag
              
                  placeholder="请选择处理方式"
                  v-model="formModel.clfs"
                  dictCode="bhz_terminology,describeinfo,describeinfo,typeinfo='处理方式'"
                ></j-multi-select-tag> -->
                <a-auto-complete v-model="formModel.clfs" placeholder="请选择处理方式">
                  <template slot="dataSource">
                    <a-select-option
                      v-for="item in clfslist"
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
              <a-form-model-item
                label="处理结果"
                :rules="[{ required: true, message: '处理结果不能为空' }]"
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                prop="cljg"
              >
                <!-- <j-multi-select-tag
                  placeholder="请选择处理结果"
                  v-model="formModel.cljg"
                  dictCode="bhz_terminology,describeinfo,describeinfo,typeinfo='处理结果'"
                ></j-multi-select-tag> -->
                  <a-auto-complete v-model="formModel.cljg" placeholder="请选择处理结果">
                    <template slot="dataSource">
                      <a-select-option
                        v-for="item in cljglist"
                        :key="item.id"
                        :value="item.describeinfo"
                        >{{ item.describeinfo }}</a-select-option
                      >
                    </template>
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
    </a-modal>
  </div>
</template>
<script>
import { getAction } from '@/api/manage'
import JImageUpload from '@/components/jeecg/JImageUpload'
import { FormTypes, getRefPromise } from '@/utils/JEditableTableUtil'

export default {
  name: 'ChuZhiTwo',
  components: { JImageUpload },
  props: ['batchNo', 'bhz', 'level'],
  data() {
    return {
      bhz1:'',
      sonloading:false,
      clfslist: [],
      cljglist: [],
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
            type: FormTypes.slot,
          },
          {
            title: '理论用量',
            key: 'theoryNumber',
            type: FormTypes.slot,
          },
          {
            title: '误差值',
            key: 'errorValue',
            type: FormTypes.slot,
          },
          {
            title: '超标值',
            key: 'overValue',
            type: FormTypes.slot,
          },
          {
            title: '超标等级',
            type: FormTypes.slot,
            slotName: 'action',
            key: 'materialeOverLevel',
          },
        ],
        columns1: [
          {
            title: '材料名',
            key: 'cailiaoming',
            type: FormTypes.normal,
          },
          {
            title: '实际用量',
            key: 'shijiyongliang',
            type: FormTypes.normal,
          },
          {
            title: '理论用量',
            key: 'theoryNumber',
            type: FormTypes.slot,
          },
          {
            title: '误差值',
            key: 'wucha',
            type: FormTypes.normal,
          },
          {
            title: '超标值',
            key: 'chaobiao',
            type: FormTypes.slot,
          },
          {
            title: '超标等级',
            type: FormTypes.slot,
            slotName: 'action',
            key: 'chaobiaodengji',
          },
        ],
      },
      fileList: [],
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
        add: '/RCUnifiedProcessBHZJob/HBZCZAddOrUpdate2',
        tableList: '/hntbhz/bhzCementBase/queryBhzCementDetailByMainId',
        tableList1: '/lqbhz/bhzLqBases/queryBhzLqCailiaoByMainId', // baseGuid
        tableList2: '/swbhz/bhzSwBases/queryBhzSwCailiaoByMainId', // baseGuid
        kuanglist: '/bhzTerminology/bhzTerminology/list',
      },
    }
  },
  mounted() {},
  methods: {
    getclfslist() {
       this.sonloading = true
      let params = { typeinfo: "处理方式" }
      getAction(this.url.kuanglist, params).then((res) => {
        if (res.success) {
          this.clfslist = res.result.records

        }
      }).finally(() => {
          this.sonloading = false
        })
      let params2 = { typeinfo: "处理结果" }
      getAction(this.url.kuanglist, params2).then((res) => {
        if (res.success) {
          this.cljglist = res.result.records
        }
      }).finally(() => {
          this.sonloading = false
        })
    },
    getTable() {
      this.bhzCementDetailTable.loading = true

      if(this.bhz === 1){
        let params = { baseGuid: this.batchNo }
        getAction(this.url.tableList1, params)
          .then((res) => {
            if (res.success) {
              this.bhzCementDetailTable.dataSource = res.result
            }
          })
          .finally(() => {
            this.bhzCementDetailTable.loading = false
          })
      }else if (this.bhz === 2){
        let params = { baseGuid: this.batchNo }
        getAction(this.url.tableList2, params)
          .then((res) => {
            if (res.success) {
              this.bhzCementDetailTable.dataSource = res.result
            }
          })
          .finally(() => {
            this.bhzCementDetailTable.loading = false
          })
      }else{
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
      }

    },
    showModal(record) {
      // this.formModel = {
      //   cljg: '',
      //   wtyy: '',
      //   clfs: '',
      // }
      this.bhz1 == this.bhz
      this.clfslist=[]
      this.cljglist= []
      this.fileList = []
      this.modalObj = Object.assign({}, record)

      this.getclfslist()
      this.visible = true
      this.$nextTick(()=>{
        this.getTable()
        this.$refs.formModel.resetFields()
      })
    },
    subcommit() {
      this.$refs.formModel.validate((valid) => {
        if (valid) {
          getAction(this.url.add, {
            cljg: this.formModel.cljg,
            wtyy: this.formModel.wtyy,
            clfs: this.formModel.clfs,
            bhz: this.bhz,
            hntbatch: this.batchNo,
            fileList: this.fileList,
            level: this.level,
            status: 10,
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
      if (this.level == '2' || this.level == '3') {
        if (this.fileList.length == 0) {
          this.$message.error('请上传附件图片！')
          return false
        }
      }
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