<template>
  <a-modal
    :title="title"
    :width="1500"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleSubmit"
    @cancel="handleCancel"
    cancelText="关闭"
    style="top: 20px; height: 100%; overflow: auto; padding-bottom: 53px"
  >
    <template slot="title">
      <div style="width: 100%">
        <span>{{ title }}</span>
        <span style="display: inline-block; width: calc(100% - 51px); padding-right: 10px; text-align: right">
          <a-button @click="toggleScreen" icon="appstore" style="height: 20px; width: 20px; border: 0px"></a-button>
        </span>
      </div>
    </template>
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
        <a-form-item label="巡检名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input
            placeholder="请输入计划名称"
            v-decorator.trim="['username', validatorRules.username]"
            :readOnly="!!model.id"
          />
        </a-form-item>

        <template v-if="!model.id"> </template>

        <a-form-item label="所属机构" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag />
        </a-form-item>
        <a-form-item label="巡检位置" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-select label-in-value :default-value="{}" @change="handleChange">
            <a-select-option v-for="(item, index) in reslut" :key="index">{{ item.title }}</a-select-option>
          </a-select>
          <!-- <j-dict-select-tag  v-decorator.trim="[ 'tight', validatorRules.realname]"/> -->
        </a-form-item>

        <a-form-item label="排查人" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-select-multi-user
            style="width: 200"
            v-decorator.trim="['desportMan']"
            :returnId="true"
            :multiple="false"
            :query-config="selectUserQueryConfig"
          />
        </a-form-item>

        <a-form-item label="部门" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input></a-input>
        </a-form-item>

        <!-- <a-form-item label="类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-select label-in-value :default-value="{}" @change="handleChange">
            <a-select-option v-for="(item, index) in reslut" :key="index">{{ item.title }}</a-select-option>
          </a-select>
        </a-form-item> -->

        <!-- <a-form-item label="排查人" :labelCol="labelCol" :wrapperCol="wrapperCol" >
          <a-input placeholder="请输入排查人" v-decorator.trim="[ 'realname', validatorRules.realname]" />
        </a-form-item> -->

        <a-form-item label="通知人" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input disabled v-decorator.trim="['programName']" />
          <!-- <j-dict-select-tag  /> -->
        </a-form-item>
        <a-form-item label="巡检时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date
            :showTime="true"
            dateFormat="YYYY-MM-DD HH:mm:ss"
            v-decorator.trim="['Time', validatorRules.realname]"
          />
        </a-form-item>
      </a-form>
    </a-spin>
    <depart-window ref="departWindow" @ok="modalFormOk"></depart-window>

    <!-- <div class="middle"> -->
    <!-- <a-table :columns="columns" :data-source="data" bordered>
    <template
      v-for="col in ['name', 'age', 'address']"
      :slot="col"
      slot-scope="text, record, index"
    >
      <div :key="col">
        <a-input
          v-if="record.editable"
          style="margin: -5px 0"
          :value="text"
          @change="e => handleChange(e.target.value, record.key, col)"
        />
        <template v-else>
          {{ text }}
        </template>
      </div>
    </template>
    <template slot="operation" slot-scope="text, record, index">
      <div class="editable-row-operations">
        <span v-if="record.editable">
          <a @click="() => save(record.key)">Save</a>
          <a-popconfirm title="Sure to cancel?" @confirm="() => cancel(record.key)">
            <a>Cancel</a>
          </a-popconfirm>
        </span>
        <span v-else>
          <a :disabled="editingKey !== ''" @click="() => edit(record.key)">Edit</a>
        </span>
      </div>
    </template>
  </a-table> -->
    <!-- <p>1</p>
    <div class="all">
    <div class="left">
      <p>666666</p>
      <p>999999</p>
    </div>
    <div class="right">
      <p>888888</p>
      
    </div>
    </div>
    </div> -->

    <div id="components-layout-demo-basic">
      <a-layout>
        <a-layout-sider style="flex: 0 0 50px; min-width: 50px">1</a-layout-sider>
        <a-layout>
          <a-layout-content>
            <a-row>
              <a-col :span="1"> 基础信息 </a-col>
              <a-col :span="12">
                <a-form-item label="隐患内容" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input
                    
                    v-decorator.trim="['username', validatorRules.username]"
                    :readOnly="!!model.id"
                  />
                </a-form-item>
                <a-form-item label="等级" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input
                    
                    v-decorator.trim="['username', validatorRules.username]"
                    :readOnly="!!model.id"
                  />
                </a-form-item>
                <a-form-item label="处理措施" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input
                    
                    v-decorator.trim="['username', validatorRules.username]"
                    :readOnly="!!model.id"
                  />
                </a-form-item>
                <a-form-item label="整改人" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input
                    
                    v-decorator.trim="['username', validatorRules.username]"
                    :readOnly="!!model.id"
                  />
                </a-form-item>
                <a-form-item label="整改完成时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input
                    
                    v-decorator.trim="['username', validatorRules.username]"
                    :readOnly="!!model.id"
                  />
                </a-form-item>
              </a-col>
            </a-row>
          </a-layout-content>
          <a-layout-content>
            <a-row>
              <a-col :span="1"> 整改信息 </a-col>
              <a-col :span="12">
                <a-form-item label="整改结果" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input
                    
                    v-decorator.trim="['username', validatorRules.username]"
                    :readOnly="!!model.id"
                  />
                </a-form-item>
                <a-form-item label="整改人" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input
                    
                    v-decorator.trim="['username', validatorRules.username]"
                    :readOnly="!!model.id"
                  />
                </a-form-item>
                <a-form-item label="文件上传" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <j-upload :fileType="fileType" v-decorator.trim="['tight', validatorRules.realname]"></j-upload>
                </a-form-item>
              </a-col>
            </a-row>
          </a-layout-content>
        </a-layout>
      </a-layout>
    </div>
  </a-modal>
</template>
<script>
const columns = [
  {
    title: 'name',
    dataIndex: 'name',
    width: '25%',
    scopedSlots: { customRender: 'name' },
  },
  {
    title: 'age',
    dataIndex: 'age',
    width: '15%',
    scopedSlots: { customRender: 'age' },
  },
  {
    title: 'address',
    dataIndex: 'address',
    width: '40%',
    scopedSlots: { customRender: 'address' },
  },
  {
    title: 'operation',
    dataIndex: 'operation',
    scopedSlots: { customRender: 'operation' },
  },
]
const data = []
for (let i = 0; i < 100; i++) {
  data.push({
    key: i.toString(),
    name: `Edrward ${i}`,
    age: 32,
    address: `London Park no. ${i}`,
  })
}
import pick from 'lodash.pick'
import moment from 'moment'
import Vue from 'vue'
// 引入搜索部门弹出框的组件
import departWindow from './DepartWindow'
import JSelectPosition from '@/components/jeecgbiz/JSelectPosition'
import { ACCESS_TOKEN, USER_INFO } from '@/store/mutation-types'
import { getAction } from '@/api/manage'
import { addUser, editUser, queryUserRole, queryall, duplicateCheck, getDictItemsFromCache } from '@/api/api'
import { disabledAuthFilter } from '@/utils/authFilter'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import JSelectMultiUser from '@/components/jeecgbiz/JSelectMultiUser'
export default {
  name: 'ProgrammeModal',
  mixins: [JeecgListMixin],
  components: {
    departWindow,
    JSelectPosition,
    JSelectMultiUser,
  },
  data() {
    this.cacheData = data.map((item) => ({ ...item }))
    return {
      data,
      columns,
      editingKey: '',
      previewVisible: false,
      previewImage: '',
      reslut: [],
      fileType: ['pdf'],
      departDisabled: false, //是否是我的部门调用该页面
      roleDisabled: false, //是否是角色维护调用该页面
      modalWidth: 800,
      modaltoggleFlag: true,
      confirmDirty: false,
      selectedDepartKeys: [], //保存用户选择部门id
      checkedDepartKeys: [],
      checkedDepartNames: [], // 保存部门的名称 =>title
      checkedDepartNameString: '', // 保存部门的名称 =>title
      resultDepartOptions: [],
      userId: '', //保存用户id
      disableSubmit: false,
      userDepartModel: { userId: '', departIdList: [] }, // 保存SysUserDepart的用户部门中间表数据需要的对象
      dateFormat: 'YYYY-MM-DD',
      validatorRules: {
        username: {
          rules: [
            {
              required: true,
              message: '请输入用户账号!',
            },
            {
              validator: this.validateUsername,
            },
          ],
        },
        password: {
          rules: [
            {
              required: true,
              pattern: /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[~!@#$%^&*()_+`\-={}:";'<>?,./]).{8,}$/,
              message: '密码由8位数字、大小写字母和特殊符号组成!',
            },
            {
              validator: this.validateToNextPassword,
            },
          ],
        },
        confirmpassword: {
          rules: [
            {
              required: true,
              message: '请重新输入登录密码!',
            },
            {
              validator: this.compareToFirstPassword,
            },
          ],
        },
        realname: { rules: [{ required: true, message: '请输入用户名称!' }] },
        phone: { rules: [{ validator: this.validatePhone }] },
        email: {
          rules: [
            {
              validator: this.validateEmail,
            },
          ],
        },
        roles: {},
        //  sex:{initialValue:((!this.model.sex)?"": (this.model.sex+""))}
        workNo: {
          rules: [{ required: true, message: '请输入工号' }, { validator: this.validateWorkNo }],
        },
        telephone: {
          rules: [{ pattern: /^0\d{2,3}-[1-9]\d{6,7}$/, message: '请输入正确的座机号码' }],
        },
      },
      departIdShow: false,
      departIds: [], //负责部门id
      title: '操作',
      visible: false,
      model: {},
      roleList: [],
      selectedRole: [],
      labelCol: {
        xs: { span: 24 },
        sm: { span: 5 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 },
      },
      uploadLoading: false,
      confirmLoading: false,
      headers: {},
      form: this.$form.createForm(this),
      userName: '',
      picUrl: '',
      url: {
        fileUpload: window._CONFIG['domianURL'] + '/sys/common/upload',
        userWithDepart: '/sys/user/userDepartList', // 引入为指定用户查看部门信息需要的url
        userId: '/sys/user/generateUserId', // 引入生成添加用户情况下的url
        syncUserByUserName: '/act/process/extActProcess/doSyncUserByUserName', //同步用户到工作流
        queryTenantList: '/sys/tenant/queryList',
      },
      identity: '1',
      fileList: [],
      tenantList: [],
      currentTenant: [],
    }
  },
  created() {
    const token = Vue.ls.get(ACCESS_TOKEN)
    this.headers = { 'X-Access-Token': token }
    this.initTenantList()
    let user = Vue.ls.get(USER_INFO)
    let userId = user.id
    this.userName = user.username
    console.log(this.userName)
  },
  computed: {
    uploadAction: function () {
      return this.url.fileUpload
    },
  },
  methods: {
    initUser() {
      const user = Vue.ls.get(USER_INFO)
      this.visible = true
      try {
        this.$nextTick(() => {
          this.form.setFieldsValue({ programName: user.realname }) // loadsh的pick方法
        })
      } catch (error) {
        console.log('赋值错误从哪里来的？')
      }
    },
    isDisabledAuth(code) {
      return disabledAuthFilter(code)
    },
    initTenantList() {
      getAction(this.url.queryTenantList).then((res) => {
        if (res.success) {
          this.tenantList = res.result
        }
      })
      this.reslut = getDictItemsFromCache('project_type')
    },
    //窗口最大化切换
    toggleScreen() {
      if (this.modaltoggleFlag) {
        this.modalWidth = window.innerWidth
      } else {
        this.modalWidth = 800
      }
      this.modaltoggleFlag = !this.modaltoggleFlag
    },
    initialRoleList() {
      queryall().then((res) => {
        if (res.success) {
          this.roleList = res.result
        } else {
          console.log(res.message)
        }
      })
    },
    loadUserRoles(userid) {
      queryUserRole({ userid: userid }).then((res) => {
        if (res.success) {
          this.selectedRole = res.result
        } else {
          console.log(res.message)
        }
      })
    },
    refresh() {
      this.selectedDepartKeys = []
      this.checkedDepartKeys = []
      this.checkedDepartNames = []
      this.checkedDepartNameString = ''
      this.userId = ''
      this.resultDepartOptions = []
      this.departId = []
      this.departIdShow = false
      this.currentTenant = []
    },
    add() {
      this.picUrl = ''
      this.refresh()
      this.initUser()
      this.edit({ activitiSync: '1' })
    },
    edit(record) {
      console.log(record)
      this.resetScreenSize() // 调用此方法,根据屏幕宽度自适应调整抽屉的宽度
      let that = this
      that.initialRoleList()
      that.checkedDepartNameString = ''
      that.form.resetFields()
      if (record.hasOwnProperty('id')) {
        that.loadUserRoles(record.id)
        setTimeout(() => {
          this.fileList = record.avatar
        }, 5)
      }
      that.userId = record.id
      that.visible = true
      that.model = Object.assign({}, record)
      that.$nextTick(() => {
        that.form.setFieldsValue(
          pick(
            this.model,
            'username',
            'sex',
            'programName',
            'realname',
            'desportMan',
            'email',
            'phone',
            'activitiSync',
            'workNo',
            'telephone',
            'post'
          )
        )
      })
      //身份为上级显示负责部门，否则不显示
      if (this.model.userIdentity == '2') {
        this.identity = '2'
        this.departIdShow = true
      } else {
        this.identity = '1'
        this.departIdShow = false
      }
      // 调用查询用户对应的部门信息的方法
      that.checkedDepartKeys = []
      that.loadCheckedDeparts()

      //update-begin-author:taoyan date:2020710 for:多租户配置
      if (!record.relTenantIds || record.relTenantIds.length == 0) {
        this.currentTenant = []
      } else {
        this.currentTenant = record.relTenantIds.split(',').map(Number)
      }
      //update-end-author:taoyan date:2020710 for:多租户配置
    },
    //
    loadCheckedDeparts() {
      let that = this
      if (!that.userId) {
        return
      }
      getAction(that.url.userWithDepart, { userId: that.userId }).then((res) => {
        that.checkedDepartNames = []
        if (res.success) {
          var depart = []
          var departId = []
          for (let i = 0; i < res.result.length; i++) {
            that.checkedDepartNames.push(res.result[i].title)
            this.checkedDepartNameString = this.checkedDepartNames.join(',')
            that.checkedDepartKeys.push(res.result[i].key)
            //新增负责部门选择下拉框
            depart.push({
              key: res.result[i].key,
              title: res.result[i].title,
            })
            departId.push(res.result[i].key)
          }
          that.resultDepartOptions = depart
          //判断部门id是否存在，不存在择直接默认当前所在部门
          if (this.model.departIds) {
            this.departIds = this.model.departIds.split(',')
          } else {
            this.departIds = departId
          }
          that.userDepartModel.departIdList = that.checkedDepartKeys
        } else {
          console.log(res.message)
        }
      })
    },
    close() {
      this.$emit('close')
      this.visible = false
      this.disableSubmit = false
      this.selectedRole = []
      this.userDepartModel = { userId: '', departIdList: [] }
      this.checkedDepartNames = []
      this.checkedDepartNameString = ''
      this.checkedDepartKeys = []
      this.selectedDepartKeys = []
      this.resultDepartOptions = []
      this.departIds = []
      this.departIdShow = false
      this.identity = '1'
      this.fileList = []
    },
    moment,
    handleSubmit() {
      const that = this
      // 触发表单验证
      this.form.validateFields((err, values) => {
        if (!err) {
          that.confirmLoading = true
          if (!values.birthday) {
            values.birthday = ''
          } else {
            values.birthday = values.birthday.format(this.dateFormat)
          }
          let formData = Object.assign(this.model, values)
          if (that.fileList != '') {
            formData.avatar = that.fileList
          } else {
            formData.avatar = null
          }
          //update-begin-author:taoyan date:2020710 for:多租户配置
          formData.relTenantIds = this.currentTenant.length > 0 ? this.currentTenant.join(',') : ''
          //update-end-author:taoyan date:2020710 for:多租户配置
          formData.selectedroles = this.selectedRole.length > 0 ? this.selectedRole.join(',') : ''
          formData.selecteddeparts =
            this.userDepartModel.departIdList.length > 0 ? this.userDepartModel.departIdList.join(',') : ''
          formData.userIdentity = this.identity
          //如果是上级择传入departIds,否则为空
          if (this.identity === '2') {
            formData.departIds = this.departIds.join(',')
          } else {
            formData.departIds = ''
          }
          // that.addDepartsToUser(that,formData); // 调用根据当前用户添加部门信息的方法
          let obj
          if (!this.model.id) {
            formData.id = this.userId
            obj = addUser(formData)
          } else {
            obj = editUser(formData)
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
              that.checkedDepartNames = []
              that.userDepartModel.departIdList = { userId: '', departIdList: [] }
              that.close()
            })
        }
      })
    },
    handleCancel() {
      this.close()
    },
    validateToNextPassword(rule, value, callback) {
      const form = this.form
      const confirmpassword = form.getFieldValue('confirmpassword')

      if (value && confirmpassword && value !== confirmpassword) {
        callback('两次输入的密码不一样！')
      }
      if (value && this.confirmDirty) {
        form.validateFields(['confirm'], { force: true })
      }
      callback()
    },
    compareToFirstPassword(rule, value, callback) {
      const form = this.form
      if (value && value !== form.getFieldValue('password')) {
        callback('两次输入的密码不一样！')
      } else {
        callback()
      }
    },
    validatePhone(rule, value, callback) {
      if (!value) {
        callback()
      } else {
        //update-begin--Author:kangxiaolin  Date:20190826 for：[05] 手机号不支持199号码段--------------------
        if (new RegExp(/^1[3|4|5|7|8|9][0-9]\d{8}$/).test(value)) {
          //update-end--Author:kangxiaolin  Date:20190826 for：[05] 手机号不支持199号码段--------------------

          var params = {
            tableName: 'sys_user',
            fieldName: 'phone',
            fieldVal: value,
            dataId: this.userId,
          }
          duplicateCheck(params).then((res) => {
            if (res.success) {
              callback()
            } else {
              callback('手机号已存在!')
            }
          })
        } else {
          callback('请输入正确格式的手机号码!')
        }
      }
    },
    validateEmail(rule, value, callback) {
      if (!value) {
        callback()
      } else {
        if (
          new RegExp(
            /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
          ).test(value)
        ) {
          var params = {
            tableName: 'sys_user',
            fieldName: 'email',
            fieldVal: value,
            dataId: this.userId,
          }
          duplicateCheck(params).then((res) => {
            console.log(res)
            if (res.success) {
              callback()
            } else {
              callback('邮箱已存在!')
            }
          })
        } else {
          callback('请输入正确格式的邮箱!')
        }
      }
    },
    validateUsername(rule, value, callback) {
      var params = {
        tableName: 'sys_user',
        fieldName: 'username',
        fieldVal: value,
        dataId: this.userId,
      }
      duplicateCheck(params).then((res) => {
        if (res.success) {
          callback()
        } else {
          callback('用户名已存在!')
        }
      })
    },
    validateWorkNo(rule, value, callback) {
      var params = {
        tableName: 'sys_user',
        fieldName: 'work_no',
        fieldVal: value,
        dataId: this.userId,
      }
      duplicateCheck(params).then((res) => {
        if (res.success) {
          callback()
        } else {
          callback('工号已存在!')
        }
      })
    },
    handleConfirmBlur(e) {
      const value = e.target.value
      this.confirmDirty = this.confirmDirty || !!value
    },

    normFile(e) {
      console.log('Upload event:', e)
      if (Array.isArray(e)) {
        return e
      }
      return e && e.fileList
    },
    beforeUpload: function (file) {
      var fileType = file.type
      if (fileType.indexOf('pdf') < 0) {
        this.$message.warning('请上传pdf')
        return false
      }
      //TODO 验证文件大小
    },
    handleChange(info) {
      this.picUrl = ''
      if (info.file.status === 'uploading') {
        this.uploadLoading = true
        return
      }
      if (info.file.status === 'done') {
        var response = info.file.response
        this.uploadLoading = false
        console.log(response)
        if (response.success) {
          this.model.avatar = response.message
          this.picUrl = 'Has no pic url yet'
        } else {
          this.$message.warning(response.message)
        }
      }
    },
    // 搜索用户对应的部门API
    onSearch() {
      this.$refs.departWindow.add(this.checkedDepartKeys, this.userId)
    },

    // 获取用户对应部门弹出框提交给返回的数据
    modalFormOk(formData) {
      this.checkedDepartNames = []
      this.selectedDepartKeys = []
      this.checkedDepartNameString = ''
      this.userId = formData.userId
      this.userDepartModel.userId = formData.userId
      this.departIds = []
      this.resultDepartOptions = []
      var depart = []
      for (let i = 0; i < formData.departIdList.length; i++) {
        this.selectedDepartKeys.push(formData.departIdList[i].key)
        this.checkedDepartNames.push(formData.departIdList[i].title)
        this.checkedDepartNameString = this.checkedDepartNames.join(',')
        //新增部门选择，如果上面部门选择后不为空直接付给负责部门
        depart.push({
          key: formData.departIdList[i].key,
          title: formData.departIdList[i].title,
        })
        this.departIds.push(formData.departIdList[i].key)
      }
      this.resultDepartOptions = depart
      this.userDepartModel.departIdList = this.selectedDepartKeys
      this.checkedDepartKeys = this.selectedDepartKeys //更新当前的选择keys
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
    identityChange(e) {
      if (e.target.value === '1') {
        this.departIdShow = false
      } else {
        this.departIdShow = true
      }
    },
  },
}
</script>
<style  scoped>
.avatar-uploader > .ant-upload {
  width: 104px;
  height: 104px;
}
.ant-upload-select-picture-card i {
  font-size: 49px;
  color: #999;
}

.ant-upload-select-picture-card .ant-upload-text {
  margin-top: 8px;
  color: #666;
}

.ant-table-tbody .ant-table-row td {
  padding-top: 10px;
  padding-bottom: 10px;
}

.drawer-bootom-button {
  position: absolute;
  bottom: -8px;
  width: 100%;
  border-top: 1px solid #e8e8e8;
  padding: 10px 16px;
  text-align: right;
  left: 0;
  background: #fff;
  border-radius: 0 0 2px 2px;
}
.middle {
  display: flex;
  width: 80%;
  margin: 0 auto;
  border: 1px solid rosybrown;
}
.middle > p {
  width: 60px;
  height: 500px;
  line-height: 500px;
  text-align: center;
  border: 1px solid silver;
  background: rgb(248, 248, 248);
}
.all {
  flex-direction: column;
  width: 100%;
}
.left {
  width: 100%;
  height: 250px;
  line-height: 250px;
  text-align: center;
  border: 1px solid silver;
  background: rgb(66, 199, 106);
}
.left > p {
  width: 60px;
  height: 250px;
  line-height: 250px;
  text-align: center;
  border: 1px solid silver;
  background: rgb(50, 119, 184);
}
.left > p:nth-child(1) {
  flex-direction: column;
  width: 60px;
  height: 250px;
  line-height: 250px;
  text-align: center;
  border: 1px solid silver;
  background: rgb(50, 119, 184);
}
.right {
  width: 100%;
  height: 250px;
  line-height: 250px;
  text-align: center;
  border: 1px solid silver;
  background: #fff;
}
.right > p {
  width: 60px;
  height: 250px;
  line-height: 250px;
  text-align: center;
  border: 1px solid silver;
  background: #fff;
}
#components-layout-demo-basic {
  text-align: center;
  line-height: 10px;
}
#components-layout-demo-basic .ant-layout-header,
#components-layout-demo-basic .ant-layout-footer {
  background: rgb(155, 224, 128);
  color: #fff;
}
#components-layout-demo-basic .ant-layout-footer {
  line-height: 1.5;
}
#components-layout-demo-basic .ant-layout-sider {
  color: #fff;
  line-height: 120px;
}
#components-layout-demo-basic .ant-layout-content {
  color: #fff;
  min-height: 120px;
  line-height: 120px;
}
#components-layout-demo-basic > .ant-layout {
  margin-bottom: 48px;
}
#components-layout-demo-basic > .ant-layout:last-child {
  margin: 0 30px;
}
.ant-col-1 {
  width: 10%;
  color: #000;
}
.ant-layout .ant-layout-has-sider{
  width: 50%;
}
#components-layout-demo-basic .ant-layout-sider {
  color: #000;
  line-height: 500px;
}
#components-layout-demo-basic{
  padding: 0 200px;
  width:100%;
}
.ant-layout{
  background: #fff;
}
.ant-layout-sider{
  background: #fbfbfb;
}
</style>