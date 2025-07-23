<template>
  <j-modal :title="title" :width="1400" :visible="visible" @ok="handleOk" @cancel="handleCancel">
    <a-tabs default-active-key="1">
      <a-tab-pane key="1" tab="智能压浆详情数据">
        <a-table
          :rowKey="(record, index) => index"
          :pagination="ipagination"
          :columns="columns"
          :loading="loading"
          :data-source="data"
          bordered
          size="middle"
          class="j-table-force-nowrap"
          :rowClassName="setClass"
        >
        </a-table>
      </a-tab-pane>
    </a-tabs>
    <div class="hole">
      <j-search-select-tag
        placeholder="请选择孔道"
        v-model="selectValue"
        :dictOptions="holeidList"
        @change="changeHole"
      ></j-search-select-tag>
    </div>
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

    <g-ch-modal ref="gch"></g-ch-modal>
  </j-modal>
</template>

<script>
import { getAction } from '@api/manage'
import GChModal from '@/views/zlyj/modules/GChModal'
export default {
  name: 'ShenHeLone',
  components: { GChModal },
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
      holeidList: [],
      holeid: '',
      level: 0,
      type: 1,
      selectValue: undefined,
      loading: false,
      ipagination: false,
      columns: [
        {
          title: '压浆时间',
          align: 'center',
          dataIndex: 'yajiangsj',
        },
        {
          title: '孔道',
          align: 'center',
          dataIndex: 'kongdao',
        },
        {
          title: '压浆模式',
          align: 'center',
          dataIndex: 'yajiangmosh',
        },
        {
          title: '配合比',
          align: 'center',
          dataIndex: 'peihebi',
        },
        {
          title: '水胶比',
          align: 'center',
          dataIndex: 'shuijiaobi',
        },
        {
          title: '搅拌时间',
          align: 'center',
          dataIndex: 'jiaobansj',
        },
        {
          title: '开始时间',
          align: 'center',
          dataIndex: 'starttime',
        },
        {
          title: '结束时间',
          align: 'center',
          dataIndex: 'endtime',
        },
        {
          title: '进浆压力MPa',
          align: 'center',
          dataIndex: 'jinjiangyal',
        },
        {
          title: '返浆压力MPa',
          align: 'center',
          dataIndex: 'fanjiangyal',
        },
        {
          title: '持续时间',
          align: 'center',
          dataIndex: 'chixushijia',
        },
        {
          title: '进浆量L',
          align: 'center',
          dataIndex: 'jinjiangshu',
        },
        {
          title: '返浆量L',
          align: 'center',
          dataIndex: 'fanjianglia',
        },
        {
          title: '真空泵压力MPa',
          align: 'center',
          dataIndex: 'zkyl',
        },
        {
          title: '压浆次数',
          align: 'center',
          dataIndex: 'yjcs',
        },
        {
          title: '是否合格',
          align: 'center',
          dataIndex: 'hege',
          scopedSlots: { customRender: 'hege' },
        },
      ],
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
        list1s: '/yajiangs/trYajiangS/list1',
        list: '/zhanglam/zhanglaYajiangOverHandler/list',
        kuanglist: '/bhzTerminology/bhzTerminology/list',
        holeidList: '/zhanglam/zhanglaYajiangOverHandler/holeidList47',
        HBZCZAddOrUpdate: 'zhanglam/zhanglaYajiangOverHandler/HBZCZAddOrUpdate',
      },
    }
  },
  created() {
    let syjid = this.$route.query.syjid
    syjid && this.whiteDetail(syjid)
  },
  methods: {
    whiteDetail(syjid) {
      let params = {
        syjid: syjid,
      }
      getAction('/yajiangs/trYajiangS/list2s', params).then((res) => {
        if (res.success) {
          let record = res.result.records[0] || {}
          this.showModal(record)
        } else {
          this.$message.warning(res.message)
        }
      })
    },
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
    zhanglamessage() {
      this.loading = true
      //请求张拉每个孔的断面数据
      let param = { syjid: this.syjid, overLevel_begin: 1 }
      getAction(this.url.list1s, param)
        .then((res) => {
          if (res.success) {
            this.data = res.result
          } else {
            this.data = []
          }
        })
        .finally(() => {
          this.loading = false
        })
    },
    overHandler() {
      this.dealObj = {}
      this.detailList = []
      let param = { baseId: this.syjid, holeid: this.holeid }
      getAction(this.url.list, param).then((res) => {
        if (res.success) {
          if (res.result.records.length > 0) {
            this.dealObj = res.result.records[0]
            this.detailList = this.dealObj.filePath2 ? this.dealObj.filePath2.split(',') : []
          }
        }
      })
    },
    //获取孔道
    getHoleidList() {
      this.holeidList = []
      let param = { syjid: this.syjid, type: this.type, status: 40 }
      getAction(this.url.holeidList, param).then((res) => {
        if (res.success) {
          let arr = []
          let data = res.result
          data.forEach((item) => {
            arr.push({
              text: this.type == 0 ? item.gsbh : item.kongdao,
              value: item.holeid + ',' + (this.type == 0 ? item.overLevel : item.isOverLevel),
            })
          })
          this.holeidList = arr
          if (this.holeidList.length > 0) {
            this.selectValue = this.holeidList[0].value
            this.changeHole(this.selectValue)
          }
        }
      })
    },
    showModal(e) {
      this.spyjlist = []
      this.spjglist = []
      this.fileList = []
      this.selectValue = undefined
      this.syjid = e.syjid
      this.zhanglamessage()
      this.getclfslist()
      this.getHoleidList()
      this.visible = true
    },
    handleOk() {
      if (!this.selectValue) {
        this.$message.warning('请选择孔道！')
        return false
      }
      this.$refs.formModel.validate((valid) => {
        if (valid) {
          let params = {}
          if (this.isAgree == 1) {
            params = {
              spyj: this.formModel.spyj,
              spjg: this.formModel.spjg,
              baseid: this.syjid,
              type: this.type,
              status: 40,
              holeid: this.holeid,
              level: this.level,
              fileList: this.fileList,
            }
          } else {
            params = {
              jlbh: this.formModel.jlbh,
              baseid: this.syjid,
              type: this.type,
              status: 30,
              holeid: this.holeid,
              level: this.level,
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
    //获取孔道id和超标等级
    changeHole(val) {
      if (val) {
        let arr = val.split(',')
        this.holeid = arr[0]
        this.level = arr[1]
        this.overHandler()
      }
    },
    //表格合并
    columnsInit(text, index, num) {
      const obj = {
        children: text,
        attrs: {},
      }
      if (index === 0 || index % num === 0) {
        obj.attrs.rowSpan = num
      } else {
        obj.attrs.rowSpan = 0
      }
      return obj
    },
    setClass(record) {
      return record.overLevel == null || record.overLevel == 0 ? '' : 'rowColor'
    },
    gchDetail(record) {
      this.$refs.gch.getList(record)
    },
  },
}
</script>

<style scoped>
.hole {
  width: 200px;
  margin: 20px 0;
}
::v-deep .rowColor {
  color: red;
}
::v-deep .ant-upload-picture-card-wrapper {
  padding: 0;
}
</style>