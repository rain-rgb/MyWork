<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form :form="form" slot="detail">
        <a-row>
          <template>
            <a-steps direction="horizontal" :current="1">
              <a-step title="钢筋绑扎" :status= "wiretiestatus" >
                <div class="biank" slot="description" v-for="(item,index) in wiretiedata" :key="index">
                  <p class="endtime" >开始时间：</p>
                  <p>{{item.starttime}}</p>
                  <p class="endtime" >结束时间：</p>
                  <p>{{item.endtime}}</p>
                  <p class="endtime">责任人：{{item.responsible}}</p>
                  <p class="endtime">监管人：{{item.createBy}}</p>
                </div>
              </a-step>
              <a-step title="混凝土浇筑" :status= "jiaozhustatus" >
                <div class="biank" slot="description" v-for="(item,index) in jiaozhudata" :key="index">
                  <p class="endtime" >开始时间：</p>
                  <p>{{item.starttime}}</p>
                  <p class="endtime" >结束时间：</p>
                  <p>{{item.endtime}}</p>
                  <p class="endtime">总方量：{{item.mete}}</p>
                  <p class="endtime">监管人：{{item.createBy}}</p>
                </div>
              </a-step>
              <a-step title="收面" :status= "jingzhistatus" >
                <div class="biank" slot="description" v-for="(item,index) in jingzhidata" :key="index">
                  <p class="endtime" >开始时间：</p>
                  <p>{{item.starttime}}</p>
                  <p class="endtime" >结束时间：</p>
                  <p>{{item.endtime}}</p>
                  <p class="endtime">监管人：{{item.createBy}}</p>
                </div>
              </a-step>
              <a-step title="养生" :status= "zhengyangstatus" >
                <div class="biank" slot="description" v-for="(item,index) in zhengyangdata" :key="index">
                  <p class="endtime" >开始时间：</p>
                  <p>{{item.starttime}}</p>
                  <p class="endtime" >结束时间：</p>
                  <p>{{item.endtime}}</p>
                  <p class="endtime">温度：{{item.temperature}}</p>
                  <p class="endtime">湿度：{{item.humidity}}</p>
                  <p class="endtime">监管人：{{item.createBy}}</p>
                </div>
              </a-step>
              <a-step title="张拉" :status= "zhanglastatus" >
                <div class="biank" slot="description" v-for="(item,index) in zhangladata" :key="index">
                  <p class="endtime" >开始时间：</p>
                  <p>{{item.starttime}}</p>
                  <p class="endtime" >结束时间：</p>
                  <p>{{item.endtime}}</p>
                  <p class="endtime">监管人：{{item.createBy}}</p>
                </div>
              </a-step>
              <a-step title="封端" :status= "fengduanstatus" >
                <div class="biank" slot="description" v-for="(item,index) in fengduandata" :key="index">
                  <p class="endtime" >开始时间：</p>
                  <p>{{item.starttime}}</p>
                  <p class="endtime" >结束时间：</p>
                  <p>{{item.endtime}}</p>
                  <p class="endtime">监管人：{{item.createBy}}</p>
                </div>
              </a-step>
              <a-step title="存梁" :status= "cunliangstatus" >
                <div class="biank" slot="description" v-for="(item,index) in cunliangdata" :key="index">
                  <p class="endtime" >存入梁时间：</p>
                  <p>{{item.cuntime}}</p>
                  <p class="endtime">存入梁监管人：{{item.cunpeople}}</p>
                  <p class="endtime" >出场时间：</p>
                  <p>{{item.chutime}}</p>
                  <p class="endtime">出场监管人：{{item.chupeople}}</p>
                  <p class="endtime">梁所存层：{{item.liangceng}}</p>
                  <p class="endtime">存梁行：{{item.cunlianghang}}</p>
                  <p class="endtime">存梁列：{{item.cunlianglie}}</p>
                  <p class="endtime">梁场名称：{{item.shebeino}}</p>
                </div>
              </a-step>
            </a-steps>
          </template>
<!--          <a-row>-->

<!--            <a-col :span="12">-->
<!--              <a-form-item label="任务单编号" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <a-input v-decorator="['code']" placeholder="请输入任务单编号"  ></a-input>-->
<!--              </a-form-item>-->
<!--            </a-col>-->
<!--          </a-row>-->

<!--          <a-row>-->
<!--            <a-col :span="12">-->
<!--              <a-form-item label="所属组织机构" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <JselectDqDepart v-decorator="['sysOrgCode']" ::multi="false"  />-->
<!--              </a-form-item>-->
<!--            </a-col>-->
<!--            <a-col :span="12">-->
<!--              <a-form-item label="创建时间" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <j-date placeholder="请选择创建时间" v-decorator="['dattim']" :trigger-change="true" style="width: 100%" dateFormat="YYYY-MM-DD HH:mm:ss"/>-->
<!--              </a-form-item>-->
<!--            </a-col>-->

<!--          </a-row>-->

<!--          <a-row>-->
<!--            <a-col :span="12">-->
<!--              <a-form-item label="任务性质" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <a-input v-decorator="['attribute']" placeholder="请输入任务性质"  ></a-input>-->
<!--              </a-form-item>-->
<!--            </a-col>-->
<!--            <a-col :span="12">-->
<!--              <a-form-item label="配合比编号" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <a-input v-decorator="['recipe']" placeholder="请输入1线配合比编号"  ></a-input>-->
<!--              </a-form-item>-->
<!--            </a-col>-->
<!--            <a-col :span="12">-->
<!--              <a-form-item label="生产线" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <j-dict-select-tag type="list" v-decorator="['station']" :trigger-change="true" dictCode="stations" placeholder="请选择生产线" />-->
<!--              </a-form-item>-->
<!--            </a-col>-->
<!--            <a-col :span="12">-->
<!--              <a-form-item label="配合比编号" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <a-select placeholder="请选择配合比编号"  v-decorator="['recipe']" >-->
<!--                  <a-select-option v-for="(item, key) in dictOptions1" :key="item.key"  >-->
<!--                  <span style="display: inline-block;width: 100%" >-->
<!--                    {{ item.label }}-->
<!--                  </span>-->
<!--                  </a-select-option>-->
<!--                </a-select>-->
<!--              </a-form-item>-->
<!--            </a-col>-->
<!--          </a-row>-->

<!--          <a-row>-->
<!--            <a-col :span="12">-->
<!--              <a-form-item label="2线配合比编号" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <a-input v-decorator="['recipes']" placeholder="请输入2线配合比编号"  ></a-input>-->
<!--              </a-form-item>-->
<!--            </a-col>-->

<!--            <a-col :span="12">-->
<!--              <a-form-item label="合同信息" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <a-input v-decorator="['contract']" placeholder="请输入合同信息"  ></a-input>-->
<!--              </a-form-item>-->
<!--            </a-col>-->
<!--          </a-row>-->

<!--          <a-row>-->
<!--            <a-col :span="12">-->
<!--              <a-form-item label="客户名称" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <a-input v-decorator="['customer']" placeholder="请输入客户名称"  ></a-input>-->
<!--              </a-form-item>-->
<!--            </a-col>-->

<!--          </a-row>-->

<!--          <a-row>-->
<!--            <a-col :span="12">-->
<!--              <a-form-item label="施工地址" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <a-input v-decorator="['projadr']" placeholder="请输入施工地址"  ></a-input>-->
<!--              </a-form-item>-->
<!--            </a-col>-->
<!--            <a-col :span="12">-->
<!--              <a-form-item label="工程类别" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <a-input v-decorator="['projtype']" placeholder="请输入工程类别"  ></a-input>-->
<!--              </a-form-item>-->
<!--            </a-col>-->
<!--            <a-col :span="12">-->
<!--              <a-form-item label="请选择施工部位" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <JSelectDqProjName  v-decorator="['projgrade']" ::multi="false"  />-->
<!--              </a-form-item>-->
<!--            </a-col>-->

<!--            <a-col :span="12">-->
<!--              <a-form-item label="工程级别" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <a-input v-decorator="['projgrade']" placeholder="请输入工程级别"  ></a-input>-->
<!--              </a-form-item>-->
<!--            </a-col>-->
<!--          </a-row>-->

<!--          <a-row>-->
<!--            <a-col :span="12">-->
<!--              <a-form-item label="开工面积" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <a-input-number v-decorator="['projarea']" placeholder="请输入开工面积" style="width: 100%" />-->
<!--              </a-form-item>-->
<!--            </a-col>-->

<!--          </a-row>-->

<!--          <a-row>-->
<!--            <a-col :span="12">-->
<!--              <a-form-item label="运输距离" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <a-input-number v-decorator="['distance']" placeholder="请输入运输距离" style="width: 100%" />-->
<!--              </a-form-item>-->
<!--            </a-col>-->
<!--            <a-col :span="12">-->
<!--              <a-form-item label="工程名称" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <a-input v-decorator="['projname']" placeholder="请输入工程名称"  :disabled="true"></a-input>-->
<!--              </a-form-item>-->
<!--            </a-col>-->
<!--            <a-col :span="12">-->
<!--              <a-form-item label="施工部位" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <a-input v-decorator="['conspos']" placeholder="请输入施工部位"  :disabled="true"></a-input>-->
<!--              </a-form-item>-->
<!--            </a-col>-->
<!--          </a-row>-->

<!--          <a-row>-->
<!--            <a-col :span="12">-->
<!--              <a-form-item label="浇筑方式" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <j-dict-select-tag type="list" v-decorator="['pour']" :trigger-change="true" dictCode="pour" placeholder="请选择浇筑方式" />-->
<!--              </a-form-item>-->
<!--            </a-col>-->
<!--            <a-col :span="12">-->
<!--              <a-form-item label="产品种类" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <a-input v-decorator="['variety']" placeholder="请输入产品种类"  ></a-input>-->
<!--              </a-form-item>-->
<!--            </a-col>-->
<!--          </a-row>-->

<!--          <a-row>-->
<!--            <a-col :span="12">-->
<!--              <a-form-item label="强度等级" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <j-dict-select-tag type="list" v-decorator="['betlev']" :trigger-change="true" dictCode="betlev" placeholder="请选择强度等级" />-->
<!--              </a-form-item>-->
<!--            </a-col>-->
<!--            <a-col :span="12">-->
<!--              <a-form-item label="抗渗等级" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <a-input v-decorator="['filter']" placeholder="请输入抗渗等级"  ></a-input>-->
<!--              </a-form-item>-->
<!--            </a-col>-->
<!--          </a-row>-->

<!--          <a-row>-->
<!--            <a-col :span="12">-->
<!--              <a-form-item label="抗冻等级" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <a-input v-decorator="['freeze']" placeholder="请输入抗冻等级"  ></a-input>-->
<!--              </a-form-item>-->
<!--            </a-col>-->
<!--            <a-col :span="12">-->
<!--              <a-form-item label="坍落度" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <j-dict-select-tag type="list" v-decorator="['lands']" :trigger-change="true" dictCode="lands" placeholder="请选择坍落度" />-->
<!--              </a-form-item>-->
<!--            </a-col>-->
<!--          </a-row>-->

<!--          <a-row>-->
<!--            <a-col :span="12">-->
<!--              <a-form-item label="水泥品种" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <a-input v-decorator="['cement']" placeholder="请输入水泥品种"  ></a-input>-->
<!--              </a-form-item>-->
<!--            </a-col>-->
<!--            <a-col :span="12">-->
<!--              <a-form-item label="石子种类" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <a-input v-decorator="['stone']" placeholder="请输入石子种类"  ></a-input>-->
<!--              </a-form-item>-->
<!--            </a-col>-->
<!--          </a-row>-->

<!--          <a-row>-->
<!--            <a-col :span="12">-->
<!--              <a-form-item label="骨料粒径" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <a-input v-decorator="['bnsize']" placeholder="请输入骨料粒径"  ></a-input>-->
<!--              </a-form-item>-->
<!--            </a-col>-->
<!--            <a-col :span="12">-->
<!--              <a-form-item label="外加剂要求" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <a-input v-decorator="['addliq']" placeholder="请输入外加剂要求"  ></a-input>-->
<!--              </a-form-item>-->
<!--            </a-col>-->
<!--          </a-row>-->

<!--          <a-row>-->
<!--            <a-col :span="12">-->
<!--              <a-form-item label="任务方量" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <a-input-number v-decorator="['mete']" placeholder="请输入任务方量" style="width: 100%" />-->
<!--              </a-form-item>-->
<!--            </a-col>-->
<!--            <a-col :span="12">-->
<!--              <a-form-item label="技术要求" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <a-input v-decorator="['request']" placeholder="请输入技术要求"  ></a-input>-->
<!--              </a-form-item>-->
<!--            </a-col>-->
<!--            <a-col :span="12">-->
<!--              <a-form-item label="搅拌时间" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <a-input-number v-decorator="['mixlast']" placeholder="请输入搅拌时间" style="width: 100%" />-->
<!--              </a-form-item>-->
<!--            </a-col>-->
<!--          </a-row>-->

<!--          <a-row>-->

<!--            <a-col :span="12">-->
<!--              <a-form-item label="浇注日期" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <j-date placeholder="请选择浇注日期" v-decorator="['begtim']" :trigger-change="true" style="width: 100%"/>-->
<!--              </a-form-item>-->
<!--            </a-col>-->
<!--            <a-col :span="12">-->
<!--              <a-form-item label="截止日期" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <j-date placeholder="请选择截止日期" v-decorator="['endtim']" :trigger-change="true" style="width: 100%"/>-->
<!--              </a-form-item>-->
<!--            </a-col>-->
<!--          </a-row>-->

<!--          <a-row>-->

<!--            <a-col :span="12">-->
<!--              <a-form-item label="调度人员" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <a-input v-decorator="['attamper']" placeholder="请输入调度人员"  ></a-input>-->
<!--              </a-form-item>-->
<!--            </a-col>-->
<!--          </a-row>-->

<!--          <a-row>-->
<!--            <a-col :span="12">-->
<!--              <a-form-item label="计划生产时间" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <j-date placeholder="请选择计划生产时间" v-decorator="['productiontime']" :trigger-change="true" style="width: 100%"/>-->
<!--              </a-form-item>-->
<!--            </a-col>-->
<!--            <a-col :span="12">-->
<!--              <a-form-item label="标识" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <a-input v-decorator="['flag']" placeholder="请输入标识"  ></a-input>-->
<!--              </a-form-item>-->
<!--            </a-col>-->
<!--            <a-col :span="12">-->
<!--              <a-form-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <a-input v-decorator="['note']" placeholder="请输入备注"  ></a-input>-->
<!--              </a-form-item>-->
<!--            </a-col>-->
<!--          </a-row>-->

<!--          <a-col :span="24">-->
<!--            <a-form-item label="创建人" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['createBy']" placeholder="请输入创建人"  ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-row>-->

            <!--          <a-col :span="24">-->
            <!--            <a-form-item label="是否删除 0未删除 1已删除" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
            <!--              <a-input-number v-decorator="['isdel']" placeholder="请输入是否删除 0未删除 1已删除" style="width: 100%" />-->
            <!--            </a-form-item>-->
            <!--          </a-col>-->
<!--            <a-col :span="12">-->
<!--              <a-form-item label="审核状态" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <a-input-number v-decorator="['status']" placeholder="请输入审核状态" style="width: 100%" />-->
<!--              </a-form-item>-->
<!--            </a-col>-->
<!--          </a-row>-->

<!--          <a-row>-->
<!--            <a-col :span="12">-->
<!--              <a-form-item label="浇筑状态" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <a-input-number v-decorator="['jiaozhustatus']" placeholder="请输入浇筑状态" style="width: 100%" />-->
<!--              </a-form-item>-->
<!--            </a-col>-->
<!--            <a-col :span="12">-->
<!--              <a-form-item label="收面静置状态" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <a-input-number v-decorator="['jingzhistatus']" placeholder="请输入收面静置状态" style="width: 100%" />-->
<!--              </a-form-item>-->
<!--            </a-col>-->
<!--          </a-row>-->

<!--          <a-row>-->
<!--            <a-col :span="12">-->
<!--              <a-form-item label="蒸养状态1" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <a-input-number v-decorator="['zhengyangstatus1']" placeholder="请输入蒸养状态1" style="width: 100%" />-->
<!--              </a-form-item>-->
<!--            </a-col>-->
<!--            <a-col :span="12">-->
<!--              <a-form-item label="蒸养状态2" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <a-input-number v-decorator="['zhengyangstatus2']" placeholder="请输入蒸养状态2" style="width: 100%" />-->
<!--              </a-form-item>-->
<!--            </a-col>-->
<!--          </a-row>-->

<!--          <a-row>-->
<!--            <a-col :span="12">-->
<!--              <a-form-item label="张拉状态" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <a-input-number v-decorator="['zhanglastatus']" placeholder="请输入张拉状态" style="width: 100%" />-->
<!--              </a-form-item>-->
<!--            </a-col>-->
<!--            <a-col :span="12">-->
<!--              <a-form-item label="提梁状态" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <a-input-number v-decorator="['tiliangstatus']" placeholder="请输入提梁状态" style="width: 100%" />-->
<!--              </a-form-item>-->
<!--            </a-col>-->
<!--          </a-row>-->

          <a-col v-if="showFlowSubmitButton" :span="24" style="text-align: center">
            <a-button @click="submitForm">提 交</a-button>
          </a-col>
        </a-row>
      </a-form>
    </j-form-container>
  </a-spin>
</template>

<script>

  import { httpAction, getAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import JFormContainer from '@/components/jeecg/JFormContainer'
  import JDate from '@/components/jeecg/JDate'
  import JselectDqDepart from '@comp/jeecgbiz/JselectDqDepart'
  import JSelectDqProjName from '@comp/jeecgbiz/JselectDqProjName'

  export default {
    name: 'ZhilianggongxuYdForm',
    components: {
      JFormContainer,
      JDate,
      JselectDqDepart,
      JSelectDqProjName
    },
    props: {
      //流程表单data
      formData: {
        type: Object,
        default: ()=>{},
        required: false
      },
      //表单模式：true流程表单 false普通表单
      formBpm: {
        type: Boolean,
        default: false,
        required: false
      },
      //表单禁用
      disabled: {
        type: Boolean,
        default: false,
        required: false
      }
    },
    data () {
      return {
        wiretiedata:[],
        jiaozhudata:[],
        jingzhidata:[],
        zhengyangdata:[],
        zhangladata:[],
        fengduandata:[],
        cunliangdata:[],
        wiretiestatus:'wait',
        jiaozhustatus:'wait',
        jingzhistatus:'wait',
        zhengyangstatus:'wait',
        zhanglastatus:'wait',
        // tiliangstatus:'wait',
        fengduanstatus:'wait',
        cunliangstatus:'wait',
        dictOptions1:[],
        form: this.$form.createForm(this),
        model: {},
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
          add: "/sys/sysDepartproject47/zlrenwudanadd",
          edit: "/sys/sysDepartproject47/zlrenwudanedit",
          queryById: "/zhiliangrenwudan/zhiliangrenwudan/queryById",
          list: '/system/shigongphb/list1',
          list1: "/zhiliangrenwudan/zhiliangrenwudan/list",
          list2:"/bangza/wiretieProcedure/list",
          list3:"/jiaozhu/pouringProcedure/list",
          list4:"/jingzhi/standingProcedure/list",
          list5:"/zhengyang/zhengyangProcedure/list",
          list6:"/zhanglaprocedure/zhanglaProcedure/list",
          // list7:"/tiliang/tiliangProcedure/list",
          list8:"/cunliang/beammanagementProcedure/list",
          list9:'/fengduanprocedure/fengduanProcedure/list'

        }
      }
    },
    computed: {
      formDisabled(){
        if(this.formBpm===true){
          if(this.formData.disabled===false){
            return false
          }
          return true
        }
        return this.disabled
      },
      showFlowSubmitButton(){
        if(this.formBpm===true){
          if(this.formData.disabled===false){
            return true
          }
        }
        return false
      }
    },
    created () {
      //如果是流程中表单，则需要加载流程表单data
      this.showFlowData();
      // this.peibiList();
    },
    methods: {
      add () {
        this.edit({});
      },
      edit (record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model))
            //'station','code','dattim','attribute','recipe','recipes','contract','customer','projname','projtype','projgrade','projarea','projadr','distance','conspos','pour','variety','betlev','filter','freeze','lands','cement','stone','bnsize','addliq','request','mixlast','mete','begtim','endtim','attamper','flag','note','createBy','sysOrgCode','isdel','status','jiaozhustatus','jingzhistatus','zhengyangstatus1','zhengyangstatus2','zhanglastatus','tiliangstatus','productiontime','uuid'
          this.gongxustatus();
          this.wiretieList();
          this.jiaozhuList();
          this.jingzhiList();
          this.zhengyangList();
          this.zhanglaList();
          // this.tiliangList();
          this.fengduanList();
          this.cunliangList();
        })
      },
      //渲染流程表单数据
      showFlowData(){
        if(this.formBpm === true){
          let params = {id:this.formData.dataId};
          getAction(this.url.queryById,params).then((res)=>{
            if(res.success){
              this.edit (res.result);
            }
          });
        }
      },
      submitForm () {
        const that = this;
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
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
            let formData = Object.assign(this.model, values);
            console.log("表单提交数据",formData)
            httpAction(httpurl,formData,method).then((res)=>{
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
      // peibiList:function(){ //配合比号（任务单编号）
      //   let params = {};
      //   getAction(this.url.list, params).then((res)=>{
      //     //console.log(res)
      //     if(res.success)  {
      //       let list=res.result;
      //       this.dictOptions1=[];
      //       list.forEach(re=>{
      //         this.dictOptions1.push({key:re.code,label:re.code})
      //       })
      //       // console.log(this.dictOptions1,"ccccccccccccccccccccccccccc")
      //     }
      //   });
      // },
      wiretieList:function (){//钢筋绑扎
        let params = {uuid:this.model.uuid};
        this.wiretiedata = []
        getAction(this.url.list2,params).then((res=>{
          if (res.success){
            let list = res.result.records
            console.log("data",list)
            list.forEach(re=>{
              this.wiretiedata.push(
                {starttime:re.starttime,endtime:re.endtime,responsible:re.responsible,createBy:re.createBy})
            })
          }
        }))
      },
      jiaozhuList:function (){//浇筑
        let params = {uuid:this.model.uuid};
        this.jiaozhudata = []
        getAction(this.url.list3,params).then((res=>{
          if (res.success){
            let list = res.result.records
            console.log("data",list)
            list.forEach(re=>{
              this.jiaozhudata.push(
                {starttime:re.starttime,endtime:re.endtime,mete:re.mete,createBy:re.createBy})
            })
          }
        }))
      },
      jingzhiList:function (){//静置
        let params = {uuid:this.model.uuid};
        this.jingzhidata = []
        getAction(this.url.list4,params).then((res=>{
          if (res.success){
            let list = res.result.records
            console.log("data",list)
            list.forEach(re=>{
              this.jingzhidata.push(
                {starttime:re.starttime,endtime:re.endtime,createBy:re.createBy})
            })
          }
        }))
      },
      zhengyangList:function (){//蒸养
        let params = {uuid:this.model.uuid};
        this.zhengyangdata = []
        getAction(this.url.list5,params).then((res=>{
          if (res.success){
            let list = res.result.records
            console.log("data",list)
            list.forEach(re=>{
              this.zhengyangdata.push(
                {starttime:re.starttime,endtime:re.endtime,temperature:re.temperature+'℃',humidity:re.humidity,createBy:re.createBy})
            })
          }
        }))
      },
      zhanglaList:function (){//张拉
        let params = {uuid:this.model.uuid};
        this.zhangladata = []
        getAction(this.url.list6,params).then((res=>{
          if (res.success){
            let list = res.result.records
            console.log("data",list)
            list.forEach(re=>{
              this.zhangladata.push(
                {starttime:re.starttime,endtime:re.endtime,createBy:re.createBy})
            })
          }
        }))
      },
      // tiliangList:function (){//提梁
      //   let params = {uuid:this.model.uuid};
      //   this.tiliangdata = []
      //   getAction(this.url.list7,params).then((res=>{
      //     if (res.success){
      //       let list = res.result.records
      //       console.log("data",list)
      //       list.forEach(re=>{
      //         this.tiliangdata.push(
      //           {endtime:re.endtime,createBy:re.createBy})
      //       })
      //     }
      //   }))
      // },
      fengduanList:function (){//封端
        let params = {uuid:this.model.uuid};
        this.fengduandata = []
        getAction(this.url.list9,params).then((res=>{
          if (res.success){
            let list = res.result.records
            console.log("data",list)
            list.forEach(re=>{
              this.fengduandata.push(
                {starttime:re.starttime,endtime:re.endtime,createBy:re.createBy})
            })
          }
        }))
      },
      cunliangList:function (){//存梁
        let params = {uuid:this.model.uuid};
        this.cunliangdata = []
        getAction(this.url.list8,params).then((res=>{
          if (res.success){
            let list = res.result.records
            console.log("data",list)
            list.forEach(re=>{
              this.cunliangdata.push(
                {cuntime:re.cuntime,chutime:re.chutime,cunpeople:re.cunpeople,
                  chupeople:re.chupeople,liangceng:re.liangceng,cunlianghang:re.cunlianghang,
                  cunlianglie:re.cunlianglie,shebeino:re.shebeino_dictText})
            })
          }
        }))
      },
      gongxustatus:function (){
        let params = {id:this.model.id};
        getAction(this.url.list1,params).then((res)=>{
          if (res.success){
            this.wiretiestatus = 'wait'
            this.jiaozhustatus = 'wait'
            this.jingzhistatus = 'wait'
            this.zhengyangstatus = 'wait'
            this.zhanglastatus = 'wait'
            this.fengduanstatus = 'wait'
            this.cunliangstatus = 'wait'
            let data = res.result.records;
            console.log("data",data)
            if (data[0].wiretiestatus == 0){
              this.wiretiestatus = 'wait'
            }else if (data[0].wiretiestatus == 1){
              this.wiretiestatus = 'process'
            }else {
              this.wiretiestatus = 'finish'
            }
            if (data[0].jiaozhustatus == 0){
              this.jiaozhustatus = 'wait'
            }else if (data[0].jiaozhustatus == 1){
              this.jiaozhustatus = 'process'
            }else {
              this.jiaozhustatus = 'finish'
            }
            if (data[0].jingzhistatus == 0){
              this.jingzhistatus = 'wait'
            }else if (data[0].jingzhistatus == 1){
              this.jingzhistatus = 'process'
            }else {
              this.jingzhistatus = 'finish'
            }
            if (data[0].zhengyangstatus1 == 0 && data[0].zhengyangstatus2 == 0){
              this.zhengyangstatus = 'wait'
            }else if (data[0].zhengyangstatus1 == 1 || data[0].zhengyangstatus2 == 1){
              this.zhengyangstatus = 'process'
            }else if (data[0].zhengyangstatus1 == 2 || data[0].zhengyangstatus2 == 2){
              this.zhengyangstatus = 'finish'
            }
            if (data[0].zhanglastatus == 0){
              this.zhanglastatus = 'wait'
            }else if (data[0].zhanglastatus == 1){
              this.zhanglastatus = 'process'
            }else {
              this.zhanglastatus = 'finish'
            }
            if (data[0].fengduanstatus == 0){
              this.fengduanstatus = 'wait'
            }else if (data[0].fengduanstatus == 1){
              this.fengduanstatus = 'process'
            }else {
              this.fengduanstatus = 'finish'
            }
            if (data[0].cunliangstatus == 0){
              this.cunliangstatus = 'wait'
            }else if (data[0].cunliangstatus == 1){
              this.cunliangstatus = 'process'
            }else {
              this.cunliangstatus = 'finish'
            }
          }
        })
      },
      popupCallback(row){
        this.form.setFieldsValue(pick(row))
          //'station','code','dattim','attribute','recipe','recipes','contract','customer','projname','projtype','projgrade','projarea','projadr','distance','conspos','pour','variety','betlev','filter','freeze','lands','cement','stone','bnsize','addliq','request','mixlast','mete','begtim','endtim','attamper','flag','note','createBy','sysOrgCode','isdel','status','jiaozhustatus','jingzhistatus','zhengyangstatus1','zhengyangstatus2','zhanglastatus','tiliangstatus','productiontime','uuid'
      },
    }
  }
</script>
<style>
.biank{
  border: 1px solid #F0F0F0;
  padding: 5px;
  margin-top: 5px;
  border-radius: 5px;
  background-color: #F0F0F0;
  /*color: #faad14;*/
}
.endtime{
  /*color: #222222;*/

}

</style>