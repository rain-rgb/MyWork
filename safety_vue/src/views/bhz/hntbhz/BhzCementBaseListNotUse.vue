<template>
  <!-- 拌合站生产动态查询 -->
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="设备名称">
              <j-search-select-tag
                placeholder="请选择设备名称"
                v-model="queryParam.shebeiNo"
                :dictOptions="dictOption"
              >
              </j-search-select-tag>
              {{ selectValue }}
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="状态">
              <j-search-select-tag
                placeholder="请选择超产说明状态"
                v-model="queryParam.renwudanstatus"
                :dictOptions="dictOption2"
              ></j-search-select-tag>
            </a-form-item>
          </a-col>

          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="生产时间范围">
              <j-date
                placeholder="开始生产时间"
                v-model="queryParam.productDatetime_begin"
                :showTime="true"
                dateFormat="YYYY-MM-DD HH:mm:ss"
              />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="">
              <j-date
                placeholder="结束生产时间"
                v-model="queryParam.productDatetime_end"
                :showTime="true"
                dateFormat="YYYY-MM-DD HH:mm:ss"
              />
            </a-form-item>
          </a-col>
        </a-row>
         <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="强度等级">
              <j-search-select-tag
                placeholder="请选择强度等级"
                v-model="queryParam.strengthRank"
                :dictOptions="dictOption1"
                @change="getQddj"
              >
              </j-search-select-tag>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="浇筑部位">
              <a-input
                placeholder="请输入浇筑部位"
                v-model="poureLocation1"
                @change="poureLocation"
              >
              </a-input>
            </a-form-item>
          </a-col>

          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span
              style="float: left; overflow: hidden"
              class="table-page-search-submitButtons"
            >
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button
                type="primary"
                @click="searchQuery1"
                icon="reload"
                style="margin-left: 8px"
                >重置</a-button
              >
              <!-- <a @click="handleToggleSearch" style="margin-left: 8px">
                {{ toggleSearchStatus ? '收起' : '展开' }}
                <a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>
              </a> -->
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div>
      <!--      <a-button type="primary" icon="save">填报</a-button>-->

      <!-- 高级查询区域 -->
      <!--      <j-super-query :fieldList="superFieldList" ref="superQueryModal" @handleSuperQuery="handleSuperQuery"></j-super-query>-->
      <!--      <a-dropdown v-if="selectedRowKeys.length > 0">-->
      <!--        <a-menu slot="overlay">-->
      <!--          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>-->
      <!--        </a-menu>-->
      <!--        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>-->
      <!--      </a-dropdown>-->
    </div>

    <!-- table区域-begin -->
    <div class="table-operator">
      <a-button v-has="'bhz:rwsh'" type="primary"   @click="shenpi">审批</a-button>
      <a-button v-has="'bhz:rwtb'" type="primary" @click="mountData()">填报</a-button>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择
        <a style="font-weight: 600">{{ selectedRowKeys.length }}</a
        >项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table
        ref="table"
        size="middle"
        bordered
        rowKey="id"
        class="j-table-force-nowrap"
        :scroll="{ x: true }"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange1 }"
        @change="handleTableChange"
      >
        <span slot="tags" slot-scope="tags, record">
          <a-tag color="green" v-if="record.overLevel == '0'">合格</a-tag>
          <a-tag color="orange" v-if="record.overLevel == '1'">初级超标</a-tag>
          <a-tag color="yellow" v-if="record.overLevel == '2'">中级超标</a-tag>
          <a-tag color="red" v-if="record.overLevel == '3'">高级超标</a-tag>
        </span>
        <span
          v-if="record.overLevel != '0' && record.overLevel != null"
          slot="tags1"
          slot-scope="tags1, record"
        >
          <a-tag color="green" v-if="record.overproofStatus === 20">已闭合</a-tag>
          <a-tag color="red" v-if="record.overproofStatus !== 20">未闭合</a-tag>
        </span>

        <span slot="isjzl" slot-scope="isjzl, record">
          <a-tag color="green" v-if="record.renwudanstatus == 1">已使用</a-tag>
          <a-tag color="grey" v-else-if="record.renwudanstatus === 0">数据关联中</a-tag>
           <a-tag color="red" v-else-if="record.renwudanstatus === 27">已驳回待重填</a-tag>
          <a-tag color="blue" v-else-if="record.renwudanstatus === 28">已关联</a-tag>
          <a-tag color="green" v-else-if="record.renwudanstatus === 29">已通过</a-tag>
          <a-tag color="orange" v-else-if="record.renwudanstatus == 40">使用异常</a-tag>
          <a-tag color="red" v-else>未使用</a-tag>
        </span>

        <span slot="formulaNo" slot-scope="formulaNo, record">
          <a-tag color="green">{{ record.formulaNo }}</a-tag>
        </span>
        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <template slot="imgSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px; font-style: italic">无图片</span>
          <img
            v-else
            :src="getImgView(text)"
            height="25px"
            alt=""
            style="max-width: 80px; font-size: 12px; font-style: italic"
          />
        </template>
        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px; font-style: italic">无文件</span>
          <a-button
            v-else
            :ghost="true"
            type="primary"
            icon="download"
            size="small"
            @click="downloadFile(text)"
          >
            下载
          </a-button>
        </template>

        <span slot="action" slot-scope="text, record">
          <!--          <a v-if="record.renwudanstatus == 30" @click="handleEdit(record)">填报</a>-->
          <!--          <a v-if="record.renwudanstatus == 28" @click="handleEdit(record)">审批</a>-->
          <!--            <a-divider type="vertical"  v-if="[28, 30].includes(record.renwudanstatus)"/>-->
          <a @click="handleDetail(record)">详情</a>
        </span>
      </a-table>
    </div>
    <j-modal
      title="审批意见"
      :width="450"
      :visible="visible"
      @ok="handleOk"
      @cancel="handleCancel"
      cancelText="关闭"
    >

      <a-row :span="12">
        <a-form-model-item label="是否同意" >
          <a-radio-group v-model="isAgree">
            <a-radio :value="29">是</a-radio>
            <a-radio :value="27">否</a-radio>
          </a-radio-group>
        </a-form-model-item>

      </a-row>
      <a-row :span="12">
        <a-form-model-item label="审批意见" >
          <a-input placeholder="请输入审批意见" v-model="option"></a-input>
        </a-form-model-item>
      </a-row>

    </j-modal>
    <bhz-not-use-modal ref="modalForm" @ok="modalFormOk" />
    <MountDataModal ref="mountData" @ok="modalFormOk"></MountDataModal>
  </a-card>
</template>

<script>
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import BhzNotUseModal from "./modules/BhzNotUseModal";
import { filterMultiDictText } from "@/components/dict/JDictSelectUtil";
import "@/assets/less/TableExpand.less";
import JSuperQuery from "@/components/jeecg/JSuperQuery.vue";
import { handertoken } from "@/mixins/getHanderToken";
import { usershebeiList } from "@/api/api";
import MountDataModal from "./modules/MountDataModal";
import { SYS_DEPART_ORGCODE } from "@/store/mutation-types";
import Vue from "vue";
import { getAction, postAction } from "@api/manage";

export default {
  name: "BhzCementBaseListRC",
  mixins: [JeecgListMixin, handertoken],
  components: {
    BhzNotUseModal,
    JSuperQuery,
    MountDataModal,
  },
  data() {
    return {
      isAgree:29,
      option: "",
      visible: false,
      poureLocation1: "",
      isusejz: "",
      selectValue: "",
      asyncSelectValue: "",
      dictOption: [],
      dictOption1: [],
      dictOption2: [
        { text: "未使用", value: "30" },
        { text: "驳回", value: "27" },
        { text: "已关联", value: "28" },
        { text: "关联通过", value: "29" },
      ],
      description: "拌合站主表管理页面",
      queryParam: {
         id_begin: 8152691,
        renwudanstatus_begin: 10,
        renwudanstatus_end: 30,
      },
      // 表头
      columns: [
        {
          title: "序号",
          dataIndex: "",
          key: "rowIndex",
          width: 60,
          align: "center",
          fixed: "left",
          customRender: function (t, r, index) {
            return parseInt(index) + 1;
          },
        },
        {
          title: "浇筑令",
          align: "center",
          dataIndex: "isjzl",
          key: "isjzl",
          fixed: "left",
          scopedSlots: { customRender: "isjzl" },
        },

        {
          title: '设备名称',
          align: 'center',
          fixed: "left",
          dataIndex: 'shebeiNo_dictText'
        },
        {
          title: "工程名称",
          align: "center",
          dataIndex: "projectName",
        },

        {
          title: "浇筑部位",
          align: "center",
          dataIndex: "poureLocation",
        },
        {
          title: "配比名称",
          align: "center",
          dataIndex: "formulaNo",
        },

        {
          title: "强度等级",
          align: "center",
          dataIndex: "strengthRank",
        },
        {
          title: "生产时间",
          align: "center",
          dataIndex: "productDatetime",
        },
        {
          title: "采集时间",
          align: "center",
          dataIndex: "collectDatetime",
        },
        {
          title: "超标等级",
          align: "center",
          dataIndex: "overLevel_dictText", //overLevel_dictText
          key: "overLevel_dictText",
          scopedSlots: { customRender: "tags" },
        },

        {
          title: "操作",
          dataIndex: "action",
          align: "center",
          fixed: "right",
          width: 100,
          scopedSlots: { customRender: "action" },
        },
      ],
      isorter: {
        column: "productDatetime",
        order: "desc",
      },

      url: {
        // list: '/hntbhz/bhzCementBase/productionList',
        list: "/hntbhz/bhzCementBase/listrc3",
        delete: "/hntbhz/bhzCementBase/delete",
        deleteBatch: "/hntbhz/bhzCementBase/deleteBatch",
        exportXlsUrl: "/hntbhz/bhzCementBase/listProduce",
        importExcelUrl: "hntbhz/bhzCementBase/importExcel",
        getQddjList: "hntbhz/bhzCementBase/getQddjList",
        updateBatch: "/hntbhz/bhzCementBase/updateBatchSP",
      },
      dictOptions: {},
      superFieldList: [],
    };
  },
  created() {
    this.getSuperFieldList();
    this.shebeiList();
    this.getQddj();
  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG["domianURL"]}/${this.url.importExcelUrl}`;
    },
  },

  methods: {
    handleCancel() {
      this.visible = false;
    },
    handleOk() {
      let ids = [];
      this.selectionRows.forEach((item) => {
        if (item.renwudanstatus !== 28) {
          this.visible = false;
          this.$message.warning("存在未关联数据，请选择已关联数据后再进行审批！");
        } else {
          ids.push({
            id: item.id,
          });
        }
      });

      let params = {
        ids: ids,
        station:this.isAgree,
        note: this.option,
      };
      if (!this.option) {
        this.$message.warning("请填写说明后再关联配料单！");
      } else {
        postAction(this.url.updateBatch, params).then((res) => {
          if (res.success) {
            this.$message.success("保存成功！");
            this.visible = false;
            this.option = "";
            this.modalFormOk();
          } else {
            this.$message.error("保存失败！");
          }
        });
      }
    },
    isjz(a) {
      console.log("-----------------" + a);
      if (a === "2") {
        this.queryParam.renwudanstatus_begin = 10;
        this.queryParam.renwudanstatus_end = 30;
      }
    },
    shenpi() {
      // var a = true
      // this.selectionRows.forEach((item) => {
      //   if(item.renwudanstatus !== 28){
      //     a = false
      //     this.$message.warning('存在未关联数据，请选择已关联数据后再进行审批！')
      //   }
      // })

      if (this.selectedRowKeys.length > 0) {
        this.visible = true;
      } else {
        this.$message.warning("请选择已关联数据进行审批！");
      }
    },
    poureLocation() {
      this.queryParam.poureLocation = "*" + this.poureLocation1 + "*";
    },
    searchQuery1() {
      this.queryParam = {};
      this.isjz("2");
      this.loadData(1);
    },
    shebeiList: function () {
      var sys_depart_orgcode = Vue.ls.get("SYS_DEPART_ORGCODE");
      usershebeiList({
        sys_depart_orgcode: sys_depart_orgcode,
        sbtypes: "0",
      }).then((res) => {
        this.dictOption = [];
        let result = res.result;
        result.forEach((re) => {
          this.dictOption.push({ text: re.sbname, value: re.sbjno });
        });
        //console.log(res)
      });
    },
    getQddj: function () {
      var sys_depart_orgcode = Vue.ls.get("SYS_DEPART_ORGCODE");
      var params = {
        sys_depart_orgcode: sys_depart_orgcode,
      };
      getAction(this.url.getQddjList, params).then((res) => {
        this.dictOption1 = [];
        let result = res.result;
        result.forEach((re) => {
          this.dictOption1.push({ text: re.strengthRank, value: re.strengthRank });
        });
      });
    },
    initDictConfig() {},
    getSuperFieldList() {},
    mountData1() {
      var a = true
      this.selectionRows.forEach((item) => {
        if(item.renwudanstatus == 28){
          a = false
         //  this.$message.warning('存在已通过数据，请选择未关联数据填报！')
        }
      })
      if (this.selectedRowKeys.length > 0) {
       if(a){
         this.$refs.mountData.selectionRowsFa = JSON.parse(JSON.stringify(this.selectionRows))
         this.$refs.mountData.getList();
       }else{
         this.$message.warning('存在已通过数据，请选择未关联数据填报！')
       }

      } else {
        this.$message.warning("请选择数据进行填报！");
      }
    },
    mountData() {
      let hasPassedData = false;
      let hasPassedData2 = false;
      this.selectionRows.forEach((item) => {
        if (item.renwudanstatus == 29) {
          hasPassedData = true;
        }
        if (item.renwudanstatus == 28) {
          hasPassedData2 = true;
        }
      });

      if (this.selectedRowKeys.length === 0) {
        this.$message.warning("请选择数据进行填报！");
        return;
      }

      if (hasPassedData) {
        this.$message.warning('存在已通过数据，请选择未关联数据填报！');
        return;
      }
      if (hasPassedData2) {
        this.$message.warning('存在已填报数据，请选择未关联数据填报！');
        return;
      }

      this.$refs.mountData.selectionRowsFa = JSON.parse(JSON.stringify(this.selectionRows));
      this.$refs.mountData.getList();
    },
    onSelectChange1(selectedRowKeys, selectionRows) {
      this.selectedRowKeys = selectedRowKeys;
      this.selectionRows = selectionRows;
      console.log(this.selectionRows, "this.selectionRows");
    },
  },
};
</script>
<style scoped>
@import "~@assets/less/common.less";
</style>
