<template>
  <a-spin :spinning="confirmLoading">
    <div v-for="(item,index) in codeUrl" :key="index" style="float: right">
      <vue-qr
        :correctLevel="3"
        :autoColor="false"
        colorDark="black"
        :text="item.codes"
        :size="150"
        :margin="10">
      </vue-qr>
    </div>
    <j-form-container :disabled="formDisabled">
      <a-form :form="form" slot="detail">
        <a-card title="任务单信息" :bordered="false" :headStyle="{color:'#0785fd'}" :bodyStyle="{padding:'10'}">
          <a-row>
            <a-col :span="12">
              <a-form-item label="设备名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-search-select-tag placeholder="请选择设备名称" v-decorator="['shebeibianhao', validatorRules.shebeibianhao]"
                                     :dictOptions="dictOption" @change="handleChangeshebei">
                </j-search-select-tag>
                {{ selectValue }}
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="任务单编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-select placeholder="请选择任务单编号" v-decorator="['renwudan', validatorRules.renwudan]"
                          @change="handleChange">
                  <a-select-option v-for="(item, key) in dictOptions1" :key="item.key">
                  <span style="display: inline-block;width: 100%">
                    {{ item.label }}
                  </span>
                  </a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item label="工程名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['projname']" placeholder="请输入工程名称" :disabled="true"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="施工部位" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['conspos']" placeholder="请输入施工部位" :disabled="true"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item label="计划方量" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['mete']" placeholder="请输入计划方量"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="生产线" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-dict-select-tag type="list" v-decorator="['station']" :trigger-change="true" dictCode="station"
                                   placeholder="生产线" :disabled="true"/>
              </a-form-item>
            </a-col>
          </a-row>
          <a-col :span="12">
            <a-form-item label="制梁任务单编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <!--              <a-input v-decorator="['zhiliangcode']" placeholder="请输入制梁任务单编号" :disabled="true"></a-input>-->
              <a-select placeholder="请选择制梁任务单编号" v-decorator="['zhiliangcode']">
                <a-select-option v-for="(item, key) in dictOptions12" :key="item.key">
                  <span style="display: inline-block;width: 100%">
                    {{ item.label }}
                  </span>
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
        </a-card>

        <!--          <a-col :span="12">-->
        <!--            <a-form-item label="施工季节" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
        <!--              <a-input v-decorator="['season']" placeholder="请输入施工季节"></a-input>-->
        <!--            </a-form-item>-->
        <!--          </a-col>-->

        <a-card title="基础信息" :bordered="false" :headStyle="{color:'#0785fd'}" :bodyStyle="{padding:'10'}">
          <a-row>
            <a-col :span="12">
              <a-form-item label="浇筑方式" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-dict-select-tag type="list" v-decorator="['pour']" :trigger-change="true" dictCode="pour"
                                   placeholder="请选择浇筑方式" :disabled="true"/>
              </a-form-item>
            </a-col>
            <!--          <a-col :span="12">-->
            <!--            <a-form-item label="生产线" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
            <!--              <j-dict-select-tag type="list" v-decorator="['station']" :trigger-change="true" dictCode="station" placeholder="生产线" disabled="true"/>-->
            <!--            </a-form-item>-->
            <!--          </a-col>-->
            <a-col :span="12">
              <a-form-item label="理论配合比号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-select placeholder="请选择理论配合比号" v-decorator="['code1', validatorRules.code1]" @change="handleChange1">
                  <a-select-option v-for="(item, key) in dictOptions2" :key="item.key">
                  <span style="display: inline-block;width: 100%">
                    {{ item.label }}
                  </span>
                  </a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <!--          <a-col :span="12">-->
            <!--            <a-form-item label="砼标记" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
            <!--              <a-input v-decorator="['tag']" placeholder="请输入砼标记"></a-input>-->
            <!--            </a-form-item>-->
            <!--          </a-col>-->
            <a-col :span="12">
              <a-form-item label="强度等级" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-dict-select-tag type="list" v-decorator="['betlev']" :trigger-change="true" dictCode="betlev"
                                   placeholder="请选择强度等级" :disabled="true"/>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="抗渗等级" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['filter']" placeholder="请输入抗渗等级" :disabled="true"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item label="抗冻等级" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['freeze']" placeholder="请输入抗冻等级" :disabled="true"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="坍落度" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-dict-select-tag type="list" v-decorator="['lands']" :trigger-change="true" dictCode="lands"
                                   placeholder="请选择坍落度" :disabled="true"/>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item label="搅拌时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['mixlast']" placeholder="请输入搅拌时间" style="width: 100%"/>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="其他要求" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['otherreq']" placeholder="请输入其他要求"></a-input>
              </a-form-item>
            </a-col>
            <!--          <a-col :span="12">-->
            <!--            <a-form-item label="设计比例" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
            <!--              <a-input v-decorator="['scale']" placeholder="请输入设计比例"></a-input>-->
            <!--            </a-form-item>-->
            <!--          </a-col>-->
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item label="试验员" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['exper']" placeholder="请输入试验员"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="审核员" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['assoss']" placeholder="请输入审核员"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item label="制定日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-date placeholder="请选择制定日期" v-decorator="['dattim']" :trigger-change="true" style="width: 100%"
                        dateFormat="YYYY-MM-DD HH:mm:ss"/>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="标识" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['flag']" placeholder="请输入标识"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['note']" placeholder="请输入备注"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="水胶比" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['waterbindratio']" placeholder="请输入水胶比"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
        </a-card>

        <a-card title="原材料信息" :bordered="false" :headStyle="{color:'#0785fd'}" :bodyStyle="{textAlign:'center'}">
          <a-row>
            <a-col :span="2">
              材料
            </a-col>
            <a-col :span="1">
              材料(1)
            </a-col>
            <a-col :span="1">
              材料(2)
            </a-col>
            <a-col :span="2">
              材料(3)
            </a-col>
            <a-col :span="2">
              材料(4)
            </a-col>
            <a-col :span="2">
              材料(5)
            </a-col>
            <a-col :span="2">
              材料(6)
            </a-col>
            <a-col :span="2">
              材料(7)
            </a-col>
            <a-col :span="2">
              材料(8)
            </a-col>
            <a-col :span="2">
              材料(9)
            </a-col>
            <a-col :span="2">
              材料(10)
            </a-col>
            <a-col :span="2">
              材料(11)
            </a-col>
            <!--            <a-col :span="1">-->
            <!--              外加剂(3)-->
            <!--            </a-col>-->
            <a-col :span="2">
              材料(12)
            </a-col>
          </a-row>
          <br/>
          <a-row>
            <a-col :span="2">
              <a-form-item>
                材料名称
              </a-form-item>
            </a-col>
            <a-col :span="1">
              <a-form-item>
                <a-input v-decorator="['m1']"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="1">
              <a-form-item>
                <a-input v-decorator="['m13']"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="2">
              <a-form-item>
                <a-input v-decorator="['m2']"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="2">
              <a-form-item>
                <a-input v-decorator="['m3']"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="2">
              <a-form-item>
                <a-input v-decorator="['m4']"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="2">
              <a-form-item>
                <a-input v-decorator="['m5']"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="2">
              <a-form-item>
                <a-input v-decorator="['m6']"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="2">
              <a-form-item>
                <a-input v-decorator="['m7']"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="2">
              <a-form-item>
                <a-input v-decorator="['m12']"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="2">
              <a-form-item>
                <a-input v-decorator="['m8']"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="2">
              <a-form-item>
                <a-input v-decorator="['m9']"></a-input>
              </a-form-item>
            </a-col>
            <!--            <a-col :span="1">-->
            <!--              <a-form-item >-->
            <!--                <a-input v-decorator="['m10']" :disabled="true"></a-input>-->
            <!--              </a-form-item>-->
            <!--            </a-col>-->
            <a-col :span="2">
              <a-form-item>
                <a-input v-decorator="['m11']"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="2">
              <a-form-item>
                料仓名称
              </a-form-item>
            </a-col>
            <a-col :span="1">
              <a-form-item>
                <j-search-select-tag style="width: 300%" v-decorator="['lc1',validatorRules.lc1]"
                                     :dictOptions="dictOptions3">
                </j-search-select-tag>
                {{ selectValue1 }}
              </a-form-item>
            </a-col>
            <a-col :span="1">
              <a-form-item>
                <j-search-select-tag style="width: 300%" v-decorator="['lc13',validatorRules.lc13]"
                                     :dictOptions="dictOptions3">
                </j-search-select-tag>
                {{ selectValue13 }}
              </a-form-item>
            </a-col>
            <a-col :span="2">
              <a-form-item>
                <j-search-select-tag style="width: 200%" v-decorator="['lc2',validatorRules.lc2]"
                                     :dictOptions="dictOptions4">
                </j-search-select-tag>
                {{ selectValue2 }}
              </a-form-item>
            </a-col>
            <a-col :span="2">
              <a-form-item>
                <j-search-select-tag style="width: 200%" v-decorator="['lc3',validatorRules.lc3]"
                                     :dictOptions="dictOptions5">
                </j-search-select-tag>
                {{ selectValue3 }}
              </a-form-item>
            </a-col>
            <a-col :span="2">
              <a-form-item>
                <j-search-select-tag style="width: 200%" v-decorator="['lc4',validatorRules.lc4]"
                                     :dictOptions="dictOptions7">
                </j-search-select-tag>
                {{ selectValue4 }}
              </a-form-item>
            </a-col>
            <a-col :span="2">
              <a-form-item>
                <j-search-select-tag style="width: 200%" v-decorator="['lc5',validatorRules.lc5]"
                                     :dictOptions="dictOptions7">
                </j-search-select-tag>
                {{ selectValue5 }}
              </a-form-item>
            </a-col>
            <a-col :span="2">
              <a-form-item>
                <j-search-select-tag style="width: 200%" v-decorator="['lc6',validatorRules.lc6]"
                                     :dictOptions="dictOptions7">
                </j-search-select-tag>
                {{ selectValue6 }}
              </a-form-item>
            </a-col>
            <a-col :span="2">
              <a-form-item>
                <j-search-select-tag style="width: 200%" v-decorator="['lc7',validatorRules.lc7]"
                                     :dictOptions="dictOptions7">
                </j-search-select-tag>
                {{ selectValue7 }}
              </a-form-item>
            </a-col>
            <a-col :span="2">
              <a-form-item>
                <j-search-select-tag style="width: 200%" v-decorator="['lc12',validatorRules.lc12]"
                                     :dictOptions="dictOptions7">
                </j-search-select-tag>
                {{ selectValue12 }}
              </a-form-item>
            </a-col>
            <a-col :span="2">
              <a-form-item>
                <j-search-select-tag style="width: 200%" v-decorator="['lc8',validatorRules.lc8]"
                                     :dictOptions="dictOptions8">
                </j-search-select-tag>
                {{ selectValue8 }}
              </a-form-item>
            </a-col>
            <a-col :span="2">
              <a-form-item>
                <j-search-select-tag style="width: 200%" v-decorator="['lc9',validatorRules.lc9]"
                                     :dictOptions="dictOptions8">
                </j-search-select-tag>
                {{ selectValue9 }}
              </a-form-item>
            </a-col>
            <!--            <a-col :span="1">-->
            <!--              <a-form-item >-->
            <!--                <j-search-select-tag style="width: 100px" v-decorator="['lc10']" :dictOptions="dictOptions8" >-->
            <!--                </j-search-select-tag>-->
            <!--                {{ selectValue10 }}-->
            <!--              </a-form-item>-->
            <!--            </a-col>-->
            <a-col :span="2">
              <a-form-item>
                <j-search-select-tag v-decorator="['lc11',validatorRules.lc11]" :dictOptions="dictOptions9">
                </j-search-select-tag>
                {{ selectValue11 }}
              </a-form-item>
            </a-col>
          </a-row>
          <!--          <a-row>-->
          <!--            <a-col :span="2">-->
          <!--              <a-form-item>-->
          <!--                批次-->
          <!--              </a-form-item>-->
          <!--            </a-col>-->
          <!--            <a-col :span="2">-->
          <!--              <a-form-item >-->
          <!--                <j-multi-select-tag   v-decorator="['pici1']" :options="dictOption2" @change="handlepici1">-->
          <!--                </j-multi-select-tag>-->
          <!--                {{ selectValues1 }}-->
          <!--              </a-form-item>-->
          <!--            </a-col>-->
          <!--            <a-col :span="2">-->
          <!--              <a-form-item >-->
          <!--                <j-multi-select-tag  v-decorator="['pici2']" :options="dictOption3" @change="handlepici2">-->
          <!--                </j-multi-select-tag>-->
          <!--                {{ selectValues2 }}-->
          <!--              </a-form-item>-->
          <!--            </a-col>-->
          <!--            <a-col :span="2">-->
          <!--              <a-form-item >-->
          <!--                <j-multi-select-tag  v-decorator="['pici3']" :options="dictOption4" @change="handlepici3">-->
          <!--                </j-multi-select-tag>-->
          <!--                {{ selectValues3 }}-->
          <!--              </a-form-item>-->
          <!--            </a-col>-->
          <!--            <a-col :span="2">-->
          <!--              <a-form-item >-->
          <!--                <j-multi-select-tag  v-decorator="['pici4']" :options="dictOption5" @change="handlepici4">-->
          <!--                </j-multi-select-tag>-->
          <!--                {{ selectValues4 }}-->
          <!--              </a-form-item>-->
          <!--            </a-col>-->
          <!--            <a-col :span="2">-->
          <!--              <a-form-item >-->
          <!--                <j-multi-select-tag  v-decorator="['pici5']" :options="dictOption6" @change="handlepici5">-->
          <!--                </j-multi-select-tag>-->
          <!--                {{ selectValues5 }}-->
          <!--              </a-form-item>-->
          <!--            </a-col>-->
          <!--            <a-col :span="2">-->
          <!--              <a-form-item >-->
          <!--                <j-multi-select-tag  v-decorator="['pici6']" :options="dictOption7" @change="handlepici6">-->
          <!--                </j-multi-select-tag>-->
          <!--                {{ selectValues6 }}-->
          <!--              </a-form-item>-->
          <!--            </a-col>-->
          <!--            <a-col :span="2">-->
          <!--              <a-form-item >-->
          <!--                <j-multi-select-tag  v-decorator="['pici7']" :options="dictOption8" @change="handlepici7">-->
          <!--                </j-multi-select-tag>-->
          <!--                {{ selectValues7 }}-->
          <!--              </a-form-item>-->
          <!--            </a-col>-->
          <!--            <a-col :span="2">-->
          <!--              <a-form-item >-->
          <!--                <j-multi-select-tag  v-decorator="['pici8']" :options="dictOption9" @change="handlepici8">-->
          <!--                </j-multi-select-tag>-->
          <!--                {{ selectValues8 }}-->
          <!--              </a-form-item>-->
          <!--            </a-col>-->
          <!--            <a-col :span="2">-->
          <!--              <a-form-item >-->
          <!--                <j-multi-select-tag  v-decorator="['pici9']" :options="dictOption10" @change="handlepici9">-->
          <!--                </j-multi-select-tag>-->
          <!--                {{ selectValues9 }}-->
          <!--              </a-form-item>-->
          <!--            </a-col>-->
          <!--            <a-col :span="2">-->
          <!--              <a-form-item >-->
          <!--                <j-multi-select-tag v-decorator="['pici10']" :options="dictOption11"@change="handlepici10">-->
          <!--                </j-multi-select-tag>-->
          <!--                {{ selectValues10 }}-->
          <!--              </a-form-item>-->
          <!--            </a-col>-->
          <!--            <a-col :span="2">-->
          <!--              <a-form-item >-->
          <!--                <j-multi-select-tag  v-decorator="['pici11']" :options="dictOption12" @change="handlepici11">-->
          <!--                </j-multi-select-tag>-->
          <!--                {{ selectValues11 }}-->
          <!--              </a-form-item>-->
          <!--            </a-col>-->
          <!--          </a-row>-->
          <a-row>
            <a-col :span="2">
              <a-form-item>
                理论用量
              </a-form-item>
            </a-col>
            <a-col :span="1">
              <a-form-item>
                <a-input-number :precision="2" v-decorator="['u1']" style="width: 100%"
                ></a-input-number>
              </a-form-item>
            </a-col>
            <a-col :span="1">
              <a-form-item>
                <a-input-number :precision="2" v-decorator="['u13']" style="width: 100%"
                ></a-input-number>
              </a-form-item>
            </a-col>
            <a-col :span="2">
              <a-form-item>
                <a-input-number :precision="2" v-decorator="['u2']" style="width: 100%"
                ></a-input-number>
              </a-form-item>
            </a-col>
            <a-col :span="2">
              <a-form-item>
                <a-input-number :precision="2" v-decorator="['u3']" style="width: 100%"
                ></a-input-number>
              </a-form-item>
            </a-col>
            <a-col :span="2">
              <a-form-item>
                <a-input-number :precision="2" v-decorator="['u4']" style="width: 100%"
                ></a-input-number>
              </a-form-item>
            </a-col>
            <a-col :span="2">
              <a-form-item>
                <a-input-number :precision="2" v-decorator="['u5']" style="width: 100%"
                ></a-input-number>
              </a-form-item>
            </a-col>
            <a-col :span="2">
              <a-form-item>
                <a-input-number :precision="2" v-decorator="['u6']" style="width: 100%"
                ></a-input-number>
              </a-form-item>
            </a-col>
            <a-col :span="2">
              <a-form-item>
                <a-input-number :precision="2" v-decorator="['u7']" style="width: 100%"
                ></a-input-number>
              </a-form-item>
            </a-col>
            <a-col :span="2">
              <a-form-item>
                <a-input-number :precision="2" v-decorator="['u12']" style="width: 100%"
                ></a-input-number>
              </a-form-item>
            </a-col>
            <a-col :span="2">
              <a-form-item>
                <a-input-number :precision="2" v-decorator="['u8']" style="width: 100%"
                ></a-input-number>
              </a-form-item>
            </a-col>
            <a-col :span="2">
              <a-form-item>
                <a-input-number :precision="2" v-decorator="['u9']" style="width: 100%"
                ></a-input-number>
              </a-form-item>
            </a-col>
            <!--            <a-col :span="1">-->
            <!--              <a-form-item >-->
            <!--                <a-input-number :precision="2"  v-decorator="['u10']" style="width: 100%" :disabled="true"></a-input-number>-->
            <!--              </a-form-item>-->
            <!--            </a-col>-->
            <a-col :span="2">
              <a-form-item>
                <a-input-number :precision="2" v-decorator="['u11']" style="width: 100%"
                ></a-input-number>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="2">
              <a-form-item>
                含水率(%)
              </a-form-item>
            </a-col>
            <a-col :span="1">
              <a-form-item>
                <a-input-number style="width: 100%" :disabled="true"></a-input-number>
              </a-form-item>
            </a-col>
            <a-col :span="1">
              <a-form-item>
                <a-input-number style="width: 100%" :disabled="true"></a-input-number>
              </a-form-item>
            </a-col>
            <a-col :span="2">
              <a-form-item>
                <a-input-number style="width: 100%" :disabled="true"></a-input-number>
              </a-form-item>
            </a-col>
            <a-col :span="2">
              <a-form-item>
                <a-input-number style="width: 100%" :disabled="true"></a-input-number>
              </a-form-item>
            </a-col>
            <a-col :span="2">
              <a-form-item>
                <a-input-number :defaultValue="0" v-decorator="['mc4']" style="width: 100%"
                                @blur="onChange1"></a-input-number>
              </a-form-item>
            </a-col>
            <a-col :span="2">
              <a-form-item>
                <a-input-number v-decorator="['mc5']" style="width: 100%" @blur="onChange2"></a-input-number>
              </a-form-item>
            </a-col>
            <a-col :span="2">
              <a-form-item>
                <a-input-number v-decorator="['mc6']" style="width: 100%" @blur="onChange3"></a-input-number>
              </a-form-item>
            </a-col>
            <a-col :span="2">
              <a-form-item>
                <a-input-number v-decorator="['mc7']" style="width: 100%" @blur="onChange4"></a-input-number>
              </a-form-item>
            </a-col>
            <a-col :span="2">
              <a-form-item>
                <a-input-number v-decorator="['mc12']" style="width: 100%" @blur="onChange7"></a-input-number>
              </a-form-item>
            </a-col>
            <a-col :span="2">
              <a-form-item>
                <a-input-number v-decorator="['mc8']" style="width: 100%" @blur="onChange"></a-input-number>
              </a-form-item>
            </a-col>
            <a-col :span="2">
              <a-form-item>
                <a-input-number v-decorator="['mc9']" style="width: 100%" @blur="onChange5"></a-input-number>
              </a-form-item>
            </a-col>
            <!--            <a-col :span="1">-->
            <!--              <a-form-item >-->
            <!--                <a-input-number v-decorator="['mc10']" style="width: 100%" @blur="onChange6"></a-input-number>-->
            <!--              </a-form-item>-->
            <!--            </a-col>-->
            <a-col :span="2">
              <a-form-item>
                <a-input-number v-decorator="['mc11']" style="width: 100%" :disabled="true"></a-input-number>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="2">
              <a-form-item>
                含水量(kg)
              </a-form-item>
            </a-col>
            <a-col :span="1">
              <a-form-item>
                <a-input-number style="width: 100%" :disabled="true"></a-input-number>
              </a-form-item>
            </a-col>
            <a-col :span="1">
              <a-form-item>
                <a-input-number style="width: 100%" :disabled="true"></a-input-number>
              </a-form-item>
            </a-col>
            <a-col :span="2">
              <a-form-item>
                <a-input-number style="width: 100%" :disabled="true"></a-input-number>
              </a-form-item>
            </a-col>
            <a-col :span="2">
              <a-form-item>
                <a-input-number style="width: 100%" :disabled="true"></a-input-number>
              </a-form-item>
            </a-col>
            <a-col :span="2">
              <a-form-item>
                <a-input-number :precision="0" v-decorator="['mcl4']" style="width: 100%"
                                :disabled="true"></a-input-number>
              </a-form-item>
            </a-col>
            <a-col :span="2">
              <a-form-item>
                <a-input-number :precision="0" v-decorator="['mcl5']" style="width: 100%"
                                :disabled="true"></a-input-number>
              </a-form-item>
            </a-col>
            <a-col :span="2">
              <a-form-item>
                <a-input-number :precision="0" v-decorator="['mcl6']" style="width: 100%"
                                :disabled="true"></a-input-number>
              </a-form-item>
            </a-col>
            <a-col :span="2">
              <a-form-item>
                <a-input-number :precision="0" v-decorator="['mcl7']" style="width: 100%"
                                :disabled="true"></a-input-number>
              </a-form-item>
            </a-col>
            <a-col :span="2">
              <a-form-item>
                <a-input-number :precision="0" v-decorator="['mcl12']" style="width: 100%"
                                :disabled="true"></a-input-number>
              </a-form-item>
            </a-col>
            <a-col :span="2">
              <a-form-item>
                <a-input-number :precision="0" v-decorator="['mcl8']" style="width: 100%"
                                :disabled="true"></a-input-number>
              </a-form-item>
            </a-col>
            <a-col :span="2">
              <a-form-item>
                <a-input-number :precision="0" v-decorator="['mcl9']" style="width: 100%"
                                :disabled="true"></a-input-number>
              </a-form-item>
            </a-col>
            <!--            <a-col :span="1">-->
            <!--              <a-form-item >-->
            <!--                <a-input-number :precision="2"  v-decorator="['mcl10']" style="width: 100%" :disabled="true"></a-input-number>-->
            <!--              </a-form-item>-->
            <!--            </a-col>-->
            <a-col :span="2">
              <a-form-item>
                <a-input-number :precision="0" v-decorator="['mcl11']" style="width: 100%"
                                :disabled="true"></a-input-number>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="2">
              <a-form-item>
                施工用量
              </a-form-item>
            </a-col>
            <a-col :span="1">
              <a-form-item>
                <a-input-number v-decorator="['ru1']" :precision="0" style="width: 100%" />
              </a-form-item>
            </a-col>
            <a-col :span="1">
              <a-form-item>
                <a-input-number style="width: 100%" :precision="0" v-decorator="['ru13']"></a-input-number>
              </a-form-item>
            </a-col>
            <a-col :span="2">
              <a-form-item>
                <a-input-number style="width: 100%" :precision="0" v-decorator="['ru2']"></a-input-number>
              </a-form-item>
            </a-col>
            <a-col :span="2">
              <a-form-item>
                <a-input-number style="width: 100%" :precision="0" v-decorator="['ru3']"></a-input-number>
              </a-form-item>
            </a-col>
            <a-col :span="2">
              <a-form-item>
                <a-input-number style="width: 100%" :precision="0" v-decorator="['ru4']"></a-input-number>
              </a-form-item>
            </a-col>
            <a-col :span="2">
              <a-form-item>
                <a-input-number style="width: 100%" :precision="0" v-decorator="['ru5']"></a-input-number>
              </a-form-item>
            </a-col>
            <a-col :span="2">
              <a-form-item>
                <a-input-number style="width: 100%" :precision="0" v-decorator="['ru6']"></a-input-number>
              </a-form-item>
            </a-col>
            <a-col :span="2">
              <a-form-item>
                <a-input-number style="width: 100%" :precision="0" v-decorator="['ru7']"></a-input-number>
              </a-form-item>
            </a-col>
            <a-col :span="2">
              <a-form-item>
                <a-input-number style="width: 100%" :precision="0" v-decorator="['ru12']"></a-input-number>
              </a-form-item>
            </a-col>
            <a-col :span="2">
              <a-form-item>
                <a-input-number style="width: 100%" :precision="2" v-decorator="['ru8']"></a-input-number>
              </a-form-item>
            </a-col>
            <a-col :span="2">
              <a-form-item>
                <a-input-number style="width: 100%" :precision="2" v-decorator="['ru9']"></a-input-number>
              </a-form-item>
            </a-col>
            <!--            <a-col :span="1">-->
            <!--              <a-form-item >-->
            <!--                <a-input-number   style="width: 100%" :precision="2"  v-decorator="['ru10']" ></a-input-number  >-->
            <!--              </a-form-item>-->
            <!--            </a-col>-->
            <a-col :span="2">
              <a-form-item>
                <a-input-number style="width: 100%" :precision="0" v-decorator="['ru11']"></a-input-number>
              </a-form-item>
            </a-col>
          </a-row>
        </a-card>
        <a-row>
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
import { SYS_DEPART_ORGCODE } from '@/store/mutation-types'
import { duplicateCheck2, usershebeiList } from '@api/api'
import Vue from 'vue'
import VueQr from 'vue-qr'

export default {
  name: 'ShigongphbForm',
  components: {
    JFormContainer,
    JDate,
    VueQr,
  },
  props: {
    //流程表单data
    formData: {
      type: Object,
      default: () => {
      },
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
  data() {
    return {
      fuzhi: 0,
      selectValue1: '',
      selectValue2: '',
      selectValue3: '',
      selectValue4: '',
      selectValue5: '',
      selectValue6: '',
      selectValue7: '',
      selectValue8: '',
      selectValue9: '',
      // selectValue10:'',
      selectValue11: '',
      selectValue12: '',
      selectValue13: '',
      // selectValues1:'',
      // selectValues2:'',
      // selectValues3:'',
      // selectValues4:'',
      // selectValues5:'',
      // selectValues6:'',
      // selectValues7:'',
      // selectValues8:'',
      // selectValues9:'',
      // selectValues10:'',
      // selectValues11:'',
      // selectValues12:'',
      // selectValues13:'',
      // dictOption1:[],
      // dictOption2:[],
      // dictOption3:[],
      // dictOption4:[],
      // dictOption5:[],
      // dictOption6:[],
      // dictOption7:[],
      // dictOption8:[],
      // dictOption9:[],
      // dictOption10:[],
      // dictOption11:[],
      // dictOption12:[],
      codeUrl: [],
      dataSource: [],
      dataSource1: [],
      dataSource2: [],
      selectValue: '',
      asyncSelectValue: '',
      selectedValue: null,
      // selectedValue1:'',
      // selectedValue2:'',
      // selectedValue3:'',
      // selectedValue4:'',
      // selectedValue5:'',
      // selectedValue6:'',
      // selectedValue7:'',
      // selectedValue8:'',
      // selectedValue9:'',
      // selectedValue10:'',
      // selectedValue11:'',
      // selectedValue12:'',
      // selectedValue13:'',
      dictOption: [],
      dictOptions1: [],
      dictOptions2: [],
      dictOptions3: [],
      dictOptions4: [],
      dictOptions5: [],
      dictOptions7: [],
      dictOptions8: [],
      dictOptions9: [],
      dictOptions10: [],
      dictOptions11: [],
      dictOptions12: [],
      projgrade1: '',
      // sumgljingzhongt:0,
      // sumjingzhongt4:0,
      form: this.$form.createForm(this),
      model: {},
      labelCol: {
        xs: { span: 24 },
        sm: { span: 6 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 },
      },
      labelCol1: {
        xs: { span: 24 },
        sm: { span: 10 },
      },
      wrapperCol1: {
        xs: { span: 24 },
        sm: { span: 12 },
      },
      wrapperCol2: {
        xs: { span: 24 },
        sm: { span: 24 },
        offset: 0
      },
      confirmLoading: false,
      validatorRules: {
        shebeibianhao: {
          rules: [{
            required: true, message: '请选择设备!'
          }]
        },
        renwudan: {
          rules: [{
            required: true, message: '请选择任务单!'
          }]
        },
        code1: {
          rules: [{
            required: true, message: '请选择理论配合比!'
          }]
        },
        // lc1: {
        //   rules: [
        //     { validator: this.validatelc1 },
        //   ]
        // },
        // lc13: {
        //   rules: [
        //     { validator: this.validatelc13 },
        //   ]
        // },
        // lc2: {
        //   rules: [
        //     { validator: this.validatelc2 },
        //   ]
        // },
        // lc3: {
        //   rules: [
        //     { validator: this.validatelc3 },
        //   ]
        // },
        // lc4: {
        //   rules: [
        //     { validator: this.validatelc4 },
        //   ]
        // },
        // lc5: {
        //   rules: [
        //     { validator: this.validatelc5 },
        //   ]
        // },
        // lc6: {
        //   rules: [
        //     { validator: this.validatelc6 },
        //   ]
        // },
        // lc7: {
        //   rules: [
        //     { validator: this.validatelc7 },
        //   ]
        // },
        // lc12: {
        //   rules: [
        //     { validator: this.validatelc12 },
        //   ]
        // },
        // lc8: {
        //   rules: [
        //     { validator: this.validatelc8 },
        //   ]
        // },
        // lc9: {
        //   rules: [
        //     { validator: this.validatelc9 },
        //   ]
        // },
        // lc11: {
        //   rules: [
        //     { validator: this.validatelc11 },
        //   ]
        // },
      },
      url: {
        add: '/system/shigongphb/add1',
        edit: '/system/shigongphb/edit',
        queryById: '/system/shigongphb/queryById',
        list: '/system/bhzrenwudan/bhzrwdlistss1',
        list1: '/bhzrecipe/bhzRecipe/list3',
        list2: '/bhzrecipe/bhzRecipe/queryBhzRecipedetailByMainId',
        list3: '/system/shigongphb/list1',
        list4: '/wzliaocang/wzliaocang/list5',
        list5: '/wztaizhang/wztaizhang/list3',
        list6: '/zhiliangrenwudan/zhiliangrenwudan/list9'
      }
    }
  },
  computed: {
    formDisabled() {
      if (this.formBpm === true) {
        if (this.formData.disabled === false) {
          return false
        }
        return true
      }
      return this.disabled
    },
    showFlowSubmitButton() {
      if (this.formBpm === true) {
        if (this.formData.disabled === false) {
          return true
        }
      }
      return false
    }
  },
  created() {
    //如果是流程中表单，则需要加载流程表单data
    this.showFlowData()
    this.shebeiList()
    this.peibiList()
    this.llpeibiList()
    this.liaocangList1()
    this.liaocangList2()
    this.liaocangList3()
    this.liaocangList5()
    this.liaocangList6()
    this.liaocangList7()
    this.zhiliangcodeList()
  },
  methods: {
    add() {
      this.edit({})
    },
    edit(record) {
      this.form.resetFields()
      this.model = Object.assign({}, record)
      this.qrcode(record)
      this.visible = true
      this.$nextTick(() => {
        this.form.setFieldsValue(pick(this.model, 'station', 'code', 'tag', 'betlev', 'filter', 'freeze', 'season', 'lands', 'mixlast', 'scale', 'otherreq', 'exper', 'assoss', 'dattim',
          'flag', 'note', 'm1', 'u1', 'm2', 'u2', 'm3', 'u3', 'm4', 'u4', 'm5', 'u5', 'm6', 'u6', 'm7', 'u7', 'm8', 'u8', 'm9', 'u9', 'm10', 'u10',
          'm11', 'u11', 'm12', 'u12', 'm13', 'u13', 'm14', 'u14', 'm15', 'u15', 'm16', 'u16', 'm17', 'u17', 'm18', 'u18', 'm19', 'u19', 'm20', 'u20',
          'm21', 'u21', 'm22', 'u22', 'm23', 'u23', 'm24', 'u24', 'm25', 'u25', 'm26', 'u26', 'm27', 'u27', 'm28', 'u28', 'm29', 'u29', 'm30', 'u30',
          'm31', 'u31', 'm32', 'u32', 'shebeibianhao', 'sysOrgCode', 'createBy', 'code1', 'projname', 'conspos', 'pour', 'mc4', 'mc5', 'mc6', 'mc7', 'mc8', 'mc9', 'mc10', 'ru4', 'ru5', 'ru6', 'ru7', 'ru8', 'ru9', 'ru10', 'renwudan',
          'mcl4', 'mcl5', 'mcl6', 'mcl7', 'mcl8', 'mcl9', 'mcl10', 'ru11', 'mete', 'ru1', 'ru2', 'ru3', 'ru12', 'ru13', 'waterbindratio', 'lc1', 'lc2', 'lc3', 'lc4', 'lc5', 'lc6', 'lc7', 'lc8', 'lc9', 'lc10', 'lc11',
          'pici1', 'pici2', 'pici3', 'pici4', 'pici5', 'pici6', 'pici7', 'pici8', 'pici9', 'pici10', 'pici11', 'zhiliangcode', 'lc12', 'lc13', 'mcl12', 'mc12'))
      })

    },

    editAdd(record) {
      this.form.resetFields()
      this.model = Object.assign({}, record)
      // this.qrcode(record)
      this.visible = true
      this.$nextTick(() => {
        this.form.setFieldsValue(pick(this.model, 'station', 'code', 'tag', 'betlev', 'filter', 'freeze', 'season', 'lands', 'mixlast', 'scale', 'otherreq', 'exper', 'assoss', 'dattim',
          'flag', 'note', 'm1', 'u1', 'm2', 'u2', 'm3', 'u3', 'm4', 'u4', 'm5', 'u5', 'm6', 'u6', 'm7', 'u7', 'm8', 'u8', 'm9', 'u9', 'm10', 'u10',
          'm11', 'u11', 'm12', 'u12', 'm13', 'u13', 'm14', 'u14', 'm15', 'u15', 'm16', 'u16', 'm17', 'u17', 'm18', 'u18', 'm19', 'u19', 'm20', 'u20',
          'm21', 'u21', 'm22', 'u22', 'm23', 'u23', 'm24', 'u24', 'm25', 'u25', 'm26', 'u26', 'm27', 'u27', 'm28', 'u28', 'm29', 'u29', 'm30', 'u30',
          'm31', 'u31', 'm32', 'u32', 'shebeibianhao', 'sysOrgCode', 'createBy', 'code1', 'projname', 'conspos', 'pour', 'mc4', 'mc5', 'mc6', 'mc7', 'mc8', 'mc9', 'mc10', 'ru4', 'ru5', 'ru6', 'ru7', 'ru8', 'ru9', 'ru10', 'renwudan',
          'mcl4', 'mcl5', 'mcl6', 'mcl7', 'mcl8', 'mcl9', 'mcl10', 'ru11', 'mete', 'ru1', 'ru2', 'ru3', 'ru12', 'ru13', 'waterbindratio', 'lc1', 'lc2', 'lc3', 'lc4', 'lc5', 'lc6', 'lc7', 'lc8', 'lc9', 'lc10', 'lc11',
          'pici1', 'pici2', 'pici3', 'pici4', 'pici5', 'pici6', 'pici7', 'pici8', 'pici9', 'pici10', 'pici11', 'zhiliangcode', 'lc12', 'lc13', 'mcl12', 'mc12'))
      })
      this.fuzhi = 1
    },

    //渲染流程表单数据
    showFlowData() {
      if (this.formBpm === true) {
        let params = { id: this.formData.dataId }
        getAction(this.url.queryById, params).then((res) => {
          if (res.success) {
            this.edit(res.result)
          }
        })
      }
    },
    submitForm() {
      const that = this
      // 触发表单验证
      this.form.validateFields((err, values) => {
        if (!err) {
          that.confirmLoading = true
          let httpurl = ''
          let method = ''
          if (!this.model.id || this.fuzhi == 1) {
            httpurl += this.url.add
            method = 'post'
          } else {
            httpurl += this.url.edit
            method = 'put'
          }
          let formData = Object.assign(this.model, values)
          //console.log('表单提交数据', formData)
          httpAction(httpurl, formData, method).then((res) => {
            if (res.success) {
              that.$message.success(res.message)
              that.$emit('ok')
            } else {
              that.$message.warning(res.message)
            }
          }).finally(() => {
            that.confirmLoading = false
          })
        }

      })
    },
    handleChangeshebei(selectedValue) {
      // this.dictOption = this.datalist
      console.log('selectedValue', selectedValue)
      this.selectedValue = selectedValue
      this.liaocangList1()
      this.liaocangList2()
      this.liaocangList3()
      this.liaocangList5()
      this.liaocangList6()
      this.liaocangList7()
      // this.callback()
    },
    // handleChangelc1(selectedValue){
    //   this.piciList1(selectedValue);
    // },
    // handleChangelc2(selectedValue){
    //   this.piciList2(selectedValue);
    // },
    // handleChangelc3(selectedValue){
    //   this.piciList3(selectedValue);
    // },
    // handleChangelc4(selectedValue){
    //   this.piciList4(selectedValue);
    // },
    // handleChangelc5(selectedValue){
    //   this.piciList5(selectedValue);
    // },
    // handleChangelc6(selectedValue){
    //   this.piciList6(selectedValue);
    // },
    // handleChangelc7(selectedValue){
    //   this.piciList7(selectedValue);
    // },
    // handleChangelc8(selectedValue){
    //   this.piciList8(selectedValue);
    // },
    // handleChangelc9(selectedValue){
    //   this.piciList9(selectedValue);
    // },
    // handleChangelc10(selectedValue){
    //   this.piciList10(selectedValue);
    // },
    // handleChangelc11(selectedValue){
    //   this.piciList11(selectedValue);
    // },
    validatelc1(rule, value, callback) {
      //console.log('rule', rule)
      //console.log('value', value)
      console.log('callback', callback)
      if (!value) {
        const form = this.form
        const m1 = form.getFieldValue('m1')
        //console.log('m1', m1)
        console.log('this.dictOptions3', this.dictOptions3)
        if (m1 != null && this.dictOptions3.length > 0) {
          callback('请输入料仓名称!')
        }
      } else {
        callback()
      }
    },
    validatelc13(rule, value, callback) {
      //console.log('rule', rule)
      //console.log('value', value)
      console.log('callback', callback)
      if (!value) {
        const form = this.form
        const m1 = form.getFieldValue('m13')
        //console.log('m1', m1)
        console.log('this.dictOptions3', this.dictOptions3)
        if (m1 != null && this.dictOptions3.length > 0) {
          callback('请输入料仓名称!')
        }
      } else {
        callback()
      }
    },
    validatelc2(rule, value, callback) {
      //console.log('rule', rule)
      //console.log('value', value)
      console.log('callback', callback)
      if (!value) {
        const form = this.form
        const m1 = form.getFieldValue('m2')
        // console.log('m1', m1)
        console.log('this.dictOptions4', this.dictOptions4)
        if (m1 != null && this.dictOptions4.length > 0) {
          callback('请输入料仓名称!')
        }
      } else {
        callback()
      }
    },
    validatelc3(rule, value, callback) {
      //console.log('rule', rule)
      //console.log('value', value)
      console.log('callback', callback)
      if (!value) {
        const form = this.form
        const m1 = form.getFieldValue('m3')
        console.log('this.dictOptions5', this.dictOptions5)
        //console.log('m1', m1)
        if (m1 != null && this.dictOptions5.length > 0) {
          callback('请输入料仓名称!')
        }
      } else {
        callback()
      }
    },
    validatelc4(rule, value, callback) {
      //console.log('rule', rule)
      // console.log('value', value)
      console.log('callback', callback)
      if (!value) {
        const form = this.form
        const m1 = form.getFieldValue('m4')
        // console.log('m1', m1)
        console.log('this.dictOptions7', this.dictOptions7)
        if (m1 != null && this.dictOptions7.length > 0) {
          callback('请输入料仓名称!')
        }
      } else {
        callback()
      }
    },
    validatelc5(rule, value, callback) {
      // console.log('rule', rule)
      // console.log('value', value)
      console.log('callback', callback)
      if (!value) {
        const form = this.form
        const m1 = form.getFieldValue('m5')
        // console.log('m1', m1)
        console.log('this.dictOptions7', this.dictOptions7)
        if (m1 != null && this.dictOptions7.length > 0) {
          callback('请输入料仓名称!')
        }
      } else {
        callback()
      }
    },
    validatelc6(rule, value, callback) {
      // console.log('rule', rule)
      // console.log('value', value)
      console.log('callback', callback)
      if (!value) {
        const form = this.form
        const m1 = form.getFieldValue('m6')
        // console.log('m1', m1)
        console.log('this.dictOptions7', this.dictOptions7)
        if (m1 != null && this.dictOptions7.length > 0) {
          callback('请输入料仓名称!')
        }
      } else {
        callback()
      }
    },
    validatelc7(rule, value, callback) {
      // console.log('rule', rule)
      // console.log('value', value)
      console.log('callback', callback)
      if (!value) {
        const form = this.form
        const m1 = form.getFieldValue('m7')
        // console.log('m1', m1)
        console.log('this.dictOptions7', this.dictOptions7)
        if (m1 != null && this.dictOptions7.length > 0) {
          callback('请输入料仓名称!')
        }
      } else {
        callback()
      }
    },
    validatelc12(rule, value, callback) {
      // console.log('rule', rule)
      // console.log('value', value)
      console.log('callback', callback)
      if (!value) {
        const form = this.form
        const m1 = form.getFieldValue('m12')
        // console.log('m1', m1)
        console.log('this.dictOptions7', this.dictOptions7)
        if (m1 != null && this.dictOptions7.length > 0) {
          callback('请输入料仓名称!')
        }
      } else {
        callback()
      }
    },
    validatelc8(rule, value, callback) {
      // console.log('rule', rule)
      // console.log('value', value)
      console.log('callback', callback)
      if (!value) {
        const form = this.form
        const m1 = form.getFieldValue('m8')
        // console.log('m1', m1)
        console.log('this.dictOptions8', this.dictOptions8)
        if (m1 != null && this.dictOptions8.length > 0) {
          callback('请输入料仓名称!')
        }
      } else {
        callback()
      }
    },
    validatelc9(rule, value, callback) {
      // console.log('rule', rule)
      // console.log('value', value)
      console.log('callback', callback)
      if (!value) {
        const form = this.form
        const m1 = form.getFieldValue('m9')
        // console.log('m1', m1)
        console.log('this.dictOptions8', this.dictOptions8)
        if (m1 != null && this.dictOptions8.length > 0) {
          callback('请输入料仓名称!')
        }
      } else {
        callback()
      }
    },
    validatelc11(rule, value, callback) {
      // console.log('rule', rule)
      // console.log('value', value)
      console.log('callback', callback)
      if (!value) {
        const form = this.form
        const m1 = form.getFieldValue('m11')
        // console.log('m1', m1)
        console.log('this.dictOptions9', this.dictOptions9)
        if (m1 != null && this.dictOptions9.length > 0) {
          callback('请输入料仓名称!')
        }
      } else {
        callback()
      }
    },
    liaocangList1: function () {//水泥/沥青
      this.dictOptions3 = []
      let params = { shebeino: this.selectedValue, cailiaono: '6' }
      getAction(this.url.list4, params).then(res => {
        if (res.success) {
          let list = res.result
          console.log('list水泥', list)
          for (let i = 0; i < list.length > 0; i++) {
            if (list[i].cailiaoname !== null && list[i].cailiaoname !== '') {
              this.dictOptions3.push({ text: list[i].name + '(' + list[i].cailiaoname + ')', value: list[i].guid })
            } else {
              this.dictOptions3.push({ text: list[i].name, value: list[i].guid })
            }
          }
        }
      })
    },
    liaocangList2: function () {//粉煤灰
      this.dictOptions4 = []
      let params = { shebeino: this.selectedValue, cailiaono: '8' }
      getAction(this.url.list4, params).then(res => {
        if (res.success) {
          let list = res.result
          for (let i = 0; i < list.length > 0; i++) {
            if (list[i].cailiaoname !== null && list[i].cailiaoname !== '') {
              this.dictOptions4.push({ text: list[i].name + '(' + list[i].cailiaoname + ')', value: list[i].guid })
            } else {
              this.dictOptions4.push({ text: list[i].name, value: list[i].guid })
            }
          }
        }
      })
    },
    liaocangList3: function () {//矿粉
      this.dictOptions5 = []
      let params = { shebeino: this.selectedValue, cailiaono: '7' }
      getAction(this.url.list4, params).then(res => {
        if (res.success) {
          let list = res.result
          console.log('list矿粉', list)
          for (let i = 0; i < list.length > 0; i++) {
            if (list[i].cailiaoname !== null && list[i].cailiaoname !== '') {
              this.dictOptions5.push({ text: list[i].name + '(' + list[i].cailiaoname + ')', value: list[i].guid })
            } else {
              this.dictOptions5.push({ text: list[i].name, value: list[i].guid })
            }
          }
        }
      })
    },
    liaocangList5: function () {//大石、中石、小石、细骨料
      this.dictOptions7 = []
      let params = { shebeino: this.selectedValue, cailiaono: '1,2,3,4' }
      getAction(this.url.list4, params).then(res => {
        if (res.success) {
          let list = res.result
          console.log('list大石、中石、小石、细骨料', list)
          for (let i = 0; i < list.length > 0; i++) {
            if (list[i].cailiaoname !== null && list[i].cailiaoname !== '') {
              this.dictOptions7.push({ text: list[i].name + '(' + list[i].cailiaoname + ')', value: list[i].guid })
            } else {
              this.dictOptions7.push({ text: list[i].name, value: list[i].guid })
            }
          }
        }
      })
    },
    liaocangList6: function () {//外加剂
      this.dictOptions8 = []
      let params = { shebeino: this.selectedValue, cailiaono: '9' }
      getAction(this.url.list4, params).then(res => {
        if (res.success) {
          let list = res.result
          console.log('list外加剂', list)
          for (let i = 0; i < list.length > 0; i++) {
            if (list[i].cailiaoname !== null && list[i].cailiaoname !== '') {
              this.dictOptions8.push({ text: list[i].name + '(' + list[i].cailiaoname + ')', value: list[i].guid })
            } else {
              this.dictOptions8.push({ text: list[i].name, value: list[i].guid })
            }
          }
        }
      })
    },
    liaocangList7: function () {//水
      this.dictOptions9 = []
      let params = { shebeino: this.selectedValue, cailiaono: '5' }
      getAction(this.url.list4, params).then(res => {
        if (res.success) {
          let list = res.result
          console.log('list水', list)
          for (let i = 0; i < list.length > 0; i++) {
            if (list[i].cailiaoname !== null && list[i].cailiaoname !== '') {
              this.dictOptions9.push({ text: list[i].name + '(' + list[i].cailiaoname + ')', value: list[i].guid })
            } else {
              this.dictOptions9.push({ text: list[i].name, value: list[i].guid })
            }
          }
        }
      })
    },
    // piciList1(selectedValue){//水泥/沥青
    //   let params = {lcbianhao:selectedValue};
    //   this.dictOption2=[];
    //   getAction(this.url.list5, params).then(res=>{
    //     if (res.success){
    //       let list = res.result;
    //       list.forEach(re=>{
    //         this.dictOption2.push({text:re.pici,value:re.pici})
    //       })
    //     }
    //   })
    // },
    // piciList2(selectedValue){//粉煤灰
    //   let params = {lcbianhao:selectedValue};
    //   console.log("selectedValue2",selectedValue)
    //   this.dictOption3=[];
    //   getAction(this.url.list5, params).then(res=>{
    //     if (res.success){
    //       let list = res.result;
    //       list.forEach(re=>{
    //         this.dictOption3.push({text:re.pici,value:re.pici})
    //       })
    //     }
    //   })
    // },
    // piciList3(selectedValue){//矿粉
    //   let params = {lcbianhao:selectedValue};
    //   this.dictOption4=[];
    //   getAction(this.url.list5, params).then(res=>{
    //     if (res.success){
    //       let list = res.result;
    //       list.forEach(re=>{
    //         this.dictOption4.push({text:re.pici,value:re.pici})
    //       })
    //     }
    //   })
    // },
    // piciList4(selectedValue){//大石、中石、小石、细骨料
    //   let params = {lcbianhao:selectedValue};
    //   this.dictOption5=[];
    //   getAction(this.url.list5, params).then(res=>{
    //     if (res.success){
    //       let list = res.result;
    //       list.forEach(re=>{
    //         this.dictOption5.push({text:re.pici,value:re.pici})
    //       })
    //     }
    //   })
    // },
    // piciList5(selectedValue){//大石、中石、小石、细骨料
    //   let params = {lcbianhao:selectedValue};
    //   this.dictOption6=[];
    //   getAction(this.url.list5, params).then(res=>{
    //     if (res.success){
    //       let list = res.result;
    //       list.forEach(re=>{
    //         this.dictOption6.push({text:re.pici,value:re.pici})
    //       })
    //     }
    //   })
    // },
    // piciList6(selectedValue){//大石、中石、小石、细骨料
    //   let params = {lcbianhao:selectedValue};
    //   this.dictOption7=[];
    //   getAction(this.url.list5, params).then(res=>{
    //     if (res.success){
    //       let list = res.result;
    //       list.forEach(re=>{
    //         this.dictOption7.push({text:re.pici,value:re.pici})
    //       })
    //     }
    //   })
    // },
    // piciList7(selectedValue){//大石、中石、小石、细骨料
    //   let params = {lcbianhao:selectedValue};
    //   this.dictOption8=[];
    //   getAction(this.url.list5, params).then(res=>{
    //     if (res.success){
    //       let list = res.result;
    //       list.forEach(re=>{
    //         this.dictOption8.push({text:re.pici,value:re.pici})
    //       })
    //     }
    //   })
    // },
    // piciList8(selectedValue){//外加剂1-3
    //   let params = {lcbianhao:selectedValue};
    //   this.dictOption9=[];
    //   getAction(this.url.list5, params).then(res=>{
    //     if (res.success){
    //       let list = res.result;
    //       list.forEach(re=>{
    //         this.dictOption9.push({text:re.pici,value:re.pici})
    //       })
    //     }
    //   })
    // },
    // piciList9(selectedValue){//外加剂1-3
    //   let params = {lcbianhao:selectedValue};
    //   this.dictOption10=[];
    //   getAction(this.url.list5, params).then(res=>{
    //     if (res.success){
    //       let list = res.result;
    //       list.forEach(re=>{
    //         this.dictOption10.push({text:re.pici,value:re.pici})
    //       })
    //     }
    //   })
    // },
    // piciList10(selectedValue){//外加剂1-3
    //   let params = {lcbianhao:selectedValue};
    //   this.dictOption11=[];
    //   getAction(this.url.list5, params).then(res=>{
    //     if (res.success){
    //       let list = res.result;
    //       list.forEach(re=>{
    //         this.dictOption11.push({text:re.pici,value:re.pici})
    //       })
    //     }
    //   })
    // },
    // piciList11(selectedValue){//水
    //   let params = {lcbianhao:selectedValue};
    //   this.dictOption12=[];
    //   getAction(this.url.list5, params).then(res=>{
    //     if (res.success){
    //       let list = res.result;
    //       list.forEach(re=>{
    //         this.dictOption12.push({text:re.pici,value:re.pici})
    //       })
    //     }
    //   })
    // },
    // handlepici1(selectedValue){
    //   let params = {pici:selectedValue}
    //   this.sumjingzhongt1 = 0
    //   getAction(this.url.list5,params).then(res=>{
    //     if (res.success){
    //       let data = res.result;
    //       console.log("data",data)
    //       for (let i = 0 ; i < data.length ; i++){
    //         this.sumjingzhongt1 = this.sumjingzhongt1 + parseFloat(data[i].jingzhongt)
    //       }
    //       console.log("sumjingzhongt_____________________",this.sumjingzhongt1)
    //       if (this.sumjingzhongt1 < this.model.u1){
    //         this.$message.warn("请继续选择批次")
    //       }
    //     }
    //   })
    // },
    // handlepici2(selectedValue){
    //   let params = {pici:selectedValue}
    //   this.sumjingzhongt2 = 0
    //   getAction(this.url.list5,params).then(res=>{
    //     if (res.success){
    //       let data = res.result;
    //       console.log("data",data)
    //       for (let i = 0 ; i < data.length ; i++){
    //         this.sumjingzhongt2 = this.sumjingzhongt2 + parseFloat(data[i].jingzhongt)
    //       }
    //       console.log("sumjingzhongt_____________________",this.sumjingzhongt2)
    //       if (this.sumjingzhongt2 < this.model.u2){
    //         this.$message.warn("请继续选择批次")
    //       }
    //     }
    //   })
    // },
    // handlepici3(selectedValue){
    //   let params = {pici:selectedValue}
    //   this.sumjingzhongt3 = 0
    //   getAction(this.url.list5,params).then(res=>{
    //     if (res.success){
    //       let data = res.result;
    //       console.log("data",data)
    //       for (let i = 0 ; i < data.length ; i++){
    //         this.sumjingzhongt3 = this.sumjingzhongt3 + parseFloat(data[i].jingzhongt)
    //       }
    //       console.log("sumjingzhongt_____________________",this.sumjingzhongt3)
    //       if (this.sumjingzhongt3 < this.model.u3){
    //         this.$message.warn("请继续选择批次")
    //       }
    //     }
    //   })
    // },
    // handlepici4(selectedValue){
    //   let params = {pici:selectedValue}
    //   this.sumjingzhongt4 = 0
    //   getAction(this.url.list5,params).then(res=>{
    //     if (res.success){
    //       let data = res.result;
    //       console.log("data",data)
    //       for (let i = 0 ; i < data.length ; i++){
    //         this.sumjingzhongt4 = this.sumjingzhongt4 + parseFloat(data[i].jingzhongt)
    //       }
    //     // this.sumgljingzhongt = this.sumjingzhongt4
    //       console.log("sumjingzhongt_____________________",this.sumjingzhongt4)
    //       if (this.sumjingzhongt4 < this.model.u4){
    //         this.$message.warn("请继续选择批次")
    //       }
    //     }
    //   })
    // },
    // handlepici5(selectedValue){
    //   let params = {pici:selectedValue}
    //   this.sumjingzhongt5 = 0
    //   getAction(this.url.list5,params).then(res=>{
    //     if (res.success){
    //       let data = res.result;
    //       console.log("data",data)
    //       for (let i = 0 ; i < data.length ; i++){
    //         this.sumjingzhongt5 = this.sumjingzhongt5 + parseFloat(data[i].jingzhongt)
    //       }
    //       // this.sumgljingzhongt = this.sumjingzhongt5
    //       console.log("sumjingzhongt_____________________",this.sumjingzhongt5)
    //       if (this.sumjingzhongt5 < this.model.u5){
    //         this.$message.warn("请继续选择批次")
    //       }
    //     }
    //   })
    // },
    // handlepici6(selectedValue){
    //   let params = {pici:selectedValue}
    //   this.sumjingzhongt6 = 0
    //   getAction(this.url.list5,params).then(res=>{
    //     if (res.success){
    //       let data = res.result;
    //       console.log("data",data)
    //       for (let i = 0 ; i < data.length ; i++){
    //         this.sumjingzhongt6 = this.sumjingzhongt6 + parseFloat(data[i].jingzhongt)
    //       }
    //       // this.sumgljingzhongt = this.sumjingzhongt6
    //       console.log("sumjingzhongt_____________________",this.sumjingzhongt6)
    //       if (this.sumjingzhongt6 < this.model.u6){
    //         this.$message.warn("请继续选择批次")
    //       }
    //     }
    //   })
    // },
    // handlepici7(selectedValue){
    //   let params = {pici:selectedValue}
    //   this.sumjingzhongt7 = 0
    //   getAction(this.url.list5,params).then(res=>{
    //     if (res.success){
    //       let data = res.result;
    //       console.log("data",data)
    //       for (let i = 0 ; i < data.length ; i++){
    //         this.sumjingzhongt7 = this.sumjingzhongt7 + parseFloat(data[i].jingzhongt)
    //       }
    //       // this.sumgljingzhongt = this.sumjingzhongt7
    //       //console.log("sumjingzhongt_____________________",this.sumjingzhongt7)
    //       if (this.sumjingzhongt7 < this.model.u7){
    //         this.$message.warn("请继续选择批次")
    //       }
    //     }
    //   })
    // },
    // handlepici8(selectedValue){
    //   let params = {pici:selectedValue}
    //   this.sumjingzhongt8 = 0
    //   getAction(this.url.list5,params).then(res=>{
    //     if (res.success){
    //       let data = res.result;
    //       console.log("data",data)
    //       for (let i = 0 ; i < data.length ; i++){
    //         this.sumjingzhongt8 = this.sumjingzhongt8 + parseFloat(data[i].jingzhongt)
    //       }
    //       // this.sumgljingzhongt = this.sumjingzhongt7
    //       //console.log("sumjingzhongt_____________________",this.sumjingzhongt8)
    //       if (this.sumjingzhongt8 < this.model.u8){
    //         this.$message.warn("请继续选择批次")
    //       }
    //     }
    //   })
    // },
    // handlepici9(selectedValue){
    //   let params = {pici:selectedValue}
    //   this.sumjingzhongt9 = 0
    //   getAction(this.url.list5,params).then(res=>{
    //     if (res.success){
    //       let data = res.result;
    //       console.log("data",data)
    //       for (let i = 0 ; i < data.length ; i++){
    //         this.sumjingzhongt9 = this.sumjingzhongt9 + parseFloat(data[i].jingzhongt)
    //       }
    //       // this.sumgljingzhongt = this.sumjingzhongt7
    //       //console.log("sumjingzhongt_____________________",this.sumjingzhongt9)
    //       if (this.sumjingzhongt9 < this.model.u9){
    //         this.$message.warn("请继续选择批次")
    //       }
    //     }
    //   })
    // },
    // handlepici10(selectedValue){
    //   let params = {pici:selectedValue}
    //   this.sumjingzhongt10 = 0
    //   getAction(this.url.list5,params).then(res=>{
    //     if (res.success){
    //       let data = res.result;
    //       console.log("data",data)
    //       for (let i = 0 ; i < data.length ; i++){
    //         this.sumjingzhongt10 = this.sumjingzhongt10 + parseFloat(data[i].jingzhongt)
    //       }
    //       // this.sumgljingzhongt = this.sumjingzhongt7
    //       //console.log("sumjingzhongt_____________________",this.sumjingzhongt10)
    //       if (this.sumjingzhongt10 < this.model.u10){
    //         this.$message.warn("请继续选择批次")
    //       }
    //     }
    //   })
    // },
    // handlepici11(selectedValue){
    //   let params = {pici:selectedValue}
    //   this.sumjingzhongt11 = 0
    //   getAction(this.url.list5,params).then(res=>{
    //     if (res.success){
    //       let data = res.result;
    //       console.log("data",data)
    //       for (let i = 0 ; i < data.length ; i++){
    //         this.sumjingzhongt11 = this.sumjingzhongt11 + parseFloat(data[i].jingzhongt)
    //       }
    //       // this.sumgljingzhongt = this.sumjingzhongt7
    //       //console.log("sumjingzhongt_____________________",this.sumjingzhongt11)
    //       if (this.sumjingzhongt11 < this.model.u11){
    //         this.$message.warn("请继续选择批次")
    //       }
    //     }
    //   })
    // },
    peibiList: function () { //配合比号（任务单编号）
      let params = {}
      getAction(this.url.list, params).then((res) => {
        //console.log(res)
        if (res.success) {
          let list = res.result
          this.dictOptions1 = []
          list.forEach(re => {
            this.dictOptions1.push({ key: re.code, label: re.code })
          })
          // console.log(this.dictOptions1,"ccccccccccccccccccccccccccc")
        }
      })
    },
    llpeibiList: function () { //理论配合比号
      let params = {}
      this.dictOptions2 = []
      getAction(this.url.list1, params).then((res) => {
        //console.log(res,"111111111111111")
        if (res.success) {
          let data = res.result
          for (let i = 0; i < data.length; i++) {
            this.dictOptions2.push({ key: data[i].code, label: data[i].code })
          }
          //console.log(this.dictOptions2,"bbbbbbbbbbbbbbbbbbbbbb")
        }
      })
    },
    zhiliangcodeList: function () { //制梁任务单编号
      let params = { column: 'id', order: 'desc' }
      this.dictOptions12 = []
      getAction(this.url.list6, params).then((res) => {
        // console.log(res,"111111111111111")
        if (res.success) {
          let data = res.result
          // if (data.length>0){
          //   this.model.zhiliangcode = data[0].code
          //   this.form.setFieldsValue(pick(this.model,'zhiliangcode'))
          // }
          for (let i = 0; i < data.length; i++) {
            this.dictOptions12.push({ key: data[i].code, label: data[i].code })
          }
          //console.log(data,"bbbbbbbbbbbbbbbbbbbbbb")
        }
      })
    },
    handleChange(dictOptions1) {//选择任务单编号显示工程名称及施工部位等信息
      this.model.renwudan = dictOptions1
      //console.log(dictOptions1);
      let params = { code: dictOptions1 }
      getAction(this.url.list, params).then((res) => {
        //console.log(res)
        if (res.success) {
          let list = res.result
          // this.projgrade1 = list[0].projgrade;
          this.model.betlev = list[0].betlev//强度等级
          this.model.filter = list[0].filter//抗渗等级
          this.model.freeze = list[0].freeze//抗冻等级
          this.model.lands = list[0].lands//坍落度
          this.model.projname = list[0].projname//工程名称
          this.model.conspos = list[0].conspos//施工部位
          this.projgrade1 = this.model.conspos
          this.model.pour = list[0].pour//浇筑方式
          this.model.mete = list[0].mete//计划方量
          this.model.station = list[0].station//生产线
          this.form.setFieldsValue(pick(this.model, 'betlev', 'filter', 'freeze', 'lands', 'projname', 'conspos', 'pour', 'mete', 'station'))
          // //console.log(this.model.betlev,"lllllllllllllllllllllllll")
        }
      })
    },
    handleChange1(dictOptions2) {//选择理论配合比编号显示工程名称及施工部位等信息
      //console.log(dictOptions2,"++++++++++++++++++")
      let params = { code: dictOptions2 }
      getAction(this.url.list1, params).then((res) => {
        //console.log(res,"__________________________________________")
        if (res.success) {
          let list = res.result
          this.model.betlev = list[0].betlevel//强度等级
          this.model.filter = list[0].filter//抗渗等级
          this.model.freeze = list[0].freeze//抗冻等级
          this.model.lands = list[0].lands//坍落度
          this.model.waterbindratio = list[0].waterbindratio//水胶比
          this.form.setFieldsValue(pick(this.model, 'betlev', 'filter', 'freeze', 'lands', 'waterbindratio'))
          let uuid = list[0].uuid
          this.cailiaoList(uuid)
          // //console.log(this.model.betlev,"lllllllllllllllllllllllll")
        }
      })
    },
    cailiaoList(uuid) {
      this.dataSource = []
      this.dataSource1 = []
      this.dataSource2 = []
      this.model.m1 = null
      this.model.m13 = null
      this.model.m2 = null
      this.model.m3 = null
      this.model.m4 = null
      this.model.m5 = null
      this.model.m6 = null
      this.model.m7 = null
      this.model.m12 = null
      this.model.m8 = null
      this.model.m9 = null
      this.model.m10 = null
      this.model.m11 = null
      let params = { id: uuid }
      getAction(this.url.list2, params).then((res) => {//原材料信息
        if (res.success) {
          let list1 = res.result
          //console.log(list1,"FJjugdwqvkdgwkqdfygbitvw8etyiuwegfjsbggjs")
          for (let i = 0; i < list1.length; i++) {
            if (list1[i].materialTypes == 6) {//水泥
              this.dataSource2.push({ materialname: list1[i].materialname, amount: list1[i].amount })
              //console.log(this.dataSource2, '+++++++++++++++++++++')
              switch (this.dataSource2.length) {
                case 1:
                  this.model.m1 = this.dataSource2[0].materialname
                  this.model.u1 = this.dataSource2[0].amount
                  this.model.ru1 = this.dataSource2[0].amount
                  break
                case 2:
                  this.model.m1 = this.dataSource2[0].materialname
                  this.model.u1 = this.dataSource2[0].amount
                  this.model.ru1 = this.dataSource2[0].amount
                  this.model.m13 = this.dataSource2[1].materialname
                  this.model.u13 = this.dataSource2[1].amount
                  this.model.ru13 = this.dataSource2[1].amount
                  break
              }
            } else if (list1[i].materialTypes == 8) {//粉煤灰
              this.model.m2 = list1[i].materialname
              this.model.u2 = list1[i].amount
              this.model.ru2 = list1[i].amount
            } else if (list1[i].materialTypes == 7) {//矿粉
              this.model.m3 = list1[i].materialname
              this.model.u3 = list1[i].amount
              this.model.ru3 = list1[i].amount
            } else if (list1[i].materialTypes == 1 || list1[i].materialTypes == 2 || list1[i].materialTypes == 3 || list1[i].materialTypes == 4) {//骨料1-骨料5
              this.dataSource.push({ materialname: list1[i].materialname, amount: list1[i].amount })
              //console.log(this.dataSource, '+++++++++++++++++++++')
              switch (this.dataSource.length) {
                case 1:
                  this.model.m4 = this.dataSource[0].materialname
                  this.model.u4 = this.dataSource[0].amount
                  this.model.ru4 = this.dataSource[0].amount
                  break
                case 2:
                  this.model.m4 = this.dataSource[0].materialname
                  this.model.u4 = this.dataSource[0].amount
                  this.model.ru4 = this.dataSource[0].amount
                  this.model.m5 = this.dataSource[1].materialname
                  this.model.u5 = this.dataSource[1].amount
                  this.model.ru5 = this.dataSource[1].amount
                  break
                case 3:
                  this.model.m4 = this.dataSource[0].materialname
                  this.model.u4 = this.dataSource[0].amount
                  this.model.ru4 = this.dataSource[0].amount
                  this.model.m5 = this.dataSource[1].materialname
                  this.model.u5 = this.dataSource[1].amount
                  this.model.ru5 = this.dataSource[1].amount
                  this.model.m6 = this.dataSource[2].materialname
                  this.model.u6 = this.dataSource[2].amount
                  this.model.ru6 = this.dataSource[2].amount
                  break
                case 4:
                  this.model.m4 = this.dataSource[0].materialname
                  this.model.u4 = this.dataSource[0].amount
                  this.model.ru4 = this.dataSource[0].amount
                  this.model.m5 = this.dataSource[1].materialname
                  this.model.u5 = this.dataSource[1].amount
                  this.model.ru5 = this.dataSource[1].amount
                  this.model.m6 = this.dataSource[2].materialname
                  this.model.u6 = this.dataSource[2].amount
                  this.model.ru6 = this.dataSource[2].amount
                  this.model.m7 = this.dataSource[3].materialname
                  this.model.u7 = this.dataSource[3].amount
                  this.model.ru7 = this.dataSource[3].amount
                  break
                case 5:
                  this.model.m4 = this.dataSource[0].materialname
                  this.model.u4 = this.dataSource[0].amount
                  this.model.ru4 = this.dataSource[0].amount
                  this.model.m5 = this.dataSource[1].materialname
                  this.model.u5 = this.dataSource[1].amount
                  this.model.ru5 = this.dataSource[1].amount
                  this.model.m6 = this.dataSource[2].materialname
                  this.model.u6 = this.dataSource[2].amount
                  this.model.ru6 = this.dataSource[2].amount
                  this.model.m7 = this.dataSource[3].materialname
                  this.model.u7 = this.dataSource[3].amount
                  this.model.ru7 = this.dataSource[3].amount
                  this.model.m12 = this.dataSource[4].materialname
                  this.model.u12 = this.dataSource[4].amount
                  this.model.ru12 = this.dataSource[4].amount
                  break
              }
            } else if (list1[i].materialTypes == 9) {//外加剂1-外加剂3
              this.dataSource1.push({ materialname: list1[i].materialname, amount: list1[i].amount })
              switch (this.dataSource1.length) {
                case 1:
                  this.model.m8 = this.dataSource1[0].materialname
                  this.model.u8 = this.dataSource1[0].amount
                  this.model.ru8 = this.dataSource1[0].amount
                  break
                case 2:
                  this.model.m8 = this.dataSource1[0].materialname
                  this.model.u8 = this.dataSource1[0].amount
                  this.model.ru8 = this.dataSource1[0].amount
                  this.model.m9 = this.dataSource1[1].materialname
                  this.model.u9 = this.dataSource1[1].amount
                  this.model.ru9 = this.dataSource1[1].amount
                  break
                // case 3:
                //   this.model.m8 = this.dataSource1[0].materialname
                //   this.model.u8 = this.dataSource1[0].amount
                //   this.model.ru8 = this.dataSource1[0].amount
                //   this.model.m9 = this.dataSource1[1].materialname
                //   this.model.u9 = this.dataSource1[1].amount
                //   this.model.ru9 = this.dataSource1[1].amount
                //   this.model.m10 = this.dataSource1[2].materialname
                //   this.model.u10 = this.dataSource1[2].amount
                //   this.model.ru10 = this.dataSource1[2].amount
                //   this.form.setFieldsValue(pick(this.model,'m8','m9','m10','u8','u9','u10','ru8','ru9','ru10'))
                //   break;
              }
            } else if (list1[i].materialTypes == 5) {//水
              this.model.m11 = list1[i].materialname
              this.model.u11 = list1[i].amount
              this.model.ru11 = list1[i].amount
            }
          }
        }
        this.form.setFieldsValue(pick(this.model, 'm1', 'u1', 'ru1', 'm13', 'u13', 'ru13', 'm2', 'u2', 'ru2', 'm3', 'u3', 'ru3', 'm4', 'u4', 'ru4', 'm5', 'm6', 'm7', 'u5', 'u6', 'u7', 'ru5', 'ru6', 'ru7', 'm12', 'u12', 'ru12', 'm8', 'm9', 'u8', 'u9', 'ru8', 'ru9', 'm11', 'u11', 'ru11'))
      })
    },
    onChange() {
      const form = this.form
      var value = form.getFieldValue('mc8')
      if (value != null) {
        let mcl1 = (value * this.model.u8) / 100
        this.model.mcl8 = mcl1
        this.model.ru8 = mcl1 + this.model.u8
        this.form.setFieldsValue(pick(this.model, 'ru8', 'mcl8'))
        //console.log(this.model.ru11,"ru111")
      }
      if (value === undefined) {
        this.model.mcl8 = 0
        this.form.setFieldsValue(pick(this.model, 'mcl8'))
      }
      this.chushishui()
      //console.log('changed', value);

    },
    chushishui() {
      const form = this.form
      var zong = form.getFieldValue('u11')//水的理论用量
      var mcl4 = form.getFieldValue('mcl4')
      if (mcl4 === undefined) {
        mcl4 = 0
      }
      var mcl5 = form.getFieldValue('mcl5')
      if (mcl5 === undefined) {
        mcl5 = 0
      }
      var mcl6 = form.getFieldValue('mcl6')
      if (mcl6 === undefined) {
        mcl6 = 0
      }
      var mcl7 = form.getFieldValue('mcl7')
      if (mcl7 === undefined) {
        mcl7 = 0
      }
      var mcl8 = form.getFieldValue('mcl8')
      if (mcl8 === undefined) {
        mcl8 = 0
      }
      var mcl9 = form.getFieldValue('mcl9')
      if (mcl9 === undefined) {
        mcl9 = 0
      }
      // var mcl10=form.getFieldValue('mcl10');
      // if(mcl10==undefined){
      //   mcl10=0;
      // }
      var mcl12 = form.getFieldValue('mcl12')
      if (mcl12 === undefined) {
        mcl12 = 0
      }
      this.model.ru11 = zong - (mcl4 + mcl5 + mcl6 + mcl7 + mcl8 + mcl9 + mcl12)
      //console.log(this.model.ru11,"水水水")
      this.form.setFieldsValue(pick(this.model, 'ru11'))
    },
    onChange1() {
      const form = this.form
      var value = form.getFieldValue('mc4')
      if (value != null) {
        let mcl2 = (value * this.model.u4) / 100
        this.model.ru4 = mcl2 + this.model.u4
        this.model.mcl4 = mcl2
        this.form.setFieldsValue(pick(this.model, 'ru4', 'mcl4'))
        //console.log(this.model.ru11, "ru11________________2")
      }
      if (value === undefined) {
        this.model.mcl4 = 0
        this.form.setFieldsValue(pick(this.model, 'mcl4'))
      }
      this.chushishui()
    },
    onChange2() {
      const form = this.form
      var value = form.getFieldValue('mc5')
      if (value != null) {
        // //console.log('changed', value);
        let mcl3 = (value * this.model.u5) / 100
        this.model.ru5 = mcl3 + this.model.u5
        this.model.mcl5 = mcl3
        this.form.setFieldsValue(pick(this.model, 'ru5', 'mcl5'))
        //console.log(this.model.ru11, "+++++++++++++++++++++++++++3")
      }
      if (value === undefined) {
        this.model.mcl5 = 0
        this.form.setFieldsValue(pick(this.model, 'mcl5'))
      }
      this.chushishui()
    },
    onChange3() {
      // //console.log('changed', value);
      const form = this.form
      var value = form.getFieldValue('mc6')
      if (value != null) {
        let mcl4 = (value * this.model.u6) / 100
        this.model.ru6 = mcl4 + this.model.u6
        this.model.mcl6 = mcl4
        this.form.setFieldsValue(pick(this.model, 'ru6', 'mcl6'))
        //console.log(this.model.ru11, "+++++++++++++++4")
      }
      if (value === undefined) {
        this.model.mcl6 = 0
        this.form.setFieldsValue(pick(this.model, 'mcl6'))
      }
      this.chushishui()
    },
    onChange4() {
      // //console.log('changed', value);
      const form = this.form
      var value = form.getFieldValue('mc7')
      if (value != null) {
        let mcl5 = (value * this.model.u7) / 100
        this.model.ru7 = mcl5 + this.model.u7
        this.model.mcl7 = mcl5
        this.form.setFieldsValue(pick(this.model, 'ru7', 'mcl7'))
        //console.log(this.model.ru11, "+++++++++++++++++5")
      }
      if (value === undefined) {
        this.model.mcl7 = 0
        this.form.setFieldsValue(pick(this.model, 'mcl7'))
      }
      this.chushishui()
    },
    onChange5() {
      const form = this.form
      var value = form.getFieldValue('mc9')
      if (value != null) {
        // //console.log('changed', value);
        let mcl6 = (value * this.model.u9) / 100
        this.model.ru9 = mcl6 + this.model.u9
        this.model.mcl9 = mcl6
        this.form.setFieldsValue(pick(this.model, 'ru9', 'mcl9'))
        //console.log(this.model.ru11, "+++++++++++++++++6")
      }
      if (value === undefined) {
        this.model.mcl9 = 0
        this.form.setFieldsValue(pick(this.model, 'mcl9'))
      }
      this.chushishui()
    },
    // onChange6(){
    //   const form = this.form;
    //   var value=form.getFieldValue('mc10');
    //   // //console.log('changed', value);
    //   if(value!=null) {
    //     let mcl7 = (value * this.model.u10) / 100
    //     this.model.ru10 = mcl7 + this.model.u10
    //     this.model.mcl10 = mcl7
    //     this.form.setFieldsValue(pick(this.model, 'ru10', 'mcl10'))
    //     console.log(this.model.ru11, "+++++++++++++++++7")
    //   }
    //   if(value==undefined){
    //     this.model.mcl10=0;
    //     this.form.setFieldsValue(pick(this.model,'mcl10'))
    //   }
    //   this.chushishui();
    // },
    onChange7() {
      const form = this.form
      var value = form.getFieldValue('mc12')
      // //console.log('changed', value);
      if (value != null) {
        let mcl8 = (value * this.model.u12) / 100
        this.model.ru12 = mcl8 + this.model.u12
        this.model.mcl12 = mcl8
        this.form.setFieldsValue(pick(this.model, 'ru12', 'mcl12'))
        //console.log(this.model.ru12, "+++++++++++++++++7")
      }
      if (value === undefined) {
        this.model.mcl12 = 0
        this.form.setFieldsValue(pick(this.model, 'mcl12'))
      }
      this.chushishui()
    },
    shebeiList: function () {
      var sys_depart_orgcode = Vue.ls.get('SYS_DEPART_ORGCODE')
      usershebeiList({
        sys_depart_orgcode: sys_depart_orgcode,
        sbtypes: '0'
      }).then(res => {
        this.dictOption = []
        let result = res.result
        result.forEach(re => {
          this.dictOption.push({ text: re.sbname, value: re.sbjno })
        })
        ////console.log(res)
      })
    },
    qrcode: function (record) {//二维码
      if (record.id === undefined || record.id == null) {
        ////console.log(record)
        ////console.log(1111111111111111)
        this.codeUrl = []
      } else {
        let params = { id: this.model.id }
        //console.log(this.model)
        getAction(this.url.list3, params).then((res) => {
            this.codeUrl = []
            if (res.success) {
              let data = res.result
              ////console.log(data, "+______________________________+")
              if (data != null) {
                for (let i = 0; i < data.length; i++) {
                  this.codeUrl.push({ codes: data[i].code })
                  ////console.log(this.codeUrl,"codeUrl");
                }
              } else {
                this.$message.error('该配料单没有二维码')
              }
            }
          }
        )
      }

    },
    popupCallback(row) {
      this.form.setFieldsValue(pick(row, 'station', 'code', 'tag', 'betlev', 'filter', 'freeze', 'season', 'lands', 'mixlast', 'scale', 'otherreq', 'exper', 'assoss',
        'dattim', 'flag', 'note', 'm1', 'u1', 'm2', 'u2', 'm3', 'u3', 'm4', 'u4', 'm5', 'u5', 'm6', 'u6', 'm7', 'u7', 'm8', 'u8', 'm9', 'u9', 'm10', 'u10', 'm11', 'u11', 'm12', 'u12', 'm13', 'u13', 'm14', 'u14', 'm15',
        'u15', 'm16', 'u16', 'm17', 'u17', 'm18', 'u18', 'm19', 'u19', 'm20', 'u20', 'm21', 'u21', 'm22', 'u22', 'm23', 'u23', 'm24', 'u24', 'm25', 'u25', 'm26',
        'u26', 'm27', 'u27', 'm28', 'u28', 'm29', 'u29', 'm30', 'u30', 'm31', 'u31', 'm32', 'u32', 'shebeibianhao', 'sysOrgCode', 'createBy', 'code1', 'projname', 'conspos',
        'pour', 'mc4', 'mc5', 'mc6', 'mc7', 'mc8', 'mc9', 'mc10', 'ru4', 'ru5', 'ru6', 'ru7', 'ru8', 'ru9', 'ru10', 'renwudan', 'mcl4', 'mcl5', 'mcl6', 'mcl7', 'mcl8', 'mcl9', 'mcl10', 'ru11', 'mete',
        'ru1', 'ru2', 'ru3', 'ru12', 'ru13', 'waterbindratio', 'lc1', 'lc2', 'lc3', 'lc4', 'lc5', 'lc6', 'lc7', 'lc8', 'lc9', 'lc10', 'lc11', 'pici1', 'pici2', 'pici3', 'pici4', 'pici5', 'pici6', 'pici7', 'pici8', 'pici9', 'pici10', 'pici11', 'zhiliangcode', 'lc12', 'lc13', 'mcl12', 'mc12'))

    },
  }
}
</script>