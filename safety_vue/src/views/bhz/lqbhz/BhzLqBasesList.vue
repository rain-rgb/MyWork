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
            <a-form-item label="超标等级">
              <j-dict-select-tag placeholder="请选择超标等级" v-model="queryParam.chaobiaodengji"
                                 dictCode="over_level"></j-dict-select-tag>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="出料时间范围">
              <j-date placeholder="开始出料时间" v-model="queryParam.chuliaoshijian_begin" :showTime="true"
                      dateFormat="YYYY-MM-DD HH:mm:ss"/>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="">
              <j-date placeholder="结束出料时间" v-model="queryParam.chuliaoshijian_end" :showTime="true"
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
      <a-button @click="handlejipei" type="primary" icon="safety">级配</a-button>
      <a-button v-has="'lqbhz:add'" @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" v-has="'lqbhz:dc'" icon="download" @click="handleExportXls('沥青主表')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl"
                @change="handleImportExcel">
        <a-button type="primary" v-has="'lqbhz:dr'" icon="import">导入</a-button>
      </a-upload>
      <!-- 高级查询区域 -->
<!--      <j-super-query :fieldList="superFieldList" ref="superQueryModal"-->
<!--                     @handleSuperQuery="handleSuperQuery"></j-super-query>-->
<!--      <a-dropdown v-if="selectedRowKeys.length > 0">-->
<!--        <a-menu slot="overlay">-->
<!--          <a-menu-item key="1" @click="batchDel">-->
<!--            <a-icon type="delete" v-has="'lqbhz:del'"/>-->
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
        <span slot="tags" slot-scope="tags, record">
        <a-tag color="green" v-if="record.chaobiaodengji == '0'">合格</a-tag>
        <a-tag color="orange" v-if="record.chaobiaodengji == '1'">初级超标</a-tag>
        <a-tag color="yellow" v-if="record.chaobiaodengji == '2'">中级超标</a-tag>
         <a-tag color="red" v-if="record.chaobiaodengji == '3'">高级超标</a-tag>
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
          <a v-has="'lqbhz:edit'" @click="handleEdit(record)">编辑</a>

          <a-divider v-has="'lqbhz:edit'" type="vertical"/>
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down"/></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item>
              <a-menu-item v-has="'lqbhz:del'">
                <a-popconfirm  title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <bhz-lq-base-jipei-model ref="modalForm1"></bhz-lq-base-jipei-model>
    <bhz-lq-bases-modal ref="modalForm" @ok="modalFormOk"/>
  </a-card>
</template>

<script>

import {JeecgListMixin} from '@/mixins/JeecgListMixin'
import BhzLqBasesModal from './modules/BhzLqBasesModal'
import {filterMultiDictText} from '@/components/dict/JDictSelectUtil'
import '@/assets/less/TableExpand.less'
import JSuperQuery from '@/components/jeecg/JSuperQuery.vue'
import { handertoken } from '@/mixins/getHanderToken'
import Vue from 'vue'
import { SYS_DEPART_ORGCODE } from '@/store/mutation-types'
import { usershebeiList } from '@api/api'
import BhzLqBaseJipeiModel from '@views/bhz/lqbhz/modules/BhzLqBaseJipeiModel'


export default {
  name: "BhzLqBasesList",
  mixins: [JeecgListMixin,handertoken],
  components: {
    BhzLqBasesModal,
    JSuperQuery,
    BhzLqBaseJipeiModel
  },
  data() {
    return {
      selectValue: '',
      asyncSelectValue: '',
      dictOption: [],
      description: '沥青主表管理页面',
      // 表头
      columns: [
        {
          title: '序号',
          dataIndex: '',
          key: 'rowIndex',
          width: 60,
          align: "center",
          customRender: function (t, r, index) {
            return parseInt(index) + 1;
          }
        },
        // {
        //   title: '所属组织机构',
        //   align: "center",
        //   dataIndex: 'sysOrgCode_dictText'
        // },
        {
          title: '设备名称',
          align: "center",
          dataIndex: 'shebeibianhao_dictText'
        },
        {
          title: '工程名称',
          align: "center",
          dataIndex: 'projectName'
        },
        {
          title: '混合料类型',
          align: "center",
          dataIndex: 'hunheliaoid_dictText'
        },
        {
          title: '总产量(/kg)',
          align: "center",
          dataIndex: 'zongchanliang',
          customRender: function (text) {
            return parseInt(text)
          }
        },
        {
          title: '油石比',
          align: "center",
          dataIndex: 'youshibi'
        },
        {
          title: '理论油石比',
          align: "center",
          dataIndex: 'llysb'
        },
        {
          title: '沥青温度',
          align: "center",
          dataIndex: 'liqingwd'
        },
        {
          title: '骨料温度',
          align: "center",
          dataIndex: 'guliaowd'
        },
        {
          title: '出料温度(/℃)',
          align: "center",
          dataIndex: 'chuliaowd',
          customRender: function (text) {
            return parseInt(text)
          }
        },
        // {
        //   title: '施工部位',
        //   align: "center",
        //   dataIndex: 'poureLocation'
        // },
        // {
        //   title: '拌合时间',
        //   align: "center",
        //   dataIndex: 'banheshijian',
        //   customRender: function (text) {
        //     return parseInt(text)
        //   }
        // },
        // {
        //   title: '操作人',
        //   align: "center",
        //   dataIndex: 'yonghu',
        // },
        {
          title: '出料时间',
          align: "center",
          dataIndex: 'chuliaoshijian'
        },
        {
          title: '超标等级',
          align: "center",
          dataIndex: 'chaobiaodengji_dictText',
          key: 'chaobiaodengji_dictText',
          scopedSlots: {customRender: 'tags'},
        },
        // {
        //   title: '施工地点',
        //   align: "center",
        //   dataIndex: 'jobLocation'
        // },
        // {
        //   title: '采集时间',
        //   align: "center",
        //   dataIndex: 'caijishijian',
        //
        // },
        // {
        //   title: '强度等级',
        //   align: "center",
        //   dataIndex: 'strengthRank'
        // },
        {
          title: '操作',
          dataIndex: 'action',
          align: "center",
          fixed: "right",
          width: 147,
          scopedSlots: {customRender: 'action'},
        }
      ],
      url: {
        list: "/lqbhz/bhzLqBases/list",
        delete: "/lqbhz/bhzLqBases/delete",
        deleteBatch: "/lqbhz/bhzLqBases/deleteBatch",
        exportXlsUrl: "/lqbhz/bhzLqBases/exportXls",
        importExcelUrl: "lqbhz/bhzLqBases/importExcel",

      },
      dictOptions: {},
      superFieldList: [],
    }
  },
  created() {
    this.getToken();
    this.getSuperFieldList();
    this.shebeiList()
  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
    }
  },
  methods: {
    handlejipei(){
      if (this.selectedRowKeys.length===1){
        this.$refs.modalForm1.show(this.selectedRowKeys)
      }else {
        this.$message.error("请选择一条数据")
      }
    },
    shebeiList:function (){
      var sys_depart_orgcode=Vue.ls.get('SYS_DEPART_ORGCODE');
      usershebeiList({
        sys_depart_orgcode:sys_depart_orgcode,
        sbtypes:'1'
      }).then(res=>{
        this.dictOption=[];
        let result=res.result;
        result.forEach(re=>{
          this.dictOption.push({text:re.sbname,value:re.sbjno})
        })
        //console.log(res)
      })
    },
    initDictConfig() {
    },
    getSuperFieldList() {
      let fieldList = [];
      fieldList.push({type: 'string', value: 'hunheliaoid', text: '混合料编号', dictCode: ''})
      fieldList.push({type: 'string', value: 'chuliaoshijian', text: '出料时间', dictCode: ''})
      fieldList.push({type: 'string', value: 'yonghu', text: '用户', dictCode: ''})
      fieldList.push({type: 'string', value: 'liqingwd', text: '沥青温度', dictCode: ''})
      fieldList.push({type: 'string', value: 'chuliaowd', text: '出料温度', dictCode: ''})
      fieldList.push({type: 'string', value: 'guliaowd', text: '骨料温度', dictCode: ''})
      fieldList.push({type: 'double', value: 'zongchanliang', text: '总产量', dictCode: ''})
      fieldList.push({type: 'double', value: 'leijichanliang', text: '累计产量', dictCode: ''})
      fieldList.push({type: 'string', value: 'shebeibianhao', text: '设备编号', dictCode: ''})
      fieldList.push({type: 'string', value: 'clientNo', text: '客户端编号', dictCode: ''})
      fieldList.push({type: 'string', value: 'cailiaobianhao', text: '材料编号', dictCode: ''})
      fieldList.push({type: 'string', value: 'baocunshijian', text: '保存时间', dictCode: ''})
      fieldList.push({type: 'date', value: 'caijishijian', text: '采集时间'})
      fieldList.push({type: 'int', value: 'banheshijian', text: '拌合时间', dictCode: ''})
      fieldList.push({type: 'string', value: 'cailiaoleixing', text: '材料类型', dictCode: ''})
      fieldList.push({type: 'string', value: 'isdel', text: '是否删除', dictCode: ''})
      fieldList.push({type: 'int', value: 'chaobiaodengji', text: '超标等级', dictCode: 'over_level'})
      fieldList.push({type: 'string', value: 'poureLocation', text: '施工部位', dictCode: ''})
      fieldList.push({type: 'string', value: 'jobLocation', text: '施工地点', dictCode: ''})
      fieldList.push({type: 'string', value: 'ts', text: '时间戳', dictCode: ''})
      fieldList.push({type: 'string', value: 'guid', text: '唯一标识', dictCode: ''})
      fieldList.push({type: 'string', value: 'strengthRank', text: '强度等级', dictCode: ''})
      fieldList.push({type: 'string', value: 'departid', text: '组织结构id', dictCode: ''})
      fieldList.push({type: 'string', value: 'projectName', text: '工程名称', dictCode: ''})
      fieldList.push({type: 'int', value: 'istongji', text: '是否统计', dictCode: ''})
      fieldList.push({type: 'int', value: 'timechaobiao', text: '时间超标', dictCode: ''})
      fieldList.push({type: 'int', value: 'wenduchaobiao', text: '温度超标', dictCode: ''})
      fieldList.push({type: 'int', value: 'cailiaochaobiao', text: '材料超标', dictCode: ''})
      fieldList.push({type: 'int', value: 'alertstate', text: '是否判断超标', dictCode: ''})
      fieldList.push({type: 'string', value: 'formulaNo', text: '配方号', dictCode: ''})
      fieldList.push({type: 'string', value: 'youshibi', text: '油石比', dictCode: ''})
      fieldList.push({type: 'string', value: 'llysb', text: '理论油石比', dictCode: ''})
      fieldList.push({type: 'int', value: 'jipeitongji', text: '筛分实验统计', dictCode: ''})
      fieldList.push({type: 'string', value: 'beiy1', text: '备用1', dictCode: ''})
      fieldList.push({type: 'string', value: 'beiy2', text: '备用2', dictCode: ''})
      fieldList.push({type: 'string', value: 'beiy3', text: '备用3', dictCode: ''})
      fieldList.push({type: 'string', value: 'sysOrgCode', text: '权限', dictCode: ''})
      this.superFieldList = fieldList
    }
  }
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>