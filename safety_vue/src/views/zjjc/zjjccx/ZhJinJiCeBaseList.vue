<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="设备名称">
              <j-search-select-tag placeholder="请选择设备名称" v-model="queryParam.shebeino" :dictOptions="dictOption" >
              </j-search-select-tag>
              {{ selectValue }}
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="评级">
              <j-search-select-tag placeholder="请选择评级" v-model="queryParam.level" :dictOptions="dictOption1"/>
            </a-form-item>
          </a-col>
          <a-col :xl="8" :lg="7" :md="8" :sm="24">
            <a-form-item label="施工部位">
              <a-input placeholder="请输入施工部位" v-model="queryParam.sgbw"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="检测日期范围">
              <j-date placeholder="开始检测日期" v-model="queryParam.ceshitime_begin" :showTime="true"
                      dateFormat="YYYY-MM-DD"/>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="">
              <j-date placeholder="结束检测日期" v-model="queryParam.ceshitime_end" :showTime="true"
                      dateFormat="YYYY-MM-DD"/>
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
      <a-button @click="handleAdd" v-has="'zjjc:add'" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" v-has="'zjjc:dc'" icon="download" @click="handleExportXls('桩基检测')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" v-has="'zjjc:dr'" icon="import">导入</a-button>
      </a-upload>
      <!-- 禁用高级查询区域 -->
      <!--      <j-super-query :fieldList="superFieldList" ref="superQueryModal" @handleSuperQuery="handleSuperQuery"></j-super-query>-->
      <!--      <a-dropdown v-if="selectedRowKeys.length > 0">-->
      <!--        <a-menu slot="overlay">-->
      <!--          <a-menu-item key="1" v-has="'hntbhz:del'" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>-->
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
        bordered
        rowKey="id"
        class="j-table-force-nowrap"
        :scroll="{x:true}"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        @change="handleTableChange"
        :customRow="handleClick"
        :rowClassName="setRowClassName"
      >
       <span slot="shizhuangleixing" slot-scope="shizhuangleixing, record">
        <a-tag color="green" v-if="record.shizhuangleixing == '20'">圆桩</a-tag>
        <a-tag color="orange" v-if="record.shizhuangleixing == '21'">方桩</a-tag>
        <a-tag color="yellow" v-if="record.shizhuangleixing == '22'">地下连续墙</a-tag>
       </span>

        <span slot="level" slot-scope="level, record">
          <a-tag v-if="record.level === 0">未评级</a-tag>
          <a-tag v-if="record.level === 1">Ⅰ类</a-tag>
          <a-tag v-if="record.level === 2">Ⅱ类</a-tag>
          <a-tag v-if="record.level === 3">Ⅲ类</a-tag>
        </span>

        <span slot="shizhuangno" slot-scope="shizhuangno, record">
        <a-tag color="green" >{{record.shizhuangno}}</a-tag>
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

        <span slot="action"  slot-scope="text, record">
          <a-dropdown>
            <a @click="handlePreview(record)">报告预览</a>
          </a-dropdown>
          <a v-has="'zjjc:edit'" @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item>
              <a-menu-item>
                <a @click="handleFile(record.id)">报告附件上传</a>
              </a-menu-item>
              <a-menu-item v-has="'zjjc:del'">
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a >删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>
    <ZhJinJiCeBaseModal ref="modalForm" @ok="modalFormOk"/>
    <zh-jin-ce-file ref="file" :id="id" @change="change"></zh-jin-ce-file>
  </a-card>
</template>

<script>

import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import ZhJinJiCeBaseModal from './modules/ZhJinJiCeBaseModal'
import {filterMultiDictText} from '@/components/dict/JDictSelectUtil'
import '@/assets/less/TableExpand.less'
import JSuperQuery from '@/components/jeecg/JSuperQuery.vue'
import { handertoken } from '@/mixins/getHanderToken'
import { mixinDevice } from '@/utils/mixin'
import Vue from 'vue'
import { SYS_DEPART_ORGCODE } from '@/store/mutation-types'
import { usershebeiList } from '@api/api'
import { base64Encode } from '@api/kkfileView'
import ZhJinCeFile from '@views/zjjc/zjjccx/modules/ZhJinCeFile'

export default {
  name: "ZhJinJiCeBaseList",
  mixins:[JeecgListMixin,mixinDevice,handertoken],
  components: {
    ZhJinCeFile,
    ZhJinJiCeBaseModal,
    JSuperQuery,
  },
  data () {
    return {
      id: 0,
      selectValue: '',
      asyncSelectValue: '',
      dictOption: [],
      dictOption1: [
        {
          text:'未评级',
          value:'0'
        },
        {
        text:'Ⅰ级',
        value:'1'
      },{
        text:'Ⅱ级',
        value:'2'
      },{
        text:'Ⅲ级',
        value:'3'
      }],
      description: '桩基主表管理页面',
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
          title:'所属分部',
          align:"center",
          dataIndex: 'bumen'
        },
        {
          title:'设备编号',
          align:"center",
          dataIndex: 'shebeino_dictText'
        },
        {
          title:'工程名称',
          align:"center",
          dataIndex: 'projectName'
        },
        {
          title:'检测单位',
          align:"center",
          dataIndex: 'jcdw'
        },
        {
          title:'受检单位',
          align:"center",
          dataIndex: 'sgdw'
        },
        {
          title:'施工部位',
          align:"center",
          dataIndex: 'sgbw'
        },
        {
          title:'任务单号 ',
          align:"center",
          dataIndex: 'liushuihao'
        },
        {
          title:'试桩类型',
          align:"center",
          dataIndex: 'shizhuangleixing',
          scopedSlots: { customRender: 'shizhuangleixing' },
        },
        {
          title:'检测日期',
          align:"center",
          dataIndex: 'ceshitime'
        },
        {
          title:'上传时间',
          align:"center",
          dataIndex: 'dangqiantime'
        },
        {
          title:'试桩编号',
          align:"center",
          dataIndex: 'shizhuangno',
          scopedSlots: { customRender: 'shizhuangno' },
        },
        {
          title:'桩径',
          align:"center",
          dataIndex: 'zhuangjing',
        },
        {
          title:'桩长 ',
          align:"center",
          dataIndex: 'zhuangchang'
        },
        {
          title:'管数',
          align:"center",
          dataIndex: 'guanshu'
        },
        {
          title:'剖面数',
          align:"center",
          dataIndex: 'poumianshu'
        },

        {
          title:'经度',
          align:"center",
          dataIndex: 'jingdu'
        },
        {
          title:'纬度 ',
          align:"center",
          dataIndex: 'weidu'
        },
        {
          title:'采样频率',
          align:"center",
          dataIndex: 'caiyangpinlv'
        },
        {
          title:'采样长度',
          align:"center",
          dataIndex: 'caiyanglength'
        },
        {
          title:'声测管内径',
          align:"center",
          dataIndex: 'shengceguanneijing'
        },
        {
          title:'声测管外径',
          align:"center",
          dataIndex: 'shengceguanwaijing'
        },
        {
          title:'方位角',
          align:"center",
          dataIndex: 'fangweijiao'
        },
        {
          title:'轮径 ',
          align:"center",
          dataIndex: 'lunjing'
        },
        {
          title:'线径 ',
          align:"center",
          dataIndex: 'xianjing'
        },
        {
          title: '评级',
          align: 'center',
          fixed: 'right',
          dataIndex: 'level',
          scopedSlots: { customRender: 'level' },
        },
        {
          title: '操作',
            dataIndex: 'action',
            align:"center",
            fixed:"right",
            width:147,
            scopedSlots: { customRender: 'action' },
        }
      ],
      url: {
        list: "/chaoshengbo/chaoshengboSyjbsj/list",
        delete: "/chaoshengbo/chaoshengboSyjbsj/delete",
        deleteBatch: "/chaoshengbo/chaoshengboSyjbsj/deleteBatch",
        exportXlsUrl: "/chaoshengbo/chaoshengboSyjbsj/exportXls",
        importExcelUrl: "chaoshengbo/chaoshengboSyjbsj/importExcel",

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
    }
  },
  methods: {
    shebeiList:function (){
      var sys_depart_orgcode=Vue.ls.get('SYS_DEPART_ORGCODE');
      usershebeiList({
        sys_depart_orgcode:sys_depart_orgcode,
        sbtypes:'14'
      }).then(res=>{
        this.dictOption=[];
        let result=res.result;
        result.forEach(re=>{
          this.dictOption.push({text:re.sbname,value:re.sbjno})
        })
        //console.log(res)
      })
    },
    handlePreview(record) {
      if (record && record.file) {
        let url = window._CONFIG['onlinePreviewDomainURL'] + '?url=' + encodeURIComponent(base64Encode(record.file))
        window.open(url, '_blank')
      }else{
        this.$message.info('未上传相关附件！')
      }
    },
    change() {
      this.modalFormOk()
    },
    handleFile: function (id) {
      this.$refs.file.showModal()
      this.id = id
      // this.routeReload();//刷新局部页面
    },
    initDictConfig(){
    },
    getSuperFieldList(){
      let fieldList=[];
      fieldList.push({type:'string',value:'batchNo',text:'唯一ID',dictCode:''})
      fieldList.push({type:'string',value:'shebeiNo',text:'设备编号',dictCode:''})
      fieldList.push({type:'string',value:'workNo',text:'工单号',dictCode:''})
      fieldList.push({type:'string',value:'handlers',text:'操作者',dictCode:''})
      fieldList.push({type:'string',value:'projectName',text:'工程名称',dictCode:''})
      fieldList.push({type:'string',value:'jobLocation',text:'施工地点',dictCode:''})
      fieldList.push({type:'string',value:'poureLocation',text:'浇筑部位',dictCode:''})
      fieldList.push({type:'string',value:'cementVariety',text:'水泥品种',dictCode:''})
      fieldList.push({type:'string',value:'additiveVariety',text:'外加剂品种',dictCode:''})
      fieldList.push({type:'string',value:'formulaNo',text:'配方号',dictCode:''})
      fieldList.push({type:'string',value:'strengthRank',text:'强度等级',dictCode:''})
      fieldList.push({type:'int',value:'stirDatetime',text:'搅拌时间',dictCode:''})
      fieldList.push({type:'date',value:'saveDatetime',text:'保存时间'})
      fieldList.push({type:'string',value:'clientNo',text:'客户端编号',dictCode:''})
      fieldList.push({type:'int',value:'status',text:'状态',dictCode:''})
      fieldList.push({type:'date',value:'collectDatetime',text:'采集时间'})
      fieldList.push({type:'double',value:'estimateNumber',text:'方量',dictCode:''})
      fieldList.push({type:'date',value:'productDatetime',text:'出料时间'})
      fieldList.push({type:'string',value:'slump',text:'坍落度',dictCode:''})
      fieldList.push({type:'int',value:'overLevel',text:'超标等级',dictCode:'over_level'})
      fieldList.push({type:'int',value:'alertstate',text:'是否超标',dictCode:''})
      fieldList.push({type:'string',value:'formulaId',text:'配方uuid(车结束符)',dictCode:''})
      fieldList.push({type:'int',value:'timeOverLevel',text:'搅拌时间超标等级',dictCode:''})
      this.superFieldList = fieldList
    }
  }
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>