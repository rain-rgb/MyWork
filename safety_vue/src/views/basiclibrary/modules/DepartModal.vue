<template>
  <a-drawer
    :title="title"
    :width="drawerWidth"
    @close="handleCancel"
    :visible="visible"
    :confirmLoading="confirmLoading"
  >
    <div :style="{ width: '100%', border: '1px solid #e9e9e9', padding: '10px 16px', background: '#fff' }">
      <a-spin :spinning="confirmLoading">
        <a-form :form="form">
          <a-form-item label="新增树形" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-radio-group @change="onChangeMenuType" v-decorator="['menuType', { initialValue: localMenuType }]">
              <a-radio :value="0">工程名称</a-radio>
              <a-radio :value="1">工程分支</a-radio>
              <a-radio :value="2">最后一级</a-radio>
            </a-radio-group>
          </a-form-item>

          <a-form-item
            v-show="localMenuType != 0"
            label="上一级别"
            :labelCol="labelCol"
            :wrapperCol="wrapperCol"
            :validate-status="validateStatus"
            :hasFeedback="true"
            :required="true"
          >
            <span slot="help">{{ validateStatus == 'error' ? '请选择上级菜单' : '&nbsp;&nbsp;' }}</span>
            <a-tree-select
              style="width: 100%"
              :dropdownStyle="{ maxHeight: '200px', overflow: 'auto' }"
              :treeData="treeData"
              v-model="model.parentId"
              placeholder="请选择父级菜单"
              :disabled="disableSubmit"
              @change="handleParentIdChange"
            >
            </a-tree-select>
          </a-form-item>
          <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="编号">
            <a-input
              placeholder="（验证功能完成中）"
              v-decorator="['num', { rules: [{ required: true, message: '请输入编号！' }] }]"
              :readOnly="disableSubmit"
            />
          </a-form-item>

          <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="名称" v-show="localMenuType != 2">
            <a-input
              placeholder="请输入名称"
              v-decorator="['cname', { rules: [{ required: true, message: '请输入名称！' }] }]"
              :readOnly="disableSubmit"
            />
          </a-form-item>

          <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="风险描述" v-show="localMenuType === 2">
            <a-input
              placeholder="请输入内容"
              v-decorator="['cname', { rules: [{ required: true, message: '请输入名称！' }] }]"
              :readOnly="disableSubmit"
            />
          </a-form-item>

          <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="预测事故类型" v-show="localMenuType === 2">
            <a-input
              placeholder="请输入内容"
              v-decorator="['cname', { rules: [{ required: true, message: '请输入名称！' }] }]"
              :readOnly="disableSubmit"
            />
          </a-form-item>

          <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="LEC法风险估测" v-show="localMenuType === 2" :required="true">
            <b>L：</b>
            <span style="margin-right: 15px">
              <a-input-number :min="0" :max="100000" :default-value="'0'" style="width: 60px" :required="true"/>
            </span>

            <b>E：</b>
            <span style="margin-right: 15px">
              <a-input-number :min="0" :max="100000" :default-value="'0'" style="width: 60px" :required="true"/>
            </span>

            <b>C：</b>
            <span style="margin-right: 15px">
              <a-input-number :min="0" :max="100000" :default-value="'0'" style="width: 60px" :required="true"/>
            </span>

            <b>D：</b>
            <span style="margin-right: 15px">
              <a-input-number :min="0" :max="100000" :default-value="'0'" style="width: 60px" :required="true"/>
            </span>
          </a-form-item>

          <a-form-item
            v-show="localMenuType === 2"
            label="风险等级"
            :labelCol="labelCol"
            :wrapperCol="wrapperCol"
            :validate-status="validateStatus"
            :required="true"
          >
            <a-select v-model="TT" placeholder="请选择风险等级">
              <a-select-option value="di">低度风险</a-select-option>
              <a-select-option value="zhong">显著风险</a-select-option>
              <a-select-option value="gao">高度风险</a-select-option>
            </a-select>
          </a-form-item>

          <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="风险控制措施" v-show="localMenuType === 2">
            <a-input
              placeholder="请输入内容"
              v-decorator="['cname', { rules: [{ required: true, message: '请输入名称！' }] }]"
              :readOnly="disableSubmit"
            />
          </a-form-item>

          <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="残余判断" v-show="localMenuType === 2" >
            <a-input placeholder="请输入内容" :readOnly="disableSubmit" />
          </a-form-item>
          <!--update_end author:wuxianquan date:20190908 for:增加组件，外链打开方式可选 -->
        </a-form>

        <!-- 选择图标 -->
        <icons @choose="handleIconChoose" @close="handleIconCancel" :iconChooseVisible="iconChooseVisible"></icons>
      </a-spin>
      <a-row :style="{ textAlign: 'right' }">
        <a-button :style="{ marginRight: '8px' }" @click="handleCancel"> 关闭 </a-button>
        <a-button :disabled="disableSubmit" @click="handleOk" type="primary">确定</a-button>
      </a-row>
    </div>
  </a-drawer>
</template>

<script>
import { addPermission, editPermission, queryTreeList, duplicateCheck } from '@/api/api'
import Icons from './icon/Icons'
import pick from 'lodash.pick'

export default {
  name: 'PermissionModal',
  components: { Icons },
  data() {
    return {
      TT:[],
      drawerWidth: 700,
      treeData: [],
      treeValue: '0-0-4',
      title: '操作',
      visible: false,
      disableSubmit: false,
      model: {},
      localMenuType: 0,
      alwaysShow: false, //表单元素-聚合路由
      menuHidden: false, //表单元素-隐藏路由
      routeSwitch: true, //是否路由菜单
      /*update_begin author:wuxianquan date:20190908 for:定义变量，初始值代表内部打开*/
      internalOrExternal: false, //菜单打开方式
      /*update_end author:wuxianquan date:20190908 for:定义变量，初始值代表内部打开*/
      isKeepalive: true, //是否缓存路由
      show: true, //根据菜单类型，动态显示隐藏表单元素
      menuLabel: '菜单名称',
      isRequrie: true, // 是否需要验证
      labelCol: {
        xs: { span: 24 },
        sm: { span: 5 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 },
      },

      confirmLoading: false,
      form: this.$form.createForm(this),

      iconChooseVisible: false,
      validateStatus: '',
    }
  },
  computed: {
    validatorRules: function () {
      return {
        name: { rules: [{ required: true, message: '请输入菜单标题!' }] },
        component: { rules: [{ required: this.show, message: '请输入前端组件!' }] },
        url: { rules: [{ required: this.show, message: '请输入菜单路径!' }] },
        permsType: { rules: [{ required: true, message: '请输入授权策略!' }] },
        sortNo: { initialValue: 1.0 },
      }
    },
  },
  created() {
    this.initDictConfig()
  },
  methods: {
    loadTree() {
      var that = this
      queryTreeList().then((res) => {
        if (res.success) {
          console.log('----queryTreeList---')
          console.log(res)
          that.treeData = []
          let treeList = res.result.treeList
          for (let a = 0; a < treeList.length; a++) {
            let temp = treeList[a]
            temp.isLeaf = temp.leaf
            that.treeData.push(temp)
          }
        }
      })
    },
    add() {
      // 默认值
      this.edit({ status: '1', permsType: '1', route: true })
    },
    edit(record) {
      this.resetScreenSize() // 调用此方法,根据屏幕宽度自适应调整抽屉的宽度
      this.form.resetFields()
      this.model = Object.assign({}, record)
      //--------------------------------------------------------------------------------------------------
      //根据菜单类型，动态展示页面字段
      console.log(record)
      this.alwaysShow = !record.alwaysShow ? false : true
      this.menuHidden = !record.hidden ? false : true

      if (record.route != null) {
        this.routeSwitch = record.route ? true : false
      }

      if (record.keepAlive != null) {
        this.isKeepalive = record.keepAlive ? true : false
      } else {
        this.isKeepalive = false // 升级兼容 如果没有（后台没有传过来、或者是新建）默认为false
      }

      /*update_begin author:wuxianquan date:20190908 for:编辑初始化数据*/
      if (record.internalOrExternal != null) {
        this.internalOrExternal = record.internalOrExternal ? true : false
      } else {
        this.internalOrExternal = false
      }
      /*update_end author:wuxianquan date:20190908 for:编辑初始化数据*/

      //console.log('record.menuType', record.menuType);
      this.show = record.menuType == 2 ? false : true
      this.menuLabel = record.menuType == 2 ? '按钮/权限' : '菜单名称'

      if (this.model.parentId) {
        this.localMenuType = 1
      } else {
        this.localMenuType = 0
      }
      //----------------------------------------------------------------------------------------------

      this.visible = true
      this.loadTree()
      let fieldsVal = pick(
        this.model,
        'name',
        'perms',
        'permsType',
        'component',
        'redirect',
        'url',
        'sortNo',
        'menuType',
        'status'
      )
      this.$nextTick(() => {
        this.form.setFieldsValue(fieldsVal)
      })
    },
    close() {
      this.$emit('close')
      this.disableSubmit = false
      this.visible = false
    },
    handleOk() {
      const that = this
      // 触发表单验证
      this.form.validateFields((err, values) => {
        if (!err) {
          this.model.alwaysShow = this.alwaysShow
          this.model.hidden = this.menuHidden
          this.model.route = this.routeSwitch
          this.model.keepAlive = this.isKeepalive
          /*update_begin author:wuxianquan date:20190908 for:获取值*/
          this.model.internalOrExternal = this.internalOrExternal
          /*update_end author:wuxianquan date:20190908 for:获取值*/

          let formData = Object.assign(this.model, values)
          if ((formData.menuType == 1 || formData.menuType == 2) && !formData.parentId) {
            that.validateStatus = 'error'
            that.$message.error('请检查你填的类型以及信息是否正确！')
            return
          } else {
            that.validateStatus = 'success'
          }
          that.confirmLoading = true
          console.log(formData)
          let obj
          if (!this.model.id) {
            obj = addPermission(formData)
          } else {
            obj = editPermission(formData)
          }
          obj
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
              that.close()
            })
        }
      })
    },
    handleCancel() {
      this.close()
    },
    validateNumber(rule, value, callback) {
      if (!value || new RegExp(/^[0-9]*[1-9][0-9]*$/).test(value)) {
        callback()
      } else {
        callback('请输入正整数!')
      }
    },
    validatePerms(rule, value, callback) {
      if (value && value.length > 0) {
        //校验授权标识是否存在
        var params = {
          tableName: 'sys_permission',
          fieldName: 'perms',
          fieldVal: value,
          dataId: this.model.id,
        }
        duplicateCheck(params).then((res) => {
          if (res.success) {
            callback()
          } else {
            callback('授权标识已存在!')
          }
        })
      } else {
        callback()
      }
    },
    onChangeMenuType(e) {
      //console.log('localMenuType checked', e.target.value)
      this.localMenuType = e.target.value
      if (e.target.value == 2) {
        this.show = false
        this.menuLabel = '按钮/权限'
      } else {
        this.show = true
        this.menuLabel = '菜单名称'
      }
      this.$nextTick(() => {
        this.form.validateFields(['url', 'component'], { force: true })
      })
    },
    selectIcons() {
      this.iconChooseVisible = true
    },
    handleIconCancel() {
      this.iconChooseVisible = false
    },
    handleIconChoose(value) {
      console.log(value)
      this.model.icon = value
      this.form.icon = value
      this.iconChooseVisible = false
    },
    // 根据屏幕变化,设置抽屉尺寸
    resetScreenSize() {
      let screenWidth = document.body.clientWidth
      if (screenWidth < 500) {
        this.drawerWidth = screenWidth
      } else {
        this.drawerWidth = 700
      }
    },
    initDictConfig() {},
    handleParentIdChange(value) {
      if (!value) {
        this.validateStatus = 'error'
      } else {
        this.validateStatus = 'success'
      }
    },
  },
}
</script>

<style scoped>
</style>
