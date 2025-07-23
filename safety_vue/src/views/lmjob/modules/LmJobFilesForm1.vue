<template>
  <j-modal
    centered
    :title="title"
    :width="1200"
    :visible="visible"
    @ok="handleOk"
    :okButtonProps="{ class: { 'jee-hidden': disableSubmit } }"
    @cancel="handleCancel"
    cancelText="关闭"
  >
      <a-tabs v-model="activeKey" @change="handleChangeTabs">
        <a-tab-pane tab="文件上传列表"  :forceRender="true">
          <j-editable-table
            :loading="loading"
            :columns="columns"
            :dataSource="dataSource1"
            :maxHeight="300"
            :disabled="formDisabled"
            :rowNumber="true"
            :rowSelection="true"
            :actionButton="true"
            @valueChange="handleValueChange"
            @confirm="handleConfirmDelete">

            <span slot="action" slot-scope="text, record">
              <a-upload style="margin-left: 20%" :action="uploadAction" :headers="headers" :default-file-list="defaultFileList" @change="handleChange(record,{ file, fileList })">
              <a-button  ><a-icon type="upload"/>
                文件上传
              </a-button>
            </a-upload>
            </span>
          </j-editable-table>
        </a-tab-pane>
      </a-tabs>
  </j-modal>
</template>

<script>

  import { httpAction, getAction } from '@/api/manage'
  import {FormTypes, getRefPromise} from "@/utils/JEditableTableUtil";
  import { JEditableTableMixin } from '@/mixins/JEditableTableMixin'
  import { cloneObject, validateDuplicateValue } from '@/utils/util'
  import JFormContainer from '@/components/jeecg/JFormContainer'
  import JEditableTable from '@comp/jeecg/JEditableTable'

  export default {
    name: 'LmJobFilesForm',
    mixins: [JEditableTableMixin],
    components: {
      JEditableTable,JEditableTableMixin,JFormContainer
    },
    props: {
      //表单禁用
      disabled: {
        type: Boolean,
        default: false,
        required: false
      }
    },
    data () {
      return {
        model:{
         },
        form: this.$form.createForm(this),
        visible: false,
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        confirmLoading: false,
        validatorRules: {
        },
        mtype: '',
        dataSource1:[],
        headers:{},
        uploadAction:window._CONFIG['domianURL']+"/sys/common/upload",
        defaultFileList:[],
        fileList:'',
        // 新增时子表默认添加几行空数据
        addDefaultRowNum: 0,
        refKeys: ['bhzRecipedetail',],
        tableKeys: ['bhzRecipedetail',],
        activeKey: 'bhzRecipedetail',
        loading: false,
        columns: [

          {
            title: '阶段类型',
            key: 'type',
            dictCode:'jobtype',
            width: '300px',
            type: FormTypes.select,
            //slotName: 'materialTypes',
            options: [],
            placeholder: '请选择${title}',
            validateRules: [{ required: true, message: '请选择${title}' }]
          },

          {
            title: '文件类型',
            key: 'filetype',
            dictCode:'',
            type: FormTypes.select,
            width: '300px',
            //slotName: 'materialname',
            options: [],
            placeholder: '请输入${title}',
            validateRules: [{ required: true, message: '请选择${title}' }]
          },
          {
            title: '上传文件',
            key: 'fileurl',
            dataIndex: 'action',
            align:"center",
            scopedSlots: { customRender: 'action' }
          },

        ],
        // 文件子表
        bhzRecipedetailTable: {

          dataSource: [],

        },
        list1: '/sys/dictItem/cailiaonamelist',
        url: {
          add: "/lmjob/lmJobFiles/add",
          edit: "/lmjob/lmJobFiles/edit",
          queryById: "/lmjob/lmJobFiles/queryById"
        }
      }
    },
    computed: {
      formDisabled(){
        return this.disabled
      },
    },
    created () {
       //备份model原始值
      this.modelDefault = JSON.parse(JSON.stringify(this.model));

    },
    methods: {

      getList({ id, sysOrgCode }) {
        this.dataSource1 = []
        let params = {
          infoid: id,
          sysOrgCode: sysOrgCode+'*',
        }
        //获取基础信息和执行情况
        getAction('/lmjob/lmJobFiles/list', params).then((res) => {
          if (res.code == 200) {
            this.dataSource1 = res.result.records

          } else {
            this.$message.warning(res.message)
          }
        })

      },


      /** 整理成formData */
      classifyIntoFormData(allValues) {
        let main = Object.assign(this.model, allValues.formValue)
        return {
          ...main, // 展开
          bhzRecipedetailList: allValues.tablesValue[0].values,
        }
      },

      // 文件上传
      handleChange(record,{ file, fileList }) {
        if (file.status !== 'uploading') {
          this.fileList=this.fileList+','+file.response.message;
        }
        console.log(this.fileList)
      },

      cailiaoname: function () {//材料名称下拉选择
        console.log(this.mtype)
        let params = { itemValue: this.mtype }
        this.bhzRecipedetailTable.columns[1].options = []
        getAction(this.url.list1, params)
          .then(res => {
            // console.log(res, 'MMMMMMMMMMMMMMMMMMMMMMMMMM')
            if (res.success) {
              let data = res.result
              console.log(this.bhzRecipedetailTable.columns[1].options)
              for (let i = 0; i < data.length; i++) {
                this.bhzRecipedetailTable.columns[1].options.push({
                  text: data[i].itemValue,
                  title: data[i].itemValue,
                  value: data[i].itemValue
                })
              }
            }
          })
      },

      /** 当选项被改变时，联动其他组件 */
      handleValueChange(event) {
        const { type, row, column, value, target } = event
        console.log(event)
        if (type === FormTypes.select) {
          // 第一列
          if (column.key === 'jobtype') {
            this.mtype=value;//拿到选择的类型并赋值
            console.log(this.mtype)
            this.cailiaoname();
          }
        }
      },
      handleConfirmDelete() {
        this.removeSelectedRows()
      },
      /** 删除被选中的行 */
      removeSelectedRows() {
        console.log(this.selectedRowIds)
        this.removeRows(this.selectedRowIds)
        this.selectedRowIds = []
      },
      /** 删除一行或多行 */
      removeRows(id) {
        let ids = id
        if (!(id instanceof Array)) {
          if (typeof id === 'string') {
            ids = [id]
          } else {
            throw  `JEditableTable.removeRows() 函数需要的参数可以是string或Array类型，但提供的却是${typeof id}`
          }
        }

        let rows = cloneObject(this.rows)
        ids.forEach(removeId => {
          removeId = this.getCleanId(removeId)
          // 找到每个id对应的真实index并删除
          const findAndDelete = (arr) => {
            for (let i = 0; i < arr.length; i++) {
              let currentId = this.getCleanId(arr[i].id)
              if (currentId === removeId) {
                arr.splice(i, 1)
                return true
              }
            }
          }
          // 找到rows对应的index，并删除
          if (findAndDelete(rows)) {
            // 找到values对应的index，并删除
            findAndDelete(this.inputValues)
            // 将caseId去除
            let id = this.getCleanId(removeId)
            this.deleteIds.push(id)
          }
        })
        this.rows = rows
        this.$emit('deleted', this.getDeleteIds(), this)
        this.$nextTick(() => {
          // 更新formValues
          this.updateFormValues()
        })
        return true
      },

      addBefore() {
        this.form.resetFields()
        this.bhzRecipedetailTable.dataSource = []
      },
      getAllTable() {
        let values = this.tableKeys.map(key => getRefPromise(this, key))
        return Promise.all(values)
      },

      handleCancel() {
        this.visible = false
      },
      handleOk() {
        this.visible = false
      },


      add () {
        this.edit(this.modelDefault);
      },
      edit (record) {
        this.model = Object.assign({}, record);
        this.visible = true;
      },
      submitForm () {
        const that = this;
        // 触发表单验证
        this.$refs.form.validate(valid => {
          if (valid) {
            that.confirmLoading = true;
            let httpurl = '';
            let method = '';
            if(!this.model.id){
              httpurl+=this.url.add;
              method = 'post';
            }else{
              httpurl+=this.url.edit;
               method = 'put';
            }
            httpAction(httpurl,this.model,method).then((res)=>{
              if(res.success){
                that.$message.success(res.message);
                that.$emit('ok');
              }else{
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
            })
          }
         
        })
      },
    }
  }
</script>