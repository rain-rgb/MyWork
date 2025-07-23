<template>
  <a-modal
    centered
    :title="title"
    :width="innerWidth"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @cancel="handleCancel"
    cancelText="关闭"
    style="top: 20px; height: 100%; overflow: auto; padding-bottom: 53px"
  >
    <template slot="title">
      <div style="width: 100%">
        <span>{{ title }}</span>
        <span
          style="
            display: inline-block;
            width: calc(100% - 51px);
            padding-right: 10px;
            text-align: right;
          "
        >
          <a-button
            @click="toggleScreen"
            icon="appstore"
            style="height: 20px; width: 20px; border: 0px"
          ></a-button>
        </span>
      </div>
    </template>
    <a-row :gutter="16">
      <a-col class="gutter-row" :span="currentOrgCode ? 12 : 24" :wrapperCol="wrapperCol">
        <a-spin :spinning="confirmLoading">
          <a-form :form="form">
            <a-form-item label="计划名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input
                placeholder="请输入计划名称"
                v-decorator.trim="['realname', validatorRules.realname]"
              />
            </a-form-item>

            <template v-if="!model.id">
              <a-form-item label="分部分项" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-select-roadwork
                  v-decorator="['workno', validatorRules.workno]"
                  :trigger-change="true"
                  customReturnField="orgCode"
                  :multi="false"
                ></j-select-roadwork>
              </a-form-item>

              <a-form-item
                label="计划发起人"
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
              >
                <a-input disabled v-decorator.trim="['planName']" />
              </a-form-item>
            </template>

            <a-form-item label="排查人" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-select-multi-user
                v-decorator.trim="['desportMan']"
                :returnId="true"
                :multiple="false"
                :query-config="selectUserQueryConfig"
              />
            </a-form-item>

            <a-form-item label="开始时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-date-picker
                :show-time="{ format: 'YYYY-MM-DD HH:mm:ss' }"
                format="YYYY-MM-DD HH:mm:ss"
                placeholder="Select Time"
                v-decorator.trim="['startTime', validatorRules.startTime]"
                @change="onChange"
              />
            </a-form-item>

            <a-form-item label="结束时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-date-picker
                :show-time="{ format: 'YYYY-MM-DD HH:mm:ss' }"
                format="YYYY-MM-DD HH:mm:ss"
                placeholder="Select Time"
                v-decorator.trim="['endTime', validatorRules.endTime]"
                @change="onChangeEndTime"
              />
            </a-form-item>

            <a-form-item label="工期" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input disabled v-decorator.trim="['tight']" />
            </a-form-item>
            <a-form-item label="风险内容" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-table
                ref="tables"
                size="middle"
                bordered
                rowKey="userId"
                :pagination="ipagination"
                :columns="columns"
                :dataSource="dataSources"
                :loading="loading"
                @change="handleTableChange"
                :customRow="handleClick"
                :rowClassName="setRowClassName"
              >
              </a-table>
            </a-form-item>
          </a-form>
        </a-spin>
      </a-col>
      <a-col class="gutter-row" :span="12" :wrapperCol="wrapperCol">
        <a-card
          class="j-address-list-right-card-box"
          :loading="cardLoading"
          :bordered="false"
          v-if="currentOrgCode"
        >
          <a-table
            ref="table"
            size="middle"
            bordered
            rowKey="userId"
            :pagination="ipagination"
            :columns="columns"
            :row-selection="{
              selectedRowKeys: selectedRowKeys,
              onChange: onSelectChange,
            }"
            :dataSource="dataSource"
            :loading="loading"
            @change="handleTableChange"
            :customRow="handleClick"
            :rowClassName="setRowClassName"
          >
          </a-table>
        </a-card>
      </a-col>
    </a-row>
    <depart-window ref="departWindow" @ok="modalForm"></depart-window>
    <div v-show="!disableSubmit" slot="footer">
      <a-button @click="handleCancel">取消</a-button>
      <a-button @click="handleSubmit" type="primary" :loading="confirmLoading"
        >提交</a-button
      >
    </div>
  </a-modal>
</template>

<script>
import pick from "lodash.pick";
import moment from "moment";
import Vue from "vue";
// 引入搜索部门弹出框的组件
import departWindow from "./DepartWindow";
import JSelectRoadwork from "@/components/jeecgbiz/JSelectRoadwork";
import JSelectMultiUser from "@/components/jeecgbiz/JSelectMultiUser";
import JSelectPosition from "@/components/jeecgbiz/JSelectPosition";
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import { ACCESS_TOKEN, USER_INFO } from "@/store/mutation-types";
import { getAction } from "@/api/manage";
import {
  addUser,
  editUser,
  queryUserRole,
  queryall,
  getDictItemsFromCache,
} from "@/api/api";
import { disabledAuthFilter } from "@/utils/authFilter";
import { duplicateCheck } from "@/api/api";
import JImageUpload from "../../../components/jeecg/JImageUpload";
export default {
  name: "ManagementModal",
  mixins: [JeecgListMixin],
  components: {
    JImageUpload,
    departWindow,
    JSelectPosition,
    JSelectMultiUser,
    JSelectRoadwork,
  },
  data() {
    return {
      dataSources: [],
      innerWidth: 0,
      currentOrgCode: false,
      selectedRowKeys: [],
      loading: false,
      departDisabled: false, //是否是我的部门调用该页面
      roleDisabled: false, //是否是角色维护调用该页面
      startTime: "", //开始时间
      endTime: "", //结束时间
      modalWidth: 800,
      cardLoading: true,
      drawerWidth: 700,
      modaltoggleFlag: true,
      confirmDirty: false,
      columns: [
        {
          title: "编号",
          key: "rowIndex",
          dataIndex: "",
          align: "center",
          customRender: (t, r, i) => parseInt(i) + 1,
        },
        {
          title: "风险描述",
          align: "center",
          dataIndex: "realname",
          width: "17%",
        },
        {
          title: "预测事故类型",
          dataIndex: "workno",
          key: "building",
          width: 70,
          align: "center",
        },
        {
          title: "LEC法风险估测",
          children: [
            {
              title: "L",
              dataIndex: "sgcd",
              key: "lbuilding",
              align: "center",
            },
            {
              title: "E",
              dataIndex: "sgcd",
              key: "ebuilding",
              align: "center",
            },
            {
              title: "C",
              dataIndex: "sgcd",
              key: "cbuilding",
              align: "center",
            },
            {
              title: "D",
              dataIndex: "sgcd",
              key: "dbuilding",
              align: "center",
            },
            {
              title: "风险等级",
              dataIndex: "telephone",
              key: "number",
              width: 70,
              align: "center",
            },
          ],
        },
        {
          title: "风险控制措施",
          align: "center",
          dataIndex: "departName",
          width: 120,
        },
        {
          title: "残余风险判断",
          align: "center",
          width: 120,
          dataIndex: "post",
        },
      ],
      selectedDepartKeys: [], //保存用户选择部门id
      checkedDepartKeys: [],
      checkedDepartNames: [], // 保存部门的名称 =>title
      checkedDepartNameString: "", // 保存部门的名称 =>title
      resultDepartOptions: [],
      userId: "", //保存用户id
      disableSubmit: false,
      userDepartModel: { userId: "", departIdList: [] }, // 保存SysUserDepart的用户部门中间表数据需要的对象
      dateFormat: "YYYY-MM-DD",
      validatorRules: {
        workno: {
          rules: [
            {
              required: true,
              message: "请选择部位!",
            },
            {
              validator: this.checkBu,
            },
          ],
        },
        startTime: {
          rules: [
            {
              required: true,
              message: "请输入结束时间!",
            },
          ],
        },
        endTime: {
          rules: [
            {
              required: true,
              message: "请输入结束时间!",
            },
          ],
        },
        realname: { rules: [{ required: true, message: "请输入计划名称!" }] },
      },
      selectUserQueryConfig: [{ key: "phone", label: "电话" }],
      departIdShow: false,
      departIds: [], //负责部门id
      title: "操作",
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
      picUrl: "",
      url: {
        delete: "/sys/sysDepart/delete",
        edit: "/sys/sysDepart/edit",
        deleteBatch: "/sys/sysDepart/deleteBatch",
        exportXlsUrl: "sys/sysDepart/exportXls",
        importExcelUrl: "sys/sysDepart/importExcel",

        list: "/sys/user/queryByOrgCodeForAddressList",
        listByPosition: "/sys/position/list",
      },
      identity: "1",
      fileList: [],
      tenantList: [],
      currentTenant: [],
    };
  },
  watch: {
    value: {
      immediate: true,
      handler(orgCode) {
        this.dataSource = [];
        this.loadData(1, orgCode);
      },
    },
  },
  created() {
    const token = Vue.ls.get(ACCESS_TOKEN);
    this.headers = { "X-Access-Token": token };
    this.initTenantList();
    this.innerWidth = window.screen.width - 50;
  },
  methods: {
    // 详情
    handleDetail(record) {
      this.$refs.modalForm.edit(record);
      this.$refs.modalForm.title = "详情";
      this.$refs.modalForm.disableSubmit = false;
    },
    checkBu(rule, value, callback) {
      console.log(value);
      this.currentOrgCode = true;
      callback();
    },
    // 表格数据获取
    handleTableChange(pagination, filters, sorter) {
      if (Object.keys(sorter).length > 0) {
        this.isorter.column = sorter.field;
        this.isorter.order = "ascend" === sorter.order ? "asc" : "desc";
      }
      console.log(this.value);
      this.ipagination = pagination;
      this.loadData(null, this.value);
    },
    onSelectChange(selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys;
      let arr = new Array();
      this.dataSource.forEach((e) => {
        if (this.selectedRowKeys.includes(e.userId)) {
          arr.push(e);
        }
      });
      this.dataSources = arr;
    },
    // 初始化数据
    loadData(pageNum, orgCode) {
      let result = getDictItemsFromCache("plan_status");
      console.log(result);
      this.selectedRowKeys = [
        "f0019fdebedb443c98dcb17d88222c38",
        "da934fe8ed1f4d33add2c9e45051bc37",
      ];
      this.loading = true;
      if (pageNum === 1) {
        this.ipagination.current = 1;
      }
      // update-begin- --- author:wangshuai ------ date:20200102 ---- for:传过来的部门编码为空全查
      if (!orgCode) {
        getAction(this.url.list, {
          ...this.getQueryParams(),
        })
          .then((res) => {
            if (res.success) {
              console.log(res.result.records);
              this.dataSource = res.result.records;
              this.ipagination.total = res.result.total;
            }
          })
          .finally(() => {
            this.loading = false;
            this.cardLoading = false;
            this.onSelectChange(this.selectedRowKeys);
          });
        // update-end- --- author:wangshuai ------ date:20200102 ---- for:传过来的部门编码为空全查
      } else {
        //加载数据 若传入参数1则加载第一页的内容
        getAction(this.url.list, {
          orgCode,
          ...this.getQueryParams(),
        })
          .then((res) => {
            if (res.success) {
              this.dataSource = res.result.records;
              this.ipagination.total = res.result.total;
            }
          })
          .finally(() => {
            this.loading = false;
            this.cardLoading = false;
          });
      }
    },
    // 初始化用户
    initUser() {
      const user = Vue.ls.get(USER_INFO);
      this.visible = true;
      try {
        this.$nextTick(() => {
          this.form.setFieldsValue({ planName: user.realname }); // loadsh的pick方法
        });
      } catch (error) {
        console.log("赋值错误从哪里来的？");
      }
    },
    initUserValue() {
      const user = Vue.ls.get(USER_INFO);
      this.visible = true;
      try {
        this.$nextTick(() => {
          this.form.setFieldsValue({ planName: user.id }); // loadsh的pick方法
        });
      } catch (error) {
        console.log("赋值错误从哪里来的？");
      }
    },
    onChange(formData, value) {
      this.startTime = value;
      if (this.endTime !== "") {
        this.difference(this.startTime, this.endTime);
      }
      console.log(value);
    },
    onChangeEndTime(formData, value) {
      this.endTime = value;
      if (this.startTime !== "") {
        this.difference(this.startTime, this.endTime);
      }
    },
    difference(beginTime, endTime) {
      var dateBegin = new Date(beginTime);
      var dateEnd = new Date(endTime);
      var dateDiff = dateEnd.getTime() - dateBegin.getTime(); //时间差的毫秒数
      var dayDiff = Math.floor(dateDiff / (24 * 3600 * 1000)); //计算出相差天数
      var leave1 = dateDiff % (24 * 3600 * 1000); //计算天数后剩余的毫秒数
      var hours = Math.floor(leave1 / (3600 * 1000)); //计算出小时数
      //计算相差分钟数
      var leave2 = leave1 % (3600 * 1000); //计算小时数后剩余的毫秒数
      var minutes = Math.floor(leave2 / (60 * 1000)); //计算相差分钟数
      //计算相差秒数
      var leave3 = leave2 % (60 * 1000); //计算分钟数后剩余的毫秒数
      var seconds = Math.round(leave3 / 1000);
      this.form.setFieldsValue({
        tight: dayDiff + "天" + hours + "小时" + minutes + "分钟",
      });
    },
    isDisabledAuth(code) {
      return disabledAuthFilter(code);
    },
    initTenantList() {
      getAction(this.url.queryTenantList).then((res) => {
        if (res.success) {
          this.tenantList = res.result;
        }
      });
    },
    //窗口最大化切换
    toggleScreen() {
      if (this.modaltoggleFlag) {
        this.modalWidth = window.innerWidth;
      } else {
        this.modalWidth = 800;
      }
      this.modaltoggleFlag = !this.modaltoggleFlag;
    },
    initialRoleList() {
      queryall().then((res) => {
        if (res.success) {
          this.roleList = res.result;
        } else {
          console.log(res.message);
        }
      });
    },
    loadUserRoles(userid) {
      queryUserRole({ userid: userid }).then((res) => {
        if (res.success) {
          this.selectedRole = res.result;
        } else {
          console.log(res.message);
        }
      });
    },
    refresh() {
      this.selectedDepartKeys = [];
      this.checkedDepartKeys = [];
      this.checkedDepartNames = [];
      this.checkedDepartNameString = "";
      this.userId = "";
      this.resultDepartOptions = [];
      this.departId = [];
      this.departIdShow = false;
      this.currentOrgCode = false;
      this.currentTenant = [];
    },
    add() {
      this.picUrl = "";
      this.refresh();
      this.initUser();
      this.edit({ activitiSync: "1" });
    },
    edit(record) {
      this.resetScreenSize(); // 调用此方法,根据屏幕宽度自适应调整抽屉的宽度
      let that = this;
      that.initialRoleList();
      that.checkedDepartNameString = "";
      that.form.resetFields();
      if (record.hasOwnProperty("id")) {
        that.loadUserRoles(record.id);
        setTimeout(() => {
          this.fileList = record.avatar;
        }, 5);
      }
      that.userId = record.id;
      that.visible = true;
      that.model = Object.assign({}, record);
      that.$nextTick(() => {
        that.form.setFieldsValue(
          pick(
            this.model,
            "realname",
            "planName",
            "desportMan",
            "workno",
            "startTime",
            "endTime"
          )
        );
      });
      //身份为上级显示负责部门，否则不显示
      if (this.model.userIdentity == "2") {
        this.identity = "2";
        this.departIdShow = true;
      } else {
        this.identity = "1";
        this.departIdShow = false;
      }
      // 调用查询用户对应的部门信息的方法
      that.checkedDepartKeys = [];
      that.loadCheckedDeparts();

      //update-begin-author:taoyan date:2020710 for:多租户配置
      if (!record.relTenantIds || record.relTenantIds.length == 0) {
        this.currentTenant = [];
      } else {
        this.currentTenant = record.relTenantIds.split(",").map(Number);
      }
      //update-end-author:taoyan date:2020710 for:多租户配置
    },
    //
    loadCheckedDeparts() {
      let that = this;
      if (!that.userId) {
        return;
      }
      getAction(that.url.userWithDepart, { userId: that.userId }).then((res) => {
        that.checkedDepartNames = [];
        if (res.success) {
          var depart = [];
          var departId = [];
          for (let i = 0; i < res.result.length; i++) {
            that.checkedDepartNames.push(res.result[i].title);
            this.checkedDepartNameString = this.checkedDepartNames.join(",");
            that.checkedDepartKeys.push(res.result[i].key);
            //新增负责部门选择下拉框
            depart.push({
              key: res.result[i].key,
              title: res.result[i].title,
            });
            departId.push(res.result[i].key);
          }
          that.resultDepartOptions = depart;
          //判断部门id是否存在，不存在择直接默认当前所在部门
          if (this.model.departIds) {
            this.departIds = this.model.departIds.split(",");
          } else {
            this.departIds = departId;
          }
          that.userDepartModel.departIdList = that.checkedDepartKeys;
        } else {
          console.log(res.message);
        }
      });
    },
    close() {
      this.$emit("close");
      this.visible = false;
      this.disableSubmit = false;
      this.selectedRole = [];
      this.userDepartModel = { userId: "", departIdList: [] };
      this.checkedDepartNames = [];
      this.checkedDepartNameString = "";
      this.checkedDepartKeys = [];
      this.selectedDepartKeys = [];
      this.resultDepartOptions = [];
      this.departIds = [];
      this.departIdShow = false;
      this.identity = "1";
      this.fileList = [];
    },
    moment,
    handleSubmit() {
      const that = this;
      // 触发表单验证
      this.form.validateFields((err, values) => {
        if (!err) {
          that.confirmLoading = true;
          values.userRow = this.selectedRowKeys;
          let formData = Object.assign(this.model, values);
          if (that.fileList != "") {
            formData.avatar = that.fileList;
          } else {
            formData.avatar = null;
          }
          console.log(values);
          //update-begin-author:taoyan date:2020710 for:多租户配置
          formData.relTenantIds =
            this.currentTenant.length > 0 ? this.currentTenant.join(",") : "";
          //update-end-author:taoyan date:2020710 for:多租户配置
          formData.selectedroles =
            this.selectedRole.length > 0 ? this.selectedRole.join(",") : "";
          formData.selecteddeparts =
            this.userDepartModel.departIdList.length > 0
              ? this.userDepartModel.departIdList.join(",")
              : "";
          formData.userIdentity = this.identity;
          //如果是上级择传入departIds,否则为空
          if (this.identity === "2") {
            formData.departIds = this.departIds.join(",");
          } else {
            formData.departIds = "";
          }
          // that.addDepartsToUser(that,formData); // 调用根据当前用户添加部门信息的方法
          let obj;
          if (!this.model.id) {
            formData.id = this.userId;
            obj = addUser(formData);
          } else {
            obj = editUser(formData);
          }
          obj
            .then((res) => {
              if (res.success) {
                that.$message.success(res.message);
                that.$emit("ok");
              } else {
                that.$message.warning(res.message);
              }
            })
            .finally(() => {
              that.confirmLoading = false;
              that.checkedDepartNames = [];
              that.userDepartModel.departIdList = { userId: "", departIdList: [] };
              that.close();
            });
        }
      });
    },
    handleCancel() {
      this.close();
    },
    validatePhone(rule, value, callback) {
      if (!value) {
        callback();
      } else {
        //update-begin--Author:kangxiaolin  Date:20190826 for：[05] 手机号不支持199号码段--------------------
        if (new RegExp(/^1[3|4|5|7|8|9][0-9]\d{8}$/).test(value)) {
          //update-end--Author:kangxiaolin  Date:20190826 for：[05] 手机号不支持199号码段--------------------

          var params = {
            tableName: "sys_user",
            fieldName: "phone",
            fieldVal: value,
            dataId: this.userId,
          };
          duplicateCheck(params).then((res) => {
            if (res.success) {
              callback();
            } else {
              callback("手机号已存在!");
            }
          });
        } else {
          callback("请输入正确格式的手机号码!");
        }
      }
    },
    // 搜索用户对应的部门API
    onSearch() {
      this.$refs.departWindow.add(this.checkedDepartKeys, this.userId);
    },

    // 获取用户对应部门弹出框提交给返回的数据
    modalForm(formData) {
      this.checkedDepartNames = [];
      this.selectedDepartKeys = [];
      this.checkedDepartNameString = "";
      this.userId = formData.userId;
      this.userDepartModel.userId = formData.userId;
      this.departIds = [];
      this.resultDepartOptions = [];
      var depart = [];
      for (let i = 0; i < formData.departIdList.length; i++) {
        this.selectedDepartKeys.push(formData.departIdList[i].key);
        this.checkedDepartNames.push(formData.departIdList[i].title);
        this.checkedDepartNameString = this.checkedDepartNames.join(",");
        //新增部门选择，如果上面部门选择后不为空直接付给负责部门
        depart.push({
          key: formData.departIdList[i].key,
          title: formData.departIdList[i].title,
        });
        this.departIds.push(formData.departIdList[i].key);
      }
      this.resultDepartOptions = depart;
      this.userDepartModel.departIdList = this.selectedDepartKeys;
      this.checkedDepartKeys = this.selectedDepartKeys; //更新当前的选择keys
    },
    // 根据屏幕变化,设置抽屉尺寸
    resetScreenSize() {
      let screenWidth = document.body.clientWidth;
      if (screenWidth < 500) {
        this.drawerWidth = screenWidth;
      } else {
        this.drawerWidth = 700;
      }
    },
    identityChange(e) {
      if (e.target.value === "1") {
        this.departIdShow = false;
      } else {
        this.departIdShow = true;
      }
    },
  },
};
</script>

<style scoped>
.gutter-example >>> .ant-row > div {
  background: transparent;
  border: 0;
}
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
</style>
