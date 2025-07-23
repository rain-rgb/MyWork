<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
      <div class="table-page-search-wrapper" style="margin-bottom: 30px">
        <a-form layout="inline">
          <a-row :gutter="24">
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="选择工程">
                <j-search-select-tag
                  style="width: 200px"
                  placeholder="请选择项目名称"
                  v-model="queryParam.projectid"
                  :dictOptions="dictOptionPro"
                  @change="handleChange"
                >
                </j-search-select-tag>
                <!-- {{ selectValue }} -->
              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="选择标段">
                <j-search-select-tag
                  style="width: 200px"
                  placeholder="请选择标段名称"
                  v-model="queryParam.sectionid"
                  :dictOptions="dictOptionBD"
                  @change="handleChangeBD"
                >
                </j-search-select-tag>
                <!-- {{ selectValueBD }} -->
              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="启始筛选">
                <a-input placeholder="启始里程" v-model="queryParam.startstation"></a-input>
              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="结束里程">
                <a-input placeholder="结束里程" v-model="queryParam.endstation"></a-input>
              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <span
                style="float: left; overflow: hidden"
                class="table-page-search-submitButtons"
              >
                <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
                <!-- <a-button type="primary" @click="searchQueryHandle" icon="search"
                  >查询</a-button
                > -->
                <a-button
                  type="primary"
                  @click="searchReset"
                  icon="reload"
                  style="margin-left: 8px"
                  >重置</a-button
                >
                <!-- <a-button
                  type="primary"
                  v-has="'tfys:dc'"
                  icon="download"
                  style="margin-left: 8px"
                  @click="handleExportXls('土方压实数据列表')"
                  >导出</a-button
                > -->
              </span>
            </a-col>
          </a-row>
        </a-form>
      </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('土方压实配置表')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>
      <!-- 高级查询区域 -->
      <j-super-query :fieldList="superFieldList" ref="superQueryModal" @handleSuperQuery="handleSuperQuery"></j-super-query>
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
      </a-dropdown>
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table
        ref="table"
        size="middle"
        :scroll="{x:true}"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        class="j-table-force-nowrap"
        @change="handleTableChange">

        <template slot="startstationS" slot-scope="text">
          {{getStartstation(text)}}
        </template>
        <template slot="endstationS" slot-scope="text">
          {{getStartstation(text)}}
        </template>
        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <template slot="imgSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无图片</span>
          <img v-else :src="getImgView(text)" height="25px" alt="" style="max-width:80px;font-size: 12px;font-style: italic;"/>
        </template>
        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无文件</span>
          <a-button
            v-else
            :ghost="true"
            type="primary"
            icon="download"
            size="small"
            @click="downloadFile(text)">
            下载
          </a-button>
        </template>

        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item>
              <a-menu-item>
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <hc-tfysworkareapeiz-modal ref="modalForm" @ok="modalFormOk"></hc-tfysworkareapeiz-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import HcTfysworkareapeizModal from './modules/HcTfysworkareapeizModal'
  import JSuperQuery from "@/components/jeecg/JSuperQuery.vue"
  import Vue from 'vue'
  import { getAction } from "@api/manage";

  export default {
    name: 'HcTfysworkareapeizList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      JSuperQuery,
      HcTfysworkareapeizModal
    },
    data () {
      return {
        description: '土方压实配置表管理页面',
        flag: 0,
        syjid: "",
        approval_remarks: null,
        selectValue: "",
        selectValueBD: "",
        asyncSelectValue: "",
        dictOption: [],
        dictOptionPro: [],
        dictOptionBD: [],
        datalist: [],
        dataSource: [],
        fields: [],
        aliases: [],
        // 表头
        columns: [
          {
            title: '#',
            dataIndex: '',
            key:'rowIndex',
            width:60,
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
          },
          {
            title:'工程名称',
            align:"center",
            dataIndex: 'projectid_dictText'
          },
          {
            title: "标段名称",
            align: "center",
            dataIndex: "sectionid_dictText",
          },
          {
            title:'开始桩号',
            align:"center",
            dataIndex: 'startstation',
            scopedSlots: { customRender: 'startstationS' },
          },
          {
            title:'结束桩号',
            align:"center",
            dataIndex: 'endstation',
            scopedSlots: { customRender: 'endstationS' },
          },
          {
            title:'碾压层数',
            align:"center",
            dataIndex: 'layername'
          },
          {
            title:'设计厚度',
            align:"center",
            dataIndex: 'avgheights'
          },
          {
            title:'设计宽度',
            align:"center",
            dataIndex: 'thickavgs'
          },
          {
            title:'施工人员',
            align:"center",
            dataIndex: 'construction_dictText'
          },
          {
            title:'监理人员',
            align:"center",
            dataIndex: 'supervisory_dictText'
          },
          {
            title:'施工日期',
            align:"center",
            dataIndex: 'starttimes'
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            fixed:"right",
            width:147,
            scopedSlots: { customRender: 'action' }
          }
        ],
        url: {
          list: "/hctfysworkareapeiz/hcTfysworkareapeiz/list",
          hcProject: "/hc_project/hcProject/list",
          hcSection: "/hc_section/hcSection/list",
          hcProject1: "/hc_section/hcSection/listls",
          delete: "/hctfysworkareapeiz/hcTfysworkareapeiz/delete",
          deleteBatch: "/hctfysworkareapeiz/hcTfysworkareapeiz/deleteBatch",
          exportXlsUrl: "/hctfysworkareapeiz/hcTfysworkareapeiz/exportXls",
          importExcelUrl: "hctfysworkareapeiz/hcTfysworkareapeiz/importExcel",
          
        },
        dictOptions:{},
        superFieldList:[],
      }
    },
    created() {
      this.getSuperFieldList();
      // this.getToken();
      // this.shebeiList();
      this.getProject();
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      },
    },
    queryParam: {
      projectid: "",
      pileNumStart1: "",
      pileNumStart2: "",
      pileNumEnd1: "",
      pileNumEnd2: "",
      begintime: "",
      endtime: "",
    },
    methods: {
      searchQueryHandle() {
        console.log(111);
        this.getData();
      },
      tableChange(pagination) {
        this.ipagination.current = pagination.current;
        this.ipagination.pageSize = pagination.pageSize;
        this.loadData();
        // this.getData();
      },
      getData() {
        // this.loading = true;
        // let username = this.$route.query.username;
        let param = {
          projectid: this.queryParam.projectid,
          sectionid: this.queryParam.sectionid,
          begintime: "",
          endtime: "",
          column: "starttime",
          order: "desc",
          startstation: "",
          endstation: "",
          pageSize: this.ipagination.pageSize,
          pageNo: this.ipagination.current,
        };
        getAction(this.url.list, param).then((res) => {
          // console.log(res);
          this.dataSource = [];
          // this.dataSource = res.result.records;
          if (res.success) {
            //update-begin---author:zhangyafei    Date:20201118  for：适配不分页的数据列表------------
            this.dataSource = res.result.records || res.result;
            if (res.result.total) {
              this.ipagination.total = res.result.total;
            } else {
              this.ipagination.total = 0;
            }
          } else {
            this.dataSource = null;
            this.$message.warning(res.message);
          }
          // this.loading = false;
        });
      },
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
            this.queryParam.projectid = result[0].pjid;
            result.forEach((res) => {
              this.dictOptionPro.push({ text: res.pjname, value: res.pjid });
            });
            console.log(this.dictOptionPro,'this.dictOptionPro------------');
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
          this.queryParam.projectid = result[0].pjid;
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
          this.queryParam.sectionid = "";
          let result = res.result.records;
          this.sectionList = res.result.records;
          // this.sectionId = result[0].sectionid;
          // this.queryParam.biaoduan = result[0].sectionname;
          this.queryParam.sectionid = result[0].sectionid;
          console.log(this.dictOptionBD, this.queryParam.sectionid, "this.dictOptionBD");
          result.forEach((res) => {
            this.dictOptionBD.push({ text: res.sectionname, value: res.sectionid });
          });
          if (this.firstGetData) {
            this.firstGetData = false;
            this.getData();
          }
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
        this.projectId = arr[0].pjid;
        let orgcode = arr[0].orgcode;
        // console.log("selectedValue", arr, arr.pjid);
        this.getDataBD(orgcode);
        // console.log(this.projectId, this.sectionId, "this.projectId------------sectionId");
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
      initDictConfig(){
      },
      getStartstation(startstation) {
        let startsta = null;
        if (startstation != null) {
          if (startstation.length == 2) {
            startsta = "K0+0" + startstation;
          } else if (startstation.length == 3) {
            startsta = "K0+" + startstation;
          } else if (startstation.length > 3) {
            let substring = startstation.substring(0, startstation.length - 3);
            let substring1 = startstation.substring(startstation.length - 3);
            startsta = "K" + substring + "+" + substring1;
          }
        }
        return startsta;
      },
      getSuperFieldList(){
        let fieldList=[];
        fieldList.push({type:'string',value:'projectid',text:'项目',dictCode:''})
        fieldList.push({type:'string',value:'sectionid',text:'标段',dictCode:''})
        fieldList.push({type:'string',value:'startstation',text:'开始桩号 面型工程没有桩号',dictCode:''})
        fieldList.push({type:'string',value:'endstation',text:'结束桩号 面型工程没有桩号',dictCode:''})
        fieldList.push({type:'string',value:'layername',text:'碾压层数',dictCode:''})
        fieldList.push({type:'string',value:'avgheights',text:'施工高程',dictCode:''})
        fieldList.push({type:'string',value:'thickavgs',text:'设计宽度',dictCode:''})
        fieldList.push({type:'string',value:'starttimes',text:'施工日期',dictCode:''})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>