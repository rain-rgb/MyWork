<template>
  <a-row :gutter="10">
    <a-col :md="8" :sm="24">
      <a-card :bordered="false">
        <div style="background: #fff;padding-left:16px;height: 100%; margin-top: 5px">
          <a-input-search @search="onSearch" style="width:100%;margin-top: 10px" placeholder="请输入部门名称" />
          <!-- 树-->

          <template v-if="departTree.length > 0">

            <!--组织机构-->
            <a-tree showLine :selectedKeys="selectedKeys" :checkStrictly="true" @select="onSelect"
              :dropdownStyle="{ maxHeight: '200px', overflow: 'auto' }" :treeData="departTree" :load-data="onLoadData"
              :autoExpandParent="autoExpandParent" :expandedKeys="iExpandedKeys" @expand="onExpand" />

          </template>
          <div style="margin-top: 24px;" v-else-if="departTree.length == 0">
            <h3><span>暂无数据</span></h3>
          </div>

        </div>
      </a-card>
    </a-col>
    <a-col :md="16" :sm="24">
      <a-card :bordered="false">
        <dept-role-info ref="DeptRoleInfo" @clearSelectedDepartKeys="clearSelectedDepartKeys" />
      </a-card>
    </a-col>
  </a-row>
</template>
<script>
// import DeptBaseInfo from './modules/DeptBaseInfo'
// import DeptUserInfo from './modules/DeptUserInfo'
import { queryMyDepartTreeList, searchByKeywords } from '@/api/api'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import DeptRoleInfo from './modules/DeptRoleInfo'
import { deleteAction, getAction, downFile, getFileAccessHttpUrl } from '@/api/manage'
export default {
  name: 'componentList',
  mixins: [JeecgListMixin],
  components: {
    DeptRoleInfo,
    // DeptBaseInfo,
    // DeptUserInfo,
  },
  data() {
    return {
      currentDeptId: '',
      iExpandedKeys: [],
      loading: false,
      autoExpandParent: true,
      currFlowId: '',
      currFlowName: '',
      disable: true,
      treeData: [],
      visible: false,
      departTree: [],
      rightClickSelectedKey: '',
      hiding: true,
      model: {},
      dropTrigger: '',
      depart: {},
      disableSubmit: false,
      checkedKeys: [],
      selectedKeys: [],
      autoIncr: 1,
      currSelected: {},
      form: this.$form.createForm(this),
      labelCol: {
        xs: { span: 24 },
        sm: { span: 5 }
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 }
      },
      graphDatasource: {
        nodes: [],
        edges: []
      },
      userIdentity: "",
      replaceFields: { children: 'children', title: 'title', key: 'id' },
      
    }
  },
  methods: {
    onLoadData(treeNode) {
      console.log(treeNode, "点击加载")
      return new Promise(resolve => {
        setTimeout(() => {
          getAction("/sys/sysDepartproject/queryTreeListSon1", { parentId: treeNode.dataRef.id }).then((res) => {
            if (res.success) {
              let data = res.result;
              if (data == null) {
                treeNode.dataRef.isLast = true
                resolve();
              } else {
                for (let i = 0; i < res.result.length; i++) {
                  let temp = res.result[i]
                  treeNode.dataRef.children.push(temp);
                }
                this.departTree = [...this.departTree];
                resolve();
              }

            }
          })
        }, 500);
      });
    },
    callback(key) {
      //console.log(key)
    },
    loadData() {
      this.refresh();
    },
    clearSelectedDepartKeys() {
      this.checkedKeys = [];
      this.selectedKeys = [];
      this.currentDeptId = '';
      this.$refs.DeptUserInfo.currentDeptId = '';
      this.$refs.DeptRoleInfo.currentDeptId = '';
    },
    loadTree() {
      var that = this
      that.treeData = []
      that.departTree = []
      getAction("/sys/sysDepartproject/queryMyDeptTreeList", { parentId: '' }).then((res) => {
        if (res.success && res.result) {
          for (let i = 0; i < res.result.length; i++) {
            let temp = res.result[i]
            that.treeData.push(temp)
            that.departTree.push(temp)
            that.setThisExpandedKeys(temp)
            // console.log(temp.id)
          }
          this.loading = false
        }
        that.userIdentity = res.message
      })
    },
    setThisExpandedKeys(node) {
      //只展开一级目录
      if (node.children && node.children.length > 0) {
        this.iExpandedKeys.push(node.key)
        //下方代码放开注释则默认展开所有节点
        /**
        for (let a = 0; a < node.children.length; a++) {
          this.setThisExpandedKeys(node.children[a])
        }
        */
      }
    },
    refresh() {
      this.loading = true
      this.loadTree()
    },

    onExpand(expandedKeys) {
      // console.log('onExpand', expandedKeys)
      // if not set autoExpandParent to false, if children expanded, parent can not collapse.
      // or, you can remove all expanded children keys.
      this.iExpandedKeys = expandedKeys
      this.autoExpandParent = false
    },

    onSearch(value) {
      let that = this
      if (value) {
        searchByKeywords({ keyWord: value }).then((res) => {
          if (res.success) {
            that.departTree = []
            for (let i = 0; i < res.result.length; i++) {
              let temp = res.result[i]
              that.departTree.push(temp)
            }
          } else {
            that.$message.warning(res.message)
          }
        })
      } else {
        that.loadTree()
      }

    },
    onCheck(checkedKeys, e) {
      let record = e.node.dataRef;
      // console.log('onCheck', checkedKeys, e);
      this.checkedKeys = [];
      // if (e.checked === true) {
      this.currentDeptId = record.id;
      this.checkedKeys.push(record.id);
      this.$refs.DeptRoleInfo.open(record);
      // }
      // else {
      //   this.checkedKeys = [];
      //   this.$refs.DeptBaseInfo.clearForm();
      //   this.$refs.DeptUserInfo.clearList();
      // }

      this.hiding = false;
      // this.checkedKeys = checkedKeys.checked
    },
    onSelect(selectedKeys, e) {
      console.log(selectedKeys, e, '这是我写的');
      if (this.selectedKeys[0] !== selectedKeys[0]) {
        this.selectedKeys = [selectedKeys[0]];
      }
      let record = e.node.dataRef;
      this.checkedKeys.push(record.id);
      this.$refs.DeptRoleInfo.onClearSelected();
      this.$refs.DeptRoleInfo.open(record);
    },
  },
  created() {
    this.currFlowId = this.$route.params.id
    this.currFlowName = this.$route.params.name
    // this.loadTree()
  },
}
</script>
<style scoped>
@import '~@assets/less/common.less'
</style>