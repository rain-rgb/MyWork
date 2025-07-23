<template>
  <!-- 施工成果-->
  <a-card :bordered="false">
    <a-form layout="inline">
      <a-row :gutter="24">
        <a-col :xl="6" :lg="7" :md="8" :sm="24">
          <a-form-item label="项目">
            <j-search-select-tag
              style="width: 250px"
              placeholder="请选择项目名称"
              v-model="queryParam.shebeino"
              :dictOptions="dictOption"
              @change="handleChange"
            >
            </j-search-select-tag>
            {{ selectValue }}
          </a-form-item>
        </a-col>
        <a-col :xl="6" :lg="7" :md="8" :sm="24">
          <a-form-item label="标段">
            <j-search-select-tag
              style="width: 250px"
              placeholder="请选择标段名称"
              v-model="queryParam.biaoduan"
              :dictOptions="dictOptionBD"
              @change="handleChangeBD"
            >
            </j-search-select-tag>
            {{ selectValueBD }}
          </a-form-item>
        </a-col>
        <a-col :xl="6" :lg="7" :md="8" :sm="24">
          <span
            style="float: left; overflow: hidden"
            class="table-page-search-submitButtons"
          >
            <a-button type="primary" @click="chaxun" icon="search">查询</a-button>
            <a-button
              type="primary"
              @click="chongzhi"
              icon="reload"
              style="margin-left: 8px"
              >重置</a-button
            >
          </span>
        </a-col>
      </a-row>
    </a-form>
    <!-- <iframe
      :src="flowSrc + projectId + `&sectionId=` + sectionId"
      style="height: 600px; width: 100%; margin: 0; border: 0"
    > -->
    <!-- <iframe
      :src="
        flowSrc +
        projectId +
        `&sectionId=${sectionId}` +
        `&start_time=${startTime}` +
        `&end_time=${endTime}`
      "
      style="height: 600px; width: 100%; margin: 0; border: 0"
    >
    </iframe> -->
    <iframe
      :src="iframeUrl"
      style="height: 80vh; width: 100%; margin: 0 30px 0 0; border: 0"
    >
    </iframe>
  </a-card>
</template>

<script>
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import JDictSelectTag from "@/components/dict/JDictSelectTag.vue";
import { getAction } from "@api/manage";
import LineChartsYajiang from "@views/zlyj/modules/LineChartsYajiang";
import * as echarts from "echarts";
import Vue from "vue";

export default {
  name: "PavingResultLive",
  mixins: [JeecgListMixin],
  components: {
    LineChartsYajiang,
    JDictSelectTag,
  },
  data() {
    return {
      flowSrc: `https://dp.huace.cn/digitalPlatform/module/realTimeReloadCanvas.shtml?UID=hongqinghui&projectId=`,

      // https://dp.huace.cn/digitalPlatform/module/realTimeReloadCanvas.shtml?UID=hongqinghui&projectId=jishu03@20220517164029&sectionId=jishu03@20220521203838
      iframeUrl: "",
      iframeUrlTem: "",
      // projectId: "jishu03@20220517164029",
      // sectionId: "jishu03@20220521203838",
      projectId: "",
      sectionId: "",
      height: 420,
      data: [],
      ipagination: false,
      yajiangrenwudanms: false,
      data2: [],
      data1: [],
      title: "",
      ipagination1: {
        current: 1,
        pageSize: 10,
        pageSizeOptions: ["10", "20", "30"],
        showTotal: (total, range) => {
          return range[0] + "-" + range[1] + " 共" + total + "条";
        },
        showQuickJumper: true,
        showSizeChanger: true,
        total: 0,
      },
      width: 800,
      visible: false,
      disableSubmit: false,
      syjid: "",
      uuid: "",
      datadetail: [],
      url: {
        hcProject: "/hc_project/hcProject/list",
        hcSection: "/hc_section/hcSection/list",
        hcProject1: "/hc_section/hcSection/listls",
        list: "/yajiangs/trYajiangS/list1",
        listdetail: "/yajiangs/trYajiangSS/list",
        listbutton: "/yajiangm/trYajiangM/list2", //压浆任务单下压浆主表信息查询
      },
      holeid: "",
      dictOption: [],
      dictOptionBD: [],
      selectValue: "",
      selectValueBD: "",
      orgcode: "",
      projectList: [],
      sectionList: [],
      startTime: "",
      endTime: "",
    };
  },
  mounted() {
    this.$nextTick(() => {
      this.callFun();
    });
  },
  methods: {
    callFun() {
      this.visible = true;
      this.getData();
    },
    getData() {
      // let username = this.$route.query.username;
      var sys_depart_orgcode = Vue.ls.get("SYS_DEPART_ORGCODE");
      console.log(sys_depart_orgcode, "sys_depart_orgcode-------------------");
      let param = {
        orgCode: sys_depart_orgcode,
      };
      getAction(this.url.hcProject, param).then((res) => {
        console.log(res, "res---------------");
        this.dictOption = [];
        let result = res.result.records;
        if (result == null){
          this.getData1() 
        }else {
          this.projectList = res.result.records;
          this.orgcode = result[0].orgcode;
          this.projectId = result[0].pjid;
          this.queryParam.shebeino = result[0].pjname;
          result.forEach((res) => {
            this.dictOption.push({ text: res.pjname, value: res.orgcode });
          });
          this.getDataBD(this.orgcode);
        }
      });
    },
    getData1() {
      // let username = this.$route.query.username;
      var sys_depart_orgcode = Vue.ls.get("SYS_DEPART_ORGCODE");
      console.log(sys_depart_orgcode, "sys_depart_orgcode-------------------");
      let param = {
        orgCode: sys_depart_orgcode,
      };
      getAction(this.url.hcProject1, param).then((res) => {
        console.log(res, "res---------------");
        this.dictOption = [];
        let result = res.result.records;
          this.projectList = res.result.records;
          this.orgcode = result[0].orgcode;
          this.projectId = result[0].pjid;
          this.queryParam.shebeino = result[0].pjname;
          result.forEach((res) => {
            this.dictOption.push({ text: res.pjname, value: res.orgcode });
          });
          this.getDataBD(this.orgcode);
      });
    },
    getDataBD(orgcode) {
      let param = {
        orgcode,
      };
      getAction(this.url.hcSection, param).then((res) => {
        console.log(res, "hcSection------------");
        this.dictOptionBD = [];
        let result = res.result.records;
        this.sectionList = res.result.records;
        this.sectionId = result[0].sectionid;
        this.iframeUrlTem =
          this.flowSrc + this.projectId + `&sectionId=${this.sectionId}`;
        console.log(this.iframeUrlTem, "iframeUrlTem--------------------------------");
        this.queryParam.biaoduan = result[0].sectionname;
        result.forEach((res) => {
          this.dictOptionBD.push({ text: res.sectionname, value: res.sectionid });
        });
        if (!this.iframeUrl) {
          this.chaxun();
        }
        console.log(
          this.projectId,
          this.sectionId,
          result[0],
          "this.projectId------------getDataBD"
        );
      });
    },
    handleChange(selectedValue) {
      let arr = this.projectList.filter((e) => {
        return e.orgcode == selectedValue;
      });
      this.projectId = arr[0].pjid;
      console.log("selectedValue", arr, arr.pjid);
      this.getDataBD(selectedValue);
      console.log(this.iframeUrlTem, "this.iframeUrlTem------------");
      // console.log(this.projectId, this.sectionId, "this.projectId------------sectionId");
    },
    handleChangeBD(selectedValue) {
      let arr = this.sectionList.filter((e) => {
        return e.sectionid == selectedValue;
      });
      this.sectionId = arr[0].sectionid;
      this.iframeUrlTem = this.flowSrc + this.projectId + `&sectionId=${this.sectionId}`;
      // console.log("selectedValue", selectedValue);
      // this.sectionId = selectedValue;
      console.log(this.iframeUrlTem, "this.iframeUrlTem------------");
    },
    chaxun: function () {
      this.iframeUrl = this.iframeUrlTem;
      console.log(this.iframeUrlTem, "chaxun .chaxun------------chaxun chaxun chaxun");
    },
    chongzhi: function () {
      this.iframeUrl = "";
      this.getData();
    },
  },
};
</script>
<style scoped>
.charts-box {
  display: flex;
}
#PavingRD_Ecahrt {
  width: 100%;
  height: 200px;
}
</style>
