<template>
  <div>
    <div>
      <div class="btn">
        <a-button type="primary" @click="createType">新增</a-button>
      </div>
      <div class="btn">
        <a-button type="primary" @click="fillOutForm">填写表单</a-button>
      </div>
    </div>
    <div>
      <a-tree
        :show-line="tree.showLine"
        :show-icon="tree.showIcon"
        @select="onSelect"
        :treeData="treeData"
        :replaceFields="replaceFields">
      </a-tree>
    </div>
    <div>
      <a-modal :visible="modal.visible" :title="modal.title" width="700px" @cancel="modalCancel" @ok="modalOk"
               :maskClosable="modal.maskClosable">
        <a-form-model layout="horizontal">
          <a-form-model-item label="类型编码">
            <a-input placeholder="类型编码由系统自动生成" v-model="model.typeEncoding" :disabled="form.disabled"></a-input>
          </a-form-model-item>
        </a-form-model>
        <!--        <a-form-model>-->
        <!--          <a-form-model-item label="样品标识">-->
        <!--            <a-input placeholder="必填" v-model="model.sampleMark"></a-input>-->
        <!--          </a-form-model-item>-->
        <!--        </a-form-model>-->
        <a-form-model>
          <a-form-model-item label="试验类型名称">
            <a-input placeholder="必填" v-model="model.expTypeName"></a-input>
          </a-form-model-item>
        </a-form-model>
        <a-form-model>
          <a-col>
            <a-form-model-item label="实验类型">
              <j-dict-select-tag placeholder="请选择" v-model="model.expType" dictCode="expType"
                                 @change="change"></j-dict-select-tag>
            </a-form-model-item>
          </a-col>
        </a-form-model>
        <a-form-model>
          <a-form-model-item label="父级编码">
            <a-input :disabled="form.disabled" v-model="model.parentEncoding"></a-input>
          </a-form-model-item>
        </a-form-model>
        <!--        <a-form-model>-->
        <!--          <a-form-model-item label="记录表样品信息">-->
        <!--            <a-input v-model="model.recordInfo"></a-input>-->
        <!--          </a-form-model-item>-->
        <!--        </a-form-model>-->
        <!--        <a-form-model>-->
        <!--          <a-form-model-item label="报告表样品信息">-->
        <!--            <a-input v-model="model.reportInfo"></a-input>-->
        <!--          </a-form-model-item>-->
        <!--        </a-form-model>-->
        <!--        <a-form-model>-->
        <!--          <a-form-model-item label="推送表头">-->
        <!--            <a-input v-model="model.pushHeader"></a-input>-->
        <!--          </a-form-model-item>-->
        <!--        </a-form-model>-->
        <!--        <a-form-model>-->
        <!--          <a-form-model-item label="备注">-->
        <!--            <a-textarea v-model="model.remark" :rows="4"/>-->
        <!--          </a-form-model-item>-->
        <!--        </a-form-model>-->
      </a-modal>
    </div>
    <div>
      <a-checkbox-group
        v-model="value"
        name="checkboxgroup"
        :options="expList"
        @change="onChange"/>
    </div>

    <exp-form-modal></exp-form-modal>

  </div>
</template>

<script>
import {getAction, postAction} from "@api/manage";
import ExpFormModal from "./expFormModal";

export default {
  name: 'expTypeTree',
  components: {
    ExpFormModal
  },
  data() {
    return {
      formVisible: true,
      value: [],
      expList: [],
      treeData: [],
      encoding: '',
      nodeType: undefined,
      model: { //表单参数
        typeEncoding: '',
        sampleMark: '',
        expTypeName: '',
        expType: undefined,
        parentEncoding: ''
        // recordInfo: '',
        // reportInfo: '',
        // pushHeader: '',
        // remark: ''
      },
      modal: { //modal属性
        visible: false,
        title: '',
        keyboard: true,
        remark: '',
        maskClosable: false
      },
      form: { //表单属性
        disabled: true
      },
      tree: { //树形列表属性
        showLine: true,
        showIcon: true
      },
      select: { //选择器属性
        allowClear: true,
        size: 'default',
      },
      url: {
        getExpList: "/syLeixingExp/syLeixingExp/getExpByTypeId",
        getTreeData: "/syLeixing/syLeixing/queryTreeList",
        add: "/syLeixing/syLeixing/add"
      },
      replaceFields: { //树形列表属性名转换
        children: 'list',
        title: 'cailiaoname',
        key: 'cailiaono'
      },
    };
  },
  created() {
    this.getTreeData();
  },
  methods: {
    //获得树形列表数据
    getTreeData() {
      let param = '';
      getAction(this.url.getTreeData, param).then(res => {
        this.treeData = res;
      })
    },
    //获得试验列表数据
    getExpList(id) {
      let param = {
        typeId :id
      }
      getAction(this.url.getExpList, param).then(res => {
        var dataList = res.result
        dataList.forEach(i=>{
          this.expList.push({label:i.expname,value:i.expid})
        })
      })

    },
    //树形列表选择事件
    onSelect(selectedKeys, info) {
      this.expList = []
      this.encoding = selectedKeys[0]
      //如果父id长度大于3则该选项为试验类型,获取该实验类型下的试验列表
      if (selectedKeys[0].length>3){
        this.getExpList(selectedKeys[0])
      }
    },
    //选择类型
    change(val) {
      this.expType = parseInt(val)
    },
    //点击新增按钮
    createType() {
      //处理脏数据
      this.model.expType = undefined
      this.model.expTypeName = ''
      //选定后取消选定encoding会undefined,故将其转为空值进行判断
      if (this.encoding === undefined) {
        this.encoding = ''
        //处理脏数据
        this.model.parentEncoding = ''
      }
      //如果未选择节点直接新增则显示新增材料，反之显示新增试验
      if (this.encoding !== '') {
        this.model.parentEncoding = this.encoding.substring(0, 3)
        this.modal.title = '新增试验'
      } else {
        this.model.parentEncoding = ''
        this.modal.title = '新增材料'
      }
      this.modal.visible = true
    },
    //取消和右上角关闭按钮
    modalCancel() {
      this.modal.visible = false
    },
    //提交按钮
    modalOk() {
      if (this.model.expTypeName === ''){
        this.warning()
      }else{
        if (this.model.parentEncoding !== '') {
          this.nodeType = 1
        } else {
          this.nodeType = 0
        }
        //实验类型对应后端数据字段
        let params = {
          cailiaoname: this.model.expTypeName,
          parentno: this.model.parentEncoding,
          nodetype: this.nodeType
        }
        postAction(this.url.add, params).then(res => {
          if (res.success){
            this.success()
            this.modal.visible = false
            //操作成功后重新获取数据
            this.getTreeData()
          }else {
            this.error()
          }
        })
      }
    },
    success() {
      this.$message.success('添加成功');
    },
    error() {
      this.$message.error('添加失败');
    },
    warning() {
      this.$message.warning('表单不能留空');
    },
    //多选框组件 选择事件
    onChange(checkedValues) {
      console.log('checked = ', checkedValues);
      console.log('value = ', this.value);
    },
    //填写表单按钮
    fillOutForm(){
      this.$bus.$emit('open',this.formVisible,this.value)
    }
  },
};
</script>
<style scoped>
 .btn{
   margin: 20px
 }
</style>
