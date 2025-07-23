<template>
  <j-modal :title="title" :width="1400" :visible="visible" @ok="handleOk" @cancel="handleCancel">
    <a-tabs default-active-key="1">
      <a-tab-pane key="1" tab="智能张拉详情数据">
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

    <g-ch-modal ref="gch"></g-ch-modal>
  </j-modal>
</template>

<script>
import { getAction } from '@api/manage'
import GChModal from '@/views/zlyj/modules/GChModal'
export default {
  name: 'ChuZhiLone',
  components: { GChModal },
  data() {
    return {
      data: [],
      holeidList: [],
      holeid: '',
      level: 0,
      type: 0,
      selectValue: undefined,
      loading: false,
      ipagination: false,
      columns: [
        {
          title: '过程',
          align: 'center',
          dataIndex: '',
          customRender: (text, record, index) => {
            let childrenVal = <a onClick={() => this.gchDetail(record)}>过程数据</a>
            const obj = {
              children: childrenVal,
              attrs: {},
            }
            if (index === 0 || index % 6 === 0) {
              obj.attrs.rowSpan = 6
            } else {
              obj.attrs.rowSpan = 0
            }
            return obj
          },
        },
        {
          title: '孔号',
          align: 'center',
          dataIndex: 'gsbh',
          customRender: (text, record, index) => this.columnsInit(text, index, 6),
        },
        {
          title: '张拉时间',
          align: 'center',
          dataIndex: 'zlsj',
          customRender: (text, record, index) => this.columnsInit(text, index, 6),
        },
        {
          title: '张拉断面',
          align: 'center',
          dataIndex: 'dh',
          customRender: (text, record, index) => this.columnsInit(text, index, 3),
        },
        {
          title: '记录点',
          align: 'center',
          dataIndex: 'recodePoint',
        },
        {
          title: '初始行程(10/15%)',
          align: 'center',
          dataIndex: 'jdbfb10',
        },
        {
          title: '第一行程(20%/30%)',
          align: 'center',
          dataIndex: 'jdbfb20',
        },
        {
          title: '第二行程(50%-1段)',
          align: 'center',
          dataIndex: 'jdbfb50I',
        },
        {
          title: '第三行程(50%-2段)',
          align: 'center',
          dataIndex: 'jdbfb50II',
        },
        {
          title: '第四行程(100%)',
          align: 'center',
          dataIndex: 'jdbfb100',
        },
        {
          title: '设计张力(KN)',
          align: 'center',
          dataIndex: 'sjzll',
          customRender: (text, record, index) => this.columnsInit(text, index, 6),
        },
        {
          title: '总伸长量(mm)',
          align: 'center',
          dataIndex: 'zscl',
          customRender: (text, record, index) => this.columnsInit(text, index, 6),
        },
        {
          title: '设计伸长量(mm)',
          align: 'center',
          dataIndex: 'llscl',
          customRender: (text, record, index) => this.columnsInit(text, index, 6),
        },
        {
          title: '伸长量误差(%)',
          align: 'center',
          dataIndex: 'sclper',
          customRender: (text, record, index) => this.columnsInit(text, index, 6),
        },
        {
          title: '超张百分比(%)',
          align: 'center',
          dataIndex: 'zzlb',
          customRender: (text, record, index) => this.columnsInit(text, index, 6),
        },
        {
          title: '持荷时间(s)',
          align: 'center',
          dataIndex: 'chsj',
          customRender: (text, record, index) => this.columnsInit(text, index, 3),
        },
        {
          title: '记录时间',
          align: 'center',
          dataIndex: 'jlsj',
          customRender: (text, record, index) => this.columnsInit(text, index, 6),
        },
        {
          title: '状态',
          align: 'center',
          dataIndex: 'overLevel',
          customRender: (text, record, index) => {
            let childrenVal
            if (text == 0 || text == null) {
              childrenVal = (
                <div>
                  <a-tag color="green">合格</a-tag>
                </div>
              )
            } else {
              childrenVal = (
                <div>
                  <a-tag color="red">不合格</a-tag>
                </div>
              )
            }
            const obj = {
              children: childrenVal,
              attrs: {},
            }
            if (index === 0 || index % 3 === 0) {
              obj.attrs.rowSpan = 3
            } else {
              obj.attrs.rowSpan = 0
            }
            return obj
          },
        },
        {
          title: '预警原因',
          align: 'center',
          dataIndex: 'overReason',
          customRender: (text, record, index) => this.columnsInit(text, index, 6),
        },
      ],
      title: '',
      width: 800,
      visible: false,
      syjid: '',
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
        list1s: '/zhanglam/trZhanglaM/list1s',
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
      getAction('/zhanglam/trZhanglaM/list5', params).then((res) => {
        if (res.success) {
          let record = res.result.records[0] || {}
          this.showModal(record)
        } else {
          this.$message.warning(res.message)
        }
      })
    },
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
    //获取孔道
    getHoleidList() {
      this.holeidList = []
      let param = { syjid: this.syjid, type: this.type, status: 10 }
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
      this.clfslist = []
      this.cljglist = []
      this.fileList = []
      this.selectValue = undefined
      this.syjid = e.syjid
      this.getclfslist()
      this.zhanglamessage()
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
          let params = {
            cljg: this.formModel.cljg,
            wtyy: this.formModel.wtyy,
            clfs: this.formModel.clfs,
            baseid: this.syjid,
            type: this.type,
            status: 10,
            holeid: this.holeid,
            level: this.level,
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
    //获取孔道id和超标等级
    changeHole(val) {
      if (val) {
        let arr = val.split(',')
        this.holeid = arr[0]
        this.level = arr[1]
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
