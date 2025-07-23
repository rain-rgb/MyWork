<template>
  <div>
    <form id="form" action="" method="post" onsubmit="return false;">
      <div class="tableHeader"></div>
      <div class="tableContent">
        <table class="layui-table">
          <tr>
            <td width="12%">工程名称</td>
            <td width="50%"><textarea id="gongchengmingcheng" name="gongchengmingcheng" BG=""></textarea></td>
            <td width="12%">试验日期</td>
            <td><input type="text" id="shiyanriqi" name="shiyanriqi"/></td>
          </tr>
          <tr>
            <td>桩号及部位</td>
            <td><textarea id="zhuanghaojibuwei" name="zhuanghaojibuwei" BG=""></textarea></td>
            <td>仪器名称及型号</td>
            <td><textarea name="yiqishebei" id="yiqishebei" BG=""></textarea></td>
          </tr>
          <tr>
            <td height="46px">养护条件</td>
            <td><input type="text" id="sampleYangHuTiaoJian" name="sampleYangHuTiaoJian"></td>
            <td>制件说明</td>
            <td><input type="text" id="zhijianshuoming" name="zhijianshuoming" BG=""></td>
          </tr>
          <tr>
            <td>试验依据</td>
            <td colspan="3"><input type="text" name="shiyanyiju" id="shiyanyiju" value=""/></td>
          </tr>
        </table>
        <table class="layui-table next">
          <tr>
            <td rowspan="2" style="width: 40px;">试件编号</td>
            <td rowspan="2">制件日期</td>
            <td rowspan="2">试压日期</td>
            <td rowspan="2" style="width: 35px;">龄期(d)</td>
            <td rowspan="2">试件尺寸(mm)</td>
            <td rowspan="2">外观描述</td>
            <td rowspan="2">破坏荷载(kN)</td>
            <td colspan="2">抗压强度(MPa)</td>
            <td rowspan="2">换算强度(MPa)</td>
            <td rowspan="2">设计强度等级</td>
          </tr>
          <tr>
            <td>单值</td>
            <td>测定值</td>
          </tr>
          <tr>
            <td rowspan="3"><textarea name="shijbh1" id="shijbh1" BG=""></textarea></td>
            <td rowspan="3"><input type="text" class="date" name="zhijrq1" id="zhijrq1" BG=""/></td>
            <td rowspan="3"><input type="text" name="shiyrq1" id="shiyrq1" readonly BG=""/></td>
            <td rowspan="3"><input type="text" name="lingq1" id="lingq1" BG="" onblur="js()"/></td>
            <td rowspan="3">
              <select name="shijcc1" id="shijcc1" BG="" onchange="js()" class="noprint">
                <option value=""></option>
                <option value="100*100*100">100*100*100</option>
                <option value="150*150*150">150*150*150</option>
                <option value="200*200*200">200*200*200</option>
                <option value="/" hidden>/</option>
              </select>
            </td>
            <td rowspan="3"><textarea name="jiegbw1" id="jiegbw1" BG=""></textarea></td>
            <td><input type="text" name="pohhz11" id="pohhz11" BG="" onblur="js()"/></td>
            <td><input type="text" name="kangyqddz11" id="kangyqddz11" BG="" readonly/></td>
            <td rowspan="3"><input type="text" name="kangyqdpingjz1" id="kangyqdpingjz1" BG="" readonly/></td>
            <td rowspan="3"><input type="text" name="huansqd1" id="huansqd1" BG="" readonly/></td>
            <td rowspan="3"><input type="text" name="sjqddj1" id="sjqddj1"/></td>
          </tr>
          <tr>
            <td><input type="text" name="pohhz12" id="pohhz12" BG="" onblur="js()"/></td>
            <td><input type="text" name="kangyqddz12" id="kangyqddz12" BG="" readonly/></td>
          </tr>
          <tr>
            <td><input type="text" name="pohhz13" id="pohhz13" BG="" onblur="js()"/></td>
            <td><input type="text" name="kangyqddz13" id="kangyqddz13" BG="" readonly/></td>
          </tr>
          <tr>
            <td class="setH" rowspan="3"><textarea name="shijbh2" id="shijbh2" BG=""></textarea></td>
            <td rowspan="3"><input type="text" class="date" name="zhijrq2" id="zhijrq2" BG=""/></td>
            <td rowspan="3"><input type="text" name="shiyrq2" id="shiyrq2" BG="" readonly/></td>
            <td rowspan="3"><input type="text" name="lingq2" id="lingq2" BG="" onblur="js()"/></td>
            <td rowspan="3">
              <select name="shijcc2" id="shijcc2" BG="" onchange="js()">
                <option value=""></option>
                <option value="100*100*100">100*100*100</option>
                <option value="150*150*150">150*150*150</option>
                <option value="200*200*200">200*200*200</option>
                <option value="/" hidden>/</option>
              </select>
            </td>
            <td rowspan="3"><textarea name="jiegbw2" id="jiegbw2" BG=""></textarea></td>
            <td><input type="text" name="pohhz21" id="pohhz21" BG="" onblur="js()"/></td>
            <td><input type="text" name="kangyqddz21" id="kangyqddz21" BG="" readonly/></td>
            <td rowspan="3"><input type="text" name="kangyqdpingjz2" id="kangyqdpingjz2" BG="" readonly/></td>
            <td rowspan="3"><input type="text" name="huansqd2" id="huansqd2" BG="" readonly/></td>
            <td rowspan="3"><input type="text" name="sjqddj2" id="sjqddj2"/></td>
          </tr>
          <tr>
            <td><input type="text" name="pohhz22" id="pohhz22" BG="" onblur="js()"/></td>
            <td><input type="text" name="kangyqddz22" id="kangyqddz22" BG="" readonly/></td>
          </tr>
          <tr>
            <td><input type="text" name="pohhz23" id="pohhz23" BG="" onblur="js()"/></td>
            <td><input type="text" name="kangyqddz23" id="kangyqddz23" BG="" readonly/></td>
          </tr>
          <tr>
            <td class="setH" rowspan="3"><textarea name="shijbh3" id="shijbh3" BG=""></textarea></td>
            <td rowspan="3"><input type="text" class="date" name="zhijrq3" id="zhijrq3" BG=""/></td>
            <td rowspan="3"><input type="text" name="shiyrq3" id="shiyrq3" BG="" readonly/></td>
            <td rowspan="3"><input type="text" name="lingq3" id="lingq3" BG="" onblur="js()"/></td>
            <td rowspan="3">
              <select name="shijcc3" id="shijcc3" BG="" onchange="js()">
                <option value=""></option>
                <option value="100*100*100">100*100*100</option>
                <option value="150*150*150">150*150*150</option>
                <option value="200*200*200">200*200*200</option>
                <option value="/" hidden>/</option>
              </select>
            </td>
            <td rowspan="3"><textarea name="jiegbw3" id="jiegbw3" BG=""></textarea></td>
            <td><input type="text" name="pohhz31" id="pohhz31" BG="" onblur="js()"/></td>
            <td><input type="text" name="kangyqddz31" id="kangyqddz31" BG="" readonly/></td>
            <td rowspan="3"><input type="text" name="kangyqdpingjz3" id="kangyqdpingjz3" BG="" readonly/></td>
            <td rowspan="3"><input type="text" name="huansqd3" id="huansqd3" BG="" readonly/></td>
            <td rowspan="3"><input type="text" name="sjqddj3" id="sjqddj3"/></td>
          </tr>
          <tr>
            <td><input type="text" name="pohhz32" id="pohhz32" BG="" onblur="js()"/></td>
            <td><input type="text" name="kangyqddz32" id="kangyqddz32" BG="" readonly/></td>
          </tr>
          <tr>
            <td><input type="text" name="pohhz33" id="pohhz33" BG="" onblur="js()"/></td>
            <td><input type="text" name="kangyqddz33" id="kangyqddz33" BG="" readonly/></td>
          </tr>
          <tr>
            <td class="setH" rowspan="3"><textarea name="shijbh4" id="shijbh4" BG=""></textarea></td>
            <td rowspan="3"><input type="text" class="date" name="zhijrq4" id="zhijrq4" BG=""/></td>
            <td rowspan="3"><input type="text" name="shiyrq4" id="shiyrq4" BG="" readonly/></td>
            <td rowspan="3"><input type="text" name="lingq4" id="lingq4" BG="" onblur="js()"/></td>
            <td rowspan="3">
              <select name="shijcc4" id="shijcc4" BG="" onchange="js()">
                <option value=""></option>
                <option value="100*100*100">100*100*100</option>
                <option value="150*150*150">150*150*150</option>
                <option value="200*200*200">200*200*200</option>
                <option value="/" hidden>/</option>
              </select>
            </td>
            <td rowspan="3"><textarea name="jiegbw4" id="jiegbw4" BG=""></textarea></td>
            <td><input type="text" name="pohhz41" id="pohhz41" BG="" onblur="js()"/></td>
            <td><input type="text" name="kangyqddz41" id="kangyqddz41" BG="" readonly/></td>
            <td rowspan="3"><input type="text" name="kangyqdpingjz4" id="kangyqdpingjz4" BG="" readonly/></td>
            <td rowspan="3"><input type="text" name="huansqd4" id="huansqd4" BG="" readonly/></td>
            <td rowspan="3"><input type="text" name="sjqddj4" id="sjqddj4"/></td>
          </tr>
          <tr>
            <td><input type="text" name="pohhz42" id="pohhz42" BG="" onblur="js()"/></td>
            <td><input type="text" name="kangyqddz42" id="kangyqddz42" BG="" readonly/></td>
          </tr>
          <tr>
            <td><input type="text" name="pohhz43" id="pohhz43" BG="" onblur="js()"/></td>
            <td><input type="text" name="kangyqddz43" id="kangyqddz43" BG="" readonly/></td>
          </tr>
          <tr>
            <td class="setH" rowspan="3"><textarea name="shijbh5" id="shijbh5" BG=""></textarea></td>
            <td rowspan="3"><input type="text" class="date" name="zhijrq5" id="zhijrq5" BG=""/></td>
            <td rowspan="3"><input type="text" name="shiyrq5" id="shiyrq5" BG="" readonly/></td>
            <td rowspan="3"><input type="text" name="lingq5" id="lingq5" BG="" onblur="js()"/></td>
            <td rowspan="3">
              <select name="shijcc5" id="shijcc5" BG="" onchange="js()">
                <option value=""></option>
                <option value="100*100*100">100*100*100</option>
                <option value="150*150*150">150*150*150</option>
                <option value="200*200*200">200*200*200</option>
                <option value="/" hidden>/</option>
              </select>
            </td>
            <td rowspan="3"><textarea name="jiegbw5" id="jiegbw5" BG=""></textarea></td>
            <td><input type="text" name="pohhz51" id="pohhz51" BG="" onblur="js()"/></td>
            <td><input type="text" name="kangyqddz51" id="kangyqddz51" BG="" readonly/></td>
            <td rowspan="3"><input type="text" name="kangyqdpingjz5" id="kangyqdpingjz5" BG="" readonly/></td>
            <td rowspan="3"><input type="text" name="huansqd5" id="huansqd5" BG="" readonly/></td>
            <td rowspan="3"><input type="text" name="sjqddj5" id="sjqddj5"/></td>
          </tr>
          <tr>
            <td><input type="text" name="pohhz52" id="pohhz52" BG="" onblur="js()"/></td>
            <td><input type="text" name="kangyqddz52" id="kangyqddz52" BG="" readonly/></td>
          </tr>
          <tr>
            <td><input type="text" name="pohhz53" id="pohhz53" BG="" onblur="js()"/></td>
            <td><input type="text" name="kangyqddz53" id="kangyqddz53" BG="" readonly/></td>
          </tr>
          <tr style="display: none">
            <td class="setH" rowspan="3"><textarea name="shijbh6" id="shijbh6" BG=""></textarea></td>
            <td rowspan="3"><textarea name="jiegbw6" id="jiegbw6" BG=""></textarea></td>
            <td rowspan="3"><input type="text" class="date" name="zhijrq6" id="zhijrq6" BG=""/></td>
            <td rowspan="3"><input type="text" name="shiyrq6" id="shiyrq6" BG="" readonly/></td>
            <td rowspan="3"><input type="text" name="lingq6" id="lingq6" BG="" onblur="js()"/></td>
            <td rowspan="3">
              <select name="shijcc6" id="shijcc6" BG="" onchange="js()">
                <option value=""></option>
                <option value="100*100*100">100*100*100</option>
                <option value="150*150*150">150*150*150</option>
                <option value="200*200*200">200*200*200</option>
                <option value="/" hidden>/</option>
              </select>
            </td>
            <td><input type="text" name="pohhz61" id="pohhz61" BG="" onblur="js()"/></td>
            <td><input type="text" name="kangyqddz61" id="kangyqddz61" BG="" readonly/></td>
            <td rowspan="3"><input type="text" name="kangyqdpingjz6" id="kangyqdpingjz6" BG="" readonly/></td>
            <td rowspan="3"><input type="text" name="huansqd6" id="huansqd6" BG="" readonly/></td>
          </tr>
          <tr style="display: none">
            <td><input type="text" name="pohhz62" id="pohhz62" BG="" onblur="js()"/></td>
            <td><input type="text" name="kangyqddz62" id="kangyqddz62" BG="" readonly/></td>
          </tr>
          <tr style="display: none">
            <td><input type="text" name="pohhz63" id="pohhz63" BG="" onblur="js()"/></td>
            <td><input type="text" name="kangyqddz63" id="kangyqddz63" BG="" readonly/></td>
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
  name: 'JJ0501a',
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
