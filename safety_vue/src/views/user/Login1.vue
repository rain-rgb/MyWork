<template>
  <div id="app">
    <div id="loader-wrapper">
      <div id="loader"></div>
      <div class="loader-section section-left"></div>
      <div class="loader-section section-right"></div>
      <!--&lt;!&ndash;    <div class="load_title">高速公路建设安全数字化管理系统&ndash;&gt;浙江省公路水运工程项目级-->
<!--      <div class="load_title">努力加载中~请稍等...-->

<!--      </div>-->
    </div>
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
  mounted() {

  },
  created() {

    this.handleLogout()
    localStorage.clear()
    sessionStorage.clear()
    this.currdatetime = new Date().getTime()
    this.getRouterData()
    this.handleChangeCheckCode()
    this.Logins();

    // update-begin- --- author:scott ------ date:20190805 ---- for:密码加密逻辑暂时注释掉，有点问题
    //this.getEncrypte();
    // update-end- --- author:scott ------ date:20190805 ---- for:密码加密逻辑暂时注释掉，有点问题
  },
  methods: {
    ...mapActions(['Login', 'Logout', 'PhoneLogin','Loginsso','Loginsso12']),

    handleLogout() {
      debugger
      const that = this
      return that.Logout({}).then(() => {
        Vue.ls.remove('SYS_DEPART_ORGCODE');
        Vue.ls.set('SYS_DEPART_ORGCODE', '');
        Vue.ls.remove('SAFETY_PARTIAL_BASIC');
        Vue.ls.set('SAFETY_PARTIAL_BASIC','');
      }).catch(err => {
        that.$message.error({
          title: '错误',
          description: err.message
        })
      })


    },

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
      var token1 = this.GetQueryString("token")
      if(token1 != null){
        loginParams.username =token1;
        // loginParams.token =token1;
        that.Loginsso12(loginParams).then((res) => {
          console.log("res.result",res.result)
          this.loginSuccess()
          // this.$refs.loginSelect.show(res.result)

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
      let username = this.GetQueryString("username")
      let token = this.GetQueryString("token")
      this.$router.push({ path: '/dashboard/analysis' }).catch(() => {
        console.log('登录跳转首页出错,这个错误从哪里来的')
      })
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
#app {
  height: 100%;
  margin: 0px;
  padding: 0px;
}
.amap-logo{
  display: none;
  opacity:0 !important;
}
.amap-copyright {
  opacity:0;
}
.chromeframe {
  margin: 0.2em 0;
  background: #ccc;
  color: #000;
  padding: 0.2em 0;
}
#loader-wrapper {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 999999;
}
#loader {
  display: block;
  position: relative;
  left: 50%;
  top: 50%;
  width: 120px;
  height: 120px;
  margin: -75px 0 0 -75px;
  border-radius: 50%;
  border: 3px solid transparent;
  /* COLOR 1 */
  border-top-color: #FFF;
  -webkit-animation: spin 2s linear infinite;
  /* Chrome, Opera 15+, Safari 5+ */
  -ms-animation: spin 2s linear infinite;
  /* Chrome, Opera 15+, Safari 5+ */
  -moz-animation: spin 2s linear infinite;
  /* Chrome, Opera 15+, Safari 5+ */
  -o-animation: spin 2s linear infinite;
  /* Chrome, Opera 15+, Safari 5+ */
  animation: spin 2s linear infinite;
  /* Chrome, Firefox 16+, IE 10+, Opera */
  z-index: 1001;
}
#loader:before {
  content: "";
  position: absolute;
  top: 5px;
  left: 5px;
  right: 5px;
  bottom: 5px;
  border-radius: 50%;
  border: 3px solid transparent;
  /* COLOR 2 */
  border-top-color: #FFF;
  -webkit-animation: spin 3s linear infinite;
  /* Chrome, Opera 15+, Safari 5+ */
  -moz-animation: spin 3s linear infinite;
  /* Chrome, Opera 15+, Safari 5+ */
  -o-animation: spin 3s linear infinite;
  /* Chrome, Opera 15+, Safari 5+ */
  -ms-animation: spin 3s linear infinite;
  /* Chrome, Opera 15+, Safari 5+ */
  animation: spin 3s linear infinite;
  /* Chrome, Firefox 16+, IE 10+, Opera */
}
#loader:after {
  content: "";
  position: absolute;
  top: 15px;
  left: 15px;
  right: 15px;
  bottom: 15px;
  border-radius: 50%;
  border: 3px solid transparent;
  border-top-color: #FFF;
  /* COLOR 3 */
  -moz-animation: spin 1.5s linear infinite;
  /* Chrome, Opera 15+, Safari 5+ */
  -o-animation: spin 1.5s linear infinite;
  /* Chrome, Opera 15+, Safari 5+ */
  -ms-animation: spin 1.5s linear infinite;
  /* Chrome, Opera 15+, Safari 5+ */
  -webkit-animation: spin 1.5s linear infinite;
  /* Chrome, Opera 15+, Safari 5+ */
  animation: spin 1.5s linear infinite;
  /* Chrome, Firefox 16+, IE 10+, Opera */
}
@-webkit-keyframes spin {
  0% {
    -webkit-transform: rotate(0deg);
    /* Chrome, Opera 15+, Safari 3.1+ */
    -ms-transform: rotate(0deg);
    /* IE 9 */
    transform: rotate(0deg);
    /* Firefox 16+, IE 10+, Opera */
  }
  100% {
    -webkit-transform: rotate(360deg);
    /* Chrome, Opera 15+, Safari 3.1+ */
    -ms-transform: rotate(360deg);
    /* IE 9 */
    transform: rotate(360deg);
    /* Firefox 16+, IE 10+, Opera */
  }
}
@keyframes spin {
  0% {
    -webkit-transform: rotate(0deg);
    /* Chrome, Opera 15+, Safari 3.1+ */
    -ms-transform: rotate(0deg);
    /* IE 9 */
    transform: rotate(0deg);
    /* Firefox 16+, IE 10+, Opera */
  }
  100% {
    -webkit-transform: rotate(360deg);
    /* Chrome, Opera 15+, Safari 3.1+ */
    -ms-transform: rotate(360deg);
    /* IE 9 */
    transform: rotate(360deg);
    /* Firefox 16+, IE 10+, Opera */
  }
}
#loader-wrapper .loader-section {
  position: fixed;
  top: 0;
  width: 51%;
  height: 100%;
  background: #071F3E;
  /* Old browsers */
  z-index: 1000;
  -webkit-transform: translateX(0);
  /* Chrome, Opera 15+, Safari 3.1+ */
  -ms-transform: translateX(0);
  /* IE 9 */
  transform: translateX(0);
  /* Firefox 16+, IE 10+, Opera */
}
#loader-wrapper .loader-section.section-left {
  left: 0;
}
#loader-wrapper .loader-section.section-right {
  right: 0;
}
/* Loaded */
.loaded #loader-wrapper .loader-section.section-left {
  -webkit-transform: translateX(-100%);
  /* Chrome, Opera 15+, Safari 3.1+ */
  -ms-transform: translateX(-100%);
  /* IE 9 */
  transform: translateX(-100%);
  /* Firefox 16+, IE 10+, Opera */
  -webkit-transition: all 0.7s 0.3s cubic-bezier(0.645, 0.045, 0.355, 1.000);
  transition: all 0.7s 0.3s cubic-bezier(0.645, 0.045, 0.355, 1.000);
}
.loaded #loader-wrapper .loader-section.section-right {
  -webkit-transform: translateX(100%);
  /* Chrome, Opera 15+, Safari 3.1+ */
  -ms-transform: translateX(100%);
  /* IE 9 */
  transform: translateX(100%);
  /* Firefox 16+, IE 10+, Opera */
  -webkit-transition: all 0.7s 0.3s cubic-bezier(0.645, 0.045, 0.355, 1.000);
  transition: all 0.7s 0.3s cubic-bezier(0.645, 0.045, 0.355, 1.000);
}
.loaded #loader {
  opacity: 0;
  -webkit-transition: all 0.3s ease-out;
  transition: all 0.3s ease-out;
}
.loaded #loader-wrapper {
  visibility: hidden;
  -webkit-transform: translateY(-100%);
  /* Chrome, Opera 15+, Safari 3.1+ */
  -ms-transform: translateY(-100%);
  /* IE 9 */
  transform: translateY(-100%);
  /* Firefox 16+, IE 10+, Opera */
  -webkit-transition: all 0.3s 1s ease-out;
  transition: all 0.3s 1s ease-out;
}
/* JavaScript Turned Off */
.no-js #loader-wrapper {
  display: none;
}
.no-js h1 {
  color: #222222;
}
#loader-wrapper .load_title {
  font-family: 'Open Sans';
  color: #FFF;
  font-size: 14px;
  width: 100%;
  text-align: center;
  z-index: 9999999999999;
  position: absolute;
  top: 60%;
  opacity: 1;
  line-height: 30px;
}
#loader-wrapper .load_title span {
  font-weight: normal;
  font-style: italic;
  font-size: 14px;
  color: #FFF;
  opacity: 0.5;
}
/* 滚动条优化 start */
::-webkit-scrollbar{
  width:8px;
  height:8px;
}
::-webkit-scrollbar-track{
  background: #f6f6f6;
  border-radius:2px;
}
::-webkit-scrollbar-thumb{
  background: #cdcdcd;
  border-radius:2px;
}
::-webkit-scrollbar-thumb:hover{
  background: #747474;
}
::-webkit-scrollbar-corner {
  background: #f6f6f6;
}
</style>