<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <!--        <a-divider type="horizontal" ><span style="color:#0785fd">路段</span></a-divider>-->
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="项目名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-model="model.proname" placeholder="请输入项目名称"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="桩号里程" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="lmname">
              <a-input v-model="model.lmname" placeholder="请输入施工路段"></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="施工路段" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <JselectDqProjName @change="changeOne" ::multi="false" placeholder="请选择" />
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="路面结构类型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="wbstype">
              <a-input v-model="model.wbstype" placeholder="请输入路面结构类型"></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
        <!--          <a-divider type="horizontal" ><span style="color:#0785fd">底基层</span></a-divider>-->
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="底基层总方量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="djcInfo1">
              <a-input v-model="model.djcInfo1" placeholder="请输入底基层混合料总方量"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="供应商" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="djcInfo2">
              <a-input v-model="model.djcInfo2" placeholder="请输入底基层混合料供应商"></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
        <!--          <a-divider type="horizontal" ><span style="color:#0785fd">基层</span></a-divider>-->
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="基层总方量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jcInfo1">
              <a-input v-model="model.jcInfo1" placeholder="请输入基层混合料总方量"></a-input>
            </a-form-model-item>
          </a-col>

          <a-col :span="12">
            <a-form-model-item label="供应商" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jcInfo2">
              <a-input v-model="model.jcInfo2" placeholder="请输入基层供应商"></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
        <!--          <a-divider type="horizontal" ><span style="color:#0785fd">下面层</span></a-divider>-->
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="下面层总方量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="xmcInfo1">
              <a-input v-model="model.xmcInfo1" placeholder="请输入下面层混合料总方量"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="供应商" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="xmcInfo2">
              <a-input v-model="model.xmcInfo2" placeholder="请输入下面层混合料供应商"></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
        <!--          <a-divider type="horizontal" ><span style="color:#0785fd">中面层</span></a-divider>-->
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="中面层总方量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="zmcInfo1">
              <a-input v-model="model.zmcInfo1" placeholder="请输入中面层混合料总方量"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="供应商" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="zmcInfo2">
              <a-input v-model="model.zmcInfo2" placeholder="请输入中面层混合料供应商"></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
        <!--          <a-divider type="horizontal" ><span style="color:#0785fd">上面层</span></a-divider>-->
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="上面层总方量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="smcInfo1">
              <a-input v-model="model.smcInfo1" placeholder="请输入上面层混合料总方量"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
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
      <a-tab-pane tab="准备阶段" key="1" :forceRender="true">
        <a-table
          rowKey="id"
          bordered
          :columns="columns1"
          :dataSource="dataSource1"
          :pagination="false"
          class="j-table-force-nowrap"
        >
          <span slot="action" slot-scope="text, record">
            <a @click="preview(record)">预览</a>
             <a-divider type="vertical" />
              <a @click="download(record)">下载</a>
          </span>
        </a-table>
      </a-tab-pane>
      <a-tab-pane tab="施工阶段" key="2" :forceRender="true">
        <a-table
          rowKey="id"
          bordered
          :columns="columns2"
          :dataSource="dataSource2"
          :pagination="false"
          class="j-table-force-nowrap"
        >
         <span slot="action" slot-scope="text, record">
            <a @click="preview(record)">预览</a>
             <a-divider type="vertical" />
              <a @click="download(record)">下载</a>
          </span>
        </a-table>
      </a-tab-pane>
    </a-tabs>
  </a-spin>
</template>

<script>
import { httpAction, getAction } from '@/api/manage'
import JFormContainer from '@/components/jeecg/JFormContainer'
import JDate from '@/components/jeecg/JDate'
import JselectDqDepart from '@/components/jeecgbiz/JselectDqDepart'
import JselectDqProjName from '@/components/jeecgbiz/JselectDqProjName'
import { JEditableTableMixin } from '@/mixins/JEditableTableMixin'
import {base64Encode} from "@api/kkfileView";
import Vue from "vue";
import {ACCESS_TOKEN} from "@/store/mutation-types";

export default {
  name: 'LmJobInfoForm',
  mixins: [JEditableTableMixin],
  components: {
    JselectDqProjName,
    JFormContainer,
    JDate,
    JselectDqDepart,
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
      columns1: [
        {
          title: '文件名称',
          align: 'center',
          dataIndex: 'filename',
        },
        {
          title: '文件类型',
          align: 'center',
          dataIndex: 'filetype_dictText',
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
          title: '文件名称',
          align: 'center',
          dataIndex: 'filename',
        },
        {
          title: '文件类型',
          align: 'center',
          dataIndex: 'filetype_dictText',
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
        add: '/lmjob/lmJobInfo/add',
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
    changeOne(val) {
      this.model.wbsOrgCode = val
      let params = {
        orgCode: val,
      }
      getAction('/sydpssysample/syDpsSySample/getProjNames', params).then((res) => {
        if (res.success) {this.model.zhlc = res.result}})
    },
    execute() {
      let params1 = {
        infoid: this.model.id,
        filetype: 1 + '*',
      }
      //准备阶段
      getAction('/lmjob/lmJobFiles/list', params1).then((res) => {
        if (res.code == 200) {
          this.dataSource1 = res.result.records
        } else {
          this.$message.warning(res.message)
        }
      })
      let params2 = {
        infoid: this.model.id,
        filetype: 2 + '*',
      }
      //施工阶段
      getAction('/lmjob/lmJobFiles/list', params2).then((res) => {
        if (res.code == 200) {
          this.dataSource2 = res.result.records
        } else {
          this.$message.warning(res.message)
        }
      })
    },
    download({ fileurl,filename }) {
      if (fileurl) {
        window.open(fileurl, '_self')
      }

    },
    preview({ fileurl }) {

      if ( fileurl ) {
        if(fileurl.indexOf("http") >-1){
          let url = window._CONFIG['onlinePreviewDomainURL'] + '?url=' + encodeURIComponent(base64Encode(fileurl ))
          window.open(url, '_blank')
        }else{
          let url = "http://web.traiot.cn/docs/wz/"+fileurl;
          window.open(url, '_blank')
        }
        // let url = window._CONFIG['onlinePreviewDomainURL'] + '?url=' + encodeURIComponent(base64Encode("http://web.traiot.cn/docs/wz/"+record.songhuodanpic))

      }else{
        this.$message.info("未上传相关资料！");
      }

    },
    add() {
      this.edit(this.modelDefault)
    },
    edit(record) {
      this.model = Object.assign({}, record)
      this.visible = true
      this.execute()
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