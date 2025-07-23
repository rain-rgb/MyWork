<template>
  <!-- <a-card :bordered="false"> -->
  <!-- 查询区域 -->
  <!-- <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
        </a-row>
      </a-form>
    </div> -->
  <!-- 查询区域-END -->

  <!-- 操作按钮区域 -->
  <!-- <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('表单打印')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl"
        @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload> -->
  <!-- 高级查询区域 -->
  <!-- <j-super-query :fieldList="superFieldList" ref="superQueryModal" @handleSuperQuery="handleSuperQuery">
      </j-super-query>
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel">
            <a-icon type="delete" />删除
          </a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作
          <a-icon type="down" />
        </a-button>
      </a-dropdown>
    </div> -->

  <!-- table区域-begin -->
  <!-- <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{
        selectedRowKeys.length }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table ref="table" size="middle" :scroll="{x:true}" bordered rowKey="id" :columns="columns"
        :dataSource="dataSource" :pagination="ipagination" :loading="loading"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}" class="j-table-force-nowrap"
        @change="handleTableChange">

        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <template slot="imgSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无图片</span>
          <img v-else :src="getImgView(text)" height="25px" alt=""
            style="max-width:80px;font-size: 12px;font-style: italic;" />
        </template>
        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无文件</span>
          <a-button v-else :ghost="true" type="primary" icon="download" size="small" @click="downloadFile(text)">
            下载
          </a-button>
        </template>

        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多
              <a-icon type="down" />
            </a>
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
    </div> -->

  <!-- <dps-jc-testitemtype-modal ref="modalForm" @ok="modalFormOk"></dps-jc-testitemtype-modal>
  </a-card> -->
  <a-card>
    <!-- <a-row :gutter="12">
      <a-col :xl="6" :lg="7" :md="8" :sm="24">
        <a-directory-tree multiple checkable default-expand-all @select="onSelect" @expand="onExpand"
          @check="handleNodeClick">
          <a-tree-node key="0-0" title="parent 0">
            <a-tree-node key="0-0-0" title="leaf 0-0" is-leaf />
            <a-tree-node key="0-0-1" title="leaf 0-1" is-leaf />
          </a-tree-node>
          <a-tree-node key="0-1" title="parent 1">
            <a-tree-node key="0-1-0" title="leaf 1-0" is-leaf />
            <a-tree-node key="0-1-1" title="leaf 1-1" is-leaf />
          </a-tree-node>
        </a-directory-tree>
      </a-col>
    </a-row> -->
    <a-row :gutter="12">
      <a-col :xl="6" :lg="7" :md="8" :sm="24">
        <a-tree v-if="treeData.length" :show-line="showLine" :checkable="true" v-model="checkedSiteId"
          :checkStrictly="true" :tree-data="treeData" defaultExpandAll ref="treeForm" show-icon @expand="onExpand"
          :replaceFields="{ children: 'children', title: 'title', key: 'id' }" @check="handleNodeClick">
          <a-icon slot="med" type="folder" />
          <a-icon slot="custom" type="folder-open" />
        </a-tree>
      </a-col>

      <a-col :xl="6" :lg="7" :md="8" :sm="24">
        <a-tree v-if="otherDataCH.length" :show-line="showLine" :checkable="true" v-model="checkedSiteId1"
          :checkStrictly="true" :tree-data="otherDataCH" defaultExpandAll ref="treeForm1" show-icon @expand="onExpand"
          :replaceFields="{ children: 'children', title: 'title', key: 'id' }" >
          <a-icon slot="med" type="folder" />
          <a-icon slot="custom" type="folder-open" />
        </a-tree>
      </a-col>

    </a-row>
  </a-card>
</template>

<script>

import '@/assets/less/TableExpand.less'
import { mixinDevice } from '@/utils/mixin'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import DpsJcTestitemtypeModal from './modules/DpsJcTestitemtypeModal'

export default {
  name: 'DpsJcTestitemtypeList',
  mixins: [JeecgListMixin, mixinDevice],
  components: {
    DpsJcTestitemtypeModal
  },
  data() {
    return {
      showLine: true,
      description: '表单打印管理页面',
      // 表头
      columns: [
        {
          title: '#',
          dataIndex: '',
          key: 'rowIndex',
          width: 60,
          align: "center",
          customRender: function (t, r, index) {
            return parseInt(index) + 1;
          }
        },
        {
          title: '试验类型编码(两位编码01开始)',
          align: "center",
          dataIndex: 'titcode'
        },
        {
          title: '试验类型名称',
          align: "center",
          dataIndex: 'titname'
        },
        {
          title: '样品标识',
          align: "center",
          dataIndex: 'titsamplemark'
        },
        {
          title: '试验类型父节点编码',
          align: "center",
          dataIndex: 'titparentcode'
        },
        {
          title: '当前节点的子节点个数',
          align: "center",
          dataIndex: 'titchildnodesnum'
        },
        {
          title: '试验类型备注',
          align: "center",
          dataIndex: 'titremark'
        },
        {
          title: '是否删除（0：正常1：已删除）',
          align: "center",
          dataIndex: 'titisdel'
        },
        {
          title: 'tipandingyiju',
          align: "center",
          dataIndex: 'tipandingyiju'
        },
        {
          title: 'tishiyanyiju',
          align: "center",
          dataIndex: 'tishiyanyiju'
        },
        {
          title: 'tittype',
          align: "center",
          dataIndex: 'tittype'
        },
        {
          title: '记录表样品信息生成配置，用来生成dps_sy_tableHeader表的ypxx字段内容 格式（, |）：样品名称,sampleName|样品描述,sampleDescribe',
          align: "center",
          dataIndex: 'tiyangpinxinxijl'
        },
        {
          title: '报告表样品信息生成配置，用来生成dps_sy_tableHeader表的ypxx字段内容',
          align: "center",
          dataIndex: 'tiyangpinxinxibg'
        },
        {
          title: 'tipushtablename',
          align: "center",
          dataIndex: 'tipushtablename'
        },
        {
          title: '操作',
          dataIndex: 'action',
          align: "center",
          fixed: "right",
          width: 147,
          scopedSlots: { customRender: 'action' }
        }
      ],
      url: {
        list: "/FormToPrint/dpsJcTestitemtype/list",
        delete: "/FormToPrint/dpsJcTestitemtype/delete",
        deleteBatch: "/FormToPrint/dpsJcTestitemtype/deleteBatch",
        exportXlsUrl: "/FormToPrint/dpsJcTestitemtype/exportXls",
        importExcelUrl: "FormToPrint/dpsJcTestitemtype/importExcel",

      },
      dictOptions: {},
      superFieldList: [],

      treeData: [
        {
          id: 1,
          title: "土工",
        },
        {
          children: [
            {
              id: "1-1",
              statu: undefined,
              title: "粗集料",
            },
            {
              id: "1-2",
              statu: undefined,
              title: "细集料",
            }
          ],
          disabled: true,
          id: -1,
          statu: undefined,
          slots: { icon: "custom" },
          title: "集料",
        },
        {
          children: [
            {
              id: "2-1",
              statu: undefined,
              title: "水泥混凝土拌和物",
            },
            {
              id: "2-2",
              statu: undefined,
              title: "硬化后水泥混凝土",
            },
            {
              id: "2-3",
              statu: undefined,
              title: "水泥混凝土、砂浆",
            }
          ],
          disabled: true,
          id: 2,
          title: "水泥混凝土、砂浆",
          slots: { icon: "custom" },
        },
      ],
      otherData: [
        {
          id: 111,
          title: "土工",
        },
        {
          id: 22123,
          title: "土工1",
        },
        {
          id: 3123,
          title: "土工2",
        },
        {
          id: 4123,
          title: "土工3",
        },
      ],
      otherDataCH: [],
      checkedSiteId: [],
      checkedSiteId1: [],
      oldCheck: "",
    }
  },
  created() {
    this.getSuperFieldList();
  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
    },
  },
  methods: {
    //点击展开有子节点的节点时 改变图标
    // 通过改变其slots的值来改变图标
    onExpand(expandedKeys, event) {
      if (event.expanded) {
        event.node.dataRef.slots.icon = 'custom'
      } else {
        event.node.dataRef.slots.icon = 'med'
      }
    },
    //a-tree 单选模式
    handleNodeClick(checkedKeys) {
      //点击自身不发生改变 --top
      if (checkedKeys.checked.length == 0) {
        checkedKeys.checked[1] = this.oldCheck;
      }
      if (checkedKeys.checked.length == 1) {
        if (!checkedKeys.checked[0]) {
          checkedKeys.checked[0] = this.oldCheck;
        }
        this.oldCheck = checkedKeys.checked[0];
      }
      if (checkedKeys.checked.length == 2) {
        if (!checkedKeys.checked[1]) {
          checkedKeys.checked[1] = this.oldCheck;
        }
        this.oldCheck = checkedKeys.checked[1];
      }
      //点击自身不发生改变-bottom

      this.otherDataCH = this.otherData;
      console.log(checkedKeys);
      this.checkedSiteId = [].concat(checkedKeys.checked[checkedKeys.checked.length - 1]);
      // this.checkedSiteId = [];
      // this.checkedSiteId.push(checkedKeys.checked[checkedKeys.checked.length - 1]);
      this.$nextTick(() => {
        if (this.checkedSiteId.length > 0) {
          this.siteActiveName = 'first';
          // this.getPlantInfo(this.checkedSiteId.join(',')); //查询站点信息
        } else {
          return this.$message.info('请选择项目进行查询');
        }
      });
    },
    onSelect(selectedKeys, info) {
      console.log('selected', selectedKeys, info);
    },
    initDictConfig() {
    },
    getSuperFieldList() {
      let fieldList = [];
      fieldList.push({ type: 'string', value: 'titcode', text: '试验类型编码(两位编码01开始)' })
      fieldList.push({ type: 'string', value: 'titname', text: '试验类型名称' })
      fieldList.push({ type: 'string', value: 'titsamplemark', text: '样品标识' })
      fieldList.push({ type: 'string', value: 'titparentcode', text: '试验类型父节点编码' })
      fieldList.push({ type: 'string', value: 'titchildnodesnum', text: '当前节点的子节点个数' })
      fieldList.push({ type: 'string', value: 'titremark', text: '试验类型备注' })
      fieldList.push({ type: 'int', value: 'titisdel', text: '是否删除（0：正常1：已删除）' })
      fieldList.push({ type: 'string', value: 'tipandingyiju', text: 'tipandingyiju' })
      fieldList.push({ type: 'string', value: 'tishiyanyiju', text: 'tishiyanyiju' })
      fieldList.push({ type: 'int', value: 'tittype', text: 'tittype' })
      fieldList.push({ type: 'string', value: 'tiyangpinxinxijl', text: '记录表样品信息生成配置，用来生成dps_sy_tableHeader表的ypxx字段内容 格式（, |）：样品名称,sampleName|样品描述,sampleDescribe' })
      fieldList.push({ type: 'string', value: 'tiyangpinxinxibg', text: '报告表样品信息生成配置，用来生成dps_sy_tableHeader表的ypxx字段内容' })
      fieldList.push({ type: 'string', value: 'tipushtablename', text: 'tipushtablename' })
      this.superFieldList = fieldList
    }
  }
}
</script>
<style lang="less" scoped>
@import '~@assets/less/common.less';

// .ant-tree /deep/ .ant-tree-treenode-switcher-open>span:nth-child(2)>span {
//   display: none;
// }

// .ant-tree /deep/ .ant-tree-treenode-switcher-close>span:nth-child(2)>span {
//   display: none;
// }
/deep/ .ant-tree-checkbox-disabled {
  display: none;
}

// /deep/ .ant-tree-node-content-wrapper {
//   // color: rgba(0, 0, 0, 0.65);
// }


/deep/ li.ant-tree-treenode-disabled>.ant-tree-node-content-wrapper span {
  color: rgba(0, 0, 0, 0.65);
  cursor: pointer;
}

/deep/ li.ant-tree-treenode-disabled>span:not(.ant-tree-switcher),
li.ant-tree-treenode-disabled>.ant-tree-node-content-wrapper {
  cursor: pointer;
}


/deep/.ant-tree-checkbox-inner {
  border-radius: 50%;
}

/deep/.ant-tree-checkbox-checked .ant-tree-checkbox-inner {
  background: #ffffff;
}

/deep/.ant-tree-checkbox-checked .ant-tree-checkbox-inner::after {
  top: 50%;
  left: 50%;
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background-color: #1890ff;
  border: 0;
  transform: translate(-50%, -50%) scale(1);
  opacity: 1;
  content: " ";
}

/deep/.ant-tree-checkbox-checked .ant-tree-checkbox-inner:hover {
  border: 0px;
}

// ant-tree-directory
</style>