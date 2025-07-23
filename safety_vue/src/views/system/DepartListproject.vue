<template xmlns:background-color="http://www.w3.org/1999/xhtml">
  <a-row :gutter="24">
    <a-col :md="12" :sm="24">
      <a-card :bordered="false">

        <!-- 按钮操作区域 -->
        <a-row style="margin-left: 14px">
          <a-button @click="handleAdd(1)" type="primary">添加项目</a-button>
          <a-button @click="handleAdd(2)" type="primary">添加下级</a-button>
          <a-button type="primary" icon="download" @click="handleExportXls('分部分项信息')">导出</a-button>
          <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
            <a-button type="primary" icon="import">导入</a-button>
          </a-upload>
          <a-button title="删除多条数据" @click="batchDel" type="default">批量删除</a-button>
          <a-button @click="routeReload" type="default" icon="reload" :loading="loading">刷新</a-button>
        </a-row>
        <div style="background: #fff;padding-left:16px;height: 100%; margin-top: 5px">
          <a-alert type="info" :showIcon="true">
            <div slot="message">
              当前选择：<span v-if="this.currSelected.title">{{ getCurrSelectedTitle() }}</span>
              <a v-if="this.currSelected.title" style="margin-left: 10px" @click="onClearSelected">取消选择</a>
            </div>
          </a-alert>
          <a-input-search @search="onSearch" style="width:100%;margin-top: 10px" placeholder="请输入项目名称"/>
          <!-- 树-->
          <a-col :md="24" :sm="24">
            <template>
              <a-dropdown :trigger="[this.dropTrigger]" @visibleChange="dropStatus">
               <span style="user-select: none">
            <a-tree
              checkable
              multiple
              showLine
              :load-data="onLoadData"
              @select="onSelect"
              :show-icon="showIcon"
              @check="onCheck"
              @rightClick="rightHandle"
              :selectedKeys="selectedKeys"
              :checkedKeys="checkedKeys"
              :treeData="departTree"
              :checkStrictly="checkStrictly"
              :autoExpandParent="autoExpandParent"
              @expand="onExpand"/>
<!--                  :expandedKeys="iExpandedKeys"   展开指定的父节点-->
<!--             <a-tree checkable-->
<!--                     multiple-->
<!--                     showLine-->
<!--                     :show-icon="showIcon"-->
<!--                     :load-data="onLoadData"-->
<!--                     :tree-data="departTree"-->
<!--                     @select="onSelect"-->
<!--                     @check="onCheck"-->
<!--                     @rightClick="rightHandle"-->
<!--                     :selectedKeys="selectedKeys"-->
<!--                     :checkedKeys="checkedKeys"-->
<!--                     :checkStrictly="checkStrictly"-->
<!--                     />-->
                </span>
                <!--新增右键点击事件,和增加添加和删除功能-->
                <a-menu slot="overlay">
                  <a-menu-item @click="handleAdd(3)" key="1">添加</a-menu-item>
                  <a-menu-item @click="handleSync()" key="4">同步下级</a-menu-item>
                  <!-- <a-menu-item @click="handleDelete" key="2">删除</a-menu-item> -->
                  <a-menu-item @click="closeDrop" key="3">取消</a-menu-item>
                </a-menu>
              </a-dropdown>
            </template>
          </a-col>
        </div>
      </a-card>
      <!---- author:os_chengtgen -- date:20190827 --  for:切换父子勾选模式 =======------>
<!--      <div class="drawer-bootom-button">-->
<!--        <a-dropdown :trigger="['click']" placement="topCenter">-->
<!--          <a-menu slot="overlay">-->
<!--            <a-menu-item key="1" @click="switchCheckStrictly(1)">父子关联</a-menu-item>-->
<!--            <a-menu-item key="2" @click="switchCheckStrictly(2)">取消关联</a-menu-item>-->
<!--            <a-menu-item key="3" @click="checkALL">全部勾选</a-menu-item>-->
<!--            <a-menu-item key="4" @click="cancelCheckALL">取消全选</a-menu-item>-->
<!--            <a-menu-item key="5" @click="expandAll">展开所有</a-menu-item>-->
<!--            <a-menu-item key="6" @click="closeAll">合并所有</a-menu-item>-->
<!--          </a-menu>-->
<!--          <a-button>-->
<!--            树操作 <a-icon type="up" />-->
<!--          </a-button>-->
<!--        </a-dropdown>-->
<!--      </div>-->
      <!---- author:os_chengtgen -- date:20190827 --  for:切换父子勾选模式 =======------>
    </a-col>
    <a-col :md="10" :sm="24">
      <a-tabs defaultActiveKey="1">
        <a-tab-pane tab="基本信息" key="1" >
          <a-card :bordered="false" v-if="selectedKeys.length>0">
            <a-form :form="form">
              <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="项目名称">
                <a-input placeholder="请输入项目/单位名称/分部工程名称/分项工程名称/具体部位名称/工序名称" v-decorator="['departName', validatorRules.departName ]"/>
              </a-form-item>
              <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="所属上级">
                <a-tree-select
                  style="width:100%"
                  :dropdownStyle="{maxHeight:'200px',overflow:'auto'}"
                  :treeData="treeData"
                  :disabled="disable"
                  v-model="model.title"
                  placeholder="无">
                </a-tree-select>
              </a-form-item>
              <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="项目编码">
                <a-input disabled placeholder="请输入项目编码" v-decorator="['orgCode', validatorRules.orgCode ]"/>
              </a-form-item>
              <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="已分配分部">
                <a-input disabled placeholder="还未分配" v-decorator="['orgCodes', validatorRules.orgCodes ]"/>
              </a-form-item>
              <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="项目类型">
                <template v-if="orgCategoryDisabled">
                  <a-radio-group v-decorator="['orgCategory',validatorRules.orgCategory]" placeholder="请选择项目类型">
                    <a-radio value="1">
                      单位工程
                    </a-radio>
                  </a-radio-group>
                </template>
                <template v-else>
                  <a-radio-group v-decorator="['orgCategory',validatorRules.orgCategory]" placeholder="请选择项目类型">
                    <a-radio value="2">
                      分部工程
                    </a-radio>
                    <a-radio value="3">
                      子分部工程
                    </a-radio>
                    <a-radio value="4">
                      分项工程
                    </a-radio>
                    <a-radio value="5">
                      具体部位
                    </a-radio>
                    <a-radio value="6">
                      工序
                    </a-radio>
                    <a-radio value="7">
                      分项交验
                    </a-radio>
                    <a-radio value="8">
                      梁
                    </a-radio>
                  </a-radio-group>
                </template>
              </a-form-item>
              <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="排序">
                <a-input-number v-decorator="[ 'departOrder',{'initialValue':0}]"/>
              </a-form-item>
<!--              <a-form-item-->
<!--                :labelCol="labelCol"-->
<!--                :wrapperCol="wrapperCol"-->
<!--                label="手机号">-->
<!--                <a-input placeholder="请输入手机号" v-decorator="['mobile', {'initialValue':''}]"/>-->
<!--              </a-form-item>-->
<!--              <a-form-item-->
<!--                :labelCol="labelCol"-->
<!--                :wrapperCol="wrapperCol"-->
<!--                label="地址">-->
<!--                <a-input placeholder="请输入地址" v-decorator="['address', {'initialValue':''}]"/>-->
<!--              </a-form-item>-->
              <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="备注">
                <a-textarea placeholder="请输入备注" v-decorator="['memo', {'initialValue':''}]"/>
              </a-form-item>
            </a-form>
            <div class="anty-form-btn">
              <a-button @click="emptyCurrForm" type="default" htmlType="button" icon="sync">重置</a-button>
              <a-button @click="submitCurrForm" type="primary" htmlType="button" icon="form">保存</a-button>
            </div>
          </a-card>
          <a-card v-else >
            <a-empty>
              <span slot="description"> 请先选择一个项目! </span>
            </a-empty>
          </a-card>
        </a-tab-pane>
        <a-tab-pane tab="分配分部分项权限" key="2" forceRender>
          <a-card>
            <a-form :form="form1">
              <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="工程名称">
                <a-tree-select
                  style="width:100%"
                  :dropdownStyle="{maxHeight:'200px',overflow:'auto'}"
                  :treeData="treeDatas"
                  :disabled="disable1"
                  v-model="model1.id"
                  placeholder="请先选择工程/大桥"
                  @select="selectdepart"
                  @change="selectdepartprojectParentnode">
                </a-tree-select>
              </a-form-item>
              <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="项目类型" v-if="orgCategoryDisableds">
                <template >
                  <a-radio-group v-decorator="['orgCategory',validatorRuless.orgCategory]" placeholder="请选择项目类型">
                    <a-radio value="1">
                      单位工程
                    </a-radio>
                  </a-radio-group>
                </template>
              </a-form-item>
              <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="组织机构">
                <a-tree-select
                  style="width:100%"
                  :dropdownStyle="{maxHeight:'200px',overflow:'auto'}"
                  :treeData="departreeData"
                  :disabled="disable2"
                  v-model="model2.id"
                  placeholder="请选择分部"
                  @select="selectdepart1"
                  @change="selectdepartprojectParentnode1">
                </a-tree-select>
              </a-form-item>
            </a-form>
          </a-card>
          <div class="anty-form-btn">
            <a-button @click="emptyCurrForm1" type="default" htmlType="button" icon="sync">重置</a-button>
            <a-button @click="submitCurrForm1" type="primary" htmlType="button" icon="form">确认关联</a-button>
          </div>
<!--          <depart-auth-modal ref="departAuth"/>-->
        </a-tab-pane>
      </a-tabs>

    </a-col>
    <depart-modal ref="departModal" @ok="loadTree"></depart-modal>
  </a-row>
</template>
<script>
  import DepartModal from './modules/DepartModalproject'
  import pick from 'lodash.pick'
  import {
    queryDepartTreeprojectList,
    searchByKeyprojectwords,
    deleteByDepartprojectId,
    queryMyDepartTreeList, searchByKeywords,queryDepartTreeprojectListSon
  } from '@/api/api'
  import {httpAction, deleteAction,getAction} from '@/api/manage'
  import {JeecgListMixin} from '@/mixins/JeecgListMixin'
  import DepartAuthModal from './modules/DepartAuthModalproject'
  // 表头
  const columns = [
    {
      title: '机构名称',
      dataIndex: 'departName'
    },
    {
      title: '机构类型',
      align: 'center',
      dataIndex: 'orgType'
    },
    {
      title: '机构编码',
      dataIndex: 'orgCode',
    },
    {
      title: '已分配到',
      align: 'center',
      dataIndex: 'orgCodes'
    },
    {
      title: '手机号',
      dataIndex: 'mobile'
    },
    {
      title: '传真',
      dataIndex: 'fax'
    },
    {
      title: '地址',
      dataIndex: 'address'
    },
    {
      title: '排序',
      align: 'center',
      dataIndex: 'departOrder'
    },
    {
      title: '操作',
      align: 'center',
      dataIndex: 'action',
      scopedSlots: {customRender: 'action'}
    }
  ]
  export default {
    name: 'DepartListproject',
    mixins: [JeecgListMixin],
    components: {
      DepartAuthModal,
      DepartModal
    },
    data() {
      return {
        treeData1: [
          { title: 'Expand to load', key: '0' },
          { title: 'Expand to load', key: '1' },
          { title: 'Tree Node', key: '2', isLeaf: true },
        ],
        showIcon: false,
        iExpandedKeys: [],
        loading: false,
        autoExpandParent: true,
        currFlowId: '',
        currFlowName: '',
        disable: true,
        disable1:false,
        disable2:false,
        departreeData:[],
        treeData: [],
        treeDatas: [],
        visible: false,
        departTree: [],
        rightClickSelectedKey: '',
        rightClickSelectedOrgCode: '',
        hiding: true,
        model: {},
        model1: {},
        model2: {},
        dropTrigger: '',
        depart: {},
        columns: columns,
        disableSubmit: false,
        checkedKeys: [],
        selectedKeys: [],
        autoIncr: 1,
        currSelected: {},
        currSelected1: {},
        currSelected2: {},
        parentId:"",
        allTreeKeys:[],
        checkStrictly: true,

        form: this.$form.createForm(this),
        form1: this.$form.createForm(this),
        form2: this.$form.createForm(this),
        labelCol: {
          xs: {span: 24},
          sm: {span: 5}
        },
        wrapperCol: {
          xs: {span: 24},
          sm: {span: 16}
        },
        graphDatasource: {
          nodes: [],
          edges: []
        },
        validatorRules: {
          departName: {rules: [{required: true, message: '请输入机构/部门名称!'}]},
          orgCode: {rules: [{required: true, message: '请输入机构编码!'}]},
          orgCodes: {rules: [{required: true, message: '未分配到分部!'}]},
          orgCategory: {rules: [{required: true, message: '请输入机构类型!'}]},
          mobile: {rules: [{validator: this.validateMobile}]}
        },
        validatorRuless: {
           //id:{rules: [{required: true, message: '请输入机构/部门名称!'}]},
          // departName: {rules: [{required: true, message: '请输入机构/部门名称!'}]},
          // orgCode: {rules: [{required: true, message: '请输入机构编码!'}]},
          orgCategory: {rules: [{required: true, message: '请输入机构类型!'}]}
        },
        url: {
          delete: '/sys/sysDepartproject/delete',
          edit: '/sys/sysDepartproject/edit',
          edit1: '/sys/sysDepartproject/edit1',
          sync: '/sys/sysDepartproject47/synchronizeChild',
          deleteBatch: '/sys/sysDepartproject/deleteBatch',
          exportXlsUrl: "/sys/sysDepartproject/exportXls",
          importExcelUrl: "/sys/sysDepartproject/importExcel",
        },
        orgCategoryDisabled:false,
        orgCategoryDisableds:false,
      }
    },
    computed: {
      importExcelUrl: function () {
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      }
    },
    methods: {
      onLoadData(treeNode) {
        //console.log(treeNode,"点击加载")
        return new Promise(resolve => {
          //console.log("异步进入")
          // if (treeNode.dataRef.children) {
          //   resolve();
          //   return;
          // }
          setTimeout(() => {
            //console.log("异步定时")
            let param={parentId:this.parentId}
            queryDepartTreeprojectListSon(param).then((res) => {
              if (res.success) {
                //console.log(res)
                let data=res.result;
                for (let i = 0; i < res.result.length; i++) {
                  let temp = res.result[i]
                  treeNode.dataRef.children.push(temp);
                }
                this.departTree = [...this.departTree];
                resolve();
              }
            })
          }, 500);
        });
      },
      selectdepart(value, node, extra){
        let record=node.dataRef
        console.log(record,"分部分项信息")
        //console.log("信息",e.node)
        //console.log("信息1",xx)
        this.currSelected1 = Object.assign({}, record)
        this.model1 = this.currSelected1
        this.model1.id = record.id
        this.model1.orgCode = record.orgCode
        this.model1.orgCodes = record.orgCodes
      },
      selectdepartprojectParentnode(value, label, extra){//分配权限中选择分部分项获取具体值
       //console.log(value,label,extra);
        //console.log("数据",extra.triggerNode.dataRef);
        let record=extra.triggerNode.dataRef;
        this.showtitle(record);
      },
      // 触发onSelect事件时,为部门树右侧的form表单赋值
      selectdepart1(value, node, extra){
        let record=node.dataRef
        console.log(record,"组织机构信息")
        this.currSelected2 = Object.assign({}, record)
        this.model2 = this.currSelected2
        this.model2.orgCode = record.orgCode
        console.log(this.model2)
      },
      selectdepartprojectParentnode1(value, label, extra){//分配权限中选择分部分项获取具体值
        let record=extra.triggerNode.dataRef;
      },
      //组织机构接口
      queryTreeData(keyword) {
        this.commonRequestThen(queryMyDepartTreeList({
          departName: keyword ? keyword : undefined
        }))
      },
      handleSearch(value) {
        if (value) {
          this.commonRequestThen(searchByKeywords({ keyWord: value }))
        } else {
          this.queryTreeData()
        }
      },
      commonRequestThen(promise) {
        this.loading = true
        promise.then(res => {
          if (res.success) {
            this.departreeData = res.result
            console.log(this.departreeData)
          } else {
            this.$message.warn(res.message)
            console.error('组织机构查询失败:', res)
          }
        }).finally(() => {
          this.loading = false
          this.cardLoading = false
        })
      },
      showtitle(record){
        if(record.orgCategory == '1'){
          this.orgCategoryDisableds=true;
        }else{
          this.orgCategoryDisableds=false;
          this.$message.error('请选择单位工程/大桥!')
        }
        this.orgCategoryDisableds=true;
        this.$nextTick(() => {
          this.form1.getFieldDecorator('fax', {initialValue: ''})
          this.form1.setFieldsValue(pick(record,'orgCategory'))
        })

      },
      loadData() {
        //this.routeReload();
        this.refresh();
      },
      loadTree() {
        var that = this
        that.treeData = []
        that.treeDatas=[]
        that.departTree = []
        console.log(this.parentId,"父节点数据")
        let param={parentId:this.parentId}
        queryDepartTreeprojectList(param).then((res) => {
          console.log(res,"分部分项")
          if (res.success) {
            //部门全选后，再添加部门，选中数量增多
            this.allTreeKeys = [];
            for (let i = 0; i < res.result.length; i++) {
              let temp = res.result[i]
              that.treeData.push(temp)
              that.treeDatas.push(temp)
              that.departTree.push(temp)
              that.setThisExpandedKeys(temp)
              that.getAllKeys(temp);
              // console.log(temp.id)
            }
            this.loading = false
          }
        })
      },
      routeReload() {//选择全局组织机构之后刷新当前的右侧页面  解决 没这个方法之前点击之后缓存数据不更新
        this.reloadFlag = false
        let ToggleMultipage = 'ToggleMultipage'
        this.$store.dispatch(ToggleMultipage, false)
        this.$nextTick(() => {
          this.$store.dispatch(ToggleMultipage, true)
          this.reloadFlag = true
        })
        console.log("刷新页面")
      },
      setThisExpandedKeys(node) {
        if (node.children && node.children.length > 0) {
          this.iExpandedKeys.push(node.key)
          for (let a = 0; a < node.children.length; a++) {
            this.setThisExpandedKeys(node.children[a])
          }
        }
      },
      refresh() {//进入页面运行 也可用来刷新页面
        this.loading = true
        this.parentId="";
        this.loadTree()
      },
      // 右键操作方法
      rightHandle(node) {
        this.dropTrigger = 'contextmenu'
        console.log(node.node.eventKey)
        this.rightClickSelectedKey = node.node.eventKey
        this.rightClickSelectedOrgCode = node.node.dataRef.orgCode
      },
      onExpand(expandedKeys) {
        //this.parentId="";
        console.log('onExpand', expandedKeys)
        // if not set autoExpandParent to false, if children expanded, parent can not collapse.
        // or, you can remove all expanded children keys.
        this.iExpandedKeys = expandedKeys
        var length=expandedKeys.length;
        this.parentId=expandedKeys[length-1];
        console.log(this.parentId,"子节点数据")
        this.autoExpandParent = false
      },
      backFlowList() {
        this.$router.back(-1)
      },
      // 右键点击下拉框改变事件
      dropStatus(visible) {
        if (visible == false) {
          this.dropTrigger = ''
        }
      },
      // 右键店家下拉关闭下拉框
      closeDrop() {
        this.dropTrigger = ''
      },
      addRootNode() {
        this.$refs.nodeModal.add(this.currFlowId, '')
      },
      batchDel: function () {
        console.log(this.checkedKeys)
        if (this.checkedKeys.length <= 0) {
          this.$message.warning('请选择一条记录！')
        } else {
          var ids = ''
          for (var a = 0; a < this.checkedKeys.length; a++) {
            ids += this.checkedKeys[a] + ','
          }
          var that = this
          this.$confirm({
            title: '确认删除',
            content: '确定要删除所选中的 ' + this.checkedKeys.length + ' 条数据，以及子节点数据吗?',
            onOk: function () {
              deleteAction(that.url.deleteBatch, {ids: ids}).then((res) => {
                if (res.success) {
                  //删除成功后，去除已选中中的数据
                  that.$message.success('删除成功!')
                  //that.loadTree()
                  //that.refresh();
                  that.routeReload();
                  //that.loadTreeseach();
                  //删除后同步清空右侧基本信息内容
                  that.onClearSelected()
                } else {
                  that.$message.warning(res.message)
                }
              })
            }
          })
        }
      },
      onSearch(value) {
        let that = this
        if (value) {
          searchByKeyprojectwords({keyWord: value}).then((res) => {
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
          //that.parentId="";
          //this.refresh();
          this.routeReload();
        }

      },
      nodeModalOk() {
        //this.parentId="";
        //this.loadTree()
        //this.refresh();
        this.routeReload();
        //this.loadTreeseach();
      },
      nodeModalClose() {
      },
      hide() {
        console.log(111)
        this.visible = false
      },
      onCheck(checkedKeys, info) {
        console.log('onCheck', checkedKeys, info)
        this.hiding = false
        //this.checkedKeys = checkedKeys.checked
        // <!---- author:os_chengtgen -- date:20190827 --  for:切换父子勾选模式 =======------>
        if(this.checkStrictly){
          this.checkedKeys = checkedKeys.checked;
        }else{
          this.checkedKeys = checkedKeys
        }
        // <!---- author:os_chengtgen -- date:20190827 --  for:切换父子勾选模式 =======------>
      },
      onSelect(selectedKeys, e) {
        console.log('selected', selectedKeys, e)
        this.hiding = false
        let record = e.node.dataRef
        console.log('onSelect-record', record)
        this.currSelected = Object.assign({}, record)
        this.model = this.currSelected
        this.selectedKeys = [record.key]
        this.model.parentId = record.parentId
        this.setValuesToForm(record)
        //this.$refs.departAuth.show(record.id);

      },
      // 触发onSelect事件时,为部门树右侧的form表单赋值
      setValuesToForm(record) {
        if(record.orgCategory == '1'){
          this.orgCategoryDisabled = true;
        }else{
          this.orgCategoryDisabled = false;
        }
        this.$nextTick(() => {
          this.form.getFieldDecorator('fax', {initialValue: ''})
          this.form.setFieldsValue(pick(record, 'departName','orgCategory', 'orgCode', 'departOrder', 'mobile', 'fax', 'address', 'memo','orgCodes'))
        })
      },
      getCurrSelectedTitle() {
        return !this.currSelected.title ? '' : this.currSelected.title
      },
      onClearSelected() {
        this.hiding = true
        this.checkedKeys = []
        this.currSelected = {}
        this.form.resetFields()
        this.selectedKeys = []
        //this.$refs.departAuth.departId = ''
      },
      handleNodeTypeChange(val) {
        this.currSelected.nodeType = val
      },
      notifyTriggerTypeChange(value) {
        this.currSelected.notifyTriggerType = value
      },
      receiptTriggerTypeChange(value) {
        this.currSelected.receiptTriggerType = value
      },
      submitCurrForm1() {
        this.form1.validateFields((err, values) => {
          if (!err) {
            if (!this.model1.id) {
              this.$message.warning('请点击选择要关联的分部分项工程!')
              return
            }
            if (!this.model2.orgCode) {
              this.$message.warning('请点击选择要关联的组织机构!')
              return
            }
            let formData={id:this.model1.id,sysOrgCodes:this.model2.orgCode,orgCode:this.model1.orgCode,orgCodes:this.model1.orgCodes}
            //let formData = Object.assign(this.currSelected, values)
            console.log('Received values of form: ', formData)
            getAction(this.url.edit1, formData).then((res) => {
              if (res.success) {
                this.$message.success('关联成功!')
                this.model2={};
                this.model1={};
                this.orgCategoryDisableds=false;
                //this.parentId="";
                //this.loadData();
                //this.loadTreeseach();
                //this.refresh();
                this.routeReload();
              } else {
                this.$message.error(res.message)
              }
            })
          }
        })
      },
      submitCurrForm() {
        this.form.validateFields((err, values) => {
          if (!err) {
            if (!this.currSelected.id) {
              this.$message.warning('请点击选择要修改分部分项!')
              return
            }

            let formData = Object.assign(this.currSelected, values)
            console.log('Received values of form: ', formData)
            httpAction(this.url.edit, formData, 'put').then((res) => {
              if (res.success) {
                this.$message.success('保存成功!')
                //this.parentId="";
                //this.loadTree();
                //this.loadTreeseach();
                //this.refresh();
                this.routeReload();
              } else {
                this.$message.error(res.message)
              }
            })
          }
        })
      },
      emptyCurrForm() {
        this.form.resetFields()
      },
      emptyCurrForm1() {
        this.form1.resetFields()
      },
      nodeSettingFormSubmit() {
        this.form.validateFields((err, values) => {
          if (!err) {
            console.log('Received values of form: ', values)
          }
        })
      },
      openSelect() {
        this.$refs.sysDirectiveModal.show()
      },
      handleAdd(num) {
        if (num == 1) {
          this.$refs.departModal.add()
          this.$refs.departModal.title = '新增'
        } else if (num == 2) {
          let key = this.currSelected.key
          if (!key) {
            this.$message.warning('请先点击选中上级部门！')
            return false
          }
          this.$refs.departModal.add(this.selectedKeys)
          this.$refs.departModal.title = '新增'
        } else {
          this.$refs.departModal.add(this.rightClickSelectedKey)
          this.$refs.departModal.title = '新增'
        }
      },
      handleSync(){
        let that = this;
        let formData = {
          orgcode:that.rightClickSelectedOrgCode
        }
        console.log('Received values of form: ', formData)
        getAction(this.url.sync, formData).then((res) => {
          if (res.success) {
            this.$message.success('同步成功!')
          } else {
            this.$message.error(res.message)
          }
        })
        console.log(`that.rightClickSelectedKey`,that.rightClickSelectedKey);
        console.log(`that.rightClickSelectedOrgCode`,that.rightClickSelectedOrgCode);
      },
      handleDelete() {
        var that = this
        this.$confirm({
          title: '确认删除',
          content: '确定要删除此部门以及子节点数据吗?',
          onOk: function () {
            deleteByDepartprojectId({id: that.rightClickSelectedKey}).then((resp) => {
              if (resp.success) {
                //删除成功后，去除已选中中的数据
                that.checkedKeys.splice(that.checkedKeys.findIndex(key => key === that.rightClickSelectedKey), 1);
                that.$message.success('删除成功!')
                that.parentId="";
                that.loadTree()
                //删除后同步清空右侧基本信息内容
                let orgCode=that.form.getFieldValue("orgCode");
                if(orgCode && orgCode === that.rightClickSelectedOrgCode){
                  that.onClearSelected()
                }
              } else {
                that.$message.warning('删除失败!')
              }
            })
          }
        })
      },
      selectDirectiveOk(record) {
        console.log('选中指令数据', record)
        this.nodeSettingForm.setFieldsValue({directiveCode: record.directiveCode})
        this.currSelected.sysCode = record.sysCode
      },
      getFlowGraphData(node) {
        this.graphDatasource.nodes.push({
          id: node.id,
          text: node.flowNodeName
        })
        if (node.children.length > 0) {
          for (let a = 0; a < node.children.length; a++) {
            let temp = node.children[a]
            this.graphDatasource.edges.push({
              source: node.id,
              target: temp.id
            })
            this.getFlowGraphData(temp)
          }
        }
      },
     // <!---- author:os_chengtgen -- date:20190827 --  for:切换父子勾选模式 =======------>
      expandAll () {
        this.iExpandedKeys = this.allTreeKeys
      },
      closeAll () {
        this.iExpandedKeys = []
      },
      checkALL () {
        this.checkStriccheckStrictlytly = false
        this.checkedKeys = this.allTreeKeys
      },
      cancelCheckALL () {
        //this.checkedKeys = this.defaultCheckedKeys
        this.checkedKeys = []
      },
      switchCheckStrictly (v) {
        if(v==1){
          this.checkStrictly = false
        }else if(v==2){
          this.checkStrictly = true
        }
      },
      getAllKeys(node) {
        // console.log('node',node);
        this.allTreeKeys.push(node.key)
        if (node.children && node.children.length > 0) {
          for (let a = 0; a < node.children.length; a++) {
            this.getAllKeys(node.children[a])
          }
        }
      }
      // <!---- author:os_chengtgen -- date:20190827 --  for:切换父子勾选模式 =======------>

    },
    created() {
      this.currFlowId = this.$route.params.id
      this.currFlowName = this.$route.params.name
      this.queryTreeData();
      // this.loadTree()
    },

  }
</script>
<style scoped>
  .ant-card-body .table-operator {
    margin: 15px;
  }

  .anty-form-btn {
    width: 100%;
    text-align: center;
  }

  .anty-form-btn button {
    margin: 0 5px;
  }

  .anty-node-layout .ant-layout-header {
    padding-right: 0
  }

  .header {
    padding: 0 8px;
  }

  .header button {
    margin: 0 3px
  }

  .ant-modal-cust-warp {
    height: 100%
  }

  .ant-modal-cust-warp .ant-modal-body {
    height: calc(100% - 110px) !important;
    overflow-y: auto
  }

  .ant-modal-cust-warp .ant-modal-content {
    height: 90% !important;
    overflow-y: hidden
  }

  #app .desktop {
    height: auto !important;
  }

  /** Button按钮间距 */
  .ant-btn {
    margin-left: 3px
  }

  .drawer-bootom-button {
    /*position: absolute;*/
    bottom: 0;
    width: 100%;
    border-top: 1px solid #e8e8e8;
    padding: 10px 16px;
    text-align: left;
    left: 0;
    background: #fff;
    border-radius: 0 0 2px 2px;
  }
</style>