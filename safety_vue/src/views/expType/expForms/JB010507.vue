<template>
  <div>
    <form id="form" action="" method="post" onsubmit="return false;">
      <div class="tableHeader"></div>
      <div class="tableContent">
        <table class="layui-table">
          <tr>
            <td width="12%">工程名称</td>
            <td width="50%"><textarea type="text" id="gongchengmingcheng" name="gongchengmingcheng"></textarea>
            </td>
            <td width="12%">试验日期</td>
            <td><input type="text" id="shiyanriqi" name="shiyanriqi"/></td>
          </tr>
          <tr>
            <td>桩号及部位</td>
            <td><textarea type="text" id="zhuanghaojibuwei" name="zhuanghaojibuwei"></textarea></td>
            <td>仪器名称及编号</td>
            <td><textarea name="yiqishebei" id="yiqishebei"></textarea></td>
          </tr>
          <tr style="height: 30px">
            <td>外观描述</td>
            <td colspan="3"><input type="text" id="sampleDescribe" name="sampleDescribe"></td>
          </tr>
        </table>
        <table class="layui-table next">
          <tr>
            <td height="46px">养护条件</td>
            <td><input type="text" id="sampleYangHuTiaoJian" name="sampleYangHuTiaoJian"></td>
            <td>制件说明</td>
            <td colspan="3"><input type="text" id="zhijianshuoming" name="zhijianshuoming"></td>
            <td colspan="2">设计强度等级</td>
            <td colspan="2"><input type="text" id="sampleQiangDuDengJi" name="sampleQiangDuDengJi"></td>
          </tr>
          <tr>
            <td rowspan="2">试件编号</td>
            <td rowspan="2">结构部位</td>
            <td rowspan="2">制件日期</td>
            <td rowspan="2">试验日期</td>
            <td rowspan="2">龄期(d)</td>
            <td rowspan="2" width="65px">试件尺寸(mm)</td>
            <td rowspan="2">破坏荷载(kN)</td>
            <td colspan="2">抗压强度(MPa)</td>
            <td rowspan="2">换算强度(MPa)</td>
          </tr>
          <tr>
            <td>单值</td>
            <td>测定值</td>
          </tr>
          <tr>
            <td class="setH" rowspan="3" style="width: 54px;"><textarea name="shijbh1" id="shijbh1"></textarea>
            </td>
            <td rowspan="3" style="width: 146px;"><textarea rows="3" name="jiegbw1" id="jiegbw1"></textarea>
            </td>
            <td rowspan="3" style="width: 66px;"><input type="text" class="date" name="zhijrq1" id="zhijrq1"/>
            </td>
            <td rowspan="3" style="width: 66px;"><input type="text" name="shiyrq1" id="shiyrq1" readonly/>
            </td>
            <td rowspan="3" style="width: 30px;"><input type="text" name="lingq1" id="lingq1" onblur="js()"/>
            </td>
            <td rowspan="3" style="">
              <input type="text" name="shijcc1" id="shijcc1" readonly/>
              <!--    <select name="shijcc1" id="shijcc1" onchange="js()">
                    <option value="0"></option>
                    <option value="1">100x100x100</option>
                    <option value="2">150x150x150</option>
                    <option value="3">200x200x200</option>
                    <option value="/" hidden>/</option>
                  </select>-->
            </td>
            <td style=""><input type="text" name="pohhz11" id="pohhz11" onblur="js()"/></td>
            <td style=""><input type="text" name="kangyqddz11" id="kangyqddz11" readonly/></td>
            <td rowspan="3" style=""><input type="text" name="kangyqdpingjz1" id="kangyqdpingjz1" readonly/>
            </td>
            <td rowspan="3" style=""><input type="text" name="huansqd1" id="huansqd1" readonly/></td>
          </tr>
          <tr>
            <td><input type="text" name="pohhz12" id="pohhz12" onblur="js()"/></td>
            <td><input type="text" name="kangyqddz12" id="kangyqddz12" readonly/></td>
          </tr>
          <tr>
            <td><input type="text" name="pohhz13" id="pohhz13" onblur="js()"/></td>
            <td><input type="text" name="kangyqddz13" id="kangyqddz13" readonly/></td>
          </tr>
          <tr>
            <td class="setH" rowspan="3"><textarea name="shijbh2" id="shijbh2"></textarea></td>
            <td rowspan="3"><textarea rows="3" name="jiegbw2" id="jiegbw2"></textarea></td>
            <td rowspan="3"><input type="text" class="date" name="zhijrq2" id="zhijrq2"/></td>
            <td rowspan="3"><input type="text" name="shiyrq2" id="shiyrq2" readonly/></td>
            <td rowspan="3"><input type="text" name="lingq2" id="lingq2" onblur="js()"/></td>
            <td rowspan="3">
              <input type="text" name="shijcc2" id="shijcc2" readonly/>

            </td>
            <td><input type="text" name="pohhz21" id="pohhz21" onblur="js()"/></td>
            <td><input type="text" name="kangyqddz21" id="kangyqddz21" readonly/></td>
            <td rowspan="3"><input type="text" name="kangyqdpingjz2" id="kangyqdpingjz2" readonly/></td>
            <td rowspan="3"><input type="text" name="huansqd2" id="huansqd2" readonly/></td>
          </tr>
          <tr>
            <td><input type="text" name="pohhz22" id="pohhz22" onblur="js()"/></td>
            <td><input type="text" name="kangyqddz22" id="kangyqddz22" readonly/></td>
          </tr>
          <tr>
            <td><input type="text" name="pohhz23" id="pohhz23" onblur="js()"/></td>
            <td><input type="text" name="kangyqddz23" id="kangyqddz23" readonly/></td>
          </tr>
          <tr>
            <td class="setH" rowspan="3"><textarea name="shijbh3" id="shijbh3"></textarea></td>
            <td rowspan="3"><textarea rows="3" name="jiegbw3" id="jiegbw3"></textarea></td>
            <td rowspan="3"><input type="text" class="date" name="zhijrq3" id="zhijrq3"/></td>
            <td rowspan="3"><input type="text" name="shiyrq3" id="shiyrq3" readonly/></td>
            <td rowspan="3"><input type="text" name="lingq3" id="lingq3" onblur="js()"/></td>
            <td rowspan="3">
              <input type="text" name="shijcc3" id="shijcc3" readonly/>

              <!-- <select name="shijcc3" id="shijcc3" onchange="js()">
                     <option value="0"></option>
                     <option value="1">100x100x100</option>
                     <option value="2">150x150x150</option>
                     <option value="3">200x200x200</option>
                     <option value="/" hidden>/</option>
                 </select>-->
            </td>
            <td><input type="text" name="pohhz31" id="pohhz31" onblur="js()"/></td>
            <td><input type="text" name="kangyqddz31" id="kangyqddz31" readonly/></td>
            <td rowspan="3"><input type="text" name="kangyqdpingjz3" id="kangyqdpingjz3" readonly/></td>
            <td rowspan="3"><input type="text" name="huansqd3" id="huansqd3" readonly/></td>
          </tr>
          <tr>
            <td><input type="text" name="pohhz32" id="pohhz32" onblur="js()"/></td>
            <td><input type="text" name="kangyqddz32" id="kangyqddz32" readonly/></td>
          </tr>
          <tr>
            <td><input type="text" name="pohhz33" id="pohhz33" onblur="js()"/></td>
            <td><input type="text" name="kangyqddz33" id="kangyqddz33" readonly/></td>
          </tr>
          <tr>
            <td class="setH" rowspan="3"><textarea name="shijbh4" id="shijbh4"></textarea></td>
            <td rowspan="3"><textarea rows="3" name="jiegbw4" id="jiegbw4"></textarea></td>
            <td rowspan="3"><input type="text" class="date" name="zhijrq4" id="zhijrq4"/></td>
            <td rowspan="3"><input type="text" name="shiyrq4" id="shiyrq4" readonly/></td>
            <td rowspan="3"><input type="text" name="lingq4" id="lingq4" onblur="js()"/></td>
            <td rowspan="3">
              <input type="text" name="shijcc4" id="shijcc4" readonly/>


              <!--     <select name="shijcc4" id="shijcc4" onchange="js()">
                         <option value="0"></option>
                         <option value="1">100x100x100</option>
                         <option value="2">150x150x150</option>
                         <option value="3">200x200x200</option>
                         <option value="/" hidden>/</option>
                     </select>
                 </td>-->
            <td><input type="text" name="pohhz41" id="pohhz41" onblur="js()"/></td>
            <td><input type="text" name="kangyqddz41" id="kangyqddz41" readonly/></td>
            <td rowspan="3"><input type="text" name="kangyqdpingjz4" id="kangyqdpingjz4" readonly/></td>
            <td rowspan="3"><input type="text" name="huansqd4" id="huansqd4" readonly/></td>
          </tr>
          <tr>
            <td><input type="text" name="pohhz42" id="pohhz42" onblur="js()"/></td>
            <td><input type="text" name="kangyqddz42" id="kangyqddz42" readonly/></td>
          </tr>
          <tr>
            <td><input type="text" name="pohhz43" id="pohhz43" onblur="js()"/></td>
            <td><input type="text" name="kangyqddz43" id="kangyqddz43" readonly/></td>
          </tr>
          <tr>
            <td class="setH" rowspan="3"><textarea name="shijbh5" id="shijbh5"></textarea></td>
            <td rowspan="3"><textarea rows="3" name="jiegbw5" id="jiegbw5"></textarea></td>
            <td rowspan="3"><input type="text" class="date" name="zhijrq5" id="zhijrq5"/></td>
            <td rowspan="3"><input type="text" name="shiyrq5" id="shiyrq5" readonly/></td>
            <td rowspan="3"><input type="text" name="lingq5" id="lingq5" onblur="js()"/></td>
            <td rowspan="3">
              <input type="text" name="shijcc5" id="shijcc5" readonly/>

              <!--   <select name="shijcc5" id="shijcc5" onchange="js()">
                       <option value="0"></option>
                       <option value="1">100x100x100</option>
                       <option value="2">150x150x150</option>
                       <option value="3">200x200x200</option>
                       <option value="/" hidden>/</option>
                   </select>
               </td>-->
            <td><input type="text" name="pohhz51" id="pohhz51" onblur="js()"/></td>
            <td><input type="text" name="kangyqddz51" id="kangyqddz51" readonly/></td>
            <td rowspan="3"><input type="text" name="kangyqdpingjz5" id="kangyqdpingjz5" readonly/></td>
            <td rowspan="3"><input type="text" name="huansqd5" id="huansqd5" readonly/></td>
          </tr>
          <tr>
            <td><input type="text" name="pohhz52" id="pohhz52" onblur="js()"/></td>
            <td><input type="text" name="kangyqddz52" id="kangyqddz52" readonly/></td>
          </tr>
          <tr>
            <td><input type="text" name="pohhz53" id="pohhz53" onblur="js()"/></td>
            <td><input type="text" name="kangyqddz53" id="kangyqddz53" readonly/></td>
          </tr>
          <tr style="display: none">
            <td class="setH" rowspan="3"><textarea name="shijbh6" id="shijbh6"></textarea></td>
            <td rowspan="3"><textarea rows="3" name="jiegbw6" id="jiegbw6"></textarea></td>
            <td rowspan="3"><input type="text" class="date" name="zhijrq6" id="zhijrq6"/></td>
            <td rowspan="3"><input type="text" name="shiyrq6" id="shiyrq6" readonly/></td>
            <td rowspan="3"><input type="text" name="lingq6" id="lingq6" onblur="js()"/></td>
            <td rowspan="3">
              <input type="text" name="shijcc6" id="shijcc6" readonly/>
              <!--
                                    <select name="shijcc6" id="shijcc6" onchange="js()">
                                          <option value="0"></option>
                                          <option value="1">100x100x100</option>
                                          <option value="2">150x150x150</option>
                                          <option value="3">200x200x200</option>
                                          <option value="/" hidden>/</option>
                                      </select>-->
            </td>
            <td><input type="text" name="pohhz61" id="pohhz61" onblur="js()"/></td>
            <td><input type="text" name="kangyqddz61" id="kangyqddz61" readonly/></td>
            <td rowspan="3"><input type="text" name="kangyqdpingjz6" id="kangyqdpingjz6" readonly/></td>
            <td rowspan="3"><input type="text" name="huansqd6" id="huansqd6" readonly/></td>
          </tr>
          <tr style="display: none">
            <td><input type="text" name="pohhz62" id="pohhz62" onblur="js()"/></td>
            <td><input type="text" name="kangyqddz62" id="kangyqddz62" readonly/></td>
          </tr>
          <tr style="display: none">
            <td><input type="text" name="pohhz63" id="pohhz63" onblur="js()"/></td>
            <td><input type="text" name="kangyqddz63" id="kangyqddz63" readonly/></td>
          </tr>
        </table>

      </div>
      <div class="tableRemark"></div>
    </form>
  </div>

</template>

<script>
import {getAction, postAction} from "@api/manage";

export default {
  name: 'JB010507',
  data() {
    return {
    }
  },
  methods: {
  }
};
</script>
<style scoped>
</style>
