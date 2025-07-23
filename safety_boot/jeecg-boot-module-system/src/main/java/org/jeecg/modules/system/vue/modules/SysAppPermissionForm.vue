<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="父id" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="parentId">
              <a-input v-model="model.parentId" placeholder="请输入父id"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="菜单标题" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="name">
              <a-input v-model="model.name" placeholder="请输入菜单标题"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="菜单权限编码" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="menuPerms">
              <a-input v-model="model.menuPerms" placeholder="请输入菜单权限编码"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="菜单类型(0:一级菜单; 1:子菜单:2:按钮权限)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="menuType">
              <a-input-number v-model="model.menuType" placeholder="请输入菜单类型(0:一级菜单; 1:子菜单:2:按钮权限)" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="按钮权限编码" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="btnPerms">
              <a-input v-model="model.btnPerms" placeholder="请输入按钮权限编码"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="权限策略1显示2禁用" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="permsType">
              <a-input v-model="model.permsType" placeholder="请输入权限策略1显示2禁用"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="菜单排序" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sortNo">
              <a-input-number v-model="model.sortNo" placeholder="请输入菜单排序" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="菜单图标" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="icon">
              <a-input v-model="model.icon" placeholder="请输入菜单图标"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="是否叶子节点:    1:是   0:不是" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="isLeaf">
              <a-input-number v-model="model.isLeaf" placeholder="请输入是否叶子节点:    1:是   0:不是" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="是否隐藏路由: 0否,1是" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="hidden">
              <a-input-number v-model="model.hidden" placeholder="请输入是否隐藏路由: 0否,1是" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="描述" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="description">
              <a-input v-model="model.description" placeholder="请输入描述"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="删除状态 0正常 1已删除" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="delFlag">
              <a-input-number v-model="model.delFlag" placeholder="请输入删除状态 0正常 1已删除" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="是否添加数据权限1是0否" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="ruleFlag">
              <a-input-number v-model="model.ruleFlag" placeholder="请输入是否添加数据权限1是0否" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="按钮权限状态(0无效1有效)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="status">
              <a-input v-model="model.status" placeholder="请输入按钮权限状态(0无效1有效)"  ></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </j-form-container>
  </a-spin>
</template>

<script>

  import { httpAction, getAction } from '@/api/manage'
  import { validateDuplicateValue } from '@/utils/util'

  export default {
    name: 'SysAppPermissionForm',
    components: {
    },
    props: {
      //表单禁用
      disabled: {
        type: Boolean,
        default: false,
        required: false
      }
    },
    data () {
      return {
        model:{
         },
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        confirmLoading: false,
        validatorRules: {
        },
        url: {
          add: "/system/sysAppPermission/add",
          edit: "/system/sysAppPermission/edit",
          queryById: "/system/sysAppPermission/queryById"
        }
      }
    },
    computed: {
      formDisabled(){
        return this.disabled
      },
    },
    created () {
       //备份model原始值
      this.modelDefault = JSON.parse(JSON.stringify(this.model));
    },
    methods: {
      add () {
        this.edit(this.modelDefault);
      },
      edit (record) {
        this.model = Object.assign({}, record);
        this.visible = true;
      },
      submitForm () {
        const that = this;
        // 触发表单验证
        this.$refs.form.validate(valid => {
          if (valid) {
            that.confirmLoading = true;
            let httpurl = '';
            let method = '';
            if(!this.model.id){
              httpurl+=this.url.add;
              method = 'post';
            }else{
              httpurl+=this.url.edit;
               method = 'put';
            }
            httpAction(httpurl,this.model,method).then((res)=>{
              if(res.success){
                that.$message.success(res.message);
                that.$emit('ok');
              }else{
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
            })
          }
         
        })
      },
    }
  }
</script>