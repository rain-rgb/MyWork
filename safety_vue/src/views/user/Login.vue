<template>
  <div class="main">
    <a-form :form="form" class="user-layout-login" ref="formLogin" id="formLogin">
      <!-- <a-tabs
        :activeKey="customActiveKey"
        :tabBarStyle="{ textAlign: 'center', borderBottom: 'unset' }"
        @change="handleTabClick">
        <a-tab-pane key="tab1"> -->
      <a-form-item>
        <a-input
          size="large"
          v-decorator="['username',validatorRules.username,{ validator: this.handleUsernameOrEmail }]"
          type="text"
          placeholder="请输入帐户名">
          <a-icon slot="prefix" type="user" :style="{ color: 'rgba(0,0,0,.25)' }"/>
        </a-input>
      </a-form-item>

      <a-form-item>
        <a-input
          v-decorator="['password',validatorRules.password]"
          size="large"
          type="password"
          autocomplete="false"
          placeholder="密码">
          <a-icon slot="prefix" type="lock" :style="{ color: 'rgba(0,0,0,.25)' }"/>
        </a-input>
      </a-form-item>

      <a-row :gutter="0">
        <a-col :span="16">
          <a-form-item>
            <a-input
              v-decorator="['inputCode',validatorRules.inputCode]"
              size="large"
              type="text"
              @change="inputCodeChange"
              placeholder="请输入验证码">
              <a-icon slot="prefix" type="smile" :style="{ color: 'rgba(0,0,0,.25)' }"/>
            </a-input>
          </a-form-item>
        </a-col>
        <a-col :span="8" style="text-align: right">
          <img v-if="requestCodeSuccess" style="margin-top: 2px;" :src="randCodeImage" @click="handleChangeCheckCode"/>
          <img v-else style="margin-top: 2px;" src="../../assets/checkcode.png" @click="handleChangeCheckCode"/>
        </a-col>
      </a-row>

      <!-- </a-tab-pane> -->
      <!-- <a-tab-pane key="tab2" tab="手机号登录">
        <a-form-item>
          <a-input
            v-decorator="['mobile',validatorRules.mobile]"
            size="large"
            type="text"
            placeholder="手机号">
            <a-icon slot="prefix" type="mobile" :style="{ color: 'rgba(0,0,0,.25)' }"/>
          </a-input>
        </a-form-item>

        <a-row :gutter="16">
          <a-col class="gutter-row" :span="16">
            <a-form-item>
              <a-input
                v-decorator="['captcha',validatorRules.captcha]"
                size="large"
                type="text"
                placeholder="请输入验证码">
                <a-icon slot="prefix" type="mail" :style="{ color: 'rgba(0,0,0,.25)' }"/>
              </a-input>
            </a-form-item>
          </a-col>
          <a-col class="gutter-row" :span="8">
            <a-button
              class="getCaptcha"
              tabindex="-1"
              :disabled="state.smsSendBtn"
              @click.stop.prevent="getCaptcha"
              v-text="!state.smsSendBtn && '获取验证码' || (state.time+' s')"></a-button>
          </a-col>
        </a-row>
      </a-tab-pane> -->
      <!-- </a-tabs> -->

      <a-form-item>
        <a-checkbox v-decorator="['rememberMe', {initialValue: true, valuePropName: 'checked'}]" style="color: #f0f2f5">自动登录</a-checkbox>
        <!-- <router-link :to="{ name: 'alteration'}" class="forge-password" style="float: right;">
          忘记密码
        </router-link>
       <router-link :to="{ name: 'register'}" class="forge-password" style="float: right;margin-right: 10px" >
          注册账户
        </router-link> -->
      </a-form-item>

      <a-form-item >
        <a-button
          size="large"
          type="primary"
          htmlType="submit"
          class="login-button"
          :loading="loginBtn"
          @click.stop.prevent="handleSubmit"
          :disabled="loginBtn">确定
        </a-button>
      </a-form-item>
    </a-form>

    <two-step-captcha
      v-if="requiredTwoStepCaptcha"
      :visible="stepCaptchaVisible"
      @success="stepCaptchaSuccess"
      @cancel="stepCaptchaCancel"></two-step-captcha>
    <login-select-tenant ref="loginSelect" @success="loginSelectOk"></login-select-tenant>
    <third-login ref="thirdLogin"></third-login>
  </div>
</template>

<script>
//import md5 from "md5"
import api from '@/api'
import TwoStepCaptcha from '@/components/tools/TwoStepCaptcha'
import { mapActions } from 'vuex'
import { timeFix } from '@/utils/util'
import Vue from 'vue'
import { ACCESS_TOKEN, ENCRYPTED_STRING } from '@/store/mutation-types'
import { putAction, postAction, getAction } from '@/api/manage'
import { initialarryOne } from '@/api/api'
import { encryption, getEncryptedString } from '@/utils/encryption/aesEncrypt'
import store from '@/store/'
import { USER_INFO } from '@/store/mutation-types'
import ThirdLogin from './third/ThirdLogin'
import LoginSelectTenant from './LoginSelectTenant'

export default {
  components: {
    LoginSelectTenant,
    TwoStepCaptcha,
    ThirdLogin
  },
  data() {
    return {
      customActiveKey: 'tab1',
      loginBtn: false,
      // login type: 0 email, 1 username, 2 telephone
      loginType: 0,
      requiredTwoStepCaptcha: false,
      stepCaptchaVisible: false,
      form: this.$form.createForm(this),
      encryptedString: {
        key: '',
        iv: '',
      },
      state: {
        time: 60,
        smsSendBtn: false,
      },
      validatorRules: {
        username: { rules: [{ required: true, message: '请输入用户名!' }, { validator: this.handleUsernameOrEmail }] },
        password: { rules: [{ required: true, message: '请输入密码!', validator: 'click' }] },
        mobile: { rules: [{ validator: this.validateMobile }] },
        captcha: { rule: [{ required: true, message: '请输入验证码!' }] },
        inputCode: { rules: [{ required: true, message: '请输入验证码!' }] }
      },
      verifiedCode: '',
      inputCodeContent: '',
      inputCodeNull: true,
      currentUsername: '',
      currdatetime: '',
      randCodeImage: '',
      requestCodeSuccess: false
    }
  },
  created() {

    this.currdatetime = new Date().getTime()
    Vue.ls.remove(ACCESS_TOKEN)
    this.getRouterData()
    this.handleChangeCheckCode()
    this.Logins();
    // update-begin- --- author:scott ------ date:20190805 ---- for:密码加密逻辑暂时注释掉，有点问题
    //this.getEncrypte();
    // update-end- --- author:scott ------ date:20190805 ---- for:密码加密逻辑暂时注释掉，有点问题
  },
  methods: {
    ...mapActions(['Login', 'Logout', 'PhoneLogin','Loginsso','Loginsso12']),

    // handler
    handleUsernameOrEmail(rule, value, callback) {
      const regex = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/
      if (regex.test(value)) {
        this.loginType = 0
      } else {
        this.loginType = 1
      }
      callback()
    },
    handleTabClick(key) {
      this.customActiveKey = key
      // this.form.resetFields()
    },
    Logins(){
      let that = this
      let loginParams = {}
      var username=this.GetQueryString("username")
      var token1 = this.GetQueryString("token")
      console.log(username,"当前用户")
      if(username!=null){
        loginParams.username =username;
        that.Loginsso(loginParams).then((res) => {
          console.log("res.result",res.result)
          this.$refs.loginSelect.show(res.result)
        }).catch((err) => {
          that.requestFailed(err)
        })
      }
      if(token1 != null){
        loginParams.username =token1;
        // loginParams.token =token1;
        that.Loginsso12(loginParams).then((res) => {
          console.log("res.result",res.result)
          this.$refs.loginSelect.show(res.result)
        }).catch((err) => {
          that.requestFailed(err)
        })
      }
    },
    GetQueryString(name){
      var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
      var r = window.location.search.substr(1).match(reg);
      if(r!=null)return  decodeURIComponent(r[2]); return null;
    },
    handleSubmit() {
      let that = this
      let loginParams = {}
      that.loginBtn = true
      // 使用账户密码登录
      if (that.customActiveKey === 'tab1') {
        that.form.validateFields(['username', 'password', 'inputCode', 'rememberMe'], { force: true }, (err, values) => {
          if (!err) {
            loginParams.username = values.username
            // update-begin- --- author:scott ------ date:20190805 ---- for:密码加密逻辑暂时注释掉，有点问题
            //loginParams.password = md5(values.password)
            //loginParams.password = encryption(values.password,that.encryptedString.key,that.encryptedString.iv)
            loginParams.password = values.password
            loginParams.remember_me = values.rememberMe
            // update-begin- --- author:scott ------ date:20190805 ---- for:密码加密逻辑暂时注释掉，有点问题
            loginParams.captcha = that.inputCodeContent
            loginParams.checkKey = that.currdatetime
            console.log('登录参数', loginParams)
            console.log('全部登录参数', values)
            that.Login(loginParams).then((res) => {
              this.$refs.loginSelect.show(res.result)
            }).catch((err) => {
              that.requestFailed(err);
              that.handleChangeCheckCode();
            })

          } else {
            that.loginBtn = false
          }
        })
        // 使用手机号登录
      } else {
        that.form.validateFields(['mobile', 'captcha', 'rememberMe'], { force: true }, (err, values) => {
          if (!err) {
            loginParams.mobile = values.mobile
            loginParams.captcha = values.captcha
            loginParams.remember_me = values.rememberMe
            that.PhoneLogin(loginParams).then((res) => {
              console.log(res.result)
              this.$refs.loginSelect.show(res.result)
            }).catch((err) => {
              that.requestFailed(err);
            })

          }
        })
      }
    },
    getCaptcha(e) {
      e.preventDefault()
      let that = this
      this.form.validateFields(['mobile'], { force: true }, (err, values) => {
          if (!values.mobile) {
            that.cmsFailed('请输入手机号')
          } else if (!err) {
            this.state.smsSendBtn = true
            let interval = window.setInterval(() => {
              if (that.state.time-- <= 0) {
                that.state.time = 60
                that.state.smsSendBtn = false
                window.clearInterval(interval)
              }
            }, 1000)

            const hide = this.$message.loading('验证码发送中..', 0)
            let smsParams = {}
            smsParams.mobile = values.mobile
            smsParams.smsmode = '0'
            postAction('/sys/sms', smsParams)
              .then(res => {
                if (!res.success) {
                  setTimeout(hide, 0)
                  this.cmsFailed(res.message)
                }
                console.log(res)
                setTimeout(hide, 500)
              })
              .catch(err => {
                setTimeout(hide, 1)
                clearInterval(interval)
                that.state.time = 60
                that.state.smsSendBtn = false
                this.requestFailed(err)
              })
          }
        }
      )
    },
    stepCaptchaSuccess() {
      this.loginSuccess()
    },
    stepCaptchaCancel() {
      this.Logout().then(() => {
        this.loginBtn = false
        this.stepCaptchaVisible = false
      })
    },
    handleChangeCheckCode() {
      this.currdatetime = new Date().getTime()
      getAction(`/sys/randomImage/${this.currdatetime}`).then(res => {
        if (res.success) {
          this.randCodeImage = res.result
          this.requestCodeSuccess = true
        } else {
          this.$message.error(res.message)
          this.requestCodeSuccess = false
        }
      }).catch(() => {
        this.requestCodeSuccess = false
      })
    },
    loginSuccess() {
      this.$router.push({ path: '/dashboard/analysis' }).catch(() => {
        console.log('登录跳转首页出错,这个错误从哪里来的')
      })
      let username = this.GetQueryString("username")
      if(username == null){//单点登录不提示
        this.$notification.success({
          message: '欢迎',
          description: `${timeFix()}，欢迎回来`,
        })
      }
      initialarryOne().then(res => {//初始化当前登录用户的拥有的权限设备
        console.log('获取当前用户下的设备信息')
      })
    },
    cmsFailed(err) {
      this.$notification['error']({
        message: '登录失败',
        description: err,
        duration: 4,
      })
    },
    requestFailed(err) {
      this.$notification['error']({
        message: '登录失败',
        description: ((err.response || {}).data || {}).message || err.message || '请求出现错误，请稍后再试',
        duration: 4,
      })
      this.loginBtn = false
    },
    validateMobile(rule, value, callback) {
      if (!value || new RegExp(/^1([38][0-9]|4[579]|5[0-3,5-9]|6[6]|7[0135678]|9[89])\d{8}$/).test(value)) {
        callback()
      } else {
        callback('您的手机号码格式不正确!')
      }

    },
    validateInputCode(rule, value, callback) {
      if (!value || this.verifiedCode == this.inputCodeContent) {
        callback()
      } else {
        callback('您输入的验证码不正确!')
      }
    },
    generateCode(value) {
      this.verifiedCode = value.toLowerCase()
    },
    inputCodeChange(e) {
      this.inputCodeContent = e.target.value
    },
    loginSelectOk() {
      this.loginSuccess()
    },
    getRouterData() {
      this.$nextTick(() => {
        if (this.$route.params.username) {
          this.form.setFieldsValue({
            'username': this.$route.params.username
          })
        }
      })
    },
    //获取密码加密规则
    getEncrypte() {
      var encryptedString = Vue.ls.get(ENCRYPTED_STRING)
      if (encryptedString == null) {
        getEncryptedString().then((data) => {
          this.encryptedString = data
        })
      } else {
        this.encryptedString = encryptedString
      }
    },
  }
}
</script>

<style lang="less" scoped>

.user-layout-login {
  label {
    font-size: 14px;
  }

  .getCaptcha {
    display: block;
    width: 100%;
    height: 40px;
  }

  .forge-password {
    font-size: 14px;
  }

  button.login-button {
    padding: 0 15px;
    font-size: 16px;
    height: 40px;
    width: 100%;
  }

  .user-login-other {
    text-align: left;
    margin-top: 24px;
    line-height: 22px;

    .item-icon {
      font-size: 24px;
      color: rgba(0, 0, 0, .2);
      margin-left: 16px;
      vertical-align: middle;
      cursor: pointer;
      transition: color .3s;

      &:hover {
        color: #1890ff;
      }
    }

    .register {
      float: right;
    }
  }
}

</style>
<style>
.valid-error .ant-select-selection__placeholder {
  color: #f5222d;
}
</style>