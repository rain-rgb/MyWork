<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="5" :lg="7" :md="8" :sm="24">
<!--            <a-form-item label="设备名称">-->
<!--              <j-search-select-tag placeholder="请选择设备名称" v-model="queryParam.sbbh" :dictOptions="dictOption">-->
<!--              </j-search-select-tag>-->
<!--              {{ selectValue }}-->
<!--            </a-form-item>-->
            <a-input placeholder="请输入部位" v-model="queryParam.cjmc"></a-input>
          </a-col>
          <a-col :xl="5" :lg="7" :md="8" :sm="24">
            <a-form-item label="强度等级">
              <a-input placeholder="请输入强度等级" v-model="queryParam.SJQD"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="4" :lg="7" :md="8" :sm="24">
            <a-form-item label="龄期">
              <a-input placeholder="请输入龄期" v-model="queryParam.lq"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="5" :lg="7" :md="8" :sm="24">
            <a-form-item label="试验日期范围">
              <j-date placeholder="开始试验日期" v-model="queryParam.stadate" :showTime="true"
                      dateFormat="YYYY-MM-DD HH:mm:ss"/>
            </a-form-item>
          </a-col>
          <a-col :xl="5" :lg="7" :md="8" :sm="24">
            <a-form-item label="">
              <j-date placeholder="结束试验日期" v-model="queryParam.enddate" :showTime="true"
                      dateFormat="YYYY-MM-DD HH:mm:ss"/>
            </a-form-item>
          </a-col>
          <a-col :xl="5" :lg="7" :md="8" :sm="24">
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
<!--      <a-button @click="handleAdd" v-has="'syj:add'" type="primary" icon="plus">新增</a-button>-->
<!--      <a-button @click="print1" type="primary" icon="printer">打印</a-button>-->
<!--      <a-button type="primary" icon="download" v-has="'syj:dc'" @click="handleExportXls('试验机标准差')">导出</a-button>-->
<!--      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl"-->
<!--                @change="handleImportExcel">-->
<!--        <a-button type="primary" v-has="'syj:dr'" icon="import">导入</a-button>-->
<!--      </a-upload>-->
      <!-- 高级查询区域 -->
      <!--      <j-super-query :fieldList="superFieldList" ref="superQueryModal"-->
      <!--                     @handleSuperQuery="handleSuperQuery"></j-super-query>-->
      <!--      <a-dropdown v-if="selectedRowKeys.length > 0">-->
      <!--        <a-menu slot="overlay">-->
      <!--          <a-menu-item key="1" @click="batchDel">-->
      <!--            <a-icon type="delete"/>-->
      <!--            删除-->
      <!--          </a-menu-item>-->
      <!--        </a-menu>-->
      <!--        <a-button style="margin-left: 8px"> 批量操作-->
      <!--          <a-icon type="down"/>-->
      <!--        </a-button>-->
      <!--      </a-dropdown>-->
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a
        style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table
        ref="table"
        size="middle"
        bordered
        rowKey="syjid"
        class="j-table-force-nowrap"
        :scroll="{x:true}"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        @change="handleTableChange">
         <span slot="tags" slot-scope="tags, record">
          <a-tag color="green" v-if="record.pdjg == '合格'">合格</a-tag>
          <a-tag color="red" v-if="record.pdjg == '不合格'">不合格</a-tag>
           <a-tag color="green" v-if="record.pdjg == '有效'">合格</a-tag>
          <a-tag color="red" v-if="record.pdjg == '无效'">不合格</a-tag>
        </span>
        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <template slot="imgSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无图片</span>
          <img v-else :src="getImgView(text)" height="25px" alt=""
               style="max-width:80px;font-size: 12px;font-style: italic;"/>
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
          <a v-has="'syj:edit'" @click="handleEdit(record)">编辑</a>

          <!--          <a-divider type="vertical"/>-->
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down"/></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item>
              <a-menu-item>
                <a @click="handleEdit(record)">分部分项</a>
              </a-menu-item>
              <a-menu-item v-has="'syj:del'">
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <bhzrenwudan-modal ref="modalForm1" @ok="modalFormOk"></bhzrenwudan-modal>

  </a-card>
</template>

<script>

import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import JDictSelectTag from '@/components/dict/JDictSelectTag.vue'
import { filterMultiDictText } from '@/components/dict/JDictSelectUtil'
import '@/assets/less/TableExpand.less'
import JSuperQuery from '@/components/jeecg/JSuperQuery.vue'
import { handertoken } from '@/mixins/getHanderToken'
import Vue from 'vue'
import { SYS_DEPART_ORGCODE } from '@/store/mutation-types'
import { usershebeiList } from '@api/api'
import {filterDictTextByCache} from '@/components/dict/JDictSelectUtil'
import { getAction } from '@api/manage'

export default {
  name: 'SyyLjBasesList',
  mixins: [JeecgListMixin, handertoken],
  components: {
    JDictSelectTag,
    JSuperQuery,
  },
  data() {
    return {
      selectValue: '',
      asyncSelectValue: '',
      dictOption: [],
      description: '压力机主表管理页面',
      // 表头
      columns: [
        {
          title: '序号',
          dataIndex: '',
          key: 'rowIndex',
          width: 60,
          align: 'center',
          customRender: function (t, r, index) {
            return parseInt(index) + 1
          }
        },
        // {
        //   title: '设备名称',
        //   align: 'center',
        //   dataIndex: 'sbname',
        // },
        // {
        //   title: '标段',
        //   align: 'center',
        //   dataIndex: 'sbname',
        // },
        {
          title: '强度等级',
          align: 'center',
          dataIndex: 'sjqd'
        },
        {
          title: '龄期',
          align: 'center',
          dataIndex: 'lq'
        },
        {
          title: '数量',
          align: 'center',
          dataIndex: 'num'
        },
        {
          title: '平均值',
          align: "center",
          dataIndex: 'qddbzavg'
        },

        {
          title: '标准差',
          align: 'center',
          dataIndex: 'qddbzbzc'
        }
      ],
      url: {
        list: '/syj/tSyjzb/bzclist',
      },
      dictOptions: {},
      superFieldList: [],
    }
  },
  created() {
    this.getToken();
    this.getSuperFieldList();
    this.shebeiList();
    this.tongji();
  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`
    }
  },
  methods: {
    handleEdit:function(record){
      this.$refs.modalForm1.edit(record);
      this.$refs.modalForm1.title="编辑";
      this.$refs.modalForm1.disableSubmit = true;
    },
    print1:function(){//打印功能需要先去报表设计页面设计打印格式
      if(this.selectedRowKeys.length!==1){
        let param = this.getQueryParams();
        console.log(param,"打印信息")
        this.$message.error("请选择一条压力机数据进行打印")
      }else if(this.selectedRowKeys.length==1){//?paramsStr=${paramsStr}
        let param = this.getQueryParams();
        param['selections'] = this.selectedRowKeys.join(",")
        console.log(param,"打印信息")
        let url = `${window._CONFIG['domianURL']}/${this.url.printUrl}?syjid=${param.selections}&token=${this.token}`;
        window.open(url);
      }
    },
    shebeiList: function () {
      var sys_depart_orgcode = Vue.ls.get('SYS_DEPART_ORGCODE')
      usershebeiList({
        sys_depart_orgcode: sys_depart_orgcode,
        sbtypes: '4'
      }).then(res => {
        //console.log(res)
        this.dictOption = []
        let result = res.result
        result.forEach(re => {
          this.dictOption.push({ text: re.sbname, value: re.sbjno })
        })
      })
    },
    initDictConfig() {
    },
    getSuperFieldList() {
      let fieldList = []
      // fieldList.push({ type: 'int', value: 'id', text: 'id', dictCode: '' })
      fieldList.push({ type: 'string', value: 'syjid', text: '唯一码', dictCode: '' })
      fieldList.push({ type: 'string', value: 'wtid', text: '委托ID', dictCode: '' })
      fieldList.push({ type: 'string', value: 'sbbh', text: '设备编号', dictCode: '' })
      fieldList.push({ type: 'string', value: 'sylx', text: '试验类型', dictCode: '' })
      fieldList.push({ type: 'string', value: 'wtbh', text: '委托编号', dictCode: '' })
      fieldList.push({ type: 'string', value: 'sjbh', text: '试件编号', dictCode: '' })
      fieldList.push({ type: 'string', value: 'zzrq', text: '制件日期', dictCode: '' })
      fieldList.push({ type: 'string', value: 'syrq', text: '试验日期', dictCode: '' })
      fieldList.push({ type: 'string', value: 'sywcsj', text: '试验完成时间' })
      fieldList.push({ type: 'int', value: 'lq', text: '龄期', dictCode: '' })
      fieldList.push({ type: 'string', value: 'sjcc', text: '试件尺寸', dictCode: '' })
      fieldList.push({ type: 'string', value: 'sjmj', text: '试件面积', dictCode: '' })
      fieldList.push({ type: 'string', value: 'sjsl', text: '试件数量', dictCode: '' })
      fieldList.push({ type: 'string', value: 'sjqd', text: '设计强度', dictCode: '' })
      fieldList.push({ type: 'string', value: 'zsxs', text: '折算系数', dictCode: '' })
      fieldList.push({ type: 'string', value: 'qddbz', text: '强度代表值', dictCode: '' })
      fieldList.push({ type: 'string', value: 'pdjg', text: '判定结果', dictCode: '' })
      fieldList.push({ type: 'string', value: 'czry', text: '操作人员', dictCode: '' })
      fieldList.push({ type: 'string', value: 'cjmc', text: '工程名称', dictCode: '' })
      fieldList.push({ type: 'string', value: 'pzbm', text: '品种编码', dictCode: '' })
      fieldList.push({ type: 'string', value: 'gczj', text: '公称直径', dictCode: '' })
      fieldList.push({ type: 'string', value: 'area', text: '承压面积', dictCode: '' })
      fieldList.push({ type: 'string', value: 'fbl', text: '生产厂家', dictCode: '' })
      fieldList.push({ type: 'string', value: 'beizhu', text: '备注', dictCode: '' })
      fieldList.push({ type: 'date', value: 'sjscsj', text: '数据上传时间', dictCode: '' })
      this.superFieldList = fieldList
    }
  }
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>