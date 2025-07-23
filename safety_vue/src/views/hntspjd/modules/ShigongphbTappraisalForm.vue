<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form :form="form" slot="detail">
        <a-row>
<!--          <a-col :span="24">-->
<!--            <a-form-item label="生产线" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input-number v-decorator="['station']" placeholder="请输入生产线" style="width: 100%" />-->
<!--            </a-form-item>-->
<!--          </a-col>-->
          <a-card title = "基础信息" :bordered="false" :headStyle="{color:'#0785fd'}" :bodyStyle="{padding:'10'}">
            <a-row>
              <a-col :span="12">
                <a-form-item label="设备编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-decorator="['shebeibianhao']" placeholder="请输入设备编号" :disabled="true"></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item label="鉴定编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-decorator="['identificationno']" placeholder="请输入鉴定编号" ></a-input>
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
                <a-form-item label="配合比号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-select placeholder="请选择配合比号"  v-decorator="['code']"  @change="handleChange">
                    <a-select-option v-for="(item, key) in dictOptions" :key="item.key"  >
                  <span style="display: inline-block;width: 100%" >
                    {{ item.label }}
                  </span>
                    </a-select-option>
                  </a-select>
                </a-form-item>
              </a-col>
              <!--          <a-col :span="24">-->
              <!--            <a-form-item label="砼标记" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
              <!--              <a-input v-decorator="['tag']" placeholder="请输入砼标记"  ></a-input>-->
              <!--            </a-form-item>-->
              <!--          </a-col>-->
              <a-col :span="12">
                <a-form-item label="强度等级" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-decorator="['betlev']" placeholder="请输入强度等级" :disabled="true"></a-input>
                </a-form-item>
              </a-col>
            </a-row>
            <!--            <a-col :span="12">-->
            <!--              <a-form-item label="抗渗等级" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
            <!--                <a-input v-decorator="['filter']" placeholder="请输入抗渗等级"  ></a-input>-->
            <!--              </a-form-item>-->
            <!--            </a-col>-->
            <!--            <a-col :span="12">-->
            <!--              <a-form-item label="抗冻等级" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
            <!--                <a-input v-decorator="['freeze']" placeholder="请输入抗冻等级"  ></a-input>-->
            <!--              </a-form-item>-->
            <!--            </a-col>-->

            <!--          <a-col :span="24">-->
            <!--            <a-form-item label="施工季节" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
            <!--              <a-input v-decorator="['season']" placeholder="请输入施工季节"  ></a-input>-->
            <!--            </a-form-item>-->
            <!--          </a-col>-->
            <a-row>
              <a-col :span="12">
                <a-form-item label="坍落度" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-decorator="['lands']" placeholder="请输入坍落度"  :disabled="true"></a-input>
                </a-form-item>
              </a-col>
              <!--            <a-col :span="12">-->
              <!--              <a-form-item label="搅拌时间" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
              <!--                <a-input-number v-decorator="['mixlast']" placeholder="请输入搅拌时间" style="width: 100%" />-->
              <!--              </a-form-item>-->
              <!--            </a-col>-->
              <a-col :span="12">
                <a-form-item label="鉴定日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <j-date placeholder="请选择鉴定日期" v-decorator="['identificationtime']" :trigger-change="true" style="width: 100%" dateFormat="YYYY-MM-DD HH:mm:ss"/>
                </a-form-item>
              </a-col>
            </a-row>
          </a-card>


<!--          <a-col :span="24">-->
<!--            <a-form-item label="设计比例" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['scale']" placeholder="请输入设计比例"  ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="其他要求" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['otherreq']" placeholder="请输入其他要求"  ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
          <a-row>

<!--            <a-col :span="24">-->
<!--              <a-form-item label="试验员" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <a-input v-decorator="['exper']" placeholder="请输入试验员"  ></a-input>-->
<!--              </a-form-item>-->
<!--            </a-col>-->
<!--            <a-col :span="24">-->
<!--              <a-form-item label="审核员" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <a-input v-decorator="['assoss']" placeholder="请输入审核员"  ></a-input>-->
<!--              </a-form-item>-->
<!--            </a-col>-->
          </a-row>
<!--          <a-col :span="24">-->
<!--            <a-form-item label="制定日期" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <j-date placeholder="请选择制定日期" v-decorator="['dattim']" :trigger-change="true" style="width: 100%" />-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="标识" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['flag']" placeholder="请输入标识"  ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['note']" placeholder="请输入备注"  ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
          <a-card title = "原材料信息" :bordered="false" :headStyle="{color:'#0785fd'}" :bodyStyle="{padding:'10'}">
            <a-row>
              <a-col :span="6">
                <a-form-item label="水泥" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
                  <a-input v-decorator="['m1']" placeholder="请输入水泥"  :disabled="true"></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item label="用量1" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
                  <a-input-number v-decorator="['u1']" placeholder="请输入用量1" style="width: 100%"  :disabled="true"/>
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item label="含水率" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
                  <a-input-number placeholder="请输入含水率" style="width: 100%"  :disabled="true"/>
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item label="实际用量" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
                  <a-input-number placeholder="请输入实际用量" style="width: 100%" :disabled="true"/>
                </a-form-item>
              </a-col>
            </a-row>

            <a-row>
              <a-col :span="6">
                <a-form-item label="粉煤灰" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
                  <a-input v-decorator="['m2']" placeholder="请输入粉煤灰"  :disabled="true"></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item label="用量2" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
                  <a-input-number v-decorator="['u2']" placeholder="请输入用量2" style="width: 100%" :disabled="true"/>
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item label="含水率" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
                  <a-input-number placeholder="请输入含水率" style="width: 100%"  :disabled="true" />
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item label="实际用量" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
                  <a-input-number placeholder="请输入实际用量" style="width: 100%" :disabled="true" />
                </a-form-item>
              </a-col>
            </a-row>

            <a-row>
              <a-col :span="6">
                <a-form-item label="矿粉" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
                  <a-input v-decorator="['m3']" placeholder="请输入矿粉"  :disabled="true"></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item label="用量3" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
                  <a-input-number v-decorator="['u3']" placeholder="请输入用量3" style="width: 100%" :disabled="true"/>
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item label="含水率" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
                  <a-input-number placeholder="请输入含水率" style="width: 100%"  :disabled="true"/>
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item label="实际用量" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
                  <a-input-number placeholder="请输入实际用量" style="width: 100%" :disabled="true"/>
                </a-form-item>
              </a-col>
            </a-row>

            <a-row>
              <a-col :span="6">
                <a-form-item label="骨料1" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
                  <a-input v-decorator="['m4']" placeholder="请输入骨料1"  :disabled="true"></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item label="用量4" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
                  <a-input-number v-decorator="['u4']" placeholder="请输入用量4" style="width: 100%" :disabled="true"/>
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item label="含水率" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
                  <a-input-number v-decorator="['mc4']" placeholder="请输入含水率" style="width: 100%" :disabled="true"/>
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item label="实际用量" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
                  <a-input-number v-decorator="['ru4']" placeholder="请输入实际用量" style="width: 100%" :disabled="true"/>
                </a-form-item>
              </a-col>
            </a-row>

            <a-row>
              <a-col :span="6">
                <a-form-item label="骨料2" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
                  <a-input v-decorator="['m5']" placeholder="请输入骨料2"  :disabled="true"></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item label="用量5" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
                  <a-input-number v-decorator="['u5']" placeholder="请输入用量5" style="width: 100%" :disabled="true"/>
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item label="含水率" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
                  <a-input-number v-decorator="['mc5']" placeholder="请输入含水率" style="width: 100%" :disabled="true"/>
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item label="实际用量" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
                  <a-input-number v-decorator="['ru5']" placeholder="请输入实际用量" style="width: 100%" :disabled="true"/>
                </a-form-item>
              </a-col>
            </a-row>

            <a-row>
              <a-col :span="6">
                <a-form-item label="骨料3" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
                  <a-input v-decorator="['m6']" placeholder="请输入骨料3"  :disabled="true"></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item label="用量6" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
                  <a-input-number v-decorator="['u6']" placeholder="请输入用量6" style="width: 100%" :disabled="true"/>
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item label="含水率" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
                  <a-input-number v-decorator="['mc6']" placeholder="请输入含水率" style="width: 100%" :disabled="true"/>
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item label="实际用量" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
                  <a-input-number v-decorator="['ru6']" placeholder="请输入实际用量" style="width: 100%" :disabled="true"/>
                </a-form-item>
              </a-col>
            </a-row>

            <a-row>
              <a-col :span="6">
                <a-form-item label="骨料4" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
                  <a-input v-decorator="['m7']" placeholder="请输入骨料4"  :disabled="true"></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item label="用量7" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
                  <a-input-number v-decorator="['u7']" placeholder="请输入用量7" style="width: 100%" :disabled="true"/>
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item label="含水率" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
                  <a-input-number v-decorator="['mc7']" placeholder="请输入含水率" style="width: 100%" :disabled="true"/>
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item label="实际用量" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
                  <a-input-number v-decorator="['ru7']" placeholder="请输入实际用量" style="width: 100%" :disabled="true"/>
                </a-form-item>
              </a-col>
            </a-row>

            <a-row>
              <a-col :span="6">
                <a-form-item label="外加剂1" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
                  <a-input v-decorator="['m8']" placeholder="请输入外加剂1"  :disabled="true"></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item label="用量8" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
                  <a-input-number v-decorator="['u8']" placeholder="请输入用量8" style="width: 100%" :disabled="true"/>
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item label="含水率" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
                  <a-input-number v-decorator="['mc8']" placeholder="请输入含水率" style="width: 100%" :disabled="true"/>
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item label="实际用量" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
                  <a-input-number v-decorator="['ru8']" placeholder="请输入实际用量" style="width: 100%" :disabled="true"/>
                </a-form-item>
              </a-col>
            </a-row>

            <a-row>
              <a-col :span="6">
                <a-form-item label="外加剂2" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
                  <a-input v-decorator="['m9']" placeholder="请输入外加剂2"  :disabled="true"></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item label="用量9" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
                  <a-input-number v-decorator="['u9']" placeholder="请输入用量9" style="width: 100%" :disabled="true"/>
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item label="含水率" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
                  <a-input-number v-decorator="['mc9']" placeholder="请输入含水率" style="width: 100%" :disabled="true"/>
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item label="实际用量" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
                  <a-input-number v-decorator="['ru9']" placeholder="请输入实际用量" style="width: 100%" :disabled="true"/>
                </a-form-item>
              </a-col>
            </a-row>

            <a-row>
              <a-col :span="6">
                <a-form-item label="外加剂3" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
                  <a-input v-decorator="['m10']" placeholder="请输入外加剂3"  :disabled="true"></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item label="用量10" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
                  <a-input-number v-decorator="['u10']" placeholder="请输入用量10" style="width: 100%" :disabled="true"/>
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item label="含水率" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
                  <a-input-number v-decorator="['mc10']" placeholder="请输入含水率" style="width: 100%" :disabled="true"/>
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item label="实际用量" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
                  <a-input-number v-decorator="['ru10']" placeholder="请输入实际用量" style="width: 100%" :disabled="true"/>
                </a-form-item>
              </a-col>
            </a-row>

            <a-row>
              <a-col :span="6">
                <a-form-item label="水" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
                  <a-input v-decorator="['m11']" placeholder="请输入水"  :disabled="true"></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item label="用量11" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
                  <a-input-number v-decorator="['u11']" placeholder="请输入用量11" style="width: 100%" :disabled="true"/>
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item label="含水率" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
                  <a-input-number placeholder="请输入含水率" style="width: 100%"  :disabled="true"/>
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item label="实际用量" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
                  <a-input-number placeholder="请输入实际用量" style="width: 100%" :disabled="true"/>
                </a-form-item>
              </a-col>
            </a-row>

            <a-row>
              <a-col :span="6">
                <a-form-item label="备用原材料1" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
                  <a-input v-decorator="['m12']" placeholder="请输入备用原材料1"  :disabled="true"></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item label="用量12" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
                  <a-input-number v-decorator="['u12']" placeholder="请输入用量12" style="width: 100%" :disabled="true"/>
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item label="含水率" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
                  <a-input-number placeholder="请输入含水率" style="width: 100%"  :disabled="true"/>
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item label="实际用量" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
                  <a-input-number placeholder="请输入实际用量" style="width: 100%" :disabled="true"/>
                </a-form-item>
              </a-col>
            </a-row>

            <a-row>
              <a-col :span="6">
                <a-form-item label="备用原材料2" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
                  <a-input v-decorator="['m13']" placeholder="请输入备用原材料2"  :disabled="true"></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item label="用量13" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
                  <a-input-number v-decorator="['u13']" placeholder="请输入用量13" style="width: 100%" :disabled="true"/>
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item label="含水率" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
                  <a-input-number placeholder="请输入含水率" style="width: 100%"  :disabled="true"/>
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item label="实际用量" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
                  <a-input-number placeholder="请输入实际用量" style="width: 100%" :disabled="true"/>
                </a-form-item>
              </a-col>
            </a-row>
          </a-card>


<!--          <a-col :span="24">-->
<!--            <a-form-item label="原材料14" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['m14']" placeholder="请输入原材料14"  ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="用量14" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input-number v-decorator="['u14']" placeholder="请输入用量14" style="width: 100%" />-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="原材料15" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['m15']" placeholder="请输入原材料15"  ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="用量15" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input-number v-decorator="['u15']" placeholder="请输入用量15" style="width: 100%" />-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="原材料16" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['m16']" placeholder="请输入原材料16"  ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="用量16" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input-number v-decorator="['u16']" placeholder="请输入用量16" style="width: 100%" />-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="原材料17" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['m17']" placeholder="请输入原材料17"  ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="用量17" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input-number v-decorator="['u17']" placeholder="请输入用量17" style="width: 100%" />-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="原材料18" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['m18']" placeholder="请输入原材料18"  ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="用量18" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input-number v-decorator="['u18']" placeholder="请输入用量18" style="width: 100%" />-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="原材料19" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['m19']" placeholder="请输入原材料19"  ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="用量19" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input-number v-decorator="['u19']" placeholder="请输入用量19" style="width: 100%" />-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="原材料20" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['m20']" placeholder="请输入原材料20"  ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="用量20" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input-number v-decorator="['u20']" placeholder="请输入用量20" style="width: 100%" />-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="原材料21" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['m21']" placeholder="请输入原材料21"  ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="用量21" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input-number v-decorator="['u21']" placeholder="请输入用量21" style="width: 100%" />-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="原材料22" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['m22']" placeholder="请输入原材料22"  ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="用量22" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input-number v-decorator="['u22']" placeholder="请输入用量22" style="width: 100%" />-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="原材料23" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['m23']" placeholder="请输入原材料23"  ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="用量23" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input-number v-decorator="['u23']" placeholder="请输入用量23" style="width: 100%" />-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="原材料24" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['m24']" placeholder="请输入原材料24"  ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="用量24" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input-number v-decorator="['u24']" placeholder="请输入用量24" style="width: 100%" />-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="原材料25" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['m25']" placeholder="请输入原材料25"  ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="用量25" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input-number v-decorator="['u25']" placeholder="请输入用量25" style="width: 100%" />-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="原材料26" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['m26']" placeholder="请输入原材料26"  ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="用量26" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input-number v-decorator="['u26']" placeholder="请输入用量26" style="width: 100%" />-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="原材料27" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['m27']" placeholder="请输入原材料27"  ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="用量27" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input-number v-decorator="['u27']" placeholder="请输入用量27" style="width: 100%" />-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="原材料28" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['m28']" placeholder="请输入原材料28"  ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="用量28" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input-number v-decorator="['u28']" placeholder="请输入用量28" style="width: 100%" />-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="原材料29" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['m29']" placeholder="请输入原材料29"  ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="用量29" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input-number v-decorator="['u29']" placeholder="请输入用量29" style="width: 100%" />-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="原材料30" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['m30']" placeholder="请输入原材料30"  ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="用量30" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input-number v-decorator="['u30']" placeholder="请输入用量30" style="width: 100%" />-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="原材料31" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['m31']" placeholder="请输入原材料31"  ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="用量31" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input-number v-decorator="['u31']" placeholder="请输入用量31" style="width: 100%" />-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="原材料32" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['m32']" placeholder="请输入原材料32"  ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="用量32" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input-number v-decorator="['u32']" placeholder="请输入用量32" style="width: 100%" />-->
<!--            </a-form-item>-->
<!--          </a-col>-->

<!--          <a-col :span="24">-->
<!--            <a-form-item label="控制权限" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['sysOrgCode']" placeholder="请输入控制权限"  ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="创建人" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['createBy']" placeholder="请输入创建人"  ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="理论配合比编号" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['code1']" placeholder="请输入理论配合比编号"  ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="浇筑方式" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['pour']" placeholder="请输入浇筑方式"  ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
          <a-card title = "鉴定信息" :bordered="false" :headStyle="{color:'#0785fd'}" :bodyStyle="{padding:'10'}">
            <a-row>
              <a-col :span="6">
                <a-form-item label="设计塌落度(mm)" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
                  <a-input v-decorator="['jdsjlands']" placeholder="请输入设计塌落度(mm)"  ></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item label="实测塌落度1(mm)" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
                  <a-input v-decorator="['jdsclands1']" placeholder="请输入实测塌落度1(mm)"  ></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item label="实测塌落度2(mm)" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
                  <a-input v-decorator="['jdsclands2']" placeholder="请输入实测塌落度2(mm)"  ></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item label="实测塌落度3(mm)" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
                  <a-input v-decorator="['jdsclands3']" placeholder="请输入实测塌落度3(mm)"  ></a-input>
                </a-form-item>
              </a-col>
            </a-row>

            <a-row>
              <a-col :span="6">
                <a-form-item label="设计含水量(%)" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
                  <a-input-number v-decorator="['jdsjmc']" placeholder="请输入设计含水量（%）" style="width: 100%" />
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item label="实测含水量1(%)" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
                  <a-input-number v-decorator="['jdscmc1']" placeholder="请输入实测含水量1（%）" style="width: 100%" />
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item label="实测含水量2(%)" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
                  <a-input-number v-decorator="['jdscmc2']" placeholder="请输入实测含水量2（%）" style="width: 100%" />
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item label="实测含水量3(%)" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
                  <a-input-number v-decorator="['jdscmc3']" placeholder="请输入实测含水量3（%）" style="width: 100%" />
                </a-form-item>
              </a-col>
            </a-row>

            <a-row>
              <a-col :span="6">
                <a-form-item label="设计出机温度(℃)" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
                  <a-input-number v-decorator="['jdsjcjwd']" placeholder="请输入设计出机温度（℃）" style="width: 100%" />
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item label="实测出机温度1(℃)" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
                  <a-input-number v-decorator="['jdsccjwd1']" placeholder="请输入实测出机温度1（℃）" style="width: 100%" />
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item label="实测出机温度2(℃)" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
                  <a-input-number v-decorator="['jdsccjwd2']" placeholder="请输入实测出机温度2（℃）" style="width: 100%" />
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item label="实测出机温度3(℃)" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
                  <a-input-number v-decorator="['jdsccjwd3']" placeholder="请输入实测出机温度3（℃）" style="width: 100%" />
                </a-form-item>
              </a-col>
            </a-row>

            <a-row>
              <a-col :span="6">
                <a-form-item label="设计泌水率" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
                  <a-input-number v-decorator="['jdsjmsl']" placeholder="请输入设计泌水率" style="width: 100%" />
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item label="实测泌水率1" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
                  <a-input-number v-decorator="['jdscmsl1']" placeholder="请输入鉴定实测泌水率1" style="width: 100%" />
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item label="实测泌水率2" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
                  <a-input-number v-decorator="['jdscmsl2']" placeholder="请输入实测泌水率2" style="width: 100%" />
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item label="实测泌水率3" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
                  <a-input-number v-decorator="['jdscmsl3']" placeholder="请输入实测泌水率3" style="width: 100%" />
                </a-form-item>
              </a-col>
            </a-row>

            <a-row>
              <a-col :span="6">
                <a-form-item label="原材料与申请单是否相符" :labelCol="labelCol2" :wrapperCol="wrapperCol2">
                  <j-dict-select-tag type="list" placeholder="请输入原材料与申请单是否相符" v-decorator="['ismatch']" dictCode="ismatch" :trigger-change="true"></j-dict-select-tag>
                </a-form-item>
              </a-col>
              <a-col :span="18">
                <a-form-item label="鉴定结论" :labelCol="labelCol3" :wrapperCol="wrapperCol3">
                  <a-input v-decorator="['identificationresult']" placeholder="请输入鉴定结论"  ></a-input>
                </a-form-item>
              </a-col>
            </a-row>

            <a-col :span="6">
              <a-form-item label="鉴定附件" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
                <a-upload style="margin-left: 20%" :action="uploadAction" :headers="headers" :default-file-list="defaultFileList" @change="handleChange1">
                  <a-button><a-icon type="upload"/>
                    添加附件
                  </a-button>
                </a-upload>
              </a-form-item>
            </a-col>
          </a-card>

          <a-card title = "签字栏" :bordered="false" :headStyle="{color:'#0785fd'}" :bodyStyle="{padding:'10'}">
            <a-row>
              <a-col :span="8">
                <a-form-item label="试验员" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-decorator="['shiyanyuan']" placeholder="请输入试验员"  ></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-form-item label="技术主管" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-decorator="['technicalDirector']" placeholder="请输入技术主管"  ></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-form-item label="监理工程师" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-decorator="['supervisionEngineer']" placeholder="请输入监理工程师"  ></a-input>
                </a-form-item>
              </a-col>
            </a-row>
          </a-card>


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
  import JFilePop from '@comp/jeecg/minipop/JFilePop'
  import { ACCESS_TOKEN } from '@/store/mutation-types'
  import Vue from 'vue'
  export default {
    name: 'ShigongphbTappraisalForm',
    components: {
      JFormContainer,
      JDate,
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
        uploadAction:window._CONFIG['domianURL']+"/sys/common/upload",
        headers:{},
        defaultFileList:[],
        dictOptions:[],
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
        labelCol1: {
          xs: { span: 24 },
          sm: { span: 12 },
        },
        wrapperCol1: {
          xs: { span: 24 },
          sm: { span: 12 },
        },
        labelCol2: {
          xs: { span: 24 },
          sm: { span: 14 },
        },
        wrapperCol2: {
          xs: { span: 24 },
          sm: { span: 10 },
        },
        labelCol3: {
          xs: { span: 24 },
          sm: { span: 4 },
        },
        wrapperCol3: {
          xs: { span: 24 },
          sm: { span: 20 },
        },
        confirmLoading: false,
        validatorRules: {
        },
        url: {
          add: "/hntspjd/shigongphbTappraisal/add",
          edit: "/hntspjd/shigongphbTappraisal/edit",
          queryById: "/hntspjd/shigongphbTappraisal/queryById",
          list:"/system/shigongphb/list1",
          list1:"/system/shigongphb/list"
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
      const token = Vue.ls.get(ACCESS_TOKEN);
      this.headers = {"X-Access-Token":token};
    },
    methods: {
      handleChange1({ file, fileList }) {
        if (file.status !== 'uploading') {
          this.fileList=file.response.message;
          this.model.filePath = this.fileList;
          //console.log(this.model.filePath,"this.model.filePath")
        }
        console.log(this.fileList)
      },
      add () {
        this.edit({});
      },
      edit (record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'station','code','tag','betlev','filter','freeze','season','lands','mixlast','scale','otherreq','exper','assoss','dattim','flag','note','m1','u1','m2','u2','m3','u3','m4','u4','m5','u5','m6','u6','m7','u7','m8','u8','m9','u9','m10','u10','m11','u11','m12','u12','m13','u13','m14','u14','m15','u15','m16','u16','m17','u17','m18','u18','m19','u19','m20','u20','m21','u21','m22','u22','m23','u23','m24','u24','m25','u25','m26','u26','m27','u27','m28','u28','m29','u29','m30','u30','m31','u31','m32','u32','shebeibianhao','sysOrgCode','createBy','code1','projname','conspos','pour','mc4','mc5','mc6','mc7','mc8','mc9','mc10','ru4','ru5','ru6','ru7','ru8','ru9','ru10','identificationno','identificationtime','jdsjlands','jdsclands1','jdsclands2','jdsclands3','jdsjmc','jdscmc1','jdscmc2','jdscmc3','jdsjcjwd','jdsccjwd1','jdsccjwd2','jdsccjwd3','jdsjmsl1','jdsjmsl2','jdsjmsl3','ismatch','identificationresult','filePath','shiyanyuan','technicalDirector','supervisionEngineer'))
          this.peibiList();
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
      peibiList:function(){ //配合比号（任务单编号）
        let params = {};
        getAction(this.url.list, params).then((res)=>{
          console.log(res)
          if(res.success)  {
            let list=res.result;
            this.dictOptions=[];
            list.forEach(re=>{
              this.dictOptions.push({key:re.code,label:re.code})
            })
            // console.log(this.dictOptions,"ccccccccccccccccccccccccccc")
          }
        });
      },
      handleChange(dictOptions){//选择任务单编号显示工程名称及施工部位等信息
        this.model.code = dictOptions;
        console.log(dictOptions);
        let params = {code:dictOptions};
        getAction(this.url.list1, params).then((res)=>{
          console.log(res)
          if(res.success)  {
            let list = res.result.records;
            this.model.lands = list[0].lands;//坍落度
            this.model.projname = list[0].projname;//工程名称
            this.model.conspos = list[0].conspos;//施工部位
            this.model.shebeibianhao = list[0].shebeibianhao;//设备编号
            this.model.betlev = list[0].betlev;//强度等级
            this.model.m1 = list[0].m1;//原材料1
            this.model.u1 = list[0].u1;//用量1
            this.model.m2 = list[0].m2;//原材料2
            this.model.u2 = list[0].u2;//用量2
            this.model.m3 = list[0].m3;//原材料3
            this.model.u3 = list[0].u3;//用量3
            this.model.m4 = list[0].m4;//原材料4
            this.model.u4 = list[0].u4;//用量4
            this.model.mc4 = list[0].mc4;//含水率4
            this.model.ru4 = list[0].ru4;//实际用量4
            this.model.m5 = list[0].m5;//原材料5
            this.model.u5 = list[0].u5;//用量5
            this.model.mc5 = list[0].mc5;//含水率5
            this.model.ru5 = list[0].ru5;//实际用量5
            this.model.m6 = list[0].m6;//原材料6
            this.model.u6 = list[0].u6;//用量6
            this.model.mc6 = list[0].mc6;//含水率6
            this.model.ru6 = list[0].ru6;//实际用量6
            this.model.m7 = list[0].m7;//原材料7
            this.model.u7 = list[0].u7;//用量7
            this.model.mc7 = list[0].mc7;//含水率7
            this.model.ru7 = list[0].ru7;//实际用量7
            this.model.m8 = list[0].m8;//原材料8
            this.model.u8 = list[0].u8;//用量8
            this.model.mc8 = list[0].mc8;//含水率8
            this.model.ru8 = list[0].ru8;//实际用量8
            this.model.m9 = list[0].m9;//原材料9
            this.model.u9 = list[0].u9;//用量9
            this.model.mc9 = list[0].mc9;//含水率9
            this.model.ru9 = list[0].ru9;//实际用量9
            this.model.m10 = list[0].m10;//原材料10
            this.model.u10 = list[0].u10;//用量10
            this.model.mc10 = list[0].mc10;//含水率10
            this.model.ru10 = list[0].ru10;//实际用量10
            this.model.m11 = list[0].m11;//原材料11
            this.model.u11 = list[0].u11;//用量11
            this.model.m12 = list[0].m12;//原材料12
            this.model.u12 = list[0].u12;//用量12
            this.model.m13 = list[0].m13;//原材料13
            this.model.u13 = list[0].u13;//用量13
            this.form.setFieldsValue(pick(this.model,'betlev','lands','projname','conspos','shebeibianhao','m1','u1','m2','u2','m3','u3','m4','u4','m5','u5','m6','u6','m7','u7','m8','u8','m9','u9','m10','u10','m11','u11','m12','u12','m13','u13','mc4','mc5','mc6','mc7','mc8','mc9','mc10','ru4','ru5','ru6','ru7','ru8','ru9','ru10'))
          }
        });
      },
      popupCallback(row){
        this.form.setFieldsValue(pick(row,'station','code','tag','betlev','filter','freeze','season','lands','mixlast','scale','otherreq','exper','assoss','dattim','flag','note','m1','u1','m2','u2','m3','u3','m4','u4','m5','u5','m6','u6','m7','u7','m8','u8','m9','u9','m10','u10','m11','u11','m12','u12','m13','u13','m14','u14','m15','u15','m16','u16','m17','u17','m18','u18','m19','u19','m20','u20','m21','u21','m22','u22','m23','u23','m24','u24','m25','u25','m26','u26','m27','u27','m28','u28','m29','u29','m30','u30','m31','u31','m32','u32','shebeibianhao','sysOrgCode','createBy','code1','projname','conspos','pour','mc4','mc5','mc6','mc7','mc8','mc9','mc10','ru4','ru5','ru6','ru7','ru8','ru9','ru10','identificationno','identificationtime','jdsjlands','jdsclands1','jdsclands2','jdsclands3','jdsjmc','jdscmc1','jdscmc2','jdscmc3','jdsjcjwd','jdsccjwd1','jdsccjwd2','jdsccjwd3','jdsjmsl1','jdsjmsl2','jdsjmsl3','ismatch','identificationresult','filePath','shiyanyuan','technicalDirector','supervisionEngineer'))
      },
    }
  }
</script>