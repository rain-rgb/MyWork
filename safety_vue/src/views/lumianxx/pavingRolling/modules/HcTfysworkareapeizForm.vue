<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled" v-if="!formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
<!--          <a-col :span="24">-->
<!--            <a-form-model-item-->
<!--              label="项目"-->
<!--              :labelCol="labelCol"-->
<!--              :wrapperCol="wrapperCol"-->
<!--              prop="projectid"-->
<!--            >-->
<!--              <j-search-select-tag-->
<!--                style="width: 200px"-->
<!--                placeholder="请选择项目名称"-->
<!--                v-model="model.projectid"-->
<!--                :dictOptions="dictOptionPro"-->
<!--                @change="handleChange"-->
<!--              >-->
<!--              </j-search-select-tag>-->
<!--              &lt;!&ndash; <a-input v-model="model.projectid" placeholder="请输入项目"  ></a-input> &ndash;&gt;-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
          <a-col :span="24">
            <a-form-model-item
              label="标段"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              prop="sectionid"
            >
              <j-search-select-tag
                style="width: 200px"
                placeholder="请选择标段名称"
                v-model="model.sectionid"
                :dictOptions="dictOptionBD"
                @change="handleChangeBD"
              >
              </j-search-select-tag>
              <!-- <a-input v-model="model.sectionid" placeholder="请输入标段"  ></a-input> -->
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item
              label="选择分部分项"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              prop="mileageid"
            >
              <JSelectDqProjName v-model="model.mileageid" ::multi="false" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item
              label="开始桩号"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              prop="startstation"
            >
              <a-input
                v-model.number="model.startstation"
                placeholder="请输入开始桩号"
              ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item
              label="结束桩号"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              prop="endstation"
            >
              <a-input
                v-model.number="model.endstation"
                placeholder="请输入结束桩号"
              ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item
              label="碾压层数"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              prop="layername"
            >
              <a-input v-model="model.layername" placeholder="请输入碾压层数"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item
              label="设计厚度(cm)"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              prop="avgheights"
            >
              <a-input v-model="model.avgheights" placeholder="请输入设计厚度" disabled="true"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item
              label="设计宽度"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              prop="thickavgs"
            >
              <a-input v-model="model.thickavgs" placeholder="请输入设计宽度"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
<!--            <a-form-model-item-->
<!--              label="施工人员"-->
<!--              :labelCol="labelCol"-->
<!--              :wrapperCol="wrapperCol"-->
<!--              prop="construction"-->
<!--            >-->
<!--              <a-input v-model="model.construction" placeholder="请输入施工人员"  ></a-input>-->
<!--            </a-form-model-item>-->
            <a-form-item label="施工人员号码组" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-model='model.construction' :trigger-change="true"
                                 dictCode="bhz_phone,names,uid,phonesname='33'" dictTable="bhz_phone"
                                 placeholder="请选择施工人员号码组"/>
            </a-form-item>
          </a-col>
          <a-col :span="24">
<!--            <a-form-model-item-->
<!--              label="监理人员"-->
<!--              :labelCol="labelCol"-->
<!--              :wrapperCol="wrapperCol"-->
<!--              prop="supervisory"-->
<!--            >-->
<!--              <a-input v-model="model.supervisory" placeholder="请输入监理人员"  ></a-input>-->
<!--            </a-form-model-item>-->
            <a-form-item label="监理人员号码组" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-model='model.supervisory' :trigger-change="true"
                                 dictCode="bhz_phone,names,uid,phonesname='34'" dictTable="bhz_phone"
                                 placeholder="请选择监理人员号码组"/>
            </a-form-item>
          </a-col>

          <a-col :span="24">
            <a-form-model-item
              label="施工日期"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              prop="starttimes"
            >
              <!-- <a-input v-model="model.starttimes" placeholder="请输入施工日期"  ></a-input> -->
              <j-date
                placeholder="请输入施工日期"
                dateFormat="YYYY-MM-DD"
                v-model="model.starttimes"
                :showTime="false"
              />
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
      <!-- <TableTH v-if="formDisabled"></TableTH> -->
    </j-form-container>
    <div v-else>
      <div class="buttonList">
        <a-button @click="bindPrint" class="BtnStyle">打印本页</a-button>
        <!-- <a-button @click="bindPrintMulti" class="BtnStyle" :loading="iconLoading">打印
          <span>{{PrintNum}}</span>
        </a-button> -->
        <a-button @click="showModal" class="BtnStyle" v-if="dataTarAll.length == 0">
          选择打印页码
        </a-button>        
        <a-button type="primary" @click="bindPrint" class="BtnStyle" v-if="dataTarAll.length != 0" :loading="iconLoading">
          打印{{PrintNum}}页~{{PrintNum+9}}页
        </a-button>
        <a-modal
          title="请输入打印页码"
          :visible="visiblePrint"
          :confirm-loading="PrintLoading"
          @ok="handleOk"
          @cancel="handleCancel"
        >
          <a-input class="inputStyle" type="number" v-model.number="PrintNum"/>页~{{PrintNum+9}}页

        </a-modal>
      </div>
      <TableTH ref="table" id="paperA4" :dataTar="dataTar" :model="model" v-if="dataTarAll.length == 0"></TableTH>
      <div id="paperA4" style="margin:0 0px 0px 18px" v-else>
        <TableTH  v-for="(item,i) in dataTarAll" :key="item.id" style="margin:0 0 188px 0" ref="tableAll" :dataTar="item.records" :model="model" :index="i+1"></TableTH>
      </div>
      <div id="not-print" class="pagination" v-if="dataTarAll.length == 0">
        <a-pagination
          v-model="current"
          :total="total"
          show-less-items
          @change="change"
        />
      </div>
    </div>
  </a-spin>
</template>

<script>
import { httpAction, getAction } from "@/api/manage";
import { validateDuplicateValue } from "@/utils/util";
import JSelectDqProjName from "@comp/jeecgbiz/JselectDqProjName";
import Vue from "vue";
import TableTH from "../form/tableTH";
import html2canvas from 'html2canvas'
import printJS  from 'print-js'
import { setTimeout } from 'timers';

export default {
  name: "HcTfysworkareapeizForm",
  components: {
    JSelectDqProjName,
    TableTH,
  },
  props: {
    //表单禁用
    disabled: {
      type: Boolean,
      default: false,
      required: false,
    },
  },
  watch:{
    // pages:{
    //   handler(){
    //     console.log(this.dataTarAll.length,this.pages,'watch--------');
    //     if(this.dataTarAll.length == this.pages){
    //       setTimeout(() => {
    //         this.bindPrint();
    //       }, 1100);
    //     }
    //   },
    //   deep: true, // 开启深度监听
    // }
  },
  data() {
    return {
      model: {
        projectid: "",
        sectionid: "",
        mileageid: "",
        startstation: "",
        endstation: "",
        starttimes: "",
        avgheights:"30"

      },
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
        projectid: [{ required: true, message: "项目名称必填", trigger: "change" }],
        sectionid: [{ required: true, message: "标段名称必填", trigger: "change" }],
        mileageid: [{ required: true, message: "分部分项必填", trigger: "change" }],
        startstation: [
          {
            type: "number",
            required: true,
            message: "开始桩号必填且为数字类型",
            trigger: "change",
          },
        ],
        endstation: [
          {
            type: "number",
            required: true,
            message: "结束桩号必填且为数字类型",
            trigger: "change",
          },
        ],
      },
      url: {
        add: "/sys/sysDepartproject/tfadd",
        edit: "/hctfysworkareapeiz/hcTfysworkareapeiz/edit",
        queryById: "/hctfysworkareapeiz/hcTfysworkareapeiz/queryById",
        hcProject: "/hc_project/hcProject/list",
        hcSection: "/hc_section/hcSection/list",
        hcProject1: "/hc_section/hcSection/listls",
        listxq: "/hc_tfstationdetail/hcTfstationdetail/listxq",//表格数据
      },
      dictOptionPro: [],
      dictOptionBD: [],
      current: 1,
      total: 0,
      dataTar: [],
      // dataTarAll: null,
      dataTarAll: [],
      pages:0,
      iconLoading:false,
      PrintNum:1,
      // PrintNumTar:"",
      visiblePrint:false,
      PrintLoading:false,
    };
  },
  computed: {
    formDisabled() {
      return this.disabled;
    },
  },
  created() {
    //备份model原始值
    this.modelDefault = JSON.parse(JSON.stringify(this.model));
    this.getProject();
  },
  methods: {
    getProject() {
      // let username = this.$route.query.username;
      let orgCode = Vue.ls.get("SYS_DEPART_ORGCODE");
      let param = { orgCode };
      getAction(this.url.hcProject, param).then((res) => {
        console.log(res, "getProject-------------------------");
        this.dictOptionPro = [];
        this.orgcode = "";
        let result = res.result.records;
        if (result == null) {
          this.getProject1();
        } else {
          this.projectList = res.result.records;
          this.orgcode = result[0].orgcode;
          this.projectId = result[0].pjid;
          this.model.projectid = result[0].pjid;
          result.forEach((res) => {
            this.dictOptionPro.push({ text: res.pjname, value: res.pjid });
          });
          console.log(this.dictOptionPro, "this.dictOptionPro------------");
          this.getDataBD(this.orgcode);
        }
      });
    },
    getProject1() {
      // let username = this.$route.query.username;
      let param = {};
      getAction(this.url.hcProject1, param).then((res) => {
        console.log(res, "getProject-------------------------");
        this.dictOptionPro = [];
        this.orgcode = "";
        let result = res.result.records;
        this.projectList = res.result.records;
        this.orgcode = result[0].orgcode;
        this.projectId = result[0].pjid;
        this.model.projectid = result[0].pjid;
        result.forEach((res) => {
          this.dictOptionPro.push({ text: res.pjname, value: res.pjid });
        });
        this.getDataBD(this.orgcode);
      });
    },
    getDataBD() {
      let orgCode = Vue.ls.get("SYS_DEPART_ORGCODE");
      let param = {
        orgcode: orgCode,
      };
      getAction(this.url.hcSection, param).then((res) => {
        console.log(res, "hcSection------------");
        this.dictOptionBD = [];
        // this.model.sectionid = "";
        let result = res.result.records;
        this.sectionList = res.result.records;
        // this.sectionId = result[0].sectionid;
        // this.model.biaoduan = result[0].sectionname;
        // this.model.sectionid = result[0].sectionid;
        console.log(this.dictOptionBD, this.model.sectionid, "this.dictOptionBD");
        result.forEach((res) => {
          this.dictOptionBD.push({ text: res.sectionname, value: res.sectionid });
        });
        // if (this.firstGetData) {
        //   this.firstGetData = false;
        //   this.getData();
        // }
        console.log(
          this.projectId,
          this.sectionId,
          result,
          res,
          "this.projectId------------getDataBD"
        );
      });
    },
    handleChange(selectedValue) {
      let arr = this.projectList.filter((e) => {
        return e.pjid == selectedValue;
      });
      // this.projectId = arr[0].pjid;
      let orgcode;
      if (selectedValue) {
        orgcode = arr[0].orgcode;
      } else {
        orgcode = "";
      }
      this.model.projectid = selectedValue;
      console.log("selectedValue", this.model, arr, arr.pjid);
      // this.getDataBD(orgcode);
      // console.log(this.projectId, this.sectionId, "this.projectId------------sectionId");
    },
    handleChangeBD(selectedValue) {
      // let arr = this.sectionList.filter((e) => {
      //   return e.sectionid == selectedValue;
      // });
      // this.sectionId = arr[0].sectionid;
      this.model.sectionid = selectedValue;
      // console.log("selectedValue", selectedValue);
      // this.sectionId = selectedValue;
      console.log(
        this.projectId,
        this.sectionId,
        "this.projectId------------sectionId",
        this.model
      );
    },
    add() {
      this.edit(this.modelDefault);
    },
    edit(record) {
      this.model = Object.assign({}, record);
      this.visible = true;
      // this.$refs.table.model = JSON.parse(JSON.stringify(this.model));
      this.getListxq();
    },
    getListxq(currentTar = '') {
      this.dataTar = [];
      let param = {
        startstation:this.model.startstation,
        endstation: this.model.endstation,
        starttimes: this.model.starttimes,
        pageNo: currentTar || this.current,
        pageSize: 10,
        stast:this.model.sectionid,
      };
      getAction(this.url.listxq, param).then((res) => {
        if(currentTar){
          this.dataTarAll.push(res.result);
          // this.dataTarAll.push(res.result.records);
        }else{
          this.dataTarAll = [];
        }
        this.dataTar = res.result.records;
        this.total = res.result.total;
        // this.pages = res.result.pages;
        // if(this.pages > 10){
        //   this.pages = 10;
        // }
        this.$nextTick(() => {
          if (this.dataTarAll.length == 0) {
            this.$refs.table.getEcarts(this.dataTar, `echartLine1`, `高程(m)`, `avgheight`);
            setTimeout(() => {
              // this.visiblePrint = false;
              // this.PrintLoading = false;
            }, 1000);
          }
        });
        // console.log(this.dataTar, "``````model``````````````");
      });
    },
    bindPrint() {
      let that = this;
      // 隐藏不希望打印的元素  
      // window.document.getElementById(`not-print`).style.display = 'none';
      // console.log(that.$refs.table,'that.$refs.table');
      this.iconLoading = true;
      let id = window.document.getElementById(`paperA4`)
      html2canvas(id, {
      // html2canvas(that.$refs.table, {
        allowTaint: true,
        taintTest: false,
        useCORS: true,
        // width: document.body.clientWidth,
        // height: 800*10,
        //dpi: window.devicePixelRatio * 4,
        // scale: 10
      }).then((canvas) => {
        // window.document.getElementById(`not-print`).style.display = '';// 恢复隐藏元素的显示  
        this.getListxq();
        this.iconLoading = false;
        const url = canvas.toDataURL();
        //console.log("url",url)
        printJS({
          printable: url, // 要打印的id
          type: "image",
          mediaPrint: true,
          // style: `@page{size:auto;margin: 0cm 1cm 0cm 1cm;}`, //去除页眉页脚//
          style: `@media print { @page {size: auto; margin:  0cm 1cm 0cm 1cm; } body{margin:0px 0 0 20px}}`//解决出现多页打印时第一页空白问题
        });
      });
    },
    bindPrintMulti(){
      // this.iconLoading = true;
      // if(this.pages > 10){
      //   this.pages = 10;
      // }
      for (let i = 0; i < 10; i++) {
        let pageNo = this.PrintNum + i;
        console.log(pageNo,i,'pageNo');
        this.getListxq(pageNo);
        setTimeout(()=>{
          if(i == 9){
            console.log(this.dataTarAll,'this.dataTarAll');
            this.dataTarAll.sort((a, b) => {  //排序
                if (a.current < b.current) {  
                    return -1;  
                }  
                if (a.current > b.current) {  
                    return 1;  
                }  
                return 0;  
            }); 
            // setTimeout(() => {
            // }, 1000);
            setTimeout(() => {
              for (let k = 0; k < 10; k++) {
                console.log(this.dataTarAll[k],k,'this.dataTarAll[k]',this.$refs.tableAll);
                // let arr = [];
                // if(!this.dataTarAll[k].records){
                //   arr = this.dataTarAll[k].records;
                // }
                this.$refs.tableAll[k].getEcarts(this.dataTarAll[k].records, `echartLine${k+1}`, `高程(m)`, `avgheight`);
                if(i == 9){
                  setTimeout(() => {
                    // this.bindPrint();
                    // setTimeout(() => {
                      this.visiblePrint = false;
                      this.PrintLoading = false;
                    // }, 1000);
                  }, 1000);
                }
              }
            }, 1500);
          }
        },1000)
      }
    },
    showModal() {
      this.visiblePrint = true;
    },
    handleOk(e) {
      this.PrintLoading = true;
      this.bindPrintMulti();
    },
    handleCancel(e) {
      // console.log('Clicked cancel button');
      this.visiblePrint = false;
    },
    change(page, pageSize) {
      this.current = page;
      this.getListxq();
    },
    submitForm() {
      console.log("model 啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊", this.model);
      const that = this;
      if (that.model.startstation)
        that.model.startstation = Number(that.model.startstation);
      if (that.model.endstation) that.model.endstation = Number(that.model.endstation);
      // 触发表单验证
      this.$refs.form.validate((valid) => {
        if (valid) {
          that.confirmLoading = true;
          let httpurl = "";
          let method = "";
          if (!this.model.id) {
            httpurl += this.url.add;
            method = "post";
          } else {
            httpurl += this.url.edit;
            method = "put";
          }
          httpAction(httpurl, this.model, method)
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
            });
        }
      });
    },
  },
};
</script>
<style lang="less" scoped>
.pagination {
  display: flex;
  justify-content: end;
  margin-top: 10px;
  margin-right:17px;
  // background-color: #c26363;
}
.BtnStyle{
  margin:0 0 10px 20px;
}
.inputStyle{
  width: 150px;
}
</style>

