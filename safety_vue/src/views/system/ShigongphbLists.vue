<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="设备名称">
              <j-search-select-tag placeholder="请选择设备名称" v-model="queryParam.shebeibianhao" :dictOptions="dictOption" >
              </j-search-select-tag>
              {{ selectValue }}
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="配合比编号">
              <a-input placeholder="请输入配合比编号" v-model="queryParam.code"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="制定时间范围">
              <j-date placeholder="开始制定日期" v-model="queryParam.dattim_begin" :showTime="true"
                      dateFormat="YYYY-MM-DD HH:mm:ss"/>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="">
              <j-date placeholder="结束制定日期" v-model="queryParam.dattim_end" :showTime="true"
                      dateFormat="YYYY-MM-DD HH:mm:ss"/>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
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
      <a-button @click="handleAdd" type="primary" v-has="'shigongphbs:add'" icon="plus">新增配料单</a-button>
      <a-button @click="printXls" v-has="'shigongphb:print'" type="primary" icon="printer">打印</a-button>
      <a-button type="primary" v-has="'shigongphb:dc'" icon="download" @click="handleExportXls('施工配合比')">导出</a-button>
      <a-upload name="file" v-has="'shigongphb:dr'" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>
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
       <span slot="code" slot-scope="code, record">
        <a-tag color="green" >{{record.code}}</a-tag>
       </span>

        <span slot="iszjzl" slot-scope="iszjzl, record">
           <a-tag color="grey" v-if="record.iszjzl==0">未推送</a-tag>
          <a-tag color="green" v-if="record.iszjzl==1">推送成功</a-tag>
          <a-tag color="red" v-if="record.iszjzl==2">推送失败</a-tag>
          <a-tag color="yellow" v-if="record.iszjzl==3">未关联浇筑令</a-tag>
        </span>


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
          <a v-has="'shigongphb:edit'" @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item>
               <a-menu-item>
                <a @click="handleEditAdd(record)">复制录入</a>
              </a-menu-item>
              <a-menu-item v-has="'shigongphb:del'">
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <shigongphb-modals ref="modalForm" @ok="modalFormOk"></shigongphb-modals>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import ShigongphbModals from './modules/ShigongphbModals'
  import JSuperQuery from '@/components/jeecg/JSuperQuery.vue'
  import { SYS_DEPART_ORGCODE } from '@/store/mutation-types'
  import { usershebeiList } from '@api/api'
  import Vue from 'vue'
  export default {
    name: 'ShigongphbList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      ShigongphbModals,
      JSuperQuery,
    },
    data () {
      return {
        selectValue: '',
        dictOption: [],
        description: '施工配合比管理页面',
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
            dataIndex: 'shebeibianhao_dictText'
          },
          {
            title:'生产线',
            align:"center",
            dataIndex: 'station_dictText'
          },
          {
            title:'强度等级',
            align:"center",
            dataIndex: 'betlev'
          },
          {
            title:'配料单号',
            align:"center",
            dataIndex: 'code',
            scopedSlots: { customRender: 'code' }

          },
          {
            title:'理论配合比号',
            align:"center",
            dataIndex: 'code1'
          },
          {
            title:'任务单号',
            align:"center",
            dataIndex: 'renwudan'
          },
          {
            title:'制梁任务单号',
            align:"center",
            dataIndex: 'zhiliangcode'
          },
          // {
          //   title:'砼标记',
          //   align:"center",
          //   dataIndex: 'tag'
          // },

          {
            title:'抗渗等级',
            align:"center",
            dataIndex: 'filter'
          },
          {
            title:'抗冻等级',
            align:"center",
            dataIndex: 'freeze'
          },
          // {
          //   title:'施工季节',
          //   align:"center",
          //   dataIndex: 'season'
          // },
          {
            title:'坍落度',
            align:"center",
            dataIndex: 'lands'
          },
          {
            title:'搅拌时间',
            align:"center",
            dataIndex: 'mixlast'
          },
          // {
          //   title:'设计比例',
          //   align:"center",
          //   dataIndex: 'scale'
          // },
          {
            title:'试验员',
            align:"center",
            dataIndex: 'exper'
          },
          {
            title:'审核员',
            align:"center",
            dataIndex: 'assoss'
          },
          {
            title:'制定日期',
            align:"center",
            dataIndex: 'dattim'
          },

          {
            title:'创建人',
            align:"center",
            dataIndex: 'createBy'
          },
          {
            title:'推送质保资料',
            align:"center",
            dataIndex: 'iszjzl',
            key: 'iszjzl',
            scopedSlots: { customRender: 'iszjzl' },
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
          list: "/system/shigongphb/lists",
          delete: "/system/shigongphb/delete",
          deleteBatch: "/system/shigongphb/deleteBatch",
          exportXlsUrl: "/system/shigongphb/exportXls",
          importExcelUrl: "system/shigongphb/importExcel",
          printUrl:'jmreport/view/585690650923728896',


        },
        dictOptions:{},
        superFieldList:[],
      }
    },
    created() {
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
          sbtypes:'0'
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
        fieldList.push({type:'int',value:'station',text:'生产线',dictCode:''})
        fieldList.push({type:'string',value:'code',text:'配合比号',dictCode:''})
        fieldList.push({type:'string',value:'tag',text:'砼标记',dictCode:''})
        fieldList.push({type:'string',value:'betlev',text:'强度等级',dictCode:''})
        fieldList.push({type:'string',value:'filter',text:'抗渗等级',dictCode:''})
        fieldList.push({type:'string',value:'freeze',text:'抗冻等级',dictCode:''})
        fieldList.push({type:'string',value:'season',text:'施工季节',dictCode:''})
        fieldList.push({type:'string',value:'lands',text:'坍落度',dictCode:''})
        fieldList.push({type:'int',value:'mixlast',text:'搅拌时间',dictCode:''})
        fieldList.push({type:'string',value:'scale',text:'设计比例',dictCode:''})
        fieldList.push({type:'string',value:'otherreq',text:'其他要求',dictCode:''})
        fieldList.push({type:'string',value:'exper',text:'试验员',dictCode:''})
        fieldList.push({type:'string',value:'assoss',text:'审核员',dictCode:''})
        fieldList.push({type:'date',value:'dattim',text:'制定日期'})
        fieldList.push({type:'string',value:'flag',text:'标识',dictCode:''})
        fieldList.push({type:'string',value:'note',text:'备注',dictCode:''})
        fieldList.push({type:'string',value:'m1',text:'原材料1',dictCode:''})
        fieldList.push({type:'double',value:'u1',text:'用量1',dictCode:''})
        fieldList.push({type:'string',value:'m2',text:'原材料2',dictCode:''})
        fieldList.push({type:'double',value:'u2',text:'用量2',dictCode:''})
        fieldList.push({type:'string',value:'m3',text:'原材料3',dictCode:''})
        fieldList.push({type:'double',value:'u3',text:'用量3',dictCode:''})
        fieldList.push({type:'string',value:'m4',text:'原材料4',dictCode:''})
        fieldList.push({type:'double',value:'u4',text:'用量4',dictCode:''})
        fieldList.push({type:'string',value:'m5',text:'原材料5',dictCode:''})
        fieldList.push({type:'double',value:'u5',text:'用量5',dictCode:''})
        fieldList.push({type:'string',value:'m6',text:'原材料6',dictCode:''})
        fieldList.push({type:'double',value:'u6',text:'用量6',dictCode:''})
        fieldList.push({type:'string',value:'m7',text:'原材料7',dictCode:''})
        fieldList.push({type:'double',value:'u7',text:'用量7',dictCode:''})
        fieldList.push({type:'string',value:'m8',text:'原材料8',dictCode:''})
        fieldList.push({type:'double',value:'u8',text:'用量8',dictCode:''})
        fieldList.push({type:'string',value:'m9',text:'原材料9',dictCode:''})
        fieldList.push({type:'double',value:'u9',text:'用量9',dictCode:''})
        fieldList.push({type:'string',value:'m10',text:'原材料10',dictCode:''})
        fieldList.push({type:'double',value:'u10',text:'用量10',dictCode:''})
        fieldList.push({type:'string',value:'m11',text:'原材料11',dictCode:''})
        fieldList.push({type:'double',value:'u11',text:'用量11',dictCode:''})
        fieldList.push({type:'string',value:'m12',text:'原材料12',dictCode:''})
        fieldList.push({type:'double',value:'u12',text:'用量12',dictCode:''})
        fieldList.push({type:'string',value:'m13',text:'原材料13',dictCode:''})
        fieldList.push({type:'double',value:'u13',text:'用量13',dictCode:''})
        fieldList.push({type:'string',value:'m14',text:'原材料14',dictCode:''})
        fieldList.push({type:'double',value:'u14',text:'用量14',dictCode:''})
        fieldList.push({type:'string',value:'m15',text:'原材料15',dictCode:''})
        fieldList.push({type:'double',value:'u15',text:'用量15',dictCode:''})
        fieldList.push({type:'string',value:'m16',text:'原材料16',dictCode:''})
        fieldList.push({type:'double',value:'u16',text:'用量16',dictCode:''})
        fieldList.push({type:'string',value:'m17',text:'原材料17',dictCode:''})
        fieldList.push({type:'double',value:'u17',text:'用量17',dictCode:''})
        fieldList.push({type:'string',value:'m18',text:'原材料18',dictCode:''})
        fieldList.push({type:'double',value:'u18',text:'用量18',dictCode:''})
        fieldList.push({type:'string',value:'m19',text:'原材料19',dictCode:''})
        fieldList.push({type:'double',value:'u19',text:'用量19',dictCode:''})
        fieldList.push({type:'string',value:'m20',text:'原材料20',dictCode:''})
        fieldList.push({type:'double',value:'u20',text:'用量20',dictCode:''})
        fieldList.push({type:'string',value:'m21',text:'原材料21',dictCode:''})
        fieldList.push({type:'double',value:'u21',text:'用量21',dictCode:''})
        fieldList.push({type:'string',value:'m22',text:'原材料22',dictCode:''})
        fieldList.push({type:'double',value:'u22',text:'用量22',dictCode:''})
        fieldList.push({type:'string',value:'m23',text:'原材料23',dictCode:''})
        fieldList.push({type:'double',value:'u23',text:'用量23',dictCode:''})
        fieldList.push({type:'string',value:'m24',text:'原材料24',dictCode:''})
        fieldList.push({type:'double',value:'u24',text:'用量24',dictCode:''})
        fieldList.push({type:'string',value:'m25',text:'原材料25',dictCode:''})
        fieldList.push({type:'double',value:'u25',text:'用量25',dictCode:''})
        fieldList.push({type:'string',value:'m26',text:'原材料26',dictCode:''})
        fieldList.push({type:'double',value:'u26',text:'用量26',dictCode:''})
        fieldList.push({type:'string',value:'m27',text:'原材料27',dictCode:''})
        fieldList.push({type:'double',value:'u27',text:'用量27',dictCode:''})
        fieldList.push({type:'string',value:'m28',text:'原材料28',dictCode:''})
        fieldList.push({type:'double',value:'u28',text:'用量28',dictCode:''})
        fieldList.push({type:'string',value:'m29',text:'原材料29',dictCode:''})
        fieldList.push({type:'double',value:'u29',text:'用量29',dictCode:''})
        fieldList.push({type:'string',value:'m30',text:'原材料30',dictCode:''})
        fieldList.push({type:'double',value:'u30',text:'用量30',dictCode:''})
        fieldList.push({type:'string',value:'m31',text:'原材料31',dictCode:''})
        fieldList.push({type:'double',value:'u31',text:'用量31',dictCode:''})
        fieldList.push({type:'string',value:'m32',text:'原材料32',dictCode:''})
        fieldList.push({type:'double',value:'u32',text:'用量32',dictCode:''})
        fieldList.push({type:'string',value:'shebeibianhao',text:'设备编号',dictCode:''})
        fieldList.push({type:'string',value:'sysOrgCode',text:'控制权限',dictCode:''})
        fieldList.push({type:'string',value:'createBy',text:'创建人',dictCode:''})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>