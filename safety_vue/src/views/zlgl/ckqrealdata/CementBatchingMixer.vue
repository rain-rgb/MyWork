<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <!-- <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="设备名称">
              <j-search-select-tag placeholder="请选择设备名称" v-model="queryParam.sid" :dictOptions="dictOption" >
              </j-search-select-tag>
              {{ selectValue }}
            </a-form-item>
          </a-col> -->
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="标段号">
              <j-dict-select-tag
                placeholder="请选择"
                v-model="queryParam.bidCode"
                dictCode="ckqbd"
              ></j-dict-select-tag>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="时间范围">
              <j-date
                placeholder="开始时间"
                v-model="queryParam.startTime"
                :showTime="true"
                dateFormat="YYYY-MM-DD"
              />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="">
              <j-date
                placeholder="结束时间"
                v-model="queryParam.endTime"
                :showTime="true"
                dateFormat="YYYY-MM-DD"
              />
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
                @click="searchReset"
                icon="reload"
                style="margin-left: 8px"
                >重置</a-button
              >
              <a @click="handleToggleSearch" style="margin-left: 8px">
                {{ toggleSearchStatus ? "收起" : "展开" }}
                <a-icon :type="toggleSearchStatus ? 'up' : 'down'" />
              </a>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择
        <a style="font-weight: 600">{{ selectedRowKeys.length }}</a
        >项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table
        ref="table"
        size="middle"
        :scroll="{ x: true }"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
        class="j-table-force-nowrap"
        @change="handleTableChange"
        :customRow="handleClick"
        :rowClassName="setRowClassName"
      >
        <span class="separate" slot="mixinstation" slot-scope="text, record">
          <!-- <div>{{record.slsj}}</div>
          <a-divider style="margin:0;height:1px" />
          <div>{{record.slll}}</div>
          <a-divider style="margin:0;height:1px" />
          <div>{{record.slwc}}</div> -->
          <div>{{ record.mixinStationCode }}号拌合机</div>
        </span>
        <!-- 配合比 -->
        <!-- <span class="separate" slot="proportion">
          <div>实际</div>
          <a-divider style="margin:0;height:1px" />
          <div>理论</div>
          <a-divider style="margin:0;height:1px" />
          <div>误差%</div>
        </span> -->
        <!-- 数量 -->
        <!-- <span class="separate" slot="number" slot-scope="text,record" >
          <div>{{record.totalWeight}}</div>
          <a-divider style="margin:0;height:1px" />
          <div></div>
          <a-divider style="margin:0;height:1px" />
          <div></div>
        </span> -->
        <!-- 水泥1 -->
        <!-- <span class="separate" slot="cement1" slot-scope="text,record" >
          <div>{{record.realityCement}}</div>
          <a-divider style="margin:0;height:1px" />
          <div>{{record.theoryCement}}</div>
          <a-divider style="margin:0;height:1px" />
          <div>{{record.cementErrorRate}}%</div>
        </span> -->
        <!-- 粉煤灰1 -->
        <!-- <span class="separate" slot="ash1" slot-scope="text,record">
          <div>{{record.realityFlyAsh}}</div>
          <a-divider style="margin:0;height:1px" />
          <div>{{record.theoryFlyAsh}}</div>
          <a-divider style="margin:0;height:1px" />
          <div>{{record.flyAshErrorRate}}%</div>
        </span> -->
        <!-- 水泥2 -->
        <!-- <span class="separate" slot="cement2" slot-scope="text,record">
          <div>{{record.realityCementTwo}}</div>
          <a-divider style="margin:0;height:1px" />
          <div>{{record.theoryCementTwo	}}</div>
          <a-divider style="margin:0;height:1px" />
          <div>{{record.cementTwoErrorRate}}</div>
        </span> -->
        <!-- 粉煤灰2 -->
        <!-- <span class="separate" slot="ash2" slot-scope="text,record">
          <div>{{record.realityFlyAshTwo}}</div>
          <a-divider style="margin:0;height:1px" />
          <div>{{record.theoryFlyAshTwo}}</div>
          <a-divider style="margin:0;height:1px" />
          <div>{{record.flyAshTwoErrorRate}}</div>
        </span> -->
        <span slot="action" slot-scope="text, record">
          <a v-has="'ckqreal:edit'" @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical" v-has="'ckqreal:edit'" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item>
              <a-menu-item v-has="'ckqreal:del'">
                <a-popconfirm
                  title="确定删除吗?"
                  @confirm="() => handleDelete(record.id)"
                >
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>
      </a-table>
    </div>

    <cement-batching-mixer-modal
      ref="modalForm"
      @ok="modalFormOk"
    ></cement-batching-mixer-modal>
  </a-card>
</template>

<script>
import "@/assets/less/TableExpand.less";
import { mixinDevice } from "@/utils/mixin";
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import CementBatchingMixerModal from "./modules/CementBatchingMixerModal";
import JSuperQuery from "@/components/jeecg/JSuperQuery.vue";
import Vue from "vue";
import { SYS_DEPART_ORGCODE } from "@/store/mutation-types";
import { usershebeiList } from "@api/api";
export default {
  name: "CementBatchingMixer",
  mixins: [JeecgListMixin, mixinDevice],
  components: {
    CementBatchingMixerModal,
    JSuperQuery,
  },

  data() {
    return {
      selectValue: "",
      asyncSelectValue: "",
      dictOption: [],
      description: "水泥配料拌合机",
      // 表头
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
          title: "标段号",
          align: "center",
          dataIndex: "bidCode",
        },
        {
          title: "拌合机名称",
          align: "center",
          scopedSlots: { customRender: "mixinstation" },
        },
        // {
        //   title:'出料时间',
        //   align:"center",
        //   dataIndex: 'dischargeTime',
        //   key: 'dischargeTime'
        // },
        {
          title: "时间",
          align: "center",
          dataIndex: "uploadTime",
          key: "uploadTime",
        },
        {
          title: "水泥-当日(kg)",
          align: "center",
          dataIndex: "todayCement",
          key: "todayCement",
        },
        {
          title: "粉煤灰-当日(kg)",
          align: "center",
          dataIndex: "todayFlyAsh",
          key: "todayFlyAsh",
        },
        {
          title: "材料用量-当日(kg)",
          align: "center",
          dataIndex: "todayCementFlyAsh",
          key: "todayCementFlyAsh",
        },
        {
          title: "水泥-累计(kg)",
          align: "center",
          dataIndex: "totalCement",
          key: "totalCement",
        },
        {
          title: "粉煤灰-累计(kg)",
          align: "center",
          dataIndex: "totalFlyAsh",
          key: "totalFlyAsh",
        },
        {
          title: "材料用量-累计(kg)",
          align: "center",
          dataIndex: "totalCementFlyAsh",
          key: "totalCementFlyAsh",
        },
        // {
        //   title:'总量',
        //   align:"center",
        //   dataIndex:'totalWeight',
        // },
        // {
        //   title:'配合比',
        //   align:"center",
        //   scopedSlots: { customRender: 'proportion' },
        // },
        // {
        //   title:'水泥(kg)',
        //   align:"center",
        //   scopedSlots: { customRender: 'cement1' },
        // },
        // {
        //   title:'粉煤灰(kg)',
        //   align:"center",
        //   scopedSlots: { customRender: 'ash1' },
        // },
        // {
        //   title:'水泥2',
        //   align:"center",
        //   scopedSlots: { customRender: 'cement2' },
        // },
        // {
        //   title:'粉煤灰2',
        //   align:"center",
        //   scopedSlots: { customRender: 'ash2' },
        // },
        // {
        //   title: '操作',
        //   dataIndex: 'action',
        //   align:"center",
        //   fixed:"right",
        //   width:147,
        //   scopedSlots: { customRender: 'action' }
        // }
      ],
      url: {
        list: "/mixin/station/selectMixinStationDetailsListPage",
        // delete: "/ckqrealdata/deviceTrafficRealdata/delete",
        // deleteBatch: "/ckqrealdata/deviceTrafficRealdata/deleteBatch",
        // exportXlsUrl: "/ckqrealdata/deviceTrafficRealdata/exportXls",
        // importExcelUrl: "ckqrealdata/deviceTrafficRealdata/importExcel",
      },
      dictOptions: {},
      superFieldList: [],
    };
  },
  created() {
    // this.getSuperFieldList();
    this.shebeiList();
  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG["domianURL"]}/${this.url.importExcelUrl}`;
    },
  },
  methods: {
    shebeiList: function () {
      var sys_depart_orgcode = Vue.ls.get("SYS_DEPART_ORGCODE");
      usershebeiList({
        sys_depart_orgcode: sys_depart_orgcode,
        sbtypes: "36,37",
      }).then((res) => {
        this.dictOption = [];
        let result = res.result;
        result.forEach((re) => {
          this.dictOption.push({ text: re.sbname, value: re.sbjno });
        });
        // console.log(res)
      });
    },
    initDictConfig() {},
    // getSuperFieldList(){
    //   let fieldList=[];
    //   fieldList.push({type:'string',value:'mixinstation',text:'拌合机名称'})
    //   fieldList.push({type:'date',value:'datatime',text:'数据上传时间'})
    //   fieldList.push({type:'string',value:'pid',text:'设备编号'})
    //   fieldList.push({type:'string',value:'v',text:'设备编号'})
    //   fieldList.push({type:'int',value:'s',text:'s'})
    //   fieldList.push({type:'int',value:'ms',text:'ms'})
    //   fieldList.push({type:'int',value:'e',text:'e'})
    //   this.superFieldList = fieldList
    //   console.log(fieldList)
    // }
  },
};
</script>
<style scoped>
@import "~@assets/less/common.less";
/* >>>.ant-table-tbody > tr > td{
    padding: 0;
  } */
.j-table-force-nowrap
  >>> .ant-table-middle
  > .ant-table-content
  > .ant-table-scroll
  > .ant-table-body
  > table
  > .ant-table-tbody
  > tr
  > td {
  padding: 0;
}
.separate div {
  /* flex: 1; */
  height: 30px;
  line-height: 30px;

  /* border-bottom: 1px solid #999999; */
}
</style>
