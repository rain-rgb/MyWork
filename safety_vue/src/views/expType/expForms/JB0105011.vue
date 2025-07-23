<template>
  <div>
    <form id="form" action="" method="post" onsubmit="return false;">
      <div class="tableHeader"></div>
      <div class="tableContent">
        <table class="layui-table">
          <tr>
            <td>工程名称</td>
            <td><textarea id="gongchengmingcheng" name="gongchengmingcheng"></textarea></td>
            <td>试验日期</td>
            <td><input type="text" id="shiyanriqi" name="shiyanriqi"></td>
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
            <td><input type="text" id="sampleQiangDuDengJi" name="sampleQiangDuDengJi" BG=""></td>
          </tr>
        </table>
        <table class="layui-table next">
          <tr>
            <td rowspan="2">试件编号</td>
            <td rowspan="2" style="width:80px">结构部位</td>
            <td rowspan="2">制件日期</td>
            <td rowspan="2">期龄(d)</td>
            <td rowspan="2">试验日期</td>
            <td rowspan="2" style="width:100px">试件尺寸(mm)</td>
            <td rowspan="2">破坏荷载(kN)</td>
            <td colspan="2">抗压强度(MPa)</td>
          </tr>
          <tr>
            <td>单值</td>
            <td>测定值</td>
          </tr>
          <tr>
            <td rowspan="6"><textarea readonly="readonly" id='shijbh' name='shijbh' enterIndex='4'></textarea>
            </td>
            <td rowspan="6"><textarea id="jiegbw" name="jiegbw" readonly="readonly"></textarea></td>
            <td rowspan="6"><input type="text" name="zhijrq" readonly="readonly" id="zhijrq" enterIndex='6'/>
            </td>
            <td rowspan="6"><input type='text' readonly="readonly" id='lingq' name='lingq' enterIndex='7'/></td>
            <td rowspan="6"><input type='text' readonly="readonly" id='shiyrq' name='shiyrq'/></td>
            <td rowspan="6"><input type='text' readonly="readonly" id='shijcc' name='shijcc' enterIndex='8'/>
            </td>
            <td><input type='text' readonly="readonly" id='pohhz1' enterIndex='9' name='pohhz1'/></td>
            <td><input type='text' readonly="readonly" id='kangyqddz1' name='kangyqddz1'/>
            </td>
            <td rowspan="6"><input type='text' readonly="readonly" id='kangyqdpingjz' name='kangyqdpingjz'/></td>
          </tr>
          <tr>
            <td><input type='text' readonly="readonly" id='pohhz2' enterIndex='10' name='pohhz2'/></td>
            <td><input type='text' readonly="readonly" id='kangyqddz2' name='kangyqddz2'/></td>
          </tr>
          <tr>
            <td><input type='text' readonly="readonly" id='pohhz3' enterIndex='11' name='pohhz3'/></td>
            <td><input type='text' readonly="readonly" id='kangyqddz3' name='kangyqddz3'/></td>
          </tr>
          <tr>
            <td><input type='text' readonly="readonly" id='pohhz4' enterIndex='12' name='pohhz4'/></td>
            <td><input type='text' readonly="readonly" id='kangyqddz4' name='kangyqddz4'/></td>
          </tr>
          <tr>
            <td><input type='text' readonly="readonly" id='pohhz5' enterIndex='13' name='pohhz5'/></td>
            <td><input type='text' readonly="readonly" id='kangyqddz5' name='kangyqddz5'/></td>
          </tr>
          <tr>
            <td><input type='text' readonly="readonly" id='pohhz6' enterIndex='14' name='pohhz6'/></td>
            <td><input type='text' readonly="readonly" id='kangyqddz6' name='kangyqddz6'/></td>
          </tr>
          <tr>
            <td rowspan="6"><textarea readonly="readonly" id='shijbh2' name='shijbh2'
                                      enterIndex='15'></textarea>
            </td>
            <td rowspan="6"><textarea id="jiegbw2" name="jiegbw2" readonly="readonly"></textarea>
            </td>
            <td rowspan="6"><input type='text' readonly="readonly" id='zhijrq2' name='zhijrq2' enterIndex='17'/>
            </td>
            <td rowspan="6"><input type='text' readonly="readonly" id='lingq2' name='lingq2' enterIndex='18'/>
            </td>
            <td rowspan="6"><input type='text' readonly="readonly" id='shiyrq2' name='shiyrq2'/></td>
            <td rowspan="6"><input type='text' readonly="readonly" id='shijcc2' name='shijcc2' enterIndex='19'/>
            </td>
            <td><input type='text' readonly="readonly" id='pohhzer1' enterIndex='20' name='pohhzer1'/></td>
            <td><input type='text' readonly="readonly" id='kangyqddzer1' name='kangyqddzer1'/></td>
            <td rowspan="6"><input type='text' readonly="readonly" id='kangyqdpingjz2' name='kangyqdpingjz2'/>
            </td>
          </tr>
          <tr>
            <td><input type='text' readonly="readonly" id='pohhzer2' enterIndex='21' name='pohhzer2'/></td>
            <td><input type='text' readonly="readonly" id='kangyqddzer2' name='kangyqddzer2'/></td>
          </tr>
          <tr>
            <td><input type='text' readonly="readonly" id='pohhzer3' enterIndex='22' name='pohhzer3'/></td>
            <td><input type='text' readonly="readonly" id='kangyqddzer3' name='kangyqddzer3'/></td>
          </tr>
          <tr>
            <td><input type='text' readonly="readonly" id='pohhzer4' enterIndex='23' name='pohhzer4'/></td>
            <td><input type='text' readonly="readonly" id='kangyqddzer4' name='kangyqddzer4'/></td>
          </tr>
          <tr>
            <td><input type='text' readonly="readonly" id='pohhzer5' enterIndex='24' name='pohhzer5'/></td>
            <td><input type='text' readonly="readonly" id='kangyqddzer5' name='kangyqddzer5'/></td>
          </tr>
          <tr>
            <td><input type='text' readonly="readonly" id='pohhzer6' enterIndex='25' name='pohhzer6'/></td>
            <td><input type='text' readonly="readonly" id='kangyqddzer6' name='kangyqddzer6'/></td>
          </tr>
          <tr>
            <td rowspan="6"><textarea readonly="readonly" id='shijbh3' name='shijbh3'
                                      enterIndex='26'></textarea>
            </td>
            <td rowspan="6"><textarea id="jiegbw3" name="jiegbw3" readonly="readonly"></textarea>
            </td>
            <td rowspan="6"><input type='text' readonly="readonly" id='zhijrq3' name='zhijrq3' enterIndex='28'/>
            </td>
            <td rowspan="6"><input type='text' readonly="readonly" id='lingq3' name='lingq3' enterIndex='29'/>
            </td>
            <td rowspan="6"><input type='text' readonly="readonly" id='shiyrq3' name='shiyrq3'/></td>
            <td rowspan="6"><input type='text' readonly="readonly" id='shijcc3' name='shijcc3' enterIndex='30'/>
            </td>
            <td><input type='text' readonly="readonly" id='pohhzsan1' enterIndex='31' name='pohhzsan1'/></td>
            <td><input type='text' readonly="readonly" id='kangyqddzsan1' name='kangyqddzsan1'/></td>
            <td rowspan="6"><input type='text' readonly="readonly" id='kangyqdpingjz3' name='kangyqdpingjz3'/>
            </td>
          </tr>
          <tr>
            <td><input type='text' readonly="readonly" id='pohhzsan2' enterIndex='32' name='pohhzsan2'/></td>
            <td><input type='text' readonly="readonly" id='kangyqddzsan2' name='kangyqddzsan2'/></td>
          </tr>
          <tr>
            <td><input type='text' readonly="readonly" id='pohhzsan3' enterIndex='33' name='pohhzsan3'/></td>
            <td><input type='text' readonly="readonly" id='kangyqddzsan3' name='kangyqddzsan3'/></td>
          </tr>
          <tr>
            <td><input type='text' readonly="readonly" id='pohhzsan4' enterIndex='34' name='pohhzsan4'/></td>
            <td><input type='text' readonly="readonly" id='kangyqddzsan4' name='kangyqddzsan4'/></td>
          </tr>
          <tr>
            <td><input type='text' readonly="readonly" id='pohhzsan5' enterIndex='35' name='pohhzsan5'/></td>
            <td><input type='text' readonly="readonly" id='kangyqddzsan5' name='kangyqddzsan5'/></td>
          </tr>
          <tr>
            <td><input type='text' readonly="readonly" id='pohhzsan6' enterIndex='36' name='pohhzsan6'/></td>
            <td><input type='text' readonly="readonly" id='kangyqddzsan6' name='kangyqddzsan6'/></td>
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
  name: 'JB0105011',
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
