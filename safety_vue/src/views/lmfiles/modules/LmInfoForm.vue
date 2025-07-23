<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <!--        <a-divider type="horizontal" ><span style="color:#0785fd">路段</span></a-divider>-->
        <a-row>
          <a-col :span="8">
            <a-form-model-item label="项目名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-model="model.proname" placeholder="请输入项目名称"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="8">
            <a-form-model-item label="桩号里程" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="lmname">
              <a-input v-model="model.lmname" placeholder="请输入施工路段"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="8">
            <a-form-model-item label="施工路段" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <JselectDqProjName @change="changeOne" ::multi="false" placeholder="请选择"/>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="8">
            <a-form-model-item label="路面结构类型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="wbstype">
              <a-input v-model="model.wbstype" placeholder="请输入路面结构类型"></a-input>
            </a-form-model-item>
          </a-col>
        <!--          <a-divider type="horizontal" ><span style="color:#0785fd">底基层</span></a-divider>-->
          <a-col :span="8">
            <a-form-model-item label="底基层总方量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="djcInfo1">
              <a-input v-model="model.djcInfo1" placeholder="请输入底基层混合料总方量"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="8">
            <a-form-model-item label="供应商" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="djcInfo2">
              <a-input v-model="model.djcInfo2" placeholder="请输入底基层混合料供应商"></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
        <!--          <a-divider type="horizontal" ><span style="color:#0785fd">基层</span></a-divider>-->
        <a-row>
          <a-col :span="8">
            <a-form-model-item label="基层总方量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jcInfo1">
              <a-input v-model="model.jcInfo1" placeholder="请输入基层混合料总方量"></a-input>
            </a-form-model-item>
          </a-col>

          <a-col :span="8">
            <a-form-model-item label="供应商" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jcInfo2">
              <a-input v-model="model.jcInfo2" placeholder="请输入基层供应商"></a-input>
            </a-form-model-item>
          </a-col>
        <!--          <a-divider type="horizontal" ><span style="color:#0785fd">下面层</span></a-divider>-->
          <a-col :span="8">
            <a-form-model-item label="下面层总方量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="xmcInfo1">
              <a-input v-model="model.xmcInfo1" placeholder="请输入下面层混合料总方量"></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="8">
            <a-form-model-item label="供应商" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="xmcInfo2">
              <a-input v-model="model.xmcInfo2" placeholder="请输入下面层混合料供应商"></a-input>
            </a-form-model-item>
          </a-col>
        <!--          <a-divider type="horizontal" ><span style="color:#0785fd">中面层</span></a-divider>-->
          <a-col :span="8">
            <a-form-model-item label="中面层总方量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="zmcInfo1">
              <a-input v-model="model.zmcInfo1" placeholder="请输入中面层混合料总方量"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="8">
            <a-form-model-item label="供应商" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="zmcInfo2">
              <a-input v-model="model.zmcInfo2" placeholder="请输入中面层混合料供应商"></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
        <!--          <a-divider type="horizontal" ><span style="color:#0785fd">上面层</span></a-divider>-->
        <a-row>
          <a-col :span="8">
            <a-form-model-item label="上面层总方量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="smcInfo1">
              <a-input v-model="model.smcInfo1" placeholder="请输入上面层混合料总方量"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="8">
            <a-form-model-item label="供应商" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="smcInfo2">
              <a-input v-model="model.smcInfo2" placeholder="请输入上面层混合料供应商"></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
        <!--          <a-col :span="24">-->
        <!--            <a-form-model-item label="文件数量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="files">-->
        <!--              <a-input-number v-model="model.files" placeholder="请输入文件数量" style="width: 100%" />-->
        <!--            </a-form-model-item>-->
        <!--          </a-col>-->
      </a-form-model>
    </j-form-container>

    <a-tabs default-active-key="1" v-show="formDisabled">
      <a-tab-pane tab="水稳施工文件" key="1" :forceRender="true">
        <template>
          <a-steps :current="current1" size="default" @change="onChange1" labelPlacement="vertical" @click="showInfo1">
            <a-step title="水稳试验段施工前"/>
            <a-step title="水稳施工前" :disabled="jinyongsw1"/>
            <a-step title="水稳施工过程中" :disabled="jinyongsw2"/>
          </a-steps>
        </template>
        <a-table
          v-show="display1"
          rowKey="id"
          bordered
          :columns="columns1"
          :dataSource="dataSource1"
          :pagination="false"
          class="j-table-force-nowrap"
        >
          <span slot="action" slot-scope="text, record">
            <a @click="pourDetail(record)">文件上传</a>
             <a-divider type="vertical"/>
            <a @click="preview(record)">预览</a>
             <a-divider type="vertical"/>
              <a @click="download(record)">下载</a>
          </span>
        </a-table>
        <LmSwFilesModal ref="files" @ok="modalFormOk"></LmSwFilesModal>
      </a-tab-pane>
      <a-tab-pane tab="沥青施工文件" key="2" :forceRender="true">
        <template>
          <a-steps :current="current2" size="default" @change="onChange2" labelPlacement="vertical" @click="showInfo2">
            <a-step title="沥青下面层试验段施工前"/>
            <a-step title="沥青下面层施工前" :disabled="jinyonglq1" content="上一阶段文件未上传完整，请补充上传..."/>
            <a-step title="沥青下面层施工过程中" :disabled="jinyonglq2"/>
            <a-step title="沥青中面层试验段" :disabled="jinyonglq3"/>
            <a-step title="沥青中面层施工前" :disabled="jinyonglq4"/>
            <a-step title="沥青上面层试验段施工前" :disabled="jinyonglq5"/>
            <a-step title="沥青上面层施工前" :disabled="jinyonglq6"/>
          </a-steps>
        </template>
        <a-table
          v-show="display2"
          rowKey="id"
          bordered
          :columns="columns2"
          :dataSource="dataSource2"
          :pagination="false"
          class="j-table-force-nowrap"
        >
         <span slot="action" slot-scope="text, record">
            <a @click="pourDetail(record)">文件上传</a>
             <a-divider type="vertical"/>
            <a @click="preview(record)">预览</a>
             <a-divider type="vertical"/>
              <a @click="download(record)">下载</a>
          </span>
        </a-table>
        <LmLqFilesModal ref="files" @ok="modalFormOk"></LmLqFilesModal>
      </a-tab-pane>
    </a-tabs>
  </a-spin>
</template>

<script>
import { httpAction, getAction } from '@/api/manage'
import JFormContainer from '@/components/jeecg/JFormContainer'
import JDate from '@/components/jeecg/JDate'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import JselectDqDepart from '@/components/jeecgbiz/JselectDqDepart'
import JselectDqProjName from '@/components/jeecgbiz/JselectDqProjName'
import { JEditableTableMixin } from '@/mixins/JEditableTableMixin'
import LmFilesModal from './LmFilesModal'
import { base64Encode } from '@api/kkfileView'
import Vue from 'vue'
import { ACCESS_TOKEN } from '@/store/mutation-types'
import LmSwFilesModal from '@views/lmfiles/modules/LmSwFilesModal'
import LmLqFilesModal from '@views/lmfiles/modules/LmLqFilesModal'

export default {
  name: 'LmInfoForm',
  mixins: [JEditableTableMixin, JeecgListMixin],
  components: {
    LmLqFilesModal,
    LmSwFilesModal,
    JselectDqProjName,
    JFormContainer,
    JDate,
    JselectDqDepart,
    LmFilesModal,
  },
  props: {
    //表单禁用
    disabled: {
      type: Boolean,
      default: false,
      required: false,
    },
  },
  data() {
    return {
      model: {},
      labelCol: {
        xs: { span: 24 },
        sm: { span: 5 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 },
      },
      dataSource1: [],
      display1: false,
      display2: false,
      jinyongsw1: true,
      jinyongsw2: true,
      jinyonglq1: true,
      jinyonglq2: true,
      jinyonglq3: true,
      jinyonglq4: true,
      jinyonglq5: true,
      jinyonglq6: true,
      current1: null,
      current2: null,
      columns1: [
        {
          title: '施工阶段',
          align: 'center',
          dataIndex: 'sgjd_dictText',
        },
        {
          title: '文件类型',
          align: 'center',
          dataIndex: 'filetype_dictText',
        },
        {
          title: '状态',
          align: 'center',
          dataIndex: 'filestatus_dictText',
        },
        {
          title: '上传人',
          align: 'center',
          dataIndex: 'updateBy',
        },
        {
          title: '上传时间',
          align: 'center',
          dataIndex: 'updateTime',
        },
        {
          title: '操作',
          dataIndex: 'action',
          align: 'center',
          fixed: 'right',
          width: 147,
          scopedSlots: { customRender: 'action' },
        },
      ],
      dataSource2: [],
      columns2: [
        {
          title: '施工阶段',
          align: 'center',
          dataIndex: 'sgjd_dictText',
        },
        {
          title: '文件类型',
          align: 'center',
          dataIndex: 'filetype_dictText',
        },
        {
          title: '状态',
          align: 'center',
          dataIndex: 'filestatus_dictText',
        },
        {
          title: '上传人',
          align: 'center',
          dataIndex: 'updateBy',
        },
        {
          title: '上传时间',
          align: 'center',
          dataIndex: 'updateTime',
        },
        {
          title: '操作',
          dataIndex: 'action',
          align: 'center',
          fixed: 'right',
          width: 147,
          scopedSlots: { customRender: 'action' },
        },
      ],
      confirmLoading: false,
      validatorRules: {},
      url: {
        add: '/lmjob/lmJobInfo/add1',
        edit: '/lmjob/lmJobInfo/edit',
        queryById: '/lmjob/lmJobInfo/queryById',
      },
    }
  },
  computed: {
    formDisabled() {
      return this.disabled
    },
  },
  created() {
    //备份model原始值
    this.modelDefault = JSON.parse(JSON.stringify(this.model))
  },
  methods: {
    showInfo1() {
      if (!this.display1) {
        this.display1 = true
      }
    },
    showInfo2() {
      if (!this.display2) {
        this.display2 = true
      }
    },
    onChange1(current1) {
      this.current1 = current1
      let params1 = {
        infoid: this.model.id,
        filetype: '1' + current1 + '*',
      }
      //水稳
      getAction('/lmjob/lmSwFiles/list', params1).then((res) => {
        if (res.code == 200) {
          this.dataSource1 = res.result.records
          if (this.dataSource1.every((e) => e.filestatus === 1)) {
            if (this.dataSource1[0].sgjd === 'sw1') {
              this.jinyongsw1 = false
            }
            if (this.dataSource1[0].sgjd === 'sw2') {
              this.jinyongsw2 = false
            }
          }
        } else {
          this.$message.warning(res.message)
        }
      })
    },
    onChange2(current2) {
      this.current2 = current2
      let params2 = {
        infoid: this.model.id,
        filetype: '2' + current2 + '*',
      }
      //沥青
      getAction('/lmjob/lmLqFiles/list', params2).then((res) => {
        if (res.code == 200) {
          this.dataSource2 = res.result.records
          if (this.dataSource2.every((e) => e.filestatus === 1)) {
            if (this.dataSource2[0].sgjd === 'lq1') {
              this.jinyonglq1 = false
            }
            if (this.dataSource2[0].sgjd === 'lq2') {
              this.jinyonglq2 = false
            }
            if (this.dataSource2[0].sgjd === 'lq3') {
              this.jinyonglq3 = false
            }
            if (this.dataSource2[0].sgjd === 'lq4') {
              this.jinyonglq4 = false
            }
            if (this.dataSource2[0].sgjd === 'lq5') {
              this.jinyonglq5 = false
            }
            if (this.dataSource2[0].sgjd === 'lq6') {
              this.jinyonglq6 = false
            }
          }
        } else {
          this.$message.warning(res.message)
        }
      })
    },
    pourDetail(record) {
      // this.$refs.files.model = { infoid: record.id}
      this.$refs.files.add({ record })
      this.$refs.files.title = '上传文件'
      this.$refs.files.disableSubmit = false
      this.$refs.files.visible = true
    },
    changeOne(val) {
      this.model.wbsOrgCode = val
      let params = {
        orgCode: val,
      }
      getAction('/sydpssysample/syDpsSySample/getProjNames', params).then((res) => {
        if (res.success) {
          this.model.zhlc = res.result
        }
      })
    },
    download({ fileurl, filename }) {
      if (fileurl) {
        window.open(fileurl, '_self')
      }

    },
    preview({ fileurl }) {

      if (fileurl) {
        if (fileurl.indexOf('http') > -1) {
          let url = window._CONFIG['onlinePreviewDomainURL'] + '?url=' + encodeURIComponent(base64Encode(fileurl))
          window.open(url, '_blank')
        } else {
          let url = 'http://web.traiot.cn/docs/wz/' + fileurl
          window.open(url, '_blank')
        }
        // let url = window._CONFIG['onlinePreviewDomainURL'] + '?url=' + encodeURIComponent(base64Encode("http://web.traiot.cn/docs/wz/"+record.songhuodanpic))

      } else {
        this.$message.info('未上传相关资料！')
      }

    },
    add() {
      this.edit(this.modelDefault)
    },
    edit(record) {
      this.model = Object.assign({}, record)
      this.visible = true
    },
    submitForm() {
      const that = this
      // 触发表单验证
      this.$refs.form.validate((valid) => {
        if (valid) {
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
          httpAction(httpurl, this.model, method)
            .then((res) => {
              if (res.success) {
                that.$message.success(res.message)
                that.$emit('ok')
              } else {
                that.$message.warning(res.message)
              }
            })
            .finally(() => {
              that.confirmLoading = false
            })
        }
      })
    },
  },
}
</script>