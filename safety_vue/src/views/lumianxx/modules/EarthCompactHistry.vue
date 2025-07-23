<template>
  <j-modal
    :title="title"
    :width="1400"
    :visible="visible"
    :maskClosable="false"
    switchFullscreen
    @ok="handleOk"
    @cancel="handleCancel"
  >
    <!-- <a-form layout="inline">
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
    </a-form> -->
    <iframe :src="iframeUrl" style="height: 600px; width: 100%; margin: 0; border: 0">
    </iframe>
  </j-modal>
</template>

<script>
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import JDictSelectTag from "@/components/dict/JDictSelectTag.vue";
import { getAction } from "@api/manage";
import LineChartsYajiang from "@views/zlyj/modules/LineChartsYajiang";
import * as echarts from "echarts";
import Vue from "vue";

export default {
  name: "PavingResultResult",
  mixins: [JeecgListMixin],
  components: {
    LineChartsYajiang,
    JDictSelectTag,
  },
  data() {
    return {
      flowSrc: `https://dp.huace.cn/digitalPlatform/module/historyReloadCanvas.shtml?UID=hongqinghui&project_id=`,
      flowSrc2: `https://dp.huace.cn/digitalPlatform/compact_reload/resultReloadCanvas.shtml?project_id=`,
      iframeUrl: "",
      // projectId: "jishu03@20220517164029",
      // sectionId: "jishu03@20220521203838",
      projectId: "",
      sectionId: "",
      height: 420,
      data: [],
      ipagination: false,
      yajiangrenwudanms: false,
      testData: [
        {
          gcmc: "2023-04-22",
          lblx: "宁波急山湾疏港高速坤亭至,",
          yjsj: "宁波急山湾疏港高速坤亭至",
          yjsbbh_dictText: "下面层",
          lianghao: "K2+550",
          sgbw: "K2+560",
          cjsjl: "左福",
          zlsj: 172,
          yajiangji: 42,
          kongdaoshu: "100%",
          yajiangfang: "0%",
        },
      ],
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
      projectid: "",
      sectionid: "",
    };
  },
  // mounted() {
  // },
  methods: {
    call(e) {
      this.syjid = e;
      this.visible = true;
      this.getData();
    },
    getData() {
      if (this.title == "历史回放") {
        this.iframeUrl =
          this.flowSrc +
          this.projectid +
          `&section_id=${this.sectionid}` +
          `&start_time=${this.startTime}` +
          `&end_time=${this.endTime}`;
      } else {
        this.iframeUrl =
          this.flowSrc2 +
          this.projectid +
          `&section_id=${this.sectionid}` +
          `&start_time=${this.startTime}` +
          `&end_time=${this.endTime}`;
      }

      console.log(this.iframeUrl, "iframeUrl--------------------------------");
      // let username = this.$route.query.username;
      // let param = {};
      // getAction(this.url.hcProject, param).then((res) => {
      //   console.log(res);
      //   this.dictOption = [];
      //   let result = res.result.records;
      //   this.projectList = res.result.records;
      //   this.orgcode = result[0].orgcode;
      //   this.projectId = result[0].pjid;
      //   this.queryParam.shebeino = result[0].pjname;
      //   result.forEach((res) => {
      //     this.dictOption.push({ text: res.pjname, value: res.orgcode });
      //   });
      //   this.getDataBD(this.orgcode);
      // });
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
        this.iframeUrl =
          this.flowSrc +
          this.projectId +
          `&section_id=${this.sectionId}` +
          `&start_time=${this.startTime}` +
          `&end_time=${this.endTime}`;
        console.log(this.iframeUrl, "iframeUrl--------------------------------");
        this.queryParam.biaoduan = result[0].sectionname;
        result.forEach((res) => {
          this.dictOptionBD.push({ text: res.sectionname, value: res.sectionid });
        });
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
      console.log(this.projectId, this.sectionId, "this.projectId------------sectionId");
    },
    handleChangeBD(selectedValue) {
      let arr = this.sectionList.filter((e) => {
        return e.sectionid == selectedValue;
      });
      this.sectionId = arr[0].sectionid;
      // console.log("selectedValue", selectedValue);
      // this.sectionId = selectedValue;
      console.log(this.projectId, this.sectionId, "this.projectId------------sectionId");
    },
    chaxun: function () {
      this.cx = this.selectedValue;
      // this.dates = this.approval_remarks;
      // this.bhzsta();
      // this.fanglaing();
      // this.hegelv();
    },
    chongzhi: function () {
      this.dictOption = [];
      this.selectedValue = null;
      this.approval_remarks = null;
      this.chaxun();
    },
    close() {
      this.visible = false;
    },
    handleOk() {
      this.visible = false;
    },
    handleCancel() {
      this.visible = false;
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
