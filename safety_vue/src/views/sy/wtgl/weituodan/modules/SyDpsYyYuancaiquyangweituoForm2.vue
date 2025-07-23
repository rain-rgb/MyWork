<template>
<!--  <a-spin :spinning="confirmLoading">-->
<!--    <j-form-container :disabled="formDisabled">-->
<!--      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">-->

        <form class="layui-form layui-form-pane" style="height: 100%;" onsubmit="return false;">
          <input id="id" name="id" type="hidden" />
          <input id="yuancaijinchangdengjiId" name="yuancaijinchangdengjiId" type="hidden" />
          <div>
            <!--startprint-->
            <div class="paperA4" id="printA4">
              <div class="titleWord">
                瑞苍高速公路（龙丽温至甬台温复线联络线工程）
              </div>
              <div class="titleWord" style="font-weight: bolder">
                中心试验室原材料取样检测委托单
              </div>
              <div class="" style="display: inline-block;float: right;margin: 10px 0 10px 30px;">
                <span id="">编号:</span>
                <span style="display: inline-block;width: 155px;">
                        <input type="text" style="width: 100%;border-bottom: 1px solid #000000;" id="weituodanbianhao"
                               name="weituodanbianhao" readonly>
                    </span>
              </div>
              <table class="tableWT layui-table" style="border: 2px solid #000;box-sizing: border-box;">
                <tr>
                  <td width="20%">施工单位</td>
                  <td><textarea type="text" name="shigongdanwei" class="shigongdanwei" disabled></textarea></td>
                  <td width="20%">监理单位</td>
                  <td><textarea type="text" name="jianglidanwei" class="jianglidanwei" disabled></textarea></td>
                </tr>
                <tr>
                  <td>委托单位</td>
                  <td colspan="3"><input type="text" name="weituodanwei" class="weituodanwei" disabled></td>
                </tr>
                <tr>
                  <td>委托人</td>
                  <td><input type="text" name="weituoren" class="weituoren" disabled></td>
                  <td>委托日期</td>
                  <td><input type="text" name="weituoriqi" class="weituoriqi"></td>
                </tr>
              </table>
              <table class="tableWT layui-table next" style="border: 2px solid #000;border-top: none">
                <tr>
                  <td width="20%">样品名称</td>
                  <td><input type="text" name="cailiaomingcheng" class="cailiaomingcheng" disabled></td>
                  <td width="20%">材料厂家</td>
                  <td><input type="text" name="cailiaochangjia" class="cailiaochangjia" disabled></td>
                </tr>
                <tr>
                  <td>规格/型号/牌号</td>
                  <td><input type="text" name="guigexinghao" class="guigexinghao" disabled></td>
                  <td>生产批号</td>
                  <td><input type="text" name="shengchanpihao" class="shengchanpihao" disabled></td>
                </tr>
                <tr>
                  <td>出厂日期</td>
                  <td><input type="text" name="chuchangriqi" class="chuchangriqi" disabled></td>
                  <td>进场日期</td>
                  <td><input type="text" name="jinchangriqi" class="jinchangriqi" disabled></td>
                </tr>
                <tr>
                  <td>使用部位</td>
                  <td><input type="text" name="shiyongbuwei" class="shiyongbuwei" disabled></td>
                  <td>代表数量</td>
                  <td><input type="text" name="jinchangshuliang" class="jinchangshuliang" disabled></td>
                </tr>
                <tr>
                  <td>取样地点</td>
                  <!--          <input type="text" name="chunfangdidian" class="chunfangdidian layui-hide">-->
                  <td colspan="3"><input type="text" name="quyangdidian" class="quyangdidian" disabled></td>
                </tr>
                <tr>
                  <td>报告份数</td>
                  <td><input type="text" name="baogaofenshu" class="baogaofenshu"></td>
                  <td>样品处理方式</td>
                  <td><input type="text" name="yangpinchulifangshi" class="yangpinchulifangshi"
                             id="yangpinchulifangshi"></td>
                </tr>
                <!--<tr>
      <td rowspan="2">检测项目</td>
      <td colspan="3" style="border-bottom: none">
        <select xm-select="jiancexiangmu" xm-select-search="" xm-select-search-type="dl" lay-verify="jiancexiangmu"
                xm-select-create></select>
      </td>
    </tr>-->
                <tr>
                  <td rowspan="2">检测项目</td>
                  <td colspan="3" rowspan="2">
                    <!--            <input type="hidden" id="jiancexiangmu" name="jiancexiangmu">-->
                    <textarea type="text" rows="3" name="jiancexiangmu" id="jiancexiangmu"
                              class="jiancexiangmu"></textarea>
                  </td>
                </tr>
                <tr>

                </tr>
                <tr>
                  <td colspan="2" rowspan="3">
                    <span style="position: absolute;">备注：</span>
                    <textarea type="text" rows="3" style="padding-top: 20px;" name="beizhu" id="beizhu"
                              class="beizhu"></textarea>
                  </td>
                  <td colspan="2" rowspan="3">
                    <button type="button" class="layui-btn" id="updateImg"><i
                      class="layui-icon">&#xe67c;</i>附件上传</button>
<!--                    <div class="layui-upload-img" id="preview" style="display:none;overflow:auto;height: 100%;">-->
<!--                      <img src="" style="max-width:100%;height: 100%;" onclick="preview()"><span-->
<!--                      style="position:absolute;cursor:pointer;" onclick="reUpload()">×<span></span>-->
<!--                    </div>-->
                  </td>
                </tr>
                <tr>

                </tr>
                <tr>

                </tr>
              </table>
            </div>
            <!--endprint-->
          </div>
          <div class="noprint" style="height: 100px;overflow:auto;">
            <table id="jcxm" class="layui-table layui-card" style="display: none">
              <tbody id="jcxmL">
              </tbody>
            </table>
          </div>
          <div class="noprint" style="height: 100px;overflow:auto;">
            <table id="ypclfs" class="layui-table layui-card" style="display: none">
              <tbody id="ypclfsL">
              <tr class="jcxmTr">
                <td style="user-select:none;" onclick="addypclfs('检测单位处理')">检测单位处理</td>
              </tr>
              <tr class="jcxmTr">
                <td style="user-select:none;" onclick="addypclfs('自行处理')">自行处理</td>
              </tr>
              <tr class="jcxmTr">
                <td style="user-select:none;" onclick="addypclfs('留样处理')">留样处理</td>
              </tr>
              </tbody>
            </table>
          </div>

          <div class="btn noprint" style="position:absolute;top: 1%;right: 2%">
            <a-button type="primary"  @click="doPrint()">打印</a-button>
          </div>

          <div class="btn">
            <a-button type="primary" >保存</a-button>
            <a-button type="primary">保存并提交</a-button>
            <a-button type="primary" >关闭</a-button>
          </div>
        </form>


<!--      </a-form-model>-->
<!--    </j-form-container>-->
<!--  </a-spin>-->
</template>

<script>



   import { httpAction, getAction } from '@/api/manage'
  import { validateDuplicateValue } from '@/utils/util'

  export default {
    name: 'SyDpsYyYuancaiquyangweituoForm2',
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
          add: "/sywtgl/syDpsYyYuancaiquyangweituo/add",
          edit: "/sywtgl/syDpsYyYuancaiquyangweituo/edit",
          queryById: "/sywtgl/syDpsYyYuancaiquyangweituo/queryById"
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
<style >
.layui-form-label {
  width: 150px !important;
}

.form {
  height: calc(100vh - 150px);
  overflow-y: auto;
}

.btn {
  position: fixed;
  bottom: 1%;
  right: 2%;
  margin-left: 5px;
}

.btn .layui-btn {
  width: 100px;
}

.layui-form-item .layui-input-inline {
  width: 50%;
}

.uploader-list .info {
  position: relative;
  margin-top: -25px;
  background-color: black;
  color: white;
  filter: alpha(Opacity=80);
  -moz-opacity: 0.5;
  opacity: 0.5;
  font-size: 11px;
  width: 100px;
  height: 25px;
  text-align: center;
  display: none;
}

.uploader-list .handle {
  position: relative;
  background-color: black;
  color: white;
  filter: alpha(Opacity=80);
  -moz-opacity: 0.5;
  opacity: 0.5;
  width: 100px;
  text-align: right;
  height: 18px;
  margin-bottom: -18px;
  display: none;
}

.uploader-list .handle i {
  margin-right: 5px;
}

.uploader-list .handle i:hover {
  cursor: pointer;
}

.uploader-list .file-iteme {
  margin: 12px 0 0 15px;
  padding: 1px;
  float: left;
}

.paperA4 {
  margin: 10px auto;
  width: 786px;
  height: 1000px;
  padding: 40px;
  box-sizing: border-box;
  overflow: hidden;
  background-color: #fff;
  box-shadow: 10px 10px 20px #ccc;
}

td,
textarea,
input[readonly],
textarea[readonly] {
  background-color: #fff
}


.titleWord {
  font-size: 30px;
  letter-spacing: 2px;
  padding: 10px 0;
  margin-bottom: 10px;
  text-align: center;
}

.tableWT tr {
  height: 55px;
}

#preview img {
  cursor: pointer;
}

@media print {
  .noprint {
    display: none;
  }

  #noprint {
    display: none;
  }

  .paperA4 {
    margin: 0;
    width: 786px;
    height: 1000px;
    padding: 40px;
    background-color: #fff;
    box-shadow: none;
  }

  #updateImg {
    display: none;
  }

  #preview span {
    display: none;
  }
}

#jcxm {
  position: absolute;
  right: 5%;
  bottom: -20%;
  width: 20%;
}

#ypclfs {
  position: absolute;
  right: 5%;
  bottom: 15%;
  width: 20%;
}

#jcxm,
#ypclfs {
  margin: 0;
  border: 1px solid #ccc;
  color: #1d1d1d;
  background-color: #FFF;
  cursor: pointer;
}

#jcxm tbody tr td:hover,
#ypclfs tbody tr td:hover {
  background-color: #e5e5e5;
}


</style>
