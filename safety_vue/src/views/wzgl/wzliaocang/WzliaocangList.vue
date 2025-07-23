<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label=料仓名称>
              <a-input placeholder="请输入料仓名称" v-model="queryParam.name"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="材料类型">
              <j-dict-select-tag placeholder="请选择" v-model="queryParam.cailiaono" dictCode="cailiaono"></j-dict-select-tag>
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
      <a-button @click="handleAdd" v-has="'liaocang:add'" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" v-has="'liaocang:dc'" icon="download" @click="handleExportXls('料仓配置表')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" v-has="'liaocang:dr'" icon="import">导入</a-button>
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
          <a-tag color="green" v-if="record.liaocangStatus == '1'">进场中</a-tag>
          <a-tag color="orange" v-if="record.liaocangStatus == '2'">检验中</a-tag>
          <a-tag color="geekblue" v-if="record.liaocangStatus == '3'">合格</a-tag>
          <a-tag color="red" v-if="record.liaocangStatus == '4'">待检验</a-tag>
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

          <a v-has="'liaocang:edit'" @click="handleEdit(record)">编辑</a>
          <a-divider type="vertical" />
           <a v-if="record.liaoweino != null" v-has="'liaocang:lwbh'" @click="queryLw(record)">料位变化</a>
           <a-divider v-if="record.liaoweino != null"  v-has="'liaocang:lwbh'" type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item>
              <a-menu-item v-has="'liaocang:del'" >
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
               <a-menu-item v-has="'liaocang:posttoOpen'" >
                <a-popconfirm title="确定开门吗?" @confirm="() => handleCommond(record.liaoweino,'posttoOpen')">
                  <a>开门</a>
                </a-popconfirm>
              </a-menu-item>
              <a-menu-item v-has="'liaocang:posttoClose'" >
                <a-popconfirm title="确定关门吗?" @confirm="() => handleCommond(record.liaoweino,'posttoClose')">
                  <a>关门</a>
                </a-popconfirm>
              </a-menu-item>
              <a-menu-item v-has="'liaocang:posttoStd'" >
                <a @click="()=>{liaoweinojc=record.liaoweino;visible=true;}">校秤</a>
              </a-menu-item>
              <a-menu-item v-has="'liaocang:posttoZero'" >
                <a-popconfirm title="确定校零吗?" @confirm="() => handleCommond(record.liaoweino,'posttoZero')">
                  <a>校零</a>
                </a-popconfirm>
              </a-menu-item>
               <a-menu-item v-has="'liaocang:posttoStd'" >
                <a @click="()=>{liaoweinojc=record.liaoweino;visible=true;}">绑定卡号</a>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <j-modal
       title="校秤"
      :width="350"
      :visible="visible"
      @ok="handleOk"
      @cancel="handleCancel"
      cancelText="关闭"
    >
      <a-input placeholder="请输入校秤值" v-model="jiaocheng"></a-input>
    </j-modal>
    <wzliaocang-modal ref="modalForm" @ok="modalFormOk"></wzliaocang-modal>
    <WzliaocangChangeFormList ref="lwlist" > </WzliaocangChangeFormList>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import WzliaocangModal from './modules/WzliaocangModal'
  import WzliaocangChangeFormList from './modules/WzliaocangChangeFormList'
  import JSuperQuery from '@/components/jeecg/JSuperQuery.vue'
  import JselectDqDepart from '@comp/jeecgbiz/JselectDqDepart'
  import { handertoken } from '@/mixins/getHanderToken'
  import { postAction } from '@/api/manage'


  export default {
    name: 'WzliaocangList',
    mixins:[JeecgListMixin, mixinDevice,handertoken],
    components: {
      WzliaocangModal,
      JSuperQuery,
      JselectDqDepart,
      WzliaocangChangeFormList
    },
    data () {
      return {
        liaoweinojc:'',
        jiaocheng:'',
        visible:false,
        description: '料仓配置表管理页面',
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
            title:'料仓名称',
            align:"center",
            dataIndex: 'name'
          },
          {
            title:'所属部门',
            align:"center",
            dataIndex: 'sysOrgCode_dictText'
          },
          {
            title:'批次',
            align:"center",
            dataIndex: 'pici',
          },
          {
            title:'重量',
            align:"center",
            dataIndex: 'picizhong',
          },
          // {
          //   title:'唯一标识',
          //   align:"center",
          //   dataIndex: 'guid'
          // },
          // {
          //   title:'isdel',
          //   align:"center",
          //   dataIndex: 'isdel'
          // },
          // {
          //   title:'时间戳',
          //   align:"center",
          //   dataIndex: 'ts'
          // },
          // {
          //   title:'orgcode',
          //   align:"center",
          //   dataIndex: 'orgcode'
          // },
          {
            title:'材料类型',
            align:"center",
            dataIndex: 'cailiaono_dictText'
          },
          {
            title:'规格类型',
            align:"center",
            dataIndex: 'guigexinghao'
          },
          {
            title:'计量单位',
            align:"center",
            dataIndex: 'danwei_dictText'
          },
          {
            title:'试验编号',
            align:"center",
            dataIndex: 'bgbianhao',
          },
          {
            title:'料仓状态',
            align:"center",
            dataIndex: 'liaocangStatus_dictText',
            scopedSlots: {customRender: 'tags'},
          },
          {
            title:'料斗号',
            align:"center",
            dataIndex: 'cailiaoname'
          },
          {
            title:'生产厂家',
            align:"center",
            dataIndex: 'changjia',
          },
          {
            title:'进场日期',
            align:"center",
            dataIndex: 'jinchangTime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'检验日期',
            align:"center",
            dataIndex: 'jianyanTime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'当前库存',
            align:"center",
            dataIndex: 'kucun',
          },
          {
            title:'创建人',
            align:"center",
            dataIndex: 'createBy_dictText'
          },
          {
            title:'创建日期',
            align:"center",
            dataIndex: 'createTime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'更新人',
            align:"center",
            dataIndex: 'updateBy_dictText'
          },
          {
            title:'更新日期',
            align:"center",
            dataIndex: 'updateTime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'料位设备编码',
            align:"center",
            dataIndex: 'liaoweino'
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
          list: "/wzliaocang/wzliaocang/list",
          delete: "/wzliaocang/wzliaocang/delete",
          deleteBatch: "/wzliaocang/wzliaocang/deleteBatch",
          exportXlsUrl: "/wzliaocang/wzliaocang/exportXls",
          importExcelUrl: "wzliaocang/wzliaocang/importExcel",
          lwcommond:"/wzliaocang/wzliaocang/lw"

        },
        dictOptions:{},
        superFieldList:[],
      }
    },
    created() {
      this.getSuperFieldList();
      this.getToken();
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      },
    },
    methods: {

      queryLw: function (record) {
        console.log(record)
        this.$refs.lwlist.edit(record);
      },

      handleCommond(liaoweino,commond){


        let param = {liaoweino:liaoweino,danwei:commond}

        postAction(this.url.lwcommond, param).then(res => {
          if(res.code === 200 ){
            this.$message.success(res.message)
            this.visible = false
            this.jiaocheng = ''
          }else{
            this.$message.error("请求失败")
          }

        })
        console.log(result)
      },
      handleCancel() {
        this.visible = false
      },
      handleOk() {
        let liaoweino = ''
        if(this.jiaocheng !== ''){
          liaoweino = this.liaoweinojc + '|'+this.jiaocheng
        }
        this.handleCommond(liaoweino,"posttoStd")

      },

      initDictConfig(){
      },
      getSuperFieldList(){
        let fieldList=[];
        fieldList.push({type:'string',value:'name',text:'料仓名称',dictCode:''})
        fieldList.push({type:'string',value:'sysOrgCode',text:'所属部门',dictCode:''})
        fieldList.push({type:'string',value:'guid',text:'唯一标识',dictCode:''})
        fieldList.push({type:'int',value:'isdel',text:'isdel',dictCode:''})
        fieldList.push({type:'int',value:'ts',text:'时间戳',dictCode:''})
        fieldList.push({type:'string',value:'orgcode',text:'orgcode',dictCode:''})
        fieldList.push({type:'string',value:'cailiaono',text:'cailiaono',dictCode:''})
        fieldList.push({type:'string',value:'danwei',text:'计量单位',dictCode:''})
        fieldList.push({type:'string',value:'createBy',text:'创建人',dictCode:''})
        fieldList.push({type:'date',value:'createTime',text:'创建日期'})
        fieldList.push({type:'string',value:'updateBy',text:'更新人',dictCode:''})
        fieldList.push({type:'date',value:'updateTime',text:'更新日期'})
        fieldList.push({type:'string',value:'liaocangStatus',text:'料仓状态',dictCode:''})
        fieldList.push({type:'date',value:'jianyanTime',text:'检验日期'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>