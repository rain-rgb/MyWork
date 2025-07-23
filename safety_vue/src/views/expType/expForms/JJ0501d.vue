<template>
  <div>
    <form id="form" action="" method="post" onsubmit="return false;">
      <div class="paperA4">
        <div class="tableHeader"></div>
        <div class="tableContent">
          <table class="layui-table">
            <tr>
              <td>工程名称</td>
              <td><textarea type="text" name="gongchengmingcheng" id="gongchengmingcheng"></textarea></td>
              <td>试验日期</td>
              <td><input type="text" name="shiyanriqi" id="shiyanriqi" BG=""/></td>
            </tr>
            <tr>
              <td>桩号及部位</td>
              <td><textarea id="zhuanghaojibuwei" name="zhuanghaojibuwei" BG=""></textarea></td>
              <td>仪器名称及型号</td>
              <td><textarea name="yiqishebei" id="yiqishebei"></textarea></td>
            </tr>
            <tr>
              <td>养护条件</td>
              <td><input type="text" id="sampleYangHuTiaoJian" name="sampleYangHuTiaoJian"></td>
              <td>设计强度等级</td>
              <td><input type="text" id="shejqddj1" name="shejqddj1" BG=""></td>
            </tr>
            <tr>
              <td>试验依据</td>
              <td colspan="3"><input type="text" name="shiyanyiju" id="shiyanyiju" value=""/></td>
            </tr>
          </table>
          <table class="layui-table next ">
            <tr>
              <td rowspan="2">试件编号</td>

              <td rowspan="2" width="12%">制件日期</td>

              <td rowspan="2" width="12%">试压日期</td>
              <td rowspan="2">期龄(d)</td>
              <td rowspan="2" style="width:100px">试件尺寸(mm)</td>
              <td rowspan="2" style="width:50px">破坏荷载(kN)</td>
              <td colspan="2">抗压强度(MPa)</td>
              <td rowspan="2">换算抗压强度（MPa）</td>
            </tr>
            <tr>
              <td style="width:50px">单值</td>
              <td style="width:50px">测定值</td>
            </tr>
            <tr>
              <td rowspan="5"><textarea BG="" id='shijbh' name='shijbh'></textarea></td>

              <td rowspan="5"><input type="text" name="zhijrq" BG="" id="zhijrq" onchange="syrq()"/></td>

              <td rowspan="5"><input type='text' id='shiyrq' name='shiyrq' readonly="readonly" BG=""/></td>
              <td rowspan="5"><input type='text' BG="" id='sampleLingQi' name='sampleLingQi' onchange="syrq()"/></td>
              <td rowspan="5"><input type='text' BG="" id='shijcc' name='shijcc'/></td>
              <td><input type='text' BG="" id='pohhz1' onchange="xiuyue(this.id,this.value,2);"
                         name='pohhz1'/></td>
              <td><input type='text' readonly="readonly" BG="" id='kangyqddz1' name='kangyqddz1'/></td>
              <td rowspan="5"><input type='text' readonly="readonly" BG="" id='kangyqdpingjz'
                                     name='kangyqdpingjz'/></td>
              <td rowspan="5"><input type='text' BG="" id='hskyqd1' name='hskyqd1'/></td>
            </tr>
            <tr>
              <td><input type='text' BG="" id='pohhz2' onchange="xiuyue(this.id,this.value,2);"
                         name='pohhz2'/></td>
              <td><input type='text' readonly="readonly" BG="" id='kangyqddz2' name='kangyqddz2'/></td>
            </tr>
            <tr>
              <td><input type='text' BG="" id='pohhz3' onchange="xiuyue(this.id,this.value,2);"
                         name='pohhz3'/></td>
              <td><input type='text' readonly="readonly" BG="" id='kangyqddz3' name='kangyqddz3'/></td>
            </tr>
            <tr>
              <td><input type='text' BG="" id='pohhz4' onchange="xiuyue(this.id,this.value,2);"
                         name='pohhz4'/></td>
              <td><input type='text' readonly="readonly" BG="" id='kangyqddz4' name='kangyqddz4'/></td>
            </tr>
            <tr>
              <td><input type='text' BG="" id='pohhz5' onchange="xiuyue(this.id,this.value,2);"
                         name='pohhz5'/></td>
              <td><input type='text' readonly="readonly" BG="" id='kangyqddz5' name='kangyqddz5'/></td>
            </tr>
            <tr>
              <td rowspan="5"><textarea BG="" id='shijbh2' name='shijbh2' onchange="number(2);"></textarea></td>

              <td rowspan="5"><input type="text" BG="" name="zhijrq2" id="zhijrq2" onchange="syrq()"/></td>

              <td rowspan="5"><input type='text' id='shiyrq2' name='shiyrq2' readonly="readonly" BG=""/></td>
              <td rowspan="5"><input type='text' BG="" id='lingq2' name='lingq2' onchange="syrq()"/></td>
              <td rowspan="5"><input type='text' BG="" id='shijcc2' name='shijcc2'/></td>
              <td><input type='text' BG="" id='pohhzer1' onchange="xiuyue(this.id,this.value,2);"
                         name='pohhzer1'/></td>
              <td><input type='text' readonly="readonly" BG="" id='kangyqddzer1' name='kangyqddzer1'/></td>
              <td rowspan="5"><input type='text' readonly="readonly" BG="" id='kangyqdpingjzer'
                                     name='kangyqdpingjzer'/></td>
              <td rowspan="5"><input type='text' BG="" id='hskyqd2' name='hskyqd2'/></td>
            </tr>
            <tr>
              <td><input type='text' BG="" id='pohhzer2' onchange="xiuyue(this.id,this.value,2);"
                         name='pohhzer2'/></td>
              <td><input type='text' readonly="readonly" BG="" id='kangyqddzer2' name='kangyqddzer2'/></td>
            </tr>
            <tr>
              <td><input type='text' BG="" id='pohhzer3' onchange="xiuyue(this.id,this.value,2);"
                         name='pohhzer3'/></td>
              <td><input type='text' readonly="readonly" BG="" id='kangyqddzer3' name='kangyqddzer3'/></td>
            </tr>
            <tr>
              <td><input type='text' BG="" id='pohhzer4' onchange="xiuyue(this.id,this.value,2);"
                         name='pohhzer4'/></td>
              <td><input type='text' readonly="readonly" BG="" id='kangyqddzer4' name='kangyqddzer4'/></td>
            </tr>
            <tr>
              <td><input type='text' BG="" id='pohhzer5' onchange="xiuyue(this.id,this.value,2);"
                         name='pohhzer5'/></td>
              <td><input type='text' readonly="readonly" BG="" id='kangyqddzer5' name='kangyqddzer5'/></td>
            </tr>
          </table>
        </div>
        <div class="tableRemark"></div>
      </div>
    </form>
  </div>

</template>

<script>
import {getAction, postAction} from "@api/manage";

export default {
  name: 'JJ0501d',
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
