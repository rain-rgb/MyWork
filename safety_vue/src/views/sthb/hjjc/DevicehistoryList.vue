<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="设备名称">
              <j-search-select-tag placeholder="请选择设备名称" v-model="queryParam.devaddr" :dictOptions="dictOption" >
              </j-search-select-tag>
              {{ selectValue }}
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="时间范围">
              <j-date placeholder="开始保存时间" v-model="queryParam.timevalue_begin" :showTime="true" dateFormat="YYYY-MM-DD HH:mm:ss" />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="">
              <j-date placeholder="结束保存时间" v-model="queryParam.timevalue_end" :showTime="true" dateFormat="YYYY-MM-DD HH:mm:ss" />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <a-button type="primary" @click="handle" icon="profile" style="margin-left: 8px">处置</a-button>
              <a @click="handleToggleSearch" style="margin-left: 8px">
                {{ toggleSearchStatus ? '收起' : '展开' }}
                <a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>
              </a>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button v-has="'dev:add'" @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" v-has="'dev:dc'" icon="download" @click="handleExportXls('环境监测历史记录')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" v-has="'dev:dr'" icon="import">导入</a-button>
      </a-upload>
<!--      &lt;!&ndash; 高级查询区域 &ndash;&gt;-->
<!--      <j-super-query :fieldList="superFieldList" ref="superQueryModal" @handleSuperQuery="handleSuperQuery"></j-super-query>-->
<!--      <a-dropdown v-if="selectedRowKeys.length > 0">-->
<!--        <a-menu slot="overlay">-->
<!--          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>-->
<!--        </a-menu>-->
<!--        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>-->
<!--      </a-dropdown>-->
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
        @change="handleTableChange"
        :customRow="handleClick"
        :rowClassName="setRowClassName"
      >

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
          <a v-has="'dev:edit'" @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item>
              <a-menu-item v-has="'dev:del'" >
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <devicehistory-modal ref="modalForm" @ok="modalFormOk"></devicehistory-modal>
    <chuzhi ref="modalChuZhi" :id="chuzhiId" :shebeiNo="shebeiNo" ></chuzhi>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import chuzhi from '../../lmd/modules/chuzhi'
  import DevicehistoryModal from './modules/DevicehistoryModal'
  import JSuperQuery from '@/components/jeecg/JSuperQuery.vue'
  import { handertoken } from '@/mixins/getHanderToken'
  import Vue from 'vue'
  import { SYS_DEPART_ORGCODE } from '@/store/mutation-types'
  import { usershebeiList } from '@api/api'

  export default {
    name: 'DevicehistoryList',
    mixins:[JeecgListMixin, mixinDevice,handertoken],
    components: {
      DevicehistoryModal,
      JSuperQuery,
      chuzhi
    },
    data () {
      return {
        selectValue: '',
        asyncSelectValue: '',
        chuzhiId: '',
        shebeiNo: '',
        dictOption: [],
        description: '环境监测历史记录管理页面',
        // 表头
        columns: [
          {
            title: '序号',
            dataIndex: '',
            key:'rowIndex',
            width:60,
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
          },
          {
            title:'设备名称',
            align:"center",
            dataIndex: 'devaddr_dictText'
          },
          {
            title:'PM10(ug/m³)',
            align:"center",
            dataIndex: 'pm10'
          },
          {
            title:'PM2.5(ug/m³)',
            align:"center",
            dataIndex: 'pm25'
          },
          {
            title:'噪声(dB)',
            align:"center",
            dataIndex: 'noise'
          },
          {
            title:'温度(℃)',
            align:"center",
            dataIndex: 'temperature'
          },
          {
            title:'湿度(%)',
            align:"center",
            dataIndex: 'humidity'
          },
          {
            title:'风力(级)',
            align:"center",
            dataIndex: 'wind'
          },
          {
            title:'风速(m/s)',
            align:"center",
            dataIndex: 'windspeed'
          },
          {
            title:'风向(方向)',
            align:"center",
            dataIndex: 'winddirection'
          },
          {
            title:'tsp(ug/m3)',
            align:"center",
            dataIndex: 'tsp'
          },
          {
            title:'大气压(Kpa)',
            align:"center",
            dataIndex: 'atmpressure'
          },
          {
            title:'数据保存的时间点',
            align:"center",
            dataIndex: 'timevalue',
            /*customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }*/
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
          list: "/devicehistory/devicehistory/list",
          delete: "/devicehistory/devicehistory/delete",
          deleteBatch: "/devicehistory/devicehistory/deleteBatch",
          exportXlsUrl: "/devicehistory/devicehistory/exportXls",
          importExcelUrl: "devicehistory/devicehistory/importExcel",

        },
        dictOptions:{},
        superFieldList:[],
      }
    },
    created() {
      this.getToken();
      this.getSuperFieldList();
      this.shebeiList();
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      },
    },
    methods: {
      shebeiList:function (){
        var sys_depart_orgcode=Vue.ls.get('SYS_DEPART_ORGCODE');
        usershebeiList({
          sys_depart_orgcode:sys_depart_orgcode,
          sbtypes:'15'
        }).then(res=>{
          this.dictOption=[];
          let result=res.result;
          result.forEach(re=>{
            this.dictOption.push({text:re.sbname,value:re.sbjno})
          })
          //console.log(res)
        })
      },
      initDictConfig(){
      },
      getSuperFieldList(){
        let fieldList=[];
        fieldList.push({type:'int',value:'devaddr',text:'设备地址',dictCode:''})
        fieldList.push({type:'string',value:'pm10',text:'PM10(ug/m³)（最高200）',dictCode:''})
        fieldList.push({type:'string',value:'pm25',text:'PM2.5(ug/m³)（最高500）',dictCode:''})
        fieldList.push({type:'string',value:'noise',text:'噪声(dB)（最高不高于100）',dictCode:''})
        fieldList.push({type:'string',value:'temperature',text:'温度(℃)',dictCode:''})
        fieldList.push({type:'string',value:'humidity',text:'湿度(%RH)（最高100）',dictCode:''})
        fieldList.push({type:'string',value:'wind',text:'风力（最高18）',dictCode:''})
        fieldList.push({type:'string',value:'windspeed',text:'风速(m/s) （最高不大于100）',dictCode:''})
        fieldList.push({type:'string',value:'winddirection',text:'风向  （风向度最高360）',dictCode:''})
        fieldList.push({type:'date',value:'timevalue',text:'数据保存的时间点'})
        this.superFieldList = fieldList
      },
      handle(){
        if(this.selectionRows.length){
          console.log(this.selectionRows,"选中数据")
          let type = true
          this.selectionRows.forEach(item => {
            if(item.devaddr != this.selectionRows[0].devaddr){
              type = false
            }
          })
          if(!type){
            this.$message.warning("请选择相同设备！")
          }else{
            let strID = '';
            this.selectionRows.forEach(item => {
              strID += `${item.id},`
            });
            strID = strID.slice(0, -1)
            this.chuzhiId = strID
            this.shebeiNo = this.selectionRows[0].devaddr
            this.$refs.modalChuZhi.counts = this.selectionRows.length
            this.$refs.modalChuZhi.visible = true
          }
        }else{
          this.$message.warning("请选择一行或多行数据！")
        }
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>