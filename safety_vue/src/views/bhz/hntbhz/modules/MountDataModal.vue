<template>
  <j-modal
    centered
    :title="title"
    :width="1300"
    :visible="visible"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭"
    okText="填报"
  >
    <a-form layout="inline">
      <!--            <a-divider type="horizontal" ><span style="color:#0785fd">施工方原因说明</span></a-divider>-->
      <a-row :gutter="[0, 24]">
        <a-col :span="24">
          <a-form-item label="说明原因">
            <a-textarea
              placeholder="请填入说明原因"
              v-model="modelData.note"
              :auto-size="{ minRows: 3, maxRows: 6 }"
              style="width: 400px"
            ></a-textarea>
            <!-- <a-input v-model="bhzCementOverHandler.problemReasons" placeholder="无"></a-input> -->
          </a-form-item>
        </a-col>
      </a-row>

      <a-divider type="horizontal"
        ><span style="color: #0785fd">未用任务单数据选择</span></a-divider
      >

      <a-row :gutter="[0, 24]">
        <a-col :span="6">
          <a-form-item label="设备名称">
            <j-search-select-tag
              style="width: 220px;"
              placeholder="请选择设备名称"
              v-model="queryParam.shebeibianhao"
              :dictOptions="dictOption"
            >
            </j-search-select-tag>
            {{ selectValue }}
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label="配合比编号">
            <a-input placeholder="请输入配合比编号" v-model="queryParam.code"></a-input>
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="浇筑部位">
            <a-input
              @change = "poureLocation1"
              placeholder="请输入浇筑部位"
              v-model="queryParam.poureLocation"
              allowClear
            ></a-input>
          </a-form-item>
        </a-col>
        <!-- <a-col :span="7">
          <a-form-item label="浇筑部位">
            <a-input
              placeholder="请输入浇筑部位"
              v-model="poureLocation"
              allowClear
            ></a-input>
          </a-form-item>
        </a-col>
        <a-col :span="7">
          <a-form-item label="出料时间范围">
            <j-date
              placeholder="开始出料时间"
              v-model="startTime"
              :showTime="true"
              dateFormat="YYYY-MM-DD HH:mm:ss"
            />
          </a-form-item>
        </a-col>
        <a-col :span="5">
          <a-form-item>
            <j-date
              placeholder="结束出料时间"
              v-model="endTime"
              :showTime="true"
              dateFormat="YYYY-MM-DD HH:mm:ss"
            />
          </a-form-item>
        </a-col> -->
        <a-col :span="4">
          <a-form-item>
            <!-- <a-button type="primary" @click="search">搜索</a-button> -->
            <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
            <a-button
              type="primary"
              @click="searchReset"
              icon="reload"
              style="margin-left: 8px"
              >重置</a-button
            >
          </a-form-item>
        </a-col>
      </a-row>
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
        :rowSelection="{
          selectedRowKeys: selectedRowKeys,
          onChange: onSelectChange,
          type: 'radio',
        }"
        :loading="loading"
        @change="tableChange"
      >
        <span slot="stirDatetime" slot-scope="stirDatetime, record">
          <a-tag color="green">{{ record.stirDatetime }}</a-tag>
        </span>
        <span slot="isjzl" slot-scope="isjzl, record">
          <a-tag color="green" v-if="record.isjzl == 1">已使用</a-tag>
          <a-tag color="grey" v-else-if="record.isjzl == 0">数据关联中</a-tag>
          <a-tag color="blue" v-else-if="record.isjzl == 28">已关联</a-tag>
          <a-tag color="blue" v-else-if="record.isjzl == 29">关联通过</a-tag>
          <a-tag color="orange" v-else-if="record.isjzl == 40">使用异常</a-tag>
          <a-tag color="red" v-else>未使用</a-tag>
        </span>
        <span slot="code" slot-scope="code, record">
          <a-tag color="green">{{ record.code }}</a-tag>
        </span>
      </a-table>
    </a-form>
  </j-modal>
</template>

<script>
import { getAction, postAction } from "@/api/manage";
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import JSearchSelectTag from '@/components/dict/JSearchSelectTag'
import { usershebeiList } from '@api/api'
import Vue from 'vue'

export default {
  mixins: [JeecgListMixin],
  name: "MountDataModal",
  components: {JSearchSelectTag},
  data() {
    return {
      labelCol: {
        xs: { span: 24 },
        sm: { span: 12 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 },
      },
      dataSource: [],
      selectedRowKeys: [],
      selectionRows: [],
      selectionRowsFa: [],
      startTime: "",
      endTime: "",
      poureLocation: "",
      modelData: {},
      loading: false,
      visible: false,
      title: "填报原因",
      ipagination: {
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
      columns: [
        {
          title: "序号",
          dataIndex: "",
          key: "rowIndex",
          width: 60,
          align: "center",
          customRender: function (t, r, index) {
            return parseInt(index) + 1;
          },
        },
        {
          title: "设备名称",
          align: "center",
          dataIndex: "shebeibianhao_dictText",
        },
        {
          title: "施工部位",
          align: "center",
          dataIndex: "conspos",
        },
        {
          title: "配料单号",
          align: "center",
          dataIndex: "code",
          scopedSlots: { customRender: "code" },
        },
        {
          title: "任务单号",
          align: "center",
          dataIndex: "renwudan",
        },
      ],
      url: {
        //  bhTimeList: '/hntbhz/bhzCementBase/bhTimeList',
        list: "/system/shigongphb/sphblist",
        bhTimeList: "/system/shigongphb/sphblist",
        updateBatch: "/hntbhz/bhzCementBase/updateBatch",
      },
      dictOption:[],
      isAgree: 1,
    };
  },
  created() {},
  methods: {
    poureLocation1(){
      this.queryParam.conspos = '*'+this.queryParam.poureLocation+'*'
    },
    getListXq() {
      this.loading = true;
      let params = {
        pageNo: this.ipagination.current,
        pageSize: this.ipagination.pageSize,
        column: "id",
        order: "desc",
        conspos: '*'+this.poureLocation+'*',
        shebeiNo: this.modelData.shebeibianhao,
        // superQueryParams: encodeURI(JSON.stringify([{"rule":"ne","type":"string","dictCode":"","val":"50","field":"renwudanstatus"}])),
        // superQueryMatchType: 'and',
      };
      getAction(this.url.bhTimeList, params)
        .then((res) => {
          if (res.success) {
            this.dataSource = res.result.records;
            if (res.result.total) {
              this.ipagination.total = res.result.total;
            } else {
              this.ipagination.total = 0;
            }
          }
        })
        .finally(() => {
          this.loading = false;
        });
    },
    getList() {
      this.startTime = "";
      this.endTime = "";
      this.poureLocation = "";
      this.ipagination.current = 1;
      this.ipagination.pageSize = 10;
      this.selectedRowKeys = [];
      this.selectionRows = [];
      this.shebeiList()
      // this.getListXq()
      this.visible = true;
    },
    shebeiList: function () {
      var sys_depart_orgcode = Vue.ls.get('SYS_DEPART_ORGCODE')
      usershebeiList({
        sys_depart_orgcode: sys_depart_orgcode,
        sbtypes: '0'
      }).then(res => {
        this.dictOption = []
        let result = res.result
        result.forEach(re => {
          this.dictOption.push({ text: re.sbname, value: re.sbjno })
        })
        //console.log(res)
      })
    },
    onSelectChange(selectedRowKeys, selectionRows) {
      this.selectedRowKeys = selectedRowKeys;
      this.selectionRows = selectionRows;
    },
    tableChange(pagination) {
      this.ipagination.current = pagination.current;
      this.ipagination.pageSize = pagination.pageSize;
      this.getListXq();
    },
    search() {
      this.ipagination.current = 1;
      this.ipagination.pageSize = 10;
      this.getListXq();
    },
    handleCancel() {
      this.visible = false;
    },
    handleOk() {
      // if (this.selectionRows.length > 0) {
        let ids = [];
        this.selectionRowsFa.forEach((item) => {
          ids.push({
            id: item.id,
            estimateNumber: item.estimateNumber,
          });
        });
        let params = {};
        if (this.selectionRows.length > 0) {
          params = {
            ids: ids,
            phbcode: this.selectionRows[0].code,
            rwdcode: this.selectionRows[0].renwudan,
            station: this.selectionRows[0].station,
            sbjno: this.selectionRows[0].shebeibianhao,
            note: this.modelData.note,
          };
        }else{//没选就设置默认值
          params = {
            ids: ids,
            phbcode: `SPHB-20250429-1900983`,
            rwdcode: `RWD-20250429-1900143`,
            station: `0`,
            sbjno: `A05A01A03A01A01A07A01_BHZ_0938`,
            note: this.modelData.note,
          };
        }
        console.log(this.selectionRows, "selectionRows   handleOk");
        console.log(this.modelData, "modelData   handleOk");
        console.log(params, "params   handleOk");
        if (!this.modelData.note) {
          this.$message.warning("请填写说明后再关联配料单！");
        } else {
          postAction(this.url.updateBatch, params).then((res) => {
            if (res.success) {
              this.$message.success("保存成功！");
              this.$emit('ok');
              this.visible = false;
            } else {
              this.$message.error("保存失败！");
            }
          });
        }
      // } else {
      //   this.$message.warning("请选择数据后保存！");
      // }
    },
    renwudan() {
      this.queryParam.renwudan = "*" + this.queryParam.renwudan1 + "*";
    },
  },
};
</script>
<style scoped></style>
