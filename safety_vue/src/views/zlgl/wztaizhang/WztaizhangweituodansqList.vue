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
            <a-form-item label="料仓名称">
              <j-search-select-tag placeholder="请选择料仓名称" v-model="queryParam.lcbianhao" :dictOptions="dictOption1" >
              </j-search-select-tag>
              {{ selectValue1 }}
            </a-form-item>
          </a-col>

          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="材料名称">
              <j-search-select-tag placeholder="请选择料仓名称" v-model="queryParam.cailiaono" :dictOptions="dictOption2" >
              </j-search-select-tag>
              {{ selectValue2 }}
            </a-form-item>
          </a-col>

          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="批次">
              <a-input placeholder="请输入批次" v-model="queryParam.pici"></a-input>
            </a-form-item>
          </a-col>

          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="时间范围">
              <j-date placeholder="开始时间" v-model="queryParam.createTime_begin" :showTime="true" dateFormat="YYYY-MM-DD HH:mm:ss" />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="">
              <j-date placeholder="结束时间" v-model="queryParam.createTime_end" :showTime="true" dateFormat="YYYY-MM-DD HH:mm:ss" />
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
<!--      <a-button @click="handleAdd" v-has="'weituodansq:add'" type="primary" icon="plus">新增</a-button>-->
      <a-button @click="handleAdd" v-has="'weituodanshenqing:add'" type="primary" icon="plus">委托单申请</a-button>
      <a-button type="primary" icon="download" v-has="'weituodansq:dc'" @click="handleExportXls('wztaizhang')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" v-has="'weituodansq:dr'" icon="import">导入</a-button>
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

        <span slot="tags" slot-scope="tags, record">
          <a-tag color="green" v-if="record.isquyang == '0'">未取样</a-tag>
          <a-tag color="orange" v-if="record.isquyang == '1'">已取样</a-tag>
        </span>

        <span slot="tags1" slot-scope="tags1, record">
          <a-tag color="green" v-if="record.issubmit == '0'">未提交</a-tag>
          <a-tag color="orange" v-if="record.issubmit == '1'">已提交</a-tag>
        </span>

        <span slot="tags2" slot-scope="tags1, record">
          <a-tag color="green" v-if="record.iscommission == '0'">未申请</a-tag>
          <a-tag color="orange" v-if="record.iscommission == '1'">已申请</a-tag>
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
          <a v-has="'wztaizhang:edit'" @click="handleEdit(record)">编辑</a>

<!--          <a-divider type="vertical" />-->
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item>
              <a-menu-item v-has="'wztaizhang:del'" >
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <wztaizhangweituodansq-modal ref="modalForm" @ok="modalFormOk"></wztaizhangweituodansq-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import JSuperQuery from '@/components/jeecg/JSuperQuery.vue'
  import { usershebeiList } from '@api/api'
  import { userliaocangList } from '@api/api'
  import { usercailiaoList } from '@api/api'
  import Vue from 'vue'
  import WztaizhangweituodansqModal from '@views/zlgl/wztaizhang/modules/WztaizhangweituodansqModal'

  export default {
    name: 'WztaizhangweituodansqList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      WztaizhangweituodansqModal,
      JSuperQuery,
    },
    data () {
      return {
        selectValue2:'',
        selectValue1:'',
        selectValue: '',
        asyncSelectValue: '',
        dictOption: [],
        dictOption1:[],
        dictOption2:[],
        selectedRowKeys:[],
        selectionRows:[],
        description: 'wztaizhang管理页面',
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
            title:'进场时间',
            align:"center",
            dataIndex: 'jinchangshijian'
          },
          {
            title:'设备名称',
            align:"center",
            dataIndex: 'shebeibianhao_dictText'
          },
          {
            title:'料仓名称',
            align:"center",
            dataIndex: 'lcbianhao_dictText'
          },
          {
            title:'材料名称',
            align:"center",
            dataIndex: 'cailiaono_dictText'
          },
          {
            title:'净重(吨)',
            align:"center",
            dataIndex: 'jingzhongt'
          },
          {
            title:'检验批次',
            align:"center",
            dataIndex: 'pici'
          },
          {
            title:'规格类型',
            align:"center",
            dataIndex: 'guigexinghao'
          },
          {
            title:'是否已取样',
            align:"center",
            dataIndex: 'isquyang',
            scopedSlots: { customRender: 'tags' },
          },
          {
            title:'是否提交',
            align:"center",
            dataIndex: 'issubmit',
            scopedSlots: { customRender: 'tags1' },
          },
          {
            title:'是否申请委托单',
            align:"center",
            dataIndex: 'iscommission',
            scopedSlots: { customRender: 'tags2' },
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
          list: "/wztaizhang/wztaizhang/list2",
          delete: "/wztaizhang/wztaizhang/delete",
          deleteBatch: "/wztaizhang/wztaizhang/deleteBatch",
          exportXlsUrl: "/wztaizhang/wztaizhang/exportXls",
          importExcelUrl: "wztaizhang/wztaizhang/importExcel",

        },
        dictOptions:{},
        superFieldList:[],
      }
    },
    created() {
      this.getSuperFieldList();
      this.shebeiList();
      this.liaocangList();
      this.cailiaoList();
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      },
    },
    methods: {
      onSelectChange(selectedRowKeys, selectionRows) {
        this.selectedRowKeys = selectedRowKeys;
        this.selectionRows = selectionRows;
        console.log("selectedRowKeys",this.selectedRowKeys)
        console.log("selectionRows",this.selectionRows)
      },
      handleAdd: function () {
        if(this.selectedRowKeys.length!==1){
          let param = this.getQueryParams();
          this.$message.error("请选择一条检验批数据")
        }else if(this.selectedRowKeys.length==1){
          let param = this.getQueryParams();
          // param['selections'] = this.selectedRowKeys.join(",")
          param['selections'] = this.selectionRows[0]
          this.$refs.modalForm.add(param);
          this.$refs.modalForm.title = "新增";
          this.$refs.modalForm.disableSubmit = false;
        }
      },
      shebeiList:function (){
        var sys_depart_orgcode=Vue.ls.get('SYS_DEPART_ORGCODE');
        usershebeiList({
          sys_depart_orgcode:sys_depart_orgcode,
          sbtypes:'27'
        }).then(res=>{
          this.dictOption=[];
          let result=res.result;
          result.forEach(re=>{
            this.dictOption.push({text:re.sbname,value:re.sbjno})
          })
          //console.log(res)
        })
      },
      liaocangList:function (){
        var sys_depart_orgcode=Vue.ls.get('SYS_DEPART_ORGCODE');
        userliaocangList({
          sys_depart_orgcode:sys_depart_orgcode,
        }).then(res=>{
          this.dictOption1=[];
          let result=res.result;
          result.forEach(re=>{
            this.dictOption1.push({text:re.name,value:re.guid})
          })
          //console.log(res)
        })
      },
      cailiaoList:function (){
        var sys_depart_orgcode=Vue.ls.get('SYS_DEPART_ORGCODE');
        usercailiaoList({
          sys_depart_orgcode:sys_depart_orgcode,
        }).then(res=>{
          this.dictOption2=[];
          let result=res.result;
          result.forEach(re=>{
            this.dictOption2.push({text:re.cailiaoname,value:re.cailiaono})
          })
          // console.log(res)
        })
      },
      initDictConfig(){
      },
      getSuperFieldList(){
        let fieldList=[];
        fieldList.push({type:'string',value:'jinchangshijian',text:'进场时间'})
        fieldList.push({type:'string',value:'shebeibianhao',text:'设备编号'})
        fieldList.push({type:'string',value:'lcbianhao',text:'料仓编号'})
        fieldList.push({type:'string',value:'cailiaono',text:'材料编号'})
        fieldList.push({type:'string',value:'maozhongt',text:'毛重(吨)'})
        fieldList.push({type:'string',value:'pizhongt',text:'皮重(吨)'})
        fieldList.push({type:'string',value:'jingzhongt',text:'净重(吨)'})
        fieldList.push({type:'string',value:'pici',text:'批次'})
        fieldList.push({type:'string',value:'guigexinghao',text:'规格类型'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>